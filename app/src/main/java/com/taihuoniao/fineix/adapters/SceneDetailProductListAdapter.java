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
import com.taihuoniao.fineix.beans.ProductAndSceneListBean;
import com.taihuoniao.fineix.product.GoodsDetailActivity;
import com.taihuoniao.fineix.view.SlidingFocusImageView;

import java.util.List;

/**
 * Created by taihuoniao on 2016/5/6.
 */
public class SceneDetailProductListAdapter extends BaseAdapter {
    private Activity activity;
    private List<ProductAndSceneListBean.ProductAndSceneItem> list;

    public SceneDetailProductListAdapter(Activity activity, List<ProductAndSceneListBean.ProductAndSceneItem> list) {
        this.activity = activity;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
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
        holder.nameTv.setText(list.get(position).getProduct().getTitle());
        holder.priceTv.setText(String.format("¥%s", list.get(position).getProduct().getSale_price()));
        if (list.get(position).getProduct().getBanner_asset().size() > 0) {
            holder.slidingFocusImageView.setAdapter(new SlidingFocusAdapter(holder.slidingFocusImageView, list.get(position).getProduct().getBanner_asset(), activity));
        }
        holder.slidingFocusImageView.setSelection(0);
        switch (list.get(position).getProduct().getAttrbute()) {
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
        ClickListener clickListener = new ClickListener(activity, list, position);
        holder.slidingFocusImageView.setOnItemClickListener(clickListener);
        holder.nameTv.setOnClickListener(clickListener);
        holder.priceTv.setOnClickListener(clickListener);
        holder.img.setOnClickListener(clickListener);
        return convertView;
    }

    static class ClickListener implements View.OnClickListener, AdapterView.OnItemClickListener {
        private Activity activity;
        private int position;
        private List<ProductAndSceneListBean.ProductAndSceneItem> list;

        public ClickListener(Activity activity, List<ProductAndSceneListBean.ProductAndSceneItem> list, int position) {
            this.activity = activity;
            this.list = list;
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(activity, GoodsDetailActivity.class);
            intent.putExtra("id", list.get(position).getProduct().get_id());
            activity.startActivity(intent);
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(activity, GoodsDetailActivity.class);
            intent.putExtra("id", list.get(this.position).getProduct().get_id());
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
