package com.taihuoniao.fineix.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.beans.ProductAndSceneListBean;

import java.util.List;

/**
 * Created by taihuoniao on 2016/5/3.
 */
public class GoodsDetailSceneRecyclerAdapter extends RecyclerView.Adapter<GoodsDetailSceneRecyclerAdapter.VH> {
    private Context context;
    private List<ProductAndSceneListBean.ProductAndSceneItem> list;
    private EditRecyclerAdapter.ItemClick itemClick;

    public GoodsDetailSceneRecyclerAdapter(Context context, List<ProductAndSceneListBean.ProductAndSceneItem> list, EditRecyclerAdapter.ItemClick itemClick) {
        this.context = context;
        this.list = list;
        this.itemClick = itemClick;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.item_horizontal_address, null);
        VH holder = new VH(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(VH holder, final int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClick.click(position);
            }
        });
        holder.textView.setText(list.get(position).getSight().getTitle());
    }

    @Override
    public int getItemCount() {
        return list.size() > 8 ? 8 : list.size();
    }

    public static class VH extends RecyclerView.ViewHolder {
        TextView textView;

        public VH(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.item_horizontal_tv);
        }
    }
}
