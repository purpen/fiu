package com.taihuoniao.fineix.adapters;

import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.beans.ProductListBean;

import java.util.List;

/**
 * Created by taihuoniao on 2016/4/13.
 */
public class AddProductGridAdapter extends BaseAdapter {
    private Context context;
    private List<ProductListBean> list;

    public AddProductGridAdapter(Context context, List<ProductListBean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
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
        ViewHolder mHolder;
        if (convertView == null) {
            mHolder = new ViewHolder();
            convertView = View.inflate(context, R.layout.item_nicegoods_gridview, null);
            mHolder.item_nicegoods_title = (TextView) convertView
                    .findViewById(R.id.item_tv_title_nicegoods);
            mHolder.item_nicegoods_zan = (TextView) convertView
                    .findViewById(R.id.tv_zan_nicegoods);
            mHolder.item_nicegoods_currentprice = (TextView) convertView
                    .findViewById(R.id.tv_currentPrice_nicegoods);
            mHolder.item_nicegoods_oldprice = (TextView) convertView
                    .findViewById(R.id.tv_oldPrice_nicegoods);
            mHolder.item_nicegoods_image = (ImageView) convertView
                    .findViewById(R.id.item_imageview_nicegoods);

            convertView.setTag(mHolder);
        } else {
            mHolder = (ViewHolder) convertView.getTag();
        }

        mHolder.item_nicegoods_title
                .setText(list.get(position).getTitle());
        mHolder.item_nicegoods_zan
                .setText(list.get(position).getLove_count() + "");
        mHolder.item_nicegoods_currentprice
                .setText("¥" + list.get(position).getSale_price());
        mHolder.item_nicegoods_currentprice
                .setTextColor(context.getResources().getColor(R.color.red));

        SpannableString ss = new SpannableString("¥" + list.get(position).getMarket_price());
        ss.setSpan(new StrikethroughSpan(), 0, ss.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mHolder.item_nicegoods_oldprice
                .setText(ss);
        ImageLoader.getInstance().displayImage(list.get(position).getCover_url(), mHolder.item_nicegoods_image);
        return convertView;
    }

    class ViewHolder {
        private ImageView item_nicegoods_image;
        private TextView item_nicegoods_title;
        private TextView item_nicegoods_oldprice;
        private TextView item_nicegoods_currentprice;
        private TextView item_nicegoods_zan;
    }

}
