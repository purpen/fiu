package com.taihuoniao.fineix.personal.alliance;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.base.GlobalDataCallBack;
import com.taihuoniao.fineix.base.HttpRequest;
import com.taihuoniao.fineix.beans.HttpResponse;
import com.taihuoniao.fineix.main.MainActivity;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.URL;
import com.taihuoniao.fineix.personal.alliance.bean.MyAccountBean;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.StringFormatUtils;
import com.taihuoniao.fineix.utils.WindowUtils;
import com.taihuoniao.fineix.view.CustomHeadView;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Stephen on 2017/1/16 14:36
 * Email: 895745843@qq.com
 */

public class MyAccountActivity extends BaseActivity {

    @Bind(R.id.custom_head)
    CustomHeadView customHead;
    @Bind(R.id.textView1)
    TextView textView1;
    @Bind(R.id.textView2)
    TextView textView2;
    @Bind(R.id.textView3)
    TextView textView3;
    @Bind(R.id.linearLayout1)
    LinearLayout linearLayout1;
    @Bind(R.id.linearLayout2)
    LinearLayout linearLayout2;
    @Bind(R.id.linearLayout3)
    LinearLayout linearLayout3;
    @Bind(R.id.linearLayout4)
    LinearLayout linearLayout4;
    @Bind(R.id.textView_link1)
    TextView textViewLink1;
    @Bind(R.id.textView_link2)
    TextView textViewLink2;

    public MyAccountActivity() {
        super(R.layout.activity_alliance_my_account);
    }

    @Override
    protected void initView() {
        customHead.setHeadCenterTxtShow(true, "我的分成");
        WindowUtils.chenjin(this);
    }


    @Override
    protected void requestNet() {
        requestData();
    }

    private void requestData() {
        HashMap<String, String> hashMap = ClientDiscoverAPI.getallianceAccount();
        HttpRequest.post(hashMap, URL.ALLIANCE_ACCOUNT, new GlobalDataCallBack() {
            @Override
            public void onSuccess(String json) {
                HttpResponse<MyAccountBean> httpResponse = JsonUtil.json2Bean(json, new TypeToken<HttpResponse<MyAccountBean>>() {
                });
                if (httpResponse.isSuccess()) {
                    dealUI(httpResponse.getData());
                }
            }

            @Override
            public void onFailure(String error) {

            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.linearLayout1, R.id.linearLayout2, R.id.linearLayout3, R.id.linearLayout4, R.id.textView_link1, R.id.textView_link2})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.linearLayout1:
                Intent intent = new Intent(MyAccountActivity.this, WithdrawActivity.class);
                intent.putExtra("balance", myAccountBean.getWait_cash_amount());
                startActivity(intent);
                break;
            case R.id.linearLayout2:
                startActivity(new Intent(MyAccountActivity.this, TradeRecordActivity.class));
                break;
            case R.id.linearLayout3:
                startActivity(new Intent(MyAccountActivity.this, SettlementRecordActivity.class));
                break;
            case R.id.linearLayout4:
                startActivity(new Intent(MyAccountActivity.this, WithdrawRecordActivity.class));
                break;
            case R.id.textView_link1:
                startActivity(new Intent(this, PrivacyPolicyActivity.class));
                break;
            case R.id.textView_link2:
                break;
        }
    }

    private MyAccountBean myAccountBean;

    private void dealUI(MyAccountBean myAccountBean){
        if (myAccountBean == null) {
            return;
        }
        textView1.setText(StringFormatUtils.formatMoney(myAccountBean.getWait_cash_amount()));
        textView2.setText(StringFormatUtils.formatMoney(myAccountBean.getTotal_balance_amount()));
        textView3.setText(StringFormatUtils.formatMoney(myAccountBean.getTotal_cash_amount()));
        this.myAccountBean = myAccountBean;
    }

    @Override
    public void onBackPressed() {
        activity.startActivity(new Intent(activity, MainActivity.class));
        activity.finish();
    }
}
