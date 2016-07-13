package com.taihuoniao.fineix.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.beans.BrandListBean;
import com.taihuoniao.fineix.main.MainApplication;
import com.taihuoniao.fineix.utils.DensityUtils;
import com.taihuoniao.fineix.view.roundImageView.RoundedImageView;

import java.util.List;

/**
 * Created by taihuoniao on 2016/7/12.
 */
public class MineBrandGridAdapter extends BaseAdapter {
    private List<BrandListBean.DataBean.RowsBean> brandList;
    private Context context;
    private boolean isFour;
    private GridView gridViewForScrollView;

    public MineBrandGridAdapter(Context context, List<BrandListBean.DataBean.RowsBean> brandList, boolean isFour, GridView gridViewForScrollView) {
        this.context = context;
        this.brandList = brandList;
        this.isFour = isFour;
        this.gridViewForScrollView = gridViewForScrollView;
    }

    @Override
    public int getCount() {
        if (isFour) {
            return brandList == null ? 0 : (brandList.size() > 4 ? 4 : brandList.size());
        }
        return brandList == null ? 0 : brandList.size();
    }

    @Override
    public BrandListBean.DataBean.RowsBean getItem(int position) {
        return brandList == null ? null : brandList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_mine_brand_grid, null);
            holder = new ViewHolder();
            holder.img = (RoundedImageView) convertView.findViewById(R.id.item_mine_brand_grid_img);
            ViewGroup.LayoutParams layoutParams = holder.img.getLayoutParams();
            layoutParams.width = (MainApplication.getContext().getScreenWidth() - DensityUtils.dp2px(context, 11) -
                    gridViewForScrollView.getPaddingRight() - gridViewForScrollView.getPaddingLeft()) / 2;
            layoutParams.height = layoutParams.width * 208 / 335;
            holder.img.setLayoutParams(layoutParams);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ImageLoader.getInstance().displayImage(brandList.get(position).getCover_url(), holder.img);
        return convertView;
    }

    static class ViewHolder {
        RoundedImageView img;
    }
}
