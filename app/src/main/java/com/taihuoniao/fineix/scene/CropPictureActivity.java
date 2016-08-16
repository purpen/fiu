//package com.taihuoniao.fineix.scene;
//
//import android.content.Intent;
//import android.content.res.Resources;
//import android.graphics.Bitmap;
//import android.net.Uri;
//import android.view.View;
//
//import com.nostra13.universalimageloader.core.DisplayImageOptions;
//import com.nostra13.universalimageloader.core.ImageLoader;
//import com.nostra13.universalimageloader.core.assist.FailReason;
//import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
//import com.taihuoniao.fineix.R;
//import com.taihuoniao.fineix.base.BaseActivity;
//import com.taihuoniao.fineix.main.MainApplication;
//import com.taihuoniao.fineix.utils.ImageUtils;
//import com.taihuoniao.fineix.utils.ToastUtils;
//import com.taihuoniao.fineix.view.GlobalTitleLayout;
//import com.taihuoniao.fineix.view.ImageCrop.ClipImageLayout;
//import com.taihuoniao.fineix.view.WaittingDialog;
//
//import java.io.IOException;
//
///**
// * Created by taihuoniao on 2016/3/16.
// */
//public class CropPictureActivity extends BaseActivity implements View.OnClickListener {
//    private GlobalTitleLayout titleLayout;
//    private ClipImageLayout clipImageLayout;
//    private WaittingDialog dialog;
//    public static CropPictureActivity instance = null;
//    private DisplayImageOptions options;
//
//
//    public CropPictureActivity() {
//        super(0);
//    }
//
//
//    @Override
//    protected void requestNet() {
//
//    }
//
//    @Override
//    protected void initList() {
//        Uri imageUri = getIntent().getData();
//        titleLayout.setTitle(R.string.crop_picture);
//        titleLayout.setBackgroundResource(R.color.black_touming);
//        titleLayout.setContinueListener(this);
////        这里可以改为ImageLoader了，图片会自动缩小至相应大小
////        https://github.com/yaoguang/AndroidChoseHeadImage
//        ImageLoader.getInstance().loadImage(imageUri.toString(), options, new ImageLoadingListener() {
//            @Override
//            public void onLoadingStarted(String imageUri, View view) {
//
//            }
//
//            @Override
//            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
//                ToastUtils.showError("图片挤在失败，请返回重试");
////                dialog.showErrorWithStatus("图片加载失败，请返回重试");
//            }
//
//            @Override
//            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
//                clipImageLayout.setImage(loadedImage);
//            }
//
//            @Override
//            public void onLoadingCancelled(String imageUri, View view) {
//                ToastUtils.showError("图片加载失败，请返回重试");
////                dialog.showErrorWithStatus("图片加载失败，请返回重试");
//            }
//        });
////        clipImageLayout.setImage(ImageUtils.decodeBitmapWithSize(imageUri.getPath(), MainApplication.getContext().getScreenWidth(), MainApplication.getContext().getScreenHeight(), false));
//    }
//
//    private int getNavigationBarHeight() {
//        int height = 0;
//        Resources resources = getResources();
//        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
//        if (resourceId > 0) {
//            height = resources.getDimensionPixelSize(resourceId);
//        }
////        Log.e("<<<", "工具栏 height:" + height);
//        return height;
//    }
//
//    @Override
//    protected void initView() {
//        setContentView(R.layout.activity_crop);
//        instance = CropPictureActivity.this;
//        titleLayout = (GlobalTitleLayout) findViewById(R.id.activity_crop_titlelayout);
////        获取状态栏的高度
//        clipImageLayout = (ClipImageLayout) findViewById(R.id.activity_crop_cliplayout);
//        dialog = new WaittingDialog(CropPictureActivity.this);
//        options = new DisplayImageOptions.Builder()
//                .showImageOnLoading(R.mipmap.default_background_750_1334)
//                .showImageForEmptyUri(R.mipmap.default_background_750_1334)
//                .showImageOnFail(R.mipmap.default_background_750_1334)
//                .cacheInMemory(false)
//                .cacheOnDisk(false).considerExifParams(true)
//                .build();
//    }
//
//    @Override
//    protected void onDestroy() {
//        instance = null;
//        super.onDestroy();
//    }
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.title_continue:
//                if (!dialog.isShowing()) {
//                    dialog.show();
//                }
//                Bitmap bitmap = clipImageLayout.clip();
//                try {
//                    ImageUtils.saveToFile(MainApplication.cropPicPath, false, bitmap);
//                    Intent intent;
//                    if (MainApplication.tag == 1) {
//                        intent = new Intent(CropPictureActivity.this, EditPictureActivity.class);
//                    } else {
//                        intent = new Intent(CropPictureActivity.this, FilterActivity.class);
//                    }
//                    intent.setData(Uri.parse("file://" + MainApplication.cropPicPath));
//                    dialog.dismiss();
//                    startActivity(intent);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    dialog.dismiss();
//                    ToastUtils.showError("图片处理错误，请清理缓存后重试");
////                    dialog.showErrorWithStatus("图片处理错误，请清理缓存后重试");
//                }
//                break;
//        }
//    }
//}
