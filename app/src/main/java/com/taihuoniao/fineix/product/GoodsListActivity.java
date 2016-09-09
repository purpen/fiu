package com.taihuoniao.fineix.product;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.SearchViewPagerAdapter;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.beans.CartBean;
import com.taihuoniao.fineix.beans.CategoryLabelListBean;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.product.fragment.GoodListFragment;
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
 * Created by taihuoniao on 2016/5/3.
 */
public class GoodsListActivity extends BaseActivity implements View.OnClickListener {
    //上个界面传递过来的产品分类id和name
    private String id;
    private String name;
    @Bind(R.id.title_layout)
    GlobalTitleLayout titleLayout;
    @Bind(R.id.tab_layout)
    TabLayout tabLayout;
    @Bind(R.id.view_pager)
    ViewPager viewPager;
    public WaittingDialog dialog;
    private List<CategoryLabelListBean.CategoryTagItem> categoryList;
    private List<SearchFragment> fragmentList;
    private List<String> titleList;

    @Override
    protected void getIntentData() {
        id = getIntent().getStringExtra("id");
        name = getIntent().getStringExtra("name");
        if (id == null) {
            ToastUtils.showError("好货分类不存在或已删除");
            finish();
        }
    }

    public GoodsListActivity() {
        super(R.layout.activity_good_list);
    }

    @Override
    protected void initView() {
        titleLayout.setTitle(name);
        titleLayout.setCartListener(this);
        dialog = new WaittingDialog(this);
        WindowUtils.chenjin(this);
    }

    @Override
    protected void initList() {
        categoryList = new ArrayList<>();
        fragmentList = new ArrayList<>();
        titleList = new ArrayList<>();
    }

    @Override
    protected void requestNet() {
//        if (!dialog.isShowing()) {
//            dialog.show();
//        }

        fragmentList.add(GoodListFragment.newInstance(id, null));
        titleList.add("");
        tabLayout.setVisibility(View.GONE);
        SearchViewPagerAdapter searchViewPagerAdapter = new SearchViewPagerAdapter(getSupportFragmentManager(), fragmentList, titleList);
        viewPager.setAdapter(searchViewPagerAdapter);
//        categoryList();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_cart:
                startActivity(new Intent(this, ShopCarActivity.class));
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        cartNumber();
    }

    //    //获取子分类
//    private void categoryList() {
//        ClientDiscoverAPI.categoryLabel(id, new RequestCallBack<String>() {
//            @Override
//            public void onSuccess(ResponseInfo<String> responseInfo) {
////                dialog.dismiss();
//                CategoryLabelListBean categoryLabelListBean = new CategoryLabelListBean();
//                try {
//                    Gson gson = new Gson();
//                    Type type = new TypeToken<CategoryLabelListBean>() {
//                    }.getType();
//                    categoryLabelListBean = gson.fromJson(responseInfo.result, type);
//                } catch (JsonSyntaxException e) {
//                    Log.e("<<<", "数据解析异常" + e.toString());
//                }
//                if (categoryLabelListBean.isSuccess()) {
//                    fragmentList.clear();
//                    categoryList.clear();
//                    titleList.clear();
//                    categoryList.addAll(categoryLabelListBean.getData().getTags());
//                    for (int i = 0; i < categoryList.size(); i++) {
//                        titleList.add(categoryList.get(i).getTitle_cn());
//                        fragmentList.add(GoodListFragment.newInstance(id, categoryList.get(i).get_id()));
//                    }
//                    if (fragmentList.size() == 0) {
//                        fragmentList.add(GoodListFragment.newInstance(id, null));
//                        titleList.add("");
//                        tabLayout.setVisibility(View.GONE);
//                        SearchViewPagerAdapter searchViewPagerAdapter = new SearchViewPagerAdapter(getSupportFragmentManager(), fragmentList, titleList);
//                        viewPager.setAdapter(searchViewPagerAdapter);
//                        return;
//                    }
//                    tabLayout.setVisibility(View.VISIBLE);
//                    SearchViewPagerAdapter searchViewPagerAdapter = new SearchViewPagerAdapter(getSupportFragmentManager(), fragmentList, titleList);
//                    viewPager.setAdapter(searchViewPagerAdapter);
//                    tabLayout.setupWithViewPager(viewPager);
//                    viewPager.setOffscreenPageLimit(fragmentList.size());
//                } else {
//                    dialog.dismiss();
//                    ToastUtils.showError(categoryLabelListBean.getMessage());
//                }
//            }
//
//            @Override
//            public void onFailure(HttpException error, String msg) {
//                dialog.dismiss();
//                ToastUtils.showError(R.string.net_fail);
//            }
//        });
//    }
    private HttpHandler<String> cartHandler;

    //获取购物车数量
    public void cartNumber() {
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

    @Override
    protected void onDestroy() {
        if (cartHandler != null)
            cartHandler.cancel();
        super.onDestroy();
    }
}
