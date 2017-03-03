package com.taihuoniao.fineix.main.tab3.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.taihuoniao.fineix.base.BaseFragment;

import java.util.List;

/**
 * Created by Stephen on 2017/3/3 23:11
 * Email: 895745843@qq.com
 */

public class WellGoodsAdapter extends FragmentPagerAdapter {
    private List<BaseFragment> fragmentList;
    private List<String> titleList;

    public WellGoodsAdapter(FragmentManager fm, List<String> titleList, List<BaseFragment> fragmentList) {
        super(fm);
        this.titleList = titleList;
        this.fragmentList = fragmentList;
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
