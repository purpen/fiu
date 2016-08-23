package com.taihuoniao.fineix.user.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.SubscribeThemeAdapter;
import com.taihuoniao.fineix.beans.HttpResponse;
import com.taihuoniao.fineix.beans.ThemeQJ;
import com.taihuoniao.fineix.beans.ThemeQJData;
import com.taihuoniao.fineix.beans.UserCompleteData;
import com.taihuoniao.fineix.main.fragment.MyBaseFragment;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.user.CompleteUserInfoActivity;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.LogUtil;
import com.taihuoniao.fineix.utils.ToastUtils;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author lilin
 *         created at 2016/8/10 17:24
 */
public class SubscribeThemeFragment extends MyBaseFragment {
    @Bind(R.id.gv_theme)
    GridView gvTheme;
    @Bind(R.id.btn_subscribe)
    Button btnSubscribe;
    @Bind(R.id.btn_next)
    Button btnNext;
    private SubscribeThemeAdapter adapter;
    private UserCompleteData data;
    private ArrayList<ThemeQJ> mList;

    public static SubscribeThemeFragment newInstance(UserCompleteData data) {
        SubscribeThemeFragment fragment = new SubscribeThemeFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("data", data);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            data = savedInstanceState.getParcelable("data");
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.setFragmentLayout(R.layout.fragment_subscribe_theme);
        super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void initViews() {
    }

    @Override
    protected void installListener() {
        gvTheme.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView textView = (TextView) view.findViewById(R.id.tv);
                View view_bg = view.findViewById(R.id.view_bg);
                ThemeQJ themeQJ = mList.get(position);
                if (themeQJ.stick == 1) {
                    themeQJ.stick = 0;
                    textView.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.circle_add, 0, 0, 0);
                    view_bg.setVisibility(View.GONE);
                } else {
                    themeQJ.stick = 1;
                    textView.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.subscribed, 0, 0, 0);
                    view_bg.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    protected void loadData() {
        ClientDiscoverAPI.categoryList(new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                if (TextUtils.isEmpty(responseInfo.result)) return;
                HttpResponse<ThemeQJData> response = JsonUtil.json2Bean(responseInfo.result, new TypeToken<HttpResponse<ThemeQJData>>() {
                });
                if (response.isSuccess()) {
                    mList = response.getData().rows;
                    refreshUIAfterNet();
                    return;
                }
                ToastUtils.showError(response.getMessage());
            }

            @Override
            public void onFailure(HttpException e, String s) {
                e.printStackTrace();
                ToastUtils.showError(R.string.network_err);
            }
        });
    }

    @Override
    protected void refreshUIAfterNet() {
        if (null == mList) return;
        if (mList.size() == 0) return;
        adapter = new SubscribeThemeAdapter(mList, activity);
        gvTheme.setAdapter(adapter);
    }

    @OnClick({R.id.btn_subscribe, R.id.btn_next})
    void onClick(final View v) {
        switch (v.getId()) {
            case R.id.btn_subscribe:
                for (ThemeQJ item : mList) {
                    item.stick = 1;
                }
                adapter.notifyDataSetChanged();
                break;
            case R.id.btn_next:
                if (activity instanceof CompleteUserInfoActivity) {
                    ViewPager viewPager = ((CompleteUserInfoActivity) activity).getViewPager();
                    if (null != viewPager) viewPager.setCurrentItem(3);
                }
                StringBuilder builder = new StringBuilder();
                for (ThemeQJ item : mList) {
                    if (item.stick == 1) {
                        builder.append(item._id + ",");
                    }
                }
                if (TextUtils.isEmpty(builder)) return;
                v.setEnabled(false);
                ClientDiscoverAPI.subscribeTheme(builder.deleteCharAt(builder.length() - 1).toString(), new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        v.setEnabled(true);
                        if (TextUtils.isEmpty(responseInfo.result)) return;
                        HttpResponse response = JsonUtil.fromJson(responseInfo.result, HttpResponse.class);
                        if (response.isSuccess()) {
                            LogUtil.e(TAG, "订阅成功");
                            return;
                        }
                        ToastUtils.showError(response.getMessage());
                    }

                    @Override
                    public void onFailure(HttpException e, String s) {
                        v.setEnabled(true);
                        ToastUtils.showError(R.string.network_err);
                    }
                });
                break;
        }
    }
}