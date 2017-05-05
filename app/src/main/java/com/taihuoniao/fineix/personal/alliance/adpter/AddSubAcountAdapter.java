package com.taihuoniao.fineix.personal.alliance.adpter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.personal.alliance.bean.SubAccountListBean;
import com.taihuoniao.fineix.utils.StringFormatUtils;
import com.taihuoniao.fineix.utils.TypeConversionUtils;

import java.util.List;

/**
 * Created by Stephen on 2017/1/19 17:59
 * Email: 895745843@qq.com
 */

public class AddSubAcountAdapter extends BaseAdapter {
    private LayoutInflater mLayoutInflater;

    public List<SubAccountListBean.RowsEntity> getmRows() {
        return mRows;
    }

    public void setmRows(List<SubAccountListBean.RowsEntity> mRows) {
        this.mRows = mRows;
    }

    private List<SubAccountListBean.RowsEntity> mRows;

    public AddSubAcountAdapter(Context context) {
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
            convertView = mLayoutInflater.inflate(R.layout.item_listview_add_account, null);
            viewHolder.textView1 = (TextView) convertView.findViewById(R.id.textView1);
            viewHolder.textView2 = (TextView) convertView.findViewById(R.id.textView2);
            viewHolder.textView3 = (TextView) convertView.findViewById(R.id.textView3);
            viewHolder.textView5 = (TextView) convertView.findViewById(R.id.textView5);
            viewHolder.textView6 = (TextView) convertView.findViewById(R.id.textView6);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        SubAccountListBean.RowsEntity rowsEntity = mRows.get(position);
        if (rowsEntity != null) {
            viewHolder.textView1.setText(rowsEntity.getUsername());
            viewHolder.textView2.setText(rowsEntity.getAccount());
            viewHolder.textView3.setText(StringFormatUtils.convert2double(rowsEntity.getAmount()));
            viewHolder.textView5.setText("佣金分成");
            int i = (int) (TypeConversionUtils.StringConvertDouble(rowsEntity.getAddition()) * 100);
            viewHolder.textView6.setText(String.valueOf( i + "(%)"));
        }
        return convertView;
    }

    public void setList(List<SubAccountListBean.RowsEntity> rows) {
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
        private TextView textView5;
        private TextView textView6;
    }
}
