package com.taihuoniao.fineix.product;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.GoodsDetailsCommentListsAdapter;
import com.taihuoniao.fineix.adapters.GoodsDetailsGridViewAdapter;
import com.taihuoniao.fineix.adapters.GoodsDetailsViewPagerAdapter;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.beans.GoodsDetailsBean;
import com.taihuoniao.fineix.beans.LoginInfo;
import com.taihuoniao.fineix.beans.NowBuyBean;
import com.taihuoniao.fineix.beans.ShopCartNumber;
import com.taihuoniao.fineix.beans.TryCommentsBean;
import com.taihuoniao.fineix.main.MainApplication;
import com.taihuoniao.fineix.network.DataConstants;
import com.taihuoniao.fineix.network.DataPaser;
import com.taihuoniao.fineix.network.NetworkManager;
import com.taihuoniao.fineix.user.OptRegisterLoginActivity;
import com.taihuoniao.fineix.utils.ActivityUtil;
import com.taihuoniao.fineix.utils.DensityUtils;
import com.taihuoniao.fineix.view.CustomScrollView;
import com.taihuoniao.fineix.view.GridViewForScrollView;
import com.taihuoniao.fineix.view.ListViewForScrollView;
import com.taihuoniao.fineix.view.MyGlobalTitleLayout;
import com.taihuoniao.fineix.view.svprogress.SVProgressHUD;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by taihuoniao on 2016/2/22.
 * 商品详情界面
 */
public class MyGoodsDetailsActivity extends BaseActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {
    //上个界面传递过来的数据
    private String id;
    //界面中的控件
    private View activity_view;
    private MyGlobalTitleLayout titleLayout;
    //    private RelativeLayout loveRelative, shareRelative;
//    private ImageView loveImg;
    private TextView cartTv;
    private TextView buyTv;
    private CustomScrollView scrollView;
    private PopupWindow popupWindow;
    private SVProgressHUD dialog;
    //topview下的控件
    private ViewPager viewPager;
    private List<ImageView> imgList;
    //    private GoodsDetailsViewPagerAdapter viewPagerAdapter;
    private LinearLayout pointLinear;
    private Bitmap pointY, pointN;
    private TextView nameTv, salepriceTv, marketpriceTv;
    private RelativeLayout moreColorRelative;
    private GridViewForScrollView gridView;
    private RelativeLayout moreCommentsRelative;
    private ListViewForScrollView listView;
    private GoodsDetailsCommentListsAdapter commentListsAdapter;
    private List<TryCommentsBean> commentsList;
    private TextView moreCommentsTv;
    private RelativeLayout goodsRelative, commentsRelative;
    //bottomview下的控件
    private WebView webView;
    private String url;//webview需要加载的url
    //popupwindow下的控件
    private ImageView productsImg;
    private TextView productsTitle;
    private TextView priceTv;
    private TextView quantity;
    private LinearLayout scrollLinear;
    private TextView numberTv;
    private int number = 1;
    private int maxNumber = 1;//库存数量，最大不得超过
    //    private BitmapUtils bitmapUtils;
    private ImageLoader imageLoader = null;
    private DisplayImageOptions optionsCoverUrl = null;
    //网络请求返回的数据
    private GoodsDetailsBean goodsDetailsBean;
    private boolean isLove = false;//判断用户是否已经点赞
    //判断用户加入或立即购买的sku是第几个
    private int which = -1;
    private DisplayImageOptions options500_500;

    public MyGoodsDetailsActivity() {
        super(0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initPopuptWindow();
        setData();
        IntentFilter filter = new IntentFilter();
        filter.addAction(DataConstants.BROAD_GOODS_DETAILS);
        registerReceiver(goodsDetailsActivityReceiver, filter);
        dialog.show();
        DataPaser.goodsDetails(id, mHandler);
        int currentPage = 1;
        DataPaser.getGoodsDetailsCommentsList(id, currentPage + "", mHandler);
    }

    @Override
    protected void initView() {
        activity_view = View.inflate(MyGoodsDetailsActivity.this, R.layout.activity_goods_details, null);
        setContentView(activity_view);
        ActivityUtil.getInstance().addActivity(this);
        iniView();
    }

    @Override
    public void onResume() {
        super.onResume();
        DataPaser.shopCartNumberParser(mHandler);
    }

    @Override
    protected void onDestroy() {
        cancelNet();
        unregisterReceiver(goodsDetailsActivityReceiver);
        super.onDestroy();
    }

    private void setData() {
        MainApplication.which_activity = DataConstants.MyGoodsDetailsActivity;
        id = getIntent().getStringExtra("id");
        Log.e("<<<自营商品id", "id=" + id);
        if (id == null) {
            Toast.makeText(MyGoodsDetailsActivity.this, "暂无此商品", Toast.LENGTH_SHORT).show();
            finish();
        }
        titleLayout.setTitle("产品详情");
        viewPager.setOnPageChangeListener(this);
        moreColorRelative.setOnClickListener(this);
        moreCommentsRelative.setOnClickListener(this);
        listView.setCacheColorHint(0);
        moreCommentsTv.setOnClickListener(this);
        goodsRelative.setOnClickListener(this);
        commentsRelative.setOnClickListener(this);
//        loveRelative.setOnClickListener(this);
//        shareRelative.setOnClickListener(this);
        cartTv.setOnClickListener(this);
        buyTv.setOnClickListener(this);
        titleLayout.setFocusable(true);
        titleLayout.setFocusableInTouchMode(true);
        titleLayout.requestFocus();
//        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
//            @Override
//            public void onCancel(DialogInterface dialog) {
//                cancelNet();
//            }
//        });
    }

    private void iniView() {
        titleLayout = (MyGlobalTitleLayout) findViewById(R.id.activity_goodsdetails_titlelayout);
        titleLayout.setBackgroundResource(R.color.white);
        titleLayout.setTitleColor(getResources().getColor(R.color.black333333));
        titleLayout.setBackImg(R.mipmap.back_black);
        titleLayout.setRightSearchButton(false);
//        loveRelative = (RelativeLayout) findViewById(R.id.activity_goodsdetails_loverelative);
//        loveImg = (ImageView) findViewById(R.id.activity_goodsdetails_loveimg);
//        shareRelative = (RelativeLayout) findViewById(R.id.activity_goodsdetails_sharerelative);
//        ImageView shareImg = (ImageView) findViewById(R.id.activity_goodsdetails_shareimg);
        cartTv = (TextView) findViewById(R.id.activity_goodsdetails_cart);
        buyTv = (TextView) findViewById(R.id.activity_goodsdetails_buy);
        scrollView = (CustomScrollView) findViewById(R.id.activity_goodsdetails_scrollview);
        View topView = View.inflate(MyGoodsDetailsActivity.this, R.layout.item_goods_details_toplinear, null);
        scrollView.addToToplinear(topView);
        dialog = new SVProgressHUD(MyGoodsDetailsActivity.this);
        //topview下的控件
        viewPager = (ViewPager) topView.findViewById(R.id.activity_goodsdetails_viewpager);
        imgList = new ArrayList<>();
        pointLinear = (LinearLayout) topView.findViewById(R.id.activity_goodsdetails_pointlinear);
        pointY = BitmapFactory.decodeResource(getResources(), R.mipmap.point_white);
        pointN = BitmapFactory.decodeResource(getResources(), R.mipmap.point_grey);
        nameTv = (TextView) topView.findViewById(R.id.activity_goodsdetails_name);
        salepriceTv = (TextView) topView.findViewById(R.id.activity_goodsdetails_saleprice);
        marketpriceTv = (TextView) topView.findViewById(R.id.activity_goodsdetails_marketprice);
        moreColorRelative = (RelativeLayout) topView.findViewById(R.id.activity_goodsdetails_morecolor);
        gridView = (GridViewForScrollView) topView.findViewById(R.id.activity_goodsdetails_gridview);
        moreCommentsRelative = (RelativeLayout) topView.findViewById(R.id.activity_goodsdetails_comments);
//        pullToRefreshView = (PullToRefreshListView) topView.findViewById(R.id.activity_goodsdetails_listview);
//        listView = pullToRefreshView.getRefreshableView();
        listView = (ListViewForScrollView) topView.findViewById(R.id.activity_gooddetails_listview);
        TextView emptyTv = (TextView) topView.findViewById(R.id.activity_goodsdetails_emptytv);
        listView.setEmptyView(emptyTv);
        commentsList = new ArrayList<>();
        commentListsAdapter = new GoodsDetailsCommentListsAdapter(MyGoodsDetailsActivity.this, commentsList, false);
        listView.setAdapter(commentListsAdapter);
        moreCommentsTv = (TextView) topView.findViewById(R.id.activity_goodsdetails_morecommentstv);
        goodsRelative = (RelativeLayout) topView.findViewById(R.id.activity_goodsdetails_goodsrelative);
        TextView goodsDetailsTv = (TextView) topView.findViewById(R.id.activity_goodsdetails_goodstv);
        TextView goodsRedLine = (TextView) topView.findViewById(R.id.activity_goodsdetails_goodsredline);
        commentsRelative = (RelativeLayout) topView.findViewById(R.id.activity_goodsdetails_commentsrelative);
        TextView commentsTv = (TextView) topView.findViewById(R.id.activity_goodsdetails_commentstv);
        TextView commentsRedLine = (TextView) topView.findViewById(R.id.activity_goodsdetails_commentsredline);
        //bottomview下的控件
        webView = new WebView(MyGoodsDetailsActivity.this);
        webView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        scrollView.addToBottomlinear(webView);
        optionsCoverUrl = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.default_background_750_422)
                .showImageForEmptyUri(R.mipmap.default_background_750_422)
                .showImageOnFail(R.mipmap.default_background_750_422)
                .cacheInMemory(true)
                .cacheOnDisk(true).considerExifParams(true)
                .build();
        options500_500 = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.default_background_500_500)
                .showImageForEmptyUri(R.mipmap.default_background_500_500)
                .showImageOnFail(R.mipmap.default_background_500_500)
                .cacheInMemory(true)
                .cacheOnDisk(true).considerExifParams(true)
                .build();
        imageLoader = ImageLoader.getInstance();
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DataConstants.PARSER_SHOP_CART_NUMBER:
                    if (msg.obj != null) {
                        if (msg.obj instanceof ShopCartNumber) {
                            ShopCartNumber numberCart = null;
                            numberCart = (ShopCartNumber) msg.obj;
                            if (numberCart.isSuccess()) {
                                titleLayout.setShopCartCountertext(numberCart.getCount() + "");
                            }
                        }
                    }
                    break;
                case DataConstants.BUY_NOW:
                    dialog.dismiss();
                    NowBuyBean netNowBuy = (NowBuyBean) msg.obj;
                    if (netNowBuy.getSuccess()) {
                        Intent intent = new Intent(MyGoodsDetailsActivity.this, ConfirmOrderActivity.class);
                        intent.putExtra("NowBuyBean", netNowBuy);
                        startActivity(intent);
                    } else {
                        Toast.makeText(MyGoodsDetailsActivity.this, netNowBuy.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    popupWindow.dismiss();
                    break;
                case DataConstants.ADD_TO_CART:
                    dialog.dismiss();
                    String netResult = (String) msg.obj;
                    Toast.makeText(MyGoodsDetailsActivity.this, netResult, Toast.LENGTH_SHORT).show();
                    popupWindow.dismiss();
                    DataPaser.shopCartNumberParser(mHandler);
                    break;
//                case DataConstants.CANCEL_LOVE:
//                    dialog.dismiss();
//                    boolean cancelSuccess = (boolean) msg.obj;
//                    if (cancelSuccess) {
//                        isLove = false;
//                        loveImg.setImageResource(R.mipmap.heart_red_3x);
//                    }
//                    break;
//                case DataConstants.LOVE:
//                    dialog.dismiss();
//                    boolean loveSuccess = (boolean) msg.obj;
//                    if (loveSuccess) {
//                        isLove = true;
//                        loveImg.setImageResource(R.mipmap.heart_y);
//                    }
//
//                    break;
                case DataConstants.GOODS_DETAILS_COMMENTS:
                    List<TryCommentsBean> netList = (List<TryCommentsBean>) msg.obj;
                    commentsList.addAll(netList);
                    if (commentsList.size() > 3) {
                        moreCommentsTv.setVisibility(View.VISIBLE);
                    } else {
                        moreCommentsTv.setVisibility(View.GONE);
                    }
                    commentListsAdapter.notifyDataSetChanged();
                    break;
                case DataConstants.GOODS_DETAILS:
                    dialog.dismiss();
                    goodsDetailsBean = (GoodsDetailsBean) msg.obj;
                    if (goodsDetailsBean == null || goodsDetailsBean.getImgUrlList() == null) {
                        Toast.makeText(MyGoodsDetailsActivity.this, "访问的商品不存在", Toast.LENGTH_SHORT).show();
                        finish();
                        return;
                    }
                    imgList.clear();
                    pointLinear.removeAllViews();
                    for (int i = 0; i < goodsDetailsBean.getImgUrlList().size(); i++) {
                        ImageView img = new ImageView(MyGoodsDetailsActivity.this);
                        img.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                        img.setScaleType(ImageView.ScaleType.FIT_XY);
                        imageLoader.displayImage(goodsDetailsBean.getImgUrlList().get(i), img, optionsCoverUrl);
                        imgList.add(img);
                    }
                    viewPager.setAdapter(new GoodsDetailsViewPagerAdapter(MyGoodsDetailsActivity.this, imgList));
                    addPointToLinear();
                    nameTv.setText(goodsDetailsBean.getTitle());
                    salepriceTv.setText("¥ " + goodsDetailsBean.getSale_price());
                    priceTv.setText("¥ " + goodsDetailsBean.getSale_price());
                    marketpriceTv.setText("¥ " + goodsDetailsBean.getMarket_price());
                    marketpriceTv.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                    GoodsDetailsGridViewAdapter gridViewAdapter = new GoodsDetailsGridViewAdapter(MyGoodsDetailsActivity.this, goodsDetailsBean.getRelationProductsList());
                    gridView.setAdapter(gridViewAdapter);
//                    if (goodsDetailsBean.getIs_love().equals("0")) {
//                        isLove = false;
//                        loveImg.setImageResource(R.mipmap.heart_red_3x);
//                    } else {
//                        isLove = true;
//                        loveImg.setImageResource(R.mipmap.heart_y);
//                    }
//                    url = goodsDetailsBean.getContent_view_url();
                    webView.loadUrl(goodsDetailsBean.getContent_view_url());
                    imageLoader.displayImage(goodsDetailsBean.getCover_url(), productsImg, options500_500);
                    productsTitle.setText(goodsDetailsBean.getTitle());
                    maxNumber = Integer.parseInt(goodsDetailsBean.getInventory());
                    quantity.setText(maxNumber + "");
                    addSkuToLinear();
                    break;
                case DataConstants.NETWORK_FAILURE:
                    dialog.dismiss();
                    Toast.makeText(MyGoodsDetailsActivity.this, R.string.host_failure, Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    private void addSkuToLinear() {
        if (Integer.parseInt(goodsDetailsBean.getSkus_count()) > 0) {
            scrollLinear.removeAllViews();
            for (int i = 0; i < goodsDetailsBean.getSkus().size(); i++) {
                View view = View.inflate(MyGoodsDetailsActivity.this, R.layout.item_dialog_horizontal, null);
                TextView textView = (TextView) view.findViewById(R.id.item_dialog_horizontal_textview);
                textView.setText(goodsDetailsBean.getSkus().get(i).getMode());
                view.setTag(textView);
                final int j = i;
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        for (int k = 0; k < scrollLinear.getChildCount(); k++) {
                            TextView text = (TextView) scrollLinear.getChildAt(k).getTag();
                            if (k == j) {
                                text.setBackgroundResource(R.drawable.corner_yellow);
                                text.setTextColor(getResources().getColor(R.color.white));
                            } else {
                                text.setBackgroundResource(R.drawable.backround_corner_gray);
                                text.setTextColor(getResources().getColor(R.color.color_999));
                            }
                        }
                        which = j;
                        number = 1;
                        numberTv.setText(number + "");
                        maxNumber = Integer.parseInt(goodsDetailsBean.getSkus().get(j).getQuantity());
                        quantity.setText(maxNumber + "");
                        priceTv.setText("¥ " + goodsDetailsBean.getSkus().get(j).getPrice());
                    }
                });
                scrollLinear.addView(view);
            }


        }
    }


    private void addPointToLinear() {
        for (int i = 0; i < imgList.size(); i++) {
            ImageView img = new ImageView(MyGoodsDetailsActivity.this);
            img.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) img.getLayoutParams();
            lp.leftMargin = DensityUtils.dp2px(MyGoodsDetailsActivity.this, 1);
            lp.rightMargin = DensityUtils.dp2px(MyGoodsDetailsActivity.this, 1);
            if (i == 0)
                img.setImageBitmap(pointY);
            else
                img.setImageBitmap(pointN);
            pointLinear.addView(img);
        }
    }

    private void selectPointInLinear(int position) {
        for (int i = 0; i < imgList.size(); i++) {
            ImageView img = (ImageView) pointLinear.getChildAt(i);
            if (position == i) {
                img.setImageBitmap(pointY);
            } else {
                if (img == null) {
                    return;
                }
                if (pointN == null) {
                    return;
                }
                img.setImageBitmap(pointN);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.activity_goodsdetails_loverelative:
//                if (LoginInfo.isUserLogin()) {
//                    dialog.show();
//                    if (isLove) {
//                        DataPaser.parserCancelLove(id, "1", mHandler);
//                    } else {
//                        DataPaser.parserLove(id, "1", mHandler);
//                    }
//                } else {
//                    Toast.makeText(MyGoodsDetailsActivity.this, "请先登录", Toast.LENGTH_SHORT).show();
//                    //在此跳转到登录界面
//                    Intent intent = new Intent(MyGoodsDetailsActivity.this, OptRegisterLoginActivity.class);
//                    startActivity(intent);
//                }
//                break;
//            case R.id.activity_goodsdetails_sharerelative:
//                if (goodsDetailsBean == null) {
////                    Toast.makeText(GoodsDetailsActivity.this, R.string.host_failure, Toast.LENGTH_SHORT).show();
//                    cancelNet();
//                    dialog.show();
//                    DataPaser.goodsDetails(id, mHandler);
//                    int currentPage = 1;
//                    DataPaser.getGoodsDetailsCommentsList(id, currentPage + "", mHandler);
//                    return;
//                }
//                Toast.makeText(MyGoodsDetailsActivity.this, "分享", Toast.LENGTH_SHORT).show();
////                Share.showShareWindow(GoodsDetailsActivity.this, activity_view,
////                        goodsDetailsBean.getTitle(), goodsDetailsBean.getShare_desc(),
////                        goodsDetailsBean.getCover_url(), goodsDetailsBean.getShare_view_url(), true);
//                break;
            case R.id.activity_goodsdetails_morecolor:
            case R.id.activity_goodsdetails_cart:
            case R.id.activity_goodsdetails_buy:
                if (goodsDetailsBean == null) {
                    cancelNet();
                    dialog.show();
                    DataPaser.goodsDetails(id, mHandler);
                    int currentPage = 1;
                    DataPaser.getGoodsDetailsCommentsList(id, currentPage + "", mHandler);
                    return;
                }
                if (LoginInfo.isUserLogin()) {
                    showPopupWindow();
                } else {
                    Toast.makeText(MyGoodsDetailsActivity.this, "请先登录", Toast.LENGTH_SHORT).show();
                    //在此跳转到登录界面
                    MainApplication.which_activity = DataConstants.ElseActivity;
                    Intent in = new Intent(MyGoodsDetailsActivity.this, OptRegisterLoginActivity.class);
                    startActivity(in);
                }
                break;
            case R.id.activity_goodsdetails_goodsrelative:
                scrollView.goToBottom();
                break;
            case R.id.activity_goodsdetails_morecommentstv:
            case R.id.activity_goodsdetails_commentsrelative:
            case R.id.activity_goodsdetails_comments:
                Intent intent = new Intent(MyGoodsDetailsActivity.this, GoodsCommentsActivity.class);
                intent.putExtra("target_id", id);
                startActivity(intent);
                break;
            //popupwindow下的控件
            case R.id.dialog_cart_cancelimg:
                popupWindow.dismiss();
                break;
            case R.id.dialog_cart_reduce:
                if (number <= 1) {
                    number = 1;
                } else {
                    number--;
                }
                numberTv.setText(number + "");
                break;
            case R.id.dialog_cart_add:
                if (maxNumber == 0)
                    return;
                if (number >= maxNumber) {
                    number = maxNumber;
                } else {
                    number++;
                }
                numberTv.setText(number + "");
                break;
            case R.id.dialog_cart_tocartbtn:
                if (goodsDetailsBean == null) {
                    cancelNet();
                    dialog.show();
                    DataPaser.goodsDetails(id, mHandler);
                    int currentPage = 1;
                    DataPaser.getGoodsDetailsCommentsList(id, currentPage + "", mHandler);
                    return;
                }
                if (Integer.parseInt(goodsDetailsBean.getSkus_count()) > 0) {
                    if (which == -1) {
                        Toast.makeText(MyGoodsDetailsActivity.this, R.string.not_switch_sku, Toast.LENGTH_SHORT).show();
                    } else {
                        dialog.show();
                        DataPaser.addToCart(goodsDetailsBean.getSkus().get(which).get_id(), "2", numberTv.getText().toString(), mHandler);
                    }
                } else {
                    dialog.show();
                    DataPaser.addToCart(id, "1", numberTv.getText().toString(), mHandler);
                }
                break;
            case R.id.dialog_cart_buybtn:
                if (goodsDetailsBean == null) {
                    cancelNet();
                    dialog.show();
                    DataPaser.goodsDetails(id, mHandler);
                    int currentPage = 1;
                    DataPaser.getGoodsDetailsCommentsList(id, currentPage + "", mHandler);
                    return;
                }
                if (Integer.parseInt(goodsDetailsBean.getSkus_count()) > 0) {
                    if (which == -1) {
                        Toast.makeText(MyGoodsDetailsActivity.this, R.string.not_switch_sku, Toast.LENGTH_SHORT).show();
                    } else {
                        dialog.show();
                        DataPaser.buyNow(goodsDetailsBean.getSkus().get(which).get_id(), "2", numberTv.getText().toString(), mHandler);
                    }
                } else {
                    dialog.show();
                    DataPaser.buyNow(id, "1", numberTv.getText().toString(), mHandler);
                }

                break;
        }
    }


    private void initPopuptWindow() {
        WindowManager windowManager = MyGoodsDetailsActivity.this.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        View popup_view = View.inflate(MyGoodsDetailsActivity.this, R.layout.dialog_cart, null);
        productsImg = (ImageView) popup_view.findViewById(R.id.dialog_cart_productimg);
        productsTitle = (TextView) popup_view.findViewById(R.id.dialog_cart_producttitle);
        priceTv = (TextView) popup_view.findViewById(R.id.dialog_cart_price);
        quantity = (TextView) popup_view.findViewById(R.id.dialog_cart_skusnumber);
        Button toBuyBtn = (Button) popup_view.findViewById(R.id.dialog_cart_buybtn);
        ImageView cancelImg = (ImageView) popup_view.findViewById(R.id.dialog_cart_cancelimg);
        scrollLinear = (LinearLayout) popup_view.findViewById(R.id.dialog_cart_scrolllinear);
        TextView reduceTv = (TextView) popup_view.findViewById(R.id.dialog_cart_reduce);
        numberTv = (TextView) popup_view.findViewById(R.id.dialog_cart_number);
        TextView addTv = (TextView) popup_view.findViewById(R.id.dialog_cart_add);
        Button toCartBtn = (Button) popup_view.findViewById(R.id.dialog_cart_tocartbtn);
        popupWindow = new PopupWindow(popup_view, display.getWidth(), (int) (display.getHeight() * 0.6), true);
        // 设置动画效果
        popupWindow.setAnimationStyle(R.style.popupwindow_style);
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        cancelImg.setOnClickListener(this);
        reduceTv.setOnClickListener(this);
        addTv.setOnClickListener(this);
        toCartBtn.setOnClickListener(this);
        toBuyBtn.setOnClickListener(this);

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams params = getWindow().getAttributes();
                params.alpha = 1f;
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                getWindow().setAttributes(params);

            }
        });
        popupWindow.setBackgroundDrawable(ContextCompat.getDrawable(MyGoodsDetailsActivity.this,
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


    private void showPopupWindow() {
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.alpha = 0.4f;
        getWindow().setAttributes(params);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//这行代码可以使window后的所有东西边暗淡
        popupWindow.showAtLocation(activity_view, Gravity.BOTTOM, 0, 0);
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        selectPointInLinear(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private BroadcastReceiver goodsDetailsActivityReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            DataPaser.goodsDetails(id, mHandler);
        }
    };

    private void cancelNet() {
        NetworkManager.getInstance().cancel("goodsDetails");
        NetworkManager.getInstance().cancel("goodsDetailsCommentsList");
//        NetworkManager.getInstance().cancel("cancelLove");
//        NetworkManager.getInstance().cancel("love");
        NetworkManager.getInstance().cancel("addToCart");
        NetworkManager.getInstance().cancel("buyNow");
    }

}
