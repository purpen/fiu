package com.taihuoniao.fineix.user;

import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.beans.FindPasswordInfo;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.DataConstants;
import com.taihuoniao.fineix.network.DataPaser;
import com.taihuoniao.fineix.utils.ActivityUtil;
import com.taihuoniao.fineix.utils.EmailUtils;
import com.taihuoniao.fineix.utils.ToastUtils;

public class FindPasswordActivity extends BaseActivity implements View.OnClickListener {

    private EditText editNewPassword;
    private EditText editPhoneNum;
    private EditText editVerifyCode;
    private Button btGetVerifyCode;
    private Boolean mFinish = false;//结束当前activity时是以左右动画方式退出,改为false则以上下动画退出

    private ReadSmsContent readSmsContent;

    public FindPasswordActivity() {
        super(R.layout.activity_find_password);
    }

    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                btGetVerifyCode.setEnabled(false);
                btGetVerifyCode.setTextColor(Color.BLACK);
//                btnSendVertifyCode.setBackgroundResource(R.drawable.user_getcode_gray);
            } else if (msg.what == 2) {
                btGetVerifyCode.setText(msg.arg1 + "s");

            } else if (msg.what == 3) {
                btGetVerifyCode.setEnabled(true);
                btGetVerifyCode.setTextColor(Color.RED);
//                btnSendVertifyCode.setBackgroundResource(R.drawable.user_getcode);
                btGetVerifyCode.setText("获取手机验证码");
            }
            if (msg.what == DataConstants.PARSER_FIND_PASSWORD) {
                if (msg.obj instanceof FindPasswordInfo) {
                    FindPasswordInfo info = (FindPasswordInfo) msg.obj;
                    if (info.getSuccess()) {
                        Toast.makeText(FindPasswordActivity.this, info.getMessage(),
                                Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(FindPasswordActivity.this,
                                info.getMessage(), Toast.LENGTH_SHORT)
                                .show();
                    }
                }
            }
        }
    };

    @Override
    public void finish() {
        super.finish();
        if (mFinish) {
            //关闭窗体动画显示
            overridePendingTransition(0, R.anim.down_register);
        }
    }

    @Override
    protected void initView() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        ActivityUtil.getInstance().addActivity(this);
        ImageView mBack = (ImageView) findViewById(R.id.image_back_find_password);
        mBack.setOnClickListener(this);
        ImageView mClose = (ImageView) findViewById(R.id.image_close_find_password);
        mClose.setOnClickListener(this);
        editNewPassword = (EditText) findViewById(R.id.pass_new);
        editPhoneNum = (EditText) findViewById(R.id.et_phone_new);
        Button btFindPassword = (Button) findViewById(R.id.bt_findpassword);
        btFindPassword.setOnClickListener(this);
        editVerifyCode = (EditText) findViewById(R.id.code_find);
        btGetVerifyCode = (Button) findViewById(R.id.verify_find);
        btGetVerifyCode.setOnClickListener(this);

        readSmsContent = new ReadSmsContent(new Handler(), this, editVerifyCode);
        //注册短信内容监听
        this.getContentResolver().registerContentObserver(Uri.parse("content://sms/"), true, readSmsContent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image_back_find_password:
                finish();
                break;
            case R.id.image_close_find_password:
                mFinish = true;
                OptRegisterLoginActivity.instance.finish();
                ToLoginActivity.instance.finish();
                finish();
                break;
            case R.id.verify_find:
                String phone = editPhoneNum.getText().toString();
                if (!TextUtils.isEmpty(phone) && EmailUtils.isMobileNO(phone)) {

                    Toast.makeText(FindPasswordActivity.this, "正在获取手机验证码",
                            Toast.LENGTH_SHORT).show();
                    //获取手机验证码
                    ClientDiscoverAPI.getVerifyCodeNet(new RequestCallBack<String>() {
                        @Override
                        public void onSuccess(ResponseInfo<String> responseInfo) {

                        }

                        @Override
                        public void onFailure(HttpException e, String s) {

                        }
                    }, phone);

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
                    ToastUtils.showInfo("请输入正确的手机号");
                }
                break;

            case R.id.bt_findpassword:
                String verifyCode = editVerifyCode.getText().toString();
                String phone1 = (editPhoneNum.getText() + "").trim();
                String newPassword = (editNewPassword.getText() + "").trim();

                if (!TextUtils.isEmpty(phone1) && phone1.length() == 11) {
                    if (!TextUtils.isEmpty(newPassword) && newPassword.length() >= 6 && newPassword.length() <= 20) {

                        if (!TextUtils.isEmpty(phone1)
                                && EmailUtils.isMobileNO(phone1)) {

                            DataPaser.findPasswordParser(mHandler, phone1, newPassword, verifyCode);

                        } else {
                            ToastUtils.showInfo("请输入手机号");
                        }
                    } else {
                        ToastUtils.showInfo("请输入6-20位密码");
                    }
                } else {
                    ToastUtils.showInfo("请输入正确的手机号");
                }
                break;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.getContentResolver().unregisterContentObserver(readSmsContent);
    }
}
