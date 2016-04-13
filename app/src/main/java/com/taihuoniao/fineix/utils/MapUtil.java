package com.taihuoniao.fineix.utils;
import android.util.Log;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.poi.PoiSortType;

/**
 * @author lilin
 *         created at 2016/4/12 14:52
 */
public class MapUtil {
    private static GeoCoder mGeoCoder;
    private static PoiSearch mPoiSearch;
    private static MyOnGetGeoCoderResultListener listener=null;
    public interface MyOnGetGeoCoderResultListener{
        void onGetReverseGeoCodeResult(ReverseGeoCodeResult result);
        void onGetGeoCodeResult(GeoCodeResult result);
    }

    /**
     *根据纬度经度获取地址
     * @param lat
     * @param lon
     * @param listener
     */
    public static void getAddressByCoordinate(double lat, double lon,MyOnGetGeoCoderResultListener listener) {
        LatLng latLng = new LatLng(lat, lon);
        getAddressByCoordinate(latLng,listener);
    }

    /**
     * 根据纬度经度获取地址
     * @param latLng
     * @param listener
     */
    public static void getAddressByCoordinate(LatLng latLng,final MyOnGetGeoCoderResultListener listener) {
        MapUtil.listener=listener;

        if (mGeoCoder==null){
            mGeoCoder = GeoCoder.newInstance();
        }

        mGeoCoder.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
            @Override
            public void onGetGeoCodeResult(GeoCodeResult result) {
//                if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
//                    LogUtil.e("onGetReverseGeoCodeResult","抱歉，未能找到结果");
//                    return;
//                }
//                String strInfo = String.format("纬度：%f 经度：%f",
//                        result.getLocation().latitude, result.getLocation().longitude);
////                Toast.makeText(activity, strInfo, Toast.LENGTH_LONG).show();
//                LogUtil.e("onGetReverseGeoCodeResult",strInfo+"strInfo");
                if (listener!=null){
                    listener.onGetGeoCodeResult(result);
                }
            }

            @Override
            public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
//                if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
//                    LogUtil.e("onGetReverseGeoCodeResult","抱歉，未能找到结果");
//                }
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
        mGeoCoder.reverseGeoCode(new ReverseGeoCodeOption()
                .location(latLng));
    }

    public static void destroyGeoCoder() {
        if (mGeoCoder != null){
            mGeoCoder.destroy();
            mGeoCoder=null;
        }
    }


    public interface MyOnGetPoiSearchResultListener{
        void onGetPoiResult(PoiResult result);
        void onGetPoiDetailResult(PoiDetailResult result);
    }

    /**
     * 根据关键字周边搜索Poi
     * @param keyword
     * @param listener
     */
    public static void getPoiNearbyByKeyWord(String keyword, LatLng ll,int radius,int pageCapacity,PoiSortType sortType,final MyOnGetPoiSearchResultListener listener) {
        if (mPoiSearch==null){
            mPoiSearch = PoiSearch.newInstance();
        }
        mPoiSearch.searchNearby(new PoiNearbySearchOption().keyword(keyword).location(ll).radius(radius).pageCapacity(pageCapacity).sortType(sortType));
        mPoiSearch.setOnGetPoiSearchResultListener(new OnGetPoiSearchResultListener() {
            @Override
            public void onGetPoiResult(PoiResult result) {
//                if (result == null || result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
//                    LogUtil.e("onGetPoiResult","未找到结果");
//                }
                if (listener!=null){
                    listener.onGetPoiResult(result);
                }
            }
            @Override
            public void onGetPoiDetailResult(PoiDetailResult result) {
//                if (result.error != SearchResult.ERRORNO.NO_ERROR) {
//                    LogUtil.e("onGetPoiDetailResult", "抱歉，未找到结果");
//                } else {
//                    LogUtil.e("onGetPoiDetailResult",result.getName() + ": " + result.getAddress());
//                }
                if (listener!=null){
                    listener.onGetPoiDetailResult(result);
                }
            }
        });
    }
    public static void destroyPoiSearch() {
        if (mPoiSearch != null){
            mPoiSearch.destroy();
            mPoiSearch=null;
        }
    }
}
