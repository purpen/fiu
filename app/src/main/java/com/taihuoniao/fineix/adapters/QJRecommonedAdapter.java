package com.taihuoniao.fineix.adapters;


import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.beans.QingJingItem;
import com.taihuoniao.fineix.utils.GlideUtils;
import com.taihuoniao.fineix.utils.Util;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;


public class QJRecommonedAdapter extends CommonBaseAdapter<QingJingItem> {
    public QJRecommonedAdapter(ArrayList<QingJingItem> list,Activity activity) {
       super(list,activity);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        QingJingItem item = list.get(position);
        ViewHolder holder;
        if (convertView == null) {
            convertView = Util.inflateView(R.layout.item_qj_layout, null);
            holder = new ViewHolder(convertView);
            holder.iv=(ImageView) convertView.findViewById(R.id.iv);
            holder.tv_desc = (TextView) convertView
                    .findViewById(R.id.tv_desc);
            holder.tv_location = (TextView) convertView
                    .findViewById(R.id.tv_location);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        GlideUtils.displayImage(item.cover_url,holder.iv);
        holder.tv_desc.setText(item.title);
        holder.tv_location.setText(item.address);
        return convertView;
    }

    public class ViewHolder {
        @Bind(R.id.iv)
        ImageView iv;
        @Bind(R.id.tv_desc)
        TextView tv_desc;
        @Bind(R.id.tv_location)
        TextView tv_location;
        public ViewHolder(View view){
            ButterKnife.bind(this,view);
        }
    }
}
