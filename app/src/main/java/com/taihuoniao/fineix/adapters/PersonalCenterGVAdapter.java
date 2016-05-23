package com.taihuoniao.fineix.adapters;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.beans.ImgTxtItem;
import com.taihuoniao.fineix.utils.Util;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author lilin
 * created at 2016/4/11 14:01
 */
public class PersonalCenterGVAdapter extends CommonBaseAdapter<ImgTxtItem>{
    public PersonalCenterGVAdapter(ArrayList<ImgTxtItem> list, Activity activity){
        super(list,activity);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImgTxtItem item = list.get(position);
        ViewHolder holder=null;
        if (convertView==null){
            convertView = Util.inflateView(activity, R.layout.item_gv_personalcenter, null);
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder)convertView.getTag();
        }
        ImageLoader.getInstance().displayImage("drawable://" + item.imgId,holder.iv,options);
        holder.tv.setText(item.txt);
        if (item.count>0){
            holder.tv_num.setVisibility(View.VISIBLE);
            if (item.count<=99)
                holder.tv_num.setText(String.valueOf(item.count));
            else {
                holder.tv_num.setText("+99");
            }
        }else {
            holder.tv_num.setVisibility(View.GONE);
        }
        return convertView;
    }

    static class ViewHolder{
        @Bind(R.id.iv)
        ImageView iv;
        @Bind(R.id.tv)
        TextView tv;
        @Bind(R.id.tv_num)
        TextView tv_num;
        public ViewHolder(View view){
            ButterKnife.bind(this,view);
        }
    }
}
