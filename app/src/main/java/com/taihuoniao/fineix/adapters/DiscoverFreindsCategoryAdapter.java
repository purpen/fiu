package com.taihuoniao.fineix.adapters;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.beans.DiscoverBean;
import com.taihuoniao.fineix.utils.GlideUtils;
import com.taihuoniao.fineix.utils.Util;
import com.taihuoniao.fineix.view.roundImageView.RoundedImageView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author lilin    发现---》好友（三列）
 */
public class DiscoverFreindsCategoryAdapter extends CommonBaseAdapter<DiscoverBean.UsersBean> {
    public DiscoverFreindsCategoryAdapter(List<DiscoverBean.UsersBean> list, Activity activity) {
        super(list, activity);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        DiscoverBean.UsersBean item = list.get(position);
        ViewHolder holder;
        if (convertView == null) {
            convertView = Util.inflateView(R.layout.item_discover_gv_category, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        GlideUtils.displayImageWithId(item.pic, holder.iv);

        holder.tvTag.setText(item.nickname);

        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.iv)
        RoundedImageView iv;
        @Bind(R.id.tv_tag)
        TextView tvTag;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
