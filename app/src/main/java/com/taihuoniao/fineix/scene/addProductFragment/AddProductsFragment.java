package com.taihuoniao.fineix.scene.addProductFragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.AddProductGridAdapter;
import com.taihuoniao.fineix.base.BaseFragment;
import com.taihuoniao.fineix.beans.HttpResponse;
import com.taihuoniao.fineix.common.GlobalDataCallBack;
import com.taihuoniao.fineix.base.HttpRequest;
import com.taihuoniao.fineix.beans.BuyGoodDetailsBean;
import com.taihuoniao.fineix.beans.CategoryListBean;
import com.taihuoniao.fineix.beans.ProductBean;
import com.taihuoniao.fineix.beans.SearchBean;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.DataConstants;
import com.taihuoniao.fineix.network.URL;
import com.taihuoniao.fineix.scene.AddProductActivity;
import com.taihuoniao.fineix.utils.DensityUtils;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.view.dialog.WaittingDialog;
import com.taihuoniao.fineix.view.pulltorefresh.PullToRefreshBase;
import com.taihuoniao.fineix.view.pulltorefresh.PullToRefreshGridView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;

/**
 * Created by taihuoniao on 2016/4/13.
 */
public class AddProductsFragment extends BaseFragment implements AdapterView.OnItemClickListener {
    private int position;
    private CategoryListBean categoryBean;
    //控件
    private PullToRefreshGridView pullToRefreshView;
    private TextView nothingTv;
    private ProgressBar progressBar;
    //当前页码
    private int currentPage = 1;
    //网络请求返回数据商品列表
    private List<ProductBean.RowsEntity> productList;
    private AddProductGridAdapter addProductGridAdapter;
    private String q = "";//搜索关键字
    private boolean search = false;//判断是不是搜索
    private List<SearchBean.SearchItem> searchList;
    private WaittingDialog dialog;


    public static AddProductsFragment newInstance(int position, CategoryListBean categoryBean) {

        Bundle args = new Bundle();
        args.putInt("position", position);
        args.putSerializable("categoryBean", categoryBean);
        AddProductsFragment fragment = new AddProductsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position = getArguments() != null ? getArguments().getInt("position") : 0;
        categoryBean = getArguments() != null ? (CategoryListBean) getArguments().getSerializable("categoryBean") : null;
        IntentFilter filter = new IntentFilter(DataConstants.BroadSearchFragment);
        getActivity().registerReceiver(searchProductReceiver, filter);
    }

    private void search(String q, String t, String page, String evt, String sort) {
        HashMap<String, String> requestParams = ClientDiscoverAPI.getsearchRequestParams(q, 7 + "", null, page, "8", evt, sort);
        Call httpHandler = HttpRequest.post(requestParams,URL.SEARCH, new GlobalDataCallBack(){
            @Override
            public void onSuccess(String json) {
                dialog.dismiss();
                pullToRefreshView.onRefreshComplete();
                progressBar.setVisibility(View.GONE);
                HttpResponse<SearchBean> searchBean = JsonUtil.json2Bean(json, new TypeToken<HttpResponse<SearchBean>>() { });
                pullToRefreshView.onRefreshComplete();
                progressBar.setVisibility(View.GONE);
                if (searchBean.isSuccess()) {
                    productList.clear();
                    if (currentPage == 1) {
                        pullToRefreshView.lastTotalItem = -1;
                        pullToRefreshView.lastSavedFirstVisibleItem = -1;
                        searchList.clear();
                    }
                    searchList.addAll(searchBean.getData().getRows());
                    if (searchList.size() <= 0) {
                        nothingTv.setVisibility(View.VISIBLE);
                    } else {
                        nothingTv.setVisibility(View.GONE);
                    }
                    addProductGridAdapter.notifyDataSetChanged();
                    pullToRefreshView.setLoadingTime();
                }
            }

            @Override
            public void onFailure(String error) {
                dialog.dismiss();
                pullToRefreshView.onRefreshComplete();
                progressBar.setVisibility(View.GONE);
                ToastUtils.showError("网络错误");
            }
        });
        addNet(httpHandler);
    }

    @Override
    protected void requestNet() {
        if (search) {
            search(q, "10", currentPage + "", null, null);
        } else {
            if (position == 0) {
                HashMap<String, String> requestParams = ClientDiscoverAPI.getgetProductListRequestParams(null, null, null, null, null, currentPage + "", 8 + "", null, null, null, null, "9");
                Call httpHandler = HttpRequest.post(requestParams, URL.URLSTRING_PRODUCTSLIST, new GlobalDataCallBack(){
                    @Override
                    public void onSuccess(String json) {
                        dialog.dismiss();
                        pullToRefreshView.onRefreshComplete();
                        progressBar.setVisibility(View.GONE);
                        getProductList(json);
                    }

                    @Override
                    public void onFailure(String error) {
                        dialog.dismiss();
                        pullToRefreshView.onRefreshComplete();
                        progressBar.setVisibility(View.GONE);
                        ToastUtils.showError("网络错误");
                    }
                });
                addNet(httpHandler);
            } else {
                HashMap<String, String> requestParams = ClientDiscoverAPI.getgetProductListRequestParams(null, null, categoryBean.getRows().get(position).get_id(), null, null, currentPage + "", 8 + "", null, null, null, null, "9");
                Call httpHandler = HttpRequest.post(requestParams,URL.URLSTRING_PRODUCTSLIST,new GlobalDataCallBack(){
                    @Override
                    public void onSuccess(String json) {
                        dialog.dismiss();
                        pullToRefreshView.onRefreshComplete();
                        progressBar.setVisibility(View.GONE);
                        getProductList(json);
                    }

                    @Override
                    public void onFailure(String error) {
                        dialog.dismiss();
                        pullToRefreshView.onRefreshComplete();
                        progressBar.setVisibility(View.GONE);
                        ToastUtils.showError("网络错误");
                    }
                });
                addNet(httpHandler);
            }
        }
    }

    private void getProductList(String result) {
        HttpResponse<ProductBean> productBean = JsonUtil.json2Bean(result, new TypeToken<HttpResponse<ProductBean>>() {});
        if (productBean.isSuccess()) {
            searchList.clear();
            if (currentPage == 1) {
                productList.clear();
                pullToRefreshView.lastTotalItem = -1;
                pullToRefreshView.lastSavedFirstVisibleItem = -1;
            }
            productList.addAll(productBean.getData().getRows());
            if (productList.size() <= 0) {
                nothingTv.setVisibility(View.VISIBLE);
            } else {
                nothingTv.setVisibility(View.GONE);
            }
            //刷新数据
            addProductGridAdapter.notifyDataSetChanged();
            pullToRefreshView.setLoadingTime();
        }
    }


    @Override
    protected void initList() {
        GridView grid = pullToRefreshView.getRefreshableView();
        grid.setVerticalScrollBarEnabled(false);
        grid.setNumColumns(2);
        grid.setHorizontalSpacing(DensityUtils.dp2px(getActivity(), 15));
        grid.setVerticalSpacing(DensityUtils.dp2px(getActivity(), 15));
        pullToRefreshView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener() {
            @Override
            public void onRefresh() {
                currentPage = 1;
                if (!dialog.isShowing()) {
                    dialog.show();
                }
                requestNet();
            }
        });
        pullToRefreshView.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {
            @Override
            public void onLastItemVisible() {
                currentPage++;
                progressBar.setVisibility(View.VISIBLE);
                requestNet();
            }
        });
        productList = new ArrayList<>();
        searchList = new ArrayList<>();
        addProductGridAdapter = new AddProductGridAdapter(getActivity(), productList, searchList);
        grid.setAdapter(addProductGridAdapter);
        grid.setOnItemClickListener(this);
        if (!dialog.isShowing()) {
            dialog.show();
        }
    }

    @Override
    protected View initView() {
        View view = View.inflate(getActivity(), R.layout.view_addproduct_fragment, null);
        pullToRefreshView = (PullToRefreshGridView) view.findViewById(R.id.view_addproduct_fragment_refresh);
        nothingTv = (TextView) view.findViewById(R.id.view_addproduct_fragment_nothingtv);
        progressBar = (ProgressBar) view.findViewById(R.id.view_addproduct_fragment_progress);
        dialog = ((AddProductActivity) getActivity()).dialog;
        return view;
    }


    private BroadcastReceiver searchProductReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //搜索接口
            int pos = intent.getIntExtra("pos", -1);
            q = intent.getStringExtra("q");
//            Log.e("<<<", "传递过来的数据,pos=" + pos + ",q=" + q + ",SEARCH=" + SEARCH);
            if (q != null && intent.getBooleanExtra("SEARCH", false) && pos == position) {
                search = intent.getBooleanExtra("SEARCH", false);
                currentPage = 1;
                if (!dialog.isShowing()) {
                    dialog.show();
                }
                search(q, "10", currentPage + "", null, null);
            } else if (search && !intent.getBooleanExtra("SEARCH", true)) {
                search = false;
                currentPage = 1;
                if (!dialog.isShowing()) {
                    dialog.show();
                }
                requestNet();
            }

        }
    };

    @Override
    public void onDestroy() {
        //        cancelNet();
        getActivity().unregisterReceiver(searchProductReceiver);
//        if (handler != null) {
//            handler.removeCallbacksAndMessages(null);
//        }
        super.onDestroy();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
        if (!dialog.isShowing()) {
            dialog.show();
        }
        String ids = null;
        if (productList.size() > 0) {
            ids = productList.get(position).get_id();
        } else if (searchList.size() > 0) {
            ids = searchList.get(position).get_id();
        }
        HashMap<String, String> requestParams = ClientDiscoverAPI.getgoodsDetailsRequestParams(ids);
        Call httpHandler = HttpRequest.post(requestParams,URL.GOOD_DETAILS, new GlobalDataCallBack(){
            @Override
            public void onSuccess(String json) {
                dialog.dismiss();
                HttpResponse<BuyGoodDetailsBean> netGood = JsonUtil.json2Bean(json, new TypeToken<HttpResponse<BuyGoodDetailsBean>>() { });
                if (netGood.isSuccess()) {
                    Intent intent = new Intent();
                    intent.putExtra("product", netGood);
                    getActivity().setResult(DataConstants.RESULTCODE_EDIT_ADDPRODUCT, intent);
                    getActivity().finish();
                } else {
                    ToastUtils.showError(netGood.getMessage());
                }
            }

            @Override
            public void onFailure(String error) {
                dialog.dismiss();
                ToastUtils.showError("网络错误");
            }
        });
        addNet(httpHandler);

    }


}
