package com.taihuoniao.fineix.product.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.GoodDetailsSceneListAdapter;
import com.taihuoniao.fineix.adapters.ViewPagerAdapter;
import com.taihuoniao.fineix.base.NetBean;
import com.taihuoniao.fineix.beans.BuyGoodDetailsBean;
import com.taihuoniao.fineix.beans.LoginInfo;
import com.taihuoniao.fineix.beans.NowBuyBean;
import com.taihuoniao.fineix.beans.ProductAndSceneListBean;
import com.taihuoniao.fineix.main.MainApplication;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.DataConstants;
import com.taihuoniao.fineix.product.BrandDetailActivity;
import com.taihuoniao.fineix.product.BuyGoodsDetailsActivity;
import com.taihuoniao.fineix.product.ConfirmOrderActivity;
import com.taihuoniao.fineix.qingjingOrSceneDetails.fragment.SearchFragment;
import com.taihuoniao.fineix.user.OptRegisterLoginActivity;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.view.ScrollableView;
import com.taihuoniao.fineix.view.WaittingDialog;
import com.taihuoniao.fineix.view.roundImageView.RoundedImageView;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by taihuoniao on 2016/8/25.
 */
public class BuyGoodsDetailsFragment extends SearchFragment implements AbsListView.OnScrollListener, View.OnClickListener {
    //商品id
    private String id;
    private View fragmentView;
    @Bind(R.id.pull_refresh_view)
    ListView listView;
    @Bind(R.id.shoucang_img)
    ImageView shoucangImg;
    @Bind(R.id.shoucang_linear)
    LinearLayout shoucangLinear;
    @Bind(R.id.buy_btn)
    Button buyBtn;
    @Bind(R.id.progress_bar)
    ProgressBar progressBar;
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
    private int lastSavedFirstVisibleItem = -1;
    private int lastTotalItem = -1;
    private ViewHolder holder;//headerview控件
    private ViewPagerAdapter<String> viewPagerAdapter;
    private BuyGoodDetailsBean buyGoodDetailsBean;
    private int page = 1;//相关情景列表页码
    private List<ProductAndSceneListBean.ProductAndSceneItem> sceneList;
    private GoodDetailsSceneListAdapter goodDetailsSceneListAdapter;
    private WaittingDialog dialog;

    public static BuyGoodsDetailsFragment newInstance(String id) {

        Bundle args = new Bundle();
        args.putString("id", id);
        BuyGoodsDetailsFragment fragment = new BuyGoodsDetailsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        id = getArguments().getString("id");
    }

    @Override
    protected View initView() {
        fragmentView = View.inflate(getActivity(), R.layout.fragment_buy_goods_details, null);
        ButterKnife.bind(this, fragmentView);
        View header = View.inflate(getActivity(), R.layout.header_fragment_buy_gooddetail, null);
        holder = new ViewHolder(header);
        listView.addHeaderView(header);
        dialog = new WaittingDialog(getActivity());
        initPopuptWindow();
        return fragmentView;
    }

    @Override
    protected void initList() {
        listView.setOnScrollListener(this);
        sceneList = new ArrayList<>();
        goodDetailsSceneListAdapter = new GoodDetailsSceneListAdapter(getActivity(), sceneList);
        listView.setAdapter(goodDetailsSceneListAdapter);
        holder.detailContainer.setOnClickListener(this);
        shoucangLinear.setOnClickListener(this);
        buyBtn.setOnClickListener(this);
    }

    @Override
    protected void requestNet() {
        getSceneList();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (holder.scrollableView != null) {
            holder.scrollableView.start();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (holder.scrollableView != null) {
            holder.scrollableView.stop();
        }
    }

    //用来刷新页面
    @Override
    public void refreshData(BuyGoodDetailsBean buyGoodDetailsBean) {
        this.buyGoodDetailsBean = buyGoodDetailsBean;
        if (viewPagerAdapter == null) {
            viewPagerAdapter = new ViewPagerAdapter<String>(activity, buyGoodDetailsBean.getData().getAsset());
            holder.scrollableView.setAdapter(viewPagerAdapter.setInfiniteLoop(true));
            holder.scrollableView.setAutoScrollDurationFactor(8);
            holder.scrollableView.setInterval(4000);
            holder.scrollableView.showIndicators();
            holder.scrollableView.start();
        } else {
            viewPagerAdapter.notifyDataSetChanged();
        }
        holder.title.setText(buyGoodDetailsBean.getData().getTitle());
        holder.price.setText("¥" + buyGoodDetailsBean.getData().getSale_price());
        holder.liangdian.setText(buyGoodDetailsBean.getData().getAdvantage());
        try {
            holder.brandName.setText(buyGoodDetailsBean.getData().getBrand().getTitle());
            ImageLoader.getInstance().displayImage(buyGoodDetailsBean.getData().getBrand().getCover_url(), holder.brandImg);
            holder.brandContainer.setOnClickListener(this);
        } catch (Exception e) {
            Log.e("<<<", "没有品牌");
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
    }

    private void initPopuptWindow() {
        View popup_view = View.inflate(getActivity(), R.layout.dialog_cart, null);
        productsImg = (ImageView) popup_view.findViewById(R.id.dialog_cart_productimg);
        productsTitle = (TextView) popup_view.findViewById(R.id.dialog_cart_producttitle);
        priceTv = (TextView) popup_view.findViewById(R.id.dialog_cart_price);
        quantity = (TextView) popup_view.findViewById(R.id.dialog_cart_skusnumber);
        Button toBuyBtn = (Button) popup_view.findViewById(R.id.dialog_cart_buybtn);
        scrollLinear = (LinearLayout) popup_view.findViewById(R.id.dialog_cart_scrolllinear);
        TextView reduceTv = (TextView) popup_view.findViewById(R.id.dialog_cart_reduce);
        numberTv = (TextView) popup_view.findViewById(R.id.dialog_cart_number);
        TextView addTv = (TextView) popup_view.findViewById(R.id.dialog_cart_add);
        Button toCartBtn = (Button) popup_view.findViewById(R.id.dialog_cart_tocartbtn);
        popupWindow = new PopupWindow(popup_view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        // 设置动画效果
        popupWindow.setAnimationStyle(R.style.popupwindow_style);
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        reduceTv.setOnClickListener(this);
        addTv.setOnClickListener(this);
        toCartBtn.setOnClickListener(this);
        toBuyBtn.setOnClickListener(this);

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams params = getActivity().getWindow().getAttributes();
                params.alpha = 1f;
                getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                getActivity().getWindow().setAttributes(params);
            }
        });
        popupWindow.setBackgroundDrawable(ContextCompat.getDrawable(getActivity(),
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
                View view = View.inflate(getActivity(), R.layout.item_dialog_horizontal, null);
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
                    }
                });
                scrollLinear.addView(view);
            }


        }
    }

    private void showPopupWindow() {
        WindowManager.LayoutParams params = getActivity().getWindow().getAttributes();
        params.alpha = 0.4f;
        getActivity().getWindow().setAttributes(params);
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//这行代码可以使window后的所有东西边暗淡
        popupWindow.showAtLocation(fragmentView, Gravity.BOTTOM, 0, 0);
    }

    //取消收藏
    private void cancelFavorate() {
        ClientDiscoverAPI.cancelShoucang(id, "1", new RequestCallBack<String>() {
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
                    ToastUtils.showSuccess(netBean.getMessage());
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

    //收藏
    private void favorate() {
        ClientDiscoverAPI.shoucang(id, "1", new RequestCallBack<String>() {
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
                    ToastUtils.showSuccess(netBean.getMessage());
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

    //获取商品相关的情境列表
    private void getSceneList() {
        ClientDiscoverAPI.productAndScene(page + "", 8 + "", null, id,null, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {

                Log.e("<<<关联列表", responseInfo.result);
//                WriteJsonToSD.writeToSD("json", responseInfo.result);
                ProductAndSceneListBean productAndSceneListBean = new ProductAndSceneListBean();
                try {
                    Gson gson = new Gson();
                    Type type = new TypeToken<ProductAndSceneListBean>() {
                    }.getType();
                    productAndSceneListBean = gson.fromJson(responseInfo.result, type);
                } catch (JsonSyntaxException e) {
                    Log.e("<<<关联列表", "解析异常=" + e.toString());
                }
                if (productAndSceneListBean.isSuccess()) {
                    if (page == 1) {
                        sceneList.clear();
                        lastSavedFirstVisibleItem = -1;
                        lastTotalItem = -1;
                    }
                    sceneList.addAll(productAndSceneListBean.getData().getRows());
                    //刷新适配器
                    goodDetailsSceneListAdapter.notifyDataSetChanged();
                }
                if (progressBar == null) {
                    return;
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                progressBar.setVisibility(View.GONE);
                ToastUtils.showError(R.string.net_fail);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (visibleItemCount > listView.getHeaderViewsCount()
                && firstVisibleItem + visibleItemCount >= totalItemCount) {
            if (firstVisibleItem != lastSavedFirstVisibleItem && lastTotalItem != totalItemCount) {
                lastSavedFirstVisibleItem = firstVisibleItem;
                lastTotalItem = totalItemCount;
                progressBar.setVisibility(View.VISIBLE);
                page++;
                //更多情景
                getSceneList();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buy_btn:
                if (!LoginInfo.isUserLogin()) {
                    MainApplication.which_activity = DataConstants.BuyGoodDetailsActivity;
                    activity.startActivity(new Intent(activity, OptRegisterLoginActivity.class));
                    return;
                }
                if (buyGoodDetailsBean == null) {
                    getActivity().sendBroadcast(new Intent(DataConstants.BroadBuyGoodDetails));
                    return;
                }
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
                    getActivity().sendBroadcast(new Intent(DataConstants.BroadBuyGoodDetails));
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
            case R.id.detail_container:
                BuyGoodsDetailsActivity activity = (BuyGoodsDetailsActivity) getActivity();
                activity.viewPager.setCurrentItem(1);
                break;
            case R.id.brand_container:

                if (buyGoodDetailsBean == null) {
                    getActivity().sendBroadcast(new Intent(DataConstants.BroadBuyGoodDetails));
                    return;
                }
                Intent intent = new Intent(getActivity(), BrandDetailActivity.class);
                intent.putExtra("id", buyGoodDetailsBean.getData().getBrand().get_id());
                getActivity().startActivity(intent);
//                startActivity(new Intent(getActivity(), AAAAAAAAAAAAAAA.class));
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
            case R.id.dialog_cart_tocartbtn:
                if (buyGoodDetailsBean == null) {
                    getActivity().sendBroadcast(new Intent(DataConstants.BroadBuyGoodDetails));
                    return;
                }
                if (buyGoodDetailsBean.getData().getSkus_count() > 0) {
                    if (which == -1) {
                        ToastUtils.showError("请选择颜色/分类");
                    } else {
                        if (!dialog.isShowing()) {
                            dialog.show();
                        }
                        addToCart(buyGoodDetailsBean.getData().getSkus().get(which).get_id(), "2", numberTv.getText().toString());
                    }
                } else {
                    if (!dialog.isShowing()) {
                        dialog.show();
                    }
                    addToCart(id, "1", numberTv.getText().toString());
                }
                break;
            case R.id.dialog_cart_buybtn:
                if (buyGoodDetailsBean == null) {
                    getActivity().sendBroadcast(new Intent(DataConstants.BroadBuyGoodDetails));
                    return;
                }
                if (buyGoodDetailsBean.getData().getSkus_count() > 0) {
                    if (which == -1) {
                        ToastUtils.showError("请选择颜色/分类");
                    } else {
                        if (!dialog.isShowing()) {
                            dialog.show();
                        }
                        buyNow(buyGoodDetailsBean.getData().getSkus().get(which).get_id(), "2", numberTv.getText().toString());
                    }
                } else {
                    if (!dialog.isShowing()) {
                        dialog.show();
                    }
                    buyNow(id, "1", numberTv.getText().toString());
                }

                break;
        }
    }

    //立即购买
    private void buyNow(String target_id, String type, String n) {
        ClientDiscoverAPI.buyNow(target_id, type, n, new RequestCallBack<String>() {
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
                    Intent intent = new Intent(getActivity(), ConfirmOrderActivity.class);
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

    //加入购物车
    private void addToCart(String target_id, String type, String n) {
        ClientDiscoverAPI.addToCartNet(target_id, type, n, new RequestCallBack<String>() {
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
                BuyGoodsDetailsActivity activity = (BuyGoodsDetailsActivity) getActivity();
                activity.cartNumber();
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                dialog.dismiss();
                ToastUtils.showError(R.string.net_fail);
            }
        });
    }

    static class ViewHolder {
        @Bind(R.id.scrollableView)
        ScrollableView scrollableView;
        @Bind(R.id.detail_container)
        LinearLayout detailContainer;
        @Bind(R.id.title)
        TextView title;
        @Bind(R.id.price)
        TextView price;
        @Bind(R.id.liangdian)
        TextView liangdian;
        @Bind(R.id.brand_container)
        RelativeLayout brandContainer;
        @Bind(R.id.brand_img)
        RoundedImageView brandImg;
        @Bind(R.id.brand_name)
        TextView brandName;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
