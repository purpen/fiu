package com.taihuoniao.fineix.user.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.http.RequestParams;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.base.GlobalDataCallBack;
import com.taihuoniao.fineix.base.HttpRequest;
import com.taihuoniao.fineix.beans.HttpResponse;
import com.taihuoniao.fineix.beans.LoginInfo;
import com.taihuoniao.fineix.beans.RegisterInfo;
import com.taihuoniao.fineix.main.MainActivity;
import com.taihuoniao.fineix.main.fragment.MyBaseFragment;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.DataConstants;
import com.taihuoniao.fineix.network.URL;
import com.taihuoniao.fineix.user.CompleteUserInfoActivity;
import com.taihuoniao.fineix.user.OptRegisterLoginActivity;
import com.taihuoniao.fineix.user.ToLoginActivity;
import com.taihuoniao.fineix.user.ToRegisterActivity;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.LogUtil;
import com.taihuoniao.fineix.utils.SPUtil;
import com.taihuoniao.fineix.utils.ToastUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author lilin
 *         created at 2016/8/10 17:24
 */
public class SetPasswordFragment extends MyBaseFragment {
    @Bind(R.id.et_password)
    EditText etPassword;
    @Bind(R.id.ibtn_show_hide)
    ImageButton ibtnShowHide;
    @Bind(R.id.bt_complete_register)
    Button btCompleteRegister;
    private boolean flag = false;
    private RegisterInfo registerInfo = null;

    public static SetPasswordFragment newInstance() {
        return new SetPasswordFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.setFragmentLayout(R.layout.fragment_set_password);
        super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void initViews() {
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        if (activity instanceof ToRegisterActivity) {
            ToRegisterActivity registerActivity = (ToRegisterActivity) this.activity;
            registerInfo = registerActivity.getRegisterInfo();
        }
    }

    @OnClick({R.id.bt_complete_register, R.id.ibtn_show_hide})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_complete_register:
                String password = etPassword.getText().toString().trim();
                if (TextUtils.isEmpty(password)) {
                    ToastUtils.showInfo("请输入密码");
                    return;
                }

                if (password.length() < 6) {
                    ToastUtils.showInfo("密码长度为6位以上");
                    return;
                }
                registerUser(password);
                break;
            case R.id.ibtn_show_hide:
                if (flag) {
                    flag = false;
                    etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    ibtnShowHide.setImageResource(R.mipmap.pass_hide);
                } else {
                    flag = true;
                    etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    ibtnShowHide.setImageResource(R.mipmap.pass_show);
                }
                int length = etPassword.getText().length();
                if (length > 0) etPassword.setSelection(length);
                break;
        }

    }

    private void registerUser(String password) {//注册完成
        if (registerInfo == null) return;
        RequestParams params = ClientDiscoverAPI.getregisterUserRequestParams(registerInfo.mobile, password, registerInfo.verify_code);
        HttpRequest.post(params, URL.AUTH_REGISTER, new GlobalDataCallBack(){
//        ClientDiscoverAPI.registerUser(registerInfo.mobile, password, registerInfo.verify_code, new RequestCallBack<String>() {
            @Override
            public void onSuccess(String json) {
                if (TextUtils.isEmpty(json)) return;
                HttpResponse<LoginInfo> response = JsonUtil.json2Bean(json, new TypeToken<HttpResponse<LoginInfo>>() {
                });
                if (response.isSuccess()) {
                    if (response.isSuccess()) {
                        LoginInfo loginInfo = response.getData();
                        SPUtil.write(DataConstants.LOGIN_INFO, JsonUtil.toJson(loginInfo));
                        if (loginInfo.identify.is_scene_subscribe == 0) { // 未订阅
                            updateUserIdentity();
                            startActivity(new Intent(activity, CompleteUserInfoActivity.class));
                        } else {
                            startActivity(new Intent(activity, MainActivity.class));
                        }

                        if (OptRegisterLoginActivity.instance != null) {
                            OptRegisterLoginActivity.instance.finish();
                        }
                        if (ToLoginActivity.instance != null) {
                            ToLoginActivity.instance.finish();
                        }
                        if (ToRegisterActivity.instance != null) {
                            ToRegisterActivity.instance.finish();
                        }
                        activity.finish();
                        return;
                    }
                    return;
                }
                ToastUtils.showError(response.getMessage());
            }

            @Override
            public void onFailure(String error) {
                ToastUtils.showError(R.string.network_err);
            }
        });
    }

    private void updateUserIdentity() {
        String type = "1";//设置非首次登录
        RequestParams params = ClientDiscoverAPI.getupdateUserIdentifyRequestParams(type);
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
