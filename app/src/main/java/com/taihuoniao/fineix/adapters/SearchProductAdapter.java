package com.taihuoniao.fineix.adapters;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.beans.ProductBean;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by taihuoniao on 2016/8/17.
 */
public class SearchProductAdapter extends BaseAdapter {
    private String brandName;
    private List<ProductBean.RowsEntity> productList;

    public SearchProductAdapter(String brandName, List<ProductBean.RowsEntity> productList) {
        this.brandName = brandName;
        this.productList = productList;
    }

    @Override
    public int getCount() {
        return productList.size();
    }

    @Override
    public Object getItem(int position) {
        return productList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(parent.getContext(), R.layout.item_search_product, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.brandName.setText(brandName);
        holder.productName.setText(productList.get(position).getTitle());
        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.brand_name)
        TextView brandName;
        @Bind(R.id.product_name)
        TextView productName;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
