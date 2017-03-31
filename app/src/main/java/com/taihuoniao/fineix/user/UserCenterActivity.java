package com.taihuoniao.fineix.user;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
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
import com.taihuoniao.fineix.BuildConfig;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.FansAdapter;
import com.taihuoniao.fineix.adapters.UserQJListAdapter1;
import com.taihuoniao.fineix.album.ImageLoaderEngine;
import com.taihuoniao.fineix.album.Picker;
import com.taihuoniao.fineix.album.PicturePickerUtils;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.base.HttpRequest;
import com.taihuoniao.fineix.beans.HttpResponse;
import com.taihuoniao.fineix.beans.LoginInfo;
import com.taihuoniao.fineix.beans.QingJingListBean;
import com.taihuoniao.fineix.beans.SceneListBean;
import com.taihuoniao.fineix.beans.User;
import com.taihuoniao.fineix.beans.UserCJListData;
import com.taihuoniao.fineix.common.GlobalDataCallBack;
import com.taihuoniao.fineix.main.App;
import com.taihuoniao.fineix.main.fragment.MineFragment;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.URL;
import com.taihuoniao.fineix.qingjingOrSceneDetails.QJDetailActivity;
import com.taihuoniao.fineix.scene.SelectPhotoOrCameraActivity;
import com.taihuoniao.fineix.utils.Constants;
import com.taihuoniao.fineix.utils.FileCameraUtil;
import com.taihuoniao.fineix.utils.GlideUtils;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.LogUtil;
import com.taihuoniao.fineix.utils.PopupWindowUtil;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.utils.Util;
import com.taihuoniao.fineix.utils.WindowUtils;
import com.taihuoniao.fineix.view.dialog.WaittingDialog;
import com.taihuoniao.fineix.view.roundImageView.RoundedImageView;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionNo;
import com.yanzhenjie.permission.PermissionYes;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.taihuoniao.fineix.utils.Constants.REQUEST_CODE_SETTING;
import static com.taihuoniao.fineix.utils.Constants.REQUEST_PERMISSION_CODE;

/**
 * @author lilin
 *         created at 2016/4/26 17:43
 */
public class UserCenterActivity extends BaseActivity implements View.OnClickListener {
    private int curPage = 1;
    private UserQJListAdapter1 adapterCJ;
    private List<SceneListBean> mSceneList = new ArrayList<>();
    private List<QingJingListBean.QingJingItem> mQJList = new ArrayList<>();
    private LinearLayout ll_box;
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
    private String mFilePath;
    private int which = MineFragment.REQUEST_CJ;
    private long userId = LoginInfo.getUserId();
    private static final int REQUEST_CODE_PICK_IMAGE = 100;
    private static final int REQUEST_CODE_CAPTURE_CAMERA = 101;
    private WaittingDialog dialog;
    private Uri mUri;

    @Bind(R.id.lv_cj)
    ListView lv_cj;
    @Bind(R.id.lv_qj)
    ListView lv_qj;
    @Bind(R.id.tv_tips)
    TextView tv_tips;
    @Bind(R.id.btn_create)
    Button btnCreate;
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
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        resetData();
        requestNet();
    }

    private void resetData() {
        curPage = 1;
        mQJList.clear();
        mSceneList.clear();
        ll_tips.setVisibility(View.GONE);
    }

    @Override
    protected void initView() {
        WindowUtils.showStatusBar(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            rl_head.setPadding(0, App.getStatusBarHeight(), 0, 0);
        }
        rl_head.getBackground().mutate().setAlpha(10);
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
        LinearLayout ll_btn_box = ButterKnife.findById(headView, R.id.ll_btn_box);
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
        mFilePath = FileCameraUtil.getFileDir() + File.separator;
    }


    @Override
    protected void requestNet() {
        resetData();
        if (userId <= 0) {
            LogUtil.e(TAG, "userId<=0");
            return;
        }
        HashMap<String, String> params = ClientDiscoverAPI.getgetMineInfoRequestParams(userId + "");
        HttpRequest.post(params, URL.MINE_INFO, new GlobalDataCallBack() {
            @Override
            public void onStart() {
                if (dialog != null && !activity.isFinishing()) dialog.show();
            }

            @Override
            public void onSuccess(String json) {
                if (!activity.isFinishing() && dialog != null) dialog.dismiss();

                try {
                    user = JsonUtil.fromJson(json, new TypeToken<HttpResponse<User>>() {
                    });
                    refreshUI();
                } catch (JsonSyntaxException e) {
                    LogUtil.e(TAG, e.getLocalizedMessage());
                }
            }

            @Override
            public void onFailure(String error) {
                if (!activity.isFinishing() && dialog != null) dialog.dismiss();
                ToastUtils.showError(R.string.network_err);
            }
        });

        switch (which) {
            case MineFragment.REQUEST_CJ:
                lv_qj.setVisibility(View.GONE);
                lv_cj.setVisibility(View.VISIBLE);
                loadCJData();
                break;
            default:
                break;
        }
    }

    /**
     * 加载情境数据
     */
    private void loadCJData() {
        HashMap<String, String> params = ClientDiscoverAPI.getSceneListRequestParams(String.valueOf(curPage), Constants.PAGE_SIZE, String.valueOf(userId), "1");
        HttpRequest.post(params, URL.SCENE_LIST, new GlobalDataCallBack() {
            @Override
            public void onStart() {
                if (dialog != null && !activity.isFinishing()) {
                    if (curPage == 1) dialog.show();
                }
                curPage++;
            }

            @Override
            public void onSuccess(String json) {
                if (!activity.isFinishing() && dialog != null) dialog.dismiss();
                HttpResponse response = JsonUtil.fromJson(json, HttpResponse.class);
                if (response.isSuccess()) {
                    UserCJListData listBean = JsonUtil.fromJson(json, new TypeToken<HttpResponse<UserCJListData>>() {
                    });
                    List list = listBean.rows;
                    refreshCJUI(list);
                    return;
                }
                ToastUtils.showError(response.getMessage());
            }

            @Override
            public void onFailure(String error) {
                if (!activity.isFinishing() && dialog != null) dialog.dismiss();
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

    /**
     * 更新headview的UI
     */
    @Override
    protected void refreshUI() {
        if (user == null) {
            return;
        }
        if (user.is_love == FansAdapter.NOT_LOVE) {
            setFocusBtnStyle(activity.getResources().getDimensionPixelSize(R.dimen.dp16), R.string.focus, R.mipmap.unfocus_white);
        } else {
            setFocusBtnStyle(activity.getResources().getDimensionPixelSize(R.dimen.dp10), R.string.focused, R.mipmap.focus_pic);
        }
        if (!TextUtils.isEmpty(user.nickname)) {
            tv_title.setText(user.nickname);
        }
        if (!TextUtils.isEmpty(user.medium_avatar_url)) {
            GlideUtils.displayImage(user.medium_avatar_url, riv);
        }
        GlideUtils.displayImage(user.head_pic_url, iv_bg);
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

        lv_cj.setOnScrollListener(new AbsListView.OnScrollListener() {
            private int headerHeight = 0;

            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
                if (i == SCROLL_STATE_IDLE || i == SCROLL_STATE_FLING) {
                    if (mSceneList.size() % 2 == 0) {
                        if (absListView.getLastVisiblePosition() == mSceneList.size() / 2) {
                            loadCJData();
                        }
                    } else {
                        if (absListView.getLastVisiblePosition() == mSceneList.size() / 2 + 1) {
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
                        rl_head.getBackground().mutate().setAlpha(10);
                    } else if (top > height) {
                        rl_head.getBackground().mutate().setAlpha(204);
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.tv_take_photo:
                PopupWindowUtil.dismiss();
                if (AndPermission.hasPermission(activity, Manifest.permission.CAMERA)) {
                    getImageFromCamera();
                } else {
                    AndPermission.with(this) .requestCode(REQUEST_PERMISSION_CODE).permission(Manifest.permission.CAMERA) .send(); // 申请权限。
                }
                break;
            case R.id.tv_album:
                PopupWindowUtil.dismiss();
                if (AndPermission.hasPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    getImageFromAlbum();
                } else {
                    AndPermission.with(this).requestCode(REQUEST_PERMISSION_CODE).permission(Manifest.permission.READ_EXTERNAL_STORAGE).send(); // 申请权限。
                }
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
                    HashMap<String, String> params = ClientDiscoverAPI.getfocusOperateRequestParams(userId + "");
                    HttpRequest.post(params, URL.FOCUS_OPRATE_URL, new GlobalDataCallBack() {
                        @Override
                        public void onSuccess(String json) {
                            bt_focus.setEnabled(true);
                            if (TextUtils.isEmpty(json)) return;
                            HttpResponse response = JsonUtil.fromJson(json, HttpResponse.class);
                            if (response.isSuccess()) {
                                user.is_love = FansAdapter.LOVE;
                                setFocusBtnStyle(activity.getResources().getDimensionPixelSize(R.dimen.dp10), R.string.focused, R.mipmap.focus_pic);
                                return;
                            }
                            ToastUtils.showError(response.getMessage());
                        }

                        @Override
                        public void onFailure(String error) {
                            bt_focus.setEnabled(true);
                            ToastUtils.showError(R.string.network_err);
                        }
                    });
                } else {
                    HashMap<String, String> params = ClientDiscoverAPI.getcancelFocusOperateRequestParams(userId + "");
                    HttpRequest.post(params, URL.CANCEL_FOCUS_URL, new GlobalDataCallBack() {
                        @Override
                        public void onSuccess(String json) {
                            bt_focus.setEnabled(true);
                            PopupWindowUtil.dismiss();
                            if (TextUtils.isEmpty(json)) return;
                            HttpResponse response = JsonUtil.fromJson(json, HttpResponse.class);
                            if (response.isSuccess()) {
                                user.is_love = FansAdapter.NOT_LOVE;
                                setFocusBtnStyle(activity.getResources().getDimensionPixelSize(R.dimen.dp16), R.string.focus, R.mipmap.unfocus_white);
                                return;
                            }
                            ToastUtils.showError(response.getMessage());
                        }

                        @Override
                        public void onFailure(String error) {
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
            case R.id.rl_qj:
                if (which == MineFragment.REQUEST_CJ) return;
                showCj();
                break;
            default:
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        // 只需要调用这一句，第一个参数是当前Acitivity/Fragment，回调方法写在当前Activity/Framgent。
        AndPermission.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    // 成功回调的方法，用注解即可，里面的数字是请求时的requestCode。
    @PermissionYes(Constants.REQUEST_PERMISSION_CODE)
    private void getRequestYes(List<String> grantedPermissions) {
        for (String item : grantedPermissions){
            if (item.contains("android.permission.READ_EXTERNAL_STORAGE")) {
                getImageFromAlbum();
            }
            if(item.contains("android.permission.CAMERA")) {
                getImageFromCamera();
            }
        }
    }

    // 失败回调的方法，用注解即可，里面的数字是请求时的requestCode。
    @PermissionNo(Constants.REQUEST_PERMISSION_CODE)
    private void getPhoneStatusNo(List<String> deniedPermissions) {
        // 用户否勾选了不再提示并且拒绝了权限，那么提示用户到设置中授权。
        if (AndPermission.hasAlwaysDeniedPermission(this, deniedPermissions)) {
            // 第一种：用默认的提示语。
            AndPermission.defaultSettingDialog(this, REQUEST_CODE_SETTING).show();
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
        if (!state.equals(Environment.MEDIA_MOUNTED)) {
            ToastUtils.showInfo("请插入SD卡");
            return;
        }
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        mUri = getUriForFile();
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mUri);
        startActivityForResult(intent, REQUEST_CODE_CAPTURE_CAMERA);
    }

    public Uri getUriForFile() {
        File path = new File(mFilePath);
        if (!path.exists()) {
            path.mkdirs();
        }
        String mFileName = "tmp.jpg";
        File file = new File(path, mFileName);
        if (file.exists()) {
            file.delete();
        }
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(getApplicationContext(), BuildConfig.APPLICATION_ID + ".fileProvider", file);
        } else {
            uri = Uri.fromFile(file);
        }
        return uri;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_PICK_IMAGE:
                    List<Uri> mSelected = PicturePickerUtils.obtainResult(data);
                    if (mSelected == null) return;
                    if (mSelected.size() == 0) return;
                    toCropActivity(mSelected.get(0));
                    break;
                case REQUEST_CODE_CAPTURE_CAMERA:
                    if (mUri != null) {
                        toCropActivity(mUri);
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
