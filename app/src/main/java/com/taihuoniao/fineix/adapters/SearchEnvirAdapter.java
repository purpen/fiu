package com.taihuoniao.fineix.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.beans.CategoryListBean;

import java.util.List;

/**
 * Created by taihuoniao on 2016/7/15.
 */
public class SearchEnvirAdapter extends RecyclerView.Adapter<SearchEnvirAdapter.VH> {
    private Context context;
    private List<CategoryListBean.CategoryListItem> envirList;
    private EditRecyclerAdapter.ItemClick itemClick;

    public SearchEnvirAdapter(Context context, List<CategoryListBean.CategoryListItem> envirList, EditRecyclerAdapter.ItemClick itemClick) {
        this.context = context;
        this.envirList = envirList;
        this.itemClick = itemClick;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context,R.layout.item_search_envir,null);
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
        holder.tv.setText(envirList.get(position).getTitle());
        if(envirList.get(position).isSelect()){
            holder.tv.setTextColor(context.getResources().getColor(R.color.yellow_bd8913));
        }else{
            holder.tv.setTextColor(context.getResources().getColor(R.color.white));
        }
    }

    @Override
    public int getItemCount() {
        return envirList == null ? 0 : envirList.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        TextView tv;
        public VH(View itemView) {
            super(itemView);
            tv = (TextView) itemView.findViewById(R.id.tv);
        }
    }
}
