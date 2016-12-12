package com.taihuoniao.fineix.adapters;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.beans.LoginInfo;
import com.taihuoniao.fineix.beans.ProductAndSceneListBean;
import com.taihuoniao.fineix.beans.SceneLoveBean;
import com.taihuoniao.fineix.main.MainApplication;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.DataConstants;
import com.taihuoniao.fineix.qingjingOrSceneDetails.QJDetailActivity;
import com.taihuoniao.fineix.user.FocusActivity;
import com.taihuoniao.fineix.user.OptRegisterLoginActivity;
import com.taihuoniao.fineix.user.UserCenterActivity;
import com.taihuoniao.fineix.utils.DensityUtils;
import com.taihuoniao.fineix.utils.SceneTitleSetUtils;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.view.ClickImageView;
import com.taihuoniao.fineix.view.dialog.WaittingDialog;
import com.taihuoniao.fineix.view.roundImageView.RoundedImageView;

import java.lang.reflect.Type;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by taihuoniao on 2016/8/25.
 */
public class GoodDetailsSceneListAdapter extends BaseAdapter {
    private Activity activity;
    private List<ProductAndSceneListBean.ProductAndSceneItem> sceneList;
    private WaittingDialog dialog;

    public GoodDetailsSceneListAdapter(Activity activity, List<ProductAndSceneListBean.ProductAndSceneItem> sceneList) {
        this.activity = activity;
        this.sceneList = sceneList;
        dialog = new WaittingDialog(activity);
    }

    @Override
    public int getCount() {
        return (sceneList.size() + 1) / 2;
    }


    @Override
    public Object getItem(int position) {
        return sceneList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(activity, R.layout.item_find_qj, null);
            holder = new ViewHolder(convertView);
            holder.subjectContainer.setVisibility(View.GONE);
            holder.qjContainer.setVisibility(View.VISIBLE);
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) holder.qjBackgroundImg1.getLayoutParams();
            lp.width = (MainApplication.getContext().getScreenWidth() - DensityUtils.dp2px(activity, 45)) / 2;
            lp.height = lp.width;
            holder.qjBackgroundImg1.setLayoutParams(lp);
            RelativeLayout.LayoutParams lp2 = (RelativeLayout.LayoutParams) holder.qjBackgroundImg2.getLayoutParams();
            lp2.width = lp.width;
            lp2.height = lp.width;
            holder.qjBackgroundImg2.setLayoutParams(lp2);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final int qjPosition = 2 * position;
        ImageLoader.getInstance().displayImage(sceneList.get(qjPosition).getSight().getCover_url(), holder.qjBackgroundImg1);
        //设置情景标题
        SceneTitleSetUtils.setTitle(holder.qjTitle1Tv1,holder.qjTitle1Tv2,sceneList.get(qjPosition).getSight().getTitle());
        ImageLoader.getInstance().displayImage(sceneList.get(qjPosition).getSight().getUser_info().getAvatar_url(), holder.qjHeadImg1);
        holder.qjName1.setText(sceneList.get(qjPosition).getSight().getUser_info().getNickname());
        if (sceneList.get(qjPosition).getSight().getIs_love() == 1) {
            holder.qjLove1.setImageResource(R.mipmap.find_has_love);
        } else {
            holder.qjLove1.setImageResource(R.mipmap.find_love);
        }
        holder.qjBackgroundImg1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, QJDetailActivity.class);
                intent.putExtra("id", sceneList.get(qjPosition).getSight().get_id());
                activity.startActivity(intent);
            }
        });
        holder.qjHeadImg1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, UserCenterActivity.class);
                long l = Long.valueOf(sceneList.get(qjPosition).getSight().getUser_info().getUser_id());
                intent.putExtra(FocusActivity.USER_ID_EXTRA, l);
                activity.startActivity(intent);
            }
        });
        holder.qjName1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, UserCenterActivity.class);
                long l = Long.valueOf(sceneList.get(qjPosition).getSight().getUser_info().getUser_id());
                intent.putExtra(FocusActivity.USER_ID_EXTRA, l);
                activity.startActivity(intent);
            }
        });
        holder.qjLove1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (LoginInfo.isUserLogin()) {
                    //已经登录
                    if (sceneList.get(qjPosition).getSight().getIs_love() == 1) {
//                        if (!dialog.isShowing()) {
//                            dialog.show();
//                        }
                        holder.qjLove1.setEnabled(false);
                        cancelLoveQJ(qjPosition, sceneList.get(qjPosition).getSight().get_id(), holder, false);
                    } else {
//                        if (!dialog.isShowing()) {
//                            dialog.show();
//                        }
                        holder.qjLove1.setEnabled(false);
                        loveQJ(qjPosition, sceneList.get(qjPosition).getSight().get_id(), holder, false);
                    }
                    return;
                }
                MainApplication.which_activity = DataConstants.BuyGoodDetailsActivity;
                activity.startActivity(new Intent(activity, OptRegisterLoginActivity.class));
            }
        });
        if (qjPosition + 1 >= sceneList.size()) {
            holder.qjItem2.setVisibility(View.GONE);
        } else {
            holder.qjItem2.setVisibility(View.VISIBLE);
            ImageLoader.getInstance().displayImage(sceneList.get(qjPosition + 1).getSight().getCover_url(), holder.qjBackgroundImg2);
            //设置情景标题
            SceneTitleSetUtils.setTitle(holder.qjTitle2Tv1,holder.qjTitle2Tv2,sceneList.get(qjPosition+1).getSight().getTitle());
            ImageLoader.getInstance().displayImage(sceneList.get(qjPosition + 1).getSight().getUser_info().getAvatar_url(), holder.qjHeadImg2);
            holder.qjName2.setText(sceneList.get(qjPosition + 1).getSight().getUser_info().getNickname());
            if (sceneList.get(qjPosition + 1).getSight().getIs_love() == 1) {
                holder.qjLove2.setImageResource(R.mipmap.find_has_love);
            } else {
                holder.qjLove2.setImageResource(R.mipmap.find_love);
            }
            holder.qjBackgroundImg2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(activity, QJDetailActivity.class);
                    intent.putExtra("id", sceneList.get(qjPosition + 1).getSight().get_id());
                    activity.startActivity(intent);
                }
            });
            holder.qjHeadImg2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(activity, UserCenterActivity.class);
                    long l = Long.valueOf(sceneList.get(qjPosition + 1).getSight().getUser_info().getUser_id());
                    intent.putExtra(FocusActivity.USER_ID_EXTRA, l);
                    activity.startActivity(intent);
                }
            });
            holder.qjName2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(activity, UserCenterActivity.class);
                    long l = Long.valueOf(sceneList.get(qjPosition + 1).getSight().getUser_info().getUser_id());
                    intent.putExtra(FocusActivity.USER_ID_EXTRA, l);
                    activity.startActivity(intent);
                }
            });
            holder.qjLove2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (LoginInfo.isUserLogin()) {
                        //已经登录
                        if (sceneList.get(qjPosition + 1).getSight().getIs_love() == 1) {
//                            if (!dialog.isShowing()) {
//                                dialog.show();
//                            }
                            holder.qjLove2.setEnabled(false);
                            cancelLoveQJ(qjPosition + 1, sceneList.get(qjPosition + 1).getSight().get_id(), holder, true);
                        } else {
//                            if (!dialog.isShowing()) {
//                                dialog.show();
//                            }
                            holder.qjLove2.setEnabled(false);
                            loveQJ(qjPosition + 1, sceneList.get(qjPosition + 1).getSight().get_id(), holder, true);
                        }
                        return;
                    }
                    MainApplication.which_activity = DataConstants.BuyGoodDetailsActivity;
                    activity.startActivity(new Intent(activity, OptRegisterLoginActivity.class));
                }
            });
        }

        return convertView;
    }

    static class ViewHolder {
        //情景页
        @Bind(R.id.qj_container)
        LinearLayout qjContainer;
        @Bind(R.id.qj_background_img1)
        ImageView qjBackgroundImg1;
        @Bind(R.id.qj_head_img1)
        RoundedImageView qjHeadImg1;
        @Bind(R.id.qj_love1)
        ClickImageView qjLove1;
        @Bind(R.id.qj_name1)
        TextView qjName1;
        @Bind(R.id.qj_bottom_container1)
        RelativeLayout qjBottomContainer1;
        @Bind(R.id.qj_title1_tv1)
        TextView qjTitle1Tv1;
        @Bind(R.id.qj_title1_tv2)
        TextView qjTitle1Tv2;
        @Bind(R.id.qj_item1)
        RelativeLayout qjItem1;
        @Bind(R.id.qj_background_img2)
        ImageView qjBackgroundImg2;
        @Bind(R.id.qj_head_img2)
        RoundedImageView qjHeadImg2;
        @Bind(R.id.qj_love2)
        ClickImageView qjLove2;
        @Bind(R.id.qj_name2)
        TextView qjName2;
        @Bind(R.id.qj_bottom_container2)
        RelativeLayout qjBottomContainer2;
        @Bind(R.id.qj_title2_tv1)
        TextView qjTitle2Tv1;
        @Bind(R.id.qj_title2_tv2)
        TextView qjTitle2Tv2;
        @Bind(R.id.qj_item2)
        RelativeLayout qjItem2;
        //主题页
        @Bind(R.id.subject_container)
        RelativeLayout subjectContainer;
        @Bind(R.id.subject_img)
        ImageView subjectImg;
        @Bind(R.id.subject_name)
        TextView subjectName;
        @Bind(R.id.subject_name2)
        TextView subjectName2;
        @Bind(R.id.subject_label)
        ImageView subjectLabel;


        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    //取消点赞
    private void cancelLoveQJ(final int position, String id, final ViewHolder holder, final boolean isRight) {
        ClientDiscoverAPI.cancelLoveQJ(id, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                holder.qjLove1.setEnabled(true);
                holder.qjLove2.setEnabled(true);
                dialog.dismiss();
                SceneLoveBean sceneLoveBean = new SceneLoveBean();
                try {
                    Gson gson = new Gson();
                    Type type = new TypeToken<SceneLoveBean>() {
                    }.getType();
                    sceneLoveBean = gson.fromJson(responseInfo.result, type);
                } catch (JsonSyntaxException e) {
                    Log.e("<<<", "解析异常");
                }
                if (sceneLoveBean.isSuccess()) {
                    if (isRight) {
                        holder.qjLove2.setImageResource(R.mipmap.find_love);
                    } else {
                        holder.qjLove1.setImageResource(R.mipmap.find_love);
                    }
                    sceneList.get(position).getSight().setIs_love(0);
                } else {
                    ToastUtils.showError(sceneLoveBean.getMessage());
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                holder.qjLove1.setEnabled(true);
                holder.qjLove2.setEnabled(true);
                dialog.dismiss();
                ToastUtils.showError(R.string.net_fail);
            }
        });
    }

    //点赞情景
    private void loveQJ(final int position, String id, final ViewHolder holder, final boolean isRight) {
        ClientDiscoverAPI.loveQJ(id, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                holder.qjLove1.setEnabled(true);
                holder.qjLove2.setEnabled(true);
                dialog.dismiss();
                SceneLoveBean sceneLoveBean = new SceneLoveBean();
                try {
                    Gson gson = new Gson();
                    Type type = new TypeToken<SceneLoveBean>() {
                    }.getType();
                    sceneLoveBean = gson.fromJson(responseInfo.result, type);
                } catch (JsonSyntaxException e) {
                    Log.e("<<<", "解析异常");
                }
                if (sceneLoveBean.isSuccess()) {
                    if (isRight) {
                        holder.qjLove2.setImageResource(R.mipmap.find_has_love);
                    } else {
                        holder.qjLove1.setImageResource(R.mipmap.find_has_love);
                    }
                    sceneList.get(position).getSight().setIs_love(1);
                } else {
                    ToastUtils.showError(sceneLoveBean.getMessage());
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                holder.qjLove1.setEnabled(true);
                holder.qjLove2.setEnabled(true);
                dialog.dismiss();
                ToastUtils.showError(R.string.net_fail);
            }
        });
    }
}
