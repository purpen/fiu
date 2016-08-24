package com.taihuoniao.fineix.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.beans.FirstProductBean;
import com.taihuoniao.fineix.view.roundImageView.RoundedImageView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by taihuoniao on 2016/8/23.
 * 最新好货推荐
 */
public class FirstProductAdapter extends RecyclerView.Adapter<FirstProductAdapter.VH> {

    private List<FirstProductBean.DataBean.RowsBean> list;
    private EditRecyclerAdapter.ItemClick itemClick;

    public FirstProductAdapter(List<FirstProductBean.DataBean.RowsBean> list, EditRecyclerAdapter.ItemClick itemClick) {
        this.list = list;
        this.itemClick = itemClick;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.item_first_product, null);
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
        ImageLoader.getInstance().displayImage(list.get(position).getCover_url(), holder.backgroundImg);
//        ImageLoader.getInstance().displayImage(list.get(position).getBrand_cover_url(), holder.brandImg);
        holder.name.setText(list.get(position).getTitle());
        holder.price.setText("¥" + list.get(position).getSale_price());
    }

    @Override
    public int getItemCount() {
        return list.size() >= 8 ? 8 : list.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        @Bind(R.id.background_img)
        ImageView backgroundImg;
        @Bind(R.id.brand_img)
        RoundedImageView brandImg;
        @Bind(R.id.price)
        TextView price;
        @Bind(R.id.name)
        TextView name;

        public VH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
