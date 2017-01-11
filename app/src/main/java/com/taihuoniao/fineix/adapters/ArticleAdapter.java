package com.taihuoniao.fineix.adapters;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
public class ArticleAdapter extends CommonBaseAdapter<DataChooseSubject.ItemChoosenSubject> {
    public ArticleAdapter(ArrayList list, Activity activity) {
        super(list, activity);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final DataChooseSubject.ItemChoosenSubject item = list.get(position);
        ViewHolder holder;
        if (convertView == null) {
            convertView = Util.inflateView(R.layout.item_artical, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
//        ImageLoader.getInstance().displayImage(item.cover_url, holder.imageView, options);
        GlideUtils.displayImage(item.cover_url, holder.imageView);
        holder.tvTitle.setText(item.title);
        holder.tvCount.setText(String.format("%s人浏览", item.view_count));
        return convertView;
    }


    static class ViewHolder {
        @Bind(R.id.imageView)
        ImageView imageView;
        @Bind(R.id.tv_title)
        TextView tvTitle;
        @Bind(R.id.tv_count)
        TextView tvCount;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
