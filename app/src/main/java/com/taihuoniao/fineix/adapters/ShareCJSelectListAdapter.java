package com.taihuoniao.fineix.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.beans.SearchBean;

import java.util.List;

/**
 * Created by taihuoniao on 2016/5/26.
 */
public class ShareCJSelectListAdapter extends BaseAdapter {
    private Context context;
    private List<SearchBean.Data.SearchItem> list;

    public ShareCJSelectListAdapter(Context context, List<SearchBean.Data.SearchItem> list) {
        this.context = context;
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
            convertView = View.inflate(context, R.layout.item_sharecj_listview, null);
            holder = new ViewHolder();
            holder.title = (TextView) convertView.findViewById(R.id.item_sharecj_listview_title);
            holder.des = (TextView) convertView.findViewById(R.id.item_sharecj_listview_des);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.title.setText(list.get(position).getTitle());
        holder.des.setText(list.get(position).getDes());
        return convertView;
    }

    static class ViewHolder {
        TextView title;
        TextView des;
    }
}
