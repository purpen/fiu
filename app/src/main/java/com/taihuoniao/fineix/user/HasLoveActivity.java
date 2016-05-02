package com.taihuoniao.fineix.user;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.beans.User;
import com.taihuoniao.fineix.network.DataConstants;
import com.taihuoniao.fineix.network.DataPaser;
import com.taihuoniao.fineix.view.GlobalTitleLayout;
import com.taihuoniao.fineix.view.WaittingDialog;
import com.taihuoniao.fineix.view.pulltorefresh.PullToRefreshBase;
import com.taihuoniao.fineix.view.pulltorefresh.PullToRefreshListView;

/**
 * Created by taihuoniao on 2016/4/30.
 * 已经点赞的场景
 */
public class HasLoveActivity extends BaseActivity {

    GlobalTitleLayout titleLayout;

    PullToRefreshListView pullToRefreshView;

    ProgressBar progressBar;
    private ListView listView;
    private WaittingDialog dialog;
    //当前用户的user_id
    private User user;
    //场景列表
    private int page = 1;


    @Override
    protected void getIntentData() {
        user = (User) getIntent().getSerializableExtra("user");
        Log.e("<<<", "user_id=" + user._id);
        if (user == null) {
            Toast.makeText(HasLoveActivity.this, "用户信息为空", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    public HasLoveActivity() {
        super(0);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_has_love);
        titleLayout = (GlobalTitleLayout) findViewById(R.id.activity_has_love_titlelayout);
        pullToRefreshView = (PullToRefreshListView) findViewById(R.id.activity_has_love_pulltorefreshview);
        progressBar = (ProgressBar) findViewById(R.id.activity_has_love_progress);
        listView = pullToRefreshView.getRefreshableView();
        dialog = new WaittingDialog(HasLoveActivity.this);
    }

    @Override
    protected void initList() {
        titleLayout.setBackgroundResource(R.color.white);
        titleLayout.setBackImg(R.mipmap.back_black);
        titleLayout.setContinueTvVisible(false);
        titleLayout.setTitle(R.string.has_love, getResources().getColor(R.color.black333333));
        pullToRefreshView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                DataPaser.commonList(page + "", 8 + "", null, user._id + "", "sight", "love", handler);
            }
        });
        pullToRefreshView.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {
            @Override
            public void onLastItemVisible() {
                page++;
                progressBar.setVisibility(View.VISIBLE);
                DataPaser.commonList(page + "", 8 + "", null, user._id + "", "sight", "love", handler);
            }
        });
    }

    @Override
    protected void requestNet() {
        dialog.show();
        DataPaser.commonList(page + "", 8 + "", null, user._id + "", "sight", "love", handler);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DataConstants.COMMON_LIST:
                    dialog.dismiss();
                    progressBar.setVisibility(View.GONE);
                    pullToRefreshView.onRefreshComplete();
                    break;
                case DataConstants.NET_FAIL:
                    dialog.dismiss();
                    progressBar.setVisibility(View.GONE);
                    pullToRefreshView.onRefreshComplete();
                    break;
            }
        }
    };

    @Override
    protected void onDestroy() {
        //cancelNet();
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
            handler = null;
        }
        super.onDestroy();
    }
}
