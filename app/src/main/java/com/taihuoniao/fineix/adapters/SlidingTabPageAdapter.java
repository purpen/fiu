package com.taihuoniao.fineix.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.taihuoniao.fineix.order.fragments.OrderFragment1;

import java.util.List;

/**
 * @author lilin
 * created at 2016/3/29 11:58
 */
public class SlidingTabPageAdapter extends FragmentPagerAdapter{
    OrderFragment1 indexFragment=null;
    private List<String> list;
    public SlidingTabPageAdapter(FragmentManager fm, List<String> list) {
        super(fm);
        this.list=list;
    }
    @Override
    public Fragment getItem(int position) {
        indexFragment = new OrderFragment1();
        return indexFragment;
//            switch (position) {
//                case 0:
//                    indexFragment = new OrderFragment1();
//                    return indexFragment;
//                case 1:
//                    findFragment = new OrderFragment2();
//                    return findFragment;
//                case 2:
//                    personalCenterFragment = new OrderFragment3();
//                    return personalCenterFragment;
//                default:
//                    return null;
//            }
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return list.get(position);
    }

}
