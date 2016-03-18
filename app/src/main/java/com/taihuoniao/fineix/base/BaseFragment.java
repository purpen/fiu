package com.taihuoniao.fineix.base;

import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by taihuoniao on 2016/3/14.
 */
public abstract class BaseFragment extends Fragment {
    protected static final String systemPhotoPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/Camera";//系统相册路径

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = initView();
        initList();
        requestNet();
        return view;
    }

    protected abstract void requestNet();

    protected abstract void initList();

    protected abstract View initView();
}
