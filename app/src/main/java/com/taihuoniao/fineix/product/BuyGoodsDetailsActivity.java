package com.taihuoniao.fineix.product;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.SearchViewPagerAdapter;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.base.HttpRequest;
import com.taihuoniao.fineix.beans.BuyGoodDetailsBean;
import com.taihuoniao.fineix.beans.CartBean;
import com.taihuoniao.fineix.beans.HttpResponse;
import com.taihuoniao.fineix.beans.LoginInfo;
import com.taihuoniao.fineix.beans.NowBuyBean;
import com.taihuoniao.fineix.beans.QJFavoriteBean;
import com.taihuoniao.fineix.common.GlobalDataCallBack;
import com.taihuoniao.fineix.main.MainApplication;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.DataConstants;
import com.taihuoniao.fineix.network.URL;
import com.taihuoniao.fineix.product.fragment.BuyGoodsDetailsFragment;
import com.taihuoniao.fineix.product.fragment.CommentFragment;
import com.taihuoniao.fineix.product.fragment.WebFragment;
import com.taihuoniao.fineix.qingjingOrSceneDetails.fragment.SearchFragment;
import com.taihuoniao.fineix.user.OptRegisterLoginActivity;
import com.taihuoniao.fineix.utils.GlideUtils;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.utils.WindowUtils;
import com.taihuoniao.fineix.view.GlobalTitleLayout;
import com.taihuoniao.fineix.view.dialog.WaittingDialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import okhttp3.Call;

/**
 * Created by taihuoniao on 2016/2/22.
 * 商品详情界面
 */
public class BuyGoodsDetailsActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.rl)
    RelativeLayout rl;
    @Bind(R.id.title_layout)
    GlobalTitleLayout titleLayout;
    @Bind(R.id.tab_layout)
    TabLayout tabLayout;
    @Bind(R.id.tab_line)
    View tabLine;
    @Bind(R.id.view_pager)
    public ViewPager viewPager;
    @Bind(R.id.shoucang_img)
    ImageView shoucangImg;
    @Bind(R.id.shoucang_linear)
    LinearLayout shoucangLinear;
    @Bind(R.id.share_linear)
    LinearLayout shareLinear;
    @Bind(R.id.add_cart_btn)
    Button addCartBtn;
    @Bind(R.id.buy_btn)
    Button buyBtn;
    private WaittingDialog dialog;
    private BuyGoodDetailsBean buyGoodDetailsBean;//网络请求返回数据
    //fragment
    private BuyGoodsDetailsFragment buyGoodsDetailsFragment;
    private WebFragment webFragment;
    private CommentFragment commentFragment;
    private List<SearchFragment> fragmentList;
    private static final String[] mtitles={"商品","详情","评价"};
    private SearchViewPagerAdapter searchViewPagerAdapter;
    //popupwindow下的控件
    private PopupWindow popupWindow;
    private ImageView productsImg;
    private TextView productsTitle;
    private TextView priceTv;
    private TextView quantity;
    private LinearLayout scrollLinear;
    private TextView numberTv;
    private int number = 1;
    private int maxNumber = 1;//库存数量，最大不得超过
    //判断用户加入或立即购买的sku是第几个
    private int which = -1;
    private Call detailHandler;
    private Call cartHandler;
    private Call cancelShoucangHandler;
    private boolean isBuy;//判断点击的是购买还是添加购物车

    //上个界面传递过来的商品id
    private String id;
    private String storage_id;

    public BuyGoodsDetailsActivity() {
        super(R.layout.activity_buy_goods_details);
    }

    @Override
    protected void getIntentData() {
        id = getIntent().getStringExtra("id");
        storage_id = getIntent().getStringExtra("storage_id");
        if (id == null) {
            ToastUtils.showError("好货不存在或已下架");
            finish();
        }
    }

    @Override
    protected void initView() {
        titleLayout.setTitle(R.string.goods_details);
        titleLayout.setCartListener(this);
        dialog = new WaittingDialog(this);
        IntentFilter intentFilter = new IntentFilter(DataConstants.BroadBuyGoodDetails);
        registerReceiver(buyReceiver, intentFilter);
        WindowUtils.chenjin(this);
        initPopuptWindow();
    }

    @Override
    protected void initList() {
        fragmentList = new ArrayList<>();
        shoucangLinear.setOnClickListener(BuyGoodsDetailsActivity.this);
        shareLinear.setOnClickListener(this);
        addCartBtn.setOnClickListener(this);
        buyBtn.setOnClickListener(BuyGoodsDetailsActivity.this);
    }


    @Override
    protected void requestNet() {
        if (!dialog.isShowing()) {
            dialog.show();
        }
        goodDetails();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_cart_btn:
                if (!LoginInfo.isUserLogin()) {
                    MainApplication.which_activity = DataConstants.BuyGoodDetailsActivity;
                    activity.startActivity(new Intent(activity, OptRegisterLoginActivity.class));
                    return;
                }
                if (buyGoodDetailsBean == null) {
                    requestNet();
                    cartNumber();
                    return;
                }
                isBuy = false;
                showPopupWindow();
                break;
            case R.id.share_linear:
                if (buyGoodDetailsBean == null) {
                    requestNet();
                    cartNumber();
                    return;
                }
                Intent intent = new Intent(activity, ShareGoodsDialogActivity.class);
                intent.putExtra(ShareGoodsDialogActivity.class.getSimpleName(), buyGoodDetailsBean);
                startActivity(intent);
                break;
            case R.id.buy_btn:
                if (!LoginInfo.isUserLogin()) {
                    MainApplication.which_activity = DataConstants.BuyGoodDetailsActivity;
                    activity.startActivity(new Intent(activity, OptRegisterLoginActivity.class));
                    return;
                }
                if (buyGoodDetailsBean == null) {
                    requestNet();
                    cartNumber();
                    return;
                }
                isBuy = true;
                showPopupWindow();
                break;
            case R.id.shoucang_linear:
                if (!LoginInfo.isUserLogin()) {
                    MainApplication.which_activity = DataConstants.BuyGoodDetailsActivity;
                    activity.startActivity(new Intent(activity, OptRegisterLoginActivity.class));
                    return;
                }
                if (id == null) {
                    return;
                }
                if (buyGoodDetailsBean == null) {
                    requestNet();
                    cartNumber();
                    return;
                }
                if (!dialog.isShowing()) {
                    dialog.show();
                }
                if (buyGoodDetailsBean.getIs_favorite() == 1) {
                    cancelFavorate();
                } else {
                    favorate();
                }
                break;
            case R.id.title_cart:
                if (!LoginInfo.isUserLogin()) {
                    MainApplication.which_activity = DataConstants.BuyGoodDetailsActivity;
                    startActivity(new Intent(this, OptRegisterLoginActivity.class));
                    return;
                }
                intent = new Intent(this, ShopCartActivity.class);
                startActivity(intent);
                break;
            //popupwindow下的控件
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
            case R.id.dialog_confirm_btn:
                if (buyGoodDetailsBean == null) {
                    requestNet();
                    cartNumber();
                    return;
                }
                if (buyGoodDetailsBean.getSkus_count() > 0) {
                    if (which == -1) {
                        ToastUtils.showError("请选择颜色/分类");
                    } else {
                        if (!dialog.isShowing()) {
                            dialog.show();
                        }
                        if (isBuy) {
                            buyNow(buyGoodDetailsBean.getSkus().get(which).get_id(), "2", numberTv.getText().toString());
                        } else {
                            addToCart(buyGoodDetailsBean.getSkus().get(which).get_id(), "2", numberTv.getText().toString());
                        }
                    }
                } else {
                    if (!dialog.isShowing()) {
                        dialog.show();
                    }
                    if (isBuy) {
                        buyNow(id, "1", numberTv.getText().toString());
                    } else {
                        addToCart(id, "1", numberTv.getText().toString());
                    }
                }
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //获取购物车数量
        cartNumber();
    }

    private void initPopuptWindow() {
        View popup_view = View.inflate(this, R.layout.dialog_cart, null);
        productsImg = (ImageView) popup_view.findViewById(R.id.dialog_cart_productimg);
        productsTitle = (TextView) popup_view.findViewById(R.id.dialog_cart_producttitle);
        priceTv = (TextView) popup_view.findViewById(R.id.dialog_cart_price);
        quantity = (TextView) popup_view.findViewById(R.id.dialog_cart_skusnumber);
//        Button toBuyBtn = (Button) popup_view.findViewById(R.id.dialog_cart_buybtn);
        scrollLinear = (LinearLayout) popup_view.findViewById(R.id.dialog_cart_scrolllinear);
        TextView reduceTv = (TextView) popup_view.findViewById(R.id.dialog_cart_reduce);
        numberTv = (TextView) popup_view.findViewById(R.id.dialog_cart_number);
        TextView addTv = (TextView) popup_view.findViewById(R.id.dialog_cart_add);
//        Button toCartBtn = (Button) popup_view.findViewById(R.id.dialog_cart_tocartbtn);
        Button confirmBtn = (Button) popup_view.findViewById(R.id.dialog_confirm_btn);
        popupWindow = new PopupWindow(popup_view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        // 设置动画效果
        popupWindow.setAnimationStyle(R.style.popupwindow_style);
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        reduceTv.setOnClickListener(this);
        addTv.setOnClickListener(this);
        confirmBtn.setOnClickListener(this);

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams params = getWindow().getAttributes();
                params.alpha = 1f;
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                getWindow().setAttributes(params);
            }
        });
        popupWindow.setBackgroundDrawable(ContextCompat.getDrawable(this,
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

    private void addSkuToLinear() {
        if (buyGoodDetailsBean.getSkus_count() > 0) {
            scrollLinear.removeAllViews();
            for (int i = 0; i < buyGoodDetailsBean.getSkus().size(); i++) {
                View view = View.inflate(this, R.layout.item_dialog_horizontal, null);
                TextView textView = (TextView) view.findViewById(R.id.item_dialog_horizontal_textview);
                textView.setText(buyGoodDetailsBean.getSkus().get(i).getMode());
                view.setTag(textView);
                final int j = i;
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        for (int k = 0; k < scrollLinear.getChildCount(); k++) {
                            TextView text = (TextView) scrollLinear.getChildAt(k).getTag();
                            if (k == j) {
                                text.setBackgroundResource(R.drawable.corner_black);
                                text.setTextColor(getResources().getColor(R.color.white));
                            } else {
                                text.setBackgroundResource(R.drawable.backround_corner_gray);
                                text.setTextColor(getResources().getColor(R.color.color_999));
                            }
                        }
                        which = j;
                        number = 1;
                        numberTv.setText(number + "");
                        maxNumber = Integer.parseInt(buyGoodDetailsBean.getSkus().get(j).getQuantity());
                        quantity.setText(maxNumber + "");
                        priceTv.setText("¥ " + buyGoodDetailsBean.getSkus().get(j).getPrice());
                        String cover_url = buyGoodDetailsBean.getSkus().get(j).getCover_url();
                        GlideUtils.displayImage(TextUtils.isEmpty(cover_url) ? buyGoodDetailsBean.getCover_url() : cover_url, productsImg);
                    }
                });
                scrollLinear.addView(view);
            }
        }
    }

    private void showPopupWindow() {
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.alpha = 0.4f;
        getWindow().setAttributes(params);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//这行代码可以使window后的所有东西边暗淡
        popupWindow.showAtLocation(rl, Gravity.BOTTOM, 0, 0);
    }

    //获取商品详情
    private void goodDetails() {
        HashMap<String, String> requestParams = ClientDiscoverAPI.getgoodsDetailsRequestParams(id);
        detailHandler = HttpRequest.post(requestParams, URL.GOOD_DETAILS, new GlobalDataCallBack() {
            @Override
            public void onSuccess(String json) {
                dialog.dismiss();
                HttpResponse<BuyGoodDetailsBean> buyGoodDetailsBean2 = JsonUtil.json2Bean(json, new TypeToken<HttpResponse<BuyGoodDetailsBean>>() { });
                if (buyGoodDetailsBean2.isSuccess()) {
                    buyGoodDetailsBean = buyGoodDetailsBean2.getData();
                    if (fragmentList.size() == 0) {
//                        titleList.add("好货");
                        buyGoodsDetailsFragment = BuyGoodsDetailsFragment.newInstance(id);
                        fragmentList.add(buyGoodsDetailsFragment);
                        if (buyGoodDetailsBean.getStage() == 9) {
//                            titleList.add("商品详情");
                            webFragment = WebFragment.newInstance();

                            fragmentList.add(webFragment);
//                            titleList.add("评价");
                            commentFragment = CommentFragment.newInstance(id);
                            fragmentList.add(commentFragment);
                        } else {
                            tabLayout.setVisibility(View.GONE);
                            tabLine.setVisibility(View.GONE);
                            buyBtn.setVisibility(View.GONE);
                            shareLinear.setVisibility(View.GONE);
                            addCartBtn.setVisibility(View.GONE);
                        }
                        //设置适配器
                        searchViewPagerAdapter = new SearchViewPagerAdapter(getSupportFragmentManager(), fragmentList, Arrays.asList(mtitles));
                        viewPager.setAdapter(searchViewPagerAdapter);
                        tabLayout.setupWithViewPager(viewPager);
                        viewPager.setOffscreenPageLimit(fragmentList.size());
                        //刷新数据
                    }
                    buyGoodsDetailsFragment.refreshData(buyGoodDetailsBean);
                    if (buyGoodDetailsBean.getStage() == 9) {
                        webFragment.refreshData(buyGoodDetailsBean.getContent_view_url());
                    }
                    if (buyGoodDetailsBean.getIs_favorite() == 1) {
                        shoucangImg.setImageResource(R.mipmap.shoucang_yes);
                    } else {
                        shoucangImg.setImageResource(R.mipmap.shoucang_not);
                    }
                    //初始化popwindow数据
                    priceTv.setText("¥ " + buyGoodDetailsBean.getSale_price());
                    ImageLoader.getInstance().displayImage(buyGoodDetailsBean.getCover_url(), productsImg);
                    productsTitle.setText(buyGoodDetailsBean.getTitle());
                    maxNumber = buyGoodDetailsBean.getInventory();
                    quantity.setText(maxNumber + "");
                    addSkuToLinear();
//                    tabLayout.setVisibility(View.GONE);
//                    tabLine.setVisibility(View.GONE);
//                    buyBtn.setVisibility(View.GONE);
//                    shareLinear.setVisibility(View.GONE);
//                    addCartBtn.setVisibility(View.GONE);
//                    ToastUtils.showError(buyGoodDetailsBean.getMessage());
                }
            }

            @Override
            public void onFailure(String error) {
                dialog.dismiss();
                ToastUtils.showError(R.string.net_fail);
            }
        });
    }

    //获取购物车数量
    public void cartNumber() {
        if (!LoginInfo.isUserLogin()) {
            titleLayout.setCartNum(0);
            return;
        }
        cartHandler = HttpRequest.post(URL.CART_NUMBER, new GlobalDataCallBack() {
            @Override
            public void onSuccess(String json) {
                HttpResponse<CartBean> netCartBean = JsonUtil.json2Bean(json, new TypeToken<HttpResponse<CartBean>>(){});
                if (netCartBean.isSuccess()) {
                    titleLayout.setCartNum(netCartBean.getData().getCount());
                    return;
                }
                titleLayout.setCartNum(0);
            }

            @Override
            public void onFailure(String error) {
                titleLayout.setCartNum(0);
            }
        });
    }

    //取消收藏
    private void cancelFavorate() {
        HashMap<String, String> params = ClientDiscoverAPI.getcancelShoucangRequestParams(id, "1");
        cancelShoucangHandler = HttpRequest.post(params, URL.FAVORITE_AJAX_CANCEL_FAVORITE, new GlobalDataCallBack() {
            @Override
            public void onSuccess(String json) {
                HttpResponse<QJFavoriteBean> qjFavoriteBeanHttpResponse = JsonUtil.json2Bean(json, new TypeToken<HttpResponse<QJFavoriteBean>>() {});
                dialog.dismiss();
                if (qjFavoriteBeanHttpResponse.isSuccess()) {
                    buyGoodDetailsBean.setIs_favorite(0);
                    shoucangImg.setImageResource(R.mipmap.shoucang_not);
                } else {
                    ToastUtils.showError(qjFavoriteBeanHttpResponse.getMessage());
                }
            }

            @Override
            public void onFailure(String error) {
                dialog.dismiss();
                ToastUtils.showError(R.string.net_fail);
            }
        });
    }

    private Call shoucangHandler;

    //收藏
    private void favorate() {
        HashMap<String, String> params = ClientDiscoverAPI.getshoucangRequestParams(id, "1");
        shoucangHandler = HttpRequest.post(params, URL.FAVORITE_AJAX_FAVORITE, new GlobalDataCallBack() {
            @Override
            public void onSuccess(String json) {
                HttpResponse<QJFavoriteBean> qjFavoriteBeanHttpResponse = JsonUtil.json2Bean(json, new TypeToken<HttpResponse<QJFavoriteBean>>() {});
                dialog.dismiss();
                if (qjFavoriteBeanHttpResponse.isSuccess()) {
                    buyGoodDetailsBean.setIs_favorite(1);
                    shoucangImg.setImageResource(R.mipmap.shoucang_yes);
                } else {
                    ToastUtils.showError(qjFavoriteBeanHttpResponse.getMessage());
                }
            }

            @Override
            public void onFailure(String error) {
                dialog.dismiss();
                ToastUtils.showError(R.string.net_fail);
            }
        });
    }

    private Call buyHandler;

    //立即购买
    private void buyNow(String target_id, String type, String n) {
        HashMap<String, String> requestParams = ClientDiscoverAPI.getbuyNowRequestParams(target_id, type, n, storage_id);
        buyHandler = HttpRequest.post(requestParams, URL.URLSTRING_BUY_NOW, new GlobalDataCallBack() {
            @Override
            public void onSuccess(String json) {
                dialog.dismiss();
                HttpResponse<NowBuyBean> nowBuyBean = JsonUtil.json2Bean(json, new TypeToken<HttpResponse<NowBuyBean>>() {});
                if (nowBuyBean.isSuccess()) {
                    Intent intent = new Intent(BuyGoodsDetailsActivity.this, ConfirmOrderActivity.class);
                    intent.putExtra("NowBuyBean", nowBuyBean);
                    startActivity(intent);
                } else {
                    ToastUtils.showError(nowBuyBean.getMessage());
                }
            }

            @Override
            public void onFailure(String error) {
                dialog.dismiss();
                ToastUtils.showError(R.string.net_fail);
            }
        });
    }

    private Call addCartHandler;

    //加入购物车
    private void addToCart(String target_id, String type, String n) {
        HashMap<String, String> requestParams = ClientDiscoverAPI.getaddToCartNetRequestParams(target_id, type, n, storage_id);
        addCartHandler = HttpRequest.post(requestParams, URL.URLSTRING_ADD_TO_CART, new GlobalDataCallBack() {
            @Override
            public void onSuccess(String json) {
                HttpResponse netBean = JsonUtil.fromJson(json, HttpResponse.class);
                dialog.dismiss();
                if (netBean.isSuccess()) {
                    ToastUtils.showSuccess(netBean.getMessage());
                } else {
                    ToastUtils.showError(netBean.getMessage());
                }
                popupWindow.dismiss();
                cartNumber();
            }

            @Override
            public void onFailure(String error) {
                dialog.dismiss();
                ToastUtils.showError(R.string.net_fail);
            }
        });
    }

    @Override
    protected void onDestroy() {
        if (addCartHandler != null)
            addCartHandler.cancel();
        if (buyHandler != null)
            buyHandler.cancel();
        if (cancelShoucangHandler != null)
            cancelShoucangHandler.cancel();
        if (cartHandler != null)
            cartHandler.cancel();
        if (shoucangHandler != null)
            shoucangHandler.cancel();
        if (detailHandler != null)
            detailHandler.cancel();
        unregisterReceiver(buyReceiver);
        super.onDestroy();
    }

    private BroadcastReceiver buyReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            requestNet();
            cartNumber();
        }
    };
}