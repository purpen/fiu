package com.taihuoniao.fineix.zone.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.google.gson.reflect.TypeToken;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.base.BaseFragment;
import com.taihuoniao.fineix.base.HttpRequest;
import com.taihuoniao.fineix.beans.HttpResponse;
import com.taihuoniao.fineix.common.GlobalDataCallBack;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.URL;
import com.taihuoniao.fineix.product.BuyGoodsDetailsActivity;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.LogUtil;
import com.taihuoniao.fineix.view.dialog.WaittingDialog;
import com.taihuoniao.fineix.zone.MarginDecoration;
import com.taihuoniao.fineix.zone.adapter.ZoneRelateProductsAdapter;
import com.taihuoniao.fineix.zone.adapter.ZoneRelateSceneAdapter;
import com.taihuoniao.fineix.zone.bean.ZoneRelateProductsBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;


/**
 * 地盘相关情境
 */
public class ZoneRelateProductsFragment extends BaseFragment {
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    private static final String ZONE_ID = "ZONE_ID";
    private WaittingDialog dialog;
    private String sZoneId;
    private int curPage = 1;
    private List<ZoneRelateProductsBean.RowsBean> productList;
    private boolean isBottom;
    private boolean isLoading;
    private OnFragmentInteractionListener mListener;
    private String stick = "1";//默认是推荐
    private String sort = "1"; //默认是推荐
    private ZoneRelateProductsAdapter productsAdapter;
    private LinearLayoutManager layoutManager;
    public ZoneRelateProductsFragment() {
        // Required empty public constructor
    }

    public static ZoneRelateProductsFragment newInstance(String param) {
        ZoneRelateProductsFragment fragment = new ZoneRelateProductsFragment();
        Bundle args = new Bundle();
        args.putString(ZONE_ID, param);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            sZoneId = getArguments().getString(ZONE_ID);
        }
    }


    @Override
    protected View initView() {
        dialog = new WaittingDialog(activity);
        View view = View.inflate(activity, R.layout.fragment_list, null);
        return view;
    }

    @Override
    protected void initList() {

        productList = new ArrayList();
        productsAdapter = new ZoneRelateProductsAdapter(activity, productList);
        layoutManager = new GridLayoutManager(activity,2);
        recyclerView.addItemDecoration(new MarginDecoration(activity,R.dimen.dp16));
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(productsAdapter);
    }


    @Override
    protected void installListener() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
                if (!isLoading &&!isBottom && lastVisibleItemPosition == productList.size()-1){
                    requestNet();
                }

            }
        });
        productsAdapter.setOnItemClickListener(new ZoneRelateSceneAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (productList==null || productList.size()==0) return;
                Intent intent = new Intent(activity, BuyGoodsDetailsActivity.class);
                intent.putExtra("id",productList.get(position).product._id);
                intent.putExtra("storage_id",sZoneId);
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
    }


    @Override
    protected void requestNet() {
        if (TextUtils.isEmpty(sZoneId)) return;
        HashMap param = ClientDiscoverAPI.getRelateProducts(curPage, sZoneId);
        HttpRequest.post(param, URL.ZONE_RELATE_PRODUCTS, new GlobalDataCallBack() {
            @Override
            public void onStart() {
                isLoading = true;
                if (dialog != null && !activity.isFinishing()) dialog.show();
            }

            @Override
            public void onSuccess(String json) {
                isLoading = false;
                if (dialog != null && !activity.isFinishing()) dialog.dismiss();
                LogUtil.e(json);
                HttpResponse<ZoneRelateProductsBean> response = JsonUtil.json2Bean(json, new TypeToken<HttpResponse<ZoneRelateProductsBean>>() {
                });

                if (response.getData().rows.size() > 0) {
                    curPage++;
                    productList.addAll(response.getData().rows);
                    refreshUI();
                } else {
                    isBottom = true;
                }
            }

            @Override
            public void onFailure(String error) {
                isLoading = false;
                LogUtil.e(error);
                if (dialog != null && !activity.isFinishing()) dialog.dismiss();
            }
        });
    }

    @Override
    protected void refreshUI() {
        if (productsAdapter == null) {
            recyclerView.setAdapter(productsAdapter);
        } else {
            productsAdapter.notifyDataSetChanged();
        }
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction();
    }
}
