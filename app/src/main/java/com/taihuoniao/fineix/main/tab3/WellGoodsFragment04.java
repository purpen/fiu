package com.taihuoniao.fineix.main.tab3;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.FrameLayout;

import com.google.gson.reflect.TypeToken;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.base.BaseFragment;
import com.taihuoniao.fineix.base.HttpRequest;
import com.taihuoniao.fineix.beans.HttpResponse;
import com.taihuoniao.fineix.beans.ProductBean;
import com.taihuoniao.fineix.beans.SearchBean;
import com.taihuoniao.fineix.beans.SubjectListBean;
import com.taihuoniao.fineix.common.GlobalDataCallBack;
import com.taihuoniao.fineix.main.tab3.adapter.ProductListGridAdapter;
import com.taihuoniao.fineix.main.tab3.adapter.WellgoodsSubjectAdapter2;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.URL;
import com.taihuoniao.fineix.product.BuyGoodsDetailsActivity;
import com.taihuoniao.fineix.utils.DensityUtils;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.view.ListViewForScrollView;
import com.taihuoniao.fineix.view.pulltorefresh.HeaderGridView;
import com.taihuoniao.fineix.view.pulltorefresh.PullToRefreshGridView2;

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

public class WellGoodsFragment04 extends BaseFragment implements AbsListView.OnScrollListener, View.OnTouchListener {

    @Bind(R.id.pull_refresh_view_001)
    PullToRefreshGridView2 pullToRefreshGridView;

    private List<SubjectListBean.RowsEntity> subjectList2;
    private WellgoodsSubjectAdapter2 wellgoodsSubjectAdapter2;

    private ProductListGridAdapter indexAdapter002;
    private List<ProductBean.RowsEntity> productList;
    private List<SearchBean.SearchItem> searchList;

    private int currentPage = 1;
    private String categoryId;
    private boolean isLoadMore;

    @Override
    protected void requestNet() {
        getLasteProduct();
        subjectList();
    }

    @Override
    protected View initView() {
        View view = View.inflate(getActivity(), R.layout.fragment_wellgoods_04, null);
        ButterKnife.bind(this, view);

        if (getArguments() != null) {
            categoryId = getArguments().getString("categoryId");
        }
        initGridView();
        return view;
    }

    private void initGridView() {
        HeaderGridView mGridView = pullToRefreshGridView.getRefreshableView();
        mGridView.addHeaderView(getHeaderView());
        pullToRefreshGridView.setMode(com.handmark.pulltorefresh.library.PullToRefreshBase.Mode.PULL_FROM_START);
        productList = new ArrayList<>();
        searchList = new ArrayList<>();
        indexAdapter002 = new ProductListGridAdapter(getActivity(),productList, searchList);
        mGridView.setAdapter(indexAdapter002);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(parent.getContext(), BuyGoodsDetailsActivity.class);
                intent.putExtra("id", productList.get(position).get_id());
                parent.getContext().startActivity(intent);
            }
        });

        pullToRefreshGridView.setOnRefreshListener(new com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2<HeaderGridView>() {
            @Override
            public void onPullDownToRefresh(com.handmark.pulltorefresh.library.PullToRefreshBase<HeaderGridView> refreshView) {
                isLoadMore = true;
                currentPage = 1;
                getLasteProduct();
            }

            @Override
            public void onPullUpToRefresh(com.handmark.pulltorefresh.library.PullToRefreshBase<HeaderGridView> refreshView) {

            }
        });
        pullToRefreshGridView.setOnLastItemVisibleListener(new com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener() {
            @Override
            public void onLastItemVisible() {
                if (isLoadMore) {
                    currentPage++;
                    getLasteProduct();
                } else {
                    isLoadMore = true;
                }
            }
        });
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

    private View headerView;
    private View getHeaderView() {
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        headerView = View.inflate(getActivity(), R.layout.headerview_wellgoods_tab4, null);
        headerView.setLayoutParams(layoutParams);
        ListViewForScrollView recyclerView002 = (ListViewForScrollView ) headerView.findViewById(R.id.recyclerView_index_002);
        subjectList2 = new ArrayList<>();
        wellgoodsSubjectAdapter2 = new WellgoodsSubjectAdapter2(getActivity(), subjectList2);
        recyclerView002.setAdapter(wellgoodsSubjectAdapter2);
        return headerView;
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int i) {}

    @Override
    public void onScroll(AbsListView absListView, int i, int i1, int i2) {}

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return false;
    }

    /**
     *  好货专题列表
     */
    private void subjectList() {
        HashMap<String, String> requestParams = ClientDiscoverAPI.getsubjectListRequestParams(null, 2 + "", null, null, 5 + "", "1");
        requestParams.put("category_id", categoryId);
        Call httpHandler = HttpRequest.post(requestParams, URL.SCENE_SUBJECT_GETLIST, new GlobalDataCallBack(){

            @Override
            public void onSuccess(String json) {
                pullToRefreshGridView.onRefreshComplete();
                HttpResponse<SubjectListBean> subjectListBean = JsonUtil.json2Bean(json, new TypeToken<HttpResponse<SubjectListBean>>() {});
                String total_rows = subjectListBean.getData().getTotal_rows();
                if (subjectListBean.isSuccess() &&  !TextUtils.isEmpty(total_rows) && Integer.valueOf(total_rows) > 0) {
                    if (currentPage == 1) {
                        subjectList2.clear();
                    }
                    FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, DensityUtils.dp2px(getActivity(), (156 + 211)));
                    headerView.setLayoutParams(layoutParams);
                    subjectList2.clear();
                    subjectList2.addAll(subjectListBean.getData().getRows());
                    wellgoodsSubjectAdapter2.notifyDataSetChanged();
                    headerView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(String error) {
                ToastUtils.showError(R.string.net_fail);
            }
        });
        addNet(httpHandler);
    }

    /**
     * 新品
     */
    private void getLasteProduct(){
        HashMap<String, String> requestParams = ClientDiscoverAPI.getgetProductListRequestParams(null, null, null, null, null, String.valueOf(currentPage), String.valueOf(8), null, null, null, "0", null);
        requestParams.put("category_id", categoryId);
        HttpRequest.post(requestParams, URL.URLSTRING_PRODUCTSLIST, new GlobalDataCallBack(){
            @Override
            public void onSuccess(String json) {
                pullToRefreshGridView.onRefreshComplete();
                HttpResponse<ProductBean> productBean = JsonUtil.json2Bean(json, new TypeToken<HttpResponse<ProductBean>>() {});
                if (productBean.isSuccess()) {
                    if (currentPage == 1) {
                        searchList.clear();
                        productList.clear();
                    }
                    productList.addAll(productBean.getData().getRows());
                    indexAdapter002.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(String error) { }
        });
    }
}
