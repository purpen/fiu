package com.taihuoniao.fineix.adapters;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.beans.SearchBean;
import com.taihuoniao.fineix.utils.GlideUtils;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by taihuoniao on 2016/8/24.
 */
public class SearchSubjectAdapter extends BaseAdapter {
    private List<SearchBean.Data.SearchItem> list;

    public SearchSubjectAdapter(List<SearchBean.Data.SearchItem> list) {
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
            convertView = View.inflate(parent.getContext(), R.layout.item_search_subject, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        GlideUtils.displayImage(list.get(position).getCover_url(),holder.subjectImg);
        holder.subjectName.setText(list.get(position).getTitle());
        holder.subjectName2.setText(list.get(position).getShort_title());
        switch (list.get(position).getType()) {
            case 1:
                holder.subjectLabel.setImageResource(R.mipmap.subject_wenzhang_big);
                break;
            case 2:
                holder.subjectLabel.setImageResource(R.mipmap.subject_huodong_big);
                break;
            case 3:
                holder.subjectLabel.setImageResource(R.mipmap.subject_cuxiao_big);
                break;
            case 4:
                holder.subjectLabel.setImageResource(R.mipmap.subject_xinpin_big);
                break;
        }
        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.subject_img)
        ImageView subjectImg;
        @Bind(R.id.subject_name)
        TextView subjectName;
        @Bind(R.id.subject_name2)
        TextView subjectName2;
        @Bind(R.id.subject_label)
        ImageView subjectLabel;
        @Bind(R.id.relative)
        RelativeLayout relative;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
