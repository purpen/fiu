package com.taihuoniao.fineix.base;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.taihuoniao.fineix.utils.LogUtil;

import java.util.List;

/**
 * Created by taihuoniao on 2016/3/14.
 */
public abstract class BaseFragment extends Fragment {
    protected static final String systemPhotoPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/Camera";//系统相册路径
    protected final String TAG = getClass().getSimpleName();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        LogUtil.e(TAG, "onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = initView();
        initList();
        requestNet();
        LogUtil.e(TAG, "onCreateView");
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
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
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    protected abstract void requestNet();

    protected abstract void initList();

    protected abstract View initView();
}
