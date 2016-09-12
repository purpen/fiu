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
import com.taihuoniao.fineix.beans.SceneList;
import com.taihuoniao.fineix.beans.SceneLoveBean;
import com.taihuoniao.fineix.beans.SubjectListBean;
import com.taihuoniao.fineix.main.MainActivity;
import com.taihuoniao.fineix.main.MainApplication;
import com.taihuoniao.fineix.main.fragment.FindFragment;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.DataConstants;
import com.taihuoniao.fineix.qingjingOrSceneDetails.FindActivity;
import com.taihuoniao.fineix.user.ActivityDetailActivity;
import com.taihuoniao.fineix.user.ArticalDetailActivity;
import com.taihuoniao.fineix.user.FocusActivity;
import com.taihuoniao.fineix.user.NewProductDetailActivity;
import com.taihuoniao.fineix.user.OptRegisterLoginActivity;
import com.taihuoniao.fineix.user.SalePromotionDetailActivity;
import com.taihuoniao.fineix.user.UserCenterActivity;
import com.taihuoniao.fineix.utils.DensityUtils;
import com.taihuoniao.fineix.utils.SceneTitleSetUtils;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.view.ClickImageView;
import com.taihuoniao.fineix.view.WaittingDialog;
import com.taihuoniao.fineix.view.roundImageView.RoundedImageView;

import java.lang.reflect.Type;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by taihuoniao on 2016/8/22.
 */
public class FindQJAdapter extends BaseAdapter {
    private final int ITEM_QJ = 0;
    private final int ITEM_SUBJECT = 1;
    private Activity activity;
    private List<SubjectListBean.DataBean.RowsBean> subjectList;
    private List<SceneList.DataBean.RowsBean> sceneList;
    private WaittingDialog dialog;
    private int page;

    public FindQJAdapter(Activity activity, List<SubjectListBean.DataBean.RowsBean> subjectList, List<SceneList.DataBean.RowsBean> sceneList) {
        this.activity = activity;
        this.subjectList = subjectList;
        this.sceneList = sceneList;
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
        if (sceneList.size() == 0) {
            return 0;
        }
        if (subjectList.size() <= 0) {
            return (sceneList.size() + 1) / 2;
        }
        if (sceneList.size() / 10 < subjectList.size()) {
            return (sceneList.size() + 1) / 2 + sceneList.size() / 10;
        }
        return (sceneList.size() + 1) / 2 + subjectList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (subjectList.size() > 0 && position % 6 == 5 && subjectList.size() * 6 > position) {
            return ITEM_SUBJECT;
        }
        return ITEM_QJ;
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
    public View getView(final int position, View convertView, final ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(activity, R.layout.item_find_qj, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        switch (getItemViewType(position)) {
            case ITEM_QJ:
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
                int cha = 0;
                if (position < subjectList.size() * 6) {
                    cha = position / 6;
                } else {
                    cha = subjectList.size();
                }
                final int qjPosition = 2 * (position - cha);
                ImageLoader.getInstance().displayImage(sceneList.get(qjPosition).getCover_url(), holder.qjBackgroundImg1);
                //设置情景标题
                SceneTitleSetUtils.setTitle(holder.qjTitle1Tv1,holder.qjTitle1Tv2,sceneList.get(qjPosition).getTitle());
                ImageLoader.getInstance().displayImage(sceneList.get(qjPosition).getUser_info().getAvatar_url(), holder.qjHeadImg1);
                holder.qjName1.setText(sceneList.get(qjPosition).getUser_info().getNickname());
                if (sceneList.get(qjPosition).getIs_love() == 1) {
                    holder.qjLove1.setImageResource(R.mipmap.find_has_love);
                } else {
                    holder.qjLove1.setImageResource(R.mipmap.find_love);
                }
                holder.qjBackgroundImg1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(activity, FindActivity.class);
                        intent.putExtra("page", page);
                        intent.putExtra(FindFragment.class.getSimpleName(), qjPosition);
                        MainApplication.sceneList = sceneList;
                        activity.startActivity(intent);
                    }
                });
                holder.qjHeadImg1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(activity, UserCenterActivity.class);
                        long l = Long.valueOf(sceneList.get(qjPosition).getUser_info().getUser_id());
                        intent.putExtra(FocusActivity.USER_ID_EXTRA, l);
                        activity.startActivity(intent);
                    }
                });
                holder.qjName1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(activity, UserCenterActivity.class);
                        long l = Long.valueOf(sceneList.get(qjPosition).getUser_info().getUser_id());
                        intent.putExtra(FocusActivity.USER_ID_EXTRA, l);
                        activity.startActivity(intent);
                    }
                });
                holder.qjLove1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (LoginInfo.isUserLogin()) {
                            //已经登录
                            if (sceneList.get(qjPosition).getIs_love() == 1) {
//                                if (!dialog.isShowing()) {
//                                    dialog.show();
//                                }
                                holder.qjLove1.setEnabled(false);
                                cancelLoveQJ(qjPosition, sceneList.get(qjPosition).get_id(), holder, false);
                            } else {
//                                if (!dialog.isShowing()) {
//                                    dialog.show();
//                                }
                                holder.qjLove1.setEnabled(false);
                                loveQJ(qjPosition, sceneList.get(qjPosition).get_id(), holder, false);
                            }
                            return;
                        }
                        if (activity instanceof MainActivity) {
                            MainApplication.which_activity = DataConstants.FindFragment;
                            ((MainActivity) activity).which = FindFragment.class.getSimpleName();
                            activity.startActivity(new Intent(activity, OptRegisterLoginActivity.class));
                        }
                    }
                });
                if (qjPosition + 1 >= sceneList.size()) {
                    holder.qjItem2.setVisibility(View.GONE);
                } else {
                    holder.qjItem2.setVisibility(View.VISIBLE);
                    ImageLoader.getInstance().displayImage(sceneList.get(qjPosition + 1).getCover_url(), holder.qjBackgroundImg2);
                    //设置情景标题
                    SceneTitleSetUtils.setTitle(holder.qjTitle2Tv1,holder.qjTitle2Tv2,sceneList.get(qjPosition+1).getTitle());
                    ImageLoader.getInstance().displayImage(sceneList.get(qjPosition + 1).getUser_info().getAvatar_url(), holder.qjHeadImg2);
                    holder.qjName2.setText(sceneList.get(qjPosition + 1).getUser_info().getNickname());
                    if (sceneList.get(qjPosition + 1).getIs_love() == 1) {
                        holder.qjLove2.setImageResource(R.mipmap.find_has_love);
                    } else {
                        holder.qjLove2.setImageResource(R.mipmap.find_love);
                    }
                    holder.qjBackgroundImg2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(activity, FindActivity.class);
                            intent.putExtra("page", page);
                            intent.putExtra(FindFragment.class.getSimpleName(), qjPosition + 1);
                            MainApplication.sceneList = sceneList;
                            activity.startActivity(intent);
                        }
                    });
                    holder.qjHeadImg2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(activity, UserCenterActivity.class);
                            long l = Long.valueOf(sceneList.get(qjPosition + 1).getUser_info().getUser_id());
                            intent.putExtra(FocusActivity.USER_ID_EXTRA, l);
                            activity.startActivity(intent);
                        }
                    });
                    holder.qjName2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(activity, UserCenterActivity.class);
                            long l = Long.valueOf(sceneList.get(qjPosition + 1).getUser_info().getUser_id());
                            intent.putExtra(FocusActivity.USER_ID_EXTRA, l);
                            activity.startActivity(intent);
                        }
                    });
                    holder.qjLove2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (LoginInfo.isUserLogin()) {
                                //已经登录
                                if (sceneList.get(qjPosition + 1).getIs_love() == 1) {
//                                    if (!dialog.isShowing()) {
//                                        dialog.show();
//                                    }
                                    holder.qjLove2.setEnabled(false);
                                    cancelLoveQJ(qjPosition + 1, sceneList.get(qjPosition + 1).get_id(), holder, true);
                                } else {
//                                    if (!dialog.isShowing()) {
//                                        dialog.show();
//                                    }
                                    holder.qjLove2.setEnabled(false);
                                    loveQJ(qjPosition + 1, sceneList.get(qjPosition + 1).get_id(), holder, true);
                                }
                                return;
                            }
                            if (activity instanceof MainActivity) {
                                MainApplication.which_activity = DataConstants.FindFragment;
                                ((MainActivity) activity).which = FindFragment.class.getSimpleName();
                                activity.startActivity(new Intent(activity, OptRegisterLoginActivity.class));
                            }
                        }
                    });
                }
                break;
            case ITEM_SUBJECT:
                holder.subjectContainer.setVisibility(View.VISIBLE);
                holder.qjContainer.setVisibility(View.GONE);
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) holder.subjectContainer.getLayoutParams();
                layoutParams.width = MainApplication.getContext().getScreenWidth();
                layoutParams.height = layoutParams.width * 422 / 750;
                holder.subjectContainer.setLayoutParams(layoutParams);
                final int subjectPosition = (position + 1) / 6 - 1;
                ImageLoader.getInstance().displayImage(subjectList.get(subjectPosition).getCover_url(), holder.subjectImg);
                holder.subjectName.setText(subjectList.get(subjectPosition).getTitle());
                holder.subjectName2.setText(subjectList.get(subjectPosition).getShort_title());
                holder.subjectContainer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        switch (subjectList.get(subjectPosition).getType()) {
                            case 1:
                                intent.setClass(activity, ArticalDetailActivity.class);
                                intent.putExtra(ArticalDetailActivity.class.getSimpleName(), subjectList.get(subjectPosition).get_id());
                                break;
                            case 2:
                                intent.setClass(activity, ActivityDetailActivity.class);
                                intent.putExtra(ActivityDetailActivity.class.getSimpleName(), subjectList.get(subjectPosition).get_id());
                                break;
                            case 3:
                                intent.setClass(activity, SalePromotionDetailActivity.class);
                                intent.putExtra(SalePromotionDetailActivity.class.getSimpleName(), subjectList.get(subjectPosition).get_id());
                                break;
                            default:
                                intent.setClass(activity, NewProductDetailActivity.class);
                                intent.putExtra(NewProductDetailActivity.class.getSimpleName(), subjectList.get(subjectPosition).get_id());
                                break;
                        }
                        activity.startActivity(intent);
                    }
                });
                switch (subjectList.get(subjectPosition).getType()) {
                    case 1:
                        holder.subjectLabel.setImageResource(R.mipmap.subject_wenzhang_big);
                        break;
                    case 2:
                        holder.subjectLabel.setImageResource(R.mipmap.subject_huodong_big);
                        break;
                    case 3:
                        holder.subjectLabel.setImageResource(R.mipmap.subject_cuxiao_big);
                        break;
                    case 4:
                        holder.subjectLabel.setImageResource(R.mipmap.subject_xinpin_big);
                        break;
                }
                break;
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
                    sceneList.get(position).setIs_love(0);
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
                    sceneList.get(position).setIs_love(1);
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
