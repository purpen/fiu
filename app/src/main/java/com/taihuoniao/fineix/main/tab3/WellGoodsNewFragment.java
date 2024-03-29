package com.taihuoniao.fineix.main.tab3;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.base.BaseFragment;
import com.taihuoniao.fineix.base.HttpRequest;
import com.taihuoniao.fineix.beans.CategoryListBean;
import com.taihuoniao.fineix.beans.HttpResponse;
import com.taihuoniao.fineix.beans.LoginInfo;
import com.taihuoniao.fineix.common.GlobalDataCallBack;
import com.taihuoniao.fineix.main.App;
import com.taihuoniao.fineix.main.MainApplication;
import com.taihuoniao.fineix.main.fragment.WellGoodsFragment;
import com.taihuoniao.fineix.main.tab3.adapter.WellGoodsAdapter;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.DataConstants;
import com.taihuoniao.fineix.network.URL;
import com.taihuoniao.fineix.product.ShopCartActivity;
import com.taihuoniao.fineix.qingjingOrSceneDetails.SearchActivity;
import com.taihuoniao.fineix.user.OptRegisterLoginActivity;
import com.taihuoniao.fineix.utils.DensityUtils;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.zxing.activity.CaptureActivityZxing;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Stephen on 2017/3/3 20:36
 * Email: 895745843@qq.com
 */

public class WellGoodsNewFragment extends BaseFragment implements ViewPager.OnPageChangeListener, View.OnClickListener {

    @Bind(R.id.tabLayout_wellGoods_category)
    TabLayout tabLayoutWellGoodsCategory;
    @Bind(R.id.viewPager_wellGoods_list)
    ViewPager viewPagerWellGoodsList;
    @Bind(R.id.title_left)
    ImageView titleLeft;
    @Bind(R.id.cart_number)
    TextView cartNumber;
    @Bind(R.id.title_right)
    RelativeLayout titleRight;
    @Bind(R.id.search_linear)
    LinearLayout searchLinear;
    @Bind(R.id.relative)
    RelativeLayout relative;

    private List<String> mStringList;
    private List<BaseFragment> mBaseFragments;
    private List<CategoryListBean.RowsEntity> categoryListItems;
    private WellGoodsAdapter wellGoodsAdapter;

    @Override
    protected void requestNet() {
        productCategoryList();
    }

    @Override
    protected View initView() {
        View view = View.inflate(getActivity(), R.layout.fragment_well_goods_new, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    private void initTabLayoutAndViewPager() {
        wellGoodsAdapter = new WellGoodsAdapter(getFragmentManager(), mStringList, mBaseFragments);
        viewPagerWellGoodsList.setAdapter(wellGoodsAdapter);
        viewPagerWellGoodsList.addOnPageChangeListener(this);
        tabLayoutWellGoodsCategory.setupWithViewPager(viewPagerWellGoodsList, true);
        setTabLayoutIndicatorWidth();
//        viewPagerWellGoodsList.setOffscreenPageLimit(mBaseFragments.size());
        viewPagerWellGoodsList.setCurrentItem(0, false);
    }

    /**
     * 默认加载 两条
     */
    private void initTabLayout() {
        mStringList = new ArrayList<>();
        mBaseFragments = new ArrayList<>();

        // 默认加载 推荐 情境
        mStringList.add("推荐");
        mStringList.add("合集");
        mStringList.add("情境");
        mStringList.add("品牌");
        mBaseFragments.add(new WellGoodsFragment01());
        mBaseFragments.add(new WellGoodsFragment03());
        mBaseFragments.add(new WellGoodsFragment02());
        mBaseFragments.add(new WellGoodsFragment05());
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
//        Toast.makeText(activity, mStringList.get(position), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private void setIndicatorWidth() throws NoSuchFieldException, IllegalAccessException {
//        int margin = activity.getResources().getDimensionPixelSize(R.dimen.dp15);
        if (mBaseFragments.size() <= 0) {
            return;
        }
        int margin = (int) (((double) tabLayoutWellGoodsCategory.getMeasuredWidth() / mBaseFragments.size() - DensityUtils.sp2px(getActivity(), 42)) / 2);
        Class<?> tablayout = tabLayoutWellGoodsCategory.getClass();
        Field tabStrip = tablayout.getDeclaredField("mTabStrip");
        tabStrip.setAccessible(true);
        LinearLayout ll_tab = (LinearLayout) tabStrip.get(tabLayoutWellGoodsCategory);
        for (int i = 0; i < ll_tab.getChildCount(); i++) {
            View child = ll_tab.getChildAt(i);
            child.setPadding(0, 0, 0, 0);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                params.setMarginStart(margin);
                params.setMarginEnd(margin);
            } else {
                params.setMargins(margin, 0, margin, 0);
            }
            child.setLayoutParams(params);
            child.invalidate();
        }
    }

    @Override
    protected void initList() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            relative.setPadding(0, App.getStatusBarHeight(), 0, 0);
        }
        initTitleBar();
        initTabLayout();
        initTabLayoutAndViewPager();
    }

    //获取产品分类列表
    private void productCategoryList() {
        HashMap<String, String> params = ClientDiscoverAPI.getcategoryListRequestParams("1", "1", null);
        HttpRequest.post(params, URL.CATEGORY_LIST, new GlobalDataCallBack() {
            @Override
            public void onSuccess(String json) {
                HttpResponse<CategoryListBean> categoryListBean = JsonUtil.json2Bean(json, new TypeToken<HttpResponse<CategoryListBean>>() {});
                if (categoryListBean.isSuccess()) {
                    categoryListItems = categoryListBean.getData().getRows();
                    addExtraCategory(categoryListItems);
                    wellGoodsAdapter.notifyDataSetChanged();
                    viewPagerWellGoodsList.setOffscreenPageLimit(mBaseFragments.size());
                    wellGoodsAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(String error) {
            }
        });
    }

    private void addExtraCategory(List<CategoryListBean.RowsEntity> categoryListItems) {
        if (categoryListItems != null) {
            for (int i = 0, index = 2; i < categoryListItems.size(); i++, index++) {
                CategoryListBean.RowsEntity categoryListItem = categoryListItems.get(i);
                mStringList.add(index, categoryListItem.getTitle());
                WellGoodsFragment04 fragment04 = new WellGoodsFragment04();
                Bundle bundle = new Bundle();
                bundle.putString("categoryId", categoryListItem.get_id());
                fragment04.setArguments(bundle);
                mBaseFragments.add(index, fragment04);
            }
        }
    }

    private void initTitleBar(){
        titleLeft.setOnClickListener(this);
        searchLinear.setOnClickListener(this);
//        titleRight.setOnClickListener(this);
        titleRight.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.to_top_img:
//                listView.setSelection(0);
                break;
            case R.id.title_left:
                startActivity(new Intent(getActivity(), CaptureActivityZxing.class));
                break;
            case R.id.title_right:
                if (!LoginInfo.isUserLogin()) {
                    MainApplication.which_activity = DataConstants.WellGoodsFragment;
                    startActivity(new Intent(getActivity(), OptRegisterLoginActivity.class));
                    return;
                }
                startActivity(new Intent(getActivity(), ShopCartActivity.class));
                break;
            case R.id.search_linear:
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                intent.putExtra(WellGoodsFragment.class.getSimpleName(), false);
                intent.putExtra("t", 7);
                startActivity(intent);
                break;
        }
    }

    private void setTabLayoutIndicatorWidth() {
        tabLayoutWellGoodsCategory.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                try {
                    setIndicatorWidth();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    tabLayoutWellGoodsCategory.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    tabLayoutWellGoodsCategory.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
            }
        });
    }

}
