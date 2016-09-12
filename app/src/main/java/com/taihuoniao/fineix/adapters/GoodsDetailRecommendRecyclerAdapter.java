//package com.taihuoniao.fineix.adapters;
//
//import android.content.Context;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.TextView;
//
//import com.nostra13.universalimageloader.core.DisplayImageOptions;
//import com.nostra13.universalimageloader.core.ImageLoader;
//import com.taihuoniao.fineix.R;
//import com.taihuoniao.fineix.beans.ProductBean;
//import com.taihuoniao.fineix.view.roundImageView.RoundedImageView;
//
//import java.util.List;
//
///**
// * Created by taihuoniao on 2016/5/3.
// */
//public class GoodsDetailRecommendRecyclerAdapter extends BaseAdapter {
//    private Context context;
//    private List<ProductBean.ProductListItem> list;
////    private EditRecyclerAdapter.ItemClick itemClick;
//    private DisplayImageOptions options;
//
//    public GoodsDetailRecommendRecyclerAdapter(Context context, List<ProductBean.ProductListItem> list) {
//        this.context = context;
//        this.list = list;
////        this.itemClick = itemClick;
//        options = new DisplayImageOptions.Builder()
//                .showImageOnLoading(R.mipmap.default_background_750_1334)
//                .showImageForEmptyUri(R.mipmap.default_background_750_1334)
//                .showImageOnFail(R.mipmap.default_background_750_1334)
//                .cacheInMemory(true)
//                .cacheOnDisk(true).considerExifParams(true)
//                .build();
//    }
//
//
//
//    @Override
//    public int getCount() {
////        Log.e("<<<适配器","getCount="+list.size());
//        return list.size();
//    }
//
//    @Override
//    public Object getItem(int position) {
//        return list.get(position);
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return position;
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        ViewHolder holder;
//        if(convertView==null){
//            holder = new ViewHolder();
//            convertView = View.inflate(context, R.layout.item_goodsdetail_recommend, null);
//            holder.img = (RoundedImageView) convertView.findViewById(R.id.item_goodsdetail_recommend_img);
//            holder.title = (TextView) convertView.findViewById(R.id.item_goodsdetail_recommend_title);
//            holder.price = (TextView) convertView.findViewById(R.id.item_goodsdetail_recommend_price);
//            convertView.setTag(holder);
//        }else{
//            holder = (ViewHolder) convertView.getTag();
//        }
//        ImageLoader.getInstance().displayImage(list.get(position).getCover_url(), holder.img, options);
//        holder.title.setText(list.get(position).getTitle());
//        holder.price.setText(String.format("¥ %s", list.get(position).getSale_price()));
//        return convertView;
//    }
//
//    public static class ViewHolder {
//        RoundedImageView img;
//        TextView title;
//        TextView price;
//
////        public VH(View itemView) {
////            super(itemView);
////
////
////
////        }
//    }
//}
