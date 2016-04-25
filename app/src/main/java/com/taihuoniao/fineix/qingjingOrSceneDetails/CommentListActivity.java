package com.taihuoniao.fineix.qingjingOrSceneDetails;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.CommentsListAdapter;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.base.NetBean;
import com.taihuoniao.fineix.beans.CommentsBean;
import com.taihuoniao.fineix.network.DataConstants;
import com.taihuoniao.fineix.network.DataPaser;
import com.taihuoniao.fineix.view.GlobalTitleLayout;
import com.taihuoniao.fineix.view.WaittingDialog;
import com.taihuoniao.fineix.view.pulltorefresh.PullToRefreshBase;
import com.taihuoniao.fineix.view.pulltorefresh.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by taihuoniao on 2016/4/21.
 */
public class CommentListActivity extends BaseActivity implements View.OnClickListener {
    //上个界面传递过来的评论关联id
    private String target_id;
    private String type;//类型  12 场景评论
    //界面下的控件
    private GlobalTitleLayout titleLayout;
    private PullToRefreshListView pullToRefreshLayout;
    private TextView nothingTv;
    private ProgressBar progressBar;
    private ListView listView;
    private List<CommentsBean.CommentItem> commentList;
    private CommentsListAdapter commentsListAdapter;
    private EditText editText;
    private Button sendBtn;
    //网络请求
    private WaittingDialog dialog;
    //评论列表页码
    private int currentPage = 1;

    public CommentListActivity() {
        super(0);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_commentlist);
        titleLayout = (GlobalTitleLayout) findViewById(R.id.activity_commentlist_titlelayout);
        pullToRefreshLayout = (PullToRefreshListView) findViewById(R.id.activity_commentlist_pulltorefreshview);
        nothingTv = (TextView) findViewById(R.id.activity_commentlist_nothing);
        progressBar = (ProgressBar) findViewById(R.id.activity_commentlist_progressbar);
        listView = pullToRefreshLayout.getRefreshableView();
        editText = (EditText) findViewById(R.id.activity_commentlist_edit);
        sendBtn = (Button) findViewById(R.id.activity_commentlist_send);
        dialog = new WaittingDialog(CommentListActivity.this);
    }

    @Override
    protected void initList() {
        target_id = getIntent().getStringExtra("target_id");
        type = getIntent().getStringExtra("type");
        if (target_id == null || type == null) {
            Toast.makeText(CommentListActivity.this, "数据错误", Toast.LENGTH_SHORT).show();
            finish();
        }
        titleLayout.setBackgroundResource(R.color.white);
        titleLayout.setBackImg(R.mipmap.back_black);
        titleLayout.setTitle(R.string.comment, getResources().getColor(R.color.title_black));
        titleLayout.setContinueTvVisible(false);
        commentList = new ArrayList<>();
        commentsListAdapter = new CommentsListAdapter(CommentListActivity.this, commentList);
        listView.setAdapter(commentsListAdapter);
        pullToRefreshLayout.setPullToRefreshEnabled(false);
        pullToRefreshLayout.setEmptyView(nothingTv);
        pullToRefreshLayout.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {
            @Override
            public void onLastItemVisible() {
                progressBar.setVisibility(View.VISIBLE);
                currentPage++;
                DataPaser.commentsList(currentPage + "", target_id, type, handler);
            }
        });
        sendBtn.setOnClickListener(this);
    }

    @Override
    protected void requestNet() {
        dialog.show();
        DataPaser.commentsList(currentPage + "", target_id, type, handler);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DataConstants.SEND_COMMENT:
                    NetBean netBean = (NetBean) msg.obj;
                    Toast.makeText(CommentListActivity.this, netBean.getMessage(), Toast.LENGTH_SHORT).show();
                    if (netBean.isSuccess()) {
                        editText.setText("");
                        currentPage = 1;
                        DataPaser.commentsList(currentPage + "", target_id, type, handler);
                    } else {
                        dialog.dismiss();
                    }
                    break;
                case DataConstants.COMMENTS_LIST:
                    dialog.dismiss();
                    progressBar.setVisibility(View.GONE);
                    CommentsBean netCommentsBean = (CommentsBean) msg.obj;
                    if (netCommentsBean.isSuccess()) {
                        if (currentPage == 1) {
                            pullToRefreshLayout.lastSavedFirstVisibleItem = -1;
                            pullToRefreshLayout.lastTotalItem = -1;
                            commentList.clear();
                        }
                        commentList.addAll(netCommentsBean.getData().getRows());
                        commentsListAdapter.notifyDataSetChanged();
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
    protected void onDestroy() {
        //        cancelNet();
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
            handler = null;
        }
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_commentlist_send:
                if (TextUtils.isEmpty(editText.getText())) {
                    return;
                }
                Toast.makeText(CommentListActivity.this, "需要登录", Toast.LENGTH_SHORT).show();
                dialog.show();
                DataPaser.sendComment(target_id, editText.getText().toString(), type, handler);
                break;
        }
    }
}
