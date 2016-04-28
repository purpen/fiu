package com.taihuoniao.fineix.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.beans.CategoryListBean;

import java.util.List;

/**
 * Created by taihuoniao on 2016/4/15.
 */
public class PinRecyclerAdapter extends RecyclerView.Adapter<PinRecyclerAdapter.VH> {
    private Context context;
    private List<CategoryListBean> list;
    private  EditRecyclerAdapter.ItemClick itemClick;
    private DisplayImageOptions options;

    public PinRecyclerAdapter(Context context, List<CategoryListBean> list, EditRecyclerAdapter.ItemClick itemClick1) {
        this.context = context;
        this.list = list;
        itemClick = itemClick1;
        options = new DisplayImageOptions.Builder()
//                .showImageOnLoading(R.mipmap.default_backround)
//                .showImageForEmptyUri(R.mipmap.default_backround)
//                .showImageOnFail(R.mipmap.default_backround)
                .cacheInMemory(true)
                .cacheOnDisk(true).considerExifParams(true)
                .displayer(new RoundedBitmapDisplayer(360)).build();
    }

    @Override
    public PinRecyclerAdapter.VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.item_fragment_pin_recycler, null);
        VH holder = new VH(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(PinRecyclerAdapter.VH holder, final int position) {
        ImageLoader.getInstance().displayImage(list.get(position).getApp_cover_s_url(), holder.img);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClick.click(position);
            }
        });
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
            img = (ImageView) itemView.findViewById(R.id.item_fragment_pin_recycler_img);
            tv = (TextView) itemView.findViewById(R.id.item_fragment_pin_recycler_tv);
        }
    }
}
