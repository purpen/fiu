package com.taihuoniao.fineix.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.utils.GPUImageFilterTools;

import jp.co.cyberagent.android.gpuimage.GPUImageFilter;

/**
 * Created by taihuoniao on 2016/3/21.
 */
public class EditRecyclerAdapter extends RecyclerView.Adapter<EditRecyclerAdapter.VH> {
    private Context context;
    private GPUImageFilterTools.FilterList filterList;
    private GPUImageFilterTools.OnGpuImageFilterChosenListener listener;
    private int lastClick = -1;//上次点击的item
    private ItemClick itemClick;

    public EditRecyclerAdapter(Context context, GPUImageFilterTools.OnGpuImageFilterChosenListener listener,ItemClick itemClick) {
        this.context = context;
        this.filterList = GPUImageFilterTools.getList();
        this.listener = listener;
        this.itemClick = itemClick;
    }


    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.item_edit_recycler, null);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(final VH holder,  int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.getAdapterPosition() != lastClick) {
                    filterList.selectFilter(holder.getAdapterPosition());
                    GPUImageFilterTools.FilterType filterType = filterList.filters.get(holder.getAdapterPosition());
                    GPUImageFilter gpuImageFilter = GPUImageFilterTools.createFilterForType(context, filterType);
                    listener.onGpuImageFilterChosenListener(gpuImageFilter, holder.getAdapterPosition());
                    notifyDataSetChanged();
                    lastClick = holder.getAdapterPosition();
                } else {
                    itemClick.click(holder.getAdapterPosition());
                }
            }
        });
        switch (filterList.getName(position)) {
            case "原图":
                holder.imageView.setImageResource(R.mipmap.yuantu);
                break;
            case "摩卡":
                holder.imageView.setImageResource(R.mipmap.moka);
                break;
            case "暮光":
                holder.imageView.setImageResource(R.mipmap.muguang);
                break;
            case "候鸟":
                holder.imageView.setImageResource(R.mipmap.houniao);
                break;
            case "戏剧":
                holder.imageView.setImageResource(R.mipmap.xiju);
                break;
            case "夏日":
                holder.imageView.setImageResource(R.mipmap.xiari);
                break;
            case "都市":
                holder.imageView.setImageResource(R.mipmap.dushi);
                break;
            case "佳人":
                holder.imageView.setImageResource(R.mipmap.jiaren);
                break;
            case "摩登":
                holder.imageView.setImageResource(R.mipmap.modeng);
                break;
            case "流年":
                holder.imageView.setImageResource(R.mipmap.liunian);
                break;
            case "日光":
                holder.imageView.setImageResource(R.mipmap.riguang);
                break;
            case "午茶":
                holder.imageView.setImageResource(R.mipmap.wucha);
                break;
            default:
                holder.imageView.setImageResource(R.mipmap.yuantu);
                break;
        }
        holder.textView.setText(filterList.getName(position));
        if (filterList.isSelected(position)) {
            holder.redTv.setVisibility(View.VISIBLE);
            holder.textView.setTextColor(context.getResources().getColor(R.color.yellow_bd8913));
        } else {
            holder.redTv.setVisibility(View.GONE);
            holder.textView.setTextColor(context.getResources().getColor(R.color.white));
        }
    }

    @Override
    public int getItemCount() {
        return filterList.size();
    }

    public class VH extends RecyclerView.ViewHolder {
        public ImageView imageView;
        //        public GPUImageView imageView;
        public TextView textView;
        public TextView redTv;

        public VH(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.item_edit_recycler_filterimg);
            textView = (TextView) itemView.findViewById(R.id.item_edit_recycler_textview);
            redTv = (TextView) itemView.findViewById(R.id.item_edit_recycler_red);

        }
    }

    public interface ItemClick {
        void click(int postion);
    }
    public interface FilterClick{
        void filterClick(int position);
    }
}
