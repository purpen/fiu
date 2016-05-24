package com.taihuoniao.fineix.user;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.HttpResponse;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.LogUtil;
import com.taihuoniao.fineix.utils.Util;
import com.taihuoniao.fineix.view.CustomHeadView;
import com.taihuoniao.fineix.view.ImageCrop.ClipSquareImageView;

import java.io.ByteArrayOutputStream;

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
    @Bind(R.id.custom_head)
    CustomHeadView custom_head;
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

        if (intent.hasExtra(UserCenterActivity.class.getSimpleName())){
            page=intent.getStringExtra(UserCenterActivity.class.getSimpleName());
        }
    }

    @Override
    protected void initView() {
        custom_head.setHeadCenterTxtShow(true, "裁剪图片");
        if (uri == null) return;
        csiv.setImageURI(uri);
    }


    @OnClick({R.id.bt_cancel, R.id.bt_clip})
    void performClick(View v) {
        switch (v.getId()) {
            case R.id.bt_cancel:
                finish();
                break;
            case R.id.bt_clip:
                Bitmap bitmap=csiv.clip();
                if (TextUtils.isEmpty(page)){
                    if (listener!=null){
                        listener.onClipComplete(bitmap);
                        finish();
                    }
//                    ByteArrayOutputStream out=new ByteArrayOutputStream();
//                    bitmap.compress(Bitmap.CompressFormat.JPEG,100,out);
//                    Intent intent = new Intent();
//                    intent.putExtra(Bitmap.class.getSimpleName(),out.toByteArray());
//                    setResult(RESULT_OK,intent);
//                    activity.finish();
                }else {//上传背景封面
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
                    bt_clip.setEnabled(false);
                    csiv.setEnabled(false);
                    if (progress_bar!=null) progress_bar.setVisibility(View.VISIBLE);
                }

                @Override
                public void onSuccess(ResponseInfo<String> responseInfo) {
                    bt_clip.setEnabled(true);
                    csiv.setEnabled(true);
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
//                        ImgUploadBean imgUploadBean = JsonUtil.fromJson(responseInfo.result, new TypeToken<HttpResponse<ImgUploadBean>>() {
//                        });
//                        if (!TextUtils.isEmpty(imgUploadBean.head_pic_url)) {
//                            new Intent()
//                            activity.setResult(UserCenterActivity.REQUEST_CODE_IMAGE_URL,);
//                            ImageLoader.getInstance().displayImage(imgUploadBean.head_pic_url,iv_bg);
//                        }
                        Util.makeToast("图片上传成功");
                        activity.finish();
                        return;
                    }
                    Util.makeToast(response.getMessage());
                }

                @Override
                public void onFailure(HttpException e, String s) {
                    bt_clip.setEnabled(true);
                    csiv.setEnabled(true);
                    progress_bar.setVisibility(View.GONE);
                    Util.makeToast(s);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            bt_clip.setEnabled(true);
            csiv.setEnabled(true);
            progress_bar.setVisibility(View.GONE);
//            if (uri != null)
//                if (UserCenterActivity.imageUri.equals(uri)){//是拍照
//                    LogUtil.e("删除拍照","删除拍照");
//                    getContentResolver().delete(UserCenterActivity.imageUri, null, null);
//                }
        }

    }

}

