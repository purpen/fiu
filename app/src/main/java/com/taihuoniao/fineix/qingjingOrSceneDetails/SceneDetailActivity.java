package com.taihuoniao.fineix.qingjingOrSceneDetails;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.beans.CommonBean;
import com.taihuoniao.fineix.beans.SceneDetails;
import com.taihuoniao.fineix.main.MainApplication;
import com.taihuoniao.fineix.network.DataConstants;
import com.taihuoniao.fineix.network.DataPaser;
import com.taihuoniao.fineix.utils.TimestampToTimeUtils;
import com.taihuoniao.fineix.view.GridViewForScrollView;
import com.taihuoniao.fineix.view.ListViewForScrollView;
import com.taihuoniao.fineix.view.WaittingDialog;

/**
 * Created by taihuoniao on 2016/4/19.
 */
public class SceneDetailActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    //上个界面传递过来的场景id
    private String id;
    //界面下的控件
    private ImageView backgroundImg;
    private TextView changjingTitle;
    private TextView suoshuqingjingTv;
    private TextView locationTv;
    private TextView timeTv;
    private ImageView userHead;
    private TextView userName;
    private TextView userInfo;
    private TextView loveCount;
    private TextView desTv;
    private LinearLayout labelLinear;
    private TextView viewCount;
    private TextView loveCountTv;
    private TextView commentNum;
    private ImageView moreImg;
    private GridViewForScrollView userHeadGrid;
    private TextView moreUser;
    private ListViewForScrollView commentsListView;
    private TextView allComment;
    private ImageView moreComment;
    private ImageView love;
    private TextView loveTv;
    //网络请求对话框
    private WaittingDialog dialog;
    //图片加载
    private DisplayImageOptions options;


    public SceneDetailActivity() {
        super(0);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_scenedetails);
        backgroundImg = (ImageView) findViewById(R.id.activity_scenedetails_background);
        changjingTitle = (TextView) findViewById(R.id.activity_scenedetails_changjing_title);
        suoshuqingjingTv = (TextView) findViewById(R.id.activity_scenedetails_suoshuqingjing);
        locationTv = (TextView) findViewById(R.id.activity_scenedetails_location);
        timeTv = (TextView) findViewById(R.id.activity_scenedetails_time);
        userHead = (ImageView) findViewById(R.id.activity_scenedetails_userhead);
        userName = (TextView) findViewById(R.id.activity_scenedetails_username);
        userInfo = (TextView) findViewById(R.id.activity_scenedetails_userinfo);
        loveCount = (TextView) findViewById(R.id.activity_scenedetails_lovecount);
        desTv = (TextView) findViewById(R.id.activity_scenedetails_changjing_des);
        labelLinear = (LinearLayout) findViewById(R.id.activity_scenedetails_labellinear);
        viewCount = (TextView) findViewById(R.id.activity_scenedetails_viewcount);
        loveCountTv = (TextView) findViewById(R.id.activity_scenedetails_love_count);
        commentNum = (TextView) findViewById(R.id.activity_scenedetails_commentcount);
        moreImg = (ImageView) findViewById(R.id.activity_scenedetails_more);
        userHeadGrid = (GridViewForScrollView) findViewById(R.id.activity_scenedetails_grid);
        moreUser = (TextView) findViewById(R.id.activity_scenedetails_moreuser);
        allComment = (TextView) findViewById(R.id.activity_scenedetails_allcomment);
        moreComment = (ImageView) findViewById(R.id.activity_scenedetails_morecomment);
        love = (ImageView) findViewById(R.id.activity_scenedetails_love);
        loveTv = (TextView) findViewById(R.id.activity_scenedetails_lovetv);
        dialog = new WaittingDialog(SceneDetailActivity.this);
    }

    @Override
    protected void initList() {
        id = getIntent().getStringExtra("id");
        if (id == null) {
            Toast.makeText(SceneDetailActivity.this, "没有这个场景", Toast.LENGTH_SHORT).show();
            finish();
        }
        Log.e("<<<", "id=" + id);
        ViewGroup.LayoutParams lp = backgroundImg.getLayoutParams();
        lp.width = MainApplication.getContext().getScreenWidth();
        lp.height = lp.width * 16 / 9;
        backgroundImg.setLayoutParams(lp);
        moreImg.setOnClickListener(this);
        userHeadGrid.setOnItemClickListener(this);
        moreUser.setOnClickListener(this);
        allComment.setOnClickListener(this);
        moreComment.setOnClickListener(this);
        love.setOnClickListener(this);
        loveTv.setOnClickListener(this);
        options = new DisplayImageOptions.Builder()
//                .showImageOnLoading(R.mipmap.default_backround)
//                .showImageForEmptyUri(R.mipmap.default_backround)
//                .showImageOnFail(R.mipmap.default_backround)
                .cacheInMemory(true)
                .cacheOnDisk(true).considerExifParams(true)
                .displayer(new RoundedBitmapDisplayer(360)).build();
    }

    @Override
    protected void requestNet() {
        dialog.show();
        DataPaser.sceneDetails(id + "", handler);
        DataPaser.commentsList(1 + "", id, 12 + "", handler);
//        服务器返回数据参数不能为空
        DataPaser.commonList(1 + "", 14 + "", id, "sight", "love", handler);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DataConstants.COMMON_LIST:
                    dialog.dismiss();
                    CommonBean netCommonBean = (CommonBean) msg.obj;
                    if (netCommonBean.isSuccess()) {

                    }
                    break;
                case DataConstants.COMMENTS_LIST:
                    dialog.dismiss();
                    break;
                case DataConstants.SCENE_DETAILS:
                    dialog.dismiss();
                    SceneDetails netSceneDetails = (SceneDetails) msg.obj;
                    if (netSceneDetails.isSuccess()) {
                        ImageLoader.getInstance().displayImage(netSceneDetails.getCover_url(), backgroundImg);
                        changjingTitle.setText(netSceneDetails.getTitle());
                        suoshuqingjingTv.setText(netSceneDetails.getScene_title());
                        locationTv.setText(netSceneDetails.getAddress());
                        timeTv.setText(TimestampToTimeUtils.getStandardDate(netSceneDetails.getCreated_on()));
                        ImageLoader.getInstance().displayImage(netSceneDetails.getUser_info().getAvatar_url(), userHead, options);
                        userName.setText(netSceneDetails.getUser_info().getNickname());
                        userInfo.setText(netSceneDetails.getUser_info().getUser_rank() + " | " + netSceneDetails.getUser_info().getSummary());
                        loveCount.setText(netSceneDetails.getLove_count() + "人赞过");
                        desTv.setText(netSceneDetails.getDes());
                        //添加标签
                        //木有名称
                        viewCount.setText(netSceneDetails.getView_count());
                        loveCountTv.setText(netSceneDetails.getLove_count());
                        commentNum.setText(netSceneDetails.getComment_count());
                        //用户头像
                        //木有数据
                        allComment.setText("全部" + netSceneDetails.getComment_count() + "条评论");
                    } else {
                        Toast.makeText(SceneDetailActivity.this, netSceneDetails.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    break;
                case DataConstants.NET_FAIL:
                    dialog.dismiss();
                    break;
            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_scenedetails_love:
            case R.id.activity_scenedetails_lovetv:
                Toast.makeText(SceneDetailActivity.this, "点赞或取消点赞", Toast.LENGTH_SHORT).show();
                break;
            case R.id.activity_scenedetails_allcomment:
            case R.id.activity_scenedetails_morecomment:
                Toast.makeText(SceneDetailActivity.this, "查看更多评论", Toast.LENGTH_SHORT).show();
                break;
            case R.id.activity_scenedetails_moreuser:
                Toast.makeText(SceneDetailActivity.this, "查看更多用户", Toast.LENGTH_SHORT).show();
                break;
            case R.id.activity_scenedetails_more:
                Toast.makeText(SceneDetailActivity.this, "更多被点击了", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.activity_scenedetails_grid:
                Toast.makeText(SceneDetailActivity.this, "跳转到个人中心", Toast.LENGTH_SHORT).show();
                break;
        }
    }

}
