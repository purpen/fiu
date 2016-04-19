package com.taihuoniao.fineix.main.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

//import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;

import butterknife.ButterKnife;

/**
 * @author lilin
 * created at 2016/4/11 20:05
 */
public abstract class MyBaseFragment<T> extends Fragment {
    protected View view;
    protected final String TAG = getClass().getSimpleName();
    protected Activity activity;
    private int layoutId;
    public MyBaseFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity=getActivity();
        initParams();
        loadData();
    }

    protected void setFragmentLayout(int layoutId){
        if (layoutId==0){
            throw new IllegalArgumentException("please invoke set setFragmentLayout");
        }
        this.layoutId=layoutId;
    };

    protected void initParams() {

    }

    protected void loadData() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view ==null)
            view = inflater.inflate(layoutId, null);
        ButterKnife.bind(this, view);
        initViews();
        return view;
    }

    public void onResume() {
        super.onResume();
//        MobclickAgent.onPageStart(TAG);
    }
    public void onPause() {
        super.onPause();
//        MobclickAgent.onPageEnd(TAG);
    }
    protected abstract void initViews();
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        installListener();
    }

    protected void installListener() {

    }

    protected void refreshUIAfterNet(){

    }

    protected void refreshUIAfterNet(ArrayList<T> list){

    }

    protected void refreshUIAfterNet(ArrayList<T> list,ArrayList<T> list1,ArrayList<T> list2,ArrayList<T> list3){

    }

}
