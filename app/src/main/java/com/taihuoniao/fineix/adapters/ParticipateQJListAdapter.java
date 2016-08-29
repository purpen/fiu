package com.taihuoniao.fineix.adapters;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.beans.DataParticipateQJ;
import com.taihuoniao.fineix.beans.HttpResponse;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.qingjingOrSceneDetails.QJDetailActivity;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.LogUtil;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.utils.Util;
import com.taihuoniao.fineix.view.roundImageView.RoundedImageView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author lilin 参与情境列表
 *         created at 2016/5/16 15:44
 */
public class ParticipateQJListAdapter extends CommonBaseAdapter<DataParticipateQJ.ItemParticipateQJ> {
    private static final String TYPE_QJ = "12";
    private ImageLoader imageLoader;
    private int i = Util.getScreenWidth() - 3 * activity.getResources().getDimensionPixelSize(R.dimen.dp16);
    private LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, i / 2, 1);
    private LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(i / 2, activity.getResources().getDimensionPixelSize(R.dimen.dp32));

    public ParticipateQJListAdapter(List<DataParticipateQJ.ItemParticipateQJ> list, Activity activity) {
        super(list, activity);
        this.imageLoader = ImageLoader.getInstance();
    }

    @Override
    public int getCount() {
        int size = super.getCount();
        return (size % 2 == 0 ? size / 2 : size / 2 + 1);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = Util.inflateView(R.layout.item_participate_qj, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.rl_zan_left.setLayoutParams(params1);
        holder.rl_zan_right.setLayoutParams(params1);

        final DataParticipateQJ.ItemParticipateQJ left_qj;
        DataParticipateQJ.ItemParticipateQJ right_qj = null;
        left_qj = list.get(2 * position);

        if (position == list.size() / 2) {
            holder.rlRight.setVisibility(View.INVISIBLE);
        } else {
            holder.rlRight.setLayoutParams(params);
            holder.rlRight.setVisibility(View.VISIBLE);
            right_qj = list.get(2 * position + 1);
            imageLoader.displayImage(right_qj.cover_url, holder.ivCoverRight, options);
            imageLoader.displayImage(right_qj.user_info.avatar_url, holder.rivRight, options);
            holder.tvName.setText(right_qj.user_info.nickname);
            if (!TextUtils.isEmpty(left_qj.title)) {
                holder.tvTitleRight.setText(right_qj.title);
                holder.tvTitleRight.setBackgroundColor(activity.getResources().getColor(R.color.black_touming_80));
            }
        }

        if (left_qj.is_love == 1) {
            holder.ibtn.setImageResource(R.mipmap.zaned);
        } else {
            holder.ibtn.setImageResource(R.mipmap.zan_normal);
        }

        if (right_qj.is_love == 1) {
            holder.ibtnRight.setImageResource(R.mipmap.zaned);
        } else {
            holder.ibtnRight.setImageResource(R.mipmap.zan_normal);
        }

        imageLoader.displayImage(left_qj.cover_url, holder.ivCoverLeft, options);
        imageLoader.displayImage(left_qj.user_info.avatar_url, holder.riv, options);
        if (!TextUtils.isEmpty(left_qj.title)) {
            holder.tvTitleLeft.setText(left_qj.title);
            holder.tvTitleLeft.setBackgroundColor(activity.getResources().getColor(R.color.black_touming_80));
        }
        holder.rlLeft.setLayoutParams(params);
        holder.rlLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, QJDetailActivity.class);
                intent.putExtra("id", left_qj._id);
                activity.startActivity(intent);
            }
        });
        final DataParticipateQJ.ItemParticipateQJ finalRight_qj = right_qj;
        holder.rlRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, QJDetailActivity.class);
                intent.putExtra("id", finalRight_qj._id);
                activity.startActivity(intent);
            }
        });
        setClickListener(holder.ibtn, left_qj);
        setClickListener(holder.ibtnRight, right_qj);
        return convertView;
    }

    private void setClickListener(final ImageButton ibtn, final DataParticipateQJ.ItemParticipateQJ item) {
        ibtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (item == null) return;
                if (item.is_love == 0) {
                    ClientDiscoverAPI.loveNet(String.valueOf(item._id), TYPE_QJ, new RequestCallBack<String>() {
                        @Override
                        public void onStart() {
                            ibtn.setEnabled(false);
                        }

                        @Override
                        public void onSuccess(ResponseInfo<String> responseInfo) {
                            ibtn.setEnabled(true);
                            if (TextUtils.isEmpty(responseInfo.result)) return;
                            HttpResponse response = JsonUtil.fromJson(responseInfo.result, HttpResponse.class);
                            if (response.isSuccess()) {
                                item.is_love = 1;
                                ibtn.setImageResource(R.mipmap.zaned);
                                return;
                            }
                            ToastUtils.showError(response.getMessage());
                        }

                        @Override
                        public void onFailure(HttpException e, String s) {
                            ibtn.setEnabled(true);
                            e.printStackTrace();
                            ToastUtils.showError(R.string.network_err);
                            LogUtil.e(TAG, s);
                        }
                    });
                } else {
                    ClientDiscoverAPI.cancelLoveNet(String.valueOf(item._id), TYPE_QJ, new RequestCallBack<String>() {
                        @Override
                        public void onStart() {
                            ibtn.setEnabled(false);
                        }

                        @Override
                        public void onSuccess(ResponseInfo<String> responseInfo) {
                            ibtn.setEnabled(true);
                            if (TextUtils.isEmpty(responseInfo.result)) return;
                            HttpResponse response = JsonUtil.fromJson(responseInfo.result, HttpResponse.class);
                            if (response.isSuccess()) {
                                item.is_love = 0;
                                ibtn.setImageResource(R.mipmap.zan_normal);
                                return;
                            }
                            ToastUtils.showError(response.getMessage());
                        }

                        @Override
                        public void onFailure(HttpException e, String s) {
                            ibtn.setEnabled(true);
                            e.printStackTrace();
                            ToastUtils.showError(R.string.network_err);
                            LogUtil.e(TAG, s);
                        }
                    });
                }
            }
        });
    }

    static class ViewHolder {
        @Bind(R.id.iv_cover_left)
        ImageView ivCoverLeft;
        @Bind(R.id.tv_title_left)
        TextView tvTitleLeft;
        @Bind(R.id.rl_left)
        RelativeLayout rlLeft;
        @Bind(R.id.riv)
        RoundedImageView riv;
        @Bind(R.id.tv_name)
        TextView tvName;
        @Bind(R.id.ibtn)
        ImageButton ibtn;
        @Bind(R.id.rl_zan_left)
        RelativeLayout rl_zan_left;


        @Bind(R.id.iv_cover_right)
        ImageView ivCoverRight;
        @Bind(R.id.tv_title_right)
        TextView tvTitleRight;
        @Bind(R.id.rl_right)
        RelativeLayout rlRight;
        @Bind(R.id.riv_right)
        RoundedImageView rivRight;
        @Bind(R.id.tv_name_right)
        TextView tvNameRight;
        @Bind(R.id.ibtn_right)
        ImageButton ibtnRight;
        @Bind(R.id.rl_zan_right)
        RelativeLayout rl_zan_right;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
