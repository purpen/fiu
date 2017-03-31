package com.taihuoniao.fineix.zone.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.utils.GlideUtils;
import com.taihuoniao.fineix.utils.Util;
import com.taihuoniao.fineix.zone.bean.ZoneRelateProductsBean;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ZoneRelateProductsAdapter extends RecyclerView.Adapter<ZoneRelateProductsAdapter.ViewHolder> {
    private LinearLayout.LayoutParams params;
    private Activity activity;
    private List<ZoneRelateProductsBean.RowsBean> list;
    private ZoneRelateSceneAdapter.OnItemClickListener mOnItemClickListener;
    public void setOnItemClickListener(ZoneRelateSceneAdapter.OnItemClickListener listener){
        this.mOnItemClickListener = listener;
    }
    public interface OnItemClickListener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }

    public ZoneRelateProductsAdapter(Activity activity, List<ZoneRelateProductsBean.RowsBean> list) {
        this.activity = activity;
        this.list = list;
        int width = Util.getScreenWidth() - 3 * activity.getResources().getDimensionPixelSize(R.dimen.dp16);
        params= new LinearLayout.LayoutParams(width / 2, width / 2);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.item_zone_relate_products, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnItemClickListener.onItemClick(holder.itemView, holder.getAdapterPosition());
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
        holder.ivCover.setLayoutParams(params);
        GlideUtils.displayImageFadein(list.get(position).product.cover_url, holder.ivCover);
        holder.tvName.setText(list.get(position).product.title);
        holder.tvPrice.setText("¥" + list.get(position).product.sale_price);

    }

//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        final ViewHolder holder;
//        if (convertView == null) {
//            convertView = Util.inflateView(R.layout.item_zone_relate_products, null);
//            holder = new ViewHolder(convertView);
//            convertView.setTag(holder);
//        } else {
//            holder = (ViewHolder) convertView.getTag();
//        }
//        final ZoneRelateProductsBean.RowsBean left_qj;
//        ZoneRelateProductsBean.RowsBean right_qj = null;
//        left_qj = list.get(2 * position);
//
//        if (position == list.size() / 2) {
//            holder.rlRight.setVisibility(View.INVISIBLE);
//        } else {
//
//            holder.rlRight.setVisibility(View.VISIBLE);
//            right_qj = list.get(2 * position + 1);
//            GlideUtils.displayImageFadein(right_qj.product.cover_url, holder.ivCoverRight);
//            holder.tvNameRight.setText(right_qj.product.title);
//            holder.tvPrice.setText("¥"+right_qj.product.sale_price);
//        }
//        GlideUtils.displayImageFadein(left_qj.product.cover_url, holder.ivCoverLeft);
//        holder.tvNameLeft.setText(left_qj.product.title);
//        holder.tvPriceLeft.setText("¥"+left_qj.product.sale_price);
//        holder.ivCoverLeft.setLayoutParams(params);
//        holder.rlLeft.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(activity, BuyGoodsDetailsActivity.class);
//                intent.putExtra("id", String.valueOf(left_qj.product._id));
//                activity.startActivity(intent);
//            }
//        });
//        final ZoneRelateProductsBean.RowsBean finalRight_qj = right_qj;
//        holder.rlRight.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(activity, BuyGoodsDetailsActivity.class);
//                intent.putExtra("id", String.valueOf(finalRight_qj.product._id));
//                activity.startActivity(intent);
//            }
//        });
//        return convertView;
//    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.iv_cover)
        ImageView ivCover;
        @Bind(R.id.tv_name)
        TextView tvName;
        @Bind(R.id.tv_price)
        TextView tvPrice;
        @Bind(R.id.rl_price)
        RelativeLayout rlPrice;
        @Bind(R.id.ll_box)
        LinearLayout llBox;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
