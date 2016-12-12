package com.taihuoniao.fineix.map;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
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
import com.taihuoniao.fineix.adapters.NearByQJAdapter;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.beans.QingJingData;
import com.taihuoniao.fineix.beans.QingJingItem;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.DataConstants;
import com.taihuoniao.fineix.beans.HttpResponse;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.LogUtil;
import com.taihuoniao.fineix.utils.MapUtil;
import com.taihuoniao.fineix.utils.Util;
import com.taihuoniao.fineix.view.CustomHeadView;
import com.taihuoniao.fineix.view.dialog.WaittingDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * @author lilin
 *         created at 2016/4/15 16:13
 */
public class DisplayOverlayerActivity extends BaseActivity<QingJingItem> {
    @Bind(R.id.custom_head)
    CustomHeadView custom_head;
    @Bind(R.id.mv)
    MapView mv;
    @Bind(R.id.lv)
    ListView lv;
    private BaiduMap mBDMap;
    private boolean isFirstLoc = true;
    private static final String STICK_ALL = "0"; //所有情境
    private static final String STICK_SELECT = "1"; //精选情境
    private static final String STICK_NO = "2"; //非精选情境
    private BitmapDescriptor bitmapDescripter;
    private NearByQJAdapter nearByAdapter; //附近的情境
    private WaittingDialog waittingDialog;
    public DisplayOverlayerActivity() {
        super(R.layout.activity_display_overlayer);
    }


    @Override
    protected void initView() {
        custom_head.setHeadCenterTxtShow(true, "附近所有情境");
        waittingDialog=new WaittingDialog(this);
        mv.showZoomControls(false);
        mBDMap = mv.getMap();
//        mBDMap.setMapStatus(MapStatusUpdateFactory.zoomTo(14));
//        mBDMap.getUiSettings().setAllGesturesEnabled(false);
        mBDMap.setMyLocationEnabled(true);
        startLocate();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                QingJingItem qingJing = (QingJingItem) lv.getAdapter().getItem(position);
                Intent intent = new Intent();
                intent.putExtra("qingjing", qingJing);
                setResult(DataConstants.RESULTCODE_MAP, intent);
                finish();
            }
        });
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
        int page = 1;
        int pageSize = 1000;
        int radius = 0;
        ClientDiscoverAPI.getQJData(ll, radius, String.valueOf(page), String.valueOf(pageSize), STICK_ALL, new RequestCallBack<String>() {
            @Override
            public void onStart() {
                if (waittingDialog!=null) waittingDialog.show();
            }

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                waittingDialog.dismiss();
                if (responseInfo == null) {
                    return;
                }
                if (responseInfo.result == null) {
                    return;
                }
                LogUtil.e("附近所有情境", responseInfo.result);
                QingJingData qingJingData = null;
                try {
                    qingJingData = JsonUtil.fromJson(responseInfo.result, new TypeToken<HttpResponse<QingJingData>>() {
                    });
                } catch (JsonSyntaxException e) {//TODO log
                    Util.makeToast(activity, "对不起,数据异常");
                }
                if (qingJingData == null) {
                    return;
                }
                refreshUI(qingJingData.rows);
            }

            @Override
            public void onFailure(HttpException e, String s) {
                waittingDialog.dismiss();
                LogUtil.e(TAG, s);
            }
        });
    }

    private void addOverlayers(final List<QingJingItem> list) {
        bitmapDescripter = BitmapDescriptorFactory.fromResource(R.mipmap.icon_marker3);
        LatLng ll;
        MarkerOptions option;
        final ArrayList<Marker> markers = new ArrayList<>();
        for (QingJingItem item : list) {
            LogUtil.e("LatLng", "lat==" + item.location.coordinates.get(1) + "&&lng==" + item.location.coordinates.get(0));
            ll = new LatLng(item.location.coordinates.get(1), item.location.coordinates.get(0));
            option = new MarkerOptions().position(ll).icon(bitmapDescripter);
            option.animateType(MarkerOptions.MarkerAnimateType.drop);
            Marker marker = (Marker) mBDMap.addOverlay(option);
            markers.add(marker);
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
                InfoWindow.OnInfoWindowClickListener listener = new InfoWindow.OnInfoWindowClickListener() {
                    public void onInfoWindowClick() {
                        // TODO InfoWindow 点击
                        Util.makeToast(activity, "InfoWindow被点击");
                    }
                };

                QingJingItem item = list.get(which);
                if (item == null) {
                    return true;
                }
                LatLng ll = marker.getPosition();
                View view = Util.inflateView(R.layout.info_window_layout, null);
                LogUtil.e("huge", item.cover_url);
                ImageLoader.getInstance().displayImage(item.cover_url, ((ImageView) view.findViewById(R.id.iv)));
                ((TextView) view.findViewById(R.id.tv_desc)).setText(item.title);
                ((TextView) view.findViewById(R.id.tv_location)).setText(item.address);
                InfoWindow mInfoWindow = new InfoWindow(BitmapDescriptorFactory.fromView(view), ll, -50, listener);
                mBDMap.showInfoWindow(mInfoWindow);
                return true;
            }
        });
    }

    @Override
    protected void refreshUI(List<QingJingItem> list) {
        if (list == null) {
//            Util.makeToast(activity, "数据异常");
            return;
        }

        if (list.size() == 0) {
            Util.makeToast(activity, "暂无数据");
            return;
        }

        addOverlayers(list);

        if (nearByAdapter == null) {
            nearByAdapter = new NearByQJAdapter(activity, list);
            lv.setAdapter(nearByAdapter);
        } else {
            nearByAdapter.notifyDataSetChanged();
        }
    }
}
