package com.taihuoniao.fineix.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.beans.LocationBean;

import java.util.List;


public class SearchPOIAdapter extends BaseAdapter {
    private Context mContext;
    private List<LocationBean> cityPoiList;
    private CityPoiHolder holder;

    public SearchPOIAdapter(Context context, List<LocationBean> list) {
        this.mContext = context;
        this.cityPoiList = list;
    }

    @Override
    public int getCount() {
        if (cityPoiList != null) {
            return cityPoiList.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        if (cityPoiList != null) {
            return cityPoiList.get(position);
        } else {
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            holder = new CityPoiHolder();
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(
                    R.layout.item_poi_lv, null);
            holder.tv_short_name = (TextView) convertView
                    .findViewById(R.id.tv_short_name);
            holder.tv_poi_detail = (TextView) convertView
                    .findViewById(R.id.tv_poi_detail);
            convertView.setTag(holder);
        } else {
            holder = (CityPoiHolder) convertView.getTag();
        }
        LocationBean cityPoi = cityPoiList.get(position);
        holder.tv_short_name.setText(cityPoi.getLocName());
        holder.tv_poi_detail.setText(cityPoi.getAddStr());
        return convertView;
    }

    public class CityPoiHolder {
        public TextView tv_short_name, tv_poi_detail;
    }
}
