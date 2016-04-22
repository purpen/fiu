package com.taihuoniao.fineix.adapters;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Gallery;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.utils.LogUtil;
import com.taihuoniao.fineix.utils.Util;
import com.taihuoniao.fineix.view.SlidingFocusImageView;
import java.util.List;

/**
 * @author lilin
 * created at 2016/4/22 10:34
 */
public class SlidingFocusAdapter<T> extends CommonBaseAdapter<T>{
    private SlidingFocusImageView sfiv;
    public SlidingFocusAdapter(SlidingFocusImageView sfiv, List<T> list, Activity activity){
        super(list,activity);
        this.sfiv=sfiv;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        T item=list.get(position);
        ViewHolder holder =null;
        if (convertView==null) {
            holder=new ViewHolder();
            convertView = holder.iv=new ImageView(activity);
            convertView.setTag(holder);
            holder.iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }else {
            holder= (ViewHolder)convertView.getTag();
        }

        if (item instanceof String){
            ImageLoader.getInstance().displayImage((String)item,holder.iv);
        }
        LogUtil.e("ImgSRC",item+"");
        if (item instanceof Integer){
            ImageLoader.getInstance().displayImage("drawable://"+(Integer)item,holder.iv);
        }

        if (sfiv.getSelectedItemPosition() == position) {
            convertView.setLayoutParams(new Gallery.LayoutParams(Util.getScreenWidth()-activity.getResources().getDimensionPixelSize(R.dimen.dp150),activity.getResources().getDimensionPixelSize(R.dimen.dp150)));
        } else {
            convertView.setLayoutParams(new Gallery.LayoutParams(Util.getScreenWidth()-activity.getResources().getDimensionPixelSize(R.dimen.dp150),activity.getResources().getDimensionPixelSize(R.dimen.dp150)));
        }
        return convertView;
    }

    static class ViewHolder{
        ImageView iv;
    }
}
