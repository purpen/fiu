package com.taihuoniao.fineix.main.fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsoluteLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.EditRecyclerAdapter;
import com.taihuoniao.fineix.adapters.GoodListAdapter;
import com.taihuoniao.fineix.adapters.PinLabelRecyclerAdapter;
import com.taihuoniao.fineix.adapters.PinRecyclerAdapter;
import com.taihuoniao.fineix.adapters.ViewPagerAdapter;
import com.taihuoniao.fineix.base.BaseFragment;
import com.taihuoniao.fineix.beans.Banner;
import com.taihuoniao.fineix.beans.BannerData;
import com.taihuoniao.fineix.beans.BrandListBean;
import com.taihuoniao.fineix.beans.CategoryBean;
import com.taihuoniao.fineix.beans.CategoryListBean;
import com.taihuoniao.fineix.beans.HotLabel;
import com.taihuoniao.fineix.beans.LoginInfo;
import com.taihuoniao.fineix.beans.ProductBean;
import com.taihuoniao.fineix.beans.ProductListBean;
import com.taihuoniao.fineix.beans.RandomImg;
import com.taihuoniao.fineix.beans.ShopCartNumber;
import com.taihuoniao.fineix.main.MainApplication;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.DataConstants;
import com.taihuoniao.fineix.network.DataPaser;
import com.taihuoniao.fineix.network.HttpResponse;
import com.taihuoniao.fineix.product.BrandDetailActivity;
import com.taihuoniao.fineix.product.GoodsListActivity;
import com.taihuoniao.fineix.product.ShopCarActivity;
import com.taihuoniao.fineix.scene.SearchActivity;
import com.taihuoniao.fineix.utils.DensityUtils;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.Util;
import com.taihuoniao.fineix.view.ScrollableView;
import com.taihuoniao.fineix.view.WaittingDialog;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WellGoodsFragment extends BaseFragment<Banner> implements EditRecyclerAdapter.ItemClick, View.OnClickListener, AbsListView.OnScrollListener {
    //界面下的控件
    private RelativeLayout titlelayout;
    private ImageView searchImg;
    private RelativeLayout cartRelative;
    private TextView cartNumber;
    //    private ImageView cartImg;
    private ListView listView;
    private ProgressBar progressBar;
    //headerview下的控件
    private AbsoluteLayout absoluteLayout;
    private RecyclerView labelRecycler;
    private RecyclerView recyclerView;
    private WaittingDialog dialog;
    private static final String PAGE_NAME = "app_fiu_product_index_slide";
    //标签列表
    private List<HotLabel.HotLabelBean> hotLabelList;
    private PinLabelRecyclerAdapter pinLabelRecyclerAdapter;
    private int labelPage = 1;
    //分类列表
    private List<CategoryListBean> list;
    private PinRecyclerAdapter pinRecyclerAdapter;
    private DisplayImageOptions options;
    private ScrollableView scrollableView;
    private ViewPagerAdapter viewPagerAdapter;
    private int page = 1;
    private static final String PRODUCT_STATE = "1"; //表示正常在线
    //商品列表
    private int productPage = 1;
    private List<ProductListBean> productList;
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
        listView = (ListView) view.findViewById(R.id.fragment_wellgoods_listview);
        progressBar = (ProgressBar) view.findViewById(R.id.fragment_wellgoods_progress);
        //headerview
        View header = View.inflate(getActivity(), R.layout.header_fragment_wellgoods, null);
        scrollableView = (ScrollableView) header.findViewById(R.id.scrollableView);
        absoluteLayout = (AbsoluteLayout) header.findViewById(R.id.fragment_wellgoods_absolute);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(MainApplication.getContext().getScreenWidth(), DensityUtils.dp2px(getActivity(), 157));
        absoluteLayout.setLayoutParams(lp);
        labelRecycler = (RecyclerView) header.findViewById(R.id.fragment_wellgoods_label_recycler);
        recyclerView = (RecyclerView) header.findViewById(R.id.fragment_wellgoods_recycler);
        listView.addHeaderView(header);
        dialog = new WaittingDialog(getActivity());
        return view;
    }


    @Override
    protected void requestNet() {
        dialog.show();
        DataPaser.getProductList(null, null, null, productPage + "", 8 + "", null, null, null, null, handler);
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
        DataPaser.categoryList(1 + "", 10 + "", handler);
        //热门标签
        ClientDiscoverAPI.labelList(null, 1, null, 5, 1, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Message msg = handler.obtainMessage();
                msg.what = DataConstants.HOT_LABEL_LIST;
                msg.obj = new HotLabel();
                try {
                    Gson gson = new Gson();
                    Type type = new TypeToken<HotLabel>() {
                    }.getType();
                    msg.obj = gson.fromJson(responseInfo.result, type);
                } catch (JsonSyntaxException e) {
                    Toast.makeText(getActivity(), "数据异常", Toast.LENGTH_SHORT).show();
                }
                handler.sendMessage(msg);
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                Log.e("<<<", "请求失败" + error.toString() + ",msg=" + msg);
            }
        });
//品牌列表
        DataPaser.brandList(1, 50, handler);
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Log.e("<<<状态栏", "statusbarheight=" + getStatusBarHeight());
            titlelayout.setPadding(0, getStatusBarHeight(), 0, 0);
        }
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
                intent.putExtra("q", hotLabelList.get(postion).getTitle_cn());
                intent.putExtra("t", "10");
                startActivity(intent);
            }
        });
        labelRecycler.addItemDecoration(new PinLabelRecyclerAdapter.LabelItemDecoration(getActivity()));
        labelRecycler.setAdapter(pinLabelRecyclerAdapter);
        list = new ArrayList<>();
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        pinRecyclerAdapter = new PinRecyclerAdapter(getActivity(), list, this);
        recyclerView.setAdapter(pinRecyclerAdapter);
        productList = new ArrayList<>();
        goodListAdapter = new GoodListAdapter(getActivity(), productList, null);
        listView.setAdapter(goodListAdapter);
        listView.setOnScrollListener(this);
        options = new DisplayImageOptions.Builder()
//                .showImageOnLoading(R.mipmap.default_backround)
//                .showImageForEmptyUri(R.mipmap.default_backround)
//                .showImageOnFail(R.mipmap.default_backround)
                .cacheInMemory(true)
                .cacheOnDisk(true).considerExifParams(true)
                .displayer(new RoundedBitmapDisplayer(360)).build();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(final Message msg) {
            switch (msg.what) {
                case DataConstants.PARSER_SHOP_CART_NUMBER:
                    if (msg.obj != null) {
                        if (msg.obj instanceof ShopCartNumber) {
                            ShopCartNumber numberCart = null;
                            numberCart = (ShopCartNumber) msg.obj;
                            if (!"0".equals(numberCart.getCount())) {
                                cartNumber.setVisibility(View.VISIBLE);
                                cartNumber.setText(numberCart.getCount());
                            } else {
                                cartNumber.setVisibility(View.GONE);
                            }
                        }
                    }
                    break;
                case DataConstants.ADD_PRODUCT_LIST:
                    progressBar.setVisibility(View.GONE);
                    ProductBean netProduct = (ProductBean) msg.obj;
                    if (netProduct.isSuccess()) {
                        if (productPage == 1) {
                            productList.clear();
                            lastSavedFirstVisibleItem = -1;
                            lastTotalItem = -1;
                        }
                        productList.addAll(netProduct.getList());
                        goodListAdapter.notifyDataSetChanged();
                    }
                    break;
                case DataConstants.BRAND_LIST:
                    BrandListBean netBrandListBean = (BrandListBean) msg.obj;
                    if (netBrandListBean.isSuccess()) {
                        brandItemList = netBrandListBean.getData().getRows();
                        thread = new Thread() {
                            @Override
                            public void run() {
                                int top = absoluteLayout.getTop();
                                int bottom = absoluteLayout.getBottom();
                                while (bottom - top <= 0) {
                                    try {
                                        Thread.sleep(100);
                                    } catch (Exception e) {
                                    } finally {
                                        top = absoluteLayout.getTop();
                                        bottom = absoluteLayout.getBottom();
                                        continue;
                                    }
                                }
                                handler.sendEmptyMessage(-10);
                            }
                        };
                        thread.start();
                    }
                    break;
                case -10:
                    addImgToAbsolute(brandItemList);
                    break;
                case DataConstants.HOT_LABEL_LIST:
                    dialog.dismiss();
                    HotLabel netHotLabel = (HotLabel) msg.obj;
                    if (netHotLabel.isSuccess()) {
                        hotLabelList.addAll(netHotLabel.getData().getRows());
                        pinLabelRecyclerAdapter.notifyDataSetChanged();
                    }
                    break;
                case DataConstants.CATEGORY_LIST:
                    dialog.dismiss();
                    CategoryBean netCategoryBean = (CategoryBean) msg.obj;
                    if (netCategoryBean.isSuccess()) {
                        list.addAll(netCategoryBean.getList());
                        pinRecyclerAdapter.notifyDataSetChanged();
                    }
                    break;
                case DataConstants.NET_FAIL:
                    dialog.dismiss();
                    progressBar.setVisibility(View.GONE);
                    break;
            }
        }
    };
    private Thread thread;

    @Override
    protected void refreshUI(ArrayList<Banner> list) {
        ArrayList<String> urlList = new ArrayList<String>();
        for (Banner banner : list) {
            urlList.add(banner.cover_url);
        }
        if (urlList.size() == 0) {
            return;
        }

        if (viewPagerAdapter == null) {
            viewPagerAdapter = new ViewPagerAdapter(activity, urlList);
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
        DataPaser.shopCartNumberParser(handler);

        if (scrollableView != null) {
            scrollableView.start();
        }
    }

    @Override
    public void onDestroy() {
        //        cancelNet();
        if (thread != null&&thread.isAlive()) {
            thread.stop();
            thread = null;
        }
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
            handler = null;
        }
        super.onDestroy();
    }

    @Override
    public void click(int postion) {
        Intent intent = new Intent(getActivity(), GoodsListActivity.class);
        intent.putExtra("position", postion);
        startActivity(intent);
    }

    private List<BrandListBean.BrandItem> brandItemList;

    /**
     * 数据里应有图片地址，类型，id，
     * 大 51dp 中 33dp 小 21dp
     */
    private void addImgToAbsolute(List<BrandListBean.BrandItem> list) {
        if (list == null) {
            return;
        }
        int top = absoluteLayout.getTop();
        int bottom = absoluteLayout.getBottom();
        Random random = new Random();
        List<RandomImg> randomImgs = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            RandomImg randomImg = new RandomImg();
            randomImg.id = list.get(i).get_id().get$id();
            randomImg.url = list.get(i).getCover_url();
            randomImg.type = i % 3 + "";
            switch (randomImg.type) {
                case "1":
                    randomImg.radius = DensityUtils.dp2px(getActivity(), 21) / 2;
                    break;
                case "2":
                    randomImg.radius = DensityUtils.dp2px(getActivity(), 33) / 2;
                    break;
                default:
                    randomImg.radius = DensityUtils.dp2px(getActivity(), 51) / 2;
                    break;
            }
            randomImgs.add(randomImg);
        }
        for (int i = 0; i < randomImgs.size(); i++) {
            RandomImg randomImg = randomImgs.get(i);

            whi:
            for (int k = 0; k < 200; k++) {
                int x = random.nextInt(MainApplication.getContext().getScreenWidth());
                int y = random.nextInt(bottom - top) + top;
                if (MainApplication.getContext().getScreenWidth() - x < randomImg.radius || x < randomImg.radius ||
                        bottom - y < randomImg.radius || y - top < randomImg.radius) {
                    continue;
                }
                for (int j = 0; j < absoluteLayout.getChildCount(); j++) {
                    ImageView img1 = (ImageView) absoluteLayout.getChildAt(j);
                    RandomImg randomImg1 = (RandomImg) img1.getTag();
                    if (randomImg1 == null) {
                        continue;
                    }
                    if (Math.sqrt((randomImg1.x - x) * (randomImg1.x - x) + (randomImg1.y - y) * (randomImg1.y - y)) < randomImg1.radius + randomImg.radius) {
                        continue whi;
                    }
                }
                randomImg.x = x;
                randomImg.y = y;
                break;
            }
            if (randomImg.x == 0 && randomImg.y == 0) {
                continue;
            }
            ImageView img = new ImageView(getActivity());
            ImageLoader.getInstance().displayImage(randomImgs.get(i).url, img, options);
            img.setLayoutParams(new AbsoluteLayout.LayoutParams(randomImg.radius * 2, randomImg.radius * 2,
                    randomImg.x - randomImg.radius, randomImg.y - top - randomImg.radius));
            img.setTag(randomImg);
            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RandomImg randomImg1 = (RandomImg) v.getTag();
                    Intent intent = new Intent(getActivity(), BrandDetailActivity.class);
                    intent.putExtra("id", randomImg1.id);
                    startActivity(intent);
                }
            });
            absoluteLayout.addView(img);
        }
    }

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
            DataPaser.getProductList(null, null, null, productPage + "", 8 + "", null, null, null, null, handler);
        }
    }
}
