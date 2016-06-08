package com.taihuoniao.fineix.adapters;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.beans.CommonBean;
import com.taihuoniao.fineix.view.roundImageView.RoundedImageView;

import java.util.List;

/**
 * Created by taihuoniao on 2016/4/21.
 */
public class SceneDetailUserHeadAdapter extends BaseAdapter {
    private Context context;
    private List<CommonBean.CommonItem> list;
    private DisplayImageOptions options;

    public SceneDetailUserHeadAdapter(Context context, List<CommonBean.CommonItem> list) {
        this.context = context;
        this.list = list;
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
        return list.size() > 14 ? 14 : list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_user_head, null);
            holder = new ViewHolder();
            holder.headImg = (RoundedImageView) convertView.findViewById(R.id.item_user_head_img);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        try {
            ImageLoader.getInstance().displayImage(list.get(position).getUser().getAvatar_url(), holder.headImg, options);
        } catch (Exception e) {
            Log.e("<<<>>>>>>", e.toString());
        }
        return convertView;
    }

    static class ViewHolder {
        RoundedImageView headImg;
    }
}
