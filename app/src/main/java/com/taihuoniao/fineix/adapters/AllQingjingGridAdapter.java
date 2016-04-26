package com.taihuoniao.fineix.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.beans.QingJingListBean;

import java.util.List;

/**
 * Created by taihuoniao on 2016/4/26.
 */
public class AllQingjingGridAdapter extends BaseAdapter {
    private List<QingJingListBean.QingJingItem> list;
    private Context context;
    private int horizontalSpace = 0;//gridview的水平间距

    public AllQingjingGridAdapter(List<QingJingListBean.QingJingItem> list, Context context, int horizontalSpace) {
        this.list = list;
        this.context = context;
        this.horizontalSpace = horizontalSpace;
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_allqingjing, null);
            holder = new ViewHolder();
            holder.backgroundImg = (ImageView) convertView.findViewById(R.id.item_allqingjing_img);
            ViewGroup.LayoutParams lp = holder.backgroundImg.getLayoutParams();
            lp.width = (parent.getWidth() - horizontalSpace) / 2;
            lp.height = lp.width * 16 / 9;
            holder.addressTv = (TextView) convertView.findViewById(R.id.item_allqingjing_address);
            holder.title = (TextView) convertView.findViewById(R.id.item_allqingjing_title);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ImageLoader.getInstance().displayImage(list.get(position).getCover_url(), holder.backgroundImg);
        holder.addressTv.setText(list.get(position).getAddress());
        holder.title.setText(list.get(position).getTitle());
        return convertView;
    }

    static class ViewHolder {
        ImageView backgroundImg;
        TextView addressTv;
        TextView title;
    }
}
