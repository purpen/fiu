package com.taihuoniao.fineix.adapters;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.beans.RedPacketData;
import com.taihuoniao.fineix.utils.Util;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author lilin
 * created at 2016/5/24 14:44
 */
public class UnUsableRedPacketAdapter extends CommonBaseAdapter<RedPacketData.RedPacketItem>{
    public UnUsableRedPacketAdapter(List list, Activity activity){
        super(list,activity);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final RedPacketData.RedPacketItem item = list.get(position);
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = Util.inflateView(activity, R.layout.account_redbag_timeout, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_redbag_code.setText(item.code);
        if (item.min_amount<=0){
            holder.tv_min_money.setText("无限制");
        }else {
            holder.tv_min_money.setText(String.format("最低使用限额: %s元",item.min_amount));
        }
        holder.tv_money.setText(item.amount);
        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.linear_bg_redbag)
        LinearLayout linear_bg_redbag;

        @Bind(R.id.tv_redbag_code)
        TextView tv_redbag_code;

        @Bind(R.id.tv_min_money)
        TextView tv_min_money;

        @Bind(R.id.tv_money)
        TextView tv_money;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
