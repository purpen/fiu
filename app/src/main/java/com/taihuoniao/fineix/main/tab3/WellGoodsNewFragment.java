package com.taihuoniao.fineix.main.tab3;

import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.base.BaseFragment;
import com.taihuoniao.fineix.main.tab3.adapter.WellGoodsAdapter;
import com.taihuoniao.fineix.utils.DensityUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Stephen on 2017/3/3 20:36
 * Email: 895745843@qq.com
 */

public class WellGoodsNewFragment extends BaseFragment implements ViewPager.OnPageChangeListener {

    @Bind(R.id.tabLayout_wellGoods_category)
    TabLayout tabLayoutWellGoodsCategory;
    @Bind(R.id.viewPager_wellGoods_list)
    ViewPager viewPagerWellGoodsList;

    private List<String> mStringList;
    private List<BaseFragment> mBaseFragments;

    @Override
    protected void requestNet() {

    }

    @Override
    protected View initView() {
        View view = View.inflate(getActivity(), R.layout.fragment_well_goods, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    private void initTabLayoutAndViewPager() {
        WellGoodsAdapter wellGoodsAdapter = new WellGoodsAdapter(getFragmentManager(), mStringList, mBaseFragments);
        viewPagerWellGoodsList.setAdapter(wellGoodsAdapter);
        viewPagerWellGoodsList.addOnPageChangeListener(this);
        tabLayoutWellGoodsCategory.setupWithViewPager(viewPagerWellGoodsList);
        tabLayoutWellGoodsCategory.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                try {
                    setIndicatorWidth();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    tabLayoutWellGoodsCategory.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    tabLayoutWellGoodsCategory.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
            }
        });
        viewPagerWellGoodsList.setOffscreenPageLimit(mBaseFragments.size());
        viewPagerWellGoodsList.setCurrentItem(0, false);
    }

    /**
     * 默认加载 两条
     */
    private void initTabLayout() {
        mStringList = new ArrayList<>();
        mBaseFragments = new ArrayList<>();

        // 默认加载 推荐 情境
        mStringList.add("推荐");
        mStringList.add("情境");

        mBaseFragments.add(new WellGoodsFragment01());
        mBaseFragments.add(new WellGoodsFragment02());
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        // TODO: 2017/3/3 跳过一页

        Toast.makeText(activity, mStringList.get(position), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private void setIndicatorWidth() throws NoSuchFieldException, IllegalAccessException {
//        int margin = activity.getResources().getDimensionPixelSize(R.dimen.dp15);
        if (mBaseFragments.size() <= 0) {
            return;
        }
        int margin = (int) (((double) tabLayoutWellGoodsCategory.getMeasuredWidth() / mBaseFragments.size() - DensityUtils.sp2px(getActivity(), 42)) / 2);
        Class<?> tablayout = tabLayoutWellGoodsCategory.getClass();
        Field tabStrip = tablayout.getDeclaredField("mTabStrip");
        tabStrip.setAccessible(true);
        LinearLayout ll_tab = (LinearLayout) tabStrip.get(tabLayoutWellGoodsCategory);
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
    protected void initList() {
        initTabLayout();
        initTabLayoutAndViewPager();
    }
}
