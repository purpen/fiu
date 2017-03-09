package com.taihuoniao.fineix.personal.alliance;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.base.HttpRequest;
import com.taihuoniao.fineix.beans.HttpResponse;
import com.taihuoniao.fineix.common.GlobalDataCallBack;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.URL;
import com.taihuoniao.fineix.personal.alliance.bean.WithDrawCreateAccountBean;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.utils.WindowUtils;
import com.taihuoniao.fineix.view.CustomHeadView;

import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Stephen on 2017/3/9 10:00
 * Email: 895745843@qq.com
 */

public class AddWithdrawAccountInfoActivity extends BaseActivity {

    @Bind(R.id.custom_head)
    CustomHeadView customHead;
    @Bind(R.id.editText1)
    EditText editText1;
    @Bind(R.id.editText2)
    EditText editText2;
    @Bind(R.id.editText3)
    EditText editText3;
    @Bind(R.id.editText4)
    EditText editText4;
    @Bind(R.id.button1)
    Button button1;

    public AddWithdrawAccountInfoActivity() {
        super(R.layout.activity_alliance_add_withdraw_account_info);
    }

    @Override
    protected void initView() {
        customHead.setHeadCenterTxtShow(true, "添加银行卡信息");
        WindowUtils.chenjin(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
        initUI();
    }

    private void initUI() {
//        balance = getIntent().getStringExtra("balance");
//        textView2.setText(String.format("¥ %s", balance));
    }


    /**
     * 修改创建银行卡信息
     */
    private void requestData() {
        Map<String, String> allianceWithDraw01 = ClientDiscoverAPI.getAllianceWithDraw01(null, null, null, null, null, null, null, null, null, null);
        HttpRequest.post(allianceWithDraw01, URL.ALLIANCE_PAYMENT_CARD_SAVE, new GlobalDataCallBack() {
            @Override
            public void onSuccess(String json) {
                WithDrawCreateAccountBean bean = JsonUtil.fromJson(json, new TypeToken<HttpResponse<WithDrawCreateAccountBean>>() {
                });
                if (bean != null) {
                    Toast.makeText(activity, "添加信息成功", Toast.LENGTH_SHORT).show();
                    AddWithdrawAccountInfoActivity.this.finish();
                }
            }

            @Override
            public void onFailure(String error) {
                ToastUtils.showSuccess("提现失败");
            }
        });
    }
}
