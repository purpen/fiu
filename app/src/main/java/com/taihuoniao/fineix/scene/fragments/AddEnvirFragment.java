package com.taihuoniao.fineix.scene.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.ShareCJSelectListAdapter;
import com.taihuoniao.fineix.beans.SearchBean;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.qingjingOrSceneDetails.fragment.SearchFragment;
import com.taihuoniao.fineix.scene.AddEnvirActivity;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.view.WaittingDialog;
import com.taihuoniao.fineix.view.pulltorefresh.PullToRefreshBase;
import com.taihuoniao.fineix.view.pulltorefresh.PullToRefreshListView;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

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
    private List<SearchBean.Data.SearchItem> list = new ArrayList<>();
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
        ClientDiscoverAPI.envirList(page + "", 8 + "", 1 + "", cid, null, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                dialog.dismiss();
                if (progressBar != null) {
                    progressBar.setVisibility(View.GONE);
                }
                SearchBean netSearch = new SearchBean();
                try {
                    Gson gson = new Gson();
                    Type type = new TypeToken<SearchBean>() {
                    }.getType();
                    netSearch = gson.fromJson(responseInfo.result, type);
                } catch (JsonSyntaxException e) {
                    Log.e("<<<", "数据解析异常" + e.toString());
                }
                if (netSearch.isSuccess()) {
                    if (page == 1) {
                        list.clear();
                        pullRefreshView.lastSavedFirstVisibleItem = -1;
                        pullRefreshView.lastTotalItem = -1;
                    }
                    list.addAll(netSearch.getData().getRows());
                    shareCJSelectListAdapter.notifyDataSetChanged();
                } else {
                    ToastUtils.showError(netSearch.getMessage());
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                dialog.dismiss();
                progressBar.setVisibility(View.GONE);
                ToastUtils.showError(R.string.net_fail);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        SearchBean.Data.SearchItem searchItem = (SearchBean.Data.SearchItem) listView.getAdapter().getItem(position);
        if (searchItem == null) {
            return;
        }
        ((AddEnvirActivity) getActivity()).title.setText(searchItem.getTitle());
        ((AddEnvirActivity) getActivity()).des.setText(searchItem.getDes());
    }
}
