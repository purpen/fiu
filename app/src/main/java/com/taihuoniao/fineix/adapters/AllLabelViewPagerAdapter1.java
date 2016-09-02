//package com.taihuoniao.fineix.adapters;
//
//import android.content.Context;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.app.FragmentPagerAdapter;
//import android.view.ViewGroup;
//
//import com.taihuoniao.fineix.beans.AllLabelBean;
//import com.taihuoniao.fineix.scene.labelFragment.AllLabelFragment;
//
//import java.util.List;
//
///**
// * Created by taihuoniao on 2016/4/12.
// */
//public class AllLabelViewPagerAdapter1 extends FragmentPagerAdapter {
//    private Context context;
//    private List<AllLabelBean> allLabelList;
//    private AllLabelListViewAdapter.MoreClick moreClick;
//    private HotLabelViewPagerAdapter.LabelClick labelClick;
//    private AllLabelListViewAdapter adapter1, adapter2, adapter3, adapter;
//    private int maxHeight;
//
//
//    @Override
//    public CharSequence getPageTitle(int position) {
//        return allLabelList.get(position).getTitle_cn();
//    }
//
//
//    @Override
//    public int getCount() {
//        return allLabelList.size();
//    }
//
//    public AllLabelViewPagerAdapter1(FragmentManager fm, Context context, List<AllLabelBean> allLabelList, AllLabelListViewAdapter.MoreClick moreClick, HotLabelViewPagerAdapter.LabelClick labelClick) {
//        super(fm);
//        this.context = context;
//        this.allLabelList = allLabelList;
//        this.moreClick = moreClick;
//        this.labelClick = labelClick;
//    }
//
//    @Override
//    public Fragment getItem(int position) {
//        return AllLabelFragment.newInstance(position, allLabelList.get(position), moreClick, labelClick,AllLabelViewPagerAdapter1.this);
//    }
//
//    @Override
//    public void destroyItem(ViewGroup container, int position, Object object) {
//
//    }
//}
