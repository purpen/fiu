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
import com.taihuoniao.fineix.interfaces.IRecycleViewItemClickListener;
import com.taihuoniao.fineix.utils.GlideUtils;

import java.util.List;

/**
 * Created by Stephen on 2017/2/15 18:06
 * Email: 895745843@qq.com
 */

public class IndexAdapter005 extends RecyclerView.Adapter<IndexAdapter005.VH> {
    private LayoutInflater mLayoutInflater;
    private List<BannerBean.RowsEntity> rowsEntities;
    private IRecycleViewItemClickListener itemClickListener;
    public void setOnItemClickListener(IRecycleViewItemClickListener listener){
        this.itemClickListener = listener;
    }
    public IndexAdapter005(Context context, List<BannerBean.RowsEntity> rowsEntities) {
        this.rowsEntities = rowsEntities;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.item_index_subject005, null);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(final VH holder, final int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClickListener != null) {
                    itemClickListener.onItemClick(v,position);
                }
            }
        });

        BannerBean.RowsEntity rowsEntity = rowsEntities.get(position);
        if (rowsEntity != null) {
            GlideUtils.displayImage(rowsEntity.getCover_url(), holder.imageViewPicture);
            holder.textViewTitle.setText(rowsEntity.getTitle());
            holder.textViewDescription.setText(rowsEntity.getSummary());
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

    public void setRowsEntities(List<BannerBean.RowsEntity> rowsEntities){
        this.rowsEntities = rowsEntities;
        notifyDataSetChanged();
    }

    public BannerBean.RowsEntity getItem(int position){
        if (rowsEntities==null || rowsEntities.size()==0) return null;
        return rowsEntities.get(position);
    }
}
