package com.taihuoniao.fineix.scene.addProductFragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.taihuoniao.fineix.beans.CategoryBean;
import com.taihuoniao.fineix.beans.GoodsDetailBean;
import com.taihuoniao.fineix.beans.ProductBean;
import com.taihuoniao.fineix.beans.SearchBean;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.DataConstants;
import com.taihuoniao.fineix.network.DataPaser;
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
    private CategoryBean categoryBean;
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
    private List<SearchBean.SearchItem> searchList;
    private WaittingDialog dialog;


    public static AddProductsFragment newInstance(int position, CategoryBean categoryBean) {

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
        categoryBean = getArguments() != null ? (CategoryBean) getArguments().getSerializable("categoryBean") : null;
        IntentFilter filter = new IntentFilter(DataConstants.BroadSearchFragment);
        getActivity().registerReceiver(searchProductReceiver, filter);
    }

    @Override
    protected void requestNet() {
//        progressBar.setVisibility(View.VISIBLE);
        if (search) {
            DataPaser.search(q, "10", currentPage + "", null, null, handler);
        } else {
            if (position == 0) {
                DataPaser.getProductList(null, null, null, currentPage + "", 8 + "", null, null, null, null, handler);
            } else {
                DataPaser.getProductList(categoryBean.getList().get(position).get_id(), null, null, currentPage + "", 8 + "", null, null, null, null, handler);
            }
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
//        pullToRefreshView.setEmptyView(nothingTv);
        pullToRefreshView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener() {
            @Override
            public void onRefresh() {
                currentPage = 1;
                dialog.show();
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

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DataConstants.SEARCH_LIST:
                    dialog.dismiss();
                    pullToRefreshView.onRefreshComplete();
                    progressBar.setVisibility(View.GONE);
                    SearchBean netSearch = (SearchBean) msg.obj;
                    if (netSearch.isSuccess()) {
                        productList.clear();
                        if (currentPage == 1) {
                            pullToRefreshView.lastTotalItem = -1;
                            pullToRefreshView.lastSavedFirstVisibleItem = -1;
                            searchList.clear();
                        }
                        searchList.addAll(netSearch.getData().getRows());
                        if (searchList.size() <= 0) {
                            nothingTv.setVisibility(View.VISIBLE);
                        } else {
                            nothingTv.setVisibility(View.GONE);
                        }
                        addProductGridAdapter.notifyDataSetChanged();
                        pullToRefreshView.setLoadingTime();
                    }
                    break;
                case DataConstants.ADD_PRODUCT_LIST:
                    dialog.dismiss();
                    pullToRefreshView.onRefreshComplete();
                    progressBar.setVisibility(View.GONE);
                    ProductBean netProductBean = (ProductBean) msg.obj;
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
                    break;
                case DataConstants.NET_FAIL:
                    dialog.dismiss();
                    pullToRefreshView.onRefreshComplete();
                    progressBar.setVisibility(View.GONE);
                    break;
            }
        }
    };

    private BroadcastReceiver searchProductReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //搜索接口
            int pos = intent.getIntExtra("pos", -1);
            q = intent.getStringExtra("q");
            Log.e("<<<", "传递过来的数据,pos=" + pos + ",q=" + q + ",search=" + search);
            if (q != null && intent.getBooleanExtra("search", false) && pos == position) {
                search = intent.getBooleanExtra("search", false);
                currentPage = 1;
                dialog.show();
                DataPaser.search(q, "10", currentPage + "", null, null, handler);
            } else if (search && !intent.getBooleanExtra("search", true)) {
                search = false;
                currentPage = 1;
                dialog.show();
                requestNet();
            }

        }
    };

    @Override
    public void onDestroy() {
        //        cancelNet();
        getActivity().unregisterReceiver(searchProductReceiver);
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
            handler = null;
        }
        super.onDestroy();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
        dialog.show();
        ClientDiscoverAPI.goodsDetails(productList.get(position).get_id(), new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                dialog.dismiss();
                Log.e("<<<商品详情", responseInfo.result);
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
//                new SVProgressHUD(getActivity()).showErrorWithStatus("网络错误");
            }
        });

    }


}
