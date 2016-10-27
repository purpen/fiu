package com.taihuoniao.fineix.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.beans.AddressData;
import com.taihuoniao.fineix.utils.Util;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author lilin
 *         created at 2016/9/27 9:37
 */

public class SimpleTextAdapter extends RecyclerView.Adapter<SimpleTextAdapter.ViewHolder> {
    private Activity activity;
    private List<AddressData.RowsEntity> list;
    private boolean clickable=true;
    public interface OnItemClickListener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.mOnItemClickListener = itemClickListener;
    }

    public SimpleTextAdapter(Activity activity, List<AddressData.RowsEntity> list) {
        this.activity = activity;
        this.list = list;
    }

    @Override
    public SimpleTextAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = Util.inflateView(R.layout.view_bottom_item, null);
        return new SimpleTextAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SimpleTextAdapter.ViewHolder holder, int position) {
        holder.mTextView.setText(list.get(position).name);
        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (clickable){
                        mOnItemClickListener.onItemClick(holder.itemView, holder.getAdapterPosition());
                    }
                }
            });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    mOnItemClickListener.onItemLongClick(holder.itemView, holder.getAdapterPosition());
                    return false;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setClickable(boolean clickable){
        this.clickable=clickable;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.tv_txt)
        TextView mTextView;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
