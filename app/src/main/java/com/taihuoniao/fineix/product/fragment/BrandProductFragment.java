//package com.taihuoniao.fineix.product.fragment;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.GridView;
//import android.widget.ProgressBar;
//
//import com.google.gson.Gson;
//import com.google.gson.JsonSyntaxException;
//import com.google.gson.reflect.TypeToken;
//import com.lidroid.xutils.exception.HttpException;
//import com.lidroid.xutils.http.ResponseInfo;
//import com.lidroid.xutils.http.callback.RequestCallBack;
//import com.taihuoniao.fineix.R;
//import com.taihuoniao.fineix.adapters.AddProductGridAdapter;
//import com.taihuoniao.fineix.beans.ProductBean;
//import com.taihuoniao.fineix.beans.SearchBean;
//import com.taihuoniao.fineix.network.ClientDiscoverAPI;
//import com.taihuoniao.fineix.product.BrandDetailActivity;
//import com.taihuoniao.fineix.product.BuyGoodsDetailsActivity;
//import com.taihuoniao.fineix.product.GoodsDetailActivity;
//import com.taihuoniao.fineix.qingjingOrSceneDetails.fragment.SearchFragment;
//import com.taihuoniao.fineix.utils.DensityUtils;
//import com.taihuoniao.fineix.utils.ToastUtils;
//import com.taihuoniao.fineix.view.WaittingDialog;
//import com.taihuoniao.fineix.view.pulltorefresh.PullToRefreshBase;
//import com.taihuoniao.fineix.view.pulltorefresh.PullToRefreshGridView;
//
//import java.lang.reflect.Type;
//import java.util.ArrayList;
//import java.util.List;
//
//import butterknife.Bind;
//import butterknife.ButterKnife;
//
///**
// * Created by taihuoniao on 2016/8/27.
// */
//public class BrandProductFragment extends SearchFragment implements AdapterView.OnItemClickListener {
//    @Bind(R.id.pull_refresh_view)
//    PullToRefreshGridView pullRefreshView;
//    public GridView gridView;
//    @Bind(R.id.progress_bar)
//    ProgressBar progressBar;
//    private String id;//品牌id
//    private WaittingDialog dialog;
//    private int page = 1;//商品列表页码
//    private List<ProductBean.ProductListItem> productList;//商品列表
//    private AddProductGridAdapter addProductGridAdapter;//商品列表适配器
//
//    public static BrandProductFragment newInstance(String id) {
//
//        Bundle args = new Bundle();
//        args.putString("id", id);
//        BrandProductFragment fragment = new BrandProductFragment();
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        id = getArguments().getString("id");
//    }
//
//    @Override
//    protected View initView() {
//        View view = View.inflate(getActivity(), R.layout.fragment_search_qj, null);
//        ButterKnife.bind(this, view);
//        gridView = pullRefreshView.getRefreshableView();
//        dialog = ((BrandDetailActivity) getActivity()).dialog;
//        return view;
//    }
//
//    @Override
//    protected void initList() {
//        pullRefreshView.setPullToRefreshEnabled(false);
//        pullRefreshView.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {
//            @Override
//            public void onLastItemVisible() {
//                page++;
//                progressBar.setVisibility(View.VISIBLE);
//                getProductList();
//            }
//        });
//        gridView.setNumColumns(2);
//        gridView.setHorizontalSpacing(DensityUtils.dp2px(getActivity(), 15));
//        gridView.setVerticalSpacing(DensityUtils.dp2px(getActivity(), 15));
//        productList = new ArrayList<>();
//        addProductGridAdapter = new AddProductGridAdapter(getActivity(), productList, new ArrayList<SearchBean.Data.SearchItem>());
//        gridView.setAdapter(addProductGridAdapter);
//        gridView.setOnItemClickListener(this);
//    }
//
//    @Override
//    protected void requestNet() {
//        if (!dialog.isShowing()) {
//            dialog.show();
//        }
//        getProductList();
//    }
//
//    //品牌下产品列表
//    private void getProductList() {
//        ClientDiscoverAPI.getProductList(null, null, null, id, null, page + "", 8 + "", null, null, null, null, new RequestCallBack<String>() {
//            @Override
//            public void onSuccess(ResponseInfo<String> responseInfo) {
//                dialog.dismiss();
//                progressBar.setVisibility(View.GONE);
//                ProductBean productBean = new ProductBean();
//                try {
//                    Gson gson = new Gson();
//                    Type type = new TypeToken<ProductBean>() {
//                    }.getType();
//                    productBean = gson.fromJson(responseInfo.result, type);
//                } catch (JsonSyntaxException e) {
//                    e.printStackTrace();
//                }
//                if (productBean.isSuccess()) {
//                    if (page == 1) {
//                        productList.clear();
//                        pullRefreshView.lastTotalItem = -1;
//                        pullRefreshView.lastSavedFirstVisibleItem = -1;
//                    }
//                    productList.addAll(productBean.getData().getRows());
//                    addProductGridAdapter.notifyDataSetChanged();
////                    if (((BrandDetailActivity) getActivity()).tabLayout.getVisibility() == View.GONE &&
////                            productList.size() > 0) {
////                        ((BrandDetailActivity) getActivity()).setTabLayoutVisible(true);
////                    }
//                    return;
//                }
//                ToastUtils.showError(productBean.getMessage());
//            }
//
//            @Override
//            public void onFailure(HttpException error, String msg) {
//                dialog.dismiss();
//                progressBar.setVisibility(View.GONE);
//                ToastUtils.showError(R.string.net_fail);
//            }
//        });
//    }
//
//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        ButterKnife.unbind(this);
//    }
//
//    @Override
//    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        Intent intent = new Intent();
//        switch (productList.get(position).getStage()) {
//            case 9:
//                Log.e("<<<","可购买");
//                intent.setClass(getActivity(), BuyGoodsDetailsActivity.class);
//                break;
//            default:
//                Log.e("<<<","不可购买");
//                intent.setClass(getActivity(), GoodsDetailActivity.class);
//                break;
//        }
//        intent.putExtra("id", productList.get(position).get_id());
//        startActivity(intent);
//    }
//}
