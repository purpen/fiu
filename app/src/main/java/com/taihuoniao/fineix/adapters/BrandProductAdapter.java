package com.taihuoniao.fineix.adapters;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.beans.ProductBean;
import com.taihuoniao.fineix.main.MainApplication;
import com.taihuoniao.fineix.product.BuyGoodsDetailsActivity;
import com.taihuoniao.fineix.utils.DensityUtils;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by taihuoniao on 2016/8/29.
 */
public class BrandProductAdapter extends BaseAdapter {
    private Activity activity;
    private List<ProductBean.ProductListItem> list;

    public BrandProductAdapter(Activity activity, List<ProductBean.ProductListItem> list) {
        this.activity = activity;
        this.list = list;
    }

    @Override
    public int getCount() {
        return (list.size() + 1) / 2;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(parent.getContext(), R.layout.item_brand_product_list, null);
            holder = new ViewHolder(convertView);
            RelativeLayout.LayoutParams layoutParams1 = (RelativeLayout.LayoutParams) holder.productImgLeft.getLayoutParams();
            layoutParams1.width = (MainApplication.getContext().getScreenWidth() - DensityUtils.dp2px(parent.getContext(), 45)) / 2;
            layoutParams1.height = layoutParams1.width;
            holder.productImgLeft.setLayoutParams(layoutParams1);
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) holder.productImgRight.getLayoutParams();
            layoutParams.width = layoutParams1.width;
            layoutParams.height = layoutParams1.width;
            holder.productImgRight.setLayoutParams(layoutParams);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Log.e("<<<品牌下的产品", "list.size=" + list.size());
        final int leftPosition = position * 2;
        final int rightPosition = leftPosition + 1;
        ImageLoader.getInstance().displayImage(list.get(leftPosition).getCover_url(), holder.productImgLeft);
        holder.nameLeft.setText(list.get(leftPosition).getTitle());
        holder.priceLeft.setText("¥" + list.get(leftPosition).getSale_price());
        if(list.get(leftPosition).getStage()==9){
            holder.priceLeft.setVisibility(View.VISIBLE);
        }else{
            holder.priceLeft.setVisibility(View.INVISIBLE);
        }
        if (rightPosition < list.size()) {
            holder.rightContainer.setVisibility(View.VISIBLE);
            ImageLoader.getInstance().displayImage(list.get(rightPosition).getCover_url(), holder.productImgRight);
            holder.nameRight.setText(list.get(rightPosition).getTitle());
            holder.priceRight.setText("¥" + list.get(rightPosition).getSale_price());
            if(list.get(rightPosition).getStage()==9){
                holder.priceRight.setVisibility(View.VISIBLE);
            }else{
                holder.priceRight.setVisibility(View.INVISIBLE);
            }
        } else {
            holder.rightContainer.setVisibility(View.GONE);
        }
        holder.leftContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
//                switch (list.get(leftPosition).getStage()) {
//                    case 9:
//                        Log.e("<<<", "可购买");
                intent.setClass(activity, BuyGoodsDetailsActivity.class);
//                        break;
//                    default:
//                        Log.e("<<<", "不可购买");
//                        intent.setClass(activity, GoodsDetailActivity.class);
//                        break;
//                }
                intent.putExtra("id", list.get(leftPosition).get_id());
                activity.startActivity(intent);
            }
        });
        holder.rightContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                        intent.setClass(activity, BuyGoodsDetailsActivity.class);
                intent.putExtra("id", list.get(rightPosition).get_id());
                activity.startActivity(intent);
            }
        });
        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.left_container)
        RelativeLayout leftContainer;
        @Bind(R.id.product_img_left)
        ImageView productImgLeft;
        @Bind(R.id.name_left)
        TextView nameLeft;
        @Bind(R.id.price_left)
        TextView priceLeft;
        @Bind(R.id.right_container)
        RelativeLayout rightContainer;
        @Bind(R.id.product_img_right)
        ImageView productImgRight;
        @Bind(R.id.name_right)
        TextView nameRight;
        @Bind(R.id.price_right)
        TextView priceRight;


        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
