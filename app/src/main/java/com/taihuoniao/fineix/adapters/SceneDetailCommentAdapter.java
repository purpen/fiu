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
public class SceneDetailCommentAdapter extends BaseAdapter {
    private Context context;
    private List<CommentsBean.CommentItem> list;
    private DisplayImageOptions options;

    public SceneDetailCommentAdapter(Context context, List<CommentsBean.CommentItem> list) {
        this.context = context;
        this.list = list;
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.default_background_750_1334)
                .showImageForEmptyUri(R.mipmap.default_background_750_1334)
                .showImageOnFail(R.mipmap.default_background_750_1334)
                .cacheInMemory(true)
                .cacheOnDisk(true).considerExifParams(true)
                .displayer(new RoundedBitmapDisplayer(360)).build();
    }

    @Override
    public int getCount() {
        return list.size() > 3 ? 3 : list.size();
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
            convertView = View.inflate(context, R.layout.item_scenedetails_comments, null);
            holder = new ViewHolder();
            holder.headImg = (ImageView) convertView.findViewById(R.id.item_scenedetails_comments_head);
            holder.content = (TextView) convertView.findViewById(R.id.item_scenedetails_comments_content);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ImageLoader.getInstance().displayImage(list.get(position).getUser().getSmall_avatar_url(), holder.headImg, options);
        holder.content.setText(list.get(position).getContent());
        return convertView;
    }

    static class ViewHolder {
        ImageView headImg;
        TextView content;
    }
}
