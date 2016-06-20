package com.taihuoniao.fineix.adapters;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.beans.QingJingItem;
import com.taihuoniao.fineix.utils.Util;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author lilin
 * created at 2016/4/14 15:04
 */
public class NearByQJAdapter extends CommonBaseAdapter<QingJingItem>{
    public NearByQJAdapter(Activity activity, List<QingJingItem> list) {
        super(list,activity);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        QingJingItem item = list.get(position);
        ViewHolder holder;
        if (convertView==null){
            convertView = Util.inflateView(R.layout.item_poi_lv, null);
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder= (ViewHolder)convertView.getTag();
        }
        holder.tv_short_name.setText(item.title);
        holder.tv_poi_detail.setText(item.address);
        return convertView;
    }

    static class ViewHolder{
        @Bind(R.id.tv_short_name)
        TextView tv_short_name;
        @Bind(R.id.tv_poi_detail)
        TextView tv_poi_detail;
        public ViewHolder(View view){
            ButterKnife.bind(this,view);
        }
    }
}
