//package com.taihuoniao.fineix.adapters;
//
//import android.content.Context;
//import android.support.v7.widget.RecyclerView;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import com.taihuoniao.fineix.R;
//import com.taihuoniao.fineix.beans.CategoryListBean;
//
//import java.util.List;
//
///**
// * Created by taihuoniao on 2016/7/15.
// */
//public class DipanCategoryAdapter extends RecyclerView.Adapter<DipanCategoryAdapter.VH> {
//    private Context context;
//    private List<CategoryListBean.CategoryListItem> list;
//    private EditRecyclerAdapter.ItemClick itemClick;
//
//    public DipanCategoryAdapter(Context context, List<CategoryListBean.CategoryListItem> list, EditRecyclerAdapter.ItemClick itemClick) {
//        this.context = context;
//        this.list = list;
//        this.itemClick = itemClick;
//    }
//
//    @Override
//    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view = View.inflate(parent.getContext(), R.layout.item_dipan_category, null);
//        VH holder = new VH(view);
//        return holder;
//    }
//
//    @Override
//    public void onBindViewHolder(VH holder, final int position) {
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                itemClick.click(position);
//            }
//        });
//        holder.textView.setText(list.get(position).getTitle());
//        if (list.get(position).isSelect()) {
//            holder.textView.setTextColor(context.getResources().getColor(R.color.yellow_bd8913));
//            holder.line.setVisibility(View.VISIBLE);
//        } else {
//            holder.textView.setTextColor(context.getResources().getColor(R.color.black333333));
//            holder.line.setVisibility(View.GONE);
//        }
//    }
//
//    @Override
//    public int getItemCount() {
//        return list == null ? 0 : list.size();
//    }
//
//    static class VH extends RecyclerView.ViewHolder {
//        TextView textView;
//        TextView line;
//
//        public VH(View itemView) {
//            super(itemView);
//            textView = (TextView) itemView.findViewById(R.id.item_dipan_category_tv);
//            line = (TextView) itemView.findViewById(R.id.item_dipan_category_line);
//        }
//    }
//}
