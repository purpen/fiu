package com.taihuoniao.fineix.adapters;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

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
        return Integer.MAX_VALUE;
    }

    @Override
    public Object getItem(int position) {
        return position;
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
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (position % (1 + item.scene_product.banner_asset.size()) == item.scene_product.banner_asset.size()) {
            convertView.setLayoutParams(new Gallery.LayoutParams(activity.getResources().getDimensionPixelSize(R.dimen.dp90), activity.getResources().getDimensionPixelSize(R.dimen.dp150)));
            ImageLoader.getInstance().displayImage(item.scene_product.cover_url, holder.iv);
            holder.left.setVisibility(View.GONE);
            return convertView;
        }
        ImageLoader.getInstance().displayImage(item.scene_product.banner_asset.get(position % (1 + item.scene_product.banner_asset.size())), holder.iv);
        switch (item.scene_product.banner_asset.size()) {
            case 2:
                if (position % 3 == 1) {
                    holder.left.setVisibility(View.VISIBLE);
                } else {
                    holder.left.setVisibility(View.GONE);
                }
                break;
            case 3:
                if (position % 4 == 1) {
                    holder.left.setVisibility(View.VISIBLE);
                } else {
                    holder.left.setVisibility(View.GONE);
                }
                break;
            case 4:
                if (position % 5 == 1 || position % 5 == 3) {
                    holder.left.setVisibility(View.VISIBLE);
                } else {
                    holder.left.setVisibility(View.GONE);
                }
                break;
            case 5:
                if (position % 6 == 1 || position % 6 == 3) {
                    holder.left.setVisibility(View.VISIBLE);
                } else {
                    holder.left.setVisibility(View.GONE);
                }
                break;
            default:
                holder.left.setVisibility(View.GONE);
                break;
        }
        convertView.setLayoutParams(new Gallery.LayoutParams(Util.getScreenWidth() - 200, activity.getResources().getDimensionPixelSize(R.dimen.dp150)));
//        if (item != null) {
//            ImageLoader.getInstance().displayImage(item.scene_product.banner_asset.get(position % (item.scene_product.banner_asset.size())), holder.iv);
//            if (position % 2 == 0) {
//                holder.left.setVisibility(View.GONE);
//            } else {
//                holder.left.setVisibility(View.VISIBLE);
//            }
//            convertView.setLayoutParams(new Gallery.LayoutParams(Util.getScreenWidth() - 200, activity.getResources().getDimensionPixelSize(R.dimen.dp150)));
//            return convertView;
//        }
        return convertView;
    }

    static class ViewHolder {
        ImageView left;
        ImageView iv;
    }
}
