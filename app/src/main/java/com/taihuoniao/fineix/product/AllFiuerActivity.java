package com.taihuoniao.fineix.product;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.view.GlobalTitleLayout;

import butterknife.Bind;

/**
 * Created by taihuoniao on 2016/7/7.
 */
public class AllFiuerActivity extends BaseActivity {
    @Bind(R.id.titlelayout)
    GlobalTitleLayout titlelayout;

    public AllFiuerActivity() {
        super(R.layout.activity_all_fiuer);
    }

    @Override
    protected void initView() {
        titlelayout.setBackgroundResource(R.color.white);
        titlelayout.setBackImg(R.mipmap.back_black);
    }

}
