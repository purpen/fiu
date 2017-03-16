package com.taihuoniao.fineix.personal.alliance;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.base.HttpRequest;
import com.taihuoniao.fineix.beans.HttpResponse;
import com.taihuoniao.fineix.common.GlobalDataCallBack;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.URL;
import com.taihuoniao.fineix.personal.AllianceRequstDeal;
import com.taihuoniao.fineix.personal.alliance.bean.BankListBean;
import com.taihuoniao.fineix.personal.alliance.bean.WithDrawAccountListBean;
import com.taihuoniao.fineix.personal.alliance.bean.WithDrawCreateAccountBean;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.utils.WindowUtils;
import com.taihuoniao.fineix.view.CustomHeadView;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 绑定/修改银行卡信息
 * Created by Stephen on 2017/3/9 10:00
 * Email: 895745843@qq.com
 */

public class AddWithdrawAccountInfo2Activity extends BaseActivity {

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
    @Bind(R.id.checkBox1)
    CheckBox checkBox1;
    @Bind(R.id.button1)
    TextView button1;
    @Bind(R.id.editText7)
    EditText editText7;
    @Bind(R.id.textView_alliance_select_bank)
    TextView textViewAllianceSelectBank;

    private static final int REQUEST_CODE_ORIGINAL_BANK = 10007;
    private WithDrawAccountListBean.RowsEntity rowsEntity;
    private boolean isDefault;
    private String payType;
    private String phone;

    public AddWithdrawAccountInfo2Activity() {
        super(R.layout.activity_alliance_add_withdraw_account_info2);
    }

    @Override
    protected void getIntentData() {
        rowsEntity = getIntent().getParcelableExtra("ParcelableExtraRowsEntity");
    }

    @Override
    protected void initView() {
        customHead.setHeadCenterTxtShow(true, rowsEntity == null ? "绑定提现账户" : "修改提现账户");
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
    protected void initList() {
        if (rowsEntity != null) {
            phone = rowsEntity.getPhone();
            payType = rowsEntity.getPay_type();
            textViewAllianceSelectBank.setText(rowsEntity.getPay_type_label());
            editText1.setText(rowsEntity.getBank_address());
            editText2.setText(rowsEntity.getUsername());
            editText3.setText(rowsEntity.getAccount());
            editText7.setText(rowsEntity.getPhone());
            checkBox1.setChecked("1".equals(rowsEntity.getIs_default()));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
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
                HttpResponse<WithDrawCreateAccountBean> bean = JsonUtil.json2Bean(json, new TypeToken<HttpResponse<WithDrawCreateAccountBean>>() {
                });
                if (bean != null) {
                    if (bean.isSuccess()) {
                        setResult(Activity.RESULT_OK);
                        AddWithdrawAccountInfo2Activity.this.finish();
                    } else {
                        ToastUtils.showError(bean.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(String error) {
                ToastUtils.showSuccess("请求失败");
            }
        });
    }

    private void commitCardInfomation() {
        String str = editText1.getText().toString();
        String str1 = editText2.getText().toString();
        String str2 = editText3.getText().toString();
        String str3 = editText7.getText().toString();
        String str4 = editText4.getText().toString();
        if (TextUtils.isEmpty(payType)) {
            ToastUtils.showError("请选择银行");
        } else if (TextUtils.isEmpty(str)) {
            ToastUtils.showError("请输入开户行名称");
        } else if (TextUtils.isEmpty(str1)) {
            ToastUtils.showError("请输入持卡人姓名");
        } else if (TextUtils.isEmpty(str2)) {
            ToastUtils.showError("请输入银行卡号");
        } else if (TextUtils.isEmpty(str3)) {
            ToastUtils.showError("请输入手机号");
        } else if (TextUtils.isEmpty(str4)) {
            ToastUtils.showError("请输入短信验证码");
        } else if (phone != null && !TextUtils.equals(str3, phone)) {
            ToastUtils.showError("接收验证码手机号与当前手机号不一致");
        } else {
            if (rowsEntity != null) {
                createBankInfomation(rowsEntity.get_id(), AllianceRequstDeal.getAllianceValue(), "1", payType, str2, str, isDefault ? "1" : "0", str1, str3, str4);
            } else {
                createBankInfomation(null, AllianceRequstDeal.getAllianceValue(), "1", payType, str2, str, isDefault ? "1" : "0", str1, str3, str4);
            }
        }
    }

    /**
     * 发送短信验证码
     */
    private void sendVerificationCode(String phone) {
        Map<String, String> allianceWithDraw01 = ClientDiscoverAPI.getgetVerifyCodeNetRequestParams(phone);
        allianceWithDraw01.put("type", "5");
        HttpRequest.post(allianceWithDraw01, URL.AUTH_VERIFY_CODE, new GlobalDataCallBack() {
            @Override
            public void onSuccess(String json) {
                HttpResponse httpResponse = JsonUtil.json2Bean(json, new TypeToken<HttpResponse<Object>>() {
                });
                if (httpResponse.isSuccess()) {
                    ToastUtils.showInfo("发送验证码成功");
                    startToTiming();
                }
            }

            @Override
            public void onFailure(String error) {
            }
        });
    }

    private Timer timer;
    private int second = 60;

    /**
     * 开始倒计时
     */
    private void startToTiming() {
        button1.setEnabled(false);
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        second--;
                        if (second == 0) {
                            resetVerificationStatus();
                            return;
                        }
                        button1.setText(String.format("%s", second + "秒"));
                    }
                });

            }
        }, 0, 1000);
    }

    /**
     * 重置验证码状态
     */
    private void resetVerificationStatus() {
        button1.setText("获取验证码");
        button1.setEnabled(true);
        second = 60;
        if (timer != null) {
            timer.cancel();
        }
    }

    @OnClick({R.id.textView_alliance_select_bank, R.id.button1})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.textView_alliance_select_bank:
                startActivityForResult(new Intent(AddWithdrawAccountInfo2Activity.this, WithdrawAllianceBankListActivity.class), REQUEST_CODE_ORIGINAL_BANK);
//                showSelectedBankListDialog();
                break;
            case R.id.button1:
                String tempPhone = editText7.getText().toString();
                if (TextUtils.isEmpty(tempPhone)) {
                    ToastUtils.showInfo("请输入手机号");
                } else if (tempPhone.length() < 11) {
                    ToastUtils.showInfo("请输入11位手机号");
                } else {
                    phone = tempPhone;
                    sendVerificationCode(this.phone);
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_ORIGINAL_BANK && data != null) {
            BankListBean.BanksEntity banksEntity = data.getParcelableExtra("banksEntity");
            textViewAllianceSelectBank.setText(banksEntity.getName());
            payType = banksEntity.getId();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
