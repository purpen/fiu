package com.taihuoniao.fineix.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.beans.CategoryListBean;
import com.taihuoniao.fineix.view.roundImageView.RoundedImageView;

import java.util.List;

/**
 * Created by taihuoniao on 2016/7/21.
 * 情景分类
 */
public class SceneCategoryAdapter extends BaseAdapter {
    private Context context;
    private List<CategoryListBean.CategoryListItem> list;

    public SceneCategoryAdapter(List<CategoryListBean.CategoryListItem> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_scene_category, null);
            holder = new ViewHolder();
            holder.img = (RoundedImageView) convertView.findViewById(R.id.img);
            holder.tv = (TextView) convertView.findViewById(R.id.tv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ImageLoader.getInstance().displayImage(list.get(position).getApp_cover_url(), holder.img);
        holder.tv.setText(list.get(position).getTitle());
        return convertView;
    }

    static class ViewHolder {
        RoundedImageView img;
        TextView tv;
    }
}
