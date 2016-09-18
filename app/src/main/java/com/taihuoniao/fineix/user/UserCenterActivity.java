package com.taihuoniao.fineix.user;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.FansAdapter;
import com.taihuoniao.fineix.adapters.UserQJListAdapter;
import com.taihuoniao.fineix.adapters.UserQJListAdapter1;
import com.taihuoniao.fineix.album.ImageLoaderEngine;
import com.taihuoniao.fineix.album.Picker;
import com.taihuoniao.fineix.album.PicturePickerUtils;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.beans.HttpResponse;
import com.taihuoniao.fineix.beans.LoginInfo;
import com.taihuoniao.fineix.beans.QingJingListBean;
import com.taihuoniao.fineix.beans.SceneListBean;
import com.taihuoniao.fineix.beans.User;
import com.taihuoniao.fineix.beans.UserCJListData;
import com.taihuoniao.fineix.main.fragment.MineFragment;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.DataConstants;
import com.taihuoniao.fineix.qingjingOrSceneDetails.QJDetailActivity;
import com.taihuoniao.fineix.scene.SelectPhotoOrCameraActivity;
import com.taihuoniao.fineix.utils.Constants;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.LogUtil;
import com.taihuoniao.fineix.utils.PopupWindowUtil;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.utils.Util;
import com.taihuoniao.fineix.utils.WindowUtils;
import com.taihuoniao.fineix.view.WaittingDialog;
import com.taihuoniao.fineix.view.roundImageView.RoundedImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author lilin
 *         created at 2016/4/26 17:43
 */
public class UserCenterActivity extends BaseActivity implements View.OnClickListener {
    private int curPage = 1;
    private UserQJListAdapter1 adapterCJ;
    private UserQJListAdapter adapterQJ;
    private List<SceneListBean> mSceneList = new ArrayList<>();
    private List<QingJingListBean.QingJingItem> mQJList = new ArrayList<>();
    private LinearLayout ll_box;
    private LinearLayout ll_btn_box;
    @Bind(R.id.tv_title)
    TextView tv_title;
    @Bind(R.id.ll_tips)
    LinearLayout ll_tips;
    private RoundedImageView riv;
    private ImageView riv_auth;
    private TextView tv_real;
    private TextView tv_qj;
    private TextView tv_focus;
    private TextView tv_fans;
    private Button bt_focus;
    private Button bt_msg;
    private ImageView iv_bg;
    private TextView tv_label;
    private TextView tv_lv;
    private TextView tv_auth;
    private TextView iv_label;
    @Bind(R.id.iv_detail)
    ImageButton iv_detail;
    @Bind(R.id.iv_right)
    ImageButton iv_right;
    @Bind(R.id.rl_head_center)
    RelativeLayout rl_head;
    private RelativeLayout rl_focus;
    private LinearLayout ll_fans;
    private RelativeLayout rl_qj;
    private User user;
    private List<Uri> mSelected;
    private int which = MineFragment.REQUEST_CJ;
    private long userId = LoginInfo.getUserId();
    private static final int REQUEST_CODE_PICK_IMAGE = 100;
    private static final int REQUEST_CODE_CAPTURE_CAMERA = 101;
    private WaittingDialog dialog;
    public static final Uri imageUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "temp.jpg"));
    @Bind(R.id.lv_cj)
    ListView lv_cj;
    @Bind(R.id.lv_qj)
    ListView lv_qj;
    @Bind(R.id.tv_tips)
    TextView tv_tips;
    @Bind(R.id.btn_create)
    Button btnCreate;
    private boolean isFirstLoad = true;
    private String flag;
    private View headView;
    public UserCenterActivity() {
        super(R.layout.activity_user_center);
    }

    @Override
    protected void getIntentData() {
        Intent intent = getIntent();
        if (intent.hasExtra(MineFragment.class.getSimpleName())) {
            which = intent.getIntExtra(MineFragment.class.getSimpleName(), MineFragment.REQUEST_CJ);
        }

        if (intent.hasExtra(FocusActivity.USER_ID_EXTRA)) {
            userId = intent.getLongExtra(FocusActivity.USER_ID_EXTRA, LoginInfo.getUserId());
        }
        Log.e("<<<", "接收到的userId=" + userId);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        resetData();
        requestNet();
    }

    private void resetData() {
        isFirstLoad = true;
        curPage = 1;
        mQJList.clear();
        mSceneList.clear();
        ll_tips.setVisibility(View.GONE);
    }

    @Override
    protected void initView() {
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.user_center_bg)
                .showImageForEmptyUri(R.mipmap.user_center_bg)
                .showImageOnFail(R.mipmap.user_center_bg)
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
        WindowUtils.showStatusBar(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            rl_head.setPadding(0, getStatusBarHeight(), 0, 0);
        }
        rl_head.getBackground().mutate().setAlpha(25);
        headView = Util.inflateView(R.layout.user_center_headview, null);
        if (LoginInfo.getUserId() != userId) {
            iv_right.setVisibility(View.GONE);
        }
        iv_bg = ButterKnife.findById(headView, R.id.iv_bg);
        tv_label = ButterKnife.findById(headView, R.id.tv_label);
        riv = ButterKnife.findById(headView, R.id.riv);
        tv_real = ButterKnife.findById(headView, R.id.tv_real);
        tv_lv = ButterKnife.findById(headView, R.id.tv_lv);
        iv_label = ButterKnife.findById(headView, R.id.iv_label);
        tv_auth = ButterKnife.findById(headView, R.id.tv_auth);
        ll_btn_box = ButterKnife.findById(headView, R.id.ll_btn_box);
        tv_qj = ButterKnife.findById(headView, R.id.tv_qj);
        tv_focus = ButterKnife.findById(headView, R.id.tv_focus);
        tv_fans = ButterKnife.findById(headView, R.id.tv_fans);
        bt_focus = ButterKnife.findById(headView, R.id.bt_focus);
        bt_msg = ButterKnife.findById(headView, R.id.bt_msg);
        ll_box = ButterKnife.findById(headView, R.id.ll_box);
        rl_focus = ButterKnife.findById(headView, R.id.rl_focus);
        ll_fans = ButterKnife.findById(headView, R.id.ll_fans);
        rl_qj = ButterKnife.findById(headView, R.id.rl_qj);
        riv_auth = ButterKnife.findById(headView, R.id.riv_auth);

        dialog = new WaittingDialog(this);
        lv_cj.addHeaderView(headView);
        adapterCJ = new UserQJListAdapter1(mSceneList, activity);
        lv_cj.setAdapter(adapterCJ);
        if (userId == LoginInfo.getUserId()) {
            ll_btn_box.setVisibility(View.INVISIBLE);
        } else {
            ll_btn_box.setVisibility(View.VISIBLE);
        }
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    @Override
    protected void requestNet() {
        resetData();
        if (userId <= 0) {
            LogUtil.e(TAG, "userId<=0");
            return;
        }
        LogUtil.e(TAG, "requestNet==" + userId);
        ClientDiscoverAPI.getMineInfo(userId + "", new RequestCallBack<String>() {
            @Override
            public void onStart() {
                if (dialog != null && !activity.isFinishing()) dialog.show();
            }

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (dialog != null) dialog.dismiss();
                    }
                }, DataConstants.DIALOG_DELAY);

                if (TextUtils.isEmpty(responseInfo.result)) {
                    return;
                }

                try {
                    user = JsonUtil.fromJson(responseInfo.result, new TypeToken<HttpResponse<User>>() {
                    });
                    refreshUI();
                } catch (JsonSyntaxException e) {
                    LogUtil.e(TAG, e.getLocalizedMessage());
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                if (dialog != null) dialog.dismiss();
                ToastUtils.showError(R.string.network_err);
            }
        });

        switch (which) {
            case MineFragment.REQUEST_CJ:
                lv_qj.setVisibility(View.GONE);
                lv_cj.setVisibility(View.VISIBLE);
                loadCJData();
                break;
//            case MineFragment.REQUEST_QJ:
//                lv_cj.setVisibility(View.GONE);
//                lv_qj.setVisibility(View.VISIBLE);
//                loadQJData();
//                break;
        }
    }

    /**
     * 加载情境数据
     */
    private void loadCJData() {
        ClientDiscoverAPI.getSceneList(String.valueOf(curPage), Constants.PAGE_SIZE, String.valueOf(userId), new RequestCallBack<String>() {
            @Override
            public void onStart() {
                if (dialog != null && !activity.isFinishing()) {
                    if (curPage == 1) dialog.show();
                }
                curPage++;
            }

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (dialog != null) dialog.dismiss();
                    }
                }, DataConstants.DIALOG_DELAY);
                if (responseInfo == null) return;
                if (TextUtils.isEmpty(responseInfo.result)) return;
                LogUtil.e("getSceneList", responseInfo.result);
                HttpResponse response = JsonUtil.fromJson(responseInfo.result, HttpResponse.class);
                if (response.isSuccess()) {
                    UserCJListData listBean = JsonUtil.fromJson(responseInfo.result, new TypeToken<HttpResponse<UserCJListData>>() {
                    });
                    List list = listBean.rows;
                    refreshCJUI(list);
                    return;
                }
                ToastUtils.showError(response.getMessage());
            }

            @Override
            public void onFailure(HttpException e, String s) {
                if (dialog != null) dialog.dismiss();
                ToastUtils.showError(R.string.network_err);
            }
        });
    }


    /**
     * 刷新情境数据
     *
     * @param list
     */
    private void refreshCJUI(List<SceneListBean> list) {
        if (list == null) return;
        if (list.size() == 0) {
            if (mSceneList.size() == 0) {
                ll_tips.setVisibility(View.VISIBLE);
                if (LoginInfo.getUserId() == userId) {
                    btnCreate.setVisibility(View.VISIBLE);
                    tv_tips.setText(R.string.user_center_cj_tip);
                } else {
                    tv_tips.setText(R.string.user_center_cj_tip1);
                    btnCreate.setVisibility(View.GONE);
                }
            } else {
                ll_tips.setVisibility(View.GONE);
            }
            return;
        }
        if (adapterCJ == null) {
            mSceneList.addAll(list);
            adapterCJ = new UserQJListAdapter1(mSceneList, activity);
            lv_cj.setAdapter(adapterCJ);
        } else {
            mSceneList.addAll(list);
            adapterCJ.notifyDataSetChanged();
        }

    }

    @Deprecated
    private void loadQJData() {
        LogUtil.e("loadQJData", String.format("curPage==%s;;PAGE_SIZE==%s;;userId==%s", curPage, Constants.PAGE_SIZE, userId));
        ClientDiscoverAPI.getQJList(String.valueOf(curPage), Constants.PAGE_SIZE, String.valueOf(userId), new RequestCallBack<String>() {
            @Override
            public void onStart() {
                if (dialog != null && !activity.isFinishing()) {
                    if (curPage == 1) dialog.show();
                }
                curPage++;
            }

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (dialog != null) dialog.dismiss();
                    }
                }, DataConstants.DIALOG_DELAY);
                if (responseInfo == null) return;
                if (TextUtils.isEmpty(responseInfo.result)) return;
                LogUtil.e("getQJList", responseInfo.result);
                QingJingListBean listBean = JsonUtil.fromJson(responseInfo.result, QingJingListBean.class);
                if (listBean.isSuccess()) {
                    List list = listBean.getData().getRows();
                    LogUtil.e("每次请求==", list.size() + "");
                    refreshQJUI(list);
                    return;
                }
                ToastUtils.showError(listBean.getMessage());
            }

            @Override
            public void onFailure(HttpException e, String s) {
                if (dialog != null) dialog.dismiss();
                ToastUtils.showError(R.string.network_err);
            }
        });
    }


    //更新情景UI
    private void refreshQJUI(List<QingJingListBean.QingJingItem> list) {
        if (list == null) return;
        if (list.size() == 0) {
            if (isFirstLoad) {
                isFirstLoad = false;
                ll_tips.setVisibility(View.VISIBLE);
                if (LoginInfo.getUserId() == userId) {
                    tv_tips.setText(R.string.user_center_qj_tip);
                } else {
                    tv_tips.setText(R.string.user_center_qj_tip1);
                }
            } else {
                ll_tips.setVisibility(View.GONE);
            }
            return;
        }
        if (adapterQJ == null) {
            mQJList.addAll(list);
            adapterQJ = new UserQJListAdapter(mQJList, activity);
            lv_qj.setAdapter(adapterQJ);
        } else {
            mQJList.addAll(list);
            adapterQJ.notifyDataSetChanged();
        }
    }


    /**
     * 更新headview的UI
     */
    @Override
    protected void refreshUI() {
        if (user == null) {
            return;
        }
        LogUtil.e(TAG, "refreshUI===" + user._id);
        if (user.is_love == FansAdapter.NOT_LOVE) {
            setFocusBtnStyle(activity.getResources().getDimensionPixelSize(R.dimen.dp16), R.string.focus, R.mipmap.unfocus_white);
        } else {
            setFocusBtnStyle(activity.getResources().getDimensionPixelSize(R.dimen.dp10), R.string.focused, R.mipmap.focus_pic);
        }
        if (!TextUtils.isEmpty(user.nickname)) {
            tv_title.setText(user.nickname);
        }
        if (!TextUtils.isEmpty(user.medium_avatar_url)) {
            ImageLoader.getInstance().displayImage(user.medium_avatar_url, riv);
        }
        ImageLoader.getInstance().displayImage(user.head_pic_url, iv_bg, options);
        if (user.identify.is_expert == 1) {
            riv_auth.setVisibility(View.VISIBLE);
        } else {
            riv_auth.setVisibility(View.GONE);
        }

        if (TextUtils.isEmpty(user.summary)) {
            if (LoginInfo.getUserId() == userId) {
                tv_real.setText(String.format(" | %s", "说说你是什么人，来自哪片山川湖海！"));
            } else {
                tv_real.setText(String.format(" | %s", "这人好神秘，什么都不说"));
            }
        } else {
            tv_real.setText(String.format(" | %s", user.summary));
        }

        if (!TextUtils.isEmpty(user.expert_label)) {
            iv_label.setText(String.format("%s |", user.expert_label));
        } else {
            iv_label.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(user.expert_info)) {
            tv_auth.setText(user.expert_info);
        } else {
            tv_auth.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(user.label)) {
            tv_label.setText(String.format(" %s", user.label));
        } else {
            tv_label.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(user.expert_info)) {
            tv_auth.setText(user.expert_info);
        } else {
            tv_auth.setVisibility(View.GONE);
        }

        tv_lv.setText(String.format("Lv%s", user.rank_id));
        tv_qj.setText(String.valueOf(user.sight_count));
//        tv_cj.setText(String.valueOf(user.sight_count));
        tv_focus.setText(String.valueOf(user.follow_count));
        tv_fans.setText(String.valueOf(user.fans_count));
    }

    private void setFocusBtnStyle(int dimensionPixelSize, int focus, int unfocus_pic) {
        bt_focus.setPadding(dimensionPixelSize, 0, dimensionPixelSize, 0);
        bt_focus.setText(focus);
        bt_focus.setTextColor(activity.getResources().getColor(android.R.color.white));
        bt_focus.setBackgroundResource(R.drawable.border_radius5_pressed);
        bt_focus.setCompoundDrawablesWithIntrinsicBounds(unfocus_pic, 0, 0, 0);
    }

    private View initPopView(int layout, String title) {
        View view = Util.inflateView(activity, layout, null);
        ((TextView) view.findViewById(R.id.tv_title)).setText(title);
        View iv_take_photo = view.findViewById(R.id.tv_take_photo);
        View iv_take_album = view.findViewById(R.id.tv_album);
        View iv_close = view.findViewById(R.id.tv_cancel);
        iv_take_photo.setOnClickListener(this);
        iv_take_album.setOnClickListener(this);
        iv_close.setOnClickListener(this);
        return view;
    }

    @Override
    protected void installListener() {
        ll_box.setOnClickListener(this);
        iv_detail.setOnClickListener(this);
        iv_right.setOnClickListener(this);
        bt_focus.setOnClickListener(this);
        rl_focus.setOnClickListener(this);
        ll_fans.setOnClickListener(this);
        bt_msg.setOnClickListener(this);
        rl_qj.setOnClickListener(this);
        btnCreate.setOnClickListener(this);
        riv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (LoginInfo.getUserId() != userId) return;
                flag = EditUserInfoActivity.class.getSimpleName();
                PopupWindowUtil.show(activity, initPopView(R.layout.popup_upload_avatar, "更换头像"));
            }
        });

        lv_cj.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (mSceneList == null) return;
                if (mSceneList.size() == 0) return;
                SceneListBean item = (SceneListBean) lv_cj.getAdapter().getItem(i);
                Intent intent = new Intent(activity, QJDetailActivity.class);
                intent.putExtra("id", item.get_id());
                startActivity(intent);
            }
        });
//
//        lv_cj.setOnScrollListener(new AbsListView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(AbsListView absListView, int i) {
//                if (i == SCROLL_STATE_IDLE || i == SCROLL_STATE_FLING) {
//                    if (absListView.getLastVisiblePosition() == mSceneList.size()) {
//                        LogUtil.e("curPage==", curPage + "");
//                        isFirstLoad = false;
//                        loadCJData();
//                    }
//                }
//            }
//
//            @Override
//            public void onScroll(AbsListView absListView, int i, int i1, int i2) {
//            }
//        });
        lv_cj.setOnScrollListener(new AbsListView.OnScrollListener() {
            private int headerHeight = 0;
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
                if (i == SCROLL_STATE_IDLE || i == SCROLL_STATE_FLING) {
                    LogUtil.e("getLastVisiblePosition", absListView.getLastVisiblePosition() + "");
                    LogUtil.e("mQJList.size", mSceneList.size() + "");
                    isFirstLoad = false;
                    if (mSceneList.size() % 2 == 0) {
                        if (absListView.getLastVisiblePosition() == mSceneList.size() / 2) {
                            LogUtil.e("curPage==偶数", curPage + "");
                            loadCJData();
                        }
                    } else {
                        if (absListView.getLastVisiblePosition() == mSceneList.size() / 2 + 1) {
                            LogUtil.e("curPage==奇数", curPage + "");
                            loadCJData();
                        }
                    }
                }
            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {
                int height = getResources().getDimensionPixelSize(R.dimen.dp150);
                if (null != headView) {
                    int top = -(headView.getTop());
                    headerHeight = headView.getMeasuredHeight();
                    if (top > 0 && top <= height) {
                        float scale = (float) top / headerHeight;
                        float alpha = (255 * scale);
                        rl_head.getBackground().mutate().setAlpha((int) alpha);
                    } else if (top == 0) {
                        rl_head.getBackground().mutate().setAlpha(25);
                    } else if (top > height) {
                        rl_head.getBackground().mutate().setAlpha(204);
                    }
                }
            }
        });
    }

    public int getScrollY() {
        View c = lv_cj.getChildAt(0);
        if (c == null) {
            return 0;
        }
        int firstVisiblePosition = lv_cj.getFirstVisiblePosition();
        int top = c.getTop();
        return -top + firstVisiblePosition * c.getHeight();
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.tv_take_photo:
                PopupWindowUtil.dismiss();
                getImageFromCamera();
                break;
            case R.id.tv_album:
                PopupWindowUtil.dismiss();
                getImageFromAlbum();
                break;
            case R.id.tv_cancel:
                PopupWindowUtil.dismiss();
                break;
            case R.id.ll_box:
                if (LoginInfo.getUserId() != userId) return;
                flag = UserCenterActivity.class.getSimpleName();
                PopupWindowUtil.show(activity, initPopView(R.layout.popup_upload_avatar, "更换个人主页封面"));
                break;
            case R.id.iv_right:
                startActivity(new Intent(activity, EditUserInfoActivity.class));
                break;
            case R.id.iv_detail:
                finish();
                break;
            case R.id.rl_focus:
                intent = new Intent(activity, FocusActivity.class);
                intent.putExtra(FocusActivity.USER_ID_EXTRA, userId);
                startActivity(intent);
                break;
            case R.id.ll_fans:
                intent = new Intent(activity, FansActivity.class);
                intent.putExtra(FocusActivity.USER_ID_EXTRA, userId);
                startActivity(intent);
                break;
            case R.id.bt_msg:
                intent = new Intent(activity, PrivateMessageActivity.class);
                intent.putExtra(UserCenterActivity.class.getSimpleName(), user);
                startActivity(intent);
                break;
            case R.id.btn_create:
                intent = new Intent(activity, SelectPhotoOrCameraActivity.class);
                startActivity(intent);
                break;
            case R.id.bt_focus:
                bt_focus.setEnabled(false);
                if (user.is_love == FansAdapter.NOT_LOVE) {
                    ClientDiscoverAPI.focusOperate(userId + "", new RequestCallBack<String>() {
                        @Override
                        public void onSuccess(ResponseInfo<String> responseInfo) {
                            bt_focus.setEnabled(true);
                            if (responseInfo == null) return;
                            if (TextUtils.isEmpty(responseInfo.result)) return;
                            LogUtil.e("focusOperate", responseInfo.result);
                            HttpResponse response = JsonUtil.fromJson(responseInfo.result, HttpResponse.class);
                            if (response.isSuccess()) {
                                user.is_love = FansAdapter.LOVE;
//                                bt_focus.setText("已关注");
                                setFocusBtnStyle(activity.getResources().getDimensionPixelSize(R.dimen.dp10), R.string.focused, R.mipmap.focus_pic);
                                return;
                            }
                            ToastUtils.showError(response.getMessage());
                        }

                        @Override
                        public void onFailure(HttpException e, String s) {
                            bt_focus.setEnabled(true);
                            ToastUtils.showError(R.string.network_err);
                        }
                    });
                } else {
                    ClientDiscoverAPI.cancelFocusOperate(userId + "", new RequestCallBack<String>() {
                        @Override
                        public void onSuccess(ResponseInfo<String> responseInfo) {
                            bt_focus.setEnabled(true);
                            PopupWindowUtil.dismiss();
                            if (responseInfo == null) return;
                            if (TextUtils.isEmpty(responseInfo.result)) return;
                            LogUtil.e("cancelFocusOperate", responseInfo.result);
                            HttpResponse response = JsonUtil.fromJson(responseInfo.result, HttpResponse.class);
                            if (response.isSuccess()) {
                                user.is_love = FansAdapter.NOT_LOVE;
//                                bt_focus.setText("关注");
                                setFocusBtnStyle(activity.getResources().getDimensionPixelSize(R.dimen.dp16), R.string.focus, R.mipmap.unfocus_white);
                                return;
                            }
                            ToastUtils.showError(response.getMessage());
                        }

                        @Override
                        public void onFailure(HttpException e, String s) {
                            bt_focus.setEnabled(true);
                            PopupWindowUtil.dismiss();
                            ToastUtils.showError(R.string.network_err);
                        }
                    });
                }
                break;
            case R.id.ibtn:
                Util.makeToast("认证");
                break;
//            case R.id.ll_cj:
//                if (which == MineFragment.REQUEST_CJ) return;
//                showCj();
//                break;
            case R.id.rl_qj:
                if (which == MineFragment.REQUEST_CJ) return;
                showCj();
                break;
        }
    }

    private void showCj() {
        resetData();
        lv_cj.setVisibility(View.GONE);
        lv_qj.setVisibility(View.VISIBLE);
        which = MineFragment.REQUEST_CJ;
        adapterCJ = null;
        loadCJData();
    }

    private void showQJ() {
        resetData();
        lv_qj.setVisibility(View.VISIBLE);
        lv_cj.setVisibility(View.GONE);
        which = MineFragment.REQUEST_QJ;
        adapterQJ = null;
        loadQJData();
    }

    protected void getImageFromAlbum() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            Picker.from(this)
                    .count(1)
                    .enableCamera(false)
                    .singleChoice()
                    .setEngine(new ImageLoaderEngine())
                    .forResult(REQUEST_CODE_PICK_IMAGE);
        } else {
            ToastUtils.showError("未检测到SD卡");
        }
    }

    protected void getImageFromCamera() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            startActivityForResult(intent, REQUEST_CODE_CAPTURE_CAMERA);
        } else {
            ToastUtils.showError("未检测到SD卡");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_PICK_IMAGE:
//                    Uri uri = data.getData();
                    mSelected = PicturePickerUtils.obtainResult(data);
                    if (mSelected == null) return;
                    if (mSelected.size() == 0) return;
//                    if (uri != null) {
//                        Bitmap bitmap = ImageUtils.decodeUriAsBitmap(uri);
//                        mClipImageLayout.setImageBitmap(bitmap);
                    toCropActivity(mSelected.get(0));
//                    } else {
//                        Util.makeToast("抱歉，从相册获取图片失败");
//                    }
                    break;
                case REQUEST_CODE_CAPTURE_CAMERA:
//                    Bitmap bitmap =ImageUtils.decodeUriAsBitmap(imageUri);
                    if (imageUri != null) {
//                        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,MediaStore.Images.Media.EXTERNAL_CONTENT_URI));
                        toCropActivity(imageUri);
                    }
                    break;
            }
        }
    }

    private void toCropActivity(Uri uri) {
        Intent intent = new Intent(activity, ImageCropActivity.class);
        intent.putExtra(ImageCropActivity.class.getSimpleName(), uri);
        intent.putExtra(ImageCropActivity.class.getName(), flag);
        startActivity(intent);
    }


}
