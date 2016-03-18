package com.taihuoniao.fineix.scene;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.View;
import android.widget.Toast;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.main.MainApplication;
import com.taihuoniao.fineix.utils.DensityUtils;
import com.taihuoniao.fineix.utils.ImageUtils;
import com.taihuoniao.fineix.utils.WaittingDialog;
import com.taihuoniao.fineix.view.GlobalTitleLayout;
import com.taihuoniao.fineix.view.ImageCrop.ClipImageLayout;

import java.io.IOException;

/**
 * Created by taihuoniao on 2016/3/16.
 */
public class CropPictureActivity extends BaseActivity implements View.OnClickListener {
    private GlobalTitleLayout titleLayout;
    private ClipImageLayout clipImageLayout;
    private WaittingDialog dialog;

    @Override
    protected void requestNet() {

    }

    @Override
    protected void initList() {
        Uri imageUri = getIntent().getData();
        titleLayout.setTitle("裁剪图片");
        titleLayout.setContinueListener(this);
        clipImageLayout.setImage(ImageUtils.decodeBitmapWithSize(imageUri.getPath(), MainApplication.getContext().getScreenWidth(), MainApplication.getContext().getScreenHeight() - DensityUtils.dp2px(CropPictureActivity.this, 50), false));
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_crop);
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
                    ImageUtils.saveToFile(MainApplication.getContext().getCacheDirPath() + "/crop", false, bitmap);
                    Intent intent = new Intent(CropPictureActivity.this, EditPictureActivity.class);
                    intent.setData(Uri.parse("file://" + MainApplication.getContext().getCacheDirPath()
                            + "/crop"));
                    dialog.dismiss();
                    startActivity(intent);
                } catch (IOException e) {
                    e.printStackTrace();
                    dialog.dismiss();
                    Toast.makeText(CropPictureActivity.this, "图片裁切异常，请稍后重试！", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
