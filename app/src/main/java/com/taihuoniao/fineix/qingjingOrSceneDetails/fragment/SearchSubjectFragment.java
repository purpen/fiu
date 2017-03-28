package com.taihuoniao.fineix.qingjingOrSceneDetails.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.SearchSubjectAdapter;
import com.taihuoniao.fineix.beans.HttpResponse;
import com.taihuoniao.fineix.common.GlobalDataCallBack;
import com.taihuoniao.fineix.base.HttpRequest;
import com.taihuoniao.fineix.beans.SearchBean;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.URL;
import com.taihuoniao.fineix.user.ActivityDetailActivity;
import com.taihuoniao.fineix.user.ArticalDetailActivity;
import com.taihuoniao.fineix.user.NewProductDetailActivity;
import com.taihuoniao.fineix.user.SalePromotionDetailActivity;
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
    private List<SearchBean.SearchItem> searchList;
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
        Intent intent = new Intent();
        switch (searchList.get(position).getType()) {
            case 1:
                intent.setClass(activity, ArticalDetailActivity.class);
                intent.putExtra(ArticalDetailActivity.class.getSimpleName(), searchList.get(position).get_id());
                break;
            case 2:
                intent.setClass(activity, ActivityDetailActivity.class);
                intent.putExtra(ActivityDetailActivity.class.getSimpleName(), searchList.get(position).get_id());
                break;
            case 3:
                intent.setClass(activity, SalePromotionDetailActivity.class);
                intent.putExtra(SalePromotionDetailActivity.class.getSimpleName(), searchList.get(position).get_id());
                break;
            default:
                intent.setClass(activity, NewProductDetailActivity.class);
                intent.putExtra(NewProductDetailActivity.class.getSimpleName(), searchList.get(position).get_id());
                break;
        }
        activity.startActivity(intent);
    }

    private void search() {
        HashMap<String, String> params = ClientDiscoverAPI.getsearchRequestParams(q, "12", null, page + "", "8", isContent ? "content" : "tag", null);
        Call httpHandler = HttpRequest.post(params, URL.SEARCH, new GlobalDataCallBack(){
            @Override
            public void onSuccess(String json) {
                HttpResponse<SearchBean> netSearch = JsonUtil.json2Bean(json, new TypeToken<HttpResponse<SearchBean>>() { });
                dialog.dismiss();
                progressBar.setVisibility(View.GONE);
                pullRefreshView.onRefreshComplete();
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
            public void onFailure(String error) {
                dialog.dismiss();
                progressBar.setVisibility(View.GONE);
                ToastUtils.showError("网络错误");
            }
        });
        addNet(httpHandler);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

}
