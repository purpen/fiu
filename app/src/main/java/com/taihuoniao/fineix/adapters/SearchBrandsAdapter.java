package com.taihuoniao.fineix.adapters;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.beans.SearchBean;
import com.taihuoniao.fineix.view.roundImageView.RoundedImageView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by taihuoniao on 2016/8/24.
 */
public class SearchBrandsAdapter extends BaseAdapter {
    private List<SearchBean.Data.SearchItem> list;

    public SearchBrandsAdapter(List<SearchBean.Data.SearchItem> list) {
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
            convertView = View.inflate(parent.getContext(), R.layout.item_search_brand, null);
            holder = new ViewHolder(convertView);
            holder.arrow.setVisibility(View.GONE);
            holder.content.setVisibility(View.VISIBLE);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ImageLoader.getInstance().displayImage(list.get(position).getCover_url(), holder.brandImg);
        holder.name.setText(list.get(position).getTitle());
        holder.content.setText(list.get(position).getContent());
        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.brand_img)
        RoundedImageView brandImg;
        @Bind(R.id.arrow)
        ImageView arrow;
        @Bind(R.id.name)
        TextView name;
        @Bind(R.id.content)
        TextView content;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
