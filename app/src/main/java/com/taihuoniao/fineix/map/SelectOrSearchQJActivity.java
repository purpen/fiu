package com.taihuoniao.fineix.map;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
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
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.NearByQJAdapter;
import com.taihuoniao.fineix.adapters.QJRecommonedAdapter;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.beans.QingJingData;
import com.taihuoniao.fineix.beans.QingJingItem;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.HttpResponse;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.LogUtil;
import com.taihuoniao.fineix.utils.MapUtil;
import com.taihuoniao.fineix.utils.Util;
import com.taihuoniao.fineix.view.CustomGridView;
import com.taihuoniao.fineix.view.CustomHeadView;
import com.taihuoniao.fineix.view.CustomListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * @author lilin
 *         created at 2016/4/13 17:42
 */
public class SelectOrSearchQJActivity extends BaseActivity<QingJingItem> implements View.OnClickListener {
    @Bind(R.id.custom_head)
    CustomHeadView custom_head;
    @Bind(R.id.mv)
    MapView mv;
    @Bind(R.id.lv)
    CustomListView lv;
    @Bind(R.id.cgv)
    CustomGridView cgv;
    @Bind(R.id.rl_box)
    RelativeLayout rl_box;
    @Bind(R.id.rl_recommend)
    RelativeLayout rl_recommend;
    @Bind(R.id.tv)
    TextView tv;
    @Bind(R.id.tv_all)
    TextView tv_all;

    private NearByQJAdapter nearByAdapter; //附近的情境
    private QJRecommonedAdapter recommendAdapter;//推荐情境
    private BaiduMap mBDMap;
    private int page; //默认查看第一页
    private int pageSize;//本界面只展示三条
    private int radius = 5000; //搜索半径
    private boolean isFirstLoc = true;
    private BitmapDescriptor bitmapDescripter;
    private static final String STICK_ALL = "0"; //所有情境
    private static final String STICK_SELECT = "1"; //精选情境
    private static final String STICK_NO = "2"; //非精选情境

    public SelectOrSearchQJActivity() {
        super(R.layout.activity_qj_select_search);
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
                    getNearByData(ll);
                    MapStatus.Builder builder = new MapStatus.Builder();
                    builder.target(ll).zoom(14);
                    mBDMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
                }
            }
        });
    }


    @Override
    protected void initView() {
        custom_head.setHeadCenterTxtShow(true, R.string.select_qj);
        custom_head.setHeadRightTxtShow(true, R.string.confirm);
        mv.showZoomControls(false);
        mBDMap = mv.getMap();
//        mBDMap.setMapStatus(MapStatusUpdateFactory.zoomTo(14));
        mBDMap.getUiSettings().setAllGesturesEnabled(false);
        mBDMap.setMyLocationEnabled(true);
        startLocate();
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

    private void getNearByData(LatLng ll) {//附近的情境
        page=1;
        pageSize=3;
        ClientDiscoverAPI.getQJData(ll, radius,String.valueOf(page),String.valueOf(pageSize),STICK_ALL,new RequestCallBack<String>() {
            @Override
            public void onStart() {
                //TODO 弹出加载框
            }

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                //TODO 关闭加载框
                if (responseInfo == null) {
                    return;
                }
                if (responseInfo.result == null) {
                    return;
                }
                LogUtil.e("附近情境", responseInfo.result);
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
                //TODO 关闭加载框
                LogUtil.e(TAG, s);
            }
        });
    }

    @Override
    protected void requestNet() {//推荐情境
        page=1;
        pageSize=2;
        ClientDiscoverAPI.getQJData(null,0,String.valueOf(page),String.valueOf(pageSize),STICK_SELECT,new RequestCallBack<String>() {
            @Override
            public void onStart() {
                //TODO 弹出加载框
            }

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                //TODO 关闭加载框
                if (responseInfo == null) {
                    return;
                }
                if (responseInfo.result == null) {
                    return;
                }
                LogUtil.e("推荐情境", responseInfo.result);
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
                refreshGVUI(qingJingData.rows);
            }

            @Override
            public void onFailure(HttpException e, String s) {
                //TODO 关闭加载框
                LogUtil.e(TAG, s);
            }
        });
    }

    private void refreshGVUI(ArrayList<QingJingItem> list) {
        if (list == null) {
//            Util.makeToast(activity, "数据异常");
            return;
        }

        if (list.size() == 0) {
//            Util.makeToast(activity, "暂无数据");
            return;
        }
        rl_recommend.setVisibility(View.VISIBLE);
        if (recommendAdapter == null) {
            recommendAdapter = new QJRecommonedAdapter(list, activity);
            cgv.setAdapter(recommendAdapter);
        } else {
            recommendAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void refreshUI(List<QingJingItem> list) {
        if (list == null) {
//            Util.makeToast(activity, "数据异常");
            return;
        }

        if (list.size() == 0) {
//            Util.makeToast(activity, "暂无数据");
            return;
        }

        rl_box.setVisibility(View.VISIBLE);
        addOverlayers(list);
        if (nearByAdapter == null) {
            nearByAdapter = new NearByQJAdapter(activity, list);
            lv.setAdapter(nearByAdapter);
        } else {
            nearByAdapter.notifyDataSetChanged();
        }

    }

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

    @Override
    protected void installListener() {
        cgv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //TODO 跳转详情页
                Util.makeToast(activity, "跳转地盘详情");
            }
        });
        tv_all.setOnClickListener(this);
        mBDMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                activity.startActivity(new Intent(activity, DisplayOverlayerActivity.class));
            }

            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {
                return false;
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_all:
                //TODO 跳转全部场景界面
                Util.makeToast(activity, "跳转地盘列表");
                break;
        }
    }
}
