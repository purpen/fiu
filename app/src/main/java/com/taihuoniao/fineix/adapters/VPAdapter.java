package com.taihuoniao.fineix.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.taihuoniao.fineix.beans.SceneListBean;
import com.taihuoniao.fineix.qingjingOrSceneDetails.ViewPagerFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by taihuoniao on 2016/7/19.
 */
public class VPAdapter extends FragmentStatePagerAdapter {
    private Context context;
    private List<SceneListBean> list;
    private List<ViewPagerFragment> fragmentList;

    public VPAdapter(FragmentManager fm, Context context, List<SceneListBean> list) {
        super(fm);
        this.context = context;
        this.list = list;
        fragmentList = new ArrayList<>(list.size());
        for (SceneListBean sceneListBean : list) {
            fragmentList.add(null);
        }
    }

    @Override
    public Fragment getItem(int position) {
        if (fragmentList.size() <= position + 1) {
            List<ViewPagerFragment> cList = new ArrayList<>(fragmentList);
            for (int i = cList.size(); i < list.size(); i++) {
                cList.add(null);
            }
            fragmentList.addAll(cList);
        }
//        Log.e("<<<", "fragmentList.size=" + fragmentList.size() + ",position=" + position + ",list.size=" + list.size());
        ViewPagerFragment viewPagerFragment = ViewPagerFragment.newInstance(list.get(position).get_id());
        fragmentList.set(position, viewPagerFragment);
        return viewPagerFragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
        fragmentList.set(position, null);
    }

    public View getView(int position) {
        return fragmentList.get(position).view;
    }

    @Override
    public int getCount() {
        return list.size();
    }

}
