package com.taihuoniao.fineix.user;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.OrderViewpagerAdapter;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.beans.HttpResponse;
import com.taihuoniao.fineix.beans.User;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.user.fragments.ShopOrderFragment;
import com.taihuoniao.fineix.user.returnGoods.ReturnGoodsFragment;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.utils.Util;
import com.taihuoniao.fineix.utils.WindowUtils;
import com.taihuoniao.fineix.view.BadgeView;
import com.taihuoniao.fineix.view.GlobalTitleLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class ShopOrderListActivity extends BaseActivity implements TabLayout.OnTabSelectedListener {
    private ViewPager mPager;
    @Bind(R.id.custom_head)
    GlobalTitleLayout custom_head;
    TabLayout tabBar;
    private ArrayList<String> mTitles;
    private User user;
    private OrderViewpagerAdapter mAdapter;
    private boolean isRestart = false;
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
        mTitles.add("全部");
        mTitles.add("待付款");
        mTitles.add("待发货");
        mTitles.add("待收货");
        mTitles.add("待评价");
        mTitles.add("退款/售后");
        List<Fragment> mFragments = new ArrayList<>();
        for (int i = 0; i < mTitles.size() - 1; i++) {
            mFragments.add(ShopOrderFragment.getInstance(i));
        }
        ReturnGoodsFragment shopOrderFragment = new ReturnGoodsFragment();
        mFragments.add(shopOrderFragment);
        mAdapter = new OrderViewpagerAdapter(getSupportFragmentManager(), mFragments, mTitles);
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
        switch (position) {
            case 1: //待支付
                user.counter.order_wait_payment = user.counter.order_wait_payment - 1;
                setTipsNum(badgeView, user.counter.order_wait_payment);
                break;
            case 2: // 待发货
                user.counter.order_ready_goods = user.counter.order_ready_goods - 1;
                setTipsNum(badgeView, user.counter.order_ready_goods);
                break;
            case 3: // 待收货
                user.counter.order_sended_goods = user.counter.order_sended_goods - 1;
                setTipsNum(badgeView, user.counter.order_sended_goods);
                break;
            case 4: // 待评价
                user.counter.order_evaluate = user.counter.order_evaluate - 1;
                setTipsNum(badgeView, user.counter.order_evaluate);
                break;
            default:
                //do nothing
                break;
        }
    }

    private void setUpTabs(int position) {
        TabLayout.Tab tab = tabBar.getTabAt(position);
        if (tab != null) {
            View view = Util.inflateView(R.layout.tab_title_layout, null);
            TextView tvTitle = (TextView) view.findViewById(R.id.tv_title);
            BadgeView badgeView = (BadgeView) view.findViewById(R.id.badgeView);
            badgeViewList.add(badgeView);
            tvTitle.setText(mTitles.get(position));
            int count;
            switch (position) {
                case 1: //待支付
                    count = user.counter.order_wait_payment;
                    setTipsNum(badgeView, count);
                    break;
                case 2: // 待发货
                    count = user.counter.order_ready_goods;
                    setTipsNum(badgeView, count);
                    break;
                case 3: // 待收货
                    count = user.counter.order_sended_goods;
                    setTipsNum(badgeView, count);
                    break;
                case 4: // 待评价
                    count = user.counter.order_evaluate;
                    setTipsNum(badgeView, count);
                    break;
                default:
                    //do nothing
                    break;
            }

            tab.setCustomView(view);
        }
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
        ClientDiscoverAPI.getUserCenterData(new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                if (TextUtils.isEmpty(responseInfo.result)) return;
                HttpResponse<User> response = JsonUtil.json2Bean(responseInfo.result, new TypeToken<HttpResponse<User>>() {
                });
                if (response.isSuccess()) {
                    user = response.getData();
                    if (isRestart) {
                        changeNum();
                    } else {
                        for (int i = 0; i < mTitles.size(); i++) {
                            setUpTabs(i);
                        }
                    }
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                ToastUtils.showError(R.string.network_err);
            }
        });
    }

    private void changeNum() {
        if (badgeViewList == null) return;
        int count;
        for (int i = 0; i < badgeViewList.size(); i++) {
            switch (i) {
                case 1: //待支付
                    count = user.counter.order_wait_payment;
                    setTipsNum(badgeViewList.get(1), count);
                    break;
                case 2: // 待发货
                    count = user.counter.order_ready_goods;
                    setTipsNum(badgeViewList.get(2), count);
                    break;
                case 3: // 待收货
                    count = user.counter.order_sended_goods;
                    setTipsNum(badgeViewList.get(3), count);
                    break;
                case 4: // 待评价
                    count = user.counter.order_evaluate;
                    setTipsNum(badgeViewList.get(4), count);
                    break;
                default:
                    //do nothing
                    break;
            }
        }
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        //tabLayout滑动的时候，选中当前pager
        int position = tab.getPosition();
        mPager.setCurrentItem(position);
        switch (position) {
            case 0:
                custom_head.setTitle("我的订单");
                break;
            case 1:
                custom_head.setTitle("待付款");
                break;
            case 2:
                custom_head.setTitle("待发货");
                break;
            case 3:
                custom_head.setTitle("待收货");
                break;
            case 4:
                custom_head.setTitle("待评价");
                break;
            case 5:
                custom_head.setTitle("退款/售后");
                break;
        }

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

}
