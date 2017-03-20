package com.taihuoniao.fineix.zone.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.base.BaseFragment;
import com.taihuoniao.fineix.map.NaviToZoneActivity;
import com.taihuoniao.fineix.zone.bean.ZoneDetailBean;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * 商家信息
 */
public class ZoneShopInfoFragment extends BaseFragment {
    private static final String ZONE_ID = "ZONE_ID";
    @Bind(R.id.mapView)
    MapView mapView;
    @Bind(R.id.tv_address)
    TextView tvAddress;
    @Bind(R.id.business_time)
    TextView businessTime;
    @Bind(R.id.business_tel)
    TextView businessTel;
    private String sZoneId;
    private ZoneDetailBean zoneDetailBean;

    public ZoneShopInfoFragment() {
        // Required empty public constructor
    }

    public static ZoneShopInfoFragment newInstance(String param) {
        ZoneShopInfoFragment fragment = new ZoneShopInfoFragment();
        Bundle args = new Bundle();
        args.putString(ZONE_ID, param);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            sZoneId = getArguments().getString(ZONE_ID);
        }
    }


    @Override
    protected View initView() {
        View view = View.inflate(activity, R.layout.fragment_shop_info, null);
        return view;
    }

    @Override
    protected void initList() {

        mapView.showZoomControls(false);
    }

    public void setData(ZoneDetailBean zoneDetail){
        this.zoneDetailBean = zoneDetail;
        resetUI();
    }

    private void resetUI() {
        if (zoneDetailBean==null) return;
        addOverlayer();
        tvAddress.setText(zoneDetailBean.address);
        businessTime.setText(zoneDetailBean.extra.shop_hours);
        businessTel.setText(zoneDetailBean.extra.tel);
    }


    private void addOverlayer() {
        if (mapView==null) return;
        BaiduMap map = mapView.getMap();
        final LatLng ll = new LatLng(zoneDetailBean.location.coordinates.get(1),zoneDetailBean.location.coordinates.get(0));
        map.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                Intent intent = new Intent(activity, NaviToZoneActivity.class);
                intent.putExtra(NaviToZoneActivity.class.getSimpleName(),zoneDetailBean);
                startActivity(intent);
            }

            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {
                return false;
            }
        });
        map.getUiSettings().setAllGesturesEnabled(false);
        map.setMapStatus(MapStatusUpdateFactory.zoomTo(18f));
        map.clear();
        BitmapDescriptor bitmapDescripter = BitmapDescriptorFactory.fromResource(R.mipmap.icon_marker3);
        MapStatus.Builder builder = new MapStatus.Builder();
        builder.target(ll).zoom(15.0f);
        map.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
        MarkerOptions option = new MarkerOptions().position(ll).icon(bitmapDescripter);
        option.animateType(MarkerOptions.MarkerAnimateType.grow);
        map.addOverlay(option);
    }

    @Override
    protected void installListener() {

    }


    @Override
    protected void requestNet() {

    }

    @Override
    protected void refreshUI() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
