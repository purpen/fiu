package com.taihuoniao.fineix.base;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.taihuoniao.fineix.R;

import java.util.List;

import butterknife.ButterKnife;
import cn.sharesdk.framework.ShareSDK;

/**
 * Created by taihuoniao on 2016/3/14.
 */
public abstract class BaseActivity<T> extends AppCompatActivity {
    protected final String TAG = getClass().getSimpleName();
    protected Activity activity;
    private int layoutResID;
    protected DisplayImageOptions options;

    public BaseActivity(int layoutResID) {
        this.layoutResID = layoutResID;
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.default_pic)
                .showImageForEmptyUri(R.mipmap.default_pic)
                .showImageOnFail(R.mipmap.default_pic)
                .delayBeforeLoading(0)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getIntentData();
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        ShareSDK.initSDK(this);
        super.onCreate(savedInstanceState);
        this.activity = this;
        if (layoutResID != 0) {
            setContentView(layoutResID);
        }
        ButterKnife.bind(this);
        initView();
        installListener();
        initList();
        requestNet();
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

    public void handMessage(Message msg) {
    }
}
