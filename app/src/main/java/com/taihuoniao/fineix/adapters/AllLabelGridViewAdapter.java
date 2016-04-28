package com.taihuoniao.fineix.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.beans.AllLabelBean;

/**
 * Created by taihuoniao on 2016/4/11.
 */
public class AllLabelGridViewAdapter extends BaseAdapter {
    private Context context;
    //    private List<AllLabelBean> allLabelList;
    private AllLabelBean allLabelBean;
    private int type;//判断是否是最后一页   1,不是。2,是。
//    private int page = 1;//页码

    public AllLabelGridViewAdapter(Context context, AllLabelBean allLabelBean) {
        this.context = context;
//        this.allLabelList = allLabelList;
        this.allLabelBean = allLabelBean;
    }

    public int getType() {
        return type;
    }

    public void setPage(int page) {
        allLabelBean.setPage(page);
    }

    public int getPage() {
        return allLabelBean.getPage();
    }

    @Override
    public int getCount() {
//        if (allLabelList.size() > page * 10) {
//            type = 1;
//            return page * 10;
//        }
//        type = 2;
//        return allLabelList.size();
        if (allLabelBean.getChildren().size() > 10 * allLabelBean.getPage()) {
            type = 1;
            return allLabelBean.getPage() * 10;
        }
        type = 2;
        return allLabelBean.getChildren().size();
    }

    @Override
    public Object getItem(int position) {
//        return allLabelList.get(position);
        return allLabelBean.getChildren().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder hold;
        if (convertView == null) {
            hold = new ViewHolder();
            convertView = View.inflate(context, R.layout.view_labellist_item, null);
            hold.textView = (TextView) convertView.findViewById(R.id.view_labellist_item_tv);
            convertView.setTag(hold);
        } else {
            hold = (ViewHolder) convertView.getTag();
        }
//        hold.textView.setText(allLabelList.get(position).getTitle_cn());
        hold.textView.setText(allLabelBean.getChildren().get(position).getTitle_cn());
        return convertView;
    }

    static class ViewHolder {
        private TextView textView;
    }
}
