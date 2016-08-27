package com.taihuoniao.fineix.product;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.SearchViewPagerAdapter;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.beans.BrandDetailBean;
import com.taihuoniao.fineix.main.MainApplication;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.product.fragment.BrandProductFragment;
import com.taihuoniao.fineix.product.fragment.BrandQJFragment;
import com.taihuoniao.fineix.qingjingOrSceneDetails.fragment.SearchFragment;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.utils.WindowUtils;
import com.taihuoniao.fineix.view.BrandScrollView;
import com.taihuoniao.fineix.view.WaittingDialog;
import com.taihuoniao.fineix.view.roundImageView.RoundedImageView;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by taihuoniao on 2016/5/5.
 */
public class BrandDetailActivity extends BaseActivity implements View.OnClickListener {
    //上个界面传递过来的名牌详情id
    private String id;
    @Bind(R.id.title_name)
    TextView titleName;
    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.scroll_view)
    BrandScrollView scrollView;
    @Bind(R.id.background_container)
    RelativeLayout backgroundContainer;
    @Bind(R.id.background_img)
    ImageView backgroundImg;
    @Bind(R.id.title_img)
    RoundedImageView titleImg;
    @Bind(R.id.des)
    TextView des;
    @Bind(R.id.tab_layout)
    public TabLayout tabLayout;
    @Bind(R.id.view_pager)
    ViewPager viewPager;
    public WaittingDialog dialog;
    private BrandProductFragment brandProductFragment;
    private BrandQJFragment brandQJFragment;
    private List<SearchFragment> fragmentList;
    private List<String> titleList;

    public BrandDetailActivity() {
        super(R.layout.activity_brand_detail);
    }

    @Override
    protected void getIntentData() {
        id = getIntent().getStringExtra("id");
        if (id == null) {
            ToastUtils.showError("暂无此品牌详细信息");
            finish();
        }
    }

    @Override
    protected void initView() {
        back.setOnClickListener(this);
        dialog = new WaittingDialog(this);
        WindowUtils.showStatusBar(this);
        backgroundContainer.setFocusable(true);
        backgroundContainer.setFocusableInTouchMode(true);
        backgroundContainer.requestFocus();
    }

    @Override
    protected void initList() {
        ViewGroup.LayoutParams lp = backgroundContainer.getLayoutParams();
        lp.width = MainApplication.getContext().getScreenWidth();
        lp.height = lp.width * 422 / 750;
        backgroundContainer.setLayoutParams(lp);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) viewPager.getLayoutParams();
        layoutParams.height = MainApplication.getContext().getScreenHeight();
        viewPager.setLayoutParams(layoutParams);
//        scrollView.setOnScrollListener(new BrandScrollView.OnScrollListener() {
//            @Override
//            public void scroll(ScrollView scrollView, int l, int t, int oldl, int oldt) {
//                //判断拦截点击手势
//                Log.e("<<<滑动", "t=" + t + ",oldt=" + oldt);
//                if (t > 300 && t > oldt) {
//                    BrandDetailActivity.this.scrollView.setStop(true);
//                } else {
//                    BrandDetailActivity.this.scrollView.setStop(false);
//                }
//            }
//        });
        //fragment
        fragmentList = new ArrayList<>();
        titleList = new ArrayList<>();
        brandProductFragment = BrandProductFragment.newInstance(id);
        brandQJFragment = BrandQJFragment.newInstance(id);
    }

//    public void setTabLayoutVisible(boolean visible) {
//        if (visible) {
//            tabLayout.setVisibility(View.VISIBLE);
//        } else {
//            tabLayout.setVisibility(View.GONE);
//        }
//
//    }
//
//    public void onlyOne(boolean isOne) {
//        if (isOne) {
//            fragmentList.clear();
//            titleList.clear();
//            des.setVisibility(View.GONE);
//            fragmentList.add(brandQJFragment);
//        } else {
//            des.setVisibility(View.VISIBLE);
//            fragmentList.add(brandProductFragment);
//            titleList.add("产品列表");
//            fragmentList.add(brandQJFragment);
//            titleList.add("情境");
//        }
//    }

    @Override
    protected void requestNet() {
        if (!dialog.isShowing()) {
            dialog.show();
        }
        ClientDiscoverAPI.brandDetail(id, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                BrandDetailBean brandDetailBean = new BrandDetailBean();
                try {
                    Gson gson = new Gson();
                    Type type = new TypeToken<BrandDetailBean>() {
                    }.getType();
                    brandDetailBean = gson.fromJson(responseInfo.result, type);
                } catch (JsonSyntaxException e) {
                    Log.e("<<<", "数据解析异常" + e.toString());
                }
                if (brandDetailBean.isSuccess()) {
                    try {
                        fragmentList.clear();
                        titleList.clear();
                        titleName.setText(brandDetailBean.getData().getTitle());
                        ImageLoader.getInstance().displayImage(brandDetailBean.getData().getCover_url(), titleImg);
                        des.setText(brandDetailBean.getData().getDes());
                        ImageLoader.getInstance().displayImage(brandDetailBean.getData().getBanner_url(), backgroundImg);
//                        onlyOne(false);
//                        setTabLayoutVisible(true);
                    } catch (Exception e) {
//                        onlyOne(true);
//                        setTabLayoutVisible(false);
                    }
                    fragmentList.add(brandProductFragment);
                    titleList.add("产品列表");
                    fragmentList.add(brandQJFragment);
                    titleList.add("情境");
                    viewPager.setAdapter(new SearchViewPagerAdapter(getSupportFragmentManager(), fragmentList, titleList));
                    tabLayout.setupWithViewPager(viewPager);
                    viewPager.setOffscreenPageLimit(fragmentList.size());
                } else {
                    ToastUtils.showError(brandDetailBean.getMessage());
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                ToastUtils.showError("网络错误");
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                onBackPressed();
                break;
        }
    }


}
