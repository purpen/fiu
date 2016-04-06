package com.taihuoniao.fineix.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.ImageView;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.beans.TabItem;
import com.taihuoniao.fineix.main.fragment.FindFragment;
import com.taihuoniao.fineix.main.fragment.IndexFragment;
import com.taihuoniao.fineix.main.fragment.PersonalCenterFragment;
import com.taihuoniao.fineix.main.fragment.WellGoodsFragment;
import com.taihuoniao.fineix.scene.SelectPhotoOrCameraActivity;
import com.taihuoniao.fineix.utils.LogUtil;
import java.util.ArrayList;
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

        switchFragmentandImg(R.id.activity_main_homepagebtn, IndexFragment.class);
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
                switchFragmentandImg(R.id.activity_main_homepagebtn,IndexFragment.class);
                break;
            case R.id.activity_main_findbtn: //发现
                switchFragmentandImg(R.id.activity_main_findbtn,FindFragment.class);
                break;
            case R.id.activity_main_shopbtn:  //好货
                switchFragmentandImg(R.id.activity_main_shopbtn,WellGoodsFragment.class);
                break;
            case R.id.activity_main_minebtn: //个人中心
                switchFragmentandImg(R.id.activity_main_minebtn,PersonalCenterFragment.class);
                break;
        }
    }

    private void switchFragmentandImg(int id,Class clazz) {
        try {
            switchImg(id);
            switchFragment(clazz);
        } catch (Exception e) {
            e.printStackTrace();
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
            if (from != null) {
                fm.beginTransaction().hide(from).commit();
            }else {
                //to和from都为空,不会出现
            }
            to = (Fragment) clazz.newInstance();
            fm.beginTransaction().add(R.id.activity_main_fragment_group, to, clazz.getSimpleName()).commit();
        } else {
            if (to==from){
                LogUtil.e("to==from",(to==from)+"");
                return;
            }

            if (from != null) {
                fm.beginTransaction().hide(from).show(to).commit();
            } else {
                //首次进入主页才会出现
            }
        }
        from = to;
    }


    private void switchImg(int imgId) {
        for (TabItem tabItem : tabList) {
            int id = tabItem.id;
            if (id == imgId) {
                tabItem.imageView.setImageResource(tabItem.selId);
            } else {
                tabItem.imageView.setImageResource(tabItem.unselId);
            }
        }
    }
}
