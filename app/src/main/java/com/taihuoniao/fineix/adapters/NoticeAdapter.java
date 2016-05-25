package com.taihuoniao.fineix.adapters;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.beans.NoticeData;
import com.taihuoniao.fineix.user.FocusActivity;
import com.taihuoniao.fineix.user.UserCenterActivity;
import com.taihuoniao.fineix.utils.Util;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author lilin
 * created at 2016/5/4 19:24
 */
public class NoticeAdapter extends CommonBaseAdapter<NoticeData.NoticeItem>{
    private ImageLoader imageLoader;
    public NoticeAdapter(List list, Activity activity){
        super(list,activity);
        this.imageLoader=ImageLoader.getInstance();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final NoticeData.NoticeItem item = list.get(position);
        ViewHolder holder=null;
        if (convertView==null){
            convertView= Util.inflateView(activity, R.layout.item_user_comments,null);
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder=(ViewHolder)convertView.getTag();
        }

        imageLoader.displayImage(item.s_user.medium_avatar_url,holder.riv,options);
        holder.tv_name.setText(item.s_user.nickname);
        if (item.is_read==0){
            holder.dot.setVisibility(View.VISIBLE);
        }else {
            holder.dot.setVisibility(View.GONE);
        }
        holder.tv_desc.setText(String.format("%s%s",item.info,item.kind_str));
        holder.tv_time.setText(item.created_at);
        holder.iv.setVisibility(View.VISIBLE);
        imageLoader.displayImage(item.target_cover_url,holder.iv,options);
        holder.riv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(activity, UserCenterActivity.class);
                intent.putExtra(FocusActivity.USER_ID_EXTRA,item.s_user._id);
                activity.startActivity(intent);
            }
        });
        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.riv)
        ImageView riv;
        @Bind(R.id.tv_name)
        TextView tv_name;
        @Bind(R.id.tv_desc)
        TextView tv_desc;
        @Bind(R.id.tv_time)
        TextView tv_time;
        @Bind(R.id.iv)
        ImageView iv;
        @Bind(R.id.dot)
        TextView dot;
        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
