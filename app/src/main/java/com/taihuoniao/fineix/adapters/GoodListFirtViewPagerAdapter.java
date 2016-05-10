package com.taihuoniao.fineix.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.taihuoniao.fineix.beans.CategoryBean;
import com.taihuoniao.fineix.product.fragment.GoodListFragment;

/**
 * Created by taihuoniao on 2016/5/4.
 */
public class GoodListFirtViewPagerAdapter extends FragmentStatePagerAdapter {
    private CategoryBean categoryBean;
//    private List<GoodListFragment> fragmentList;

    public GoodListFirtViewPagerAdapter(FragmentManager fm, CategoryBean categoryBean) {
        super(fm);
        this.categoryBean = categoryBean;
//        fragmentList = new ArrayList<>();
//        for(int i=0;i<categoryBean.getList().size();i++){
//            fragmentList.add(GoodListFragment.newInstance(i,categoryBean));
//        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return categoryBean.getList().get(position).getTitle();
    }

    @Override
    public Fragment getItem(int position) {
        return GoodListFragment.newInstance(position,categoryBean
        );
    }

    @Override
    public int getCount() {
        return categoryBean.getList().size();
    }

//    @Override
//    public Object instantiateItem(ViewGroup container, int position) {
//        return super.instantiateItem(container, position);
//    }

//    @Override
//    public void destroyItem(ViewGroup container, int position, Object object) {
//        container.removeView(container.getChildAt(position));
//    }
}
