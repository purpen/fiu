package com.taihuoniao.fineix.qingjingOrSceneDetails;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import com.taihuoniao.fineix.beans.SceneDetails;
import com.taihuoniao.fineix.beans.TagItem;
import com.taihuoniao.fineix.main.MainApplication;
import com.taihuoniao.fineix.network.DataConstants;
import com.taihuoniao.fineix.network.DataPaser;
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
public class SceneDetailActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    //上个界面传递过来的场景id
    private String id;
    //界面下的控件
    private RelativeLayout imgRelative;
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


    public SceneDetailActivity() {
        super(0);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_scenedetails);
        imgRelative = (RelativeLayout) findViewById(R.id.activity_scenedetails_imgrelative);
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
        Log.e("<<<", "id=" + id);
        ViewGroup.LayoutParams lp = backgroundImg.getLayoutParams();
        lp.width = MainApplication.getContext().getScreenWidth();
        lp.height = lp.width * 16 / 9;
        backgroundImg.setLayoutParams(lp);
        backgroundImg.setFocusable(true);
        backgroundImg.setFocusableInTouchMode(true);
        backgroundImg.requestFocus();
        backgroundImg.setOnClickListener(this);
        commentImg.setOnClickListener(this);
        commentNum.setOnClickListener(this);
        moreImg.setOnClickListener(this);
        headList = new ArrayList<>();
        sceneDetailUserHeadAdapter = new SceneDetailUserHeadAdapter(SceneDetailActivity.this, headList);
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
    }

    @Override
    protected void requestNet() {
        dialog.show();
        DataPaser.sceneDetails(id + "", handler);
        DataPaser.commentsList(1 + "", id, 12 + "", handler);
        DataPaser.commonList(1 + "", 14 + "", id, "sight", "love", handler);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DataConstants.COMMON_LIST:
                    dialog.dismiss();
                    Log.e("<<<", "通用列表");
                    CommonBean netCommonBean = (CommonBean) msg.obj;
                    if (netCommonBean.isSuccess()) {
                        headList.addAll(netCommonBean.getData().getRows());
                        if (headList.size() > 12) {
                            moreUser.setVisibility(View.VISIBLE);
                        } else {
                            moreUser.setVisibility(View.GONE);
                        }
                        sceneDetailUserHeadAdapter.notifyDataSetChanged();
                    }
                    break;
                case DataConstants.COMMENTS_LIST:
                    dialog.dismiss();
                    Log.e("<<<", "评论列表");
                    CommentsBean netCommentBean = (CommentsBean) msg.obj;
                    if (netCommentBean.isSuccess()) {
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
                    Log.e("<<<", "场景详情");
                    SceneDetails netSceneDetails = (SceneDetails) msg.obj;
                    if (netSceneDetails.isSuccess()) {
                        Log.e("<<<", "url=" + netSceneDetails.getCover_url());
                        ImageLoader.getInstance().displayImage(netSceneDetails.getCover_url(), backgroundImg);
                        //用户是否已经点赞

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
                        desTv.setText(netSceneDetails.getDes());
                        //添加标签
                        addLabelToLinear(netSceneDetails.getTag_titles(), netSceneDetails.getTags());
                        viewCount.setText(netSceneDetails.getView_count());
                        loveCountTv.setText(netSceneDetails.getLove_count());
                        commentNum.setText(netSceneDetails.getComment_count());
                        allComment.setText("全部" + netSceneDetails.getComment_count() + "条评论");
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
            Log.e("<<<", productList.toString());
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
                    Toast.makeText(SceneDetailActivity.this, "跳转到商品详情", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void addLabelToLinear(List<String> tagsTitleList, List<Integer> tagsList) {
        for (int i = 0; i < tagsTitleList.size(); i++) {
            View view = View.inflate(SceneDetailActivity.this, R.layout.view_horizontal_label_item, null);
            TextView textView = (TextView) view.findViewById(R.id.view_horizontal_label_item_tv);
            textView.setText(tagsTitleList.get(i));
            view.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) view.getLayoutParams();
            lp.rightMargin = DensityUtils.dp2px(SceneDetailActivity.this, 10);
            view.setLayoutParams(lp);
            view.setTag(tagsList.get(i));
            labelLinear.addView(view);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
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
                Toast.makeText(SceneDetailActivity.this, "需要登录", Toast.LENGTH_SHORT).show();
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

    @Override
    protected void onStart() {
        super.onStart();
        for (int i = 0; i < imgRelative.getChildCount(); i++) {
            View view = imgRelative.getChildAt(i);
            if (view instanceof LabelView) {
                LabelView labelView = (LabelView) view;
                labelView.wave();
                Log.e("<<<", "开始动画");
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
                Log.e("<<<", "动画结束");
            }
        }
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        //cancelNet();
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
            Log.e("<<<", "销毁handler中还未接收到的数据");
            handler = null;
        }
        super.onDestroy();
    }

}
