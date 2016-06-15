package com.taihuoniao.fineix.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.taihuoniao.fineix.beans.CategoryBean;
import com.taihuoniao.fineix.scene.addProductFragment.AddProductsFragment;

/**
 * Created by taihuoniao on 2016/4/13.
 */
public class AddProductViewPagerAdapter extends FragmentPagerAdapter {
    private CategoryBean categoryBean;

    public AddProductViewPagerAdapter(FragmentManager fm, CategoryBean categoryBean) {
        super(fm);
        this.categoryBean = categoryBean;
    }

    @Override
    public Fragment getItem(int position) {
        return AddProductsFragment.newInstance(position, categoryBean);
    }

    @Override
    public CharSequence getPageTitle(int position) {
//        if (position == 0) {
//            return "全部";
//        }
        return categoryBean.getList().get(position ).getTitle();
    }

    @Override
    public int getCount() {
        return categoryBean.getList().size() ;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

    }
}
