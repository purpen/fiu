package com.taihuoniao.fineix.base;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.main.MainActivity;
import com.taihuoniao.fineix.main.PermissionsActivity;
import com.taihuoniao.fineix.utils.LogUtil;
import com.taihuoniao.fineix.utils.PermissionsChecker;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import cn.sharesdk.framework.ShareSDK;
import okhttp3.Call;

/**
 * Created by taihuoniao on 2016/3/14.
 */
public abstract class BaseActivity<T> extends AppCompatActivity {
    private static final int REQUEST_CODE = 100; // 请求码
    protected final String TAG = getClass().getSimpleName();
    protected FragmentActivity activity;
    private int layoutResID;
    protected DisplayImageOptions options;
    private List<Call> handlerList;
    // 所需的全部权限
    static final String[] PERMISSIONS = new String[]{
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private PermissionsChecker mPermissionsChecker;

    public BaseActivity(int layoutResID) {
        this.layoutResID = layoutResID;
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.default_background_750_1334)
                .showImageForEmptyUri(R.mipmap.default_background_750_1334)
                .showImageOnFail(R.mipmap.default_background_750_1334)
                .resetViewBeforeLoading(true)
                .delayBeforeLoading(0)
                .imageScaleType(ImageScaleType.EXACTLY)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mPermissionsChecker = new PermissionsChecker(this);
        LogUtil.e("taihuoniao", "---------->onCreate()" + getClass().getSimpleName());
        getIntentData();
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        ShareSDK.initSDK(this);
        super.onCreate(savedInstanceState);
        this.activity = this;
        if (layoutResID != 0) {
            setContentView(layoutResID);
        }
        setContenttView();
        ButterKnife.bind(this);
        initView();
        installListener();
        initList();
        requestNet();
        PushAgent.getInstance(this).onAppStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 缺少权限时, 进入权限配置页面
        if (mPermissionsChecker.lacksPermissions(PERMISSIONS)) {
            startPermissionsActivity();
        }
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    protected void addNet(Call httpHandler) {
        if (handlerList == null) {
            handlerList = new ArrayList<>();
        }
        handlerList.add(httpHandler);
    }
    private void clearNet(){
        if (handlerList != null) {
            for (Call httpHandler : handlerList) {
                if (httpHandler != null) {
                    httpHandler.cancel();
                }
            }
        }
    }

    protected void setContenttView() {
    }

    protected void getIntentData() {
    }

    protected void installListener() {
    }

    protected void requestNet() {
    }

    protected void initList() {
    }

    protected abstract void initView();

    protected void refreshUI() {

    }

    protected void refreshUI(List<T> list) {

    }

    @Override
    protected void onDestroy() {
        clearNet();
        super.onDestroy();
        if (this instanceof MainActivity) MobclickAgent.onKillProcess(this);
    }

    private void startPermissionsActivity() {
        PermissionsActivity.startActivityForResult(this, REQUEST_CODE, PERMISSIONS);
    }

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 拒绝时, 关闭页面, 缺少主要权限, 无法运行
        if (requestCode == REQUEST_CODE && resultCode == PermissionsActivity.PERMISSIONS_DENIED) {
            finish();
        }
    }
}
