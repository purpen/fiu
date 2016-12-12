package com.taihuoniao.fineix.adapters;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.beans.ActivityPrizeData;
import com.taihuoniao.fineix.beans.HttpResponse;
import com.taihuoniao.fineix.beans.LoginInfo;
import com.taihuoniao.fineix.main.MainApplication;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.DataConstants;
import com.taihuoniao.fineix.product.BuyGoodsDetailsActivity;
import com.taihuoniao.fineix.qingjingOrSceneDetails.QJDetailActivity;
import com.taihuoniao.fineix.user.OptRegisterLoginActivity;
import com.taihuoniao.fineix.utils.GlideUtils;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.utils.Util;
import com.taihuoniao.fineix.view.LabelView;
import com.taihuoniao.fineix.view.roundImageView.RoundedImageView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author lilin
 *         created at 2016/8/17 15:29
 */
public class ActivityResultAdapter extends CommonBaseAdapter<ActivityPrizeData.PrizeSightsEntity.DataEntity> {
    public ActivityResultAdapter(ArrayList<ActivityPrizeData.PrizeSightsEntity> prizeSights, ArrayList list, Activity activity) {
        super(list, activity);
        ArrayList<ActivityPrizeData.PrizeSightsEntity> prizeSights1 = prizeSights;
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        final ActivityPrizeData.PrizeSightsEntity.DataEntity item = list.get(position);
        ViewHolder holder;
        if (convertView == null) {
            convertView = Util.inflateView(R.layout.item_activity_result, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
//        ImageLoader.getInstance().displayImage(item.user.avatar_url, holder.headImg, options);
//        ImageLoader.getInstance().displayImage(item.cover_url, holder.qjImg, options);

        GlideUtils.displayImage(item.user.avatar_url, holder.headImg);
        GlideUtils.displayImageFadein(item.cover_url, holder.qjImg);
        holder.publishTime.setText(item.created_at);
        if (TextUtils.isEmpty(item.city) && TextUtils.isEmpty(item.address)) {
            holder.locationTv.setVisibility(View.GONE);
        } else {
            holder.locationTv.setText(item.city + item.address);
        }
//        String prize_num = activity.getResources().getString(R.string.prize_num);
        if (item.flagHead){
            holder.tv_prize.setVisibility(View.VISIBLE);
            holder.tv_prize.setText(item.prizeGrade);//String.format(prize_num,item.prizeGrade,item.prizeNum)
        }else {
            holder.tv_prize.setVisibility(View.GONE);
        }

        if (item.showBottom){
            holder.container.setPadding(0,0,0,activity.getResources().getDimensionPixelSize(R.dimen.dp10));
        }else {
            holder.container.setPadding(0,0,0,0);
        }

        holder.qjTitleTv.setText(item.title);
//        holder.qjTitleTv2.setText(item.short_title);
        holder.userNameTv.setText(item.user.nickname);
        if (item.user.is_expert == 1) {
            holder.vImg.setVisibility(View.VISIBLE);
        } else {
            holder.vImg.setVisibility(View.GONE);
        }

        if (item.user.is_follow == 1) {
            setFocusBtnStyle(holder.attentionBtn, activity.getResources().getDimensionPixelSize(R.dimen.dp10), R.string.focused, R.mipmap.focus_pic, android.R.color.white, R.drawable.border_radius5_pressed);
        } else {
            setFocusBtnStyle(holder.attentionBtn, activity.getResources().getDimensionPixelSize(R.dimen.dp16), R.string.focus, R.mipmap.unfocus_white, android.R.color.white, R.drawable.shape_subscribe_theme);
        }

        //添加商品标签
        for (final ActivityPrizeData.PrizeSightsEntity.DataEntity.ProductEntity productBean : item.product) {
            final RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            final LabelView labelView = new LabelView(parent.getContext());
            labelView.nameTv.setText(productBean.title);
            labelView.setLayoutParams(layoutParams);
            if (productBean.loc == 2) {
                labelView.nameTv.setBackgroundResource(R.drawable.label_left);
                RelativeLayout.LayoutParams layoutParams1 = (RelativeLayout.LayoutParams) labelView.pointContainer.getLayoutParams();
                layoutParams1.leftMargin = (int) labelView.labelMargin;
                labelView.pointContainer.setLayoutParams(layoutParams1);
            }
            labelView.post(new Runnable() {
                @Override
                public void run() {
                    if (productBean.loc == 2) {
                        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) labelView.getLayoutParams();
                        lp.leftMargin = (int) (productBean.x * MainApplication.getContext().getScreenWidth() - labelView.labelMargin - labelView.pointWidth / 2);
                        lp.topMargin = (int) (productBean.y * MainApplication.getContext().getScreenWidth() - labelView.getMeasuredHeight() + labelView.pointWidth / 2);
                        labelView.setLayoutParams(lp);
                    } else {
                        labelView.nameTv.setBackgroundResource(R.drawable.label_right);
                        RelativeLayout.LayoutParams layoutParams1 = (RelativeLayout.LayoutParams) labelView.pointContainer.getLayoutParams();
                        layoutParams1.leftMargin = (int) (labelView.nameTv.getMeasuredWidth() - labelView.pointWidth - labelView.labelMargin);
                        labelView.pointContainer.setLayoutParams(layoutParams1);
                        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) labelView.getLayoutParams();
                        lp.leftMargin = (int) (productBean.x * MainApplication.getContext().getScreenWidth() - labelView.getMeasuredWidth() + labelView.labelMargin + labelView.pointWidth / 2);
                        lp.topMargin = (int) (productBean.y * MainApplication.getContext().getScreenWidth() - labelView.getMeasuredHeight() + labelView.pointWidth / 2);
                        labelView.setLayoutParams(lp);
                    }
                }
            });
            holder.labelContainer.addView(labelView);
            labelView.wave();
            labelView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(parent.getContext(), BuyGoodsDetailsActivity.class);
                    intent.putExtra("id", productBean.id);
                    parent.getContext().startActivity(intent);
                }
            });
        }
        holder.attentionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {//关注
                doFocus(item, view);
            }
        });
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, QJDetailActivity.class);
                intent.putExtra("id", item._id);
                activity.startActivity(intent);
            }
        });
        return convertView;
    }

    private void doFocus(final ActivityPrizeData.PrizeSightsEntity.DataEntity item, final View view) {
        if (LoginInfo.isUserLogin()) {
            if (item.user.is_follow == 0) {
                ClientDiscoverAPI.focusOperate(String.valueOf(item.user._id), new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        view.setEnabled(true);
                        if (TextUtils.isEmpty(responseInfo.result)) return;
                        HttpResponse response = JsonUtil.fromJson(responseInfo.result, HttpResponse.class);
                        if (response.isSuccess()) {
                            for (ActivityPrizeData.PrizeSightsEntity.DataEntity dataEntity:list){
                                if (TextUtils.equals(item.user._id,dataEntity.user._id)){
                                    dataEntity.user.is_follow=1;
                                }
                            }
//                            item.user.is_follow = 1;
                            setFocusBtnStyle((Button) view, activity.getResources().getDimensionPixelSize(R.dimen.dp10), R.string.focused, R.mipmap.focus_pic, android.R.color.white, R.drawable.border_radius5_pressed);
                            notifyDataSetChanged();
                            return;
                        }
                        ToastUtils.showError(response.getMessage());
                    }

                    @Override
                    public void onFailure(HttpException e, String s) {
                        view.setEnabled(true);
                        ToastUtils.showError(R.string.network_err);
                    }
                });
            } else {
                ClientDiscoverAPI.cancelFocusOperate(item.user._id + "", new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        view.setEnabled(true);
                        if (TextUtils.isEmpty(responseInfo.result)) return;
                        HttpResponse response = JsonUtil.fromJson(responseInfo.result, HttpResponse.class);
                        if (response.isSuccess()) {
                            for (ActivityPrizeData.PrizeSightsEntity.DataEntity dataEntity:list){
                                if (TextUtils.equals(item.user._id,dataEntity.user._id)){
                                    dataEntity.user.is_follow=0;
                                }
                            }
//                            item.user.is_follow = 0;
                            setFocusBtnStyle((Button) view, activity.getResources().getDimensionPixelSize(R.dimen.dp16), R.string.focus, R.mipmap.unfocus_white, android.R.color.white, R.drawable.shape_subscribe_theme);
                            notifyDataSetChanged();
                            return;
                        }
                        ToastUtils.showError(response.getMessage());
                    }

                    @Override
                    public void onFailure(HttpException e, String s) {
                        view.setEnabled(true);
                        ToastUtils.showError(R.string.network_err);
                    }
                });
            }
        } else {
            MainApplication.which_activity = DataConstants.ActivityDetail;
            activity.startActivity(new Intent(activity, OptRegisterLoginActivity.class));
        }
    }

    private void setFocusBtnStyle(Button bt_focus, int dimensionPixelSize, int focus, int unfocus_pic, int color, int drawable) {
        bt_focus.setPadding(dimensionPixelSize, 0, dimensionPixelSize, 0);
        bt_focus.setText(focus);
        bt_focus.setTextColor(activity.getResources().getColor(color));
        bt_focus.setBackgroundResource(drawable);
        bt_focus.setCompoundDrawablesWithIntrinsicBounds(unfocus_pic, 0, 0, 0);
    }

    static class ViewHolder {
        @Bind(R.id.tv_prize)
        TextView tv_prize;
        @Bind(R.id.head_img)
        RoundedImageView headImg;
        @Bind(R.id.v_img)
        ImageView vImg;
        @Bind(R.id.relative)
        RelativeLayout relative;
        @Bind(R.id.attention_btn)
        Button attentionBtn;
        @Bind(R.id.user_name_tv)
        TextView userNameTv;
        @Bind(R.id.publish_time)
        TextView publishTime;
        @Bind(R.id.location_tv)
        TextView locationTv;
        @Bind(R.id.qj_img)
        ImageView qjImg;
        @Bind(R.id.qj_title_tv)
        TextView qjTitleTv;
        @Bind(R.id.qj_title_tv2)
        TextView qjTitleTv2;
        @Bind(R.id.label_container)
        RelativeLayout labelContainer;
        @Bind(R.id.container)
        RelativeLayout container;
        @Bind(R.id.ll_title)
        LinearLayout llTitle;
        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
