package com.taihuoniao.fineix.user.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.ShopOrderListAdapter;
import com.taihuoniao.fineix.base.Base2Fragment;
import com.taihuoniao.fineix.beans.OrderEntity;
import com.taihuoniao.fineix.network.DataConstants;
import com.taihuoniao.fineix.network.DataPaser;
import com.taihuoniao.fineix.view.dialog.WaittingDialog;
import com.taihuoniao.fineix.view.pulltorefresh.PullToRefreshBase;
import com.taihuoniao.fineix.view.pulltorefresh.PullToRefreshListView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by android on 2016/2/22.
 */
public class ShopOrderFragment extends Base2Fragment {

    private PullToRefreshListView pullToRefreshListView;
    //    private ProgressBar progressBar_order;
    private WaittingDialog mDialog;
    private View footerView;

    private ShopOrderListAdapter mAdapter;
    private List<OrderEntity> mList = new ArrayList<>();
    private String status;//根据不同状态值获取订单中不同状态的列表数据
    private boolean isBottom = false;
    private boolean pullVisible;
    private int curPage = 1;
    private int size = 10;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case DataConstants.PARSER_ORDER:
                    if (msg.obj != null) {
                        if (msg.obj instanceof List) {
                            if (curPage == 1) {
                                mList.clear();
                                pullToRefreshListView.lastTotalItem = -1;
                                pullToRefreshListView.lastSavedFirstVisibleItem = -1;
                            }
                            mList.addAll((Collection<? extends OrderEntity>) msg.obj);
//                            progressBar_order.setVisibility(View.GONE);
                            pullToRefreshListView.onRefreshComplete();
                            pullToRefreshListView.setLoadingTime();
                            mAdapter.notifyDataSetChanged();
                            if (mDialog != null) {
                                if (mDialog.isShowing()) {
                                    mDialog.dismiss();
                                }
                            }
                        }
                    }
                    break;
            }
        }
    };

    public static ShopOrderFragment getInstance(int status) {
        ShopOrderFragment fragment = new ShopOrderFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("status", status);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (!isVisibleToUser) {
            mHandler.removeCallbacks(mLoadData);
        } else {
            mDialog = new WaittingDialog(getActivity());
            if (curPage == 1) {
                if(!mDialog.isShowing()){
                    mDialog.show();
                }
            }
            curPage = 1;
            initData();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shop_order_viewpager, container, false);
        initView(view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        curPage = 1;
        Bundle bundle = getArguments();
        status = String.valueOf(bundle.getInt("status"));
        DataPaser.orderListParser(status, curPage + "", size + "", mHandler);
    }

    private Runnable mLoadData = new Runnable() {
        @Override
        public void run() {
            //在这里讲数据内容加载到Fragment上
            Bundle bundle = getArguments();
            status = String.valueOf(bundle.getInt("status"));
            DataPaser.orderListParser(status, curPage + "", size + "", mHandler);
        }
    };

    public void initData() {
        mHandler.postDelayed(mLoadData, 600);
    }

    private void initView(View view) {
        pullToRefreshListView = (PullToRefreshListView) view.findViewById(R.id.pullToRefreshListView_order);
//        progressBar_order = (ProgressBar) view.findViewById(R.id.order_progressBar);
        TextView textView_empty = (TextView) view.findViewById(R.id.order_textView_empty);
        ListView listView_show = pullToRefreshListView.getRefreshableView();
        listView_show.setEmptyView(textView_empty);
        listView_show.setCacheColorHint(0);

        //下拉监听
        pullToRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener() {
            @Override
            public void onRefresh() {

                //开始刷新
                curPage = 1; //页码从新开始
                Bundle bundle = getArguments();
                status = String.valueOf(bundle.getInt("status"));
                DataPaser.orderListParser(status, curPage + "", size + "", mHandler);
            }
        });

        // 设置上拉加载下一页
        pullToRefreshListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (isBottom && scrollState == SCROLL_STATE_IDLE) {
                    curPage++;
                    Bundle bundle = getArguments();
                    status = String.valueOf(bundle.getInt("status"));
                    DataPaser.orderListParser(status, curPage + "", size + "", mHandler);
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                isBottom = ((firstVisibleItem + visibleItemCount) == totalItemCount);
            }
        });
        Bundle bundle = getArguments();
        status = String.valueOf(bundle.getInt("status"));
        mAdapter = new ShopOrderListAdapter(mList, getActivity(), status);
        listView_show.setAdapter(mAdapter);

        // 加载网络数据，刷新ListView
//        progressBar_order.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
    }
}
