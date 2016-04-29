package com.taihuoniao.fineix.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.beans.CommonBean;

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
//                .showImageOnLoading(R.mipmap.default_backround)
//                .showImageForEmptyUri(R.mipmap.default_backround)
//                .showImageOnFail(R.mipmap.default_backround)
                .cacheInMemory(true)
                .cacheOnDisk(true).considerExifParams(true)
                .displayer(new RoundedBitmapDisplayer(360)).build();
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
            holder.headImg = (ImageView) convertView.findViewById(R.id.item_user_head_img);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ImageLoader.getInstance().displayImage(list.get(position).getUser().getAvatar_url(), holder.headImg, options);
        return convertView;
    }

    static class ViewHolder {
        ImageView headImg;
    }
}
