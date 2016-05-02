package com.taihuoniao.fineix.user.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.main.fragment.MyBaseFragment;

/**
 * Created by Administrator on 2016/5/2.
 */
public class UserFocusFragment extends MyBaseFragment{
    public UserFocusFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.setFragmentLayout(R.layout.fragment_list);
        super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    protected void loadData() {

    }

    @Override
    protected void initViews() {

    }
}
