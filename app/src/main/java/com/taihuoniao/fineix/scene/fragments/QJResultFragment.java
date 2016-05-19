package com.taihuoniao.fineix.scene.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.AllQingjingGridAdapter;
import com.taihuoniao.fineix.base.BaseFragment;
import com.taihuoniao.fineix.beans.SearchBean;
import com.taihuoniao.fineix.network.DataConstants;
import com.taihuoniao.fineix.network.DataPaser;
import com.taihuoniao.fineix.qingjingOrSceneDetails.QingjingDetailActivity;
import com.taihuoniao.fineix.utils.DensityUtils;
import com.taihuoniao.fineix.view.WaittingDialog;
import com.taihuoniao.fineix.view.pulltorefresh.PullToRefreshBase;
import com.taihuoniao.fineix.view.pulltorefresh.PullToRefreshGridView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lilin
 *         created at 2016/4/25 18:38
 */
public class QJResultFragment extends BaseFragment implements AdapterView.OnItemClickListener {
    private String q;
    private String t;
    //控件
    private PullToRefreshGridView pullToRefreshView;
    private GridView gridView;
    private ProgressBar progressBar;
    private TextView emptyView;
    //情景列表
    private int page = 1;
    private List<SearchBean.SearchItem> list;
    private AllQingjingGridAdapter allQingjingGridAdapter;
    private WaittingDialog dialog;

    public static QJResultFragment newInstance(String q, String t) {

        Bundle args = new Bundle();
        args.putString("q", q);
        args.putString("t", t);
        QJResultFragment fragment = new QJResultFragment();
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
        View view = View.inflate(getActivity(), R.layout.fragment_qjresult, null);
        pullToRefreshView = (PullToRefreshGridView) view.findViewById(R.id.fragment_qjresult_pullrefreshview);
        gridView = pullToRefreshView.getRefreshableView();
        progressBar = (ProgressBar) view.findViewById(R.id.fragment_qjresult_progress);
        emptyView = (TextView) view.findViewById(R.id.fragment_qjresult_emptyview);
        dialog = new WaittingDialog(getActivity());
        return view;
    }

    @Override
    protected void initList() {
        pullToRefreshView.setPullToRefreshEnabled(false);
        pullToRefreshView.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {
            @Override
            public void onLastItemVisible() {
                progressBar.setVisibility(View.VISIBLE);
                page++;
                DataPaser.search(q, t, page + "","tag", handler);
            }
        });
        gridView.setNumColumns(2);
        int space = DensityUtils.dp2px(getActivity(), 5);
        gridView.setHorizontalSpacing(space);
        gridView.setVerticalSpacing(space);
        list = new ArrayList<>();
        allQingjingGridAdapter = new AllQingjingGridAdapter(null, list, getActivity(), space);
        gridView.setAdapter(allQingjingGridAdapter);
        gridView.setOnItemClickListener(this);
    }

    @Override
    protected void requestNet() {
        Log.e("<<<qjresult", "q=" + q + ",t=" + t);
        if (TextUtils.isEmpty(q) || TextUtils.isEmpty(t)) {
            return;
        }
        dialog.show();
        DataPaser.search(q, t, page + "","tag", handler);
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
                        if (list.size() <= 0) {
                            emptyView.setVisibility(View.VISIBLE);
                        } else {
                            emptyView.setVisibility(View.GONE);
                        }
                        allQingjingGridAdapter.notifyDataSetChanged();
                    }
                    break;
                case DataConstants.NET_FAIL:
                    dialog.dismiss();
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), "请求失败", Toast.LENGTH_SHORT).show();
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
        SearchBean.SearchItem searchItem = (SearchBean.SearchItem) gridView.getAdapter().getItem(position);
        if (searchItem != null) {
            Intent intent = new Intent(getActivity(), QingjingDetailActivity.class);
            intent.putExtra("id", searchItem.get_id());
            startActivity(intent);
        }
    }
}
