package com.taihuoniao.fineix.main.tab3.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.beans.CategoryListBean;
import com.taihuoniao.fineix.common.GlobalCallBack;
import com.taihuoniao.fineix.utils.GlideUtils;

import java.util.List;

/**
 * Created by Stephen on 2017/3/3 21:20
 * Email: 895745843@qq.com
 */

public class WellGoodsCategoryAdapter extends RecyclerView.Adapter<WellGoodsCategoryAdapter.VH> {

    private LayoutInflater mLayoutInflater;
    private GlobalCallBack mGlobalCallBack;
    private List<CategoryListBean.RowsEntity> categoryListItems;

    public WellGoodsCategoryAdapter(Context context, GlobalCallBack globalCallBack) {
        this.mGlobalCallBack = globalCallBack;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public WellGoodsCategoryAdapter.VH onCreateViewHolder(ViewGroup parent, int viewType) {
        return new WellGoodsCategoryAdapter.VH(mLayoutInflater.inflate(R.layout.item_recyclerview_wellgoods_category, null));
    }

    @Override
    public void onBindViewHolder(WellGoodsCategoryAdapter.VH holder, int position) {
        final CategoryListBean.RowsEntity rowsEntity = categoryListItems.get(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mGlobalCallBack != null) {
                    mGlobalCallBack.callBack(rowsEntity);
                }
            }
        });
        if (rowsEntity != null) {
            GlideUtils.displayImage(rowsEntity.getApp_cover_url(), holder.imageViewIcon);
            holder.textViewTitle.setText(rowsEntity.getTitle());
        }
    }

    @Override
    public int getItemCount() {
        return categoryListItems == null ? 0 : categoryListItems.size();
    }

    class VH extends RecyclerView.ViewHolder {
        private ImageView imageViewIcon;
        private TextView textViewTitle;

        public VH(View itemView) {
            super(itemView);
            imageViewIcon = (ImageView) itemView.findViewById(R.id.imageView_icon);
            textViewTitle = (TextView)itemView. findViewById(R.id.textView_title);
        }
    }

    public void putList(List<CategoryListBean.RowsEntity> categoryListItems){
        this.categoryListItems = categoryListItems;
        notifyDataSetChanged();
    }
}
