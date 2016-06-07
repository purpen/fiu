package com.taihuoniao.fineix.adapters;
import android.app.Activity;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.BackgroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.beans.FindFriendData;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author lilin
 * created at 2016/5/8 18:38
 */
public class FindFriendRecycleViewAdapter extends RecyclerView.Adapter<FindFriendRecycleViewAdapter.ViewHolder> {
    public interface OnItemClickListener{
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }
    private OnItemClickListener mOnItemClickLitener;

    public void setmOnItemClickLitener(OnItemClickListener itemClickLitener){
        this.mOnItemClickLitener=itemClickLitener;
    }
    private Activity activity;
    private ArrayList<FindFriendData.CJItem> list;
    private DisplayImageOptions options;
    private ImageLoader imageLoader;
    public FindFriendRecycleViewAdapter(Activity activity, ArrayList<FindFriendData.CJItem> list) {
        this.activity = activity;
        this.list = list;
        imageLoader=ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.default_background_500_500)
                .showImageForEmptyUri(R.mipmap.default_background_500_500)
                .showImageOnFail(R.mipmap.default_background_500_500)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(activity).inflate(R.layout.item_recycle_find_friend,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        FindFriendData.CJItem item= list.get(position);
        if (mOnItemClickLitener!=null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnItemClickLitener.onItemClick(holder.itemView,position);
                }
            });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    mOnItemClickLitener.onItemLongClick(holder.itemView,position);
                    return false;
                }
            });
        }

        imageLoader.displayImage(item.cover_url,holder.iv,options);
        SpannableStringBuilder style=new SpannableStringBuilder(list.get(position).title);
        BackgroundColorSpan backgroundColorSpan = new BackgroundColorSpan(activity.getResources().getColor(R.color.black));
        style.setSpan(backgroundColorSpan, 0, list.get(position).title.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        holder.title.setText(style);
//        holder.title.setText(item.title);
        holder.address.setText(item.address);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.iv)
        ImageView iv;
        @Bind(R.id.item_qingjing_list_title)
        TextView title;
        @Bind(R.id.item_qingjing_list_address)
        TextView address;
        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}

