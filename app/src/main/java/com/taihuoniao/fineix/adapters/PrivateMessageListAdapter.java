package com.taihuoniao.fineix.adapters;
import android.app.Activity;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.beans.PrivateMessageListData;
import com.taihuoniao.fineix.utils.Util;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author lilin
 * created at 2016/5/6 19:38
 */
public class PrivateMessageListAdapter extends CommonBaseAdapter<PrivateMessageListData.RowItem>{
    private ImageLoader imageLoader;
    public PrivateMessageListAdapter(List list, Activity activity){
        super(list,activity);
        this.imageLoader=ImageLoader.getInstance();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final PrivateMessageListData.RowItem item = list.get(position);
        ViewHolder holder=null;
        if (convertView==null){
            convertView= Util.inflateView(activity, R.layout.item_message_list,null);
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder=(ViewHolder)convertView.getTag();
        }

        imageLoader.displayImage(item.users.to_user.big_avatar_url,holder.riv,options);
        holder.tv_name.setTextColor(activity.getResources().getColor(R.color.color_af8323));
        holder.tv_name.setText(item.users.to_user.nickname);
        holder.tv_desc.setText(item.last_content.content);
        holder.tv_desc.setTextSize(TypedValue.COMPLEX_UNIT_SP,14f);
        holder.tv_desc.setTextColor(activity.getResources().getColor(R.color.color_333));
        holder.tv_time.setText(item.last_time_at);
        if (item.is_read>0){
            holder.tv_num.setVisibility(View.VISIBLE);
        }else {
            holder.tv_num.setVisibility(View.GONE);
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
