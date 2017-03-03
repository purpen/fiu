package com.taihuoniao.fineix.main.tab3;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.EditRecyclerAdapter;
import com.taihuoniao.fineix.adapters.FirstProductAdapter;
import com.taihuoniao.fineix.adapters.WellGoodsProductCategoryAdapter;
import com.taihuoniao.fineix.adapters.WellgoodsSubjectAdapter;
import com.taihuoniao.fineix.base.BaseFragment;
import com.taihuoniao.fineix.base.HttpRequest;
import com.taihuoniao.fineix.beans.CategoryListBean;
import com.taihuoniao.fineix.beans.FirstProductBean;
import com.taihuoniao.fineix.beans.SubjectListBean;
import com.taihuoniao.fineix.common.GlobalCallBack;
import com.taihuoniao.fineix.common.GlobalDataCallBack;
import com.taihuoniao.fineix.main.ShopMarginDecoration;
import com.taihuoniao.fineix.main.tab3.adapter.WellGoodsCategoryAdapter;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.DataConstants;
import com.taihuoniao.fineix.network.URL;
import com.taihuoniao.fineix.product.BuyGoodsDetailsActivity;
import com.taihuoniao.fineix.product.GoodsListActivity;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.view.CustomGridViewForScrollView;
import com.taihuoniao.fineix.view.dialog.WaittingDialog;
import com.taihuoniao.fineix.view.pulltorefresh.PullToRefreshBase;
import com.taihuoniao.fineix.view.pulltorefresh.PullToRefreshListView;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;

import static com.taihuoniao.fineix.R.id.listView;

/**
 * Created by Stephen on 2017/3/3 23:00
 * Email: 895745843@qq.com
 */

public class WellGoodsFragment01 extends BaseFragment implements AbsListView.OnScrollListener, View.OnTouchListener {

    @Bind(R.id.pull_refresh_view_001)
    PullToRefreshListView pullRefreshView001;

    private ListView mListView;

    private List<CategoryListBean.CategoryListItem> categoryList;//产品分类数据
    private WellGoodsProductCategoryAdapter wellGoodsProductCategoryAdapter;//产品分类大图适配器

    private List<FirstProductBean.DataBean.ItemsBean> firstProductList;//最新好货推荐数据
    private FirstProductAdapter firstProductAdapter;//最新好货推荐适配器

    private List<SubjectListBean.DataBean.RowsBean> subjectList;//好货页面专题及产品列表
    private WellgoodsSubjectAdapter wellgoodsSubjectAdapter;//好货页面爪蹄及产品适配器

    private int currentPage = 0;

    @Override
    protected void requestNet() {
        firstProducts();
        subjectList();
    }

    @Override
    protected View initView() {
        View view = View.inflate(getActivity(), R.layout.fragment_wellgoods_01, null);
        ButterKnife.bind(this, view);

        initListView();
        mListView.addHeaderView(getHeaderView());

        return view;
    }

    private void initListView() {
        pullRefreshView001.animLayout();
        mListView = pullRefreshView001.getRefreshableView();
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
        // TODO: inflate a fragment view
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
        View headerView = View.inflate(getActivity(), R.layout.header1_wellgoods_fragment, null);
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
        HashMap<String, String> requestParams = ClientDiscoverAPI.getsubjectListRequestParams(currentPage + "", 8 + "", null, null, 5 + "", "2");
        Call httpHandler = HttpRequest.post(requestParams, URL.SCENE_SUBJECT_GETLIST, new GlobalDataCallBack(){

            //        HttpHandler<String> httpHandler = ClientDiscoverAPI.subjectList(currentPage + "", 8 + "", null, null, 5 + "", "2", new RequestCallBack<String>() {
            @Override
            public void onSuccess(String json) {
                Log.e("<<<好货专题列表", json);
                pullRefreshView001.onRefreshComplete();
                SubjectListBean subjectListBean = new SubjectListBean();
                try {
                    Gson gson = new Gson();
                    Type type = new TypeToken<SubjectListBean>() {
                    }.getType();
                    subjectListBean = gson.fromJson(json, type);
                } catch (JsonSyntaxException e) {
                    Log.e("<<<", "解析异常=" + e.toString());
                }
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
            //        HttpHandler<String> httpHandler = ClientDiscoverAPI.firstProducts(new RequestCallBack<String>() {
            @Override
            public void onSuccess(String json) {
                Log.e("<<<最新好货推荐", json);
//                WriteJsonToSD.writeToSD("json",json);
                FirstProductBean firstProductBean = new FirstProductBean();
                try {
                    Gson gson = new Gson();
                    Type type = new TypeToken<FirstProductBean>() {
                    }.getType();
                    firstProductBean = gson.fromJson(json, type);
                } catch (JsonSyntaxException e) {
                    Log.e("<<<", "解析异常=" + e.toString());
                }
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
}
