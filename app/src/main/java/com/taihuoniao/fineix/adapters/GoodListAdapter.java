package com.taihuoniao.fineix.adapters;

import android.app.Activity;
import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.beans.ProductListBean;
import com.taihuoniao.fineix.beans.SearchBean;
import com.taihuoniao.fineix.product.GoodsDetailActivity;
import com.taihuoniao.fineix.view.SlidingFocusImageView;

import java.util.List;

/**
 * Created by taihuoniao on 2016/5/5.
 */
public class GoodListAdapter extends BaseAdapter {
    private Activity activity;
    private List<ProductListBean> list;
    private List<SearchBean.SearchItem> searchList;

    public GoodListAdapter(Activity activity, List<ProductListBean> list, List<SearchBean.SearchItem> searchList) {
        this.activity = activity;
        this.list = list;
        this.searchList = searchList;
    }

    @Override
    public int getCount() {
        if (list != null) {
            return list.size();
        } else if (searchList != null) {
            return searchList.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (list != null) {
            return list.get(position);
        } else if (searchList != null) {
            return searchList.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(activity, R.layout.item_good_listview, null);
            holder = new ViewHolder();
            holder.slidingFocusImageView = (SlidingFocusImageView) convertView.findViewById(R.id.sfiv);
            holder.nameTv = (TextView) convertView.findViewById(R.id.tv_name);
            holder.priceTv = (TextView) convertView.findViewById(R.id.tv_price);
            holder.img = (ImageView) convertView.findViewById(R.id.img_fiu);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.slidingFocusImageView.setAnimationDuration(500);
        holder.slidingFocusImageView.setFadingEdgeLength(200);
        holder.slidingFocusImageView.setGravity(Gravity.CENTER_VERTICAL);
        if (list != null) {
            holder.nameTv.setText(list.get(position).getTitle());
            holder.priceTv.setText(String.format("¥%s", list.get(position).getSale_price()));
            if (list.get(position).banner_asset.size() > 0) {
                holder.slidingFocusImageView.setAdapter(new SlidingFocusAdapter(holder.slidingFocusImageView, list.get(position).banner_asset, activity));
            }
            holder.slidingFocusImageView.setSelection(Integer.MAX_VALUE / 2);
            switch (list.get(position).getAttrbute()) {
                case "1":
                    holder.img.setImageResource(R.mipmap.product_fiu);
                    break;
                case "2":
                    holder.img.setImageResource(R.mipmap.product_taobao);
                    break;
                case "3":
                    holder.img.setImageResource(R.mipmap.product_tianmao);
                    break;
                case "4":
                    holder.img.setImageResource(R.mipmap.product_jingdong);
                    break;
                default:
                    holder.img.setVisibility(View.INVISIBLE);
                    break;
            }
            ClickListener clickListener = new ClickListener(activity, list, null, position);
            holder.slidingFocusImageView.setOnItemClickListener(clickListener);
            holder.nameTv.setOnClickListener(clickListener);
            holder.priceTv.setOnClickListener(clickListener);
            holder.img.setOnClickListener(clickListener);
        } else if (searchList != null) {
            holder.nameTv.setText(searchList.get(position).getTitle());
            holder.priceTv.setText(String.format("¥%s", searchList.get(position).getSale_price()));
            if (searchList.get(position).getBanners().size() > 0) {
                holder.slidingFocusImageView.setAdapter(new SlidingFocusAdapter(holder.slidingFocusImageView, searchList.get(position).getBanners(), activity));
            }
            holder.slidingFocusImageView.setSelection(Integer.MAX_VALUE / 2);
            switch (searchList.get(position).getAttrbute()) {
                case "1":
                    holder.img.setImageResource(R.mipmap.product_fiu);
                    break;
                case "2":
                    holder.img.setImageResource(R.mipmap.product_taobao);
                    break;
                case "3":
                    holder.img.setImageResource(R.mipmap.product_tianmao);
                    break;
                case "4":
                    holder.img.setImageResource(R.mipmap.product_jingdong);
                    break;
                default:
                    holder.img.setVisibility(View.INVISIBLE);
                    break;
            }
            ClickListener clickListener = new ClickListener(activity, null, searchList, position);
            holder.slidingFocusImageView.setOnItemClickListener(clickListener);
            holder.nameTv.setOnClickListener(clickListener);
            holder.priceTv.setOnClickListener(clickListener);
            holder.img.setOnClickListener(clickListener);
        }
        return convertView;
    }

    static class ClickListener implements View.OnClickListener, AdapterView.OnItemClickListener {
        private Activity activity;
        private int position;
        private List<ProductListBean> list;
        private List<SearchBean.SearchItem> searchList;

        public ClickListener(Activity activity, List<ProductListBean> list, List<SearchBean.SearchItem> searchList, int position) {
            this.activity = activity;
            this.list = list;
            this.searchList = searchList;
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(activity, GoodsDetailActivity.class);
            if (list != null) {
                intent.putExtra("id", list.get(position).get_id());
            } else if (searchList != null) {
                intent.putExtra("id", searchList.get(position).get_id());
            }
            activity.startActivity(intent);
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(activity, GoodsDetailActivity.class);
            if (list != null) {
                intent.putExtra("id", list.get(this.position).get_id());
            } else if (searchList != null) {
                intent.putExtra("id", searchList.get(this.position).get_id());
            }
            activity.startActivity(intent);
        }
    }

    static class ViewHolder {
        SlidingFocusImageView slidingFocusImageView;
        TextView nameTv;
        TextView priceTv;
        ImageView img;
    }
}