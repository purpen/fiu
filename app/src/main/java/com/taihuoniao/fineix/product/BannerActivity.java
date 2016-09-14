package com.taihuoniao.fineix.product;

import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.PictureViewPagerAdapter;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.main.MainApplication;

import butterknife.Bind;

/**
 * Created by taihuoniao on 2016/9/14.
 */
public class BannerActivity extends BaseActivity {
    @Bind(R.id.view_pager)
    ViewPager viewPager;
    @Bind(R.id.cancel)
    ImageView cancel;

    public BannerActivity() {
        super(R.layout.activity_picture);
    }

    @Override
    protected void initView() {
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        viewPager.setAdapter(new PictureViewPagerAdapter(this, MainApplication.picList));
    }

    @Override
    public void onBackPressed() {
        MainApplication.picList = null;
        super.onBackPressed();
    }
}
