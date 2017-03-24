package com.taihuoniao.fineix.zone.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
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
import com.taihuoniao.fineix.qingjingOrSceneDetails.QJPictureActivity;
import com.taihuoniao.fineix.qingjingOrSceneDetails.ReportActivity;
import com.taihuoniao.fineix.qingjingOrSceneDetails.ShareActivity;
import com.taihuoniao.fineix.scene.CreateQJActivity;
import com.taihuoniao.fineix.user.FocusActivity;
import com.taihuoniao.fineix.user.OptRegisterLoginActivity;
import com.taihuoniao.fineix.user.UserCenterActivity;
import com.taihuoniao.fineix.utils.GlideUtils;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.SceneTitleSetUtils;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.utils.Util;
import com.taihuoniao.fineix.view.ClickImageView;
import com.taihuoniao.fineix.view.LabelView;
import com.taihuoniao.fineix.view.ListViewForScrollView;
import com.taihuoniao.fineix.view.roundImageView.RoundedImageView;

import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by lilin on 2017/2/21.
 */

public class ZoneRelateSceneAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_UPLOAD = 0;
    private static final int TYPE_SCENE = 1;
    //popupwindow下的控件
    private View popup_view;
    private PopupWindow popupWindow;
    private TextView bianjiTv;
    private TextView shoucangTv;
    private TextView jubaoTv;
    private TextView cancelTv;
    public interface ISortListener {
        void onClick(View view);
    }

    private ISortListener sortListener;

    private Activity activity;
    private List<SceneList.DataBean.RowsBean> list;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }

    public ZoneRelateSceneAdapter(Activity activity, List list) {
        this.activity = activity;
        this.list = list;
        initPopupWindow();
    }

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.mOnItemClickListener = itemClickListener;
    }

    public void setSortListener(ISortListener listener) {
        this.sortListener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_UPLOAD) {
            View view = LayoutInflater.from(activity).inflate(R.layout.item_relate_scene_upload, parent, false);
            return new UploadViewHolder(view);
        } else {
            View view = LayoutInflater.from(activity).inflate(R.layout.item_relate_zone, parent, false);
            return new ViewHolder(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_UPLOAD;
        }
        return TYPE_SCENE;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof UploadViewHolder) {
            ((UploadViewHolder)holder).tuiJian.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (sortListener!=null){
                        sortListener.onClick(v);
                        ((UploadViewHolder)holder).tuiJian.setTextColor(activity.getResources().getColor(R.color.yellow_bd8913));
                        ((UploadViewHolder)holder).zuiXin.setTextColor(activity.getResources().getColor(R.color.color_666));
                    }
                }
            });

            ((UploadViewHolder)holder).zuiXin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sortListener.onClick(v);
                    ((UploadViewHolder)holder).tuiJian.setTextColor(activity.getResources().getColor(R.color.color_666));
                    ((UploadViewHolder)holder).zuiXin.setTextColor(activity.getResources().getColor(R.color.yellow_bd8913));
                }
            });

            ((UploadViewHolder)holder).btnUpload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sortListener.onClick(v);
//
                }
            });
        } else {
            if (position<1) return;
            final int itemPosition = position-1;
            if (mOnItemClickListener != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mOnItemClickListener.onItemClick(holder.itemView, holder.getAdapterPosition());
                    }
                });

                holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        mOnItemClickListener.onItemLongClick(holder.itemView, holder.getAdapterPosition());
                        return false;
                    }
                });
            }
            ViewGroup.LayoutParams params = ((ViewHolder)holder).container.getLayoutParams();
            params.width = Util.getScreenWidth();
            params.height = params.width;
            ((ViewHolder)holder).container.setLayoutParams(params);

            GlideUtils.displayImageFadein(list.get(itemPosition).getCover_url(), ((ViewHolder)holder).qjImg);
            GlideUtils.displayImage(list.get(itemPosition).getUser_info().getAvatar_url(), ((ViewHolder)holder).headImg);
            ((ViewHolder)holder).labelContainer.setVisibility(View.VISIBLE);

            //设置情景标题
            SceneTitleSetUtils.setTitle(((ViewHolder)holder).qjTitleTv, ((ViewHolder)holder).qjTitleTv2, list.get(itemPosition).getTitle());
            //添加商品标签
            List<SceneList.DataBean.RowsBean.ProductBean> productBeanList = list.get(itemPosition).getProduct();
            if (productBeanList != null && compareble(productBeanList, (List<SceneList.DataBean.RowsBean.ProductBean>) ((ViewHolder)holder).labelContainer.getTag(R.id.label_container))) {
                ((ViewHolder)holder).labelContainer.setTag(R.id.label_container, productBeanList);
            } else {
                stopAnimate(((ViewHolder)holder)); //停止商品动画 移除所有标签
                method1(itemPosition, ((ViewHolder)holder));
                addGoodsTag(itemPosition, ((ViewHolder)holder)); //添加商品标签
                ((ViewHolder)holder).labelContainer.setTag(R.id.label_container, productBeanList);
            }
            //跳转到分享页面
            ((ViewHolder) holder).shareImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent4 = new Intent(activity, ShareActivity.class);
                    intent4.putExtra("id", list.get(itemPosition).get_id());
                    activity.startActivity(intent4);
                }
            });

            //更多
             ((ViewHolder) holder).moreImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showPopup(itemPosition);
                }
            });

            //跳转个人中心
            ((ViewHolder)holder).headImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), UserCenterActivity.class);
                    long l = Long.valueOf(list.get(itemPosition).getUser_info().getUser_id());
                    intent.putExtra(FocusActivity.USER_ID_EXTRA, l);
                    v.getContext().startActivity(intent);
                }
            });
            //跳转情景地图
            ((ViewHolder)holder).mapLinear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                跳转到地图界面，查看附近的情境
                    String address = list.get(itemPosition).getAddress();
                    LatLng ll = new LatLng(list.get(itemPosition).getLocation().getCoordinates().get(1), list.get(itemPosition).getLocation().getCoordinates().get(0));
                    Intent intent2 = new Intent(activity, MapNearByCJActivity.class);
                    intent2.putExtra("address", address);
                    intent2.putExtra(MapNearByCJActivity.class.getSimpleName(), ll);
                    activity.startActivity(intent2);
                }
            });

            //点赞或取消点赞
            ((ViewHolder)holder).loveImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (LoginInfo.isUserLogin()) {
                        //已经登录
                        if (list.get(itemPosition).getIs_love() == 1) {
                            ((ViewHolder)holder).loveContainer.setEnabled(false);
                            cancelLoveQJ(itemPosition, list.get(itemPosition).get_id(), ((ViewHolder)holder));
                        } else {
                            ((ViewHolder)holder).loveContainer.setEnabled(false);
                            loveQJ(itemPosition, list.get(itemPosition).get_id(), ((ViewHolder)holder));
                        }
                        return;
                    }
                    MainApplication.which_activity = DataConstants.IndexFragment;
                    activity.startActivity(new Intent(activity, OptRegisterLoginActivity.class));
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size()+1;
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
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });
    }

    private void showPopup(final int position) {
        if (LoginInfo.getUserId() == Long.parseLong(list.get(position).getUser_id())) {
            //自己不能举报自己。改为删除
            jubaoTv.setText("删除");
            bianjiTv.setVisibility(View.VISIBLE);
        } else {
            bianjiTv.setVisibility(View.GONE);
            jubaoTv.setText("举报");
        }
        if (list.get(position).getIs_favorite() == 1) {
            shoucangTv.setText("取消收藏");
        } else {
            shoucangTv.setText("收藏");
        }
        bianjiTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, CreateQJActivity.class);
                intent.putExtra(IndexFragment.class.getSimpleName(), list.get(position));
                activity.startActivity(intent);
                popupWindow.dismiss();
            }
        });
        shoucangTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                if (!LoginInfo.isUserLogin()) {
                    MainApplication.which_activity = DataConstants.IndexFragment;
                    activity.startActivity(new Intent(activity, OptRegisterLoginActivity.class));
                    return;
                }
                if (list.get(position).getIs_favorite() == 1) {
                    cancelShoucang(position);
                } else {
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
                if (LoginInfo.getUserId() == list.get(position).getIs_favorite()) {
                    deleteScene(list.get(position).get_id());
                    //过滤自己
                    return;
                }
                Intent intent1 = new Intent(activity, ReportActivity.class);
                intent1.putExtra("target_id", list.get(position).get_id());
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

    //取消收藏情景
    private void cancelShoucang(final int position) {
        HashMap<String, String> params = ClientDiscoverAPI.getcancelShoucangRequestParams(list.get(position).get_id(), "12");
        HttpRequest.post(params, URL.FAVORITE_AJAX_CANCEL_FAVORITE, new GlobalDataCallBack() {
            @Override
            public void onSuccess(String json) {
                HttpResponse<QJFavoriteBean> qjFavoriteBeanHttpResponse = JsonUtil.json2Bean(json, new TypeToken<HttpResponse<QJFavoriteBean>>() {});
                if (qjFavoriteBeanHttpResponse.isSuccess()) {
                    ToastUtils.showSuccess(qjFavoriteBeanHttpResponse.getMessage());
                    list.get(position).setIs_favorite(0);
                } else {
                    ToastUtils.showError(qjFavoriteBeanHttpResponse.getMessage());
                }
            }

            @Override
            public void onFailure(String error) {
                ToastUtils.showError(R.string.net_fail);
            }
        });
    }

    //删除情景
    private void deleteScene(String i) {
        HashMap<String, String> requestParams = ClientDiscoverAPI.getdeleteSceneRequestParams(i);
        HttpRequest.post(requestParams, URL.DELETE_SCENE, new GlobalDataCallBack() {
            @Override
            public void onSuccess(String json) {
                HttpResponse netBean = JsonUtil.fromJson(json, HttpResponse.class);
                if (netBean.isSuccess()) {
                    ToastUtils.showSuccess(netBean.getMessage());
                    notifyDataSetChanged();
                } else {
                    ToastUtils.showError(netBean.getMessage());
                }
            }

            @Override
            public void onFailure(String error) {
                ToastUtils.showError(R.string.net_fail);
            }
        });
    }


    //收藏情景
    private void shoucang(final int position) {
        HashMap<String, String> params = ClientDiscoverAPI.getshoucangRequestParams(list.get(position).get_id(), "12");
        HttpRequest.post(params, URL.FAVORITE_AJAX_FAVORITE, new GlobalDataCallBack() {
            @Override
            public void onSuccess(String json) {
                HttpResponse<QJFavoriteBean> qjFavoriteBeanHttpResponse = JsonUtil.json2Bean(json, new TypeToken<HttpResponse<QJFavoriteBean>>() {});
                if (qjFavoriteBeanHttpResponse.isSuccess()) {
                    ToastUtils.showSuccess(qjFavoriteBeanHttpResponse.getMessage());
                    list.get(position).setIs_favorite(1);
                } else {
                    ToastUtils.showError(qjFavoriteBeanHttpResponse.getMessage());
                }
            }

            @Override
            public void onFailure(String error) {
                ToastUtils.showError(R.string.net_fail);
            }
        });
    }



    //取消点赞
    private void cancelLoveQJ(final int position, String id, final ZoneRelateSceneAdapter.ViewHolder holder) {
        HashMap<String, String> requestParams = ClientDiscoverAPI.getcancelLoveQJRequestParams(id);
        HttpRequest.post(requestParams, URL.CANCEL_LOVE_SCENE, new GlobalDataCallBack() {
            @Override
            public void onStart() {
                holder.loveContainer.setEnabled(false);
            }

            @Override
            public void onSuccess(String json) {
                holder.loveContainer.setEnabled(true);
                HttpResponse<SceneLoveBean> sceneLoveBean = JsonUtil.json2Bean(json, new TypeToken<HttpResponse<SceneLoveBean>>() {});
                if (sceneLoveBean.isSuccess()) {
                    holder.loveImg.setImageResource(R.mipmap.index_love);
                    holder.loveCount.setText(sceneLoveBean.getData().getLove_count() + "");
                    list.get(position).setIs_love(0);
                    list.get(position).setLove_count(sceneLoveBean.getData().getLove_count() + "");
                } else {
                    ToastUtils.showError(sceneLoveBean.getMessage());
                }
            }

            @Override
            public void onFailure(String error) {
                holder.loveContainer.setEnabled(true);
                ToastUtils.showError(R.string.net_fail);
            }
        });
    }

    //点赞情景
    private void loveQJ(final int position, String id, final ZoneRelateSceneAdapter.ViewHolder holder) {
        HashMap<String, String> requestParams = ClientDiscoverAPI.getloveQJRequestParams(id);
        HttpRequest.post(requestParams, URL.LOVE_SCENE, new GlobalDataCallBack() {
            @Override
            public void onStart() {
                holder.loveContainer.setEnabled(false);
            }

            @Override
            public void onSuccess(String json) {
                holder.loveContainer.setEnabled(true);
                HttpResponse<SceneLoveBean> sceneLoveBean = JsonUtil.json2Bean(json, new TypeToken<HttpResponse<SceneLoveBean>>() {});
                if (sceneLoveBean.isSuccess()) {
                    holder.loveImg.setImageResource(R.mipmap.index_has_love);
                    holder.loveCount.setText(sceneLoveBean.getData().getLove_count() + "");
                    list.get(position).setIs_love(1);
                    list.get(position).setLove_count(sceneLoveBean.getData().getLove_count() + "");
                } else {
                    ToastUtils.showError(sceneLoveBean.getMessage());
                }
            }

            @Override
            public void onFailure(String error) {
                holder.loveContainer.setEnabled(true);
                ToastUtils.showError(R.string.net_fail);
            }
        });
    }


    /**
     * 添加商品标签
     *
     * @param position
     * @param holder
     */
    private void addGoodsTag(int position, ZoneRelateSceneAdapter.ViewHolder holder) {
        for (final SceneList.DataBean.RowsBean.ProductBean productBean : list.get(position).getProduct()) {
            final RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            final LabelView labelView = new LabelView(activity);
            labelView.nameTv.setText(productBean.getTitle());
            if (TextUtils.equals("0",productBean.price)){
                labelView.price.setVisibility(View.GONE);
            }else {
                labelView.price.setText("¥"+productBean.price);
            }
            labelView.setLayoutParams(layoutParams);
            if (productBean.getLoc() == 2) {
                labelView.llTag.setBackgroundResource(R.drawable.label_left);
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
//            Log.e("<<<", "开启动画" + holder.qjTitleTv.getText() + ",现在位置=" + position);
            labelView.wave();
            labelView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(activity, BuyGoodsDetailsActivity.class);
                    intent.putExtra("id", productBean.getId());
                    v.getContext().startActivity(intent);
                }
            });
        }
    }


    private void stopAnimate(ZoneRelateSceneAdapter.ViewHolder holder) {
        //停止商品动画
        for (int i = 0; i < holder.labelContainer.getChildCount(); i++) {
            LabelView view = (LabelView) holder.labelContainer.getChildAt(i);
            view.stopAnim();
        }
        //移除所有标签
        holder.labelContainer.removeAllViews();
    }


    private void method1(final int position, final ZoneRelateSceneAdapter.ViewHolder holder) {
        if (list.get(position).getUser_info().getIs_expert() == 1) {
            holder.vImg.setVisibility(View.VISIBLE);
        } else {
            holder.vImg.setVisibility(View.GONE);
        }
        holder.userNameTv.setText(list.get(position).getUser_info().getNickname());
        holder.publishTime.setText(list.get(position).getCreated_at());
        if (TextUtils.isEmpty(list.get(position).getAddress())) {
            holder.locationImg.setVisibility(View.GONE);
            holder.locationTv.setVisibility(View.GONE);
        } else {
            holder.locationTv.setText(list.get(position).getCity() + " " + list.get(position).getAddress());
            holder.locationImg.setVisibility(View.VISIBLE);
            holder.locationTv.setVisibility(View.VISIBLE);
        }
        holder.qjImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, QJPictureActivity.class);
                intent.putExtra("img", list.get(position).getCover_url());
                intent.putExtra("fine", list.get(position).getFine() == 1);
                intent.putExtra("stick", list.get(position).getStick() == 1);
                intent.putExtra("check", list.get(position).getIs_check() == 0);
                intent.putExtra("id", list.get(position).get_id());
                activity.startActivity(intent);
            }
        });
        holder.viewCount.setText(list.get(position).getView_count());
        holder.loveCount.setText(list.get(position).getLove_count());
        if (list.get(position).getIs_love() == 1) {
            holder.loveImg.setImageResource(R.mipmap.index_has_love);
        } else {
            holder.loveImg.setImageResource(R.mipmap.index_love);
        }
        SpannableString spannableStringBuilder = SceneTitleSetUtils.setDes(list.get(position).getDes(), activity);
        holder.qjDesTv.setText(spannableStringBuilder);
        holder.qjDesTv.setMovementMethod(LinkMovementMethod.getInstance());
        holder.qjDesTv.setMaxLines(3);
        holder.qjDesTv.post(new Runnable() {
            @Override
            public void run() {
                if (holder.qjDesTv.getLineCount() > 3) {
                    Layout layout = holder.qjDesTv.getLayout();
                    String str = list.get(position).getDes().substring(layout.getLineStart(0), layout.getLineEnd(2) - 1) + "…";
                    holder.qjDesTv.setText(SceneTitleSetUtils.setDes(str, activity));
                }
            }
        });
        holder.commentList.setAdapter(new ZoneRelateSceneAdapter.IndexCommentAdapter(list.get(position).getComments()));
        if (list.get(position).getComment_count() > 0) {
            holder.moreComment.setText("查看所有" + list.get(position).getComment_count() + "条评论");
            holder.moreComment.setVisibility(View.GONE);
        } else {
            holder.moreComment.setVisibility(View.GONE);
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
            ZoneRelateSceneAdapter.IndexCommentAdapter.ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(parent.getContext(), R.layout.item_index_comment, null);
                holder = new ZoneRelateSceneAdapter.IndexCommentAdapter.ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ZoneRelateSceneAdapter.IndexCommentAdapter.ViewHolder) convertView.getTag();
            }
//            ImageLoader.getInstance().displayImage(commentList.get(position).getUser_avatar_url(), holder.headImg);
//            Glide.with(holder.headImg.getContext()).load(commentList.get(position).getUser_avatar_url()).into(holder.headImg);
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

    static class UploadViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.rl_upload)
        RelativeLayout rlUpload;
        @Bind(R.id.tui_jian)
        TextView tuiJian;
        @Bind(R.id.zui_xin)
        TextView zuiXin;
        @Bind(R.id.btn_upload)
        Button btnUpload;

        public UploadViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
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
        @Bind(R.id.head_img)
        RoundedImageView headImg;
        @Bind(R.id.v_img)
        ImageView vImg;
        @Bind(R.id.relative)
        RelativeLayout relative;
        @Bind(R.id.user_name_tv)
        TextView userNameTv;
        @Bind(R.id.img)
        ImageView img;
        @Bind(R.id.publish_time)
        TextView publishTime;
        @Bind(R.id.location_img)
        ImageView locationImg;
        @Bind(R.id.location_tv)
        TextView locationTv;
        @Bind(R.id.map_linear)
        LinearLayout mapLinear;
        @Bind(R.id.qj_des_tv)
        TextView qjDesTv;
        @Bind(R.id.img1)
        ImageView img1;
        @Bind(R.id.view_count)
        TextView viewCount;
        @Bind(R.id.more_img)
        ImageView moreImg;
        @Bind(R.id.share_img)
        ImageView shareImg;
        @Bind(R.id.comment_img)
        ImageView commentImg;
        @Bind(R.id.love_img)
        ClickImageView loveImg;
        @Bind(R.id.love_count)
        TextView loveCount;
        @Bind(R.id.love_container)
        RelativeLayout loveContainer;
        @Bind(R.id.comment_list)
        ListViewForScrollView commentList;
        @Bind(R.id.more_comment)
        TextView moreComment;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    private boolean compareble(List<SceneList.DataBean.RowsBean.ProductBean> list1, List<SceneList.DataBean.RowsBean.ProductBean> list2) {
        return list1.equals(list2);
    }
}
