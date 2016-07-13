package com.taihuoniao.fineix.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.product.AllBrandsActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by taihuoniao on 2016/7/12.
 */
public class CharBrandAdapter extends BaseAdapter {
    private Context context;
    private List<String> charList;

    public CharBrandAdapter(Context context, List<AllBrandsActivity.Item> itemList) {
        this.context = context;
        charList = new ArrayList<>();
        for (AllBrandsActivity.Item item : itemList) {
            if (item.type == AllBrandsActivity.Item.SECTION) {
                charList.add(item.text);
            }
        }
    }

    @Override
    public int getCount() {
        return charList.size();
    }

    @Override
    public String getItem(int position) {
        return charList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_char_list, null);
            holder = new ViewHolder();
            holder.textView = (TextView) convertView.findViewById(R.id.item_char_list_tv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.textView.setText(charList.get(position));
        return convertView;
    }

    static class ViewHolder {
        TextView textView;
    }
}
