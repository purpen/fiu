//package com.taihuoniao.fineix.product.fragment;
//
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
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
//import com.taihuoniao.fineix.adapters.BrandQJAdapter;
//import com.taihuoniao.fineix.beans.ProductAndSceneListBean;
//import com.taihuoniao.fineix.network.ClientDiscoverAPI;
//import com.taihuoniao.fineix.product.BrandDetailActivity;
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
//public class BrandQJFragment extends SearchFragment {
//
//    private String id;//品牌id
//    @Bind(R.id.pull_refresh_view)
//    PullToRefreshGridView pullRefreshView;
//    public GridView gridView;
//    @Bind(R.id.progress_bar)
//    ProgressBar progressBar;
//    private WaittingDialog dialog;
//    private int page = 1;
//    private List<ProductAndSceneListBean.ProductAndSceneItem> qjList;
//    private BrandQJAdapter brandQJAdapter;
//
//    public static BrandQJFragment newInstance(String id) {
//
//        Bundle args = new Bundle();
//        args.putString("id", id);
//        BrandQJFragment fragment = new BrandQJFragment();
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
//                getQJList();
//            }
//        });
//        gridView.setNumColumns(2);
//        gridView.setHorizontalSpacing(DensityUtils.dp2px(getActivity(), 15));
//        gridView.setVerticalSpacing(DensityUtils.dp2px(getActivity(), 15));
//        qjList = new ArrayList<>();
//        //设置适配器
//        brandQJAdapter = new BrandQJAdapter(getActivity(), qjList);
//        gridView.setAdapter(brandQJAdapter);
//    }
//
//    @Override
//    protected void requestNet() {
//        if (!dialog.isShowing()) {
//            dialog.show();
//        }
//        getQJList();
//    }
//
//    //获取品牌下的情景
//    private void getQJList() {
//        ClientDiscoverAPI.productAndScene(page + "", 1 + "", null, null, id, new RequestCallBack<String>() {
//            @Override
//            public void onSuccess(ResponseInfo<String> responseInfo) {
//                dialog.dismiss();
//                progressBar.setVisibility(View.GONE);
//                Log.e("<<<品牌下的情景", responseInfo.result);
//                ProductAndSceneListBean productAndSceneListBean = new ProductAndSceneListBean();
//                try {
//                    Gson gson = new Gson();
//                    Type type = new TypeToken<ProductAndSceneListBean>() {
//                    }.getType();
//                    productAndSceneListBean = gson.fromJson(responseInfo.result, type);
//                } catch (JsonSyntaxException e) {
//                    Log.e("<<<品牌下的情景", "解析异常=" + e.toString());
//                }
//                if (productAndSceneListBean.isSuccess()) {
//                    if (page == 1) {
//                        qjList.clear();
//                    }
//                    qjList.addAll(productAndSceneListBean.getData().getRows());
//                    brandQJAdapter.notifyDataSetChanged();
//                }
////                WriteJsonToSD.writeToSD("json",responseInfo.result);
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
//}
