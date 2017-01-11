package com.taihuoniao.fineix.user.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.ActivityAdapter;
import com.taihuoniao.fineix.base.GlobalDataCallBack;
import com.taihuoniao.fineix.base.HttpRequest;
import com.taihuoniao.fineix.beans.DataChooseSubject;
import com.taihuoniao.fineix.beans.HttpResponse;
import com.taihuoniao.fineix.main.fragment.MyBaseFragment;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.NetworkManager;
import com.taihuoniao.fineix.network.URL;
import com.taihuoniao.fineix.user.ActivityDetailActivity;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.MD5Utils;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.view.dialog.WaittingDialog;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author lilin
 *         created at 2016/8/10 17:24
 */
public class ActivityFragment extends MyBaseFragment {
    public static final String PAGE_TYPE = "2"; //0.全部；1.文章；2.活动；3.促销；4.新品；5.--
    public static final String FINE = "0"; //0.全部；-1.否；1.是
    public static final String SORT = "0";
    public int curPage = 1;
    @Bind(R.id.pull_lv)
    PullToRefreshListView pullLv;
    private ArrayList<DataChooseSubject.ItemChoosenSubject> mList;
    private ActivityAdapter adapter;
    private WaittingDialog dialog;
    private boolean isLoadMore = false;
    public static ActivityFragment newInstance() {
        return new ActivityFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.setFragmentLayout(R.layout.fragment_article);
        super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void initViews() {
        pullLv.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        mList = new ArrayList<>();
        dialog = new WaittingDialog(activity);
    }

    @Override
    protected void installListener() {
        pullLv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                isLoadMore = true;
                curPage = 1;
                mList.clear();
                loadData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

            }
        });

        pullLv.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {
            @Override
            public void onLastItemVisible() {
                isLoadMore = true;
                loadData();
            }
        });

        pullLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mList != null && mList.size() > 0) {
                    DataChooseSubject.ItemChoosenSubject item = mList.get(position - 1);
                    Intent intent = new Intent(activity, ActivityDetailActivity.class);
                    intent.putExtra(ActivityDetailActivity.class.getSimpleName(), item._id);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    protected void loadData() {
        HashMap<String, String> params = ClientDiscoverAPI.getChoosenSubjectRequestParams(String.valueOf(curPage), PAGE_TYPE, FINE, SORT);
        HttpRequest.post(params, URL.CHOOSEN_SUBJECT_URL, new GlobalDataCallBack(){
//        ClientDiscoverAPI.getChoosenSubject(String.valueOf(curPage), PAGE_TYPE, FINE, SORT, new RequestCallBack<String>() {
            @Override
            public void onStart() {
                if (!isLoadMore && dialog != null) {
                    dialog.show();
                }
            }

            @Override
            public void onSuccess(String json) {
                if (dialog != null) dialog.dismiss();
                if (TextUtils.isEmpty(json)) return;

                HttpResponse<DataChooseSubject> response = JsonUtil.json2Bean(json, new TypeToken<HttpResponse<DataChooseSubject>>() {
                });

                if (response.isSuccess()) {
                    pullLv.onRefreshComplete();
                    ArrayList<DataChooseSubject.ItemChoosenSubject> list = response.getData().rows;
                    refreshUIAfterNet(list);
                    return;
                }
                ToastUtils.showError(response.getMessage());
            }

            @Override
            public void onFailure(String error) {
                if (dialog != null) dialog.dismiss();
                ToastUtils.showError(R.string.network_err);
            }
        });
    }

    @Override
    protected void refreshUIAfterNet(ArrayList list) {
        if (list == null || list.size() == 0) return;
        curPage++;
        mList.addAll(list);
        if (adapter == null) {
            adapter = new ActivityAdapter(mList, activity);
            pullLv.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onDestroy() {
        NetworkManager.getInstance().cancel(MD5Utils.getMD5(URL.CHOOSEN_SUBJECT_URL));
        super.onDestroy();
    }
}
