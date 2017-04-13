package com.taihuoniao.fineix.main.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.google.gson.reflect.TypeToken;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.AddProductGridAdapter;
import com.taihuoniao.fineix.adapters.AddProductGridAdapter2;
import com.taihuoniao.fineix.adapters.EditRecyclerAdapter;
import com.taihuoniao.fineix.adapters.IndexQJListAdapter;
import com.taihuoniao.fineix.adapters.IndexSubjectAdapter;
import com.taihuoniao.fineix.adapters.ViewPagerAdapter;
import com.taihuoniao.fineix.adapters.WellgoodsSubjectAdapter2;
import com.taihuoniao.fineix.base.BaseFragment;
import com.taihuoniao.fineix.base.HttpRequest;
import com.taihuoniao.fineix.beans.CategoryListBean;
import com.taihuoniao.fineix.beans.FirstProductBean;
import com.taihuoniao.fineix.beans.HttpResponse;
import com.taihuoniao.fineix.beans.InterestUserData;
import com.taihuoniao.fineix.beans.LoginInfo;
import com.taihuoniao.fineix.beans.ProductBean;
import com.taihuoniao.fineix.beans.SearchBean;
import com.taihuoniao.fineix.beans.SubjectListBean;
import com.taihuoniao.fineix.beans.User;
import com.taihuoniao.fineix.common.GlobalCallBack;
import com.taihuoniao.fineix.common.GlobalDataCallBack;
import com.taihuoniao.fineix.common.bean.BannerBean;
import com.taihuoniao.fineix.home.GoToNextUtils;
import com.taihuoniao.fineix.home.MoreWellGoodsActivity;
import com.taihuoniao.fineix.home.adapters.IndexAdapter001;
import com.taihuoniao.fineix.home.adapters.IndexAdapter005;
import com.taihuoniao.fineix.home.adapters.ProductAlbumAdapter;
import com.taihuoniao.fineix.interfaces.IRecycleViewItemClickListener;
import com.taihuoniao.fineix.main.App;
import com.taihuoniao.fineix.main.MainApplication;
import com.taihuoniao.fineix.main.ShopMarginDecoration;
import com.taihuoniao.fineix.main.tab3.Product3Bean;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.DataConstants;
import com.taihuoniao.fineix.network.NetWorkUtils;
import com.taihuoniao.fineix.network.URL;
import com.taihuoniao.fineix.product.BuyGoodsDetailsActivity;
import com.taihuoniao.fineix.qingjingOrSceneDetails.CommentListActivity;
import com.taihuoniao.fineix.qingjingOrSceneDetails.SearchActivity;
import com.taihuoniao.fineix.qingjingOrSceneDetails.bean.SceneListBean2;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.StringFormatUtils;
import com.taihuoniao.fineix.utils.StringUtils;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.utils.TypeConversionUtils;
import com.taihuoniao.fineix.view.GridViewForScrollView;
import com.taihuoniao.fineix.view.ListViewForScrollView;
import com.taihuoniao.fineix.view.ScrollableView;
import com.taihuoniao.fineix.view.dialog.WaittingDialog;
import com.taihuoniao.fineix.view.pulltorefresh.PullToRefreshBase;
import com.taihuoniao.fineix.view.pulltorefresh.PullToRefreshListView;
import com.taihuoniao.fineix.zxing.activity.CaptureActivityZxing;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionNo;
import com.yanzhenjie.permission.PermissionYes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.Call;

import static com.taihuoniao.fineix.beans.LoginInfo.getLoginInfo;
import static com.taihuoniao.fineix.utils.Constants.REQUEST_CODE_SETTING;
import static com.taihuoniao.fineix.utils.Constants.REQUEST_PHONE_STATE_CODE;

/**
 * 首页精选
 * Created by taihuoniao on 2016/8/9.
 */
public class IndexFragment extends BaseFragment<BannerBean> implements View.OnClickListener, PullToRefreshBase.OnRefreshListener, AbsListView.OnScrollListener, EditRecyclerAdapter.ItemClick {

    @Bind(R.id.pullToRefreshListView_home)PullToRefreshListView pullRefreshView;
    @Bind(R.id.progress_bar)ProgressBar progressBar;
    @Bind(R.id.title_layout)RelativeLayout titleRelative;
    @Bind(R.id.search_img)ImageView searchImg;
    @Bind(R.id.subs_img)ImageView subsImg;

    private boolean isScan;
    private ScrollableView scrollableView; //顶部轮播图
    private WaittingDialog dialog;//网络请求对话框

    private ViewPagerAdapter viewPagerAdapter;//banner图适配器
    private IndexQJListAdapter indexQJListAdapter;//情景列表适配器


    private List<SceneListBean2.RowsEntity> sceneList;//情景列表数据
    private ArrayList<User> userList;//插入情景列表的用户列表数据
    private int currentPage = 1;//网络请求页码

    private List<SubjectListBean.RowsEntity> subjectList003;//主题列表数据

    private IndexAdapter001 indexAdapter001;//新手
    private AddProductGridAdapter indexAdapter002;//主题列表适配器
    private List<ProductBean.RowsEntity> productList;
    private List<SearchBean.SearchItem> searchList;

    private ProductAlbumAdapter indexAdapter003;//主题列表适配器
    private IndexAdapter005 indexAdapter005;//D3IN

    // 好货人气王
    private List<FirstProductBean.ItemsEntity> secondProductList6;
    private AddProductGridAdapter2 indexAdapter006;//主题列表适配器
    private LinearLayout linearLayout001;
    private RecyclerView recyclerView001;

    private WellgoodsSubjectAdapter2 indexSubjectAdapter;
    private List<SubjectListBean.RowsEntity> rowsEntities;

    private boolean isLogin;

    @Override
    protected View initView() {
        isLogin = LoginInfo.isUserLogin();
        View fragmentView = View.inflate(getActivity(), R.layout.fragment_index, null);
        dialog = new WaittingDialog(getActivity());
        IntentFilter intentFilter = new IntentFilter(DataConstants.BroadIndex);
        getActivity().registerReceiver(indexReceiver, intentFilter);
        return fragmentView;
    }

    @Override
    protected void initList() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            titleRelative.setPadding(0, App.getStatusBarHeight(), 0, 0);
        }
        ListView listView = pullRefreshView.getRefreshableView();
        listView.addHeaderView(getHeaderView());
        listView.addFooterView(getFooterView());

        pullRefreshView.animLayout();
        listView.setDividerHeight(0);

        searchImg.setOnClickListener(this);
        subsImg.setOnClickListener(this);

        pullRefreshView.setOnRefreshListener(this);
        listView.setOnScrollListener(this);

        ViewGroup.LayoutParams lp = scrollableView.getLayoutParams();
        lp.width = MainApplication.getContext().getScreenWidth();
        lp.height = lp.width * 422 / 750;
        scrollableView.setLayoutParams(lp);

        sceneList = new ArrayList<>();
        userList = new ArrayList<>();
        indexQJListAdapter = new IndexQJListAdapter(getActivity(), sceneList, userList);
        listView.setAdapter(indexQJListAdapter);
    }

    @Override
    public void onRefresh() {
        userList.clear();
        currentPage = 1;
        sneceComplete = 0;
        if (!dialog.isShowing()) {
            dialog.show();
        }
        requestNet();
    }

    @Override
    protected void requestNet() {
        if (AndPermission.hasPermission(activity, android.Manifest.permission.READ_PHONE_STATE, android.Manifest.permission.CAMERA)) {
            requestData();
        } else {
            // 申请权限。
            AndPermission.with(this)
                    .requestCode(REQUEST_PHONE_STATE_CODE)
                    .permission(android.Manifest.permission.READ_PHONE_STATE, android.Manifest.permission.CAMERA)
                    .send();

        }
    }

    private void requestData() {
        if (!dialog.isShowing()) {
            dialog.show();
        }
        Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
        android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
        NetWorkUtils netWorkUtils = new NetWorkUtils(activity);
        netWorkUtils.checkVersionInfo();
        sceneNet();
        subjectList();
        getBanners();

        linearLayout001.setVisibility(isHiddenRedPacketWelfareArea() ? View.GONE : View.VISIBLE);
        recyclerView001.setVisibility(isHiddenRedPacketWelfareArea() ? View.GONE : View.VISIBLE);
        if (!isHiddenRedPacketWelfareArea()) {
            getBanners2();
        }
        secondProduct();
        getLasteProduct();
        subjectList3();
//        getBanners4();
        getBanners5();
//        if (indexQJListAdapter.isNoUser()) {
//            userList.clear();
//            sneceComplete = 2;
//            return;
//        }
//        getUserList();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        AndPermission.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    @PermissionYes(REQUEST_PHONE_STATE_CODE)
    private void getPhoneStatusYes(List<String> grantedPermissions) {
        if (grantedPermissions.contains(Manifest.permission.READ_PHONE_STATE)) {
            requestData();
        }
        if (grantedPermissions.contains(Manifest.permission.CAMERA)&&isScan) {
            startActivity(new Intent(getActivity(), CaptureActivityZxing.class));
        }

    }

    @PermissionNo(REQUEST_PHONE_STATE_CODE)
    private void getPhoneStatusNo(List<String> deniedPermissions) {
        // 用户否勾选了不再提示并且拒绝了权限，那么提示用户到设置中授权。
        if (AndPermission.hasAlwaysDeniedPermission(this, deniedPermissions)) {
            // 第一种：用默认的提示语。
            AndPermission.defaultSettingDialog(this, REQUEST_CODE_SETTING).show();
        } else {
            activity.finish();
        }
    }


    @OnClick({R.id.ll_select_city})
    void performClick(View v) {
        switch (v.getId()) {
            case R.id.ll_select_city:

                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search_img:
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                intent.putExtra("t", 7);
                startActivity(intent);
                break;
            case R.id.subs_img:
                // 扫码
                isScan =true;
                if (AndPermission.hasPermission(activity, android.Manifest.permission.CAMERA)) {
                    startActivity(new Intent(getActivity(), CaptureActivityZxing.class));
                } else {
                    AndPermission.with(this).requestCode(REQUEST_PHONE_STATE_CODE).permission(android.Manifest.permission.CAMERA).send();
                }
                break;
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
        if (scrollableView != null) {
            scrollableView.start();
        }
    }


    protected void refreshUI(List<BannerBean.RowsBean> list) {
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

    private int sneceComplete;//判断情景是否加载完毕 0，情景用户都没加载 1,情景加载完毕等待用户加载 2，用户加载完毕等待情景加载

    //获取中间插入的用户列表
    private void getUserList() {
        HashMap<String, String> re = ClientDiscoverAPI.getUserListRequestParams(5);
        Call httpHandler = HttpRequest.post(re, URL.USER_FIND_USER, new GlobalDataCallBack() {
            @Override
            public void onSuccess(String json) {
                HttpResponse<InterestUserData> indexUserListBean = JsonUtil.json2Bean(json, new TypeToken<HttpResponse<InterestUserData>>() {});
                if (indexUserListBean.isSuccess()) {
                    userList.clear();
                    userList.addAll(indexUserListBean.getData().users);
                    if (sneceComplete == 1) {
                        new android.os.Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                indexQJListAdapter.notifyDataSetChanged();
                            }
                        }, 200);
                        sneceComplete = 2;
                    }
                    return;
                }
                if (sneceComplete == 1) {
                    new android.os.Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            indexQJListAdapter.notifyDataSetChanged();
                        }
                    }, 200);
                    sneceComplete = 0;
                }
            }

            @Override
            public void onFailure(String error) {
                if (sneceComplete == 1) {
                    indexQJListAdapter.notifyDataSetChanged();
                    sneceComplete = 0;
                }
            }
        });
        addNet(httpHandler);
    }

    //获取精选主题
    private void subjectList() {
        HashMap<String, String> requestParams = ClientDiscoverAPI.getIndexChosenSubjectRequestParams();
        Call httpHandler = HttpRequest.post(requestParams, URL.SCENE_SUBJECT_INDEX_SUJECT_STICK, new GlobalDataCallBack() {
            @Override
            public void onSuccess(String json) {
                HttpResponse<SubjectListBean> subjectListBean = JsonUtil.json2Bean(json, new TypeToken<HttpResponse<SubjectListBean>>() {});
                if (subjectListBean.isSuccess()) {
                    if (rowsEntities != null) {
                        rowsEntities.clear();
                        rowsEntities.addAll(subjectListBean.getData().getRows());
                        indexSubjectAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(String error) {
            }
        });
        addNet(httpHandler);
    }

    //获取情景列表
    private void sceneNet() {
        dialog.show();
        HashMap<String, String> sceneListRequestParams = ClientDiscoverAPI.getSceneListRequestParams(currentPage + "", 20 + "", null, null, 2 + "", null, null, null);
        sceneListRequestParams.put("is_product", "1");
        HttpRequest.post(sceneListRequestParams,URL.SCENE_LIST, new GlobalDataCallBack() {
            @Override
            public void onSuccess(String json) {
                HttpResponse<SceneListBean2> sceneL = JsonUtil.json2Bean(json, new TypeToken<HttpResponse<SceneListBean2>>() {});
                pullRefreshView.onRefreshComplete();
                dialog.dismiss();
                progressBar.setVisibility(View.GONE);
                if (sceneL.isSuccess()) {
                    if (currentPage == 1) {
                        sceneList.clear();
                        pullRefreshView.lastTotalItem = -1;
                        pullRefreshView.lastSavedFirstVisibleItem = -1;
                    }
                    sceneList.addAll(sceneL.getData().getRows());
                    if (userList.size() <= 0 && sneceComplete == 0) {
                        sneceComplete = 1;
                        return;
                    }
                    new android.os.Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            indexQJListAdapter.notifyDataSetChanged();
                        }
                    }, 200);
                }
            }

            @Override
            public void onFailure(String error) {
                dialog.dismiss();
                progressBar.setVisibility(View.GONE);
                pullRefreshView.onRefreshComplete();
                ToastUtils.showError("网络错误");
            }
        });
    }


    @Override
    public void onDestroyView() {
        getActivity().unregisterReceiver(indexReceiver);
        super.onDestroyView();
    }

    private BroadcastReceiver indexReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            onRefresh();
        }
    };


    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }

    @Override
    public void click(int postion) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {
            return;
        }
        switch (resultCode) {
            case Activity.RESULT_OK:
                if (indexQJListAdapter == null) {
                    return;
                }
                int count = data.getIntExtra(CommentListActivity.class.getSimpleName(), -1);
                if (count == -1) {
                    return;
                }
                sceneList.get(indexQJListAdapter.getPos()).setComment_count(TypeConversionUtils.IntConvertString(count));
                indexQJListAdapter.notifyDataSetChanged();
                break;
        }
    }

    /**
     * headView 轮播图 + 精选主题
     *
     * @return headView
     */
    private View getHeaderView() {
        View headerView = View.inflate(getActivity(), R.layout.header_index, null);
        scrollableView = (ScrollableView) headerView.findViewById(R.id.scrollableView);
        linearLayout001 = (LinearLayout) headerView.findViewById(R.id.linearLayout_index_001);
        recyclerView001 = (RecyclerView) headerView.findViewById(R.id.recyclerView_index_001);
        recyclerView001.setHasFixedSize(true);
        recyclerView001.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView001.addItemDecoration(new ShopMarginDecoration(activity, R.dimen.dp5));
        indexAdapter001 = new IndexAdapter001(getActivity(), new GlobalCallBack() {
            @Override
            public void callBack(Object object) {
                if (object instanceof BannerBean.RowsBean) {
                    BannerBean.RowsBean rowsEntity = (BannerBean.RowsBean) object;
                    GoToNextUtils.goToIntent(getActivity(), Integer.valueOf(rowsEntity.type), rowsEntity.web_url);
                }
            }
        });
        recyclerView001.setAdapter(indexAdapter001);

        GridViewForScrollView   recyclerView002 = (GridViewForScrollView ) headerView.findViewById(R.id.recyclerView_index_002);
        productList = new ArrayList<>();
        searchList = new ArrayList<>();
        indexAdapter002 = new AddProductGridAdapter(getActivity(), productList, searchList);
        recyclerView002.setAdapter(indexAdapter002);
        recyclerView002.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(parent.getContext(), BuyGoodsDetailsActivity.class);
                intent.putExtra("id", productList.get(position).get_id());
                parent.getContext().startActivity(intent);
            }
        });

        // 好货人气王
        GridViewForScrollView recyclerView006 = (GridViewForScrollView ) headerView.findViewById(R.id.recyclerView_index_006);
        secondProductList6 = new ArrayList<>();
        indexAdapter006 = new AddProductGridAdapter2(secondProductList6);
        recyclerView006.setAdapter(indexAdapter002);
        recyclerView006.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), BuyGoodsDetailsActivity.class);
                intent.putExtra("id", secondProductList6.get(position).get_id());
                getActivity().startActivity(intent);
            }
        });

        RecyclerView recyclerView003 = (RecyclerView) headerView.findViewById(R.id.recyclerView_index_003);
        recyclerView003.setHasFixedSize(true);
        recyclerView003.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        subjectList003 = new ArrayList<>();
        indexAdapter003 = new ProductAlbumAdapter(getActivity(), subjectList003);
        recyclerView003.setAdapter(indexAdapter003);

        //地盘
        RecyclerView recyclerView005 = (RecyclerView) headerView.findViewById(R.id.recyclerView_index_005);
        recyclerView005.setHasFixedSize(true);
        recyclerView005.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView005.addItemDecoration(new ShopMarginDecoration(activity, R.dimen.dp5));
        indexAdapter005 = new IndexAdapter005(activity, null);
        indexAdapter005.setOnItemClickListener(new IRecycleViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                BannerBean.RowsBean item = indexAdapter005.getItem(position);
                if (item == null) return;
                GoToNextUtils.goToIntent(activity, Integer.valueOf(item.type), item.web_url);
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
        recyclerView005.setAdapter(indexAdapter005);

        // 好货合集 查看更多
        headerView.findViewById(R.id.textView_indexFragment_headerIndex_more).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), MoreWellGoodsActivity.class));
            }
        });
        return headerView;
    }

    /**
     * 轮播图
     */
    private void getBanners() {
        HashMap<String, String> requestParams = ClientDiscoverAPI.getgetBannersRequestParams("app_fiu_sight_index_slide");
        Call httpHandler = HttpRequest.post(requestParams, URL.BANNERS_URL, new GlobalDataCallBack() {

            @Override
            public void onSuccess(String json) {
                BannerBean bannerData = JsonUtil.fromJson(json, new TypeToken<HttpResponse<BannerBean>>() {
                });
                if (bannerData != null) {
                    List<BannerBean.RowsBean> rows = bannerData.rows;
                    refreshUI(rows);
                }
            }

            @Override
            public void onFailure(String error) {
            }
        });
        addNet(httpHandler);
    }

    /**
     * 新手指南
     */
    private void getBanners2() {
        HashMap<String, String> requestParams = ClientDiscoverAPI.getgetBannersRequestParams("app_fiu_index_new_zone");
        Call httpHandler = HttpRequest.post(requestParams, URL.BANNERS_URL, new GlobalDataCallBack() {
            @Override
            public void onSuccess(String json) {
                BannerBean bannerData = JsonUtil.fromJson(json, new TypeToken<HttpResponse<BannerBean>>() {
                });
                if (bannerData != null) {
                    List<BannerBean.RowsBean> rows = bannerData.rows;
                    indexAdapter001.setRowsEntities(rows);
                }
            }

            @Override
            public void onFailure(String error) {
            }
        });
        addNet(httpHandler);
    }

    /**
     * 新品
     */
    private void getLasteProduct(){
        HashMap<String, String> requestParams = ClientDiscoverAPI.getgetProductListRequestParams(null, "2", null, null, null, null, String.valueOf(4), null, null, null, "1", null);
        HttpRequest.post(requestParams, URL.URLSTRING_PRODUCTSLIST, new GlobalDataCallBack() {
            @Override
            public void onSuccess(String json) {
                HttpResponse<ProductBean> productBean = JsonUtil.json2Bean(json, new TypeToken<HttpResponse<ProductBean>>() {});
                if (productBean.isSuccess()) {
                    searchList.clear();
                    if (currentPage == 1) {
                        productList.clear();
                    }
                    productList.addAll(productBean.getData().getRows());
                    //刷新数据
                    indexAdapter002.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(String error) {
            }
        });
    }

    //好货专题列表
    private void subjectList3() {
        HashMap<String, String> requestParams = ClientDiscoverAPI.getsubjectListRequestParams(currentPage + "", 1 + "", null, String.valueOf(1), 5 + "", "2");
        HttpRequest.post(requestParams, URL.SCENE_SUBJECT_GETLIST, new GlobalDataCallBack() {

            @Override
            public void onSuccess(String json) {
                HttpResponse<SubjectListBean> subjectListBean = JsonUtil.json2Bean(json, new TypeToken<HttpResponse<SubjectListBean>>() {});
                if (subjectListBean.isSuccess()) {
                    subjectList003.clear();
                    subjectList003.addAll(subjectListBean.getData().getRows());
                    indexAdapter003.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(String error) {
            }
        });
    }

    /**
     * 地盘分类
     */
    private void getBanners4() {
        HashMap<String, String> requestParams = ClientDiscoverAPI.getcategoryListRequestParams("1", "12", null);
        requestParams.put("show_sub", "1");
        HttpRequest.post(requestParams, URL.CATEGORY_LIST, new GlobalDataCallBack() {

            @Override
            public void onSuccess(String json) {
                HttpResponse<CategoryListBean> categoryListBean = JsonUtil.json2Bean(json, new TypeToken<HttpResponse<CategoryListBean>>() {});
                if (categoryListBean != null) {
//                    indexAdapter004.setRowsEntities(categoryListBean.getRows());
                }
            }

            @Override
            public void onFailure(String error) {

            }
        });
    }

    /**
     * 地盘推荐
     */
    private void getBanners5() {
        HashMap<String, String> requestParams = ClientDiscoverAPI.getgetBannersRequestParams("app_fiu_index_scene_stick");
        Call httpHandler = HttpRequest.post(requestParams, URL.BANNERS_URL, new GlobalDataCallBack() {
            @Override
            public void onSuccess(String json) {
                BannerBean bannerData = JsonUtil.fromJson(json, new TypeToken<HttpResponse<BannerBean>>() {
                });
                if (bannerData != null) {
                    List<BannerBean.RowsBean> rows = bannerData.rows;
                    indexAdapter005.setRowsEntities(rows);
                }
            }

            @Override
            public void onFailure(String error) {
            }
        });
        addNet(httpHandler);
    }

    private View getFooterView(){
        View footerView = View.inflate(getActivity(), R.layout.footerview_index_bottom, null);
        ListViewForScrollView recyclerView = (ListViewForScrollView) footerView.findViewById(R.id.recycler_view);
        rowsEntities = new ArrayList<>();
        indexSubjectAdapter = new WellgoodsSubjectAdapter2(getActivity(), rowsEntities);
        recyclerView.setAdapter(indexSubjectAdapter);
        return footerView;
    }

    /**
     * 好货最热推荐接口
     */
    private void secondProduct(){
        HashMap<String, String> requestParams = ClientDiscoverAPI.getfirstProductsRequestParams();
        requestParams.put("size", "8");
        HttpRequest.post(requestParams, URL.PRODUCT_INDEX_HOT, new GlobalDataCallBack(){

            @Override
            public void onSuccess(String json) {
                Product3Bean product3Bean = JsonUtil.fromJson(json,new TypeToken<HttpResponse<Product3Bean>>(){});
                if (product3Bean != null && product3Bean.getItems() != null) {
                    int size = product3Bean.getItems().size();
                    secondProductList6.clear();
                    for(int i = 0; i < size; i++) {
                        Product3Bean.ItemsEntity itemsEntity = product3Bean.getItems().get(i);
                        FirstProductBean.ItemsEntity bean = new FirstProductBean.ItemsEntity();
                        bean.set_id(itemsEntity.get_id());
                        bean.setTitle(itemsEntity.getTitle());
                        bean.setBrand_cover_url(itemsEntity.getBrand_cover_url());
                        bean.setBrand_id(itemsEntity.getBrand_id());
                        bean.setCover_url(itemsEntity.getCover_url());
                        bean.setSale_price(itemsEntity.getSale_price());
                        secondProductList6.add(bean);
                    }
                    indexAdapter006.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(String error) { }
        });
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (LoginInfo.isUserLogin() != isLogin) {
            isLogin = LoginInfo.isUserLogin();
            requestNet();
        }
        super.onHiddenChanged(hidden);
    }

    private boolean isHiddenRedPacketWelfareArea() {
        String created_on = LoginInfo.getCreated_on();
        long createTime = TypeConversionUtils.StringConvertLong(created_on);
        return createTime != 0 && isLogin && LoginInfo.getLoginInfo() != null && createTime < System.currentTimeMillis() - 86400 * 1000;
    }
}
