package com.taihuoniao.fineix.adapters;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.beans.CollectionItem;
import com.taihuoniao.fineix.utils.Util;

public class CollectSlidingAdapter extends BaseAdapter {
    private Activity activity;
    private CollectionItem item;

    public CollectSlidingAdapter(Activity activity, CollectionItem item) {
        this.activity = activity;
        this.item = item;
    }

    @Override
    public int getCount() {
        if (item != null) {
            if (item.banner_asset != null && item.banner_asset.size() > 0) {
                return Integer.MAX_VALUE;
            }
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (item != null) {
            if (item.banner_asset != null && item.banner_asset.size() > 0) {
                return item.banner_asset.get(position);
            }
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(parent.getContext(), R.layout.view_sliding_focus, null);
            holder = new ViewHolder();
            convertView.setTag(holder);
            holder.left = (ImageView) convertView.findViewById(R.id.view_sliding_focus_left);
            holder.iv = (ImageView) convertView.findViewById(R.id.view_sliding_focus_img);
            holder.zhezhaoTv = (TextView) convertView.findViewById(R.id.view_sliding_focus_zhezhao);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (item != null) {
            if (position == item.pos) {
                holder.zhezhaoTv.setVisibility(View.GONE);
            } else {
                holder.zhezhaoTv.setVisibility(View.VISIBLE);
            }
            if (item.sights != null && item.sights.size() > 0 && item.sights.get(0) != null) {
                if (position % (1 + item.banner_asset.size()) == item.banner_asset.size()) {
                    convertView.setLayoutParams(new Gallery.LayoutParams(activity.getResources().getDimensionPixelSize(R.dimen.dp90), activity.getResources().getDimensionPixelSize(R.dimen.dp150)));
                    ImageLoader.getInstance().displayImage(item.sights.get(0).cover_url, holder.iv);
                    holder.left.setVisibility(View.GONE);
                    return convertView;
                }
                ImageLoader.getInstance().displayImage(item.banner_asset.get(position % (1 + item.banner_asset.size())), holder.iv);
                if (position % 2 == 0) {
                    holder.left.setVisibility(View.GONE);
                } else {
                    holder.left.setVisibility(View.VISIBLE);
                }
                convertView.setLayoutParams(new Gallery.LayoutParams(Util.getScreenWidth() - 200, activity.getResources().getDimensionPixelSize(R.dimen.dp150)));
                return convertView;
            }
            ImageLoader.getInstance().displayImage(item.banner_asset.get(position % (item.banner_asset.size())), holder.iv);
            if (position % 2 == 0) {
                holder.left.setVisibility(View.GONE);
            } else {
                holder.left.setVisibility(View.VISIBLE);
            }
            convertView.setLayoutParams(new Gallery.LayoutParams(Util.getScreenWidth() - 200, activity.getResources().getDimensionPixelSize(R.dimen.dp150)));
            return convertView;
        }
        return null;
    }

    static class ViewHolder {
        ImageView left;
        ImageView iv;
        TextView zhezhaoTv;
    }
}
