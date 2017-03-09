package com.taihuoniao.fineix.personal.alliance;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.base.HttpRequest;
import com.taihuoniao.fineix.beans.HttpResponse;
import com.taihuoniao.fineix.common.GlobalDataCallBack;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.URL;
import com.taihuoniao.fineix.personal.AllianceRequstDeal;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.utils.WindowUtils;
import com.taihuoniao.fineix.view.CustomHeadView;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Stephen on 2017/3/9 10:00
 * Email: 895745843@qq.com
 */

public class WithdrawAllianceBankListActivity extends BaseActivity {

    @Bind(R.id.custom_head)
    CustomHeadView customHead;
    @Bind(R.id.listView_backlist)
    ListView listViewBacklist;

    public WithdrawAllianceBankListActivity() {
        super(R.layout.activity_alliance_bank_list);
    }

    @Override
    protected void initView() {
        customHead.setHeadCenterTxtShow(true, "提现");
        WindowUtils.chenjin(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        initUI();
    }

    private void initUI() {
//        balance = getIntent().getStringExtra("balance");
//        textView2.setText(String.format("¥ %s", balance));
    }

    @OnClick({R.id.textView_link1, R.id.button_commit})
    public void onClick(View view) {
        switch (view.getId()) {
//            case R.id.textView_link1:
//                amount = balance;
//                editText1.setText(amount);
//                editText1.setSelection(editText1.length());
//                break;
//            case R.id.button_commit:
//                new DefaultDialog(this, String.format("¥ %s", amount), "请确认要提现的金额", "提现", new IDialogListenerConfirmBack() {
//                    @Override
//                    public void clickRight() {
//                        requestData(amount);
//                    }
//                });
//                break;
        }
    }

    private void requestData(String amount) {
        String allianceValue = AllianceRequstDeal.getAllianceValue();
        HashMap<String, String> hashMap = ClientDiscoverAPI.getWithdraw_cash(allianceValue, amount);
        HttpRequest.post(hashMap, URL.ALLIANCE_BALANCE_WITHDRAW_CASH_APPLY_CASH, new GlobalDataCallBack() {
            @Override
            public void onSuccess(String json) {
                HttpResponse httpResponse = JsonUtil.fromJson(json, HttpResponse.class);
                if (httpResponse.isSuccess()) {

                    // TODO: 2017/1/19 提现成功
                    startActivity(new Intent(WithdrawAllianceBankListActivity.this, MyAccountActivity.class));
                    WithdrawAllianceBankListActivity.this.finish();
                }
            }

            @Override
            public void onFailure(String error) {
                ToastUtils.showSuccess("提现失败");
            }
        });
    }
}
