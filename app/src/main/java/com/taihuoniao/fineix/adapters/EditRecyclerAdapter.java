package com.taihuoniao.fineix.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.utils.GPUImageFilterTools;

/**
 * Created by taihuoniao on 2016/3/21.
 */
public class EditRecyclerAdapter extends RecyclerView.Adapter<EditRecyclerAdapter.VH> {
    //    private Uri imageUri;
    private Context context;
    private GPUImageFilterTools.FilterList filterList;
    private GPUImageFilterTools.OnGpuImageFilterChosenListener listener;
    private int lastClick = -1;//上次点击的item
    private ItemClick itemClick;

    public EditRecyclerAdapter(Context context, GPUImageFilterTools.OnGpuImageFilterChosenListener listener, ItemClick itemClick) {
//        this.imageUri = imageUri;
        this.context = context;
        this.filterList = GPUImageFilterTools.getList();
        this.listener = listener;
        this.itemClick = itemClick;
    }


    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.item_edit_recycler, null);
        //小图的滤镜预览效果
        VH holder = new VH(view);
//        holder.imageView.setScaleType(GPUImage.ScaleType.CENTER_CROP);
//        ImageUtils.asyncLoadImage(context, imageUri, new ImageUtils.LoadImageCallback() {
//            @Override
//            public void callback(Bitmap result) {
//                holder.imageView.setImage(result);
//            }
//        });
        return holder;
    }

    @Override
    public void onBindViewHolder(final VH holder, int position) {
//        holder.imageView.setImage(imageUri);
//        final GPUImageFilter filter = GPUImageFilterTools.createFilterForType(context, filterList.filters.get(position));
//        final GPUImageFilterTools.FilterAdjuster filterAdjuster = new GPUImageFilterTools.FilterAdjuster(filter);
//        holder.imageView.setFilter(filter);
//        if (filterAdjuster.canAdjust()) {
//            filterAdjuster.adjust(50);
//        }
//        holder.imageView.requestRender();
        holder.imageView.setImageResource(R.mipmap.ic_launcher);
        holder.textView.setText(filterList.getName(position));
        if (filterList.isSelected(position)) {
            holder.redTv.setVisibility(View.VISIBLE);
            holder.textView.setTextColor(context.getResources().getColor(R.color.red));
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
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getPosition() == lastClick) {
                        GPUImageFilterTools.FilterAdjuster filterAdjuster = new GPUImageFilterTools.FilterAdjuster(GPUImageFilterTools.createFilterForType(context, filterList.filters.get(getPosition())));
                        if (filterAdjuster.canAdjust())
                            itemClick.click(getPosition());
                    } else {
                        filterList.selectFilter(getPosition());
                        listener.onGpuImageFilterChosenListener(GPUImageFilterTools.createFilterForType(context, filterList.filters.get(getPosition())));
                        notifyDataSetChanged();
                        lastClick = getPosition();
                    }
                }
            });
        }
    }

    public interface ItemClick {
        void click(int postion);
    }

}