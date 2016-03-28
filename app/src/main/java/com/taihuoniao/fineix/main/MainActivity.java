package com.taihuoniao.fineix.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.ImageView;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.beans.TabItem;
import com.taihuoniao.fineix.scene.fragments.FindFragment;
import com.taihuoniao.fineix.scene.fragments.IndexFragment;
import com.taihuoniao.fineix.scene.fragments.PersonalCenterFragment;
import com.taihuoniao.fineix.scene.fragments.WellGoodsFragment;

import java.util.ArrayList;

//import com.taihuoniao.fineix.scene.SelectPhotoOrCameraActivity;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    private ImageView homepageImg, findImg, sceneImg, shopImg, mineImg;
    private FragmentManager fm;
    private ArrayList<TabItem> tabList;
    private Fragment from, to;

    public MainActivity() {
        super(R.layout.activity_main);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        fm = getSupportFragmentManager();
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void installListener() {
        homepageImg.setOnClickListener(this);
        findImg.setOnClickListener(this);
        sceneImg.setOnClickListener(this);
        shopImg.setOnClickListener(this);
        mineImg.setOnClickListener(this);
    }

    @Override
    protected void initView() {
        if (tabList != null) {
            tabList.clear();
        } else {
            tabList = new ArrayList<TabItem>();
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
//        to = from = new IndexFragment();
        try {
            switchFragment(IndexFragment.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
//        fm.beginTransaction().add(R.id.activity_main_fragment_group, to, IndexFragment.class.getSimpleName()).commit();
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
//                startActivity(new Intent(MainActivity.this, SelectPhotoOrCameraActivity.class));
                break;
            case R.id.activity_main_homepagebtn://主页

                try {
                    setCurTab(R.id.activity_main_homepagebtn);
                    switchFragment(IndexFragment.class);
                } catch (Exception e) {
                    e.printStackTrace();
                }
//                startActivity(new Intent(MainActivity.this, LocationActivity.class));
                break;
            case R.id.activity_main_findbtn: //发现
                try {
                    setCurTab(R.id.activity_main_findbtn);
                    switchFragment(FindFragment.class);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.activity_main_shopbtn:  //好货
                try {
                    setCurTab(R.id.activity_main_shopbtn);
                    switchFragment(WellGoodsFragment.class);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.activity_main_minebtn: //个人中心
                try {
                    setCurTab(R.id.activity_main_minebtn);
                    switchFragment(PersonalCenterFragment.class);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }


    /**
     * 切换fragment
     *
     * @param clazz
     * @throws Exception
     */
    private void switchFragment(Class clazz) throws Exception {
        to = fm.findFragmentByTag(clazz.getSimpleName());
        if (to == null) {
            to = (Fragment) clazz.newInstance();
            if (from != null) {
                fm.beginTransaction().hide(from).commit();
            }
            fm.beginTransaction().add(R.id.activity_main_fragment_group, to, clazz.getSimpleName()).commit();
        } else {
            if (from != null) {
                fm.beginTransaction().hide(from).show(to).commit();
            } else {
                //TODO
            }
        }
        from = to;
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
