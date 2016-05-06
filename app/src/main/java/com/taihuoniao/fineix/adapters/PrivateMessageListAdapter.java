package com.taihuoniao.fineix.adapters;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.beans.CommentsBean;
import com.taihuoniao.fineix.utils.Util;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author lilin
 * created at 2016/5/6 19:38
 */
public class PrivateMessageListAdapter extends CommonBaseAdapter{
    private ImageLoader imageLoader;
    public PrivateMessageListAdapter(List list, Activity activity){
        super(list,activity);
        this.imageLoader=ImageLoader.getInstance();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//        final CommentsBean.CommentItem item = list.get(position);
//        ViewHolder holder=null;
//        if (convertView==null){
//            convertView= Util.inflateView(activity, R.layout.item_user_comments,null);
//            holder=new ViewHolder(convertView);
//            convertView.setTag(holder);
//        }else {
//            holder=(ViewHolder)convertView.getTag();
//        }
//
//        imageLoader.displayImage(item.getUser().getSmall_avatar_url(),holder.riv,options);
//        holder.tv_name.setText(item.getUser().getNickname());
//        holder.tv_desc.setText(item.getContent());
//        holder.tv_time.setText(item.getCreated_at());
//         imageLoader.displayImage(item.follows.avatar_url,holder.riv,options);
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
        @Bind(R.id.btn)
        Button btn;
        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
