package com.taihuoniao.fineix.base;

import android.app.Activity;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by taihuoniao on 2016/3/14.
 */
public abstract class BaseFragment<T> extends Fragment {
    //    protected static final String systemPhotoPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/Camera";//系统相册路径
    protected final String TAG = getClass().getSimpleName();
    protected  Activity activity;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        if (savedInstanceState != null) {
            FragmentManager fm = getActivity().getSupportFragmentManager();
            List<Fragment> fragments = fm.getFragments();
            for (Fragment fragment : fragments) {
                if (TextUtils.isEmpty(fragment.getTag())) {
                    continue;
                }
                if (TextUtils.equals(TAG, fragment.getTag())) {
                    fm.beginTransaction().show(fm.findFragmentByTag(TAG)).commit();
                } else {
                    fm.beginTransaction().hide(fm.findFragmentByTag(fragment.getTag())).commit();
                }
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = initView();
        ButterKnife.bind(this, view);
        initList();
        requestNet();
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        installListener();
    }

    protected void installListener() {
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        activity=null;
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    protected abstract void requestNet();

    protected abstract void initList();

    protected abstract View initView();

    protected void refreshUI() {

    }

    protected void refreshUI(ArrayList<T> list) {

    }

    public void handMessage(Message msg) {
    }
}
