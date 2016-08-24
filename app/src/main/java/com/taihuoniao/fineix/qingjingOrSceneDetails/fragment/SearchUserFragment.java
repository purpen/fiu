package com.taihuoniao.fineix.qingjingOrSceneDetails.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.SearchQJAdapter;
import com.taihuoniao.fineix.beans.SearchBean;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.utils.DensityUtils;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.view.WaittingDialog;
import com.taihuoniao.fineix.view.pulltorefresh.PullToRefreshBase;
import com.taihuoniao.fineix.view.pulltorefresh.PullToRefreshGridView;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by taihuoniao on 2016/8/23.
 */
public class SearchUserFragment extends SearchFragment {
    private String q;
    private boolean isContent;
    @Bind(R.id.pull_refresh_view)
    PullToRefreshGridView pullRefreshView;
    @Bind(R.id.empty_view)
    TextView emptyView;
    @Bind(R.id.progress_bar)
    ProgressBar progressBar;
    private GridView gridView;
    private WaittingDialog dialog;
    private int page = 1;
    private List<SearchBean.Data.SearchItem> searchList;
    private SearchQJAdapter searchQJAdapter;

    public static SearchUserFragment newInstance(String q, boolean isContent) {
        Bundle args = new Bundle();
        args.putString("q", q);
        args.putBoolean("isContent", isContent);
        SearchUserFragment fragment = new SearchUserFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        q = getArguments().getString("q", null);
        isContent = getArguments().getBoolean("isContent");
    }

    @Override
    protected View initView() {
        View view = View.inflate(getActivity(), R.layout.fragment_search_qj, null);
        ButterKnife.bind(this, view);
        gridView = pullRefreshView.getRefreshableView();
        dialog = new WaittingDialog(getActivity());
        return view;
    }

    @Override
    protected void initList() {
        pullRefreshView.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {
            @Override
            public void onLastItemVisible() {
                page++;
                progressBar.setVisibility(View.VISIBLE);
                search();
            }
        });
        gridView.setNumColumns(2);
        gridView.setHorizontalSpacing(DensityUtils.dp2px(getActivity(), 15));
        gridView.setVerticalSpacing(DensityUtils.dp2px(getActivity(), 15));
        searchList = new ArrayList<>();
        searchQJAdapter = new SearchQJAdapter(getActivity(), searchList);
        gridView.setAdapter(searchQJAdapter);
    }

    @Override
    protected void requestNet() {
        if (TextUtils.isEmpty(q)) {
            return;
        }
        if (!dialog.isShowing()) {
            dialog.show();
        }
        search();
    }

    public void refreshData(String q) {
        if(TextUtils.isEmpty(q)||TextUtils.equals(this.q,q)){
            return;
        }
        this.q = q;
        page = 1;
        requestNet();
    }

    private void search() {
        ClientDiscoverAPI.search(q, "9", null, page+"", isContent ? "content" : "tag", null, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                SearchBean searchBean = new SearchBean();
                try {
                    Gson gson = new Gson();
                    Type type = new TypeToken<SearchBean>() {
                    }.getType();
                    searchBean = gson.fromJson(responseInfo.result, type);
                } catch (JsonSyntaxException e) {
                    Log.e("<<<", "数据解析异常" + e.toString());
                }
                dialog.dismiss();
                progressBar.setVisibility(View.GONE);
                SearchBean netSearch = searchBean;
                if (netSearch.isSuccess()) {
                    pullRefreshView.setLoadingTime();
                    if (page == 1) {
                        searchList.clear();
                        pullRefreshView.lastTotalItem = -1;
                        pullRefreshView.lastSavedFirstVisibleItem = -1;
                    }
                    searchList.addAll(netSearch.getData().getRows());
                    if (searchList.size() <= 0) {
                        emptyView.setVisibility(View.VISIBLE);
                    } else {
                        emptyView.setVisibility(View.GONE);
                    }
                    searchQJAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                dialog.dismiss();
                progressBar.setVisibility(View.GONE);
                ToastUtils.showError("网络错误");
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}