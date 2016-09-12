//package com.taihuoniao.fineix.adapters;
//
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.TextView;
//
//import com.taihuoniao.fineix.R;
//import com.taihuoniao.fineix.beans.HotLabel;
//import com.taihuoniao.fineix.beans.Label;
//import com.taihuoniao.fineix.beans.UsedLabelBean;
//
///**
// * Created by taihuoniao on 2016/5/17.
// */
//public class UsedAndHotLabelAdapter extends BaseAdapter {
//    private int pos;
//    private Label label;
//
//    public UsedAndHotLabelAdapter(int pos, Label label) {
//        this.pos = pos;
//        this.label = label;
//    }
//
//    @Override
//    public int getCount() {
//        if (label.getUsedLabelList().size() == 0) {
//            return label.getHotLabelList().size() > 15 ? 15 : label.getHotLabelList().size();
//        } else {
//            if (pos == 0) {
//                return label.getUsedLabelList().size() > 15 ? 15 : label.getUsedLabelList().size();
//            } else {
//                return label.getHotLabelList().size() > 15 ? 15 : label.getHotLabelList().size();
//            }
//        }
//    }
//
//    @Override
//    public Object getItem(int position) {
//        if (label.getUsedLabelList().size() == 0) {
//            return label.getHotLabelList().get(position);
//        } else {
//            if (pos == 0) {
//                return label.getUsedLabelList().get(position);
//            } else {
//                return label.getHotLabelList().get(position);
//            }
//        }
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return position;
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        if (convertView == null) {
//            convertView = View.inflate(parent.getContext(), R.layout.view_labellist_item, null);
//        }
//        TextView textView = (TextView) convertView.findViewById(R.id.view_labellist_item_tv);
//        if (getItem(position) instanceof UsedLabelBean) {
//            UsedLabelBean usedLabelBean = (UsedLabelBean) getItem(position);
//        } else {
//            HotLabel.HotLabelBean hotLabelBean = (HotLabel.HotLabelBean) getItem(position);
//            if (hotLabelBean.isSelect()) {
//                textView.setBackgroundResource(R.drawable.yellow_corner_white);
//                textView.setTextColor(parent.getResources().getColor(R.color.yellow_bd8913));
//            } else {
//                textView.setBackgroundResource(R.drawable.background_corner_black);
//                textView.setTextColor(parent.getResources().getColor(R.color.black333333));
//            }
//            textView.setText(hotLabelBean.getTitle_cn());
//        }
//        return convertView;
//    }
//}
