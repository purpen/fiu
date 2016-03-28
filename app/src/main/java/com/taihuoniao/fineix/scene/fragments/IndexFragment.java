package com.taihuoniao.fineix.scene.fragments;

import android.content.Intent;
import android.view.View;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.base.BaseFragment;
import com.taihuoniao.fineix.map.LocationActivity;
import com.taihuoniao.fineix.map.POIListActivity;

import butterknife.OnClick;

public class IndexFragment extends BaseFragment {
    public IndexFragment() {
        // Required empty public constructor
    }
    @Override
    protected View initView() {
        View view = View.inflate(getActivity(), R.layout.fragment_index, null);
        return view;
    }

    @OnClick({R.id.location_btn, R.id.poi_btn})
    protected void onClick(View v) {
        switch (v.getId()) {
            case R.id.location_btn:
                activity.startActivity(new Intent(activity, LocationActivity.class));
                break;
            case R.id.poi_btn:
                activity.startActivity(new Intent(activity, POIListActivity.class));
                break;
        }
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
