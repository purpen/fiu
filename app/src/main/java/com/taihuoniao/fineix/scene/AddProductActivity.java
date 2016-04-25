package com.taihuoniao.fineix.scene;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RelativeLayout;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.AddProductViewPagerAdapter;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.beans.CategoryBean;
import com.taihuoniao.fineix.network.DataConstants;
import com.taihuoniao.fineix.network.DataPaser;
import com.taihuoniao.fineix.view.CustomSlidingTab;
import com.taihuoniao.fineix.view.GlobalTitleLayout;

/**
 * Created by taihuoniao on 2016/3/22.
 */
public class AddProductActivity extends BaseActivity implements View.OnClickListener{
    private GlobalTitleLayout titleLayout;
    private CustomSlidingTab slidingTab;
    private AddProductViewPagerAdapter addProductViewPagerAdapter;
    private ViewPager viewPager;
    private RelativeLayout search;

    public AddProductActivity() {
        super(0);
    }

    @Override
    protected void requestNet() {
        //获取分类列表
        DataPaser.categoryList(1 + "", 1 + "", handler);
    }

    @Override
    protected void initList() {
        titleLayout.setTitle(R.string.add_product, getResources().getColor(R.color.black333333));
        titleLayout.setBackgroundColor(getResources().getColor(R.color.white));
        titleLayout.setContinueTvVisible(false);
        titleLayout.setBackImgVisible(false);
        titleLayout.setCancelImgVisible(true);
        titleLayout.setCancelImg(R.mipmap.cancel_black);
        slidingTab.setIndicatorColor(getResources().getColor(R.color.yellow_bd8913));
        slidingTab.setTextColor(getResources().getColor(R.color.black333333));
        slidingTab.setCurTabTextColor(getResources().getColor(R.color.yellow_bd8913));
        slidingTab.setTypeface(null, Typeface.NORMAL);
        slidingTab.setTextSize(getResources().getDimensionPixelSize(R.dimen.sp14));
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_add_product);
        titleLayout = (GlobalTitleLayout) findViewById(R.id.activity_add_product_title);
        slidingTab = (CustomSlidingTab) findViewById(R.id.activity_add_product_slidingtab);
        viewPager = (ViewPager) findViewById(R.id.activity_add_product_viewpager);
        search = (RelativeLayout) findViewById(R.id.rl);
    }

    @Override
    protected void installListener() {
        search.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.rl:
                startActivity(new Intent(activity,SearchResultActivity.class));
                return;
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DataConstants.CATEGORY_LIST:
                    CategoryBean netCategoryBean = (CategoryBean) msg.obj;
                    if (netCategoryBean.isSuccess()) {
                        addProductViewPagerAdapter = new AddProductViewPagerAdapter(getSupportFragmentManager(), netCategoryBean);
                        viewPager.setAdapter(addProductViewPagerAdapter);
                        slidingTab.setViewPager(viewPager);
                    }
                    break;
                case DataConstants.NET_FAIL:
                    break;
            }
        }
    };

    @Override
    protected void onDestroy() {
        //        cancelNet();
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
            handler = null;
        }
        super.onDestroy();
    }
}
