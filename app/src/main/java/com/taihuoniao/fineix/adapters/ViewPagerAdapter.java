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
import com.taihuoniao.fineix.beans.Banner;
import com.taihuoniao.fineix.main.MainActivity;
import com.taihuoniao.fineix.product.GoodsDetailActivity;
import com.taihuoniao.fineix.qingjingOrSceneDetails.QingjingDetailActivity;
import com.taihuoniao.fineix.qingjingOrSceneDetails.SceneDetailActivity;
import com.taihuoniao.fineix.user.UserGuideActivity;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.view.WaittingDialog;

import java.util.List;

public class ViewPagerAdapter<T> extends RecyclingPagerAdapter {
    private final String TAG = getClass().getSimpleName();
    private Activity activity;
    private List<T> list;
    protected DisplayImageOptions options;
    private int size;
    private boolean isInfiniteLoop;
    private WaittingDialog svProgressHUD;

    public int getSize() {
        return size;
    }

    public ViewPagerAdapter(Activity activity, List<T> list) {
        this.activity = activity;
        this.list = list;
        this.size = list.size();
        this.svProgressHUD = new WaittingDialog(activity);
        isInfiniteLoop = false;
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.default_background_750_1334)
                .showImageForEmptyUri(R.mipmap.default_background_750_1334)
                .showImageOnFail(R.mipmap.default_background_750_1334)
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
            view = holder.imageView = new ImageView(activity);
            holder.imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        final T content = list.get(getPosition(position));

        if (content instanceof Banner){
            ImageLoader.getInstance().displayImage(((Banner) content).cover_url, holder.imageView,options);
        }

        if (content instanceof Integer) {
            holder.imageView.setImageResource((Integer) content);
//            ImageLoader.getInstance().displayImage("drawable://"+(Integer) content,holder.imageView,options);
        }

        if (content instanceof String) {
            if (TextUtils.isEmpty((String) content)) {
                ToastUtils.showError("图片链接为空");
            } else {
                ImageLoader.getInstance().displayImage((String) content, holder.imageView,options);
            }
        }

        if (activity instanceof UserGuideActivity) {
            if (position == size - 1) {
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (TextUtils.isEmpty(UserGuideActivity.fromPage)) {
                            activity.startActivity(new Intent(activity, MainActivity.class));
                        } else {
                            UserGuideActivity.fromPage = null;
                        }
                        activity.finish();
                    }
                });
            }
        }

        if (activity instanceof MainActivity){
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Banner banner=(Banner)content;
                    Intent intent;
                    switch (banner.type){
                        case 8:     //场景详情
                            intent=new Intent(activity, SceneDetailActivity.class);
                            intent.putExtra("id", banner.web_url);
                            activity.startActivity(intent);
                            break;
                        case 9:     //产品
                            intent = new Intent(activity,GoodsDetailActivity.class);
                            intent.putExtra("id",banner.web_url);
                            activity.startActivity(intent);
                            break;
                        case 10:    //情景
                            intent = new Intent(activity, QingjingDetailActivity.class);
                            intent.putExtra("id",banner.web_url);
                            activity.startActivity(intent);
                            break;
                    }

                }
            });
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