package com.taihuoniao.fineix.scene;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.scene.fragments.CameraFragment;
import com.taihuoniao.fineix.scene.fragments.PhotoFragment;

/**
 * Created by taihuoniao on 2016/3/14.
 */
public class SelectPhotoOrCameraActivity extends FragmentActivity implements View.OnClickListener {
    private RelativeLayout photoRelative, cameraRelative;
    private TextView photoTv, cameraTv;
    private TextView photoRedline, cameraRedline;
    private FragmentManager fm;
    private PhotoFragment photoFragment;
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
        setContentView(R.layout.activity_select);
        photoRelative = (RelativeLayout) findViewById(R.id.activity_select_photorelative);
        cameraRelative = (RelativeLayout) findViewById(R.id.activity_select_camerarelative);
        photoTv = (TextView) findViewById(R.id.activity_select_phototv);
        cameraTv = (TextView) findViewById(R.id.activity_select_cameratv);
        photoRedline = (TextView) findViewById(R.id.activity_select_photoreredline);
        cameraRedline = (TextView) findViewById(R.id.activity_select_cameraredline);
        fm = getSupportFragmentManager();
        photoFragment = new PhotoFragment();
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
        photoRedline.setVisibility(View.GONE);
        cameraRedline.setVisibility(View.GONE);
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
                photoTv.setTextColor(getResources().getColor(R.color.red));
                photoRedline.setVisibility(View.VISIBLE);
                break;
            case R.id.activity_select_camerarelative:
                cameraTv.setTextColor(getResources().getColor(R.color.red));
                cameraRedline.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null)
            Log.e("<<<", data.toString());
    }
}
