package com.taihuoniao.fineix.map;

import android.Manifest;
import android.content.Intent;
import android.text.TextUtils;
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
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.base.HttpRequest;
import com.taihuoniao.fineix.beans.HttpResponse;
import com.taihuoniao.fineix.beans.SceneListBean;
import com.taihuoniao.fineix.beans.UserCJListData;
import com.taihuoniao.fineix.common.GlobalDataCallBack;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.URL;
import com.taihuoniao.fineix.utils.GlideUtils;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.LogUtil;
import com.taihuoniao.fineix.utils.MapUtil;
import com.taihuoniao.fineix.utils.Util;
import com.taihuoniao.fineix.view.CustomHeadView;
import com.taihuoniao.fineix.view.dialog.WaittingDialog;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionNo;
import com.yanzhenjie.permission.PermissionYes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

import static com.taihuoniao.fineix.utils.Constants.REQUEST_CODE_SETTING;
import static com.taihuoniao.fineix.utils.Constants.REQUEST_PHONE_STATE_CODE;

/**
 * @author lilin
 *         created at 2016/4/15 16:13
 */
public class MapNearByCJActivity extends BaseActivity<SceneListBean> {
    @Bind(R.id.custom_head)
    CustomHeadView custom_head;
    @Bind(R.id.mv)
    MapView mv;
    private BaiduMap mBDMap;
    private boolean isFirstLoc = true;
    private BitmapDescriptor bitmapDescripter;
    private WaittingDialog waittingDialog;
    private LatLng ll;
    private String address;

    public MapNearByCJActivity() {
        super(R.layout.activity_nearby_cj);
    }

    @Override
    protected void getIntentData() {
        Intent intent = getIntent();
        if (intent.hasExtra(MapNearByCJActivity.class.getSimpleName())) {
            ll = intent.getParcelableExtra(TAG);
        }

        if (intent.hasExtra("address")) {
            address = intent.getStringExtra("address");
        }

    }

    @Override
    protected void initView() {
        if (!TextUtils.isEmpty(address)) {
            custom_head.setHeadCenterTxtShow(true, address);
        }
        waittingDialog = new WaittingDialog(this);
        mv.showZoomControls(false);
        mBDMap = mv.getMap();
        if (AndPermission.hasPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)) {
            loadCurrentData();
        }else {
            // 申请权限。
            AndPermission.with(activity)
                    .requestCode(REQUEST_PHONE_STATE_CODE)
                    .permission(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
                    .send();
        }
    }

    private void move2CurrentLocation() {
        MapStatus.Builder builder = new MapStatus.Builder();
        builder.target(ll).zoom(18);
        mBDMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
    }


    private void loadCurrentData() {
        if (ll != null) {
            move2CurrentLocation();
            getNearByData(ll);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        // 只需要调用这一句，第一个参数是当前Acitivity/Fragment，回调方法写在当前Activity/Framgent。
        AndPermission.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    // 成功回调的方法，用注解即可，里面的数字是请求时的requestCode。
    @PermissionYes(REQUEST_PHONE_STATE_CODE)
    private void getPhoneStatusYes(List<String> grantedPermissions) {
        loadCurrentData();
    }

    // 失败回调的方法，用注解即可，里面的数字是请求时的requestCode。
    @PermissionNo(REQUEST_PHONE_STATE_CODE)
    private void getPhoneStatusNo(List<String> deniedPermissions) {
        // 用户否勾选了不再提示并且拒绝了权限，那么提示用户到设置中授权。
        if (AndPermission.hasAlwaysDeniedPermission(this, deniedPermissions)) {
            // 第一种：用默认的提示语。
            AndPermission.defaultSettingDialog(this, REQUEST_CODE_SETTING).show();
        } else {
            activity.finish();
        }
    }

    @OnClick({R.id.btn, R.id.ibtn})
    void performClick(View v) {
        switch (v.getId()) {
            case R.id.btn: //重新搜索
                mBDMap.clear();
                loadCurrentData();
                break;
            case R.id.ibtn: //重新定位当前经纬度
                move2CurrentLocation();
                break;
        }
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
        MapUtil.destroyLocationClient();
        mv.onDestroy();
        if (bitmapDescripter != null) {
            bitmapDescripter.recycle();
        }
        super.onDestroy();
    }

    private void getNearByData(LatLng ll) {//附近的所有情境
        int page = 1;
        int pageSize = 100;
        int radius = 5000;
        HashMap<String, String> sceneListRequestParams = ClientDiscoverAPI.getSceneListRequestParams(String.valueOf(page), String.valueOf(pageSize), null, null, null, null, String.valueOf(ll.longitude), String.valueOf(ll.latitude));
        HttpRequest.post(sceneListRequestParams, URL.SCENE_LIST, new GlobalDataCallBack() {
            @Override
            public void onStart() {
                if (waittingDialog != null && !activity.isFinishing()) waittingDialog.show();
            }

            @Override
            public void onSuccess(String json) {
                if (waittingDialog != null) waittingDialog.dismiss();
                HttpResponse<UserCJListData> response;
                try {
                    response = JsonUtil.json2Bean(json, new TypeToken<HttpResponse<UserCJListData>>() {
                    });
                    if (response == null) return;
                    if (response.isSuccess()) {
                        List<SceneListBean> rows = response.getData().rows;
                        refreshUI(rows);
                    }
                } catch (JsonSyntaxException e) {
                    Util.makeToast(activity, "对不起,数据异常");
                }
            }

            @Override
            public void onFailure(String error) {
                if (waittingDialog != null) waittingDialog.dismiss();
                LogUtil.e(TAG, ">>>"+error);
            }
        });
    }

    private void addOverlayers(final List<SceneListBean> list) {
        bitmapDescripter = BitmapDescriptorFactory.fromResource(R.mipmap.icon_marker3);
        LatLng ll;
        MarkerOptions option;
        final ArrayList<Marker> markers = new ArrayList<>();
        for (SceneListBean item : list) {
            ll = new LatLng(item.location.coordinates.get(1), item.location.coordinates.get(0));
            option = new MarkerOptions().position(ll).icon(bitmapDescripter);
            option.animateType(MarkerOptions.MarkerAnimateType.grow);
            Marker marker = (Marker) mBDMap.addOverlay(option);
            markers.add(marker);
            if (this.ll.longitude == ll.longitude && this.ll.latitude == ll.latitude) {
                showInfoWindow(marker.getPosition(), item);
            }
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

                if (which == -1) {
                    return true;
                }
                SceneListBean item = list.get(which);
                if (item == null) {
                    return true;
                }
                showInfoWindow(marker.getPosition(), item);
                return true;
            }
        });
    }

    private void showInfoWindow(LatLng ll, SceneListBean item) {
        View view = Util.inflateView(R.layout.info_window_layout, null);
        view.findViewById(R.id.btn_navi).setVisibility(View.GONE);
        GlideUtils.displayImage(item.getCover_url(), ((ImageView) view.findViewById(R.id.iv)));
        ((TextView) view.findViewById(R.id.tv_desc)).setText(item.getScene_title());
        ((TextView) view.findViewById(R.id.tv_location)).setText(item.getAddress());
        InfoWindow mInfoWindow = new InfoWindow(BitmapDescriptorFactory.fromView(view), ll, -90, infoWindowClickListener);
        mBDMap.showInfoWindow(mInfoWindow);
    }

    InfoWindow.OnInfoWindowClickListener infoWindowClickListener = new InfoWindow.OnInfoWindowClickListener() {
        public void onInfoWindowClick() {
//          Util.makeToast(activity, "InfoWindow被点击");
        }
    };

    @Override
    protected void refreshUI(List<SceneListBean> list) {
        if (list == null) return;

        if (list.size() == 0) {
            Util.makeToast(activity, "暂无数据");
            return;
        }
        addOverlayers(list);
    }
}
