package com.taihuoniao.fineix.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.beans.ProductListBean;

import java.util.List;

/**
 * Created by taihuoniao on 2016/5/3.
 */
public class GoodsDetailRecommendRecyclerAdapter extends RecyclerView.Adapter<GoodsDetailRecommendRecyclerAdapter.VH> {
    private Context context;
    private List<ProductListBean> list;
    private EditRecyclerAdapter.ItemClick itemClick;
    private int itemHeight = 0;

    public GoodsDetailRecommendRecyclerAdapter(Context context, List<ProductListBean> list, EditRecyclerAdapter.ItemClick itemClick) {
        this.context = context;
        this.list = list;
        this.itemClick = itemClick;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.item_goodsdetail_recommend, null);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(VH holder, final int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClick.click(position);
            }
        });
        ImageLoader.getInstance().displayImage(list.get(position).getCover_url(), holder.img);
        holder.title.setText(list.get(position).getTitle());
        holder.price.setText(String.format("¥ %s", list.get(position).getSale_price()));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public int getItemHeight() {
        return itemHeight;
    }

    public static class VH extends RecyclerView.ViewHolder {
        ImageView img;
        TextView title;
        TextView price;

        public VH(View itemView) {
            super(itemView);
            img = (ImageView) itemView.findViewById(R.id.item_goodsdetail_recommend_img);
            title = (TextView) itemView.findViewById(R.id.item_goodsdetail_recommend_title);
            price = (TextView) itemView.findViewById(R.id.item_goodsdetail_recommend_price);
        }
    }
}
