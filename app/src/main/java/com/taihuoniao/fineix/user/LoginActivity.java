package com.taihuoniao.fineix.user;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.beans.LoginInfo;
import com.taihuoniao.fineix.main.MainActivity;
import com.taihuoniao.fineix.main.MainApplication;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.DataConstants;
import com.taihuoniao.fineix.network.DataPaser;
import com.taihuoniao.fineix.network.HttpResponse;
import com.taihuoniao.fineix.utils.ActivityUtil;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.LogUtil;
import com.taihuoniao.fineix.utils.SPUtil;
import com.taihuoniao.fineix.utils.Util;
import com.taihuoniao.fineix.view.WaittingDialog;

public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private TextView findPassword;
    private Button login;
    private EditText et_password;
    private EditText phoneNum;
    private ImageView mClose;
    private ImageView mBack;
    public static LoginActivity instance = null;
    private WaittingDialog mDialog = null;

    public LoginActivity() {
        super(R.layout.activity_login);
    }
//    private TextWatcher textWatcher = new TextWatcher() {
//        @Override
//        public void onTextChanged(CharSequence s, int start, int before, int count) {
//            if (phoneNum.getText().toString().length() > 0
//                    && et_password.getText().toString().length() > 0) {
//                login.setEnabled(true);
//            } else {
//
//                login.setEnabled(false);
//            }
//        }
//
//        @Override
//        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//        }
//
//        @Override
//        public void afterTextChanged(Editable s) {
//        }
//    };

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case DataConstants.PARSER_LOGIN:
                    int isFirstLogin = 1;
                    // 登录成功
                    if (msg.obj instanceof LoginInfo) {
                        LoginInfo loginInfo = (LoginInfo) msg.obj;
                        if ("true".equals(loginInfo.getSuccess())) {
//                            if (isFirstLogin==loginInfo.getFirstLogin()) {
//                            Toast.makeText(LoginActivity.this, loginInfo.getMessage(), Toast.LENGTH_SHORT).show();
//                            Log.e(">>>", ">>>>>which_activityllll>>>" + MainApplication.which_activity);
//                            Log.e(">>>", ">>>>>forPaywayToMainlllll>>>" + THNApplication.forPaywayToMain);
                            switch (MainApplication.which_activity) {
                                case DataConstants.QingjingDetailActivity:
                                    sendBroadcast(new Intent(DataConstants.BroadQingjingDetail));
                                    break;
                                case DataConstants.SceneDetailActivity:
                                    sendBroadcast(new Intent(DataConstants.BroadSceneDetail));
                                    break;
                                case DataConstants.ElseActivity:
                                    //其他不需要刷新界面的activity
                                    break;
                                default:
//                                    THNMainActivity.instance.finish();
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    break;
                            }
                            MainApplication.getIsLoginInfo().setIs_login("1");
                            mDialog.dismiss();
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
                            mHandler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    finish();
                                }
                            }, 600);


//                            }else {
//                                Context context = THNAppl                            ication.getContext();
//                                SharedPreferences sp = context.getSharedPreferences(DataConstants.USERDATA_SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE);
//                                //获取保存文件中的用户名和密码
//                                String savedUsername = sp.getString(THNApplication.THN_MOBILE,"");
//                                String savedPassword = sp.getString(THNApplication.THN_PASSWORD, "");
//
//                                phoneNum.setText(savedUsername);
//                                et_password.setText(savedPassword);
//                            }

                        } else {
                            Toast.makeText(LoginActivity.this, loginInfo.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
                case DataConstants.NETWORK_FAILURE:
                    Toast.makeText(LoginActivity.this, "连接失败，请检查网络", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    protected void initView() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        overridePendingTransition(R.anim.up_register, 0);
        instance = this;
        ActivityUtil.getInstance().addActivity(this);
        mBack = (ImageView) findViewById(R.id.image_back_login);
        mBack.setOnClickListener(this);
        mClose = (ImageView) findViewById(R.id.image_close_login);
        mClose.setOnClickListener(this);
        login = (Button) findViewById(R.id.bt_login);
//        login.setEnabled(false);
        login.setOnClickListener(this);
        et_password = (EditText) findViewById(R.id.et_password);
        et_password.setOnClickListener(this);
//        et_password.setText("123456");
        phoneNum = (EditText) findViewById(R.id.et_phone);
//        phoneNum.setText("18310080981");
        phoneNum.setOnClickListener(this);

//        phoneNum.addTextChangedListener(textWatcher);
//        et_password.addTextChangedListener(textWatcher);
        mDialog = new WaittingDialog(this);
        findPassword = (TextView) findViewById(R.id.tv_findpassword);
        findPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentFindPassword = new Intent(LoginActivity.this,
                        FindPasswordActivity.class);
                startActivity(intentFindPassword);
            }
        });
    }

    @Override
    public void finish() {
        super.finish();
        //关闭窗体动画显示
        overridePendingTransition(0, R.anim.down_register);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_login:
//                String phone = phoneNum.getText().toString();
//                String password = et_password.getText().toString();
                submitData(v);

//                if (!TextUtils.isEmpty(phone)) {
//                    if (!TextUtils.isEmpty(password)) {
//                        mDialog.show();
//                        DataPaser.loginParser(MainApplication.uuid, mHandler, phone, password);
//                    } else {
//                        Toast.makeText(LoginActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
//                    }
//                } else {
//                    Toast.makeText(LoginActivity.this, "请输入用户名", Toast.LENGTH_SHORT).show();
//                }
                break;
            case R.id.image_close_login:
                OptRegisterLoginActivity.instance.finish();
                ToLoginActivity.instance.finish();
                finish();
                break;
            case R.id.image_back_login:
                finish();
                break;
        }
    }


    private void submitData(final View v) {
        String phone = phoneNum.getText().toString().trim();
        String password = et_password.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            Util.makeToast("请输入手机号");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Util.makeToast("请输入密码");
            return;
        }

        ClientDiscoverAPI.clickLoginNet(phone, password, new RequestCallBack<String>() {
            @Override
            public void onStart() {
                v.setEnabled(false);
                if (mDialog != null) mDialog.show();
                super.onStart();
            }

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                v.setEnabled(true);
                mDialog.dismiss();
                if (responseInfo == null) return;
                if (TextUtils.isEmpty(responseInfo.result)) return;
                HttpResponse<LoginInfo> response = JsonUtil.json2Bean(responseInfo.result, new TypeToken<HttpResponse<LoginInfo>>() {
                });
                LogUtil.e("LOGIN_INFO", responseInfo.result);
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
                Util.makeToast(response.getMessage());
            }

            @Override
            public void onFailure(HttpException e, String s) {
                v.setEnabled(true);
                mDialog.dismiss();
                Util.makeToast("对不起，网络异常");
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
    public void onBackPressed() {
        super.onBackPressed();
    }
}
