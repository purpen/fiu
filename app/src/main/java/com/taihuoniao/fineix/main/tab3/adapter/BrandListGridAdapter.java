package com.taihuoniao.fineix.main.tab3.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.beans.BrandListBean;
import com.taihuoniao.fineix.utils.GlideUtils;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Stephen on 2017/4/12 17:26
 * Email: 895745843@qq.com
 */

public class BrandListGridAdapter extends BaseAdapter {

    private List<BrandListBean.RowsEntity> mRowsEntities;

    public List<BrandListBean.RowsEntity> getmRowsEntities() {
        return mRowsEntities;
    }

    @Override
    public int getCount() {
        return mRowsEntities == null ? 0 : mRowsEntities.size();
    }

    @Override
    public Object getItem(int position) {
        return mRowsEntities.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BrandListGridAdapter.ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(parent.getContext(), R.layout.item_gridview_productlist_brand, null);
            holder = new BrandListGridAdapter.ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (BrandListGridAdapter.ViewHolder) convertView.getTag();
        }
        BrandListBean.RowsEntity rowsEntity = mRowsEntities.get(position);
        if (rowsEntity != null) {
            holder.productBrandName.setText(rowsEntity.getTitle());
            GlideUtils.displayImage(rowsEntity.getCover_url(), holder.productBrandImg);
        }
        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.productBrand_img)
        ImageView productBrandImg;
        @Bind(R.id.productBarand_name)
        TextView productBrandName;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public void setList(List<BrandListBean.RowsEntity> rows) {
        if (mRowsEntities == null) {
            mRowsEntities = rows;
        } else {
            mRowsEntities.clear();
            mRowsEntities.addAll(rows);
        }
        notifyDataSetChanged();
    }

    public void addList(List<BrandListBean.RowsEntity> rows) {
        if (mRowsEntities == null) {
            mRowsEntities = rows;
        } else {
            mRowsEntities.addAll(rows);
        }
        notifyDataSetChanged();
    }
}
