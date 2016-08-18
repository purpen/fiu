package com.taihuoniao.fineix.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * @author lilin
 *         created at 2016/8/8 13:03
 */
public class CollectViewPagerAdapter extends FragmentPagerAdapter {
    private Fragment[] fragments;
    private String[] titles;

    public CollectViewPagerAdapter(FragmentManager manager, Fragment[] fragments, String[] titles) {
        super(manager);
        this.fragments = fragments;
        this.titles = titles;
        if (fragments == null || fragments.length <= 0)
            throw new IllegalArgumentException("fragments==null || fragments.length<=0");
    }

    @Override
    public int getCount() {
        return fragments.length;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments[position];
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (titles == null || titles.length == 0) return "";
        return titles[position];
    }
}
