package com.taihuoniao.fineix.user;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.CropOptionAdapter;
import com.taihuoniao.fineix.adapters.FocusFansAdapter;
import com.taihuoniao.fineix.adapters.UserCJListAdapter;
import com.taihuoniao.fineix.adapters.UserQJListAdapter;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.beans.CropOption;
import com.taihuoniao.fineix.beans.ImgUploadBean;
import com.taihuoniao.fineix.beans.LoginInfo;
import com.taihuoniao.fineix.beans.QingJingListBean;
import com.taihuoniao.fineix.beans.SceneListBean;
import com.taihuoniao.fineix.beans.User;
import com.taihuoniao.fineix.beans.UserCJListData;
import com.taihuoniao.fineix.main.fragment.MineFragment;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.HttpResponse;
import com.taihuoniao.fineix.utils.Base64Utils;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.LogUtil;
import com.taihuoniao.fineix.utils.PopupWindowUtil;
import com.taihuoniao.fineix.utils.Util;
import com.taihuoniao.fineix.view.WaittingDialog;
import com.taihuoniao.fineix.view.roundImageView.RoundedImageView;

import java.io.File;
import java.io.IOException;
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
    private static final String PAGE_SIZE = "10";
    private UserCJListAdapter adapterCJ;
    private UserQJListAdapter adapterQJ;
    private List<SceneListBean> mSceneList = new ArrayList<>();
    private List<QingJingListBean.QingJingItem> mQJList = new ArrayList<>();
    private LinearLayout ll_box;
    private LinearLayout ll_btn_box;
    private TextView tv_title;
    private RoundedImageView riv;
    private TextView tv_real;
    private TextView tv_nick;
    private TextView tv_rank;
    private TextView tv_qj;
    private TextView tv_cj;
    private TextView tv_focus;
    private TextView tv_fans;
    private Button bt_focus;
    private Button bt_msg;
    private ImageButton ibtn;
    private ImageView iv_bg;
    private ImageButton iv_detail;
    private ImageButton iv_right;
    private LinearLayout ll_focus;
    private LinearLayout ll_fans;
    private LinearLayout ll_qj;
    private LinearLayout ll_cj;
    private User user;
    private Uri mImageUri;
    private int which = MineFragment.REQUEST_CJ;
    private long userId = LoginInfo.getUserId();
    private static final int PICK_FROM_FILE = 0;
    private static final int PICK_FROM_CAMERA = 1;
    private static final int CROP_FROM_CAMERA = 2;
    private boolean isLoadMore = false;
    private WaittingDialog dialog;
    @Bind(R.id.lv_cj)
    ListView lv_cj;
    @Bind(R.id.lv_qj)
    ListView lv_qj;

    public UserCenterActivity() {
        super(R.layout.activity_user_center);
    }

    @Override
    protected void getIntentData() {
        Intent intent = getIntent();
        if (intent.hasExtra(MineFragment.class.getSimpleName())) {
            which = intent.getIntExtra(MineFragment.class.getSimpleName(), MineFragment.REQUEST_CJ);
        }

        if (intent.hasExtra(FocusFansActivity.USER_ID_EXTRA)) {
            userId = intent.getLongExtra(FocusFansActivity.USER_ID_EXTRA, LoginInfo.getUserId());
        }
    }


    @Override
    protected void initView() {
        View headView = Util.inflateView(activity, R.layout.user_center_headview, null);
        iv_detail = ButterKnife.findById(headView, R.id.iv_detail);
        iv_bg = ButterKnife.findById(headView, R.id.iv_bg);
        riv = ButterKnife.findById(headView, R.id.riv);
        tv_nick = ButterKnife.findById(headView, R.id.tv_nick);
        tv_real = ButterKnife.findById(headView, R.id.tv_real);
        tv_rank = ButterKnife.findById(headView, R.id.tv_rank);
        tv_title = ButterKnife.findById(headView, R.id.tv_title);
        ibtn = ButterKnife.findById(headView, R.id.ibtn);
        ll_btn_box = ButterKnife.findById(headView, R.id.ll_btn_box);
        tv_cj = ButterKnife.findById(headView, R.id.tv_cj);
        tv_qj = ButterKnife.findById(headView, R.id.tv_qj);
        tv_focus = ButterKnife.findById(headView, R.id.tv_focus);
        tv_fans = ButterKnife.findById(headView, R.id.tv_fans);
        bt_focus = ButterKnife.findById(headView, R.id.bt_focus);
        bt_msg = ButterKnife.findById(headView, R.id.bt_msg);
        ll_box = ButterKnife.findById(headView, R.id.ll_box);
        iv_right = ButterKnife.findById(headView, R.id.iv_right);
        ll_focus = ButterKnife.findById(headView, R.id.ll_focus);
        ll_fans = ButterKnife.findById(headView, R.id.ll_fans);
        ll_cj = ButterKnife.findById(headView, R.id.ll_cj);
        ll_qj = ButterKnife.findById(headView, R.id.ll_qj);

        dialog = new WaittingDialog(this);

        lv_cj.addHeaderView(headView);
        lv_qj.addHeaderView(headView);
        if (userId == LoginInfo.getUserId()) {
            ll_btn_box.setVisibility(View.GONE);
            ibtn.setVisibility(View.VISIBLE);
        } else {
            ll_btn_box.setVisibility(View.VISIBLE);
            ibtn.setVisibility(View.GONE);
        }
    }

    @Override
    protected void requestNet() {
        if (userId < 0) {
            LogUtil.e(TAG, "userId<0");
            return;
        }
        LogUtil.e(TAG, "requestNet==" + userId);
        dialog.show();
        ClientDiscoverAPI.getMineInfo(userId + "", new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                dialog.dismiss();
                LogUtil.e("result", responseInfo.result);
                if (responseInfo == null) {
                    return;
                }

                if (TextUtils.isEmpty(responseInfo.result)) {
                    return;
                }

                try {
                    user = JsonUtil.fromJson(responseInfo.result, new TypeToken<HttpResponse<User>>() {
                    });
                } catch (JsonSyntaxException e) {
                    LogUtil.e(TAG, e.getLocalizedMessage());
                    Util.makeToast("对不起,数据异常");
                }
                refreshUI();
            }

            @Override
            public void onFailure(HttpException e, String s) {
                dialog.dismiss();
                if (TextUtils.isEmpty(s))
                    LogUtil.e(TAG, s);
                Util.makeToast(s);
            }
        });

        switch (which) {
            case MineFragment.REQUEST_CJ:
                lv_qj.setVisibility(View.GONE);
                lv_cj.setVisibility(View.VISIBLE);
                loadCJData();
                break;
            case MineFragment.REQUEST_QJ:
                lv_cj.setVisibility(View.GONE);
                lv_qj.setVisibility(View.VISIBLE);
                loadQJData();
                break;
        }
    }

    /**
     * 加载场景数据
     */
    private void loadCJData() {
        ClientDiscoverAPI.getSceneList(String.valueOf(curPage), PAGE_SIZE, String.valueOf(userId), new RequestCallBack<String>() {
            @Override
            public void onStart() {
                if (dialog != null) {
                    if (curPage == 1) dialog.show();
                }
            }

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                dialog.dismiss();
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
                Util.makeToast(response.getMessage());
            }

            @Override
            public void onFailure(HttpException e, String s) {
                dialog.dismiss();
                if (TextUtils.isEmpty(s)) return;
                Util.makeToast(s);
            }
        });
    }


    /**
     * 刷新场景数据
     *
     * @param list
     */
    private void refreshCJUI(List<SceneListBean> list) {
        if (list == null) return;
        if (isLoadMore) {
            if (list.size() == 0) return;
        } else {
            if (list.size() == 0) {
                lv_cj.setAdapter(null);
                return;
            }
        }
        curPage++;
        if (adapterCJ == null) {
            mSceneList.addAll(list);
            adapterCJ = new UserCJListAdapter(mSceneList, activity);
            lv_cj.setAdapter(adapterCJ);
        } else {
            mSceneList.addAll(list);
            adapterCJ.notifyDataSetChanged();
        }

    }

    /**
     * 加载情景数据
     */
    private void loadQJData() {
        LogUtil.e("loadQJData",String.format("curPage==%s;;PAGE_SIZE==%s;;userId==%s",curPage,PAGE_SIZE,userId));
        ClientDiscoverAPI.getQJList(String.valueOf(curPage), PAGE_SIZE, String.valueOf(userId), new RequestCallBack<String>() {
            @Override
            public void onStart() {
                if (curPage == 1) dialog.show();
            }

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                dialog.dismiss();
                if (responseInfo == null) return;
                if (TextUtils.isEmpty(responseInfo.result)) return;
                LogUtil.e("getQJList", responseInfo.result);
                QingJingListBean listBean = JsonUtil.fromJson(responseInfo.result, QingJingListBean.class);
                if (listBean.isSuccess()) {
                    List list = listBean.getData().getRows();
                    LogUtil.e("每次请求==",list.size()+"");
                    refreshQJUI(list);
                    return;
                }
                Util.makeToast(listBean.getMessage());
            }

            @Override
            public void onFailure(HttpException e, String s) {
                dialog.dismiss();
                if (TextUtils.isEmpty(s)) return;
                Util.makeToast(s);
            }
        });
    }


    //更新情景UI
    private void refreshQJUI(List<QingJingListBean.QingJingItem> list) {
        if (list == null) return;
        if (isLoadMore) {
            if (list.size() == 0) return;
        } else {
            if (list.size() == 0) {
                lv_qj.setAdapter(null);
                return;
            }
        }
        curPage++;
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
        if (user.is_love == FocusFansAdapter.NOT_LOVE) {
            bt_focus.setText("关注");
        } else {
            bt_focus.setText("已关注");
        }
        if (!TextUtils.isEmpty(user.nickname)) {
            if (LoginInfo.getUserId() != userId) {
                tv_title.setVisibility(View.VISIBLE);
                tv_title.setText(user.nickname);
            } else {
                tv_title.setVisibility(View.GONE);
            }
        }
        if (!TextUtils.isEmpty(user.medium_avatar_url)) {
            ImageLoader.getInstance().displayImage(user.medium_avatar_url, riv, options);
        }
        if (!TextUtils.isEmpty(user.head_pic_url)) {
//            ImageUtils.loadBgImg(user.head_pic_url, ll_box);
            ImageLoader.getInstance().displayImage(user.head_pic_url, iv_bg, options);
        }

        if (TextUtils.isEmpty(user.summary)) {
            tv_real.setVisibility(View.GONE);
        } else {
            tv_real.setText(user.summary);
        }


        if (TextUtils.isEmpty(user.nickname) || LoginInfo.getUserId() != userId) {
            tv_nick.setVisibility(View.GONE);
        } else {
            tv_nick.setText(user.nickname);
        }

        if (TextUtils.isEmpty(user.rank_title)) {
            tv_rank.setVisibility(View.GONE);
        } else {
            tv_rank.setText(String.format("%s | V%s", user.rank_title, user.rank_id));
        }
        tv_qj.setText(String.valueOf(user.scene_count));
        tv_cj.setText(String.valueOf(user.sight_count));
        tv_focus.setText(String.valueOf(user.follow_count));
        tv_fans.setText(String.valueOf(user.fans_count));
    }


//    @OnClick({R.id.ll_box, R.id.ll_qj,R.id.ibtn,R.id.ll_cj, R.id.ll_focus, R.id.ll_fans, R.id.iv_right, R.id.iv_detail, R.id.bt_focus, R.id.bt_msg})
//    void performClick(View v) {
//        Intent intent = null;
//        switch (v.getId()) {
//            case R.id.iv_detail:
//                finish();
//                break;
//            case R.id.iv_right:
//                startActivity(new Intent(activity, EditUserInfoActivity.class));
//                break;
//            case R.id.ibtn:
//                Util.makeToast("认证");
//                break;
//            case R.id.bt_focus:
////                bt_focus.setEnabled(false);
//                if (user.is_love == FocusFansAdapter.NOT_LOVE){
//                    ClientDiscoverAPI.focusOperate(userId + "", new RequestCallBack<String>() {
//                        @Override
//                        public void onSuccess(ResponseInfo<String> responseInfo) {
////                            bt_focus.setEnabled(true);
//                            if (responseInfo == null) return;
//                            if (TextUtils.isEmpty(responseInfo.result)) return;
//                            LogUtil.e("focusOperate",responseInfo.result);
//                            HttpResponse response = JsonUtil.fromJson(responseInfo.result, HttpResponse.class);
//                            if (response.isSuccess()) {
//                                user.is_love=FocusFansAdapter.LOVE;
////                                bt_focus.setText("已关注");
//                                Util.makeToast(response.getMessage());
//                                return;
//                            }
//
//                            Util.makeToast(response.getMessage());
//                        }
//
//                        @Override
//                        public void onFailure(HttpException e, String s) {
////                            bt_focus.setEnabled(true);
//                            Util.makeToast(s);
//                        }
//                    });
//                }else {
//                    ClientDiscoverAPI.cancelFocusOperate(userId+"", new RequestCallBack<String>() {
//                        @Override
//                        public void onSuccess(ResponseInfo<String> responseInfo) {
////                            bt_focus.setEnabled(true);
//                            PopupWindowUtil.dismiss();
//                            if (responseInfo==null) return;
//                            if (TextUtils.isEmpty(responseInfo.result)) return;
//                            LogUtil.e("cancelFocusOperate",responseInfo.result);
//                            HttpResponse response = JsonUtil.fromJson(responseInfo.result, HttpResponse.class);
//                            if (response.isSuccess()){
//                                user.is_love=FocusFansAdapter.NOT_LOVE;
////                                bt_focus.setText("关注");
//                                Util.makeToast(response.getMessage());
//                                return;
//                            }
//
//                            Util.makeToast(response.getMessage());
//                        }
//
//                        @Override
//                        public void onFailure(HttpException e, String s) {
////                            bt_focus.setEnabled(true);
//                            PopupWindowUtil.dismiss();
//                            Util.makeToast(s);
//                        }
//                    });
//                }
//                break;
//            case R.id.bt_msg:
//                intent=new Intent(activity,PrivateMessageActivity.class);
//                intent.putExtra(UserCenterActivity.class.getSimpleName(),user);
//                startActivity(intent);
//                break;
//            case R.id.ll_qj:
////                switchFragmentandImg(UserQJFragment.class);
//                break;
//            case R.id.ll_cj:
////                switchFragmentandImg(UserCJFragment.class);
//                break;
//            case R.id.ll_focus:
//                intent = new Intent(activity, FocusFansActivity.class);
//                intent.putExtra(FocusFansActivity.class.getSimpleName(), FocusFansActivity.FOCUS_TYPE);
//                intent.putExtra(FocusFansActivity.USER_ID_EXTRA, userId);
//                startActivity(intent);
//                break;
//            case R.id.ll_fans:
//                intent = new Intent(activity, FocusFansActivity.class);
//                intent.putExtra(FocusFansActivity.class.getSimpleName(), FocusFansActivity.FANS_TYPE);
//                intent.putExtra(FocusFansActivity.USER_ID_EXTRA, userId);
//                startActivity(intent);
//                break;
//
//            case R.id.ll_box:
////                if (LoginInfo.getUserId()!=userId) return;
////                PopupWindowUtil.show(activity, initPopView(R.layout.popup_upload_avatar));
//                break;
//        }
//    }

    private View initPopView(int layout) {
        View view = Util.inflateView(this, layout, null);
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
        ll_focus.setOnClickListener(this);
        ll_fans.setOnClickListener(this);
        bt_msg.setOnClickListener(this);
        ibtn.setOnClickListener(this);
        ll_cj.setOnClickListener(this);
        ll_qj.setOnClickListener(this);

        lv_cj.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
                if (i == SCROLL_STATE_IDLE ||i==SCROLL_STATE_FLING) {
                    if (absListView.getLastVisiblePosition() == mSceneList.size()) {
                        isLoadMore = true;
                        LogUtil.e("curPage==",curPage+"");
                        loadCJData();
                    }
                }
            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {
            }
        });

        lv_qj.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
                if (i == SCROLL_STATE_IDLE || i==SCROLL_STATE_FLING) {
                    LogUtil.e("getLastVisiblePosition",absListView.getLastVisiblePosition()+"");
                    LogUtil.e("mQJList.size",mQJList.size()+"");
                    if (mQJList.size()%2==0){
                        if (absListView.getLastVisiblePosition() == mQJList.size()/2) {
                            isLoadMore = true;
                            LogUtil.e("curPage==偶数",curPage+"");
                            loadQJData();
                        }
                    }else {
                        if (absListView.getLastVisiblePosition() == mQJList.size()/2+1) {
                            isLoadMore = true;
                            LogUtil.e("curPage==奇数",curPage+"");
                            loadQJData();
                        }
                    }
                }
            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.tv_take_photo:
                PopupWindowUtil.dismiss();
                mImageUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "tmp" + ".png"));
                intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
                try {
                    intent.putExtra("return-data", true);
                    startActivityForResult(intent, PICK_FROM_CAMERA);
                } catch (ActivityNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.tv_album:
                intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                activity.startActivityForResult(Intent.createChooser(intent, "请选择要使用的应用"), PICK_FROM_FILE);//Intent.createChooser(intent, "选择要使用的应用")
                PopupWindowUtil.dismiss();
                break;
            case R.id.tv_cancel:
                PopupWindowUtil.dismiss();
                break;
            case R.id.ll_box:
                if (LoginInfo.getUserId() != userId) return;
                PopupWindowUtil.show(activity, initPopView(R.layout.popup_upload_avatar));
                break;
            case R.id.iv_right:
                startActivity(new Intent(activity, EditUserInfoActivity.class));
                break;
            case R.id.iv_detail:
                finish();
                break;
            case R.id.ll_focus:
                intent = new Intent(activity, FocusFansActivity.class);
                intent.putExtra(FocusFansActivity.class.getSimpleName(), FocusFansActivity.FOCUS_TYPE);
                intent.putExtra(FocusFansActivity.USER_ID_EXTRA, userId);
                startActivity(intent);
                break;
            case R.id.ll_fans:
                intent = new Intent(activity, FocusFansActivity.class);
                intent.putExtra(FocusFansActivity.class.getSimpleName(), FocusFansActivity.FANS_TYPE);
                intent.putExtra(FocusFansActivity.USER_ID_EXTRA, userId);
                startActivity(intent);
                break;
            case R.id.bt_msg:
                intent = new Intent(activity, PrivateMessageActivity.class);
                intent.putExtra(UserCenterActivity.class.getSimpleName(), user);
                startActivity(intent);
                break;
            case R.id.bt_focus:
                bt_focus.setEnabled(false);
                if (user.is_love == FocusFansAdapter.NOT_LOVE) {
                    ClientDiscoverAPI.focusOperate(userId + "", new RequestCallBack<String>() {
                        @Override
                        public void onSuccess(ResponseInfo<String> responseInfo) {
                            bt_focus.setEnabled(true);
                            if (responseInfo == null) return;
                            if (TextUtils.isEmpty(responseInfo.result)) return;
                            LogUtil.e("focusOperate", responseInfo.result);
                            HttpResponse response = JsonUtil.fromJson(responseInfo.result, HttpResponse.class);
                            if (response.isSuccess()) {
                                user.is_love = FocusFansAdapter.LOVE;
                                bt_focus.setText("已关注");
                                Util.makeToast(response.getMessage());
                                return;
                            }

                            Util.makeToast(response.getMessage());
                        }

                        @Override
                        public void onFailure(HttpException e, String s) {
                            bt_focus.setEnabled(true);
                            Util.makeToast(s);
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
                                user.is_love = FocusFansAdapter.NOT_LOVE;
                                bt_focus.setText("关注");
                                Util.makeToast(response.getMessage());
                                return;
                            }

                            Util.makeToast(response.getMessage());
                        }

                        @Override
                        public void onFailure(HttpException e, String s) {
                            bt_focus.setEnabled(true);
                            PopupWindowUtil.dismiss();
                            Util.makeToast(s);
                        }
                    });
                }
                break;
            case R.id.ibtn:
                Util.makeToast("认证");
                break;
            case R.id.ll_cj:
                if (which==MineFragment.REQUEST_CJ) return;
                showCj();
                break;
            case R.id.ll_qj:
                if (which==MineFragment.REQUEST_QJ) return;
                showQJ();
                break;
        }
    }

    private void showCj(){
        which=MineFragment.REQUEST_CJ;
        curPage=1;
        isLoadMore=false;
        mSceneList.clear();
        lv_qj.setVisibility(View.GONE);
        lv_cj.setVisibility(View.VISIBLE);
        loadCJData();
    }

    private void showQJ(){
        LogUtil.e("清除前==",mQJList.size()+"");
        mQJList.clear();
        LogUtil.e("清除后==",mQJList.size()+"");
        which=MineFragment.REQUEST_QJ;
        curPage=1;
        isLoadMore=false;
        lv_cj.setVisibility(View.GONE);
        lv_qj.setVisibility(View.VISIBLE);
        loadQJData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            File file = null;
            switch (requestCode) {
                case PICK_FROM_CAMERA:
                    doCrop();
                    break;
                case PICK_FROM_FILE:
                    mImageUri = data.getData();
                    doCrop();
                    break;
                case CROP_FROM_CAMERA:
                    Bundle extras = data.getExtras();
                    if (extras != null) {
                        Bitmap photo = extras.getParcelable("data");
                        if (photo != null) {
                            uploadFile(Util.saveBitmapToFile(photo));
                        } else {
                            Util.makeToast(activity, "截取头像失败");
                            return;
                        }
//                    }
                        break;
                    }
            }
        }
    }

    private void uploadFile(final File file) { //换个人中心背景图
        if (file == null) return;
        if (file.length() == 0) return;
        try {
            ClientDiscoverAPI.uploadBgImg(Base64Utils.encodeFile2Base64Str(file), new RequestCallBack<String>() {
                @Override
                public void onSuccess(ResponseInfo<String> responseInfo) {
                    if (responseInfo == null) {
                        return;
                    }
                    if (TextUtils.isEmpty(responseInfo.result)) {
                        return;
                    }
                    LogUtil.e(TAG, responseInfo.result);
                    HttpResponse response = JsonUtil.fromJson(responseInfo.result, HttpResponse.class);
                    if (response.isSuccess()) {
                        ImgUploadBean imgUploadBean = JsonUtil.fromJson(responseInfo.result, new TypeToken<HttpResponse<ImgUploadBean>>() {
                        });
                        if (!TextUtils.isEmpty(imgUploadBean.head_pic_url)) {
//                            ImageUtils.loadBgImg(imgUploadBean.head_pic_url,ll_box);
                            ImageLoader.getInstance().displayImage(imgUploadBean.head_pic_url,iv_bg);
                        }
                        Util.makeToast(response.getMessage());
                        return;
                    }
                    Util.makeToast(response.getMessage());
                }

                @Override
                public void onFailure(HttpException e, String s) {
                    Util.makeToast(s);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (file.exists()) file.delete();
        }

    }

    private void doCrop() {
        final ArrayList<CropOption> cropOptions = new ArrayList<CropOption>();
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setType("image/*");
        List<ResolveInfo> list = getPackageManager().queryIntentActivities(intent, 0);
        int size = list.size();
        if (size == 0) {
            Util.makeToast(this, "找不到图片裁剪应用");
            return;
        } else {
            intent.setData(mImageUri);
            intent.putExtra("noFaceDetection", true);
            intent.putExtra("outputX", 400);
            intent.putExtra("outputY", 300);
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            intent.putExtra("scale", true);
            intent.putExtra("return-data", true);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
            if (size == 1) {
                Intent i = new Intent(intent);
                ResolveInfo res = list.get(0);
                i.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
                startActivityForResult(i, CROP_FROM_CAMERA);
            } else {
                for (ResolveInfo res : list) {
                    final CropOption co = new CropOption();
                    co.title = getPackageManager().getApplicationLabel(res.activityInfo.applicationInfo);
                    co.icon = getPackageManager().getApplicationIcon(res.activityInfo.applicationInfo);
                    co.appIntent = new Intent(intent);
                    co.appIntent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
                    cropOptions.add(co);
                }
                CropOptionAdapter adapter = new CropOptionAdapter(getApplicationContext(), cropOptions);
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("选择要使用的应用");
                builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        startActivityForResult(cropOptions.get(item).appIntent, CROP_FROM_CAMERA);
                    }
                });

                builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {

                        if (mImageUri != null) {
                            getContentResolver().delete(mImageUri, null, null);
                            mImageUri = null;
                        }
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();
            }
        }
    }
}
