package com.taihuoniao.fineix.adapters;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import java.util.List;
/**
 * @author lilin
 *         created at 2016/3/29 11:58
 */
public class SlidingTabPageAdapter extends FragmentPagerAdapter {
    private List<String> list;
    private FragmentManager fm;
    private Class<Fragment>[] clazzes;
    public SlidingTabPageAdapter(FragmentManager fm,Class[] clazzes,List<String> list) {
        super(fm);
        this.list = list;
        this.fm = fm;
        this.clazzes=clazzes;
    }

    @Override
    public Fragment getItem(int position) {
        return initFragments(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return list.get(position);
    }


    public Fragment initFragments(int position){
        try {
            return clazzes[position].newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
