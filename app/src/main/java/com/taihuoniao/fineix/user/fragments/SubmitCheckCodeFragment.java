package com.taihuoniao.fineix.user.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.common.GlobalDataCallBack;
import com.taihuoniao.fineix.base.HttpRequest;
import com.taihuoniao.fineix.beans.HttpResponse;
import com.taihuoniao.fineix.beans.RegisterInfo;
import com.taihuoniao.fineix.main.fragment.MyBaseFragment;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.URL;
import com.taihuoniao.fineix.user.ToRegisterActivity;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.view.MaskedEditText;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author lilin
 *         created at 2016/8/10 17:24
 */
public class SubmitCheckCodeFragment extends MyBaseFragment {
    @Bind(R.id.masked_edit_text)
    MaskedEditText maskedEditText;
    @Bind(R.id.bt_send_code)
    Button btSendCode;
    private RegisterInfo registerInfo = null;

    public static SubmitCheckCodeFragment newInstance() {
        return new SubmitCheckCodeFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.setFragmentLayout(R.layout.fragment_submit_check_code);
        super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void initViews() {
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }

    @OnClick(R.id.bt_send_code)
    public void onClick() {
        String text = maskedEditText.getUnmaskedText();
        if (TextUtils.isEmpty(text)) return;
        submitCheckCode(text);
    }

    private void submitCheckCode(final String text) {
        if (activity instanceof ToRegisterActivity) {
            ToRegisterActivity registerActivity = (ToRegisterActivity) this.activity;
            registerInfo = registerActivity.getRegisterInfo();
        }

        if (registerInfo == null) return;
        String phone = registerInfo.mobile;
        if (TextUtils.isEmpty(phone)) return;
        HashMap<String, String> params = ClientDiscoverAPI.getsubmitCheckCodeRequestParams(phone, text);
        HttpRequest.post(params, URL.AUTH_CHECK_VERIFY_CODE , new GlobalDataCallBack(){
//        ClientDiscoverAPI.submitCheckCode(phone, text, new RequestCallBack<String>() {
            @Override
            public void onSuccess(String json) {
                if (TextUtils.isEmpty(json)) return;
                HttpResponse response = JsonUtil.fromJson(json, HttpResponse.class);
                if (response.isSuccess()) {
                    registerInfo.verify_code = text;
                    if (activity instanceof ToRegisterActivity) {
                        ViewPager viewPager = ((ToRegisterActivity) activity).getViewPager();
                        if (null != viewPager) viewPager.setCurrentItem(2);
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
}
