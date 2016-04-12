package com.taihuoniao.fineix.adapters;

import android.app.Activity;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.beans.City;
import com.taihuoniao.fineix.utils.LogUtil;
import com.taihuoniao.fineix.utils.Util;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author lilin
 *         created at 2016/3/30 16:31
 */
public class HotCitiesAdapter extends RecyclerView.Adapter<HotCitiesAdapter.ViewHolder> {
    public interface OnItemClickListener{
        void onItemClick(View view, int position);
        void onItemLongClick(View view , int position);
    }

    private OnItemClickListener mOnItemClickLitener;

    public void setmOnItemClickLitener(OnItemClickListener itemClickLitener){
        this.mOnItemClickLitener=itemClickLitener;
    }
    private Activity activity;
    private ArrayList<City> list;

    public HotCitiesAdapter(Activity activity, ArrayList<City> list) {
        this.activity = activity;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(activity).inflate(R.layout.item_hotcity_list,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        City city = list.get(position);
        if (mOnItemClickLitener!=null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnItemClickLitener.onItemClick(holder.itemView,position);
                }
            });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    mOnItemClickLitener.onItemLongClick(holder.itemView,position);
                    return false;
                }
            });
        }
        LogUtil.e("onBindViewHolder",position+"");
//        if (position==0){
//            holder.sv.setVisibility(View.VISIBLE);
//        }else {
            holder.sv.setVisibility(View.GONE);
            holder.tv.setText(city.name);
            ImageLoader.getInstance().displayImage(city.image_url,holder.iv);
//        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.iv)
        public ImageView iv;
        @Bind(R.id.tv)
        TextView tv;
        @Bind(R.id.sv)
        SearchView sv;
        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}

