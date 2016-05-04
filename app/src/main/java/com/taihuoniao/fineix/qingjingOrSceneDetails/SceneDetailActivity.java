package com.taihuoniao.fineix.qingjingOrSceneDetails;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PointF;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
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
import com.taihuoniao.fineix.beans.LoginInfo;
import com.taihuoniao.fineix.beans.SceneDetails;
import com.taihuoniao.fineix.beans.SceneLoveBean;
import com.taihuoniao.fineix.beans.TagItem;
import com.taihuoniao.fineix.main.MainApplication;
import com.taihuoniao.fineix.network.DataConstants;
import com.taihuoniao.fineix.network.DataPaser;
import com.taihuoniao.fineix.product.GoodsDetailActivity;
import com.taihuoniao.fineix.user.OptRegisterLoginActivity;
import com.taihuoniao.fineix.utils.DensityUtils;
import com.taihuoniao.fineix.utils.TimestampToTimeUtils;
import com.taihuoniao.fineix.view.GridViewForScrollView;
import com.taihuoniao.fineix.view.LabelView;
import com.taihuoniao.fineix.view.ListViewForScrollView;
import com.taihuoniao.fineix.view.WaittingDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by taihuoniao on 2016/4/19.
 */
public class SceneDetailActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener, View.OnTouchListener {
    //上个界面传递过来的场景id
    private String id;
    //界面下的控件
    private ScrollView scrollView;
    private LinearLayout loveRelative;
    private ImageView backImg;
    private ImageView shareImg;
    private RelativeLayout imgRelative;
    private ImageView backgroundImg;
    private TextView changjingTitle;
    private TextView suoshuqingjingTv;
    private ImageView locationImg;
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
    private ImageView commentImg;
    private TextView commentNum;
    private ImageView moreImg;
    private GridViewForScrollView userHeadGrid;
    private List<CommonBean.CommonItem> headList;
    private SceneDetailUserHeadAdapter sceneDetailUserHeadAdapter;
    private TextView moreUser;
    private ListViewForScrollView commentsListView;
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
    //图片上的商品
    private List<SceneDetails.Product> productList;
    //是否显示标签和价格的标识
    private boolean isShowAll = false;
    //当前用户是否已经点赞
    private int isLove = 0;//0是未点赞，1是已点赞


    public SceneDetailActivity() {
        super(0);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_scenedetails);
        scrollView = (ScrollView) findViewById(R.id.activity_scenedetails_scrollview);
        loveRelative = (LinearLayout) findViewById(R.id.activity_scenedetails_loverelative);
        backImg = (ImageView) findViewById(R.id.activity_scenedetails_back);
        shareImg = (ImageView) findViewById(R.id.activity_scenedetails_share);
        imgRelative = (RelativeLayout) findViewById(R.id.activity_scenedetails_imgrelative);
        backgroundImg = (ImageView) findViewById(R.id.activity_scenedetails_background);
        changjingTitle = (TextView) findViewById(R.id.activity_scenedetails_changjing_title);
        suoshuqingjingTv = (TextView) findViewById(R.id.activity_scenedetails_suoshuqingjing);
        locationImg = (ImageView) findViewById(R.id.activity_scenedetails_locationimg);
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
        commentImg = (ImageView) findViewById(R.id.activity_scenedetails_commentimg);
        commentNum = (TextView) findViewById(R.id.activity_scenedetails_commentcount);
        moreImg = (ImageView) findViewById(R.id.activity_scenedetails_more);
        userHeadGrid = (GridViewForScrollView) findViewById(R.id.activity_scenedetails_grid);
        moreUser = (TextView) findViewById(R.id.activity_scenedetails_moreuser);
        commentsListView = (ListViewForScrollView) findViewById(R.id.activity_scenedetails_commentlistview);
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
        scrollView.setOnTouchListener(this);
        ViewGroup.LayoutParams lp = backgroundImg.getLayoutParams();
        lp.width = MainApplication.getContext().getScreenWidth();
        lp.height = lp.width * 16 / 9;
        backgroundImg.setLayoutParams(lp);
        backgroundImg.setFocusable(true);
        backgroundImg.setFocusableInTouchMode(true);
        backgroundImg.requestFocus();
        backImg.setOnClickListener(this);
        shareImg.setOnClickListener(this);
        backgroundImg.setOnClickListener(this);
        locationImg.setOnClickListener(this);
        locationTv.setOnClickListener(this);
        commentImg.setOnClickListener(this);
        commentNum.setOnClickListener(this);
        moreImg.setOnClickListener(this);
        headList = new ArrayList<>();
        sceneDetailUserHeadAdapter = new SceneDetailUserHeadAdapter(SceneDetailActivity.this, headList);
        userHeadGrid.setAdapter(sceneDetailUserHeadAdapter);
        userHeadGrid.setOnItemClickListener(this);
        moreUser.setOnClickListener(this);
        commentsListView.setDividerHeight(0);
        commentList = new ArrayList<>();
        sceneDetailCommentAdapter = new SceneDetailCommentAdapter(SceneDetailActivity.this, commentList);
        commentsListView.setAdapter(sceneDetailCommentAdapter);
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
        IntentFilter filter = new IntentFilter();
        filter.addAction(DataConstants.BroadSceneDetail);
        registerReceiver(sceneDetailReceiver, filter);
    }

    @Override
    protected void requestNet() {
        dialog.show();
        DataPaser.sceneDetails(id + "", handler);
        DataPaser.commentsList(1 + "", 3 + "", id, 12 + "", handler);
        DataPaser.commonList(1 + "", 14 + "", id, null, "sight", "love", handler);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DataConstants.CANCEL_LOVE_SCENE:
                    SceneLoveBean netSceneLoveBean1 = (SceneLoveBean) msg.obj;
                    if (netSceneLoveBean1.isSuccess()) {
                        Toast.makeText(SceneDetailActivity.this, netSceneLoveBean1.getData().getLove_count() + "", Toast.LENGTH_SHORT).show();
                        DataPaser.commonList(1 + "", 14 + "", id, null, "sight", "love", handler);
                        isLove = 0;
                        love.setImageResource(R.mipmap.like_height_43px);
                        loveCount.setText(netSceneLoveBean1.getData().getLove_count() + "人赞过");
                        loveCountTv.setText(netSceneLoveBean1.getData().getLove_count() + "");
                        moreUser.setText(netSceneLoveBean1.getData().getLove_count() + "+");
                        if (netSceneLoveBean1.getData().getLove_count() > 14) {
                            moreUser.setVisibility(View.VISIBLE);
                        } else {
                            moreUser.setVisibility(View.GONE);
                        }
                    } else {
                        dialog.dismiss();
                        Toast.makeText(SceneDetailActivity.this, netSceneLoveBean1.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    break;
                case DataConstants.LOVE_SCENE:
                    SceneLoveBean netSceneLoveBean = (SceneLoveBean) msg.obj;
                    Toast.makeText(SceneDetailActivity.this, netSceneLoveBean.getData().getLove_count() + "", Toast.LENGTH_SHORT).show();
                    if (netSceneLoveBean.isSuccess()) {
                        DataPaser.commonList(1 + "", 14 + "", id, null, "sight", "love", handler);
                        isLove = 1;
                        love.setImageResource(R.mipmap.love_yes);
                        loveCount.setText(netSceneLoveBean.getData().getLove_count() + "人赞过");
                        loveCountTv.setText(netSceneLoveBean.getData().getLove_count() + "");
                        moreUser.setText(netSceneLoveBean.getData().getLove_count() + "+");
                        if (netSceneLoveBean.getData().getLove_count() > 14) {
                            moreUser.setVisibility(View.VISIBLE);
                        } else {
                            moreUser.setVisibility(View.GONE);
                        }
                    } else {
                        dialog.dismiss();
                        Toast.makeText(SceneDetailActivity.this, netSceneLoveBean.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    break;
                case DataConstants.COMMON_LIST:
                    dialog.dismiss();
                    CommonBean netCommonBean = (CommonBean) msg.obj;
                    if (netCommonBean.isSuccess()) {
                        headList.clear();
                        headList.addAll(netCommonBean.getData().getRows());
                        sceneDetailUserHeadAdapter.notifyDataSetChanged();
                    }
                    break;
                case DataConstants.COMMENTS_LIST:
                    dialog.dismiss();
//                    Log.e("<<<", "评论列表");
                    CommentsBean netCommentBean = (CommentsBean) msg.obj;
                    if (netCommentBean.isSuccess()) {
//                        commentList.clear();
                        commentList.addAll(netCommentBean.getData().getRows());
                        if (netCommentBean.getData().getRows().size() > 3) {
                            allComment.setVisibility(View.VISIBLE);
                            moreComment.setVisibility(View.VISIBLE);
                        }
                        sceneDetailCommentAdapter.notifyDataSetChanged();
                    }
                    break;
                case DataConstants.SCENE_DETAILS:
                    dialog.dismiss();
//                    Log.e("<<<", "场景详情");
                    SceneDetails netSceneDetails = (SceneDetails) msg.obj;
                    if (netSceneDetails.isSuccess()) {
//                        Log.e("<<<", "url=" + netSceneDetails.getCover_url());
                        ImageLoader.getInstance().displayImage(netSceneDetails.getCover_url(), backgroundImg);
                        //用户是否已经点赞
                        isLove = netSceneDetails.getIs_love();
                        switch (isLove) {
                            case 1:
                                love.setImageResource(R.mipmap.love_yes);
                                break;
                            default:
                                love.setImageResource(R.mipmap.like_height_43px);
                                break;
                        }
                        //场景上的商品
                        productList = netSceneDetails.getProduct();
                        //添加商品
                        addProductToImg();
                        changjingTitle.setText(netSceneDetails.getTitle());
                        suoshuqingjingTv.setText(netSceneDetails.getScene_title());
                        locationTv.setText(netSceneDetails.getAddress());
                        timeTv.setText(TimestampToTimeUtils.getStandardDate(netSceneDetails.getCreated_on()));
                        ImageLoader.getInstance().displayImage(netSceneDetails.getUser_info().getAvatar_url(), userHead, options);
                        userName.setText(netSceneDetails.getUser_info().getNickname());
                        userInfo.setText(netSceneDetails.getUser_info().getUser_rank() + " | " + netSceneDetails.getUser_info().getSummary());
                        loveCount.setText(netSceneDetails.getLove_count() + "人赞过");
                        moreUser.setText(netSceneDetails.getLove_count() + "+");
                        desTv.setText(netSceneDetails.getDes());
                        //添加标签
                        addLabelToLinear(netSceneDetails.getTag_titles(), netSceneDetails.getTags());
                        viewCount.setText(netSceneDetails.getView_count());
                        loveCountTv.setText(netSceneDetails.getLove_count() + "");
                        commentNum.setText(netSceneDetails.getComment_count());
                        allComment.setText("全部" + netSceneDetails.getComment_count() + "条评论");
                        if (netSceneDetails.getLove_count() > 14) {
                            moreUser.setVisibility(View.VISIBLE);
                        } else {
                            moreUser.setVisibility(View.GONE);
                        }
                    } else {
                        Toast.makeText(SceneDetailActivity.this, netSceneDetails.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    break;
                case DataConstants.NET_FAIL:
                    dialog.dismiss();
                    Log.e("<<<", "请求失败 ");
                    break;
            }
        }
    };

    private void addProductToImg() {
        if (productList == null || productList.size() == 0) {
            return;
        }
        isShowAll = false;
        for (final SceneDetails.Product product : productList) {
//            Log.e("<<<", productList.toString());
            final LabelView labelView = new LabelView(SceneDetailActivity.this);
            TagItem tagItem = new TagItem();
            tagItem.setId(product.getId());
            tagItem.setName(product.getTitle());
            tagItem.setPrice(product.getPrice());
            labelView.init(tagItem);
            final RelativeLayout.LayoutParams labelLp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            labelView.pointOrAll(product.getX() <= 0.5, isShowAll);
            if (product.getX() > 0.5) {
                labelView.post(new Runnable() {
                    @Override
                    public void run() {
//                        Log.e("<<<", "x=" + (product.getX() * backgroundImg.getWidth()) + ",y=" + (product.getY() * backgroundImg.getMeasuredHeight()));
                        labelLp.leftMargin = (int) (product.getX() * backgroundImg.getWidth());
                        labelLp.topMargin = (int) (product.getY() * backgroundImg.getMeasuredHeight());
                    }
                });
            } else {
//                Log.e("<<<", "x=" + (product.getX() * backgroundImg.getWidth()) + ",y=" + (product.getY() * backgroundImg.getMeasuredHeight()));
                labelLp.leftMargin = (int) (product.getX() * backgroundImg.getWidth());
                labelLp.topMargin = (int) (product.getY() * backgroundImg.getMeasuredHeight());
            }
            labelView.setLayoutParams(labelLp);
            imgRelative.addView(labelView);
            labelView.wave();
            labelView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(SceneDetailActivity.this, GoodsDetailActivity.class);
                    String id = labelView.getTagInfo().getId();//商品id
                    intent.putExtra("id", id);
                    startActivity(intent);
                }
            });
        }
    }

    private void addLabelToLinear(final List<String> tagsTitleList, List<Integer> tagsList) {
        for (int i = 0; i < tagsTitleList.size(); i++) {
            View view = View.inflate(SceneDetailActivity.this, R.layout.view_horizontal_label_item, null);
            TextView textView = (TextView) view.findViewById(R.id.view_horizontal_label_item_tv);
            textView.setText(tagsTitleList.get(i));
            view.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) view.getLayoutParams();
            lp.rightMargin = DensityUtils.dp2px(SceneDetailActivity.this, 10);
            view.setLayoutParams(lp);
            view.setTag(tagsList.get(i));
            final int finalI = i;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(SceneDetailActivity.this, "跳转到搜索场景界面" + tagsTitleList.get(finalI), Toast.LENGTH_SHORT).show();
                }
            });
            labelLinear.addView(view);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_scenedetails_share:
                Toast.makeText(SceneDetailActivity.this, "分享场景", Toast.LENGTH_SHORT).show();
                break;
            case R.id.activity_scenedetails_back:
                onBackPressed();
                break;
            case R.id.activity_scenedetails_locationimg:
            case R.id.activity_scenedetails_location:
                Toast.makeText(SceneDetailActivity.this, "跳转到地图界面", Toast.LENGTH_SHORT).show();
                break;
            case R.id.activity_scenedetails_background:
                if (isShowAll) {
                    isShowAll = false;
                } else {
                    isShowAll = true;
                }
                for (int i = 0; i < imgRelative.getChildCount(); i++) {
                    View view = imgRelative.getChildAt(i);
                    if (view instanceof LabelView) {
                        LabelView labelView = (LabelView) view;
                        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) labelView.getLayoutParams();
                        labelView.pointOrAll(lp.leftMargin <= backgroundImg.getWidth() / 2, isShowAll);
                    }
                }
                break;
            case R.id.activity_scenedetails_love:
            case R.id.activity_scenedetails_lovetv:
                if (!LoginInfo.isUserLogin()) {
                    Toast.makeText(SceneDetailActivity.this, "请先登录", Toast.LENGTH_SHORT).show();
                    MainApplication.which_activity = DataConstants.SceneDetailActivity;
                    startActivity(new Intent(SceneDetailActivity.this, OptRegisterLoginActivity.class));
                    return;
                }
                dialog.show();
                switch (isLove) {
                    case 1:
                        DataPaser.cancelLoveScene(id, handler);
                        break;
                    default:
                        DataPaser.loveScene(id, handler);
                        break;
                }
                break;
            case R.id.activity_scenedetails_commentimg:
            case R.id.activity_scenedetails_commentcount:
            case R.id.activity_scenedetails_allcomment:
            case R.id.activity_scenedetails_morecomment:
                Intent intent = new Intent(SceneDetailActivity.this, CommentListActivity.class);
                intent.putExtra("target_id", id);
                intent.putExtra("type", 12 + "");
                startActivity(intent);
                break;
            case R.id.activity_scenedetails_moreuser:
                break;
            case R.id.activity_scenedetails_more:
                Intent intent1 = new Intent(SceneDetailActivity.this, ReportActivity.class);
                intent1.putExtra("target_id", id);
                intent1.putExtra("type", 4 + "");
                startActivity(intent1);
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

    @Override
    protected void onStart() {
        super.onStart();
        for (int i = 0; i < imgRelative.getChildCount(); i++) {
            View view = imgRelative.getChildAt(i);
            if (view instanceof LabelView) {
                LabelView labelView = (LabelView) view;
                labelView.wave();
            }
        }
    }

    @Override
    protected void onStop() {
        for (int i = 0; i < imgRelative.getChildCount(); i++) {
            View view = imgRelative.getChildAt(i);
            if (view instanceof LabelView) {
                LabelView labelView = (LabelView) view;
                labelView.stopAnim();
            }
        }
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        //cancelNet();
        unregisterReceiver(sceneDetailReceiver);
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
            handler = null;
        }
        super.onDestroy();
    }

    //广播接收器
    private BroadcastReceiver sceneDetailReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            dialog.show();
            DataPaser.sceneDetails(id + "", handler);
        }
    };


    @Override
    public boolean onTouch(View v, MotionEvent event) {
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                startP.x = event.getX();
//                startP.y = event.getY();
//                break;
//            case MotionEvent.ACTION_MOVE:
//                nowP.x = event.getX();
//                nowP.y = event.getY();
//                RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) loveRelative.getLayoutParams();
//                if (nowP.y > startP.y) {
//                    if (lp.bottomMargin > -loveRelative.getMeasuredHeight() && lp.bottomMargin <= 0) {
//                        lp.bottomMargin = (int) (startP.y - nowP.y);
//                    }
//                    backImg.setVisibility(View.VISIBLE);
//                    shareImg.setVisibility(View.VISIBLE);
//                } else if (nowP.y < startP.y) {
//                    if (lp.bottomMargin >= -loveRelative.getMeasuredHeight() && lp.bottomMargin < 0) {
//                        lp.bottomMargin = (int) (startP.y - nowP.y - loveRelative.getMeasuredHeight());
//                    }
//                    backImg.setVisibility(View.GONE);
//                    shareImg.setVisibility(View.GONE);
//                }
//                if (lp.bottomMargin > 0) {
//                    lp.bottomMargin = 0;
//                } else if (lp.bottomMargin < -loveRelative.getMeasuredHeight()) {
//                    lp.bottomMargin = -loveRelative.getMeasuredHeight();
//                }
//                loveRelative.setLayoutParams(lp);
//                break;
//        }
        return false;
    }

    private PointF startP = new PointF();
    private PointF nowP = new PointF();
}
