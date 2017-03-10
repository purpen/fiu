package com.taihuoniao.fineix.personal.alliance;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
import com.taihuoniao.fineix.personal.AllianceRequstDeal;
import com.taihuoniao.fineix.personal.alliance.bean.WithDrawAccountListBean;
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
    @Bind(R.id.checkBox1)
    CheckBox checkBox1;

    private WithDrawAccountListBean.RowsEntity rowsEntity;
    private boolean isDefault;

    public AddWithdrawAccountInfoActivity() {
        super(R.layout.activity_alliance_add_withdraw_account_info);
    }

    @Override
    protected void initView() {
        customHead.setHeadCenterTxtShow(true, "绑定支付宝");
        customHead.setHeadRightTxtShow(true, "保存");
        customHead.getHeadRightTV().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                commitCardInfomation();
            }
        });
        WindowUtils.chenjin(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        rowsEntity = getIntent().getParcelableExtra("rowsEntity");
        checkBox1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                isDefault = b;
            }
        });
    }

    /**
     * 修改创建银行卡信息
     */
    private void createBankInfomation(String id, String alliance_id, String kind, String pay_type, String account, String bank_address, String is_default, String username, String phone, String verify_code) {
        Map<String, String> allianceWithDraw01 = ClientDiscoverAPI.getAllianceWithDraw01(id, alliance_id, kind, pay_type, account, bank_address, is_default, username, phone, verify_code);
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

    private void commitCardInfomation() {
        String str = editText1.getText().toString();
        String str1 = editText2.getText().toString();
        String str2 = editText3.getText().toString();
        String str3 = editText4.getText().toString();
        if (TextUtils.isEmpty(str)) {
            ToastUtils.showError("姓名不能为空");
        } else if (TextUtils.isEmpty(str1)) {
            ToastUtils.showError("请输入支付宝账号");
        } else if (TextUtils.isEmpty(str2)) {
            ToastUtils.showError("请输入手机号");
        } else if (TextUtils.isEmpty(str3)) {
            ToastUtils.showError("请输入短信验证码");
        } else {
            if (rowsEntity != null) {
                createBankInfomation(rowsEntity.get_id(), AllianceRequstDeal.getAllianceValue(), "2", null, str1, null, isDefault ? "1" : "0", str, str2, str3);
            } else {
                createBankInfomation(null, AllianceRequstDeal.getAllianceValue(), "2", null, str1, null, isDefault ? "1" : "0", str, str2, str3);
            }
        }
    }
}
