package com.taihuoniao.fineix.map;

import android.support.v7.widget.RecyclerView;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.view.CustomHeadView;

import butterknife.Bind;

/**
 * Created by lilin on 2017/2/27.
 * 选择开通城市
 */

public class SelectCitiesActivity extends BaseActivity {
    @Bind(R.id.custom_head)
    CustomHeadView customHeadView;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    public SelectCitiesActivity(){
        super(R.layout.activity_select_cities);
    }

    @Override
    protected void initView() {
        customHeadView.setHeadCenterTxtShow(true,R.string.title_switch_city);
    }

    @Override
    protected void requestNet() {

    }
}
