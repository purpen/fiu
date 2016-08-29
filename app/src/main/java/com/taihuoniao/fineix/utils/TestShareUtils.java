package com.taihuoniao.fineix.utils;

import android.content.Context;
import android.text.Layout;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.beans.QJDetailBean;
import com.taihuoniao.fineix.main.MainApplication;
import com.taihuoniao.fineix.view.roundImageView.RoundedImageView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by taihuoniao on 2016/5/24.
 */
public class TestShareUtils {
    public boolean isShowL;
    public String title1, title2;
    int layout;

    //根据position动态改变控件的位置
    public View selectStyle(final Context context, int position, QJDetailBean sceneDetails, final double b) {
        selectStyle(position);
        View view = View.inflate(context, layout, null);
        final ViewHolder holder = new ViewHolder(view);
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) holder.imgContainer.getLayoutParams();
        lp.width = (int) (b * MainApplication.getContext().getScreenWidth());
        lp.height = lp.width;
        holder.imgContainer.setLayoutParams(lp);
//        //添加商品标签
//        for (final QJDetailBean.DataBean.ProductBean productBean : sceneDetails.getData().getProduct()) {
//            final RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
//            final LabelView labelView = new LabelView(context);
//            labelView.nameTv.setText(productBean.getTitle());
//            labelView.setLayoutParams(layoutParams);
//            if (productBean.getLoc() == 2) {
//                labelView.nameTv.setBackgroundResource(R.drawable.label_left);
//                RelativeLayout.LayoutParams layoutParams1 = (RelativeLayout.LayoutParams) labelView.pointContainer.getLayoutParams();
//                layoutParams1.leftMargin = (int) labelView.labelMargin;
//                labelView.pointContainer.setLayoutParams(layoutParams1);
//            }
//            labelView.post(new Runnable() {
//                @Override
//                public void run() {
//                    if (productBean.getLoc() == 2) {
//                        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) labelView.getLayoutParams();
//                        lp.leftMargin = (int) (productBean.getX() * b * MainApplication.getContext().getScreenWidth() - labelView.labelMargin - labelView.pointWidth / 2);
//                        lp.topMargin = (int) (productBean.getY() * b * MainApplication.getContext().getScreenWidth() - labelView.getMeasuredHeight() + labelView.pointWidth / 2);
//                        labelView.setLayoutParams(lp);
//                    } else {
//                        labelView.nameTv.setBackgroundResource(R.drawable.label_right);
//                        RelativeLayout.LayoutParams layoutParams1 = (RelativeLayout.LayoutParams) labelView.pointContainer.getLayoutParams();
//                        layoutParams1.leftMargin = (int) (labelView.nameTv.getMeasuredWidth() - labelView.pointWidth - labelView.labelMargin);
//                        labelView.pointContainer.setLayoutParams(layoutParams1);
//                        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) labelView.getLayoutParams();
//                        lp.leftMargin = (int) (productBean.getX() * b * MainApplication.getContext().getScreenWidth() - labelView.getMeasuredWidth() + labelView.labelMargin + labelView.pointWidth / 2);
//                        lp.topMargin = (int) (productBean.getY() * b * MainApplication.getContext().getScreenWidth() - labelView.getMeasuredHeight() + labelView.pointWidth / 2);
//                        labelView.setLayoutParams(lp);
//                    }
//                }
//            });
//            holder.imgContainer.addView(labelView);
//            labelView.wave();
//        }
        ImageLoader.getInstance().displayImage(sceneDetails.getData().getCover_url(), holder.backgroundImg);
        holder.qjTitleTv.setText(sceneDetails.getData().getTitle());
        holder.qjTitleTv.post(new Runnable() {
            @Override
            public void run() {
                if (holder.qjTitleTv.getLineCount() >= 2) {
                    Layout layout = holder.qjTitleTv.getLayout();
                    StringBuilder SrcStr = new StringBuilder(holder.qjTitleTv.getText().toString());
                    String str0 = SrcStr.subSequence(layout.getLineStart(0), layout.getLineEnd(0)).toString();
                    String str1 = SrcStr.subSequence(layout.getLineStart(1), layout.getLineEnd(1)).toString();
                    holder.qjTitleTv2.setText(str0);
                    holder.qjTitleTv.setText(str1);
                    holder.qjTitleTv2.setVisibility(View.VISIBLE);
                    title1 = str0;
                    title2 = str1;
                } else {
                    holder.qjTitleTv2.setVisibility(View.GONE);
                }
            }
        });
        ImageLoader.getInstance().displayImage(sceneDetails.getData().getUser_info().getAvatar_url(), holder.userImg);
        if (sceneDetails.getData().getUser_info().getIs_expert() == 1) {
            holder.vImg.setVisibility(View.VISIBLE);
        } else {
            holder.vImg.setVisibility(View.GONE);
        }
        holder.userName.setText(sceneDetails.getData().getUser_info().getNickname());
        holder.publishTime.setText(sceneDetails.getData().getCreated_at());
        holder.locationTv.setText(sceneDetails.getData().getAddress());
        holder.des.setText(sceneDetails.getData().getDes());
//        holder.des.post(new Runnable() {
//            @Override
//            public void run() {
//                if (holder.des.getLineCount() >3) {
//                    Layout layout = holder.des.getLayout();
//                    StringBuilder SrcStr = new StringBuilder(holder.des.getText().toString());
//                    String str0 = SrcStr.subSequence(layout.getLineStart(0), layout.getLineEnd(2)).toString();
//                    SpannableString spannableString = new SpannableString(str0);
//                    ImageSpan imageSpan = new ImageSpan(context,R.mipmap.share_des_add);
//                    spannableString.setSpan(imageSpan,str0.length()-2,str0.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//                    holder.des.setText(spannableString);
//                }
//            }
//        });
        return view;
    }

    private void selectStyle(int position) {
        switch (position) {
//            case 1:
//                layout = R.layout.view_share_style2;
//                break;
//            case 2:
//                layout = R.layout.view_share_style3;
//                break;
//            case 3:
//                layout = R.layout.view_share_style4;
//                break;
            default:
                layout = R.layout.view_share_style1;
                break;
        }
    }

    public static class ViewHolder {
        @Bind(R.id.background_img)
        public ImageView backgroundImg;
        @Bind(R.id.qj_title_tv)
        public TextView qjTitleTv;
        @Bind(R.id.qj_title_tv2)
        public TextView qjTitleTv2;
        @Bind(R.id.img_container)
        public RelativeLayout imgContainer;
        @Bind(R.id.user_img)
        public RoundedImageView userImg;
        @Bind(R.id.v_img)
        public ImageView vImg;
        @Bind(R.id.user_name)
        public TextView userName;
        @Bind(R.id.publish_time)
        public TextView publishTime;
        @Bind(R.id.location_tv)
        public TextView locationTv;
        @Bind(R.id.des)
        public TextView des;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

//    private  void setColor() {
//        userName.setTextColor(context.getResources().getColor(R.color.black));
//        userInfo.setTextColor(context.getResources().getColor(R.color.black969696));
//        locationTv.setTextColor(context.getResources().getColor(R.color.black969696));
//        locationImg.setImageResource(R.mipmap.location_height_22px);
//        desTv.setTextColor(context.getResources().getColor(R.color.black969696));
//        line.setTextColor(context.getResources().getColor(R.color.black969696));
//    }


}
