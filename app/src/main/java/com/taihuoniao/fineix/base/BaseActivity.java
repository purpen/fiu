package com.taihuoniao.fineix.base;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.taihuoniao.fineix.provider.CartActionProvider;
import com.taihuoniao.fineix.R;

import butterknife.Bind;
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

    protected void refreshUI(){

    }
}
