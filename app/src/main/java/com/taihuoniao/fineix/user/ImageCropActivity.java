package com.taihuoniao.fineix.user;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.download.ImageDownloader;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.beans.ImgUploadBean;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.HttpResponse;
import com.taihuoniao.fineix.network.NetworkConstance;
import com.taihuoniao.fineix.network.NetworkManager;
import com.taihuoniao.fineix.utils.FileUtils;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.LogUtil;
import com.taihuoniao.fineix.utils.Util;
import com.taihuoniao.fineix.view.CustomHeadView;
import com.taihuoniao.fineix.view.ImageCrop.ClipSquareImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;

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
    @Bind(R.id.progress_bar)
    ProgressBar progress_bar;
    private String page;
    public ImageCropActivity() {
        super(R.layout.activity_image_crop);
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
                    if (progress_bar!=null) progress_bar.setVisibility(View.VISIBLE);
                }

                @Override
                public void onSuccess(ResponseInfo<String> responseInfo) {
                    setViewEnable(true);
                    progress_bar.setVisibility(View.GONE);
                    if (responseInfo == null) {
                        return;
                    }
                    if (TextUtils.isEmpty(responseInfo.result)) {
                        return;
                    }
                    LogUtil.e(TAG, responseInfo.result);
                    HttpResponse response = JsonUtil.fromJson(responseInfo.result, HttpResponse.class);
                    if (response.isSuccess()) {
                        Util.makeToast("背景图片上传成功");
                        activity.finish();
                        return;
                    }
                    Util.makeToast(response.getMessage());
                }

                @Override
                public void onFailure(HttpException e, String s) {
                    setViewEnable(true);
                    progress_bar.setVisibility(View.GONE);
                    Util.makeToast(s);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            setViewEnable(true);
            progress_bar.setVisibility(View.GONE);
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
                    if (progress_bar!=null) progress_bar.setVisibility(View.VISIBLE);
                    setViewEnable(false);
                }

                @Override
                public void onSuccess(ResponseInfo<String> responseInfo) {
                    progress_bar.setVisibility(View.GONE);
                    setViewEnable(true);
                    if (responseInfo==null){
                        return;
                    }
                    if (TextUtils.isEmpty(responseInfo.result)){
                        return;
                    }

                    HttpResponse response = JsonUtil.fromJson(responseInfo.result, HttpResponse.class);
                    if (response.isSuccess()){
                        if (listener!=null){
                            listener.onClipComplete(bitmap);
                        }
                        finish();
                        Util.makeToast("头像上传成功");
                        return;
                    }
                    Util.makeToast(response.getMessage());
                }

                @Override
                public void onFailure(HttpException e, String s) {
                    progress_bar.setVisibility(View.GONE);
                    setViewEnable(true);
                    Util.makeToast(s);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            setViewEnable(true);
            progress_bar.setVisibility(View.GONE);
        }
    }

}

