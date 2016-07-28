package com.taihuoniao.fineix.map;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.beans.SceneListBean;
import com.taihuoniao.fineix.beans.UserCJListData;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.beans.HttpResponse;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.LogUtil;
import com.taihuoniao.fineix.utils.MapUtil;
import com.taihuoniao.fineix.utils.Util;
import com.taihuoniao.fineix.view.CustomHeadView;
import com.taihuoniao.fineix.view.WaittingDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @author lilin
 *         created at 2016/4/15 16:13
 */
public class MapNearByCJActivity extends BaseActivity<SceneListBean> {
    @Bind(R.id.custom_head)
    CustomHeadView custom_head;
    @Bind(R.id.mv)
    MapView mv;
    //    @Bind(R.id.btn)
//    Button btn;
//    @Bind(R.id.ibtn)
//    ImageButton ibtn;
    private BaiduMap mBDMap;
    private int page; //默认查看第一页
    private int pageSize;//本界面只展示三条
    private int radius = 5000; //搜索半径
    private boolean isFirstLoc = true;
    private static final String STICK_ALL = "0"; //所有情境
    private static final String STICK_SELECT = "1"; //精选情境
    private static final String STICK_NO = "2"; //非精选情境
    private BitmapDescriptor bitmapDescripter;
    private BitmapDescriptor curDescripter;
    private WaittingDialog waittingDialog;
    private LatLng ll;
    private String address;

    public MapNearByCJActivity() {
        super(R.layout.activity_nearby_cj);
    }

    @Override
    protected void getIntentData() {
        Intent intent = getIntent();
        if (intent.hasExtra(MapNearByCJActivity.class.getSimpleName())) {
            ll = intent.getParcelableExtra(TAG);
        }

        if (intent.hasExtra("address")) {
            address = intent.getStringExtra("address");
        }

    }

    @Override
    protected void initView() {
        if (!TextUtils.isEmpty(address)) {
            custom_head.setHeadCenterTxtShow(true, address);
        }
        waittingDialog = new WaittingDialog(this);
        mv.showZoomControls(false);
        mBDMap = mv.getMap();
//        mBDMap.setMapStatus(MapStatusUpdateFactory.zoomTo(14));
//        mBDMap.getUiSettings().setAllGesturesEnabled(false);
//        mBDMap.setMyLocationEnabled(true);
//        startLocate();
        loadCurrentData();
    }

    private void move2CurrentLocation() {
        MapStatus.Builder builder = new MapStatus.Builder();
        builder.target(ll).zoom(18);
        mBDMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
    }

    private void loadCurrentData() {
        if (ll != null) {
            move2CurrentLocation();
            getNearByData(ll);
        }
    }

    @OnClick({R.id.btn, R.id.ibtn})
    void performClick(View v) {
        switch (v.getId()) {
            case R.id.btn: //重新搜索
                mBDMap.clear();
                loadCurrentData();
                break;
            case R.id.ibtn: //重新定位当前经纬度
                move2CurrentLocation();
                break;
        }
    }

    @Override
    protected void onResume() {
        if (mv != null)
            mv.onResume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        if (mv != null)
            mv.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
//        mBDMap.setMyLocationEnabled(false);
        MapUtil.destroyLocationClient();
        mv.onDestroy();
        if (bitmapDescripter != null) {
            bitmapDescripter.recycle();
        }

        super.onDestroy();
    }

    private void startLocate() {
        MapUtil.getCurrentLocation(new MapUtil.OnReceiveLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                if (bdLocation == null && mv == null) {
                    return;
                }
                MyLocationData locData = new MyLocationData.Builder()
                        .accuracy(bdLocation.getRadius())
                        // 此处设置开发者获取到的方向信息，顺时针0-360
                        .direction(100).latitude(bdLocation.getLatitude())
                        .longitude(bdLocation.getLongitude()).build();
                mBDMap.setMyLocationData(locData);
                if (isFirstLoc) {
                    isFirstLoc = false;
                    LatLng ll = new LatLng(bdLocation.getLatitude(),
                            bdLocation.getLongitude());
                    MapStatus.Builder builder = new MapStatus.Builder();
                    builder.target(ll).zoom(14);
                    mBDMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
                    getNearByData(ll);
                }
            }
        });
    }

    private void getNearByData(LatLng ll) {//附近的所有情境
        page = 1;
        pageSize = 1000;
        ClientDiscoverAPI.getSceneList(ll, String.valueOf(page), String.valueOf(pageSize), String.valueOf(radius), new RequestCallBack<String>() {
            @Override
            public void onStart() {
                if (waittingDialog != null && !activity.isFinishing()) waittingDialog.show();
            }

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                if (waittingDialog != null) waittingDialog.dismiss();
                if (responseInfo == null) {
                    return;
                }
                if (responseInfo.result == null) {
                    return;
                }
                LogUtil.e("附近所有场景", responseInfo.result);
                HttpResponse<UserCJListData> response;
                try {
                    response = JsonUtil.json2Bean(responseInfo.result, new TypeToken<HttpResponse<UserCJListData>>() {
                    });
                    if (response == null) return;
                    if (response.isSuccess()) {
                        List<SceneListBean> rows = response.getData().rows;
                        refreshUI(rows);
                    }
                } catch (JsonSyntaxException e) {//TODO log
                    Util.makeToast(activity, "对不起,数据异常");
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                if (waittingDialog != null) waittingDialog.dismiss();
                LogUtil.e(TAG, s);
            }
        });
    }

    private void addOverlayers(final List<SceneListBean> list) {
        bitmapDescripter = BitmapDescriptorFactory.fromResource(R.mipmap.icon_marker3);
        LatLng ll;
        MarkerOptions option;
        final ArrayList<Marker> markers = new ArrayList<>();
        for (SceneListBean item : list) {
            LogUtil.e("LatLng", "lat==" + item.location.coordinates.get(1) + "&&lng==" + item.location.coordinates.get(0));
            ll = new LatLng(item.location.coordinates.get(1), item.location.coordinates.get(0));
            option = new MarkerOptions().position(ll).icon(bitmapDescripter);
            option.animateType(MarkerOptions.MarkerAnimateType.grow);
            Marker marker = (Marker) mBDMap.addOverlay(option);
            markers.add(marker);
            if (this.ll.longitude == ll.longitude && this.ll.latitude == ll.latitude) {
                showInfoWindow(marker.getPosition(), item);
            }
        }

        mBDMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                int which = -1;
                for (int i = 0; i < markers.size(); i++) {
                    if (markers.get(i).equals(marker)) {
                        which = i;
                        break;
                    }
                }

                if (which==-1){
                    return true;
                }

                SceneListBean item = list.get(which);
                if (item == null) {
                    return true;
                }
                showInfoWindow(marker.getPosition(), item);
//                LatLng ll = marker.getPosition();
//                View view = Util.inflateView(activity, R.layout.info_window_layout, null);
//                LogUtil.e("huge", item.getCover_url());
//                ImageLoader.getInstance().displayImage(item.getCover_url(), ((ImageView) view.findViewById(R.id.iv)));
//                ((TextView) view.findViewById(R.id.tv_desc)).setText(item.getScene_title());
//                ((TextView) view.findViewById(R.id.tv_location)).setText(item.getAddress());
//                InfoWindow mInfoWindow = new InfoWindow(BitmapDescriptorFactory.fromView(view), ll, -50, infoWindowClickListener);
//                mBDMap.showInfoWindow(mInfoWindow);
                return true;
            }
        });
    }

    private void showInfoWindow(LatLng ll, SceneListBean item) {
        View view = Util.inflateView(R.layout.info_window_layout, null);
        LogUtil.e(TAG,item.getCover_url());
        ImageLoader.getInstance().displayImage(item.getCover_url(), ((ImageView) view.findViewById(R.id.iv)), options);
        ((TextView) view.findViewById(R.id.tv_desc)).setText(item.getScene_title());
        ((TextView) view.findViewById(R.id.tv_location)).setText(item.getAddress());
        InfoWindow mInfoWindow = new InfoWindow(BitmapDescriptorFactory.fromView(view), ll, -80, infoWindowClickListener);
        mBDMap.showInfoWindow(mInfoWindow);
    }

    InfoWindow.OnInfoWindowClickListener infoWindowClickListener = new InfoWindow.OnInfoWindowClickListener() {
        public void onInfoWindowClick() {
            // TODO InfoWindow 点击
//          Util.makeToast(activity, "InfoWindow被点击");
        }
    };

    @Override
    protected void refreshUI(List<SceneListBean> list) {
        if (list == null) return;

        if (list.size() == 0) {
            Util.makeToast(activity, "暂无数据");
            return;
        }
        addOverlayers(list);
    }
}
