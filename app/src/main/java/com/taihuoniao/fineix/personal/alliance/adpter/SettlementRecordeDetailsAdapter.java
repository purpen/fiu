package com.taihuoniao.fineix.personal.alliance.adpter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.personal.alliance.bean.SettlementRecordeDetailsBean;
import com.taihuoniao.fineix.utils.StringFormatUtils;

import java.util.List;

/**
 * Created by Stephen on 2017/1/19 17:59
 * Email: 895745843@qq.com
 */

public class SettlementRecordeDetailsAdapter extends BaseAdapter {
    private LayoutInflater mLayoutInflater;
    private List<SettlementRecordeDetailsBean.RowsBean> mRows;

    public SettlementRecordeDetailsAdapter(Context context) {
        this.mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mRows == null ? 0 : mRows.size();
    }

    @Override
    public Object getItem(int position) {
        return mRows.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = mLayoutInflater.inflate(R.layout.item_listview_allience_settlement_details, null);
            viewHolder.linearLayout1 = (LinearLayout) convertView.findViewById(R.id.linearLayout1);
            viewHolder.linearLayout2 = (LinearLayout) convertView.findViewById(R.id.linearLayout2);
            viewHolder.linearLayout3 = (LinearLayout) convertView.findViewById(R.id.linearLayout3);
            viewHolder.linearLayout4 = (LinearLayout) convertView.findViewById(R.id.linearLayout4);
            viewHolder.linearLayout5 = (LinearLayout) convertView.findViewById(R.id.linearLayout5);
            viewHolder.linearLayout6 = (LinearLayout) convertView.findViewById(R.id.linearLayout6);
            viewHolder.linearLayout7 = (LinearLayout) convertView.findViewById(R.id.linearLayout7);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        SettlementRecordeDetailsBean.RowsBean rowsEntity = mRows.get(position);
        if (rowsEntity != null) {
            ((TextView)viewHolder.linearLayout1.getChildAt(0)).setText(rowsEntity.created_at);
            ((TextView)viewHolder.linearLayout1.getChildAt(1)).setText("");
            ((TextView)viewHolder.linearLayout2.getChildAt(0)).setText("名称");
            ((TextView)viewHolder.linearLayout2.getChildAt(1)).setText(rowsEntity.balance.title);
            ((TextView)viewHolder.linearLayout3.getChildAt(0)).setText("单价");
            ((TextView)viewHolder.linearLayout3.getChildAt(1)).setText(StringFormatUtils.convert2double(rowsEntity.balance.sku_price));
            ((TextView)viewHolder.linearLayout4.getChildAt(0)).setText("收益比率");
            ((TextView)viewHolder.linearLayout4.getChildAt(1)).setText(String.valueOf(Double.valueOf(rowsEntity.balance.commision_percent) * 100D) + "%");
            ((TextView)viewHolder.linearLayout5.getChildAt(0)).setText("收益");
            ((TextView)viewHolder.linearLayout5.getChildAt(1)).setText(StringFormatUtils.convert2double(rowsEntity.balance.unit_price));
            ((TextView)viewHolder.linearLayout6.getChildAt(0)).setText("数量");
            ((TextView)viewHolder.linearLayout6.getChildAt(1)).setText(rowsEntity.balance.quantity+"");
            ((TextView)viewHolder.linearLayout7.getChildAt(0)).setText("合计收益");
            ((TextView)viewHolder.linearLayout7.getChildAt(1)).setText(StringFormatUtils.convert2double(rowsEntity.balance.total_price));
        }
        return convertView;
    }

    public void setList(List<SettlementRecordeDetailsBean.RowsBean> rows) {
        if (mRows == null) {
            mRows = rows;
        } else {
            mRows.clear();
            mRows.addAll(rows);
        }
        notifyDataSetChanged();
    }

    class ViewHolder {
        private LinearLayout linearLayout1;
        private LinearLayout linearLayout2;
        private LinearLayout linearLayout3;
        private LinearLayout linearLayout4;
        private LinearLayout linearLayout5;
        private LinearLayout linearLayout6;
        private LinearLayout linearLayout7;
    }
}
