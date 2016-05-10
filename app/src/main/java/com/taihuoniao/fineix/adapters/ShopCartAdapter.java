package com.taihuoniao.fineix.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.main.MainApplication;
import com.taihuoniao.fineix.product.MyGoodsDetailsActivity;
import com.taihuoniao.fineix.product.ShopCarActivity;

import java.text.DecimalFormat;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by android on 2016/3/3.
 */
public class ShopCartAdapter extends BaseAdapter {
    private LayoutInflater inflater = null;
    private List<Map<String, Object>> list;
    private ShopCarActivity shopCarActivity;
    private Context context;
    private DecimalFormat df = null;
    public BitmapUtils bitmapUtils_listview = null;

    public ShopCartAdapter(List<Map<String, Object>> list, ShopCarActivity shopCarActivity, Context context) {
        this.shopCarActivity = shopCarActivity;
        this.list = list;
        this.context = context;
        inflater = (LayoutInflater) shopCarActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        String diskCachePath = MainApplication.getContext().getCacheDirPath();
        bitmapUtils_listview = new BitmapUtils(context, diskCachePath)
                .configMemoryCacheEnabled(true)
                .configDefaultCacheExpiry(1024 * 1024 * 4)
                .configDefaultBitmapMaxSize(300, 300)
                .configDefaultBitmapConfig(Bitmap.Config.ALPHA_8)
//                .configDefaultLoadingImage(R.mipmap.default_shopcart)
//                .configDefaultLoadFailedImage(R.mipmap.default_shopcart)
                .configThreadPoolSize(5)
                .configDefaultImageLoadAnimation(
                        AnimationUtils.loadAnimation(context, R.anim.fade_in));
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder mHolder = null;

        if (convertView == null) {
            mHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_shopcart, parent, false);
            mHolder.mCount = (TextView) convertView.findViewById(R.id.tv_count_shopcart_item);
            mHolder.mColor = (TextView) convertView.findViewById(R.id.tv_color_shopcart_item);
            mHolder.mPrice = (TextView) convertView.findViewById(R.id.tv_price_shopcart_item);
            mHolder.mImageGoods = (ImageView) convertView.findViewById(R.id.image_shopcart_item);
            mHolder.mTittle = (TextView) convertView.findViewById(R.id.tv_title_shopcart_item);
            mHolder.mCheckBox = (CheckBox) convertView.findViewById(R.id.checkbox_shopcart_item);
            mHolder.mRightItem = (LinearLayout) convertView.findViewById(R.id.linear_item_right);
            convertView.setTag(mHolder);
        } else {
            mHolder = (ViewHolder) convertView.getTag();
        }
        mHolder.mCheckBox.setChecked((Boolean) list.get(position).get("status"));
        mHolder.mCount.setText("数量 * " + list.get(position).get("keyCount") + "");
        mHolder.mTittle.setText(list.get(position).get("keyTitle") + "");
        if ((list.get(position).get("keyColor")).equals("null")) {
            mHolder.mColor.setText("颜色/分类：默认");
        } else {
            mHolder.mColor.setText("颜色/分类：" + list.get(position).get("keyColor") + "");
        }
        mHolder.mPrice.setText("¥" + list.get(position).get("keyPrice") + "");
        bitmapUtils_listview.display(mHolder.mImageGoods, list.get(position).get("keyImage") + "");

        mHolder.mRightItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (shopCarActivity.getChange() == 0) {
                    Intent intent = new Intent(shopCarActivity, MyGoodsDetailsActivity.class);
                    intent.putExtra("id", list.get(position).get("keyProductId") + "");
                    shopCarActivity.startActivity(intent);
                }
            }
        });
        return convertView;
    }

    public class ViewHolder {
        public ImageView mImageGoods;
        TextView mTittle;
        TextView mPrice;
        TextView mCount;
        TextView mColor;
        public LinearLayout mRightItem;
        public CheckBox mCheckBox;
    }

    // 重新加载Listview，即刷新
    public void reloadListView() {
        notifyDataSetChanged();
    }

    // 删除一批数据
    public void removeItems(Collection<? extends Map<String, Object>> data) {
        list.removeAll(data);
        notifyDataSetChanged();
    }

}
