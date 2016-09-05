package com.taihuoniao.fineix.user;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.OrderViewpagerAdapter;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.user.fragments.ShopOrderFragment;
import com.taihuoniao.fineix.user.returnGoods.ReturnGoodsFragment;
import com.taihuoniao.fineix.utils.WindowUtils;
import com.taihuoniao.fineix.view.GlobalTitleLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class ShopOrderListActivity extends BaseActivity implements TabLayout.OnTabSelectedListener {
    private ViewPager mPager;
    private TabLayout.Tab mAllTab, mPayTab, mDeliverTab, mCriticalTab, mReceiverTab;
    @Bind(R.id.custom_head)
    GlobalTitleLayout custom_head;

    private String mFlag;

//    private Handler mHandler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//
//        }
//    };

    public ShopOrderListActivity() {
        super(R.layout.activity_shop_order_list);
    }

    protected void initView() {
        custom_head.setContinueTvVisible(false);
        custom_head.setTitle("我的订单");
        TabLayout tabBar = (TabLayout) findViewById(R.id.tab_order);
        mPager = (ViewPager) findViewById(R.id.viewpaer_order);
        //手动添加tab
        mAllTab = tabBar.newTab().setText("全部");
        mDeliverTab = tabBar.newTab().setText("待发货");
        mPayTab = tabBar.newTab().setText("待付款");
        mReceiverTab = tabBar.newTab().setText("待收货");
        mCriticalTab = tabBar.newTab().setText("待评价");

        boolean mAll = false, mDeliver = false, mPay = false, mReceiver = false, mCritical = false;
        tabBar.addTab(mAllTab, mAll);
        tabBar.addTab(mPayTab, mPay);
        tabBar.addTab(mDeliverTab, mDeliver);
        tabBar.addTab(mReceiverTab, mReceiver);
        tabBar.addTab(mCriticalTab, mCritical);

        List<Fragment> mFragments = new ArrayList<>();
        for (int i = 0; i < tabBar.getTabCount(); i++) {
            mFragments.add(ShopOrderFragment.getInstance(i));
        }
        ReturnGoodsFragment shopOrderFragment = new ReturnGoodsFragment();
        mFragments.add(shopOrderFragment);
        OrderViewpagerAdapter mAdapter = new OrderViewpagerAdapter(getSupportFragmentManager(), mFragments);
        mPager.setAdapter(mAdapter);
        tabBar.setupWithViewPager(mPager);
//        mPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabBar));
        //tabBar设置点击联动
        tabBar.setOnTabSelectedListener(ShopOrderListActivity.this);
        mFlag = getIntent().getStringExtra("optFragmentFlag");
        if (mFlag == null) {
            mFlag = "0";
        }
        switch (mFlag) {
            case "0":
                mAllTab.select();
                break;
            case "1":
                mPager.setCurrentItem(1);
                break;
            case "2":
                mPager.setCurrentItem(2);
                break;
            case "3":
                mPager.setCurrentItem(3);
                break;
            case "4":
                mPager.setCurrentItem(4);
                break;
        }
        mPager.setOffscreenPageLimit(mFragments.size());
        WindowUtils.chenjin(this);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        //tabLayout滑动的时候，选中当前pager
        int position = tab.getPosition();
        mPager.setCurrentItem(position);

        switch (position) {
            case 0:
                custom_head.setTitle("全部");
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
