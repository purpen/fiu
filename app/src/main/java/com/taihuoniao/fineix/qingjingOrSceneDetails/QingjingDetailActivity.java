package com.taihuoniao.fineix.qingjingOrSceneDetails;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.SceneDetailCommentAdapter;
import com.taihuoniao.fineix.adapters.SceneDetailUserHeadAdapter;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.beans.CommentsBean;
import com.taihuoniao.fineix.beans.CommonBean;
import com.taihuoniao.fineix.beans.QingjingDetailBean;
import com.taihuoniao.fineix.main.MainApplication;
import com.taihuoniao.fineix.network.DataConstants;
import com.taihuoniao.fineix.network.DataPaser;
import com.taihuoniao.fineix.view.GridViewForScrollView;
import com.taihuoniao.fineix.view.ListViewForScrollView;
import com.taihuoniao.fineix.view.WaittingDialog;

import java.util.List;

/**
 * Created by taihuoniao on 2016/4/25.
 */
public class QingjingDetailActivity extends BaseActivity implements View.OnClickListener {
    //上个界面传递过来的情景id
    private String id;
    //界面下的控件
    private RelativeLayout imgRelative;
    private ImageView backgroundImg;
    private TextView qingjingTitle;
    private TextView locationTv;
    private TextView timeTv;
    private ImageView userHead;
    private TextView userName;
    private TextView userInfo;
    private TextView subscriptionCount;
    private TextView desTv;
    private LinearLayout labelLinear;
    private TextView viewCount;
    private TextView loveCountTv;
    private ImageView commentImg;
    private TextView commentNum;
    private ImageView moreImg;
    private GridViewForScrollView userHeadGrid;
    private List<CommonBean.CommonItem> headList;
    private SceneDetailUserHeadAdapter sceneDetailUserHeadAdapter;
    private TextView moreUser;
    private ListViewForScrollView changjingListView;
    private LinearLayout subLinear;
    private List<CommentsBean.CommentItem> commentList;
    private SceneDetailCommentAdapter sceneDetailCommentAdapter;
    private TextView allComment;
    private ImageView moreComment;
    private ImageView love;
    private TextView loveTv;
    //网络请求对话框
    private WaittingDialog dialog;
    //图片加载
    private DisplayImageOptions options;


    public QingjingDetailActivity() {
        super(0);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_qingjingdetail);
        backgroundImg = (ImageView) findViewById(R.id.activity_qingjingdetail_background);
        qingjingTitle = (TextView) findViewById(R.id.activity_qingjingdetail_qingjing_title);
        locationTv = (TextView) findViewById(R.id.activity_qingjingdetail_location);
        timeTv = (TextView) findViewById(R.id.activity_qingjingdetail_time);
        userHead = (ImageView) findViewById(R.id.activity_qingjingdetail_userhead);
        userName = (TextView) findViewById(R.id.activity_qingjingdetail_username);
        userInfo = (TextView) findViewById(R.id.activity_qingjingdetail_userinfo);
        subscriptionCount = (TextView) findViewById(R.id.activity_qingjingdetail_subsnum);
        desTv = (TextView) findViewById(R.id.activity_qingjingdetail_des);
        labelLinear = (LinearLayout) findViewById(R.id.activity_qingjingdetail_labellinear);
        userHeadGrid = (GridViewForScrollView) findViewById(R.id.activity_qingjingdetail_grid);
        moreUser = (TextView) findViewById(R.id.activity_qingjingdetail_more_user);
        changjingListView = (ListViewForScrollView) findViewById(R.id.activity_qingjingdetail_listview);
        subLinear = (LinearLayout) findViewById(R.id.activity_qingjingdetail_sublinear);
        options = new DisplayImageOptions.Builder()
//                .showImageOnLoading(R.mipmap.default_backround)
//                .showImageForEmptyUri(R.mipmap.default_backround)
//                .showImageOnFail(R.mipmap.default_backround)
                .cacheInMemory(true)
                .cacheOnDisk(true).considerExifParams(true)
                .displayer(new RoundedBitmapDisplayer(360)).build();
        dialog = new WaittingDialog(QingjingDetailActivity.this);
    }

    @Override
    protected void initList() {
        id = getIntent().getStringExtra("id");
        if (id == null) {
            Toast.makeText(QingjingDetailActivity.this, "没有这个情景", Toast.LENGTH_SHORT).show();
            finish();
        }
        ViewGroup.LayoutParams lp = backgroundImg.getLayoutParams();
        lp.width = MainApplication.getContext().getScreenWidth();
        lp.height = lp.width * 16 / 9;
        backgroundImg.setLayoutParams(lp);
        backgroundImg.setFocusable(true);
        backgroundImg.setFocusableInTouchMode(true);
        backgroundImg.requestFocus();
        subLinear.setOnClickListener(this);
    }

    @Override
    protected void requestNet() {
        dialog.show();
        DataPaser.qingjingDetails(id, handler);
        //订阅的用户列表，暂无数据

    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DataConstants.QINGJING_DETAILS:
                    dialog.dismiss();
                    QingjingDetailBean netQingjingDetailBean = (QingjingDetailBean) msg.obj;
                    if (netQingjingDetailBean.isSuccess()) {
                        Log.e("<<<", "cover_url=" + netQingjingDetailBean.getData().getCover_url());
                        ImageLoader.getInstance().displayImage(netQingjingDetailBean.getData().getCover_url(), backgroundImg);
                        qingjingTitle.setText(netQingjingDetailBean.getData().getTitle());
                        locationTv.setText(netQingjingDetailBean.getData().getAddress());
                        timeTv.setText(netQingjingDetailBean.getData().getCreated_at());
                        //用户信息，服务器无字段
//                        ImageLoader.getInstance().displayImage(netQingjingDetailBean.get);
                        subscriptionCount.setText(netQingjingDetailBean.getData().getSubscription_count() + "人订阅");
                        desTv.setText(netQingjingDetailBean.getData().getDes());
                    } else {
                        Toast.makeText(QingjingDetailActivity.this, netQingjingDetailBean.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    break;
                case DataConstants.NET_FAIL:
                    dialog.dismiss();
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_qingjingdetail_sublinear:
                Toast.makeText(QingjingDetailActivity.this, "点赞或取消点赞", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
