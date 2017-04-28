package com.taihuoniao.fineix.personal.alliance.adpter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.personal.alliance.bean.WithDrawRecordBean;
import com.taihuoniao.fineix.utils.StringFormatUtils;

import java.util.List;

/**
 * Created by Stephen on 2017/1/19 17:59
 * Email: 895745843@qq.com
 */

public class WithDrawRecordAdapter extends BaseAdapter {
    private LayoutInflater mLayoutInflater;
    private List<WithDrawRecordBean.RowsEntity> mRows;

    public WithDrawRecordAdapter(Context context) {
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
            convertView = mLayoutInflater.inflate(R.layout.item_listview_allience_withdrawrecorde, null);
            viewHolder.textView1 = (TextView) convertView.findViewById(R.id.textView1);
            viewHolder.textView2 = (TextView) convertView.findViewById(R.id.textView2);
            viewHolder.textView3 = (TextView) convertView.findViewById(R.id.textView3);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        WithDrawRecordBean.RowsEntity rowsEntity = mRows.get(position);
        if (rowsEntity != null) {
            viewHolder.textView1.setText(getStatus(Integer.valueOf(rowsEntity.getStatus()), viewHolder.textView1));
            viewHolder.textView2.setText(rowsEntity.getCreated_at());
            viewHolder.textView3.setText(String.format("- %s", StringFormatUtils.convert2double(rowsEntity.getAmount())));
        }
        return convertView;
    }

    public void setList(List<WithDrawRecordBean.RowsEntity> rows) {
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

    private String getStatus(int flag, TextView textView){
        switch (flag) {
            case 0:
                textView.setTextColor(Color.parseColor("#FF5550"));
                return "提现失败";
            case 1:
                textView.setTextColor(Color.parseColor("#262726"));
                return "等待审核";
            case 2:
                textView.setTextColor(Color.parseColor("#262726"));
                return "审核中";
            case 5:
                textView.setTextColor(Color.parseColor("#262726"));
                return "提现成功";
        }
        textView.setTextColor(Color.parseColor("#262726"));
        return "";
    }
}
