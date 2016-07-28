package com.taihuoniao.fineix.scene;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.baidu.mapapi.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.AllQingjingGridAdapter;
import com.taihuoniao.fineix.adapters.DipanCategoryAdapter;
import com.taihuoniao.fineix.adapters.EditRecyclerAdapter;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.beans.CategoryListBean;
import com.taihuoniao.fineix.beans.QingJingItem;
import com.taihuoniao.fineix.beans.QingJingListBean;
import com.taihuoniao.fineix.beans.SearchBean;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.DataConstants;
import com.taihuoniao.fineix.utils.DensityUtils;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.view.GlobalTitleLayout;
import com.taihuoniao.fineix.view.WaittingDialog;
import com.taihuoniao.fineix.view.pulltorefresh.PullToRefreshBase;
import com.taihuoniao.fineix.view.pulltorefresh.PullToRefreshGridView;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by taihuoniao on 2016/5/9.
 */
public class SelectAllQingjingActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener, EditRecyclerAdapter.ItemClick {
    //上个界面传递过来的数据
    private String isSearch;//判断是搜索还是展示 0展示 1搜索
    private LatLng latLng;//当前位置经纬度
    private int pos;
    @Bind(R.id.activity_select_allqj_titlelayout)
    GlobalTitleLayout titleLayout;
    @Bind(R.id.activity_select_allqj_searchlinear)
    LinearLayout searchLinear;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.activity_select_allqj_pullrefreshview)
    PullToRefreshGridView pullToRefreshView;
    GridView gridView;
    @Bind(R.id.activity_select_allqj_progress)
    ProgressBar progressBar;
    private WaittingDialog dialog;
    //地盘分类
    private String category_id = null;
    private List<CategoryListBean.CategoryListItem> dipanList;
    private DipanCategoryAdapter dipanCategoryAdapter;
    //情景列表
    private int page = 1;
    private double distance = 5000;//搜索范围
    private List<QingJingListBean.QingJingItem> qingjingList;
    private List<SearchBean.SearchItem> searchList;
    private AllQingjingGridAdapter allQingjingGridAdapter;
    //搜索情景
    private String q;

    @Override
    protected void getIntentData() {
        isSearch = getIntent().getStringExtra("isSearch");
        latLng = getIntent().getParcelableExtra("latLng");
        pos = getIntent().getIntExtra("pos",0);
        if (isSearch == null) {
            isSearch = "0";
        }
//        if (latLng == null) {
//            ToastUtils.showError("无法获得您当前位置信息");
//            finish();
//        }
    }

    public SelectAllQingjingActivity() {
        super(R.layout.activity_select_allqj);
    }

    @Override
    protected void initView() {
        titleLayout.setBackgroundResource(R.color.white);
        titleLayout.setTitle(R.string.select_qingjing, getResources().getColor(R.color.black333333));
        titleLayout.setBackImg(R.mipmap.back_black);
        titleLayout.setRightTv(R.string.confirm, getResources().getColor(R.color.black333333), this);
        searchLinear.setVisibility(View.VISIBLE);
        searchLinear.setOnClickListener(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        //地盘分类
        dipanList = new ArrayList<>();
        dipanCategoryAdapter = new DipanCategoryAdapter(this, dipanList, this);
        recyclerView.setAdapter(dipanCategoryAdapter);
        gridView = pullToRefreshView.getRefreshableView();
        pullToRefreshView.setPullToRefreshEnabled(false);
        pullToRefreshView.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {
            @Override
            public void onLastItemVisible() {
                progressBar.setVisibility(View.VISIBLE);
                page++;
                if (isSearch.equals("0")) {
                    qingjingList(page + "", category_id, 1 + "", 0 + "", distance + "", null, null);
                } else if (isSearch.equals("1")) {
                    search(q, 8 + "", page + "", "tag", null);
                }
            }
        });
        gridView.setNumColumns(2);
        int space = DensityUtils.dp2px(SelectAllQingjingActivity.this, 5);
//        gridView.setHorizontalSpacing(space);
        gridView.setVerticalSpacing(space);
        if (isSearch.equals("0")) {
            qingjingList = new ArrayList<>();
            allQingjingGridAdapter = new AllQingjingGridAdapter(qingjingList, null, SelectAllQingjingActivity.this, space);
        } else if (isSearch.equals("1")) {
            searchList = new ArrayList<>();
            allQingjingGridAdapter = new AllQingjingGridAdapter(null, searchList, SelectAllQingjingActivity.this, space);
        }
        gridView.setAdapter(allQingjingGridAdapter);
        gridView.setOnItemClickListener(this);
        dialog = new WaittingDialog(SelectAllQingjingActivity.this);
    }

    @Override
    protected void requestNet() {
        if (!dialog.isShowing()) {
            dialog.show();
        }
        if (isSearch.equals("0")) {
            qingjingList(page + "", category_id, 1 + "", 0 + "", distance + "", null, null);
        }
        categoryList(1 + "", 12 + "", true + "");
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_select_allqj_searchlinear:
                Intent intent2 = new Intent(SelectAllQingjingActivity.this, SelectSearchQingjingActivity.class);
                intent2.putExtra("isSearch", "1");
                startActivityForResult(intent2, DataConstants.REQUESTCODE_SELECTQJ_ALLQJ);
                break;
            case R.id.activity_select_allqj_cancel:
//                editText.setText("");
                break;
            case R.id.title_continue:
                if (isSearch.equals("0")) {
                    for (int i = 0; i < qingjingList.size(); i++) {
                        if (qingjingList.get(i).isSelect()) {
                            Intent intent1 = new Intent();
                            intent1.putExtra("qingjing", qingjingList.get(i));
                            setResult(DataConstants.RESULTCODE_SELECTQJ_ALLQJ, intent1);
                            finish();
                            break;
                        }
                    }
                    onBackPressed();
                } else if (isSearch.equals("1")) {
                    for (int i = 0; i < searchList.size(); i++) {
                        if (searchList.get(i).isSelect()) {
                            Intent intent1 = new Intent();
                            intent1.putExtra("searchqj", searchList.get(i));
                            setResult(DataConstants.RESULTCODE_SELECTQJ_SALLQJ, intent1);
                            finish();
                            break;
                        }
                    }
                }
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (isSearch.equals("0")) {
            for (int i = 0; i < qingjingList.size(); i++) {
                if (position == i) {
                    qingjingList.get(i).setIsSelect(true);
                } else {
                    qingjingList.get(i).setIsSelect(false);
                }
            }
        } else if (isSearch.equals("1")) {
            for (int i = 0; i < searchList.size(); i++) {
                if (position == i) {
                    searchList.get(i).setIsSelect(true);
                } else {
                    searchList.get(i).setIsSelect(false);
                }
            }
        }
        allQingjingGridAdapter.notifyDataSetChanged();
    }

    @Override
    public void click(int postion) {
        if (!dialog.isShowing()) {
            dialog.show();
        }
        for (int i = 0; i < dipanList.size(); i++) {
            if (i == postion) {
                dipanList.get(i).setIsSelect(true);
            } else {
                dipanList.get(i).setIsSelect(false);
            }
        }
        dipanCategoryAdapter.notifyDataSetChanged();
        page = 1;
        category_id = dipanList.get(postion).get_id();
        qingjingList(page + "", category_id, 1 + "", 0 + "", distance + "", null, null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            switch (resultCode) {
                case DataConstants.RESULTCODE_MAP:
                    QingJingItem qingJing = (QingJingItem) data.getSerializableExtra("qingjing");
                    if (qingJing != null) {
                        Intent intent1 = new Intent();
                        intent1.putExtra("qingjing", qingJing);
                        setResult(DataConstants.RESULTCODE_MAP_SELECTQJ, intent1);
                        finish();
                    }
                    break;
                case DataConstants.RESULTCODE_SELECTQJ_ALLQJ:
                    QingJingListBean.QingJingItem qingJingItem = (QingJingListBean.QingJingItem) data.getSerializableExtra("qingjing");
                    if (qingJingItem != null) {
                        Intent intent = new Intent();
                        intent.putExtra("qingjing", qingJingItem);
                        setResult(DataConstants.RESULTCODE_CREATESCENE_SELECTQJ, intent);
                        finish();
                    }
                    break;
                case DataConstants.RESULTCODE_SELECTQJ_SALLQJ:
                    SearchBean.SearchItem searchItem = (SearchBean.SearchItem) data.getSerializableExtra("searchqj");
                    if (searchItem != null) {
                        Intent intent = new Intent();
//                        Log.e("<<<>>>", searchItem.getTitle());
                        intent.putExtra("searchqj", searchItem);
                        setResult(DataConstants.RESULTCODE_CREATESCENE_SEARCHQJ, intent);
                        finish();
                    }
                    break;
            }
        }
    }

    //搜索
    private void search(String q, String t, String p, String evt, String sort) {
        ClientDiscoverAPI.search(q, t, null, p, evt, sort, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                SearchBean searchBean = new SearchBean();
                try {
                    Gson gson = new Gson();
                    Type type = new TypeToken<SearchBean>() {
                    }.getType();
                    searchBean = gson.fromJson(responseInfo.result, type);
                } catch (JsonSyntaxException e) {
                    Log.e("<<<", "数据解析异常" + e.toString());
                }
                dialog.dismiss();
                progressBar.setVisibility(View.GONE);
                SearchBean netSearch = searchBean;
                if (netSearch.isSuccess()) {
                    if (page == 1) {
                        searchList.clear();
                        pullToRefreshView.lastTotalItem = -1;
                        pullToRefreshView.lastSavedFirstVisibleItem = -1;
                    }
                    searchList.addAll(netSearch.getData().getRows());
                    allQingjingGridAdapter.notifyDataSetChanged();
                } else {
                    ToastUtils.showError(netSearch.getMessage());
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                dialog.dismiss();
                progressBar.setVisibility(View.GONE);
                ToastUtils.showError("网络错误");
            }
        });
    }

    //情景列表
    private void qingjingList(String p, String category_id, String sort, String fine, String dis, String lng, String lat) {
        ClientDiscoverAPI.qingjingList(p, category_id, sort, fine, dis, lng, lat, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                QingJingListBean qingJingListBean = new QingJingListBean();
                try {
                    Gson gson = new Gson();
                    Type type = new TypeToken<QingJingListBean>() {
                    }.getType();
                    qingJingListBean = gson.fromJson(responseInfo.result, type);
                } catch (JsonSyntaxException e) {
                    Log.e("<<<", "数据异常：" + e.toString());
                }
                dialog.dismiss();
                progressBar.setVisibility(View.GONE);
                QingJingListBean netQingjingListBean = qingJingListBean;
                if (netQingjingListBean.isSuccess()) {
                    if (page == 1) {
                        qingjingList.clear();
                        pullToRefreshView.lastTotalItem = -1;
                        pullToRefreshView.lastSavedFirstVisibleItem = -1;
                    }
//                    Log.e("<<<情景列表", netQingjingListBean.getData().getRows().toString());
                    qingjingList.addAll(netQingjingListBean.getData().getRows());
                    allQingjingGridAdapter.notifyDataSetChanged();
                } else {
                    ToastUtils.showError(netQingjingListBean.getMessage());
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                dialog.dismiss();
                progressBar.setVisibility(View.GONE);
                ToastUtils.showError("网络错误");
            }
        });
    }

    //地盘分类列表
    private void categoryList(String page, String domain, String show_all) {
        ClientDiscoverAPI.categoryList(page, domain, show_all, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
//                Log.e("<<<分类列表", responseInfo.result);
                dialog.dismiss();
                CategoryListBean categoryListBean = new CategoryListBean();
                try {
                    Gson gson = new Gson();
                    Type type = new TypeToken<CategoryListBean>() {
                    }.getType();
                    categoryListBean = gson.fromJson(responseInfo.result, type);
                } catch (JsonSyntaxException e) {
                    Log.e("<<<分类列表", "数据解析异常" + e.toString());
                }
                if (categoryListBean.isSuccess()) {
                    dipanList.addAll(categoryListBean.getData().getRows());
                    dipanCategoryAdapter.notifyDataSetChanged();
                    click(pos);
                } else {
                    ToastUtils.showError(categoryListBean.getMessage());
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                dialog.dismiss();
                ToastUtils.showError(R.string.net_fail);
            }
        });
    }


}
