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
import com.taihuoniao.fineix.beans.SearchBean;

import java.util.List;

/**
 * Created by taihuoniao on 2016/4/26.
 */
public class AllQingjingGridAdapter extends BaseAdapter {
    private List<QingJingListBean.QingJingItem> list;
    private List<SearchBean.SearchItem> searchList;
    private Context context;
    private int horizontalSpace = 0;//gridview的水平间距

    public AllQingjingGridAdapter(List<QingJingListBean.QingJingItem> list, List<SearchBean.SearchItem> searchList, Context context, int horizontalSpace) {
        this.list = list;
        this.searchList = searchList;
        this.context = context;
        this.horizontalSpace = horizontalSpace;
    }

    @Override
    public int getCount() {
        if (list != null) {
            return list.size();
        } else if (searchList != null) {
            return searchList.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (list != null) {
            return list.get(position);
        } else if (searchList != null) {
            return searchList.get(position);
        }
        return null;
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
            holder.selectImg = (ImageView) convertView.findViewById(R.id.item_allqingjing_selectbackground);
            ViewGroup.LayoutParams slp = holder.selectImg.getLayoutParams();
            slp.width = lp.width;
            slp.height = lp.height;
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (list != null) {
            ImageLoader.getInstance().displayImage(list.get(position).getCover_url(), holder.backgroundImg);
            holder.addressTv.setText(list.get(position).getAddress());
            holder.title.setText(list.get(position).getTitle());
            if (list.get(position).isSelect()) {
                holder.selectImg.setVisibility(View.VISIBLE);
            } else {
                holder.selectImg.setVisibility(View.GONE);
            }
        } else if (searchList != null) {
            ImageLoader.getInstance().displayImage(searchList.get(position).getCover_url(), holder.backgroundImg);
            holder.addressTv.setText(searchList.get(position).getAddress());
            holder.title.setText(searchList.get(position).getTitle());
            if (searchList.get(position).isSelect()) {
                holder.selectImg.setVisibility(View.VISIBLE);
            } else {
                holder.selectImg.setVisibility(View.GONE);
            }
        }
        return convertView;
    }

    static class ViewHolder {
        ImageView backgroundImg;
        TextView addressTv;
        TextView title;
        ImageView selectImg;
    }
}
