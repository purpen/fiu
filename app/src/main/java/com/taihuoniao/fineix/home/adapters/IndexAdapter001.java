package com.taihuoniao.fineix.home.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.common.bean.BannerBean;
import com.taihuoniao.fineix.common.GlobalCallBack;
import com.taihuoniao.fineix.utils.GlideUtils;

import java.util.List;

/**
 * 新手福利见面礼
 * Created by Stephen on 2017/2/15 18:06
 * Email: 895745843@qq.com
 */

public class IndexAdapter001 extends RecyclerView.Adapter<IndexAdapter001.VH> {
    private LayoutInflater mLayoutInflater;
    private List<BannerBean.RowsBean> rowsEntities;
    private GlobalCallBack mGlobalCallBack;

    public IndexAdapter001(Context context, GlobalCallBack globalCallBack) {
        this.mGlobalCallBack = globalCallBack;
        mLayoutInflater = LayoutInflater.from(context);
    }

    public IndexAdapter001(Context context, List<BannerBean.RowsBean> rowsEntities) {
        this.rowsEntities = rowsEntities;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        return new VH(mLayoutInflater.inflate(R.layout.item_index_subject001, null));
    }

    @Override
    public void onBindViewHolder(final VH holder, int position) {
        final BannerBean.RowsBean rowsEntity = rowsEntities.get(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mGlobalCallBack != null) {
                    mGlobalCallBack.callBack(rowsEntity);
                }
            }
        });

        if (rowsEntity != null) {
            GlideUtils.displayImage(rowsEntity.cover_url, holder.imageViewPicture);
            holder.textViewTitle.setText(rowsEntity.title);
            holder.textViewDescription.setText(rowsEntity.sub_title);
        }

    }

    @Override
    public int getItemCount() {
        return rowsEntities == null ? 0 : rowsEntities.size();
    }

    class VH extends RecyclerView.ViewHolder {
        private ImageView imageViewPicture;
        private TextView textViewTitle;
        private ImageView i;
        private TextView textViewDescription;
        private ImageView label;

        public VH(View itemView) {
            super(itemView);
            imageViewPicture = (ImageView) itemView.findViewById(R.id.imageView_picture);
            textViewTitle = (TextView)itemView. findViewById(R.id.textView_title);
            i = (ImageView) itemView.findViewById(R.id.i);
            textViewDescription = (TextView) itemView.findViewById(R.id.textView_description);
            label = (ImageView) itemView.findViewById(R.id.label);
        }
    }

    public void setRowsEntities(List<BannerBean.RowsBean> rowsEntities){
        this.rowsEntities = rowsEntities;
        notifyDataSetChanged();
    }
}
