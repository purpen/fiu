package com.taihuoniao.fineix.main.fragment;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dodola.bubblecloud.BubbleCloudView;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.EditRecyclerAdapter;
import com.taihuoniao.fineix.adapters.FiuBrandsAdapter;
import com.taihuoniao.fineix.adapters.GoodListAdapter;
import com.taihuoniao.fineix.adapters.PinLabelRecyclerAdapter;
import com.taihuoniao.fineix.adapters.PinRecyclerAdapter;
import com.taihuoniao.fineix.adapters.ViewPagerAdapter;
import com.taihuoniao.fineix.base.BaseFragment;
import com.taihuoniao.fineix.beans.Banner;
import com.taihuoniao.fineix.beans.BannerData;
import com.taihuoniao.fineix.beans.BrandListBean;
import com.taihuoniao.fineix.beans.CJHotLabelBean;
import com.taihuoniao.fineix.beans.CategoryListBean;
import com.taihuoniao.fineix.beans.LoginInfo;
import com.taihuoniao.fineix.beans.ProductBean;
import com.taihuoniao.fineix.beans.ShopCartNumber;
import com.taihuoniao.fineix.main.MainApplication;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.DataConstants;
import com.taihuoniao.fineix.network.HttpResponse;
import com.taihuoniao.fineix.product.BrandDetailActivity;
import com.taihuoniao.fineix.product.GoodsListActivity;
import com.taihuoniao.fineix.product.ShopCarActivity;
import com.taihuoniao.fineix.scene.SearchActivity;
import com.taihuoniao.fineix.user.OptRegisterLoginActivity;
import com.taihuoniao.fineix.utils.DensityUtils;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.utils.Util;
import com.taihuoniao.fineix.view.ScrollableView;
import com.taihuoniao.fineix.view.WaittingDialog;
import com.taihuoniao.fineix.view.pulltorefresh.PullToRefreshBase;
import com.taihuoniao.fineix.view.pulltorefresh.PullToRefreshListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class WellGoodsFragment extends BaseFragment<Banner> implements EditRecyclerAdapter.ItemClick, View.OnClickListener, AbsListView.OnScrollListener {
    //界面下的控件
    private RelativeLayout titlelayout;
    private ImageView searchImg;
    private RelativeLayout cartRelative;
    private TextView cartNumber;
    //    private ImageView cartImg;
    private PullToRefreshListView pullToRefreshView;
    private ListView listView;
    private ProgressBar progressBar;
    //headerview下的控件
    private BubbleCloudView absoluteLayout;
    private RecyclerView labelRecycler;
    private RecyclerView recyclerView;
    private WaittingDialog dialog;
    private static final String PAGE_NAME = "app_fiu_product_index_slide";
    //标签列表
    private List<String> hotLabelList;
    private PinLabelRecyclerAdapter pinLabelRecyclerAdapter;
    private int labelPage = 1;
    //分类列表
    private List<CategoryListBean.CategoryListItem> list;
    private PinRecyclerAdapter pinRecyclerAdapter;
    private DisplayImageOptions options;
    private ScrollableView scrollableView;
    private ViewPagerAdapter viewPagerAdapter;
    private int page = 1;
    private static final String PRODUCT_STATE = "1"; //表示正常在线
    //商品列表
    private int productPage = 1;
    private List<ProductBean.ProductListItem> productList;
    private GoodListAdapter goodListAdapter;
    private int lastSavedFirstVisibleItem = -1;
    private int lastTotalItem = -1;

    @Override
    protected View initView() {
        View view = View.inflate(getActivity(), R.layout.fragment_wellgoods, null);
        titlelayout = (RelativeLayout) view.findViewById(R.id.title_relative1);
        searchImg = (ImageView) view.findViewById(R.id.fragment_wellgoods_search);
        cartRelative = (RelativeLayout) view.findViewById(R.id.fragment_wellgoods_cart_relative);
        cartNumber = (TextView) view.findViewById(R.id.fragment_wellgoods_cart_number);
//        cartImg = (ImageView) view.findViewById(R.id.fragment_wellgoods_cart);
        pullToRefreshView = (PullToRefreshListView) view.findViewById(R.id.fragment_wellgoods_listview);
        listView = pullToRefreshView.getRefreshableView();
        progressBar = (ProgressBar) view.findViewById(R.id.fragment_wellgoods_progress);
        //headerview
        View header = View.inflate(getActivity(), R.layout.header_fragment_wellgoods, null);
        scrollableView = (ScrollableView) header.findViewById(R.id.scrollableView);
        absoluteLayout = (BubbleCloudView) header.findViewById(R.id.fragment_wellgoods_absolute);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(MainApplication.getContext().getScreenWidth(), DensityUtils.dp2px(getActivity(), 157));
        absoluteLayout.setLayoutParams(lp);
        absoluteLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                dis(absoluteLayout.getParent());
                absoluteLayout.onTouchEvent(event);
                return true;
            }
        });
        labelRecycler = (RecyclerView) header.findViewById(R.id.fragment_wellgoods_label_recycler);
        recyclerView = (RecyclerView) header.findViewById(R.id.fragment_wellgoods_recycler);
        listView.addHeaderView(header);
        pullToRefreshView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener() {
            @Override
            public void onRefresh() {
                productPage = 1;
                requestNet();
            }
        });
        dialog = new WaittingDialog(getActivity());
        pullToRefreshView.animLayout();
        return view;
    }

    private void dis(ViewParent viewParent) {
        viewParent.requestDisallowInterceptTouchEvent(true);
        if (viewParent.getParent() != null) {
            dis(viewParent.getParent());
        }
    }

    @Override
    protected void requestNet() {
        dialog.show();
        getProductList(null, null, null, productPage + "", 8 + "", null, null, null, null);
        ClientDiscoverAPI.getBanners(PAGE_NAME, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                if (responseInfo == null) {
                    return;
                }

                if (TextUtils.isEmpty(responseInfo.result)) {
                    return;
                }

                try {
                    BannerData bannerData = JsonUtil.fromJson(responseInfo.result, new TypeToken<HttpResponse<BannerData>>() {
                    });
                    if (bannerData == null) {
                        return;
                    }

                    if (bannerData.rows == null) {
                        return;
                    }

                    if (bannerData.rows.size() == 0) {
                        return;
                    }
                    refreshUI(bannerData.rows);
                } catch (JsonSyntaxException e) {
                    Util.makeToast(activity, "对不起,数据异常");
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                Util.makeToast(s);
            }
        });
//        DataPaser.hotLabelList(labelPage + "", handler);
        categoryList(1 + "", 10 + "", 1 + "");
        //热门标签
        cjHotLabel(false);
//        ClientDiscoverAPI.labelList(null, 1, null, 5, 1, new RequestCallBack<String>() {
//            @Override
//            public void onSuccess(ResponseInfo<String> responseInfo) {
//                Message msg = handler.obtainMessage();
//                msg.what = DataConstants.HOT_LABEL_LIST;
//                msg.obj = new HotLabel();
//                try {
//                    Gson gson = new Gson();
//                    Type type = new TypeToken<HotLabel>() {
//                    }.getType();
//                    msg.obj = gson.fromJson(responseInfo.result, type);
//                } catch (JsonSyntaxException e) {
//                    Toast.makeText(getActivity(), "数据异常", Toast.LENGTH_SHORT).show();
//                }
//                handler.sendMessage(msg);
//            }
//
//            @Override
//            public void onFailure(HttpException error, String msg) {
//                Log.e("<<<", "请求失败" + error.toString() + ",msg=" + msg);
//            }
//        });
//品牌列表
        brandList(1, 60);
    }

    private int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    @Override
    protected void initList() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            Log.e("<<<状态栏", "statusbarheight=" + getStatusBarHeight());
//            titlelayout.setPadding(0, getStatusBarHeight(), 0, 0);
//        }
        searchImg.setOnClickListener(this);
//        cartImg.setOnClickListener(this);
        cartRelative.setOnClickListener(this);
        ViewGroup.LayoutParams lp = scrollableView.getLayoutParams();
        lp.width = MainApplication.getContext().getScreenWidth();
        lp.height = lp.width * 422 / 750;
        scrollableView.setLayoutParams(lp);
        hotLabelList = new ArrayList<>();
        labelRecycler.setHasFixedSize(true);
        labelRecycler.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.HORIZONTAL));
        pinLabelRecyclerAdapter = new PinLabelRecyclerAdapter(getActivity(), hotLabelList, new EditRecyclerAdapter.ItemClick() {
            @Override
            public void click(int postion) {
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                intent.putExtra("q", hotLabelList.get(postion));
                intent.putExtra("t", "10");
                startActivity(intent);
            }
        });
        labelRecycler.addItemDecoration(new PinLabelRecyclerAdapter.LabelItemDecoration(getActivity()));
        labelRecycler.setAdapter(pinLabelRecyclerAdapter);
        list = new ArrayList<>();
        recyclerView.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 5);
        recyclerView.setLayoutManager(layoutManager);
        pinRecyclerAdapter = new PinRecyclerAdapter(getActivity(), list, this);
        recyclerView.setAdapter(pinRecyclerAdapter);
        productList = new ArrayList<>();
        goodListAdapter = new GoodListAdapter(getActivity(), productList, null);
        listView.setAdapter(goodListAdapter);
        listView.setOnScrollListener(this);
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.default_background_750_1334)
                .showImageForEmptyUri(R.mipmap.default_background_750_1334)
                .showImageOnFail(R.mipmap.default_background_750_1334)
                .cacheInMemory(true)
                .cacheOnDisk(true).considerExifParams(true)
                .build();
        absoluteLayout.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), BrandDetailActivity.class);
                intent.putExtra("id",brandItemList.get(position).get_id());
                startActivity(intent);
            }
        });
    }

//    private Handler handler = new Handler() {
//        @Override
//        public void handleMessage(final Message msg) {
//            switch (msg.what) {
//                case DataConstants.PARSER_SHOP_CART_NUMBER:
//                    if (msg.obj != null) {
//                        if (msg.obj instanceof ShopCartNumber) {
//                            ShopCartNumber numberCart;
//                            numberCart = (ShopCartNumber) msg.obj;
//                            if (!"0".equals(numberCart.getCount())) {
//                                cartNumber.setVisibility(View.VISIBLE);
//                                cartNumber.setText(numberCart.getCount());
//                            } else {
//                                cartNumber.setVisibility(View.GONE);
//                            }
//                        }
//                    }
//                    break;
//                case DataConstants.ADD_PRODUCT_LIST:
//                    pullToRefreshView.onRefreshComplete();
//                    dialog.dismiss();
//                    progressBar.setVisibility(View.GONE);
//                    ProductBean netProduct = (ProductBean) msg.obj;
//                    if (netProduct.isSuccess()) {
//                        if (productPage == 1) {
//                            productList.clear();
//                            lastSavedFirstVisibleItem = -1;
//                            lastTotalItem = -1;
//                        }
//
//                        productList.addAll(netProduct.getData().getRows());
//                        goodListAdapter.notifyDataSetChanged();
//                    }
//                    break;
//                case DataConstants.BRAND_LIST:
//                    BrandListBean netBrandListBean = (BrandListBean) msg.obj;
//                    if (netBrandListBean.isSuccess()) {
//                        brandItemList = netBrandListBean.getData().getRows();
//                        absoluteLayout.init(getActivity(),null,netBrandListBean);
//                    }
//                    break;
//                case DataConstants.CJ_HOTLABEL:
//                    CJHotLabelBean netHot = (CJHotLabelBean) msg.obj;
//                    if (netHot.isSuccess()) {
//                        hotLabelList.clear();
//                        hotLabelList.addAll(netHot.getData().getTags());
//                        pinLabelRecyclerAdapter.notifyDataSetChanged();
//                    } else {
//                        ToastUtils.showError(netHot.getMessage());
//                    }
//                    break;
//                case DataConstants.CATEGORY_LIST:
//                    CategoryBean netCategoryBean = (CategoryBean) msg.obj;
//                    if (netCategoryBean.isSuccess()) {
//                        list.clear();
//                        list.addAll(netCategoryBean.getList());
//                        pinRecyclerAdapter.notifyDataSetChanged();
//                    }
//                    break;
//                case DataConstants.NET_FAIL:
//                    pullToRefreshView.onRefreshComplete();
//                    dialog.dismiss();
//                    progressBar.setVisibility(View.GONE);
//                    break;
//            }
//        }
//    };

    @Override
    protected void refreshUI(ArrayList<Banner> list) {
        if (viewPagerAdapter == null) {
            viewPagerAdapter = new ViewPagerAdapter(activity, list);
            scrollableView.setAdapter(viewPagerAdapter.setInfiniteLoop(true));
            scrollableView.setAutoScrollDurationFactor(8);
            scrollableView.setInterval(4000);
            scrollableView.showIndicators();
            scrollableView.start();
        } else {
            viewPagerAdapter.notifyDataSetChanged();
        }
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
//        DataPaser.shopCartNumberParser(handler);
        ClientDiscoverAPI.shopCartNumberNet(new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                ShopCartNumber shopCartNumber = new ShopCartNumber();
                try {
                    JSONObject obj = new JSONObject(responseInfo.result);
                    shopCartNumber.setSuccess(obj.optBoolean("success"));
                    JSONObject cartNumberObj = obj.getJSONObject("data");
                    shopCartNumber.setCount(cartNumberObj.optInt("count"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
//                    if (msg.obj != null) {
//                        if (msg.obj instanceof ShopCartNumber) {
                ShopCartNumber numberCart;
                numberCart = shopCartNumber;
                if (numberCart.getCount() > 0) {
                    cartNumber.setVisibility(View.VISIBLE);
                    cartNumber.setText(numberCart.getCount() + "");
                } else {
                    cartNumber.setVisibility(View.GONE);
                }
//                        }
//                    }
            }

            @Override
            public void onFailure(HttpException e, String s) {

            }
        });
        if (scrollableView != null) {
            scrollableView.start();
        }
    }


    @Override
    public void click(int postion) {
        Intent intent = new Intent(getActivity(), GoodsListActivity.class);
        intent.putExtra("position", postion);
        startActivity(intent);
    }

    private List<BrandListBean.BrandItem> brandItemList;


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fragment_wellgoods_search:
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                intent.putExtra("t", "10");
                startActivity(intent);
                break;
            case R.id.fragment_wellgoods_cart_relative:
                if (!LoginInfo.isUserLogin()) {
//                    Toast.makeText(getActivity(), "请先登录", Toast.LENGTH_SHORT).show();
                    MainApplication.which_activity = DataConstants.ElseActivity;
                    startActivity(new Intent(getActivity(), OptRegisterLoginActivity.class));
                    return;
                }
                Intent intent1 = new Intent(getActivity(), ShopCarActivity.class);
                startActivity(intent1);
                break;
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        //由于添加了headerview的原因，所以visibleitemcount要大于1，正常只需要大于0就可以
        if (visibleItemCount > 1 && (firstVisibleItem + visibleItemCount >= totalItemCount)
                && firstVisibleItem != lastSavedFirstVisibleItem && lastTotalItem != totalItemCount
                ) {
            lastSavedFirstVisibleItem = firstVisibleItem;
            lastTotalItem = totalItemCount;
            productPage++;
            progressBar.setVisibility(View.VISIBLE);
            getProductList(null, null, null, productPage + "", 8 + "", null, null, null, null);
        }
    }

    //产品
    //列表
    private void getProductList(String category_id, String brand_id, String category_tag_ids, String page, String size, String ids, String ignore_ids,
                                String stick, String fine) {
        ClientDiscoverAPI.getProductList(category_id, brand_id, category_tag_ids, page, size, ids, ignore_ids, stick, fine, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                ProductBean productBean = new ProductBean();
                try {
                    Gson gson = new Gson();
                    Type type = new TypeToken<ProductBean>() {
                    }.getType();
                    productBean = gson.fromJson(responseInfo.result, type);
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                }
                pullToRefreshView.onRefreshComplete();
                dialog.dismiss();
                progressBar.setVisibility(View.GONE);
                ProductBean netProduct = productBean;
                if (netProduct.isSuccess()) {
                    if (productPage == 1) {
                        pullToRefreshView.setLoadingTime();
                        productList.clear();
                        lastSavedFirstVisibleItem = -1;
                        lastTotalItem = -1;
                    }

                    productList.addAll(netProduct.getData().getRows());
                    goodListAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                pullToRefreshView.onRefreshComplete();
                dialog.dismiss();
                progressBar.setVisibility(View.GONE);
                ToastUtils.showError(R.string.net_fail);
            }
        });
    }

    //分类列表
    private void categoryList(String page, String domain, String show_all) {
        ClientDiscoverAPI.categoryList(page, domain, show_all, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                CategoryListBean categoryListBean = new CategoryListBean();
                try {
                    Gson gson = new Gson();
                    Type type = new TypeToken<CategoryListBean>() {
                    }.getType();
                    categoryListBean = gson.fromJson(responseInfo.result, type);
                } catch (JsonSyntaxException e) {
                    Log.e("<<<分类列表", "数据解析异常" + e.toString());
                }
                CategoryListBean netCategoryBean = categoryListBean;
                if (netCategoryBean.isSuccess()) {
                    list.clear();
                    list.addAll(netCategoryBean.getData().getRows());
                    pinRecyclerAdapter.notifyDataSetChanged();
                } else {
                    dialog.dismiss();
                    ToastUtils.showError(netCategoryBean.getMessage());
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                pullToRefreshView.onRefreshComplete();
                dialog.dismiss();
                progressBar.setVisibility(View.GONE);
                ToastUtils.showError(R.string.net_fail);
            }
        });

    }

    //热门标签
    private void cjHotLabel(boolean isCJ) {
        ClientDiscoverAPI.cjHotLabel(isCJ, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                CJHotLabelBean cjHotLabelBean = new CJHotLabelBean();
                try {
                    Gson gson = new Gson();
                    Type type = new TypeToken<CJHotLabelBean>() {
                    }.getType();
                    cjHotLabelBean = gson.fromJson(responseInfo.result, type);
                } catch (JsonSyntaxException e) {
                    Log.e("<<<场景热门标签", "数据解析异常");
                }
                CJHotLabelBean netHot = cjHotLabelBean;
                if (netHot.isSuccess()) {
                    hotLabelList.clear();
                    hotLabelList.addAll(netHot.getData().getTags());
                    pinLabelRecyclerAdapter.notifyDataSetChanged();
                } else {
                    dialog.dismiss();
                    ToastUtils.showError(netHot.getMessage());
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                pullToRefreshView.onRefreshComplete();
                dialog.dismiss();
                progressBar.setVisibility(View.GONE);
                ToastUtils.showError(R.string.net_fail);
            }
        });
    }

    //品牌列表
    private void brandList(int page, int size) {
        ClientDiscoverAPI.brandList(page, size, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                BrandListBean brandListBean = new BrandListBean();
                try {
                    Gson gson = new Gson();
                    Type type = new TypeToken<BrandListBean>() {
                    }.getType();
                    brandListBean = gson.fromJson(responseInfo.result, type);
                } catch (JsonSyntaxException e) {
                    Log.e("<<<", "数据异常" + e.toString());
                }
                BrandListBean netBrandListBean = brandListBean;
                if (netBrandListBean.isSuccess()) {
                    FiuBrandsAdapter fiuBrandsAdapter = new FiuBrandsAdapter(getActivity(), netBrandListBean);
                    brandItemList = netBrandListBean.getData().getRows();
                    absoluteLayout.setAdapter(fiuBrandsAdapter);
//                    absoluteLayout.init(getActivity(), null, netBrandListBean);
                } else {
                    ToastUtils.showError(netBrandListBean.getMessage());
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                dialog.dismiss();
                ToastUtils.showError(R.string.net_fail);
            }
        });
    }
}
