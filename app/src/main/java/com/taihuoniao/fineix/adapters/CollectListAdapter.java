package com.taihuoniao.fineix.adapters;

import android.app.Activity;
import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.beans.CollectionItem;
import com.taihuoniao.fineix.product.GoodsDetailActivity;
import com.taihuoniao.fineix.qingjingOrSceneDetails.SceneDetailActivity;
import com.taihuoniao.fineix.view.SlidingFocusImageView;

import java.util.List;

public class CollectListAdapter extends CommonBaseAdapter<CollectionItem> {
    public CollectListAdapter(List<CollectionItem> list, Activity activity) {
        super(list, activity);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        CollectionItem item = list.get(position);
        final ViewHolder holder;
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
        holder.slidingFocusImageView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int posi, long id) {
                if (list != null) {
                    list.get(position).pos = posi;
                }
                CollectSlidingAdapter CollectSlidingAdapter = (CollectSlidingAdapter) holder.slidingFocusImageView.getTag();
                CollectSlidingAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        if (list != null) {
            holder.nameTv.setText(item.scene_product.title);
            holder.priceTv.setText(String.format("¥%s", item.scene_product.sale_price));
            if (item.scene_product.banner_asset.size() > 0) {
//                holder.slidingFocusImageView.setAdapter(new SlidingFocusAdapter<String>(holder.slidingFocusImageView, list.get(position).getSights(), list.get(position).getBanner_asset(), activity));
                CollectSlidingAdapter productSlidingAdapter1 = new CollectSlidingAdapter(activity, item);
                holder.slidingFocusImageView.setAdapter(productSlidingAdapter1);
                holder.slidingFocusImageView.setTag(productSlidingAdapter1);
                if (item.scene_product.sights != null && item.scene_product.sights.size() > 0 && item.scene_product.sights.get(0) != null) {
                    switch (item.scene_product.sights.size()) {
                        case 2:
                            holder.slidingFocusImageView.setSelection(Integer.MAX_VALUE / 2);
                            break;
                        case 4:
                            holder.slidingFocusImageView.setSelection(Integer.MAX_VALUE / 2 + 2);
                            break;
                        default:
                            holder.slidingFocusImageView.setSelection(Integer.MAX_VALUE / 2 + 1);
                            break;
                    }
                } else {
                    holder.slidingFocusImageView.setSelection(Integer.MAX_VALUE / 2 - 1);
                }
            }
//            holder.slidingFocusImageView.setSelection(0);
            switch (item.scene_product.attrbute) {
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
        }
        return convertView;
    }

    static class ClickListener implements View.OnClickListener, AdapterView.OnItemClickListener {
        private Activity activity;
        private int position;
        private List<CollectionItem> list;

        public ClickListener(Activity activity, List<CollectionItem> list, int position) {
            this.activity = activity;
            this.list = list;
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(activity, GoodsDetailActivity.class);
            if (list != null) {
                intent.putExtra("id", list.get(position)._id);
            }
            activity.startActivity(intent);
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(activity, GoodsDetailActivity.class);
            if (list != null) {
                if (list.get(this.position).scene_product.sights != null && list.get(this.position).scene_product.sights.size() > 0 && list.get(this.position).scene_product.sights.get(0) != null) {
                    if (position % (1 + list.get(this.position).scene_product.banner_asset.size()) == list.get(this.position).scene_product.banner_asset.size()) {
                        Intent intent1 = new Intent(activity, SceneDetailActivity.class);
                        intent1.putExtra("id", list.get(this.position).scene_product.sights.get(0).id);
                        activity.startActivity(intent1);
                        return;
                    } else {
                        intent.putExtra("id", list.get(this.position)._id);
                    }
                } else {
                    intent.putExtra("id", list.get(this.position)._id);
                }

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
