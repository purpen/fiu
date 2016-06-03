package com.taihuoniao.fineix.user;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.HttpResponse;
import com.taihuoniao.fineix.network.NetworkConstance;
import com.taihuoniao.fineix.network.NetworkManager;
import com.taihuoniao.fineix.utils.FileUtils;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.LogUtil;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.utils.Util;
import com.taihuoniao.fineix.view.ImageCrop.ClipSquareImageView;
import com.taihuoniao.fineix.view.WaittingDialog;
import com.taihuoniao.fineix.view.svprogress.SVProgressHUD;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @author lilin
 *         created at 2016/5/18 18:01
 */
public class ImageCropActivity extends BaseActivity {
    interface OnClipCompleteListener{
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
    private WaittingDialog dialog;
    private SVProgressHUD svProgressHUD;
    public ImageCropActivity() {
        super(R.layout.activity_image_crop);
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.default_background_500_500)
                .showImageForEmptyUri(R.mipmap.default_background_500_500)
                .showImageOnFail(R.mipmap.default_background_500_500)
                .delayBeforeLoading(0)
                .imageScaleType(ImageScaleType.EXACTLY)
                .cacheInMemory(false)
                .cacheOnDisk(false)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
    }

    public static void setOnClipCompleteListener(OnClipCompleteListener listener){
        ImageCropActivity.listener=listener;
    }
    @Override
    protected void getIntentData() {
        Intent intent = getIntent();
        if (intent.hasExtra(ImageCropActivity.class.getSimpleName())) {
            uri = intent.getParcelableExtra(ImageCropActivity.class.getSimpleName());
        }

        if (intent.hasExtra(ImageCropActivity.class.getName())){//区分界面
            page=intent.getStringExtra(ImageCropActivity.class.getName());
        }
    }

    @Override
    protected void initView() {
        if (uri == null) return;
        dialog=new WaittingDialog(this);
        svProgressHUD=new SVProgressHUD(this);
        String path = FileUtils.getRealFilePath(getApplicationContext(), uri);
//        LogUtil.e("path",path);
        ImageLoader.getInstance().displayImage("file:///"+path,csiv,options);
    }


    @OnClick({R.id.bt_cancel, R.id.bt_clip})
    void performClick(View v) {
        switch (v.getId()) {
            case R.id.bt_cancel:
                NetworkManager.getInstance().cancel(NetworkConstance.UPLOAD_BG_URL);
                finish();
                break;
            case R.id.bt_clip:
                Bitmap bitmap=csiv.clip();
                if (TextUtils.isEmpty(page)){//认证图片
                    if (listener!=null){
                        listener.onClipComplete(bitmap);
                        finish();
                    }
                }else if (TextUtils.equals(EditUserInfoActivity.class.getSimpleName(),page) || TextUtils.equals(CompleteUserInfoActivity.class.getSimpleName(),page)){//上传头像
                    uploadUserAvatar(bitmap);
                } else {//上传背景封面
                    uploadFile(bitmap);
                }
                break;
        }
    }

    @Override
    public void onConfigurationChanged(Configuration config) {
        super.onConfigurationChanged(config);
    }

    private void uploadFile(Bitmap bitmap) { //换个人中心背景图
        if (bitmap == null) return;
        String imgStr = Util.saveBitmap2Base64Str(bitmap);
        bitmap.recycle();
        try {
            ClientDiscoverAPI.uploadBgImg(imgStr, new RequestCallBack<String>() {
                @Override
                public void onStart() {
                    setViewEnable(false);
                    if (dialog!=null && !activity.isFinishing()) dialog.show();
                }

                @Override
                public void onSuccess(ResponseInfo<String> responseInfo) {
                    setViewEnable(true);
                    if (dialog!=null && !activity.isFinishing()) dialog.dismiss();
                    if (responseInfo == null) {
                        return;
                    }
                    if (TextUtils.isEmpty(responseInfo.result)) {
                        return;
                    }
                    LogUtil.e(TAG, responseInfo.result);
                    HttpResponse response = JsonUtil.fromJson(responseInfo.result, HttpResponse.class);
                    if (response.isSuccess()) {
                        ToastUtils.showSuccess("背景图上传成功");
//                        svProgressHUD.showSuccessWithStatus("背景图上传成功");
                        activity.finish();
                        return;
                    }
                    ToastUtils.showError(response.getMessage());
//                    svProgressHUD.showErrorWithStatus(response.getMessage());
                }

                @Override
                public void onFailure(HttpException e, String s) {
                    setViewEnable(true);
                    if (dialog!=null && !activity.isFinishing()) dialog.dismiss();
                    ToastUtils.showError("网络异常，请确认网络畅通");
//                    svProgressHUD.showErrorWithStatus("网络异常，请确认网络畅通");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            setViewEnable(true);
            if (dialog!=null && !activity.isFinishing()) dialog.dismiss();
        }

    }

    private void setViewEnable(boolean enable){
        bt_clip.setEnabled(enable);
        csiv.setEnabled(enable);
    }

    private void uploadUserAvatar(final Bitmap bitmap){
        if (bitmap==null)  return;
        String type="3"; //上传头像
        String imgStr=Util.saveBitmap2Base64Str(bitmap);
        try {
            ClientDiscoverAPI.uploadImg(imgStr,type, new RequestCallBack<String>() {
                @Override
                public void onStart() {
                    if (dialog!=null && !activity.isFinishing()) dialog.show();
                    setViewEnable(false);
                }

                @Override
                public void onSuccess(ResponseInfo<String> responseInfo) {
                    if (dialog!=null && !activity.isFinishing()) dialog.dismiss();
                    setViewEnable(true);
                    if (responseInfo==null){
                        return;
                    }
                    if (TextUtils.isEmpty(responseInfo.result)){
                        return;
                    }

                    HttpResponse response = JsonUtil.fromJson(responseInfo.result, HttpResponse.class);
                    if (response.isSuccess()){
                        ToastUtils.showSuccess("头像上传成功");
//                        svProgressHUD.showSuccessWithStatus("头像上传成功");
                        if (listener!=null){
                            listener.onClipComplete(bitmap);
                        }
                        finish();
                        return;
                    }
                    ToastUtils.showError(response.getMessage());
//                    svProgressHUD.showErrorWithStatus(response.getMessage());
                }

                @Override
                public void onFailure(HttpException e, String s) {
                    if (dialog!=null && !activity.isFinishing()) dialog.dismiss();
                    setViewEnable(true);
                    ToastUtils.showError("网络异常，请确认网络畅通");
//                    svProgressHUD.showErrorWithStatus("网络异常，请确认网络畅通");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            setViewEnable(true);
            if (dialog!=null && !activity.isFinishing()) dialog.dismiss();
        }
    }

}

