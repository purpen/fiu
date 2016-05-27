package com.taihuoniao.fineix.user;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.ShopOrderListAdapter;
import com.taihuoniao.fineix.beans.OrderEntity;
import com.taihuoniao.fineix.network.DataConstants;
import com.taihuoniao.fineix.network.DataPaser;
import com.taihuoniao.fineix.utils.ActivityUtil;
import com.taihuoniao.fineix.view.MyGlobalTitleLayout;
import com.taihuoniao.fineix.view.pulltorefresh.PullToRefreshBase;
import com.taihuoniao.fineix.view.pulltorefresh.PullToRefreshListView;
import com.taihuoniao.fineix.view.svprogress.SVProgressHUD;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

//
public class ReturnGoodsActivity extends Activity {
    private MyGlobalTitleLayout title = null;
    private PullToRefreshListView pullToRefreshListView;
    private ProgressBar progressBar;
    private ShopOrderListAdapter mAdapter;
    private List<OrderEntity> mList = new ArrayList<>();
    private String status = "8";
    private SVProgressHUD mDialog;
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
                            progressBar.setVisibility(View.GONE);
                            pullToRefreshListView.onRefreshComplete();
                            pullToRefreshListView.setLoadingTime();

                            mAdapter.notifyDataSetChanged();
                            mDialog.dismiss();
                        }
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        StatusBarChange.initWindow(this);
        setContentView(R.layout.activity_return_goods);
        iniView();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (curPage == 1) {
            mDialog.show();
        }
        mAdapter.notifyDataSetChanged();
        DataPaser.orderListParser(status, curPage + "", size + "", mHandler);
    }

    private void iniView() {
        ActivityUtil.getInstance().addActivity(this);
        title = (MyGlobalTitleLayout) findViewById(R.id.title_return);
        title.setTitle("退款/售后");
        title.setBackgroundResource(R.color.white);
        title.setBackImg(R.mipmap.back_black);
        title.setRightSearchButton(false);
        title.setRightShopCartButton(false);
        mDialog = new SVProgressHUD(this);
        if (curPage == 1) {
            mDialog.show();
        }
        pullToRefreshListView = (PullToRefreshListView) findViewById(R.id.pullToRefreshListView_return);
        progressBar = (ProgressBar) findViewById(R.id.return_progressBar);
        TextView textView_empty = (TextView) findViewById(R.id.return_textView_empty);
        ListView listView_show = pullToRefreshListView.getRefreshableView();
        listView_show.setEmptyView(textView_empty);
        listView_show.setCacheColorHint(0);
        //下拉监听
        pullToRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //页码从新开始
                curPage = 1;
                //开始刷新
                DataPaser.orderListParser(status, curPage + "", size + "", mHandler);

            }
        });
        // 设置上拉加载下一页
        pullToRefreshListView.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {
            @Override
            public void onLastItemVisible() {
                curPage++;
                DataPaser.orderListParser(status, curPage + "", size + "", mHandler);
            }
        });

        mAdapter = new ShopOrderListAdapter(mList, this, status);
        listView_show.setAdapter(mAdapter);
        // 加载网络数据，刷新ListView
        progressBar.setVisibility(View.VISIBLE);
    }

}
