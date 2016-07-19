package com.taihuoniao.fineix.adapters;

import android.app.Activity;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.BackgroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.beans.QingJingListBean;
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
            convertView = Util.inflateView(R.layout.item_qingjing_list, null);
            holder=new ViewHolder(convertView);
            ViewGroup.LayoutParams lp = holder.container.getLayoutParams();
            lp.height = activity.getResources().getDimensionPixelSize(R.dimen.dp300);
            holder.container.setLayoutParams(lp);
            convertView.setTag(holder);
        }else {
            holder=(ViewHolder)convertView.getTag();
        }

        imageLoader.displayImage(item.getCover_url(),holder.iv_cover,options);
        SpannableStringBuilder style = new SpannableStringBuilder(list.get(position).getTitle());
        BackgroundColorSpan backgroundColorSpan = new BackgroundColorSpan(activity.getResources().getColor(R.color.black));
        style.setSpan(backgroundColorSpan, 0, list.get(position).getTitle().length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        holder.tv_title.setText(style);
//        SceneTitleSetUtils.setTitle(holder.tv_title, holder.item_frame);
        holder.tv_desc.setText(item.getAddress());
        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.item_qingjing_list_container)
        RelativeLayout container;
        @Bind(R.id.item_qingjing_list_background)
        ImageView iv_cover;
        @Bind(R.id.item_qingjing_list_title)
        TextView tv_title;
        @Bind(R.id.item_qingjing_list_address)
        TextView tv_desc;
        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
