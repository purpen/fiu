package com.taihuoniao.fineix.main.tab3;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.reflect.TypeToken;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.AddProductGridAdapter;
import com.taihuoniao.fineix.adapters.WellgoodsSubjectAdapter;
import com.taihuoniao.fineix.base.BaseFragment;
import com.taihuoniao.fineix.base.HttpRequest;
import com.taihuoniao.fineix.beans.HttpResponse;
import com.taihuoniao.fineix.beans.ProductBean;
import com.taihuoniao.fineix.beans.SearchBean;
import com.taihuoniao.fineix.beans.SubjectListBean;
import com.taihuoniao.fineix.common.GlobalDataCallBack;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.URL;
import com.taihuoniao.fineix.product.BuyGoodsDetailsActivity;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.view.GridViewForScrollView;
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

public class WellGoodsFragment03 extends BaseFragment implements AbsListView.OnScrollListener, View.OnTouchListener {

    @Bind(R.id.pull_refresh_view_001)
    PullToRefreshListView pullRefreshView001;

    private List<SubjectListBean.RowsEntity> subjectList;//好货页面专题及产品列表
    private WellgoodsSubjectAdapter wellgoodsSubjectAdapter;//好货页面爪蹄及产品适配器

    private AddProductGridAdapter indexAdapter002;//主题列表适配器
    private List<ProductBean.RowsEntity> productList;
    private List<SearchBean.SearchItem> searchList;

    private int currentPage = 1;

    @Override
    protected void requestNet() {
        subjectList();
//        getLasteProduct();
    }

    @Override
    protected View initView() {
        View view = View.inflate(getActivity(), R.layout.fragment_wellgoods_01, null);
        ButterKnife.bind(this, view);

        initListView();
//        mListView.addHeaderView(getHeaderView());

        return view;
    }

    private void initListView() {
        pullRefreshView001.animLayout();
        ListView mListView = pullRefreshView001.getRefreshableView();
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
        mListView = pullRefreshView001.getRefreshableView();
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
        View headerView = View.inflate(getActivity(), R.layout.headerview_wellgoods_tab3, null);

        GridViewForScrollView recyclerView002 = (GridViewForScrollView ) headerView.findViewById(R.id.recyclerView_index_002);
        productList = new ArrayList<>();
        searchList = new ArrayList<>();
        indexAdapter002 = new AddProductGridAdapter(getActivity(),productList, searchList);
        recyclerView002.setAdapter(indexAdapter002);
        recyclerView002.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(parent.getContext(), BuyGoodsDetailsActivity.class);
                intent.putExtra("id", productList.get(position).get_id());
                parent.getContext().startActivity(intent);
            }
        });

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
        HashMap<String, String> requestParams = ClientDiscoverAPI.getsubjectListRequestParams(currentPage + "", 8 + "", null, null, 5 + "", "0");
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

    /**
     * 新品
     */
    private void getLasteProduct(){
        HashMap<String, String> requestParams = ClientDiscoverAPI.getgetProductListRequestParams(null, null, null, null, null, null, String.valueOf(5), null, null, null, "1", null);
        HttpRequest.post(requestParams, URL.URLSTRING_PRODUCTSLIST, new GlobalDataCallBack(){
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
            public void onFailure(String error) { }
        });
    }
}
