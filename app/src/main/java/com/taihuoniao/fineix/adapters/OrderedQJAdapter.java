package com.taihuoniao.fineix.adapters;

import android.app.Activity;
import android.content.Intent;
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
import com.taihuoniao.fineix.beans.ItemSubscribedQJ;
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
 * @author lilin
 * created at 2016/5/5 19:13
 */
public class OrderedQJAdapter extends CommonBaseAdapter<ItemSubscribedQJ> {
    private static final String TYPE_QJ = "12";

    public OrderedQJAdapter(List<ItemSubscribedQJ> list, Activity activity) {
        super(list,activity);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ItemSubscribedQJ item = list.get(position);
        ViewHolder holder;
        if (convertView == null) {
            convertView = Util.inflateView(R.layout.item_favorite_qj, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ImageLoader.getInstance().displayImage(item.cover_url, holder.imageView, options);
        ImageLoader.getInstance().displayImage(item.user_info.avatar_url, holder.riv, options);
        holder.tvName.setText(item.user_info.nickname);
        if (!TextUtils.isEmpty(item.title)) {
            if (item.title.length() <= 10) {
                holder.tv_title.setText("");
                holder.tv_title1.setText(item.title);
                holder.tv_title1.setBackgroundColor(activity.getResources().getColor(R.color.black_touming_80));
                holder.tv_title.setBackgroundColor(activity.getResources().getColor(android.R.color.transparent));
            } else {
                holder.tv_title.setText(item.title.substring(0, 10));
                holder.tv_title1.setText(item.title.substring(10, item.title.length() - 1));
                holder.tv_title1.setBackgroundColor(activity.getResources().getColor(R.color.black_touming_80));
                holder.tv_title.setBackgroundColor(activity.getResources().getColor(R.color.black_touming_80));
            }
        }
        if (item.is_love == 1) {
            holder.ibtn.setImageResource(R.mipmap.zaned);
        } else {
            holder.ibtn.setImageResource(R.mipmap.zan_normal);
        }
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, QJDetailActivity.class);
                intent.putExtra("id", String.valueOf(item._id));
                activity.startActivity(intent);
            }
        });
        setClickListener(holder.ibtn, item);
        return convertView;
    }

    private void setClickListener(final ImageButton ibtn, final ItemSubscribedQJ item) {
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
