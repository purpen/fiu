package com.taihuoniao.fineix.zone.adapter;

import android.app.Activity;
import android.content.Intent;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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
import com.taihuoniao.fineix.adapters.CommonBaseAdapter;
import com.taihuoniao.fineix.qingjingOrSceneDetails.SearchActivity;
import com.taihuoniao.fineix.utils.LogUtil;
import com.taihuoniao.fineix.utils.Util;
import com.taihuoniao.fineix.zone.bean.ZoneDetailBean;

import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by lilin on 2017/2/16.
 */

public class ZoneShopInfoAdapter extends CommonBaseAdapter {
    private ZoneDetailBean zoneDetail;
    private MapView mapView;
    public ZoneShopInfoAdapter(Activity activity, List list, ZoneDetailBean zoneDetail){
        super(list,activity);
        this.zoneDetail = zoneDetail;
    }

    @Override
    public int getCount() {
        return super.getCount()+1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (position==0){
            convertView = activity.getLayoutInflater().inflate(R.layout.item_shop_info, null);
            mapView = ButterKnife.findById(convertView,R.id.mapView);
            mapView.showZoomControls(false);
            addOverlayer();
            ((TextView)convertView.findViewById(R.id.business_time)).setText(" "+zoneDetail.extra.shop_hours);
            ((TextView)convertView.findViewById(R.id.business_tel)).setText(" "+zoneDetail.extra.tel);
            LinearLayout linearLayout = (LinearLayout)convertView.findViewById(R.id.shop_tags);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.leftMargin = activity.getResources().getDimensionPixelSize(R.dimen.dp5);
            for (String str:zoneDetail.tags) {
                final TextView textView = new TextView(activity);
                textView.setText(str);
                textView.setTextColor(activity.getResources().getColor(R.color.color_222));
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP,10);
                textView.setLayoutParams(params);
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(activity, SearchActivity.class);
                        intent.putExtra("q", textView.getText());
                        intent.putExtra("t", 9);
                        activity.startActivity(intent);
                    }
                });
                linearLayout.addView(textView);
            }
        }else {

        }
        return convertView;
    }

    private void addOverlayer() {
        BaiduMap map = mapView.getMap();
        map.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                LogUtil.e("msg");
            }

            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {
                return false;
            }
        });
        map.getUiSettings().setAllGesturesEnabled(false);
        map.setMapStatus(MapStatusUpdateFactory.zoomTo(18f));
        map.clear();
        LatLng ll = new LatLng(zoneDetail.location.coordinates.get(1),zoneDetail.location.coordinates.get(0));
        BitmapDescriptor bitmapDescripter = BitmapDescriptorFactory.fromResource(R.mipmap.icon_marker3);
        MapStatus.Builder builder = new MapStatus.Builder();
        builder.target(ll).zoom(15.0f);
        map.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
        MarkerOptions option = new MarkerOptions().position(ll).icon(bitmapDescripter);
        option.animateType(MarkerOptions.MarkerAnimateType.grow);
        map.addOverlay(option);
    }
}
