package com.taihuoniao.fineix.adapters;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.beans.PrivateMessageListData;
import com.taihuoniao.fineix.utils.GlideUtils;
import com.taihuoniao.fineix.utils.Util;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author lilin
 * created at 2016/5/6 19:38
 */
public class PrivateMessageListAdapter extends CommonBaseAdapter<PrivateMessageListData.RowItem>{
    public PrivateMessageListAdapter(List list, Activity activity){
        super(list,activity);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final PrivateMessageListData.RowItem item = list.get(position);
        ViewHolder holder;
        if (convertView==null){
            convertView = Util.inflateView(R.layout.item_message_list, null);
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder=(ViewHolder)convertView.getTag();
        }

        GlideUtils.displayImage(item.users.from_user.big_avatar_url,holder.riv);
        holder.tv_name.setText(item.users.from_user.nickname);
        holder.tv_desc.setText(item.last_content.content);
        holder.tv_desc.setTextColor(activity.getResources().getColor(R.color.color_333));
        holder.tv_time.setText(item.last_time_at);
        if (item.is_read>0){
            holder.tv_num.setVisibility(View.GONE);
        }else {//0是未读
            holder.tv_num.setVisibility(View.VISIBLE);
        }
        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.riv)
        ImageView riv;
        @Bind(R.id.tv_name)
        TextView tv_name;
        @Bind(R.id.tv_desc)
        TextView tv_desc;
        @Bind(R.id.tv_time)
        TextView tv_time;
        @Bind(R.id.tv_num)
        TextView tv_num;
        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
