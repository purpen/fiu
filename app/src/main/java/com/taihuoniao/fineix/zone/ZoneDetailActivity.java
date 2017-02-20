package com.taihuoniao.fineix.zone;

import android.app.Dialog;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mapapi.map.MapView;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.ViewPagerAdapter;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.base.HttpRequest;
import com.taihuoniao.fineix.beans.HttpResponse;
import com.taihuoniao.fineix.beans.SceneList;
import com.taihuoniao.fineix.common.GlobalDataCallBack;
import com.taihuoniao.fineix.main.MainApplication;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.URL;
import com.taihuoniao.fineix.user.MessageActivity;
import com.taihuoniao.fineix.utils.Constants;
import com.taihuoniao.fineix.utils.GlideUtils;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.LogUtil;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.utils.Util;
import com.taihuoniao.fineix.view.CustomHeadView;
import com.taihuoniao.fineix.view.ScrollableView;
import com.taihuoniao.fineix.view.dialog.WaittingDialog;
import com.taihuoniao.fineix.view.roundImageView.RoundedImageView;
import com.taihuoniao.fineix.zone.adapter.ZoneRelateProductsAdapter;
import com.taihuoniao.fineix.zone.adapter.ZoneRelateSceneAdapter;
import com.taihuoniao.fineix.zone.adapter.ZoneShopInfoAdapter;
import com.taihuoniao.fineix.zone.bean.ZoneDetailBean;
import com.taihuoniao.fineix.zone.bean.ZoneRelateProductsBean;
import com.taihuoniao.fineix.zone.bean.ZoneRelateSceneBean;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 地盘详情
 */
public class ZoneDetailActivity extends BaseActivity implements View.OnClickListener {
//    @Bind(R.id.head_view)
//    CustomHeadView headView;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.ibtn_shoucang)
    ImageButton ibtnShouCang;
    @Bind(R.id.relate_scene)
    ListView relateScene;
    @Bind(R.id.relate_products)
    ListView relateProducts;
    @Bind(R.id.merchant_info)
    ListView merchantInfo;
    ScrollableView scrollableView;
    RoundedImageView zoneLogo;
    TextView shopName;
    RatingBar ratingbar;
    TextView averageSpend;
    TextView takeCoupon;
    TextView shopDesc;
    Button btnSpread;
    TextView highLight;
    Button highLightDetail;
    TextView tvRule;
    View lineRule;
    RelativeLayout rlRule;
    TextView tvParticipate;
    View lineParticipate;
    RelativeLayout rlParticipate;
    TextView tvResult;
    View lineResult;
    RelativeLayout rlResult;
    private LinearLayout llContainer;
    private ZoneRelateSceneAdapter relateSceneAdapter;
    private ZoneRelateProductsAdapter relateProductsAdapter;
    private ZoneShopInfoAdapter shopInfoAdapter;
    private List<SceneList.DataBean.RowsBean> relateSceneList;
    private List<ZoneRelateProductsBean.RowsBean> relateProductList;
    private List commentList;
    private ZoneDetailBean zoneDetailBean;
    private Dialog dialog;
    private int currentPageScene = 1;
    private int currentPageProducts = 1;
    private String stick = "1";//默认是推荐
    private String sort = "1"; //默认是推荐
    private boolean isSceneBottom;
    private boolean isProductsBottom;
    private static final int SHRINK=0;
    private static final int SPREAD=1;
    private int mState = SHRINK;
    private String qjId="";
    private String title="";
    public ZoneDetailActivity() {
        super(R.layout.activity_zone_detial);
    }

    @Override
    protected void getIntentData() {
        Intent intent = getIntent();
        if (intent.hasExtra("id")) {
            qjId = intent.getStringExtra("id");
        }
        if (intent.hasExtra("title")) {
            title = intent.getStringExtra("title");
        }
    }

    @Override
    protected void initView() {
        tvTitle.setText(title);
        dialog = new WaittingDialog(this);
        View view = Util.inflateView(activity, R.layout.zone_detail_head_layout, null);
        scrollableView = ButterKnife.findById(view, R.id.scrollableView);
        ViewGroup.LayoutParams lp = scrollableView.getLayoutParams();
        lp.width = MainApplication.getContext().getScreenWidth();
        lp.height = lp.width;
        scrollableView.setLayoutParams(lp);
        zoneLogo = ButterKnife.findById(view, R.id.zone_logo);
        shopName = ButterKnife.findById(view, R.id.shop_name);
        ratingbar = ButterKnife.findById(view, R.id.ratingbar);
        averageSpend = ButterKnife.findById(view, R.id.average_spend);
        takeCoupon = ButterKnife.findById(view, R.id.take_coupon);
        shopDesc = ButterKnife.findById(view, R.id.shop_desc);
        btnSpread = ButterKnife.findById(view, R.id.btn_spread);
        highLight = ButterKnife.findById(view, R.id.high_light);
        llContainer = ButterKnife.findById(view, R.id.ll_container);

        highLightDetail = ButterKnife.findById(view, R.id.high_light_detail);

        rlRule = ButterKnife.findById(view, R.id.rl_rule);
        tvRule = ButterKnife.findById(view, R.id.tv_rule);
        tvRule.setTextColor(getResources().getColor(R.color.yellow_bd8913));
        lineRule = ButterKnife.findById(view, R.id.line_rule);

        rlParticipate = ButterKnife.findById(view, R.id.rl_participate);
        tvParticipate = ButterKnife.findById(view, R.id.tv_participate);
        lineParticipate = ButterKnife.findById(view, R.id.line_participate);

        rlResult = ButterKnife.findById(view, R.id.rl_result);
        tvResult = ButterKnife.findById(view, R.id.tv_result);
        lineResult = ButterKnife.findById(view, R.id.line_result);

        relateScene.addHeaderView(view);
        relateProducts.addHeaderView(view);
        merchantInfo.addHeaderView(view);

        relateSceneList = new ArrayList();
        relateSceneAdapter = new ZoneRelateSceneAdapter(activity, relateSceneList);
        relateScene.setAdapter(relateSceneAdapter);

        relateProductList = new ArrayList();
        relateProductsAdapter = new ZoneRelateProductsAdapter(activity, relateProductList);
        relateProducts.setAdapter(relateProductsAdapter);
    }

    private void setShopInfo() {
        commentList = new ArrayList();
        shopInfoAdapter = new ZoneShopInfoAdapter(activity, commentList, zoneDetailBean);
        merchantInfo.setAdapter(shopInfoAdapter);
    }

    @Override
    protected void installListener() {
        btnSpread.setOnClickListener(this);
        rlRule.setOnClickListener(this);
        rlParticipate.setOnClickListener(this);
        rlResult.setOnClickListener(this);

        relateScene.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
                LogUtil.e(relateScene.getLastVisiblePosition() + ";;;;" + relateSceneList.size());
                if (!isSceneBottom && relateScene.getLastVisiblePosition() == relateSceneList.size()) {
                    loadRelateScene();
                }
            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {

            }
        });

        relateProducts.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == SCROLL_STATE_IDLE || scrollState == SCROLL_STATE_FLING) {
                    if (relateProductList.size() % 2 == 0) {
                        if (view.getLastVisiblePosition() == relateSceneList.size() / 2) {
                            LogUtil.e("curPage==偶数", currentPageProducts + "");
//                            loadData();
                        }
                    } else {
                        if (view.getLastVisiblePosition() == relateSceneList.size() / 2 + 1) {
                            LogUtil.e("curPage==奇数", currentPageProducts + "");
//                            loadData();
                        }
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_spread:
                switch (mState){
                    case SHRINK:
                        shopDesc.setMaxLines(Integer.MAX_VALUE);
                        shopDesc.requestLayout();
                        btnSpread.setText(R.string.look_shrink);
                        mState = SPREAD;
                        break;
                    case SPREAD:
                        shopDesc.setMaxLines(3);
                        shopDesc.requestLayout();
                        btnSpread.setText(R.string.look_more);
                        mState = SHRINK;
                        break;
                    default:
                        break;
                }
                break;
            case R.id.rl_rule:
                resetUI();
                relateScene.setVisibility(View.VISIBLE);
                lineRule.setVisibility(View.VISIBLE);
                tvRule.setTextColor(getResources().getColor(R.color.yellow_bd8913));
                break;
            case R.id.rl_participate:// 点击后为空去加载相关产品
                if (relateProductList.size() == 0) loadRelateProducts();
                resetUI();
                relateProducts.setVisibility(View.VISIBLE);
                lineParticipate.setVisibility(View.VISIBLE);
                tvParticipate.setTextColor(getResources().getColor(R.color.yellow_bd8913));
                break;
            case R.id.rl_result:
                resetUI();
                merchantInfo.setVisibility(View.VISIBLE);
                lineResult.setVisibility(View.VISIBLE);
                tvResult.setTextColor(getResources().getColor(R.color.yellow_bd8913));
                break;
        }
    }

    @OnClick({R.id.head_goback,R.id.ibtn_shoucang,R.id.ibtn_share})
    void click(final View v){
        switch (v.getId()){
            case R.id.head_goback:
                finish();
                break;
            case R.id.ibtn_shoucang: //收藏
                if (zoneDetailBean.is_love==0){
                    HashMap<String, String> params = ClientDiscoverAPI.getfavoriteRequestParams("62", "11");
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
                                zoneDetailBean.is_love = 1;
                                ibtnShouCang.setImageResource(R.mipmap.shoucang_yes);
                            }else {
                                ToastUtils.showError(R.string.network_err);
                            }

                        }

                        @Override
                        public void onFailure(String error) {
                            v.setEnabled(true);
                        }
                    });
                }else { //取消收藏
                    HashMap<String, String> params = ClientDiscoverAPI.getcancelFavoriteRequestParams("62", "11");
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
                                zoneDetailBean.is_love = 0;
                                ibtnShouCang.setImageResource(R.mipmap.shoucang_white);
                            }else {
                                ToastUtils.showError(R.string.network_err);
                            }

                        }

                        @Override
                        public void onFailure(String error) {
                            v.setEnabled(true);
                        }
                    });

                }
                break;
            case R.id.ibtn_share: //分享
                ToastUtils.showInfo("分享");
                break;
            default:
                break;
        }
    }

    private void resetUI() {
        relateScene.setVisibility(View.GONE);
        relateProducts.setVisibility(View.GONE);
        merchantInfo.setVisibility(View.GONE);
        lineRule.setVisibility(View.INVISIBLE);
        lineParticipate.setVisibility(View.INVISIBLE);
        lineResult.setVisibility(View.INVISIBLE);
        tvRule.setTextColor(getResources().getColor(R.color.color_666));
        tvParticipate.setTextColor(getResources().getColor(R.color.color_666));
        tvResult.setTextColor(getResources().getColor(R.color.color_666));
    }

    @Override
    public void onPause() {
        super.onPause();
        if (scrollableView != null) {
            scrollableView.stop();
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        if (scrollableView != null) {
            scrollableView.start();
        }
    }

    @Override
    protected void requestNet() {
        //TODO 62换成情境id
        HashMap param = ClientDiscoverAPI.getZoneDetailParams("62");
        HttpRequest.post(param, URL.ZONE_DETAIL, new GlobalDataCallBack() {
            @Override
            public void onStart() {
                if (dialog != null && !activity.isFinishing()) dialog.show();
            }

            @Override
            public void onSuccess(String json) {
                if (dialog != null && !activity.isFinishing()) dialog.dismiss();
                LogUtil.e(json);
                HttpResponse<ZoneDetailBean> response = JsonUtil.json2Bean(json, new TypeToken<HttpResponse<ZoneDetailBean>>() {
                });
                zoneDetailBean = response.getData();
                loadRelateScene();
                setShopInfo();
                refreshUI();
            }

            @Override
            public void onFailure(String error) {
                LogUtil.e(error);
                if (dialog != null && !activity.isFinishing()) dialog.dismiss();
            }
        });
    }

    /**
     * 加载相关产品
     */
    private void loadRelateProducts() {
        HashMap param = ClientDiscoverAPI.getRelateProducts(currentPageProducts, zoneDetailBean._id);
        HttpRequest.post(param, URL.ZONE_RELATE_PRODUCTS, new GlobalDataCallBack() {
            @Override
            public void onStart() {
                if (dialog != null && !activity.isFinishing()) dialog.show();
            }

            @Override
            public void onSuccess(String json) {
                if (dialog != null && !activity.isFinishing()) dialog.dismiss();
                LogUtil.e(json);
                HttpResponse<ZoneRelateProductsBean> response = JsonUtil.json2Bean(json, new TypeToken<HttpResponse<ZoneRelateProductsBean>>() {
                });

                if (response.getData().rows.size() > 0) {
                    currentPageProducts++;
                    relateProductList.addAll(response.getData().rows);
                    refreshRelateProductsUI();
                } else {
                    isProductsBottom = true;
                }
            }

            @Override
            public void onFailure(String error) {
                LogUtil.e(error);
                if (dialog != null && !activity.isFinishing()) dialog.dismiss();
            }
        });
    }

    private void refreshRelateProductsUI() {
        if (relateProductsAdapter == null) {
            relateProducts.setAdapter(relateProductsAdapter);
        } else {
            relateProductsAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 加载相关情境
     */
    private void loadRelateScene() {
        HashMap param = ClientDiscoverAPI.getRelateScene(currentPageScene, zoneDetailBean._id, sort, stick);
        HttpRequest.post(param, URL.ZONE_RELATE_SCENE, new GlobalDataCallBack() {
            @Override
            public void onStart() {
                if (dialog != null && !activity.isFinishing()) dialog.show();
            }

            @Override
            public void onSuccess(String json) {
                if (dialog != null && !activity.isFinishing()) dialog.dismiss();
//                HttpResponse<ZoneRelateSceneBean> response = JsonUtil.json2Bean(json, new TypeToken<HttpResponse<ZoneRelateSceneBean>>() {
//                });
                SceneList sceneL = new SceneList();
                try {
                    Gson gson = new Gson();
                    Type type = new TypeToken<SceneList>() {
                    }.getType();
                    sceneL = gson.fromJson(json, type);
                } catch (JsonSyntaxException e) {
                    Log.e("<<<", "情景列表解析异常" + e.toString());
                }
                if (sceneL.getData().getRows().size() > 0) {
                    currentPageScene++;
                    relateSceneList.addAll(sceneL.getData().getRows());
                    refreshRelateSceneUI();
                } else {
                    isSceneBottom = true;
                }
            }

            @Override
            public void onFailure(String error) {
                LogUtil.e(error);
                if (dialog != null && !activity.isFinishing()) dialog.dismiss();
            }
        });
    }

    private void refreshRelateSceneUI() {
        if (relateSceneAdapter == null) {
            relateScene.setAdapter(relateSceneAdapter);
        } else {
            relateSceneAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void refreshUI() {
        if (zoneDetailBean == null) return;
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(activity, zoneDetailBean.banners);
        scrollableView.setAdapter(viewPagerAdapter.setInfiniteLoop(true));
        scrollableView.setAutoScrollDurationFactor(8);
        scrollableView.setInterval(4000);
        scrollableView.showIndicators();
        scrollableView.start();
        setBrightSpots(zoneDetailBean.bright_spot);
        GlideUtils.displayImageFadein(zoneDetailBean.avatar_url, zoneLogo);
        shopName.setText(zoneDetailBean.title);
        ratingbar.setRating(zoneDetailBean.score_average);
//        averageSpend.setText(zoneDetailBean.);
        shopDesc.setText(zoneDetailBean.des);
    }

    private void setBrightSpots(List<String> brightSpot) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.bottomMargin =getResources().getDimensionPixelSize(R.dimen.dp10);
        for (String str : brightSpot) {
            if (!str.contains(Constants.SEPERATOR)) continue;
            String[] split = str.split(Constants.SEPERATOR);
            if (TextUtils.equals(split[0],Constants.TEXT_TYPE)){
                TextView textView = new TextView(activity);
                textView.setLayoutParams(params);
                textView.setText(split[1]);
                textView.setLineSpacing(getResources().getDimensionPixelSize(R.dimen.dp3),1);
                textView.setTextColor(getResources().getColor(R.color.color_666));
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP,12);
                llContainer.addView(textView);
            }else if(TextUtils.equals(split[0],Constants.IMAGE_TYPE)){
                ImageView imageView = new ImageView(activity);
                imageView.setLayoutParams(params);
                GlideUtils.displayImageFadein(split[1],imageView);
                llContainer.addView(imageView);
            }
        }
    }
}
