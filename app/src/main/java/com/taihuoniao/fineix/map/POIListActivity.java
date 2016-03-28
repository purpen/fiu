package com.taihuoniao.fineix.map;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.AroundPoiAdapter;
import com.taihuoniao.fineix.adapters.SearchPOIAdapter;
import com.taihuoniao.fineix.beans.LocationBean;
import com.taihuoniao.fineix.utils.BaiduMapUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * @author lilin
 *         created at 2016/3/28 13:32
 */
public class POIListActivity extends FragmentActivity implements OnGetPoiSearchResultListener, OnGetSuggestionResultListener {

    public static final int SHOW_MAP = 0;
    private static final int SHOW_SEARCH_RESULT = 1;
    // 延时多少秒diss掉dialog
    private static final int DELAY_DISMISS = 1000 * 30;
    private static Context mContext;
    // 搜索当前城市poi数据源
    private static List<LocationBean> searchPoiList;
    private static Animation hyperspaceJumpAnimation = null;
    private LocationBean mLocationBean;
    // 定位poi地名信息数据源
    private List<PoiInfo> aroundPoiList;
    private AroundPoiAdapter mAroundPoiAdapter;
    // 搜索模块，也可去掉地图模块独立使用
    private Marker mMarker = null;
    private SearchPOIAdapter mSearchPoiAdapter;
    private BaiduMap mBaiduMap;
    // 控件
    private MapView mMapView;
    private EditText etMLCityPoi;
    BaiduMap.OnMapClickListener mapOnClickListener = new BaiduMap.OnMapClickListener() {
        /**
         * 地图单击事件回调函数
         *
         * @param point
         *            点击的地理坐标
         */
        public void onMapClick(LatLng point) {
            hideSoftinput(mContext);
        }

        /**
         * 地图内 Poi 单击事件回调函数
         *
         * @param poi
         *            点击的 poi 信息
         */
        public boolean onMapPoiClick(MapPoi poi) {
            return false;
        }
    };
    private LinearLayout llMLMain;
    private ListView lvAroundPoi, lvSearchPoi;
    private ImageView ivMLPLoading;
    Handler loadingHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0: {
                    // if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
                    // mLoadingDialog.dismiss();
                    // // showToast(mActivity.getString(R.string.map_locate_fault),
                    // // DialogType.LOAD_FAILURE);
                    // }
                    if (ivMLPLoading != null) {
                        ivMLPLoading.clearAnimation();
                        ivMLPLoading.setVisibility(View.GONE);
                    }
                    break;
                }
                case 1: {
                    // 加载动画
                    hyperspaceJumpAnimation = AnimationUtils.loadAnimation(
                            mContext, R.anim.loading_animation);
                    lvAroundPoi.setVisibility(View.GONE);
                    ivMLPLoading.setVisibility(View.VISIBLE);
                    // 使用ImageView显示动画
                    ivMLPLoading.startAnimation(hyperspaceJumpAnimation);
                    if (ivMLPLoading != null
                            && ivMLPLoading.getVisibility() == View.VISIBLE) {
                        loadingHandler.sendEmptyMessageDelayed(0, DELAY_DISMISS);
                    }
                    break;
                }
                default:
                    break;
            }
        }
    };
    private Button btMapZoomIn, btMapZoomOut;
    private ImageButton ibMLLocate;
    private ImageButton head_ib;
    private boolean isCanUpdateMap = true;
    BaiduMap.OnMapStatusChangeListener mapStatusChangeListener = new BaiduMap.OnMapStatusChangeListener() {
        /**
         * 手势操作地图，设置地图状态等操作导致地图状态开始改变。
         *
         * @param status
         *            地图状态改变开始时的地图状态
         */
        public void onMapStatusChangeStart(MapStatus status) {
        }

        /**
         * 地图状态变化中
         *
         * @param status
         *            当前地图状态
         */
        public void onMapStatusChange(MapStatus status) {
        }

        /**
         * 地图状态改变结束
         *
         * @param status
         *            地图状态改变结束后的地图状态
         */
        public void onMapStatusChangeFinish(MapStatus status) {
            if (isCanUpdateMap) {
                LatLng ptCenter = new LatLng(status.target.latitude,
                        status.target.longitude);
                // 反Geo搜索
                reverseGeoCode(ptCenter, true);
                if (ivMLPLoading != null
                        && ivMLPLoading.getVisibility() == View.GONE) {
                    loadingHandler.sendEmptyMessageDelayed(1, 0);
                }
            } else {
                isCanUpdateMap = true;
            }
        }
    };
    private TextView head_tv;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.mapview_location_poi);
        mContext = this;
        ibMLLocate = (ImageButton) findViewById(R.id.ibMLLocate);
        etMLCityPoi = (EditText) findViewById(R.id.etMLCityPoi);
        lvAroundPoi = (ListView) findViewById(R.id.lvPoiList);
        lvSearchPoi = (ListView) findViewById(R.id.lvMLCityPoi);
        ivMLPLoading = (ImageView) findViewById(R.id.ivMLPLoading);
        btMapZoomIn = (Button) findViewById(R.id.btMapZoomIn);
        btMapZoomOut = (Button) findViewById(R.id.btMapZoomOut);
        llMLMain = (LinearLayout) findViewById(R.id.llMLMain);
        // 地图初始化
        mMapView = (MapView) findViewById(R.id.mMapView);
        BaiduMapUtil.goneMapViewChild(mMapView, true, true);
        mBaiduMap = mMapView.getMap();
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(16));
        mBaiduMap.setOnMapStatusChangeListener(mapStatusChangeListener);
        mBaiduMap.setOnMapClickListener(mapOnClickListener);
        mBaiduMap.getUiSettings().setZoomGesturesEnabled(true);// 缩放手势
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        Intent in = getIntent();
        String long_and_lat = in.getStringExtra("long_and_lat");
        if (long_and_lat != null) {
            int index = long_and_lat.indexOf(",");
            String _long = long_and_lat.substring(0, index);
            String _lat = long_and_lat.substring(index + 1,
                    long_and_lat.length());
            BaiduMapUtil.moveToTarget(Double.parseDouble(_lat),
                    Double.parseDouble(_long), mBaiduMap);
            reverseGeoCode(
                    new LatLng(Double.parseDouble(_lat),
                            Double.parseDouble(_long)), false);
        } else {
            locate();
        }
        iniEvent();
    }

    // 显示地图界面亦或搜索结果界面
    private void showMapOrSearch(int index) {
        if (index == SHOW_SEARCH_RESULT) {
            llMLMain.setVisibility(View.GONE);
            lvSearchPoi.setVisibility(View.VISIBLE);
        } else {
            lvSearchPoi.setVisibility(View.GONE);
            llMLMain.setVisibility(View.VISIBLE);
            if (searchPoiList != null) {
                searchPoiList.clear();
            }
        }
    }

    public void locate() {
        BaiduMapUtil.locateByBaiduMap(mContext, 2000,
                new BaiduMapUtil.LocateListener() {
                    @Override
                    public void onLocateSucceed(LocationBean locationBean) {
                        mLocationBean = locationBean;
                        if (mMarker != null) {
                            mMarker.remove();
                        } else {
                            if (mBaiduMap != null)
                                mBaiduMap.clear();
                        }
                        mMarker = BaiduMapUtil.showMarkerByResource(
                                locationBean.getLatitude(),
                                locationBean.getLongitude(), R.mipmap.point,
                                mBaiduMap, 0, true);
                    }

                    @Override
                    public void onLocateFiled() {

                    }

                    @Override
                    public void onLocating() {

                    }
                });
    }

    public void getPoiByPoiSearch() {
        try {
            BaiduMapUtil.getPoiByPoiSearch(mLocationBean.getCity(),
                    etMLCityPoi.getText().toString().trim(), 0,
                    new BaiduMapUtil.PoiSearchListener() {

                        // 通过关键字查询地址返回的结果
                        @Override
                        public void onGetSucceed(List<LocationBean> locationList,
                                                 PoiResult res) {
                            if (etMLCityPoi.getText().toString().trim().length() > 0) {
                                if (searchPoiList == null) {
                                    searchPoiList = new ArrayList<LocationBean>();
                                }
                                searchPoiList.clear();
                                searchPoiList.addAll(locationList);
                                updateCityPoiListAdapter();
                            }
                        }

                        @Override
                        public void onGetFailed() {
                            Toast.makeText(mContext, "抱歉，未能找到结果",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void reverseGeoCode(LatLng ll, final boolean isShowTextView) {
        BaiduMapUtil.getPoisByGeoCode(ll.latitude, ll.longitude,
                new BaiduMapUtil.GeoCodePoiListener() {

                    @Override
                    public void onGetSucceed(LocationBean locationBean,
                                             List<PoiInfo> poiList) {
                        mLocationBean = (LocationBean) locationBean.clone();
                        // Toast.makeText(
                        // mContext,
                        // mLocationBean.getProvince() + "-"
                        // + mLocationBean.getCity() + "-"
                        // + mLocationBean.getDistrict() + "-"
                        // + mLocationBean.getStreet() + "-"
                        // + mLocationBean.getStreetNum(),
                        // Toast.LENGTH_SHORT).show();
                        // mBaiduMap.setMapStatus(MapStatusUpdateFactory
                        // .newLatLng(new LatLng(locationBean
                        // .getLatitude(), locationBean
                        // .getLongitude())));
                        if (isShowTextView) {
                        }
                        if (aroundPoiList == null) {
                            aroundPoiList = new ArrayList<PoiInfo>();
                        }
                        aroundPoiList.clear();
                        if (poiList != null) {
                            aroundPoiList.addAll(poiList);
                        } else {
                            Toast.makeText(mContext, "該周邊沒有熱點",
                                    Toast.LENGTH_SHORT).show();
                        }
                        updatePoiListAdapter(aroundPoiList, -1);
                    }

                    @Override
                    public void onGetFailed() {
                        Toast.makeText(mContext, "抱歉，未能找到结果",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void iniEvent() {
        etMLCityPoi.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (etMLCityPoi.getText().toString().trim().length() > 0) {
                    getPoiByPoiSearch();
                }
            }
        });
        etMLCityPoi.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int start, int before,
                                      int count) {
                if (cs.toString().trim().length() > 0) {
                    getPoiByPoiSearch();
                } else {
                    if (searchPoiList != null) {
                        searchPoiList.clear();
                    }
                    showMapOrSearch(SHOW_MAP);
                    hideSoftinput(mContext);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        ibMLLocate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                locate();
            }
        });
        btMapZoomIn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                isCanUpdateMap = false;
                BaiduMapUtil.zoomInMapView(mMapView);
            }
        });
        btMapZoomOut.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                isCanUpdateMap = false;
                BaiduMapUtil.zoomOutMapView(mMapView);
            }
        });
        lvAroundPoi.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                isCanUpdateMap = false;

                final PoiInfo poi = aroundPoiList.get(arg2);
                String address = poi.name + "(" + poi.address + ")";// listview点击获取名称
                String jingweidu = poi.location.longitude + ","
                        + poi.location.latitude;// listview点击获取经纬度

//                    Intent in1 = new Intent(POIListActivity.this,
//                            AppendAddressAcivity.class);
//                    in1.putExtra("address", address);
//                    in1.putExtra("long_and_lat", jingweidu);
//				startActivityForResult(in1, GO_APPEND_DETAILS_ACTIVITY);
//                    setResult(3,in1);
//                    finish();
                BaiduMapUtil.moveToTarget(poi.location.latitude,
                        poi.location.longitude, mBaiduMap);
            }
        });
        lvSearchPoi.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                // Geo搜索
                // mGeoCoder.geocode(new GeoCodeOption().city(
                // searchPoiList.get(arg2).getCity()).address(
                // searchPoiList.get(arg2).getLocName()));
                hideSoftinput(mContext);
                isCanUpdateMap = false;
                LocationBean poi = searchPoiList.get(arg2);

                String address = poi.getLocName() + "(" + poi.getAddStr() + ")";// listview点击获取名称
                String jingweidu = poi.getLongitude() + "," + poi.getLatitude();// listview点击获取经纬度
//                    Intent in2 = new Intent(POIListActivity.this,
//                            AppendAddressAcivity.class);
//                    in2.putExtra("address", address);
//                    in2.putExtra("long_and_lat", jingweidu);
//                    setResult(3,in2);
//                    finish();
                BaiduMapUtil.moveToTarget(poi.getLatitude(), poi.getLongitude(), mBaiduMap);
                // 反Geo搜索
                reverseGeoCode(
                        new LatLng(poi.getLatitude(), poi.getLongitude()),
                        false);
                if (ivMLPLoading != null
                        && ivMLPLoading.getVisibility() == View.GONE) {
                    loadingHandler.sendEmptyMessageDelayed(1, 0);
                }
                showMapOrSearch(SHOW_MAP);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
//            if (requestCode == GO_APPEND_DETAILS_ACTIVITY
//                    && resultCode == BACK_APPEND_DETAILS_ACTIVITY) {
//                setResult(BACK_ADDRESS_ADD_ACTIVITY, data);
//                finish();
//
//            }
    }

    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mMapView.onResume();
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        if (llMLMain.getVisibility() == View.GONE) {
            showMapOrSearch(SHOW_MAP);
        } else {
            this.finish();
        }
    }

    /**
     * 隐藏软键盘
     */
    private void hideSoftinput(Context mContext) {
        InputMethodManager manager = (InputMethodManager) mContext
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (manager.isActive()) {
            manager.hideSoftInputFromWindow(etMLCityPoi.getWindowToken(), 0);
        }
    }

    // 刷新热门地名列表界面的adapter
    private void updatePoiListAdapter(List<PoiInfo> list, int index) {
        ivMLPLoading.clearAnimation();
        ivMLPLoading.setVisibility(View.GONE);
        lvAroundPoi.setVisibility(View.VISIBLE);
        if (mAroundPoiAdapter == null) {
            mAroundPoiAdapter = new AroundPoiAdapter(mContext, list);
            lvAroundPoi.setAdapter(mAroundPoiAdapter);
        } else {
            mAroundPoiAdapter.setNewList(list, index);
        }
    }

    // 刷新当前城市兴趣地点列表界面的adapter
    private void updateCityPoiListAdapter() {
        if (mSearchPoiAdapter == null) {
            mSearchPoiAdapter = new SearchPOIAdapter(mContext, searchPoiList);
            lvSearchPoi.setAdapter(mSearchPoiAdapter);
        } else {
            mSearchPoiAdapter.notifyDataSetChanged();
        }
        showMapOrSearch(SHOW_SEARCH_RESULT);
    }

    @Override
    protected void onDestroy() {
        mLocationBean = null;
        lvAroundPoi = null;
        lvSearchPoi = null;
        btMapZoomIn.setBackgroundResource(0);
        btMapZoomIn = null;
        btMapZoomOut.setBackgroundResource(0);
        btMapZoomOut = null;
        ibMLLocate.setImageBitmap(null);
        ibMLLocate.setImageResource(0);
        ibMLLocate = null;
        if (aroundPoiList != null) {
            aroundPoiList.clear();
            aroundPoiList = null;
        }
        mAroundPoiAdapter = null;
        if (searchPoiList != null) {
            searchPoiList.clear();
            searchPoiList = null;
        }
        mSearchPoiAdapter = null;
        if (mBaiduMap != null) {
            mBaiduMap.setMyLocationEnabled(false);// 关闭定位图层
            mBaiduMap = null;
        }
        if (mMapView != null) {
            mMapView.destroyDrawingCache();
            mMapView.onDestroy();
            mMapView = null;
        }
        if (etMLCityPoi != null) {
            etMLCityPoi.setBackgroundResource(0);
            etMLCityPoi = null;
        }
        mMarker = null;
        super.onDestroy();
        System.gc();
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
