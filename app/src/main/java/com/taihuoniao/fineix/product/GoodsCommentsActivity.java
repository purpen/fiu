package com.taihuoniao.fineix.product;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.GoodsDetailsCommentListsAdapter;
import com.taihuoniao.fineix.beans.TryCommentsBean;
import com.taihuoniao.fineix.network.DataConstants;
import com.taihuoniao.fineix.network.DataPaser;
import com.taihuoniao.fineix.network.NetworkManager;
import com.taihuoniao.fineix.view.MyGlobalTitleLayout;
import com.taihuoniao.fineix.view.pulltorefresh.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by taihuoniao on 2016/2/24.
 */
public class GoodsCommentsActivity extends Activity {
    //上个界面传递过来的数据
    private String target_id;
    //界面下的控件
    private MyGlobalTitleLayout titleLayout;
    private RelativeLayout bottomRelative;
    private PullToRefreshListView pullToRefreshView;
    private GoodsDetailsCommentListsAdapter commentListsAdapter;
    private List<TryCommentsBean> commentsList;
    private ProgressBar progressBar;
    private TextView emptyView;
    //当前页码
    private int currentPage = 1;
    private int lastSavedFirstVisibleItem = -1;
    private int lastTotalItem = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commentlists);
        initView();
        initData();
        progressBar.setVisibility(View.VISIBLE);
        DataPaser.getGoodsDetailsCommentsList( target_id, currentPage + "", mHandler);
    }

    @Override
    protected void onDestroy() {
        cancelNet();
        super.onDestroy();
    }

    private void initData() {
        target_id = getIntent().getStringExtra("target_id");
        titleLayout.setRightShopCartButton(false);
        titleLayout.setRightSearchButton(false);
        titleLayout.setTitle("评论");
        titleLayout.setBackgroundResource(R.color.white);
        titleLayout.setBackImg(R.mipmap.back_black);
        titleLayout.setTitleColor(getResources().getColor(R.color.black333333));
        bottomRelative.setVisibility(View.GONE);
        pullToRefreshView.setPullToRefreshEnabled(false);
        pullToRefreshView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (visibleItemCount > 0
                        && (firstVisibleItem + visibleItemCount == totalItemCount)) {
                    if (firstVisibleItem != lastSavedFirstVisibleItem && totalItemCount != lastTotalItem) {
                        lastSavedFirstVisibleItem = firstVisibleItem;
                        lastTotalItem = totalItemCount;
                        currentPage++;
                        progressBar.setVisibility(View.VISIBLE);
                        DataPaser.getGoodsDetailsCommentsList( target_id, currentPage + "", mHandler);
                    }
                }
            }
        });
        pullToRefreshView.setEmptyView(emptyView);
    }

    private void initView() {
        titleLayout = (MyGlobalTitleLayout) findViewById(R.id.activity_commentlists_titlelayout);
        bottomRelative = (RelativeLayout) findViewById(R.id.activity_commentlists_bottomrelative);
        pullToRefreshView = (PullToRefreshListView) findViewById(R.id.activity_commentlists_listview);
        ListView listView = pullToRefreshView.getRefreshableView();
        commentsList = new ArrayList<>();
        commentListsAdapter = new GoodsDetailsCommentListsAdapter(GoodsCommentsActivity.this, commentsList, true);
        listView.setAdapter(commentListsAdapter);
        progressBar = (ProgressBar) findViewById(R.id.activity_commentlists_progressbar);
        emptyView = (TextView) findViewById(R.id.activity_commentlists_nocomments);
//        StatusBarChange.initWindow(GoodsCommentsActivity.this);
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DataConstants.GOODS_DETAILS_COMMENTS:
                    progressBar.setVisibility(View.GONE);
                    List<TryCommentsBean> netList = (List<TryCommentsBean>) msg.obj;
                    commentsList.addAll(netList);
                    commentListsAdapter.notifyDataSetChanged();
                    break;
                case DataConstants.NETWORK_FAILURE:
                    progressBar.setVisibility(View.GONE);
//                    Toast.makeText(GoodsCommentsActivity.this, R.string.host_failure, Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    private void cancelNet() {
        NetworkManager.getInstance().cancel("goodsDetailsCommentsList");
    }
}
