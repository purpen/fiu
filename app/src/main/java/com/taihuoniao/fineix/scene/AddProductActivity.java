package com.taihuoniao.fineix.scene;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.TabPagerAdapter;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.network.DataConstants;
import com.taihuoniao.fineix.network.DataPaser;
import com.taihuoniao.fineix.view.CustomSlidingTab;
import com.taihuoniao.fineix.view.GlobalTitleLayout;

/**
 * Created by taihuoniao on 2016/3/22.
 */
public class AddProductActivity extends BaseActivity {
    ImageView img;//测试使用
    private GlobalTitleLayout titleLayout;
    private CustomSlidingTab slidingTab;
    private ViewPager viewPager;

    public AddProductActivity() {
        super(0);
    }

    @Override
    protected void requestNet() {
        //暂无数据
        DataPaser.getProductList(handler);
    }

    @Override
    protected void initList() {
        slidingTab.setIndicatorColor(getResources().getColor(R.color.red));
        slidingTab.setTextColor(getResources().getColor(R.color.black333333));
        slidingTab.setCurTabTextColor(getResources().getColor(R.color.red));
        slidingTab.setTypeface(null, Typeface.NORMAL);
        slidingTab.setTextSize(getResources().getDimensionPixelSize(R.dimen.sp14));
        viewPager.setAdapter(new TabPagerAdapter(((FragmentActivity) activity).getSupportFragmentManager()));
        slidingTab.setViewPager(viewPager);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_add_product);
        titleLayout = (GlobalTitleLayout) findViewById(R.id.activity_add_product_title);
        slidingTab = (CustomSlidingTab) findViewById(R.id.activity_add_product_slidingtab);
        viewPager = (ViewPager) findViewById(R.id.activity_add_product_viewpager);
        //测试使用
        img = (ImageView) findViewById(R.id.activity_add_product_img);
    }

    //测试使用
    public void click(View view) {
        Intent intent = new Intent();
        //测试使用图片，正常情况返回的是javabean
        //http://frbird.qiniudn.com/product/160126/56a718b93ffca26a098baf2c-5-p500x500.jpg
        intent.putExtra("url", "http://frbird.qiniudn.com/product/160126/56a718b93ffca26a098baf2c-5-p500x500.jpg");
        setResult(DataConstants.RESULTCODE_EDIT_ADDPRODUCT, intent);
        finish();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DataConstants.ADD_PRODUCT_LIST:
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
