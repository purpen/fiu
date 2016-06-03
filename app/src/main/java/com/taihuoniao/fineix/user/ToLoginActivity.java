package com.taihuoniao.fineix.user;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.beans.LoginInfo;
import com.taihuoniao.fineix.beans.ThirdLogin;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.DataConstants;
import com.taihuoniao.fineix.network.HttpResponse;
import com.taihuoniao.fineix.utils.ActivityUtil;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.LogUtil;
import com.taihuoniao.fineix.utils.LoginCompleteUtils;
import com.taihuoniao.fineix.utils.SPUtil;
import com.taihuoniao.fineix.utils.Util;
import com.taihuoniao.fineix.view.svprogress.SVProgressHUD;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.PlatformDb;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;

public class ToLoginActivity extends BaseActivity implements View.OnClickListener, Handler.Callback, PlatformActionListener {
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
    private static final String LOGIN_TYPE_WX = "1"; //微信登录
    private static final String LOGIN_TYPE_SINA = "2"; //新浪微博D
    private static final String LOGIN_TYPE_QQ = "3"; //  QQ
    private String loginType = LOGIN_TYPE_WX;//1.微信；2.微博；3.ＱＱ
    private Boolean mFinish = false;//结束当前activity时是以左右动画方式退出,改为false则以上下动画退出
    public static ToLoginActivity instance = null;
    private SVProgressHUD mDialog;
    public ToLoginActivity() {
        super(R.layout.activity_to_login);
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case DataConstants.PARSER_THIRD_LOGIN_CANCEL:
                    if (mDialog!=null) {
                        mDialog.dismiss();
                        mDialog.showInfoWithStatus("取消授权");
                    }
//                    Toast.makeText(ToLoginActivity.this, "取消授权", Toast.LENGTH_SHORT).show();
                    break;
                case DataConstants.PARSER_THIRD_LOGIN_ERROR:
                    if (mDialog!=null) {
                        mDialog.dismiss();
                        mDialog.showErrorWithStatus("授权失败");
                    }
//                    Toast.makeText(ToLoginActivity.this, "授权失败", Toast.LENGTH_SHORT).show();
                    break;
                case DataConstants.PARSER_THIRD_LOGIN:
//                    if (msg.obj != null) {
//                        if (msg.obj instanceof ThirdLogin) {
//                            ThirdLogin thirdLogin = (ThirdLogin) msg.obj;
//                            if (mDialog.isShowing()) {
//                                mDialog.dismiss();
//                            }
//                            if ("true".equals(thirdLogin.getSuccess())) {
//                                Log.e(">>>", ">>>>>userId>>>" + userId);
//                                Log.e(">>>", ">>>>>openidForWeChat>>>" + openidForWeChat);
//                                //为0不存在，1是存在，不存在表示第一次用这个三方号登录本APP，那就去绑定界面，存在则直接进入APP
//                                if ("0".equals(thirdLogin.getHas_user())) {
//                                    Intent intent = new Intent(ToLoginActivity.this, BindPhoneActivity.class);
//                                    intent.putExtra("oid", userId);
//                                    intent.putExtra("oidForWeChat", openidForWeChat);
//                                    intent.putExtra("token", token);
//                                    intent.putExtra("avatarUrl", avatarUrl);
//                                    intent.putExtra("nickName", nickName);
//                                    intent.putExtra("type", type + "");
//                                    intent.putExtra("sex", sex);
//                                    startActivity(intent);
//                                } else {
//                                    switch (MainApplication.which_activity) {
//                                        case DataConstants.SceneDetailActivity:
//                                            sendBroadcast(new Intent(DataConstants.BroadSceneDetail));
//                                            break;
//                                        case DataConstants.ElseActivity:
//                                            break;
//                                        default:
////                                            THNMainActivity.instance.finish();
//                                            Intent intent = new Intent(ToLoginActivity.this,
//                                                    MainActivity.class);
//                                            startActivity(intent);
//                                            break;
//                                    }
//                                    MainApplication.getIsLoginInfo().setIs_login("1");
//                                    mDialog.dismiss();
//                                    if (OptRegisterLoginActivity.instance != null) {
//                                        OptRegisterLoginActivity.instance.finish();
//                                    }
//                                    if (ToRegisterActivity.instance != null) {
//                                        ToRegisterActivity.instance.finish();
//                                    }
//                                    mHandler.postDelayed(new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            ToLoginActivity.this.finish();
//                                        }
//                                    }, 600);
//                                }
//                            } else {
//                                Toast.makeText(ToLoginActivity.this, "登录失败，请检查网络", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    }

                    break;
            }
        }
    };

    @Override
    protected void initView() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        ShareSDK.initSDK(this);//必须先在程序入口处初始化SDK
        instance = this;
        ActivityUtil.getInstance().addActivity(this);
        mDialog = new SVProgressHUD(this);
        mPhone = (TextView) findViewById(R.id.tv_phone_number_tologin);
        mPhone.setOnClickListener(this);
        mClose = (ImageView) findViewById(R.id.image_close_tologin);
        mClose.setOnClickListener(this);
        mBack = (ImageView) findViewById(R.id.image_back_tologin);
        mBack.setOnClickListener(this);
        mQq = (TextView) findViewById(R.id.tv_qq_tologin);
        mQq.setOnClickListener(this);
        mWeChat = (TextView) findViewById(R.id.tv_weixin_tologin);
        mWeChat.setOnClickListener(this);
        mSinaWeiBo = (TextView) findViewById(R.id.tv_weibo_tologin);
        mSinaWeiBo.setOnClickListener(this);
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        if (mDialogAppear) {
//            if (!mDialog.isShowing()) {
//                mDialog.show();
//            }
//        }
//    }

    @Override
    public void finish() {
        super.finish();
        if (mFinish) {
            //关闭窗体动画显示
            overridePendingTransition(0, R.anim.down_register);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_qq_tologin: //QQ登录
                if (mDialog!=null) {
                    mDialog.show();
                }
                mQq.setEnabled(false);
                loginType = LOGIN_TYPE_QQ;
                //QQ 不用打包签名即可测试
                Platform qq = ShareSDK.getPlatform(QQ.NAME);
                authorize(qq);
                break;
            case R.id.tv_weixin_tologin: //微信登录
                if (mDialog!=null) {
                    mDialog.show();
                }
                mWeChat.setEnabled(false);
                loginType = LOGIN_TYPE_WX;
                //微信登录
                //测试时，需要打包签名；sample测试时，用项目里面的demokey.keystore
                //打包签名apk,然后才能产生微信的登录
                Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
                authorize(wechat);
                break;
            case R.id.tv_weibo_tologin: //新浪微博登录
                if (mDialog!=null) {
                    mDialog.show();
                }
                mSinaWeiBo.setEnabled(false);
                loginType = LOGIN_TYPE_SINA;
                //新浪微博，测试时，需要打包签名
                Platform sina = ShareSDK.getPlatform(SinaWeibo.NAME);
                authorize(sina);
                break;
            case R.id.tv_phone_number_tologin:
                Intent toLoginIntent = new Intent(ToLoginActivity.this, LoginActivity.class);
                startActivity(toLoginIntent);
                break;
            case R.id.image_back_tologin:
                finish();
                break;
            case R.id.image_close_tologin:
                mFinish = true;
                if (OptRegisterLoginActivity.instance!=null){
                    OptRegisterLoginActivity.instance.finish();
                }
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
    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
        // 这个方法中不能放对话框、吐丝这些耗时的操作，否则会直接跳到onError()中执行
        //用户资源都保存到hashMap，通过打印hashMap数据看看有哪些数据是你想要的
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
            } else if (TextUtils.equals(LOGIN_TYPE_WX,loginType)) {
                //微信这个神坑，我已无力吐槽，干嘛要搞两个ID出来，泥马，后台说要传的是这个ID，字段没有错！用platDB.getUserId()不行！
                userId = platform.getDb().get("unionid").toString();
            } else {
                //除QQ和微信两特例，其他的ID这样取就行了
                userId = platDB.getUserId().toString();
            }
//            DataPaser.thirdLoginParser(userId, token, type + "", mHandler);
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
                if (!activity.isFinishing()&& mDialog!=null) mDialog.show();
            }

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                if (!activity.isFinishing()&& mDialog!=null) mDialog.dismiss();
                mQq.setEnabled(true);
                mWeChat.setEnabled(true);
                mSinaWeiBo.setEnabled(true);
                if (responseInfo == null) return;
                if (TextUtils.isEmpty(responseInfo.result)) return;
                HttpResponse<ThirdLogin> response = JsonUtil.json2Bean(responseInfo.result, new TypeToken<HttpResponse<ThirdLogin>>() {
                });
                if (response.isSuccess()) {
                    ThirdLogin thirdLogin=response.getData();
                    if (thirdLogin== null) return;
                    if (thirdLogin.has_user == 0) { //跳转去绑定用户
                        Intent intent = new Intent(activity, BindPhoneActivity.class);
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
//                        MainApplication.getIsLoginInfo().setIs_login("1");

                        if (thirdLogin.user.identify.is_scene_subscribe==0){ //未订阅
                            updateUserIdentity();

                            startActivity(new Intent(activity, OrderInterestQJActivity.class));
                        }else {
                            LoginCompleteUtils.goFrom(ToLoginActivity.this);
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
                   mDialog.showErrorWithStatus(response.getMessage());
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                mQq.setEnabled(true);
                mWeChat.setEnabled(true);
                mSinaWeiBo.setEnabled(true);
                if (mDialog!=null){
                    mDialog.dismiss();
                }
                mDialog.showErrorWithStatus("网络异常,请确认网络畅通");
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
        mQq.setEnabled(true);
        mWeChat.setEnabled(true);
        mSinaWeiBo.setEnabled(true);
        if (i == Platform.ACTION_USER_INFOR) {
            mHandler.sendEmptyMessage(DataConstants.PARSER_THIRD_LOGIN_ERROR);
        }
        throwable.printStackTrace();
    }

    @Override
    public void onCancel(Platform platform, int i) {
        mQq.setEnabled(true);
        mWeChat.setEnabled(true);
        mSinaWeiBo.setEnabled(true);
        if (i == Platform.ACTION_USER_INFOR) {
            mHandler.sendEmptyMessage(DataConstants.PARSER_THIRD_LOGIN_CANCEL);
        }
    }

    @Override
    public boolean handleMessage(Message msg) {
        return false;
    }


}
