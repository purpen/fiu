package com.taihuoniao.fineix.order;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.app.FragmentActivity;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.TabPagerAdapter;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.view.CustomSlidingTab;
import com.taihuoniao.fineix.view.CustomViewPager;

import butterknife.Bind;

/**
 * @author lilin
 * created at 2016/3/28 17:24
 */
public class OrderListActivity extends BaseActivity {
    @Bind(R.id.custom_sliding_tab)
    CustomSlidingTab custom_sliding_tab;
    @Bind(R.id.custom_view_pager)
    CustomViewPager custom_view_pager;
    public OrderListActivity(){
        super(R.layout.activity_orderlist_layout);
    }

    @Override
    protected void initView() {
        custom_sliding_tab.setIndicatorColor(Color.RED);
        custom_sliding_tab.setTextColor(getResources().getColor(R.color.blue));
        custom_sliding_tab.setTypeface(null, Typeface.NORMAL);
        custom_sliding_tab.setIndicatorHeight(5);
        custom_sliding_tab.setUnderlineHeight(5);
        custom_sliding_tab.setTextSize(getResources().getDimensionPixelSize(R.dimen.sp14));
        custom_view_pager.setAdapter(new TabPagerAdapter(((FragmentActivity) activity).getSupportFragmentManager()));
        custom_sliding_tab.setViewPager(custom_view_pager);
    }

    @Override
    protected void installListener() {
        super.installListener();
    }

    @Override
    protected void requestNet() {
        super.requestNet();
    }

    @Override
    protected void initList() {
        super.initList();
    }

    @Override
    protected void refreshUI() {
        super.refreshUI();
    }
}
