package com.taihuoniao.fineix.adapters;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;



import java.util.List;

public class ViewPagerAdapter<T> extends RecyclingPagerAdapter {
    private final String TAG=getClass().getSimpleName();
    private Activity activity;
    private List<T> imageList;

    private int size;
    private boolean isInfiniteLoop;

    public ViewPagerAdapter(Activity activity, List<T> imageList) {
        this.activity = activity;
        this.imageList = imageList;
        this.size = imageList.size();
        isInfiniteLoop = false;
    }

    @Override
    public int getCount() {
        // Infinite loop
        return isInfiniteLoop ? Integer.MAX_VALUE : imageList.size();
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
            holder.imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        final T content = imageList.get(getPosition(position));


        if (content instanceof Integer){
            holder.imageView.setImageResource((Integer) content);
        }
//        if (content instanceof Goods) {
////            Imageloader.loadImg(activity, Util.replaceChinese2UTF8(String.format("%s%s", Constants.PIC_URI,((Goods) content).goods_img)), holder.imageView, R.mipmap.placeholder180);
//        } else if(content instanceof Integer){
//            holder.imageView.setImageResource((Integer) content);
//            LogUtil.e("content",content+"");
//        }else if(content instanceof String){
////            Imageloader.loadImg(activity, Util.replaceChinese2UTF8(String.format("%s%s", Constants.PIC_URI,content.toString())), holder.imageView, R.mipmap.placeholder180);
//        }else {
//            new IllegalArgumentException("please put right image resources into ViewPagerAdapter!");
//        }

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