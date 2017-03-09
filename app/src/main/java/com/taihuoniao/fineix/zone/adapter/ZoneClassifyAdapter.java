package com.taihuoniao.fineix.zone.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.zone.bean.ZoneClassifyBean;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 地盘分类适配
 */
public class ZoneClassifyAdapter extends RecyclerView.Adapter<ZoneClassifyAdapter.ViewHolder> {
    private Activity activity;
    private List<ZoneClassifyBean.RowsBean> list;
    private ZoneClassifyAdapter.OnItemClickListener mOnItemClickListener;
    public void setOnItemClickListener(ZoneClassifyAdapter.OnItemClickListener listener){
        this.mOnItemClickListener = listener;
    }
    public interface OnItemClickListener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }

    public ZoneClassifyAdapter(Activity activity, List<ZoneClassifyBean.RowsBean> list) {
        this.activity = activity;
        this.list = list;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.item_zone_classify, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        ZoneClassifyBean.RowsBean item = list.get(position);
        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnItemClickListener.onItemClick(holder.idTVTag, holder.getAdapterPosition());
                }
            });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    mOnItemClickListener.onItemLongClick(holder.idTVTag, holder.getAdapterPosition());
                    return false;
                }
            });
        }
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,activity.getResources().getDimensionPixelOffset(R.dimen.dp40));
        holder.idTVTag.setLayoutParams(params);
        if (item.isSelected){
            holder.idTVTag.setBackgroundResource(R.drawable.shape_classify_selected);
            holder.idTVTag.setTextColor(activity.getResources().getColor(android.R.color.white));
        }else {
            holder.idTVTag.setBackgroundResource(R.drawable.shape_classify_tag);
            holder.idTVTag.setTextColor(activity.getResources().getColor(R.color.color_666));
        }
        holder.idTVTag.setText(list.get(position).title);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.id_tvTag)
        TextView idTVTag;
        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
