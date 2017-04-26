package com.taihuoniao.fineix.user.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.gson.reflect.TypeToken;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.base.HttpRequest;
import com.taihuoniao.fineix.beans.HttpResponse;
import com.taihuoniao.fineix.beans.LoginInfo;
import com.taihuoniao.fineix.beans.ThirdLogin;
import com.taihuoniao.fineix.common.GlobalCallBack;
import com.taihuoniao.fineix.common.GlobalDataCallBack;
import com.taihuoniao.fineix.main.MainApplication;
import com.taihuoniao.fineix.main.fragment.MyBaseFragment;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.DataConstants;
import com.taihuoniao.fineix.network.URL;
import com.taihuoniao.fineix.personal.AllianceRequstDeal;
import com.taihuoniao.fineix.user.BindPhoneActivity;
import com.taihuoniao.fineix.user.CompleteUserInfoActivity;
import com.taihuoniao.fineix.user.OptRegisterLoginActivity;
import com.taihuoniao.fineix.user.ToLoginActivity;
import com.taihuoniao.fineix.user.ToRegisterActivity;
import com.taihuoniao.fineix.utils.EmailUtils;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.LogUtil;
import com.taihuoniao.fineix.utils.LoginCompleteUtils;
import com.taihuoniao.fineix.utils.SPUtil;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.view.MaskedEditText;
import com.taihuoniao.fineix.view.dialog.WaittingDialog;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.PlatformDb;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;

/**
 * @author lilin
 *         created at 2016/8/10 14:24
 */
public class SendCheckCodeFragment extends MyBaseFragment implements PlatformActionListener {
    @Bind(R.id.btn_wechat)
    ImageButton btnWechat;
    @Bind(R.id.btn_sina)
    ImageButton btnSina;
    @Bind(R.id.btn_qq)
    ImageButton btnQq;
    @Bind(R.id.masked_edit_text)
    MaskedEditText maskedEditText;
    @Bind(R.id.bt_send_code)
    Button btSendCode;
    private WaittingDialog mDialog;
    private static final String LOGIN_TYPE_WX = "1"; //微信登录
    private static final String LOGIN_TYPE_SINA = "2"; //新浪微博D
    private static final String LOGIN_TYPE_QQ = "3"; //  QQ
    private String loginType = LOGIN_TYPE_WX;//1.微信；2.微博；3.ＱＱ
    private String openidForWeChat;//这个只代表微信的openid
    private String userId;//对微信来说，这个是unionid，对QQ和微博，这个都是openid
    private String avatarUrl;
    private String sex;
    private String nickName;
    private String token;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    if (btSendCode == null) return;
                    btSendCode.setEnabled(false);
                    btSendCode.setTextColor(getResources().getColor(R.color.color_af8323));
                    break;
                case 2:
                    if (btSendCode == null) return;
                    btSendCode.setText(msg.arg1 + "s");
                    btSendCode.setTextColor(getResources().getColor(R.color.color_af8323));
                    break;
                case 3:
                    if (btSendCode == null) return;
                    btSendCode.setEnabled(true);
                    btSendCode.setTextColor(getResources().getColor(R.color.color_af8323));
                    btSendCode.setText("发送验证码");
                    break;
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
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.setFragmentLayout(R.layout.fragment_send_check_code);
        super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, view);
        return view;
    }

    public static SendCheckCodeFragment newInstance() {
        return new SendCheckCodeFragment();
    }

    @Override
    protected void initViews() {
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        mDialog = new WaittingDialog(activity);
    }


    @OnClick({R.id.bt_send_code, R.id.btn_wechat, R.id.btn_sina, R.id.btn_qq})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_send_code:
                String phone = maskedEditText.getUnmaskedText();
                if (TextUtils.isEmpty(phone)) {
                    ToastUtils.showInfo("请输入手机号");
                    return;
                }
                if (!EmailUtils.isMobileNO(phone)) {
                    ToastUtils.showInfo("请输入正确手机号");
                    return;
                }
                isPhoneRegisted(phone);
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

    private void isPhoneRegisted(final String phone) { //true 未被注册
        HashMap<String, String> defaultParams = ClientDiscoverAPI.getDefaultParams();
        defaultParams.put("account", phone);
        HttpRequest.post(defaultParams, URL.GET_REGIST_STATE, new GlobalDataCallBack() {
            @Override
            public void onSuccess(String json) {
                if (json == null) return;
                if (TextUtils.isEmpty(json)) return;
                HttpResponse response = JsonUtil.fromJson(json, HttpResponse.class);
                if (response.isSuccess()) {
                    getCheckCode(phone);
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
                                    e.printStackTrace();
                                }
                            }

                            Message msg3 = new Message();
                            msg3.what = 3;
                            mHandler.sendMessage(msg3);
                        }

                    }.start();
                } else {
                    ToastUtils.showError(response.getMessage());
                }
            }

            @Override
            public void onFailure(String error) {
                ToastUtils.showError(R.string.network_err);
            }
        });

    }

    private void getCheckCode(final String phone) {
        HashMap<String, String> params = ClientDiscoverAPI.getgetVerifyCodeNetRequestParams(phone);
        HttpRequest.post(params,URL.AUTH_VERIFY_CODE, new GlobalDataCallBack(){
            @Override
            public void onSuccess(String json) {
                if (json == null) return;
                HttpResponse response = JsonUtil.fromJson(json, HttpResponse.class);
                if (response.isSuccess()) {
                    if (activity instanceof ToRegisterActivity) {
                        ToRegisterActivity registerActivity = (ToRegisterActivity) SendCheckCodeFragment.this.activity;
                        ViewPager viewPager = registerActivity.getViewPager();
                        if (null != viewPager) {
                            viewPager.setCurrentItem(1);
                            registerActivity.getRegisterInfo().mobile = phone;
                        }
                    }
                    return;
                }
                ToastUtils.showError(response.getMessage());
            }

            @Override
            public void onFailure(String error) {
//                ToastUtils.showError(R.string.network_err);
            }
        }/*, phone*/);
    }

    private void authorize(Platform plat) {
        if (plat == null) return;
        if (plat.isAuthValid()) {
            plat.removeAccount(true);
        }
        plat.setPlatformActionListener(this);
        //开启SSO授权
        plat.SSOSetting(false);
        plat.showUser(null);
        LogUtil.e("authorize=="+Thread.currentThread().getName());
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

    private void loginSuccess(ThirdLogin thirdLogin) {
        if (thirdLogin.user.identify.is_scene_subscribe == 0) { //未订阅
            updateUserIdentity();
//          startActivity(new Intent(activity, OrderInterestQJActivity.class));
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
            activity.finish();
        } else {
            LoginCompleteUtils.goFrom(activity,null,thirdLogin);
        }
    }


    private void updateUserIdentity() {
        String type = "1";//设置非首次登录
        HashMap<String, String> params = ClientDiscoverAPI.getupdateUserIdentifyRequestParams(type);
        HttpRequest.post(params,  URL.UPDATE_USER_IDENTIFY, new GlobalDataCallBack(){
            @Override
            public void onSuccess(String json) {
                if (TextUtils.isEmpty(json)) return;
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
        LogUtil.e("onError=="+Thread.currentThread().getName());
        setBtnIsEnable(true);
        if (i == Platform.ACTION_USER_INFOR) {
            mHandler.sendEmptyMessage(DataConstants.PARSER_THIRD_LOGIN_ERROR);
        }
        LogUtil.e(TAG,throwable.toString());
        throwable.printStackTrace();
    }

    @Override
    public void onCancel(Platform platform, int i) {
        setBtnIsEnable(true);
        if (i == Platform.ACTION_USER_INFOR) {
            mHandler.sendEmptyMessage(DataConstants.PARSER_THIRD_LOGIN_CANCEL);
        }
    }

    private void setBtnIsEnable(boolean b){
        btnQq.setEnabled(b);
        btnSina.setEnabled(b);
        btnWechat.setEnabled(b);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mHandler != null) mHandler.removeCallbacksAndMessages(null);
        ButterKnife.unbind(this);
    }

}
