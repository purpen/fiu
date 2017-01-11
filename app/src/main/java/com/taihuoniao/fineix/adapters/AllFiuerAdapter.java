package com.taihuoniao.fineix.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.beans.FiuUserListBean;
import com.taihuoniao.fineix.utils.GlideUtils;
import com.taihuoniao.fineix.view.roundImageView.RoundedImageView;

/**
 * Created by taihuoniao on 2016/7/11.
 */
public class AllFiuerAdapter extends BaseAdapter {
    private Context context;
    private FiuUserListBean fiuUserListBean;

    public AllFiuerAdapter(Context context, FiuUserListBean fiuUserListBean) {
        this.context = context;
        this.fiuUserListBean = fiuUserListBean;
    }

    @Override
    public int getCount() {
        if (fiuUserListBean.getData() == null) {
            return 0;
        }
        if (fiuUserListBean.getData().getRows() == null) {
            return 0;
        }
        return fiuUserListBean.getData().getRows().size();
    }

    @Override
    public Object getItem(int position) {
        if (fiuUserListBean.getData() == null) {
            return null;
        }
        if (fiuUserListBean.getData().getRows() == null) {
            return null;
        }
        return fiuUserListBean.getData().getRows().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.item_all_fiuer, null);
            holder.rankImg = (ImageView) convertView.findViewById(R.id.item_all_fiuer_rank_img);
            holder.rankTv = (TextView) convertView.findViewById(R.id.item_all_fiuer_rank_tv);
            holder.headImg = (RoundedImageView) convertView.findViewById(R.id.item_all_fiuer_headimg);
            holder.vImg = (RoundedImageView) convertView.findViewById(R.id.item_all_fiuer_vimg);
            holder.nameTv = (TextView) convertView.findViewById(R.id.item_all_fiuer_name);
            holder.rank = (TextView) convertView.findViewById(R.id.item_all_fiuer_rank);
            holder.infoTv = (TextView) convertView.findViewById(R.id.item_all_fiuer_info);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.rankImg.setVisibility(View.VISIBLE);
        holder.rankTv.setVisibility(View.GONE);
        switch (position) {
            case 0:
                holder.rankImg.setImageResource(R.mipmap.jiang1);
                break;
            case 1:
                holder.rankImg.setImageResource(R.mipmap.jiang2);
                break;
            case 2:
                holder.rankImg.setImageResource(R.mipmap.jiang3);
                break;
            default:
                holder.rankImg.setVisibility(View.GONE);
                holder.rankTv.setVisibility(View.VISIBLE);
                holder.rankTv.setText(position + 1 + "");
                break;
        }
//        ImageLoader.getInstance().displayImage(fiuUserListBean.getData().getRows().get(position).getAvatar_url(), holder.headImg);
        GlideUtils.displayImage(fiuUserListBean.getData().getRows().get(position).getAvatar_url(), holder.headImg);
        holder.nameTv.setText(fiuUserListBean.getData().getRows().get(position).getNickname());
        holder.rank.setText("LV" + fiuUserListBean.getData().getRows().get(position).getRank_id());
        if (fiuUserListBean.getData().getRows().get(position).getIs_expert() == 1) {
            holder.vImg.setVisibility(View.VISIBLE);
            holder.infoTv.setText(fiuUserListBean.getData().getRows().get(position).getExpert_label() + " | " + fiuUserListBean.getData().getRows().get(position).getExpert_info());
        } else {
            holder.vImg.setVisibility(View.GONE);
            holder.infoTv.setText(fiuUserListBean.getData().getRows().get(position).getSummary());
        }
        return convertView;
    }

    static class ViewHolder {
        ImageView rankImg;
        TextView rankTv;
        RoundedImageView headImg;
        RoundedImageView vImg;
        TextView nameTv;
        TextView rank;
        TextView infoTv;
    }
}
