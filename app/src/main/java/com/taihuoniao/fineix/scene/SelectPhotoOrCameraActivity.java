package com.taihuoniao.fineix.scene;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.main.MainApplication;
import com.taihuoniao.fineix.scene.fragments.CameraFragment;
import com.taihuoniao.fineix.scene.fragments.PictureFragment;
import com.taihuoniao.fineix.utils.EffectUtil;

/**
 * Created by taihuoniao on 2016/3/14.
 */
public class SelectPhotoOrCameraActivity extends FragmentActivity implements View.OnClickListener {
    private RelativeLayout photoRelative, cameraRelative;
    private TextView photoTv, cameraTv;
    private FragmentManager fm;
    //    private PhotoFragment photoFragment;
    private PictureFragment photoFragment;
    private CameraFragment cameraFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initList();
        requestNet();
    }

    protected void requestNet() {

    }

    protected void initList() {
        photoRelative.setOnClickListener(this);
        cameraRelative.setOnClickListener(this);
    }

    protected void initView() {
        View activityView = View.inflate(SelectPhotoOrCameraActivity.this, R.layout.activity_select, null);
        setContentView(activityView);
        photoRelative = (RelativeLayout) findViewById(R.id.activity_select_photorelative);
        cameraRelative = (RelativeLayout) findViewById(R.id.activity_select_camerarelative);
        photoTv = (TextView) findViewById(R.id.activity_select_phototv);
        cameraTv = (TextView) findViewById(R.id.activity_select_cameratv);
        fm = getSupportFragmentManager();
//        photoFragment = new PhotoFragment();
        photoFragment = new PictureFragment();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.activity_select_fragment_container, photoFragment);
        ft.commit();
    }

    @Override
    public void onClick(View v) {
        goneColor();
        goneFragment();
        selectColor(v);
        selectFragment(v);
    }

    private void goneColor() {
        photoTv.setTextColor(getResources().getColor(R.color.white));
        cameraTv.setTextColor(getResources().getColor(R.color.white));
    }

    private void goneFragment() {
        FragmentTransaction ft = fm.beginTransaction();
        ft.hide(photoFragment);
        if (cameraFragment != null) {
            ft.hide(cameraFragment);
        }
        ft.commit();
    }

    private void selectFragment(View v) {
        FragmentTransaction ft = fm.beginTransaction();
        switch (v.getId()) {
            case R.id.activity_select_photorelative:
                ft.show(photoFragment);
                break;
            case R.id.activity_select_camerarelative:
                if (cameraFragment == null) {
                    cameraFragment = new CameraFragment();
                    ft.add(R.id.activity_select_fragment_container, cameraFragment);
                } else {
                    ft.show(cameraFragment);
                }
                break;
        }
        ft.commit();
    }

    private void selectColor(View v) {
        switch (v.getId()) {
            case R.id.activity_select_photorelative:
                photoTv.setTextColor(getResources().getColor(R.color.yellow_bd8913));
                break;
            case R.id.activity_select_camerarelative:
                cameraTv.setTextColor(getResources().getColor(R.color.yellow_bd8913));
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        EffectUtil.clear();
        MainApplication.blurBitmap = null;
        MainApplication.cropBitmap = null;
        MainApplication.editBitmap = null;
        MainApplication.subjectId = null;
    }
    //    @Override
//    public void onWindowFocusChanged(boolean hasFocus) {
//        super.onWindowFocusChanged(hasFocus);
//        if (hasFocus) {
//            SharedPreferences firstInSp = getSharedPreferences(DataConstants.SHAREDPREFRENCES_FIRST_IN, Context.MODE_PRIVATE);
//            //判断是不是第一次进入Fiu界面
//            boolean isFirstIn = firstInSp.getBoolean(DataConstants.FIRST_IN_FIU, true);
//            if (isFirstIn) {
//                FirstInAppUtils.showPop(SelectPhotoOrCameraActivity.this, FirstInAppUtils.FIU, activityView);
//                SharedPreferences.Editor editor = firstInSp.edit();
//                editor.putBoolean(DataConstants.FIRST_IN_FIU, false);
//                editor.apply();
//            }
//        }
//    }

}
