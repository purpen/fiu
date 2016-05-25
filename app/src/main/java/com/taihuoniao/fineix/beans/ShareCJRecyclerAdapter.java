package com.taihuoniao.fineix.beans;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.EditRecyclerAdapter;

import java.util.List;

/**
 * Created by taihuoniao on 2016/5/24.
 */
public class ShareCJRecyclerAdapter extends RecyclerView.Adapter<ShareCJRecyclerAdapter.VH> {
    private List<ShareDemoBean> shareList;
    private EditRecyclerAdapter.ItemClick itemClick;

    public ShareCJRecyclerAdapter(List<ShareDemoBean> shareList, EditRecyclerAdapter.ItemClick itemClick) {
        this.shareList = shareList;
        this.itemClick = itemClick;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.item_share_recycler, null);
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
        holder.img.setImageResource(shareList.get(position).getImgId());
        if (shareList.get(position).isSelect()) {
            holder.line.setVisibility(View.VISIBLE);
        } else {
            holder.line.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return shareList.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        ImageView img;
        TextView line;

        public VH(View itemView) {
            super(itemView);
            img = (ImageView) itemView.findViewById(R.id.item_share_recycler_img);
            line = (TextView) itemView.findViewById(R.id.item_share_recycler_line);
        }
    }
}
