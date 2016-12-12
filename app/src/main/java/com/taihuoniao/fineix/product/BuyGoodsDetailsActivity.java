package com.taihuoniao.fineix.product;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.SearchViewPagerAdapter;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.beans.NetBean;
import com.taihuoniao.fineix.beans.BuyGoodDetailsBean;
import com.taihuoniao.fineix.beans.CartBean;
import com.taihuoniao.fineix.beans.LoginInfo;
import com.taihuoniao.fineix.beans.NowBuyBean;
import com.taihuoniao.fineix.main.MainApplication;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.DataConstants;
import com.taihuoniao.fineix.product.fragment.BuyGoodsDetailsFragment;
import com.taihuoniao.fineix.product.fragment.CommentFragment;
import com.taihuoniao.fineix.product.fragment.WebFragment;
import com.taihuoniao.fineix.qingjingOrSceneDetails.fragment.SearchFragment;
import com.taihuoniao.fineix.user.OptRegisterLoginActivity;
import com.taihuoniao.fineix.utils.GlideUtils;
import com.taihuoniao.fineix.utils.PopupWindowUtil;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.utils.WindowUtils;
import com.taihuoniao.fineix.view.GlobalTitleLayout;
import com.taihuoniao.fineix.view.dialog.WaittingDialog;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

/**
 * Created by taihuoniao on 2016/2/22.
 * 商品详情界面
 */
public class BuyGoodsDetailsActivity extends BaseActivity implements View.OnClickListener {
    //上个界面传递过来的商品id
    private String id;
    private View activityView;
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
    private List<String> titleList;
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
    private HttpHandler<String> detailHandler;
    private HttpHandler<String> cartHandler;
    private HttpHandler<String> cancelShoucangHandler;
    private boolean isBuy;//判断点击的是购买还是添加购物车

    public BuyGoodsDetailsActivity() {
        super(0);
    }

    @Override
    protected void setContenttView() {
        activityView = View.inflate(this, R.layout.activity_buy_goods_details, null);
        setContentView(activityView);
    }

    @Override
    protected void getIntentData() {
        id = getIntent().getStringExtra("id");
        Log.e("<<<商品详情", "id=" + id);
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
        titleList = new ArrayList<>();
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

    private View initPop() {
        View view = View.inflate(this, R.layout.share_layout, null);
        GridView gv_share = (GridView) view.findViewById(R.id.gv_share);
        View tv_cancel = view.findViewById(R.id.tv_cancel);
        int[] image = {R.mipmap.wechat, R.mipmap.wechatmoment, R.mipmap.sina, R.mipmap.qqzone};
        String[] name = {"微信好友", "微信朋友圈", "新浪微博", "QQ空间",};
        List<HashMap<String, Object>> shareList = new ArrayList<>();
        for (int i = 0; i < image.length; i++) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("image", image[i]);
            map.put("text", name[i]);
            shareList.add(map);
        }
        SimpleAdapter adapter = new SimpleAdapter(this, shareList, R.layout.share_item_layout, new String[]{"image", "text"}, new int[]{R.id.iv_plat_logo, R.id.tv_plat_name});
        gv_share.setAdapter(adapter);
        gv_share.setOnItemClickListener(itemClicklistener);
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupWindowUtil.dismiss();
            }
        });
        return view;
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
                PopupWindowUtil.show(this, initPop());
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
                if (buyGoodDetailsBean.getData().getIs_favorite() == 1) {
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
                Intent intent = new Intent(this, ShopCartActivity.class);
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
                if (buyGoodDetailsBean.getData().getSkus_count() > 0) {
                    if (which == -1) {
                        ToastUtils.showError("请选择颜色/分类");
                    } else {
                        if (!dialog.isShowing()) {
                            dialog.show();
                        }
                        if (isBuy) {
                            buyNow(buyGoodDetailsBean.getData().getSkus().get(which).get_id(), "2", numberTv.getText().toString());
                        } else {
                            addToCart(buyGoodDetailsBean.getData().getSkus().get(which).get_id(), "2", numberTv.getText().toString());
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
        if (buyGoodDetailsBean.getData().getSkus_count() > 0) {
            scrollLinear.removeAllViews();
            for (int i = 0; i < buyGoodDetailsBean.getData().getSkus().size(); i++) {
                View view = View.inflate(this, R.layout.item_dialog_horizontal, null);
                TextView textView = (TextView) view.findViewById(R.id.item_dialog_horizontal_textview);
                textView.setText(buyGoodDetailsBean.getData().getSkus().get(i).getMode());
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
                        maxNumber = Integer.parseInt(buyGoodDetailsBean.getData().getSkus().get(j).getQuantity());
                        quantity.setText(maxNumber + "");
                        priceTv.setText("¥ " + buyGoodDetailsBean.getData().getSkus().get(j).getPrice());
                        String cover_url = buyGoodDetailsBean.getData().getSkus().get(j).getCover_url();
                        GlideUtils.displayImage(TextUtils.isEmpty(cover_url) ? buyGoodDetailsBean.getData().getCover_url() : cover_url, productsImg);
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
        popupWindow.showAtLocation(activityView, Gravity.BOTTOM, 0, 0);
    }

    //获取商品详情
    private void goodDetails() {
        detailHandler = ClientDiscoverAPI.goodsDetails(id, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                dialog.dismiss();
                Log.e("<<<商品详情", responseInfo.result);
                try {
                    Gson gson = new Gson();
                    Type type = new TypeToken<BuyGoodDetailsBean>() {
                    }.getType();
                    buyGoodDetailsBean = gson.fromJson(responseInfo.result, type);
                } catch (JsonSyntaxException e) {
                    Log.e("<<<商品详情", "解析异常=" + e.toString());
                }
                if (buyGoodDetailsBean.isSuccess()) {
                    titleList.add("好货");
                    buyGoodsDetailsFragment = BuyGoodsDetailsFragment.newInstance(id);
                    fragmentList.add(buyGoodsDetailsFragment);
                    if (buyGoodDetailsBean.getData().getStage() == 9) {
                        titleList.add("商品详情");
                        webFragment = WebFragment.newInstance();

                        fragmentList.add(webFragment);
                        titleList.add("评价");
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
                    searchViewPagerAdapter = new SearchViewPagerAdapter(getSupportFragmentManager(), fragmentList, titleList);
                    viewPager.setAdapter(searchViewPagerAdapter);
                    tabLayout.setupWithViewPager(viewPager);
                    viewPager.setOffscreenPageLimit(fragmentList.size());
                    //刷新数据
                    buyGoodsDetailsFragment.refreshData(buyGoodDetailsBean);
                    if (buyGoodDetailsBean.getData().getStage() == 9) {
                        webFragment.refreshData(buyGoodDetailsBean.getData().getContent_view_url());
                    }
                    if (buyGoodDetailsBean.getData().getIs_favorite() == 1) {
                        shoucangImg.setImageResource(R.mipmap.shoucang_yes);
                    } else {
                        shoucangImg.setImageResource(R.mipmap.shoucang_not);
                    }
                    //初始化popwindow数据
                    priceTv.setText("¥ " + buyGoodDetailsBean.getData().getSale_price());
                    ImageLoader.getInstance().displayImage(buyGoodDetailsBean.getData().getCover_url(), productsImg);
                    productsTitle.setText(buyGoodDetailsBean.getData().getTitle());
                    maxNumber = buyGoodDetailsBean.getData().getInventory();
                    quantity.setText(maxNumber + "");
                    addSkuToLinear();
                    return;
                }
                tabLayout.setVisibility(View.GONE);
                tabLine.setVisibility(View.GONE);
                buyBtn.setVisibility(View.GONE);
                shareLinear.setVisibility(View.GONE);
                addCartBtn.setVisibility(View.GONE);
                ToastUtils.showError(buyGoodDetailsBean.getMessage());
//                finish();
            }

            @Override
            public void onFailure(HttpException error, String msg) {
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
        cartHandler = ClientDiscoverAPI.cartNum(new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                CartBean cartBean = new CartBean();
                try {
                    Gson gson = new Gson();
                    Type type = new TypeToken<CartBean>() {
                    }.getType();
                    cartBean = gson.fromJson(responseInfo.result, type);
                } catch (JsonSyntaxException e) {
                    Log.e("<<<>>>", "数据异常" + e.toString());
                }
                CartBean netCartBean = cartBean;
                if (netCartBean.isSuccess()) {
                    titleLayout.setCartNum(netCartBean.getData().getCount());
                    return;
                }
                titleLayout.setCartNum(0);
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                titleLayout.setCartNum(0);
            }
        });
    }

    //取消收藏
    private void cancelFavorate() {
        cancelShoucangHandler = ClientDiscoverAPI.cancelShoucang(id, "1", new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                NetBean netBean = new NetBean();
                try {
                    Gson gson = new Gson();
                    Type type = new TypeToken<NetBean>() {
                    }.getType();
                    netBean = gson.fromJson(responseInfo.result, type);
                } catch (JsonSyntaxException e) {
                    Log.e("<<<取消收藏情景", "数据解析异常");
                }
                dialog.dismiss();
                if (netBean.isSuccess()) {
                    buyGoodDetailsBean.getData().setIs_favorite(0);
                    shoucangImg.setImageResource(R.mipmap.shoucang_not);
                } else {
                    ToastUtils.showError(netBean.getMessage());
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                dialog.dismiss();
                ToastUtils.showError(R.string.net_fail);
            }
        });
    }

    private HttpHandler<String> shoucangHandler;

    //收藏
    private void favorate() {
        shoucangHandler = ClientDiscoverAPI.shoucang(id, "1", new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                NetBean netBean = new NetBean();
                try {
                    Gson gson = new Gson();
                    Type type = new TypeToken<NetBean>() {
                    }.getType();
                    netBean = gson.fromJson(responseInfo.result, type);
                } catch (JsonSyntaxException e) {
                    Log.e("<<<收藏情景", "数据解析异常");
                }
                dialog.dismiss();
                if (netBean.isSuccess()) {
                    buyGoodDetailsBean.getData().setIs_favorite(1);
                    shoucangImg.setImageResource(R.mipmap.shoucang_yes);
                } else {
                    ToastUtils.showError(netBean.getMessage());
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                dialog.dismiss();
                ToastUtils.showError(R.string.net_fail);
            }
        });
    }

    private HttpHandler<String> buyHandler;

    //立即购买
    private void buyNow(String target_id, String type, String n) {
        buyHandler = ClientDiscoverAPI.buyNow(target_id, type, n, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                dialog.dismiss();
                Log.e("<<<立即购买", responseInfo.result);
//                WriteJsonToSD.writeToSD("json", responseInfo.result);
                NowBuyBean nowBuyBean = new NowBuyBean();
                try {
                    Gson gson = new Gson();
                    Type type = new TypeToken<NowBuyBean>() {
                    }.getType();
                    nowBuyBean = gson.fromJson(responseInfo.result, type);
                } catch (JsonSyntaxException e) {
                    Log.e("<<<立即购买", "解析异常=" + e.toString());
                }
                if (nowBuyBean.isSuccess()) {
                    Intent intent = new Intent(BuyGoodsDetailsActivity.this, ConfirmOrderActivity.class);
                    intent.putExtra("NowBuyBean", nowBuyBean);
                    startActivity(intent);
                } else {
                    ToastUtils.showError(nowBuyBean.getMessage());
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                dialog.dismiss();
                ToastUtils.showError(R.string.net_fail);
            }
        });
    }

    private HttpHandler<String> addCartHandler;

    //加入购物车
    private void addToCart(String target_id, String type, String n) {
        addCartHandler = ClientDiscoverAPI.addToCartNet(target_id, type, n, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                NetBean netBean = new NetBean();
                try {
                    Gson gson = new Gson();
                    Type type1 = new TypeToken<NetBean>() {
                    }.getType();
                    netBean = gson.fromJson(responseInfo.result, type1);
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                }
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
            public void onFailure(HttpException error, String msg) {
                dialog.dismiss();
                ToastUtils.showError(R.string.net_fail);
            }
        });
    }

    private AdapterView.OnItemClickListener itemClicklistener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Log.e("<<<", "imgPath=" + MainApplication.getContext().getCacheDirPath());
            Platform.ShareParams params;

            if (!dialog.isShowing()) {
                dialog.show();
            }

            switch (position) {
                case 3:
                    //qqzong
                    params = new Platform.ShareParams();
                    params.setShareType(Platform.SHARE_WEBPAGE);
                    params.setTitle(buyGoodDetailsBean.getData().getTitle());
                    params.setText(buyGoodDetailsBean.getData().getAdvantage());
                    params.setTitleUrl(buyGoodDetailsBean.getData().getShare_view_url());
                    if (buyGoodDetailsBean.getData().getCover_url() != null) {
                        params.setImageUrl(buyGoodDetailsBean.getData().getCover_url());
                    }
                    Platform qzone = ShareSDK.getPlatform(QZone.NAME);
                    qzone.share(params);
                    break;
                case 2:
                    //sina
                    params = new Platform.ShareParams();
                    params.setShareType(Platform.SHARE_WEBPAGE);
                    params.setText(buyGoodDetailsBean.getData().getAdvantage() + buyGoodDetailsBean.getData().getShare_view_url());
                    if (buyGoodDetailsBean.getData().getCover_url() != null) {
                        params.setImageUrl(buyGoodDetailsBean.getData().getCover_url());
                    }
                    Platform weibo = ShareSDK.getPlatform(SinaWeibo.NAME);
                    weibo.share(params);
                    break;
                case 0:
                    //wechat
                    params = new Platform.ShareParams();
                    params.setShareType(Platform.SHARE_WEBPAGE);
                    params.setTitle(buyGoodDetailsBean.getData().getTitle());
                    params.setText(buyGoodDetailsBean.getData().getAdvantage());
                    if (buyGoodDetailsBean.getData().getCover_url() != null) {
                        params.setImageUrl(buyGoodDetailsBean.getData().getCover_url());
                    }
                    params.setUrl(buyGoodDetailsBean.getData().getShare_view_url());
                    Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
                    wechat.share(params);
                    break;
                case 1:
                    //wechatmoment
                    params = new Platform.ShareParams();
                    params.setShareType(Platform.SHARE_WEBPAGE);
                    params.setTitle(buyGoodDetailsBean.getData().getTitle());
                    params.setText(buyGoodDetailsBean.getData().getAdvantage());
                    if (buyGoodDetailsBean.getData().getCover_url() != null) {
                        params.setImageUrl(buyGoodDetailsBean.getData().getCover_url());
                    }
                    params.setUrl(buyGoodDetailsBean.getData().getShare_view_url());
                    Platform wechatMoments = ShareSDK.getPlatform(WechatMoments.NAME);
                    wechatMoments.share(params);
                    break;
            }
            PopupWindowUtil.dismiss();
        }
    };

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