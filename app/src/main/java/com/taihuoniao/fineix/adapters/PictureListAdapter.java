package com.taihuoniao.fineix.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.beans.AlbumBean;
import com.taihuoniao.fineix.utils.DensityUtils;

import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by taihuoniao on 2016/8/29.
 */
public class PictureListAdapter extends RecyclerView.Adapter<PictureListAdapter.VH> {

    private Activity activity;
    private List<String> albumPaths;
    private Map<String, AlbumBean> albumList;
    private EditRecyclerAdapter.ItemClick itemClick;

    public PictureListAdapter(Activity activity, List<String> albumPaths, Map<String, AlbumBean> albumList, EditRecyclerAdapter.ItemClick itemClick) {
        this.activity = activity;
        this.albumPaths = albumPaths;
        this.albumList = albumList;
        this.itemClick = itemClick;
    }


    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(activity, R.layout.item_albumlist, null);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(final VH holder, int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClick.click(holder.getAdapterPosition());
            }
        });
        holder.name.setText(albumList.get(albumPaths.get(position)).getTitle());
        holder.number.setText(albumList.get(albumPaths.get(position)).getPhotos().size() + "");
        ImageLoader.getInstance().displayImage("file://" + albumList.get(albumPaths.get(position)).getPhotos().get(0).getImageUri(), holder.img);
    }


    @Override
    public int getItemCount() {
        return albumList == null ? 0 : albumList.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        @Bind(R.id.item_albumlist_img)
        ImageView img;
        @Bind(R.id.item_albumlist_number)
        TextView number;
        @Bind(R.id.item_albumlist_name)
        TextView name;

        public VH(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            img.setLayoutParams(new RelativeLayout.LayoutParams(DensityUtils.dp2px(itemView.getContext(), 50), DensityUtils.dp2px(itemView.getContext(), 50)));
        }
    }
}
