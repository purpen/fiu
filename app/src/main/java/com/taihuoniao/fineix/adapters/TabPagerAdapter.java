package com.taihuoniao.fineix.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.taihuoniao.fineix.order.fragments.OrderFragment1;

/**
 * @author lilin
 * created at 2016/3/29 11:58
 */
public class TabPagerAdapter extends FragmentPagerAdapter{
    OrderFragment1 indexFragment=null;
    String[] title = {"大家说", "详情", "进展","你好漂亮","hello","good good","tab","page","style"};
    public TabPagerAdapter(FragmentManager fm) {
        super(fm);
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
        return title.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return title[position];
    }

}
