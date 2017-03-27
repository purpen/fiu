package com.taihuoniao.fineix.zone;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.model.LatLng;
import com.google.gson.reflect.TypeToken;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.ViewPagerAdapter;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.base.HttpRequest;
import com.taihuoniao.fineix.beans.HttpResponse;
import com.taihuoniao.fineix.beans.LoginInfo;
import com.taihuoniao.fineix.common.GlobalDataCallBack;
import com.taihuoniao.fineix.main.MainApplication;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.DataConstants;
import com.taihuoniao.fineix.network.URL;
import com.taihuoniao.fineix.user.OptRegisterLoginActivity;
import com.taihuoniao.fineix.utils.Constants;
import com.taihuoniao.fineix.utils.GlideUtils;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.LogUtil;
import com.taihuoniao.fineix.utils.MapUtil;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.view.ScrollableView;
import com.taihuoniao.fineix.view.roundImageView.RoundedImageView;
import com.taihuoniao.fineix.zone.bean.ZoneDetailBean;
import com.taihuoniao.fineix.zone.fragment.ZoneRelateProductsFragment;
import com.taihuoniao.fineix.zone.fragment.ZoneRelateSceneFragment;
import com.taihuoniao.fineix.zone.fragment.ZoneShopInfoFragment;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionNo;
import com.yanzhenjie.permission.PermissionYes;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

import static com.taihuoniao.fineix.utils.Constants.REQUEST_CODE_SETTING;

/**
 * 地盘详情
 */
public class ZoneDetailActivity extends BaseActivity {
    private static final int REQUEST_CODE =100;
    @Bind(R.id.head_goback)
    ImageButton headGoback;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.ibtn_shoucang)
    ImageButton ibtnShoucang;
    @Bind(R.id.ibtn_share)
    ImageButton ibtnShare;
    @Bind(R.id.scrollableView)
    ScrollableView scrollableView;
    @Bind(R.id.zone_logo)
    RoundedImageView zoneLogo;
    @Bind(R.id.shop_name)
    TextView shopName;
    @Bind(R.id.high_light)
    TextView highLight;
    @Bind(R.id.ll_container)
    LinearLayout llContainer;
    @Bind(R.id.tab_layout)
    TabLayout tabLayout;
    @Bind(R.id.viewpager)
    ViewPager viewpager;
    @Bind(R.id.sub_title)
    TextView subTitle;
    @Bind(R.id.id_flowLayout)
    TagFlowLayout idFlowLayout;
    @Bind(R.id.shop_desc)
    TextView shopDesc;
    private boolean isFirstLoc = true;
    private ZoneDetailBean zoneDetailBean;
    private String sZoneId = ""; //地盘ID
    private TabViewPagerAdapter adapter;

    public ZoneDetailActivity() {
        super(R.layout.activity_zone_detial);
    }

    @Override
    protected void getIntentData() {
        Intent intent = getIntent();
        if (intent.hasExtra("id")) {
            sZoneId = intent.getStringExtra("id");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void initView() {
        highLight.getPaint().setFakeBoldText(true);
        ViewGroup.LayoutParams lp = scrollableView.getLayoutParams();
        lp.width = MainApplication.getContext().getScreenWidth();
        lp.height = lp.width;
        scrollableView.setLayoutParams(lp);
        setupViewPager(viewpager);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setupWithViewPager(viewpager);
    }

    private void setIndicatorWidth() throws NoSuchFieldException, IllegalAccessException {
        int margin = activity.getResources().getDimensionPixelSize(R.dimen.dp30);
        Class<?> tablayout = tabLayout.getClass();
        Field tabStrip = tablayout.getDeclaredField("mTabStrip");
        tabStrip.setAccessible(true);
        LinearLayout ll_tab = (LinearLayout) tabStrip.get(tabLayout);
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
    protected void installListener() {
        tabLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                try {
                    setIndicatorWidth();
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    tabLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    tabLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
            }
        });
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new TabViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(ZoneRelateSceneFragment.newInstance(sZoneId), getResources().getString(R.string.relate_scene));
        adapter.addFrag(ZoneRelateProductsFragment.newInstance(sZoneId), getResources().getString(R.string.relate_products));
        adapter.addFrag(ZoneShopInfoFragment.newInstance(sZoneId), getResources().getString(R.string.shop_info));
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(2);
    }

    static class TabViewPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public TabViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        public Fragment getFragment(int position) {
            if (mFragmentList.size() > position) {
                return mFragmentList.get(position);
            }
            return null;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }


    @OnClick({R.id.head_goback, R.id.look_detail, R.id.ibtn_shoucang, R.id.ibtn_share})
    void click(final View v) {
        switch (v.getId()) {
            case R.id.look_detail:
                Intent intent1 = new Intent(activity, LightSpotDetailActivity.class);
                intent1.putExtra(TAG,zoneDetailBean);
                activity.startActivity(intent1);
                break;
            case R.id.head_goback:
                finish();
                break;
            case R.id.ibtn_shoucang: //收藏
                if (LoginInfo.isUserLogin()) {
                    if (zoneDetailBean.is_favorite == 0) {
                        HashMap<String, String> params = ClientDiscoverAPI.getfavoriteRequestParams(sZoneId, "11");
                        HttpRequest.post(params, URL.FAVORITE_AJAX_FAVORITE, new GlobalDataCallBack() {
                            @Override
                            public void onStart() {
                                v.setEnabled(false);
                            }

                            @Override
                            public void onSuccess(String json) {
                                v.setEnabled(true);
                                HttpResponse response = JsonUtil.fromJson(json, HttpResponse.class);
                                if (response.isSuccess()) {
                                    zoneDetailBean.is_favorite = 1;
                                    ibtnShoucang.setImageResource(R.mipmap.shoucang_yes);
                                } else {
                                    ToastUtils.showError(R.string.network_err);
                                }

                            }

                            @Override
                            public void onFailure(String error) {
                                v.setEnabled(true);
                            }
                        });
                    } else { //取消收藏
                        HashMap<String, String> params = ClientDiscoverAPI.getcancelFavoriteRequestParams(sZoneId, "11");
                        HttpRequest.post(params, URL.FAVORITE_AJAX_CANCEL_FAVORITE, new GlobalDataCallBack() {
                            @Override
                            public void onStart() {
                                v.setEnabled(false);
                            }

                            @Override
                            public void onSuccess(String json) {
                                v.setEnabled(true);
                                HttpResponse response = JsonUtil.fromJson(json, HttpResponse.class);
                                if (response.isSuccess()) {
                                    zoneDetailBean.is_favorite = 0;
                                    ibtnShoucang.setImageResource(R.mipmap.shoucang_white);
                                } else {
                                    ToastUtils.showError(R.string.network_err);
                                }

                            }

                            @Override
                            public void onFailure(String error) {
                                v.setEnabled(true);
                            }
                        });

                    }
                } else {
                    MainApplication.which_activity = DataConstants.ZONE_DETAIL_ACTIVITY;
                    activity.startActivity(new Intent(activity, OptRegisterLoginActivity.class));
                }
                break;
            case R.id.ibtn_share: //分享
                Intent intent = new Intent(activity, ShareDialogActivity.class);
                intent.putExtra(ShareDialogActivity.class.getSimpleName(),zoneDetailBean);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
//        if (scrollableView != null) {
//            scrollableView.stop();
//        }
    }


    @Override
    public void onResume() {
        super.onResume();
//        if (scrollableView != null) {
//            scrollableView.start();
//        }
    }

    @Override
    protected void requestNet() {
        HashMap param = ClientDiscoverAPI.getZoneDetailParams(sZoneId,"0");
        HttpRequest.post(param, URL.ZONE_DETAIL, new GlobalDataCallBack() {

            @Override
            public void onSuccess(String json) {
                HttpResponse<ZoneDetailBean> response = JsonUtil.json2Bean(json, new TypeToken<HttpResponse<ZoneDetailBean>>() {
                });
                if (response.isSuccess()) {
                    zoneDetailBean = response.getData();
                    ((ZoneShopInfoFragment) adapter.getFragment(2)).setData(zoneDetailBean);
                    refreshUI();
                } else {
                    ToastUtils.showError(response.getMessage());
                }
            }

            @Override
            public void onFailure(String error) {
                LogUtil.e(error);
            }
        });
    }

    /**
     * 获得距离
     */
    private void getDistance() {
        MapUtil.getCurrentLocation(new MapUtil.OnReceiveLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                if (isFirstLoc) {
                    isFirstLoc = false;
                    LatLng p1 = new LatLng(bdLocation.getLatitude(),
                            bdLocation.getLongitude());
                    zoneDetailBean.location.myLocation = p1;
                    LatLng p2 = new LatLng(zoneDetailBean.location.coordinates.get(1), zoneDetailBean.location.coordinates.get(0));
                    double distance = MapUtil.getDistance(p1, p2);
                    LogUtil.e(distance + "");
                    if (distance < 1000) {
                        scrollableView.showDistance((int) distance + "m");
                    } else {
                        scrollableView.showDistance((int) (distance / 1000) + "km");
                    }
                }
            }
        });
    }

    @Override
    protected void refreshUI() {
        if (zoneDetailBean == null) return;
        if (AndPermission.hasPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)){
            getDistance();
        } else {
            AndPermission.with(this)
                    .requestCode(REQUEST_CODE)
                    .permission(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
                    .send();
        }

        if (zoneDetailBean.is_favorite == 1) {
            ibtnShoucang.setImageResource(R.mipmap.shoucang_yes);
        } else {
            ibtnShoucang.setImageResource(R.mipmap.shoucang_white);
        }
        tvTitle.setText(zoneDetailBean.title);
        subTitle.setText(zoneDetailBean.sub_title);
        idFlowLayout.setAdapter(new TagAdapter<String>(zoneDetailBean.tags) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                View view = View.inflate(activity, R.layout.item_tag, null);
                ((TextView) view).setText(s);
                return view;
            }
        });
        idFlowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                return true;
            }
        });

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(activity, zoneDetailBean.covers);
        scrollableView.setAdapter(viewPagerAdapter.setInfiniteLoop(true));
//        scrollableView.setAutoScrollDurationFactor(8);
//        scrollableView.setInterval(4000);
        scrollableView.showIndicatorRight();
//        scrollableView.start();
        setBrightSpots(zoneDetailBean.bright_spot);
        GlideUtils.displayImageFadein(zoneDetailBean.avatar_url, zoneLogo);
        shopName.setText(zoneDetailBean.title);

//        ratingbar.setRating(zoneDetailBean.score_average);
//        averageSpend.setText(zoneDetailBean.);
        shopDesc.setText(zoneDetailBean.des);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        // 只需要调用这一句，第一个参数是当前Acitivity/Fragment，回调方法写在当前Activity/Framgent。
        AndPermission.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    // 成功回调的方法，用注解即可，里面的数字是请求时的requestCode。
    @PermissionYes(REQUEST_CODE)
    private void getRequestYes(List<String> grantedPermissions) {
        getDistance();
    }

    // 失败回调的方法，用注解即可，里面的数字是请求时的requestCode。
    @PermissionNo(REQUEST_CODE)
    private void getPhoneStatusNo(List<String> deniedPermissions) {
        // 用户否勾选了不再提示并且拒绝了权限，那么提示用户到设置中授权。
        if (AndPermission.hasAlwaysDeniedPermission(this, deniedPermissions)) {
            // 第一种：用默认的提示语。
            AndPermission.defaultSettingDialog(this,REQUEST_CODE_SETTING).show();
        }else {
//            finish();
        }
    }

    private void setBrightSpots(List<String> brightSpot) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,getResources().getDimensionPixelSize(R.dimen.dp195));
        int size = getResources().getDimensionPixelSize(R.dimen.dp10);
        params.bottomMargin = size;
        imageParams.bottomMargin = size;
        int i=0;
        for (String str : brightSpot) {
            if (!str.contains(Constants.SEPERATOR)) continue;
            if (i>1) break;
            String[] split = str.split(Constants.SEPERATOR);
            if (TextUtils.equals(split[0], Constants.TEXT_TYPE)) {
                TextView textView = new TextView(activity);
                textView.setLayoutParams(params);
                textView.setText(split[1]);
                textView.setLineSpacing(1, 1.3f);
                textView.setTextColor(getResources().getColor(R.color.color_666));
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
                llContainer.addView(textView);
            } else if (TextUtils.equals(split[0], Constants.IMAGE_TYPE)) {
                ImageView imageView = new ImageView(activity);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setLayoutParams(imageParams);
                GlideUtils.displayImageFadein(split[1], imageView);
                llContainer.addView(imageView);
            }
            i++;
        }
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (TextUtils.equals(DataConstants.ZONE_DETAIL_ACTIVITY_NAME, intent.getAction())) {
                if (TextUtils.isEmpty(sZoneId)) return;
                requestNet();
            }
        }
    };

}
