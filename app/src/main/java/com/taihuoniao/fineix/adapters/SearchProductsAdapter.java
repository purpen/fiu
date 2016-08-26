package com.taihuoniao.fineix.adapters;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.beans.SearchBean;
import com.taihuoniao.fineix.main.MainApplication;
import com.taihuoniao.fineix.utils.DensityUtils;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by taihuoniao on 2016/8/24.
 */
public class SearchProductsAdapter extends BaseAdapter {
    private List<SearchBean.Data.SearchItem> list;

    public SearchProductsAdapter(List<SearchBean.Data.SearchItem> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(parent.getContext(), R.layout.item_search_products, null);
            holder = new ViewHolder(convertView);
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) holder.productImg.getLayoutParams();
            layoutParams.width = (MainApplication.getContext().getScreenWidth() - DensityUtils.dp2px(parent.getContext(), 45)) / 2;
            layoutParams.height = layoutParams.width;
            holder.productImg.setLayoutParams(layoutParams);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ImageLoader.getInstance().displayImage(list.get(position).getCover_url(), holder.productImg);
        holder.name.setText(list.get(position).getTitle());
        holder.price.setText("¥" + list.get(position).getSale_price());
        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.product_img)
        ImageView productImg;
        @Bind(R.id.name)
        TextView name;
        @Bind(R.id.price)
        TextView price;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
