package com.taihuoniao.fineix.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.beans.RelationProductsBean;
import com.taihuoniao.fineix.product.MyGoodsDetailsActivity;

import java.util.List;

/**
 * Created by taihuoniao on 2016/2/23.
 */
public class GoodsDetailsGridViewAdapter extends BaseAdapter {
    private Context context;
    private List<RelationProductsBean> relationProductsBeanList;
    private BitmapUtils bitmapUtils;

    public GoodsDetailsGridViewAdapter(Context context, List<RelationProductsBean> relationProductsBeanList) {
        this.context = context;
        this.relationProductsBeanList = relationProductsBeanList;
        String diskCachePath = context.getExternalCacheDir().getAbsolutePath();
        bitmapUtils = new BitmapUtils(context, diskCachePath)
                .configMemoryCacheEnabled(true)
                .configDefaultCacheExpiry(1024 * 1024 * 4)
                .configDefaultBitmapMaxSize(300, 300)
                .configDefaultBitmapConfig(Bitmap.Config.ALPHA_8)
//                .configDefaultLoadingImage(R.mipmap.default_backround)
//                .configDefaultLoadFailedImage(R.mipmap.default_backround)
                .configThreadPoolSize(5)
                .configDefaultImageLoadAnimation(
                        AnimationUtils.loadAnimation(context, R.anim.fade_in));
    }

    @Override
    public int getCount() {
        return relationProductsBeanList.size();
    }

    @Override
    public Object getItem(int position) {
        return relationProductsBeanList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.item_goodsdetails_gridview, null);
            holder.img = (ImageView) convertView.findViewById(R.id.item_goodsdetails_gridview_img);
            holder.titleTv = (TextView) convertView.findViewById(R.id.item_goodsdetails_gridview_title);
            holder.priceTv = (TextView) convertView.findViewById(R.id.item_goodsdetails_gridview_price);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        bitmapUtils.display(holder.img, relationProductsBeanList.get(position).getCover_url());
        holder.titleTv.setText(relationProductsBeanList.get(position).getTitle());
        holder.priceTv.setText("¥" + relationProductsBeanList.get(position).getSale_price());
        final int i = position;
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
                    Intent intent = new Intent(context, MyGoodsDetailsActivity.class);
//                intent.addFlags(Intent.F)
                    intent.putExtra("id", relationProductsBeanList.get(i).get_id());
                    context.startActivity(intent);

            }
        });
        return convertView;
    }

    class ViewHolder {
        private ImageView img;
        private TextView titleTv;
        private TextView priceTv;
    }

}
