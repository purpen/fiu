package com.taihuoniao.fineix.utils;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.beans.QJDetailBean;
import com.taihuoniao.fineix.beans.SceneList;
import com.taihuoniao.fineix.main.MainApplication;
import com.taihuoniao.fineix.product.BuyGoodsDetailsActivity;
import com.taihuoniao.fineix.view.LabelView;
import com.taihuoniao.fineix.view.roundImageView.RoundedImageView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by taihuoniao on 2016/5/24.
 */
public class TestShareUtils {
    private double height, width;
    private int layout;

    //根据position动态改变控件的位置
    public View selectStyle(final Context context, int position, QJDetailBean sceneDetails, final double b, double height, double width) {
        this.height = height;
        this.width = width;
        selectStyle(position);
        View view = View.inflate(context, layout, null);
        final ViewHolder holder = new ViewHolder(view);
        ImageLoader.getInstance().displayImage(sceneDetails.getData().getCover_url(), holder.backgroundImg);
        SceneTitleSetUtils.setTitle(holder.qjTitleTv, holder.qjTitleTv2, sceneDetails.getData().getTitle());
        holder.userName.setText(sceneDetails.getData().getUser_info().getNickname());
        holder.publishTime.setText(sceneDetails.getData().getCreated_at());
        holder.locationTv.setText(sceneDetails.getData().getAddress());
        holder.des.setText(sceneDetails.getData().getDes());
        setStyle(position, holder);
        //添加商品标签
        holder.labelContainer.removeAllViews();
        for (final SceneList.DataBean.RowsBean.ProductBean productBean : sceneDetails.getData().getProduct()) {
            final RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            final LabelView labelView = new LabelView(context);
            labelView.nameTv.setText(productBean.getTitle());
            labelView.setLayoutParams(layoutParams);
            if (productBean.getLoc() == 2) {
                labelView.nameTv.setBackgroundResource(R.drawable.label_left);
                RelativeLayout.LayoutParams layoutParams1 = (RelativeLayout.LayoutParams) labelView.pointContainer.getLayoutParams();
                layoutParams1.leftMargin = (int) labelView.labelMargin;
                labelView.pointContainer.setLayoutParams(layoutParams1);
            }
            labelView.post(new Runnable() {
                @Override
                public void run() {
                    if (productBean.getLoc() == 2) {
                        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) labelView.getLayoutParams();
                        lp.leftMargin = (int) (productBean.getX() * holder.labelContainer.getWidth() - labelView.labelMargin - labelView.pointWidth / 2);
                        lp.topMargin = (int) (productBean.getY() * holder.labelContainer.getHeight() - labelView.getMeasuredHeight() + labelView.pointWidth / 2);
                        labelView.setLayoutParams(lp);
                    } else {
                        labelView.nameTv.setBackgroundResource(R.drawable.label_right);
                        RelativeLayout.LayoutParams layoutParams1 = (RelativeLayout.LayoutParams) labelView.pointContainer.getLayoutParams();
                        layoutParams1.leftMargin = (int) (labelView.nameTv.getMeasuredWidth() - labelView.pointWidth - labelView.labelMargin);
                        labelView.pointContainer.setLayoutParams(layoutParams1);
                        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) labelView.getLayoutParams();
                        lp.leftMargin = (int) (productBean.getX() * holder.labelContainer.getWidth() - labelView.getMeasuredWidth() + labelView.labelMargin + labelView.pointWidth / 2);
                        lp.topMargin = (int) (productBean.getY() * holder.labelContainer.getHeight() - labelView.getMeasuredHeight() + labelView.pointWidth / 2);
                        labelView.setLayoutParams(lp);
                    }
                }
            });
            holder.labelContainer.addView(labelView);
            labelView.wave();
        }
        return view;
    }

    private void setStyle(int position, ViewHolder holder) {
        RelativeLayout.LayoutParams imgLp;
        RelativeLayout.LayoutParams title2Lp;
        RelativeLayout.LayoutParams linear1Lp;
        RelativeLayout.LayoutParams desLp;
        switch (position) {
            case 1:
                imgLp = (RelativeLayout.LayoutParams) holder.backgroundImg.getLayoutParams();
                imgLp.topMargin = (int) (height * 214 / 730);
                imgLp.height = (int) (height * 337 / 730);
                imgLp.width = (int) (height * 336 / 730);
                holder.backgroundImg.setLayoutParams(imgLp);
                RelativeLayout.LayoutParams containerLp = (RelativeLayout.LayoutParams) holder.labelContainer.getLayoutParams();
                containerLp.topMargin = imgLp.topMargin;
                containerLp.height = imgLp.height;
                containerLp.width = imgLp.width;
                holder.labelContainer.setLayoutParams(containerLp);
                title2Lp = (RelativeLayout.LayoutParams) holder.qjTitleTv2.getLayoutParams();
                title2Lp.topMargin = (int) (height * 68 / 730);
                holder.qjTitleTv2.setLayoutParams(title2Lp);
                break;
            case 2:
                holder.backgroundImg.setCornerRadius((float) (height * 57 / 730), 0, (float) (height * 57 / 730), 0);
                imgLp = (RelativeLayout.LayoutParams) holder.backgroundImg.getLayoutParams();
                imgLp.topMargin = (int) (height * 57 / 730);
                imgLp.height = (int) (height * 387 / 730);
                imgLp.width = (int) (height * 387 / 730);
                holder.backgroundImg.setLayoutParams(imgLp);
                RelativeLayout.LayoutParams containerLp2 = (RelativeLayout.LayoutParams) holder.labelContainer.getLayoutParams();
                containerLp2.topMargin = imgLp.topMargin;
                containerLp2.height = imgLp.height;
                containerLp2.width = imgLp.width;
                holder.labelContainer.setLayoutParams(containerLp2);
                linear1Lp = (RelativeLayout.LayoutParams) holder.linear1.getLayoutParams();
                linear1Lp.topMargin = (int) (height * 552 / 730);
                holder.linear1.setLayoutParams(linear1Lp);
                desLp = (RelativeLayout.LayoutParams) holder.des.getLayoutParams();
                desLp.topMargin = (int) (height * 489 / 730);
                holder.des.setLayoutParams(desLp);
                break;
            case 3:
                imgLp = (RelativeLayout.LayoutParams) holder.backgroundImg.getLayoutParams();
                imgLp.topMargin = (int) (height * 79 / 730);
                imgLp.leftMargin = (int) (height * 21 / 730);
                imgLp.height = (int) (height * 365 / 730);
                imgLp.width = (int) (height * 365 / 730);
                holder.backgroundImg.setLayoutParams(imgLp);
                holder.backgroundImg.setCornerRadius((float) (height * 182 / 730));
                RelativeLayout.LayoutParams containerLp3 = (RelativeLayout.LayoutParams) holder.labelContainer.getLayoutParams();
                containerLp3.topMargin = imgLp.topMargin;
                containerLp3.leftMargin = imgLp.leftMargin;
                containerLp3.height = imgLp.height;
                containerLp3.width = imgLp.width;
                holder.labelContainer.setLayoutParams(containerLp3);
                linear1Lp = (RelativeLayout.LayoutParams) holder.linear1.getLayoutParams();
                linear1Lp.topMargin = (int) (height * 560 / 730);
                holder.linear1.setLayoutParams(linear1Lp);
                desLp = (RelativeLayout.LayoutParams) holder.des.getLayoutParams();
                desLp.topMargin = (int) (height * 521 / 730);
                holder.des.setLayoutParams(desLp);
                break;
            default:
                imgLp = (RelativeLayout.LayoutParams) holder.backgroundImg.getLayoutParams();
                imgLp.topMargin = (int) (height * 56 / 730);
                imgLp.height = (int) width;
                imgLp.width = (int) width;
                holder.backgroundImg.setLayoutParams(imgLp);
                RelativeLayout.LayoutParams containerLp1 = (RelativeLayout.LayoutParams) holder.labelContainer.getLayoutParams();
                containerLp1.topMargin = imgLp.topMargin;
                containerLp1.height = imgLp.height;
                containerLp1.width = imgLp.width;
                holder.labelContainer.setLayoutParams(containerLp1);
                linear1Lp = (RelativeLayout.LayoutParams) holder.linear1.getLayoutParams();
                linear1Lp.topMargin = (int) (height * 558 / 730);
                holder.linear1.setLayoutParams(linear1Lp);
                desLp = (RelativeLayout.LayoutParams) holder.des.getLayoutParams();
                desLp.topMargin = (int) (height * 478 / 730);
                holder.des.setLayoutParams(desLp);
                holder.des.setMaxHeight((int) (height * 522 / 730 - desLp.topMargin) - 1);
                break;
        }
    }

    private void selectStyle(int position) {
        switch (position) {
            case 1:
                layout = R.layout.view_share_style2;
                break;
            case 2:
                layout = R.layout.view_share_style3;
                break;
            case 3:
                layout = R.layout.view_share_style4;
                break;
            default:
                layout = R.layout.view_share_style1;
                break;
        }
    }

    public static class ViewHolder {
        @Bind(R.id.background_img)
        public RoundedImageView backgroundImg;
        @Bind(R.id.label_container)
        RelativeLayout labelContainer;
        @Bind(R.id.qj_title_tv)
        public TextView qjTitleTv;
        @Bind(R.id.qj_title_tv2)
        public TextView qjTitleTv2;
        @Bind(R.id.user_name)
        TextView userName;
        @Bind(R.id.publish_time)
        public TextView publishTime;
        @Bind(R.id.location_tv)
        public TextView locationTv;
        @Bind(R.id.des)
        public TextView des;
        @Bind(R.id.linear1)
        LinearLayout linear1;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }


}
