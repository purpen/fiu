package com.taihuoniao.fineix.qingjingOrSceneDetails;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
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
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.base.NetBean;
import com.taihuoniao.fineix.beans.LoginInfo;
import com.taihuoniao.fineix.beans.QJDetailBean;
import com.taihuoniao.fineix.beans.SceneList;
import com.taihuoniao.fineix.beans.SceneLoveBean;
import com.taihuoniao.fineix.main.MainApplication;
import com.taihuoniao.fineix.map.MapNearByCJActivity;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.DataConstants;
import com.taihuoniao.fineix.product.BuyGoodsDetailsActivity;
import com.taihuoniao.fineix.product.GoodsDetailActivity;
import com.taihuoniao.fineix.user.FocusActivity;
import com.taihuoniao.fineix.user.OptRegisterLoginActivity;
import com.taihuoniao.fineix.user.UserCenterActivity;
import com.taihuoniao.fineix.utils.DensityUtils;
import com.taihuoniao.fineix.utils.PopupWindowUtil;
import com.taihuoniao.fineix.utils.SceneTitleSetUtils;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.utils.Util;
import com.taihuoniao.fineix.utils.WindowUtils;
import com.taihuoniao.fineix.view.GlobalTitleLayout;
import com.taihuoniao.fineix.view.LabelView;
import com.taihuoniao.fineix.view.ListViewForScrollView;
import com.taihuoniao.fineix.view.WaittingDialog;
import com.taihuoniao.fineix.view.roundImageView.RoundedImageView;

import java.lang.reflect.Type;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by taihuoniao on 2016/8/25.
 */
public class QJDetailActivity extends BaseActivity {
    //上个界面传递过来的情景id
    private String id;
    @Bind(R.id.title_layout)
    GlobalTitleLayout titleLayout;
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
    @Bind(R.id.img)
    ImageView img;
    @Bind(R.id.publish_time)
    TextView publishTime;
    @Bind(R.id.location_tv)
    TextView locationTv;
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
    @Bind(R.id.view_count)
    TextView viewCount;
    @Bind(R.id.more_img)
    ImageView moreImg;
    @Bind(R.id.share_img)
    ImageView shareImg;
    @Bind(R.id.comment_img)
    ImageView commentImg;
    @Bind(R.id.love_img)
    ImageView loveImg;
    @Bind(R.id.love_count)
    TextView loveCount;
    @Bind(R.id.love_container)
    RelativeLayout loveContainer;
    @Bind(R.id.qj_des_tv)
    TextView qjDesTv;
    @Bind(R.id.comment_list)
    ListViewForScrollView commentList;
    @Bind(R.id.more_comment)
    TextView moreComment;
    private WaittingDialog dialog;

    private QJDetailBean qjDetailBean;//网络情景返回情境详情

    public QJDetailActivity() {
        super(R.layout.activity_qjdetail);
    }

    @Override
    protected void getIntentData() {
        id = getIntent().getStringExtra("id");
        Log.e("<<<情景详情", "id=" + id);
        if (id == null) {
            ToastUtils.showError("访问的情境不存在或已删除");
            finish();
        }
    }

    @Override
    protected void initView() {
        titleLayout.setTitle(R.string.qj_detail);
        titleLayout.setContinueTvVisible(false);
        qjDesTv.setMaxLines(Integer.MAX_VALUE);
        dialog = new WaittingDialog(this);
        if (!dialog.isShowing()) {
            dialog.show();
        }
        WindowUtils.chenjin(this);
        IntentFilter intentFilter = new IntentFilter(DataConstants.BroadQJDetail);
        registerReceiver(qjDetailReceiver, intentFilter);
        initPopupWindow();
    }

    @Override
    protected void requestNet() {
        ClientDiscoverAPI.sceneDetails(id, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                dialog.dismiss();
                Log.e("<<<情景详情", responseInfo.result);
                qjDetailBean = new QJDetailBean();
                try {
                    Gson gson = new Gson();
                    Type type = new TypeToken<QJDetailBean>() {
                    }.getType();
                    qjDetailBean = gson.fromJson(responseInfo.result, type);
                } catch (JsonSyntaxException e) {
                    Log.e("<<<情景详情", "解析异常=" + e.toString());
                }
                if (qjDetailBean.isSuccess()) {
                    setData();
                } else {
                    ToastUtils.showError(qjDetailBean.getMessage());
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                dialog.dismiss();
                Log.e("<<<", "网络错误=" + msg);
                ToastUtils.showError(R.string.net_fail);
            }
        });
    }

    //popupwindow下的控件
    private View popup_view;
    private PopupWindow popupWindow;
    private TextView shoucangTv;
    private TextView jubaoTv;
    private TextView cancelTv;

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

    private void showPopup() {
        if (qjDetailBean == null) {
            if (!dialog.isShowing()) {
                dialog.show();
            }
            requestNet();
            return;
        }
        if (LoginInfo.getUserId() == Long.parseLong(qjDetailBean.getData().getUser_id())) {
            //自己不能举报自己。改为删除
            jubaoTv.setText("删除");
        } else {
            jubaoTv.setText("举报");
        }
        if (qjDetailBean.getData().getIs_favorite() == 1) {
            shoucangTv.setText("取消收藏");
        } else {
            shoucangTv.setText("收藏");
        }
        shoucangTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                if (!LoginInfo.isUserLogin()) {
                    MainApplication.which_activity = DataConstants.QJDetailActivity;
                    activity.startActivity(new Intent(activity, OptRegisterLoginActivity.class));
                    return;
                }
                if (qjDetailBean.getData().getIs_favorite() == 1) {
                    if (!dialog.isShowing()) {
                        dialog.show();
                    }
                    cancelShoucang();
                } else {
                    if (!dialog.isShowing()) {
                        dialog.show();
                    }
                    shoucang();
                }
            }
        });
        jubaoTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                if (!LoginInfo.isUserLogin()) {
                    MainApplication.which_activity = DataConstants.QJDetailActivity;
                    activity.startActivity(new Intent(activity, OptRegisterLoginActivity.class));
                    return;
                }
                if (LoginInfo.getUserId() == Long.parseLong(qjDetailBean.getData().getUser_id())) {
                    if (!dialog.isShowing()) {
                        dialog.show();
                    }
                    deleteScene();
                    //过滤自己
                    return;
                }
                Intent intent1 = new Intent(activity, ReportActivity.class);
                intent1.putExtra("target_id", qjDetailBean.getData().get_id());
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
    protected void onPause() {
        super.onPause();
        for (int i = 0; i < labelContainer.getChildCount(); i++) {
            LabelView labelView = (LabelView) labelContainer.getChildAt(i);
            labelView.stopAnim();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        for (int i = 0; i < labelContainer.getChildCount(); i++) {
            LabelView labelView = (LabelView) labelContainer.getChildAt(i);
            labelView.wave();
        }
    }

    private void setData() {
        ViewGroup.LayoutParams layoutParams2 = container.getLayoutParams();
        layoutParams2.width = MainApplication.getContext().getScreenWidth();
        layoutParams2.height = layoutParams2.width;
        container.setLayoutParams(layoutParams2);
        ImageLoader.getInstance().displayImage(qjDetailBean.getData().getUser_info().getAvatar_url(), headImg);
        if (qjDetailBean.getData().getUser_info().getIs_expert() == 1) {
            vImg.setVisibility(View.VISIBLE);
        } else {
            vImg.setVisibility(View.GONE);
        }
        userNameTv.setText(qjDetailBean.getData().getUser_info().getNickname());
        publishTime.setText(qjDetailBean.getData().getCreated_at());
        locationTv.setText(qjDetailBean.getData().getAddress());
        if (LoginInfo.getUserId() == Long.parseLong(qjDetailBean.getData().getUser_id())) {
            //自己的话隐藏关注按钮
           attentionBtn.setVisibility(View.GONE);
        } else {
            attentionBtn.setVisibility(View.VISIBLE);
            if (qjDetailBean.getData().getUser_info().getIs_follow() == 1) {
//                attentionBtn.setBackgroundResource(R.mipmap.index_has_attention);
                attentionBtn.setBackgroundResource(R.drawable.shape_corner_969696_nothing);
                attentionBtn.setText("已关注");
                attentionBtn.setPadding(DensityUtils.dp2px(activity, 6), 0, DensityUtils.dp2px(activity, 6), 0);
                attentionBtn.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.focus_pic, 0, 0, 0);
                attentionBtn.setTextColor(activity.getResources().getColor(R.color.white));
            } else {
                attentionBtn.setBackgroundResource(R.mipmap.index_attention);
                attentionBtn.setText("");
                attentionBtn.setPadding(0, 0, 0, 0);
                attentionBtn.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            }
        }
        ImageLoader.getInstance().displayImage(qjDetailBean.getData().getCover_url(), qjImg);
        viewCount.setText(qjDetailBean.getData().getView_count());
        loveCount.setText(qjDetailBean.getData().getLove_count());
        if (qjDetailBean.getData().getIs_love() == 1) {
            loveImg.setImageResource(R.mipmap.index_has_love);
        } else {
            loveImg.setImageResource(R.mipmap.index_love);
        }
        SpannableString spannableStringBuilder = SceneTitleSetUtils.setDes(qjDetailBean.getData().getDes(), activity);
        qjDesTv.setText(spannableStringBuilder);
        qjDesTv.setMovementMethod(LinkMovementMethod.getInstance());
        qjDesTv.setMaxLines(Integer.MAX_VALUE);
        commentList.setAdapter(new IndexCommentAdapter(qjDetailBean.getData().getComments()));
        if (qjDetailBean.getData().getComment_count() > 0) {
            moreComment.setText("查看所有" + qjDetailBean.getData().getComment_count() + "条评论");
            moreComment.setVisibility(View.VISIBLE);
        } else {
            moreComment.setVisibility(View.GONE);
        }
        //设置情景标题
        SceneTitleSetUtils.setTitle(qjTitleTv, qjTitleTv2, qjDetailBean.getData().getTitle());
        //添加商品标签
        for (final SceneList.DataBean.RowsBean.ProductBean productBean : qjDetailBean.getData().getProduct()) {
            final RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            final LabelView labelView = new LabelView(this);
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
            labelContainer.addView(labelView);
//            Log.e("<<<", "开启动画" + qjTitleTv.getText() + ",现在位置=" + position);
            labelView.wave();
            labelView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    switch (productBean.getType()) {
                        case 2:
                            Log.e("<<<", "可购买");
                            intent.setClass(activity, BuyGoodsDetailsActivity.class);
                            break;
                        default:
                            Log.e("<<<", "不可购买");
                            intent.setClass(activity, GoodsDetailActivity.class);
                            break;
                    }
                    intent.putExtra("id", productBean.getId());
                   startActivity(intent);
                }
            });
        }
        //跳转个人中心
        headImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QJDetailActivity.this, UserCenterActivity.class);
                long l = Long.valueOf(qjDetailBean.getData().getUser_info().getUser_id());
                intent.putExtra(FocusActivity.USER_ID_EXTRA, l);
                startActivity(intent);
            }
        });
        //跳转情景地图
        mapLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                跳转到地图界面，查看附近的情境
                String address = qjDetailBean.getData().getAddress();
                LatLng ll = new LatLng(qjDetailBean.getData().getLocation().getCoordinates().get(1), qjDetailBean.getData().getLocation().getCoordinates().get(0));
                Intent intent2 = new Intent(activity, MapNearByCJActivity.class);
                intent2.putExtra("address", address);
                intent2.putExtra(MapNearByCJActivity.class.getSimpleName(), ll);
                activity.startActivity(intent2);
            }
        });
        //关注或取消关注
        attentionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (LoginInfo.isUserLogin()) {
                    //已经登录
                    if (LoginInfo.getUserId() == Long.parseLong(qjDetailBean.getData().getUser_id())) {
                        //过滤自己
                        return;
                    }
                    if (qjDetailBean.getData().getUser_info().getIs_follow() == 0) {
                        if (!dialog.isShowing()) {
                            dialog.show();
                        }
                        fllow();
                    } else {
                        showFocusFansConfirmView();
                    }
                    return;
                }
                MainApplication.which_activity = DataConstants.QJDetailActivity;
                activity.startActivity(new Intent(activity, OptRegisterLoginActivity.class));
            }
        });

        //点赞或取消点赞
        loveContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (LoginInfo.isUserLogin()) {
                    //已经登录
                    if (qjDetailBean.getData().getIs_love() == 1) {
                        loveContainer.setEnabled(false);
                        cancelLoveQJ();
                    } else {
                        loveContainer.setEnabled(false);
                        loveQJ();
                    }
                    return;
                }
                MainApplication.which_activity = DataConstants.QJDetailActivity;
                activity.startActivity(new Intent(activity, OptRegisterLoginActivity.class));
            }
        });
        //跳转到评论界面
        commentImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent3 = new Intent(activity, CommentListActivity.class);
                intent3.putExtra("target_id", qjDetailBean.getData().get_id());
                intent3.putExtra("type", 12 + "");
                intent3.putExtra("target_user_id", qjDetailBean.getData().getUser_info().getUser_id());
                activity.startActivityForResult(intent3, 1);
            }
        });
        commentList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> pare, View vie, int positi, long d) {
                Intent intent3 = new Intent(activity, CommentListActivity.class);
                intent3.putExtra("target_id", qjDetailBean.getData().get_id());
                intent3.putExtra("type", 12 + "");
                intent3.putExtra("target_user_id", qjDetailBean.getData().getUser_info().getUser_id());
                activity.startActivityForResult(intent3, 1);
            }
        });
        moreComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent3 = new Intent(activity, CommentListActivity.class);
                intent3.putExtra("target_id", qjDetailBean.getData().get_id());
                intent3.putExtra("type", 12 + "");
                intent3.putExtra("target_user_id", qjDetailBean.getData().getUser_info().getUser_id());
                activity.startActivityForResult(intent3, 1);
            }
        });
        //跳转到分享页面
        shareImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent4 = new Intent(activity, ShareActivity.class);
                intent4.putExtra("id", qjDetailBean.getData().get_id());
                activity.startActivity(intent4);
            }
        });
        //更多
        moreImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup();
            }
        });
    }

    //取消收藏情景
    private void cancelShoucang() {
        ClientDiscoverAPI.cancelShoucang(qjDetailBean.getData().get_id(), "12", new RequestCallBack<String>() {
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
                    qjDetailBean.getData().setIs_favorite(0);
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
    private void shoucang() {
        ClientDiscoverAPI.shoucang(qjDetailBean.getData().get_id(), "12", new RequestCallBack<String>() {
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
                    qjDetailBean.getData().setIs_favorite(1);
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
    private void deleteScene() {
        ClientDiscoverAPI.deleteScene(qjDetailBean.getData().get_id(), new RequestCallBack<String>() {
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
                    finish();
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
    private void cancelLoveQJ() {
        ClientDiscoverAPI.cancelLoveQJ(id, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                loveContainer.setEnabled(true);
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
                    loveImg.setImageResource(R.mipmap.index_love);
                    loveCount.setText(sceneLoveBean.getData().getLove_count() + "");
                    qjDetailBean.getData().setIs_love(0);
                    qjDetailBean.getData().setLove_count(sceneLoveBean.getData().getLove_count() + "");
                } else {
                    ToastUtils.showError(sceneLoveBean.getMessage());
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                loveContainer.setEnabled(true);
                dialog.dismiss();
                ToastUtils.showError(R.string.net_fail);
            }
        });
    }

    //点赞情景
    private void loveQJ() {
        ClientDiscoverAPI.loveQJ(id, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                loveContainer.setEnabled(true);
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
                    loveImg.setImageResource(R.mipmap.index_has_love);
                    loveCount.setText(sceneLoveBean.getData().getLove_count() + "");
                    qjDetailBean.getData().setIs_love(1);
                    qjDetailBean.getData().setLove_count(sceneLoveBean.getData().getLove_count() + "");
                } else {
                    ToastUtils.showError(sceneLoveBean.getMessage());
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                loveContainer.setEnabled(true);
                dialog.dismiss();
                ToastUtils.showError(R.string.net_fail);
            }
        });
    }

    //关注用户
    private void fllow() {
        ClientDiscoverAPI.focusOperate(qjDetailBean.getData().getUser_id(), new RequestCallBack<String>() {
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
                    attentionBtn.setBackgroundResource(R.mipmap.index_has_attention);
                    attentionBtn.setText("");
                    attentionBtn.setPadding(0, 0, 0, 0);
                    attentionBtn.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    qjDetailBean.getData().getUser_info().setIs_follow(1);
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
    //取消关注弹窗
    private void showFocusFansConfirmView() {
        View view = Util.inflateView(activity, R.layout.popup_focus_fans, null);
        RoundedImageView riv = (RoundedImageView) view.findViewById(R.id.riv);
        TextView tv_take_photo = (TextView) view.findViewById(R.id.tv_take_photo);
        TextView tv_album = (TextView) view.findViewById(R.id.tv_album);
        TextView tv_cancel = (TextView) view.findViewById(R.id.tv_cancel);
        ImageLoader.getInstance().displayImage(qjDetailBean.getData().getUser_info().getAvatar_url(), riv);
        tv_take_photo.setText(String.format("取消关注" + " %s ?", qjDetailBean.getData().getUser_info().getNickname()));
        tv_album.setText("取消关注");
        tv_album.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupWindowUtil.dismiss();
                if (!dialog.isShowing()) {
                    dialog.show();
                }
                cancelFollow();
            }
        });
        tv_album.setTag(qjDetailBean);
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupWindowUtil.dismiss();
            }
        });
        PopupWindowUtil.show(activity, view);
    }
    //取消关注
    private void cancelFollow() {
        ClientDiscoverAPI.cancelFocusOperate(qjDetailBean.getData().getUser_id(), new RequestCallBack<String>() {
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
                    attentionBtn.setBackgroundResource(R.mipmap.index_attention);
                    attentionBtn.setText("");
                    attentionBtn.setPadding(0, 0, 0, 0);
                    attentionBtn.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    qjDetailBean.getData().getUser_info().setIs_follow(0);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case RESULT_OK:
                if (!dialog.isShowing()) {
                    dialog.show();
                }
                requestNet();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(qjDetailReceiver);
        super.onDestroy();
    }

    private BroadcastReceiver qjDetailReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (!dialog.isShowing()) {
                dialog.show();
            }
            requestNet();
        }
    };
}
