package com.taihuoniao.fineix.adapters;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.beans.DiscoverIndexBean;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by lilin on 2017/2/17.
 */

public class DiscoverIndexAdapter extends CommonBaseAdapter<DiscoverIndexBean> {


    public DiscoverIndexAdapter(List list, Activity activity) {
        super(list, activity);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        DiscoverIndexBean item = list.get(position);
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(activity, R.layout.item_discover_index, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv.setText(item.indexName);
        if (item.isSelected) {
            holder.colorBlock.setVisibility(View.VISIBLE);
            holder.tv.setTextColor(activity.getResources().getColor(R.color.yellow_bd8913));
        } else {
            holder.colorBlock.setVisibility(View.INVISIBLE);
            holder.tv.setTextColor(activity.getResources().getColor(R.color.color_222));
        }
        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.color_block)
        View colorBlock;
        @Bind(R.id.tv)
        TextView tv;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
