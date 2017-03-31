package com.taihuoniao.fineix.adapters;

import android.app.Activity;
import android.os.Build;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.beans.DataChooseSubject;
import com.taihuoniao.fineix.utils.GlideUtils;
import com.taihuoniao.fineix.utils.Util;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author lilin
 *         created at 2016/8/17 15:29
 */
public class SalePromotionAdapter extends CommonBaseAdapter<DataChooseSubject.ItemChoosenSubject> {
    public SalePromotionAdapter(ArrayList list, Activity activity) {
        super(list, activity);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final DataChooseSubject.ItemChoosenSubject item = list.get(position);
        final ViewHolder holder;
        if (convertView == null) {
            convertView = Util.inflateView(R.layout.item_sale_promotion, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        GlideUtils.displayImage(item.cover_url, holder.imageView);
        if (!TextUtils.isEmpty(item.title)) {
            holder.tvTitle.setText(item.title);
            holder.tvTitle.setBackgroundColor(activity.getResources().getColor(android.R.color.black));
        }
        if (TextUtils.equals("2", item.evt)) {
            holder.ivFinish.setVisibility(View.VISIBLE);
            holder.tvDuring.setText("已结束");
        } else {
            holder.ivFinish.setVisibility(View.GONE);
            holder.tvDuring.setText(String.format("%s-%s", item.begin_time_at, item.end_time_at));
        }
        holder.tvDesc.setText(item.short_title);
        holder.tvCount.setText(String.format("%s人浏览", item.view_count));
        holder.tvTitle.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int width = holder.tvTitle.getMeasuredWidth();
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, activity.getResources().getDimensionPixelSize(R.dimen.dp2));
                params.gravity = Gravity.CENTER_HORIZONTAL;
                holder.viewLine.setLayoutParams(params);
                holder.viewLine.setBackgroundColor(activity.getResources().getColor(R.color.color_af8323));
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
                    holder.tvTitle.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    holder.tvTitle.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
            }
        });
        return convertView;
    }


    static class ViewHolder {
        @Bind(R.id.imageView)
        ImageView imageView;
        @Bind(R.id.tv_title)
        TextView tvTitle;
        @Bind(R.id.iv_finish)
        ImageView ivFinish;
        @Bind(R.id.tv_desc)
        TextView tvDesc;
        @Bind(R.id.tv_count)
        TextView tvCount;
        @Bind(R.id.view_line)
        View viewLine;
        @Bind(R.id.tv_during)
        TextView tvDuring;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
