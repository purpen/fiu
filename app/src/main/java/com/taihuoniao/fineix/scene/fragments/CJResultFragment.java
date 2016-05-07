package com.taihuoniao.fineix.scene.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.SceneListViewAdapter;
import com.taihuoniao.fineix.base.BaseFragment;
import com.taihuoniao.fineix.beans.SearchBean;
import com.taihuoniao.fineix.network.DataConstants;
import com.taihuoniao.fineix.network.DataPaser;
import com.taihuoniao.fineix.view.WaittingDialog;
import com.taihuoniao.fineix.view.pulltorefresh.PullToRefreshBase;
import com.taihuoniao.fineix.view.pulltorefresh.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;


public class CJResultFragment extends BaseFragment implements AdapterView.OnItemClickListener {
    private String q;
    private String t;
    //控件
    private RelativeLayout title;
    private PullToRefreshListView pullToRefreshLayout;
    private ListView listView;
    private ProgressBar progressBar;
    //场景列表
    private int page = 1;
    private WaittingDialog dialog;
    private List<SearchBean.SearchItem> list;
    private SceneListViewAdapter sceneListViewAdapter;

    public static CJResultFragment newInstance(String q, String t) {

        Bundle args = new Bundle();
        args.putString("q", q);
        args.putString("t", t);
        CJResultFragment fragment = new CJResultFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        q = getArguments().getString("q", null);
        t = getArguments().getString("t", null);
    }

    @Override
    protected View initView() {
        View view = View.inflate(getActivity(), R.layout.fragment_index, null);
        title = (RelativeLayout) view.findViewById(R.id.fragment_index_title);
        title.setVisibility(View.GONE);
        pullToRefreshLayout = (PullToRefreshListView) view.findViewById(R.id.fragment_index_pullrefreshview);
        listView = pullToRefreshLayout.getRefreshableView();
        progressBar = (ProgressBar) view.findViewById(R.id.fragment_index_progress);
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
                DataPaser.search(q, t, page + "", handler);
            }
        });
        list = new ArrayList<>();
        sceneListViewAdapter = new SceneListViewAdapter(getActivity(), null, null, list);
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
        DataPaser.search(q, t, page + "", handler);
    }

    public void refreshData(String q, String t) {
        this.q = q;
        this.t = t;
        page = 1;
        requestNet();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DataConstants.SEARCH_LIST:
                    dialog.dismiss();
                    progressBar.setVisibility(View.GONE);
                    SearchBean netSearch = (SearchBean) msg.obj;
                    if (netSearch.isSuccess()) {
                        if (page == 1) {
                            list.clear();
                        }
                        list.addAll(netSearch.getData().getRows());
                        sceneListViewAdapter.notifyDataSetChanged();
                    }
                    break;
                case DataConstants.NET_FAIL:
                    dialog.dismiss();
                    progressBar.setVisibility(View.GONE);
                    break;
            }
        }
    };

    @Override
    public void onDestroy() {
        //cancelNet();
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
            handler = null;
        }
        super.onDestroy();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
