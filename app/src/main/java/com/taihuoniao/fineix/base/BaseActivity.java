package com.taihuoniao.fineix.base;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import butterknife.ButterKnife;

/**
 * Created by taihuoniao on 2016/3/14.
 */
public abstract class BaseActivity extends FragmentActivity {
    protected final String tag = getClass().getSimpleName();
    protected Activity activity;
    private int layoutResID;

    public BaseActivity(int layoutResID) {
        this.layoutResID = layoutResID;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.activity = this;
        ButterKnife.bind(this);
        setContentView(layoutResID);
        initView();
        installListener();
        initList();
        requestNet();
    }

    /**
     * 获得界面跳转数据
     */
    protected void getIntentData() {
    }

    protected void installListener() {
    }

    protected void requestNet() {
    }

    protected void initList() {
    }

    protected abstract void initView();
}
