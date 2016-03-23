package com.taihuoniao.fineix.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.ImageView;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.beans.TabItem;
import com.taihuoniao.fineix.scene.SelectPhotoOrCameraActivity;

import java.util.ArrayList;

public class MainActivity extends FragmentActivity implements View.OnClickListener {
    private ImageView homepageImg, findImg, sceneImg, shopImg, mineImg;
    private FragmentManager fm;
    private ArrayList<TabItem> tabList;

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
            tabList = new ArrayList<TabItem>();
        }

        if (tabList == null) {
            return;
        }

        homepageImg = (ImageView) findViewById(R.id.activity_main_homepagebtn);
        initTabItem(homepageImg, R.id.activity_main_homepagebtn, R.mipmap.homepage_red, R.mipmap.homepage_grey);

        findImg = (ImageView) findViewById(R.id.activity_main_findbtn);
        initTabItem(findImg, R.id.activity_main_findbtn, R.mipmap.find_red, R.mipmap.find_grey);

        sceneImg = (ImageView) findViewById(R.id.activity_main_scenebtn);

        shopImg = (ImageView) findViewById(R.id.activity_main_shopbtn);
        initTabItem(shopImg, R.id.activity_main_shopbtn, R.mipmap.shop_red, R.mipmap.shop_grey);

        mineImg = (ImageView) findViewById(R.id.activity_main_minebtn);
        initTabItem(mineImg, R.id.activity_main_minebtn, R.mipmap.mine_red, R.mipmap.mine_grey);

        setCurTab(R.id.activity_main_homepagebtn);
    }

    private void initTabItem(ImageView imageView, int resId, int selId, int unselId) {
        TabItem tabItem = new TabItem();
        tabItem.imageView = imageView;
        tabItem.id = resId;
        tabItem.selId = selId;
        tabItem.unselId = unselId;
        tabList.add(tabItem);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_main_scenebtn:
                startActivity(new Intent(MainActivity.this, SelectPhotoOrCameraActivity.class));
                break;
            case R.id.activity_main_homepagebtn://主页
//                startActivity(new Intent(MainActivity.this, LocationActivity.class));

                setCurTab(R.id.activity_main_homepagebtn);
                break;
            case R.id.activity_main_findbtn: //发现
                setCurTab(R.id.activity_main_findbtn);
                break;
            case R.id.activity_main_shopbtn:  //好货
                setCurTab(R.id.activity_main_shopbtn);
                break;
            case R.id.activity_main_minebtn: //个人中心
                setCurTab(R.id.activity_main_minebtn);
                break;
        }
    }

    private void setCurTab(int imgId) {
        for (TabItem tabItem : tabList) {
            int id = tabItem.id;
            if (id == imgId) {
                setTabState(tabItem, id, tabItem.selId);
            } else {
                setTabState(tabItem, id, tabItem.unselId);
            }
        }
    }

    /**
     * 设置选中和未选中状态
     *
     * @param tabItem
     * @param id
     * @param imgId
     */
    private void setTabState(TabItem tabItem, int id, int imgId) {
        switch (id) {
            case R.id.activity_main_homepagebtn:
                tabItem.imageView.setImageResource(imgId);
                break;
            case R.id.activity_main_findbtn:
                tabItem.imageView.setImageResource(imgId);
                break;
            case R.id.activity_main_shopbtn:
                tabItem.imageView.setImageResource(imgId);
                break;
            case R.id.activity_main_minebtn:
                tabItem.imageView.setImageResource(imgId);
                break;
        }
    }
}
