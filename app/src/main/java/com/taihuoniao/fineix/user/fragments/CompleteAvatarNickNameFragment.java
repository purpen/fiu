package com.taihuoniao.fineix.user.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.album.ImageLoaderEngine;
import com.taihuoniao.fineix.album.Picker;
import com.taihuoniao.fineix.album.PicturePickerUtils;
import com.taihuoniao.fineix.beans.HttpResponse;
import com.taihuoniao.fineix.beans.LoginInfo;
import com.taihuoniao.fineix.beans.User;
import com.taihuoniao.fineix.beans.UserCompleteData;
import com.taihuoniao.fineix.main.fragment.MyBaseFragment;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.DataConstants;
import com.taihuoniao.fineix.user.CompleteUserInfoActivity;
import com.taihuoniao.fineix.user.ImageCropActivity;
import com.taihuoniao.fineix.utils.Constants;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.PopupWindowUtil;
import com.taihuoniao.fineix.utils.SPUtil;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.utils.Util;
import com.taihuoniao.fineix.view.roundImageView.RoundedImageView;

import java.io.File;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

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
    private UserCompleteData data;
    private static final int REQUEST_CODE_PICK_IMAGE = 100;
    private static final int REQUEST_CODE_CAPTURE_CAMERA = 101;
    public static final Uri imageUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "temp.jpg"));
    private String gender = Constants.MALE;
    private Bitmap bitmap;
    private DisplayImageOptions options;

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
            data = savedInstanceState.getParcelable("data");
        }
        super.onCreate(savedInstanceState);
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.default_background_750_1334)
                .showImageForEmptyUri(R.mipmap.default_background_750_1334)
                .showImageOnFail(R.mipmap.default_background_750_1334)
                .imageScaleType(ImageScaleType.EXACTLY)
                .cacheInMemory(false)
                .cacheOnDisk(false)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
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
        ImageLoader.getInstance().displayImage(LoginInfo.getHeadPicUrl(), riv, options);
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

                ClientDiscoverAPI.updateNickNameSex(nickName, gender, new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        if (TextUtils.isEmpty(responseInfo.result)) return;
                        HttpResponse<User> response = JsonUtil.json2Bean(responseInfo.result, new TypeToken<HttpResponse<User>>() {
                        });
                        if (response.isSuccess()) {
//                            User user = response.getData();
                            LoginInfo loginInfo = LoginInfo.getLoginInfo();
                            loginInfo.setSex(gender);
                            loginInfo.setNickname(nickName);
                            SPUtil.write(DataConstants.LOGIN_INFO, JsonUtil.toJson(loginInfo));
                            if (activity instanceof CompleteUserInfoActivity) {
                                ViewPager viewPager = ((CompleteUserInfoActivity) activity).getViewPager();
                                if (null != viewPager) viewPager.setCurrentItem(1);
                            }
                            return;
                        }
                        ToastUtils.showError(response.getMessage());
                    }

                    @Override
                    public void onFailure(HttpException e, String s) {
                        e.printStackTrace();
                        ToastUtils.showError(R.string.network_err);
                    }
                });

                break;
            case R.id.riv:
                PopupWindowUtil.show(activity, initPopView(R.layout.popup_upload_avatar, "上传头像"));
                break;
        }
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
                    getImageFromCamera();
                    break;
                case R.id.tv_album:
                    PopupWindowUtil.dismiss();
                    getImageFromAlbum();
                    break;
                case R.id.tv_cancel:
                default:
                    PopupWindowUtil.dismiss();
                    break;
            }
        }
    };

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

    protected void getImageFromAlbum() {
        Picker.from(this)
                .count(1)
                .enableCamera(false)
                .singleChoice()
                .setEngine(new ImageLoaderEngine())
                .forResult(REQUEST_CODE_PICK_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_PICK_IMAGE:
                    List<Uri> mSelected = PicturePickerUtils.obtainResult(data);
                    if (mSelected == null) return;
                    if (mSelected.size() == 0) return;
                    toCropActivity(mSelected.get(0));
                    break;
                case REQUEST_CODE_CAPTURE_CAMERA:
                    if (imageUri != null) {
                        toCropActivity(imageUri);
                    }
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
