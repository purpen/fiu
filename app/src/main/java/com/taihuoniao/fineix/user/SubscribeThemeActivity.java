package com.taihuoniao.fineix.user;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.SubscribeThemeAdapter;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.common.GlobalDataCallBack;
import com.taihuoniao.fineix.base.HttpRequest;
import com.taihuoniao.fineix.beans.HttpResponse;
import com.taihuoniao.fineix.beans.ThemeQJ;
import com.taihuoniao.fineix.beans.ThemeQJData;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.URL;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.LogUtil;
import com.taihuoniao.fineix.utils.ToastUtils;

import java.util.ArrayList;
import java.util.HashMap;

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
                    HashMap<String, String> requestParams = ClientDiscoverAPI.getcancelSubscribeRequestParams(themeQJ._id);
                    HttpRequest.post(requestParams, URL.MY_REMOVE_INTEREST_SCENE_ID, new GlobalDataCallBack(){
                        @Override
                        public void onStart() {
                            super.onStart();
                            view.setEnabled(false);
                        }

                        @Override
                        public void onSuccess(String json) {
                            view.setEnabled(true);
                            if (TextUtils.isEmpty(json)) return;
                            HttpResponse response = JsonUtil.fromJson(json, HttpResponse.class);
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
                        public void onFailure(String error) {
                            view.setEnabled(true);
                            ToastUtils.showError(R.string.network_err);
                        }
                    });

                } else {
                    HashMap<String, String> requestParams = ClientDiscoverAPI.getsubscribeRequestParams(themeQJ._id);
                    HttpRequest.post(requestParams, URL.MY_ADD_INTEREST_SCENE_ID, new GlobalDataCallBack(){
                        @Override
                        public void onStart() {
                            super.onStart();
                            view.setEnabled(false);
                        }

                        @Override
                        public void onSuccess(String json) {
                            view.setEnabled(true);
                            if (TextUtils.isEmpty(json)) return;
                            HttpResponse response = JsonUtil.fromJson(json, HttpResponse.class);
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
                        public void onFailure(String error) {
                            view.setEnabled(true);
                            ToastUtils.showError(R.string.network_err);
                        }
                    });
                }
            }
        });
    }

    @Override
    protected void requestNet() {
        HashMap<String, String> params = ClientDiscoverAPI.getcategoryListRequestParams();
        HttpRequest.post(params, URL.CATEGORY_LIST, new GlobalDataCallBack(){
            @Override
            public void onSuccess(String json) {
                if (TextUtils.isEmpty(json)) return;
                HttpResponse<ThemeQJData> response = JsonUtil.json2Bean(json, new TypeToken<HttpResponse<ThemeQJData>>() {
                });
                if (response.isSuccess()) {
                    mList = response.getData().rows;
                    refreshUI();
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
