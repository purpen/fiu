package com.taihuoniao.fineix.adapters;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.beans.PhotoItem;
import com.taihuoniao.fineix.main.MainApplication;
import com.taihuoniao.fineix.utils.DensityUtils;

import java.util.List;

/**
 * Created by taihuoniao on 2016/3/14.
 */
public class AlbumGridAdapter extends BaseAdapter {
    private Context context;
    private List<PhotoItem> photoList;
    private DisplayImageOptions options;
//    private BitmapUtils bitmapUtils;

    public AlbumGridAdapter(Context context, List<PhotoItem> photoList) {
        this.context = context;
        this.photoList = photoList;
//        bitmapUtils = new BitmapUtils(context);
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.default_background_750_1334)
                .showImageForEmptyUri(R.mipmap.default_background_750_1334)
                .showImageOnFail(R.mipmap.default_background_750_1334)
                .cacheOnDisk(true)
                .cacheInMemory(true)
                .considerExifParams(true)
                .build();
    }

    @Override
    public int getCount() {
        return photoList.size();
    }

    @Override
    public Object getItem(int position) {
        return photoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder hold;
        int width = (MainApplication.getContext().getScreenWidth() - DensityUtils.dp2px(context, 6)) / 4;
        int height = width;
        if (convertView == null) {
            hold = new ViewHolder();
            convertView = View.inflate(context, R.layout.item_albumgrid, null);
            hold.img = (ImageView) convertView.findViewById(R.id.item_albumgrid_img);
            hold.tv = (TextView) convertView.findViewById(R.id.item_albumgrid_tv);
            hold.img.setLayoutParams(new FrameLayout.LayoutParams(width, height));
            hold.tv.setLayoutParams(new FrameLayout.LayoutParams(width, height));
            convertView.setTag(hold);
        } else {
            hold = (ViewHolder) convertView.getTag();
        }
        Log.e("<<<加载图片的路径",photoList.get(position).getImageUri());
        ImageLoader.getInstance().displayImage("file://" + photoList.get(position).getImageUri(), hold.img, options);
        if (photoList.get(position).isChecked()) {
            hold.tv.setBackgroundResource(R.drawable.yellow_album_ground);
        } else {
            hold.tv.setBackgroundResource(R.color.nothing);
        }
        return convertView;
    }

    public void selectImg(int position) {
        for (int i = 0; i < photoList.size(); i++) {
            if (i == position) {
                photoList.get(position).setIsChecked(true);
            } else {
                photoList.get(i).setIsChecked(false);
            }
        }
        notifyDataSetChanged();
    }

    static class ViewHolder {
        private ImageView img;
        private TextView tv;
    }

}
