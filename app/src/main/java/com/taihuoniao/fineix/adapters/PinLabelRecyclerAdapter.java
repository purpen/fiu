//package com.taihuoniao.fineix.adapters;
//
//import android.content.Context;
//import android.graphics.Rect;
//import android.support.v7.widget.RecyclerView;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import com.taihuoniao.fineix.R;
//import com.taihuoniao.fineix.utils.DensityUtils;
//
//import java.util.List;
//
///**
// * Created by taihuoniao on 2016/4/18.
// */
//public class PinLabelRecyclerAdapter extends RecyclerView.Adapter<PinLabelRecyclerAdapter.VH> {
//    private Context context;
//    private List<String> hotLabelList;
//    private EditRecyclerAdapter.ItemClick itemClick;
//
//    public PinLabelRecyclerAdapter(Context context1, List<String> hotLabelList, EditRecyclerAdapter.ItemClick itemClick1) {
//        context = context1;
//        this.hotLabelList = hotLabelList;
//        itemClick = itemClick1;
//    }
//
//
//    @Override
//    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view = View.inflate(context, R.layout.view_horizontal_label_item, null);
//        return new VH(view);
//    }
//
//    @Override
//    public void onBindViewHolder(final VH holder,  int position) {
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                itemClick.click(holder.getAdapterPosition());
//                PinLabelRecyclerAdapter.this.notifyItemChanged(holder.getAdapterPosition());
//            }
//        });
//        holder.textView.setText(hotLabelList.get(position));
//    }
//
//    @Override
//    public int getItemCount() {
//        return hotLabelList == null ? 0 : hotLabelList.size();
//    }
//
//    static class VH extends RecyclerView.ViewHolder {
//        TextView textView;
//
//        public VH(final View itemView) {
//            super(itemView);
//            textView = (TextView) itemView.findViewById(R.id.view_horizontal_label_item_tv);
//        }
//    }
//
//    public static class LabelItemDecoration extends RecyclerView.ItemDecoration {
//        private Context context;
//
//        public LabelItemDecoration(Context context) {
//            this.context = context;
//        }
//
//        @Override
//        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
//            outRect.left = DensityUtils.dp2px(context, 10);
//            outRect.right = 0;
//            outRect.bottom = 0;
//            outRect.top = DensityUtils.dp2px(context, 10);
//        }
//    }
//
//}
