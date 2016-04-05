package com.taihuoniao.fineix.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;

/**
 * Created by taihuoniao on 2016/3/14.
 */
public abstract class BaseActivity extends AppCompatActivity {
    protected final String TAG = getClass().getSimpleName();
    protected Activity activity;
    private int layoutResID;

    public BaseActivity(int layoutResID) {
        this.layoutResID = layoutResID;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.activity = this;
        setContentView(layoutResID);
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
}
