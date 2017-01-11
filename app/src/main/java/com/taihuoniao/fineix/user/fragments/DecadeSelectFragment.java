package com.taihuoniao.fineix.user.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.base.GlobalDataCallBack;
import com.taihuoniao.fineix.base.HttpRequest;
import com.taihuoniao.fineix.beans.HttpResponse;
import com.taihuoniao.fineix.beans.User;
import com.taihuoniao.fineix.beans.UserCompleteData;
import com.taihuoniao.fineix.main.fragment.MyBaseFragment;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.URL;
import com.taihuoniao.fineix.user.CompleteUserInfoActivity;
import com.taihuoniao.fineix.utils.Constants;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.LogUtil;
import com.taihuoniao.fineix.utils.ToastUtils;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author lilin
 *         created at 2016/8/10 17:24
 */
public class DecadeSelectFragment extends MyBaseFragment {
    @Bind(R.id.rg_decade)
    RadioGroup rgDecade;

    @Bind(R.id.rg_consume)
    RadioGroup rgConsume;
    private String age_group = Constants.DECADE_00;
    private String assets = Constants.YG;

    public static DecadeSelectFragment newInstance(UserCompleteData data) {
        DecadeSelectFragment fragment = new DecadeSelectFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("data", data);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            UserCompleteData data = savedInstanceState.getParcelable("data");
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.setFragmentLayout(R.layout.fragment_decade_select);
        super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void installListener() {
        rgDecade.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_00:
                        group.check(checkedId);
                        age_group = Constants.DECADE_00;
                        break;
                    case R.id.rb_90:
                        group.check(checkedId);
                        age_group = Constants.DECADE_90;
                        break;
                    case R.id.rb_80:
                        group.check(checkedId);
                        age_group = Constants.DECADE_80;
                        break;
                    case R.id.rb_70:
                        group.check(checkedId);
                        age_group = Constants.DECADE_70;
                        break;
                    case R.id.rb_60:
                        group.check(checkedId);
                        age_group = Constants.DECADE_60;
                        break;
                }
            }
        });

        rgConsume.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_yg:
                        group.check(checkedId);
                        assets = Constants.YG;
                        break;
                    case R.id.rb_xz:
                        group.check(checkedId);
                        assets = Constants.XZ;
                        break;
                    case R.id.rb_zc:
                        group.check(checkedId);
                        assets = Constants.XZC;
                        break;
                    case R.id.rb_th:
                        group.check(checkedId);
                        assets = Constants.TH;
                        break;
                    case R.id.rb_dh:
                        group.check(checkedId);
                        assets = Constants.DH;
                        break;
                }
            }
        });
    }

    @OnClick({R.id.btn_next})
    void onClick(final View v) {
        switch (v.getId()) {
            case R.id.btn_next:
                v.setEnabled(false);
                HashMap<String, String> params =ClientDiscoverAPI. getupdateAgeAssetsRequestParams(age_group, assets);
                HttpRequest.post(params,  URL.UPDATE_USERINFO_URL, new GlobalDataCallBack(){
//                ClientDiscoverAPI.updateAgeAssets(age_group, assets, new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(String json) {
                        v.setEnabled(true);
                        if (TextUtils.isEmpty(json)) return;
                        HttpResponse<User> response = JsonUtil.fromJson(json, HttpResponse.class);
                        if (response.isSuccess()) {
//                            ToastUtils.showSuccess("更新成功");
                            LogUtil.e(TAG, "更新成功");
                            return;
                        }
                        ToastUtils.showError(response.getMessage());
                    }

                    @Override
                    public void onFailure(String error) {
                        v.setEnabled(true);
                        ToastUtils.showError(R.string.network_err);
                    }
                });
                if (activity instanceof CompleteUserInfoActivity) {
                    ViewPager viewPager = ((CompleteUserInfoActivity) activity).getViewPager();
                    if (null != viewPager) viewPager.setCurrentItem(2);
                }
                break;
        }
    }
}
