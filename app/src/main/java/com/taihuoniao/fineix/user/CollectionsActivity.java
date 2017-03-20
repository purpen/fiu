package com.taihuoniao.fineix.user;

import android.os.Build;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.CollectViewPagerAdapter;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.beans.ItemQJCollect;
import com.taihuoniao.fineix.user.fragments.FavoriteProductFragment;
import com.taihuoniao.fineix.user.fragments.FavoriteQJFragment;
import com.taihuoniao.fineix.utils.WindowUtils;
import com.taihuoniao.fineix.view.CustomHeadView;

import java.lang.reflect.Field;

import butterknife.Bind;

/**
 * @author lilin
 *         created at 2016/6/21 15:43
 */
public class CollectionsActivity extends BaseActivity<ItemQJCollect> {
    @Bind(R.id.custom_head)
    CustomHeadView custom_head;
    @Bind(R.id.tabLayout)
    TabLayout tabLayout;
    @Bind(R.id.viewPager)
    ViewPager viewPager;

    public CollectionsActivity() {
        super(R.layout.activity_collect);
    }

    @Override
    protected void initView() {
        custom_head.setHeadCenterTxtShow(true, "收藏");
        String[] array = getResources().getStringArray(R.array.favorite_tabs);
        for (int i = 0; i < array.length; i++) {
            if (i == 0) {
                tabLayout.addTab(tabLayout.newTab().setText(array[0]), true);
            } else {
                tabLayout.addTab(tabLayout.newTab().setText(array[i]), false);
            }
        }

        Fragment[] fragments = {FavoriteQJFragment.newInstance(), FavoriteProductFragment.newInstance()};
        CollectViewPagerAdapter adapter = new CollectViewPagerAdapter(getSupportFragmentManager(), fragments, array);
        viewPager.setAdapter(adapter);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setupWithViewPager(viewPager);
        WindowUtils.chenjin(this);
    }


    private void setIndicatorWidth() throws NoSuchFieldException, IllegalAccessException {
        int margin = activity.getResources().getDimensionPixelSize(R.dimen.dp75);
        Class<?> tablayout = tabLayout.getClass();
        Field tabStrip = tablayout.getDeclaredField("mTabStrip");
        tabStrip.setAccessible(true);
        LinearLayout ll_tab = (LinearLayout) tabStrip.get(tabLayout);
        for (int i = 0; i < ll_tab.getChildCount(); i++) {
            View child = ll_tab.getChildAt(i);
            child.setPadding(0, 0, 0, 0);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                params.setMarginStart(margin);
                params.setMarginEnd(margin);
            } else {
                params.setMargins(margin, 0, margin, 0);
            }
            child.setLayoutParams(params);
            child.invalidate();
        }
    }

    @Override
    protected void installListener() {
        tabLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                try {
                    setIndicatorWidth();
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    tabLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    tabLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
            }
        });
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}
