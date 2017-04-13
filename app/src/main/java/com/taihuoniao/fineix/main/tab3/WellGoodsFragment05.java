package com.taihuoniao.fineix.main.tab3;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;

import com.google.gson.reflect.TypeToken;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.base.BaseFragment;
import com.taihuoniao.fineix.base.HttpRequest;
import com.taihuoniao.fineix.beans.BrandListBean;
import com.taihuoniao.fineix.beans.HttpResponse;
import com.taihuoniao.fineix.common.GlobalDataCallBack;
import com.taihuoniao.fineix.main.tab3.adapter.BrandListGridAdapter;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.URL;
import com.taihuoniao.fineix.product.BrandDetailActivity;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.view.pulltorefresh.HeaderGridView;
import com.taihuoniao.fineix.view.pulltorefresh.PullToRefreshGridView2;

import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Stephen on 2017/4/12 17:00
 * Email: 895745843@qq.com
 */

public class WellGoodsFragment05 extends BaseFragment implements AbsListView.OnScrollListener, View.OnTouchListener {

    @Bind(R.id.pull_refresh_view_001)
    PullToRefreshGridView2 pullToRefreshGridView;

    private BrandListGridAdapter indexAdapter002;
    private int currentPage = 1;
    private boolean isLoadMore;

    @Override
    protected void requestNet() {
        getLasteProduct();
    }

    @Override
    protected View initView() {
        View view = View.inflate(getActivity(), R.layout.fragment_wellgoods_04, null);
        view.setBackgroundResource(R.color.white);
        ButterKnife.bind(this, view);
        initGridView();
        return view;
    }

    private void initGridView() {
        pullToRefreshGridView.getRefreshableView().setNumColumns(4);
        pullToRefreshGridView.setMode(com.handmark.pulltorefresh.library.PullToRefreshBase.Mode.PULL_FROM_START);
        indexAdapter002 = new BrandListGridAdapter();
        pullToRefreshGridView.setAdapter(indexAdapter002);
        pullToRefreshGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(activity, BrandDetailActivity.class);
                intent.putExtra("id", indexAdapter002.getmRowsEntities().get(position).get_id());
                activity.startActivity(intent);
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

    @Override
    public void onScrollStateChanged(AbsListView absListView, int i) {}

    @Override
    public void onScroll(AbsListView absListView, int i, int i1, int i2) {}

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return false;
    }

    /**
     * 新品
     */
    private void getLasteProduct(){
        Map<String, String> requestParams = ClientDiscoverAPI.getSceneBrandsList(currentPage);
        HttpRequest.post(requestParams, URL.BRAND_SCENE_LIST, new GlobalDataCallBack(){
            @Override
            public void onSuccess(String json) {
                if (pullToRefreshGridView != null) {
                    pullToRefreshGridView.onRefreshComplete();
                }
                HttpResponse<BrandListBean> productBean = JsonUtil.json2Bean(json, new TypeToken<HttpResponse<BrandListBean>>() {});
                List<BrandListBean.RowsEntity> rows = productBean.getData().getRows();
                if (productBean.isSuccess()) {
                    if (currentPage == 1) {
                        indexAdapter002.setList(rows);
                    } else {
                        indexAdapter002.addList(rows);
                    }
                }
            }

            @Override
            public void onFailure(String error) { }
        });
    }
}
