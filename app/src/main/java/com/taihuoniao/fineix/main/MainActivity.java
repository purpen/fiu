package com.taihuoniao.fineix.main;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.beans.LoginInfo;
import com.taihuoniao.fineix.beans.TabItem;
import com.taihuoniao.fineix.interfaces.OnMessageCountChangeListener;
import com.taihuoniao.fineix.main.fragment.CartFragment;
import com.taihuoniao.fineix.main.fragment.DiscoverFragment;
import com.taihuoniao.fineix.main.fragment.IndexFragment;
import com.taihuoniao.fineix.main.fragment.MineFragment;
import com.taihuoniao.fineix.main.tab3.WellGoodsNewFragment;
import com.taihuoniao.fineix.user.OptRegisterLoginActivity;
import com.taihuoniao.fineix.utils.LogUtil;
import com.taihuoniao.fineix.utils.MapUtil;
import com.taihuoniao.fineix.utils.WindowUtils;
import com.taihuoniao.fineix.zxing.activity.CaptureActivityZxing;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionNo;
import com.yanzhenjie.permission.PermissionYes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;

import static com.taihuoniao.fineix.utils.Constants.REQUEST_CODE_SETTING;
import static com.taihuoniao.fineix.utils.Constants.REQUEST_PHONE_STATE_CODE;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.activity_main_container)RelativeLayout container;
    @Bind(R.id.ll_nav0)LinearLayout ll_nav0;
    @Bind(R.id.ll_nav1)LinearLayout ll_nav1;
    @Bind(R.id.ll_nav2)LinearLayout ll_nav2;
    @Bind(R.id.ll_nav3)LinearLayout ll_nav3;
    @Bind(R.id.ll_nav4)LinearLayout ll_nav4;
    @Bind(R.id.activity_main_fragment_group)FrameLayout fragmetnContainer;
    @Bind(R.id.activity_main_bottomlinear)LinearLayout bottomLinear;

    @Bind(R.id.activity_main_homebtn)ImageView homeImg;
    @Bind(R.id.activity_main_findbtn)ImageView findImg;
    @Bind(R.id.activity_main_shopbtn)ImageView shopImg;
    @Bind(R.id.activity_main_minebtn)ImageView mineImg;
    @Bind(R.id.activity_main_cartbtn)ImageView cartImg;

    @Bind(R.id.tv_nav0)TextView tv_nav0;
    @Bind(R.id.tv_nav1)TextView tv_nav1;
    @Bind(R.id.tv_nav3)TextView tv_nav3;
    @Bind(R.id.tv_nav4)TextView tv_nav4;
    @Bind(R.id.tv_nav2)TextView tv_nav2;

    //用户第一次进入app会用到
    @Bind(R.id.activity_main_first_relative)RelativeLayout firstRelative;
    @Bind(R.id.activity_main_first_left_img)ImageView firstLeftImg;
    @Bind(R.id.activity_main_first_right_img)ImageView firstRightImg;
    @Bind(R.id.tv_msg_indicator)TextView tv_msg_indicator;

    private FragmentManager fm;
    private ArrayList<TabItem> tabList;
    private ArrayList<Fragment> fragments;
    private Fragment showFragment;
    public String which = IndexFragment.class.getSimpleName();
    private Boolean isExit = false;

    public MainActivity() {
        super(R.layout.activity_main);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        if (intent.hasExtra(IndexFragment.class.getSimpleName())) {
            which = IndexFragment.class.getSimpleName();
            boolean exit = intent.getBooleanExtra("exit", false);
            if (exit) {
                tv_msg_indicator.setVisibility(View.GONE);
            }
        } else if (intent.hasExtra(WellGoodsNewFragment.class.getSimpleName())) {
            which = WellGoodsNewFragment.class.getSimpleName();
        } else if (intent.hasExtra(DiscoverFragment.class.getSimpleName())) {
            which = DiscoverFragment.class.getSimpleName();
        }
        which2Switch();
        super.onNewIntent(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (Arrays.asList(permissions).contains("android.permission.CAMERA")){
            Fragment visibleFragment = getVisibleFragment();
            if (null == visibleFragment) return;
            AndPermission.onRequestPermissionsResult(visibleFragment, REQUEST_PHONE_STATE_CODE, permissions, grantResults);
        }else {
            AndPermission.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
        }
    }

    @PermissionYes(REQUEST_PHONE_STATE_CODE)
    private void getPhoneStatusYes(List<String> grantedPermissions) {
        LogUtil.e(grantedPermissions.get(0));
        if (grantedPermissions.contains(Manifest.permission.READ_PHONE_STATE)) {
            which2Switch();
        }

        if (grantedPermissions.contains(Manifest.permission.CAMERA)) {
            startActivity(new Intent(this, CaptureActivityZxing.class));
        }

    }

    @PermissionNo(REQUEST_PHONE_STATE_CODE)
    private void getPhoneStatusNo(List<String> deniedPermissions) {
        if (AndPermission.hasAlwaysDeniedPermission(this, deniedPermissions)) {
            AndPermission.defaultSettingDialog(this, REQUEST_CODE_SETTING).show();
        } else {
            activity.finish();
        }
    }

    private void which2Switch() {
        if (TextUtils.equals(IndexFragment.class.getSimpleName(), which)) {
            switchFragmentandImg(IndexFragment.class);
        } else if (TextUtils.equals(MineFragment.class.getSimpleName(), which)) {
            switchFragmentandImg(MineFragment.class);
        } else if (TextUtils.equals(WellGoodsNewFragment.class.getSimpleName(), which)) {
            switchFragmentandImg(WellGoodsNewFragment.class);
        } else if (TextUtils.equals(DiscoverFragment.class.getSimpleName(), which)) {
            switchFragmentandImg(DiscoverFragment.class);
        } else if (TextUtils.equals(CartFragment.class.getSimpleName(), which)) {
            switchFragmentandImg(CartFragment.class);
        }
    }


    @Override
    protected void getIntentData() {
        Intent intent = getIntent();
        if (intent.hasExtra(IndexFragment.class.getSimpleName())) {
            which = IndexFragment.class.getSimpleName();
        } else if (intent.hasExtra(WellGoodsNewFragment.class.getSimpleName())) {
            which = WellGoodsNewFragment.class.getSimpleName();
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
            if (AndPermission.hasPermission(activity, android.Manifest.permission.READ_PHONE_STATE)) {
                which2Switch();
            } else {
                // 申请权限。
                AndPermission.with(this)
                        .requestCode(REQUEST_PHONE_STATE_CODE)
                        .permission(android.Manifest.permission.READ_PHONE_STATE)
                        .send();

            }

        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            WindowUtils.showStatusBar(this);
            firstRelative.setPadding(0, App.getStatusBarHeight(), 0, 0);
        }
    }
    public void hint() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WindowUtils.hide(this);
        }
        ObjectAnimator.ofFloat(bottomLinear, "translationY", bottomLinear.getMeasuredHeight())
                .setDuration(300)
                .start();
    }

    public void show() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WindowUtils.show(this);
        }
        ObjectAnimator.ofFloat(bottomLinear, "translationY", 0)
                .setDuration(300)
                .start();
    }

    private void recoverAllState(Bundle savedInstanceState) {
        Fragment indexFragment = fm.getFragment(savedInstanceState, IndexFragment.class.getSimpleName());
        addFragment2List(indexFragment);
        Fragment findFragment = fm.getFragment(savedInstanceState, DiscoverFragment.class.getSimpleName());
        addFragment2List(findFragment);
        Fragment WellGoodsNewFragment = fm.getFragment(savedInstanceState, WellGoodsNewFragment.class.getSimpleName());
        addFragment2List(WellGoodsNewFragment);
        Fragment mineFragment = fm.getFragment(savedInstanceState, MineFragment.class.getSimpleName());
        addFragment2List(mineFragment);
        Fragment cartFragment = fm.getFragment(savedInstanceState, CartFragment.class.getSimpleName());
        addFragment2List(cartFragment);
        Class clazz = (Class) savedInstanceState.getSerializable(MainActivity.class.getSimpleName());
        if (clazz == null) return;
        LogUtil.e(TAG, clazz.getSimpleName() + "///////////");
        switchFragmentandImg(clazz);
    }

    private void addFragment2List(Fragment fragment) {
        if (fragment == null || fragments.contains(fragment)) {
            LogUtil.e(TAG, "addedFragment==null || addedFragment  contains");
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

        MineFragment.setOnMessageCountChangeListener(new OnMessageCountChangeListener() {
            @Override
            public void onMessageCountChange(int count) {
                if (count > 0) {
                    tv_msg_indicator.setVisibility(View.VISIBLE);
                } else {
                    tv_msg_indicator.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    protected void initView() {
        if (tabList != null) {
            tabList.clear();
        } else {
            tabList = new ArrayList<>();
        }
        initTabItem(homeImg, tv_nav0, IndexFragment.class, R.mipmap.icon_home_pressed, R.mipmap.icon_home_normal);
        initTabItem(findImg, tv_nav1, DiscoverFragment.class, R.mipmap.icon_discover_pressed, R.mipmap.icon_discover_normal);
        initTabItem(shopImg, tv_nav3, WellGoodsNewFragment.class, R.mipmap.icon_shop_pressed, R.mipmap.icon_shop_normal);
        initTabItem(cartImg, tv_nav2, CartFragment.class, R.mipmap.icon_cart_pressed, R.mipmap.icon_cart_normal);
        initTabItem(mineImg, tv_nav4, MineFragment.class, R.mipmap.icon_mine_pressed, R.mipmap.icon_mine_normal);
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
                if (getVisibleFragment() instanceof CartFragment) return;
                if (LoginInfo.isUserLogin()) {
                    switchFragmentandImg(CartFragment.class);
                } else {
                    MainApplication.which_activity = 0;
                    which = CartFragment.class.getSimpleName();
                    removeMineFragment(CartFragment.class);
                    startActivity(new Intent(activity, OptRegisterLoginActivity.class));
                }
                break;
            case R.id.ll_nav0://情
                switchFragmentandImg(IndexFragment.class);
                break;
            case R.id.ll_nav1: //景
                switchFragmentandImg(DiscoverFragment.class);
                onWindowFocusChanged(true);
                break;
            case R.id.ll_nav3:  //品
                switchFragmentandImg(WellGoodsNewFragment.class);
                onWindowFocusChanged(true);
                break;
            case R.id.ll_nav4: //个人中心
                if (getVisibleFragment() instanceof MineFragment) return;
                if (LoginInfo.isUserLogin()) {
                    switchFragmentandImg(MineFragment.class);
                } else {
                    MainApplication.which_activity = 0;
                    which = MineFragment.class.getSimpleName();
                    removeMineFragment(MineFragment.class);
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
        Fragment fragment = fm.findFragmentByTag(clazz.getSimpleName());
        if (fragment == null) {
            fragment = (Fragment) clazz.newInstance();
            fm.beginTransaction().add(R.id.activity_main_fragment_group, fragment, clazz.getSimpleName()).commitAllowingStateLoss();
        } else {
            fm.beginTransaction().show(fragment).commitAllowingStateLoss();
        }
        addFragment2List(fragment);
        showFragment = fragment;
    }

    private void resetUI() {
        if (fragments == null || fragments.size() == 0) {
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
                tabItem.tv.setTextColor(getResources().getColor(R.color.yellow_bd8913));
            } else {
                tabItem.imageView.setImageResource(tabItem.unselId);
                tabItem.tv.setTextColor(getResources().getColor(R.color.white));
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        int size = fragments.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                fm.putFragment(outState, fragments.get(i).getTag(), fragments.get(i));
            }
        }
        if (showFragment != null) {
            outState.putSerializable(MainActivity.class.getSimpleName(), showFragment.getClass());
        }
        super.onSaveInstanceState(outState);
    }

    public Fragment getVisibleFragment() {
        if (null == fragments) return null;
        for (Fragment fragment : fragments) {
            if (fragment != null && fragment.isVisible())
                return fragment;
        }
        return null;
    }

    @Override
    protected void onDestroy() {
        MapUtil.destroyGeoCoder();
        super.onDestroy();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        for (Fragment fragment : fragments) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
        super.onActivityResult(requestCode, resultCode, data);

    }

    /**
     * 菜单、返回键响应
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitBy2Click(); //调用双击退出函数
        }
        return false;
    }

    private void exitBy2Click() {
        Timer tExit;
        if (!isExit) {
            isExit = true;
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            tExit = new Timer();
            tExit.schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false;
                }
            }, 2000);

        } else {
            finish();
            System.exit(0);
        }
    }

    private void removeMineFragment(Class clazz) {
        Fragment fragment = fm.findFragmentByTag(clazz.getSimpleName());
        if (fragment != null) {
            fm.beginTransaction().remove(fragment).commitAllowingStateLoss();
        }
        for (int i = 0; i < fragments.size(); i++) {
            if (fragments.get(i) == fragment) {
                fragments.remove(i);
                break;
            }
        }
    }
}
