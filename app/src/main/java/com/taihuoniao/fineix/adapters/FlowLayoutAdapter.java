package com.taihuoniao.fineix.adapters;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.beans.AllLabelBean;
import com.taihuoniao.fineix.view.FlowLayout;

import java.util.List;

/**
 * Created by taihuoniao on 2016/5/17.
 */
public class FlowLayoutAdapter {
    private Context context;
    private FlowLayout flowLayout;
    private List<AllLabelBean> list;

    public FlowLayoutAdapter(Context context, FlowLayout flowLayout, List<AllLabelBean> list) {
        this.context = context;
        this.flowLayout = flowLayout;
        this.list = list;
    }

    private int getCount() {
        return list.size();
    }

    private void getView() {
        for (int i = 0; i < getCount(); i++) {
            View v = View.inflate(context, R.layout.view_labellist_item, null);
            TextView textView = (TextView) v.findViewById(R.id.view_labellist_item_tv);
            textView.setText(list.get(i).getTitle_cn());
        }
    }

}
