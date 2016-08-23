package com.taihuoniao.fineix.adapters;

import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.beans.DataChooseSubject;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.utils.Util;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author lilin
 *         created at 2016/8/17 15:29
 */
public class SalePromotionAdapter extends CommonBaseAdapter<DataChooseSubject.ItemChoosenSubject> {
    public SalePromotionAdapter(ArrayList list, Activity activity) {
        super(list, activity);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final DataChooseSubject.ItemChoosenSubject item = list.get(position);
        ViewHolder holder;
        if (convertView == null) {
            convertView = Util.inflateView(R.layout.item_sale_promotion, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ImageLoader.getInstance().displayImage(item.cover_url, holder.imageView, options);
        holder.tvTitle.setText(item.title);
        holder.tvCount.setText(item.view_count);
        LinearLayoutManager manager = new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false);
        holder.recyclerView.setLayoutManager(manager);
        holder.recyclerView.setHasFixedSize(true);
        SalePromotionRecycleViewAdapter adapter = new SalePromotionRecycleViewAdapter(activity, item.products);
        adapter.setmOnItemClickLitener(new SalePromotionRecycleViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                ToastUtils.showInfo("促销详情");
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
        holder.recyclerView.setAdapter(adapter);
        return convertView;
    }


    static class ViewHolder {
        @Bind(R.id.imageView)
        ImageView imageView;
        @Bind(R.id.tv_title)
        TextView tvTitle;
        @Bind(R.id.tv_desc)
        TextView tvDesc;
        @Bind(R.id.tv_count)
        TextView tvCount;
        @Bind(R.id.recycler_view)
        RecyclerView recyclerView;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
