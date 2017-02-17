package com.taihuoniao.fineix.home.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.common.GlobalCallBack;
import com.taihuoniao.fineix.home.beans.CategoryListBean;
import com.taihuoniao.fineix.utils.GlideUtils;

import java.util.List;

/**
 * Created by Stephen on 2017/2/15 18:06
 * Email: 895745843@qq.com
 */

public class IndexAdapter004 extends RecyclerView.Adapter<IndexAdapter004.VH> {
    private LayoutInflater mLayoutInflater;
    private List<CategoryListBean.RowsEntity> rowsEntities;
    private GlobalCallBack mGlobalCallBack;

    public IndexAdapter004(Context context, GlobalCallBack globalCallBack) {
        this.mGlobalCallBack = globalCallBack;
        mLayoutInflater = LayoutInflater.from(context);
    }

    public IndexAdapter004(Context context, List<CategoryListBean.RowsEntity> rowsEntities) {
        this.rowsEntities = rowsEntities;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.item_index_subject004, null);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(final VH holder, int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mGlobalCallBack != null) {
                    mGlobalCallBack.callBack(holder.getAdapterPosition());
                }
            }
        });

        CategoryListBean.RowsEntity rowsEntity = rowsEntities.get(position);
        if (rowsEntity != null) {
            GlideUtils.displayImage(rowsEntity.getBack_url(), holder.imageViewIcon);
            holder.textViewTitle.setText(rowsEntity.getTitle());
        }

    }

    @Override
    public int getItemCount() {
        return rowsEntities == null ? 0 : rowsEntities.size();
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

    public void setRowsEntities(List<CategoryListBean.RowsEntity> rowsEntities){
        this.rowsEntities = rowsEntities;
        notifyDataSetChanged();
    }
}
