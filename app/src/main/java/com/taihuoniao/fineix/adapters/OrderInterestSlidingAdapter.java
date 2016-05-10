package com.taihuoniao.fineix.adapters;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Gallery;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.beans.QingJingListBean;
import com.taihuoniao.fineix.utils.Util;
import com.taihuoniao.fineix.view.SlidingFocusImageView;
import com.taihuoniao.fineix.view.roundImageView.RoundedImageView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author lilin
 * created at 2016/5/9 17:55
 */
public class OrderInterestSlidingAdapter extends CommonBaseAdapter<QingJingListBean.QingJingItem>{
    private SlidingFocusImageView sfiv;
    public OrderInterestSlidingAdapter(SlidingFocusImageView sfiv,List list, Activity activity){
        super(list,activity);
        this.sfiv=sfiv;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        QingJingListBean.QingJingItem item=list.get(position);
        ViewHolder holder =null;
        if (convertView==null) {
            convertView=Util.inflateView(activity,R.layout.item_order_interest,null);
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);
            holder.iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }else {
            holder= (ViewHolder)convertView.getTag();
        }

        ImageLoader.getInstance().displayImage(item.getCover_url(),holder.iv);
        if (sfiv.getSelectedItemPosition()==position){
            convertView.setLayoutParams(new Gallery.LayoutParams(Util.getScreenWidth()-300, ViewGroup.LayoutParams.WRAP_CONTENT));
        }else {
            convertView.setScaleY(0.9f);
            convertView.setScaleX(0.9f);
        }

        holder.tv_title.setText(item.getTitle());
        holder.tv_desc.setText(item.getAddress());
        setOnClickListener(holder.ibtn,item);
        return convertView;
    }

    private void setOnClickListener(ImageButton itbn,QingJingListBean.QingJingItem item){
        itbn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO
            }
        });
    }

    static class ViewHolder{
        @Bind(R.id.iv)
        RoundedImageView iv;
        @Bind(R.id.tv_title)
        TextView tv_title;
        @Bind(R.id.tv_desc)
        TextView tv_desc;
        @Bind(R.id.ibtn)
        ImageButton ibtn;
        public ViewHolder(View view){
            ButterKnife.bind(this,view);
        }
    }
}
