package com.taihuoniao.fineix.main.tab3;

import android.view.View;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.base.BaseFragment;

import butterknife.ButterKnife;

/**
 * Created by Stephen on 2017/3/3 23:00
 * Email: 895745843@qq.com
 */

public class WellGoodsFragment02 extends BaseFragment{

    @Override
    protected void requestNet() {

    }

    @Override
    protected View initView() {
        View view = View.inflate(getActivity(), R.layout.fragment_wellgoods_02, null);
        ButterKnife.bind(this, view);
        return view;
    }
}
