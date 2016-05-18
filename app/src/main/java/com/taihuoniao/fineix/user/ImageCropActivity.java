package com.taihuoniao.fineix.user;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.utils.ImageUtils;
import com.taihuoniao.fineix.view.CustomHeadView;
import com.taihuoniao.fineix.view.ImageCrop.ClipSquareImageView;

import java.io.ByteArrayOutputStream;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @author lilin
 * created at 2016/5/18 18:01
 */
public class ImageCropActivity extends BaseActivity{
    @Bind(R.id.custom_head)
    CustomHeadView custom_head;
    @Bind(R.id.csiv)
    ImageView csiv;
    private Uri uri;
    public ImageCropActivity(){
        super(R.layout.activity_image_crop);
    }

    @Override
    protected void getIntentData() {
        Intent intent = getIntent();
        if (intent.hasExtra(ImageCropActivity.class.getSimpleName())){
            uri =intent.getParcelableExtra(ImageCropActivity.class.getSimpleName());
        }
    }

    @Override
    protected void initView() {
        custom_head.setHeadCenterTxtShow(true,"裁剪图片");
        if (uri==null) return;
//        Bitmap bitmap = ImageUtils.decodeUriAsBitmap(uri);
//        if (bitmap==null) return;
        csiv.setImageURI(uri);
    }


    @OnClick({R.id.bt_cancel,R.id.bt_clip})
    void performClick(View v){
        switch (v.getId()) {
            case R.id.bt_cancel:
                finish();
                break;
            case R.id.bt_clip:
//                Bitmap bitmap = csiv.clip();
                break;
        }
    }

    @Override
    public void onConfigurationChanged(Configuration config) {
        super.onConfigurationChanged(config);
    }
}

