package com.taihuoniao.fineix.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.beans.CategoryListBean;
import com.taihuoniao.fineix.utils.GlideUtils;
import com.taihuoniao.fineix.view.roundImageView.RoundedImageView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by taihuoniao on 2016/8/22.
 */
public class FindRecyclerAdapter extends RecyclerView.Adapter<FindRecyclerAdapter.VH> {
    private List<CategoryListBean.CategoryListItem> list;
    private EditRecyclerAdapter.ItemClick itemClick;

    public FindRecyclerAdapter(List<CategoryListBean.CategoryListItem> list, EditRecyclerAdapter.ItemClick itemClick) {
        this.list = list;
        this.itemClick = itemClick;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.item_find_recycler, null);
        VH holder = new VH(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final VH holder, final int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClick.click(holder.getAdapterPosition());
            }
        });
//        ImageLoader.getInstance().displayImage(list.get(position).getApp_cover_url(), holder.roundImg);
        GlideUtils.displayImage(list.get(position).getApp_cover_url(), holder.roundImg);
        holder.name.setText(list.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        @Bind(R.id.round_img)
        RoundedImageView roundImg;
        @Bind(R.id.name)
        TextView name;

        public VH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
