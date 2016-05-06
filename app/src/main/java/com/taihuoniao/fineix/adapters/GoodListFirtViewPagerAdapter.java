package com.taihuoniao.fineix.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.taihuoniao.fineix.beans.CategoryBean;
import com.taihuoniao.fineix.product.fragment.GoodListFragment;

/**
 * Created by taihuoniao on 2016/5/4.
 */
public class GoodListFirtViewPagerAdapter extends FragmentPagerAdapter {
    private CategoryBean categoryBean;

    public GoodListFirtViewPagerAdapter(FragmentManager fm, CategoryBean categoryBean) {
        super(fm);
        this.categoryBean = categoryBean;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return categoryBean.getList().get(position).getTitle();
    }

    @Override
    public Fragment getItem(int position) {
        return GoodListFragment.newInstance(position, categoryBean);
    }

    @Override
    public int getCount() {
        return categoryBean.getList().size();
    }


}
