package com.taihuoniao.fineix.product;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.google.gson.reflect.TypeToken;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.SearchViewPagerAdapter;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.beans.HttpResponse;
import com.taihuoniao.fineix.common.GlobalDataCallBack;
import com.taihuoniao.fineix.base.HttpRequest;
import com.taihuoniao.fineix.beans.CartBean;
import com.taihuoniao.fineix.beans.LoginInfo;
import com.taihuoniao.fineix.main.MainApplication;
import com.taihuoniao.fineix.network.DataConstants;
import com.taihuoniao.fineix.network.URL;
import com.taihuoniao.fineix.product.fragment.GoodListFragment;
import com.taihuoniao.fineix.qingjingOrSceneDetails.fragment.SearchFragment;
import com.taihuoniao.fineix.user.OptRegisterLoginActivity;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.utils.WindowUtils;
import com.taihuoniao.fineix.view.GlobalTitleLayout;
import com.taihuoniao.fineix.view.dialog.WaittingDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import okhttp3.Call;

/**
 * Created by taihuoniao on 2016/5/3.
 */
public class GoodsListActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.title_layout)
    GlobalTitleLayout titleLayout;
    @Bind(R.id.tab_layout)
    TabLayout tabLayout;
    @Bind(R.id.view_pager)
    ViewPager viewPager;

    public WaittingDialog dialog;
    private List<SearchFragment> fragmentList;
    private List<String> titleList;
    private Call cartHandler;
    private String id; //上个界面传递过来的产品分类id和name
    private String name;

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
        fragmentList = new ArrayList<>();
        titleList = new ArrayList<>();
    }

    @Override
    protected void requestNet() {
        fragmentList.add(GoodListFragment.newInstance(id, null));
        titleList.add("");
        tabLayout.setVisibility(View.GONE);
        SearchViewPagerAdapter searchViewPagerAdapter = new SearchViewPagerAdapter(getSupportFragmentManager(), fragmentList, titleList);
        viewPager.setAdapter(searchViewPagerAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_cart:
                if (!LoginInfo.isUserLogin()) {
                    MainApplication.which_activity = DataConstants.ElseActivity;
                    startActivity(new Intent(this, OptRegisterLoginActivity.class));
                    return;
                }
                startActivity(new Intent(this, ShopCartActivity.class));
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
            cartNumber();
    }

    //获取购物车数量
    public void cartNumber() {
        if (!LoginInfo.isUserLogin()) {
            titleLayout.setCartNum(0);
            return;
        }
        cartHandler = HttpRequest.post( URL.CART_NUMBER, new GlobalDataCallBack(){
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

    @Override
    protected void onDestroy() {
        if (cartHandler != null)
            cartHandler.cancel();
        super.onDestroy();
    }
}
