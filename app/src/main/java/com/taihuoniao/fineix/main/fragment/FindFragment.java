package com.taihuoniao.fineix.main.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.base.BaseFragment;

/**
 * @author lilin
 *         created at 2016/3/23 19:15
 */
public class FindFragment extends BaseFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected View initView() {
        View view = View.inflate(getActivity(), R.layout.fragment_find, null);
        return view;
    }

    @Override
    protected void initList() {

    }

    @Override
    protected void requestNet() {

    }
}
