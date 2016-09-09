package com.taihuoniao.fineix.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.beans.CategoryListBean;

import java.util.List;

/**
 * Created by taihuoniao on 2016/7/7.
 */
public class SeconCategoryAdapter extends RecyclerView.Adapter<SeconCategoryAdapter.VH> {
    private List<CategoryListBean.CategoryListItem> list;
    private Context context;
    private EditRecyclerAdapter.ItemClick itemClick;

    public SeconCategoryAdapter(Context context, List<CategoryListBean.CategoryListItem> list,EditRecyclerAdapter.ItemClick itemClick) {
        this.context = context;
        this.list = list;
        this.itemClick = itemClick;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        return new VH(View.inflate(context, R.layout.item_second_categorylist, null));
    }

    @Override
    public void onBindViewHolder(final VH holder, int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClick.click(holder.getAdapterPosition());
            }
        });
        ImageLoader.getInstance().displayImage(list.get(position).getBack_url(), holder.img);
        holder.tv.setText(list.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        ImageView img;
        TextView tv;

        public VH(View itemView) {
            super(itemView);
            img = (ImageView) itemView.findViewById(R.id.item_second_categorylist_img);
            tv = (TextView) itemView.findViewById(R.id.item_second_categorylist_tv);
        }
    }
}
