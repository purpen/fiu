package com.taihuoniao.fineix.adapters;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.beans.ActiveTagsBean;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by taihuoniao on 2016/8/18.
 */
public class UsedLabelAdapter extends BaseAdapter {
    private ActiveTagsBean activeTagsBean;
    private List<String> list;

    public UsedLabelAdapter(ActiveTagsBean activeTagsBean, List<String> list) {
        this.activeTagsBean = activeTagsBean;
        this.list = list;
    }

    //1：点击无效 0：可以点击
    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return 1;
        }
        if (activeTagsBean == null) {
            return 0;
        }
        if (position == activeTagsBean.getData().getItems().size() + 1) {
            return 1;
        }
        return 0;
    }

    @Override
    public int getCount() {
        if (activeTagsBean == null) {
            if (list.size() >= 10) {
                return 11;
            }
            return list.size() + 1;
        }
        if (list.size() >= 10) {
            return 12 + activeTagsBean.getData().getItems().size();
        }
        return list.size() + 2 + activeTagsBean.getData().getItems().size();
    }

    @Override
    public String getItem(int position) {
        if (getItemViewType(position) == 1) {
            return null;
        }
        if (activeTagsBean == null) {
            return "#" + list.get(position - 1) + " ";
        }
        if (position  <= activeTagsBean.getData().getItems().size()) {
            return "#" + activeTagsBean.getData().getItems().get(position - 1).get(0) + " ";
        }
        return "#" + list.get(position - 2 - activeTagsBean.getData().getItems().size()) + " ";
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(parent.getContext(), R.layout.item_add_label, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (activeTagsBean == null) {
            if (position == 0) {
                holder.name.setTextColor(parent.getResources().getColor(R.color.color_999));
                holder.name.setText("最近使用的标签");
            } else {
                holder.name.setTextColor(parent.getResources().getColor(R.color.title_black));
                holder.name.setText("#" + list.get(position - 1) + " ");
            }
        } else {
            if (position == 0) {
                holder.name.setTextColor(parent.getResources().getColor(R.color.color_999));
                holder.name.setText("推荐标签");
            } else if (position == activeTagsBean.getData().getItems().size() + 1) {
                holder.name.setTextColor(parent.getResources().getColor(R.color.color_999));
                holder.name.setText("最近使用的标签");
            } else if (position <= activeTagsBean.getData().getItems().size()) {
                holder.name.setTextColor(parent.getResources().getColor(R.color.title_black));
                holder.name.setText("#" + activeTagsBean.getData().getItems().get(position - 1).get(0) + " ");
            } else {
                holder.name.setTextColor(parent.getResources().getColor(R.color.title_black));
                holder.name.setText("#" + list.get(position - 2 - activeTagsBean.getData().getItems().size()) + " ");
            }
        }
        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.name)
        TextView name;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
