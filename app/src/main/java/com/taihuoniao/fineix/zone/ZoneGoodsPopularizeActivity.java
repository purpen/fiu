package com.taihuoniao.fineix.zone;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.google.gson.reflect.TypeToken;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.base.HttpRequest;
import com.taihuoniao.fineix.beans.CategoryListBean;
import com.taihuoniao.fineix.beans.HttpResponse;
import com.taihuoniao.fineix.common.GlobalDataCallBack;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.URL;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.view.CustomHeadView;
import com.taihuoniao.fineix.zone.adapter.ZonePopularizeViewPageAdapter;
import com.taihuoniao.fineix.zone.bean.ZoneDetailBean;
import com.taihuoniao.fineix.zone.fragment.ZonePopularizeGoodsFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;

/**
 * Created by lilin on 2017/5/17.
 */

public class ZoneGoodsPopularizeActivity extends BaseActivity implements ViewPager.OnPageChangeListener{
    @Bind(R.id.custom_head)
    CustomHeadView customHead;
    @Bind(R.id.tabLayout)
    TabLayout tabLayout;
    @Bind(R.id.viewPager)
    ViewPager viewPager;
    private List<Fragment> mFragments;
    private List<CategoryListBean.RowsEntity> categoryItems;
    private List<String> mTitles;
    private ZoneDetailBean zoneDetailBean;
    public ZoneGoodsPopularizeActivity() {
        super(R.layout.activity_zone_popularize_goods);
    }

    @Override
    protected void getIntentData() {
        Intent intent = getIntent();
        if (intent!=null)
            zoneDetailBean=intent.getParcelableExtra(TAG);
    }

    @Override
    protected void initView() {
        customHead.setHeadCenterTxtShow(true, R.string.zone_goods_popularize);
    }

    @Override
    protected void initList() {
        categoryItems=new ArrayList<>();
    }

    @Override
    protected void requestNet() {
        HashMap<String, String> params = ClientDiscoverAPI.getcategoryListRequestParams("1", "1", "1");
        HttpRequest.post(params, URL.CATEGORY_LIST, new GlobalDataCallBack() {
            @Override
            public void onSuccess(String json) {
                HttpResponse<CategoryListBean> response = JsonUtil.json2Bean(json, new TypeToken<HttpResponse<CategoryListBean>>() {});
                if (response.isSuccess()) {
                    categoryItems.addAll(response.getData().getRows());
                    initTabLayout();
                } else {
                    ToastUtils.showError(response.getMessage());
                }
            }

            @Override
            public void onFailure(String error) {
                ToastUtils.showError(R.string.network_err);
            }
        });
    }

    private void initTabLayout() {
        if (categoryItems.size() == 0) return;
        mFragments = new ArrayList<>();
        mTitles = new ArrayList<>();
        for (int i = 0; i < categoryItems.size(); i++) {
            CategoryListBean.RowsEntity item = categoryItems.get(i);
            mFragments.add(ZonePopularizeGoodsFragment.newInstance(item,zoneDetailBean._id));
            mTitles.add(item.getTitle());
        }
        initTabLayoutAndViewPager();
    }

    private void initTabLayoutAndViewPager(){
        ZonePopularizeViewPageAdapter viewPageAdapter = new ZonePopularizeViewPageAdapter(getSupportFragmentManager(),mFragments,mTitles);
        viewPager.setAdapter(viewPageAdapter);
        viewPager.addOnPageChangeListener(this);
        viewPager.setOffscreenPageLimit(mFragments.size());
        viewPager.setCurrentItem(0);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onPageSelected(int position) {
        viewPager.setCurrentItem(position);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    protected void installListener() {
        customHead.setGoBackListenr(new CustomHeadView.IgobackLister() {
            @Override
            public void goback() {
                setResult(RESULT_OK);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK);
        super.onBackPressed();

    }
}
