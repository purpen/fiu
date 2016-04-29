package com.taihuoniao.fineix.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.beans.CommentsBean;

import java.util.List;

/**
 * Created by taihuoniao on 2016/4/21.
 */
public class CommentsListAdapter extends BaseAdapter {
    private Context context;
    private List<CommentsBean.CommentItem> list;
    private DisplayImageOptions options;

    public CommentsListAdapter(Context context, List<CommentsBean.CommentItem> list) {
        this.context = context;
        this.list = list;
        options = new DisplayImageOptions.Builder()
//                .showImageOnLoading(R.mipmap.default_backround)
//                .showImageForEmptyUri(R.mipmap.default_backround)
//                .showImageOnFail(R.mipmap.default_backround)
                .cacheInMemory(true)
                .cacheOnDisk(true).considerExifParams(true)
                .displayer(new RoundedBitmapDisplayer(360)).build();
    }

    @Override
    public int getCount() {
        return list.size();
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
            convertView = View.inflate(context, R.layout.item_comment, null);
            holder = new ViewHolder();
            holder.headImg = (ImageView) convertView.findViewById(R.id.item_comment_head);
            holder.name = (TextView) convertView.findViewById(R.id.item_comment_name);
            holder.huifu = (TextView) convertView.findViewById(R.id.item_comment_huifu);
            holder.whoName = (TextView) convertView.findViewById(R.id.item_comment_who_name);
            holder.content = (TextView) convertView.findViewById(R.id.item_comment_content);
            holder.time = (TextView) convertView.findViewById(R.id.item_comment_time);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ImageLoader.getInstance().displayImage(list.get(position).getUser().getSmall_avatar_url(), holder.headImg, options);
        holder.name.setText(list.get(position).getUser().getNickname());
        holder.content.setText(list.get(position).getContent());
        holder.time.setText(list.get(position).getCreated_at());
        if (list.get(position).getReply_user_nickname() != null) {
            holder.whoName.setText(list.get(position).getReply_user_nickname());
            holder.whoName.setVisibility(View.VISIBLE);
            holder.huifu.setVisibility(View.VISIBLE);
        } else {
            holder.whoName.setVisibility(View.GONE);
            holder.huifu.setVisibility(View.GONE);
        }

        return convertView;
    }

    static class ViewHolder {
        ImageView headImg;
        TextView name;
        TextView huifu;
        TextView whoName;
        TextView content;
        TextView time;
    }
}
