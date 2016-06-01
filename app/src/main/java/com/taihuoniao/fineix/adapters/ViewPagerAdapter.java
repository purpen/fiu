package com.taihuoniao.fineix.adapters;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.main.MainActivity;
import com.taihuoniao.fineix.user.UserGuideActivity;
import com.taihuoniao.fineix.utils.Util;
import com.taihuoniao.fineix.view.svprogress.SVProgressHUD;

import java.util.List;

public class ViewPagerAdapter<T> extends RecyclingPagerAdapter {
    private final String TAG=getClass().getSimpleName();
    private Activity activity;
    private List<T> imageList;
    protected DisplayImageOptions options;
    private int size;
    private boolean isInfiniteLoop;
    private SVProgressHUD svProgressHUD;
    public int getSize(){
        return size;
    }
    public ViewPagerAdapter(Activity activity, List<T> imageList) {
        this.activity = activity;
        this.imageList = imageList;
        this.size = imageList.size();
        this.svProgressHUD=new SVProgressHUD(activity);
        isInfiniteLoop = false;
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.ic_launcher)
                .showImageForEmptyUri(R.mipmap.ic_launcher)
                .showImageOnFail(R.mipmap.ic_launcher)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
    }

    @Override
    public int getCount() {
        // Infinite loop
        return isInfiniteLoop ? Integer.MAX_VALUE : size;
    }

    /**
     * get really position
     *
     * @param position
     * @return
     */
    private int getPosition(int position) {
        return isInfiniteLoop ? position % size : position;
    }

    @Override
    public View getView(int position, View view, ViewGroup container) {
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = holder.imageView =new ImageView(activity);
            holder.imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        final T content = imageList.get(getPosition(position));

        if (content instanceof Integer){
            holder.imageView.setImageResource((Integer) content);
//            ImageLoader.getInstance().displayImage("drawable://"+(Integer) content,holder.imageView,options);
        }

        if (content instanceof String){
            if (TextUtils.isEmpty((String)content)){
                svProgressHUD.showErrorWithStatus("图片链接为空");
            }else {
                ImageLoader.getInstance().displayImage((String) content,holder.imageView);
            }
        }

        if (activity instanceof UserGuideActivity){
            if (position==size-1){
                if (activity instanceof UserGuideActivity && TextUtils.isEmpty(UserGuideActivity.fromPage)){
                    if (position==size-1){
                        view.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                activity.startActivity(new Intent(activity,MainActivity.class));
                                activity.finish();
                            }
                        });
                    }
                }
            }
        }
        return view;
    }

    private static class ViewHolder {
        ImageView imageView;
    }

    /**
     * @return the isInfiniteLoop
     */
    public boolean isInfiniteLoop() {
        return isInfiniteLoop;
    }

    /**
     * @param isInfiniteLoop the isInfiniteLoop to set
     */
    public ViewPagerAdapter setInfiniteLoop(boolean isInfiniteLoop) {
        this.isInfiniteLoop = isInfiniteLoop;
        return this;
    }
}