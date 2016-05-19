package com.taihuoniao.fineix.qingjingOrSceneDetails;

import android.animation.ObjectAnimator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PointF;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.GoodListAdapter;
import com.taihuoniao.fineix.adapters.SceneDetailCommentAdapter;
import com.taihuoniao.fineix.adapters.SceneDetailProductListAdapter;
import com.taihuoniao.fineix.adapters.SceneDetailUserHeadAdapter;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.beans.CommentsBean;
import com.taihuoniao.fineix.beans.CommonBean;
import com.taihuoniao.fineix.beans.LoginInfo;
import com.taihuoniao.fineix.beans.ProductAndSceneListBean;
import com.taihuoniao.fineix.beans.ProductBean;
import com.taihuoniao.fineix.beans.ProductListBean;
import com.taihuoniao.fineix.beans.SceneDetails;
import com.taihuoniao.fineix.beans.SceneLoveBean;
import com.taihuoniao.fineix.beans.TagItem;
import com.taihuoniao.fineix.main.MainApplication;
import com.taihuoniao.fineix.network.DataConstants;
import com.taihuoniao.fineix.network.DataPaser;
import com.taihuoniao.fineix.product.GoodsDetailActivity;
import com.taihuoniao.fineix.scene.SearchActivity;
import com.taihuoniao.fineix.user.FocusFansActivity;
import com.taihuoniao.fineix.user.OptRegisterLoginActivity;
import com.taihuoniao.fineix.user.UserCenterActivity;
import com.taihuoniao.fineix.utils.DensityUtils;
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
    private View activity_view;
    //    private ListView nearListView;
    private ScrollView scrollView;
    private LinearLayout loveRelative;
    private ImageView backImg;
    private ImageView shareImg;
    private RelativeLayout imgRelative;
    private ImageView backgroundImg;
    private LinearLayout titleLinear;
    private TextView changjingTitle;
    private TextView suoshuqingjingTv;
    private ImageView locationImg;
    private TextView locationTv;
    private TextView timeTv;
    private LinearLayout leftLabelLinear;
    private ImageView userHead;
    private TextView userName;
    private TextView userInfo;
    //    private LinearLayout bottomLinear;
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
    private LinearLayout commentsLinear;
    private ListViewForScrollView commentsListView;
    private List<CommentsBean.CommentItem> commentList;
    private SceneDetailCommentAdapter sceneDetailCommentAdapter;
    private TextView allComment;
    private ImageView moreComment;
    private ImageView love;
    private TextView loveTv;
    private ListViewForScrollView productListView;
    private ListViewForScrollView nearProductListView;
    private PopupWindow popupWindow;
    //popupwindow下的控件
    private TextView shareTv;
    private TextView jubaoTv;
    private TextView cancelTv;
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
    private SceneDetails netScene = null;//网络请求返回数据
    private SceneDetails.UserInfo netUserInfo = null;//网络请求返回用户信息
    private String[] location = null;//网络请求返回的经纬度
    //场景下的商品
    private List<ProductAndSceneListBean.ProductAndSceneItem> sceneProductList;
    private SceneDetailProductListAdapter sceneProductAdapter;
    //相近产品
    private int currentTime = 1;
    private List<ProductListBean> nearProductList;
    private GoodListAdapter goodListAdapter;

    public SceneDetailActivity() {
        super(0);
    }

    @Override
    protected void initView() {
        activity_view = View.inflate(SceneDetailActivity.this, R.layout.activity_scenedetails, null);
        setContentView(activity_view);
//        nearListView = (ListView) findViewById(R.id.activity_scenedetails_nearlistview);
//        View header = View.inflate(SceneDetailActivity.this,R.layout.header_scenedetails,null);
        scrollView = (ScrollView) findViewById(R.id.activity_scenedetails_scrollview);
        loveRelative = (LinearLayout) findViewById(R.id.activity_scenedetails_loverelative);
        backImg = (ImageView) findViewById(R.id.activity_scenedetails_back);
        shareImg = (ImageView) findViewById(R.id.activity_scenedetails_share);
        imgRelative = (RelativeLayout) findViewById(R.id.activity_scenedetails_imgrelative);
        backgroundImg = (ImageView) findViewById(R.id.activity_scenedetails_background);
        titleLinear = (LinearLayout) findViewById(R.id.activity_scenedetails_titlelinear);
        changjingTitle = (TextView) findViewById(R.id.activity_scenedetails_changjing_title);
        suoshuqingjingTv = (TextView) findViewById(R.id.activity_scenedetails_suoshuqingjing);
        locationImg = (ImageView) findViewById(R.id.activity_scenedetails_locationimg);
        locationTv = (TextView) findViewById(R.id.activity_scenedetails_location);
        timeTv = (TextView) findViewById(R.id.activity_scenedetails_time);
        leftLabelLinear = (LinearLayout) findViewById(R.id.activity_scenedetails_left_label);
        userHead = (ImageView) findViewById(R.id.activity_scenedetails_userhead);
        userName = (TextView) findViewById(R.id.activity_scenedetails_username);
        userInfo = (TextView) findViewById(R.id.activity_scenedetails_userinfo);
//        bottomLinear = (LinearLayout) findViewById(R.id.activity_scenedetails_bottomlinear);
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
        commentsLinear = (LinearLayout) findViewById(R.id.activity_scenedetails_commentlinear);
        commentsListView = (ListViewForScrollView) findViewById(R.id.activity_scenedetails_commentlistview);
        allComment = (TextView) findViewById(R.id.activity_scenedetails_allcomment);
        moreComment = (ImageView) findViewById(R.id.activity_scenedetails_morecomment);
        love = (ImageView) findViewById(R.id.activity_scenedetails_love);
        loveTv = (TextView) findViewById(R.id.activity_scenedetails_lovetv);
        productListView = (ListViewForScrollView) findViewById(R.id.activity_scenedetails_productlistview);
        nearProductListView = (ListViewForScrollView) findViewById(R.id.activity_scenedetails_nearproductlistview);
        dialog = new WaittingDialog(SceneDetailActivity.this);
        initPopupWindow();
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
        ViewGroup.LayoutParams rLp = imgRelative.getLayoutParams();
        rLp.width = lp.width;
        rLp.height = lp.height;
        imgRelative.setLayoutParams(rLp);
//        FrameLayout.LayoutParams bLp = (FrameLayout.LayoutParams) bottomLinear.getLayoutParams();
//        bLp.topMargin = lp.height;
//        bottomLinear.setLayoutParams(bLp);
        backgroundImg.setFocusable(true);
        backgroundImg.setFocusableInTouchMode(true);
        backgroundImg.requestFocus();
        backImg.setOnClickListener(this);
        shareImg.setOnClickListener(this);
        backgroundImg.setOnClickListener(this);
        locationImg.setOnClickListener(this);
        locationTv.setOnClickListener(this);
        leftLabelLinear.setOnClickListener(this);
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
        commentsListView.setOnItemClickListener(this);
//        commentsLinear.setOnClickListener(this);
        allComment.setOnClickListener(this);
        moreComment.setOnClickListener(this);
        loveRelative.setOnClickListener(this);
        love.setOnClickListener(this);
        loveTv.setOnClickListener(this);
        sceneProductList = new ArrayList<>();
        sceneProductAdapter = new SceneDetailProductListAdapter(SceneDetailActivity.this, sceneProductList);
        productListView.setAdapter(sceneProductAdapter);
        nearProductList = new ArrayList<>();
        goodListAdapter = new GoodListAdapter(SceneDetailActivity.this, nearProductList, null);
        nearProductListView.setAdapter(goodListAdapter);
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
        DataPaser.commentsList(1 + "", 3 + "", id, null, 12 + "", handler);
        DataPaser.commonList(1 + "", 14 + "", id, null, "sight", "love", handler);
        DataPaser.productAndScene(1 + "", 4 + "", id, null, handler);
    }

    private void initPopupWindow() {
        WindowManager windowManager = SceneDetailActivity.this.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        View popup_view = View.inflate(SceneDetailActivity.this, R.layout.popup_scene_details_more, null);
        shareTv = (TextView) popup_view.findViewById(R.id.popup_scene_detail_more_share);
        jubaoTv = (TextView) popup_view.findViewById(R.id.popup_scene_detail_more_jubao);
        cancelTv = (TextView) popup_view.findViewById(R.id.popup_scene_detail_more_cancel);
        popupWindow = new PopupWindow(popup_view, display.getWidth(), ViewGroup.LayoutParams.WRAP_CONTENT, true);
        // 设置动画效果
        popupWindow.setAnimationStyle(R.style.popupwindow_style);
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        shareTv.setOnClickListener(this);
        jubaoTv.setOnClickListener(this);
        cancelTv.setOnClickListener(this);

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams params = getWindow().getAttributes();
                params.alpha = 1f;
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                getWindow().setAttributes(params);

            }
        });
        popupWindow.setBackgroundDrawable(ContextCompat.getDrawable(SceneDetailActivity.this,
                R.color.white));
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });
    }

    private void showPopup() {
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.alpha = 0.4f;
        getWindow().setAttributes(params);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        popupWindow.showAtLocation(activity_view, Gravity.BOTTOM, 0, 0);
    }


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DataConstants.ADD_PRODUCT_LIST:
                    ProductBean netProductBean = (ProductBean) msg.obj;
                    if (netProductBean.isSuccess() && currentTime == 1) {
                        if (netProductBean.getList().size() <= 0) {
                            return;
                        }
                        StringBuilder tags = new StringBuilder();
                        for (int i = 0; i < netProductBean.getList().size(); i++) {
                            List<String> categoryList = netProductBean.getList().get(i).getCategory_tags();
                            if (categoryList != null && categoryList.size() > 0) {
                                for (int j = 0; j < categoryList.size(); j++) {
                                    tags.append(",").append(netProductBean.getList().get(i).getCategory_tags().get(j));
                                }
                                if (tags.length() > 0) {
                                    tags.deleteCharAt(0);
                                }
                            }
                        }
                        currentTime++;
                        DataPaser.getProductList(null, null, null, 1 + "", 3 + "", null, tags.toString(), null, null, handler);
                    } else if (netProductBean.isSuccess() && currentTime == 2) {
                        dialog.dismiss();
                        nearProductList.clear();
                        nearProductList.addAll(netProductBean.getList());
                        goodListAdapter.notifyDataSetChanged();
                    } else {
                        dialog.dismiss();
                        Toast.makeText(SceneDetailActivity.this, netProductBean.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    break;
                case DataConstants.PRODUCT_AND_SCENE:
                    ProductAndSceneListBean netProScene = (ProductAndSceneListBean) msg.obj;
                    if (netProScene.isSuccess()) {
                        sceneProductList.clear();
                        sceneProductList.addAll(netProScene.getData().getRows());
                        sceneProductAdapter.notifyDataSetChanged();
                    }
                    break;
                case DataConstants.CANCEL_LOVE_SCENE:
                    SceneLoveBean netSceneLoveBean1 = (SceneLoveBean) msg.obj;
                    if (netSceneLoveBean1.isSuccess()) {
//                        Toast.makeText(SceneDetailActivity.this, netSceneLoveBean1.getData().getLove_count() + "", Toast.LENGTH_SHORT).show();
                        DataPaser.commonList(1 + "", 14 + "", id, null, "sight", "love", handler);
                        isLove = 0;
                        love.setImageResource(R.mipmap.like_height_43px);
                        loveTv.setText("点赞");
                        loveCount.setText(String.format("%d人赞过", netSceneLoveBean1.getData().getLove_count()));
                        loveCountTv.setText(String.format("%d", netSceneLoveBean1.getData().getLove_count()));
                        moreUser.setText(String.format("%d+", netSceneLoveBean1.getData().getLove_count()));
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
//                    Toast.makeText(SceneDetailActivity.this, netSceneLoveBean.getData().getLove_count() + "", Toast.LENGTH_SHORT).show();
                    if (netSceneLoveBean.isSuccess()) {
                        DataPaser.commonList(1 + "", 14 + "", id, null, "sight", "love", handler);
                        isLove = 1;
                        love.setImageResource(R.mipmap.love_yes);
                        loveTv.setText("取消点赞");
                        loveCount.setText(String.format("%d人赞过", netSceneLoveBean.getData().getLove_count()));
                        loveCountTv.setText(String.format("%d", netSceneLoveBean.getData().getLove_count()));
                        moreUser.setText(String.format("%d+", netSceneLoveBean.getData().getLove_count()));
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
                        netScene = netSceneDetails;
                        ImageLoader.getInstance().displayImage(netSceneDetails.getCover_url(), backgroundImg);
                        //用户是否已经点赞
                        isLove = netSceneDetails.getIs_love();
                        switch (isLove) {
                            case 1:
                                love.setImageResource(R.mipmap.love_yes);
                                loveTv.setText("取消点赞");
                                break;
                            default:
                                love.setImageResource(R.mipmap.like_height_43px);
                                loveTv.setText("点赞");
                                break;
                        }
                        //场景上的商品
                        productList = netSceneDetails.getProduct();
                        getNearProductList();
                        //添加商品
                        addProductToImg();
                        changjingTitle.setText(netSceneDetails.getTitle());
                        if (changjingTitle.getText().length() < 8) {
                            changjingTitle.setTextSize(40);
                        } else if (changjingTitle.getText().length() >= 13) {
                            changjingTitle.setTextSize(20);
                        } else {
                            changjingTitle.setTextSize(30);
                        }
                        suoshuqingjingTv.setText(netSceneDetails.getScene_title());
                        locationTv.setText(netSceneDetails.getAddress());
                        timeTv.setText(netSceneDetails.getCreated_at());
                        netUserInfo = netSceneDetails.getUser_info();
                        ImageLoader.getInstance().displayImage(netSceneDetails.getUser_info().getAvatar_url(), userHead, options);
                        userName.setText(netSceneDetails.getUser_info().getNickname());
                        isSpertAndSummary(userInfo, netSceneDetails.getUser_info().getIs_expert(), netSceneDetails.getUser_info().getSummary());
                        loveCount.setText(String.format("%d人赞过", netSceneDetails.getLove_count()));
                        moreUser.setText(String.format("%d+", netSceneDetails.getLove_count()));
                        desTv.setText(netSceneDetails.getDes());
                        //添加标签
                        addLabelToLinear(netSceneDetails.getTag_titles(), netSceneDetails.getTags());

                        viewCount.setText(netSceneDetails.getView_count());
                        loveCountTv.setText(String.format("%d", netSceneDetails.getLove_count()));
                        commentNum.setText(netSceneDetails.getComment_count());
                        allComment.setText(String.format("全部%s条评论", netSceneDetails.getComment_count()));
                        if (netSceneDetails.getLove_count() > 14) {
                            moreUser.setVisibility(View.VISIBLE);
                        } else {
                            moreUser.setVisibility(View.GONE);
                        }
                        location = netSceneDetails.getLocation();
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

    //获取相近产品
    private void getNearProductList() {
        if (productList == null || productList.size() <= 0) {
            return;
        }
        StringBuilder ids = new StringBuilder();
        for (int i = 0; i < productList.size(); i++) {
            ids.append(",").append(productList.get(i).getId());
        }
        ids.deleteCharAt(0);
        DataPaser.getProductList(null, null, null, 1 + "", 8 + "", ids.toString(), null, null, null, handler);
    }

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
            tagItem.setPrice("¥" + product.getPrice());
            labelView.init(tagItem);
            final RelativeLayout.LayoutParams labelLp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            labelView.pointOrAll(false, isShowAll);
            labelLp.leftMargin = (int) (product.getX() * MainApplication.getContext().getScreenWidth());
            labelLp.topMargin = (int) (product.getY() * MainApplication.getContext().getScreenWidth() * 16 / 9);
            labelView.setLayoutParams(labelLp);
            imgRelative.addView(labelView);
            labelView.wave();
            labelView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (labelView.nameLeft.getVisibility() == View.INVISIBLE) {
                        return;
                    }
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
                    Intent intent = new Intent(SceneDetailActivity.this, SearchActivity.class);
                    intent.putExtra("q", tagsTitleList.get(finalI));
                    intent.putExtra("t", "9");
                    startActivity(intent);
                }
            });
            labelLinear.addView(view);
        }
    }

    private void isSpertAndSummary(TextView userInfo, String isSpert, String summary) {
        if ("1".equals(isSpert) && (summary == null || "null".equals(summary))) {
            userInfo.setText("达人");
        } else if ("1".equals(isSpert)) {
            userInfo.setText(String.format("%s | %s", "达人", summary));
        } else if (summary == null || "null".equals(summary)) {
            userInfo.setText("");
        } else {
            userInfo.setText(summary);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_scenedetails_left_label:
                if (netUserInfo == null) {
                    dialog.show();
                    DataPaser.sceneDetails(id, handler);
                    return;
                }
                Intent intent = new Intent(SceneDetailActivity.this, UserCenterActivity.class);
                intent.putExtra(FocusFansActivity.USER_ID_EXTRA, Long.parseLong(netUserInfo.getUser_id()));
                startActivity(intent);
                break;
            case R.id.popup_scene_detail_more_share:
            case R.id.activity_scenedetails_share:
                Toast.makeText(SceneDetailActivity.this, "分享场景", Toast.LENGTH_SHORT).show();
                break;
            case R.id.popup_scene_detail_more_jubao:
                Intent intent1 = new Intent(SceneDetailActivity.this, ReportActivity.class);
                intent1.putExtra("target_id", id);
                intent1.putExtra("type", 4 + "");
                startActivity(intent1);
                break;
            case R.id.popup_scene_detail_more_cancel:
                popupWindow.dismiss();
                break;

            case R.id.activity_scenedetails_back:
                onBackPressed();
                break;
            case R.id.activity_scenedetails_locationimg:
            case R.id.activity_scenedetails_location:
//                跳转到地图界面，查看附近的场景
                if (location == null) {
                    dialog.show();
                    DataPaser.sceneDetails(id, handler);
                    return;
                }
                if(netScene==null){
                    dialog.show();
                    DataPaser.sceneDetails(id,handler);
                    return;
                }
                String address = netScene.getAddress();
                Toast.makeText(SceneDetailActivity.this, "跳转到地图界面" + location, Toast.LENGTH_SHORT).show();
                break;
            case R.id.activity_scenedetails_background:
                isShowAll = !isShowAll;
                for (int i = 0; i < imgRelative.getChildCount(); i++) {
                    View view = imgRelative.getChildAt(i);
                    if (view instanceof LabelView) {
                        LabelView labelView = (LabelView) view;
                        labelView.pointOrAll(false, isShowAll);
                    }
                }
                RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) titleLinear.getLayoutParams();
                Log.e("<<<动画", "height=" + titleLinear.getHeight() + ",bottomMargin=" + lp.bottomMargin);
                if (isShowAll) {
                    ObjectAnimator.ofFloat(titleLinear, "translationY", titleLinear.getMeasuredHeight() + lp.bottomMargin).setDuration(300).start();
                } else {
                    ObjectAnimator.ofFloat(titleLinear, "translationY", 0).setDuration(300).start();
                }
                break;
            case R.id.activity_scenedetails_loverelative:
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
//            case R.id.activity_scenedetails_commentlinear:
            case R.id.activity_scenedetails_commentimg:
            case R.id.activity_scenedetails_commentcount:
            case R.id.activity_scenedetails_allcomment:
            case R.id.activity_scenedetails_morecomment:
                Log.e("<<<点击", "点击评论");
                if (netScene == null) {
                    dialog.show();
                    DataPaser.sceneDetails(id + "", handler);
                    return;
                }
                Intent intent2 = new Intent(SceneDetailActivity.this, CommentListActivity.class);
                intent2.putExtra("target_id", id);
                intent2.putExtra("type", 12 + "");
                intent2.putExtra("target_user_id", netScene.getUser_info().getUser_id());
                startActivity(intent2);
                break;
            case R.id.activity_scenedetails_moreuser:
                break;
            case R.id.activity_scenedetails_more:
                showPopup();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.activity_scenedetails_grid:
                Log.e("<<<点击用户头像", "user-id=" + headList.get(position).getUser().getUser_id());
                Intent intent1 = new Intent(SceneDetailActivity.this, UserCenterActivity.class);
                intent1.putExtra(FocusFansActivity.USER_ID_EXTRA, Long.parseLong(headList.get(position).getUser().getUser_id()));
                startActivity(intent1);
                break;
            case R.id.activity_scenedetails_commentlistview:
                if (netScene == null) {
                    dialog.show();
                    DataPaser.sceneDetails(id + "", handler);
                    return;
                }
                Intent intent2 = new Intent(SceneDetailActivity.this, CommentListActivity.class);
                intent2.putExtra("target_id", this.id);
                intent2.putExtra("type", 12 + "");
                intent2.putExtra("target_user_id", netScene.getUser_info().getUser_id());
                startActivity(intent2);
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
//                RelativeLayout.LayoutParams lpd = (RelativeLayout.LayoutParams) loveRelative.getLayoutParams();
//                cha = startP.y + lpd.bottomMargin;
//                break;
//            case MotionEvent.ACTION_MOVE:
//                nowP.x = event.getX();
//                nowP.y = event.getY();
//                RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) loveRelative.getLayoutParams();
//                lp.bottomMargin = (int) (cha - nowP.y);
//                if (nowP.y > startP.y) {
////                    backImg.setVisibility(View.VISIBLE);
////                    createImg.setVisibility(View.VISIBLE);
//                } else if (nowP.y < startP.y) {
////                    backImg.setVisibility(View.GONE);
////                    createImg.setVisibility(View.GONE);
//                }
//                if (lp.bottomMargin > 0) {
//                    lp.bottomMargin = 0;
//                    cha = nowP.y + lp.bottomMargin;
//                } else if (lp.bottomMargin < -loveRelative.getMeasuredHeight()) {
//                    lp.bottomMargin = -loveRelative.getMeasuredHeight();
//                    cha = nowP.y + lp.bottomMargin;
//                }
//                loveRelative.setLayoutParams(lp);
//                break;
//        }
        return false;
    }

    private double cha;//手指按下的时候，y与bottomMargin的差值
    private PointF startP = new PointF();
    private PointF nowP = new PointF();
}
