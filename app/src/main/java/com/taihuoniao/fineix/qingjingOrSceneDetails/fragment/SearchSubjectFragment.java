package com.taihuoniao.fineix.qingjingOrSceneDetails.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.SearchSubjectAdapter;
import com.taihuoniao.fineix.beans.SearchBean;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
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
 * Created by taihuoniao on 2016/8/23.
 */
public class SearchSubjectFragment extends SearchFragment implements AdapterView.OnItemClickListener {
    private String q;
    private boolean isContent;
    @Bind(R.id.pull_refresh_view)
    PullToRefreshListView pullRefreshView;
    @Bind(R.id.empty_view)
    TextView emptyView;
    @Bind(R.id.progress_bar)
    ProgressBar progressBar;
    private ListView listView;
    private WaittingDialog dialog;
    private int page = 1;
    private List<SearchBean.Data.SearchItem> searchList;
    private SearchSubjectAdapter searchSubjectAdapter;

    public static SearchSubjectFragment newInstance(String q, boolean isContent) {
        Bundle args = new Bundle();
        args.putString("q", q);
        args.putBoolean("isContent", isContent);
        SearchSubjectFragment fragment = new SearchSubjectFragment();
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
        View view = View.inflate(getActivity(), R.layout.fragment_search_user, null);
        ButterKnife.bind(this, view);
        listView = pullRefreshView.getRefreshableView();
        dialog = new WaittingDialog(getActivity());
        return view;
    }

    @Override
    protected void initList() {
        pullRefreshView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                requestNet();
            }
        });
        pullRefreshView.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {
            @Override
            public void onLastItemVisible() {
                page++;
                progressBar.setVisibility(View.VISIBLE);
                search();
            }
        });
        listView.setDividerHeight(0);
        listView.setSelector(R.color.nothing);
        searchList = new ArrayList<>();
        searchSubjectAdapter = new SearchSubjectAdapter(searchList);
        listView.setAdapter(searchSubjectAdapter);
        listView.setOnItemClickListener(this);
//        searchUsersAdapter = new SearchUsersAdapter(getActivity(),searchList);
//        listView.setAdapter(searchUsersAdapter);
    }

    @Override
    protected void requestNet() {
        if (TextUtils.isEmpty(q)) {
            pullRefreshView.onRefreshComplete();
            return;
        }
        if (!dialog.isShowing()) {
            dialog.show();
        }
        search();
    }

    public void refreshData(String q) {
        if (dialog == null || TextUtils.isEmpty(q) || TextUtils.equals(this.q, q)) {
            return;
        }
        this.q = q;
        page = 1;
        requestNet();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ToastUtils.showError("主题详情跳转" + position);
    }

    private void search() {
        ClientDiscoverAPI.search(q, "12", null, page + "", isContent ? "content" : "tag", null, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Log.e("<<<搜索主题", responseInfo.result);
//                WriteJsonToSD.writeToSD("json", responseInfo.result);
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
                pullRefreshView.onRefreshComplete();
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
                    searchSubjectAdapter.notifyDataSetChanged();
//                    searchUsersAdapter.notifyDataSetChanged();
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
