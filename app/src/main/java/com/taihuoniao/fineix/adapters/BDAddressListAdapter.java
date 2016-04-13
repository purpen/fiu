package com.taihuoniao.fineix.adapters;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.baidu.mapapi.search.core.PoiInfo;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.utils.Util;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author lilin
 *         created at 2016/4/12 18:41
 */
public class BDAddressListAdapter extends CommonBaseAdapter<PoiInfo> {

    public BDAddressListAdapter(Activity activity, ArrayList<PoiInfo> list) {
        super(list,activity);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PoiInfo info = list.get(position);
        ViewHolder holder=null;
        if (convertView==null){
            convertView = Util.inflateView(activity, R.layout.item_poi_lv, null);
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder= (ViewHolder)convertView.getTag();
        }
        holder.tv_short_name.setText(info.name);
        holder.tv_poi_detail.setText(info.address);
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
