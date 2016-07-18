package com.taihuoniao.fineix.scene.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.SceneListViewAdapter;
import com.taihuoniao.fineix.base.BaseFragment;
import com.taihuoniao.fineix.beans.SearchBean;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.qingjingOrSceneDetails.SceneDetailActivity;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.utils.WriteJsonToSD;
import com.taihuoniao.fineix.view.WaittingDialog;
import com.taihuoniao.fineix.view.pulltorefresh.PullToRefreshBase;
import com.taihuoniao.fineix.view.pulltorefresh.PullToRefreshListView;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class CJResultFragment extends BaseFragment implements AdapterView.OnItemClickListener {
    private String q;
    private String t;
    private boolean isContent = false;
    //控件
    private PullToRefreshListView pullToRefreshLayout;
    private ListView listView;
    private ProgressBar progressBar;
    private TextView emptyView;
    //场景列表
    private int page = 1;
    private WaittingDialog dialog;
    private List<SearchBean.SearchItem> list;
    private SceneListViewAdapter sceneListViewAdapter;

    public static CJResultFragment newInstance(String q, String t, boolean isContent) {

        Bundle args = new Bundle();
        args.putString("q", q);
        args.putString("t", t);
        args.putBoolean("isContent", isContent);
        CJResultFragment fragment = new CJResultFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        q = getArguments().getString("q", null);
        t = getArguments().getString("t", null);
        isContent = getArguments().getBoolean("isContent");
    }

    @Override
    protected View initView() {
        View view = View.inflate(getActivity(), R.layout.fragment_index, null);
        RelativeLayout title = (RelativeLayout) view.findViewById(R.id.fragment_index_title);
        title.setVisibility(View.GONE);
        pullToRefreshLayout = (PullToRefreshListView) view.findViewById(R.id.fragment_index_pullrefreshview);
        listView = pullToRefreshLayout.getRefreshableView();
        progressBar = (ProgressBar) view.findViewById(R.id.fragment_index_progress);
        emptyView = (TextView) view.findViewById(R.id.fragment_index_emptyview);
        dialog = new WaittingDialog(getActivity());
        return view;
    }

    @Override
    protected void initList() {
        pullToRefreshLayout.setPullToRefreshEnabled(false);
        pullToRefreshLayout.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {
            @Override
            public void onLastItemVisible() {
                page++;
                progressBar.setVisibility(View.VISIBLE);
                if (isContent) {
                    search(q, t, page + "", "content", null);
                } else {
                    search(q, t, page + "", "tag", null);
                }
            }
        });
        list = new ArrayList<>();
        sceneListViewAdapter = new SceneListViewAdapter(getActivity(), null, null, list, null);
        listView.setAdapter(sceneListViewAdapter);
        listView.setOnItemClickListener(this);
    }

    @Override
    protected void requestNet() {
        Log.e("<<<cjresult", "q=" + q + ",t=" + t);
        if (TextUtils.isEmpty(q) || TextUtils.isEmpty(t)) {
            return;
        }
        dialog.show();
//        progressBar.setVisibility(View.VISIBLE);
        if (isContent)
            search(q, t, page + "", "content", null);
        else
            search(q, t, page + "", "tag", null);
    }

    private void search(String q, String t, String p, String evt, String sort) {
        ClientDiscoverAPI.search(q, t,null, p, evt, sort, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Log.e("<<<搜索", responseInfo.result);
                WriteJsonToSD.writeToSD("json", responseInfo.result);
//                Message msg = handler.obtainMessage();
//                msg.what = DataConstants.SEARCH_LIST;
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
                    if (page == 1) {
                        list.clear();
                    }
                    list.addAll(netSearch.getData().getRows());
                    if (list.size() <= 0) {
                        emptyView.setVisibility(View.VISIBLE);
                    } else {
                        emptyView.setVisibility(View.GONE);
                    }
                    sceneListViewAdapter.notifyDataSetChanged();
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

    public void refreshData(String q, String t) {
        this.q = q;
        this.t = t;
        page = 1;
        requestNet();
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        SearchBean.SearchItem searchItem = (SearchBean.SearchItem) listView.getAdapter().getItem(position);
        Intent intent = new Intent(getActivity(), SceneDetailActivity.class);
        intent.putExtra("id", searchItem.get_id());
        startActivity(intent);
    }
}
