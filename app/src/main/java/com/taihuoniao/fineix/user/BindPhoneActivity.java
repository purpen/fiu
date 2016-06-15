package com.taihuoniao.fineix.user;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.beans.BindPhone;
import com.taihuoniao.fineix.beans.LoginInfo;
import com.taihuoniao.fineix.beans.SkipBind;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.DataConstants;
import com.taihuoniao.fineix.network.HttpResponse;
import com.taihuoniao.fineix.utils.ActivityUtil;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.LogUtil;
import com.taihuoniao.fineix.utils.LoginCompleteUtils;
import com.taihuoniao.fineix.utils.SPUtil;
import com.taihuoniao.fineix.utils.Util;
import com.taihuoniao.fineix.view.WaittingDialog;

public class BindPhoneActivity extends BaseActivity implements View.OnClickListener {
    private EditText mPhone;
    private EditText mPassWord;
    public static BindPhoneActivity instance = null;
    private WaittingDialog mDialog = null;
    private String avatarUrl, nickName, sex, token;
    private String type;//来源: 1.微信；2.微博；3.ＱＱ
    private String unionId;//微信的联合id
    private String openId;

    public BindPhoneActivity() {
        super(R.layout.activity_bind_phone);
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case DataConstants.PARSER_THIRD_LOGIN_SKIP_PHONE:
                    if (msg.obj != null) {
                        if (msg.obj instanceof SkipBind) {
                            SkipBind skipBind = (SkipBind) msg.obj;
                            if ("true".equals(skipBind.getSuccess())) {
//                                loginSuccess();
                            } else {
                                Toast.makeText(BindPhoneActivity.this, "登录失败，请检查网络", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                    break;
                case DataConstants.PARSER_THIRD_LOGIN_BIND_PHONE:
                    if (msg.obj != null) {
                        if (msg.obj instanceof BindPhone) {
                            BindPhone bindPhone = (BindPhone) msg.obj;
                            if ("true".equals(bindPhone.getSuccess())) {
//                                loginSuccess();
                            } else {
                                Toast.makeText(BindPhoneActivity.this, "绑定失败，请核查手机号和密码是否正确", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                    break;
                case DataConstants.NETWORK_FAILURE:
                    Toast.makeText(BindPhoneActivity.this, "登录失败，请检查网络", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        overridePendingTransition(R.anim.up_register, 0);
        ActivityUtil.getInstance().addActivity(this);
        instance = this;
        initData();


    }

    @Override
    protected void initView() {
        mDialog = new WaittingDialog(this);
        mPhone = (EditText) findViewById(R.id.et_phone_bindPhone);
        mPassWord = (EditText) findViewById(R.id.et_password_bindPhone);
        Button mBind = (Button) findViewById(R.id.bt_bindPhone);
        mBind.setOnClickListener(this);
        TextView mLoginNow = (TextView) findViewById(R.id.tv_click_login_bindPhone);
        mLoginNow.setOnClickListener(this);
        mLoginNow.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        mLoginNow.getPaint().setAntiAlias(true);//抗锯齿
        findViewById(R.id.image_back_bindPhone).setOnClickListener(this);
        findViewById(R.id.image_close_bindPhone).setOnClickListener(this);
    }

    private void initData() {
        token = getIntent().getStringExtra("token");
        type = getIntent().getStringExtra("type");
        nickName = getIntent().getStringExtra("nickName");
        avatarUrl = getIntent().getStringExtra("avatarUrl");
        sex = getIntent().getStringExtra("sex");
        // 下面这两个id的字段没有取错，之所以会这样取，是因在ToLoginActivity中为了三方登录网络访问的方便，在那儿作了判断，因为那儿只需传一个id，而单单是
        // 针对微信不用传openid，要传unionid，换言之，单独对微信是把unionid当作openid传了，但在这个Activity的接口却要求openid和unionid都要传，而除了微信，QQ和微
        // 博都没有unionid的，只有openid，所以要像这样，单独把微信的unionid提出来
        unionId = getIntent().getStringExtra("oid");//微信的联合id
        String openIdWeChat = getIntent().getStringExtra("oidForWeChat");
        openId = getIntent().getStringExtra("oid");
        //1.微信；2.微博；3.ＱＱ
        if ("1".equals(type)) {
            openId = openIdWeChat;
        }
    }

    @Override
    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.bt_bindPhone: //绑定已有账号
                String mPhoneNumber = mPhone.getText() + "";
                String mPassWordNumber = mPassWord.getText() + "";
                ClientDiscoverAPI.bindPhoneNet(openId, unionId, token, mPhoneNumber, mPassWordNumber, type, new RequestCallBack<String>() {
                    @Override
                    public void onStart() {
                        v.setEnabled(false);
                        if (mDialog != null) {
                            mDialog.show();
                        }
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        v.setEnabled(true);
                        mDialog.dismiss();
                        if (responseInfo == null) return;
                        if (TextUtils.isEmpty(responseInfo.result)) return;
                        HttpResponse<LoginInfo> response = JsonUtil.json2Bean(responseInfo.result, new TypeToken<HttpResponse<LoginInfo>>() {
                        });

                        if (response.isSuccess()) {
                            LoginInfo loginInfo = response.getData();
                            SPUtil.write(activity, DataConstants.LOGIN_INFO, JsonUtil.toJson(loginInfo));
                            loginSuccess(loginInfo);
                        }else {
                            Util.makeToast(response.getMessage());
                        }
                    }

                    @Override
                    public void onFailure(HttpException e, String s) {
                        v.setEnabled(true);
                        mDialog.dismiss();
                        Util.makeToast("网络异常");
                    }
                });
//                DataPaser.bindPhoneParser(openId, unionId, token, mPhoneNumber, mPassWordNumber, type, mHandler);
                break;
            case R.id.tv_click_login_bindPhone: //跳过绑定直接登录
//                DataPaser.skipBindParser(MainApplication.uuid, openId, unionId, token, nickName, sex, avatarUrl, type, mHandler);

                ClientDiscoverAPI.skipBindNet(openId, unionId, token, nickName, sex, avatarUrl, type, new RequestCallBack<String>() {
                    @Override
                    public void onStart() {
                        v.setEnabled(false);
                        if (mDialog != null) {
                            mDialog.show();
                        }
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        v.setEnabled(true);
                        mDialog.dismiss();
                        if (responseInfo == null) return;
                        if (TextUtils.isEmpty(responseInfo.result)) return;
                        HttpResponse<LoginInfo> response = JsonUtil.json2Bean(responseInfo.result, new TypeToken<HttpResponse<LoginInfo>>() {
                        });

                        if (response.isSuccess()) {
                            LoginInfo loginInfo = response.getData();
                            SPUtil.write(activity, DataConstants.LOGIN_INFO, JsonUtil.toJson(loginInfo));
                            loginSuccess(loginInfo);
                        }else {
                            Util.makeToast(response.getMessage());
                        }
                    }

                    @Override
                    public void onFailure(HttpException e, String s) {
                        v.setEnabled(true);
                        mDialog.dismiss();
                        Util.makeToast("网络异常");
                    }
                });
                break;
            case R.id.image_close_bindPhone:
                if (OptRegisterLoginActivity.instance != null) {
                    OptRegisterLoginActivity.instance.finish();
                }
                if (ToLoginActivity.instance != null) {
                    ToLoginActivity.instance.finish();
                }
                if (ToRegisterActivity.instance != null) {
                    ToRegisterActivity.instance.finish();
                }
                finish();
                break;
            case R.id.image_back_bindPhone:
                finish();
                break;
        }
    }

    @Override
    public void finish() {
        super.finish();
        //关闭窗体动画显示
        overridePendingTransition(0, R.anim.down_register);
    }

    //选择绑定成功，或跳过绑定之后完成登录，跳入相应界面
    private void loginSuccess(LoginInfo loginInfo) {

//        MainApplication.getIsLoginInfo().setIs_login("1");
        mDialog.dismiss();
//        OptRegisterLoginActivity.instance.finish();
//        mHandler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                BindPhoneActivity.this.finish();
//            }
//        }, 600);


        if (loginInfo.identify.is_scene_subscribe == 0) { //未订阅
            updateUserIdentity();
            startActivity(new Intent(activity, OrderInterestQJActivity.class));
        } else {
            LoginCompleteUtils.goFrom(this);
//            activity.startActivity(new Intent(activity, MainActivity.class));
        }

        if (ToRegisterActivity.instance != null) {
            ToRegisterActivity.instance.finish();
        }
        if (RegisterActivity.instance != null) {
            RegisterActivity.instance.finish();
        }
        if (OptRegisterLoginActivity.instance != null) {
            OptRegisterLoginActivity.instance.finish();
        }
        if (ToLoginActivity.instance != null) {
            ToLoginActivity.instance.finish();
        }

        finish();
    }

    private void updateUserIdentity() {
        String type = "1";//设置非首次登录
        ClientDiscoverAPI.updateUserIdentify(type, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                if (responseInfo == null) return;
                if (TextUtils.isEmpty(responseInfo.result)) return;
                LogUtil.e("updateUserIdentity", responseInfo.result);
                HttpResponse response = JsonUtil.fromJson(responseInfo.result, HttpResponse.class);
                if (response.isSuccess()) {
                    LogUtil.e("updateUserIdentity", "成功改为非首次登录");
                    return;
                }
                LogUtil.e("改为非首次登录失败", responseInfo.result + "===" + response.getMessage());
            }

            @Override
            public void onFailure(HttpException e, String s) {
                if (TextUtils.isEmpty(s)) return;
                LogUtil.e("网络异常", "改为非首次登录失败");
            }
        });
    }
}
