package com.taihuoniao.fineix.zone.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.utils.GlideUtils;
import com.taihuoniao.fineix.utils.Util;
import com.taihuoniao.fineix.zone.bean.ZoneDetailBean;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 地盘covers
 */
public class ZoneEditCoversAdapter extends RecyclerView.Adapter<ZoneEditCoversAdapter.ViewHolder> {
    private RelativeLayout.LayoutParams params;
    private Activity activity;
    private List<ZoneDetailBean.NcoverBean> list;
    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }

    public ZoneEditCoversAdapter(Activity activity, List<ZoneDetailBean.NcoverBean> list) {
        this.activity = activity;
        this.list = list;
        int size = (Util.getScreenWidth()
                - 2 * activity.getResources().getDimensionPixelSize(R.dimen.dp15)
                - 3 * activity.getResources().getDimensionPixelSize(R.dimen.dp10)) >> 2;
        params = new RelativeLayout.LayoutParams(size, size);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.item_zone_cover, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final ZoneDetailBean.NcoverBean item = list.get(position);
        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnItemClickListener.onItemClick(holder.iv, holder.getAdapterPosition());
                }
            });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    mOnItemClickListener.onItemLongClick(holder.iv, holder.getAdapterPosition());
                    return false;
                }
            });
        }
//        holder.ivDelete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                HashMap<String, String> param = new HashMap<>();
//                param.put("id", item.id);
//                HttpRequest.post(param, URL.ZONE_COVER_DELETE, new GlobalDataCallBack() {
//                    @Override
//                    public void onSuccess(String json) {
//                        HttpResponse response = JsonUtil.fromJson(json, HttpResponse.class);
//                        if (response.isSuccess()) {
//                            list.remove(position);
//                            notifyDataSetChanged();
//                            return;
//                        }
//                        ToastUtils.showError(response.getMessage());
//                    }
//
//                    @Override
//                    public void onFailure(String error) {
//                        ToastUtils.showError(R.string.network_err);
//                    }
//                });
//            }
//        });
        holder.iv.setLayoutParams(params);
        if (position == list.size() - 1) {
            GlideUtils.displayImageNoFading(R.mipmap.zone_upload_banner, holder.iv);
        } else {
            GlideUtils.displayImageNoFading(item.url, holder.iv);
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.rl)
        RelativeLayout rl;
        @Bind(R.id.iv)
        ImageView iv;
        @Bind(R.id.iv_delete)
        ImageView ivDelete;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
