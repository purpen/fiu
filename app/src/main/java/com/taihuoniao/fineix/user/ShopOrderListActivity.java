package com.taihuoniao.fineix.user;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.http.RequestParams;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.OrderViewpagerAdapter;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.base.GlobalDataCallBack;
import com.taihuoniao.fineix.base.HttpRequest;
import com.taihuoniao.fineix.beans.HttpResponse;
import com.taihuoniao.fineix.beans.User;
import com.taihuoniao.fineix.main.MainActivity;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.URL;
import com.taihuoniao.fineix.user.fragments.ShopOrderFragment;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.utils.Util;
import com.taihuoniao.fineix.utils.WindowUtils;
import com.taihuoniao.fineix.view.BadgeView;
import com.taihuoniao.fineix.view.GlobalTitleLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.Bind;

public class ShopOrderListActivity extends BaseActivity implements TabLayout.OnTabSelectedListener {

    @Bind(R.id.custom_head)
    GlobalTitleLayout custom_head;
    private ViewPager mPager;
    private TabLayout tabBar;

    private static final String[] TITLES = {"全部", "待付款", "待发货", "待收货", "待评价"/*, "退款/售后"*/};
    private boolean isRestart = false;
    private ArrayList<String> mTitles;
    private User user;
    public ArrayList<BadgeView> badgeViewList;


    public ShopOrderListActivity() {
        super(R.layout.activity_shop_order_list);
    }

    protected void initView() {
        custom_head.setContinueTvVisible(false);
        custom_head.setTitle("我的订单");
        tabBar = (TabLayout) findViewById(R.id.tab_order);
        mPager = (ViewPager) findViewById(R.id.viewpaer_order);
        mTitles = new ArrayList<>();
        Collections.addAll(mTitles, TITLES);
        List<Fragment> mFragments = new ArrayList<>();
        for (int i = 0; i < mTitles.size(); i++) {
            mFragments.add(ShopOrderFragment.getInstance(i));
        }
        //去掉退款和售后
//        ReturnGoodsFragment shopOrderFragment = new ReturnGoodsFragment();
//        mFragments.add(shopOrderFragment);
        OrderViewpagerAdapter mAdapter = new OrderViewpagerAdapter(getSupportFragmentManager(), mFragments, mTitles);
        mPager.setAdapter(mAdapter);
        tabBar.setupWithViewPager(mPager);

        //tabBar设置点击联动
        tabBar.setOnTabSelectedListener(ShopOrderListActivity.this);
        mPager.setOffscreenPageLimit(mFragments.size());
        WindowUtils.chenjin(this);
        badgeViewList = new ArrayList<>();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        isRestart = true;
        requestNet();
    }

    public void changeNum(int position) {
        if (badgeViewList == null) return;
        BadgeView badgeView = badgeViewList.get(position);
        setClassify(position, badgeView, true);
    }

    /**
     * 刷新 badgeView 显示的数量
     * isRestart == true
     */
    private void changeNum() {
        if (badgeViewList == null) return;
        for (int i = 0; i < badgeViewList.size(); i++) {
            if (i > 0 && i < 5) {
                setClassify(i, badgeViewList.get(i), false);
            }
        }
    }

    /**
     * 刷新 badgeView 显示的数量
     * isRestart == false
     */
    private void setUpTabs() {
        for (int i = 0; i < mTitles.size(); i++) {
            TabLayout.Tab tab = tabBar.getTabAt(i);
            if (tab != null) {
                View view = Util.inflateView(R.layout.tab_title_layout, null);
                TextView tvTitle = (TextView) view.findViewById(R.id.tv_title);
                BadgeView badgeView = (BadgeView) view.findViewById(R.id.badgeView);
                badgeViewList.add(badgeView);
                tvTitle.setText(mTitles.get(i));
                if (i > 0 && i < 5) {
                    setClassify(i, badgeView, false);
                }
                tab.setCustomView(view);
            }
        }
    }

    private void setClassify(int position, BadgeView badgeView, boolean changeNum) {
        int hintCount = 0;
        switch (position) {
            case 1: //待支付
                hintCount = changeNum ? --user.counter.order_wait_payment : user.counter.order_wait_payment;
                break;
            case 2: // 待发货
                hintCount = changeNum ? --user.counter.order_ready_goods : user.counter.order_ready_goods;
                break;
            case 3: // 待收货
                hintCount = changeNum ? --user.counter.order_sended_goods : user.counter.order_sended_goods;
                break;
            case 4: // 待评价
                hintCount = changeNum ? --user.counter.order_evaluate : user.counter.order_evaluate;
                break;
        }

        setTipsNum(badgeView, hintCount);
    }

    public void setTipsNum(BadgeView badgeView, int count) {
        if (count > 0) {
            badgeView.setVisibility(View.VISIBLE);
            badgeView.setBackground(15, getResources().getColor(R.color.color_af8323));
            badgeView.setBadgeCount(count);
        } else {
            badgeView.setVisibility(View.GONE);
        }
    }

    @Override
    protected void requestNet() {
        RequestParams params = ClientDiscoverAPI.getgetUserCenterDataRequestParams();
        HttpRequest.post(params, URL.USER_CENTER, new GlobalDataCallBack(){
//        ClientDiscoverAPI.getUserCenterData(new RequestCallBack<String>() {
            @Override
            public void onSuccess(String json) {
                if (TextUtils.isEmpty(json)) return;
                HttpResponse<User> response = JsonUtil.json2Bean(json, new TypeToken<HttpResponse<User>>() { });
                if (response.isSuccess()) {
                    user = response.getData();
                    if (isRestart) {
                        changeNum();
                    } else {
                        setUpTabs();
                    }
                }
            }

            @Override
            public void onFailure(String error) {
                ToastUtils.showError(R.string.network_err);
            }
        });
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {

        //tabLayout滑动的时候，选中当前pager
        int position = tab.getPosition();
        mPager.setCurrentItem(position);
        custom_head.setTitle(TITLES[position]);
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        startActivity(new Intent(ShopOrderListActivity.this, MainActivity.class));
    }
}
