package com.taihuoniao.fineix.user;

import android.content.Intent;
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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.beans.LoginInfo;
import com.taihuoniao.fineix.beans.ThirdLogin;
import com.taihuoniao.fineix.main.MainActivity;
import com.taihuoniao.fineix.main.MainApplication;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.DataConstants;
import com.taihuoniao.fineix.network.HttpResponse;
import com.taihuoniao.fineix.utils.ActivityUtil;
import com.taihuoniao.fineix.utils.EmailUtils;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.LogUtil;
import com.taihuoniao.fineix.utils.LoginCompleteUtils;
import com.taihuoniao.fineix.utils.SPUtil;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.utils.Util;
import com.taihuoniao.fineix.view.WaittingDialog;

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

public class ToRegisterActivity extends BaseActivity implements View.OnClickListener, Handler.Callback, PlatformActionListener {
    private TextView mPhone;
    private ImageView mClose;
    private ImageView mBack;
    private TextView mQq;
    private TextView mWeChat;
    private TextView mSinaWeiBo;
    private String openidForWeChat;//这个只代表微信的openid
    private String userId;//对微信来说，这个是unionid，对QQ和微博，这个都是openid
    private String avatarUrl;
    private String sex;
    private String nickName;
    private String token;
    @Bind(R.id.ll_register)
    LinearLayout ll_register;
    @Bind(R.id.ll_third)
    LinearLayout ll_third;
    @Bind(R.id.ll_phone)
    LinearLayout ll_phone;
    @Bind(R.id.et_phone)
    EditText et_phone;
    @Bind(R.id.et_password)
    EditText et_password;
    @Bind(R.id.et_code)
    EditText et_code;
    @Bind(R.id.btn_verify)
    Button btn_verify;
    private static final String LOGIN_TYPE_WX = "1"; //微信登录
    private static final String LOGIN_TYPE_SINA = "2"; //新浪微博D
    private static final String LOGIN_TYPE_QQ = "3"; //  QQ
    private String loginType = LOGIN_TYPE_WX;//1.微信；2.微博；3.ＱＱ
    private Boolean mFinish = false;//结束当前activity时是以左右动画方式退出,改为false则以上下动画退出
    public static ToRegisterActivity instance = null;
    private WaittingDialog mDialog = null;
    private ReadSmsContent readSmsContent;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                btn_verify.setEnabled(false);
                btn_verify.setTextColor(Color.BLACK);
//                btnSendVertifyCode.setBackgroundResource(R.drawable.user_getcode_gray);
            } else if (msg.what == 2) {
                btn_verify.setText(msg.arg1 + "s");
            } else if (msg.what == 3) {
                btn_verify.setEnabled(true);
                btn_verify.setTextColor(Color.RED);
//                btnSendVertifyCode.setBackgroundResource(R.drawable.user_getcode);
                btn_verify.setText("获取手机验证码");
            } else if (msg.what == 4) {

            }
        }
    };
    public ToRegisterActivity() {
        super(R.layout.activity_to_register);
    }
    @Override
    protected void initView() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        ShareSDK.initSDK(this);//必须先在程序入口处初始化SDK
        ActivityUtil.getInstance().addActivity(this);
        instance = this;
        mDialog = new WaittingDialog(this);
        mPhone = (TextView) findViewById(R.id.tv_phone_number_toregister);
        mPhone.setOnClickListener(this);
        mClose = (ImageView) findViewById(R.id.image_close_toregister);
        mClose.setOnClickListener(this);
        mBack = (ImageView) findViewById(R.id.image_back_toregister);
        mBack.setOnClickListener(this);
        mQq = (TextView) findViewById(R.id.tv_qq_register);
        mQq.setOnClickListener(this);
        mSinaWeiBo = (TextView) findViewById(R.id.tv_weibo_register);
        mSinaWeiBo.setOnClickListener(this);
        mWeChat = (TextView) findViewById(R.id.tv_weixin_register);
        mWeChat.setOnClickListener(this);
        readSmsContent = new ReadSmsContent(new Handler(), this, et_code);
        //注册短信内容监听
        this.getContentResolver().registerContentObserver(Uri.parse("content://sms/"), true, readSmsContent);
    }

    @Override
    public void finish() {
        super.finish();
        if (mFinish) {
            //关闭窗体动画显示
            overridePendingTransition(0, R.anim.down_register);
        }
    }

    @OnClick({R.id.btn_verify,R.id.btn_register})
    void performClick(View v){
        String phone=null;
        switch (v.getId()){
            case R.id.btn_verify: //提交验证码
                 phone= et_phone.getText().toString().trim();
                if (TextUtils.isEmpty(phone)){
                    ToastUtils.showInfo("请输入手机号");
                     return;
                }

                if (!EmailUtils.isMobileNO(phone)){
                    ToastUtils.showInfo("请输入正确手机号");
                    return;
                }
                isPhoneRegisted(phone);
                break;
            case R.id.btn_register: //注册
                phone=et_phone.getText().toString().trim();
                String password=et_password.getText().toString().trim();
                String code=et_code.getText().toString().trim();
                if (TextUtils.isEmpty(phone)){
                    ToastUtils.showInfo("请输入手机号");
                    return;
                }

                if (!EmailUtils.isMobileNO(phone)){
                    ToastUtils.showInfo("请输入正确手机号");
                    return;
                }

                if (TextUtils.isEmpty(code)){
                    ToastUtils.showInfo("请输入手机的验证码");
                    return;
                }

                if (TextUtils.isEmpty(password)){
                    ToastUtils.showInfo("请输入密码");
                    return;
                }
                submitData(password,phone,code);
                break;
        }
    }

    //点击注册按钮提交数据
    private void submitData(String password, String phone, String code) {
        ClientDiscoverAPI.clickRegisterNet(new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                if(responseInfo==null) return;
                if (TextUtils.isEmpty(responseInfo.result)) return;
                HttpResponse<LoginInfo> response = JsonUtil.json2Bean(responseInfo.result, new TypeToken<HttpResponse<LoginInfo>>() {
                });

                if (response.isSuccess()) {
                    LoginInfo loginInfo=response.getData();
                    SPUtil.write(MainApplication.getContext(), DataConstants.LOGIN_INFO,JsonUtil.toJson(loginInfo));
                    if (loginInfo.identify.is_scene_subscribe == 0) { // 未订阅
                        updateUserIdentity();
                        startActivity(new Intent(activity, OrderInterestQJActivity.class));
                    } else {
                        startActivity(new Intent(activity, MainActivity.class));
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
                    return;
                }
                ToastUtils.showError(response.getMessage());
            }

            @Override
            public void onFailure(HttpException e, String s) {
                ToastUtils.showError("网络异常,请确认网络畅通");
            }
        }, password, phone, code);
    }

    private void isPhoneRegisted(final String phone) { //true 未被注册
        ClientDiscoverAPI.getPhoneState(phone, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                if (responseInfo==null) return;
                if (TextUtils.isEmpty(responseInfo.result)) return;
                HttpResponse response = JsonUtil.fromJson(responseInfo.result, HttpResponse.class);
                if(response.isSuccess()){
                    submitPhone(phone);
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
                }else {
                    ToastUtils.showError(response.getMessage());
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                ToastUtils.showError("网络异常,请确保网络畅通");
            }
        });

    }

    // 提交手机号的方法
    private void submitPhone(String phone) {
        ClientDiscoverAPI.getVerifyCodeNet(new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                if (responseInfo==null) return;
                if (responseInfo.result==null) return;
                HttpResponse response = JsonUtil.fromJson(responseInfo.result, HttpResponse.class);
                if (response.isSuccess()){
                    ToastUtils.showInfo(response.getMessage());
                    return;
                }
                ToastUtils.showError(response.getMessage());
            }

            @Override
            public void onFailure(HttpException e, String s) {
                ToastUtils.showError("网络异常,请确保网络畅通");
            }
        }, phone);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_phone_number_toregister:
//                startActivity(new Intent(activity, RegisterActivity.class));
                ll_phone.setVisibility(View.GONE);
                ll_third.setVisibility(View.GONE);
                ll_register.setVisibility(View.VISIBLE);
                ll_register.setAnimation(Util.fromBottom2Top());
                break;
            case R.id.tv_qq_register:
                if (mDialog!=null) {
                    mDialog.show();
                }
                loginType = LOGIN_TYPE_QQ;
                //QQ
                Platform qq = ShareSDK.getPlatform(QQ.NAME);
                authorize(qq);
                break;
            case R.id.tv_weibo_register:
                if (mDialog!=null) {
                    mDialog.show();
                }
                loginType = LOGIN_TYPE_SINA;
                //新浪微博
                Platform sina = ShareSDK.getPlatform(SinaWeibo.NAME);
                authorize(sina);
                break;
            case R.id.tv_weixin_register:
                if (mDialog!=null) {
                    mDialog.show();
                }
                loginType = LOGIN_TYPE_WX;
                //微信登录
                //测试时，需要打包签名；sample测试时，用项目里面的demokey.keystore
                //打包签名apk,然后才能产生微信的登录
                Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
                authorize(wechat);
                break;
            case R.id.image_back_toregister:
                if (ll_third.isShown()){
                    finish();
                }else {
                    ll_register.setVisibility(View.GONE);
                    ll_phone.setVisibility(View.VISIBLE);
                    ll_third.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.image_close_toregister:
                mFinish = true;
                OptRegisterLoginActivity.instance.finish();
                finish();
                break;
        }

    }

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
    public boolean handleMessage(Message msg) {
        return false;
    }

    @Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
        //用户资源都保存到hashMap,通过打印hashMap数据看看有哪些数据是你想要的
        if (i == Platform.ACTION_USER_INFOR) {
            PlatformDb platDB = platform.getDb();//获取数平台数据DB
            //通过DB获取各种数据

            sex = platDB.getUserGender().toString();
            if ("f".equals(sex)) {
                sex = "2";//女
            } else if ("m".equals(sex)) {
                sex = "1";//男
            } else {
                sex = "0";//保密
            }
            avatarUrl = platDB.getUserIcon().toString();
            nickName = platDB.getUserName().toString();
            openidForWeChat = platDB.getUserId();
            token = platDB.getToken().toString();
            userId = null;
            if (TextUtils.equals(LOGIN_TYPE_QQ,loginType)) {
                //QQ的ID得这样获取，这是MOB公司的错，不是字段写错了
                userId = platform.getDb().get("weibo").toString();
            } else if (TextUtils.equals(LOGIN_TYPE_WX, loginType)) {
                //微信这个神坑，我已无力吐槽，干嘛要搞两个ID出来，泥马，后台说要传的是这个ID，字段没有错！用platDB.getUserId()不行！
                userId = platform.getDb().get("unionid").toString();
            } else {
                //除QQ和微信两特例，其他的ID这样取就行了
                userId = platDB.getUserId().toString();
            }
            doThirdLogin();
        }
    }

    private void doThirdLogin() {
        if (TextUtils.isEmpty(userId))  return;
        if (TextUtils.isEmpty(token)) return;
        if(TextUtils.isEmpty(loginType)) return;

        ClientDiscoverAPI.thirdLoginNet(userId, token, loginType, new RequestCallBack<String>() {
            @Override
            public void onStart() {
                if (mDialog!=null) mDialog.show();
            }

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                if (mDialog!=null){
                    mDialog.dismiss();
                }
                if (responseInfo == null) return;
                if (TextUtils.isEmpty(responseInfo.result)) return;
                HttpResponse<ThirdLogin> response = JsonUtil.json2Bean(responseInfo.result, new TypeToken<HttpResponse<ThirdLogin>>() {
                });
                if (response.isSuccess()) {
                    ThirdLogin thirdLogin=response.getData();
                    if (thirdLogin== null) return;
                    if (thirdLogin.has_user == 0) { //跳转去绑定用户
                        Intent intent = new Intent(ToRegisterActivity.this, BindPhoneActivity.class);
                        intent.putExtra("oid", userId);
                        intent.putExtra("oidForWeChat", openidForWeChat);
                        intent.putExtra("token", token);
                        intent.putExtra("avatarUrl", avatarUrl);
                        intent.putExtra("nickName", nickName);
                        intent.putExtra("type", loginType);
                        intent.putExtra("sex", sex);
                        startActivity(intent);
                    }else {        //已有该用户，直接跳转主页或订阅情景
                        LoginInfo instance = LoginInfo.getInstance();
                        instance.setId(thirdLogin.user._id);
                        instance.setNickname(thirdLogin.user.nickname);
                        instance.setSex(thirdLogin.user.sex);
                        instance.areas=thirdLogin.user.areas;
                        instance.setMedium_avatar_url(thirdLogin.user.medium_avatar_url);
                        instance.identify=thirdLogin.user.identify;
                        SPUtil.write(activity, DataConstants.LOGIN_INFO,JsonUtil.toJson(instance));
                        if (thirdLogin.user.identify.is_scene_subscribe==0){ //未订阅
                            updateUserIdentity();
                            startActivity(new Intent(activity, OrderInterestQJActivity.class));
                        }else {
                            LoginCompleteUtils.goFrom(ToRegisterActivity.this);
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
                } else {
                    ToastUtils.showError(response.getMessage());
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                if (mDialog!=null){
                    mDialog.dismiss();
                    ToastUtils.showError("网络异常，请确认网络畅通");
                }
            }
        });
    }
    private void updateUserIdentity() {
        String type = "1";//设置非首次登录
        ClientDiscoverAPI.updateUserIdentify(type, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                if (responseInfo==null) return;
                if (TextUtils.isEmpty(responseInfo.result)) return;
                LogUtil.e("updateUserIdentity",responseInfo.result);
                HttpResponse response = JsonUtil.fromJson(responseInfo.result, HttpResponse.class);
                if (response.isSuccess()){
                    LogUtil.e("updateUserIdentity","成功改为非首次登录");
                    return;
                }
                LogUtil.e("改为非首次登录失败",responseInfo.result+"==="+response.getMessage());
            }

            @Override
            public void onFailure(HttpException e, String s) {
                if (TextUtils.isEmpty(s)) return;
                LogUtil.e("网络异常","改为非首次登录失败");
            }
        });
    }
    @Override
    public void onError(Platform platform, int i, Throwable throwable) {
        if (i == Platform.ACTION_USER_INFOR) {
            if (mDialog.isShowing()) {
                mDialog.dismiss();
                ToastUtils.showError("对不起，授权失败");
            }
        }
        throwable.printStackTrace();
    }

    @Override
    public void onCancel(Platform platform, int i) {
        if (i == Platform.ACTION_USER_INFOR) {
            if (mDialog.isShowing()) {
                mDialog.dismiss();
                ToastUtils.showError("您取消了授权");
            }
        }
    }

    @Override
    protected void onDestroy() {
        this.getContentResolver().unregisterContentObserver(readSmsContent);
        super.onDestroy();
    }
}
