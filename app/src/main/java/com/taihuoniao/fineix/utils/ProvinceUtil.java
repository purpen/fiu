package com.taihuoniao.fineix.utils;

import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;
import com.taihuoniao.fineix.common.GlobalDataCallBack;
import com.taihuoniao.fineix.base.HttpRequest;
import com.taihuoniao.fineix.beans.HttpResponse;
import com.taihuoniao.fineix.beans.ProvinceCityData;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.URL;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author lilin
 *         created at 2016/4/27 13:30
 */
public class ProvinceUtil {
    private static ProvinceCityData data;
    private static final String TAG = "ProvinceUtil";
    private static HashMap<String, ArrayList<String>> provinceCityMap = null;
    private static HashMap<String,Integer> idProvinceMap=null;
    private static HashMap<String,Integer> idCitiesMap=null;
    public static void init() {
        HashMap<String, String> params =ClientDiscoverAPI. getgetAllCitiesRequestParams();
        HttpRequest.post(params,  URL.ALL_CITY_URL, new GlobalDataCallBack(){
//        ClientDiscoverAPI.getAllCities(new RequestCallBack<String>() {
            @Override
            public void onSuccess(String json) {
                if (TextUtils.isEmpty(json)){
                    return;
                }
                data = JsonUtil.fromJson(json, new TypeToken<HttpResponse<ProvinceCityData>>() {
                });
                dealData();
            }

            @Override
            public void onFailure(String error) {
                Util.makeToast(error);
            }
        });
    }

    private static void dealData(){
        if (data==null){
            LogUtil.e(TAG,"dealData---->data==null");
            return;
        }

        if (data.rows==null ||data.rows.size()==0){
            LogUtil.e(TAG,"dealData---->data.rows==null || data.rows.size()==0");
            return;
        }
        provinceCityMap = new HashMap<>();
        idProvinceMap=new HashMap<>();
        idCitiesMap=new HashMap<>();
        ArrayList<String> cities;
        for (ProvinceCityData.Province province:data.rows){
            idProvinceMap.put(province.city,province._id);
            cities = new ArrayList<>();
            for (ProvinceCityData.City city :province.cities){
                cities.add(city.city);
                idCitiesMap.put(city.city,city._id);
            }
            provinceCityMap.put(province.city,cities);
        }
    }

    public static int getProvinceIdByName(String name){
        return idProvinceMap.get(name);
    }

    public static int getCityIdByName(String name){
        return idCitiesMap.get(name);
    }

    public static ArrayList<String> getProvinces() {
        if (data==null){
            Util.makeToast("抱歉无法获得地址数据,请先确保网络畅通");
            return null;
        }
        ArrayList<String> provinces = new ArrayList<>();
        for (ProvinceCityData.Province province : data.rows) {
            provinces.add(province.city);
        }
        return provinces;
    }


    public static ArrayList<String> getCitiesByProvince(String province) {
        if (data==null){
            Util.makeToast("抱歉无法获得地址数据,请先开启网络");
            return null;
        }
        return provinceCityMap.get(province);
    }

}
