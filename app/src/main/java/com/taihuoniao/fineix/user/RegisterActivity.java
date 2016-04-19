package com.taihuoniao.fineix.user;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.NetworkManager;
import com.taihuoniao.fineix.utils.ActivityUtil;
import com.taihuoniao.fineix.utils.EmailUtils;
import com.taihuoniao.fineix.utils.Util;

import org.json.JSONObject;


public class RegisterActivity extends BaseActivity implements View.OnClickListener {
    private SystemBarTintManager tintManager;
    private View mDecorView;

    private Button btnRegister;
    private Button btnSendVertifyCode;
    private EditText editUser;
    private EditText editPass;
    private EditText editCode;
    private ImageView mBack;
    private ImageView mClose;
    public static RegisterActivity instance = null;
    private ReadSmsContent readSmsContent;
    private String phone;

    public RegisterActivity(){
        super(R.layout.activity_register);
    }
    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                btnSendVertifyCode.setEnabled(false);
                btnSendVertifyCode.setTextColor(Color.BLACK);
//                btnSendVertifyCode.setBackgroundResource(R.drawable.user_getcode_gray);
            } else if (msg.what == 2) {
                btnSendVertifyCode.setText(msg.arg1 + "s");
            } else if (msg.what == 3) {
                btnSendVertifyCode.setEnabled(true);
                btnSendVertifyCode.setTextColor(Color.RED);
//                btnSendVertifyCode.setBackgroundResource(R.drawable.user_getcode);
                btnSendVertifyCode.setText("获取手机验证码");
            } else if (msg.what == 4) {

            }
        }
    };

    @Override
    protected void initView() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        ActivityUtil.getInstance().addActivity(this);
        overridePendingTransition(R.anim.up_register, 0);
        instance=this;

        mBack = (ImageView) findViewById(R.id.image_back_register);
        mClose = (ImageView) findViewById(R.id.image_close_register);
        mBack.setOnClickListener(this);
        mClose.setOnClickListener(this);
        btnRegister = (Button) findViewById(R.id.register);
        btnRegister.setOnClickListener(this);
        btnSendVertifyCode = (Button) findViewById(R.id.vertify);
        btnSendVertifyCode.setOnClickListener(this);
        editUser = (EditText) findViewById(R.id.user);
//        editUser.setText("13366469976");
        editPass = (EditText) findViewById(R.id.pass);
        editCode = (EditText) findViewById(R.id.code);

        readSmsContent = new ReadSmsContent(new Handler(), this, editCode);
        //注册短信内容监听
        this.getContentResolver().registerContentObserver(Uri.parse("content://sms/"), true, readSmsContent);
    }

    @Override
    public void finish() {
        super.finish();
            //关闭窗体动画显示
            overridePendingTransition(0, R.anim.down_register);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.image_back_register:
                finish();
                break;
            case R.id.image_close_register:
                OptRegisterLoginActivity.instance.finish();
                ToRegisterActivity.instance.finish();
                finish();
                break;
            case R.id.user:

                break;

            case R.id.vertify:
                phone = editUser.getText().toString();
                if (!TextUtils.isEmpty(phone) && EmailUtils.isMobileNO(phone)) {
                    submitphone(phone);
                    new Thread() {
                        @Override
                        public void run() {
                            super.run();

                            int recordSed = 60;
                            Message msg = new Message();
                            msg.what = 1;
                            mHandler.sendMessage(msg);

                            for (; ; ) {
                                if (--recordSed < 0)
                                    break;

                                Message msg2 = new Message();
                                msg2.what = 2;
                                msg2.arg1 = recordSed;
                                mHandler.sendMessage(msg2);
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                            }

                            Message msg3 = new Message();
                            msg3.what = 3;
                            mHandler.sendMessage(msg3);
                        }

                    }.start();
                } else {
                    Util.makeToast("请输入正确的手机号");
                }
                break;
            case R.id.register:

                final String codes = editCode.getText().toString();
                final String phone1 = editUser.getText().toString();
                final String password = editPass.getText().toString();

                if (!TextUtils.isEmpty(phone1) && phone1.length() == 11) {
                    if (!TextUtils.isEmpty(password) && password.length() >= 6 && password.length() <= 20) {

                        if (!TextUtils.isEmpty(phone1)
                                && EmailUtils.isMobileNO(phone1)) {
                            NetworkManager.getInstance().cancel("register");
                            NetworkManager.getInstance().cancelAll();
                            submitData(password, phone1, codes);
                        } else {
                            Util.makeToast("请输入正确的手机号");
                        }
                    } else {
                        Util.makeToast("请输入6-20位密码");
                    }
                } else {
                    Util.makeToast("请输入正确的手机号");
                }

                break;
            default:
                break;
        }
    }

    // 提交手机号的方法
    private void submitphone(String phone) {

        ClientDiscoverAPI.getVerifyCodeNet(new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                if (responseInfo==null){
                    return;
                }
                if (responseInfo.result==null){
                    return;
                }
                Util.makeToast(responseInfo.result);
            }

            @Override
            public void onFailure(HttpException e, String s) {
                if (!TextUtils.isEmpty(s)){
                    Util.makeToast(s);
                }
            }
        }, phone);
    }


    //点击注册按钮提交数据
    private void submitData(String password, String phone, String code) {
        ClientDiscoverAPI.clickRegisterNet(new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                if (!TextUtils.isEmpty(responseInfo.result)) {
                    try {
                        JSONObject obj = new JSONObject(responseInfo.result);
                        String successObject = obj.optString("success");
                        if ("true".equals(successObject)) {
                            Util.makeToast("注册成功");
//                            RegisterInfo user = (RegisterInfo) DataParser.parserRegisterToList(responseInfo.result);
//                            Log.e(">>>", ">>>>>which_activity>>>" +MainApplication.which_activity);
//                            Log.e(">>>", ">>>>>forPaywayToMain>>>" +THNApplication.forPaywayToMain);
                            Intent registerIntent = new Intent(RegisterActivity.this,LoginActivity.class);
                            startActivity(registerIntent);

                        } else {
                            JSONObject obj2 = new JSONObject(responseInfo.result);
                            String  failure= obj2.optString("message");
                            if (!TextUtils.isEmpty(failure)){
                                Util.makeToast(failure);
                            }
                        }
                        //点击跳到登录界面


                    } catch (Exception e) {
                    }
                }
            }


            @Override
            public void onFailure(HttpException e, String s) {
                if (!TextUtils.isEmpty(s)){
                    Util.makeToast(s);
                }
            }
        }, password, phone, code);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.getContentResolver().unregisterContentObserver(readSmsContent);

    }

}


