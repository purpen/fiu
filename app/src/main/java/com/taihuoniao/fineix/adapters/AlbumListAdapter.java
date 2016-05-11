package com.taihuoniao.fineix.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.beans.AlbumBean;
import com.taihuoniao.fineix.utils.DensityUtils;

import java.util.List;
import java.util.Map;

/**
 * Created by taihuoniao on 2016/3/14.
 */
public class AlbumListAdapter extends BaseAdapter {
    private Context context;
    private List<String> albumPaths;
    private Map<String, AlbumBean> albumList;

    public AlbumListAdapter(Context context, List<String> albumPaths, Map<String, AlbumBean> albumList) {
        this.context = context;
        this.albumPaths = albumPaths;
        this.albumList = albumList;
    }

    @Override
    public int getCount() {
        return albumList.size();
    }

    @Override
    public Object getItem(int position) {
        return albumList.get(albumPaths.get(position)).getPhotos().get(0);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder hold;
        int width = DensityUtils.dp2px(context, 50);
        if (convertView == null) {
            hold = new ViewHolder();
            convertView = View.inflate(context, R.layout.item_albumlist, null);
            hold.img = (ImageView) convertView.findViewById(R.id.item_albumlist_img);
            hold.img.setLayoutParams(new RelativeLayout.LayoutParams(width, width));
            hold.name = (TextView) convertView.findViewById(R.id.item_albumlist_name);
            hold.number = (TextView) convertView.findViewById(R.id.item_albumlist_number);
            convertView.setTag(hold);
        } else {
            hold = (ViewHolder) convertView.getTag();
        }
        hold.name.setText(albumList.get(albumPaths.get(position)).getTitle());
        hold.number.setText(albumList.get(albumPaths.get(position)).getPhotos().size()+"");
        ImageLoader.getInstance().displayImage("file://" + albumList.get(albumPaths.get(position)).getPhotos().get(0).getImageUri(), new ImageViewAware(hold.img));
        return convertView;
    }

   static class ViewHolder {
        private ImageView img;
        private TextView name;
        private TextView number;
    }
}
