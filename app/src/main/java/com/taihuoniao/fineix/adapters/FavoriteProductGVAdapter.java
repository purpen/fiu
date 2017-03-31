package com.taihuoniao.fineix.adapters;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.beans.ItemProductCollect;
import com.taihuoniao.fineix.utils.GlideUtils;
import com.taihuoniao.fineix.utils.Util;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author lilin
 *         created at 2016/8/17 15:29
 */
public class FavoriteProductGVAdapter extends CommonBaseAdapter<ItemProductCollect> {
    public FavoriteProductGVAdapter(ArrayList list, Activity activity) {
        super(list, activity);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ItemProductCollect item = list.get(position);
        ViewHolder holder;
        if (convertView == null) {
            convertView = Util.inflateView(R.layout.item_favorite_product, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (item.product != null) {
            GlideUtils.displayImage(item.product.cover_url, holder.imageView);
            holder.tvName.setText(item.product.title);
            if (item.product.stage==9){
                holder.tvPrice.setVisibility(View.VISIBLE);
                holder.tvPrice.setText("￥" + item.product.sale_price);
            }else {
                holder.tvPrice.setVisibility(View.GONE);
            }
        }
        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.imageView)
        ImageView imageView;
        @Bind(R.id.tv_title)
        TextView tvTitle;
        @Bind(R.id.tv_name)
        TextView tvName;
        @Bind(R.id.tv_price)
        TextView tvPrice;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
