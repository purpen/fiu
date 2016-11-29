package com.taihuoniao.fineix.personal.salesevice.adapter;


import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.personal.salesevice.bean.ChargeBackListBean;
import com.taihuoniao.fineix.utils.GlideUtils;
import com.taihuoniao.fineix.view.WaittingDialog;

import java.util.List;

/**
 * 退款/ 售后
 * Created by Stephen on 2016/11/26.
 */
public class SaleAfterListAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private AlertDialog.Builder alertDialog;
    private WaittingDialog mdialog;
    private View mView;
    private List<ChargeBackListBean.RowsBean> list;
    private View.OnClickListener onClickListener;

    public SaleAfterListAdapter(List<ChargeBackListBean.RowsBean> rows, Context context) {
        this(rows, context, null);
    }
    public SaleAfterListAdapter(List<ChargeBackListBean.RowsBean> rows, Context context, View.OnClickListener onClickListener) {
        mInflater = LayoutInflater.from(context);
        alertDialog = new AlertDialog.Builder(context);
        mdialog = new WaittingDialog(context);
        this.list = rows;
        this.onClickListener = onClickListener;
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
        ViewHolder mHolder;
        if (convertView == null) {
            mHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_sale_after, null);
            mHolder.textViewOrderCodeTitle = (TextView) convertView.findViewById(R.id.textView_order_code_title);
            mHolder.textViewOrderNumber = (TextView) convertView.findViewById(R.id.textView_order_number);
            mHolder.textViewOrderStatus = (TextView) convertView.findViewById(R.id.textView_order_status);
            mHolder.textViewOrderDate = (TextView) convertView.findViewById(R.id.textView_order_date);
            mHolder.textViewOrderMoney = (TextView)convertView. findViewById(R.id.textView_order_money);
            mHolder.imageViewGoods = (ImageView) convertView.findViewById(R.id.imageView_goods);
            mHolder.textViewGoodsDescription = (TextView) convertView.findViewById(R.id.textView_goods_description);
            mHolder.textViewSpecification = (TextView) convertView.findViewById(R.id.textView_specification);
            mHolder.textViewPrice = (TextView) convertView.findViewById(R.id.textView_price);
            mHolder.relativeLayoutGoodsInfo = (RelativeLayout) convertView.findViewById(R.id.relativeLayout_good_info);
            convertView.setTag(mHolder);
        } else {
            mHolder = (ViewHolder) convertView.getTag();
        }

        ChargeBackListBean.RowsBean rowsEntity = list.get(position);
        if (rowsEntity != null) {
            mHolder.textViewOrderCodeTitle.setText("退款编号: ");
            mHolder.textViewOrderNumber.setText(String.valueOf(rowsEntity.get_id()));
            mHolder.textViewOrderStatus.setText(rowsEntity.getStage_label());
            mHolder.textViewOrderDate.setText(rowsEntity.getCreated_at());
            mHolder.textViewOrderMoney.setText(String.format("¥%s", rowsEntity.getRefund_price()));

//            mHolder.relativeLayoutGoodsInfo.setOnClickListener(onClickListener);
//            mHolder.relativeLayoutGoodsInfo.setTag(R.id.relativeLayout_good_info, rowsEntity.getProduct_id());

            ChargeBackListBean.RowsBean.ProductBean product = rowsEntity.getProduct();
            if (product != null) {
                GlideUtils.displayImageFadein(product.getCover_url(), mHolder.imageViewGoods);
                mHolder.textViewGoodsDescription.setText(product.getTitle());
                mHolder.textViewSpecification.setText(product.getSku_name() + String.format(" * %s", product.getQuantity()));
                mHolder.textViewPrice.setText(String.format("¥%s", product.getSale_price()));
            }
        }
        return convertView;
    }

    static class ViewHolder {
        private TextView textViewOrderCodeTitle;
        private TextView textViewOrderNumber;
        private TextView textViewOrderStatus;
        private TextView textViewOrderDate;
        private TextView textViewOrderMoney;
        private ImageView imageViewGoods;
        private TextView textViewGoodsDescription;
        private TextView textViewSpecification;
        private TextView textViewPrice;
        private RelativeLayout relativeLayoutGoodsInfo;
    }
}
