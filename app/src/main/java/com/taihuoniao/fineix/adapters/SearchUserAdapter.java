package com.taihuoniao.fineix.adapters;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.beans.ItemUserSearch;
import com.taihuoniao.fineix.utils.GlideUtils;
import com.taihuoniao.fineix.utils.Util;
import com.taihuoniao.fineix.view.roundImageView.RoundedImageView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author lilin
 *         created at 2016/8/18 17:48
 */
public class SearchUserAdapter extends CommonBaseAdapter<ItemUserSearch> {

    public SearchUserAdapter(List<ItemUserSearch> list, Activity activity) {
        super(list, activity);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ItemUserSearch item = list.get(position);
        ViewHolder holder;
        if (convertView == null) {
            convertView = Util.inflateView(R.layout.item_search_user, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        GlideUtils.displayImage(item.avatar_url, holder.riv);
        holder.tvName.setText(item.nickname);
        holder.tvSummary.setText(item.summary);
        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.riv)
        RoundedImageView riv;
        @Bind(R.id.tv_name)
        TextView tvName;
        @Bind(R.id.tv_summary)
        TextView tvSummary;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
