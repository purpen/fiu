package com.taihuoniao.fineix.qingjingOrSceneDetails;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.main.MainApplication;
import com.taihuoniao.fineix.utils.ImageUtils;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.view.ImageCrop.ClipZoomImageView;
import com.taihuoniao.fineix.view.WaittingDialog;

import java.io.IOException;

import butterknife.Bind;

/**
 * Created by taihuoniao on 2016/9/14.
 */
public class QJPictureActivity extends BaseActivity {
    private String imgStr;//上个界面传递过来的图片路径
    @Bind(R.id.clip_img)
    ClipZoomImageView clipImg;
    private Bitmap bitmap;
    private WaittingDialog dialog;

    public QJPictureActivity() {
        super(R.layout.activity_qj_picture);
    }

    @Override
    protected void getIntentData() {
        imgStr = getIntent().getStringExtra("img");
    }

    @Override
    protected void initView() {
        dialog = new WaittingDialog(this);
        clipImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
//        clipImg.setLongClickable(true);
        clipImg.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(QJPictureActivity.this);
                builder.setMessage("保存到本地？");
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        QJPictureActivity.this.dialog.show();
                        savePicture();
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create().show();
                return true;
            }
        });
        ImageLoader.getInstance().displayImage(imgStr, clipImg, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {

            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                bitmap = loadedImage;
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {

            }
        });
    }

    private void savePicture() {
        if (bitmap == null) {
            ImageLoader.getInstance().displayImage(imgStr, clipImg, new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {

                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    bitmap = loadedImage;
                }

                @Override
                public void onLoadingCancelled(String imageUri, View view) {

                }
            });
            return;
        }
        new Thread() {
            @Override
            public void run() {
                try {
                    ImageUtils.saveToFile(MainApplication.systemPhotoPath, true, bitmap);
                } catch (IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dialog.dismiss();
                            ToastUtils.showError("图片保存失败，请重试");
                        }
                    });
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dialog.dismiss();
                        ToastUtils.showSuccess("保存成功");
                    }
                });
            }
        }.start();

    }
}
