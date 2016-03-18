package com.taihuoniao.fineix.base;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by taihuoniao on 2016/3/14.
 */
public abstract class BaseActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initList();
        requestNet();
    }

    protected abstract void requestNet();

    protected abstract void initList();

    protected abstract void initView();
}
