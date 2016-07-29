package com.taihuoniao.fineix.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.beans.RelationProductsBean;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by taihuoniao on 2016/2/23.
 */
public class GoodsDetailsGridViewAdapter extends RecyclerView.Adapter<GoodsDetailsGridViewAdapter.VH> {
    private Context context;
    private List<RelationProductsBean> relationProductsBeanList;
    private EditRecyclerAdapter.ItemClick itemClick;
    public GoodsDetailsGridViewAdapter(Context context, List<RelationProductsBean> relationProductsBeanList,EditRecyclerAdapter.ItemClick itemClick) {
        this.context = context;
        this.relationProductsBeanList = relationProductsBeanList;
        this.itemClick = itemClick;
    }


    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.item_goodsdetails_gridview, null);
        VH holder = new VH(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(VH holder, final int position) {
        ImageLoader.getInstance().displayImage(relationProductsBeanList.get(position).getCover_url(), holder.img);
        holder.titleTv.setText(relationProductsBeanList.get(position).getTitle());
        holder.priceTv.setText("¥" + relationProductsBeanList.get(position).getSale_price());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClick.click(position);
            }
        });
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return relationProductsBeanList.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        @Bind(R.id.item_goodsdetails_gridview_img)
        ImageView img;
        @Bind(R.id.item_goodsdetails_gridview_title)
        TextView titleTv;
        @Bind(R.id.item_goodsdetails_gridview_price)
        TextView priceTv;

        public VH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
