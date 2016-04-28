package com.taihuoniao.fineix.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.beans.TabItem;
import com.taihuoniao.fineix.main.fragment.FindFragment;
import com.taihuoniao.fineix.main.fragment.IndexFragment;
import com.taihuoniao.fineix.main.fragment.MineFragment;
import com.taihuoniao.fineix.main.fragment.WellGoodsFragment;
import com.taihuoniao.fineix.scene.SelectPhotoOrCameraActivity;
import com.taihuoniao.fineix.utils.MapUtil;

import java.util.ArrayList;

import butterknife.Bind;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.ll_nav0)
    LinearLayout ll_nav0;
    @Bind(R.id.ll_nav1)
    LinearLayout ll_nav1;
    @Bind(R.id.ll_nav2)
    LinearLayout ll_nav2;
    @Bind(R.id.ll_nav3)
    LinearLayout ll_nav3;
    @Bind(R.id.ll_nav4)
    LinearLayout ll_nav4;

//    @Bind(R.id.custom_head)
//    CustomHeadView custom_head;

    @Bind(R.id.activity_main_homepagebtn)
    ImageView homepageImg;

    @Bind(R.id.activity_main_findbtn)
    ImageView findImg;

    @Bind(R.id.activity_main_shopbtn)
    ImageView shopImg;

    @Bind(R.id.activity_main_minebtn)
    ImageView mineImg;

    @Bind(R.id.tv_nav0)
    TextView tv_nav0;
    @Bind(R.id.tv_nav1)
    TextView tv_nav1;
    @Bind(R.id.tv_nav3)
    TextView tv_nav3;
    @Bind(R.id.tv_nav4)
    TextView tv_nav4;
    private FragmentManager fm;
    private ArrayList<TabItem> tabList;
    private Fragment from, to;
    private ArrayList<Fragment> fragments;

    public MainActivity() {
        super(R.layout.activity_main);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        fm = getSupportFragmentManager();
//        WindowUtils.immerseStatusBar(MainActivity.this);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void installListener() {
        ll_nav0.setOnClickListener(this);
        ll_nav1.setOnClickListener(this);
        ll_nav2.setOnClickListener(this);
        ll_nav3.setOnClickListener(this);
        ll_nav4.setOnClickListener(this);
    }

    @Override
    protected void initView() {
//        custom_head.setHeadGoBackShow(false);
//        custom_head.setHeadLogoShow(true);
        if (tabList != null) {
            tabList.clear();
        } else {
            tabList = new ArrayList<TabItem>();
        }
//        homepageImg = (ImageView) findViewById(R.id.activity_main_homepagebtn);
        initTabItem(homepageImg, tv_nav0, R.id.activity_main_homepagebtn, R.mipmap.home_red, R.mipmap.home_gray);

//        findImg = (ImageView) findViewById(R.id.activity_main_findbtn);
        initTabItem(findImg, tv_nav1, R.id.activity_main_findbtn, R.mipmap.find_red, R.mipmap.find_gray);

//        sceneImg = (ImageView) findViewById(R.id.activity_main_scenebtn);

//        shopImg = (ImageView) findViewById(R.id.activity_main_shopbtn);
        initTabItem(shopImg, tv_nav3, R.id.activity_main_shopbtn, R.mipmap.shop_red, R.mipmap.shop_gray);

//        mineImg = (ImageView) findViewById(R.id.activity_main_minebtn);
        initTabItem(mineImg, tv_nav4, R.id.activity_main_minebtn, R.mipmap.mine_red, R.mipmap.mine_gray);

        switchFragmentandImg(R.id.activity_main_homepagebtn, IndexFragment.class);
    }

    private void initTabItem(ImageView imageView, TextView tv, int resId, int selId, int unselId) {
        TabItem tabItem = new TabItem();
        tabItem.imageView = imageView;
        tabItem.tv = tv;
        tabItem.id = resId;
        tabItem.selId = selId;
        tabItem.unselId = unselId;
        tabList.add(tabItem);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_nav2:
                MainApplication.tag = 1;
                startActivity(new Intent(MainActivity.this, SelectPhotoOrCameraActivity.class));
                break;
            case R.id.ll_nav0://情
//                custom_head.setVisibility(View.GONE);
                switchFragmentandImg(R.id.activity_main_homepagebtn, IndexFragment.class);
                break;
            case R.id.ll_nav1: //景
//                custom_head.setVisibility(View.GONE);
                switchFragmentandImg(R.id.activity_main_findbtn, FindFragment.class);
                break;
            case R.id.ll_nav3:  //品
//                custom_head.setVisibility(View.GONE);
                switchFragmentandImg(R.id.activity_main_shopbtn, WellGoodsFragment.class);
                break;
            case R.id.ll_nav4: //个人中心
//                custom_head.setVisibility(View.GONE);
                switchFragmentandImg(R.id.activity_main_minebtn, MineFragment.class);
                break;
        }
    }

    private void switchFragmentandImg(int id, Class clazz) {
        try {
            switchImgAndTxtStyle(id);
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
        resetUI();
        fragments=new ArrayList<>();
        to = fm.findFragmentByTag(clazz.getSimpleName());
        if (to == null) {
            if (from != null) {
                fm.beginTransaction().hide(from).commit();
            } else {
                //to和from都为空,不会出现
            }
            to = (Fragment) clazz.newInstance();
            fm.beginTransaction().add(R.id.activity_main_fragment_group, to, clazz.getSimpleName()).commit();
            fragments.add(to);
        } else {
            if (to == from) {
//                LogUtil.e("to==from", (to == from)  "");
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

    private void resetUI() {
        if (fragments == null) {
            return;
        }
        if (fragments.size() == 0) {
            return;
        }

        for (Fragment fragment : fragments) {
            fm.beginTransaction().hide(fragment).commit();
        }
    }

    private void switchImgAndTxtStyle(int imgId) {
        for (TabItem tabItem : tabList) {
            int id = tabItem.id;
            if (id == imgId) {
                tabItem.imageView.setImageResource(tabItem.selId);
                tabItem.tv.setTextColor(getResources().getColor(R.color.color_af8323));
            } else {
                tabItem.imageView.setImageResource(tabItem.unselId);
                tabItem.tv.setTextColor(getResources().getColor(R.color.color_333));
            }
        }
    }

    @Override
    protected void onDestroy() {
        MapUtil.destroyGeoCoder();
        super.onDestroy();
    }
}
