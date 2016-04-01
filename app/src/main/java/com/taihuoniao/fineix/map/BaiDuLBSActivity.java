package com.taihuoniao.fineix.map;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.beans.LocationBean;
import com.taihuoniao.fineix.utils.BaiduMapUtil;
import com.taihuoniao.fineix.view.CustomHeadView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;


/**
 * @author lilin
 *         created at 2016/3/28 13:32
 *         1、定位
 *         2、获取lbs数据
 *         3、添加marker
 *         4、点击marker跳转详情
 */
public class BaiDuLBSActivity extends BaseActivity implements OnGetPoiSearchResultListener, OnGetSuggestionResultListener {
    @Bind(R.id.custom_head)
    CustomHeadView custom_head;
    @Bind(R.id.btMapZoomIn)
    Button btMapZoomIn;
    @Bind(R.id.btMapZoomOut)
    Button btMapZoomOut;
    @Bind(R.id.mMapView)
    MapView mMapView;
    private BaiduMap mBaiduMap;
    private LocationClient mLocClient;
    boolean isFirstLoc = true; // 是否首次定位
    public BaiDuLBSActivity() {
        super(R.layout.activity_lbs_layout);
    }

    @Override
    protected void getIntentData() {
        super.getIntentData();
    }

    @Override
    protected void initView() {
        custom_head.setHeadCenterTxtShow(true, "搜索");
        // 地图初始化
        mBaiduMap = mMapView.getMap();
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        // 定位初始化
        mLocClient = new LocationClient(this);
        mLocClient.registerLocationListener(locationListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);
        mLocClient.setLocOption(option);
        mLocClient.start();
        mBaiduMap = mMapView.getMap();
//        mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(16));
        mBaiduMap.getUiSettings().setZoomGesturesEnabled(true);// 缩放手势
    }

    private BDLocationListener locationListener=new BDLocationListener() {
        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null || mMapView == null) {
                return;
            }
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                            // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mBaiduMap.setMyLocationData(locData);
            if (isFirstLoc) {
                isFirstLoc = false;
                LatLng ll = new LatLng(location.getLatitude(),
                        location.getLongitude());
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(ll).zoom(12.0f);
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
            }
        }
    };

    @Override
    protected void installListener() {
        btMapZoomIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaiduMapUtil.zoomInMapView(mMapView);
            }
        });
        btMapZoomOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaiduMapUtil.zoomOutMapView(mMapView);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }


    @Override
    protected void onResume() {
        mMapView.onResume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mLocClient.stop();
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
        mMapView = null;
        super.onDestroy();
    }


    @Override
    public void onGetPoiResult(PoiResult poiResult) {

    }

    @Override
    public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {

    }

    @Override
    public void onGetSuggestionResult(SuggestionResult suggestionResult) {

    }


}
