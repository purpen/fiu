package com.taihuoniao.fineix.map;

import android.content.Intent;
import android.media.Image;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
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
import com.taihuoniao.fineix.utils.Util;
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
    @Bind(R.id.ib_zoom_in)
    ImageButton ib_zoom_in;
    @Bind(R.id.ib_zoom_out)
    ImageButton ib_zoom_out;
    @Bind(R.id.ib_move_locate)
    ImageButton ib_move_locate;
    @Bind(R.id.mMapView)
    MapView mMapView;
    private BaiduMap mBaiduMap;
    private LocationClient mLocClient;
    boolean isFirstLoc = true; // 是否首次定位
    private String title;
    private BDLocation location;
    public BaiDuLBSActivity() {
        super(R.layout.activity_lbs_layout);
    }

    @Override
    protected void getIntentData() {
        Intent intent = getIntent();
        String key=HotCitiesActivity.class.getSimpleName();
        if (intent.hasExtra(key)){
            title=intent.getStringExtra(key);
        }
    }

    @Override
    protected void initView() {
        custom_head.setHeadCenterTxtShow(true, title);
        // 地图初始化
        BaiduMapUtil.goneMapViewChild(mMapView,false,true);
        mBaiduMap = mMapView.getMap();
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        mBaiduMap.getUiSettings().setZoomGesturesEnabled(true);// 缩放手势
        // 定位初始化
        mLocClient = new LocationClient(this);
        mLocClient.registerLocationListener(locationListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);
        mLocClient.setLocOption(option);
        mLocClient.start();
//        mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(16));
    }

    @Override
    protected void requestNet() {

    }

    private BDLocationListener locationListener=new BDLocationListener() {
        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null || mMapView == null) {
                return;
            }
            BaiDuLBSActivity.this.location=location;
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                            // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mBaiduMap.setMyLocationData(locData);
        }
    };

    @Override
    protected void installListener() {
        ib_zoom_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaiduMapUtil.zoomInMapView(mMapView);
            }
        });
        ib_zoom_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaiduMapUtil.zoomOutMapView(mMapView);
            }
        });

        ib_move_locate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (location!=null){
                    LatLng ll = new LatLng(location.getLatitude(),
                            location.getLongitude());
                    MapStatus.Builder builder = new MapStatus.Builder().target(ll);
                    mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
                }else {
                    Util.makeToast(activity,"很抱歉,未能移动到当前位置");
                }
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
        //添加Marker
    }

    @Override
    public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {

    }

    @Override
    public void onGetSuggestionResult(SuggestionResult suggestionResult) {

    }


}
