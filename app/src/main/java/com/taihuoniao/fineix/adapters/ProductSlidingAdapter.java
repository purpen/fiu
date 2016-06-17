package com.taihuoniao.fineix.adapters;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.beans.ProductBean;
import com.taihuoniao.fineix.beans.SearchBean;
import com.taihuoniao.fineix.utils.Util;

/**
 * Created by taihuoniao on 2016/6/17.
 */
public class ProductSlidingAdapter extends BaseAdapter {
    private Activity activity;
    private ProductBean.ProductListItem productListItem;
    private SearchBean.SearchItem searchItem;
    private DisplayImageOptions options;

    public ProductSlidingAdapter(Activity activity, ProductBean.ProductListItem productListItem, SearchBean.SearchItem searchItem) {
        this.activity = activity;
        this.productListItem = productListItem;
        this.searchItem = searchItem;
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.default_background_750_1334)
                .showImageForEmptyUri(R.mipmap.default_background_750_1334)
                .showImageOnFail(R.mipmap.default_background_750_1334)
                .cacheInMemory(true)
                .cacheOnDisk(true).considerExifParams(true)
                .build();
    }

    @Override
    public int getCount() {
        if (productListItem != null) {
            if (productListItem.getBanner_asset() != null && productListItem.getBanner_asset().size() > 0) {
                return Integer.MAX_VALUE;
            }
        } else if (searchItem != null) {
            if (searchItem.getBanners() != null && searchItem.getBanners().size() > 0) {
                return Integer.MAX_VALUE;
            }
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (productListItem != null) {
            if (productListItem.getBanner_asset() != null && productListItem.getBanner_asset().size() > 0) {
                return productListItem.getBanner_asset().get(position);
            }
        } else if (searchItem != null) {
            if (searchItem.getBanners() != null && searchItem.getBanners().size() > 0) {
                return searchItem.getBanners().get(position);
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
        ViewHolder holder = null;
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
        if (productListItem != null) {
            if (position == productListItem.pos) {
                holder.zhezhaoTv.setVisibility(View.GONE);
            } else {
                holder.zhezhaoTv.setVisibility(View.VISIBLE);
            }
            if (productListItem.getSights() != null && productListItem.getSights().size() > 0 && productListItem.getSights().get(0) != null) {
                if (position % (1 + productListItem.getBanner_asset().size()) == productListItem.getBanner_asset().size()) {
                    convertView.setLayoutParams(new Gallery.LayoutParams(activity.getResources().getDimensionPixelSize(R.dimen.dp90), activity.getResources().getDimensionPixelSize(R.dimen.dp150)));
                    ImageLoader.getInstance().displayImage(productListItem.getSights().get(0).getCover_url(), holder.iv);
                    holder.left.setVisibility(View.GONE);
                    return convertView;
                }
                ImageLoader.getInstance().displayImage(productListItem.getBanner_asset().get(position % (1 + productListItem.getBanner_asset().size())), holder.iv);
                if (position % 2 == 0) {
                    holder.left.setVisibility(View.GONE);
                } else {
                    holder.left.setVisibility(View.VISIBLE);
                }
                convertView.setLayoutParams(new Gallery.LayoutParams(Util.getScreenWidth() - 200, activity.getResources().getDimensionPixelSize(R.dimen.dp150)));
                return convertView;
            }
            ImageLoader.getInstance().displayImage(productListItem.getBanner_asset().get(position % (productListItem.getBanner_asset().size())), holder.iv);
            if (position % 2 == 0) {
                holder.left.setVisibility(View.GONE);
            } else {
                holder.left.setVisibility(View.VISIBLE);
            }
            convertView.setLayoutParams(new Gallery.LayoutParams(Util.getScreenWidth() - 200, activity.getResources().getDimensionPixelSize(R.dimen.dp150)));
            return convertView;
        } else if (searchItem != null) {
            if (position == searchItem.pos) {
                holder.zhezhaoTv.setVisibility(View.GONE);
            } else {
                holder.zhezhaoTv.setVisibility(View.VISIBLE);
            }
            ImageLoader.getInstance().displayImage(searchItem.getBanners().get(position % (searchItem.getBanners().size())), holder.iv);
            if (position % 2 == 0) {
                holder.left.setVisibility(View.GONE);
            } else {
                holder.left.setVisibility(View.VISIBLE);
            }
            convertView.setLayoutParams(new Gallery.LayoutParams(Util.getScreenWidth() - 200, activity.getResources().getDimensionPixelSize(R.dimen.dp150)));
            return convertView;
        }
        return null;
//        if (sights != null && sights.size() > 0 && sights.get(0) != null) {
//            if (position % (1 + list.size()) == list.size()) {
//                ImageView imageView = new ImageView(parent.getContext());
//                imageView.setLayoutParams(new Gallery.LayoutParams(activity.getResources().getDimensionPixelSize(R.dimen.dp85), activity.getResources().getDimensionPixelSize(R.dimen.dp150)));
//                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//                ImageLoader.getInstance().displayImage(sights.get(0).getCover_url(), imageView, options);
//                return imageView;
//            }
//            item = list.get(position % (1 + list.size()));
//        } else {
//            item = list.get(position % list.size());
//        }
//
//
//        if (item instanceof String) {
//            ImageLoader.getInstance().displayImage((String) item, holder.iv, options);
//        }
//        if (item instanceof Integer) {
//            ImageLoader.getInstance().displayImage("drawable://" + (Integer) item, holder.iv, options);
//        }
//        if (position % 2 == 0) {
//            holder.left.setVisibility(View.GONE);
//        } else {
//            holder.left.setVisibility(View.VISIBLE);
//        }
//        convertView.setLayoutParams(new Gallery.LayoutParams(Util.getScreenWidth() - 200, activity.getResources().getDimensionPixelSize(R.dimen.dp150)));
//        return convertView;
    }

    static class ViewHolder {
        ImageView left;
        ImageView iv;
        TextView zhezhaoTv;
    }
}
