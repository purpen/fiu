package com.taihuoniao.fineix.user;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.google.gson.reflect.TypeToken;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.base.HttpRequest;
import com.taihuoniao.fineix.beans.HttpResponse;
import com.taihuoniao.fineix.beans.ZoneUploadCoverBean;
import com.taihuoniao.fineix.common.GlobalDataCallBack;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.NetworkManager;
import com.taihuoniao.fineix.network.URL;
import com.taihuoniao.fineix.utils.GlideUtils;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.utils.Util;
import com.taihuoniao.fineix.view.ImageCrop.ClipSquareImageView;
import com.taihuoniao.fineix.view.dialog.WaittingDialog;
import com.taihuoniao.fineix.zone.ZoneBaseInfoActivity;
import com.taihuoniao.fineix.zone.ZoneManagementActivity;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @author lilin
 *         created at 2016/5/18 18:01
 */
public class ImageCropActivity extends BaseActivity {
    public interface OnClipCompleteListener {
        void onClipComplete(Bitmap bitmap);
    }

    private static OnClipCompleteListener listener;
    @Bind(R.id.csiv)
    ClipSquareImageView csiv;
    private Uri uri;
    @Bind(R.id.bt_cancel)
    Button bt_cancel;
    @Bind(R.id.bt_clip)
    Button bt_clip;
    private String page;
    private String zoneId;
    private WaittingDialog dialog;

    public ImageCropActivity() {
        super(R.layout.activity_image_crop);
    }

    public static void setOnClipCompleteListener(OnClipCompleteListener listener) {
        ImageCropActivity.listener = listener;
    }

    @Override
    protected void getIntentData() {
        Intent intent = getIntent();
        if (intent.hasExtra(ImageCropActivity.class.getSimpleName())) {
            uri = intent.getParcelableExtra(ImageCropActivity.class.getSimpleName());
        }

        if (intent.hasExtra(ImageCropActivity.class.getName())) {//区分界面
            page = intent.getStringExtra(ImageCropActivity.class.getName());
            zoneId = intent.getStringExtra(ZoneManagementActivity.class.getSimpleName());
        }
    }

    @Override
    protected void initView() {
        if (uri == null) return;
        dialog = new WaittingDialog(this);
        GlideUtils.displayImage(uri, csiv);
    }


    @OnClick({R.id.bt_cancel, R.id.bt_clip})
    void performClick(View v) {
        switch (v.getId()) {
            case R.id.bt_cancel:
                NetworkManager.getInstance().cancel(URL.UPLOAD_BG_URL);
                finish();
                break;
            case R.id.bt_clip:
                Bitmap bitmap = csiv.clip();
                if (TextUtils.isEmpty(page)) {//认证图片
                    if (listener != null) {
                        listener.onClipComplete(bitmap);
                        finish();
                    }
                } else if (TextUtils.equals(EditUserInfoActivity.class.getSimpleName(), page) || TextUtils.equals(CompleteUserInfoActivity.class.getSimpleName(), page)) {//上传头像
                    uploadUserAvatar(bitmap);
                } else if (TextUtils.equals(ZoneManagementActivity.class.getSimpleName(), page)) { //上传地盘封面
                    uploadZoneCover(zoneId, bitmap);
                } else if (TextUtils.equals(ZoneBaseInfoActivity.class.getSimpleName(), page)) {
                    uploadZoneLogo(zoneId, bitmap);
                } else {//上传背景封面
                    uploadFile(bitmap);
                }
                break;
        }
    }

    /**
     * 上传地盘封面
     */
    private void uploadZoneCover(String id, Bitmap bitmap) {
        if (bitmap == null) return;
        if (dialog != null && !activity.isFinishing()) dialog.show();
        String imgStr = Util.saveBitmap2Base64Str(bitmap,550);
        bitmap.recycle();
        HashMap params = ClientDiscoverAPI.getZoneCoverParams(id, imgStr, "1");
        HttpRequest.post(params, URL.ZONE_ADD_COVER, new GlobalDataCallBack() {
            @Override
            public void onStart() {
                setViewEnable(false);
            }

            @Override
            public void onSuccess(String json) {
                if (dialog != null && !activity.isFinishing()) dialog.dismiss();
                HttpResponse httpResponse = JsonUtil.fromJson(json, HttpResponse.class);
                setViewEnable(true);
                if (httpResponse.isSuccess()) {
                    ZoneUploadCoverBean response = JsonUtil.fromJson(json, new TypeToken<HttpResponse<ZoneUploadCoverBean>>() {
                    });
                    ToastUtils.showSuccess(httpResponse.getMessage());
                    finish();
                    return;
                }
                ToastUtils.showError(httpResponse.getMessage());
            }

            @Override
            public void onFailure(String error) {
                if (dialog != null && !activity.isFinishing()) dialog.dismiss();
                setViewEnable(true);
                ToastUtils.showError(R.string.network_err);
            }
        });
    }


    private void uploadFile(Bitmap bitmap) { //换个人中心背景图
        if (bitmap == null) return;
        if (dialog != null && !activity.isFinishing()) dialog.show();
        String imgStr = Util.saveBitmap2Base64Str(bitmap, 512);
        bitmap.recycle();
        try {
            HashMap<String, String> params = ClientDiscoverAPI.getuploadBgImgRequestParams(imgStr);
            HttpRequest.post(params, URL.UPLOAD_BG_URL, new GlobalDataCallBack() {
                @Override
                public void onStart() {
                    setViewEnable(false);
                }

                @Override
                public void onSuccess(String json) {
                    if (dialog != null && !activity.isFinishing()) dialog.dismiss();
                    HttpResponse response = JsonUtil.fromJson(json, HttpResponse.class);
                    setViewEnable(true);
                    if (response.isSuccess()) {
                        ToastUtils.showSuccess("背景图上传成功");
                        activity.finish();
                        return;
                    }
                    ToastUtils.showError(response.getMessage());
                }

                @Override
                public void onFailure(String error) {
                    setViewEnable(true);
                    if (dialog != null && !activity.isFinishing()) dialog.dismiss();
                    ToastUtils.showError(R.string.network_err);
                }
            });
        } catch (Exception e) {
            bitmap.recycle();
            e.printStackTrace();
        } finally {
            setViewEnable(true);
            if (dialog != null && !activity.isFinishing()) dialog.dismiss();
        }

    }

    private void setViewEnable(boolean enable) {
        bt_clip.setEnabled(enable);
        csiv.setEnabled(enable);
    }

    private void uploadUserAvatar(final Bitmap bitmap) {
        if (bitmap == null) return;
        String type = "3"; //上传头像
        if (dialog != null && !activity.isFinishing()) dialog.show();
        String imgStr = Util.saveBitmap2Base64Str(bitmap,200);
        try {
            HashMap<String, String> params = ClientDiscoverAPI.getuploadImgRequestParams(imgStr, type);
            HttpRequest.post(params, URL.UPLOAD_IMG_URL, new GlobalDataCallBack() {
                @Override
                public void onStart() {
                    setViewEnable(false);
                }

                @Override
                public void onSuccess(String json) {
                    if (dialog != null && !activity.isFinishing()) dialog.dismiss();
                    HttpResponse response = JsonUtil.fromJson(json, HttpResponse.class);
                    setViewEnable(true);
                    if (response.isSuccess()) {
                        ToastUtils.showSuccess("头像上传成功");
                        if (listener != null) {
                            listener.onClipComplete(bitmap);
                        }
                        finish();
                        return;
                    }
                    ToastUtils.showError(response.getMessage());
                }

                @Override
                public void onFailure(String error) {
                    if (dialog != null && !activity.isFinishing()) dialog.dismiss();
                    setViewEnable(true);
                    ToastUtils.showError(R.string.network_err);
                }
            });
        } catch (Exception e) {
            bitmap.recycle();
            e.printStackTrace();
        } finally {
            setViewEnable(true);
            if (dialog != null && !activity.isFinishing()) dialog.dismiss();
        }
    }

    //上传地盘logo
    private void uploadZoneLogo(String id, final Bitmap bitmap) {
        if (bitmap == null) return;
        try {
            if (dialog != null && !activity.isFinishing()) dialog.show();
            String imgStr = Util.saveBitmap2Base64Str(bitmap, 200);
            HashMap<String, String> params = new HashMap<>();
            params.put("id", id);
            params.put("avatar_tmp", imgStr);
            HttpRequest.post(params, URL.SCENE_SCENE_SAVE_URL, new GlobalDataCallBack() {
                @Override
                public void onStart() {
                    setViewEnable(false);
                }

                @Override
                public void onSuccess(String json) {
                    if (dialog != null && !activity.isFinishing()) dialog.dismiss();
                    HttpResponse response = JsonUtil.fromJson(json, HttpResponse.class);
                    setViewEnable(true);
                    if (response.isSuccess()) {
                        ToastUtils.showSuccess("地盘logo上传成功");
                        if (listener != null) {
                            listener.onClipComplete(bitmap);
                        }
                        finish();
                        return;
                    }
                    ToastUtils.showError(response.getMessage());
                }

                @Override
                public void onFailure(String error) {
                    if (dialog != null && !activity.isFinishing()) dialog.dismiss();
                    setViewEnable(true);
                    ToastUtils.showError(R.string.network_err);
                }
            });
        } catch (Exception e) {
            bitmap.recycle();
            e.printStackTrace();
        } finally {
            setViewEnable(true);
            if (dialog != null && !activity.isFinishing()) dialog.dismiss();
        }
    }

}

