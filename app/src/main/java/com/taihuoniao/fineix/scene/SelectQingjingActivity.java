package com.taihuoniao.fineix.scene;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.AllQingjingGridAdapter;
import com.taihuoniao.fineix.adapters.NearByQJAdapter;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.beans.QingJingData;
import com.taihuoniao.fineix.beans.QingJingItem;
import com.taihuoniao.fineix.beans.QingJingListBean;
import com.taihuoniao.fineix.beans.SearchBean;
import com.taihuoniao.fineix.map.DisplayOverlayerActivity;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.DataConstants;
import com.taihuoniao.fineix.network.HttpResponse;
import com.taihuoniao.fineix.utils.DensityUtils;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.LogUtil;
import com.taihuoniao.fineix.utils.MapUtil;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.utils.Util;
import com.taihuoniao.fineix.view.CustomListView;
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
public class SelectQingjingActivity extends BaseActivity<QingJingItem> implements View.OnClickListener, AdapterView.OnItemClickListener, AbsListView.OnScrollListener {
    private LatLng ll;
    @Bind(R.id.activity_select_qingjing_titlelayout)
    GlobalTitleLayout titleLayout;
    private LinearLayout searchLinear;
    private RelativeLayout mapRelative;
    private MapView mapView;
    private TextView nearTv;
    private CustomListView nearListView;
    private TextView qingjingTv;
    private TextView allQingjingTv;
    @Bind(R.id.activity_select_qingjing_grid)
    GridViewWithHeaderAndFooter qingjingGrid;
    @Bind(R.id.activity_select_qingjing_progress)
    ProgressBar progressBar;
    //地图
    private BaiduMap mBDMap;
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
//    判断是否是全部情景的标识
//    private int stick = 1;


//    @Override
//    protected void getIntentData() {
//        latLng = getIntent().getParcelableExtra("latLng");
//    }

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
        mapRelative = (RelativeLayout) header.findViewById(R.id.rl_box);
        mapView = (MapView) header.findViewById(R.id.mv);
        nearTv = (TextView) header.findViewById(R.id.tv);
        nearListView = (CustomListView) header.findViewById(R.id.lv);
        qingjingTv = (TextView) header.findViewById(R.id.activity_select_qingjing_qingjingtv);
        allQingjingTv = (TextView) header.findViewById(R.id.activity_select_qingjing_all);
        qingjingGrid.addHeaderView(header);
    }

    @Override
    protected void initList() {
        mapView.showZoomControls(false);
        mBDMap = mapView.getMap();
        mBDMap.getUiSettings().setAllGesturesEnabled(false);
        mBDMap.setMyLocationEnabled(true);
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
        titleLayout.setFocusable(true);
        titleLayout.setFocusableInTouchMode(true);
        titleLayout.requestFocus();
        mBDMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                activity.startActivityForResult(new Intent(activity, DisplayOverlayerActivity.class), DataConstants.REQUESTCODE_SELECTQJ_MAP);
            }

            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {
                return false;
            }
        });
        nearListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                QingJingItem qingJingItem = (QingJingItem) nearListView.getAdapter().getItem(position);
                Intent intent1 = new Intent();
                intent1.putExtra("qingjing", qingJingItem);
                setResult(DataConstants.RESULTCODE_MAP_SELECTQJ, intent1);
                finish();
            }
        });
    }

    private void getCurrentLocation() {
        MapUtil.getCurrentLocation(activity, new MapUtil.OnReceiveLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                if (bdLocation == null) {
                    return;
                }
                MyLocationData locData = new MyLocationData.Builder()
                        .accuracy(bdLocation.getRadius())
                                // 此处设置开发者获取到的方向信息，顺时针0-360
                        .direction(100).latitude(bdLocation.getLatitude())
                        .longitude(bdLocation.getLongitude()).build();
                mBDMap.setMyLocationData(locData);
                if (isFirstLoc) {
                    dialog.show();
                    isFirstLoc = false;
                    LatLng ll = new LatLng(bdLocation.getLatitude(),
                            bdLocation.getLongitude());
                    SelectQingjingActivity.this.ll = ll;
                    getNearByData(ll);
                    MapStatus.Builder builder = new MapStatus.Builder();
                    builder.target(ll).zoom(18);
                    mBDMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
                    qingjingList(page + "",2+"", 1 + "", distance + "", ll.longitude + "", ll.latitude + "");
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
                    if(qingJing!=null){
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
                        Log.e("<<<>>>", qingJingItem.getTitle());
                        intent.putExtra("qingjing", qingJingItem);
                        setResult(DataConstants.RESULTCODE_CREATESCENE_SELECTQJ, intent);
                        finish();
                    }
                    break;
                case DataConstants.RESULTCODE_SELECTQJ_SALLQJ:
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

//    private Handler handler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            switch (msg.what) {
//                case DataConstants.QINGJING_LIST:
//                    dialog.dismiss();
//                    progressBar.setVisibility(View.GONE);
//                    QingJingListBean netQingjingListBean = (QingJingListBean) msg.obj;
//                    if (netQingjingListBean.isSuccess()) {
//                        allQingjingTv.setText("查看全部");
//                        qingjingTv.setText("推荐情景");
//                        if (page == 1) {
//                            qingjingList.clear();
//                            lastTotalItem = -1;
//                            lastSavedFirstVisibleItem = -1;
//                        }
//                        qingjingList.addAll(netQingjingListBean.getData().getRows());
//                        allQingjingGridAdapter.notifyDataSetChanged();
//                    }
//                    break;
//                case DataConstants.NET_FAIL:
//                    dialog.show();
//                    progressBar.setVisibility(View.GONE);
//                    break;
//            }
//        }
//    };

    @Override
    protected void onResume() {
        if (mapView != null)
            mapView.onResume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        if (mapView != null)
            mapView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        //cancelNet();
        mBDMap.setMyLocationEnabled(false);
        MapUtil.destroyLocationClient();
        mapView.onDestroy();
        if (bitmapDescripter != null) {
            bitmapDescripter.recycle();
        }
//        if (handler != null) {
//            handler.removeCallbacksAndMessages(null);
//            handler = null;
//        }
        super.onDestroy();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        int pos = position/* - qingjingGrid.getHeaderViewCount() * qingjingGrid.getNumColumns()*/;//实际点的position
        for (int i = 0; i < qingjingList.size(); i++) {
            if (pos == i) {
                qingjingList.get(pos).setIsSelect(true);
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
            qingjingList(page + "",2+"", 1 + "", distance + "", ll.longitude + "", ll.latitude + "");
        }
    }

    private void getNearByData(LatLng ll) {//附近的情境
        mapPage = 1;
        pageSize = 3;
        ClientDiscoverAPI.getQJData(ll, radius, String.valueOf(mapPage), String.valueOf(pageSize), STICK_ALL, new RequestCallBack<String>() {
            @Override
            public void onStart() {
                //TODO 弹出加载框
            }

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                //TODO 关闭加载框
                if (responseInfo == null) {
                    mapRelative.setVisibility(View.GONE);
                    return;
                }
                if (responseInfo.result == null) {mapRelative.setVisibility(View.GONE);
                    return;
                }
                LogUtil.e("附近情境", responseInfo.result);
                QingJingData qingJingData = null;
                try {
                    qingJingData = JsonUtil.fromJson(responseInfo.result, new TypeToken<HttpResponse<QingJingData>>() {
                    });
                } catch (JsonSyntaxException e) {//TODO log
                    mapRelative.setVisibility(View.GONE);
                    Util.makeToast(activity, "对不起,数据异常");
                }
                if (qingJingData == null) {
                    mapRelative.setVisibility(View.GONE);
                    return;
                }
                refreshUI(qingJingData.rows);
            }

            @Override
            public void onFailure(HttpException e, String s) {
                //TODO 关闭加载框
                mapRelative.setVisibility(View.GONE);
                LogUtil.e(TAG, s);
            }
        });
    }

    private NearByQJAdapter nearByAdapter; //附近的情境

    @Override
    protected void refreshUI(List<QingJingItem> list) {
        if (list == null) {
            mapRelative.setVisibility(View.GONE);
//            Util.makeToast(activity, "数据异常");
            return;
        }

        if (list.size() == 0) {
            mapRelative.setVisibility(View.GONE);
//            Util.makeToast(activity, "暂无数据");
            return;
        }

        mapRelative.setVisibility(View.VISIBLE);
        addOverlayers(list);
        if (nearByAdapter == null) {
            nearByAdapter = new NearByQJAdapter(activity, list);
            nearListView.setAdapter(nearByAdapter);
        } else {
            nearByAdapter.notifyDataSetChanged();
        }

    }

    private BitmapDescriptor bitmapDescripter;

    private void addOverlayers(List<QingJingItem> list) {
        bitmapDescripter = BitmapDescriptorFactory.fromResource(R.mipmap.icon_marker3);
        LatLng ll;
        MarkerOptions option;
        ArrayList<OverlayOptions> options = new ArrayList<>();
        for (QingJingItem item : list) {
            LogUtil.e("LatLng", "lat==" + item.location.coordinates.get(1) + "&&lng==" + item.location.coordinates.get(0));
            ll = new LatLng(item.location.coordinates.get(1), item.location.coordinates.get(0));
            option = new MarkerOptions().position(ll).icon(bitmapDescripter)
                    .perspective(false).anchor(0.5f, 0.5f).zIndex(7);
            option.animateType(MarkerOptions.MarkerAnimateType.drop);
            options.add(option);
        }
        mBDMap.addOverlays(options);
    }
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
                    qingjingTv.setText("推荐情景");
                    if (page == 1) {
                        qingjingList.clear();
                        lastTotalItem = -1;
                        lastSavedFirstVisibleItem = -1;
                    }
                    qingjingList.addAll(netQingjingListBean.getData().getRows());
                    allQingjingGridAdapter.notifyDataSetChanged();
                }else{
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
}
