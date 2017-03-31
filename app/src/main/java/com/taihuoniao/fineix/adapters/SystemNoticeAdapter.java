package com.taihuoniao.fineix.adapters;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.beans.SystemNoticeData;
import com.taihuoniao.fineix.utils.GlideUtils;
import com.taihuoniao.fineix.utils.Util;
import com.taihuoniao.fineix.view.roundImageView.RoundedImageView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author lilin
 * created at 2016/5/4 19:24
 */
public class SystemNoticeAdapter extends CommonBaseAdapter<SystemNoticeData.SystemNoticeItem>{
    public SystemNoticeAdapter(List list, Activity activity){
        super(list,activity);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final SystemNoticeData.SystemNoticeItem item = list.get(position);
        ViewHolder holder;
        if (convertView==null){
            convertView = Util.inflateView(R.layout.item_system_notice, null);
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder=(ViewHolder)convertView.getTag();
        }

        holder.title.setText(item.title);
        holder.tv_desc.setText(item.content);
        holder.tv_time.setText(item.created_at);
        if (item.is_unread){//未读
            holder.dot.setVisibility(View.VISIBLE);
        }else {
            holder.dot.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(item.cover_url)){
            holder.riv.setVisibility(View.VISIBLE);
            GlideUtils.displayImage(item.cover_url,holder.riv);
        }else {
            holder.riv.setVisibility(View.GONE);
        }
        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.title)
        TextView title;
        @Bind(R.id.tv_desc)
        TextView tv_desc;
        @Bind(R.id.dot)
        TextView dot;
        @Bind(R.id.tv_time)
        TextView tv_time;
        @Bind(R.id.riv)
        RoundedImageView riv;
        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
