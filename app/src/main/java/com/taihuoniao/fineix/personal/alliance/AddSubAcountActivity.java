package com.taihuoniao.fineix.personal.alliance;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.base.HttpRequest;
import com.taihuoniao.fineix.beans.HttpResponse;
import com.taihuoniao.fineix.common.GlobalDataCallBack;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.URL;
import com.taihuoniao.fineix.personal.alliance.bean.SubAccountListBean;
import com.taihuoniao.fineix.personal.alliance.bean.WithDrawSetDefaultAccoutBean;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.utils.TypeConversionUtils;
import com.taihuoniao.fineix.utils.WindowUtils;
import com.taihuoniao.fineix.view.CustomHeadView;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Stephen on 2017/4/27 18:50
 * Email: 895745843@qq.com
 */

public class AddSubAcountActivity extends BaseActivity {

    @Bind(R.id.custom_head)
    CustomHeadView customHead;
    @Bind(R.id.editText1)
    EditText editText1;
    @Bind(R.id.textView6)
    TextView textView6;
    @Bind(R.id.editText2)
    EditText editText2;
    @Bind(R.id.button1)
    TextView button1;
    @Bind(R.id.editText3)
    EditText editText3;
    @Bind(R.id.editText4)
    EditText editText4;
    @Bind(R.id.editText5)
    EditText editText5;
    @Bind(R.id.linearLayout_set_password)
    LinearLayout linearLayoutSetPassword;
    @Bind(R.id.editText6)
    EditText editText6;
    @Bind(R.id.linearLayout_alliance_verification_container)
    LinearLayout linearLayoutAllianceVerificationContainer;

    private boolean isPhoneRegisted;
    private SubAccountListBean.RowsEntity rowsEntity;

    public AddSubAcountActivity() {
        super(R.layout.activity_alliance_my_sub_account_add);
    }

    @Override
    protected void initView() {
        customHead.setHeadCenterTxtShow(true, rowsEntity == null ? "添加子账号" : "修改子账号信息");
        customHead.setHeadRightTxtShow(true, "保存");
        customHead.getHeadRightTV().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rowsEntity == null) {
                    commitSubAccountInfomation();
                } else {
                    modifySubAccountInfomation();
                }
            }
        });
        WindowUtils.chenjin(this);

        if (rowsEntity != null) {
            linearLayoutAllianceVerificationContainer.setVisibility(View.GONE);
            button1.setVisibility(View.GONE);
            editText2.setEnabled(false);
            editText1.setText(rowsEntity.getUsername());
            editText2.setText(rowsEntity.getAccount());
            editText6.setText(String.valueOf(TypeConversionUtils.StringConvertDouble(rowsEntity.getAddition()) * 100));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    private void commitSubAccountInfomation() {
        String str = editText1.getText().toString();
        String str1 = editText2.getText().toString();
        String str2 = editText3.getText().toString();
        String str3 = editText4.getText().toString();
        String str4 = editText5.getText().toString();
        String str5 = editText6.getText().toString();
        double ratio = TypeConversionUtils.StringConvertDouble(str5);
        if (TextUtils.isEmpty(str)) {
            ToastUtils.showError("请输入姓名");
        } else if (TextUtils.isEmpty(str1)) {
            ToastUtils.showError("请输入手机号");
        } else if (TextUtils.isEmpty(str2)) {
            ToastUtils.showError("请输入验证码");
        } else if (TextUtils.isEmpty(str5)) {
            ToastUtils.showError("请输入子账号佣金分成");
        } else if (ratio <= 0) {
            ToastUtils.showError("分成比例不能小于0");
        } else if (ratio > 100) {
            ToastUtils.showError("分成比例不能大于100");
        }else if (!isPhoneRegisted) {
            if (TextUtils.isEmpty(str3)) {
                ToastUtils.showError("请输入密码");
            } else if (TextUtils.isEmpty(str4)) {
                ToastUtils.showError("请输入确认密码");
            } else if (!str3.equals(str4)) {
                ToastUtils.showError("密码不一致，请重新设置密码");
            } else if (phone != null && !TextUtils.equals(str1, phone)) {
                ToastUtils.showError("接收验证码手机号与当前手机号不一致");
            } else {
                addOrModifySubAccount(null, str1, str, str2, str3, String.valueOf(ratio/100));
            }
        } else if (phone != null && !TextUtils.equals(str1, phone)) {
            ToastUtils.showError("接收验证码手机号与当前手机号不一致");
        } else {
            addOrModifySubAccount(null, str1, str, str2, str3, String.valueOf(ratio/100));
        }
    }

    /**
     * 添加子账号
     */
    private void addOrModifySubAccount(String id, String account, String username, String verify_code, String password, String ratio) {
        Map<String, String> allianceWithDraw01 = ClientDiscoverAPI.addAndModifySubAccount(id, account, username, verify_code, password, ratio);
        HttpRequest.post(allianceWithDraw01, URL.STORAGE_MANAGE_SAVE, new GlobalDataCallBack() {
            @Override
            public void onSuccess(String json) {
                HttpResponse<WithDrawSetDefaultAccoutBean> bean = JsonUtil.json2Bean(json, new TypeToken<HttpResponse<WithDrawSetDefaultAccoutBean>>() {
                });
                if (bean != null) {
                    if (bean.isSuccess()) {
                        setResult(Activity.RESULT_OK);
                        AddSubAcountActivity.this.finish();
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

    /**
     * 发送短信验证码
     */
    private void sendVerificationCode(String phone) {
        Map<String, String> allianceWithDraw01 = ClientDiscoverAPI.getgetVerifyCodeNetRequestParams(phone);
        HttpRequest.post(allianceWithDraw01, URL.AUTH_VERIFY_CODE, new GlobalDataCallBack() {
            @Override
            public void onSuccess(String json) {
                HttpResponse httpResponse = JsonUtil.json2Bean(json, new TypeToken<HttpResponse<Object>>() {
                });
                if (httpResponse.isSuccess()) {
                    ToastUtils.showInfo("发送验证码成功");
                    startToTiming();
                } else {
                    ToastUtils.showError("发送验证码失败");
                    resetVerificationStatus();
                }
            }

            @Override
            public void onFailure(String error) {
                ToastUtils.showSuccess(error);
                resetVerificationStatus();
            }
        });
    }

    private Timer timer;
    private int second = 60;
    private String phone;

    /**
     * 开始倒计时
     */
    private void startToTiming() {
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

    @OnClick({R.id.button1})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button1:
                String tempPhone = editText2.getText().toString();
                if (TextUtils.isEmpty(tempPhone)) {
                    ToastUtils.showInfo("请输入手机号");
                } else if (tempPhone.length() < 11) {
                    ToastUtils.showInfo("请输入11位手机号");
                } else {
                    phone = tempPhone;
                    isPhoneRegisted(phone);
                    button1.setEnabled(false);
                    sendVerificationCode(this.phone);
                }
                break;
        }
    }

    private void isPhoneRegisted(final String phone) { //true 未被注册
        HashMap<String, String> defaultParams = ClientDiscoverAPI.getDefaultParams();
        defaultParams.put("account", phone);
        HttpRequest.post(defaultParams, URL.GET_REGIST_STATE, new GlobalDataCallBack() {
            @Override
            public void onSuccess(String json) {
                if (json == null) return;
                if (TextUtils.isEmpty(json)) return;
                HttpResponse response = JsonUtil.fromJson(json, HttpResponse.class);
                isPhoneRegisted = !response.isSuccess();
                linearLayoutSetPassword.setVisibility(isPhoneRegisted ? View.GONE : View.VISIBLE);
            }

            @Override
            public void onFailure(String error) {
                ToastUtils.showError(R.string.network_err);
            }
        });
    }

    @Override
    protected void getIntentData() {
        rowsEntity = getIntent().getParcelableExtra("modifySubAccountInfo");
    }

    private void modifySubAccountInfomation() {
        String str = editText1.getText().toString();
        String str5 = editText6.getText().toString();
        double ratio = TypeConversionUtils.StringConvertDouble(str5);
        if (TextUtils.isEmpty(str)) {
            ToastUtils.showError("请输入姓名");
        } else if (TextUtils.isEmpty(str5)) {
            ToastUtils.showError("请输入子账号佣金分成");
        }  else if (ratio <= 0) {
            ToastUtils.showError("分成比例不能小于0");
        } else if (ratio > 100) {
            ToastUtils.showError("分成比例不能大于100");
        }else if (TextUtils.equals(rowsEntity.getUsername(), str) && TextUtils.equals(rowsEntity.getAddition(), String.valueOf(ratio/100))) {
            ToastUtils.showError("您没有需要修改的信息");
        } else {
            addOrModifySubAccount(rowsEntity.get_id(), null, str, null, null, String.valueOf(ratio/100));
        }
    }
}
