package com.taihuoniao.fineix.qingjingOrSceneDetails.qjdetails;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.AddProductGridAdapter;
import com.taihuoniao.fineix.adapters.IndexQJListAdapter;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.base.HttpRequest;
import com.taihuoniao.fineix.beans.HttpResponse;
import com.taihuoniao.fineix.beans.IndexUserListBean;
import com.taihuoniao.fineix.beans.ProductBean;
import com.taihuoniao.fineix.beans.SceneList;
import com.taihuoniao.fineix.beans.SearchBean;
import com.taihuoniao.fineix.beans.User;
import com.taihuoniao.fineix.common.GlobalDataCallBack;
import com.taihuoniao.fineix.main.tab3.Tools;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.URL;
import com.taihuoniao.fineix.product.BuyGoodsDetailsActivity;
import com.taihuoniao.fineix.qingjingOrSceneDetails.adapter.QJDetailsProductAdapter;
import com.taihuoniao.fineix.qingjingOrSceneDetails.adapter.QJDetailsTopAdapter;
import com.taihuoniao.fineix.qingjingOrSceneDetails.bean.SceneDetailsBean2;
import com.taihuoniao.fineix.qingjingOrSceneDetails.bean.SceneListBean2;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.utils.WindowUtils;
import com.taihuoniao.fineix.view.GlobalTitleLayout;
import com.taihuoniao.fineix.view.GridViewForScrollView;
import com.taihuoniao.fineix.view.ListViewForScrollView;
import com.taihuoniao.fineix.view.pulltorefresh.PullToRefreshBase;
import com.taihuoniao.fineix.view.pulltorefresh.PullToRefreshListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 情境详情
 * Created by Stephen on 2017/03/01.
 */
public class QJDetailActivity2 extends BaseActivity implements PullToRefreshBase.OnRefreshListener, AbsListView.OnScrollListener {

    @Bind(R.id.title_layout)
    GlobalTitleLayout titleLayout;
    @Bind(R.id.pullToRefreshListView_home)
    PullToRefreshListView pullRefreshView;

    // 相关情境
    private ListView mListView;
    private List<SceneListBean2.RowsEntity> sceneList;
    private IndexQJListAdapter indexQJListAdapter;

    // 情境详情
    private List<SceneList.DataBean.RowsBean> sceneList2;
    private QJDetailsTopAdapter indexQJListAdapter2;

    // 情境详情下产品列表
    private QJDetailsProductAdapter mQjDetailsProductAdapter;

    //可能喜欢的产品
    private AddProductGridAdapter indexAdapter002;//主题列表适配器
    private List<ProductBean.RowsEntity> productList;
    private List<SearchBean.SearchItem> searchList;

    // 情境ID
    private String situationId;

    public QJDetailActivity2() {
        super(R.layout.activity_qjdetail2);
    }

    @Override
    protected void getIntentData() {
        situationId = getIntent().getStringExtra("id");
        if (situationId == null) {
            ToastUtils.showError("访问的情境不存在或已删除");
            finish();
        }
    }

    @Override
    protected void initView() {
        titleLayout.setTitle(R.string.qj_detail);
        titleLayout.setContinueTvVisible(false);
        WindowUtils.chenjin(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    @Override
    protected void requestNet() {
        requestData001(situationId);
    }

    /**
     * 情境详情
     */
    private void requestData001(String situationId){
        HashMap<String, String> requestParams = ClientDiscoverAPI.getsceneDetailsRequestParams(situationId);
        HttpRequest.post(requestParams, URL.SCENE_DETAILS, new GlobalDataCallBack(){
            @Override
            public void onSuccess(String json) {
                SceneDetailsBean2 bean = JsonUtil.fromJson(json, new TypeToken<HttpResponse<SceneDetailsBean2>>() { });
                if (bean != null) {
                    dealResult(bean);
                } else {
                    Toast.makeText(QJDetailActivity2.this, "解析错误", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(String error) {}
        });
    }

    /**
     * 相关情境
     */
    private void requestData002(String categoryIDs){
        HashMap<String, String> re = ClientDiscoverAPI.getSceneListRequestParams(1 + "", 10 + "", null, null, 0 + "", null, null, null);
        re.put("category_ids", categoryIDs);
        re.put("stick","1");
        re.put("sort","1");
        re.put("is_product", "1");
        HttpRequest.post(re, URL.SCENE_LIST, new GlobalDataCallBack(){
            @Override
            public void onSuccess(String json) {
                pullRefreshView.onRefreshComplete();
                refreshListView(json);
            }

            @Override
            public void onFailure(String error) {}
        });
    }

    private void refreshListView(String json) {
        SceneListBean2 bean = JsonUtil.fromJson(json, new TypeToken<HttpResponse<SceneListBean2>>() { });
        if (bean != null) {
            if (currentPage == 1) {
                sceneList.clear();
                pullRefreshView.lastTotalItem = -1;
                pullRefreshView.lastSavedFirstVisibleItem = -1;
            }
            List<SceneListBean2.RowsEntity> rows = bean.getRows();
//            List<SceneList.DataBean.RowsBean> rowsBeen = Tools.newListConvertOldList(rows);
            sceneList.addAll(rows);
            new android.os.Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    indexQJListAdapter.notifyDataSetChanged();
                }
            }, 200);
        }
    }

    /**
     * 猜你喜欢
     */
    private void requestData003(String cagegoryId){
        HashMap<String, String> requestParams = ClientDiscoverAPI.getgetProductListRequestParams(null, null, null, null, null, null, null, null, null, null, null, null);
        requestParams.put("category_id", cagegoryId);
        requestParams.put("stick", "1");
        requestParams.put("sort", "4");
        HttpRequest.post(requestParams, URL.URLSTRING_PRODUCTSLIST, new GlobalDataCallBack(){
            @Override
            public void onSuccess(String json) {
            }

            @Override
            public void onFailure(String error) {}
        });
    }

    @Override
    protected void initList() {
        mListView = pullRefreshView.getRefreshableView();
        mListView.addHeaderView(getHeadView());
        pullRefreshView.animLayout();
        mListView.setDividerHeight(0);
        pullRefreshView.setOnRefreshListener(this);
        mListView.setOnScrollListener(this);

        sceneList = new ArrayList<>();
        ArrayList<User> userList = new ArrayList<>();
        indexQJListAdapter = new IndexQJListAdapter(activity, sceneList, userList);
        mListView.setAdapter(indexQJListAdapter);
    }

    /**
     * headView 详情 + 产品 + 喜欢
     * @return headView
     */
    public View getHeadView() {
        View headerView = View.inflate(activity, R.layout.header_qj_details_headerview, null);
        ListViewForScrollView listViewForScrollView = (ListViewForScrollView) headerView.findViewById(R.id.listViewForScrollView);

        sceneList2 = new ArrayList<>();
        List<IndexUserListBean.DataBean.UsersBean> userList2 = new ArrayList<>();
        indexQJListAdapter2 = new QJDetailsTopAdapter(activity, sceneList2, userList2);
        listViewForScrollView.setAdapter(indexQJListAdapter2);

        ListViewForScrollView listViewForScrollViewQjDetailsProduct = (ListViewForScrollView ) headerView.findViewById(R.id.listViewForScrollView_qj_details_product);
        mQjDetailsProductAdapter = new QJDetailsProductAdapter(null);
        listViewForScrollViewQjDetailsProduct.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(QJDetailActivity2.this, BuyGoodsDetailsActivity.class);
                intent.putExtra("id", mQjDetailsProductAdapter.getList().get(i).get_id());
                startActivity(intent);
            }
        });

        GridViewForScrollView recyclerView002 = (GridViewForScrollView ) headerView.findViewById(R.id.recyclerView_index_002);
        productList = new ArrayList<>();
        searchList = new ArrayList<>();
        indexAdapter002 = new AddProductGridAdapter(activity,productList, searchList);
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
    public void onRefresh() {
        currentPage = 1;
        requestNet();
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int i) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (visibleItemCount > mListView.getHeaderViewsCount()
                && (firstVisibleItem + visibleItemCount >= totalItemCount)) {
            if (firstVisibleItem != pullRefreshView.lastSavedFirstVisibleItem && pullRefreshView.lastTotalItem != totalItemCount) {
                pullRefreshView.lastSavedFirstVisibleItem = firstVisibleItem;
                pullRefreshView.lastTotalItem = totalItemCount;
                currentPage++;
                requestData003(situationId);
            }
        }
    }

    private int currentPage;


    private String getStringFromList(List<String> strings){
        StringBuilder newStr = new StringBuilder();
        if (strings == null) {
            return null;
        }
        for(int i = 0 ; i < strings.size(); i ++) {
            newStr.append(strings.get(i));
            if (i == strings.size() - 1) {
                continue;
            }
            newStr.append(",");
        }
        return newStr.toString();
    }

    private void dealResult(SceneDetailsBean2 bean2) {

        // 请求相关情境
        loadData2(bean2);

        // 请求可能喜欢的产品
        loadData3(bean2);

        // 2017/3/2 情境详情
        List<SceneList.DataBean.RowsBean> rowsBeen = Tools.newListConvertOldList2(bean2);
        sceneList2.addAll(rowsBeen);

        List<SceneDetailsBean2.ProductsEntity> products = bean2.getProducts();
        mQjDetailsProductAdapter.putList(products);

        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                indexQJListAdapter2.notifyDataSetChanged();
            }
        }, 200);
    }

    private void loadData3(SceneDetailsBean2 bean2) {
        List<SceneDetailsBean2.ProductsEntity> products = bean2.getProducts();
        if (products != null) {
            List<String> categoryIds = new ArrayList<>();
            for(int i = 0; i < products.size(); i++) {
                List<String> ids = products.get(i).getCategory_ids();
                if (ids != null) {
                    categoryIds.addAll(ids);
                }
            }
//            requestData003(getStringFromList(categoryIds));
            getLasteProduct(getStringFromList(categoryIds));
        }
    }

    private void loadData2(SceneDetailsBean2 bean2) {
        List<String> category_id = bean2.getCategory_ids();
        if (category_id != null) {
            requestData002(getStringFromList(category_id));
        }
    }

    /**
     * 你可能喜欢的产品
     */
    private void getLasteProduct(String ids){
        HashMap<String, String> requestParams = ClientDiscoverAPI.getgetProductListRequestParams(null, null, null, null, null, null, null, null, null, null, null, null);
        requestParams.put("category_id", ids);
        requestParams.put("stick", "1");
        requestParams.put("sort", "4");
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
