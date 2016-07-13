package com.taihuoniao.fineix.scene;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.AllQingjingGridAdapter;
import com.taihuoniao.fineix.adapters.EditRecyclerAdapter;
import com.taihuoniao.fineix.adapters.JingQingjingRecyclerAdapter;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.beans.QingJingItem;
import com.taihuoniao.fineix.beans.QingJingListBean;
import com.taihuoniao.fineix.beans.SearchBean;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.DataConstants;
import com.taihuoniao.fineix.utils.DensityUtils;
import com.taihuoniao.fineix.utils.MapUtil;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.view.GlobalTitleLayout;
import com.taihuoniao.fineix.view.GridViewWithHeaderAndFooter;
import com.taihuoniao.fineix.view.WaittingDialog;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by taihuoniao on 2016/4/28.
 */
public class SelectQingjingActivity extends BaseActivity<QingJingItem> implements View.OnClickListener, AdapterView.OnItemClickListener, AbsListView.OnScrollListener, EditRecyclerAdapter.ItemClick {
    private LatLng ll;
    @Bind(R.id.activity_select_qingjing_titlelayout)
    GlobalTitleLayout titleLayout;
    private LinearLayout searchLinear;
    private RelativeLayout mapRelative;
    //    private MapView mapView;
    private TextView nearTv;
    private RecyclerView qingjingRecycler;
    //    private ListViewForScrollView nearListView;
    private TextView qingjingTv;
    private TextView allQingjingTv;
    @Bind(R.id.activity_select_qingjing_grid)
    GridViewWithHeaderAndFooter qingjingGrid;
    @Bind(R.id.activity_select_qingjing_progress)
    ProgressBar progressBar;
    //地图
//    private BaiduMap mBDMap;
    private boolean isFirstLoc = true;
    private int mapPage; //默认查看第一页
    private int pageSize;//本界面只展示三条
    private int radius = 5000; //搜索半径
    private static final String STICK_ALL = "0"; //所有情境
    //    private GridView qingjingGrid;
    private WaittingDialog dialog;
    //情景列表
    private int page = 1;
    private double distance = 5000;
    private List<QingJingListBean.QingJingItem> qingjingList;
    private AllQingjingGridAdapter allQingjingGridAdapter;
    private int lastSavedFirstVisibleItem = -1;
    private int lastTotalItem = -1;
    private JingQingjingRecyclerAdapter jingQingjingRecyclerAdapter;

    public SelectQingjingActivity() {
        super(R.layout.activity_select_qingjing);
    }

    @Override
    protected void initView() {
        dialog = new WaittingDialog(SelectQingjingActivity.this);
        titleLayout.setBackgroundResource(R.color.white);
        titleLayout.setBackImg(R.mipmap.back_black);
        titleLayout.setTitle(R.string.select_qingjing, getResources().getColor(R.color.black333333));
        titleLayout.setRightTv(R.string.confirm, getResources().getColor(R.color.black333333), this);
        View header = View.inflate(SelectQingjingActivity.this, R.layout.header_select_qingjing, null);
        searchLinear = (LinearLayout) header.findViewById(R.id.activity_select_qingjing_searchlinear);
        searchLinear.setVisibility(View.GONE);
        mapRelative = (RelativeLayout) header.findViewById(R.id.rl_box);
//        mapView = (MapView) header.findViewById(R.id.mv);
        nearTv = (TextView) header.findViewById(R.id.tv);
        qingjingRecycler = (RecyclerView) header.findViewById(R.id.activity_select_qingjing_recycler);
//        nearListView = (ListViewForScrollView) header.findViewById(R.id.lv);
        qingjingTv = (TextView) header.findViewById(R.id.activity_select_qingjing_qingjingtv);
        allQingjingTv = (TextView) header.findViewById(R.id.activity_select_qingjing_all);
        qingjingGrid.addHeaderView(header);
    }


    @Override
    protected void initList() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        qingjingRecycler.setLayoutManager(linearLayoutManager);
        getCurrentLocation();
        qingjingGrid.setOnScrollListener(this);
        searchLinear.setOnClickListener(this);
        allQingjingTv.setOnClickListener(this);
        int space = DensityUtils.dp2px(SelectQingjingActivity.this, 5);
        qingjingGrid.setVerticalSpacing(space);
        qingjingList = new ArrayList<>();
        allQingjingGridAdapter = new AllQingjingGridAdapter(qingjingList, null, SelectQingjingActivity.this, space);
        qingjingGrid.setAdapter(allQingjingGridAdapter);
        qingjingGrid.setOnItemClickListener(this);
    }

    private void getCurrentLocation() {
        MapUtil.getCurrentLocation(activity, new MapUtil.OnReceiveLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                if (bdLocation == null) {
                    return;
                }
                if (isFirstLoc) {
                    dialog.show();
                    isFirstLoc = false;
                    LatLng ll = new LatLng(bdLocation.getLatitude(),
                            bdLocation.getLongitude());
                    SelectQingjingActivity.this.ll = ll;
                    getNearByData(ll);
                    MapStatus.Builder builder = new MapStatus.Builder();
                    builder.target(ll).zoom(18);
                    qingjingList(page + "", 2 + "", 1 + "", distance + "", ll.longitude + "", ll.latitude + "");
                }
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_select_qingjing_searchlinear:
                if (ll == null) {
                    getCurrentLocation();
                    return;
                }
                Intent intent2 = new Intent(SelectQingjingActivity.this, SelectAllQingjingActivity.class);
                intent2.putExtra("isSearch", "1");
                intent2.putExtra("latLng", ll);
                startActivityForResult(intent2, DataConstants.REQUESTCODE_SELECTQJ_ALLQJ);
                break;
            case R.id.activity_select_qingjing_all:
                if (ll == null) {
                    getCurrentLocation();
                    return;
                }
                Intent intent = new Intent(SelectQingjingActivity.this, SelectAllQingjingActivity.class);
                intent.putExtra("latLng", ll);
                startActivityForResult(intent, DataConstants.REQUESTCODE_SELECTQJ_ALLQJ);
                break;
            case R.id.title_continue:
                for (int i = 0; i < qingjingList.size(); i++) {
                    if (qingjingList.get(i).isSelect()) {
                        Intent intent1 = new Intent();
                        intent1.putExtra("qingjing", qingjingList.get(i));
                        setResult(DataConstants.RESULTCODE_CREATESCENE_SELECTQJ, intent1);
                        finish();
                        break;
                    }
                }
                onBackPressed();
                break;
        }
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
                case DataConstants.RESULTCODE_CREATESCENE_SELECTQJ:
                    QingJingListBean.QingJingItem qingJingItem = (QingJingListBean.QingJingItem) data.getSerializableExtra("qingjing");
                    if (qingJingItem != null) {
                        Intent intent = new Intent();
//                        Log.e("<<<>>>", qingJingItem.getTitle());
                        intent.putExtra("qingjing", qingJingItem);
                        setResult(DataConstants.RESULTCODE_CREATESCENE_SELECTQJ, intent);
                        finish();
                    }
                    break;
                case DataConstants.RESULTCODE_CREATESCENE_SEARCHQJ:
                    SearchBean.SearchItem searchItem = (SearchBean.SearchItem) data.getSerializableExtra("searchqj");
                    if (searchItem != null) {
                        Intent intent = new Intent();
                        Log.e("<<<>>>", searchItem.getTitle());
                        intent.putExtra("searchqj", searchItem);
                        setResult(DataConstants.RESULTCODE_CREATESCENE_SEARCHQJ, intent);
                        finish();
                    }
                    break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        MapUtil.destroyLocationClient();
//        if (bitmapDescripter != null) {
//            bitmapDescripter.recycle();
//        }
        super.onDestroy();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        for (int i = 0; i < qingjingList.size(); i++) {
            if (position == i) {
                qingjingList.get(position).setIsSelect(true);
            } else {
                qingjingList.get(i).setIsSelect(false);
            }
        }
        allQingjingGridAdapter.notifyDataSetChanged();
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (visibleItemCount > qingjingGrid.getHeaderViewCount() * qingjingGrid.getNumColumns() && (firstVisibleItem + visibleItemCount >= totalItemCount)
                && firstVisibleItem != lastSavedFirstVisibleItem && lastTotalItem != totalItemCount
                && ll != null) {
            lastSavedFirstVisibleItem = firstVisibleItem;
            lastTotalItem = totalItemCount;
            page++;
            progressBar.setVisibility(View.VISIBLE);
            qingjingList(page + "", 2 + "", 1 + "", distance + "", ll.longitude + "", ll.latitude + "");
        }
    }

    private void getNearByData(LatLng ll) {//附近的情境
        mapPage = 1;
        pageSize = 3;
        ClientDiscoverAPI.qingjingList(1 + "", null, null, null, ll.longitude + "", ll.latitude + "", new RequestCallBack<String>() {
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
                    mapRelative.setVisibility(View.GONE);
                }
                QingJingListBean netQingjingListBean = qingJingListBean;
                if (netQingjingListBean.isSuccess()) {
                    mapRelative.setVisibility(View.VISIBLE);
                    recyclerList = netQingjingListBean.getData().getRows();
                    jingQingjingRecyclerAdapter = new JingQingjingRecyclerAdapter(SelectQingjingActivity.this, netQingjingListBean.getData().getRows(), SelectQingjingActivity.this, qingjingRecycler.getMeasuredHeight());
                    qingjingRecycler.setAdapter(jingQingjingRecyclerAdapter);
                    qingjingGrid.requestLayout();
                } else {
                    ToastUtils.showError(netQingjingListBean.getMessage());
                    mapRelative.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                ToastUtils.showError(R.string.net_fail);
            }
        });
    }

    private List<QingJingListBean.QingJingItem> recyclerList = new ArrayList<>();

    //情景列表
    private void qingjingList(String p, String sort, String fine, String dis, String lng, String lat) {
        ClientDiscoverAPI.qingjingList(p, sort, fine, dis, lng, lat, new RequestCallBack<String>() {
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
                    allQingjingTv.setText("查看全部");
                    qingjingTv.setText("推荐地盘");
                    if (page == 1) {
                        qingjingList.clear();
                        lastTotalItem = -1;
                        lastSavedFirstVisibleItem = -1;
                    }
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

    @Override
    public void click(int postion) {
        QingJingListBean.QingJingItem qingJingItem = recyclerList.get(postion);
        Intent intent = new Intent();
//                        Log.e("<<<>>>", qingJingItem.getTitle());
        intent.putExtra("qingjing", qingJingItem);
        setResult(DataConstants.RESULTCODE_CREATESCENE_SELECTQJ, intent);
        finish();
    }
}
