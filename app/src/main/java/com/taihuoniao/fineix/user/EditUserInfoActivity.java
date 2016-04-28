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
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.CropOptionAdapter;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.beans.CropOption;
import com.taihuoniao.fineix.beans.ProvinceCityData;
import com.taihuoniao.fineix.beans.User;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.HttpResponse;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.LogUtil;
import com.taihuoniao.fineix.utils.PopupWindowUtil;
import com.taihuoniao.fineix.utils.ProvinceUtil;
import com.taihuoniao.fineix.utils.Util;
import com.taihuoniao.fineix.view.CustomAddressSelectView;
import com.taihuoniao.fineix.view.CustomBirthdaySelectView;
import com.taihuoniao.fineix.view.CustomHeadView;
import com.taihuoniao.fineix.view.CustomItemLayout;
//import com.zcjcn.beans.HttpResponse;
//import com.zcjcn.beans.UserLogin;
//import com.zcjcn.http.HttpRequestData;
//import com.zcjcn.interfaces.ICallback4Http;
//import com.zcjcn.ui.customview.CustomBirthdaySelectView;
//import com.zcjcn.ui.wheelview.StringWheelAdapter;
//import com.zcjcn.ui.wheelview.WheelView;
//import com.zcjcn.utils.LogUtil;
//import com.zcjcn.utils.LoginUtil;
//import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.http.RequestParams;
import com.taihuoniao.fineix.view.wheelview.StringWheelAdapter;
import com.taihuoniao.fineix.view.wheelview.WheelView;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author lilin
 *         created at 2016/4/26 18:50
 */
public class EditUserInfoActivity extends BaseActivity {
    @Bind(R.id.head_view)
    CustomHeadView head_view;
    @Bind(R.id.custom_user_avatar)
    CustomItemLayout custom_user_avatar;
    @Bind(R.id.custom_nick_name)
    CustomItemLayout custom_nick_name;
    @Bind(R.id.custom_signature)

    CustomItemLayout custom_signature;
    @Bind(R.id.custom_area)
    CustomItemLayout custom_area;

    @Bind(R.id.custom_user_sex)
    CustomItemLayout custom_user_sex;
    @Bind(R.id.custom_user_birthday)
    CustomItemLayout custom_user_birthday;

    @Bind(R.id.custom_code)
    CustomItemLayout custom_code;

    @Bind(R.id.custom_auth)
    CustomItemLayout custom_auth;
    private User user;
    //    private UserLogin userLogin;
    private static final int PICK_FROM_FILE = 0;
    private static final int PICK_FROM_CAMERA = 1;
    private static final int CROP_FROM_CAMERA = 2;
    private static final int REQUEST_NICK_NAME = 3;
    private Uri mImageUri;
    private static final int SECRET = 0;
    private static final int MAN = 1;
    private static final int WOMAN = 2;
    private String key;
    private String value;
    public static boolean isSubmitAddress=false;
    public EditUserInfoActivity() {
        super(R.layout.activity_user_info_layout);
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        head_view.setHeadCenterTxtShow(true, R.string.title_user_data);
        custom_user_avatar.setHeight(R.dimen.dp80);
        custom_user_avatar.setUserAvatar(null);
        custom_user_avatar.setTVStyle(0, R.string.user_avatar, R.color.color_333, false);
        custom_nick_name.setTVStyle(0, R.string.nick_name, R.color.color_333, false);
        custom_signature.setTVStyle(0, R.string.user_signature, R.color.color_333, false);
        custom_signature.setTvArrowLeftStyle(true, R.string.input_signature);
        custom_user_sex.setTVStyle(0, R.string.user_sex, R.color.color_333, false);
        custom_user_sex.setTvArrowLeftStyle(true, R.string.select_gender);
        custom_user_birthday.setTVStyle(0, R.string.user_birthday, R.color.color_333, false);
        custom_user_birthday.setTvArrowLeftStyle(true, R.string.select_birth);
        custom_area.setTVStyle(0, R.string.user_area, R.color.color_333, false);
        custom_area.setTvArrowLeftStyle(true, R.string.select_city);
        custom_code.setTVStyle(0, R.string.user_code, R.color.color_333, false);
        custom_auth.setTVStyle(0, R.string.user_auth, R.color.color_333, false);
    }

    @OnClick({R.id.custom_nick_name, R.id.custom_user_avatar,R.id.custom_area,R.id.custom_signature, R.id.custom_auth, R.id.custom_user_sex, R.id.custom_user_birthday, R.id.custom_code})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.custom_user_avatar:
                PopupWindowUtil.show(activity, initPopView(R.layout.popup_upload_avatar));
                break;
            case R.id.custom_nick_name:
                Intent intent = new Intent(activity, UserEditNameActivity.class);
                intent.putExtra(User.class.getSimpleName(),user);
                startActivityForResult(intent, REQUEST_NICK_NAME);
                break;
            case R.id.custom_signature:
                //TODO 编辑个性签名
                break;
            case R.id.custom_area:
                PopupWindowUtil.show(activity, initAddressPopView(R.layout.popup_address_layout, R.string.select_address,ProvinceUtil.getProvinces()));
                //TODO 选择区域
                break;
            case R.id.custom_user_birthday:
                PopupWindowUtil.show(activity, initPopView(R.layout.popup_birth_layout, R.string.select_birth));
                break;
            case R.id.custom_user_sex:
                PopupWindowUtil.show(activity, initPopView(R.layout.popup_gender_layout, R.string.select_gender, Arrays.asList(getResources().getStringArray(R.array.user_gender))));
                break;
            case R.id.custom_code:
                startActivity(new Intent(activity, MyBarCodeActivity.class));
                break;
            case R.id.custom_auth:
                //TODO 申请认证
                break;
        }
    }

    private View initPopView(int layout, int resId, List<String> list) {
        View view = Util.inflateView(this, layout, null);
        View tv_cancel_select = view.findViewById(R.id.tv_cancel_select);
        View tv_confirm_select = view.findViewById(R.id.tv_confirm_select);
        TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
        WheelView wv_province = (WheelView) view.findViewById(R.id.custom_wv);
        wv_province.setAdapter(new StringWheelAdapter(list));
        wv_province.setVisibleItems(5);
        tv_title.setText(resId);
        setClickListener(tv_cancel_select, resId,wv_province, list);
        setClickListener(tv_confirm_select, resId,wv_province, list);
        return view;
    }

    private View initAddressPopView(int layout, int resId, List<String> list) {
        View view = Util.inflateView(this, layout, null);
        View tv_cancel_select = view.findViewById(R.id.tv_cancel_select);
        View tv_confirm_select = view.findViewById(R.id.tv_confirm_select);
        TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
        CustomAddressSelectView addressView = (CustomAddressSelectView)view.findViewById(R.id.custom_address_select);
//        wv_province.setAdapter(new StringWheelAdapter(list));
//        wv_province.setVisibleItems(5);
        tv_title.setText(resId);
        setClickListener(tv_cancel_select,addressView);
        setClickListener(tv_confirm_select,addressView);
        return view;
    }

    private View initPopView(int layout) {
        View view = Util.inflateView(this, layout, null);
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
            Intent intent = null;
            switch (v.getId()) {
                case R.id.tv_take_photo:
                    PopupWindowUtil.dismiss();
                    intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    mImageUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(),
                            "tmp_avatar_" + String.valueOf(System.currentTimeMillis()) + ".jpg"));

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
                default:
                    PopupWindowUtil.dismiss();
                    break;
            }
        }
    };
    @Override
    protected void requestNet() {
        ProvinceUtil.init();
        ClientDiscoverAPI.getMineInfo(new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
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
                LogUtil.e(TAG, s);
                Util.makeToast("对不起,网络请求失败");
            }
        });
    }

    protected void loadActivityData() {
//        hashMap.clear();
//        hashMap.put("Action", "GetUserInfo");
//        hashMap.put("uid", LoginUtil.getUserId());
//        HttpRequestData.sendPostRequest(Constants.APP_URI, hashMap, new ICallback4Http() {
//            @Override
//            public void onResponse(String response) {
//                LogUtil.e(tag, response);
//                userLogin = JsonUtil.fromJson(response, new TypeToken<HttpResponse<UserLogin>>() {});
//                String locaStr=SPUtil.read(activity,Constants.LOGIN_INFO);
//                if (!TextUtils.isEmpty(locaStr)){
//                    UserLogin localInfo = JsonUtil.fromJson(locaStr,UserLogin.class);
//                    localInfo.user_name=userLogin.user_name;
//                    localInfo.nickname=userLogin.nickname;
//                    localInfo.header=userLogin.header;
//                    SPUtil.write(activity,Constants.LOGIN_INFO,JsonUtil.toJson(localInfo));
//                }
//                refreshUIAfterNet();
//            }
//
//            @Override
//            public void onFailure(String errorMessage) {
//                Util.makeToast(activity, errorMessage);
//            }
//        });
    }


    private View initPopView(int layout, int resId) {
        View view = Util.inflateView(this, layout, null);
        View tv_cancel_select = view.findViewById(R.id.tv_cancel_select);
        View tv_confirm_select = view.findViewById(R.id.tv_confirm_select);
        TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
        CustomBirthdaySelectView custom_birth_select = (CustomBirthdaySelectView) view.findViewById(R.id.custom_birth_select);
        tv_title.setText(resId);
        setClickListener(tv_confirm_select, custom_birth_select);
        setClickListener(tv_cancel_select, custom_birth_select);
        return view;
    }

    /**
     * 确认选择地址
     * @param view
     * @param casv
     */
    private void setClickListener(View view, final CustomAddressSelectView casv) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.tv_confirm_select:
                        isSubmitAddress=true;
                        String addr=casv.getAddress();
//                        LogUtil.e(TAG,addr.split("\\s")[0]);
//                        LogUtil.e(TAG,addr.split("\\s")[1]);
                        key=ProvinceUtil.getProvinceIdByName(addr.split("\\s")[0])+"";
                        value=ProvinceUtil.getCityIdByName(addr.split("\\s")[1])+"";
                        submitData();
                        custom_area.setTvArrowLeftStyle(true,addr, R.color.color_333);
                    case R.id.tv_cancel_select:
                    default:
                        PopupWindowUtil.dismiss();
                        break;
                }
            }
        });
    }


    private void setClickListener(View view, final CustomBirthdaySelectView cbsv) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.tv_confirm_select:
                        isSubmitAddress=false;
                        key = "birthday";
                        value = cbsv.getBithday();
                        submitData();
                        custom_user_birthday.setTvArrowLeftStyle(true, cbsv.getBithday(), R.color.color_333);
                    case R.id.tv_cancel_select:
                    default:
                        PopupWindowUtil.dismiss();
                        break;
                }
            }
        });
    }


//    private View initPopView(int layout, int resId, List<String> list) {
//        View view = Util.inflateView(this, layout, null);
//        View tv_cancel_select = view.findViewById(R.id.tv_cancel_select);
//        View tv_confirm_select = view.findViewById(R.id.tv_confirm_select);
//        TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
//        WheelView custom_wv = (WheelView) view.findViewById(R.id.custom_wv);
//        custom_wv.setAdapter(new StringWheelAdapter(list));
//        custom_wv.setVisibleItems(5);
//        tv_title.setText(resId);
//        setClickListener(tv_cancel_select, resId, custom_wv, list);
//        setClickListener(tv_confirm_select, resId, custom_wv, list);
//        return view;
//    }

    private void setClickListener(View view, final int id, final WheelView wheelView, final List<String> list) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.tv_cancel_select:
                        PopupWindowUtil.dismiss();
                        break;
                    case R.id.tv_confirm_select:
                        switch (id) {
                            case R.string.select_gender:
                                isSubmitAddress=false;
                                String sex = list.get(wheelView.getCurrentItem());
                                key = "sex";
                                if (TextUtils.equals("保密",sex)) {
                                    value = String.valueOf(SECRET);
                                } else if (TextUtils.equals("男", sex)) {
                                    value = String.valueOf(MAN);
                                } else {
                                    value = String.valueOf(WOMAN);
                                }
                                submitData();
                                custom_user_sex.setTvArrowLeftStyle(true,sex, R.color.color_333);
                                break;
                        }
                        PopupWindowUtil.dismiss();
                        break;
                    default:
                        PopupWindowUtil.dismiss();
                        break;
                }
            }
        });
    }

    protected void submitData() {
        ClientDiscoverAPI.updateUserInfo(key,value, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                if (responseInfo==null){
                    return;
                }

                if (TextUtils.isEmpty(responseInfo.result)){
                    return;
                }

                HttpResponse response = JsonUtil.fromJson(responseInfo.result, HttpResponse.class);

                if (response.isSuccess()){
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
//        hashMap.clear();
//        hashMap.put("Action", "EditUserInfo");
//        hashMap.put("uid", LoginUtil.getUserId());
//        hashMap.put("key", key);
//        hashMap.put("data", data);
//        HttpRequestData.sendPostRequest(Constants.APP_URI, hashMap, new ICallback4Http() {
//            @Override
//            public void onResponse(String response) {
//                loadActivityData();
//                LogUtil.e(tag, response);
//                HttpResponse httpResponse = JsonUtil.fromJson(response, HttpResponse.class);
//                Util.makeToast(activity, httpResponse.getMessage());
//            }
//
//            @Override
//            public void onFailure(String errorMessage) {
//                Util.makeToast(activity, errorMessage);
//            }
//        });
    }

    @Override
    protected void installListener() {
    }

    @Override
    protected void refreshUI() {
        if (user != null) {
            if (!TextUtils.isEmpty(user.avatar)) {
                ImageLoader.getInstance().displayImage(user.avatar, custom_user_avatar.getAvatarIV(), options);
            }
//            custom_user_name.setTvArrowLeftStyle(true, userLogin.user_name, R.color.color_333);
//            custom_user_name.sertTVRightTxt(userLogin.user_name);
//            custom_user_name.setRightMoreImgStyle(false);
            if (!TextUtils.isEmpty(user.nickname)) {
                custom_nick_name.setTvArrowLeftStyle(true, user.nickname, R.color.color_333);
            }

            if (!TextUtils.isEmpty(user.summary)) {
                custom_signature.setTvArrowLeftStyle(true, user.summary, R.color.color_333);
            }
            if (!TextUtils.isEmpty(user.address)) {
                custom_area.setTvArrowLeftStyle(true, user.address, R.color.color_333);
            }

            switch (user.sex) {
                case SECRET:
                    custom_user_sex.setTvArrowLeftStyle(true, "保密", R.color.color_333);
                    break;
                case MAN:
                    custom_user_sex.setTvArrowLeftStyle(true, "男", R.color.color_333);
                    break;
                case WOMAN:
                    custom_user_sex.setTvArrowLeftStyle(true, "女", R.color.color_333);
                    break;
            }

            if (!TextUtils.isEmpty(user.birthday)) {
                custom_user_birthday.setTvArrowLeftStyle(true, user.birthday, R.color.color_333);
            }

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            File file = null;
            switch (requestCode) {
                case REQUEST_NICK_NAME:
//                    loadActivityData();
                    user=(User)data.getSerializableExtra(User.class.getSimpleName());
                    custom_nick_name.setTvArrowLeftStyle(true,user.nickname,R.color.color_333);
                    break;
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
                            file = Util.saveBitmapToFile(photo);
//                           String path=file.getAbsolutePath();
                            uploadFile(file, photo);
                        } else {
                            Util.makeToast(activity, "截取头像失败");
                            return;
                        }
                    }
                    break;
            }
        }
    }

    private void uploadFile(final File file, final Bitmap bitmap) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("Action", "UploadHeader");
//        params.addBodyParameter("uid",LoginUtil.getUserId());
//        params.addBodyParameter("photos",file);
//        HttpRequestData.uploadFileWithParams(Constants.APP_URI, params, new ICallback4Http() {
//            @Override
//            public void onResponse(String response) {
//                if (file.exists()) file.delete();
//                loadActivityData();
//                HttpResponse httpResponse = JsonUtil.fromJson(response, HttpResponse.class);
//                Util.makeToast(activity,httpResponse.getMessage());
//            }
//
//            @Override
//            public void onFailure(String errorMessage) {
//                Util.makeToast(activity,errorMessage);
//            }
//        });
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
            intent.putExtra("outputX", 250);
            intent.putExtra("outputY", 250);
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            intent.putExtra("scale", true);
            intent.putExtra("return-data", true);
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
