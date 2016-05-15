package com.taihuoniao.fineix.scene;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Toast;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.main.MainApplication;
import com.taihuoniao.fineix.utils.DensityUtils;
import com.taihuoniao.fineix.utils.ImageUtils;
import com.taihuoniao.fineix.view.GlobalTitleLayout;
import com.taihuoniao.fineix.view.ImageCrop.ClipImageLayout;
import com.taihuoniao.fineix.view.WaittingDialog;

import java.io.IOException;

/**
 * Created by taihuoniao on 2016/3/16.
 */
public class CropPictureActivity extends BaseActivity implements View.OnClickListener {
    private Uri imageUri;//上个界面获取的图片位置uri
    private GlobalTitleLayout titleLayout;
    private ClipImageLayout clipImageLayout;
    private WaittingDialog dialog;
    public static CropPictureActivity instance = null;


    public CropPictureActivity() {
        super(0);
    }


    @Override
    protected void requestNet() {

    }

    @Override
    protected void initList() {
        imageUri = getIntent().getData();
        titleLayout.setTitle(R.string.crop_picture);
        titleLayout.setContinueListener(this);
        clipImageLayout.setImage(ImageUtils.decodeBitmapWithSize(imageUri.getPath(), MainApplication.getContext().getScreenWidth(), MainApplication.getContext().getScreenHeight() - DensityUtils.dp2px(CropPictureActivity.this, 50), true));
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_crop);
        instance = CropPictureActivity.this;
        titleLayout = (GlobalTitleLayout) findViewById(R.id.activity_crop_titlelayout);
        clipImageLayout = (ClipImageLayout) findViewById(R.id.activity_crop_cliplayout);
        dialog = new WaittingDialog(CropPictureActivity.this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_continue:
                dialog.show();
                Bitmap bitmap = clipImageLayout.clip();
                try {
                    ImageUtils.saveToFile(MainApplication.cropPicPath, false, bitmap);
                    Intent intent = new Intent(CropPictureActivity.this, EditPictureActivity.class);
                    intent.setData(Uri.parse("file://" + MainApplication.cropPicPath));
//                    ImageUtils.writeLocation(ImageUtils.picLocation(imageUri.getPath()), MainApplication.cropPicPath);
                    dialog.dismiss();
                    startActivity(intent);
                } catch (IOException e) {
                    e.printStackTrace();
                    dialog.dismiss();
                    AlertDialog.Builder builder = new AlertDialog.Builder(CropPictureActivity.this);
                    builder.setMessage("图片处理错误，请清理缓存后重试");
                    builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            Toast.makeText(CropPictureActivity.this, "清理缓存", Toast.LENGTH_SHORT).show();
                        }
                    });
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.create().show();
                }
                break;
        }
    }
}
