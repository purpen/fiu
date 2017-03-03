package com.taihuoniao.fineix.qingjingOrSceneDetails.qjdetails;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
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
import com.taihuoniao.fineix.common.GlobalDataCallBack;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.URL;
import com.taihuoniao.fineix.product.BuyGoodsDetailsActivity;
import com.taihuoniao.fineix.qingjingOrSceneDetails.adapter.QJDetailsProductAdapter;
import com.taihuoniao.fineix.qingjingOrSceneDetails.adapter.QJDetailsTopAdapter;
import com.taihuoniao.fineix.qingjingOrSceneDetails.bean.SceneDetailsBean2;
import com.taihuoniao.fineix.qingjingOrSceneDetails.bean.SceneListBean2;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.LogUtil;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.utils.WindowUtils;
import com.taihuoniao.fineix.view.GlobalTitleLayout;
import com.taihuoniao.fineix.view.GridViewForScrollView;
import com.taihuoniao.fineix.view.ListViewForScrollView;
import com.taihuoniao.fineix.view.pulltorefresh.PullToRefreshBase;
import com.taihuoniao.fineix.view.pulltorefresh.PullToRefreshListView;

import java.lang.reflect.Type;
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
    private List<SceneList.DataBean.RowsBean> sceneList;
    private IndexQJListAdapter indexQJListAdapter;

    // 情境详情
    private List<SceneList.DataBean.RowsBean> sceneList2;
    private QJDetailsTopAdapter indexQJListAdapter2;

    // 情境详情下产品列表
    private QJDetailsProductAdapter mQjDetailsProductAdapter;

    //可能喜欢的产品
    private AddProductGridAdapter indexAdapter002;//主题列表适配器
    private List<ProductBean.ProductListItem> productList;
    private List<SearchBean.Data.SearchItem> searchList;

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
        // TODO: add setContentView(...) invocation
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
                    LogUtil.d("TAG", bean.toString());
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
            // TODO: 2017/3/2 加载数据

            if (currentPage == 1) {
                sceneList.clear();
                pullRefreshView.lastTotalItem = -1;
                pullRefreshView.lastSavedFirstVisibleItem = -1;
            }
            List<SceneListBean2.RowsEntity> rows = bean.getRows();
            List<SceneList.DataBean.RowsBean> rowsBeen = newListConvertOldList(rows);
            sceneList.addAll(rowsBeen);
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
        List<IndexUserListBean.DataBean.UsersBean> userList = new ArrayList<>();
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

        // TODO: 2017/3/2 情境详情
        List<SceneList.DataBean.RowsBean> rowsBeen = newListConvertOldList2(bean2);
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
     * 新数据格式转换成旧的数据格式
     * @param newRowEntitys  newRowEntitys
     * @return oleRowEntitys
     */
    private List<SceneList.DataBean.RowsBean> newListConvertOldList(List<SceneListBean2.RowsEntity> newRowEntitys){
        if (newRowEntitys == null) {
            return null;
        }

        List<SceneList.DataBean.RowsBean> oldRowEntitys = new ArrayList<>();
        for(int i = 0; i < newRowEntitys.size(); i ++) {
            SceneListBean2.RowsEntity newRowBean = newRowEntitys.get(i);
            SceneList.DataBean.RowsBean oldRowBean = new SceneList.DataBean.RowsBean();
            oldRowBean.set_id(newRowBean.get_id());
            oldRowBean.setAddress(newRowBean.getAddress());
            oldRowBean.setCategory_id(newRowBean.getCategory_id());
            oldRowBean.setCity(newRowBean.getCity());
            oldRowBean.setUser_id(newRowBean.getUser_id());
            oldRowBean.setTitle(newRowBean.getTitle());
            oldRowBean.setDes(newRowBean.getDes());
            oldRowBean.setCategory_ids(newRowBean.getCategory_ids());
            oldRowBean.setScene_id(newRowBean.getScene_id());
            oldRowBean.setUsed_count(newRowBean.getUsed_count());

            oldRowBean.setView_count(newRowBean.getView_count());
            oldRowBean.setLove_count(newRowBean.getLove_count());
            oldRowBean.setComment_count(Integer.valueOf(newRowBean.getComment_count()));
            oldRowBean.setStick(Integer.valueOf(newRowBean.getStick()));
            oldRowBean.setFine(Integer.valueOf(newRowBean.getFine()));
            oldRowBean.setIs_check(Integer.valueOf(newRowBean.getIs_check()));
            oldRowBean.setStatus(newRowBean.getStatus());
            oldRowBean.setDeleted(newRowBean.getDeleted());
            oldRowBean.setCreated_on(Long.valueOf(newRowBean.getCreated_on()));
            oldRowBean.setUpdated_on(newRowBean.getUpdated_on());

            oldRowBean.setTags_s(newRowBean.getTags_s());
            oldRowBean.setCover_url(newRowBean.getCover_url());
            oldRowBean.setCreated_at(newRowBean.getCreated_at());
            oldRowBean.setIs_love(Integer.valueOf(newRowBean.getIs_love()));
            oldRowBean.setIs_favorite(Integer.valueOf(newRowBean.getIs_favorite()));

            oldRowBean.setTags(newRowBean.getTags());

            SceneList.DataBean.RowsBean.LocationBean oldLocationBean = new SceneList.DataBean.RowsBean.LocationBean();
            SceneListBean2.RowsEntity.LocationEntity location = newRowBean.getLocation();
            oldLocationBean.setType(location.getType());

            final List<String> coordinates = location.getCoordinates();
            List<Double> co = new ArrayList<>();
            for(int j = 0 ; j < coordinates.size(); j ++) {
                co.add(Double.valueOf(coordinates.get(j)));
            }
            oldLocationBean.setCoordinates(co);
            oldRowBean.setLocation(oldLocationBean);

            SceneList.DataBean.RowsBean.UserInfoBean oldUserINfoBean = new SceneList.DataBean.RowsBean.UserInfoBean();
            SceneListBean2.RowsEntity.UserInfoEntity user_info = newRowBean.getUser_info();

            oldUserINfoBean.setUser_id(user_info.getUser_id());
            oldUserINfoBean.setNickname(user_info.getNickname());
            oldUserINfoBean.setAvatar_url(user_info.getAvatar_url());
            oldUserINfoBean.setSummary(user_info.getSummary());
            oldUserINfoBean.setFollow_count(user_info.getFollow_count());
            oldUserINfoBean.setFans_count(user_info.getFans_count());
            oldUserINfoBean.setLove_count(user_info.getLove_count());
            oldUserINfoBean.setIs_expert(Integer.valueOf(user_info.getIs_expert()));
            oldUserINfoBean.setIs_follow(Integer.valueOf(user_info.getIs_follow()));
            oldUserINfoBean.setLabel(user_info.getLabel());
            oldUserINfoBean.setExpert_label(user_info.getExpert_label());
            oldUserINfoBean.setExpert_info(user_info.getExpert_info());

            SceneList.DataBean.RowsBean.UserInfoBean.CounterBean oldCounterBean = new SceneList.DataBean.RowsBean.UserInfoBean.CounterBean();
            oldCounterBean.setMessage_count(user_info.getCounter().getMessage_count());
            oldCounterBean.setNotice_count(user_info.getCounter().getNotice_count());
            oldCounterBean.setAlert_count(user_info.getCounter().getAlert_count());
            oldCounterBean.setFans_count(user_info.getCounter().getFans_count());
            oldCounterBean.setComment_count(user_info.getCounter().getComment_count());
            oldCounterBean.setPeople_count(user_info.getCounter().getPeople_count());
            oldCounterBean.setOrder_wait_payment(user_info.getCounter().getOrder_wait_payment());
            oldCounterBean.setOrder_ready_goods(user_info.getCounter().getOrder_ready_goods());
            oldCounterBean.setOrder_sended_goods(user_info.getCounter().getOrder_sended_goods());
            oldCounterBean.setOrder_evaluate(user_info.getCounter().getOrder_evaluate());
            oldCounterBean.setFiu_comment_count(user_info.getCounter().getFiu_comment_count());
            oldCounterBean.setFiu_alert_count(user_info.getCounter().getFiu_alert_count());
            oldCounterBean.setFiu_notice_count(user_info.getCounter().getFiu_notice_count());
            oldUserINfoBean.setCounter(oldCounterBean);
            oldRowBean.setUser_info(oldUserINfoBean);
            oldRowBean.setCategory_ids(newRowBean.getCategory_ids());

            List<SceneList.DataBean.RowsBean.ProductBean> oldProductBeans = new ArrayList<>();
            List<SceneListBean2.RowsEntity.ProductEntity> newProduct = newRowBean.getProduct();
            for(int k = 0 ; k < newProduct.size(); k ++) {
                SceneList.DataBean.RowsBean.ProductBean oldProductBean = new SceneList.DataBean.RowsBean.ProductBean();
                oldProductBean.setId(newProduct.get(k).getId());
                oldProductBean.setLoc(Integer.valueOf(newProduct.get(k).getLoc()));
                oldProductBean.setTitle(newProduct.get(k).getTitle());
                oldProductBean.setX(newProduct.get(k).getX());
                oldProductBean.setY(newProduct.get(k).getY());
                oldProductBeans.add(oldProductBean);
            }
            oldRowBean.setProduct(oldProductBeans);

            List<SceneList.DataBean.RowsBean.CommentsBean> oldCommentBeans = new ArrayList<>();
            SceneList.DataBean.RowsBean.CommentsBean oldCommentBean = new SceneList.DataBean.RowsBean.CommentsBean();
            List<SceneListBean2.RowsEntity.CommentsEntity> comments = newRowBean.getComments();
            for(int m = 0 ; m < comments.size(); m++) {
                oldCommentBean.set_id(comments.get(i).get_id());
                oldCommentBean.setContent(comments.get(i).getContent());
                oldCommentBean.setUser_avatar_url(comments.get(i).getUser_avatar_url());
                oldCommentBean.setUser_id(comments.get(i).getUser_id());
                oldCommentBean.setUser_nickname(comments.get(i).getUser_nickname());
                oldCommentBeans.add(oldCommentBean);
            }
            oldRowBean.setComments(oldCommentBeans);
            oldRowEntitys.add(oldRowBean);
        }
        return oldRowEntitys;
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
                ProductBean productBean = new ProductBean();
                try {
                    Gson gson = new Gson();
                    Type type = new TypeToken<ProductBean>() {
                    }.getType();
                    productBean = gson.fromJson(json, type);
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                }

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

    /**
     * 新数据格式转换成旧的数据格式2
     * @param newRowEntitys  newRowEntitys
     * @return oleRowEntitys
     */
    private List<SceneList.DataBean.RowsBean> newListConvertOldList2(SceneDetailsBean2 newRowEntitys){
        if (newRowEntitys == null) {
            return null;
        }

        List<SceneList.DataBean.RowsBean> oldRowEntitys = new ArrayList<>();
        for(int i = 0; i < 1; i ++) {
//            SceneListBean2.RowsEntity newRowBean = newRowEntitys.get(i);
            SceneList.DataBean.RowsBean oldRowBean = new SceneList.DataBean.RowsBean();
            oldRowBean.set_id(newRowEntitys.get_id());
            oldRowBean.setAddress(newRowEntitys.getAddress());
            oldRowBean.setCategory_id(newRowEntitys.getCategory_id());
            oldRowBean.setCity(newRowEntitys.getCity());
            oldRowBean.setUser_id(newRowEntitys.getUser_id());
            oldRowBean.setTitle(newRowEntitys.getTitle());
            oldRowBean.setDes(newRowEntitys.getDes());
            oldRowBean.setCategory_ids(newRowEntitys.getCategory_ids());
            oldRowBean.setScene_id(newRowEntitys.getScene_id());
            oldRowBean.setUsed_count(newRowEntitys.getUsed_count());

            oldRowBean.setView_count(newRowEntitys.getView_count());
            oldRowBean.setLove_count(newRowEntitys.getLove_count());
            oldRowBean.setComment_count(Integer.valueOf(newRowEntitys.getComment_count()));
            oldRowBean.setStick(Integer.valueOf(newRowEntitys.getStick()));
            oldRowBean.setFine(Integer.valueOf(newRowEntitys.getFine()));
            oldRowBean.setIs_check(Integer.valueOf(newRowEntitys.getIs_check()));
            oldRowBean.setStatus(newRowEntitys.getStatus());
            oldRowBean.setDeleted(newRowEntitys.getDeleted());
            oldRowBean.setCreated_on(Long.valueOf(newRowEntitys.getCreated_on()));
            oldRowBean.setUpdated_on(newRowEntitys.getUpdated_on());

            oldRowBean.setTags_s(newRowEntitys.getTags_s());
            oldRowBean.setCover_url(newRowEntitys.getCover_url());
            oldRowBean.setCreated_at(newRowEntitys.getCreated_at());
            oldRowBean.setIs_love(Integer.valueOf(newRowEntitys.getIs_love()));
            oldRowBean.setIs_favorite(Integer.valueOf(newRowEntitys.getIs_favorite()));

            oldRowBean.setTags(newRowEntitys.getTags());

            SceneList.DataBean.RowsBean.LocationBean oldLocationBean = new SceneList.DataBean.RowsBean.LocationBean();
            SceneDetailsBean2.LocationEntity location = newRowEntitys.getLocation();
            oldLocationBean.setType(location.getType());

            final List<String> coordinates = location.getCoordinates();
            List<Double> co = new ArrayList<>();
            for(int j = 0 ; j < coordinates.size(); j ++) {
                co.add(Double.valueOf(coordinates.get(j)));
            }
            oldLocationBean.setCoordinates(co);
            oldRowBean.setLocation(oldLocationBean);

            SceneList.DataBean.RowsBean.UserInfoBean oldUserINfoBean = new SceneList.DataBean.RowsBean.UserInfoBean();
            SceneDetailsBean2.UserInfoEntity user_info = newRowEntitys.getUser_info();

            oldUserINfoBean.setUser_id(user_info.getUser_id());
            oldUserINfoBean.setNickname(user_info.getNickname());
            oldUserINfoBean.setAvatar_url(user_info.getAvatar_url());
            oldUserINfoBean.setSummary(user_info.getSummary());
            oldUserINfoBean.setFollow_count(user_info.getFollow_count());
            oldUserINfoBean.setFans_count(user_info.getFans_count());
            oldUserINfoBean.setLove_count(user_info.getLove_count());
            oldUserINfoBean.setIs_expert(Integer.valueOf(user_info.getIs_expert()));
            oldUserINfoBean.setIs_follow(Integer.valueOf(user_info.getIs_follow()));
            oldUserINfoBean.setLabel(user_info.getLabel());
            oldUserINfoBean.setExpert_label(user_info.getExpert_label());
            oldUserINfoBean.setExpert_info(user_info.getExpert_info());

            SceneList.DataBean.RowsBean.UserInfoBean.CounterBean oldCounterBean = new SceneList.DataBean.RowsBean.UserInfoBean.CounterBean();
            oldCounterBean.setMessage_count(user_info.getCounter().getMessage_count());
            oldCounterBean.setNotice_count(user_info.getCounter().getNotice_count());
            oldCounterBean.setAlert_count(user_info.getCounter().getAlert_count());
            oldCounterBean.setFans_count(user_info.getCounter().getFans_count());
            oldCounterBean.setComment_count(user_info.getCounter().getComment_count());
            oldCounterBean.setPeople_count(user_info.getCounter().getPeople_count());
            oldCounterBean.setOrder_wait_payment(user_info.getCounter().getOrder_wait_payment());
            oldCounterBean.setOrder_ready_goods(user_info.getCounter().getOrder_ready_goods());
            oldCounterBean.setOrder_sended_goods(user_info.getCounter().getOrder_sended_goods());
            oldCounterBean.setOrder_evaluate(user_info.getCounter().getOrder_evaluate());
            oldCounterBean.setFiu_comment_count(user_info.getCounter().getFiu_comment_count());
            oldCounterBean.setFiu_alert_count(user_info.getCounter().getFiu_alert_count());
            oldCounterBean.setFiu_notice_count(user_info.getCounter().getFiu_notice_count());
            oldUserINfoBean.setCounter(oldCounterBean);
            oldRowBean.setUser_info(oldUserINfoBean);
            oldRowBean.setCategory_ids(newRowEntitys.getCategory_ids());

            List<SceneList.DataBean.RowsBean.ProductBean> oldProductBeans = new ArrayList<>();
            List<SceneDetailsBean2.ProductEntity> newProduct = newRowEntitys.getProduct();
            for(int k = 0 ; k < newProduct.size(); k ++) {
                SceneList.DataBean.RowsBean.ProductBean oldProductBean = new SceneList.DataBean.RowsBean.ProductBean();
                oldProductBean.setId(newProduct.get(k).getId());

                try {
                    oldProductBean.setLoc(Integer.valueOf(newProduct.get(k).getLoc()));
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                oldProductBean.setTitle(newProduct.get(k).getTitle());
                oldProductBean.setX(Double.valueOf(newProduct.get(k).getX()));
                oldProductBean.setY(Double.valueOf(newProduct.get(k).getY()));
                oldProductBeans.add(oldProductBean);
            }
            oldRowBean.setProduct(oldProductBeans);

            List<SceneList.DataBean.RowsBean.CommentsBean> oldCommentBeans = new ArrayList<>();
            SceneList.DataBean.RowsBean.CommentsBean oldCommentBean = new SceneList.DataBean.RowsBean.CommentsBean();
            List<SceneDetailsBean2.CommentsEntity> comments = newRowEntitys.getComments();
            for(int m = 0 ; m < comments.size(); m++) {
                oldCommentBean.set_id(comments.get(i).get_id());
                oldCommentBean.setContent(comments.get(i).getContent());
                oldCommentBean.setUser_avatar_url(comments.get(i).getUser_avatar_url());
                oldCommentBean.setUser_id(comments.get(i).getUser_id());
                oldCommentBean.setUser_nickname(comments.get(i).getUser_nickname());
                oldCommentBeans.add(oldCommentBean);
            }
            oldRowBean.setComments(oldCommentBeans);
            oldRowEntitys.add(oldRowBean);
        }
        return oldRowEntitys;
    }
}
