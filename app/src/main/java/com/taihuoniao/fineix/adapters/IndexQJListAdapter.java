package com.taihuoniao.fineix.adapters;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mapapi.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.base.NetBean;
import com.taihuoniao.fineix.beans.IndexUserListBean;
import com.taihuoniao.fineix.beans.LoginInfo;
import com.taihuoniao.fineix.beans.SceneList;
import com.taihuoniao.fineix.beans.SceneLoveBean;
import com.taihuoniao.fineix.main.MainApplication;
import com.taihuoniao.fineix.map.MapNearByCJActivity;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.DataConstants;
import com.taihuoniao.fineix.product.BuyGoodsDetailsActivity;
import com.taihuoniao.fineix.product.GoodsDetailActivity;
import com.taihuoniao.fineix.qingjingOrSceneDetails.CommentListActivity;
import com.taihuoniao.fineix.qingjingOrSceneDetails.ReportActivity;
import com.taihuoniao.fineix.qingjingOrSceneDetails.SearchActivity;
import com.taihuoniao.fineix.qingjingOrSceneDetails.TestShare;
import com.taihuoniao.fineix.user.FocusActivity;
import com.taihuoniao.fineix.user.OptRegisterLoginActivity;
import com.taihuoniao.fineix.user.UserCenterActivity;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.view.LabelView;
import com.taihuoniao.fineix.view.ListViewForScrollView;
import com.taihuoniao.fineix.view.WaittingDialog;
import com.taihuoniao.fineix.view.roundImageView.RoundedImageView;

import java.lang.reflect.Type;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by taihuoniao on 2016/8/10.
 */
public class IndexQJListAdapter extends BaseAdapter {
    private Activity activity;
    private List<SceneList.DataBean.RowsBean> sceneList;//情景列表数据
    private List<IndexUserListBean.DataBean.UsersBean> userList;//插入情景列表
    //    private List<IndexUserListBean.DataBean.UsersBean> firstUserList;
//    private List<IndexUserListBean.DataBean.UsersBean> secondUserList;
    private WaittingDialog dialog;
    //popupwindow下的控件
    private View popup_view;
    private PopupWindow popupWindow;
    private TextView shoucangTv;
    private TextView jubaoTv;
    private TextView cancelTv;

    public IndexQJListAdapter(Activity activity, List<SceneList.DataBean.RowsBean> sceneList, List<IndexUserListBean.DataBean.UsersBean> userList) {
        this.activity = activity;
        this.sceneList = sceneList;
        this.userList = userList;
        dialog = new WaittingDialog(activity);
        initPopupWindow();
//        firstUserList = userList.subList(0, userList.size() / 2);
//        secondUserList = userList.subList(userList.size() / 2 - 1, userList.size());
    }

    private void initPopupWindow() {
        popup_view = View.inflate(activity, R.layout.popup_scene_details_more, null);
        shoucangTv = (TextView) popup_view.findViewById(R.id.popup_scene_detail_shoucang);
        jubaoTv = (TextView) popup_view.findViewById(R.id.popup_scene_detail_more_jubao);
        cancelTv = (TextView) popup_view.findViewById(R.id.popup_scene_detail_more_cancel);
        popupWindow = new PopupWindow(popup_view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        // 设置动画效果
        popupWindow.setAnimationStyle(R.style.popupwindow_style);
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams params = activity.getWindow().getAttributes();
                params.alpha = 1f;
                activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                activity.getWindow().setAttributes(params);
            }
        });
        popupWindow.setBackgroundDrawable(ContextCompat.getDrawable(activity,
                R.color.nothing));
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });
    }

    private void showPopup(final int position) {
        if (LoginInfo.getUserId() == Long.parseLong(sceneList.get(position).getUser_id())) {
            //自己不能举报自己。改为删除
            jubaoTv.setText("删除");
        } else {
            jubaoTv.setText("举报");
        }
        if (sceneList.get(position).getIs_favorite() == 1) {
            shoucangTv.setText("取消收藏");
        } else {
            shoucangTv.setText("收藏");
        }
        shoucangTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                if (!LoginInfo.isUserLogin()) {
                    MainApplication.which_activity = DataConstants.IndexFragment;
                    activity.startActivity(new Intent(activity, OptRegisterLoginActivity.class));
                    return;
                }
                if (sceneList.get(position).getIs_favorite() == 1) {
                    if (!dialog.isShowing()) {
                        dialog.show();
                    }
                    cancelShoucang(position);
                } else {
                    if (!dialog.isShowing()) {
                        dialog.show();
                    }
                    shoucang(position);
                }
            }
        });
        jubaoTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                if (!LoginInfo.isUserLogin()) {
                    MainApplication.which_activity = DataConstants.IndexFragment;
                    activity.startActivity(new Intent(activity, OptRegisterLoginActivity.class));
                    return;
                }
                if (LoginInfo.getUserId() == Long.parseLong(sceneList.get(position).getUser_id())) {
                    if (!dialog.isShowing()) {
                        dialog.show();
                    }
                    deleteScene(sceneList.get(position).get_id());
                    //过滤自己
                    return;
                }
                Intent intent1 = new Intent(activity, ReportActivity.class);
                intent1.putExtra("target_id", sceneList.get(position).get_id());
                intent1.putExtra("type", 4 + "");
                activity.startActivity(intent1);
            }
        });
        cancelTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        WindowManager.LayoutParams params = activity.getWindow().getAttributes();
        params.alpha = 0.4f;
        activity.getWindow().setAttributes(params);
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        popupWindow.showAtLocation(popup_view, Gravity.BOTTOM, 0, 0);
    }

    @Override
    public int getCount() {
        return sceneList == null ? 0 : sceneList.size();
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
    public View getView(final int position, View convertView, final ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(parent.getContext(), R.layout.item_index_qj, null);
            holder = new ViewHolder(convertView);
            holder.userRecycler.setHasFixedSize(true);
            holder.userRecycler.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));
            ViewGroup.LayoutParams layoutParams = holder.container.getLayoutParams();
            layoutParams.width = MainApplication.getContext().getScreenWidth();
            layoutParams.height = layoutParams.width;
            holder.container.setLayoutParams(layoutParams);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (position == 6) {
            holder.userRecycler.setAdapter(new UserAdapter(activity, userList));
            holder.userRecycler.setVisibility(View.VISIBLE);
        } else if (position == 17) {
            holder.userRecycler.setAdapter(new UserAdapter(activity, userList));
            holder.userRecycler.setVisibility(View.VISIBLE);
        } else {
            holder.userRecycler.setAdapter(null);
            holder.userRecycler.setVisibility(View.GONE);
        }
        //停止商品动画
        for (int i = 0; i < holder.labelContainer.getChildCount(); i++) {
            LabelView view = (LabelView) holder.labelContainer.getChildAt(i);
            view.stopAnim();
        }
        //移除所有标签
        holder.labelContainer.removeAllViews();
        ImageLoader.getInstance().displayImage(sceneList.get(position).getUser_info().getAvatar_url(), holder.headImg);
        if (sceneList.get(position).getUser_info().getIs_expert() == 1) {
            holder.vImg.setVisibility(View.VISIBLE);
        } else {
            holder.vImg.setVisibility(View.GONE);
        }
        holder.userNameTv.setText(sceneList.get(position).getUser_info().getNickname());
        holder.publishTime.setText(sceneList.get(position).getCreated_at());
        holder.locationTv.setText(sceneList.get(position).getAddress());
        if (LoginInfo.getUserId() == Long.parseLong(sceneList.get(position).getUser_id())) {
            //自己的话隐藏关注按钮
            holder.attentionBtn.setVisibility(View.GONE);
        } else {
            holder.attentionBtn.setVisibility(View.VISIBLE);
            if (sceneList.get(position).getUser_info().getIs_follow() == 1) {
                holder.attentionBtn.setBackgroundResource(R.drawable.shape_corner_969696_nothing);
                holder.attentionBtn.setText("已关注");
            } else {
                holder.attentionBtn.setBackgroundResource(R.drawable.shape_corner_969696_nothing);
                holder.attentionBtn.setText("+ 关注");
            }
        }
        ImageLoader.getInstance().displayImage(sceneList.get(position).getCover_url(), holder.qjImg);
        holder.viewCount.setText(sceneList.get(position).getView_count());
        holder.loveCount.setText(sceneList.get(position).getLove_count());
        if (sceneList.get(position).getIs_love() == 1) {
            holder.loveImg.setImageResource(R.mipmap.index_has_love);
        } else {
            holder.loveImg.setImageResource(R.mipmap.index_love);
        }
        int sta = 0;
        SpannableString spannableStringBuilder = new SpannableString(sceneList.get(position).getDes());
        while (sceneList.get(position).getDes().substring(sta).contains("#")) {
            TextClick textClick;
            sta = sceneList.get(position).getDes().indexOf("#", sta);
            if (sceneList.get(position).getDes().substring(sta).contains(" ")) {
                int en = sceneList.get(position).getDes().indexOf(" ", sta);
                textClick = new TextClick(activity, sceneList.get(position).getDes().substring(sta + 1, en));
                spannableStringBuilder.setSpan(textClick, sta, en + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                sta = en;
            } else {
                textClick = new TextClick(activity, sceneList.get(position).getDes().substring(sta + 1, sceneList.get(position).getDes().length()));
                spannableStringBuilder.setSpan(textClick, sta, sceneList.get(position).getDes().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                break;
            }
        }
        holder.qjDesTv.setText(spannableStringBuilder);
        holder.qjDesTv.setMovementMethod(LinkMovementMethod.getInstance());
        holder.qjDesTv.setMaxLines(3);
//        holder.qjDesTv.setEllipsize(TextUtils.TruncateAt.END);
        holder.commentList.setAdapter(new IndexCommentAdapter(sceneList.get(position).getComments()));
        if (sceneList.get(position).getComment_count() > 0) {
            holder.moreComment.setText("查看所有" + sceneList.get(position).getComment_count() + "条评论");
            holder.moreComment.setVisibility(View.VISIBLE);
        } else {
            holder.moreComment.setVisibility(View.GONE);
        }
        //设置情景标题
        holder.qjTitleTv.setText(sceneList.get(position).getTitle());
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
                } else {
                    holder.qjTitleTv2.setVisibility(View.GONE);
                }
            }
        });
        //添加商品标签
        for (final SceneList.DataBean.RowsBean.ProductBean productBean : sceneList.get(position).getProduct()) {
            final RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            final LabelView labelView = new LabelView(parent.getContext());
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
                        lp.leftMargin = (int) (productBean.getX() * MainApplication.getContext().getScreenWidth() - labelView.labelMargin - labelView.pointWidth / 2);
                        lp.topMargin = (int) (productBean.getY() * MainApplication.getContext().getScreenWidth() - labelView.getMeasuredHeight() + labelView.pointWidth / 2);
                        labelView.setLayoutParams(lp);
                    } else {
                        labelView.nameTv.setBackgroundResource(R.drawable.label_right);
                        RelativeLayout.LayoutParams layoutParams1 = (RelativeLayout.LayoutParams) labelView.pointContainer.getLayoutParams();
                        layoutParams1.leftMargin = (int) (labelView.nameTv.getMeasuredWidth() - labelView.pointWidth - labelView.labelMargin);
                        labelView.pointContainer.setLayoutParams(layoutParams1);
                        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) labelView.getLayoutParams();
                        lp.leftMargin = (int) (productBean.getX() * MainApplication.getContext().getScreenWidth() - labelView.getMeasuredWidth() + labelView.labelMargin + labelView.pointWidth / 2);
                        lp.topMargin = (int) (productBean.getY() * MainApplication.getContext().getScreenWidth() - labelView.getMeasuredHeight() + labelView.pointWidth / 2);
                        labelView.setLayoutParams(lp);
                    }
                }
            });
            holder.labelContainer.addView(labelView);
//            Log.e("<<<", "开启动画" + holder.qjTitleTv.getText() + ",现在位置=" + position);
            labelView.wave();
            labelView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    switch (productBean.getType()) {
                        case 2:
                            intent.setClass(activity, BuyGoodsDetailsActivity.class);
                            break;
                        default:
                            intent.setClass(activity, GoodsDetailActivity.class);
                            break;
                    }
                    intent.putExtra("id", productBean.getId());
                    parent.getContext().startActivity(intent);
                }
            });
        }
        //跳转个人中心
        holder.headImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(parent.getContext(), UserCenterActivity.class);
                long l = Long.valueOf(sceneList.get(position).getUser_info().getUser_id());
                intent.putExtra(FocusActivity.USER_ID_EXTRA, l);
                parent.getContext().startActivity(intent);
            }
        });
        //跳转情景地图
        holder.mapLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                跳转到地图界面，查看附近的情境
                String address = sceneList.get(position).getAddress();
                LatLng ll = new LatLng(sceneList.get(position).getLocation().getCoordinates().get(1), sceneList.get(position).getLocation().getCoordinates().get(0));
                Intent intent2 = new Intent(activity, MapNearByCJActivity.class);
                intent2.putExtra("address", address);
                intent2.putExtra(MapNearByCJActivity.class.getSimpleName(), ll);
                activity.startActivity(intent2);
            }
        });
        //关注或取消关注
        holder.attentionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (LoginInfo.isUserLogin()) {
                    //已经登录
                    if (LoginInfo.getUserId() == Long.parseLong(sceneList.get(position).getUser_id())) {
                        //过滤自己
                        return;
                    }
                    if (sceneList.get(position).getUser_info().getIs_follow() == 0) {
                        if (!dialog.isShowing()) {
                            dialog.show();
                        }
                        fllow(position, sceneList.get(position).getUser_id(), holder);
                    } else {
                        if (!dialog.isShowing()) {
                            dialog.show();
                        }
                        cancelFollow(position, sceneList.get(position).getUser_id(), holder);
                    }
                    return;
                }
                MainApplication.which_activity = DataConstants.IndexFragment;
                activity.startActivity(new Intent(activity, OptRegisterLoginActivity.class));
            }
        });

        //点赞或取消点赞
        holder.loveRelative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (LoginInfo.isUserLogin()) {
                    //已经登录
                    if (sceneList.get(position).getIs_love() == 1) {
//                        holder.loveImg.setImageResource(R.mipmap.has_love);
                        if (!dialog.isShowing()) {
                            dialog.show();
                        }
                        cancelLoveQJ(position, sceneList.get(position).get_id(), holder);
                    } else {
//                        holder.loveImg.setImageResource(R.mipmap.index_love);
                        if (!dialog.isShowing()) {
                            dialog.show();
                        }
                        loveQJ(position, sceneList.get(position).get_id(), holder);
                    }
                    return;
                }
                MainApplication.which_activity = DataConstants.IndexFragment;
                activity.startActivity(new Intent(activity, OptRegisterLoginActivity.class));
            }
        });
        //跳转到评论界面
        holder.commentImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent3 = new Intent(activity, CommentListActivity.class);
                intent3.putExtra("target_id", sceneList.get(position).get_id());
                intent3.putExtra("type", 12 + "");
                intent3.putExtra("target_user_id", sceneList.get(position).getUser_info().getUser_id());
                activity.startActivity(intent3);
            }
        });
        holder.commentList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> pare, View vie, int positi, long d) {
                Intent intent3 = new Intent(activity, CommentListActivity.class);
                intent3.putExtra("target_id", sceneList.get(position).get_id());
                intent3.putExtra("type", 12 + "");
                intent3.putExtra("target_user_id", sceneList.get(position).getUser_info().getUser_id());
                activity.startActivity(intent3);
            }
        });
        holder.moreComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent3 = new Intent(activity, CommentListActivity.class);
                intent3.putExtra("target_id", sceneList.get(position).get_id());
                intent3.putExtra("type", 12 + "");
                intent3.putExtra("target_user_id", sceneList.get(position).getUser_info().getUser_id());
                activity.startActivity(intent3);
            }
        });
        //跳转到分享页面
        holder.shareImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent4 = new Intent(activity, TestShare.class);
                intent4.putExtra("id", sceneList.get(position).get_id());
                activity.startActivity(intent4);
            }
        });
        //更多
        holder.moreImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup(position);
            }
        });
        //情景描述添加监听
        holder.qjDesTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    if (holder.qjDesTv.getMaxLines() == 3) {
                        holder.qjDesTv.setMaxLines(Integer.MAX_VALUE);
                    } else {
                        holder.qjDesTv.setMaxLines(3);
                    }
                }
            }
        });
        return convertView;
    }

    //取消收藏情景
    private void cancelShoucang(final int position) {
        ClientDiscoverAPI.cancelShoucang(sceneList.get(position).get_id(), "12", new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                NetBean netBean = new NetBean();
                try {
                    Gson gson = new Gson();
                    Type type = new TypeToken<NetBean>() {
                    }.getType();
                    netBean = gson.fromJson(responseInfo.result, type);
                } catch (JsonSyntaxException e) {
                    Log.e("<<<取消收藏情景", "数据解析异常");
                }
                dialog.dismiss();
                if (netBean.isSuccess()) {
                    ToastUtils.showSuccess(netBean.getMessage());
                    sceneList.get(position).setIs_favorite(0);
                } else {
                    ToastUtils.showError(netBean.getMessage());
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                dialog.dismiss();
                ToastUtils.showError(R.string.net_fail);
            }
        });
    }

    //收藏情景
    private void shoucang(final int position) {
        ClientDiscoverAPI.shoucang(sceneList.get(position).get_id(), "12", new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                NetBean netBean = new NetBean();
                try {
                    Gson gson = new Gson();
                    Type type = new TypeToken<NetBean>() {
                    }.getType();
                    netBean = gson.fromJson(responseInfo.result, type);
                } catch (JsonSyntaxException e) {
                    Log.e("<<<收藏情景", "数据解析异常");
                }
                dialog.dismiss();
                if (netBean.isSuccess()) {
                    ToastUtils.showSuccess(netBean.getMessage());
                    sceneList.get(position).setIs_favorite(1);
                } else {
                    ToastUtils.showError(netBean.getMessage());
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                dialog.dismiss();
                ToastUtils.showError(R.string.net_fail);
            }
        });
    }

    //删除情景
    private void deleteScene(String i) {
        ClientDiscoverAPI.deleteScene(i, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                NetBean netBean = new NetBean();
                try {
                    Gson gson = new Gson();
                    Type type = new TypeToken<NetBean>() {
                    }.getType();
                    netBean = gson.fromJson(responseInfo.result, type);
                } catch (JsonSyntaxException e) {
                    Log.e("<<<删除场景", "数据解析异常");
                }
                dialog.dismiss();
                if (netBean.isSuccess()) {
                    ToastUtils.showSuccess(netBean.getMessage());
                    notifyDataSetChanged();
                } else {
                    ToastUtils.showError(netBean.getMessage());
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                dialog.dismiss();
                ToastUtils.showError(R.string.net_fail);
            }
        });
    }

    //取消点赞
    private void cancelLoveQJ(final int position, String id, final ViewHolder holder) {
        ClientDiscoverAPI.cancelLoveQJ(id, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
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
                    holder.loveImg.setImageResource(R.mipmap.index_love);
                    holder.loveCount.setText(sceneLoveBean.getData().getLove_count() + "");
                    sceneList.get(position).setIs_love(0);
                    sceneList.get(position).setLove_count(sceneLoveBean.getData().getLove_count() + "");
                } else {
                    ToastUtils.showError(sceneLoveBean.getMessage());
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                dialog.dismiss();
                ToastUtils.showError(R.string.net_fail);
            }
        });
    }

    //点赞情景
    private void loveQJ(final int position, String id, final ViewHolder holder) {
        ClientDiscoverAPI.loveQJ(id, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
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
                    holder.loveImg.setImageResource(R.mipmap.index_has_love);
                    holder.loveCount.setText(sceneLoveBean.getData().getLove_count() + "");
                    sceneList.get(position).setIs_love(1);
                    sceneList.get(position).setLove_count(sceneLoveBean.getData().getLove_count() + "");
                } else {
                    ToastUtils.showError(sceneLoveBean.getMessage());
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                dialog.dismiss();
                ToastUtils.showError(R.string.net_fail);
            }
        });
    }

    //关注用户
    private void fllow(final int position, String otherUserId, final ViewHolder holder) {
        ClientDiscoverAPI.focusOperate(otherUserId, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                dialog.dismiss();
                Log.e("<<<关注用户", responseInfo.result);
                NetBean netBean = new NetBean();
                try {
                    Gson gson = new Gson();
                    Type type = new TypeToken<NetBean>() {
                    }.getType();
                    netBean = gson.fromJson(responseInfo.result, type);
                } catch (JsonSyntaxException e) {
                    Log.e("<<<", "解析异常");
                }
                if (netBean.isSuccess()) {
                    holder.attentionBtn.setBackgroundResource(R.drawable.corner_yellow);
                    holder.attentionBtn.setText("已关注");
                    sceneList.get(position).getUser_info().setIs_follow(1);
                    for (SceneList.DataBean.RowsBean rowsBean : sceneList) {
                        if (rowsBean.getUser_id().equals(sceneList.get(position).getUser_id())) {
                            rowsBean.getUser_info().setIs_follow(1);
                        }
                    }
                } else {
                    ToastUtils.showError(netBean.getMessage());
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                dialog.dismiss();
                ToastUtils.showError(R.string.net_fail);
            }
        });
    }

    //取消关注
    private void cancelFollow(final int position, final String otherUserId, final ViewHolder holder) {
        ClientDiscoverAPI.cancelFocusOperate(otherUserId, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                dialog.dismiss();
                NetBean netBean = new NetBean();
                try {
                    Gson gson = new Gson();
                    Type type = new TypeToken<NetBean>() {
                    }.getType();
                    netBean = gson.fromJson(responseInfo.result, type);
                } catch (JsonSyntaxException e) {
                    Log.e("<<<", "解析异常");
                }
                if (netBean.isSuccess()) {
                    holder.attentionBtn.setBackgroundResource(R.drawable.shape_corner_969696_nothing);
                    holder.attentionBtn.setText("+ 关注");
                    for (SceneList.DataBean.RowsBean rowsBean : sceneList) {
                        if (rowsBean.getUser_id().equals(sceneList.get(position).getUser_id())) {
                            rowsBean.getUser_info().setIs_follow(0);
                        }
                    }
                    return;
                }
                ToastUtils.showError(netBean.getMessage());
            }

            @Override
            public void onFailure(HttpException e, String s) {
                dialog.dismiss();
                ToastUtils.showError(R.string.net_fail);
            }
        });
    }

    static class ViewHolder {
        @Bind(R.id.user_recycler)
        RecyclerView userRecycler;
        @Bind(R.id.head_img)
        RoundedImageView headImg;
        @Bind(R.id.v_img)
        ImageView vImg;
        @Bind(R.id.attention_btn)
        Button attentionBtn;
        @Bind(R.id.user_name_tv)
        TextView userNameTv;
        @Bind(R.id.map_linear)
        LinearLayout mapLinear;
        @Bind(R.id.publish_time)
        TextView publishTime;
        @Bind(R.id.location_tv)
        TextView locationTv;
        @Bind(R.id.container)
        RelativeLayout container;
        @Bind(R.id.qj_img)
        ImageView qjImg;
        @Bind(R.id.qj_title_tv)
        TextView qjTitleTv;
        @Bind(R.id.qj_title_tv2)
        TextView qjTitleTv2;
        @Bind(R.id.label_container)
        RelativeLayout labelContainer;
        @Bind(R.id.view_count)
        TextView viewCount;
        @Bind(R.id.more_img)
        ImageView moreImg;
        @Bind(R.id.share_img)
        ImageView shareImg;
        @Bind(R.id.comment_img)
        ImageView commentImg;
        @Bind(R.id.love_container)
        RelativeLayout loveRelative;
        @Bind(R.id.love_count)
        TextView loveCount;
        @Bind(R.id.love_img)
        ImageView loveImg;
        @Bind(R.id.qj_des_tv)
        TextView qjDesTv;
        @Bind(R.id.comment_list)
        ListViewForScrollView commentList;
        @Bind(R.id.more_comment)
        TextView moreComment;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    //用户列表适配器
    static class UserAdapter extends RecyclerView.Adapter<UserAdapter.VH> implements View.OnClickListener {
        private Activity activity;
        private List<IndexUserListBean.DataBean.UsersBean> userList;
        private WaittingDialog dialog;

        public UserAdapter(Activity activity, List<IndexUserListBean.DataBean.UsersBean> userList) {
            this.activity = activity;
            this.userList = userList;
            dialog = new WaittingDialog(activity);
        }

        @Override
        public VH onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = View.inflate(parent.getContext(), R.layout.item_index_user, null);
            VH holder = new VH(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(final VH holder, final int position) {
            ImageLoader.getInstance().displayImage(userList.get(position).getMedium_avatar_url(), holder.headImg);
            holder.name.setText(userList.get(position).getNickname());
            if (userList.get(position).getIdentify().getIs_expert() == 1) {
                holder.summary.setText(userList.get(position).getExpert_label() + " " + userList.get(position).getExpert_info());
            } else {
                holder.summary.setText(userList.get(position).getSummary());
            }
            if (userList.get(position).getIs_follow() == 1) {
                holder.followBtn.setBackgroundResource(R.drawable.corner_yellow);
                holder.followBtn.setTextColor(activity.getResources().getColor(R.color.white));
                holder.followBtn.setText("已关注");
            } else {
                holder.followBtn.setBackgroundResource(R.drawable.shape_corner_969696_nothing);
                holder.followBtn.setTextColor(activity.getResources().getColor(R.color.title_black));
                holder.followBtn.setText("+关注");
            }
            holder.headImg.setTag(userList.get(position).get_id());
            holder.headImg.setOnClickListener(this);
            holder.delete.setTag(userList.get(position).get_id());
            holder.delete.setOnClickListener(this);
            holder.followBtn.setTag(userList.get(position).get_id());
            holder.followBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (LoginInfo.isUserLogin()) {
                        int k = -1;
                        String t = (String) v.getTag();
                        for (int j = 0; j < userList.size(); j++) {
                            if (userList.get(j).get_id().equals(t)) {
                                k = j;
                                break;
                            }
                        }
                        if (k == -1) {
                            return;
                        }
                        //已经登录
                        if (LoginInfo.getUserId() == Long.parseLong(userList.get(k).get_id())) {
                            //过滤自己
                            return;
                        }
                        if (userList.get(k).getIs_follow() == 1) {
                            if (!dialog.isShowing()) {
                                dialog.show();
                            }
                            cancelFollow(k, userList.get(k).get_id(), holder);
                        } else {
                            if (!dialog.isShowing()) {
                                dialog.show();
                            }
                            fllow(k, userList.get(k).get_id(), holder);
                        }
                        return;
                    }
                    MainApplication.which_activity = DataConstants.IndexFragment;
                    activity.startActivity(new Intent(activity, OptRegisterLoginActivity.class));
                }
            });
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.head_img:
                    Intent intent = new Intent(activity, UserCenterActivity.class);
                    long l = Long.valueOf((String) v.getTag());
                    intent.putExtra(FocusActivity.USER_ID_EXTRA, l);
                    activity.startActivity(intent);
                    break;
                case R.id.delete:
                    int i = -1;
                    String tag = (String) v.getTag();
                    for (int j = 0; j < userList.size(); j++) {
                        if (userList.get(j).get_id().equals(tag)) {
                            i = j;
                            break;
                        }
                    }
                    if (i == -1) {
                        return;
                    }
                    userList.remove(i);
                    notifyItemRemoved(i);
                    break;
            }
        }

        //关注用户
        private void fllow(final int position, String otherUserId, final VH holder) {
            ClientDiscoverAPI.focusOperate(otherUserId, new RequestCallBack<String>() {
                @Override
                public void onSuccess(ResponseInfo<String> responseInfo) {
                    dialog.dismiss();
                    Log.e("<<<关注用户", responseInfo.result);
                    NetBean netBean = new NetBean();
                    try {
                        Gson gson = new Gson();
                        Type type = new TypeToken<NetBean>() {
                        }.getType();
                        netBean = gson.fromJson(responseInfo.result, type);
                    } catch (JsonSyntaxException e) {
                        Log.e("<<<", "解析异常");
                    }
                    if (netBean.isSuccess()) {
                        holder.followBtn.setBackgroundResource(R.drawable.corner_yellow);
                        holder.followBtn.setTextColor(activity.getResources().getColor(R.color.white));
                        holder.followBtn.setText("已关注");
                        userList.get(position).setIs_follow(1);
                    } else {
                        ToastUtils.showError(netBean.getMessage());
                    }
                }

                @Override
                public void onFailure(HttpException error, String msg) {
                    dialog.dismiss();
                    ToastUtils.showError(R.string.net_fail);
                }
            });
        }

        //取消关注
        private void cancelFollow(final int position, final String otherUserId, final VH holder) {
            ClientDiscoverAPI.cancelFocusOperate(otherUserId, new RequestCallBack<String>() {
                @Override
                public void onSuccess(ResponseInfo<String> responseInfo) {
                    dialog.dismiss();
                    NetBean netBean = new NetBean();
                    try {
                        Gson gson = new Gson();
                        Type type = new TypeToken<NetBean>() {
                        }.getType();
                        netBean = gson.fromJson(responseInfo.result, type);
                    } catch (JsonSyntaxException e) {
                        Log.e("<<<", "解析异常");
                    }
                    if (netBean.isSuccess()) {
                        holder.followBtn.setBackgroundResource(R.drawable.shape_corner_969696_nothing);
                        holder.followBtn.setTextColor(activity.getResources().getColor(R.color.title_black));
                        holder.followBtn.setText("+关注");
                        userList.get(position).setIs_follow(0);
                        return;
                    }
                    ToastUtils.showError(netBean.getMessage());
                }

                @Override
                public void onFailure(HttpException e, String s) {
                    dialog.dismiss();
                    ToastUtils.showError(R.string.net_fail);
                }
            });
        }

        @Override
        public int getItemCount() {
            return userList == null ? 0 : userList.size();
        }


        static class VH extends RecyclerView.ViewHolder {
            @Bind(R.id.head_img)
            RoundedImageView headImg;
            @Bind(R.id.name)
            TextView name;
            @Bind(R.id.summary)
            TextView summary;
            @Bind(R.id.follow_btn)
            Button followBtn;
            @Bind(R.id.delete)
            ImageView delete;

            public VH(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }

    //设置部分文字可以点击
    static class TextClick extends ClickableSpan {
        private Activity activity;
        private String q;

        public TextClick(Activity activity, String q) {
            this.activity = activity;
            this.q = q;
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setColor(activity.getResources().getColor(R.color.yellow_bd8913));
            ds.setUnderlineText(false);
        }

        @Override
        public void onClick(View widget) {
            Intent intent = new Intent(activity, SearchActivity.class);
            intent.putExtra("q", q);
            intent.putExtra("t", 9);
            activity.startActivity(intent);
        }
    }

    static class IndexCommentAdapter extends BaseAdapter {
        private List<SceneList.DataBean.RowsBean.CommentsBean> commentList;

        public IndexCommentAdapter(List<SceneList.DataBean.RowsBean.CommentsBean> commentList) {
            this.commentList = commentList;
        }

        @Override
        public int getCount() {
            if (commentList == null) {
                return 0;
            }
            if (commentList.size() <= 2) {
                return commentList.size();
            }
            return 2;
        }

        @Override
        public Object getItem(int position) {
            return commentList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(parent.getContext(), R.layout.item_index_comment, null);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            ImageLoader.getInstance().displayImage(commentList.get(position).getUser_avatar_url(), holder.headImg);
            SpannableStringBuilder spannableString = new SpannableStringBuilder(commentList.get(position).getUser_nickname() + ": " + commentList.get(position).getContent());
            ForegroundColorSpan backgroundColorSpan = new ForegroundColorSpan(parent.getResources().getColor(R.color.black));
            spannableString.setSpan(backgroundColorSpan, 0, commentList.get(position).getUser_nickname().length() + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(parent.getResources().getColor(R.color.color_666));
            spannableString.setSpan(foregroundColorSpan, commentList.get(position).getUser_nickname().length() + 1, spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.contentTv.setText(spannableString);
            return convertView;
        }

        static class ViewHolder {
            @Bind(R.id.head_img)
            RoundedImageView headImg;
            @Bind(R.id.content_tv)
            TextView contentTv;

            ViewHolder(View view) {
                ButterKnife.bind(this, view);
            }
        }
    }
}
