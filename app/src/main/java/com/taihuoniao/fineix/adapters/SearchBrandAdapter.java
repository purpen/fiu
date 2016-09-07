package com.taihuoniao.fineix.adapters;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.beans.SearchBean;
import com.taihuoniao.fineix.view.roundImageView.RoundedImageView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by taihuoniao on 2016/8/17.
 */
public class SearchBrandAdapter extends BaseAdapter {
    private List<SearchBean.Data.SearchItem> brandList;

    public SearchBrandAdapter(List<SearchBean.Data.SearchItem> brandList) {
        this.brandList = brandList;
    }

    @Override
    public int getCount() {
        return brandList.size();
    }

    @Override
    public Object getItem(int position) {
        return brandList.get(position);
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
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ImageLoader.getInstance().displayImage(brandList.get(position).getCover_url(),holder.brandImg);
        holder.name.setText(brandList.get(position).getTitle());
        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.brand_img)
        RoundedImageView brandImg;
        @Bind(R.id.name)
        TextView name;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
