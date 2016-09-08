package com.taihuoniao.fineix.product.fragment;

import android.content.Intent;
import android.os.Bundle;
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
import com.taihuoniao.fineix.adapters.GoodListFragmentAdapter;
import com.taihuoniao.fineix.beans.ProductBean;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.product.BuyGoodsDetailsActivity;
import com.taihuoniao.fineix.product.GoodsListActivity;
import com.taihuoniao.fineix.qingjingOrSceneDetails.fragment.SearchFragment;
import com.taihuoniao.fineix.utils.DensityUtils;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.view.WaittingDialog;
import com.taihuoniao.fineix.view.pulltorefresh.PullToRefreshBase;
import com.taihuoniao.fineix.view.pulltorefresh.PullToRefreshGridView;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by taihuoniao on 2016/5/4.
 */
public class GoodListFragment extends SearchFragment implements AdapterView.OnItemClickListener {
    @Bind(R.id.pull_refresh_view)
    PullToRefreshGridView pullRefreshView;
    private GridView gridView;
    @Bind(R.id.empty_view)
    TextView empthView;
    @Bind(R.id.progress_bar)
    ProgressBar progressBar;
    private String id;//父分类id
    private String tag_id;//子分类id
    private WaittingDialog dialog;
    private int page = 1;//产品列表页码
    private List<ProductBean.ProductListItem> list;
    private GoodListFragmentAdapter goodListFragmentAdapter;

    public static GoodListFragment newInstance(String id, String tag_id) {
        Bundle args = new Bundle();
        args.putString("tag_id", tag_id);
        args.putString("id", id);
        GoodListFragment fragment = new GoodListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        id = getArguments().getString("id");
        tag_id = getArguments().getString("tag_id");
        dialog = ((GoodsListActivity) getActivity()).dialog;
    }

    @Override
    protected View initView() {
        View view = View.inflate(getActivity(), R.layout.fragment_search_qj, null);
        ButterKnife.bind(this, view);
        gridView = pullRefreshView.getRefreshableView();
        pullRefreshView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                requestNet();
            }
        });
        pullRefreshView.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {
            @Override
            public void onLastItemVisible() {
                page++;
                progressBar.setVisibility(View.VISIBLE);
                productList();
            }
        });
        return view;
    }

    @Override
    protected void initList() {
        gridView.setNumColumns(2);
        gridView.setSelector(R.color.nothing);
        gridView.setHorizontalSpacing(DensityUtils.dp2px(getActivity(), 15));
        gridView.setVerticalSpacing(DensityUtils.dp2px(getActivity(), 15));
        list = new ArrayList<>();
        goodListFragmentAdapter = new GoodListFragmentAdapter(list);
        gridView.setAdapter(goodListFragmentAdapter);
        gridView.setOnItemClickListener(this);
    }

    @Override
    protected void requestNet() {
        if (!dialog.isShowing()) {
            dialog.show();
        }
        productList();
    }

    //根据子分类获取商品列表
    private void productList() {
        ClientDiscoverAPI.getProductList(null, null, id, null, tag_id, page + "", 8 + "", null, null,
                null, null,"9", new RequestCallBack<String>() {

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        dialog.dismiss();
                        pullRefreshView.onRefreshComplete();
                        progressBar.setVisibility(View.GONE);
                        ProductBean productBean = new ProductBean();
                        try {
                            Gson gson = new Gson();
                            Type type = new TypeToken<ProductBean>() {
                            }.getType();
                            productBean = gson.fromJson(responseInfo.result, type);
                        } catch (JsonSyntaxException e) {
                            e.printStackTrace();
                        }
                        if (productBean.isSuccess()) {
                            if (page == 1) {
                                list.clear();
                                pullRefreshView.lastTotalItem = -1;
                                pullRefreshView.lastSavedFirstVisibleItem = -1;
                            }
                            list.addAll(productBean.getData().getRows());
                            goodListFragmentAdapter.notifyDataSetChanged();
                            if (list.size() <= 0) {
                                empthView.setText("暂无产品");
                                empthView.setVisibility(View.VISIBLE);
                            } else {
                                empthView.setVisibility(View.GONE);
                            }
                            return;
                        }
                        ToastUtils.showError(productBean.getMessage());
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        dialog.dismiss();
                        pullRefreshView.onRefreshComplete();
                        progressBar.setVisibility(View.GONE);
                        ToastUtils.showError(R.string.net_fail);
                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ProductBean.ProductListItem productListItem = (ProductBean.ProductListItem) gridView.getAdapter().getItem(position);
        Intent intent = new Intent(getActivity(), BuyGoodsDetailsActivity.class);
        intent.putExtra("id", productListItem.get_id());
        startActivity(intent);
    }
}
