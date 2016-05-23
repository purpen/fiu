package com.taihuoniao.fineix.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.beans.LoginInfo;
import com.taihuoniao.fineix.beans.TabItem;
import com.taihuoniao.fineix.main.fragment.FindFragment;
import com.taihuoniao.fineix.main.fragment.IndexFragment;
import com.taihuoniao.fineix.main.fragment.MineFragment;
import com.taihuoniao.fineix.main.fragment.WellGoodsFragment;
import com.taihuoniao.fineix.network.DataConstants;
import com.taihuoniao.fineix.scene.SelectPhotoOrCameraActivity;
import com.taihuoniao.fineix.user.OptRegisterLoginActivity;
import com.taihuoniao.fineix.utils.LogUtil;
import com.taihuoniao.fineix.utils.MapUtil;
import com.taihuoniao.fineix.utils.WindowUtils;

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
    @Bind(R.id.activity_main_fragment_group)
    FrameLayout fragmetnContainer;
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
    private ArrayList<Fragment> fragments;
    private Fragment showFragment;
    private String which = IndexFragment.class.getSimpleName();

    public MainActivity() {
        super(R.layout.activity_main);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        if (intent.hasExtra(IndexFragment.class.getSimpleName())) {
            which = IndexFragment.class.getSimpleName();
        }
        which2Switch();
        super.onNewIntent(intent);
    }

    private void which2Switch() {
        if (TextUtils.equals(IndexFragment.class.getSimpleName(), which)) {
            switchFragmentandImg(IndexFragment.class);
        } else if (TextUtils.equals(MineFragment.class.getSimpleName(), which)) {
            switchFragmentandImg(MineFragment.class);
        }
    }

    @Override
    protected void getIntentData() {
        Intent intent = getIntent();
        if (intent.hasExtra(IndexFragment.class.getSimpleName())) {
            which = IndexFragment.class.getSimpleName();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (fragments == null) {
            fragments = new ArrayList<>();
        }
        fm = getSupportFragmentManager();
        if (savedInstanceState != null) {
            recoverAllState(savedInstanceState);
        } else {
//            if (TextUtils.equals(IndexFragment.class.getSimpleName(),which)){
//                switchFragmentandImg(IndexFragment.class);
//            }
            which2Switch();
        }
        IntentFilter intentFilter = new IntentFilter(DataConstants.BroadShopCart);
        registerReceiver(mainReceiver, intentFilter);
//        WindowUtils.immerseStatusBar(MainActivity.this);
        WindowUtils.chenjin(MainActivity.this);
    }

    private void recoverAllState(Bundle savedInstanceState) {
        Fragment indexFragment = fm.getFragment(savedInstanceState, IndexFragment.class.getSimpleName());
        addFragment2List(indexFragment);
        Fragment findFragment = fm.getFragment(savedInstanceState, FindFragment.class.getSimpleName());
        addFragment2List(findFragment);
        Fragment wellGoodsFragment = fm.getFragment(savedInstanceState, WellGoodsFragment.class.getSimpleName());
        addFragment2List(wellGoodsFragment);
        Fragment mineFragment = fm.getFragment(savedInstanceState, MineFragment.class.getSimpleName());
        addFragment2List(mineFragment);
        Class clazz = (Class) savedInstanceState.getSerializable(MainActivity.class.getSimpleName());
        if (clazz == null) return;
        LogUtil.e(TAG, clazz.getSimpleName() + "///////////");
        switchFragmentandImg(clazz);
    }

    private void addFragment2List(Fragment fragment) {
        if (fragment == null) {
            LogUtil.e(TAG, "addedFragment==null");
            return;
        }

        if (fragments.contains(fragment)) {
            LogUtil.e(TAG, "addedFragment  contains");
            return;
        }

        fragments.add(fragment);
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
        initTabItem(homepageImg, tv_nav0, IndexFragment.class, R.mipmap.home_red, R.mipmap.home_gray);

//        findImg = (ImageView) findViewById(R.id.activity_main_findbtn);
        initTabItem(findImg, tv_nav1, FindFragment.class, R.mipmap.find_red, R.mipmap.find_gray);

//        sceneImg = (ImageView) findViewById(R.id.activity_main_scenebtn);

//        shopImg = (ImageView) findViewById(R.id.activity_main_shopbtn);
        initTabItem(shopImg, tv_nav3, WellGoodsFragment.class, R.mipmap.shop_red, R.mipmap.shop_gray);

//        mineImg = (ImageView) findViewById(R.id.activity_main_minebtn);
        initTabItem(mineImg, tv_nav4, MineFragment.class, R.mipmap.mine_red, R.mipmap.mine_gray);


    }


    private void initTabItem(ImageView imageView, TextView tv, Class clazz, int selId, int unselId) {
        TabItem tabItem = new TabItem();
        tabItem.imageView = imageView;
        tabItem.tv = tv;
        tabItem.clazz = clazz;
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
                switchFragmentandImg(IndexFragment.class);
                break;
            case R.id.ll_nav1: //景
//                custom_head.setVisibility(View.GONE);
                switchFragmentandImg(FindFragment.class);
                break;
            case R.id.ll_nav3:  //品
//                custom_head.setVisibility(View.GONE);
                Log.e("<<<", "点击切换");
                switchFragmentandImg(WellGoodsFragment.class);
                break;
            case R.id.ll_nav4: //个人中心
//                custom_head.setVisibility(View.GONE);
                if (LoginInfo.isUserLogin()) {
                    switchFragmentandImg(MineFragment.class);
                } else {
                    which = MineFragment.class.getSimpleName();
                    startActivity(new Intent(activity, OptRegisterLoginActivity.class));
                }
                break;
        }
    }

    private void switchFragmentandImg(Class clazz) {
        resetUI();
        try {
            switchImgAndTxtStyle(clazz);
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
        LogUtil.e(TAG, "<<<<<<<<" + clazz.getSimpleName());
        Fragment fragment = fm.findFragmentByTag(clazz.getSimpleName());
        if (fragment == null) {
            fragment = (Fragment) clazz.newInstance();
            fm.beginTransaction().add(R.id.activity_main_fragment_group, fragment, clazz.getSimpleName()).commitAllowingStateLoss();
        } else {
            fm.beginTransaction().show(fragment).commitAllowingStateLoss();
        }
        addFragment2List(fragment);
        showFragment = fragment;
        LogUtil.e(TAG, fragments.size() + "<<<<<<");
    }

    private void resetUI() {
        if (fragments == null) {
            return;
        }
        if (fragments.size() == 0) {
            return;
        }

        for (Fragment fragment : fragments) {
            fm.beginTransaction().hide(fragment).commitAllowingStateLoss();
        }
    }

    private void switchImgAndTxtStyle(Class clazz) {
        for (TabItem tabItem : tabList) {
            if (tabItem.clazz.equals(clazz)) {
                tabItem.imageView.setImageResource(tabItem.selId);
                tabItem.tv.setTextColor(getResources().getColor(R.color.color_af8323));
            } else {
                tabItem.imageView.setImageResource(tabItem.unselId);
                tabItem.tv.setTextColor(getResources().getColor(R.color.color_333));
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        int size = fragments.size();
        if (fragments != null && size > 0) {
            for (int i = 0; i < size; i++) {
                fm.putFragment(outState, fragments.get(i).getTag(), fragments.get(i));
            }
        }
        if (showFragment != null) {
            outState.putSerializable(MainActivity.class.getSimpleName(), showFragment.getClass());
        }
        super.onSaveInstanceState(outState);
    }


    @Override
    protected void onDestroy() {
        unregisterReceiver(mainReceiver);
        MapUtil.destroyGeoCoder();
        super.onDestroy();
    }

//    @Override
//    public void onWindowFocusChanged(boolean hasFocus) {
//        super.onWindowFocusChanged(hasFocus);
//        if (hasFocus) {
//            SharedPreferences firstInSp = getSharedPreferences(DataConstants.SHAREDPREFRENCES_FIRST_IN, Context.MODE_PRIVATE);
//            if (showFragment instanceof IndexFragment) {
//                //判断是不是第一次进入情界面
//                boolean isFirstIn = firstInSp.getBoolean(DataConstants.FIRST_IN_QING, true);
//                if (isFirstIn) {
//                    FirstInAppUtils.showPop(MainActivity.this, FirstInAppUtils.QING, fragmetnContainer);
//                    SharedPreferences.Editor editor = firstInSp.edit();
//                    editor.putBoolean(DataConstants.FIRST_IN_QING, false);
//                    editor.apply();
//                }
//            } else if (showFragment instanceof FindFragment) {
//                boolean isFirstIn = firstInSp.getBoolean(DataConstants.FIRST_IN_JING, true);
//                if (isFirstIn) {
//                    FirstInAppUtils.showPop(MainActivity.this, FirstInAppUtils.JING, fragmetnContainer);
//                    SharedPreferences.Editor editor = firstInSp.edit();
//                    editor.putBoolean(DataConstants.FIRST_IN_JING, false);
//                    editor.apply();
//                }
//            } else if (showFragment instanceof WellGoodsFragment) {
//                boolean isFirstIn = firstInSp.getBoolean(DataConstants.FIRST_IN_PIN, true);
//                if (isFirstIn) {
//                    FirstInAppUtils.showPop(MainActivity.this, FirstInAppUtils.PIN, fragmetnContainer);
//                    SharedPreferences.Editor editor = firstInSp.edit();
//                    editor.putBoolean(DataConstants.FIRST_IN_PIN, false);
//                    editor.apply();
//                }
//            } else if (showFragment instanceof MineFragment) {
//                boolean isFirstIn = firstInSp.getBoolean(DataConstants.FIRST_IN_WO, true);
//                if (isFirstIn) {
//                    FirstInAppUtils.showPop(MainActivity.this, FirstInAppUtils.WO, fragmetnContainer);
//                    SharedPreferences.Editor editor = firstInSp.edit();
//                    editor.putBoolean(DataConstants.FIRST_IN_WO, false);
//                    editor.apply();
//                }
//            }
//        }
//    }

    private BroadcastReceiver mainReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e("<<<", "接收到广播");
            onClick(ll_nav3);
        }
    };
}
