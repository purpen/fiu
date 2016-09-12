//package com.taihuoniao.fineix.adapters;
//
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.TextView;
//
//import com.taihuoniao.fineix.R;
//import com.taihuoniao.fineix.beans.ActiveTagsBean;
//
//import butterknife.Bind;
//import butterknife.ButterKnife;
//
///**
// * Created by taihuoniao on 2016/8/18.
// */
//public class HotLabelAdapter extends BaseAdapter {
//    private ActiveTagsBean activeTagsBean;
//
//    public HotLabelAdapter(ActiveTagsBean activeTagsBean) {
//        this.activeTagsBean = activeTagsBean;
//    }
//
//    @Override
//    public int getCount() {
//        if (activeTagsBean.getData().getItems() == null) {
//            return 0;
//        }
//        if (activeTagsBean.getData().getItems().size() >= 10) {
//            return 10;
//        }
//        return activeTagsBean.getData().getItems().size();
//    }
//
//    @Override
//    public Object getItem(int position) {
//        return activeTagsBean.getData().getItems().get(position);
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
//            convertView = View.inflate(parent.getContext(), R.layout.item_add_label, null);
//            holder = new ViewHolder(convertView);
//            convertView.setTag(holder);
//        } else {
//            holder = (ViewHolder) convertView.getTag();
//        }
//        holder.name.setText("#" + activeTagsBean.getData().getItems().get(position).get(0) + " ");
//        return convertView;
//    }
//
//    static class ViewHolder {
//        @Bind(R.id.name)
//        TextView name;
//
//        ViewHolder(View view) {
//            ButterKnife.bind(this, view);
//        }
//    }
//}
