package com.taihuoniao.fineix.adapters;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.beans.User;
import com.taihuoniao.fineix.utils.Util;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author lilin
 *         created at 2016/4/11 14:01
 */
public class FocusInterestAdapter extends CommonBaseAdapter<User> {
    public FocusInterestAdapter(List<User> list, Activity activity) {
        super(list, activity);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        User item = list.get(position);
        ViewHolder holder;
        if (convertView == null) {
            convertView = Util.inflateView(R.layout.item_foucus_interest, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ImageLoader.getInstance().displayImage(item.medium_avatar_url, holder.riv);
        if (item.identify.is_expert == 1) {
            holder.v.setVisibility(View.VISIBLE);
        } else {
            holder.v.setVisibility(View.GONE);
        }
        holder.tv_nickname.setText(item.nickname);
        holder.tv_info.setText(item.expert_label);
        if (item.is_love == 1) {
            holder.btn_focus.setPadding(activity.getResources().getDimensionPixelSize(R.dimen.dp10), 0, activity.getResources().getDimensionPixelSize(R.dimen.dp10), 0);
            holder.btn_focus.setText(R.string.focused);
            holder.btn_focus.setTextColor(activity.getResources().getColor(android.R.color.white));
            holder.btn_focus.setBackgroundResource(R.drawable.border_radius5_pressed);
            holder.btn_focus.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.focus_pic, 0, 0, 0);
        } else {
            holder.btn_focus.setPadding(activity.getResources().getDimensionPixelSize(R.dimen.dp16), 0, activity.getResources().getDimensionPixelSize(R.dimen.dp16), 0);
            holder.btn_focus.setText(R.string.focus);
            holder.btn_focus.setTextColor(activity.getResources().getColor(android.R.color.black));
            holder.btn_focus.setBackgroundResource(R.drawable.shape_subscribe_theme);
            holder.btn_focus.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.unfocus_pic, 0, 0, 0);
        }
        setClickListener(holder.btn_focus, item);
        return convertView;
    }

    private void setClickListener(Button btn_focus, final User item) {
        btn_focus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (item.is_love == 1) {
                    item.is_love = 0;
                } else {
                    item.is_love = 1;
                }
                notifyDataSetChanged();
            }
        });
    }

    static class ViewHolder {
        @Bind(R.id.riv)
        ImageView riv;
        @Bind(R.id.v)
        ImageView v;
        @Bind(R.id.tv_nickname)
        TextView tv_nickname;
        @Bind(R.id.tv_info)
        TextView tv_info;
        @Bind(R.id.btn_focus)
        Button btn_focus;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
