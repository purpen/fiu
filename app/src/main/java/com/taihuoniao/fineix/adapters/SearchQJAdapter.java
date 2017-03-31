package com.taihuoniao.fineix.adapters;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.beans.HttpResponse;
import com.taihuoniao.fineix.common.GlobalDataCallBack;
import com.taihuoniao.fineix.base.HttpRequest;
import com.taihuoniao.fineix.beans.LoginInfo;
import com.taihuoniao.fineix.beans.SceneLoveBean;
import com.taihuoniao.fineix.beans.SearchBean;
import com.taihuoniao.fineix.main.MainApplication;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.DataConstants;
import com.taihuoniao.fineix.network.URL;
import com.taihuoniao.fineix.qingjingOrSceneDetails.QJDetailActivity;
import com.taihuoniao.fineix.user.FocusActivity;
import com.taihuoniao.fineix.user.OptRegisterLoginActivity;
import com.taihuoniao.fineix.user.UserCenterActivity;
import com.taihuoniao.fineix.utils.DensityUtils;
import com.taihuoniao.fineix.utils.GlideUtils;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.SceneTitleSetUtils;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.view.ClickImageView;
import com.taihuoniao.fineix.view.dialog.WaittingDialog;
import com.taihuoniao.fineix.view.roundImageView.RoundedImageView;

import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by taihuoniao on 2016/8/23.
 */
public class SearchQJAdapter extends BaseAdapter {
    private Activity activity;
    private List<SearchBean.SearchItem> list;
    private WaittingDialog dialog;
    private int page;

    public SearchQJAdapter(Activity activity, List<SearchBean.SearchItem> list) {
        this.activity = activity;
        this.list = list;
        dialog = new WaittingDialog(activity);
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(parent.getContext(), R.layout.item_search_qj, null);
            holder = new ViewHolder(convertView);
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) holder.qjBackgroundImg.getLayoutParams();
            layoutParams.width = (MainApplication.getContext().getScreenWidth() - DensityUtils.dp2px(activity, 45)) / 2;
            layoutParams.height = layoutParams.width;
            holder.qjBackgroundImg.setLayoutParams(layoutParams);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        GlideUtils.displayImage(list.get(position).getCover_url(), holder.qjBackgroundImg);
        GlideUtils.displayImage(list.get(position).getUser_info().getAvatar_url(), holder.qjHeadImg);
        if (list.get(position).getIs_love() == 1) {
            holder.qjLove.setImageResource(R.mipmap.find_has_love);
        } else {
            holder.qjLove.setImageResource(R.mipmap.find_love);
        }
        holder.qjName.setText(list.get(position).getUser_info().getNickname());
        //设置情景标题
        SceneTitleSetUtils.setTitle(holder.qjTitleTv1,holder.qjTitleTv2,list.get(position).getTitle());
        holder.qjBackgroundImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, QJDetailActivity.class);
                intent.putExtra("id", list.get(position).get_id());
                activity.startActivity(intent);
            }
        });
        holder.qjHeadImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(parent.getContext(), UserCenterActivity.class);
                long l = Long.valueOf(list.get(position).getUser_info().getUser_id());
                intent.putExtra(FocusActivity.USER_ID_EXTRA, l);
                parent.getContext().startActivity(intent);
            }
        });
        holder.qjName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(parent.getContext(), UserCenterActivity.class);
                long l = Long.valueOf(list.get(position).getUser_info().getUser_id());
                intent.putExtra(FocusActivity.USER_ID_EXTRA, l);
                parent.getContext().startActivity(intent);
            }
        });
        holder.qjLove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (LoginInfo.isUserLogin()) {
                    //已经登录
                    if (list.get(position).getIs_love() == 1) {
                        holder.qjLove.setEnabled(false);
                        cancelLoveQJ(position, list.get(position).get_id(), holder);
                    } else {
                        holder.qjLove.setEnabled(false);
                        loveQJ(position, list.get(position).get_id(), holder);
                    }
                    return;
                }
                MainApplication.which_activity = DataConstants.SearchActivity;
                activity.startActivity(new Intent(activity, OptRegisterLoginActivity.class));
            }
        });
        return convertView;
    }

    //取消点赞
    private void cancelLoveQJ(final int position, String id, final ViewHolder holder) {
        HashMap<String, String> requestParams = ClientDiscoverAPI.getcancelLoveQJRequestParams(id);
        HttpRequest.post(requestParams, URL.CANCEL_LOVE_SCENE, new GlobalDataCallBack(){
            @Override
            public void onSuccess(String json) {
                holder.qjLove.setEnabled(true);
                dialog.dismiss();
                HttpResponse<SceneLoveBean> sceneLoveBean = JsonUtil.json2Bean(json, new TypeToken<HttpResponse<SceneLoveBean>>() {});
                if (sceneLoveBean.isSuccess()) {
                    holder.qjLove.setImageResource(R.mipmap.find_love);
                    list.get(position).setIs_love(0);
                } else {
                    ToastUtils.showError(sceneLoveBean.getMessage());
                }
            }

            @Override
            public void onFailure(String error) {
                holder.qjLove.setEnabled(true);
                dialog.dismiss();
                ToastUtils.showError(R.string.net_fail);
            }
        });
    }

    //点赞情景
    private void loveQJ(final int position, String id, final ViewHolder holder) {
        HashMap<String, String> requestParams = ClientDiscoverAPI.getloveQJRequestParams(id);
        HttpRequest.post(requestParams, URL.LOVE_SCENE, new GlobalDataCallBack(){
            @Override
            public void onSuccess(String json) {
                holder.qjLove.setEnabled(true);
                dialog.dismiss();
                HttpResponse<SceneLoveBean> sceneLoveBean = JsonUtil.json2Bean(json, new TypeToken<HttpResponse<SceneLoveBean>>() {});
                if (sceneLoveBean.isSuccess()) {
                    holder.qjLove.setImageResource(R.mipmap.find_has_love);
                    list.get(position).setIs_love(1);
                } else {
                    ToastUtils.showError(sceneLoveBean.getMessage());
                }
            }

            @Override
            public void onFailure(String error) {
                holder.qjLove.setEnabled(true);
                dialog.dismiss();
                ToastUtils.showError(R.string.net_fail);
            }
        });
    }

    static class ViewHolder {
        @Bind(R.id.qj_background_img)
        ImageView qjBackgroundImg;
        @Bind(R.id.qj_head_img)
        RoundedImageView qjHeadImg;
        @Bind(R.id.qj_love)
        ClickImageView qjLove;
        @Bind(R.id.qj_name)
        TextView qjName;
        @Bind(R.id.qj_title_tv1)
        TextView qjTitleTv1;
        @Bind(R.id.qj_title_tv2)
        TextView qjTitleTv2;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
