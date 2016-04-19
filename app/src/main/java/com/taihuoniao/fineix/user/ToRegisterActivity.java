package com.taihuoniao.fineix.user;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.beans.ThirdLogin;
import com.taihuoniao.fineix.main.MainActivity;
import com.taihuoniao.fineix.main.MainApplication;
import com.taihuoniao.fineix.network.DataConstants;
import com.taihuoniao.fineix.network.DataPaser;
import com.taihuoniao.fineix.utils.ActivityUtil;
import com.taihuoniao.fineix.view.WaittingDialog;

import java.util.HashMap;

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
    private int type = 1;//1.微信；2.微博；3.ＱＱ
    private Boolean mFinish = false;//结束当前activity时是以左右动画方式退出,改为false则以上下动画退出
    public static ToRegisterActivity instance = null;
    private WaittingDialog mDialog = null;
    private boolean mDialogAppear=false;//判断对话框要不要出现

    public ToRegisterActivity(){
        super(R.layout.activity_to_register);
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case DataConstants.PARSER_THIRD_LOGIN_CANCEL:
                    if (mDialog.isShowing()) {
                        mDialog.dismiss();
                    }
                    Toast.makeText(ToRegisterActivity.this, "取消授权", Toast.LENGTH_SHORT).show();
                    break;
                case DataConstants.PARSER_THIRD_LOGIN_ERROR:
                    if (mDialog.isShowing()) {
                        mDialog.dismiss();
                    }
                    Toast.makeText(ToRegisterActivity.this, "授权失败", Toast.LENGTH_SHORT).show();
                    break;
                case DataConstants.PARSER_THIRD_LOGIN:
                    if (msg.obj != null) {
                        if (msg.obj instanceof ThirdLogin) {
                            ThirdLogin thirdLogin = (ThirdLogin) msg.obj;
                            if (mDialog.isShowing()) {
                                mDialog.dismiss();
                            }
                            if ("true".equals(thirdLogin.getSuccess())) {
                                //为0不存在，1是存在，不存在表示第一次用这个三方号登录本APP，那就去绑定界面，存在则直接进入APP
                                if ("0".equals(thirdLogin.getHas_user())) {
                                    Intent intent = new Intent(ToRegisterActivity.this, BindPhoneActivity.class);
                                    intent.putExtra("oid", userId);
                                    intent.putExtra("oidForWeChat", openidForWeChat);
                                    intent.putExtra("token", token);
                                    intent.putExtra("avatarUrl", avatarUrl);
                                    intent.putExtra("nickName", nickName);
                                    intent.putExtra("type", type + "");
                                    intent.putExtra("sex", sex);
                                    startActivity(intent);
                                } else {
                                    switch (MainApplication.which_activity) {
                                        case DataConstants.ACTIVITY_TOPIC_COMMENTS:
                                            finish();
                                            break;
                                        case DataConstants.ACTIVITY_WEB:
                                            sendBroadcast(new Intent(DataConstants.BROAD_TOPIC_DETAILS));
                                            finish();
                                            break;
                                        case DataConstants.ACTIVITY_TRY_DETAILS_COMMENTS:
                                            finish();
                                            break;
                                        case DataConstants.ACTIVITY_COMMENTLISTS:
                                            finish();
                                            break;
                                        case DataConstants.ACTIVITY_SPECIAL_DETAILS:
                                            finish();
                                            break;
                                        case DataConstants.ACTIVITY_TRY_DETAILS:
                                            sendBroadcast(new Intent(DataConstants.BROAD_TRY_DETAILS));
                                            finish();
                                            break;
                                        case DataConstants.ACTIVITY_GOODS_DETAILS:
                                            sendBroadcast(new Intent(DataConstants.BROAD_GOODS_DETAILS));
                                            finish();
                                            break;
                                        default:
//                                            THNMainActivity.instance.finish();
                                            Intent intent = new Intent(ToRegisterActivity.this,
                                                    MainActivity.class);
                                            startActivity(intent);
                                            break;
                                    }
                                    MainApplication.getIsLoginInfo().setIs_login("1");
                                    mDialog.dismiss();
                                    if (OptRegisterLoginActivity.instance != null) {
                                        OptRegisterLoginActivity.instance.finish();
                                    }
                                    if (ToLoginActivity.instance != null) {
                                        ToLoginActivity.instance.finish();
                                    }
                                    mHandler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            ToRegisterActivity.this.finish();
                                        }
                                    }, 600);
                                }
                            } else {
                                Toast.makeText(ToRegisterActivity.this, "注册失败，请检查网络", Toast.LENGTH_SHORT).show();
                            }
                        }
                        break;
                    }
            }
        }
    };

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

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mDialogAppear) {
            if (!mDialog.isShowing()) {
                mDialog.show();
            }
        }
    }

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
            case R.id.tv_phone_number_toregister:
                startActivity(new Intent(activity, RegisterActivity.class));
                break;
            case R.id.tv_qq_register:
                mDialogAppear=true;
                if (!mDialog.isShowing()) {
                    mDialog.show();
                }
                type = 3;
                //QQ
                Platform qq = ShareSDK.getPlatform(QQ.NAME);
                authorize(qq);
                break;
            case R.id.tv_weibo_register:
                mDialogAppear=true;
                if (!mDialog.isShowing()) {
                    mDialog.show();
                }
                type = 2;
                //新浪微博
                Platform sina = ShareSDK.getPlatform(SinaWeibo.NAME);
                authorize(sina);
                break;
            case R.id.tv_weixin_register:
                mDialogAppear=true;
                if (!mDialog.isShowing()) {
                    mDialog.show();
                }
                type = 1;
                //微信登录
                //测试时，需要打包签名；sample测试时，用项目里面的demokey.keystore
                //打包签名apk,然后才能产生微信的登录
                Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
                authorize(wechat);
                break;
            case R.id.image_back_toregister:
                finish();
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
            if (type == 3) {
                //QQ的ID得这样获取，这是MOB公司的错，不是字段写错了
                userId = platform.getDb().get("weibo").toString();
            } else if (type == 1) {
                //微信这个神坑，我已无力吐槽，干嘛要搞两个ID出来，泥马，后台说要传的是这个ID，字段没有错！用platDB.getUserId()不行！
                userId = platform.getDb().get("unionid").toString();
            } else if (type == 2) {
                //除QQ和微信两特例，其他的ID这样取就行了
                userId = platDB.getUserId().toString();
            }
            DataPaser.thirdLoginParser(userId, token, type + "", mHandler);
        }
    }

    @Override
    public void onError(Platform platform, int i, Throwable throwable) {
        mDialogAppear=false;
        if (i == Platform.ACTION_USER_INFOR) {
            mHandler.sendEmptyMessage(DataConstants.PARSER_THIRD_LOGIN_ERROR);
        }
        throwable.printStackTrace();
    }

    @Override
    public void onCancel(Platform platform, int i) {
        mDialogAppear=false;
        if (i == Platform.ACTION_USER_INFOR) {
            mHandler.sendEmptyMessage(DataConstants.PARSER_THIRD_LOGIN_CANCEL);
        }
    }
}
