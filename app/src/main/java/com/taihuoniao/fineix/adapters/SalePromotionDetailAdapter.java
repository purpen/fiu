package com.taihuoniao.fineix.adapters;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.beans.SubjectData;
import com.taihuoniao.fineix.product.BuyGoodsDetailsActivity;
import com.taihuoniao.fineix.utils.GlideUtils;
import com.taihuoniao.fineix.utils.Util;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author lilin
 *         created at 2016/8/17 15:29
 */
public class SalePromotionDetailAdapter extends CommonBaseAdapter<SubjectData.ProductBean> {
    public SalePromotionDetailAdapter(ArrayList list, Activity activity) {
        super(list, activity);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final SubjectData.ProductBean item = list.get(position);
        ViewHolder holder;
        if (convertView == null) {
            convertView = Util.inflateView(R.layout.item_sale_promotion_detail, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
//        ImageLoader.getInstance().displayImage(item.banner_url, holder.imageView, options);
        GlideUtils.displayImage(item.banner_url, holder.imageView);
        holder.tvTitle.setText(String.format("%s. %s", position + 1, item.title));
        holder.tvDesc.setText(item.summary);
        holder.tvSalePrice.setText(String.format("￥%s", item.sale_price));
        if (TextUtils.equals(item.sale_price, item.market_price)) {
            holder.tvMarketPrice.setVisibility(View.GONE);
        } else {
            holder.tvMarketPrice.setVisibility(View.VISIBLE);
            holder.tvMarketPrice.setText(String.format("￥%s", item.market_price));
        }
        holder.tvMarketPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        holder.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, BuyGoodsDetailsActivity.class);
                intent.putExtra("id", item._id);
                activity.startActivity(intent);
            }
        });
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, BuyGoodsDetailsActivity.class);
                intent.putExtra("id", item._id);
                activity.startActivity(intent);
            }
        });
        return convertView;
    }


    static class ViewHolder {
        @Bind(R.id.tv_title)
        TextView tvTitle;
        @Bind(R.id.tv_desc)
        TextView tvDesc;
        @Bind(R.id.imageView)
        ImageView imageView;
        @Bind(R.id.tv_sale_price)
        TextView tvSalePrice;
        @Bind(R.id.tv_market_price)
        TextView tvMarketPrice;
        @Bind(R.id.btn)
        Button btn;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
