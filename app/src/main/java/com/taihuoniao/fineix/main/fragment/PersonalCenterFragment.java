package com.taihuoniao.fineix.main.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.base.BaseFragment;

public class PersonalCenterFragment extends BaseFragment {

    public PersonalCenterFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected View initView() {
        View view = View.inflate(getActivity(), R.layout.fragment_personalcenter, null);

        return view;
    }

    @Override
    protected void requestNet() {

    }

    @Override
    protected void initList() {

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
