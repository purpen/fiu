package com.taihuoniao.fineix.adapters;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.beans.ThemeQJ;
import com.taihuoniao.fineix.utils.Util;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author lilin
 *         created at 2016/4/11 14:01
 */
public class SubscribeThemeAdapter extends CommonBaseAdapter<ThemeQJ> {
    public SubscribeThemeAdapter(ArrayList<ThemeQJ> list, Activity activity) {
        super(list, activity);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ThemeQJ item = list.get(position);
        ViewHolder holder;
        if (convertView == null) {
            convertView = Util.inflateView(R.layout.item_subcribe_theme, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ImageLoader.getInstance().displayImage(item.back_url, holder.iv, options);
        holder.tv.setText(item.title);
        holder.tv_count.setText(String.format("已有%s人订阅", item.sub_count));
        if (item.isSubscribed) {
            holder.tv.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.subscribed, 0, 0, 0);
            holder.view_bg.setVisibility(View.VISIBLE);
        } else {
            holder.tv.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.circle_add, 0, 0, 0);
            holder.view_bg.setVisibility(View.GONE);
        }
        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.iv)
        ImageView iv;
        @Bind(R.id.tv)
        TextView tv;
        @Bind(R.id.view_bg)
        View view_bg;
        @Bind(R.id.tv_count)
        TextView tv_count;
        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
