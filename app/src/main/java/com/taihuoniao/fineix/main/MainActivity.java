package com.taihuoniao.fineix.main;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
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
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.activity_main_container)
    RelativeLayout container;
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
    @Bind(R.id.activity_main_bottomlinear)
    LinearLayout bottomLinear;
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
    //用户第一次进入app会用到
    @Bind(R.id.activity_main_first_relative)
    RelativeLayout firstRelative;
    @Bind(R.id.activity_main_first_img)
    ImageView firstImg;
    @Bind(R.id.activity_main_first_left_img)
    ImageView firstLeftImg;
    @Bind(R.id.activity_main_first_right_img)
    ImageView firstRightImg;
    @Bind(R.id.tv_msg_indicator)
    TextView tv_msg_indicator;
    private FragmentManager fm;
    private ArrayList<TabItem> tabList;
    private ArrayList<Fragment> fragments;
    private Fragment showFragment;
    public String which = IndexFragment.class.getSimpleName();

    public MainActivity() {
        super(R.layout.activity_main);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        if (intent.hasExtra(IndexFragment.class.getSimpleName())) {
            which = IndexFragment.class.getSimpleName();
            boolean exit = intent.getBooleanExtra("exit", false);
            if (exit) tv_msg_indicator.setVisibility(View.GONE);
        } else if (intent.hasExtra(WellGoodsFragment.class.getSimpleName())) {
            which = WellGoodsFragment.class.getSimpleName();
            if (bottomLinear != null) {
                bottomLinear.setTranslationY(0);
                animFlag = 0;
            }
        }else if(intent.hasExtra(FindFragment.class.getSimpleName())){
            which = FindFragment.class.getSimpleName();
        }
        which2Switch();

        super.onNewIntent(intent);
    }

    private void which2Switch() {
        if (TextUtils.equals(IndexFragment.class.getSimpleName(), which)) {
            switchFragmentandImg(IndexFragment.class);
        } else if (TextUtils.equals(MineFragment.class.getSimpleName(), which)) {
            switchFragmentandImg(MineFragment.class);
        } else if (TextUtils.equals(WellGoodsFragment.class.getSimpleName(), which)) {
            switchFragmentandImg(WellGoodsFragment.class);
        }else if(TextUtils.equals(FindFragment.class.getSimpleName(),which)){
            switchFragmentandImg(FindFragment.class);
        }
    }

    @Override
    protected void getIntentData() {
        Intent intent = getIntent();
        if (intent.hasExtra(IndexFragment.class.getSimpleName())) {
            which = IndexFragment.class.getSimpleName();
        } else if (intent.hasExtra(WellGoodsFragment.class.getSimpleName())) {
            which = WellGoodsFragment.class.getSimpleName();
            if (bottomLinear != null) {
                bottomLinear.setTranslationY(0);
                animFlag = 0;
            }
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewGroup.LayoutParams layoutParams = firstImg.getLayoutParams();
        layoutParams.width = MainApplication.getContext().getScreenWidth();
        layoutParams.height = layoutParams.width * 1013 / 750;
        firstImg.setLayoutParams(layoutParams);
        if (fragments == null) {
            fragments = new ArrayList<>();
        }
        fm = getSupportFragmentManager();
        if (savedInstanceState != null) {
            recoverAllState(savedInstanceState);
        } else {
            which2Switch();
        }
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(DataConstants.BroadShopCart);
        registerReceiver(mainReceiver, intentFilter);
        WindowUtils.showStatusBar(this);
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
        initTabItem(homepageImg, tv_nav0, IndexFragment.class, R.mipmap.home_red, R.mipmap.home_gray);

        initTabItem(findImg, tv_nav1, FindFragment.class, R.mipmap.find_red, R.mipmap.find_gray);

        initTabItem(shopImg, tv_nav3, WellGoodsFragment.class, R.mipmap.shop_red, R.mipmap.shop_gray);

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
//                SharedPreferences firstInSp = getSharedPreferences(DataConstants.SHAREDPREFRENCES_FIRST_IN, Context.MODE_PRIVATE);
//                boolean first = firstInSp.getBoolean(DataConstants.FIRST_IN_FIU, true);
//                if (first) {
//                    firstRelative.setVisibility(View.VISIBLE);
//                    firstRelative.setBackgroundResource(R.color.black_touming_80);
//                    firstImg.setImageResource(R.mipmap.first_in_fiu);
//                    firstRelative.setTag(2);
//                    firstImg.setVisibility(View.VISIBLE);
//                    firstRelative.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            if ((int) (v.getTag()) == 2) {
//                                firstRelative.setVisibility(View.GONE);
//                                firstImg.setVisibility(View.GONE);
//                            }
//                        }
//                    });
//                    SharedPreferences.Editor editor = firstInSp.edit();
//                    editor.putBoolean(DataConstants.FIRST_IN_FIU, false);
//                    editor.apply();
//                    return;
//                }
                if (LoginInfo.isUserLogin()) {
//                    MainApplication.tag = 1;
                    startActivity(new Intent(MainActivity.this, SelectPhotoOrCameraActivity.class));
                } else {
                    MainApplication.which_activity = 0;
                    startActivity(new Intent(activity, OptRegisterLoginActivity.class));
                }
                break;
            case R.id.ll_nav0://情
                switchFragmentandImg(IndexFragment.class);
                break;
            case R.id.ll_nav1: //景
                switchFragmentandImg(FindFragment.class);
                onWindowFocusChanged(true);
                break;
            case R.id.ll_nav3:  //品
//                Log.e("<<<", "点击切换");
                switchFragmentandImg(WellGoodsFragment.class);
                onWindowFocusChanged(true);
                break;
            case R.id.ll_nav4: //个人中心
                if (getVisibleFragment() instanceof MineFragment) return;
                if (LoginInfo.isUserLogin()) {
                    switchFragmentandImg(MineFragment.class);
                    onWindowFocusChanged(true);
                } else {
                    MainApplication.which_activity = 0;
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
            if (bottomLinear != null) {
                bottomLinear.setTranslationY(0);
                animFlag = 0;
            }
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


    public Fragment getVisibleFragment() {
        List<Fragment> fragments = fm.getFragments();
        for (Fragment fragment : fragments) {
            if (fragment != null && fragment.isVisible())
                return fragment;
        }
        return null;
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(mainReceiver);
        MapUtil.destroyGeoCoder();
        super.onDestroy();
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            final SharedPreferences firstInSp = getSharedPreferences(DataConstants.SHAREDPREFRENCES_FIRST_IN, Context.MODE_PRIVATE);
            if (showFragment instanceof IndexFragment) {
                //判断是不是第一次进入情界面
                boolean isFirstIn = firstInSp.getBoolean(DataConstants.FIRST_IN_QING, true);
                if (isFirstIn) {
                    firstRelative.setVisibility(View.VISIBLE);
                    firstRelative.setBackgroundResource(R.color.black_touming_80);
                    firstLeftImg.setImageResource(R.mipmap.first_in_index);
                    firstLeftImg.setVisibility(View.VISIBLE);
                    firstRelative.setTag(1);
                    firstRelative.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if ((int) (v.getTag()) == 1) {
                                firstLeftImg.setVisibility(View.GONE);
                                firstRelative.setBackgroundResource(R.color.black_touming_80);
                                firstRightImg.setImageResource(R.mipmap.first_in_index2);
                                firstRightImg.setVisibility(View.VISIBLE);
                                firstRelative.setTag(6);
                            } else if ((int) (v.getTag()) == 6) {
                                firstRelative.setVisibility(View.GONE);
                                firstRightImg.setVisibility(View.GONE);
                                firstRelative.setBackgroundResource(R.color.nothing);
                                firstRelative.setPadding(0, 0, 0, 0);
                            }
                        }
                    });
                    SharedPreferences.Editor editor = firstInSp.edit();
                    editor.putBoolean(DataConstants.FIRST_IN_QING, false);
                    editor.apply();
                }
            } else if (showFragment instanceof FindFragment) {
                boolean isFirstIn = firstInSp.getBoolean(DataConstants.FIRST_IN_JING, true);
                if (isFirstIn) {
                    firstRelative.setVisibility(View.VISIBLE);
                    firstRelative.setBackgroundResource(R.color.black_touming_80);
                    firstLeftImg.setImageResource(R.mipmap.first_in_find);
                    firstLeftImg.setVisibility(View.VISIBLE);
                    firstRelative.setTag(1);
                    firstRelative.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if ((int) (v.getTag()) == 1) {
                                firstLeftImg.setVisibility(View.GONE);
                                firstRelative.setBackgroundResource(R.color.black_touming_80);
                                firstRightImg.setImageResource(R.mipmap.first_in_find2);
                                firstRightImg.setVisibility(View.VISIBLE);
                                firstRelative.setTag(6);
                            } else if ((int) (v.getTag()) == 6) {
                                firstRelative.setVisibility(View.GONE);
                                firstRightImg.setVisibility(View.GONE);
                                firstRelative.setBackgroundResource(R.color.nothing);
                                firstRelative.setPadding(0, 0, 0, 0);
                            }
                        }
                    });
                    SharedPreferences.Editor editor = firstInSp.edit();
                    editor.putBoolean(DataConstants.FIRST_IN_JING, false);
                    editor.apply();
                }
            } else if (showFragment instanceof WellGoodsFragment) {
                boolean isFirstIn = firstInSp.getBoolean(DataConstants.FIRST_IN_PIN, true);
                if (isFirstIn) {
                    firstRelative.setVisibility(View.VISIBLE);
                    firstRelative.setBackgroundResource(R.color.black_touming_80);
                    firstLeftImg.setImageResource(R.mipmap.first_in_wellgood);
                    firstLeftImg.setVisibility(View.VISIBLE);
                    firstRelative.setTag(1);
                    firstRelative.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if ((int) (v.getTag()) == 1) {
                                firstLeftImg.setVisibility(View.GONE);
                                firstRelative.setBackgroundResource(R.color.black_touming_80);
                                firstRightImg.setImageResource(R.mipmap.first_in_wellgood2);
                                firstRightImg.setVisibility(View.VISIBLE);
                                firstRelative.setTag(6);
                            } else if ((int) (v.getTag()) == 6) {
                                firstRelative.setVisibility(View.GONE);
                                firstRightImg.setVisibility(View.GONE);
                                firstRelative.setBackgroundResource(R.color.nothing);
                                firstRelative.setPadding(0, 0, 0, 0);
                            }
                        }
                    });
                    SharedPreferences.Editor editor = firstInSp.edit();
                    editor.putBoolean(DataConstants.FIRST_IN_PIN, false);
                    editor.apply();
                }
            }
//            else if (showFragment instanceof MineFragment) {
//                boolean isFirstIn = firstInSp.getBoolean(DataConstants.FIRST_IN_WO, true);
//                if (isFirstIn) {
//                    firstRelative.setBackgroundResource(R.color.black_touming_80);
//                    firstRelative.setVisibility(View.VISIBLE);
//                    firstImg.setImageResource(R.mipmap.first_in_mine);
//                    firstImg.setVisibility(View.VISIBLE);
//                    firstRelative.setTag(11);
//                    firstRelative.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            if ((int) (v.getTag()) == 11) {
//                                firstImg.setVisibility(View.GONE);
//                                firstRelative.setBackgroundResource(R.color.black_touming_80);
//                                firstLeftImg.setImageResource(R.mipmap.mine2);
//                                firstLeftImg.setVisibility(View.VISIBLE);
//                                firstRelative.setTag(12);
//                            } else if ((int) (v.getTag()) == 12) {
//                                firstLeftImg.setVisibility(View.GONE);
//                                firstRelative.setVisibility(View.GONE);
//                                firstRelative.setBackgroundResource(R.color.nothing);
//                                firstRelative.setPadding(0, 0, 0, 0);
//                            }
//                        }
//                    });
//                    SharedPreferences.Editor editor = firstInSp.edit();
//                    editor.putBoolean(DataConstants.FIRST_IN_WO, false);
//                    editor.apply();
//                }
//            }
        }
    }


    private BroadcastReceiver mainReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
//            Log.e("<<<", "接收到广播");
            if (1 == intent.getIntExtra("index", -1)) {
                moveToDown();
            } else if (2 == intent.getIntExtra("index", -1)) {
//                WindowUtils.chenjin(MainActivity.this);
                moveToReset();
            }
        }
    };

    public void moveToDown() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            container.setSystemUiVisibility(View.INVISIBLE);
        }
        ObjectAnimator downAnimator = ObjectAnimator.ofFloat(bottomLinear, "translationY", bottomLinear.getMeasuredHeight()).setDuration(300);
        downAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                animFlag = 1;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                animFlag = 2;
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        if (animFlag == 0) {
            downAnimator.start();
        }
    }

    public void moveToReset() {
        if (bottomLinear.getTranslationY() <= 0) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            container.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        ObjectAnimator upAnimator = ObjectAnimator.ofFloat(bottomLinear, "translationY", 0).setDuration(300);
        upAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                animFlag = 1;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                animFlag = 0;
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        if (animFlag == 2) {
            upAnimator.start();
        }
    }

    private int animFlag = 0;

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

    private Boolean isExit = false;

    private void exitBy2Click() {
        Timer tExit = null;
        if (!isExit) {
            isExit = true; // 准备退出
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            tExit = new Timer();
            tExit.schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false; // 取消退出
                }
            }, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务

        } else {
            finish();
            System.exit(0);
        }
    }

}
