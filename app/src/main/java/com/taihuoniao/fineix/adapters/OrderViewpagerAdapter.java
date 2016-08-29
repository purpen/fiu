package com.taihuoniao.fineix.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by android on 2016/3/9.
 */
public class OrderViewpagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragments;

    public OrderViewpagerAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {

        return fragments.get(position);
    }

    @Override
    public int getCount() {
        int ret = 0;
        if (fragments != null) {
            ret = fragments.size();
        }
        return ret;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {

            case 1:
                return "待付款";

            case 2:
                return "待发货";

            case 3:
                return "待收货";

            case 4:
                return "待评价";

            case 5:
                return "退款/售后";
            default:
                return "全部";
        }
    }
    //    @Override
//    public int getItemPosition(Object object) {
//        return POSITION_NONE;
//    }
//    @Override
//    public void destroyItem(ViewGroup container, int position, Object object) {
//        super.destroyItem(container, position, object);
//
//    }
}


