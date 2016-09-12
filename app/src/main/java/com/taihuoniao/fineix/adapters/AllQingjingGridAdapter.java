//package com.taihuoniao.fineix.adapters;
//
//import android.content.Context;
//import android.text.Spannable;
//import android.text.SpannableStringBuilder;
//import android.text.style.BackgroundColorSpan;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.nostra13.universalimageloader.core.DisplayImageOptions;
//import com.nostra13.universalimageloader.core.ImageLoader;
//import com.taihuoniao.fineix.R;
//import com.taihuoniao.fineix.beans.QingJingListBean;
//import com.taihuoniao.fineix.beans.SearchBean;
//import com.taihuoniao.fineix.utils.DensityUtils;
//
//import java.util.List;
//
///**
// * Created by taihuoniao on 2016/4/26.
// */
//public class AllQingjingGridAdapter extends BaseAdapter {
//    private List<QingJingListBean.QingJingItem> list;
//    private List<SearchBean.Data.SearchItem> searchList;
//    private Context context;
//    private int horizontalSpace = 0;//gridview的水平间距
//    private DisplayImageOptions options;
//
//    public AllQingjingGridAdapter(List<QingJingListBean.QingJingItem> list, List<SearchBean.Data.SearchItem> searchList, Context context, int horizontalSpace) {
//        this.list = list;
//        this.searchList = searchList;
//        this.context = context;
//        this.horizontalSpace = horizontalSpace;
//        options = new DisplayImageOptions.Builder()
//                .showImageOnLoading(R.mipmap.default_background_750_1334)
//                .showImageForEmptyUri(R.mipmap.default_background_750_1334)
//                .showImageOnFail(R.mipmap.default_background_750_1334)
//                .cacheInMemory(true)
//                .cacheOnDisk(true)
//                .considerExifParams(true)
//                .build();
//    }
//
//    @Override
//    public int getCount() {
//        if (list != null) {
//            return list.size();
//        } else if (searchList != null) {
//            return searchList.size();
//        }
//        return 0;
//    }
//
//    @Override
//    public Object getItem(int position) {
//        if (list != null) {
//            return list.get(position);
//        } else if (searchList != null) {
//            return searchList.get(position);
//        }
//        return null;
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
//        if (convertView == null) {
//            convertView = View.inflate(context, R.layout.item_qingjing_list, null);
//            holder = new ViewHolder();
//            holder.backgroundImg = (ImageView) convertView.findViewById(R.id.item_qingjing_list_background);
//            ViewGroup.LayoutParams lp = holder.backgroundImg.getLayoutParams();
////            Log.e("<<<", "总宽度=" + (parent.getWidth() - parent.getPaddingLeft() - parent.getPaddingRight()));
//            lp.width = (parent.getWidth() - DensityUtils.dp2px(context, 10)) / 2;
//            lp.height = lp.width * 16 / 9;
//            holder.backgroundImg.setLayoutParams(lp);
//            holder.addressTv = (TextView) convertView.findViewById(R.id.item_qingjing_list_address);
//            holder.title = (TextView) convertView.findViewById(R.id.item_qingjing_list_title);
//            holder.selectImg = (ImageView) convertView.findViewById(R.id.item_allqingjing_selectbackground);
//            ViewGroup.LayoutParams slp = holder.selectImg.getLayoutParams();
//            slp.width = lp.width;
//            slp.height = lp.height;
//            holder.selectImg.setLayoutParams(slp);
//            convertView.setTag(holder);
//        } else {
//            holder = (ViewHolder) convertView.getTag();
//        }
////        改变选中的样式
//        if (list != null) {
//            ImageLoader.getInstance().displayImage(list.get(position).getCover_url(), holder.backgroundImg, options);
//            holder.addressTv.setText(list.get(position).getAddress());
//            SpannableStringBuilder style = new SpannableStringBuilder(list.get(position).getTitle());
//            BackgroundColorSpan backgroundColorSpan = new BackgroundColorSpan(context.getResources().getColor(R.color.black));
//            style.setSpan(backgroundColorSpan, 0, list.get(position).getTitle().length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
//            holder.title.setText(style);
//            if (list.get(position).isSelect()) {
//                holder.selectImg.setVisibility(View.VISIBLE);
//            } else {
//                holder.selectImg.setVisibility(View.GONE);
//            }
//        } else if (searchList != null) {
//            ImageLoader.getInstance().displayImage(searchList.get(position).getCover_url(), holder.backgroundImg, options);
//            holder.addressTv.setText(searchList.get(position).getAddress());
//            SpannableStringBuilder style = new SpannableStringBuilder(searchList.get(position).getTitle());
//            BackgroundColorSpan backgroundColorSpan = new BackgroundColorSpan(context.getResources().getColor(R.color.black));
//            style.setSpan(backgroundColorSpan, 0, searchList.get(position).getTitle().length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
//            holder.title.setText(style);
//            if (searchList.get(position).isSelect()) {
//                holder.selectImg.setVisibility(View.VISIBLE);
//            } else {
//                holder.selectImg.setVisibility(View.GONE);
//            }
//        }
//
//        return convertView;
//    }
//
//    static class ViewHolder {
//        ImageView backgroundImg;
//        TextView addressTv;
//        TextView title;
//        ImageView selectImg;
//    }
//}
