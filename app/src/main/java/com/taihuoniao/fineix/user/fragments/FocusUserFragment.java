package com.taihuoniao.fineix.user.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.FocusInterestedUserViewPagerAdapter;
import com.taihuoniao.fineix.base.GlobalDataCallBack;
import com.taihuoniao.fineix.base.HttpRequest;
import com.taihuoniao.fineix.beans.HttpResponse;
import com.taihuoniao.fineix.beans.InterestUserData;
import com.taihuoniao.fineix.beans.User;
import com.taihuoniao.fineix.beans.UserCompleteData;
import com.taihuoniao.fineix.main.MainActivity;
import com.taihuoniao.fineix.main.fragment.MyBaseFragment;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.URL;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.LogUtil;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.view.CustomViewPager;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author lilin
 *         created at 2016/8/10 17:24
 */
public class FocusUserFragment extends MyBaseFragment {
    @Bind(R.id.viewPager)
    CustomViewPager viewPager;
    @Bind(R.id.btn_next)
    Button btnNext;
    @Bind(R.id.ll)
    LinearLayout ll;
    private ArrayList<User> users;
    private ArrayList<ImageView> imageViews = new ArrayList<>();
    private int size;
    private int userSize;

    public static FocusUserFragment newInstance(UserCompleteData data) {
        FocusUserFragment fragment = new FocusUserFragment();
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
        super.setFragmentLayout(R.layout.fragment_foucus_user);
        super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void loadData() {
        RequestParams params = ClientDiscoverAPI.getfocusInterestUserRequestParams();
        HttpRequest.post(params, URL.USER_FIND_USER, new GlobalDataCallBack(){
//        ClientDiscoverAPI.focusInterestUser(new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo, String json) {
                if (TextUtils.isEmpty(json)) return;
                HttpResponse<InterestUserData> response = JsonUtil.json2Bean(json, new TypeToken<HttpResponse<InterestUserData>>() {
                });

                if (response.isSuccess()) {
                    users = response.getData().users;
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
    protected void installListener() {
        viewPager.addOnPageChangeListener(new CustomOnPageChangeListener());
    }

    private class CustomOnPageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageSelected(int position) {
            setCurFocus(position);
        }

        private void setCurFocus(int position) {
            for (int i = 0; i < size; i++) {
                if (i == position) {
                    imageViews.get(i).setImageResource(R.drawable.shape_oval_sel);
                } else {
                    imageViews.get(i).setImageResource(R.drawable.shape_oval_unsel);
                }
            }
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
    }

    public void setCurrentItem(int i) {
        viewPager.setCurrentItem(i);
    }


    @Override
    protected void refreshUIAfterNet() {
        if (users == null) return;
        userSize = users.size();
        if (userSize <= 6) {
            for (User user : users) {
                user.is_love = 1;
            }
        } else {
            for (int i = 0; i < 6; i++) {
                users.get(i).is_love = 1;
            }
        }
        FocusInterestedUserViewPagerAdapter adapter = new FocusInterestedUserViewPagerAdapter(users, activity);
        viewPager.setAdapter(adapter);
        showIndicators();
    }


    public void showIndicators() {
        if (users == null) return;
        imageViews = new ArrayList<>();
        LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        llp.setMargins(activity.getResources().getDimensionPixelSize(R.dimen.dp5), 0, 0, 0);
        ViewGroup.LayoutParams vlp = new ViewGroup.LayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        ImageView imageView;
        if (userSize <= 6) {
            size = 1;
        } else {
            if (userSize % 6 == 0) {
                size = userSize / 6;
            } else {
                size = userSize / 6 + 1;
            }
        }
        for (int i = 0; i < size; i++) {
            imageView = new ImageView(activity);
            imageView.setLayoutParams(vlp);
            if (i == 0) {
                imageView.setImageResource(R.drawable.shape_oval_sel);
            } else {
                imageView.setImageResource(R.drawable.shape_oval_unsel);
            }
            imageViews.add(imageView);
            ll.addView(imageView, llp);
        }
    }

    @OnClick(R.id.btn_next)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_next:
                new MyHandler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(activity, MainActivity.class));
                        activity.finish();
                    }
                }, 200);
                StringBuilder builder = new StringBuilder();
                for (User user : users) {
                    if (user.is_love == 1) builder.append(user._id).append(",");
                }
                if (TextUtils.isEmpty(builder)) return;
                LogUtil.e(TAG, builder.deleteCharAt(builder.length() - 1).toString());
                RequestParams params = ClientDiscoverAPI.getfocusUsersRequestParams(builder.deleteCharAt(builder.length() - 1).toString());
                HttpRequest.post(params,URL.FOLLOW_BATCH_FOLLOW, new GlobalDataCallBack(){
//                ClientDiscoverAPI.focusUsers(builder.deleteCharAt(builder.length() - 1).toString(), new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo, String json) {
                        if (TextUtils.isEmpty(json)) return;
                        HttpResponse response = JsonUtil.fromJson(json, HttpResponse.class);
                        if (response.isSuccess()) {
                            LogUtil.e(TAG, "关注成功");
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
                break;
        }
    }

    private static class MyHandler extends Handler {
    }
}
