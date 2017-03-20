package com.taihuoniao.fineix.adapters;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.common.GlobalDataCallBack;
import com.taihuoniao.fineix.base.HttpRequest;
import com.taihuoniao.fineix.beans.HttpResponse;
import com.taihuoniao.fineix.beans.ItemQJCollect;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.URL;
import com.taihuoniao.fineix.utils.GlideUtils;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.LogUtil;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.utils.Util;
import com.taihuoniao.fineix.view.roundImageView.RoundedImageView;

import java.util.ArrayList;
import java.util.HashMap;

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
//        ImageLoader.getInstance().displayImage(item.sight.cover_url, holder.imageView, options);
        GlideUtils.displayImage(item.sight.cover_url, holder.imageView);
        if (item.sight.user_info != null) {
//            ImageLoader.getInstance().displayImage(item.sight.user_info.avatar_url, holder.riv, options);
            GlideUtils.displayImage(item.sight.user_info.avatar_url, holder.riv);
            holder.tvName.setText(item.sight.user_info.nickname);
        }
        if (!TextUtils.isEmpty(item.sight.title)) {
            holder.tv_title.setVisibility(View.VISIBLE);
            holder.tv_title1.setVisibility(View.VISIBLE);
            if (item.sight.title.length() <= 10) {
                holder.tv_title.setText("");
                holder.tv_title1.setText(item.sight.title);
                holder.tv_title1.setBackgroundColor(activity.getResources().getColor(R.color.black_touming_80));
                holder.tv_title.setBackgroundColor(activity.getResources().getColor(android.R.color.transparent));
            } else {
                holder.tv_title.setText(item.sight.title.substring(0, 10));
                holder.tv_title1.setText(item.sight.title.substring(10, item.sight.title.length()));
                holder.tv_title1.setBackgroundColor(activity.getResources().getColor(R.color.black_touming_80));
                holder.tv_title.setBackgroundColor(activity.getResources().getColor(R.color.black_touming_80));
            }
        } else {
            holder.tv_title.setVisibility(View.GONE);
            holder.tv_title1.setVisibility(View.GONE);
        }
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
                    HashMap<String, String> params = ClientDiscoverAPI.getloveNetRequestParams(String.valueOf(item.sight._id), TYPE_QJ);
                    HttpRequest.post(params,  URL.URLSTRING_LOVE, new GlobalDataCallBack(){
                        @Override
                        public void onStart() {
                            ibtn.setEnabled(false);
                        }

                        @Override
                        public void onSuccess(String json) {
                            ibtn.setEnabled(true);
                            if (TextUtils.isEmpty(json)) return;
                            HttpResponse response = JsonUtil.fromJson(json, HttpResponse.class);
                            if (response.isSuccess()) {
                                item.is_follow = 1;
                                ibtn.setImageResource(R.mipmap.zaned);
                                return;
                            }
                            ToastUtils.showError(response.getMessage());
                        }

                        @Override
                        public void onFailure(String error) {
                            ibtn.setEnabled(true);
                            ToastUtils.showError(R.string.network_err);
                            LogUtil.e(TAG, error);
                        }
                    });
                } else {
                    HashMap<String, String> params = ClientDiscoverAPI.getcancelLoveNetRequestParams(String.valueOf(item.sight._id), TYPE_QJ);
                    HttpRequest.post(params,  URL.URLSTRING_CANCELLOVE, new GlobalDataCallBack(){
                        @Override
                        public void onStart() {
                            ibtn.setEnabled(false);
                        }

                        @Override
                        public void onSuccess(String json) {
                            ibtn.setEnabled(true);
                            if (TextUtils.isEmpty(json)) return;
                            HttpResponse response = JsonUtil.fromJson(json, HttpResponse.class);
                            if (response.isSuccess()) {
                                item.is_follow = 0;
                                ibtn.setImageResource(R.mipmap.zan_normal);
                                return;
                            }
                            ToastUtils.showError(response.getMessage());
                        }

                        @Override
                        public void onFailure(String error) {
                            ibtn.setEnabled(true);
                            ToastUtils.showError(R.string.network_err);
                            LogUtil.e(TAG, error);
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
        @Bind(R.id.tv_title1)
        TextView tv_title1;
        @Bind(R.id.ibtn)
        ImageButton ibtn;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
