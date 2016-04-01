package com.taihuoniao.fineix.map;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
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
import android.widget.Toast;
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
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.beans.LocationBean;
import com.taihuoniao.fineix.utils.BaiduMapUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;


/**
 * @author lilin
 * created at 2016/3/28 13:32
 */
public class POIListActivity extends BaseActivity implements OnGetPoiSearchResultListener, OnGetSuggestionResultListener {
    @Bind(R.id.ibMLLocate)
    ImageButton ibMLLocate;
    @Bind(R.id.et_search_box)
    EditText et_search_box;
    @Bind(R.id.lvPoiList)
    ListView lvPoiList;
    @Bind(R.id.lv_search_result)
    ListView lv_search_result;
    @Bind(R.id.ivMLPLoading)
    ImageView ivMLPLoading;
    @Bind(R.id.btMapZoomIn)
    Button btMapZoomIn;
    @Bind(R.id.btMapZoomOut)
    Button btMapZoomOut;
    @Bind(R.id.llMLMain)
    LinearLayout llMLMain;
    @Bind(R.id.mMapView)
    MapView mMapView;

    public static final int SHOW_MAP = 0;
    private static final int SHOW_SEARCH_RESULT = 1;

    private static final int LOADING_DATA=0;

    private static final int LOADING_COMPLETE=0;

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
    private boolean isCanUpdateMap = true;

    Handler loadingHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0: {//加载完成
                    if (ivMLPLoading != null) {
                        ivMLPLoading.clearAnimation();
                        ivMLPLoading.setVisibility(View.GONE);
                    }
                    break;
                }
                case 1: {//加载中
                    hyperspaceJumpAnimation = AnimationUtils.loadAnimation(
                            activity, R.anim.loading_animation);
                    lvPoiList.setVisibility(View.GONE);
                    ivMLPLoading.setVisibility(View.VISIBLE);
                    ivMLPLoading.startAnimation(hyperspaceJumpAnimation);
                    break;
                }
                default:
                    break;
            }
        }
    };

    public POIListActivity() {
        super(R.layout.mapview_location_poi);
    }

    @Override
    protected void getIntentData() {
        super.getIntentData();
    }

    @Override
    protected void initView() {
        BaiduMapUtil.goneMapViewChild(mMapView, true, true);
        mBaiduMap = mMapView.getMap();
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(16));
        mBaiduMap.getUiSettings().setZoomGesturesEnabled(true);// 缩放手势
        mBaiduMap.setMyLocationEnabled(true);


//            BaiduMapUtil.moveToTarget(Double.parseDouble(_lat),
//                    Double.parseDouble(_long), mBaiduMap);
//            reverseGeoCode(
//                    new LatLng(Double.parseDouble(_lat),
//                            Double.parseDouble(_long)), false);
        //如果上个界面有地址直接移动过去
        if (false){//TODO 暂时

        }else {
            locate();
        }
    }

    @Override
    protected void installListener() {
        mBaiduMap.setOnMapStatusChangeListener(mapStatusChangeListener);
        mBaiduMap.setOnMapClickListener(mapOnClickListener);

        et_search_box.setOnClickListener(new View.OnClickListener() {//点击搜索

            @Override
            public void onClick(View v) {
                if (et_search_box.getText().toString().trim().length() > 0) {
                    getPoiByPoiSearch();
                }
            }
        });
        et_search_box.addTextChangedListener(new TextWatcher() {//监听搜索内容

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
                    hideSoftinput();
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
        lvPoiList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

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

        lv_search_result.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                // Geo搜索
                // mGeoCoder.geocode(new GeoCodeOption().city(
                // searchPoiList.get(arg2).getCity()).address(
                // searchPoiList.get(arg2).getLocName()));
                hideSoftinput();
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

    BaiduMap.OnMapClickListener mapOnClickListener = new BaiduMap.OnMapClickListener() {
        public void onMapClick(LatLng point) {
            hideSoftinput();
        }

        public boolean onMapPoiClick(MapPoi poi) {
            return false;
        }
    };

    BaiduMap.OnMapStatusChangeListener mapStatusChangeListener = new BaiduMap.OnMapStatusChangeListener() {
        public void onMapStatusChangeStart(MapStatus status) {
        }

        public void onMapStatusChange(MapStatus status) {
        }

        public void onMapStatusChangeFinish(MapStatus status) {
            if (isCanUpdateMap) {
                LatLng ptCenter = new LatLng(status.target.latitude,
                        status.target.longitude);
                reverseGeoCode(ptCenter, true);
            } else {
                isCanUpdateMap = true;
            }
        }
    };

    private void showMapOrSearch(int index) {
        if (index == SHOW_SEARCH_RESULT) {//展示搜索结果
            llMLMain.setVisibility(View.GONE);
            lv_search_result.setVisibility(View.VISIBLE);
        } else {                        //展示地图图层
            lv_search_result.setVisibility(View.GONE);
            llMLMain.setVisibility(View.VISIBLE);
            if (searchPoiList != null) {
                searchPoiList.clear();
            }
        }
    }

    public void locate() {
        BaiduMapUtil.locateByBaiduMap(activity, 2000,
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
        loadingHandler.sendEmptyMessage(LOADING_DATA);
        try {
            BaiduMapUtil.getPoiByPoiSearch(mLocationBean.getCity(),
                    et_search_box.getText().toString().trim(), 0,
                    new BaiduMapUtil.PoiSearchListener() {

                        // 通过关键字查询地址返回的结果
                        @Override
                        public void onGetSucceed(List<LocationBean> locationList,
                                                 PoiResult res) {
                            loadingHandler.sendEmptyMessage(LOADING_COMPLETE);
                            if (et_search_box.getText().toString().trim().length() > 0) {
                                if (searchPoiList == null) {
                                    searchPoiList = new ArrayList<LocationBean>();
                                } else {
                                    searchPoiList.clear();
                                }
                                if (locationList != null && locationList.size() > 0) {
                                    searchPoiList.addAll(locationList);
                                    updateCityPoiListAdapter();
                                }
                            }
                        }

                        @Override
                        public void onGetFailed() {
                            Toast.makeText(activity, "抱歉，未能找到结果",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void reverseGeoCode(LatLng ll, final boolean isShowTextView) {
        loadingHandler.sendEmptyMessage(LOADING_DATA);
        BaiduMapUtil.getPoisByGeoCode(ll.latitude, ll.longitude,
                new BaiduMapUtil.GeoCodePoiListener() {
                    @Override
                    public void onGetSucceed(LocationBean locationBean,
                                             List<PoiInfo> poiList) {
                        loadingHandler.sendEmptyMessage(LOADING_COMPLETE);
                        mLocationBean = (LocationBean) locationBean.clone();
                        if (aroundPoiList == null) {
                            aroundPoiList = new ArrayList<PoiInfo>();
                        } else {
                            aroundPoiList.clear();
                        }
                        if (poiList != null) {
                            aroundPoiList.addAll(poiList);
                            updatePoiListAdapter(aroundPoiList, -1);
                        } else {
                            Toast.makeText(activity, "附近未搜索到数据",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onGetFailed() {
                        Toast.makeText(activity, "抱歉，未能搜索到数据",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//            if (requestCode == GO_APPEND_DETAILS_ACTIVITY
//                    && resultCode == BACK_APPEND_DETAILS_ACTIVITY) {
//                setResult(BACK_ADDRESS_ADD_ACTIVITY, data);
//                finish();
//
//            }
    }


    @Override
    public void onBackPressed() {
        if (llMLMain.getVisibility() == View.GONE) {
            showMapOrSearch(SHOW_MAP);
        } else {
            super.onBackPressed();
        }
    }

    private void hideSoftinput() {
        InputMethodManager manager = (InputMethodManager) activity
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (manager.isActive()) {
            manager.hideSoftInputFromWindow(et_search_box.getWindowToken(), 0);
        }
    }

    // 刷新热门地名列表界面的adapter
    private void updatePoiListAdapter(List<PoiInfo> list, int index) {
        ivMLPLoading.clearAnimation();
        ivMLPLoading.setVisibility(View.GONE);
        lvPoiList.setVisibility(View.VISIBLE);
        if (mAroundPoiAdapter == null) {
            mAroundPoiAdapter = new AroundPoiAdapter(activity, list);
            lvPoiList.setAdapter(mAroundPoiAdapter);
        } else {
            mAroundPoiAdapter.setNewList(list, index);
        }
    }

    // 刷新当前城市兴趣地点列表界面的adapter
    private void updateCityPoiListAdapter() {
        if (mSearchPoiAdapter == null) {
            mSearchPoiAdapter = new SearchPOIAdapter(activity, searchPoiList);
            if (lv_search_result != null) {
                lv_search_result.setAdapter(mSearchPoiAdapter);
            }
        } else {
            mSearchPoiAdapter.notifyDataSetChanged();
        }
        showMapOrSearch(SHOW_SEARCH_RESULT);
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
//        mLocationBean = null;
//        lvAroundPoi = null;
//        lvSearchPoi = null;
//        btMapZoomIn.setBackgroundResource(0);
//        btMapZoomIn = null;
//        btMapZoomOut.setBackgroundResource(0);
//        btMapZoomOut = null;
//        ibMLLocate.setImageBitmap(null);
//        ibMLLocate.setImageResource(0);
//        ibMLLocate = null;
        if (aroundPoiList != null) {
            aroundPoiList.clear();
            aroundPoiList = null;
        }
//        mAroundPoiAdapter = null;
        if (searchPoiList != null) {
            searchPoiList.clear();
            searchPoiList = null;
        }
//        mSearchPoiAdapter = null;
        if (mBaiduMap != null) {
            mBaiduMap.setMyLocationEnabled(false);// 关闭定位图层
            mBaiduMap = null;
        }
        if (mMapView != null) {
            mMapView.destroyDrawingCache();
            mMapView.onDestroy();
            mMapView = null;
        }
//        mMarker = null;
        BaiduMapUtil.destroyPoiSearch();
        BaiduMapUtil.destroySuggestion();
        BaiduMapUtil.destroyGeoCode();
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
