package com.taihuoniao.fineix.user.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.beans.HttpResponse;
import com.taihuoniao.fineix.main.fragment.MyBaseFragment;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.user.ToRegisterActivity;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.view.MaskedEditText;

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

    public static SendCheckCodeFragment newInstance() {
        return new SendCheckCodeFragment();
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

    }

    @OnClick(R.id.bt_send_code)
    public void onClick() {
        String text = maskedEditText.getUnmaskedText();
        if (TextUtils.isEmpty(text)) return;
        submitCheckCode(text);
    }

    private void submitCheckCode(String text) {
        ClientDiscoverAPI.submitCheckCode(text, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                if (TextUtils.isEmpty(responseInfo.result)) return;
                HttpResponse response = JsonUtil.fromJson(responseInfo.result, HttpResponse.class);
                if (response.isSuccess()) {
                    if (activity instanceof ToRegisterActivity) {
                        ViewPager viewPager = ((ToRegisterActivity) activity).getViewPager();
                        if (null != viewPager) viewPager.setCurrentItem(2);
                    }
                    return;
                }
                ToastUtils.showError(response.getMessage());
            }

            @Override
            public void onFailure(HttpException e, String s) {
                ToastUtils.showError(R.string.network_err);
            }
        });
    }
}
