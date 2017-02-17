package com.taihuoniao.fineix.zone.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.CommonBaseAdapter;
import com.taihuoniao.fineix.product.BuyGoodsDetailsActivity;
import com.taihuoniao.fineix.utils.GlideUtils;
import com.taihuoniao.fineix.utils.Util;
import com.taihuoniao.fineix.zone.bean.ZoneRelateProductsBean;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ZoneRelateProductsAdapter extends CommonBaseAdapter<ZoneRelateProductsBean.RowsBean> {
    private int i = Util.getScreenWidth() - 3 * activity.getResources().getDimensionPixelSize(R.dimen.dp16);
    private LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, i / 2, 1);

    public ZoneRelateProductsAdapter(Activity activity,List<ZoneRelateProductsBean.RowsBean> list) {
        super(list, activity);
    }

    @Override
    public int getCount() {
        int size = super.getCount();
        return (size % 2 == 0 ? size / 2 : size / 2 + 1);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = Util.inflateView(R.layout.item_zone_relate_products, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final ZoneRelateProductsBean.RowsBean left_qj;
        ZoneRelateProductsBean.RowsBean right_qj = null;
        left_qj = list.get(2 * position);

        if (position == list.size() / 2) {
            holder.rlRight.setVisibility(View.INVISIBLE);
        } else {
            holder.ivCoverRight.setLayoutParams(params);
            holder.rlRight.setVisibility(View.VISIBLE);
            right_qj = list.get(2 * position + 1);
            GlideUtils.displayImageFadein(right_qj.product.cover_url, holder.ivCoverRight);
            holder.tvNameRight.setText(right_qj.product.title);
            holder.tvPrice.setText("¥"+right_qj.product.sale_price);
        }
        GlideUtils.displayImageFadein(left_qj.product.cover_url, holder.ivCoverLeft);
        holder.tvNameLeft.setText(left_qj.product.title);
        holder.tvPriceLeft.setText("¥"+left_qj.product.sale_price);
        holder.ivCoverLeft.setLayoutParams(params);
        holder.rlLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, BuyGoodsDetailsActivity.class);
                intent.putExtra("id", String.valueOf(left_qj.product._id));
                activity.startActivity(intent);
            }
        });
        final ZoneRelateProductsBean.RowsBean finalRight_qj = right_qj;
        holder.rlRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, BuyGoodsDetailsActivity.class);
                intent.putExtra("id", String.valueOf(finalRight_qj.product._id));
                activity.startActivity(intent);
            }
        });
        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.iv_cover_left)
        ImageView ivCoverLeft;
        @Bind(R.id.tv_name_left)
        TextView tvNameLeft;
        @Bind(R.id.tv_price_left)
        TextView tvPriceLeft;
        @Bind(R.id.rl_left)
        LinearLayout rlLeft;
        @Bind(R.id.iv_cover_right)
        ImageView ivCoverRight;
        @Bind(R.id.tv_name_right)
        TextView tvNameRight;
        @Bind(R.id.tv_price)
        TextView tvPrice;
        @Bind(R.id.rl_right)
        LinearLayout rlRight;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
