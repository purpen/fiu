//package com.taihuoniao.fineix.adapters;
//
//import android.content.Context;
//import android.support.v7.widget.RecyclerView;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import com.taihuoniao.fineix.R;
//import com.taihuoniao.fineix.beans.CategoryLabelListBean;
//
//import java.util.List;
//
///**
// * Created by taihuoniao on 2016/5/5.
// */
//public class GoodListFragmentRecyclerAdapter extends RecyclerView.Adapter<GoodListFragmentRecyclerAdapter.VH> {
//    private Context context;
//    private List<CategoryLabelListBean.CategoryTagItem> list;
//    private EditRecyclerAdapter.ItemClick itemClick;
//
//    public GoodListFragmentRecyclerAdapter(Context context, List<CategoryLabelListBean.CategoryTagItem> list, EditRecyclerAdapter.ItemClick itemClick) {
//        this.context = context;
//        this.list = list;
//        this.itemClick = itemClick;
//    }
//
//    @Override
//    public GoodListFragmentRecyclerAdapter.VH onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view = View.inflate(parent.getContext(), R.layout.item_category_second, null);
//        VH holder = new VH(view);
//        return holder;
//    }
//
//    @Override
//    public void onBindViewHolder(final VH holder,  int position) {
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                itemClick.click(holder.getAdapterPosition());
//            }
//        });
//        holder.tv.setText(list.get(position).getTitle_cn());
//        if (list.get(position).isSelect()) {
//            holder.tv.setTextColor(context.getResources().getColor(R.color.yellow_bd8913));
//            holder.tv.setBackgroundResource(R.drawable.yellow_corner_white);
//        } else {
//            holder.tv.setTextColor(context.getResources().getColor(R.color.black333333));
//            holder.tv.setBackgroundResource(R.drawable.background_corner_black);
//        }
//    }
//
//
//    @Override
//    public int getItemCount() {
//        return list == null ? 0 : list.size();
//    }
//
//    public static class VH extends RecyclerView.ViewHolder {
//        TextView tv;
//
//        public VH(View itemView) {
//            super(itemView);
//            tv = (TextView) itemView.findViewById(R.id.view_labellist_item_tv);
//        }
//    }
//}
