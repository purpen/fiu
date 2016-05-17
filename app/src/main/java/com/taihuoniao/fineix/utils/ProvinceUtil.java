package com.taihuoniao.fineix.utils;
import android.text.TextUtils;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.taihuoniao.fineix.beans.ProvinceCityData;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.HttpResponse;

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
        ClientDiscoverAPI.getAllCities(new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                if (responseInfo==null){
                    return;
                }
                if (TextUtils.isEmpty(responseInfo.result)){
                    return;
                }
                data = JsonUtil.fromJson(responseInfo.result, new TypeToken<HttpResponse<ProvinceCityData>>() {
                });
                dealData();
            }

            @Override
            public void onFailure(HttpException e, String s) {
                Util.makeToast(s);
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
        ArrayList<String> cities = null;
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
            Util.makeToast("抱歉无法获得地址数据,请先开启网络");
            return null;
        }
        ArrayList<String> provinces = new ArrayList<String>();
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