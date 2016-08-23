package com.taihuoniao.fineix.scene.addProductFragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.AddProductGridAdapter;
import com.taihuoniao.fineix.base.BaseFragment;
import com.taihuoniao.fineix.beans.CategoryListBean;
import com.taihuoniao.fineix.beans.GoodsDetailBean;
import com.taihuoniao.fineix.beans.ProductBean;
import com.taihuoniao.fineix.beans.SearchBean;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.DataConstants;
import com.taihuoniao.fineix.utils.DensityUtils;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.view.WaittingDialog;
import com.taihuoniao.fineix.view.pulltorefresh.PullToRefreshBase;
import com.taihuoniao.fineix.view.pulltorefresh.PullToRefreshGridView;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

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
    private List<ProductBean.ProductListItem> productList;
    private AddProductGridAdapter addProductGridAdapter;
    private String q = "";//搜索关键字
    private boolean search = false;//判断是不是搜索
    private List<SearchBean.Data.SearchItem> searchList;
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
        ClientDiscoverAPI.search(q, t, null,page, evt, sort, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                dialog.dismiss();
                SearchBean searchBean = new SearchBean();
                try {
                    Gson gson = new Gson();
                    Type type = new TypeToken<SearchBean>() {
                    }.getType();
                    searchBean = gson.fromJson(responseInfo.result, type);
                } catch (JsonSyntaxException e) {
                    Log.e("<<<", "数据解析异常" + e.toString());
                }
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
            public void onFailure(HttpException error, String msg) {
                dialog.dismiss();
                ToastUtils.showError("网络错误");
            }
        });
    }

    @Override
    protected void requestNet() {
        if (search) {
            search(q, "10", currentPage + "", null, null);
        } else {
            if (position == 0) {
                ClientDiscoverAPI.getProductList(null,null,null, null, null, currentPage + "", 8 + "", null, null, null, null, new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        dialog.dismiss();
                        getProductList(responseInfo.result);
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        dialog.dismiss();
                        ToastUtils.showError("网络错误");
                    }
                });
            } else {
                ClientDiscoverAPI.getProductList(null,null,categoryBean.getData().getRows().get(position).get_id(), null, null, currentPage + "", 8 + "", null, null, null, null, new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        dialog.dismiss();
                        getProductList(responseInfo.result);
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        dialog.dismiss();
                        ToastUtils.showError("网络错误");
                    }
                });
            }
        }
    }

    private void getProductList(String result) {
        ProductBean productBean = new ProductBean();
        try {
            Gson gson = new Gson();
            Type type = new TypeToken<ProductBean>() {
            }.getType();
            productBean = gson.fromJson(result, type);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
        pullToRefreshView.onRefreshComplete();
        progressBar.setVisibility(View.GONE);
        ProductBean netProductBean = productBean;
        if (netProductBean.isSuccess()) {
            searchList.clear();
            if (currentPage == 1) {
                productList.clear();
                pullToRefreshView.lastTotalItem = -1;
                pullToRefreshView.lastSavedFirstVisibleItem = -1;
            }
            productList.addAll(netProductBean.getData().getRows());
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
        int dp10 = DensityUtils.dp2px(getActivity(), 10);
        int dp12 = DensityUtils.dp2px(getActivity(), 12);
        grid.setPadding(dp10, dp12, dp10, dp12);
        grid.setHorizontalSpacing(dp10);
        grid.setVerticalSpacing(dp10);
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
                requestNet();
            }
        });
        productList = new ArrayList<>();
        searchList = new ArrayList<>();
        addProductGridAdapter = new AddProductGridAdapter(getActivity(), productList, searchList);
        grid.setAdapter(addProductGridAdapter);
        grid.setOnItemClickListener(this);
        dialog.show();
    }

    @Override
    protected View initView() {
        View view = View.inflate(getActivity(), R.layout.view_addproduct_fragment, null);
        pullToRefreshView = (PullToRefreshGridView) view.findViewById(R.id.view_addproduct_fragment_refresh);
        nothingTv = (TextView) view.findViewById(R.id.view_addproduct_fragment_nothingtv);
        progressBar = (ProgressBar) view.findViewById(R.id.view_addproduct_fragment_progress);
        dialog = new WaittingDialog(getActivity());
        return view;
    }


    private BroadcastReceiver searchProductReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //搜索接口
            int pos = intent.getIntExtra("pos", -1);
            q = intent.getStringExtra("q");
//            Log.e("<<<", "传递过来的数据,pos=" + pos + ",q=" + q + ",search=" + search);
            if (q != null && intent.getBooleanExtra("search", false) && pos == position) {
                search = intent.getBooleanExtra("search", false);
                currentPage = 1;
                if (!dialog.isShowing()) {
                    dialog.show();
                }
                search(q,"10",currentPage+"",null,null);
            } else if (search && !intent.getBooleanExtra("search", true)) {
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
        ClientDiscoverAPI.goodsDetails(ids, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                dialog.dismiss();
//                Log.e("<<<商品详情", responseInfo.result);
                GoodsDetailBean netGood = new GoodsDetailBean();
                try {
                    Gson gson = new Gson();
                    Type type = new TypeToken<GoodsDetailBean>() {
                    }.getType();
                    netGood = gson.fromJson(responseInfo.result, type);
                    Intent intent = new Intent();
                    intent.putExtra("product", netGood);
                    getActivity().setResult(DataConstants.RESULTCODE_EDIT_ADDPRODUCT, intent);
                    getActivity().finish();
                } catch (JsonSyntaxException e) {
                    Log.e("<<<>>>", "数据异常" + e.toString());
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                dialog.dismiss();
                ToastUtils.showError("网络错误");
            }
        });

    }


}
