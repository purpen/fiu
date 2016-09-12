package com.taihuoniao.fineix.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.BackgroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
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
    private DisplayImageOptions options;

    public JingQingjingRecyclerAdapter(Context context, List<QingJingListBean.QingJingItem> list, EditRecyclerAdapter.ItemClick itemClick, int parentHeight) {
        this.context = context;
        this.list = list;
        this.itemClick = itemClick;
        this.parentHeight = parentHeight > 0 ? parentHeight : DensityUtils.dp2px(context, 267);
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.default_background_750_1334)
                .showImageForEmptyUri(R.mipmap.default_background_750_1334)
                .showImageOnFail(R.mipmap.default_background_750_1334)
                .cacheInMemory(true)
                .cacheOnDisk(true).considerExifParams(true)
                .build();
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.item_qingjing_list, null);
        VH vh = new VH(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(final VH holder, int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClick.click(holder.getAdapterPosition());
            }
        });
        ViewGroup.LayoutParams lp = holder.container.getLayoutParams();
        lp.height = parentHeight;
        lp.width = lp.height * 9 / 16;
        holder.container.setLayoutParams(lp);
        ImageLoader.getInstance().displayImage(list.get(position).getCover_url(), holder.backgroundImg,options);
        SpannableStringBuilder style=new SpannableStringBuilder(list.get(position).getTitle());
        BackgroundColorSpan backgroundColorSpan = new BackgroundColorSpan(context.getResources().getColor(R.color.black));
        style.setSpan(backgroundColorSpan, 0, list.get(position).getTitle().length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        holder.title.setText(style);
        holder.addressTv.setText(list.get(position).getAddress());
    }


    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        RelativeLayout container;
        ImageView backgroundImg;
        TextView addressTv;
        TextView title;

        public VH(View itemView) {
            super(itemView);
            container = (RelativeLayout) itemView.findViewById(R.id.item_qingjing_list_container);
            backgroundImg = (ImageView) itemView.findViewById(R.id.item_qingjing_list_background);
            addressTv = (TextView) itemView.findViewById(R.id.item_qingjing_list_address);
            title = (TextView) itemView.findViewById(R.id.item_qingjing_list_title);
        }
    }
}
