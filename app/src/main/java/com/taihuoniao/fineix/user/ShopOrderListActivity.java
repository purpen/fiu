package com.taihuoniao.fineix.user;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.OrderViewpagerAdapter;
import com.taihuoniao.fineix.user.fragments.ShopOrderFragment;
import com.taihuoniao.fineix.utils.ActivityUtil;
import com.taihuoniao.fineix.view.MyGlobalTitleLayout;
import com.taihuoniao.fineix.view.WaittingDialog;

import java.util.ArrayList;
import java.util.List;

public class ShopOrderListActivity extends FragmentActivity implements TabLayout.OnTabSelectedListener {
    private WaittingDialog mDialog;
    private ViewPager mPager;
    private TabLayout.Tab mAllTab, mPayTab, mDeliverTab, mCriticalTab, mReceiverTab;
    private MyGlobalTitleLayout title;
    private String mFlag;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        StatusBarChange.initWindow(this);
        setContentView(R.layout.activity_shop_order_list);
        ActivityUtil.getInstance().addActivity(this);
        initView();
    }

    private void initView() {
        title = (MyGlobalTitleLayout) findViewById(R.id.order_title);
        title.setBackgroundResource(R.color.white);
        title.setTitleColor(getResources().getColor(R.color.black333333));
        title.setBackImg(R.mipmap.back_black);
        title.setBackButtonVisibility(true);
        title.setRightSearchButton(false);
        title.setRightShopCartButton(false);

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
        OrderViewpagerAdapter mAdapter = new OrderViewpagerAdapter(getSupportFragmentManager(), mFragments);
        mPager.setAdapter(mAdapter);
        mPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabBar));
        //tabBar设置点击联动
        tabBar.setOnTabSelectedListener(ShopOrderListActivity.this);
        mFlag = getIntent().getStringExtra("optFragmentFlag");
        if (mFlag == null) {
            mFlag = "0";
        }
        switch (mFlag) {
            case "0":
                mAll = true;
                mAllTab.select();
                break;
            case "1":
                mPay = true;
                mPager.setCurrentItem(1);
                break;
            case "2":
                mDeliver = true;
                mPager.setCurrentItem(2);
                break;
            case "3":
                mReceiver = true;
                mPager.setCurrentItem(3);
                break;
            case "4":
                mCritical = true;
                mPager.setCurrentItem(4);
                break;
        }
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        //tabLayout滑动的时候，选中当前pager
        int position = tab.getPosition();
        mPager.setCurrentItem(position);

        switch (position) {
            case 0:
                title.setTitle("全部");
                break;
            case 1:
                title.setTitle("待付款");
                break;
            case 2:
                title.setTitle("待发货");
                break;
            case 3:
                title.setTitle("待收货");
                break;
            case 4:
                title.setTitle("待评价");
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
