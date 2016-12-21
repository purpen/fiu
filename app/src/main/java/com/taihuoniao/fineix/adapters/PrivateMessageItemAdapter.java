package com.taihuoniao.fineix.adapters;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.beans.LoginInfo;
import com.taihuoniao.fineix.beans.MessageDetailData;
import com.taihuoniao.fineix.beans.User;
import com.taihuoniao.fineix.utils.GlideUtils;
import com.taihuoniao.fineix.utils.LogUtil;
import com.taihuoniao.fineix.utils.Util;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author lilin
 * created at 2016/5/6 19:38
 */
public class PrivateMessageItemAdapter extends CommonBaseAdapter<MessageDetailData.MessageItem>{
    private ImageLoader imageLoader;
    private static final int MESSAGE_OTHERS=0;
    private static final int MESSAGE_ME=1;
    private User user;
    public PrivateMessageItemAdapter(List list, Activity activity,User user){
        super(list,activity);
        this.user=user;
        this.imageLoader=ImageLoader.getInstance();
    }

    @Override
    public int getItemViewType(int position) {
        int user_type = list.get(position).user_type;
        LogUtil.e("user_type========",user_type+"");
        if (user_type==MESSAGE_OTHERS){
            return MESSAGE_OTHERS;
        }else {
            return MESSAGE_ME;
        }
    }

    @Override
    public int getViewTypeCount() {
        return super.getViewTypeCount()+1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final MessageDetailData.MessageItem item = list.get(position);
        ViewHolder holder;
        if (convertView == null) {
            if (getItemViewType(position)==MESSAGE_OTHERS){
                convertView = Util.inflateView(R.layout.item_msg_others, null);
            }else {
                convertView = Util.inflateView(R.layout.item_msg_me, null);
            }
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (getItemViewType(position)==MESSAGE_OTHERS){
            GlideUtils.displayImage(user.avatar,holder.iv);
        }else {
            GlideUtils.displayImage(LoginInfo.getHeadPicUrl(),holder.iv);
        }
        holder.tv_msg.setText(item.content);
        holder.tv_time.setText(item.created_at);
        if(position>0&&list.get(position).created_at.equals(list.get(position-1).created_at)){
            holder.tv_time.setVisibility(View.GONE);
        }else{
            holder.tv_time.setVisibility(View.VISIBLE);
        }
        return convertView;
    }



    static class ViewHolder {
        @Bind(R.id.iv)
        ImageView iv;
        @Bind(R.id.tv_msg)
        TextView tv_msg;
        @Bind(R.id.tv_time)
        TextView tv_time;
        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
