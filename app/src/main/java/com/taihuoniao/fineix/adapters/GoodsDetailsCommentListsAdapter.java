package com.taihuoniao.fineix.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.beans.TryCommentsBean;

import java.util.List;

/**
 * Created by taihuoniao on 2016/2/23.
 */
public class GoodsDetailsCommentListsAdapter extends BaseAdapter {
    private Context context;
    private List<TryCommentsBean> commentsList;
    private ImageLoader imageLoader;
    private DisplayImageOptions optionsAvater = null;
    private boolean tag = false;//判断是商品详情界面还是商品评论界面的标识 false为商品详情界面 true为商品评论列表界面

    public GoodsDetailsCommentListsAdapter(Context context, List<TryCommentsBean> commentsList, boolean tag) {
        this.context = context;
        this.commentsList = commentsList;
        this.tag = tag;
        optionsAvater = new DisplayImageOptions.Builder()
//                .showImageOnLoading(R.mipmap.default_backround)
//                .showImageForEmptyUri(R.mipmap.default_backround)
//                .showImageOnFail(R.mipmap.default_backround)
                .cacheInMemory(true)
                .cacheOnDisk(true).considerExifParams(true)
                .displayer(new RoundedBitmapDisplayer(360)).build();
        imageLoader = ImageLoader.getInstance();
    }

    @Override
    public int getCount() {
        if (tag) {
            return commentsList.size();
        } else {
            return commentsList.size() > 3 ? 3 : commentsList.size();
        }

    }

    @Override
    public Object getItem(int position) {
        return commentsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.item_goodsdetails_commentslists, null);
            holder.img = (ImageView) convertView.findViewById(R.id.item_goodsdetails_commentslists_img);
            holder.nameTv = (TextView) convertView.findViewById(R.id.item_goodsdetails_commentslists_name);
            holder.timeTv = (TextView) convertView.findViewById(R.id.item_goodsdetails_commentslists_time);
            holder.ratingBar = (RatingBar) convertView.findViewById(R.id.item_goodsdetails_commentslists_ratingbar);
            holder.commentTv = (TextView) convertView.findViewById(R.id.item_goodsdetails_commentslists_comment);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        imageLoader.displayImage(commentsList.get(position).getUser().getSmall_avatar_url(), holder.img, optionsAvater);
        holder.nameTv.setText(commentsList.get(position).getUser().getNickname());
        holder.timeTv.setText(commentsList.get(position).getCreated_at());
        switch (commentsList.get(position).getStar()) {
            case "0":
                holder.ratingBar.setRating(0);
                break;
            case "1":
                holder.ratingBar.setRating(1);
                break;
            case "2":
                holder.ratingBar.setRating(2);
                break;
            case "3":
                holder.ratingBar.setRating(3);
                break;
            case "4":
                holder.ratingBar.setRating(4);
                break;
            case "5":
                holder.ratingBar.setRating(5);
                break;
        }
        holder.commentTv.setText(commentsList.get(position).getContent());
        return convertView;
    }

    class ViewHolder {
        private ImageView img;
        private TextView nameTv;
        private TextView timeTv;
        private RatingBar ratingBar;
        private TextView commentTv;
    }
}
