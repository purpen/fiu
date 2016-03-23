package com.taihuoniao.fineix.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.ImageView;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.scene.SelectPhotoOrCameraActivity;

import java.util.ArrayList;

public class MainActivity extends FragmentActivity implements View.OnClickListener {
    private ImageView homepageImg, findImg, sceneImg, shopImg, mineImg;
    private FragmentManager fm;
    private ArrayList<ImageView> tabList;

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
        if (tabList != null) {
            tabList.clear();
        } else {
            tabList = new ArrayList<ImageView>();
        }

        if (tabList == null) {
            return;
        }
        homepageImg = (ImageView) findViewById(R.id.activity_main_homepagebtn);
        tabList.add(homepageImg);
        findImg = (ImageView) findViewById(R.id.activity_main_findbtn);
        tabList.add(findImg);
        sceneImg = (ImageView) findViewById(R.id.activity_main_scenebtn);
        shopImg = (ImageView) findViewById(R.id.activity_main_shopbtn);
        tabList.add(shopImg);
        mineImg = (ImageView) findViewById(R.id.activity_main_minebtn);
        tabList.add(mineImg);
        setCurTab(tabList.get(0));
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_main_scenebtn:
                startActivity(new Intent(MainActivity.this, SelectPhotoOrCameraActivity.class));
                break;
            case R.id.activity_main_homepagebtn://主页
//                startActivity(new Intent(MainActivity.this, LocationActivity.class));
                setCurTab(v);
                break;
            case R.id.activity_main_findbtn: //发现
                setCurTab(v);
                break;
            case R.id.activity_main_shopbtn:  //好货
                setCurTab(v);
                break;
            case R.id.activity_main_minebtn: //个人中心
                setCurTab(v);
                break;
        }
    }

    private void setCurTab(View v) {
        for (ImageView img : tabList) {
            int id = img.getId();
            if (id == v.getId()) {
                switch (id) {
                    case R.id.activity_main_homepagebtn:
                        img.setImageResource(R.mipmap.homepage_red);
                        break;
                    case R.id.activity_main_findbtn:
                        img.setImageResource(R.mipmap.find_red);
                        break;
                    case R.id.activity_main_shopbtn:
                        img.setImageResource(R.mipmap.shop_red);
                        break;
                    case R.id.activity_main_minebtn:
                        img.setImageResource(R.mipmap.mine_red);
                        break;
                }
            } else {
                switch (id) {
                    case R.id.activity_main_homepagebtn:
                        img.setImageResource(R.mipmap.homepage_grey);
                        break;
                    case R.id.activity_main_findbtn:
                        img.setImageResource(R.mipmap.find_grey);
                        break;
                    case R.id.activity_main_shopbtn:
                        img.setImageResource(R.mipmap.shop_grey);
                        break;
                    case R.id.activity_main_minebtn:
                        img.setImageResource(R.mipmap.mine_grey);
                        break;
                }
            }
        }
    }
}
