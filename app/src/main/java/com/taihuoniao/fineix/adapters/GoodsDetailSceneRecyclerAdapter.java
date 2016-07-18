package com.taihuoniao.fineix.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.beans.ProductAndSceneListBean;
import com.taihuoniao.fineix.utils.DensityUtils;

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
        View view = View.inflate(context, R.layout.item_small_scene, null);
        VH holder = new VH(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final VH holder, final int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClick.click(position);
            }
        });
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(DensityUtils.dp2px(context, 300) * 9 / 16, DensityUtils.dp2px(context, 280));
        holder.itemView.setLayoutParams(layoutParams);
        ImageLoader.getInstance().displayImage(list.get(position).getSight().getCover_url(), holder.imageView);
        holder.titleTv.setText(list.get(position).getSight().getTitle());
        holder.addressTv.setText(list.get(position).getSight().getAddress());
        holder.titleTv.post(new Runnable() {
            @Override
            public void run() {
                LinearLayout.LayoutParams layoutParams1 = (LinearLayout.LayoutParams) holder.frameLayout.getLayoutParams();
                layoutParams1.height = holder.titleTv.getMeasuredHeight();
                layoutParams1.width = holder.titleTv.getMeasuredWidth();
                holder.frameLayout.setLayoutParams(layoutParams1);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size() > 8 ? 8 : list.size();
    }

    public static class VH extends RecyclerView.ViewHolder {
        ImageView imageView;
        FrameLayout frameLayout;
        TextView titleTv;
        TextView addressTv;

        public VH(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.iv_cover);
            frameLayout = (FrameLayout) itemView.findViewById(R.id.item_frame);
            titleTv = (TextView) itemView.findViewById(R.id.tv_title);
            addressTv = (TextView) itemView.findViewById(R.id.tv_desc);
        }
    }
}
