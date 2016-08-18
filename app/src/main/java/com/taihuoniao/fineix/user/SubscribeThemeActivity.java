package com.taihuoniao.fineix.user;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.SubscribeThemeAdapter;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.beans.HttpResponse;
import com.taihuoniao.fineix.beans.ThemeQJ;
import com.taihuoniao.fineix.beans.ThemeQJData;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.LogUtil;
import com.taihuoniao.fineix.utils.ToastUtils;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @author lilin
 *         created at 2016/8/17 21:39
 */
public class SubscribeThemeActivity extends BaseActivity {
    @Bind(R.id.gv_theme)
    GridView gvTheme;
    private SubscribeThemeAdapter adapter;
    private ArrayList<ThemeQJ> mList;
    private ArrayList<String> subscribedIds;

    public SubscribeThemeActivity() {
        super(R.layout.activity_subscribe_theme);
    }

    @Override
    protected void getIntentData() {
        Intent intent = getIntent();
        if (intent.hasExtra(TAG)) {
            subscribedIds = intent.getStringArrayListExtra(TAG);
        }
    }

    @Override
    protected void initView() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
    }

    @Override
    protected void installListener() {
        gvTheme.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                final TextView textView = (TextView) view.findViewById(R.id.tv);
                final View view_bg = view.findViewById(R.id.view_bg);
                final ThemeQJ themeQJ = mList.get(position);
                if (themeQJ.isSubscribed) {
                    ClientDiscoverAPI.cancelSubscribe(themeQJ._id, new RequestCallBack<String>() {
                        @Override
                        public void onStart() {
                            super.onStart();
                            view.setEnabled(false);
                        }

                        @Override
                        public void onSuccess(ResponseInfo<String> responseInfo) {
                            view.setEnabled(true);
                            if (TextUtils.isEmpty(responseInfo.result)) return;
                            HttpResponse response = JsonUtil.fromJson(responseInfo.result, HttpResponse.class);
                            if (response.isSuccess()) {
                                themeQJ.isSubscribed = false;
                                adapter.notifyDataSetChanged();
                                if (subscribedIds == null) return;
                                if (subscribedIds.contains(themeQJ._id)) {
                                    subscribedIds.remove(themeQJ._id);
                                }
                                LogUtil.e(TAG, "已取消订阅");
                                return;
                            }
                            ToastUtils.showError(response.getMessage());
                        }

                        @Override
                        public void onFailure(HttpException e, String s) {
                            view.setEnabled(true);
                            e.printStackTrace();
                            ToastUtils.showError(R.string.network_err);
                        }
                    });

                } else {
                    ClientDiscoverAPI.subscribe(themeQJ._id, new RequestCallBack<String>() {
                        @Override
                        public void onStart() {
                            super.onStart();
                            view.setEnabled(false);
                        }

                        @Override
                        public void onSuccess(ResponseInfo<String> responseInfo) {
                            view.setEnabled(true);
                            if (TextUtils.isEmpty(responseInfo.result)) return;
                            HttpResponse response = JsonUtil.fromJson(responseInfo.result, HttpResponse.class);
                            if (response.isSuccess()) {
                                themeQJ.isSubscribed = true;
                                adapter.notifyDataSetChanged();
                                if (subscribedIds == null) return;
                                if (!subscribedIds.contains(themeQJ._id)) {
                                    subscribedIds.add(themeQJ._id);
                                }
                                LogUtil.e(TAG, "订阅成功");
                                return;
                            }
                            ToastUtils.showError(response.getMessage());
                        }

                        @Override
                        public void onFailure(HttpException e, String s) {
                            view.setEnabled(true);
                            e.printStackTrace();
                            ToastUtils.showError(R.string.network_err);
                        }
                    });
                }

//                ClientDiscoverAPI.subscribeTheme(themeQJ._id, new RequestCallBack<String>() {
//                    @Override
//                    public void onSuccess(ResponseInfo<String> responseInfo) {
//                        view.setEnabled(true);
//                        if (TextUtils.isEmpty(responseInfo.result)) return;
//                        HttpResponse response = JsonUtil.fromJson(responseInfo.result, HttpResponse.class);
//                        if (response.isSuccess()) {
//                            LogUtil.e(TAG, "订阅成功");
//                            return;
//                        }
//                        ToastUtils.showError(response.getMessage());
//                    }
//
//                    @Override
//                    public void onFailure(HttpException e, String s) {
//                        view.setEnabled(true);
//                        ToastUtils.showError(R.string.network_err);
//                    }
//                });
            }
        });
    }

    @Override
    protected void requestNet() {
        ClientDiscoverAPI.categoryList(new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                if (TextUtils.isEmpty(responseInfo.result)) return;
                HttpResponse<ThemeQJData> response = JsonUtil.json2Bean(responseInfo.result, new TypeToken<HttpResponse<ThemeQJData>>() {
                });
                if (response.isSuccess()) {
                    mList = response.getData().rows;
                    refreshUI();
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
    protected void refreshUI() {
        if (null == mList) return;
        if (mList.size() == 0) return;
        if (subscribedIds != null && subscribedIds.size() > 0) {
            for (ThemeQJ item : mList) {
                item.isSubscribed = subscribedIds.contains(item._id);
            }
        }
        adapter = new SubscribeThemeAdapter(mList, activity);
        gvTheme.setAdapter(adapter);
    }

    @OnClick(R.id.ibtn_back)
    public void onClick() {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        if (subscribedIds != null) {
            intent.putStringArrayListExtra(OrderQJActivity.class.getSimpleName(), subscribedIds);
        }
        setResult(RESULT_OK, intent);
        finish();
    }

}
