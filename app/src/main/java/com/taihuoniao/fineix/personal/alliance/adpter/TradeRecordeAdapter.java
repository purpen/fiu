package com.taihuoniao.fineix.personal.alliance.adpter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.personal.alliance.bean.TradeRecordBean;
import com.taihuoniao.fineix.utils.StringFormatUtils;

import java.util.List;

/**
 * Created by Stephen on 2017/1/19 17:59
 * Email: 895745843@qq.com
 */

public class TradeRecordeAdapter extends BaseAdapter {
    private LayoutInflater mLayoutInflater;
    private List<TradeRecordBean.RowsEntity> mRows;

    public TradeRecordeAdapter(Context context) {
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
            convertView = mLayoutInflater.inflate(R.layout.item_listview_allience_traderecorde, null);
            viewHolder.textView1 = (TextView) convertView.findViewById(R.id.textView1);
            viewHolder.textView2 = (TextView) convertView.findViewById(R.id.textView2);
            viewHolder.textView3 = (TextView) convertView.findViewById(R.id.textView3);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        TradeRecordBean.RowsEntity rowsEntity = mRows.get(position);
        if (rowsEntity != null) {
            viewHolder.textView1.setText(StringFormatUtils.convert2double(rowsEntity.getTotal_price()));
            viewHolder.textView2.setText(rowsEntity.getCreated_at());
            viewHolder.textView3.setText(rowsEntity.getStatus_label());
        }
        return convertView;
    }

    public void setList(List<TradeRecordBean.RowsEntity> rows) {
        if (mRows == null) {
            mRows = rows;
        } else {
            mRows.clear();
            mRows.addAll(rows);
        }
        notifyDataSetChanged();
    }

    class ViewHolder {
        private TextView textView1;
        private TextView textView2;
        private TextView textView3;
    }
}
