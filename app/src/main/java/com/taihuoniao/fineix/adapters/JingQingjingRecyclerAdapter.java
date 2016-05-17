package com.taihuoniao.fineix.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.beans.QingJingListBean;
import com.taihuoniao.fineix.utils.DensityUtils;

import java.util.List;

/**
 * Created by taihuoniao on 2016/4/21.
 */
public class JingQingjingRecyclerAdapter extends RecyclerView.Adapter<JingQingjingRecyclerAdapter.VH> {
    private Context context;
    private List<QingJingListBean.QingJingItem> list;
    private EditRecyclerAdapter.ItemClick itemClick;
    private int parentHeight;

    public JingQingjingRecyclerAdapter(Context context, List<QingJingListBean.QingJingItem> list, EditRecyclerAdapter.ItemClick itemClick, int parentHeight) {
        this.context = context;
        this.list = list;
        this.itemClick = itemClick;
        this.parentHeight = parentHeight > 0 ? parentHeight : DensityUtils.dp2px(context, 267);
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.item_qingjing_list, null);
        VH vh = new VH(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(VH holder, final int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClick.click(position);
            }
        });
        ViewGroup.LayoutParams lp = holder.backgroundImg.getLayoutParams();
        lp.height = parentHeight;
        lp.width = lp.height * 9 / 16;
        holder.backgroundImg.setLayoutParams(lp);
        ImageLoader.getInstance().displayImage(list.get(position).getCover_url(), holder.backgroundImg);
        holder.title.setText(list.get(position).getTitle());
        holder.addressTv.setText(list.get(position).getAddress());
    }


    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        ImageView backgroundImg;
        TextView addressTv;
        TextView title;

        public VH(View itemView) {
            super(itemView);
            backgroundImg = (ImageView) itemView.findViewById(R.id.item_qingjing_list_background);
            addressTv = (TextView) itemView.findViewById(R.id.item_qingjing_list_address);
            title = (TextView) itemView.findViewById(R.id.item_qingjing_list_title);
        }
    }
}