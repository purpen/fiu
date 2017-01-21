package com.taihuoniao.fineix.user;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

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

public class OptRegisterLoginActivity extends BaseActivity implements Handler.Callback, PlatformActionListener {
    @Bind(R.id.btn_login)
    Button btnLogin;
    @Bind(R.id.btn_register)
    Button btnRegister;
    @Bind(R.id.image_close_reg_login)
    ImageView imageCloseRegLogin;
    public static OptRegisterLoginActivity instance;
    @Bind(R.id.ll_btn)
    LinearLayout llBtn;
    @Bind(R.id.btn_wechat)
    ImageButton btnWechat;
    @Bind(R.id.btn_sina)
    ImageButton btnSina;
    @Bind(R.id.btn_qq)
    ImageButton btnQq;
    private String openidForWeChat;//这个只代表微信的openid
    private String userId;//对微信来说，这个是unionid，对QQ和微博，这个都是openid
    private String avatarUrl;
    private String sex;
    private String nickName;
    private String token;
    private WaittingDialog mDialog;
    private static final String LOGIN_TYPE_WX = "1"; //微信登录
    private static final String LOGIN_TYPE_SINA = "2"; //新浪微博D
    private static final String LOGIN_TYPE_QQ = "3"; //  QQ
    private String loginType = LOGIN_TYPE_WX;//1.微信；2.微博；3.ＱＱ
    public OptRegisterLoginActivity() {
        super(R.layout.activity_opt_register_login);
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case DataConstants.PARSER_THIRD_LOGIN_CANCEL:
                    if (mDialog != null) {
                        mDialog.dismiss();
                        ToastUtils.showInfo("取消授权");
                    }
                    break;
                case DataConstants.PARSER_THIRD_LOGIN_ERROR:
                    if (mDialog != null) {
                        mDialog.dismiss();
                        ToastUtils.showError("授权失败");
                    }
                    break;
                case DataConstants.PARSER_THIRD_LOGIN:
                    break;
            }
        }
    };

    //执行授权,获取用户信息
    //文档：http://wiki.mob.com/Android_%E8%8E%B7%E5%8F%96%E7%94%A8%E6%88%B7%E8%B5%84%E6%96%99
    private void authorize(Platform plat) {
        if (plat == null) {
            return;
        }
        if (plat.isValid()) {
            plat.removeAccount();
        }
        plat.setPlatformActionListener(this);
        //开启SSO授权
        plat.SSOSetting(false);
        plat.showUser(null);
    }


    @Override
    public void finish() {
        instance = null;
        super.finish();
        //关闭窗体动画显示
        overridePendingTransition(0, R.anim.down_register);
    }

    @Override
    protected void onDestroy() {
        instance = null;
        super.onDestroy();
    }

    @Override
    protected void initView() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        instance = this;
        mDialog = new WaittingDialog(activity);
        overridePendingTransition(R.anim.up_register, 0);
    }

    @OnClick({R.id.image_close_reg_login, R.id.btn_login, R.id.btn_register, R.id.btn_wechat, R.id.btn_sina, R.id.btn_qq})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_close_reg_login:
                finish();
                break;
            case R.id.btn_login:
                startActivity(new Intent(this, ToLoginActivity.class));
                break;
            case R.id.btn_register:
                startActivity(new Intent(this, ToRegisterActivity.class));
                break;
            case R.id.btn_wechat:
                if (mDialog != null) {
                    mDialog.show();
                }
                btnWechat.setEnabled(false);
                loginType = LOGIN_TYPE_WX;
                Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
                authorize(wechat);
                break;
            case R.id.btn_sina:
                if (mDialog != null) {
                    mDialog.show();
                }
                btnSina.setEnabled(false);
                loginType = LOGIN_TYPE_SINA;
                //新浪微博，测试时，需要打包签名
                Platform sina = ShareSDK.getPlatform(SinaWeibo.NAME);
                authorize(sina);
                break;
            case R.id.btn_qq:
                if (mDialog != null) {
                    mDialog.show();
                }
                btnQq.setEnabled(false);
                loginType = LOGIN_TYPE_QQ;
                Platform qq = ShareSDK.getPlatform(QQ.NAME);
                authorize(qq);
                break;
        }
    }


    @Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
        // 这个方法中不能放对话框、吐丝这些耗时的操作，否则会直接跳到onError()中执行
        //用户资源都保存到hashMap，通过打印hashMap数据看看有哪些数据是你想要的
        if (i == Platform.ACTION_USER_INFOR) {
            PlatformDb platDB = platform.getDb();//获取数平台数据DB
            //通过DB获取各种数据
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
                //QQ的ID得这样获取，这是MOB公司的错，不是字段写错了
                userId = platform.getDb().get("weibo");
            } else if (TextUtils.equals(LOGIN_TYPE_WX, loginType)) {
                //微信这个神坑，我已无力吐槽，干嘛要搞两个ID出来，泥马，后台说要传的是这个ID，字段没有错！用platDB.getUserId()不行！
                userId = platform.getDb().get("unionid");
            } else {
                //除QQ和微信两特例，其他的ID这样取就行了
                userId = platDB.getUserId();
            }
            doThirdLogin();
        }
    }

    private void doThirdLogin() {
        if (TextUtils.isEmpty(userId)) return;
        if (TextUtils.isEmpty(token)) return;
        if (TextUtils.isEmpty(loginType)) return;

        HashMap<String, String> params =ClientDiscoverAPI. getthirdLoginNetRequestParams(userId, token, loginType);
        HttpRequest.post(params, URL.AUTH_THIRD_SIGN, new GlobalDataCallBack(){
//        ClientDiscoverAPI.thirdLoginNet(userId, token, loginType, new RequestCallBack<String>() {
            @Override
            public void onStart() {
                if (!activity.isFinishing() && mDialog != null) mDialog.show();
            }

            @Override
            public void onSuccess(String json) {
                Log.e("<<<登录成功",json);
                if (!activity.isFinishing() && mDialog != null) mDialog.dismiss();
                btnQq.setEnabled(true);
                btnSina.setEnabled(true);
                btnWechat.setEnabled(true);
                if (TextUtils.isEmpty(json)) return;
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

            @Override
            public void onFailure(String error) {
                btnQq.setEnabled(true);
                btnSina.setEnabled(true);
                btnWechat.setEnabled(true);
                if (mDialog != null) {
                    mDialog.dismiss();
                }
                ToastUtils.showError(R.string.network_err);
            }
        });
    }

    void loginSuccess(ThirdLogin thirdLogin) {
        if (thirdLogin.user.identify.is_scene_subscribe == 0) { //未订阅
            updateUserIdentity();
//                            startActivity(new Intent(activity, OrderInterestQJActivity.class));
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

    @Override
    public void onError(Platform platform, int i, Throwable throwable) {
        btnQq.setEnabled(true);
        btnSina.setEnabled(true);
        btnWechat.setEnabled(true);
        if (i == Platform.ACTION_USER_INFOR) {
            mHandler.sendEmptyMessage(DataConstants.PARSER_THIRD_LOGIN_ERROR);
        }
        throwable.printStackTrace();
    }

    @Override
    public void onCancel(Platform platform, int i) {
        btnQq.setEnabled(true);
        btnSina.setEnabled(true);
        btnWechat.setEnabled(true);
        if (i == Platform.ACTION_USER_INFOR) {
            mHandler.sendEmptyMessage(DataConstants.PARSER_THIRD_LOGIN_CANCEL);
        }
    }

    @Override
    public boolean handleMessage(Message msg) {
        return false;
    }
}
