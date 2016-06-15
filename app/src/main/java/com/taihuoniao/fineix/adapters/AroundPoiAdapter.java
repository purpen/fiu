package com.taihuoniao.fineix.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.baidu.mapapi.search.core.PoiInfo;
import com.taihuoniao.fineix.R;

import java.util.List;


/**
 * Created by Amy on 2016/3/22.
 */
public class AroundPoiAdapter extends BaseAdapter {

    private Context mContext;
    private List<PoiInfo> mkPoiInfoList;

    public AroundPoiAdapter(Context context, List<PoiInfo> list) {
        this.mContext = context;
        this.mkPoiInfoList = list;
    }

    @Override
    public int getCount() {
        return mkPoiInfoList.size();
    }

    @Override
    public Object getItem(int position) {
        if (mkPoiInfoList != null) {
            return mkPoiInfoList.get(position);
        }
        return null;
    }

    public void setNewList(List<PoiInfo> list, int index) {
        this.mkPoiInfoList = list;
        int selected = index;
        this.notifyDataSetChanged();


    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        RecordHolder holder;
        if (convertView == null) {
            holder = new RecordHolder();
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(
                    R.layout.item_poi_lv, null);
            holder.tv_short_name = (TextView) convertView
                    .findViewById(R.id.tv_short_name);
            holder.tv_poi_detail = (TextView) convertView
                    .findViewById(R.id.tv_poi_detail);
            convertView.setTag(holder);
        } else {
            holder = (RecordHolder) convertView.getTag();
        }
        holder.tv_short_name.setText(mkPoiInfoList.get(position).name);
        holder.tv_poi_detail.setText(mkPoiInfoList.get(position).address);
        // if (selected >= 0 && selected == position) {
        // holder.rlMLPIItem.setSelected(true);
        // } else {
        // holder.rlMLPIItem.setSelected(false);
        // }
        return convertView;
    }

    public class RecordHolder {
        public TextView tv_poi_detail, tv_short_name;
    }
}
