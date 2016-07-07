package com.taihuoniao.fineix.adapters;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.beans.QingJingListBean;
import com.taihuoniao.fineix.utils.SceneTitleSetUtils;
import com.taihuoniao.fineix.utils.Util;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * @author lilin
 * created at 2016/5/5 19:13
 */
public class OrderedQJAdapter extends CommonBaseAdapter<QingJingListBean.QingJingItem>{
    private ImageLoader imageLoader;
    public OrderedQJAdapter(List<QingJingListBean.QingJingItem> list, Activity activity){
        super(list,activity);
        this.imageLoader=ImageLoader.getInstance();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final QingJingListBean.QingJingItem item = list.get(position);
        ViewHolder holder;
        if (convertView==null){
            convertView = Util.inflateView(R.layout.item_ordered_qj, null);
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder=(ViewHolder)convertView.getTag();
        }

        imageLoader.displayImage(item.getCover_url(),holder.iv_cover,options);
        holder.tv_title.setText(item.getTitle());
        SceneTitleSetUtils.setTitle(holder.tv_title, holder.item_frame);
        holder.tv_desc.setText(item.getAddress());
        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.iv_cover)
        ImageView iv_cover;
        @Bind(R.id.tv_title)
        TextView tv_title;
        @Bind(R.id.tv_desc)
        TextView tv_desc;
        @Bind(R.id.item_frame)
        FrameLayout item_frame;
        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
