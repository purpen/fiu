package com.taihuoniao.fineix.qingjingOrSceneDetails;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.baidu.location.BDLocation;
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
import com.taihuoniao.fineix.beans.QingJingListBean;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.utils.DensityUtils;
import com.taihuoniao.fineix.utils.MapUtil;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.view.WaittingDialog;
import com.taihuoniao.fineix.view.pulltorefresh.PullToRefreshBase;
import com.taihuoniao.fineix.view.pulltorefresh.PullToRefreshGridView;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by taihuoniao on 2016/4/25.
 */
public class AllQingjingActivity extends BaseActivity implements AdapterView.OnItemClickListener, View.OnClickListener, EditRecyclerAdapter.ItemClick {
    private ImageView searchQJImg;
    private ImageView createQingjingImg;
    private LinearLayout searchLinear;
    private RecyclerView recyclerView;
    private PullToRefreshGridView pullToRefreshView;
    private GridView qingjingGrid;
    private List<QingJingListBean.QingJingItem> qingjingList;
    private RelativeLayout firstRelative;//第一次进入app会用到
    //适配器
    private AllQingjingGridAdapter allQingjingGridAdapter;
    private ProgressBar progressBar;
    //情景列表
    private double[] location = null;
    private int page = 1;//页码
    private double distance = 5000;//列表范围
    //网络请求对话框
    private WaittingDialog dialog;
    private List<CategoryListBean.CategoryListItem> dipanList;
    private DipanCategoryAdapter dipanCategoryAdapter;

    public AllQingjingActivity() {
        super(0);
    }

    @Override
    protected void initView() {
//        View activity_view = View.inflate(AllQingjingActivity.this, R.layout.activity_all_qingjing, null);
        setContentView(R.layout.activity_all_qingjing);
        searchQJImg = (ImageView) findViewById(R.id.activity_all_qingjing_search);
        createQingjingImg = (ImageView) findViewById(R.id.activity_all_qingjing_createqinjing);
        searchLinear = (LinearLayout) findViewById(R.id.search_linear);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        pullToRefreshView = (PullToRefreshGridView) findViewById(R.id.activity_all_qingjing_pullrefreshview);
        qingjingGrid = pullToRefreshView.getRefreshableView();
        progressBar = (ProgressBar) findViewById(R.id.activity_all_qingjing_progress);
        firstRelative = (RelativeLayout) findViewById(R.id.activity_all_qingjing_first);
        dialog = new WaittingDialog(AllQingjingActivity.this);
    }

    @Override
    protected void initList() {
        searchQJImg.setOnClickListener(this);
        createQingjingImg.setOnClickListener(this);
        searchLinear.setOnClickListener(this);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        //地盘分类
        dipanList = new ArrayList<>();
        dipanCategoryAdapter = new DipanCategoryAdapter(this, dipanList, this);
        recyclerView.setAdapter(dipanCategoryAdapter);
        pullToRefreshView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (location == null) {
                    getCurrentLocation();
                    return;
                }
                if (!dialog.isShowing()) {
                    dialog.show();
                }
                page = 1;
                getQJList(page + "", 0 + "", 0 + "", distance + "", location[0] + "", location[1] + "");
            }
        });
        pullToRefreshView.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {
            @Override
            public void onLastItemVisible() {
                if (location == null) {
                    getCurrentLocation();
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                page++;
                getQJList(page + "", 0 + "", 0 + "", distance + "", location[0] + "", location[1] + "");
            }
        });
        qingjingGrid.setNumColumns(2);
        int space = DensityUtils.dp2px(AllQingjingActivity.this, 5);
        qingjingGrid.setHorizontalSpacing(0);
        qingjingGrid.setVerticalSpacing(space);
        qingjingList = new ArrayList<>();
        allQingjingGridAdapter = new AllQingjingGridAdapter(qingjingList, null, AllQingjingActivity.this, space);
        qingjingGrid.setAdapter(allQingjingGridAdapter);
        qingjingGrid.setOnItemClickListener(this);
        getCurrentLocation();
        categoryList(1 + "", 12 + "", 1 + "");
    }

    private void getCurrentLocation() {
        MapUtil.getCurrentLocation(new MapUtil.OnReceiveLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                if (location == null && bdLocation != null) {
                    if (!dialog.isShowing()) {
                        dialog.show();
                    }
                    location = new double[]{bdLocation.getLongitude(), bdLocation.getLatitude()};
                    MapUtil.destroyLocationClient();
                    getQJList(page + "", 0 + "", 0 + "", distance + "", location[0] + "", location[1] + "");
                }
            }
        });
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(AllQingjingActivity.this, QingjingDetailActivity.class);
        intent.putExtra("id", qingjingList.get(position).get_id());
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search_linear:
                Intent intent1 = new Intent(this, SearchActivity.class);
                intent1.putExtra("t", "8");
                startActivity(intent1);
                break;
            case R.id.activity_all_qingjing_search:
                onBackPressed();
                break;
            case R.id.activity_all_qingjing_createqinjing:
//                ToastUtils.showError("创建地盘暂不开放");
                break;
        }
    }

//    @Override
//    public void onWindowFocusChanged(boolean hasFocus) {
//        super.onWindowFocusChanged(hasFocus);
//        if (hasFocus) {
//            SharedPreferences firstInSp = getSharedPreferences(DataConstants.SHAREDPREFRENCES_FIRST_IN, Context.MODE_PRIVATE);
//            //判断是不是第一次进入Fiu界面
//            boolean isFirstIn = firstInSp.getBoolean(DataConstants.FIRST_IN_ALL, true);
//            if (isFirstIn) {
//                firstRelative.setVisibility(View.VISIBLE);
//                firstRelative.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        firstRelative.setVisibility(View.GONE);
//                    }
//                });
//                SharedPreferences.Editor editor = firstInSp.edit();
//                editor.putBoolean(DataConstants.FIRST_IN_ALL, false);
//                editor.apply();
//            }
//        }
//    }

    @Override
    public void click(int postion) {
//        dialog.show();
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
        getQJList(page + "", 0 + "", 0 + "", distance + "", null, null);
    }

    private String category_id = null;

    private void getQJList(String pa, String sort, String fine, String dis, String lng, String lat) {
        if (!dialog.isShowing()) {
            dialog.show();
        }
        ClientDiscoverAPI.qingjingList(pa, category_id, sort, fine, dis, null, null, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                dialog.dismiss();
                QingJingListBean netQingjing = new QingJingListBean();
                try {
                    Gson gson = new Gson();
                    Type type = new TypeToken<QingJingListBean>() {
                    }.getType();
                    netQingjing = gson.fromJson(responseInfo.result, type);
                } catch (JsonSyntaxException e) {
                    Log.e("<<<", "数据异常：" + e.toString());
                }
//                handler.sendMessage(msg);
                progressBar.setVisibility(View.GONE);
                pullToRefreshView.onRefreshComplete();
                if (netQingjing.isSuccess()) {
                    if (page == 1) {
                        qingjingList.clear();
                        pullToRefreshView.lastTotalItem = -1;
                        pullToRefreshView.lastSavedFirstVisibleItem = -1;
                    }
                    qingjingList.addAll(netQingjing.getData().getRows());
                    pullToRefreshView.setLoadingTime();
                    allQingjingGridAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                dialog.dismiss();
                progressBar.setVisibility(View.GONE);
                pullToRefreshView.onRefreshComplete();
                ToastUtils.showError("网络错误");
            }
        });
    }

    //地盘分类列表
    private void categoryList(String page, String domain, String show_all) {
        if (!dialog.isShowing()) {
            dialog.show();
        }
        ClientDiscoverAPI.categoryList(page, domain, show_all, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
//                Log.e("<<<分类列表", responseInfo.result);
//                dialog.dismiss();
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
                    click(0);
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
