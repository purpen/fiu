package com.taihuoniao.fineix.scene;

import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.utils.DensityUtils;
import com.taihuoniao.fineix.utils.ImageUtils;
import com.taihuoniao.fineix.view.GlobalTitleLayout;
import com.taihuoniao.fineix.view.roundImageView.RoundedImageView;

/**
 * Created by taihuoniao on 2016/4/6.
 */
public class CreateSceneActivity extends BaseActivity implements View.OnClickListener {
    //上个界面传递过来的图片存储地址
    private Uri imageUri;
    //控件
    private GlobalTitleLayout titleLayout;
    //    private ImageView sceneImg;
    private RoundedImageView sceneImg;
    private EditText contentEdt;
    private EditText titleEdt;
    private TextView locationTv;
    private RecyclerView recyclerView;
    private RelativeLayout labelRelative;
    private RelativeLayout qingjingRelative;


    public CreateSceneActivity() {
        super(0);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_create_scene);
        titleLayout = (GlobalTitleLayout) findViewById(R.id.activity_create_scene_titlelayout);
        sceneImg = (RoundedImageView) findViewById(R.id.activity_create_scene_img);
        contentEdt = (EditText) findViewById(R.id.activity_create_scene_contentedt);
        titleEdt = (EditText) findViewById(R.id.activity_create_scene_titleedt);
        locationTv = (TextView) findViewById(R.id.activity_create_scene_location);
        recyclerView = (RecyclerView) findViewById(R.id.activity_create_scene_recycler);
        labelRelative = (RelativeLayout) findViewById(R.id.activity_create_scene_labelrelative);
        qingjingRelative = (RelativeLayout) findViewById(R.id.activity_create_scene_qingjing);
    }

    @Override
    protected void initList() {
        imageUri = getIntent().getData();
        titleLayout.setFocusable(true);
        titleLayout.setFocusableInTouchMode(true);
        titleLayout.requestFocus();
        titleLayout.setBackgroundResource(R.color.white);
        titleLayout.setBackImg(R.mipmap.back_black);
        titleLayout.setBackListener(this);
        titleLayout.setTitle(R.string.create_scene, getResources().getColor(R.color.black333333));
        titleLayout.setRightTv(R.string.publish, getResources().getColor(R.color.black333333), this);
        ImageUtils.asyncLoadImage(CreateSceneActivity.this, imageUri, new ImageUtils.LoadImageCallback() {
            @Override
            public void callback(Bitmap result) {
                sceneImg.setImageBitmap(result);
                sceneImg.setCornerRadius(DensityUtils.dp2px(CreateSceneActivity.this, 5));
            }
        });
        double[] location = ImageUtils.picLocation(imageUri.getPath());
        if (location != null) {
            //图片上有位置信息

        } else {
            //图片上无位置信息

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_continue:
                break;
            case R.id.title_back:
                break;
        }
    }
}
