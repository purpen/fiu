package com.taihuoniao.fineix.product;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.SearchViewPagerAdapter;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.beans.BuyGoodDetailsBean;
import com.taihuoniao.fineix.beans.CartBean;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.DataConstants;
import com.taihuoniao.fineix.product.fragment.BuyGoodsDetailsFragment;
import com.taihuoniao.fineix.product.fragment.CommentFragment;
import com.taihuoniao.fineix.product.fragment.WebFragment;
import com.taihuoniao.fineix.qingjingOrSceneDetails.fragment.SearchFragment;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.utils.WindowUtils;
import com.taihuoniao.fineix.view.GlobalTitleLayout;
import com.taihuoniao.fineix.view.WaittingDialog;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by taihuoniao on 2016/2/22.
 * 商品详情界面
 */
public class BuyGoodsDetailsActivity extends BaseActivity implements View.OnClickListener {
    //上个界面传递过来的商品id
    private String id;
    @Bind(R.id.title_layout)
    GlobalTitleLayout titleLayout;
    @Bind(R.id.tab_layout)
    TabLayout tabLayout;
    @Bind(R.id.view_pager)
    public ViewPager viewPager;
    private WaittingDialog dialog;
    private BuyGoodDetailsBean buyGoodDetailsBean;//网络请求返回数据

    //fragment
    private BuyGoodsDetailsFragment buyGoodsDetailsFragment;
    private WebFragment webFragment;
    private CommentFragment commentFragment;
    private List<SearchFragment> fragmentList;
    private List<String> titleList;
    private SearchViewPagerAdapter searchViewPagerAdapter;

    public BuyGoodsDetailsActivity() {
        super(R.layout.activity_buy_goods_details);
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
    }

    @Override
    protected void initList() {
        fragmentList = new ArrayList<>();
        titleList = new ArrayList<>();
        titleList.add("好货");
        buyGoodsDetailsFragment = BuyGoodsDetailsFragment.newInstance(id);
        fragmentList.add(buyGoodsDetailsFragment);
        titleList.add("商品详情");
        webFragment = WebFragment.newInstance();
        fragmentList.add(webFragment);
        titleList.add("评价");
        commentFragment = CommentFragment.newInstance(id);
        fragmentList.add(commentFragment);
        //设置适配器
        searchViewPagerAdapter = new SearchViewPagerAdapter(getSupportFragmentManager(), fragmentList, titleList);
        viewPager.setAdapter(searchViewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setOffscreenPageLimit(fragmentList.size());
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
            case R.id.title_cart:
                Intent intent = new Intent(this, ShopCarActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //获取购物车数量
        cartNumber();
    }


    //获取商品详情
    private void goodDetails() {
        ClientDiscoverAPI.goodsDetails(id, new RequestCallBack<String>() {
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
                    buyGoodsDetailsFragment.refreshData(buyGoodDetailsBean);
                    webFragment.refreshData(buyGoodDetailsBean.getData().getContent_view_url());
                    return;
                }
                ToastUtils.showError(buyGoodDetailsBean.getMessage());
                finish();
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
        ClientDiscoverAPI.cartNum(new RequestCallBack<String>() {
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

    @Override
    protected void onDestroy() {
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