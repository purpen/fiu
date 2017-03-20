package com.taihuoniao.fineix.main.tab3.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.beans.ProductBean;
import com.taihuoniao.fineix.beans.SearchBean;
import com.taihuoniao.fineix.main.MainApplication;
import com.taihuoniao.fineix.utils.DensityUtils;
import com.taihuoniao.fineix.utils.GlideUtils;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Stephen on 2017/3/3 23:11
 * Email: 895745843@qq.com
 */
public class ProductListGridAdapter extends BaseAdapter {
    private List<ProductBean.ProductListItem> list;
    private List<SearchBean.Data.SearchItem> searchList;

    public ProductListGridAdapter(Context context, List<ProductBean.ProductListItem> list, List<SearchBean.Data.SearchItem> searchList) {
        Context context1 = context;
        this.list = list;
        this.searchList = searchList;
    }

    @Override
    public int getCount() {
        if (list.size() != 0) {
            return list.size();
        } else if (searchList.size() != 0) {
            return searchList.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (list.size() != 0) {
            return list.get(position);
        } else if (searchList.size() != 0) {
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
            convertView = View.inflate(parent.getContext(), R.layout.item_gridview_productlist_product, null);
            holder = new ViewHolder(convertView);
//            RelativeLayout relativeLayout = (RelativeLayout) convertView.findViewById(R.id.relayouLayout_container_wellGoods_product);
//            if (position % 2 == 0) {
//                relativeLayout.setGravity(Gravity.END);
//            }
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) holder.productImg.getLayoutParams();
            layoutParams.width = (MainApplication.getContext().getScreenWidth() - DensityUtils.dp2px(parent.getContext(), 45)) / 2;
            layoutParams.height = layoutParams.width;
            holder.productImg.setLayoutParams(layoutParams);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (list.size() != 0) {
//            ImageLoader.getInstance().displayImage(list.get(position).getCover_url(), holder.productImg);
            GlideUtils.displayImage(list.get(position).getCover_url(), holder.productImg);
            holder.name.setText(list.get(position).getTitle());
            holder.price.setText("¥" + list.get(position).getSale_price());
        } else if (searchList.size() != 0) {
//            ImageLoader.getInstance().displayImage(searchList.get(position).getCover_url(), holder.productImg);
            GlideUtils.displayImage(searchList.get(position).getCover_url(), holder.productImg);
            holder.name.setText(searchList.get(position).getTitle());
            holder.price.setText("¥" + searchList.get(position).getSale_price());
        }
        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.product_img)
        ImageView productImg;
        @Bind(R.id.name)
        TextView name;
        @Bind(R.id.price)
        TextView price;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

}
