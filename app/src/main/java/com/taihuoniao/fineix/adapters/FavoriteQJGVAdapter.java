package com.taihuoniao.fineix.adapters;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.beans.HttpResponse;
import com.taihuoniao.fineix.beans.ItemQJCollect;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.LogUtil;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.utils.Util;
import com.taihuoniao.fineix.view.roundImageView.RoundedImageView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author lilin
 *         created at 2016/8/17 15:29
 */
public class FavoriteQJGVAdapter extends CommonBaseAdapter<ItemQJCollect> {
    private static final String TYPE_QJ = "12";

    public FavoriteQJGVAdapter(ArrayList list, Activity activity) {
        super(list, activity);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ItemQJCollect item = list.get(position);
        ViewHolder holder;
        if (convertView == null) {
            convertView = Util.inflateView(R.layout.item_favorite_qj, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ImageLoader.getInstance().displayImage(item.sight.cover_url, holder.imageView, options);
        ImageLoader.getInstance().displayImage(item.sight.user_info.avatar_url, holder.riv, options);
        holder.tvName.setText(item.sight.user_info.nickname);
        holder.tv_title.setText(item.sight.scene_title);
        holder.ibtn.setVisibility(View.GONE);
//        if (item.is_follow == 1) {
//            holder.ibtn.setImageResource(R.mipmap.zaned);
//        } else {
//            holder.ibtn.setImageResource(R.mipmap.zan_normal);
//        }
//        setClickListener(holder.ibtn, item);
        return convertView;
    }

    private void setClickListener(final ImageButton ibtn, final ItemQJCollect item) {
        ibtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (item == null) return;
                if (item.is_follow == 0) {
                    ClientDiscoverAPI.loveNet(String.valueOf(item.sight._id), TYPE_QJ, new RequestCallBack<String>() {
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
                                item.is_follow = 1;
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
                    ClientDiscoverAPI.cancelLoveNet(String.valueOf(item.sight._id), TYPE_QJ, new RequestCallBack<String>() {
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
                                item.is_follow = 0;
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
        @Bind(R.id.imageView)
        ImageView imageView;
        @Bind(R.id.riv)
        RoundedImageView riv;
        @Bind(R.id.tv_name)
        TextView tvName;
        @Bind(R.id.tv_title)
        TextView tv_title;
        @Bind(R.id.ibtn)
        ImageButton ibtn;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
