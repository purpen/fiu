package com.taihuoniao.fineix.adapters;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.beans.CommentsBean;
import com.taihuoniao.fineix.beans.SystemNoticeData;
import com.taihuoniao.fineix.utils.Util;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author lilin
 * created at 2016/5/4 19:24
 */
public class SystemNoticeAdapter extends CommonBaseAdapter<SystemNoticeData.SystemNoticeItem>{
    private ImageLoader imageLoader;
    public SystemNoticeAdapter(List list, Activity activity){
        super(list,activity);
        this.imageLoader=ImageLoader.getInstance();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final SystemNoticeData.SystemNoticeItem item = list.get(position);
        ViewHolder holder=null;
        if (convertView==null){
            convertView= Util.inflateView(activity, R.layout.item_system_notice,null);
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder=(ViewHolder)convertView.getTag();
        }

        holder.tv_name.setText(item.title);
        holder.tv_desc.setText(item.content);
        holder.tv_time.setText(item.created_at);
        if (!TextUtils.isEmpty(item.url)){
            holder.iv.setVisibility(View.VISIBLE);
            imageLoader.displayImage(item.url,holder.iv,options);
        }else {
            holder.iv.setVisibility(View.GONE);
        }
        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.tv_name)
        TextView tv_name;
        @Bind(R.id.tv_desc)
        TextView tv_desc;
        @Bind(R.id.tv_time)
        TextView tv_time;
        @Bind(R.id.iv)
        ImageView iv;
        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
