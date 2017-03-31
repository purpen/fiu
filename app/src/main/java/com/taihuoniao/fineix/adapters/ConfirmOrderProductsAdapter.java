package com.taihuoniao.fineix.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.beans.CartOrderContentItem;
import com.taihuoniao.fineix.beans.NowBuyBean;
import com.taihuoniao.fineix.utils.GlideUtils;

import java.util.List;

/**
 * Created by taihuoniao on 2016/3/7.
 */
public class ConfirmOrderProductsAdapter extends BaseAdapter {
    private List<NowBuyBean.OrderInfoBean.DictBean.ItemsBean> list;
    private Context context;
    private List<CartOrderContentItem> cartList;

    public ConfirmOrderProductsAdapter(List<NowBuyBean.OrderInfoBean.DictBean.ItemsBean> list, Context context, List<CartOrderContentItem> cartList) {
        this.list = list;
        this.context = context;
        this.cartList = cartList;
    }

    @Override
    public int getCount() {
        return list == null ? cartList.size() : list.size();
    }

    @Override
    public Object getItem(int position) {
        return list == null ? cartList.get(position) : list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder hold;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_confirmorder_products, null);
            hold = new ViewHolder();
            hold.img = (ImageView) convertView.findViewById(R.id.activity_confirmorder_productimg);
            hold.titleTv = (TextView) convertView.findViewById(R.id.activity_confirmorder_producttitle);
            hold.skuNameTv = (TextView) convertView.findViewById(R.id.activity_confirmorder_skuname);
            hold.skuNumberTv = (TextView) convertView.findViewById(R.id.activity_confirmorder_skunumber);
            hold.totalPriceTv = (TextView) convertView.findViewById(R.id.activity_confirmorder_totalprice);
            convertView.setTag(hold);
        } else {
            hold = (ViewHolder) convertView.getTag();
        }
        if (list != null) {
            GlideUtils.displayImage(list.get(position).getCover(), hold.img);
            hold.titleTv.setText(list.get(position).getTitle());
            if (list.get(position).getType().equals("1")) {
                hold.skuNameTv.setText("默认");
            } else {
                hold.skuNameTv.setText(list.get(position).getSku_mode());
            }
            hold.skuNumberTv.setText(list.get(position).getQuantity());
            hold.totalPriceTv.setText("¥ " + list.get(position).getSubtotal());
        } else if (cartList != null) {
            GlideUtils.displayImage(cartList.get(position).getCover(), hold.img);
            hold.titleTv.setText(cartList.get(position).getTitle());
            if (cartList.get(position).getType().equals("1")) {
                hold.skuNameTv.setText("默认");
            } else {
                hold.skuNameTv.setText(cartList.get(position).getSku_mode());
            }
            hold.skuNumberTv.setText(cartList.get(position).getQuantity());
            hold.totalPriceTv.setText("¥ " + cartList.get(position).getSubtotal());
        }
        return convertView;
    }

    private class ViewHolder {
        private ImageView img;
        private TextView titleTv;
        private TextView skuNameTv;
        private TextView skuNumberTv;
        private TextView totalPriceTv;
    }
}
