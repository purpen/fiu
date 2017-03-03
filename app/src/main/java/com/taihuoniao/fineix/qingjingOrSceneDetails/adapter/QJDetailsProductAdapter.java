package com.taihuoniao.fineix.qingjingOrSceneDetails.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.qingjingOrSceneDetails.bean.SceneDetailsBean2;
import com.taihuoniao.fineix.utils.GlideUtils;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 情境详情下 产品列表
 * Created by Stephen on 2017/03/01.
 */
public class QJDetailsProductAdapter extends BaseAdapter {

    private List<SceneDetailsBean2.ProductsEntity> listProducts;

    public QJDetailsProductAdapter(List<SceneDetailsBean2.ProductsEntity> listProducts) {
        this.listProducts = listProducts;
    }

    @Override
    public int getCount() {
        return listProducts == null ? 0 : listProducts.size();
    }

    @Override
    public Object getItem(int position) {
        return listProducts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(parent.getContext(), R.layout.item_listview_qj_details_product, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        GlideUtils.displayImage(listProducts.get(position).getCover_url(), holder.imageViewQjDetailsPicture);
        holder.textViewQjDetailsName.setText(listProducts.get(position).getTitle());
        holder.textViewQjDetailsPrice.setText(listProducts.get(position).getSale_price());
        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.imageView_qj_detials_picture)
        ImageView imageViewQjDetailsPicture;
        @Bind(R.id.textView_qj_details_name)
        TextView textViewQjDetailsName;
        @Bind(R.id.textView_qj_details_price)
        TextView textViewQjDetailsPrice;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public void putList(List<SceneDetailsBean2.ProductsEntity> listProducts) {
        this.listProducts = listProducts;
        this.notifyDataSetChanged();
    }

    public List<SceneDetailsBean2.ProductsEntity> getList() {
        return listProducts;
    }
}