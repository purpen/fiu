package com.taihuoniao.fineix.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.scene.SelectPhotoOrCameraActivity;

public class MainActivity extends FragmentActivity implements View.OnClickListener {
    private ImageView homepageImg, findImg, sceneImg, shopImg, mineImg;
    private FragmentManager fm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        setListener();
    }

    private void setListener() {
        homepageImg.setOnClickListener(this);
        findImg.setOnClickListener(this);
        sceneImg.setOnClickListener(this);
        shopImg.setOnClickListener(this);
        mineImg.setOnClickListener(this);
    }

    private void initView() {
        homepageImg = (ImageView) findViewById(R.id.activity_main_homepagebtn);
        findImg = (ImageView) findViewById(R.id.activity_main_findbtn);
        sceneImg = (ImageView) findViewById(R.id.activity_main_scenebtn);
        shopImg = (ImageView) findViewById(R.id.activity_main_shopbtn);
        mineImg = (ImageView) findViewById(R.id.activity_main_minebtn);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_main_scenebtn:
               startActivity(new Intent(MainActivity.this,SelectPhotoOrCameraActivity.class));
                break;
            case R.id.activity_main_homepagebtn:
                startActivity(new Intent(MainActivity.this, LocationActivity.class));
//                Toast.makeText(MainActivity.this, "首页切换未做", Toast.LENGTH_SHORT).show();
                break;
            case R.id.activity_main_findbtn:
                Toast.makeText(MainActivity.this, "发现切换未做", Toast.LENGTH_SHORT).show();
                break;
            case R.id.activity_main_shopbtn:
                Toast.makeText(MainActivity.this, "好货切换未做", Toast.LENGTH_SHORT).show();
                break;
            case R.id.activity_main_minebtn:
                Toast.makeText(MainActivity.this, "个人切换未做", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
