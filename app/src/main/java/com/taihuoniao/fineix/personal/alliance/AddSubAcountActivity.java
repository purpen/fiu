package com.taihuoniao.fineix.personal.alliance;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
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


    public AddSubAcountActivity() {
        super(R.layout.activity_alliance_my_sub_account_add);
    }

    @Override
    protected void initView() {
        customHead.setHeadCenterTxtShow(true, "添加子账号");
        customHead.setHeadRightTxtShow(true, "保存");
        customHead.getHeadRightTV().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                commitSubAccountInfomation();
            }
        });
        WindowUtils.chenjin(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    private void commitSubAccountInfomation() {
        String str = editText1.getText().toString();
        String str1 = editText2.getText().toString();
        String str2 = editText3.getText().toString();
        String str3 = editText4.getText().toString();
        String str4 = editText5.getText().toString();
        if (TextUtils.isEmpty(str)) {
            ToastUtils.showError("请输入姓名");
        } else if (TextUtils.isEmpty(str1)) {
            ToastUtils.showError("请输入手机号");
        } else if (TextUtils.isEmpty(str2)) {
            ToastUtils.showError("请输入验证码");
        } else if (TextUtils.isEmpty(str3)) {
            ToastUtils.showError("请输入密码");
        } else if (TextUtils.isEmpty(str4)) {
            ToastUtils.showError("请输入确认密码");
        } else if (phone != null && !TextUtils.equals(str3, phone)) {
            ToastUtils.showError("接收验证码手机号与当前手机号不一致");
        } else {

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
    private String phone;

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
                    sendVerificationCode(this.phone);
                }
                break;
        }
    }
}
