package com.taihuoniao.fineix.adapters;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Gallery;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.beans.ProductBean;
import com.taihuoniao.fineix.utils.Util;
import com.taihuoniao.fineix.view.SlidingFocusImageView;

import java.util.List;

/**
 * @author lilin
 *         created at 2016/4/22 10:34
 */
public class SlidingFocusAdapter<T> extends CommonBaseAdapter<T> {
    private SlidingFocusImageView sfiv;
    private DisplayImageOptions options;
    private List<ProductBean.Sight> sights;

    public SlidingFocusAdapter(SlidingFocusImageView sfiv, List<ProductBean.Sight> sights, List<T> list, Activity activity) {
        super(list, activity);
        this.sights = sights;
        this.sfiv = sfiv;
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.default_background_750_422)
                .showImageForEmptyUri(R.mipmap.default_background_750_422)
                .showImageOnFail(R.mipmap.default_background_750_422)
                .cacheInMemory(true)
                .cacheOnDisk(true).considerExifParams(true)
                .build();
    }

    public int getCount() {
        return list.size() > 0 ? Integer.MAX_VALUE : 0;
//             return Integer.MAX_VALUE;

    }

    public Object getItem(int position) {
        return list.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        T item = null;
        if (sights != null && sights.size() > 0 && sights.get(0) != null) {
            if (position % (1 + list.size()) == list.size()) {
                ImageView imageView = new ImageView(parent.getContext());
                imageView.setLayoutParams(new Gallery.LayoutParams(activity.getResources().getDimensionPixelSize(R.dimen.dp85), activity.getResources().getDimensionPixelSize(R.dimen.dp150)));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                ImageLoader.getInstance().displayImage(sights.get(0).getCover_url(), imageView, options);
                return imageView;
            }
            item = list.get(position % (1 + list.size()));
        } else {
            item = list.get(position % list.size());
        }
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = View.inflate(parent.getContext(), R.layout.view_sliding_focus, null);
            holder = new ViewHolder();
            convertView.setTag(holder);
            holder.left = (ImageView) convertView.findViewById(R.id.view_sliding_focus_left);
            holder.iv = (ImageView) convertView.findViewById(R.id.view_sliding_focus_img);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (item instanceof String) {
            ImageLoader.getInstance().displayImage((String) item, holder.iv, options);
        }
        if (item instanceof Integer) {
            ImageLoader.getInstance().displayImage("drawable://" + (Integer) item, holder.iv, options);
        }
        if (position % 2 == 0) {
            holder.left.setVisibility(View.GONE);
        } else {
            holder.left.setVisibility(View.VISIBLE);
        }
//        测试双数的话显示分割线
//        LogUtil.e(TAG,"getSelectedItemPosition=====>>"+sfiv.getSelectedItemPosition());
//        LogUtil.e(TAG,"Position=====>>"+position);
        convertView.setLayoutParams(new Gallery.LayoutParams(Util.getScreenWidth() - 200, activity.getResources().getDimensionPixelSize(R.dimen.dp150)));
        return convertView;
    }

    static class ViewHolder {
        ImageView left;
        ImageView iv;
    }
}
