package com.taihuoniao.fineix.scene.fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.gson.reflect.TypeToken;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.ShareCJSelectListAdapter;
import com.taihuoniao.fineix.beans.HttpResponse;
import com.taihuoniao.fineix.common.GlobalDataCallBack;
import com.taihuoniao.fineix.base.HttpRequest;
import com.taihuoniao.fineix.beans.SearchBean;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.URL;
import com.taihuoniao.fineix.qingjingOrSceneDetails.fragment.SearchFragment;
import com.taihuoniao.fineix.scene.AddEnvirActivity;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.view.dialog.WaittingDialog;
import com.taihuoniao.fineix.view.pulltorefresh.PullToRefreshBase;
import com.taihuoniao.fineix.view.pulltorefresh.PullToRefreshListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;

/**
 * Created by taihuoniao on 2016/9/2.
 */
public class AddEnvirFragment extends SearchFragment implements AdapterView.OnItemClickListener {
    //语境分类id
    private String cid;
    @Bind(R.id.pull_refresh_view)
    PullToRefreshListView pullRefreshView;
    private ListView listView;
    @Bind(R.id.progress_bar)
    ProgressBar progressBar;
    private WaittingDialog dialog;
    private int page = 1;
    private List<SearchBean.SearchItem> list = new ArrayList<>();
    private ShareCJSelectListAdapter shareCJSelectListAdapter;

    public static AddEnvirFragment newInstance(String cid) {

        Bundle args = new Bundle();
        args.putString("cid", cid);
        AddEnvirFragment fragment = new AddEnvirFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected View initView() {
        cid = getArguments().getString("cid");
        View view = View.inflate(getActivity(), R.layout.fragment_add_envir, null);
        ButterKnife.bind(this, view);
        listView = pullRefreshView.getRefreshableView();
        listView.setDividerHeight(0);
        listView.setSelector(R.color.nothing);
        dialog = ((AddEnvirActivity) getActivity()).dialog;
        pullRefreshView.setPullToRefreshEnabled(false);
        pullRefreshView.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {
            @Override
            public void onLastItemVisible() {
                progressBar.setVisibility(View.VISIBLE);
                page++;
                envirList();
            }
        });
        shareCJSelectListAdapter = new ShareCJSelectListAdapter(getActivity(), list);
        listView.setAdapter(shareCJSelectListAdapter);
        listView.setOnItemClickListener(this);
        return view;
    }

    @Override
    protected void requestNet() {
        envirList();
    }

    //语境列表
    private void envirList() {
        HashMap<String, String> params = ClientDiscoverAPI.getenvirListRequestParams(page + "", 8 + "", 1 + "", cid, null);
        Call httpHandler = HttpRequest.post(params,  URL.SCENE_CONTEXT_GETLIST, new GlobalDataCallBack(){
            @Override
            public void onSuccess(String json) {
                dialog.dismiss();
                if (progressBar != null) {
                    progressBar.setVisibility(View.GONE);
                }
                HttpResponse<SearchBean> netSearch = JsonUtil.json2Bean(json, new TypeToken<HttpResponse<SearchBean>>() {});
                if (netSearch.isSuccess()) {
                    if (page == 1) {
                        list.clear();
                        if (pullRefreshView != null) {
                            pullRefreshView.lastSavedFirstVisibleItem = -1;
                            pullRefreshView.lastTotalItem = -1;
                        }
                    }
                    list.addAll(netSearch.getData().getRows());
                    shareCJSelectListAdapter.notifyDataSetChanged();
                } else {
                    ToastUtils.showError(netSearch.getMessage());
                }
            }

            @Override
            public void onFailure(String error) {
                dialog.dismiss();
                progressBar.setVisibility(View.GONE);
                ToastUtils.showError(R.string.net_fail);
            }
        });
        addNet(httpHandler);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        SearchBean.SearchItem searchItem = (SearchBean.SearchItem) listView.getAdapter().getItem(position);
        if (searchItem == null) {
            return;
        }
        ((AddEnvirActivity) getActivity()).title.setText(searchItem.getTitle());
        ((AddEnvirActivity) getActivity()).des.setText(searchItem.getDes());
    }
}
