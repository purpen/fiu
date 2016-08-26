package com.taihuoniao.fineix.adapters;

import android.app.Activity;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
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
 *         created at 2016/5/8 18:38
 */
public class FindFriendRecycleViewAdapter extends RecyclerView.Adapter<FindFriendRecycleViewAdapter.ViewHolder> {
    public interface OnItemClickListener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }

    private OnItemClickListener mOnItemClickLitener;

    public void setmOnItemClickLitener(OnItemClickListener itemClickLitener) {
        this.mOnItemClickLitener = itemClickLitener;
    }

    private Activity activity;
    private ArrayList<FindFriendData.CJItem> list;
    private DisplayImageOptions options;
    private ImageLoader imageLoader;

    public FindFriendRecycleViewAdapter(Activity activity, ArrayList<FindFriendData.CJItem> list) {
        this.activity = activity;
        this.list = list;
        imageLoader = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.default_background_750_1334)
                .showImageForEmptyUri(R.mipmap.default_background_750_1334)
                .showImageOnFail(R.mipmap.default_background_750_1334)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.item_recycle_find_friend, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        FindFriendData.CJItem item = list.get(position);
        if (mOnItemClickLitener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnItemClickLitener.onItemClick(holder.itemView, position);
                }
            });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    mOnItemClickLitener.onItemLongClick(holder.itemView, position);
                    return false;
                }
            });
        }

        imageLoader.displayImage(item.cover_url, holder.iv_cover, options);
        if (!TextUtils.isEmpty(item.title)) {
            holder.tv_title.setText(item.title);
            holder.tv_title.setBackgroundColor(activity.getResources().getColor(R.color.black_touming_80));
        }
        holder.tv_desc.setText(item.address);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.iv_cover)
        ImageView iv_cover;
        @Bind(R.id.tv_title)
        TextView tv_title;
        @Bind(R.id.tv_desc)
        TextView tv_desc;
        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}

