package com.taihuoniao.fineix.zone;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

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
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSortType;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.BDAddressListAdapter;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.map.MapAddressDetailActivity;
import com.taihuoniao.fineix.utils.MapUtil;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.utils.Util;
import com.taihuoniao.fineix.utils.WindowUtils;
import com.taihuoniao.fineix.view.CustomHeadView;
import com.taihuoniao.fineix.view.dialog.WaittingDialog;
import com.taihuoniao.fineix.zone.bean.ZoneDetailBean;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionNo;
import com.yanzhenjie.permission.PermissionYes;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

import static com.taihuoniao.fineix.utils.Constants.REQUEST_CODE_SETTING;

/**
 * @author lilin
 * 地盘位置选择
 */
public class ZoneMapSelectAddressActivity extends BaseActivity implements View.OnClickListener {
    private static final int REQUEST_CODE= 100;
    private static final int REQUEST_ADDRESS = 101;
    @Bind(R.id.custom_head)
    CustomHeadView custom_head;
    @Bind(R.id.search_view)
    EditText search_view;
    @Bind(R.id.ll)
    ListView ll;
    @Bind(R.id.ibtn)
    ImageButton ibtn;
    @Bind(R.id.mapView)
    MapView mapView;
    private BaiduMap mBDMap;
    private ArrayList<PoiInfo> list;
    private BDAddressListAdapter adapter;
    private boolean isFirstLoc = true;
    private int pageNum = 0;
    private PoiSortType sortType = PoiSortType.distance_from_near_to_far; //默认排序类型
    private LatLng latLng;
    private BitmapDescriptor bitmapDescripter;
    private WaittingDialog dialog;
    private ZoneDetailBean zoneDetailBean;
    //当前位置的市和区
    private String city, district;


    public ZoneMapSelectAddressActivity() {
        super(R.layout.activity_zone_select_address);
    }

    @Override
    protected void getIntentData() {
        Intent intent = getIntent();
        if (intent!=null){
            zoneDetailBean = intent.getParcelableExtra(TAG);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (AndPermission.hasPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)){
            displayCurrentLocation();
        } else {
            AndPermission.with(this)
                    .requestCode(REQUEST_CODE)
                    .permission(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
                    .send();
        }
        WindowUtils.chenjin(this);
    }

    private void displayCurrentLocation() {
        MapUtil.getCurrentLocation(new MapUtil.OnReceiveLocationListener() {
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
                    loadAndshowGeoCoderResult(latLng);
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        // 只需要调用这一句，第一个参数是当前Acitivity/Fragment，回调方法写在当前Activity/Framgent。
        AndPermission.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    // 成功回调的方法，用注解即可，里面的数字是请求时的requestCode。
    @PermissionYes(REQUEST_CODE)
    private void getRequestYes(List<String> grantedPermissions) {
        displayCurrentLocation();
    }

    // 失败回调的方法，用注解即可，里面的数字是请求时的requestCode。
    @PermissionNo(REQUEST_CODE)
    private void getPhoneStatusNo(List<String> deniedPermissions) {
        // 用户否勾选了不再提示并且拒绝了权限，那么提示用户到设置中授权。
        if (AndPermission.hasAlwaysDeniedPermission(this, deniedPermissions)) {
            // 第一种：用默认的提示语。
            AndPermission.defaultSettingDialog(this,REQUEST_CODE_SETTING).show();
        }else {
//            finish();
        }
    }

    private void loadAndshowGeoCoderResult(LatLng latLng) {
        if (latLng == null)
            return;
        if (!activity.isFinishing() && dialog != null) dialog.show();
        MapUtil.getAddressByCoordinate(latLng, new MapUtil.MyOnGetGeoCoderResultListener() {
            @Override
            public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
                if (!activity.isFinishing() && dialog != null) dialog.dismiss();
                if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                    return;
                }
                city = result.getAddressDetail().city;
                district = result.getAddressDetail().district;
                if (list == null) {
                    list = new ArrayList<>();
                } else {
                    list.clear();
                }
                if(result.getPoiList()!=null){
                    list.addAll(result.getPoiList());
                }
                if (list.size() > 0) {
                    if (adapter == null) {
                        adapter = new BDAddressListAdapter(activity, list);
                        ll.setAdapter(adapter);
                    } else {
                        adapter.notifyDataSetChanged();
                    }
                    addOverlayer(list);
                } else {
                    ToastUtils.showError("抱歉,没有检索到结果!");
                }
            }

            @Override
            public void onGetGeoCodeResult(GeoCodeResult result) {
            }
        });
    }

    private void addOverlayer(final List<PoiInfo> list) {
        if (list == null) return;
        if (list.size() == 0) return;
        mBDMap.clear();
        PoiInfo poiInfo = list.get(0);
        bitmapDescripter = BitmapDescriptorFactory.fromResource(R.mipmap.icon_marker3);
        LatLng ll = new LatLng(poiInfo.location.latitude, poiInfo.location.longitude);
        MapStatus.Builder builder = new MapStatus.Builder();
        builder.target(ll).zoom(15.0f);
        mBDMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
        MarkerOptions option = new MarkerOptions().position(ll).icon(bitmapDescripter);
        option.animateType(MarkerOptions.MarkerAnimateType.grow);
        mBDMap.addOverlay(option);
    }

    @Override
    protected void initView() {
        custom_head.setHeadCenterTxtShow(true,R.string.title_zone_select_address);
        dialog = new WaittingDialog(this);
        custom_head.setHeadGoBackShow(false);
        custom_head.setIvLeft(R.mipmap.current_location);
        custom_head.setHeadRightTxtShow(true, R.string.cancel);
        mapView.showZoomControls(false);
        mBDMap = mapView.getMap();
        mBDMap.setMyLocationEnabled(true);

    }

    @Override
    protected void installListener() {
        search_view.addTextChangedListener(tw);

        search_view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    String keyWord = search_view.getText().toString().trim();
                    if (!TextUtils.isEmpty(keyWord)) {
                        loadAndshowPoiResult(keyWord, latLng);
                    }
                }
                return false;
            }
        });
        ll.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (city == null || district == null) {
                    return;
                }
                Intent intent = new Intent();
                intent.putExtra(PoiInfo.class.getSimpleName(), list.get(i));
                intent.putExtra("city", city);
                intent.putExtra("district", district);
                setResult(RESULT_OK, intent);
//                setResult(DataConstants.RESULTCODE_CREATESCENE_BDSEARCH, intent);
                finish();
            }
        });
        ibtn.setOnClickListener(this);
        custom_head.getIvLeft().setOnClickListener(this);
        custom_head.getHeadRightTV().setOnClickListener(this);
        mBDMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                Intent intent = new Intent(activity, MapAddressDetailActivity.class);
                if (list != null && list.size() > 0) {
                    intent.putExtra(MapAddressDetailActivity.class.getSimpleName(), list.get(0));
                }
                startActivityForResult(intent, REQUEST_ADDRESS);
            }

            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {
                return false;
            }
        });
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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ibtn:
                search_view.getText().clear();
                ibtn.setVisibility(View.GONE);
                break;
            case R.id.tv_head_right:
                finish();
            case R.id.iv_left:
                if (mBDMap == null) return;
                mBDMap.clear();
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(latLng).zoom(15.0f);
                mBDMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
                loadAndshowGeoCoderResult(latLng);
                break;
        }
    }

    private TextWatcher tw = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence cs, int start, int before, int count) {
            String keyWord = cs.toString().trim();
            if (!TextUtils.isEmpty(keyWord)) {
                ibtn.setVisibility(View.VISIBLE);
//                pageNum = 0;
//                loadAndshowPoiResult(keyWord, latLng);
            } else {
                ibtn.setVisibility(View.GONE);
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    private void loadAndshowPoiResult(String keyWord, LatLng latLng) {
        if (!activity.isFinishing() && dialog != null) dialog.show();
        int radius = 1000;
        int pageCapacity = 10;
        MapUtil.getPoiNearbyByKeyWord(keyWord, latLng, radius, pageNum, pageCapacity, sortType, new MapUtil.MyOnGetPoiSearchResultListener() {
            @Override
            public void onGetPoiResult(PoiResult result) {
                if (!activity.isFinishing() && dialog != null) dialog.dismiss();
                if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                    return;
                }
                pageNum++;
                if (list == null) {
                    list = new ArrayList<>();
                } else {
                    list.clear();
                }
                list.addAll(result.getAllPoi());
                if (list.size() > 0) {
                    if (adapter == null) {
                        adapter = new BDAddressListAdapter(activity, list);
                        ll.setAdapter(adapter);
                    } else {
                        adapter.notifyDataSetChanged();
                    }
                } else {
                    Util.makeToast(activity, "抱歉,没有检索到结果！");
                }
            }

            @Override
            public void onGetPoiDetailResult(PoiDetailResult result) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;
        if (requestCode == REQUEST_ADDRESS) {
            ArrayList<PoiInfo> list = data.getParcelableArrayListExtra("list");
            this.list.clear();
            this.list.addAll(list);
            adapter.notifyDataSetChanged();
            addOverlayer(list);
        }
    }

    @Override
    protected void onDestroy() {
        MapUtil.destroyGeoCoder();
        MapUtil.destroyPoiSearch();
        MapUtil.destroyLocationClient();
        if (bitmapDescripter != null) {
            bitmapDescripter.recycle();
        }
        mapView.onDestroy();
        super.onDestroy();
    }

}
