//package com.taihuoniao.fineix.adapters;
//
//import android.support.v7.widget.RecyclerView;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import com.taihuoniao.fineix.R;
//import com.taihuoniao.fineix.utils.DensityUtils;
//
//import java.util.List;
//
///**
// * Created by taihuoniao on 2016/7/22.
// */
//public class LabelRecycleAdapter extends RecyclerView.Adapter<LabelRecycleAdapter.VH> {
//    private List<String> list;
//
//    public LabelRecycleAdapter(List<String> list) {
//        this.list = list;
//    }
//
//    @Override
//    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view = View.inflate(parent.getContext(), R.layout.view_horizontal_label_item, null);
//        return new VH(view);
//    }
//
//    @Override
//    public void onBindViewHolder(VH holder, int position) {
//        holder.textView.setText(list.get(position));
//        holder.textView.setTextColor(holder.itemView.getResources().getColor(R.color.white));
//        holder.textView.setBackgroundResource(R.drawable.tags_yellow);
//        holder.textView.setPadding(DensityUtils.dp2px(holder.textView.getContext(), 10), 0,
//                DensityUtils.dp2px(holder.textView.getContext(), 18), 0);
//        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        layoutParams.rightMargin = DensityUtils.dp2px(holder.textView.getContext(), 10);
//        holder.textView.setLayoutParams(layoutParams);
//    }
//
//    @Override
//    public int getItemCount() {
//        return list.size();
//    }
//
//    static class VH extends RecyclerView.ViewHolder {
//        TextView textView;
//
//        public VH(View itemView) {
//            super(itemView);
//            textView = (TextView) itemView.findViewById(R.id.view_horizontal_label_item_tv);
//        }
//    }
//}
