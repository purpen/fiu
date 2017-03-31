package com.taihuoniao.fineix.adapters;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
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
import com.google.gson.reflect.TypeToken;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.base.HttpRequest;
import com.taihuoniao.fineix.beans.HttpResponse;
import com.taihuoniao.fineix.beans.LoginInfo;
import com.taihuoniao.fineix.beans.QJFavoriteBean;
import com.taihuoniao.fineix.beans.SceneList;
import com.taihuoniao.fineix.beans.SceneLoveBean;
import com.taihuoniao.fineix.common.GlobalDataCallBack;
import com.taihuoniao.fineix.main.MainApplication;
import com.taihuoniao.fineix.main.fragment.IndexFragment;
import com.taihuoniao.fineix.map.MapNearByCJActivity;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.DataConstants;
import com.taihuoniao.fineix.network.URL;
import com.taihuoniao.fineix.product.BuyGoodsDetailsActivity;
import com.taihuoniao.fineix.qingjingOrSceneDetails.CommentListActivity;
import com.taihuoniao.fineix.qingjingOrSceneDetails.QJPictureActivity;
import com.taihuoniao.fineix.qingjingOrSceneDetails.ReportActivity;
import com.taihuoniao.fineix.qingjingOrSceneDetails.SearchActivity;
import com.taihuoniao.fineix.qingjingOrSceneDetails.ShareActivity;
import com.taihuoniao.fineix.qingjingOrSceneDetails.bean.SceneListBean2;
import com.taihuoniao.fineix.scene.CreateQJActivity;
import com.taihuoniao.fineix.user.FocusActivity;
import com.taihuoniao.fineix.user.OptRegisterLoginActivity;
import com.taihuoniao.fineix.user.UserCenterActivity;
import com.taihuoniao.fineix.utils.DensityUtils;
import com.taihuoniao.fineix.utils.GlideUtils;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.PopupWindowUtil;
import com.taihuoniao.fineix.utils.SceneTitleSetUtils;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.utils.TypeConversionUtils;
import com.taihuoniao.fineix.utils.Util;
import com.taihuoniao.fineix.view.ClickImageView;
import com.taihuoniao.fineix.view.LabelView;
import com.taihuoniao.fineix.view.ListViewForScrollView;
import com.taihuoniao.fineix.view.dialog.WaittingDialog;
import com.taihuoniao.fineix.view.roundImageView.RoundedImageView;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by taihuoniao on 2016/8/24.
 */
public class FindQJSceneListAdapter extends BaseAdapter {
    private Activity activity;
    private List<SceneListBean2.RowsEntity> sceneList;//情景列表数据
    private WaittingDialog dialog;
    private int pos;

    //popupwindow下的控件
    private View popup_view;
    private PopupWindow popupWindow;
    private TextView bianjiTv;
    private TextView shoucangTv;
    private TextView jubaoTv;
    private TextView cancelTv;

    public FindQJSceneListAdapter(Activity activity, List<SceneListBean2.RowsEntity> sceneList) {
        this.activity = activity;
        this.sceneList = sceneList;
        dialog = new WaittingDialog(activity);
        initPopupWindow();
    }

    public int getPos() {
        return pos;
    }

    private void initPopupWindow() {
        popup_view = View.inflate(activity, R.layout.popup_scene_details_more, null);
        bianjiTv = (TextView) popup_view.findViewById(R.id.popup_scene_detail_more_bianji);
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
            }
        });
    }

    private void showPopup(final int position) {
        if (LoginInfo.getUserId() == Long.parseLong(sceneList.get(position).getUser_id())) {
            //自己不能举报自己。改为删除
            jubaoTv.setText("删除");
            bianjiTv.setVisibility(View.VISIBLE);
        } else {
            bianjiTv.setVisibility(View.GONE);
            jubaoTv.setText("举报");
        }
        if (TypeConversionUtils.StringConvertInt(sceneList.get(position).getIs_favorite()) == 1) {
            shoucangTv.setText("取消收藏");
        } else {
            shoucangTv.setText("收藏");
        }
        bianjiTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, CreateQJActivity.class);
                intent.putExtra(IndexFragment.class.getSimpleName(), sceneList.get(position));
                activity.startActivity(intent);
                popupWindow.dismiss();
            }
        });
        shoucangTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                if (!LoginInfo.isUserLogin()) {
                    MainApplication.which_activity = DataConstants.FindActivity;
                    activity.startActivity(new Intent(activity, OptRegisterLoginActivity.class));
                    return;
                }
                if (TypeConversionUtils.StringConvertInt(sceneList.get(position).getIs_favorite()) == 1) {
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
                    MainApplication.which_activity = DataConstants.FindActivity;
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
            ViewGroup.LayoutParams layoutParams = holder.container.getLayoutParams();
            layoutParams.width = MainApplication.getContext().getScreenWidth();
            layoutParams.height = layoutParams.width;
            holder.container.setLayoutParams(layoutParams);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        GlideUtils.displayImageFadein(sceneList.get(position).getCover_url(), holder.qjImg);
        GlideUtils.displayImage(sceneList.get(position).getUser_info().getAvatar_url(), holder.headImg);
        holder.labelContainer.setVisibility(View.VISIBLE);

        //设置情景标题
        if (TextUtils.isEmpty(sceneList.get(position).getTitle())) {
            holder.qjTitleTv.setVisibility(View.GONE);
        } else {
            holder.qjTitleTv.setText(sceneList.get(position).getTitle());
            holder.qjTitleTv.setVisibility(View.VISIBLE);
        }
        //添加商品标签
        List<SceneListBean2.RowsEntity.ProductEntity> productBeanList = sceneList.get(position).getProduct();
        if (productBeanList != null && compareble(productBeanList, (List<SceneListBean2.RowsEntity.ProductEntity>) holder.labelContainer.getTag(R.id.label_container))) {
            holder.labelContainer.setTag(R.id.label_container, productBeanList);
        } else {
            stopAnimate(holder); //停止商品动画 移除所有标签
            method1(position, holder);
            addGoodsTag(position, parent, holder); //添加商品标签
            holder.labelContainer.setTag(R.id.label_container, productBeanList);
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
                LatLng ll = new LatLng(TypeConversionUtils.StringConvertDouble(sceneList.get(position).getLocation().getCoordinates().get(1)), TypeConversionUtils.StringConvertDouble(sceneList.get(position).getLocation().getCoordinates().get(0)));
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
                    if (TypeConversionUtils.StringConvertInt(sceneList.get(position).getUser_info().getIs_follow()) == 0) {
                        if (!dialog.isShowing()) {
                            dialog.show();
                        }
                        fllow(position, sceneList.get(position).getUser_id(), holder);
                    } else {
                        showFocusFansConfirmView(sceneList.get(position), holder);
                    }
                    return;
                }
                MainApplication.which_activity = DataConstants.FindActivity;
                activity.startActivity(new Intent(activity, OptRegisterLoginActivity.class));
            }
        });

        //点赞或取消点赞
        holder.loveImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (LoginInfo.isUserLogin()) {
                    //已经登录
                    if (TypeConversionUtils.StringConvertInt(sceneList.get(position).getIs_love()) == 1) {
                        holder.loveImg.setEnabled(false);
                        cancelLoveQJ(position, sceneList.get(position).get_id(), holder);
                    } else {
                        holder.loveImg.setEnabled(false);
                        loveQJ(position, sceneList.get(position).get_id(), holder);
                    }
                    return;
                }
                MainApplication.which_activity = DataConstants.FindActivity;
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
                activity.startActivityForResult(intent3, 1);
                pos = position;
            }
        });
        holder.commentList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> pare, View vie, int positi, long d) {
                Intent intent3 = new Intent(activity, CommentListActivity.class);
                intent3.putExtra("target_id", sceneList.get(position).get_id());
                intent3.putExtra("type", 12 + "");
                intent3.putExtra("target_user_id", sceneList.get(position).getUser_info().getUser_id());
                activity.startActivityForResult(intent3, 1);
                pos = position;
            }
        });
        //跳转到分享页面
        holder.shareImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent4 = new Intent(activity, ShareActivity.class);
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
                        holder.qjDesTv.setText(SceneTitleSetUtils.setDes(sceneList.get(position).getDes(), activity));
                        holder.qjDesTv.setMaxLines(Integer.MAX_VALUE);
                    } else {
                        if (holder.qjDesTv.getLineCount() > 3) {
                            Layout layout = holder.qjDesTv.getLayout();
                            String str = sceneList.get(position).getDes().substring(layout.getLineStart(0), layout.getLineEnd(2) - 1) + "…";
                            holder.qjDesTv.setText(SceneTitleSetUtils.setDes(str, activity));
                        }
                        holder.qjDesTv.setMaxLines(3);
                    }
                }
            }
        });
        return convertView;
    }

    /**
     * 添加商品标签
     * @param position
     * @param parent
     * @param holder
     */
    private void addGoodsTag(int position, final ViewGroup parent, ViewHolder holder) {
        for (final SceneListBean2.RowsEntity.ProductEntity productBean : sceneList.get(position).getProduct()) {
            final RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            final LabelView labelView = new LabelView(parent.getContext());
            labelView.nameTv.setText(productBean.getTitle());
            if (TextUtils.equals("0",productBean.getPrice())){
                labelView.price.setVisibility(View.GONE);
            }else {
                labelView.price.setText("¥"+productBean.getPrice());
            }
            labelView.setLayoutParams(layoutParams);
            if (TypeConversionUtils.StringConvertInt(productBean.getLoc()) == 2) {
                labelView.llTag.setBackgroundResource(R.drawable.label_left);
                RelativeLayout.LayoutParams layoutParams1 = (RelativeLayout.LayoutParams) labelView.pointContainer.getLayoutParams();
                layoutParams1.leftMargin = (int) labelView.labelMargin;
                labelView.pointContainer.setLayoutParams(layoutParams1);
            }
            labelView.post(new Runnable() {
                @Override
                public void run() {
                    if (TypeConversionUtils.StringConvertInt(productBean.getLoc()) == 2) {
                        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) labelView.getLayoutParams();
                        lp.leftMargin = (int) (productBean.getX() * MainApplication.getContext().getScreenWidth() - labelView.labelMargin - labelView.pointWidth / 2);
                        lp.topMargin = (int) (productBean.getY() * MainApplication.getContext().getScreenWidth() - labelView.getMeasuredHeight() + labelView.pointWidth / 2);
                        labelView.setLayoutParams(lp);
                    } else {
                        labelView.llTag.setBackgroundResource(R.drawable.label_right);
                        RelativeLayout.LayoutParams layoutParams1 = (RelativeLayout.LayoutParams) labelView.pointContainer.getLayoutParams();
                        layoutParams1.leftMargin = (int) (labelView.llTag.getMeasuredWidth() - labelView.pointWidth - labelView.labelMargin);
                        labelView.pointContainer.setLayoutParams(layoutParams1);
                        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) labelView.getLayoutParams();
                        lp.leftMargin = (int) (productBean.getX() * MainApplication.getContext().getScreenWidth() - labelView.getMeasuredWidth() + labelView.labelMargin + labelView.pointWidth / 2);
                        lp.topMargin = (int) (productBean.getY() * MainApplication.getContext().getScreenWidth() - labelView.getMeasuredHeight() + labelView.pointWidth / 2);
                        labelView.setLayoutParams(lp);
                    }
                }
            });
            holder.labelContainer.addView(labelView);
            labelView.wave();
            labelView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setClass(activity, BuyGoodsDetailsActivity.class);
                    intent.putExtra("id", productBean.getId());
                    parent.getContext().startActivity(intent);
                }
            });
        }
    }

    private void method1(final int position, final ViewHolder holder) {
        if (TypeConversionUtils.StringConvertInt(sceneList.get(position).getUser_info().getIs_expert()) == 1) {
            holder.vImg.setVisibility(View.VISIBLE);
        } else {
            holder.vImg.setVisibility(View.GONE);
        }
        holder.userNameTv.setText(sceneList.get(position).getUser_info().getNickname());
        holder.publishTime.setText(sceneList.get(position).getCreated_at());
        if (TextUtils.isEmpty(sceneList.get(position).getAddress())) {
            holder.locationImg.setVisibility(View.GONE);
            holder.locationTv.setVisibility(View.GONE);
        } else {
            holder.locationTv.setText(sceneList.get(position).getCity() + " " + sceneList.get(position).getAddress());
            holder.locationImg.setVisibility(View.VISIBLE);
            holder.locationTv.setVisibility(View.VISIBLE);
        }
        if (LoginInfo.getUserId() == Long.parseLong(sceneList.get(position).getUser_id())) {
            //自己的话隐藏关注按钮
            holder.attentionBtn.setVisibility(View.GONE);
        } else {
            holder.attentionBtn.setVisibility(View.VISIBLE);
            if (TypeConversionUtils.StringConvertInt(sceneList.get(position).getUser_info().getIs_follow()) == 1) {
//                holder.attentionBtn.setBackgroundResource(R.mipmap.index_has_attention);
                holder.attentionBtn.setBackgroundResource(R.drawable.shape_corner_969696_nothing);
                holder.attentionBtn.setText("已关注");
                holder.attentionBtn.setPadding(DensityUtils.dp2px(activity, 6), 0, DensityUtils.dp2px(activity, 6), 0);
                holder.attentionBtn.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.focus_pic, 0, 0, 0);
                holder.attentionBtn.setTextColor(activity.getResources().getColor(R.color.white));
            } else {
                holder.attentionBtn.setBackgroundResource(R.mipmap.index_attention);
                holder.attentionBtn.setText("");
                holder.attentionBtn.setPadding(0, 0, 0, 0);
                holder.attentionBtn.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            }
        }
        holder.qjImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, QJPictureActivity.class);
                intent.putExtra("img", sceneList.get(position).getCover_url());
                intent.putExtra("fine", TypeConversionUtils.StringConvertInt(sceneList.get(position).getFine()) == 1);
                intent.putExtra("stick", TypeConversionUtils.StringConvertInt(sceneList.get(position).getStick()) == 1);
                intent.putExtra("check", TypeConversionUtils.StringConvertInt(sceneList.get(position).getIs_check()) == 0);
                intent.putExtra("id",sceneList.get(position).get_id());
                activity.startActivity(intent);
            }
        });
        holder.viewCount.setText(sceneList.get(position).getView_count());
        holder.loveCount.setText(sceneList.get(position).getLove_count());
        if (TypeConversionUtils.StringConvertInt(sceneList.get(position).getIs_love()) == 1) {
            holder.loveImg.setImageResource(R.mipmap.index_has_love);
        } else {
            holder.loveImg.setImageResource(R.mipmap.index_love);
        }
        SpannableString spannableStringBuilder = SceneTitleSetUtils.setDes(sceneList.get(position).getDes(), activity);
        holder.qjDesTv.setText(spannableStringBuilder);
        holder.qjDesTv.setMovementMethod(LinkMovementMethod.getInstance());
        holder.qjDesTv.setMaxLines(3);
        holder.qjDesTv.post(new Runnable() {
            @Override
            public void run() {
                if (holder.qjDesTv.getLineCount() > 3) {
                    Layout layout = holder.qjDesTv.getLayout();
                    String str = sceneList.get(position).getDes().substring(layout.getLineStart(0), layout.getLineEnd(2) - 1) + "…";
                    holder.qjDesTv.setText(SceneTitleSetUtils.setDes(str, activity));
                }
            }
        });
        holder.commentList.setAdapter(new IndexCommentAdapter(sceneList.get(position).getComments()));
        if (TypeConversionUtils.StringConvertInt(sceneList.get(position).getComment_count()) > 0) {
            holder.moreComment.setText("查看所有" + sceneList.get(position).getComment_count() + "条评论");
            holder.moreComment.setVisibility(View.GONE);
        } else {
            holder.moreComment.setVisibility(View.GONE);
        }
    }

    private void stopAnimate(ViewHolder holder) {
        //停止商品动画
        for (int i = 0; i < holder.labelContainer.getChildCount(); i++) {
            LabelView view = (LabelView) holder.labelContainer.getChildAt(i);
            view.stopAnim();
        }
        //移除所有标签
        holder.labelContainer.removeAllViews();
    }

    //取消关注弹窗
    private void showFocusFansConfirmView(final SceneListBean2.RowsEntity item, final ViewHolder holder) {
        View view = Util.inflateView(activity, R.layout.popup_focus_fans, null);
        RoundedImageView riv = (RoundedImageView) view.findViewById(R.id.riv);
        TextView tv_take_photo = (TextView) view.findViewById(R.id.tv_take_photo);
        TextView tv_album = (TextView) view.findViewById(R.id.tv_album);
        TextView tv_cancel = (TextView) view.findViewById(R.id.tv_cancel);
        GlideUtils.displayImage(item.getUser_info().getAvatar_url(), riv);
        tv_take_photo.setText(String.format("取消关注" + " %s ?", item.getUser_info().getNickname()));
        tv_album.setText("取消关注");
        tv_album.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupWindowUtil.dismiss();
                if (!dialog.isShowing()) {
                    dialog.show();
                }
                cancelFollow(item, holder);
            }
        });
        tv_album.setTag(item);
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupWindowUtil.dismiss();
            }
        });
        PopupWindowUtil.show(activity, view);
    }

    //取消收藏情景
    private void cancelShoucang(final int position) {
        HashMap<String, String> params = ClientDiscoverAPI.getcancelShoucangRequestParams(sceneList.get(position).get_id(), "12");
        HttpRequest.post(params,URL.FAVORITE_AJAX_CANCEL_FAVORITE, new GlobalDataCallBack(){
            @Override
            public void onSuccess(String json) {
                HttpResponse<QJFavoriteBean> qjFavoriteBeanHttpResponse = JsonUtil.json2Bean(json, new TypeToken<HttpResponse<QJFavoriteBean>>() {});
                dialog.dismiss();
                if (qjFavoriteBeanHttpResponse.isSuccess()) {
                    ToastUtils.showSuccess(qjFavoriteBeanHttpResponse.getMessage());
                    sceneList.get(position).setIs_favorite(TypeConversionUtils.IntConvertString(0));
                } else {
                    ToastUtils.showError(qjFavoriteBeanHttpResponse.getMessage());
                }
            }

            @Override
            public void onFailure(String error) {
                dialog.dismiss();
                ToastUtils.showError(R.string.net_fail);
            }
        });
    }

    //收藏情景
    private void shoucang(final int position) {
        HashMap<String, String> params = ClientDiscoverAPI.getshoucangRequestParams(sceneList.get(position).get_id(), "12");
        HttpRequest.post(params, URL.FAVORITE_AJAX_FAVORITE, new GlobalDataCallBack(){
            @Override
            public void onSuccess(String json) {
                HttpResponse<QJFavoriteBean> qjFavoriteBeanHttpResponse = JsonUtil.json2Bean(json, new TypeToken<HttpResponse<QJFavoriteBean>>() {});
                dialog.dismiss();
                if (qjFavoriteBeanHttpResponse.isSuccess()) {
                    ToastUtils.showSuccess(qjFavoriteBeanHttpResponse.getMessage());
                    sceneList.get(position).setIs_favorite(TypeConversionUtils.IntConvertString(1));
                } else {
                    ToastUtils.showError(qjFavoriteBeanHttpResponse.getMessage());
                }
            }

            @Override
            public void onFailure(String error) {
                dialog.dismiss();
                ToastUtils.showError(R.string.net_fail);
            }
        });
    }

    //删除情景
    private void deleteScene(String i) {
        HashMap<String, String> requestParams = ClientDiscoverAPI.getdeleteSceneRequestParams(i);
        HttpRequest.post(requestParams, URL.DELETE_SCENE, new GlobalDataCallBack(){
            @Override
            public void onSuccess(String json) {
                HttpResponse netBean = JsonUtil.fromJson(json, HttpResponse.class);
                dialog.dismiss();
                if (netBean.isSuccess()) {
                    ToastUtils.showSuccess(netBean.getMessage());
                    notifyDataSetChanged();
                } else {
                    ToastUtils.showError(netBean.getMessage());
                }
            }

            @Override
            public void onFailure(String error) {
                dialog.dismiss();
                ToastUtils.showError(R.string.net_fail);
            }
        });
    }

    //取消点赞
    private void cancelLoveQJ(final int position, String id, final ViewHolder holder) {
        HashMap<String, String> requestParams = ClientDiscoverAPI.getcancelLoveQJRequestParams(id);
        HttpRequest.post(requestParams, URL.CANCEL_LOVE_SCENE, new GlobalDataCallBack(){
            @Override
            public void onSuccess(String json) {
                holder.loveImg.setEnabled(true);
                dialog.dismiss();
                HttpResponse<SceneLoveBean> sceneLoveBean = JsonUtil.json2Bean(json, new TypeToken<HttpResponse<SceneLoveBean>>() {});
                if (sceneLoveBean.isSuccess()) {
                    holder.loveImg.setImageResource(R.mipmap.index_love);
                    holder.loveCount.setText(sceneLoveBean.getData().getLove_count());
                    sceneList.get(position).setIs_love(TypeConversionUtils.IntConvertString(0));
                    sceneList.get(position).setLove_count(sceneLoveBean.getData().getLove_count() + "");
                } else {
                    ToastUtils.showError(sceneLoveBean.getMessage());
                }
            }

            @Override
            public void onFailure(String error) {
                holder.loveImg.setEnabled(true);
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
                holder.loveImg.setEnabled(true);
                dialog.dismiss();
                HttpResponse<SceneLoveBean> sceneLoveBean = JsonUtil.json2Bean(json, new TypeToken<HttpResponse<SceneLoveBean>>() {});
                if (sceneLoveBean.isSuccess()) {
                    holder.loveImg.setImageResource(R.mipmap.index_has_love);
                    holder.loveCount.setText(sceneLoveBean.getData().getLove_count() + "");
                    sceneList.get(position).setIs_love(TypeConversionUtils.IntConvertString(1));
                    sceneList.get(position).setLove_count(sceneLoveBean.getData().getLove_count() + "");
                } else {
                    ToastUtils.showError(sceneLoveBean.getMessage());
                }
            }

            @Override
            public void onFailure(String error) {
                holder.loveImg.setEnabled(true);
                dialog.dismiss();
                ToastUtils.showError(R.string.net_fail);
            }
        });
    }

    //关注用户
    private void fllow(final int position, String otherUserId, final ViewHolder holder) {
        HashMap<String, String> params = ClientDiscoverAPI.getfocusOperateRequestParams(otherUserId);
        HttpRequest.post(params, URL.FOCUS_OPRATE_URL, new GlobalDataCallBack(){
            @Override
            public void onSuccess(String json) {
                dialog.dismiss();
                HttpResponse netBean = JsonUtil.fromJson(json, HttpResponse.class);
                if (netBean.isSuccess()) {
                    holder.attentionBtn.setBackgroundResource(R.mipmap.index_has_attention);
                    holder.attentionBtn.setText("");
                    holder.attentionBtn.setPadding(0, 0, 0, 0);
                    holder.attentionBtn.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    for (SceneListBean2.RowsEntity rowsBean : sceneList) {
                        if (rowsBean.getUser_id().equals(sceneList.get(position).getUser_id())) {
                            rowsBean.getUser_info().setIs_follow(TypeConversionUtils.IntConvertString(1));
                        }
                    }
                } else {
                    ToastUtils.showError(netBean.getMessage());
                }
            }

            @Override
            public void onFailure(String error) {
                dialog.dismiss();
                ToastUtils.showError(R.string.net_fail);
            }
        });
    }

    //取消关注
    private void cancelFollow(final SceneListBean2.RowsEntity item, final ViewHolder holder) {
        HashMap<String, String> params = ClientDiscoverAPI.getcancelFocusOperateRequestParams(item.getUser_info().getUser_id());
        HttpRequest.post(params, URL.CANCEL_FOCUS_URL, new GlobalDataCallBack(){
            @Override
            public void onSuccess(String json) {
                dialog.dismiss();
                HttpResponse netBean = JsonUtil.fromJson(json, HttpResponse.class);
                if (netBean.isSuccess()) {
                    holder.attentionBtn.setBackgroundResource(R.mipmap.index_attention);
                    holder.attentionBtn.setText("");
                    holder.attentionBtn.setPadding(0, 0, 0, 0);
                    holder.attentionBtn.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    for (SceneListBean2.RowsEntity rowsBean : sceneList) {
                        if (rowsBean.getUser_id().equals(item.getUser_info().getUser_id())) {
                            rowsBean.getUser_info().setIs_follow(TypeConversionUtils.IntConvertString(0));
                        }
                    }
                    return;
                }
                ToastUtils.showError(netBean.getMessage());
            }

            @Override
            public void onFailure(String error) {
                dialog.dismiss();
                ToastUtils.showError(R.string.net_fail);
            }
        });
    }

    static class ViewHolder {
        @Bind(R.id.map_linear)
        LinearLayout mapLinear;
        @Bind(R.id.head_img)
        RoundedImageView headImg;
        @Bind(R.id.v_img)
        ImageView vImg;
        @Bind(R.id.attention_btn)
        Button attentionBtn;
        @Bind(R.id.user_name_tv)
        TextView userNameTv;
        @Bind(R.id.publish_time)
        TextView publishTime;
        @Bind(R.id.location_img)
        ImageView locationImg;
        @Bind(R.id.location_tv)
        TextView locationTv;
        @Bind(R.id.container)
        RelativeLayout container;
        @Bind(R.id.qj_img)
        ImageView qjImg;
        @Bind(R.id.qj_title_tv)
        TextView qjTitleTv;
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
        @Bind(R.id.love_count)
        TextView loveCount;
        @Bind(R.id.love_img)
        ClickImageView loveImg;
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

    //设置部分文字可以点击
    public static class TextClick extends ClickableSpan {
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
        private List<SceneListBean2.RowsEntity.CommentsEntity> commentList;

        public IndexCommentAdapter(List<SceneListBean2.RowsEntity.CommentsEntity> commentList) {
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
            GlideUtils.displayImage(commentList.get(position).getUser_avatar_url(), holder.headImg);
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

    private boolean compareble(List<SceneListBean2.RowsEntity.ProductEntity> list1, List<SceneListBean2.RowsEntity.ProductEntity> list2){
        return list1.equals(list2);
    }
}
