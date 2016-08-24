package com.taihuoniao.fineix.qingjingOrSceneDetails.fragment;

import android.os.Bundle;
import android.view.View;

import com.taihuoniao.fineix.R;

/**
 * Created by taihuoniao on 2016/8/23.
 */
public class SearchSubjectFragment extends SearchFragment {
    private String q;
    private boolean isContent;
    private int page =1;

    public static SearchSubjectFragment newInstance(String q, boolean isContent) {
        Bundle args = new Bundle();
        args.putString("q", q);
        args.putBoolean("isContent", isContent);
        SearchSubjectFragment fragment = new SearchSubjectFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        q = getArguments().getString("q", null);
        isContent = getArguments().getBoolean("isContent");
    }

    @Override
    protected View initView() {
        View view = View.inflate(getActivity(), R.layout.activity_create_qj,null);
        return view;
    }

    @Override
    protected void initList() {

    }

    @Override
    protected void requestNet() {

    }

    public void refreshData(String q) {
        this.q = q;
        page = 1;
        requestNet();
    }
}
