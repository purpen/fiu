package com.taihuoniao.fineix.scene;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.view.GlobalTitleLayout;

/**
 * Created by taihuoniao on 2016/4/6.
 */
public class CreateSceneActivity extends BaseActivity {
    //控件
    private GlobalTitleLayout titleLayout;
    private ImageView sceneImg;
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
        sceneImg = (ImageView) findViewById(R.id.activity_create_scene_img);
        contentEdt = (EditText) findViewById(R.id.activity_create_scene_contentedt);
        titleEdt = (EditText) findViewById(R.id.activity_create_scene_titleedt);
        locationTv = (TextView) findViewById(R.id.activity_create_scene_location);
        recyclerView = (RecyclerView) findViewById(R.id.activity_create_scene_recycler);
        labelRelative = (RelativeLayout) findViewById(R.id.activity_create_scene_labelrelative);
        qingjingRelative = (RelativeLayout) findViewById(R.id.activity_create_scene_qingjing);
//        DisplayImageOptions options =
    }

    @Override
    protected void initList() {
        Uri imageUri = getIntent().getData();
//        ImageLoader.getInstance().displayImage();
    }
}
