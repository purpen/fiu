package com.taihuoniao.fineix.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.beans.BrandListBean;
import com.taihuoniao.fineix.view.roundImageView.RoundedImageView;

/**
 * Created by taihuoniao on 2016/6/24.
 */
public class FiuBrandsAdapter extends BaseAdapter {
    private Context context;
    private BrandListBean brandListBean;

    public FiuBrandsAdapter(Context context, BrandListBean brandListBean) {
        this.context = context;
        this.brandListBean = brandListBean;
    }

    @Override
    public int getCount() {
        return brandListBean.getData().getRows() == null ? 0 : brandListBean.getData().getRows().size();
    }

    @Override
    public Object getItem(int position) {
        return brandListBean.getData().getRows().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_apple_view, null);
            holder = new ViewHolder();
            holder.img = (RoundedImageView) convertView.findViewById(R.id.item_round);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
//            FileManagerImageLoader.getInstance().addTask(getItem(position).getMedium_avatar_url(), itemRound, null, 48, 48, false);
        ImageLoader.getInstance().displayImage(brandListBean.getData().getRows().get(position).getCover_url(), holder.img);
        return convertView;
    }

    static class ViewHolder {
        RoundedImageView img;
    }
}
