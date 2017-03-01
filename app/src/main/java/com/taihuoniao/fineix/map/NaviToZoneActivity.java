package com.taihuoniao.fineix.map;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.beans.SceneListBean;
import com.taihuoniao.fineix.utils.GlideUtils;
import com.taihuoniao.fineix.utils.MapUtil;
import com.taihuoniao.fineix.view.CustomHeadView;
import com.taihuoniao.fineix.zone.SelectMapDialogActivity;
import com.taihuoniao.fineix.zone.bean.ZoneDetailBean;

import butterknife.Bind;

/**
 * @author lilin
 *         created at 2016/4/15 16:13
 *         地盘导航
 */
public class NaviToZoneActivity extends BaseActivity<SceneListBean> {

    @Bind(R.id.custom_head)
    CustomHeadView custom_head;
    @Bind(R.id.mv)
    MapView mv;
    private boolean isFirstLoc = true;
    private BaiduMap mBDMap;
    private BitmapDescriptor bitmapDescripter;
    private LatLng latLng;
    private ZoneDetailBean zoneDetailBean;
    public NaviToZoneActivity() {
        super(R.layout.activity_navi_to_zone);
    }

    @Override
    protected void getIntentData() {
        Intent intent = getIntent();
        if (intent.hasExtra(NaviToZoneActivity.class.getSimpleName())) {
            zoneDetailBean = intent.getParcelableExtra(TAG);
        }
    }

    @Override
    protected void initView() {
        if (zoneDetailBean == null) return;
        custom_head.setHeadCenterTxtShow(true, zoneDetailBean.title);
        mv.showZoomControls(false);
        mBDMap = mv.getMap();
        mBDMap.setMyLocationEnabled(true);
        getCurrentLocation();
        move2ZoneLocation();
        addOverlayers();
    }

    private void getCurrentLocation() {
        MyLocationData locData = new MyLocationData.Builder()
                            // 此处设置开发者获取到的方向信息，顺时针0-360
                            .direction(100).latitude(zoneDetailBean.location.myLocation.latitude)
                            .longitude(zoneDetailBean.location.myLocation.longitude).build();
                    mBDMap.setMyLocationData(locData);
//        MapUtil.getCurrentLocation(new MapUtil.OnReceiveLocationListener() {
//            @Override
//            public void onReceiveLocation(BDLocation bdLocation) {
//                if (isFirstLoc) {
//                    isFirstLoc = false;
//                    LatLng latLng = new LatLng(bdLocation.getLatitude(),
//                            bdLocation.getLongitude());
//                    MyLocationData locData = new MyLocationData.Builder()
//                            .accuracy(bdLocation.getRadius())
//                            // 此处设置开发者获取到的方向信息，顺时针0-360
//                            .direction(100).latitude(bdLocation.getLatitude())
//                            .longitude(bdLocation.getLongitude()).build();
//                    mBDMap.setMyLocationData(locData);
//                }
//            }
//        });
    }

    private void move2ZoneLocation() {
        latLng = new LatLng(zoneDetailBean.location.coordinates.get(1), zoneDetailBean.location.coordinates.get(0));
        MapStatus.Builder builder = new MapStatus.Builder();
        builder.target(latLng).zoom(18);
        mBDMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
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
        mBDMap.setMyLocationEnabled(false);
        MapUtil.destroyLocationClient();
        mv.onDestroy();
        if (bitmapDescripter != null) {
            bitmapDescripter.recycle();
        }
        super.onDestroy();
    }

    private void addOverlayers() {
        bitmapDescripter = BitmapDescriptorFactory.fromResource(R.mipmap.icon_marker3);
        MarkerOptions option;
        option = new MarkerOptions().position(latLng).icon(bitmapDescripter);
        option.animateType(MarkerOptions.MarkerAnimateType.grow);
        mBDMap.addOverlay(option);
        showInfoWindow();
    }

    private void showInfoWindow() {
        View view = getLayoutInflater().inflate(R.layout.info_window_layout, null);
        GlideUtils.displayImage(zoneDetailBean.banners.get(0), ((ImageView) view.findViewById(R.id.iv)));
        ((TextView) view.findViewById(R.id.tv_desc)).setText(zoneDetailBean.title);
        ((TextView) view.findViewById(R.id.tv_location)).setText(zoneDetailBean.address);
        InfoWindow mInfoWindow = new InfoWindow(BitmapDescriptorFactory.fromView(view), latLng, -90,infoWindowClickListener);
        mBDMap.showInfoWindow(mInfoWindow);
    }

    InfoWindow.OnInfoWindowClickListener infoWindowClickListener = new InfoWindow.OnInfoWindowClickListener() {
        public void onInfoWindowClick() {

            Intent intent = new Intent(activity, SelectMapDialogActivity.class);
            intent.putExtra(SelectMapDialogActivity.class.getSimpleName(),zoneDetailBean);
//            intent.putExtra(SelectMapDialogActivity.class.getSimpleName(),destination);
            startActivity(intent);
//            String origin= currentLatlng.latitude+","+currentLatlng.longitude;

        }
    };

}
