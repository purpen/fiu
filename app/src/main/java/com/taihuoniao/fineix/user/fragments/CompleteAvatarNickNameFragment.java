package com.taihuoniao.fineix.user.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.album.PicturePickerUtils;
import com.taihuoniao.fineix.base.HttpRequest;
import com.taihuoniao.fineix.beans.HttpResponse;
import com.taihuoniao.fineix.beans.LoginInfo;
import com.taihuoniao.fineix.beans.User;
import com.taihuoniao.fineix.beans.UserCompleteData;
import com.taihuoniao.fineix.common.GlobalDataCallBack;
import com.taihuoniao.fineix.main.MainActivity;
import com.taihuoniao.fineix.main.fragment.MyBaseFragment;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.DataConstants;
import com.taihuoniao.fineix.network.URL;
import com.taihuoniao.fineix.personal.AllianceRequstDeal;
import com.taihuoniao.fineix.user.CompleteUserInfoActivity;
import com.taihuoniao.fineix.user.ImageCropActivity;
import com.taihuoniao.fineix.utils.Constants;
import com.taihuoniao.fineix.utils.GlideUtils;
import com.taihuoniao.fineix.utils.ImageUtils;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.LogUtil;
import com.taihuoniao.fineix.utils.PopupWindowUtil;
import com.taihuoniao.fineix.utils.SPUtil;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.utils.Util;
import com.taihuoniao.fineix.view.roundImageView.RoundedImageView;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionNo;
import com.yanzhenjie.permission.PermissionYes;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.taihuoniao.fineix.utils.Constants.REQUEST_CODE_SETTING;

/**
 * @author lilin
 *         created at 2016/8/10 17:24
 */
public class CompleteAvatarNickNameFragment extends MyBaseFragment {
    @Bind(R.id.riv)
    RoundedImageView riv;
    @Bind(R.id.rl)
    RelativeLayout rl;
    @Bind(R.id.et_nickname)
    EditText etNickname;
    @Bind(R.id.rg)
    RadioGroup rg;
    private File mCurrentPhotoFile;
    private String gender = Constants.MALE;
    private Bitmap bitmap;

    public static CompleteAvatarNickNameFragment newInstance(UserCompleteData data) {
        CompleteAvatarNickNameFragment fragment = new CompleteAvatarNickNameFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("data", data);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            UserCompleteData data = savedInstanceState.getParcelable("data");
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.setFragmentLayout(R.layout.fragment_complete_avatar_nickname);
        super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void initViews() {
        GlideUtils.displayImage(LoginInfo.getHeadPicUrl(), riv);
        String nickName = LoginInfo.getNickName();
        if (!TextUtils.isEmpty(nickName)) {
            etNickname.setText(nickName);
            etNickname.setSelection(nickName.length());
        }

        String gender = LoginInfo.getGender();
        if (!TextUtils.isEmpty(gender)) {
            if (TextUtils.equals(Constants.SECRET, gender)) {
                rg.check(R.id.rb_secret);
            } else if (TextUtils.equals(Constants.MALE, gender)) {
                rg.check(R.id.rb_man);
            } else if (TextUtils.equals(Constants.FEMALE, gender)) {
                rg.check(R.id.rb_woman);
            }
        }
    }

    @Override
    protected void installListener() {
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_man:
                        rg.check(checkedId);
                        gender = Constants.MALE;
                        break;
                    case R.id.rb_woman:
                        rg.check(checkedId);
                        gender = Constants.FEMALE;
                        break;
                    case R.id.rb_secret:
                        rg.check(checkedId);
                        gender = Constants.SECRET;
                        break;
                }
            }
        });
    }

    @OnClick({R.id.btn_next, R.id.riv})
    void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_next:
                //更新用户信息
                if (TextUtils.isEmpty(LoginInfo.getHeadPicUrl())) {
                    ToastUtils.showInfo("您还没有上传头像哦");
                    return;
                }
                final String nickName = etNickname.getText().toString().trim();
                if (TextUtils.isEmpty(nickName)) {
                    ToastUtils.showInfo("昵称不能为空");
                }
                HashMap<String, String> params = ClientDiscoverAPI.getupdateNickNameSexRequestParams(nickName, gender);
                HttpRequest.post(params, URL.UPDATE_USERINFO_URL, new GlobalDataCallBack(){
                    @Override
                    public void onSuccess(String json) {
                        HttpResponse<User> response = JsonUtil.json2Bean(json, new TypeToken<HttpResponse<User>>() {
                        });
                        if (response.isSuccess()) {
                            LoginInfo loginInfo = LoginInfo.getLoginInfo();
                            if (loginInfo != null) {
                                loginInfo.setSex(gender);
                                loginInfo.setNickname(nickName);
                                SPUtil.write(DataConstants.LOGIN_INFO, JsonUtil.toJson(loginInfo));
                                AllianceRequstDeal.requestAllianceIdentify(null);
                            }
//                            if (activity instanceof CompleteUserInfoActivity) {
//                                ViewPager viewPager = ((CompleteUserInfoActivity) activity).getViewPager();
//                                if (null != viewPager) viewPager.setCurrentItem(1);
//                            }
                            updateUserIdentity();

                            return;
                        }
                        ToastUtils.showError(response.getMessage());
                    }

                    @Override
                    public void onFailure(String error) {
                        ToastUtils.showError(R.string.network_err);
                    }
                });

                break;
            case R.id.riv:
                PopupWindowUtil.show(activity, initPopView(R.layout.popup_upload_avatar, "上传头像"));
                break;
        }
    }

    private void updateUserIdentity() {
        String type = "1";//设置非首次登录
        HashMap<String, String> params = ClientDiscoverAPI.getupdateUserIdentifyRequestParams(type);
        HttpRequest.post(params,  URL.UPDATE_USER_IDENTIFY, new GlobalDataCallBack(){
            @Override
            public void onSuccess(String json) {
                HttpResponse response = JsonUtil.fromJson(json, HttpResponse.class);
                if (response.isSuccess()) {
                    startActivity(new Intent(activity, MainActivity.class));
                    activity.finish();
                    return;
                }
                LogUtil.e("改为非首次登录失败", json + "===" + response.getMessage());
            }

            @Override
            public void onFailure(String error) {
                ToastUtils.showError(R.string.network_err);
                startActivity(new Intent(activity, MainActivity.class));
                activity.finish();
            }
        });
    }

    private View initPopView(int layout, String title) {
        View view = Util.inflateView(activity, layout, null);
        ((TextView) view.findViewById(R.id.tv_title)).setText(title);
        View iv_take_photo = view.findViewById(R.id.tv_take_photo);
        View iv_take_album = view.findViewById(R.id.tv_album);
        View iv_close = view.findViewById(R.id.tv_cancel);
        iv_take_photo.setOnClickListener(onClickListener);
        iv_take_album.setOnClickListener(onClickListener);
        iv_close.setOnClickListener(onClickListener);
        return view;
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_take_photo:
                    PopupWindowUtil.dismiss();
                    if (AndPermission.hasPermission(activity, Manifest.permission.CAMERA)) {
                        mCurrentPhotoFile = ImageUtils.getDefaultFile();
                        if (null==mCurrentPhotoFile) return;
                        ImageUtils.getImageFromCamera(activity, ImageUtils.getUriForFile(mCurrentPhotoFile));
                    } else {
                        // 申请权限。
                        AndPermission.with(getActivity())
                                .requestCode(Constants.REQUEST_PERMISSION_CODE)
                                .permission(Manifest.permission.CAMERA)
                                .send();
                    }
                    break;
                case R.id.tv_album:
                    PopupWindowUtil.dismiss();
                    if (AndPermission.hasPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        ImageUtils.getImageFromAlbum(activity,1);
                    } else {
                        // 申请权限。
                        AndPermission.with(getActivity())
                                .requestCode(Constants.REQUEST_PERMISSION_CODE)
                                .permission(Manifest.permission.READ_EXTERNAL_STORAGE)
                                .send();
                    }
                    break;
                case R.id.tv_cancel:
                default:
                    PopupWindowUtil.dismiss();
                    break;
            }
        }
    };


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
                ImageUtils.getImageFromAlbum(activity,1);
            }
            if(item.contains("android.permission.CAMERA")) {
                mCurrentPhotoFile = ImageUtils.getDefaultFile();
                if (null==mCurrentPhotoFile) return;
                ImageUtils.getImageFromCamera(activity, ImageUtils.getUriForFile(mCurrentPhotoFile));
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


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case Constants.REQUEST_CODE_PICK_IMAGE:
                    List<Uri> mSelected = PicturePickerUtils.obtainResult(data);
                    if (mSelected == null) return;
                    if (mSelected.size() == 0) return;
                    toCropActivity(mSelected.get(0));
                    break;
                case Constants.REQUEST_CODE_CAPTURE_CAMERA:
                    if (null==mCurrentPhotoFile) return;
                    toCropActivity(ImageUtils.getUriForFile(mCurrentPhotoFile));
                    break;
            }
        }
    }

    private void toCropActivity(Uri uri) {
        ImageCropActivity.setOnClipCompleteListener(new ImageCropActivity.OnClipCompleteListener() {
            @Override
            public void onClipComplete(Bitmap bitmap) {
                CompleteAvatarNickNameFragment.this.bitmap = bitmap;
                riv.setImageBitmap(bitmap);
            }
        });
        Intent intent = new Intent(activity, ImageCropActivity.class);
        intent.putExtra(ImageCropActivity.class.getSimpleName(), uri);
        intent.putExtra(ImageCropActivity.class.getName(), CompleteUserInfoActivity.class.getSimpleName());
        startActivity(intent);
    }
    @Override
    public void onDestroy() {
        if (bitmap != null) bitmap.recycle();
        super.onDestroy();
    }
}
