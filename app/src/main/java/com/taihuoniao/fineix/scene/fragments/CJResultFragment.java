package com.taihuoniao.fineix.scene.fragments;

import android.content.Intent;
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
import android.widget.TextView;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.SceneListViewAdapter;
import com.taihuoniao.fineix.base.BaseFragment;
import com.taihuoniao.fineix.beans.SearchBean;
import com.taihuoniao.fineix.network.DataConstants;
import com.taihuoniao.fineix.network.DataPaser;
import com.taihuoniao.fineix.qingjingOrSceneDetails.SceneDetailActivity;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.view.WaittingDialog;
import com.taihuoniao.fineix.view.pulltorefresh.PullToRefreshBase;
import com.taihuoniao.fineix.view.pulltorefresh.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;


public class CJResultFragment extends BaseFragment implements AdapterView.OnItemClickListener {
    private String q;
    private String t;
    private boolean isContent = false;
    //控件
    public static CJResultFragment instance;
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
        instance = CJResultFragment.this;
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
                    DataPaser.search(q, t, page + "", "content", null, handler);
                } else
                    DataPaser.search(q, t, page + "", "tag", null, handler);
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
            DataPaser.search(q, t, page + "", "content", null, handler);
        else
            DataPaser.search(q, t, page + "", "tag", null, handler);
    }

    public void refreshData(String q, String t) {
        this.q = q;
        this.t = t;
        page = 1;
        requestNet();
    }

    //外界调用刷新场景列表
    public void refreshList() {
        if (TextUtils.isEmpty(q) || TextUtils.isEmpty(t)) {
            return;
        }
        page = 1;
        dialog.show();
//        progressBar.setVisibility(View.VISIBLE);
        DataPaser.search(q, t, page + "", "tag", null, handler);
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
                        if (list.size() <= 0) {
                            emptyView.setVisibility(View.VISIBLE);
                        } else {
                            emptyView.setVisibility(View.GONE);
                        }
                        sceneListViewAdapter.notifyDataSetChanged();
                    }
                    break;
                case DataConstants.NET_FAIL:
                    dialog.dismiss();
                    progressBar.setVisibility(View.GONE);
                    ToastUtils.showError("网络错误");
//                    new SVProgressHUD(getActivity()).showErrorWithStatus("网络错误");
//                    dialog.showErrorWithStatus("网络错误");
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
        SearchBean.SearchItem searchItem = (SearchBean.SearchItem) listView.getAdapter().getItem(position);
        Intent intent = new Intent(getActivity(), SceneDetailActivity.class);
        intent.putExtra("id", searchItem.get_id());
        startActivity(intent);
    }
}
