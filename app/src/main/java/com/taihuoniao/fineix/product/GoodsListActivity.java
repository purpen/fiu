package com.taihuoniao.fineix.product;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.GoodListFirtViewPagerAdapter;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.beans.CartBean;
import com.taihuoniao.fineix.beans.CategoryBean;
import com.taihuoniao.fineix.network.DataConstants;
import com.taihuoniao.fineix.network.DataPaser;
import com.taihuoniao.fineix.view.CustomSlidingTab;
import com.taihuoniao.fineix.view.WaittingDialog;

import butterknife.Bind;

/**
 * Created by taihuoniao on 2016/5/3.
 */
public class GoodsListActivity extends BaseActivity implements View.OnClickListener {
    //上个界面传递过来的点击哪个跳转
    private int position = 0;

    @Bind(R.id.activity_good_list_search)
    ImageView searchImg;
    @Bind(R.id.activity_good_list_cart_relative)
    RelativeLayout cartRelative;
    @Bind(R.id.activity_good_list_cart_number)
    TextView cartNumber;
    @Bind(R.id.activity_good_list_first_sliding)
    CustomSlidingTab firstSliding;
    @Bind(R.id.activity_good_list_first_viewpager)
    ViewPager firstViewPager;//关联slidingtab的时候，设置监听应该是他关联的slidingtab
    //网络请求对话框
    private WaittingDialog dialog;


    @Override
    protected void getIntentData() {
        position = getIntent().getIntExtra("position", 0);
    }

    public GoodsListActivity() {
        super(R.layout.activity_good_list);
    }

    @Override
    protected void initView() {
        dialog = new WaittingDialog(GoodsListActivity.this);
        searchImg.setOnClickListener(this);
        cartRelative.setOnClickListener(this);
        firstSliding.setIndicatorColor(getResources().getColor(R.color.yellow_bd8913));
        firstSliding.setTextColor(getResources().getColor(R.color.black333333));
        firstSliding.setCurTabTextColor(getResources().getColor(R.color.yellow_bd8913));
        firstSliding.setTypeface(null, Typeface.NORMAL);
        firstSliding.setTextSize(getResources().getDimensionPixelSize(R.dimen.sp14));
    }

    @Override
    protected void requestNet() {
        if (!dialog.isShowing()) {
            dialog.show();
        }
        DataPaser.categoryList(1 + "", 10 + "", handler);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_good_list_cart_relative:
                Intent intent1 = new Intent(GoodsListActivity.this, ShopCarActivity.class);
                startActivity(intent1);
                break;
            case R.id.activity_good_list_search:
                onBackPressed();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        DataPaser.cartNum(handler);
    }

    @Override
    protected void onDestroy() {
        //cancelNet();
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
            handler = null;
        }
        super.onDestroy();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DataConstants.CATEGORY_LIST:
                    dialog.dismiss();
                    CategoryBean netCategoryBean = (CategoryBean) msg.obj;
                    if (netCategoryBean.isSuccess()) {
                        GoodListFirtViewPagerAdapter goodListFirtViewPagerAdapter = new GoodListFirtViewPagerAdapter(getSupportFragmentManager(), netCategoryBean);
                        firstViewPager.setAdapter(goodListFirtViewPagerAdapter);
                        firstSliding.setViewPager(firstViewPager);
                        firstViewPager.setCurrentItem(position+1, true);
                    }
                    break;
                case DataConstants.CART_NUM:
                    CartBean netCartBean = (CartBean) msg.obj;
                    if (netCartBean.isSuccess() && netCartBean.getData().getCount() > 0) {
                        cartNumber.setVisibility(View.VISIBLE);
                        cartNumber.setText(String.format("%d", netCartBean.getData().getCount()));
                    } else {
                        cartNumber.setVisibility(View.GONE);
                    }
                    break;
                case DataConstants.NET_FAIL:
                    dialog.dismiss();
                    break;
            }
        }
    };

}
