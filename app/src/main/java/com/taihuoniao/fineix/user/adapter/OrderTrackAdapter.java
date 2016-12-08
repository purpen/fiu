package com.taihuoniao.fineix.user.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.user.bean.OrderTrackBean;

import java.util.List;

/**
 * Created by Stephen on 12/9/2016.
 * Emial: 895745843@qq.com
 */
public class OrderTrackAdapter extends RecyclerView.Adapter<OrderTrackAdapter.ViewHolder> {

    public static final int TYPE_HEADER = 0;
    public static final int TYPE_NORMAL = 1;
    private Context mContext;
    private List<OrderTrackBean.TracesEntity> mTraces;
    private OnItemClickLitener mOnItemClickLitener;
    private View mHeaderView;

    public void setHeaderView(View headerView) {
        mHeaderView = headerView;
        notifyItemInserted(0);
    }

    public View getHeaderView() {
        return mHeaderView;
    }


    @Override
    public int getItemViewType(int position) {
        if(mHeaderView == null) return TYPE_NORMAL;
        if(position == 0) return TYPE_HEADER;
        return TYPE_NORMAL;
    }

    public interface OnItemClickLitener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }

    public OrderTrackAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public OrderTrackAdapter(Context mContext, List<OrderTrackBean.TracesEntity> traces) {
        this.mContext = mContext;
        this.mTraces = traces;
    }

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    @Override
    public OrderTrackAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(mHeaderView != null && viewType == TYPE_HEADER) return new ViewHolder(mHeaderView);
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_order_track, parent, false));
    }

    @Override
    public void onBindViewHolder(final OrderTrackAdapter.ViewHolder holder, final int position) {
        if(getItemViewType(position) == TYPE_HEADER) return;
        int realPosition = getRealPosition(holder);
        holder.itemDate.setText(mTraces.get(realPosition).getAcceptTime());
        holder.itemDescription.setText(mTraces.get(realPosition).getAcceptStation());
        if (position == 1) {
            holder.itemFlag.setImageResource(R.mipmap.checked);
            holder.itemDate.setTextColor(Color.parseColor("#BE8914"));
            holder.itemDescription.setTextColor(Color.parseColor("#BE8914"));
        } else {
            holder.itemFlag.setImageResource(R.mipmap.check);
            holder.itemDate.setTextColor(Color.parseColor("#999999"));
            holder.itemDescription.setTextColor(Color.parseColor("#666666"));
        }
        if (mOnItemClickLitener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int layoutPosition = holder.getLayoutPosition();
                    mOnItemClickLitener.onItemClick(holder.itemView, layoutPosition);
                }
            });
        }
    }


    @Override
    public int getItemCount() {
        return mTraces == null ? 0: mHeaderView == null ? mTraces.size() : mTraces.size() + 1;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView itemFlag;
        private TextView itemDate;
        private TextView itemDescription;

        public ViewHolder(View itemView) {
            super(itemView);
            itemFlag = (ImageView) itemView.findViewById(R.id.item_flag);
            itemDate = (TextView) itemView.findViewById(R.id.item_date);
            itemDescription = (TextView) itemView.findViewById(R.id.item_description);
        }
    }

    public void putAll(List<OrderTrackBean.TracesEntity> traces) {
        mTraces = traces;
        notifyDataSetChanged();
    }

    public int getRealPosition(RecyclerView.ViewHolder holder) {
        int position = holder.getLayoutPosition();
        return mHeaderView == null ? position : position - 1;
    }
}
