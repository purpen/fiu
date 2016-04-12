package com.taihuoniao.fineix.utils;
import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.CityInfo;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
/**
 * @author lilin
 *         created at 2016/4/12 14:52
 */
public class MapUtil {
    private static GeoCoder mSearch;
    private static PoiSearch mPoiSearch;
    private static OnGetReverseGeoCodeResultListener listener=null;
    public interface OnGetReverseGeoCodeResultListener{
        void onGetReverseGeoCodeResult(ReverseGeoCodeResult result);
    }

    /**
     *根据纬度经度获取地址
     * @param lat
     * @param lon
     * @param listener
     */
    public static void getAddressByCoordinate(double lat, double lon,OnGetReverseGeoCodeResultListener listener) {
        LatLng latLng = new LatLng(lat, lon);
        getAddressByCoordinate(latLng,listener);
    }

    /**
     * 根据纬度经度获取地址
     * @param latLng
     * @param listener
     */
    public static void getAddressByCoordinate(LatLng latLng,final OnGetReverseGeoCodeResultListener listener) {
        MapUtil.listener=listener;
        // 初始化搜索模块，注册事件监听
        mSearch = GeoCoder.newInstance();

        mSearch.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
            @Override
            public void onGetGeoCodeResult(GeoCodeResult result) {
                if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                    LogUtil.e("onGetReverseGeoCodeResult","抱歉，未能找到结果");
                    return;
                }
//                String strInfo = String.format("纬度：%f 经度：%f",
//                        result.getLocation().latitude, result.getLocation().longitude);
////                Toast.makeText(activity, strInfo, Toast.LENGTH_LONG).show();
//                LogUtil.e("onGetReverseGeoCodeResult",strInfo+"strInfo");
            }

            @Override
            public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
                if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
//                    Toast.makeText(activity, "抱歉，未能找到结果", Toast.LENGTH_LONG)
//                            .show();
                    LogUtil.e("onGetReverseGeoCodeResult","抱歉，未能找到结果");
                    return;
                }

                if (listener!=null){
                    listener.onGetReverseGeoCodeResult(result);
                }
//                LogUtil.e("onGetReverseGeoCodeResult",result.getAddress()+"getAddress");
//                List<PoiInfo> list = result.getPoiList();
//                for (PoiInfo info: list) {
//                    LogUtil.e("address",info.address);
//                }
            }
        });
        // 反Geo搜索
        mSearch.reverseGeoCode(new ReverseGeoCodeOption()
                .location(latLng));
    }

    public static void destroyGeoCoder() {
        if (mSearch != null)
            mSearch.destroy();
    }

    /**
     *@param activity
     * @param lat
     * @param lon
     */
    public static void getPOIByCoordinate(final Activity activity,double lat, double lon){
        // 初始化搜索模块，注册搜索事件监听
        mPoiSearch = PoiSearch.newInstance();
//        mPoiSearch.searchNearby(new PoiNearbySearchOption().location(new LatLng(lat,lon)));
        mPoiSearch.setOnGetPoiSearchResultListener(new OnGetPoiSearchResultListener() {
            @Override
            public void onGetPoiResult(PoiResult result) {
                if (result == null
                        || result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
                    Toast.makeText(activity, "未找到结果", Toast.LENGTH_LONG)
                            .show();
                    return;
                }

                if (result.error == SearchResult.ERRORNO.AMBIGUOUS_KEYWORD) {

                    // 当输入关键字在本市没有找到，但在其他城市找到时，返回包含该关键字信息的城市列表
                    String strInfo = "在";
                    for (CityInfo cityInfo : result.getSuggestCityList()) {
                        strInfo += cityInfo.city;
                        strInfo += ",";
                    }
                    strInfo += "找到结果";
                    Toast.makeText(activity, strInfo, Toast.LENGTH_LONG)
                            .show();
                }
            }

            @Override
            public void onGetPoiDetailResult(PoiDetailResult result) {
                if (result.error != SearchResult.ERRORNO.NO_ERROR) {
                    Toast.makeText(activity, "抱歉，未找到结果", Toast.LENGTH_SHORT)
                            .show();
                } else {
                    Toast.makeText(activity, result.getName() + ": " + result.getAddress(), Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });
    }


    public static void destroyPoiSearch() {
        if (mPoiSearch != null)
            mPoiSearch.destroy();
    }
}
