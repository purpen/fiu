package com.taihuoniao.fineix.qingjingOrSceneDetails;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.baidu.mapapi.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.GoodListAdapter;
import com.taihuoniao.fineix.adapters.SceneDetailCommentAdapter;
import com.taihuoniao.fineix.adapters.SceneDetailUserHeadAdapter;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.base.NetBean;
import com.taihuoniao.fineix.beans.CommentsBean;
import com.taihuoniao.fineix.beans.CommonBean;
import com.taihuoniao.fineix.beans.LoginInfo;
import com.taihuoniao.fineix.beans.ProductBean;
import com.taihuoniao.fineix.beans.SceneDetailsBean;
import com.taihuoniao.fineix.beans.SceneLoveBean;
import com.taihuoniao.fineix.beans.TagItem;
import com.taihuoniao.fineix.main.MainApplication;
import com.taihuoniao.fineix.map.MapNearByCJActivity;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.DataConstants;
import com.taihuoniao.fineix.product.GoodsDetailActivity;
import com.taihuoniao.fineix.scene.CreateQJActivity;
import com.taihuoniao.fineix.scene.SearchActivity;
import com.taihuoniao.fineix.user.FocusActivity;
import com.taihuoniao.fineix.user.OptRegisterLoginActivity;
import com.taihuoniao.fineix.user.UserCenterActivity;
import com.taihuoniao.fineix.utils.DensityUtils;
import com.taihuoniao.fineix.utils.LoginCompleteUtils;
import com.taihuoniao.fineix.utils.SceneTitleSetUtils;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.view.GridViewForScrollView;
import com.taihuoniao.fineix.view.LabelView;
import com.taihuoniao.fineix.view.ListViewForScrollView;
import com.taihuoniao.fineix.view.MyScrollView;
import com.taihuoniao.fineix.view.WaittingDialog;
import com.taihuoniao.fineix.view.roundImageView.RoundedImageView;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by taihuoniao on 2016/4/19.
 */
public class SceneDetailActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener, MyScrollView.OnScrollListener {
    //上个界面传递过来的场景id
    private String id;
    private boolean isCreate;//判断是不是从创建界面跳转过来的
    //界面下的控件
    private View activity_view;
    private MyScrollView scrollView;
    private RelativeLayout titleRelative;
    private ImageView backImg;
    private ImageView shareImg;
    private RelativeLayout imgRelative;
    private RelativeLayout container;
    private ImageView backgroundImg;
    private LinearLayout titleLinear;
    private FrameLayout frameLayout;
    private ImageView titleImg;
    private TextView changjingTitle;
    private TextView suoshuqingjingTv;
    private ImageView locationImg;
    private TextView locationTv;
    private TextView timeTv;
    private LinearLayout leftLabelLinear;
    private RoundedImageView userHead;
    private RoundedImageView vImg;
    private TextView userName;
    private TextView userInfo;
    //    private LinearLayout bottomLinear;
    private TextView loveCount;
    private LinearLayout goneLayout;
    private TextView desTv;
    private LinearLayout labelLinear;
    private TextView viewCount;
    private TextView loveCountTv;
    private ImageView commentImg;
    private TextView commentNum;
    private ImageView moreImg;
    private RelativeLayout headRelative;
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
    //    private ImageView love;
//    private TextView loveTv;
    private LinearLayout productLinear;
    private LinearLayout nearLinear;
    private ListViewForScrollView productListView;
    private ListViewForScrollView nearProductListView;
    private PopupWindow sharePop, popupWindow;
    //popupwindow下的控件
    private TextView pinglunTv;
    private TextView shareTv;
    private LinearLayout bianjiLinear;
    private TextView jubaoTv;
    private TextView cancelTv;
    //网络请求对话框
    private WaittingDialog dialog;
    //图片加载
    private DisplayImageOptions options, options750_1334;
    //图片上的商品
    private List<SceneDetailsBean.DataBean.ProductBean> productList;
    //是否显示标签和价格的标识
    private boolean isShowAll = false;
    //当前用户是否已经点赞
    private int isLove = 0;//0是未点赞，1是已点赞
    private SceneDetailsBean netScene = null;//网络请求返回数据
    private SceneDetailsBean.DataBean.UserInfoBean netUserInfo = null;//网络请求返回用户信息
    private SceneDetailsBean.DataBean.LocationBean location = null;//网络请求返回的经纬度
    //场景下的商品
    private List<ProductBean.ProductListItem> sceneProductList;
    private GoodListAdapter sceneProductAdapter;
    //相近产品
    private int currentTime = 1;
    private List<ProductBean.ProductListItem> nearProductList;
    private GoodListAdapter goodListAdapter;
    public static SceneDetailActivity instance;

    public SceneDetailActivity() {
        super(0);
    }

    @Override
    protected void initView() {
        instance = SceneDetailActivity.this;
        activity_view = View.inflate(SceneDetailActivity.this, R.layout.activity_scenedetails, null);
        setContentView(activity_view);
        scrollView = (MyScrollView) findViewById(R.id.activity_scenedetails_scrollview);
        titleRelative = (RelativeLayout) findViewById(R.id.activity_scenedetails_title);
        backImg = (ImageView) findViewById(R.id.activity_scenedetails_back);
        shareImg = (ImageView) findViewById(R.id.activity_scenedetails_share);
        imgRelative = (RelativeLayout) findViewById(R.id.activity_scenedetails_imgrelative);
        container = (RelativeLayout) findViewById(R.id.activity_scenedetails_container);
        backgroundImg = (ImageView) findViewById(R.id.activity_scenedetails_background);
        titleLinear = (LinearLayout) findViewById(R.id.activity_scenedetails_titlelinear);
        frameLayout = (FrameLayout) findViewById(R.id.activity_scenedetails_framelayout);
        titleImg = (ImageView) findViewById(R.id.activity_scenedetails_title_img);
        changjingTitle = (TextView) findViewById(R.id.activity_scenedetails_changjing_title);
        suoshuqingjingTv = (TextView) findViewById(R.id.activity_scenedetails_suoshuqingjing);
        locationImg = (ImageView) findViewById(R.id.activity_scenedetails_locationimg);
        locationTv = (TextView) findViewById(R.id.activity_scenedetails_location);
        timeTv = (TextView) findViewById(R.id.activity_scenedetails_time);
        leftLabelLinear = (LinearLayout) findViewById(R.id.activity_scenedetails_left_label);
        userHead = (RoundedImageView) findViewById(R.id.activity_scenedetails_userhead);
        vImg = (RoundedImageView) findViewById(R.id.riv_auth);
        userName = (TextView) findViewById(R.id.activity_scenedetails_username);
        userInfo = (TextView) findViewById(R.id.activity_scenedetails_userinfo);
        loveCount = (TextView) findViewById(R.id.activity_scenedetails_lovecount);
        goneLayout = (LinearLayout) findViewById(R.id.activity_scenedetails_gonelinear);
        desTv = (TextView) findViewById(R.id.activity_scenedetails_changjing_des);
        labelLinear = (LinearLayout) findViewById(R.id.activity_scenedetails_labellinear);
        viewCount = (TextView) findViewById(R.id.activity_scenedetails_viewcount);
        loveCountTv = (TextView) findViewById(R.id.activity_scenedetails_love_count);
        commentImg = (ImageView) findViewById(R.id.activity_scenedetails_commentimg);
        commentNum = (TextView) findViewById(R.id.activity_scenedetails_commentcount);
        moreImg = (ImageView) findViewById(R.id.activity_scenedetails_more);
        headRelative = (RelativeLayout) findViewById(R.id.activity_scenedetails_head_relative);
        userHeadGrid = (GridViewForScrollView) findViewById(R.id.activity_scenedetails_grid);
        moreUser = (TextView) findViewById(R.id.activity_scenedetails_moreuser);
        commentsLinear = (LinearLayout) findViewById(R.id.activity_scenedetails_commentlinear);
        commentsListView = (ListViewForScrollView) findViewById(R.id.activity_scenedetails_commentlistview);
        allComment = (TextView) findViewById(R.id.activity_scenedetails_allcomment);
        moreComment = (ImageView) findViewById(R.id.activity_scenedetails_morecomment);
        productLinear = (LinearLayout) findViewById(R.id.product_linear);
        nearLinear = (LinearLayout) findViewById(R.id.near_linear);
        productListView = (ListViewForScrollView) findViewById(R.id.activity_scenedetails_productlistview);
        nearProductListView = (ListViewForScrollView) findViewById(R.id.activity_scenedetails_nearproductlistview);
        dialog = new WaittingDialog(SceneDetailActivity.this);
        initPopupWindow();
    }


    private int scrollY = 0;
    private int isEnd = 0;//0没开始，1已开始，2已结束（向上的动画，向下的相反）

    @Override
    protected void initList() {
        id = getIntent().getStringExtra("id");
        isCreate = getIntent().getBooleanExtra("create", false);
        if (id == null) {
            ToastUtils.showError(getString(R.string.no_this_sight));
            finish();
        }
        if (isCreate) {
            backImg.setImageResource(R.mipmap.cancel_black);
            View popup_view = View.inflate(SceneDetailActivity.this, R.layout.pop_share_scene_detail, null);
            ImageView cancelImg = (ImageView) popup_view.findViewById(R.id.pop_share_scene_detail_cancel);
            Button shareBtn = (Button) popup_view.findViewById(R.id.pop_share_scene_detail_share_btn);
            cancelImg.setOnClickListener(this);
            shareBtn.setOnClickListener(this);
            sharePop = new PopupWindow(popup_view, DensityUtils.dp2px(this, 300), ViewGroup.LayoutParams.WRAP_CONTENT, true);
            // 设置动画效果
            sharePop.setAnimationStyle(R.style.popup_style);
            sharePop.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

            sharePop.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    WindowManager.LayoutParams params = getWindow().getAttributes();
                    params.alpha = 1f;
                    getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                    getWindow().setAttributes(params);
                    sharePop = null;
                }
            });
            sharePop.setBackgroundDrawable(ContextCompat.getDrawable(SceneDetailActivity.this,
                    R.drawable.corner_white_4dp));
            sharePop.setTouchInterceptor(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return false;
                    // 这里如果返回true的话，touch事件将被拦截
                    // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
                }
            });
        }
        scrollView.setOnScrollListener(this);
        scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        titleLinear.getTop();
                        break;
                }
                return false;
            }
        });
        ViewGroup.LayoutParams lp = backgroundImg.getLayoutParams();
        lp.width = MainApplication.getContext().getScreenWidth();
        lp.height = lp.width * 16 / 9;
        backgroundImg.setLayoutParams(lp);
        ViewGroup.LayoutParams rLp = imgRelative.getLayoutParams();
        rLp.width = lp.width;
        rLp.height = lp.height;
        imgRelative.setLayoutParams(rLp);
        backgroundImg.setFocusable(true);
        backgroundImg.setFocusableInTouchMode(true);
        backgroundImg.requestFocus();
        backImg.setOnClickListener(this);
        shareImg.setOnClickListener(this);
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
        allComment.setOnClickListener(this);
        moreComment.setOnClickListener(this);
        loveCount.setOnClickListener(this);
        sceneProductList = new ArrayList<>();
        sceneProductAdapter = new GoodListAdapter(SceneDetailActivity.this, sceneProductList, null);
        productListView.setAdapter(sceneProductAdapter);
        nearProductList = new ArrayList<>();
        goodListAdapter = new GoodListAdapter(SceneDetailActivity.this, nearProductList, null);
        nearProductListView.setAdapter(goodListAdapter);
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.default_background_750_1334)
                .showImageForEmptyUri(R.mipmap.default_background_750_1334)
                .showImageOnFail(R.mipmap.default_background_750_1334)
                .cacheInMemory(true)
                .cacheOnDisk(true).considerExifParams(true)
                .build();
        options750_1334 = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.default_background_750_1334)
                .showImageForEmptyUri(R.mipmap.default_background_750_1334)
                .showImageOnFail(R.mipmap.default_background_750_1334)
                .cacheInMemory(true)
                .cacheOnDisk(true).considerExifParams(true)
                .build();
        IntentFilter filter = new IntentFilter();
        filter.addAction(DataConstants.BroadSceneDetail);
        registerReceiver(sceneDetailReceiver, filter);
    }

    @Override
    protected void requestNet() {
        if (!dialog.isShowing()) {
            dialog.show();
        }
        sceneDetails(id);
        commentsList(1 + "", 3 + "", id, null, 12 + "");
        commonList(1 + "", 14 + "", id, null, "sight", "love");
    }

    private void initPopupWindow() {
        WindowManager windowManager = SceneDetailActivity.this.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        View popup_view = View.inflate(SceneDetailActivity.this, R.layout.popup_scene_details_more, null);
        pinglunTv = (TextView) popup_view.findViewById(R.id.popup_scene_detail_more_pinglun);
        shareTv = (TextView) popup_view.findViewById(R.id.popup_scene_detail_more_share);
        bianjiLinear = (LinearLayout) popup_view.findViewById(R.id.popup_scene_detail_more_bianji_linear);
        jubaoTv = (TextView) popup_view.findViewById(R.id.popup_scene_detail_more_jubao);
        cancelTv = (TextView) popup_view.findViewById(R.id.popup_scene_detail_more_cancel);
        popupWindow = new PopupWindow(popup_view, display.getWidth(), ViewGroup.LayoutParams.WRAP_CONTENT, true);
        // 设置动画效果
        popupWindow.setAnimationStyle(R.style.popupwindow_style);
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        pinglunTv.setOnClickListener(this);
        shareTv.setOnClickListener(this);
        bianjiLinear.setOnClickListener(this);
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

    private void setTitleWidth() {
        SceneTitleSetUtils.setTitle(changjingTitle, frameLayout, titleImg, 12, 1);
    }


    //获取相近产品
    private void getNearProductList() {
        if (productList == null || productList.size() <= 0) {
            nearLinear.setVisibility(View.GONE);
            return;
        }
        StringBuilder ids = new StringBuilder();
        for (int i = 0; i < productList.size(); i++) {
            ids.append(",").append(productList.get(i).getId());
        }
        ids.deleteCharAt(0);
//        Log.e("<<<场景下商品id", ids.toString());
        getProducts(null, null, null, 1 + "", 3 + "", null, ids.toString(), null, null);
    }


    private void addProductToImg() {
        if (productList == null || productList.size() == 0) {
            return;
        }
        isShowAll = false;
        for (final SceneDetailsBean.DataBean.ProductBean product : productList) {
//            Log.e("<<<", productList.toString());
            final LabelView labelView = new LabelView(SceneDetailActivity.this);
            TagItem tagItem = new TagItem();
            tagItem.setId(product.getId() + "");
            tagItem.setName(product.getTitle());
            tagItem.setPrice("¥" + product.getPrice());
            labelView.init(tagItem);
            final RelativeLayout.LayoutParams labelLp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
//            labelView.pointOrAll(false, true);
            labelLp.leftMargin = (int) (product.getX() * MainApplication.getContext().getScreenWidth());
            labelLp.topMargin = (int) (product.getY() * MainApplication.getContext().getScreenWidth() * 16 / 9);
            labelView.setLayoutParams(labelLp);
            container.addView(labelView);
//            labelView.stopAnim();
            labelView.wave();
            labelView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    if (labelView.nameLeft.getVisibility() == View.INVISIBLE) {
//                        return;
//                    }
                    Intent intent = new Intent(SceneDetailActivity.this, GoodsDetailActivity.class);
                    String id = labelView.getTagInfo().getId();//商品id
                    intent.putExtra("id", id);
                    startActivity(intent);
                }
            });
        }
    }

    private void addLabelToLinear(final List<String> tagsTitleList) {
        for (int i = 0; i < tagsTitleList.size(); i++) {
            View view = View.inflate(SceneDetailActivity.this, R.layout.view_horizontal_label_item, null);
            TextView textView = (TextView) view.findViewById(R.id.view_horizontal_label_item_tv);
            textView.setText(tagsTitleList.get(i));
            view.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) view.getLayoutParams();
            lp.rightMargin = DensityUtils.dp2px(SceneDetailActivity.this, 10);
            view.setLayoutParams(lp);
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


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pop_share_scene_detail_cancel:
                sharePop.dismiss();
                break;
            case R.id.activity_scenedetails_left_label:
                if (netUserInfo == null) {
                    if (!dialog.isShowing()) {
                        dialog.show();
                    }
                    sceneDetails(id);
                    return;
                }
                Intent intent = new Intent(SceneDetailActivity.this, UserCenterActivity.class);
                long l = Long.valueOf(netUserInfo.getUser_id() + "");
                Log.e("<<<", "用户id=" + netScene.getData().getUser_info().getUser_id() + ",,,," + netUserInfo.getUser_id());
                intent.putExtra(FocusActivity.USER_ID_EXTRA, l);
                startActivity(intent);
                break;
            case R.id.pop_share_scene_detail_share_btn:
            case R.id.popup_scene_detail_more_share:
            case R.id.activity_scenedetails_share:
                if (sharePop != null) {
                    sharePop.dismiss();
                }
                Intent intent4 = new Intent(SceneDetailActivity.this, TestShare.class);
                intent4.putExtra("id", id);
                startActivity(intent4);
                break;
            case R.id.popup_scene_detail_more_jubao:
                if (!LoginInfo.isUserLogin()) {
                    MainApplication.which_activity = DataConstants.SceneDetailActivity;
                    LoginCompleteUtils.id = id;
                    startActivity(new Intent(SceneDetailActivity.this, OptRegisterLoginActivity.class));
                    return;
                }
                if (netScene == null) {
                    if (!dialog.isShowing()) {
                        dialog.show();
                    }
                    sceneDetails(id);
                    return;
                }
                if (netScene.getCurrent_user_id() != null && netScene.getCurrent_user_id().equals(netScene.getData().getUser_info().getUser_id() + "")) {
                    popupWindow.dismiss();
                    if (!dialog.isShowing()) {
                        dialog.show();
                    }
                    deleteScene(id);
                    return;
                }
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
                    if (!dialog.isShowing()) {
                        dialog.show();
                    }
                    sceneDetails(id);
                    return;
                }
                if (netScene == null) {
                    if (!dialog.isShowing()) {
                        dialog.show();
                    }
                    sceneDetails(id);
                    return;
                }
                String address = netScene.getData().getAddress();
                LatLng ll = new LatLng(netScene.getData().getLocation().getCoordinates().get(1), netScene.getData().getLocation().getCoordinates().get(0));
                Intent intent2 = new Intent(SceneDetailActivity.this, MapNearByCJActivity.class);
                intent2.putExtra("address", address);
                intent2.putExtra(MapNearByCJActivity.class.getSimpleName(), ll);
                startActivity(intent2);
//                Toast.makeText(SceneDetailActivity.this, "跳转到地图界面" + location, Toast.LENGTH_SHORT).show();
                break;
            case R.id.activity_scenedetails_background:
                isShowAll = !isShowAll;
                for (int i = 0; i < container.getChildCount(); i++) {
                    View view = container.getChildAt(i);
                    if (view instanceof LabelView) {
                        LabelView labelView = (LabelView) view;
//                        labelView.pointOrAll(false, isShowAll);
                    }
                }

                RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) titleLinear.getLayoutParams();
//                Log.e("<<<动画", "height=" + titleLinear.getHeight() + ",bottomMargin=" + lp.bottomMargin);
                if (isShowAll) {
                    ObjectAnimator.ofFloat(titleLinear, "translationY", titleLinear.getMeasuredHeight() + lp.bottomMargin).setDuration(300).start();
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            goneLayout.setVisibility(View.GONE);
                        }
                    });
                } else {
                    ObjectAnimator.ofFloat(titleLinear, "translationY", 0).setDuration(300).start();
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            goneLayout.setVisibility(View.VISIBLE);
                        }
                    });
                }
                break;
            case R.id.activity_scenedetails_lovecount:
                if (!LoginInfo.isUserLogin()) {
                    MainApplication.which_activity = DataConstants.SceneDetailActivity;
                    LoginCompleteUtils.id = id;
                    startActivity(new Intent(SceneDetailActivity.this, OptRegisterLoginActivity.class));
                    return;
                }
                dialog.show();
                switch (isLove) {
                    case 1:
                        cancelLoveScene(id);
                        break;
                    default:
                        loveScene(id);
                        break;
                }
                break;
//            case R.id.activity_scenedetails_commentlinear:
            case R.id.popup_scene_detail_more_pinglun:
            case R.id.activity_scenedetails_commentimg:
            case R.id.activity_scenedetails_commentcount:
            case R.id.activity_scenedetails_allcomment:
            case R.id.activity_scenedetails_morecomment:
                popupWindow.dismiss();
//                Log.e("<<<点击", "点击评论");
                if (netScene == null) {
                    if (!dialog.isShowing()) {
                        dialog.show();
                    }
                    sceneDetails(id + "");
                    return;
                }
                Intent intent3 = new Intent(SceneDetailActivity.this, CommentListActivity.class);
                intent3.putExtra("target_id", id);
                intent3.putExtra("type", 12 + "");
                intent3.putExtra("target_user_id", netScene.getData().getUser_info().getUser_id() + "");
                startActivity(intent3);
                break;
            case R.id.activity_scenedetails_moreuser:
//                Intent intent6 = new Intent(this, LoveSceneActivity.class);
//                startActivity(intent6);
                break;
            case R.id.popup_scene_detail_more_bianji_linear:
                if (!LoginInfo.isUserLogin()) {
                    MainApplication.which_activity = DataConstants.SceneDetailActivity;
                    LoginCompleteUtils.id = id;
                    startActivity(new Intent(SceneDetailActivity.this, OptRegisterLoginActivity.class));
                    return;
                }
                if (netScene == null) {
                    dialog.show();
                    sceneDetails(id);
                    return;
                }
                Intent intent5 = new Intent(SceneDetailActivity.this, CreateQJActivity.class);
                MainApplication.tag = 1;
                intent5.putExtra(SceneDetailActivity.class.getSimpleName(), netScene);
                startActivity(intent5);
                break;
            case R.id.activity_scenedetails_more:
                if (netScene == null) {
                    if (!dialog.isShowing()) {
                        dialog.show();
                    }
                    sceneDetails(id + "");
                    return;
                }
//                Log.e("<<<", "当前id=" + netScene.getCurrent_user_id() + "," + netScene.getData().getUser_info().getUser_id() + "," + netScene.getData().getUser_id());
//                if(netScene.getUser_info().getUser_id().equals(netScene.getCurrent_user_id()))
                if (netScene.getCurrent_user_id() != null && netScene.getCurrent_user_id().equals(netScene.getData().getUser_info().getUser_id() + "")) {
                    bianjiLinear.setVisibility(View.VISIBLE);
                    jubaoTv.setText("删除");
                }
                showPopup();
                break;
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
//        Log.e("<<<", "hasFocus=" + hasFocus);
        if (hasFocus && isCreate && sharePop != null) {
            WindowManager.LayoutParams params = getWindow().getAttributes();
            params.alpha = 0.4f;
            getWindow().setAttributes(params);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            sharePop.showAtLocation(activity_view, Gravity.CENTER, 0, 0);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.activity_scenedetails_grid:
//                Log.e("<<<点击用户头像", "user-id=" + headList.get(position).getUser().getUser_id());
                Intent intent1 = new Intent(SceneDetailActivity.this, UserCenterActivity.class);
                intent1.putExtra(FocusActivity.USER_ID_EXTRA, Long.parseLong(headList.get(position).getUser().getUser_id()));
                startActivity(intent1);
                break;
            case R.id.activity_scenedetails_commentlistview:
                if (netScene == null) {
                    dialog.show();
                    sceneDetails(this.id);
                    return;
                }
                Intent intent2 = new Intent(SceneDetailActivity.this, CommentListActivity.class);
                intent2.putExtra("target_id", this.id);
                intent2.putExtra("type", 12 + "");
                intent2.putExtra("target_user_id", netScene.getData().getUser_info().getUser_id() + "");
                startActivity(intent2);
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        for (int i = 0; i < container.getChildCount(); i++) {
            View view = container.getChildAt(i);
            if (view instanceof LabelView) {
                LabelView labelView = (LabelView) view;
                labelView.stopAnim();
                labelView.wave();
            }
        }
    }

    @Override
    protected void onStop() {
        for (int i = 0; i < container.getChildCount(); i++) {
            View view = container.getChildAt(i);
            if (view instanceof LabelView) {
                LabelView labelView = (LabelView) view;
                labelView.stopAnim();
            }
        }
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        instance = null;
        unregisterReceiver(sceneDetailReceiver);
        super.onDestroy();
    }

    //广播接收器
    private BroadcastReceiver sceneDetailReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (!dialog.isShowing()) {
                dialog.show();
            }
            sceneDetails(id);
        }
    };

    @Override
    public void scroll(ScrollView scrollView, int l, int t, int oldl, int oldt) {
        if (t <= titleLinear.getMeasuredHeight()) {
            for (int i = 0; i < container.getChildCount(); i++) {
                View view = container.getChildAt(i);
                if (view instanceof LabelView) {
                    LabelView labelView = (LabelView) view;
                    VisibleAnimListener visibleAnimListener = new VisibleAnimListener();
//                    ObjectAnimator visibleAnim1 = ObjectAnimator.ofFloat(labelView.nameLeft, "alpha", 1).setDuration(500);
//                    ObjectAnimator visibleAnim2 = ObjectAnimator.ofFloat(labelView.priceRight, "alpha", 1).setDuration(500);
//                    ObjectAnimator visibleAnim3 = ObjectAnimator.ofFloat(labelView.relativeRight, "alpha", 1).setDuration(500);
//                    visibleAnim1.addListener(visibleAnimListener);
//                    visibleAnim2.addListener(visibleAnimListener);
//                    visibleAnim3.addListener(visibleAnimListener);
//                    if (animFlag == 0) {
//                        visibleAnim1.start();
//                        visibleAnim2.start();
//                        visibleAnim3.start();
//                    }
                }
            }
            imgRelative.setTranslationY(t);
        } else if (t >= titleLinear.getMeasuredHeight() && t <= titleLinear.getMeasuredHeight() + (double) imgRelative.getMeasuredHeight() * 3 / 5) {

            imgRelative.setTranslationY((float) ((double) titleLinear.getMeasuredHeight() * 2 / 3 + (double) t / 3));
        } else if (t >= titleLinear.getMeasuredHeight() + (double) imgRelative.getMeasuredHeight() * 3 / 5) {
            imgRelative.setTranslationY((float) (-(double) imgRelative.getMeasuredHeight() * 2 / 5 + t));
        }
        if (imgRelative.getTranslationY() <= 0) {
            imgRelative.setTranslationY(0);
        }
        if (t > oldt + DensityUtils.dp2px(SceneDetailActivity.this, 25)) {
            titleRelative.setVisibility(View.GONE);
        } else if (t < oldt - DensityUtils.dp2px(SceneDetailActivity.this, 25)) {
            titleRelative.setVisibility(View.VISIBLE);
        }
        if (t == 0) {
            titleRelative.setVisibility(View.VISIBLE);

        }
        if (t >= titleLinear.getMeasuredHeight()) {
            for (int i = 0; i < container.getChildCount(); i++) {
                View view = container.getChildAt(i);
                if (view instanceof LabelView) {
                    LabelView labelView = (LabelView) view;
                    GoneAnimListener visibleAnimListener = new GoneAnimListener();
//                    ObjectAnimator visibleAnim1 = ObjectAnimator.ofFloat(labelView.nameLeft, "alpha", 0).setDuration(500);
//                    ObjectAnimator visibleAnim2 = ObjectAnimator.ofFloat(labelView.priceRight, "alpha", 0).setDuration(500);
//                    ObjectAnimator visibleAnim3 = ObjectAnimator.ofFloat(labelView.relativeRight, "alpha", 0).setDuration(500);
//                    visibleAnim1.addListener(visibleAnimListener);
//                    visibleAnim2.addListener(visibleAnimListener);
//                    visibleAnim3.addListener(visibleAnimListener);
//                    if (animFlag == 2) {
//                        visibleAnim1.start();
//                        visibleAnim2.start();
//                        visibleAnim3.start();
//                    }
                }
            }
        }
//        Log.e("<<<滑动", imgRelative.getBottom() + "," + scrollView.getTop() + "," + t + "," + scrollView.getScrollY());
    }

    private int animFlag = 2;

    private class VisibleAnimListener implements Animator.AnimatorListener {

        @Override
        public void onAnimationStart(Animator animation) {
//            animFlag = 1;
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            animFlag = 2;
        }

        @Override
        public void onAnimationCancel(Animator animation) {

        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }
    }

    private class GoneAnimListener implements Animator.AnimatorListener {

        @Override
        public void onAnimationStart(Animator animation) {
//            animFlag = 3;
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            animFlag = 0;
        }

        @Override
        public void onAnimationCancel(Animator animation) {

        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }
    }

    private void sceneDetails(String i) {
        ClientDiscoverAPI.sceneDetails(i, new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        Log.e("<<<场景详情", responseInfo.result);
//                        WriteJsonToSD.writeToSD("json", responseInfo.result);
                        SceneDetailsBean sceneDetails = new SceneDetailsBean();
                        try {
                            Gson gson = new Gson();
                            Type type = new TypeToken<SceneDetailsBean>() {
                            }.getType();
                            sceneDetails = gson.fromJson(responseInfo.result, type);
                        } catch (JsonSyntaxException e) {
                            Log.e("<<<", "解析异常");
                        }
                        dialog.dismiss();
//                    Log.e("<<<", "场景详情");
                        SceneDetailsBean netSceneDetails = sceneDetails;
                        if (netSceneDetails.isSuccess()) {
//                        Log.e("<<<", "url=" + netSceneDetails.getCover_url());
                            netScene = netSceneDetails;
                            ImageLoader.getInstance().displayImage(netSceneDetails.getData().getCover_url(), backgroundImg, options750_1334);
                            //用户是否已经点赞
                            isLove = netSceneDetails.getData().getIs_love();
                            switch (isLove) {
                                case 1:
                                    loveCount.setBackgroundResource(R.mipmap.loved_scene);
//                                love.setImageResource(R.mipmap.love_yes);
                                    break;
                                default:
                                    loveCount.setBackgroundResource(R.mipmap.love_scene);
//                                love.setImageResource(R.mipmap.like_height_43px);
                                    break;
                            }
                            //场景上的商品
                            productList = netSceneDetails.getData().getProduct();
                            getProductList();
                            getNearProductList();
                            //添加商品
                            addProductToImg();
                            changjingTitle.setText(netSceneDetails.getData().getTitle());
                            setTitleWidth();
                            suoshuqingjingTv.setText(netSceneDetails.getData().getScene_title());
                            locationTv.setText(netSceneDetails.getData().getAddress());
                            timeTv.setText(netSceneDetails.getData().getCreated_at());
                            netUserInfo = netSceneDetails.getData().getUser_info();
                            ImageLoader.getInstance().displayImage(netSceneDetails.getData().getUser_info().getAvatar_url(), userHead, options);
                            userName.setText(netSceneDetails.getData().getUser_info().getNickname());
                            if (netSceneDetails.getData().getUser_info().getIs_expert() == 1) {
                                vImg.setVisibility(View.VISIBLE);
                                userInfo.setText(netSceneDetails.getData().getUser_info().getExpert_label() + " | " + netSceneDetails.getData().getUser_info().getExpert_info());

                            } else {

                                vImg.setVisibility(View.GONE);
                                userInfo.setText(netSceneDetails.getData().getUser_info().getSummary());
                            }
//                        isSpertAndSummary(userInfo, netSceneDetails.getData().getUser_info().getIs_expert(), netSceneDetails.getData().getUser_info().getSummary());
                            loveCount.setText(String.format("%d人赞过", netSceneDetails.getData().getLove_count()));
                            moreUser.setText(String.format("%d+", netSceneDetails.getData().getLove_count()));
                            String des = netSceneDetails.getData().getDes().replace("\n", "\n\t");
                            desTv.setText("\t" + des);
                            //添加标签
                            addLabelToLinear(netSceneDetails.getData().getTags());

                            viewCount.setText(netSceneDetails.getData().getView_count() + "");
                            loveCountTv.setText(netSceneDetails.getData().getLove_count() + "");
                            commentNum.setText(netSceneDetails.getData().getComment_count() + "");
                            allComment.setText(String.format("全部%s条评论", netSceneDetails.getData().getComment_count()));
                            if (netSceneDetails.getData().getLove_count() > 14) {
                                moreUser.setVisibility(View.VISIBLE);
                            } else {
                                moreUser.setVisibility(View.GONE);
                            }
                            location = netSceneDetails.getData().getLocation();
//                        scrollView.scrollTo(0, container.getMeasuredHeight() - MainApplication.getContext().getScreenHeight());
                        } else {
                            ToastUtils.showError(netSceneDetails.getMessage());
//                            new Handler().post(new Runnable() {
//                                @Override
//                                public void run() {
//                                    finish();
//                                }
//                            });
                        }
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        dialog.dismiss();
                        ToastUtils.showError("网络错误");
//                    dialog.showErrorWithStatus("网络错误");
//                    Log.e("<<<", "请求失败 ");
                    }
                }

        );
    }

    private void loveScene(String i) {
        ClientDiscoverAPI.loveScene(i, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                SceneLoveBean sceneLoveBean = new SceneLoveBean();
                try {
                    Gson gson = new Gson();
                    Type type = new TypeToken<SceneLoveBean>() {
                    }.getType();
                    sceneLoveBean = gson.fromJson(responseInfo.result, type);
                } catch (JsonSyntaxException e) {
//                    Toast.makeText(MainApplication.getContext(), "解析异常" + e.toString(), Toast.LENGTH_SHORT).show();
                }
                SceneLoveBean netSceneLoveBean = sceneLoveBean;
//                    Toast.makeText(SceneDetailActivity.this, netSceneLoveBean.getData().getLove_count() + "", Toast.LENGTH_SHORT).show();
                if (netSceneLoveBean.isSuccess()) {
                    commonList(1 + "", 14 + "", id, null, "sight", "love");
                    isLove = 1;
                    loveCount.setBackgroundResource(R.mipmap.loved_scene);
//                        love.setImageResource(R.mipmap.love_yes);
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
                    ToastUtils.showError(netSceneLoveBean.getMessage());
//                        dialog.showErrorWithStatus(netSceneLoveBean.getMessage());
//                        Toast.makeText(SceneDetailActivity.this, netSceneLoveBean.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                dialog.dismiss();
                ToastUtils.showError("网络错误");
            }
        });
    }

    private void cancelLoveScene(String i) {
        ClientDiscoverAPI.cancelLoveScene(i, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                SceneLoveBean sceneLoveBean = new SceneLoveBean();
                try {
                    Gson gson = new Gson();
                    Type type = new TypeToken<SceneLoveBean>() {
                    }.getType();
                    sceneLoveBean = gson.fromJson(responseInfo.result, type);
                } catch (JsonSyntaxException e) {
//                    Toast.makeText(MainApplication.getContext(), "解析异常" + e.toString(), Toast.LENGTH_SHORT).show();
                }
                //                case DataConstants.CANCEL_LOVE_SCENE:
                SceneLoveBean netSceneLoveBean1 = sceneLoveBean;
                if (netSceneLoveBean1.isSuccess()) {
//                        Toast.makeText(SceneDetailActivity.this, netSceneLoveBean1.getData().getLove_count() + "", Toast.LENGTH_SHORT).show();
                    commonList(1 + "", 14 + "", id, null, "sight", "love");
                    isLove = 0;
                    loveCount.setBackgroundResource(R.mipmap.love_scene);
//                        love.setImageResource(R.mipmap.like_height_43px);
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
                    ToastUtils.showError(netSceneLoveBean1.getMessage());
//                        dialog.showErrorWithStatus(netSceneLoveBean1.getMessage());
//                        Toast.makeText(SceneDetailActivity.this, netSceneLoveBean1.getMessage(), Toast.LENGTH_SHORT).show();
                }
//                    break;
//                case DataConstants.LOVE_SCENE:
//
//                    break;
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                dialog.dismiss();
                ToastUtils.showError("网络错误");
            }
        });
    }

    private void deleteScene(String i) {
        ClientDiscoverAPI.deleteScene(i, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                NetBean netBean = new NetBean();
                try {
                    Gson gson = new Gson();
                    Type type = new TypeToken<NetBean>() {
                    }.getType();
                    netBean = gson.fromJson(responseInfo.result, type);
                } catch (JsonSyntaxException e) {
                    Log.e("<<<删除场景", "数据解析异常");
                }
                dialog.dismiss();
                if (netBean.isSuccess()) {
                    ToastUtils.showSuccess(netBean.getMessage());
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                        }
                    });
//                        刷新列表
                } else {
                    ToastUtils.showError(netBean.getMessage());
//                        dialog.showErrorWithStatus(netBean.getMessage());
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                dialog.dismiss();
                ToastUtils.showError("网络错误");
            }
        });
    }

    private void commonList(String page, String size, String id, String user_id, String type, String event) {
        ClientDiscoverAPI.commonList(page, size, id, user_id, type, event, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
//                Log.e("<<<通用列表", responseInfo.result);
                CommonBean commonBean = new CommonBean();
                try {
                    Gson gson = new Gson();
                    Type type = new TypeToken<CommonBean>() {
                    }.getType();
                    commonBean = gson.fromJson(responseInfo.result, type);
                } catch (JsonSyntaxException e) {
                    Log.e("<<<通用列表>>>", "数据解析异常" + e.toString());
                }
                dialog.dismiss();
                CommonBean netCommonBean = commonBean;
                if (netCommonBean.isSuccess()) {
                    headList.clear();
                    headList.addAll(netCommonBean.getData().getRows());
                    if (headList.size() <= 0) {
                        headRelative.setVisibility(View.GONE);
                    } else {
                        headRelative.setVisibility(View.VISIBLE);
                    }
                    sceneDetailUserHeadAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                dialog.dismiss();
                ToastUtils.showError("网络错误");
            }
        });
    }

    private void commentsList(String page, String size, String target_id, String target_user_id, String type) {
        ClientDiscoverAPI.commentsList(page, size, target_id, target_user_id, type, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
//                Log.e("<<<<评论", responseInfo.result);
                CommentsBean commentsBean = new CommentsBean();
                try {
                    Gson gson = new Gson();
                    Type type = new TypeToken<CommentsBean>() {
                    }.getType();
                    commentsBean = gson.<CommentsBean>fromJson(responseInfo.result, type);
                } catch (JsonSyntaxException e) {
                    Log.e("<<<评论列表>>>", "数据解析异常" + e.toString());
                }
                dialog.dismiss();
                CommentsBean netCommentBean = commentsBean;
                if (netCommentBean.isSuccess()) {
                    commentList.addAll(netCommentBean.getData().getRows());
                    if (netCommentBean.getData().getRows().size() > 3) {
                        allComment.setVisibility(View.VISIBLE);
                        moreComment.setVisibility(View.VISIBLE);
                    }
                    if (netCommentBean.getData().getRows().size() <= 0) {
                        commentsLinear.setVisibility(View.GONE);
                    }
                    sceneDetailCommentAdapter.notifyDataSetChanged();
                } else {
                    ToastUtils.showError(netCommentBean.getMessage());
                }

            }

            @Override
            public void onFailure(HttpException error, String msg) {
                dialog.dismiss();
                ToastUtils.showError("网络错误");
            }
        });
    }

    //获取场景下产品列表
    private void getProductList() {
        if (productList == null || productList.size() <= 0) {
            productLinear.setVisibility(View.GONE);
            return;
        }
        StringBuilder ids = new StringBuilder();
        for (int i = 0; i < productList.size(); i++) {
            ids.append(",").append(productList.get(i).getId());
        }
        ids.deleteCharAt(0);
//        Log.e("<<<场景下商品id", ids.toString());
        ClientDiscoverAPI.getProductList(null,null, null, null, null, 1 + "", 4 + "", ids.toString(), null, null, null, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                ProductBean netProductBean = new ProductBean();
                try {
                    Gson gson = new Gson();
                    Type type = new TypeToken<ProductBean>() {
                    }.getType();
                    netProductBean = gson.fromJson(responseInfo.result, type);
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                }
                if (netProductBean.isSuccess() /*&& currentTime == 2*/) {
                    dialog.dismiss();
                    sceneProductList.clear();
                    sceneProductList.addAll(netProductBean.getData().getRows());
//                    Log.e("<<<场景产品",sceneProductList.size()+","+sceneProductList.toString());
                    sceneProductAdapter.notifyDataSetChanged();
                } else {
                    dialog.dismiss();
                    ToastUtils.showError(netProductBean.getMessage());
//                        dialog.showErrorWithStatus(netProductBean.getMessage());
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                dialog.dismiss();
                ToastUtils.showError("网络错误");
            }
        });
//        DataPaser.getProductList(null, null, null, 1 + "", 3 + "", ids.toString(), null, null, null, handler);
    }

    private void getProducts(String category_id, String brand_id, String category_tag_ids, String page, String size, String ids, String ignore_ids,
                             String stick, String fine) {
        ClientDiscoverAPI.getProductList(null,null, category_id, brand_id, category_tag_ids, page, size, ids, ignore_ids, stick, fine, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                ProductBean productBean = new ProductBean();
                try {
                    Gson gson = new Gson();
                    Type type = new TypeToken<ProductBean>() {
                    }.getType();
                    productBean = gson.fromJson(responseInfo.result, type);
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                }
                ProductBean netProductBean = productBean;
                if (netProductBean.isSuccess() /*&& currentTime == 2*/) {
                    dialog.dismiss();
                    nearProductList.clear();
                    nearProductList.addAll(netProductBean.getData().getRows());
                    goodListAdapter.notifyDataSetChanged();
                } else {
                    dialog.dismiss();
                    ToastUtils.showError(netProductBean.getMessage());
//                        dialog.showErrorWithStatus(netProductBean.getMessage());
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                dialog.dismiss();
                ToastUtils.showError("网络错误");
            }
        });
    }
}
