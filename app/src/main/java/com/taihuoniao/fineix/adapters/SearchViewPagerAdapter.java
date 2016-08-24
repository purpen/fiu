package com.taihuoniao.fineix.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.taihuoniao.fineix.qingjingOrSceneDetails.fragment.SearchFragment;

import java.util.List;

/**
 * Created by taihuoniao on 2016/8/23.
 */
public class SearchViewPagerAdapter extends FragmentStatePagerAdapter {
    private List<SearchFragment> fragmentList;
    private List<String> titleList;

    public SearchViewPagerAdapter(FragmentManager fm, List<SearchFragment> fragmentList, List<String> titleList) {
        super(fm);
        this.fragmentList = fragmentList;
        this.titleList = titleList;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titleList.get(position);
    }
}
