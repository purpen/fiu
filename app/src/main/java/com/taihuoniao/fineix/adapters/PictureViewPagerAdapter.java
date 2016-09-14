package com.taihuoniao.fineix.adapters;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.view.ImageCrop.ClipZoomImageView;

import java.util.List;

/**
 * Created by taihuoniao on 2016/9/14.
 */
public class PictureViewPagerAdapter extends PagerAdapter {
    private List<String> picList;
    private Activity activity;

    public PictureViewPagerAdapter(Activity activity, List<String> picList) {
        this.activity = activity;
        this.picList = picList;
    }

    @Override
    public int getCount() {
        Log.e("<<<", "getCount=" + picList.size());
        return picList == null ? 0 : picList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = View.inflate(activity, R.layout.item_pic_view_pager, null);
        ClipZoomImageView clipSquareImageView = (ClipZoomImageView) view.findViewById(R.id.clip_img);
        ImageLoader.getInstance().displayImage(picList.get(position), clipSquareImageView);
        view.setTag(position);
        container.addView(view);
        return view;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        for (int i = 0; i < container.getChildCount(); i++) {
            View view = container.getChildAt(i);
            if (view.getTag() != null && (Integer) (view.getTag()) == position) {
                container.removeView(view);
            }
        }
    }
}
