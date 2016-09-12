package com.taihuoniao.fineix.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.beans.CategoryListBean;
import com.taihuoniao.fineix.view.roundImageView.RoundedImageView;

import java.util.List;

/**
 * Created by taihuoniao on 2016/4/15.
 */
public class PinRecyclerAdapter extends RecyclerView.Adapter<PinRecyclerAdapter.VH> {
    private Context context;
    private List<CategoryListBean.CategoryListItem> list;
    private EditRecyclerAdapter.ItemClick itemClick;
    private DisplayImageOptions options;

    public PinRecyclerAdapter(Context context, List<CategoryListBean.CategoryListItem> list, EditRecyclerAdapter.ItemClick itemClick1) {
        this.context = context;
        this.list = list;
        itemClick = itemClick1;
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.default_background_750_1334)
                .showImageForEmptyUri(R.mipmap.default_background_750_1334)
                .showImageOnFail(R.mipmap.default_background_750_1334)
                .cacheInMemory(true)
                .cacheOnDisk(true).considerExifParams(true)
                .build();
    }

    @Override
    public PinRecyclerAdapter.VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.item_fragment_pin_recycler, null);
        VH holder = new VH(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final PinRecyclerAdapter.VH holder, int position) {
        ImageLoader.getInstance().displayImage(list.get(position).getApp_cover_url(), holder.img, options);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClick.click(holder.getAdapterPosition());
            }
        });
        holder.tv.setText(list.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        RoundedImageView img;
        TextView tv;

        public VH(View itemView) {
            super(itemView);
            img = (RoundedImageView) itemView.findViewById(R.id.item_fragment_pin_recycler_img);
            tv = (TextView) itemView.findViewById(R.id.item_fragment_pin_recycler_tv);
        }
    }
}
