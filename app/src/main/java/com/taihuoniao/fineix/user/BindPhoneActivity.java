package com.taihuoniao.fineix.user;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.BindPhonePagerAdapter;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.base.GlobalDataCallBack;
import com.taihuoniao.fineix.base.HttpRequest;
import com.taihuoniao.fineix.beans.HttpResponse;
import com.taihuoniao.fineix.beans.LoginInfo;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.DataConstants;
import com.taihuoniao.fineix.network.URL;
import com.taihuoniao.fineix.utils.EmailUtils;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.LogUtil;
import com.taihuoniao.fineix.utils.LoginCompleteUtils;
import com.taihuoniao.fineix.utils.SPUtil;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.utils.Util;
import com.taihuoniao.fineix.view.CustomViewPager;
import com.taihuoniao.fineix.view.dialog.WaittingDialog;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;

public class BindPhoneActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.viewPager)
    CustomViewPager viewPager;
    private EditText mPhone;
    private EditText mPassWord;
    public static BindPhoneActivity instance = null;
    private WaittingDialog mDialog = null;
    private String avatarUrl, nickName, sex, token;
    private String type;//来源: 1.微信；2.微博；3.ＱＱ
    private String unionId;//微信的联合id
    private String openId;
    private Button bt_login;
    private TextView tv_bind;
    private TextView tv_login;
    private Button bindAccount;

    public BindPhoneActivity() {
        super(R.layout.activity_bind_phone);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        overridePendingTransition(R.anim.up_register, 0);
        instance = this;
        initData();

    }

    @Override
    protected void initView() {
        ArrayList<View> viewList = new ArrayList<>();
        mDialog = new WaittingDialog(this);
        viewPager.setPagingEnabled(false);
        View viewSkip = Util.inflateView(R.layout.view_bindphone_skip, null);
        View viewBindPhone = Util.inflateView(R.layout.view_bindphone_bind, null);
        viewList.add(viewSkip);
        viewList.add(viewBindPhone);
        BindPhonePagerAdapter adapter = new BindPhonePagerAdapter(viewList);
        viewPager.setAdapter(adapter);
        bt_login = ButterKnife.findById(viewSkip, R.id.bt_login); //跳过绑定
        tv_bind = ButterKnife.findById(viewSkip, R.id.tv_bind); //切换到绑定界面

        tv_login = ButterKnife.findById(viewBindPhone, R.id.tv_login); //绑定界面，立即登录
        mPhone = ButterKnife.findById(viewBindPhone, R.id.et_phone_bindPhone); //绑定界面手机号
        mPassWord = ButterKnife.findById(viewBindPhone, R.id.et_password_bindPhone); //绑定界面密码
        bindAccount = ButterKnife.findById(viewBindPhone, R.id.bt_bindPhone); //绑定界面密码

//        mPhone = (EditText) findViewById(R.id.et_phone_bindPhone);
//        mPassWord = (EditText) findViewById(R.id.et_password_bindPhone);
//        Button mBind = (Button) findViewById(R.id.bt_bindPhone);
//        mBind.setOnClickListener(this);
//        TextView mLoginNow = (TextView) findViewById(R.id.tv_click_login_bindPhone);
//        mLoginNow.setOnClickListener(this);
        tv_bind.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        tv_bind.getPaint().setAntiAlias(true);//抗锯齿
        tv_login.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        tv_login.getPaint().setAntiAlias(true);//抗锯齿
    }

    @Override
    protected void installListener() {
        findViewById(R.id.image_back_bindPhone).setOnClickListener(this);
        findViewById(R.id.image_close_bindPhone).setOnClickListener(this);
        tv_bind.setOnClickListener(this);
        bt_login.setOnClickListener(this);
        tv_login.setOnClickListener(this);
        bindAccount.setOnClickListener(this);
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
            case R.id.tv_bind:
                viewPager.setCurrentItem(1, true);
                break;
            case R.id.bt_bindPhone: //绑定已有账号
                String phone = mPhone.getText().toString().trim();
                String password = mPassWord.getText().toString().trim();
                if (TextUtils.isEmpty(phone)) {
                    ToastUtils.showInfo("请输入手机号");
                    return;
                }

                if (!EmailUtils.isMobileNO(phone)) {
                    ToastUtils.showInfo("请输入正确手机号");
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    ToastUtils.showInfo("请输入密码");
                    return;
                }
                HashMap<String, String> params = ClientDiscoverAPI.getbindPhoneNetRequestParams(openId, unionId, token, phone, password, type);
                HttpRequest.post(params, URL.AUTH_THIRD_REGISTER_WITH_PHONE, new GlobalDataCallBack(){
//                ClientDiscoverAPI.bindPhoneNet(openId, unionId, token, phone, password, type, new GlobalDataCallBack() {
                    @Override
                    public void onStart() {
                        v.setEnabled(false);
                        if (mDialog != null) {
                            mDialog.show();
                        }
                    }

                    @Override
                    public void onSuccess(String json) {
                        Log.e("<<<登录成功", json);
                        v.setEnabled(true);
                        mDialog.dismiss();
                        if (TextUtils.isEmpty(json)) return;
                        HttpResponse<LoginInfo> response = JsonUtil.json2Bean(json, new TypeToken<HttpResponse<LoginInfo>>() {
                        });

                        if (response.isSuccess()) {
                            LoginInfo loginInfo = response.getData();
                            loginInfo.setMedium_avatar_url(avatarUrl);
                            SPUtil.write(DataConstants.LOGIN_INFO, JsonUtil.toJson(loginInfo));
                            loginSuccess(loginInfo);
                        } else {
                            Util.makeToast(response.getMessage());
                        }
                    }

                    @Override
                    public void onFailure(String error) {
                        v.setEnabled(true);
                        mDialog.dismiss();
                        ToastUtils.showError(R.string.network_err);
                    }
                });
                break;
            case R.id.tv_login:
            case R.id.bt_login: //跳过绑定直接登录
                HashMap<String, String> params3 =ClientDiscoverAPI. getskipBindNetRequestParams(openId, unionId, token, nickName, sex, avatarUrl, type);
                HttpRequest.post(params3, URL.AUTH_THIRD_REGISTER_WITHOUT_PHONE, new GlobalDataCallBack(){
//                ClientDiscoverAPI.skipBindNet(openId, unionId, token, nickName, sex, avatarUrl, type, new RequestCallBack<String>() {
                    @Override
                    public void onStart() {
                        v.setEnabled(false);
                        if (mDialog != null) {
                            mDialog.show();
                        }
                    }

                    @Override
                    public void onSuccess(String json) {
                        Log.e("<<<登录成功", json);
                        v.setEnabled(true);
                        mDialog.dismiss();
                        if (TextUtils.isEmpty(json)) return;
                        HttpResponse<LoginInfo> response = JsonUtil.json2Bean(json, new TypeToken<HttpResponse<LoginInfo>>() {
                        });

                        if (response.isSuccess()) {
                            LoginInfo loginInfo = response.getData();
                            loginInfo.setMedium_avatar_url(avatarUrl);
                            SPUtil.write(DataConstants.LOGIN_INFO, JsonUtil.toJson(loginInfo));
                            loginSuccess(loginInfo);
                        } else {
                            Util.makeToast(response.getMessage());
                        }
                    }

                    @Override
                    public void onFailure(String error) {
                        v.setEnabled(true);
                        mDialog.dismiss();
                        ToastUtils.showError(R.string.network_err);
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
        mDialog.dismiss();
        if (loginInfo.identify.is_scene_subscribe == 0) { //未订阅
            updateUserIdentity();
//            startActivity(new Intent(activity, OrderInterestQJActivity.class));
            startActivity(new Intent(activity, CompleteUserInfoActivity.class));
            if (ToRegisterActivity.instance != null) {
                ToRegisterActivity.instance.finish();
            }
            if (OptRegisterLoginActivity.instance != null) {
                OptRegisterLoginActivity.instance.finish();
            }
            if (ToLoginActivity.instance != null) {
                ToLoginActivity.instance.finish();
            }
            finish();
        } else {
            LoginCompleteUtils.goFrom(this, loginInfo, null);
        }
    }

    private void updateUserIdentity() {
        String type = "1";//设置非首次登录
        HashMap<String, String> params = ClientDiscoverAPI.getupdateUserIdentifyRequestParams(type);
        HttpRequest.post(params,  URL.UPDATE_USER_IDENTIFY, new GlobalDataCallBack(){
//        ClientDiscoverAPI.updateUserIdentify(type, new RequestCallBack<String>() {
            @Override
            public void onSuccess(String json) {
                if (TextUtils.isEmpty(json)) return;
                LogUtil.e("updateUserIdentity", json);
                HttpResponse response = JsonUtil.fromJson(json, HttpResponse.class);
                if (response.isSuccess()) {
                    LogUtil.e("updateUserIdentity", "成功改为非首次登录");
                    return;
                }
                LogUtil.e("改为非首次登录失败", json + "===" + response.getMessage());
            }

            @Override
            public void onFailure(String error) {
                if (TextUtils.isEmpty(error)) return;
                LogUtil.e("网络异常", "改为非首次登录失败");
            }
        });
    }
}
