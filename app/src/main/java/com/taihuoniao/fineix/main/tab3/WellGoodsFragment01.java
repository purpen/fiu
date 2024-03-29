package com.taihuoniao.fineix.main.tab3;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import com.google.gson.reflect.TypeToken;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.EditRecyclerAdapter;
import com.taihuoniao.fineix.adapters.FirstProductAdapter;
import com.taihuoniao.fineix.adapters.WellgoodsSubjectAdapter;
import com.taihuoniao.fineix.base.BaseFragment;
import com.taihuoniao.fineix.base.HttpRequest;
import com.taihuoniao.fineix.beans.FirstProductBean;
import com.taihuoniao.fineix.beans.HttpResponse;
import com.taihuoniao.fineix.beans.SubjectListBean;
import com.taihuoniao.fineix.common.GlobalDataCallBack;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.URL;
import com.taihuoniao.fineix.product.BuyGoodsDetailsActivity;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.view.pulltorefresh.PullToRefreshBase;
import com.taihuoniao.fineix.view.pulltorefresh.PullToRefreshListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;


/**
 * Created by Stephen on 2017/3/3 23:00
 * Email: 895745843@qq.com
 */

public class WellGoodsFragment01 extends BaseFragment implements AbsListView.OnScrollListener, View.OnTouchListener {

    @Bind(R.id.pull_refresh_view_001)
    PullToRefreshListView pullRefreshView001;

    // 新鲜好货早知道
    private List<FirstProductBean.ItemsEntity> firstProductList;
    private FirstProductAdapter firstProductAdapter;

    // 好货人气王
    private List<FirstProductBean.ItemsEntity> secondProductList;
    private FirstProductAdapter secondProductAdapter;

    // 好货专题(推荐)
    private List<SubjectListBean.RowsEntity> subjectList;
    private WellgoodsSubjectAdapter wellgoodsSubjectAdapter;

    private int currentPage = 0;

    @Override
    protected void requestNet() {
        subjectList();
        firstProducts();
        secondProduct();
    }

    @Override
    protected View initView() {
        View view = View.inflate(getActivity(), R.layout.fragment_wellgoods_01, null);
        ButterKnife.bind(this, view);
        initListView();
        return view;
    }

    private void initListView() {
        pullRefreshView001.animLayout();
        ListView mListView = pullRefreshView001.getRefreshableView();
        mListView.addHeaderView(getHeaderView());
        pullRefreshView001.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestNet();
            }
        });
        mListView.setSelector(R.color.nothing);
        mListView.setDividerHeight(0);
        mListView.setOnScrollListener(this);
        mListView.setOnTouchListener(this);
        subjectList = new ArrayList<>();
        wellgoodsSubjectAdapter = new WellgoodsSubjectAdapter(getActivity(), subjectList);
        mListView.setAdapter(wellgoodsSubjectAdapter);
        pullRefreshView001.animLayout();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    private View getHeaderView() {
        View headerView = View.inflate(getActivity(), R.layout.headerview_wellgoods_tab1, null);
        RecyclerView productRecycler = (RecyclerView) headerView.findViewById(R.id.product_recycler);
        productRecycler.setHasFixedSize(true);
        productRecycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        firstProductList = new ArrayList<>();
        firstProductAdapter = new FirstProductAdapter(firstProductList, new EditRecyclerAdapter.ItemClick() {
            @Override
            public void click(int postion) {
                Intent intent = new Intent(getActivity(), BuyGoodsDetailsActivity.class);
                intent.putExtra("id", firstProductList.get(postion).get_id());
                getActivity().startActivity(intent);
            }
        });
        productRecycler.setAdapter(firstProductAdapter);
        RecyclerView productRecycler2 = (RecyclerView) headerView.findViewById(R.id.product_recycler2);
        productRecycler2.setHasFixedSize(true);
        productRecycler2.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        secondProductList = new ArrayList<>();
        secondProductAdapter = new FirstProductAdapter(secondProductList, new EditRecyclerAdapter.ItemClick() {
            @Override
            public void click(int postion) {
                Intent intent = new Intent(getActivity(), BuyGoodsDetailsActivity.class);
                intent.putExtra("id", secondProductList.get(postion).get_id());
                getActivity().startActivity(intent);
            }
        });
        productRecycler2.setAdapter(secondProductAdapter);
        return headerView;
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int i) {

    }

    @Override
    public void onScroll(AbsListView absListView, int i, int i1, int i2) {

    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return false;
    }

    //好货专题列表
    private void subjectList() {
        HashMap<String, String> requestParams = ClientDiscoverAPI.getsubjectListRequestParams(currentPage + "", 8 + "", "1", null, 5 + "", "1");
//        HashMap<String, String> requestParams = ClientDiscoverAPI.getsubjectListRequestParams(currentPage + "", 8 + "", null, null, 5 + "", "2");
        Call httpHandler = HttpRequest.post(requestParams, URL.SCENE_SUBJECT_GETLIST, new GlobalDataCallBack(){

            @Override
            public void onSuccess(String json) {
                pullRefreshView001.onRefreshComplete();
                HttpResponse<SubjectListBean> subjectListBean = JsonUtil.json2Bean(json, new TypeToken<HttpResponse<SubjectListBean>>() {});
                if (subjectListBean.isSuccess()) {
                    if (currentPage == 1) {
                        pullRefreshView001.lastTotalItem = -1;
                        pullRefreshView001.lastSavedFirstVisibleItem = -1;
                        subjectList.clear();
                    }
                    subjectList.addAll(subjectListBean.getData().getRows());
                    wellgoodsSubjectAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(String error) {
                pullRefreshView001.onRefreshComplete();
                ToastUtils.showError(R.string.net_fail);
            }
        });
        addNet(httpHandler);
    }

    //最新好货推荐
    private void firstProducts() {
        HashMap<String, String> requestParams = ClientDiscoverAPI.getfirstProductsRequestParams();
        Call httpHandler =  HttpRequest.post(requestParams, URL.PRODUCCT_INDEX_NEW, new GlobalDataCallBack(){
            @Override
            public void onSuccess(String json) {
                HttpResponse<FirstProductBean> firstProductBean = JsonUtil.json2Bean(json, new TypeToken<HttpResponse<FirstProductBean>>() { });
                if (firstProductBean.isSuccess()) {
                    firstProductList.clear();
                    firstProductList.addAll(firstProductBean.getData().getItems());
                    firstProductAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(String error) {

            }
        });
        addNet(httpHandler);
    }

    /**
     * 好货最热推荐接口
     */
    private void secondProduct(){
        HashMap<String, String> requestParams = ClientDiscoverAPI.getfirstProductsRequestParams();
        HttpRequest.post(requestParams, URL.PRODUCT_INDEX_HOT, new GlobalDataCallBack(){

            @Override
            public void onSuccess(String json) {
                Product3Bean product3Bean = JsonUtil.fromJson(json,new TypeToken<HttpResponse<Product3Bean>>(){});
                if (product3Bean != null && product3Bean.getItems() != null) {
                    int size = product3Bean.getItems().size();

                    secondProductList.clear();
                    for(int i = 0; i < size; i++) {
                        Product3Bean.ItemsEntity itemsEntity = product3Bean.getItems().get(i);
                        FirstProductBean.ItemsEntity bean = new FirstProductBean.ItemsEntity();
                        bean.set_id(itemsEntity.get_id());
                        bean.setTitle(itemsEntity.getTitle());
                        bean.setBrand_cover_url(itemsEntity.getBrand_cover_url());
                        bean.setBrand_id(itemsEntity.getBrand_id());
                        bean.setCover_url(itemsEntity.getCover_url());
                        bean.setSale_price(itemsEntity.getSale_price());
                        secondProductList.add(bean);
                    }
                    secondProductAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(String error) { }
        });
    }
}
