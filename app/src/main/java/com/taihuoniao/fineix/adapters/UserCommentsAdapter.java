package com.taihuoniao.fineix.adapters;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.beans.CommentsBean;
import com.taihuoniao.fineix.user.FocusActivity;
import com.taihuoniao.fineix.user.UserCenterActivity;
import com.taihuoniao.fineix.utils.GlideUtils;
import com.taihuoniao.fineix.utils.Util;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author lilin
 * created at 2016/5/4 19:24
 */
public class UserCommentsAdapter extends CommonBaseAdapter<CommentsBean.CommentItem>{
    public UserCommentsAdapter(List list, Activity activity){
        super(list,activity);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final CommentsBean.CommentItem item = list.get(position);
        ViewHolder holder;
        if (convertView==null){
            convertView = Util.inflateView(R.layout.item_user_comments, null);
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder=(ViewHolder)convertView.getTag();
        }

        GlideUtils.displayImage(item.getUser().getSmall_avatar_url(),holder.riv);
        holder.tv_name.setText(item.getUser().getNickname());
        holder.tv_desc.setText(item.getContent());
        holder.tv_time.setText(item.getCreated_at());
        holder.iv.setVisibility(View.VISIBLE);
        if (item.is_unread){
            holder.dot.setVisibility(View.VISIBLE);
        }else {
            holder.dot.setVisibility(View.GONE);
        }
        GlideUtils.displayImage(item.target_small_cover_url,holder.iv);
        holder.riv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(activity, UserCenterActivity.class);
                intent.putExtra(FocusActivity.USER_ID_EXTRA,item.user_id);
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
