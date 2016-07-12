package com.taihuoniao.fineix.map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.utils.MapUtil;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.view.CustomHeadView;
import com.taihuoniao.fineix.view.WaittingDialog;

import java.util.ArrayList;

import butterknife.Bind;

/**
 * @author lilin
 *         created at 2016/7/11 12:06
 */
public class MapAddressDetailActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.custom_head)
    CustomHeadView customHead;
    @Bind(R.id.tv_address)
    TextView tvAddress;
    @Bind(R.id.mapView)
    MapView mapView;
    private BaiduMap mBDMap;
    private boolean isFirstLoc = true;
    private boolean isFirstMapStatusChange = true;
    private LatLng latLng;
    private PoiInfo poiInfo;
    private WaittingDialog dialog;
    private ArrayList<PoiInfo> list;

    public MapAddressDetailActivity() {
        super(R.layout.activity_map_address_detail);
    }

    @Override
    protected void getIntentData() {
        super.getIntentData();
        poiInfo = getIntent().getParcelableExtra(TAG);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MapUtil.getCurrentLocation(activity, new MapUtil.OnReceiveLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation location) {
                if (location == null) {
                    return;
                }
                MyLocationData locData = new MyLocationData.Builder()
                        .accuracy(location.getRadius())
                        // 此处设置开发者获取到的方向信息，顺时针0-360
                        .direction(100).latitude(location.getLatitude())
                        .longitude(location.getLongitude()).build();
                mBDMap.setMyLocationData(locData);
                if (isFirstLoc) {
                    isFirstLoc = false;
                    latLng = new LatLng(location.getLatitude(),
                            location.getLongitude());
                    if (poiInfo != null) {
                        latLng = new LatLng(poiInfo.location.latitude, poiInfo.location.longitude);
                        tvAddress.setText(poiInfo.name);
                    } else {
                        getNearByAddress();
                    }
                    MapStatus.Builder builder = new MapStatus.Builder();
                    builder.target(latLng).zoom(18.0f);
                    mBDMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
                }
            }
        });
    }

    private void getNearByAddress() {
        if (latLng == null) return;
        if (!activity.isFinishing() && dialog != null) dialog.show();
        MapUtil.getAddressByCoordinate(latLng, new MapUtil.MyOnGetGeoCoderResultListener() {
            @Override
            public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
                if (!activity.isFinishing() && dialog != null) dialog.dismiss();
                if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                    return;
                }
                list = (ArrayList) result.getPoiList();
                if (list == null) return;

                if (list.size() > 0) {
                    tvAddress.setText(list.get(0).name);
                } else {
                    ToastUtils.showError("抱歉,没有检索到结果!");
                }
            }

            @Override
            public void onGetGeoCodeResult(GeoCodeResult result) {
            }
        });
    }

    @Override
    protected void initView() {
        customHead.setHeadCenterTxtShow(true, R.string.position);
        customHead.setHeadRightTxtShow(true, R.string.confirm);
        customHead.getHeadRightTV().setEnabled(false);
        dialog = new WaittingDialog(this);
        mapView.showZoomControls(false);
        mBDMap = mapView.getMap();
        mBDMap.setMyLocationEnabled(true);
    }

    @Override
    protected void installListener() {
        customHead.getHeadRightTV().setOnClickListener(this);
        mBDMap.setOnMapStatusChangeListener(new BaiduMap.OnMapStatusChangeListener() {
            @Override
            public void onMapStatusChangeStart(MapStatus mapStatus) {

            }

            @Override
            public void onMapStatusChange(MapStatus mapStatus) {

            }

            @Override
            public void onMapStatusChangeFinish(MapStatus mapStatus) {
                if (!isFirstMapStatusChange) {
                    customHead.getHeadRightTV().setEnabled(true);
                    latLng = new LatLng(mapStatus.target.latitude,
                            mapStatus.target.longitude);
                    getNearByAddress();
                } else {
                    isFirstMapStatusChange = false;
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_head_right:
                Intent intent = new Intent();
                intent.putParcelableArrayListExtra("list", list);
                setResult(RESULT_OK, intent);
                finish();
                break;
        }
    }

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
        MapUtil.destroyGeoCoder();
        MapUtil.destroyPoiSearch();
        MapUtil.destroyLocationClient();
        mapView.onDestroy();
        super.onDestroy();
    }
}
