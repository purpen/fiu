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
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.CropOptionAdapter;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.beans.CropOption;
import com.taihuoniao.fineix.beans.ImgUploadBean;
import com.taihuoniao.fineix.beans.User;
import com.taihuoniao.fineix.main.MainActivity;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.HttpResponse;
import com.taihuoniao.fineix.utils.Base64Utils;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.LogUtil;
import com.taihuoniao.fineix.utils.PopupWindowUtil;
import com.taihuoniao.fineix.utils.Util;
import com.taihuoniao.fineix.view.CustomHeadView;
import com.taihuoniao.fineix.view.SegmentedGroup;
import com.taihuoniao.fineix.view.roundImageView.RoundedImageView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @author lilin
 *         created at 2016/5/9 14:42
 */
public class CompleteUserInfoActivity extends BaseActivity {
    @Bind(R.id.custom_head)
    CustomHeadView custom_head;
    @Bind(R.id.et_nickname)
    EditText et_nickname;
    @Bind(R.id.et_sign)
    EditText et_sign;
    @Bind(R.id.riv)
    RoundedImageView riv;
    @Bind(R.id.radioGroup)
    SegmentedGroup radioGroup;
    private String gender = SECRET;
    private static final String SECRET = "0";
    private static final String MALE = "1";
    private static final String FEMALE = "2";
    private static final int PICK_FROM_FILE = 0;
    private static final int PICK_FROM_CAMERA = 1;
    private static final int CROP_FROM_CAMERA = 2;
    private static final String TYPE = "3";
    private Uri mImageUri;

    public CompleteUserInfoActivity() {
        super(R.layout.activity_complete_user_info);
    }

    @Override
    protected void initView() {
        custom_head.setHeadCenterTxtShow(true, "完善个人资料");
    }

    @OnClick({R.id.btn, R.id.riv})
    void performClick(View v) {
        switch (v.getId()) {
            case R.id.btn:
                submitData(v);
                break;
            case R.id.riv:
                PopupWindowUtil.show(activity, initPopView(R.layout.popup_upload_avatar,"上传头像"));
                break;
        }
    }

    private View initPopView(int layout,String title) {
        View view = Util.inflateView(this, layout, null);
        ((TextView)view.findViewById(R.id.tv_title)).setText(title);
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
                            "tmp_avatar" + ".jpg"));

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
    protected void installListener() {
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.male:
                        gender = MALE;
                        break;
                    case R.id.female:
                        gender = FEMALE;
                        break;
                    case R.id.secret:
                        gender = SECRET;
                        break;
                }
            }
        });
    }

    private void submitData(View v) {
        String nickname = et_nickname.getText().toString().trim();
        String sign = et_sign.getText().toString().trim();
        if (TextUtils.isEmpty(nickname)) {
            Util.makeToast("请填写昵称");
            return;
        }

        if (TextUtils.isEmpty(sign)) {
            Util.makeToast("请填写个性签名");
            return;
        }

        ClientDiscoverAPI.updateNickNameSummary(nickname, sign, gender, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                if (responseInfo == null) {
                    return;
                }

                if (TextUtils.isEmpty(responseInfo.result)) {
                    return;
                }

                HttpResponse response = JsonUtil.fromJson(responseInfo.result, HttpResponse.class);

                if (response.isSuccess()) {
                    startActivity(new Intent(activity, MainActivity.class));
                    finish();
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
//                            file = Util.saveBitmapToFile(photo);
                            uploadFile(photo);
                        } else {
                            Util.makeToast(activity, "截取头像失败");
                            return;
                        }
                    }
                    break;
            }
        }
    }

    private void uploadFile(final Bitmap bitmap) {
        if (bitmap == null) return;
        try {
            ClientDiscoverAPI.uploadImg(Util.saveBitmap2Base64Str(bitmap), TYPE, new RequestCallBack<String>() {
                @Override
                public void onSuccess(ResponseInfo<String> responseInfo) {
                    if (responseInfo == null) {
                        return;
                    }
                    if (TextUtils.isEmpty(responseInfo.result)) {
                        return;
                    }

                    HttpResponse response = JsonUtil.fromJson(responseInfo.result, HttpResponse.class);
                    if (response.isSuccess()) {
                        ImgUploadBean imgUploadBean = JsonUtil.fromJson(responseInfo.result, new TypeToken<HttpResponse<ImgUploadBean>>() {
                        });
                        if (!TextUtils.isEmpty(imgUploadBean.file_url)) {
                            ImageLoader.getInstance().displayImage(imgUploadBean.file_url,riv);
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
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bitmap!=null) bitmap.recycle();
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
            intent.putExtra("outputX", 250);
            intent.putExtra("outputY", 250);
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
