package com.taihuoniao.fineix.adapters;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.beans.User;

import java.util.ArrayList;

/**
 * @author lilin
 *         created at 2016/4/11 14:01
 */
public class FocusInterestedUserViewPagerAdapter extends PagerAdapter {
    private ArrayList<User> list;
    private Activity activity;
    private int size;

    public FocusInterestedUserViewPagerAdapter(ArrayList<User> list, Activity activity) {
        this.list = list;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        if (list == null) return 0;
        size = list.size();
        if (size == 0) return 0;
        if (size <= 6) return 1;
        if (size % 6 == 0) {
            return size / 6;
        } else {
            return size / 6 + 1;
        }
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        FocusInterestAdapter adapter;
        GridView gridView = new GridView(activity);
        gridView.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
        gridView.setVerticalScrollBarEnabled(false);
        gridView.setHorizontalSpacing(activity.getResources().getDimensionPixelSize(R.dimen.dp10));
        gridView.setVerticalSpacing(activity.getResources().getDimensionPixelSize(R.dimen.dp10));
        gridView.setNumColumns(3);
        if (size <= 6) {
            adapter = new FocusInterestAdapter(list, activity);
        } else if (size % 6 == 0) {
            adapter = new FocusInterestAdapter(list.subList(6 * position, 6 * position + 6), activity);
        } else {
            if (position <= size / 6 - 1) {
                adapter = new FocusInterestAdapter(list.subList(6 * position, 6 * position + 6), activity);
            } else {
                adapter = new FocusInterestAdapter(list.subList(6 * position, size), activity);
            }
        }
        gridView.setAdapter(adapter);
        container.addView(gridView);
        return gridView;
    }
}
