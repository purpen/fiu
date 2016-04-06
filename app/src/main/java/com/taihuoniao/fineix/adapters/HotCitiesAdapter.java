package com.taihuoniao.fineix.adapters;

import android.app.Activity;
import android.content.res.Resources;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.TextView;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.beans.City;
import com.taihuoniao.fineix.utils.LogUtil;

import java.util.ArrayList;

/**
 * @author lilin
 * created at 2016/3/30 16:31
 */
public class HotCitiesAdapter extends CommonBaseAdapter<City> {
    public HotCitiesAdapter(ArrayList<City> list, Activity activity){
        super(list,activity);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        City city = list.get(position);
        TextView textView=new TextView(activity);
        Resources resources=activity.getResources();
        textView.setBackgroundDrawable(resources.getDrawable(R.drawable.border_radius5));
        textView.setText(city.name);
        AbsListView.LayoutParams lp=new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT,resources.getDimensionPixelSize(R.dimen.dp35));
        textView.setLayoutParams(lp);
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(12);
        textView.setBackgroundResource(R.drawable.selector_hotcity);
        textView.setTextColor(resources.getColor(R.color.color_333));
        LogUtil.e(TAG, list.size() + ";" + list.get(position));
        return textView;
    }

}
