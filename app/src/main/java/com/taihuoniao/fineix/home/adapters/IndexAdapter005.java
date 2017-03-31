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
 * D3IN在这里
 */

public class IndexAdapter005 extends RecyclerView.Adapter<IndexAdapter005.VH> {
    private LayoutInflater mLayoutInflater;
    private List<BannerBean.RowsBean> rowsEntities;
    private IRecycleViewItemClickListener itemClickListener;
    public void setOnItemClickListener(IRecycleViewItemClickListener listener){
        this.itemClickListener = listener;
    }
    public IndexAdapter005(Context context, List<BannerBean.RowsBean> rowsEntities) {
        this.rowsEntities = rowsEntities;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        return new VH(mLayoutInflater.inflate(R.layout.item_index_subject005, null));
    }

    @Override
    public void onBindViewHolder(final VH holder, final int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClickListener != null) {
                    itemClickListener.onItemClick(v,holder.getAdapterPosition());
                }
            }
        });

        BannerBean.RowsBean rowsEntity = rowsEntities.get(position);
        if (rowsEntity != null) {
            GlideUtils.displayImage(rowsEntity.cover_url, holder.imageViewPicture);
            holder.textViewTitle.setText(rowsEntity.title);
            holder.textViewDescription.setText(rowsEntity.sub_title);
            switch (rowsEntity.cate_title){
                case "1":
                    holder.label.setImageResource(R.mipmap.shop_ti_yan);
                    break;
                case "2":
                    holder.label.setImageResource(R.mipmap.shop_chuang_yi);
                    break;
                case "3":
                    holder.label.setImageResource(R.mipmap.shop_min_su);
                    break;
                case "4":
                    holder.label.setImageResource(R.mipmap.shop_chuang_yi);
                    break;
                case "5":
                    holder.label.setImageResource(R.mipmap.shop_cafe);
                    break;
                case "6":
                    holder.label.setImageResource(R.mipmap.shop_foods);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return rowsEntities == null ? 0 : rowsEntities.size();
    }

    class VH extends RecyclerView.ViewHolder {
        private ImageView imageViewPicture;
        private TextView textViewTitle;
        private TextView textViewDescription;
        private ImageView label;

        public VH(View itemView) {
            super(itemView);
            imageViewPicture = (ImageView) itemView.findViewById(R.id.imageView_picture);
            textViewTitle = (TextView)itemView. findViewById(R.id.textView_title);
            textViewDescription = (TextView) itemView.findViewById(R.id.textView_description);
            label = (ImageView) itemView.findViewById(R.id.label);
        }
    }

    public void setRowsEntities(List<BannerBean.RowsBean> rowsEntities){
        this.rowsEntities = rowsEntities;
        notifyDataSetChanged();
    }

    public BannerBean.RowsBean getItem(int position){
        if (rowsEntities==null || rowsEntities.size()==0) return null;
        return rowsEntities.get(position);
    }
}
