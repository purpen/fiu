package com.taihuoniao.fineix.user;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.beans.BindPhone;
import com.taihuoniao.fineix.beans.SkipBind;
import com.taihuoniao.fineix.main.MainActivity;
import com.taihuoniao.fineix.main.MainApplication;
import com.taihuoniao.fineix.network.DataConstants;
import com.taihuoniao.fineix.network.DataPaser;
import com.taihuoniao.fineix.utils.ActivityUtil;
import com.taihuoniao.fineix.view.WaittingDialog;

public class BindPhoneActivity extends BaseActivity implements View.OnClickListener{
    private EditText mPhone;
    private EditText mPassWord;
    private Button mBind;
    private TextView mLoginNow;
    public static BindPhoneActivity instance = null;
    private WaittingDialog mDialog=null;
    private String avatarUrl,nickName,sex,token;
    private String type;//来源: 1.微信；2.微博；3.ＱＱ
    private String mPhoneNumber,mPassWordNumber;
    private String unionId;//微信的联合id
    private String openIdWeChat;//微信专用的openId
    private String openId;
    public BindPhoneActivity(){
        super(R.layout.activity_bind_phone);
    }
    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case DataConstants.PARSER_THIRD_LOGIN_SKIP_PHONE:
                    if (msg.obj != null) {
                        if (msg.obj instanceof SkipBind) {
                            SkipBind skipBind = (SkipBind) msg.obj;
                            if ("true".equals(skipBind.getSuccess())) {
                                loginSuccess();
                            }else {
                                Toast.makeText(BindPhoneActivity.this, "登录失败，请检查网络", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                    break;
                case DataConstants.PARSER_THIRD_LOGIN_BIND_PHONE:
                    if (msg.obj != null) {
                        if (msg.obj instanceof BindPhone) {
                            BindPhone bindPhone= (BindPhone) msg.obj;
                            if ("true".equals(bindPhone.getSuccess())) {
                                loginSuccess();
                            }else {
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
//        setContentView(R.layout.activity_bind_phone);
        overridePendingTransition(R.anim.up_register, 0);
        ActivityUtil.getInstance().addActivity(this);
        instance=this;
//        iniView();
        initData();



    }

    @Override
    protected void initView() {
        mDialog = new WaittingDialog(this);
        mPhone = (EditText) findViewById(R.id.et_phone_bindPhone);
        mPassWord = (EditText) findViewById(R.id.et_password_bindPhone);
        mBind = (Button) findViewById(R.id.bt_bindPhone);
        mBind.setOnClickListener(this);
        mLoginNow = (TextView) findViewById(R.id.tv_click_login_bindPhone);
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
        openIdWeChat=getIntent().getStringExtra("oidForWeChat");//微信的openid
        openId = getIntent().getStringExtra("oid");
        //1.微信；2.微博；3.ＱＱ
        if ("1".equals(type)) {
            openId=openIdWeChat;
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_bindPhone:
                mPhoneNumber= mPhone.getText()+"";
                mPassWordNumber=mPassWord.getText()+"";
                DataPaser.bindPhoneParser(openId,unionId,token,mPhoneNumber,mPassWordNumber,type,mHandler);
                break;
            case R.id.tv_click_login_bindPhone:
                DataPaser.skipBindParser(MainApplication.uuid,openId,unionId,token,nickName,sex,avatarUrl,type,mHandler);
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
    private  void loginSuccess(){
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
//                THNMainActivity.instance.finish();
                Intent intent = new Intent(BindPhoneActivity.this,
                        MainActivity.class);
                startActivity(intent);
                break;
        }
        MainApplication.getIsLoginInfo().setIs_login("1");
        mDialog.dismiss();
        OptRegisterLoginActivity.instance.finish();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                BindPhoneActivity.this.finish();
            }
        }, 600);
    }
}
