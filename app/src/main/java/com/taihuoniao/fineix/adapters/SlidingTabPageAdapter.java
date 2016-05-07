package com.taihuoniao.fineix.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.taihuoniao.fineix.scene.fragments.CJResultFragment;
import com.taihuoniao.fineix.scene.fragments.ProductResultFragment;
import com.taihuoniao.fineix.scene.fragments.QJResultFragment;

import java.util.List;

/**
 * @author lilin
 *         created at 2016/3/29 11:58
 */
public class SlidingTabPageAdapter extends FragmentPagerAdapter {
    private List<String> list;
    private FragmentManager fm;
    //    private Class<Fragment>[] clazzes;
    private String q;
    private String t;

    public SlidingTabPageAdapter(FragmentManager fm, List<String> list, String q, String t) {
        super(fm);
        this.list = list;
        this.fm = fm;
//        this.clazzes = clazzes;
        this.q = q;
        this.t = t;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 1:
                return CJResultFragment.newInstance(q, t);
            case 2:
                return ProductResultFragment.newInstance(q, t);
            default:
                return QJResultFragment.newInstance(q, t);
        }
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
