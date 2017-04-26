package com.taihuoniao.fineix.user;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.gson.reflect.TypeToken;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.common.GlobalCallBack;
import com.taihuoniao.fineix.common.GlobalDataCallBack;
import com.taihuoniao.fineix.base.HttpRequest;
import com.taihuoniao.fineix.beans.HttpResponse;
import com.taihuoniao.fineix.beans.LoginInfo;
import com.taihuoniao.fineix.beans.ThirdLogin;
import com.taihuoniao.fineix.main.MainApplication;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.DataConstants;
import com.taihuoniao.fineix.network.URL;
import com.taihuoniao.fineix.personal.AllianceRequstDeal;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.LogUtil;
import com.taihuoniao.fineix.utils.LoginCompleteUtils;
import com.taihuoniao.fineix.utils.SPUtil;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.view.dialog.WaittingDialog;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.OnClick;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.PlatformDb;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;

public class ToLoginActivity extends BaseActivity implements PlatformActionListener {
    @Bind(R.id.btn_wechat)
    ImageButton btnWechat;
    @Bind(R.id.btn_sina)
    ImageView btnSina;
    @Bind(R.id.btn_qq)
    ImageView btnQq;
    private String openidForWeChat;//这个只代表微信的openid
    private String userId;//对微信来说，这个是unionid，对QQ和微博，这个都是openid
    private String avatarUrl;
    private String sex;
    private String nickName;
    private String token;
    @Bind(R.id.et_phone)
    EditText et_phone;
    @Bind(R.id.et_password)
    EditText et_password;
    @Bind(R.id.ibtn_clear)
    ImageButton ibtnClear;
    @Bind(R.id.ibtn_show_hide)
    ImageButton ibtnShowHide;
    private boolean flag = false;
    private static final String LOGIN_TYPE_WX = "1"; //微信登录
    private static final String LOGIN_TYPE_SINA = "2"; //新浪微博D
    private static final String LOGIN_TYPE_QQ = "3"; //  QQ
    private String loginType = LOGIN_TYPE_WX;//1.微信；2.微博；3.ＱＱ
    public static ToLoginActivity instance = null;
    private WaittingDialog mDialog;

    public ToLoginActivity() {
        super(R.layout.activity_to_login);
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case DataConstants.PARSER_THIRD_LOGIN_CANCEL:
                    if (mDialog != null) {
                        mDialog.dismiss();
                        ToastUtils.showInfo("您取消了授权");
                    }
                    break;
                case DataConstants.PARSER_THIRD_LOGIN_ERROR:
                    if (mDialog != null) {
                        mDialog.dismiss();
                        ToastUtils.showError("对不起,授权失败");
                    }
                    break;
                case DataConstants.PARSER_THIRD_LOGIN:
                    break;
            }
        }
    };

    @Override
    protected void initView() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        ShareSDK.initSDK(this);//必须先在程序入口处初始化SDK
        instance = this;
        mDialog = new WaittingDialog(this);
    }

    @Override
    public void finish() {
        super.finish();
        Boolean mFinish = false;
        if (mFinish) {
            //关闭窗体动画显示
            overridePendingTransition(0, R.anim.down_register);
        }
    }

    @Override
    protected void installListener() {
        et_phone.addTextChangedListener(tw);
    }

    private TextWatcher tw = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence cs, int start, int before, int count) {
            String keyWord = cs.toString().trim();
            if (!TextUtils.isEmpty(keyWord)) {
                ibtnClear.setVisibility(View.VISIBLE);
            } else {
                ibtnClear.setVisibility(View.GONE);
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    @OnClick({R.id.ibtn_back, R.id.ibtn_show_hide, R.id.ibtn_clear, R.id.bt_login, R.id.btn_find_pass, R.id.btn_wechat, R.id.btn_sina, R.id.btn_qq})
    void performClick(View v) {
        switch (v.getId()) {
            case R.id.ibtn_show_hide:
                if (flag) {
                    flag = false;
                    et_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    ibtnShowHide.setImageResource(R.mipmap.pass_hide);
                } else {
                    flag = true;
                    et_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    ibtnShowHide.setImageResource(R.mipmap.pass_show);
                }
                int length = et_password.getText().length();
                if (length > 0) et_password.setSelection(length);
                break;
            case R.id.ibtn_clear:
                et_phone.getText().clear();
                ibtnClear.setVisibility(View.GONE);
                break;
            case R.id.ibtn_back:
                finish();
                break;
            case R.id.bt_login:
                submitData(v);
                break;
            case R.id.btn_find_pass:
                Intent intentFindPassword = new Intent(activity,
                        FindPasswordActivity.class);
                startActivity(intentFindPassword);
                break;
            case R.id.btn_wechat:
                if (mDialog != null) {
                    mDialog.show();
                }
                setBtnIsEnable(false);
                loginType = LOGIN_TYPE_WX;
                Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
                authorize(wechat);
                break;
            case R.id.btn_sina:
                if (mDialog != null) {
                    mDialog.show();
                }
                setBtnIsEnable(false);
                loginType = LOGIN_TYPE_SINA;
                //新浪微博，测试时，需要打包签名
                Platform sina = ShareSDK.getPlatform(SinaWeibo.NAME);
                authorize(sina);
                break;
            case R.id.btn_qq:
                if (mDialog != null) {
                    mDialog.show();
                }
                setBtnIsEnable(false);
                loginType = LOGIN_TYPE_QQ;
                Platform qq = ShareSDK.getPlatform(QQ.NAME);
                authorize(qq);
                break;

        }
    }

    private void authorize(Platform plat) {
        if (plat == null) {
            return;
        }
        if (plat.isAuthValid()) {
            plat.removeAccount(true);
        }
        plat.setPlatformActionListener(this);
        //开启SSO授权
        plat.SSOSetting(false);
        plat.showUser(null);
    }

    @Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
        if (i == Platform.ACTION_USER_INFOR) {
            PlatformDb platDB = platform.getDb();
            sex = platDB.getUserGender();
            if ("f".equals(sex)) {
                sex = "2";//女
            } else if ("m".equals(sex)) {
                sex = "1";//男
            } else {
                sex = "0";//保密
            }
            avatarUrl = platDB.getUserIcon();
            nickName = platDB.getUserName();
            openidForWeChat = platDB.getUserId();
            token = platDB.getToken();
            userId = null;
            if (TextUtils.equals(LOGIN_TYPE_QQ, loginType)) {
                userId = platform.getDb().get("weibo");
            } else if (TextUtils.equals(LOGIN_TYPE_WX, loginType)) {
                userId = platform.getDb().get("unionid");
            } else {
                userId = platDB.getUserId();
            }
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    doThirdLogin();
                }
            });

        }
    }

    private void doThirdLogin() {
        if (TextUtils.isEmpty(userId)) return;
        if (TextUtils.isEmpty(token)) return;
        if (TextUtils.isEmpty(loginType)) return;
        HashMap<String, String> params =ClientDiscoverAPI. getthirdLoginNetRequestParams(userId, token, loginType);
        HttpRequest.post(params, URL.AUTH_THIRD_SIGN, new GlobalDataCallBack(){
            @Override
            public void onStart() {
                if (!activity.isFinishing() && mDialog != null) mDialog.show();
            }

            @Override
            public void onSuccess(String json) {
                if (!activity.isFinishing() && mDialog != null) mDialog.dismiss();
                setBtnIsEnable(true);
                HttpResponse<ThirdLogin> response = JsonUtil.json2Bean(json, new TypeToken<HttpResponse<ThirdLogin>>() {
                });
                if (response.isSuccess()) {
                    final ThirdLogin thirdLogin = response.getData();
                    if (thirdLogin == null) return;
                    if (thirdLogin.has_user == 0) { //跳转去绑定用户
                        MainApplication.hasUser = false;
                        Intent intent = new Intent(activity, BindPhoneActivity.class);
                        intent.putExtra("oid", userId);
                        intent.putExtra("oidForWeChat", openidForWeChat);
                        intent.putExtra("token", token);
                        intent.putExtra("avatarUrl", avatarUrl);
                        intent.putExtra("nickName", nickName);
                        intent.putExtra("type", loginType);
                        intent.putExtra("sex", sex);
                        startActivity(intent);
                    } else {        //已有该用户，直接跳转主页或订阅情景
                        MainApplication.hasUser = true;
                        LoginInfo instance = LoginInfo.getInstance();
                        instance.setId(thirdLogin.user._id);
                        instance.setNickname(thirdLogin.user.nickname);
                        instance.setSex(thirdLogin.user.sex);
                        instance.areas = thirdLogin.user.areas;
                        instance.setMedium_avatar_url(avatarUrl);
                        instance.identify = thirdLogin.user.identify;
                        SPUtil.write(DataConstants.LOGIN_INFO, JsonUtil.toJson(instance));
                        AllianceRequstDeal.requestAllianceIdentify(new GlobalCallBack() {
                            @Override
                            public void callBack(Object object) {
                                loginSuccess(thirdLogin);
                            }
                        });
                    }
                } else {
                    ToastUtils.showError(response.getMessage());
                }
            }

            void loginSuccess(ThirdLogin thirdLogin) {
                if (thirdLogin.user.identify.is_scene_subscribe == 0) { //未订阅
                    updateUserIdentity();
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
                    LoginCompleteUtils.goFrom(activity,null,thirdLogin);
                }
            }

            @Override
            public void onFailure(String error) {
                setBtnIsEnable(true);
                if (mDialog != null) {
                    mDialog.dismiss();
                }
                ToastUtils.showError(R.string.network_err);
            }
        });
    }

    private void updateUserIdentity() {
        String type = "1";//设置非首次登录
        HashMap<String, String> params = ClientDiscoverAPI.getupdateUserIdentifyRequestParams(type);
        HttpRequest.post(params,  URL.UPDATE_USER_IDENTIFY, new GlobalDataCallBack(){
            @Override
            public void onSuccess(String json) {
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

    @Override
    public void onError(Platform platform, int i, Throwable throwable) {
        setBtnIsEnable(true);
        if (i == Platform.ACTION_USER_INFOR) {
            mHandler.sendEmptyMessage(DataConstants.PARSER_THIRD_LOGIN_ERROR);
        }
        throwable.printStackTrace();
    }

    @Override
    public void onCancel(Platform platform, int i) {
        setBtnIsEnable(true);
        if (i == Platform.ACTION_USER_INFOR) {
            mHandler.sendEmptyMessage(DataConstants.PARSER_THIRD_LOGIN_CANCEL);
        }
    }



    private void submitData(final View v) {
        String phone = et_phone.getText().toString().trim();
        String password = et_password.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            ToastUtils.showInfo("请输入手机号");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            ToastUtils.showInfo("请输入密码");
            return;
        }

        HashMap<String, String> params = ClientDiscoverAPI.getclickLoginNetRequestParams(phone, password);
        HttpRequest.post(params, URL.AUTH_LOGIN, new GlobalDataCallBack(){
            @Override
            public void onStart() {
                v.setEnabled(false);
                if (mDialog != null) mDialog.show();
                super.onStart();
            }

            @Override
            public void onSuccess(String json) {
                v.setEnabled(true);
                mDialog.dismiss();
                HttpResponse<LoginInfo> response = JsonUtil.json2Bean(json, new TypeToken<HttpResponse<LoginInfo>>() {
                });
                if (response.isSuccess()) {//登录界面登录成功
                    MainApplication.hasUser = true;
                    final LoginInfo loginInfo = response.getData();
                    SPUtil.write(DataConstants.LOGIN_INFO, JsonUtil.toJson(loginInfo));
                    AllianceRequstDeal.requestAllianceIdentify(new GlobalCallBack() {
                        @Override
                        public void callBack(Object object) {
                            loginSuccess(loginInfo);
                        }
                    });
                    return;
                }
                ToastUtils.showError(response.getMessage());
            }

            void loginSuccess(LoginInfo loginInfo) {
                if (loginInfo.identify.is_scene_subscribe == 0) { // 未订阅
                    updateUserIdentity();
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
//                        startActivity(new Intent(activity, MainActivity.class));
                    LoginCompleteUtils.goFrom(ToLoginActivity.this,loginInfo,null);
                }
            }

            @Override
            public void onFailure(String error) {
                v.setEnabled(true);
                mDialog.dismiss();
                ToastUtils.showError(R.string.network_err);
            }
        });

    }

    private void setBtnIsEnable(boolean b){
        btnQq.setEnabled(b);
        btnSina.setEnabled(b);
        btnWechat.setEnabled(b);
    }
}
