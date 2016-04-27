package com.taihuoniao.fineix.adapters;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.beans.FocusFansItem;
import com.taihuoniao.fineix.utils.Util;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author lilin
 *         created at 2016/4/22 19:00
 */
public class FocusFansAdapter extends CommonBaseAdapter<FocusFansItem> {
    private ImageLoader imageLoader;
    public FocusFansAdapter(List<FocusFansItem> list, Activity activity) {
        super(list, activity);
        this.imageLoader=ImageLoader.getInstance();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        FocusFansItem item = list.get(position);
        ViewHolder holder=null;
        if (convertView==null){
            convertView= Util.inflateView(activity,R.layout.item_focus_fans,null);
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder=(ViewHolder)convertView.getTag();
        }

        imageLoader.displayImage(item.follows.avatar_url,holder.riv,options);
        holder.tv_name.setText(item.follows.nickname);
        holder.tv_desc.setText(item.follows.nickname);
        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.riv)
        ImageView riv;
        @Bind(R.id.tv_name)
        TextView tv_name;
        @Bind(R.id.tv_desc)
        TextView tv_desc;
        @Bind(R.id.btn)
        Button btn;
        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
