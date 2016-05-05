package com.taihuoniao.fineix.user;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.mob.tools.network.BufferedByteArrayOutputStream;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.CropOptionAdapter;
import com.taihuoniao.fineix.adapters.FocusFansAdapter;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.beans.CropOption;
import com.taihuoniao.fineix.beans.ImgUploadBean;
import com.taihuoniao.fineix.beans.LoginInfo;
import com.taihuoniao.fineix.beans.User;
import com.taihuoniao.fineix.main.fragment.FindFragment;
import com.taihuoniao.fineix.main.fragment.IndexFragment;
import com.taihuoniao.fineix.main.fragment.MineFragment;
import com.taihuoniao.fineix.main.fragment.WellGoodsFragment;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.HttpResponse;
import com.taihuoniao.fineix.user.fragments.UserCJFragment;
import com.taihuoniao.fineix.user.fragments.UserFansFragment;
import com.taihuoniao.fineix.user.fragments.UserFocusFragment;
import com.taihuoniao.fineix.user.fragments.UserQJFragment;
import com.taihuoniao.fineix.utils.Base64Utils;
import com.taihuoniao.fineix.utils.ImageUtils;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.LogUtil;
import com.taihuoniao.fineix.utils.PopupWindowUtil;
import com.taihuoniao.fineix.utils.Util;
import com.taihuoniao.fineix.view.WaittingDialog;
import com.taihuoniao.fineix.view.imageViewTouch.easing.Linear;
import com.taihuoniao.fineix.view.roundImageView.RoundedImageView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @author lilin
 *         created at 2016/4/26 17:43
 */
public class UserCenterActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.ll_box)
    LinearLayout ll_box;
    @Bind(R.id.ll_btn_box)
    LinearLayout ll_btn_box;
    @Bind(R.id.tv_title)
    TextView tv_title;
    @Bind(R.id.riv)
    RoundedImageView riv;
    @Bind(R.id.tv_real)
    TextView tv_real;
    @Bind(R.id.tv_nick)
    TextView tv_nick;
    @Bind(R.id.tv_rank)
    TextView tv_rank;
    @Bind(R.id.tv_qj)
    TextView tv_qj;
    @Bind(R.id.tv_cj)
    TextView tv_cj;
    @Bind(R.id.tv_focus)
    TextView tv_focus;
    @Bind(R.id.tv_fans)
    TextView tv_fans;
    @Bind(R.id.bt_focus)
    Button bt_focus;
    private User user;
    private Uri mImageUri;
    private FragmentManager fm;
    private ArrayList<Fragment> fragments;
    private Fragment showFragment;
    private int which;
    private long userId = LoginInfo.getUserId();
    private static final int PICK_FROM_FILE = 0;
    private static final int PICK_FROM_CAMERA = 1;
    private static final int CROP_FROM_CAMERA = 2;
    private WaittingDialog dialog;
    public UserCenterActivity() {
        super(R.layout.activity_user_center);
    }

    @Override
    protected void getIntentData() {
        Intent intent = getIntent();
        if (intent.hasExtra(MineFragment.class.getSimpleName())) {
            which = intent.getIntExtra(MineFragment.class.getSimpleName(), 0);
        }

        if (intent.hasExtra(FocusFansActivity.USER_ID_EXTRA)) {
            userId = intent.getLongExtra(FocusFansActivity.USER_ID_EXTRA,LoginInfo.getUserId());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (fragments == null) {
            fragments = new ArrayList<>();
        }
        fm = getSupportFragmentManager();
        if (savedInstanceState != null) {
            recoverAllState(savedInstanceState);
        } else {
            switch (which) {
                case MineFragment.REQUEST_QJ:
                    switchFragmentandImg(UserQJFragment.class);
                    break;
                case MineFragment.REQUEST_CJ:
                    switchFragmentandImg(UserCJFragment.class);
                    break;
                case MineFragment.REQUEST_FOCUS:
                    switchFragmentandImg(UserFocusFragment.class);
                    break;
                case MineFragment.REQUEST_FANS:
                    switchFragmentandImg(UserFansFragment.class);
                    break;
                default:
                    break;
            }
        }

    }


    @Override
    protected void initView() {
        dialog=new WaittingDialog(this);

        if (userId==LoginInfo.getUserId()){
            ll_btn_box.setVisibility(View.GONE);
        }else {
            ll_btn_box.setVisibility(View.VISIBLE);
        }
    }

    private void recoverAllState(Bundle savedInstanceState) {
        Fragment userQJFragment = fm.getFragment(savedInstanceState, UserQJFragment.class.getSimpleName());
        addFragment2List(userQJFragment);
        Fragment userCJFragment = fm.getFragment(savedInstanceState, UserCJFragment.class.getSimpleName());
        addFragment2List(userCJFragment);
        Fragment userfocusFragment = fm.getFragment(savedInstanceState, UserFocusFragment.class.getSimpleName());
        addFragment2List(userfocusFragment);
        Fragment userFansFragment = fm.getFragment(savedInstanceState, UserFansFragment.class.getSimpleName());
        addFragment2List(userFansFragment);
        Class clazz = (Class) savedInstanceState.getSerializable(getClass().getSimpleName());
        if (clazz == null) return;
        switchFragmentandImg(clazz);
    }


    private void addFragment2List(Fragment fragment) {
        if (fragment == null) {
            return;
        }

        if (fragments.contains(fragment)) {
            return;
        }

        fragments.add(fragment);
    }

    private void switchFragmentandImg(Class clazz) {
        resetUI();
        try {
            switchFragment(clazz);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 切换fragment
     *
     * @param clazz
     * @throws Exception
     */
    private void switchFragment(Class clazz) throws Exception {
        Fragment fragment = fm.findFragmentByTag(clazz.getSimpleName());
        if (fragment == null) {
            fragment = (Fragment) clazz.newInstance();
            fm.beginTransaction().add(R.id.fl_box, fragment, clazz.getSimpleName()).commit();
        } else {
            fm.beginTransaction().show(fragment).commit();
        }
        addFragment2List(fragment);
        showFragment = fragment;
    }

    private void resetUI() {
        if (fragments == null) {
            return;
        }
        if (fragments.size() == 0) {
            return;
        }

        for (Fragment fragment : fragments) {
            fm.beginTransaction().hide(fragment).commit();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        int size = fragments.size();
        if (fragments != null && size > 0) {
            for (int i = 0; i < size; i++) {
                fm.putFragment(outState, fragments.get(i).getTag(), fragments.get(i));
            }
        }
        if (showFragment != null) {
            outState.putSerializable(getClass().getSimpleName(), showFragment.getClass());
        }
        super.onSaveInstanceState(outState);
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
    }

    @Override
    protected void refreshUI() {
        if (user == null) {
            return;
        }
        LogUtil.e(TAG, "refreshUI===" + user._id);
        if (user.is_love==FocusFansAdapter.NOT_LOVE){
            bt_focus.setText("关注");
        }else{
            bt_focus.setText("已关注");
        }
        if (TextUtils.isEmpty(user.nickname)){
            tv_title.setText(user.nickname);
        }
        if (!TextUtils.isEmpty(user.medium_avatar_url)) {
            ImageLoader.getInstance().displayImage(user.medium_avatar_url, riv, options);
        }
        if (!TextUtils.isEmpty(user.head_pic_url)) {
            ImageUtils.loadBgImg(user.head_pic_url, ll_box);
        }
        if (TextUtils.isEmpty(user.realname)) {
            tv_real.setVisibility(View.GONE);
        } else {
            tv_real.setText(user.realname);
        }

        if (TextUtils.isEmpty(user.nickname)) {
            tv_nick.setVisibility(View.GONE);
        } else {
            tv_nick.setText(user.nickname);
        }

        if (TextUtils.isEmpty(user.rank_title)) {
            tv_rank.setVisibility(View.GONE);
        } else {
            tv_rank.setText(String.format("%s | V%s",user.rank_title,user.rank_id));
        }
        tv_qj.setText(String.valueOf(user.scene_count));
        tv_cj.setText(String.valueOf(user.sight_count));
        tv_focus.setText(String.valueOf(user.follow_count));
        tv_fans.setText(String.valueOf(user.fans_count));
    }

    @OnClick({R.id.ll_box, R.id.ll_qj, R.id.ll_cj, R.id.ll_focus, R.id.ll_fans, R.id.iv_right, R.id.iv_detail, R.id.bt_focus, R.id.bt_msg})
    void performClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.iv_detail:
                finish();
                break;
            case R.id.iv_right:
                startActivity(new Intent(activity, EditUserInfoActivity.class));
                break;
            case R.id.bt_focus:
                bt_focus.setEnabled(false);
                if (user.is_love == FocusFansAdapter.NOT_LOVE){
                    ClientDiscoverAPI.focusOperate(userId + "", new RequestCallBack<String>() {
                        @Override
                        public void onSuccess(ResponseInfo<String> responseInfo) {
                            bt_focus.setEnabled(true);
                            if (responseInfo == null) return;
                            if (TextUtils.isEmpty(responseInfo.result)) return;
                            LogUtil.e("focusOperate",responseInfo.result);
                            HttpResponse response = JsonUtil.fromJson(responseInfo.result, HttpResponse.class);
                            if (response.isSuccess()) {
                                user.is_love=FocusFansAdapter.LOVE;
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
                }else {
                    ClientDiscoverAPI.cancelFocusOperate(userId+"", new RequestCallBack<String>() {
                        @Override
                        public void onSuccess(ResponseInfo<String> responseInfo) {
                            bt_focus.setEnabled(true);
                            PopupWindowUtil.dismiss();
                            if (responseInfo==null) return;
                            if (TextUtils.isEmpty(responseInfo.result)) return;
                            LogUtil.e("cancelFocusOperate",responseInfo.result);
                            HttpResponse response = JsonUtil.fromJson(responseInfo.result, HttpResponse.class);
                            if (response.isSuccess()){
                                user.is_love=FocusFansAdapter.NOT_LOVE;
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
            case R.id.bt_msg:
                Util.makeToast("私信操作");
                break;
            case R.id.ll_qj:
                switchFragmentandImg(UserQJFragment.class);
                break;
            case R.id.ll_cj:
                switchFragmentandImg(UserCJFragment.class);
                break;
            case R.id.ll_focus:
//                switchFragmentandImg(UserFocusFragment.class);
                intent = new Intent(activity, FocusFansActivity.class);
                intent.putExtra(FocusFansActivity.class.getSimpleName(), FocusFansActivity.FOCUS_TYPE);
                intent.putExtra(FocusFansActivity.USER_ID_EXTRA, userId);
                startActivity(intent);
                break;
            case R.id.ll_fans:
//                switchFragmentandImg(UserFansFragment.class);
                intent = new Intent(activity, FocusFansActivity.class);
                intent.putExtra(FocusFansActivity.class.getSimpleName(), FocusFansActivity.FANS_TYPE);
                intent.putExtra(FocusFansActivity.USER_ID_EXTRA, userId);
                startActivity(intent);
                break;

            case R.id.ll_box:
                PopupWindowUtil.show(activity, initPopView(R.layout.popup_upload_avatar));
                break;
        }
    }

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
        }
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
                    LogUtil.e(TAG,responseInfo.result);
                    HttpResponse response = JsonUtil.fromJson(responseInfo.result, HttpResponse.class);
                    if (response.isSuccess()) {
                        ImgUploadBean imgUploadBean = JsonUtil.fromJson(responseInfo.result, new TypeToken<HttpResponse<ImgUploadBean>>() {
                        });
                        if (!TextUtils.isEmpty(imgUploadBean.head_pic_url)) {
                            ImageUtils.loadBgImg(imgUploadBean.head_pic_url,ll_box);
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
