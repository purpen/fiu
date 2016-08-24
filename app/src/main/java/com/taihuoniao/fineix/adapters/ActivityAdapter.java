package com.taihuoniao.fineix.adapters;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.beans.DataChooseSubject;
import com.taihuoniao.fineix.utils.Util;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author lilin
 *         created at 2016/8/17 15:29
 */
public class ActivityAdapter extends CommonBaseAdapter<DataChooseSubject.ItemChoosenSubject> {
    public ActivityAdapter(ArrayList list, Activity activity) {
        super(list, activity);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final DataChooseSubject.ItemChoosenSubject item = list.get(position);
        ViewHolder holder;
        if (convertView == null) {
            convertView = Util.inflateView(R.layout.item_activity, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ImageLoader.getInstance().displayImage(item.cover_url, holder.imageView, options);
        holder.tvDuring.setText(String.format("活动时间：%s-%s", item.begin_time_at, item.end_time_at));
        if (TextUtils.equals("2", item.evt)) {
            holder.ivFinish.setVisibility(View.VISIBLE);
        } else {
            holder.ivFinish.setVisibility(View.GONE);
        }
        holder.tvTitle.setText(item.title);
        holder.tvDesc.setText(item.short_title);
        holder.tvCount.setText(String.format("已有%s人参与", item.view_count));
        return convertView;
    }


    static class ViewHolder {
        @Bind(R.id.tv_during)
        TextView tvDuring;
        @Bind(R.id.iv_finish)
        ImageView ivFinish;
        @Bind(R.id.imageView)
        ImageView imageView;
        @Bind(R.id.tv_title)
        TextView tvTitle;
        @Bind(R.id.tv_count)
        TextView tvCount;
        @Bind(R.id.tv_desc)
        TextView tvDesc;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
