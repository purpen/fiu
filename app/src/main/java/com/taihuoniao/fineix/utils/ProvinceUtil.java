package com.taihuoniao.fineix.utils;

import android.content.Context;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.taihuoniao.fineix.beans.ProvinceCityData;
import com.taihuoniao.fineix.main.MainApplication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author lilin
 *         created at 2016/4/27 13:30
 */
public class ProvinceUtil {
    private static final String TAG = "ProvinceUtil";
    private static ArrayList<ProvinceCityData> provinceList = null;
    private static HashMap<String, ArrayList<String>> provinceCityMap = null;
    private static HashMap<String, ArrayList<String>> cityCountiesMap = null;
    public static void init(Context context) {
        try {
            StringBuffer buffer = new StringBuffer();
            InputStream open = context.getResources().getAssets().open("address.json");
            if (open != null) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(open, Constants.CHARSET));
                provinceList = new ArrayList<ProvinceCityData>();
                JsonArray jsonArray = JsonUtil.getJsonArray(reader);
                for (JsonElement element : jsonArray) {
                    provinceList.add(JsonUtil.fromJson(element, ProvinceCityData.class));
                }
                initProviceCityMap();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void initProviceCityMap() {
        if (provinceList == null) {
            init(MainApplication.getContext());
        }
        provinceCityMap = new HashMap<String, ArrayList<String>>();
        cityCountiesMap=new HashMap<String,ArrayList<String>>();
        ArrayList<String> cities = null;
        ArrayList<String> counties = null;
        for (ProvinceCityData provinceCityDistrict : provinceList) {
            cities = new ArrayList<String>();
            for (ProvinceCityData.City city : provinceCityDistrict.cities) {
                cities.add(city.areaName);
                for (ProvinceCityData.City.County county:city.counties){
                    counties=new ArrayList<String>();
                    counties.add(county.areaName);
                }
                cityCountiesMap.put(city.areaName,counties);
            }
            provinceCityMap.put(provinceCityDistrict.areaName, cities);
        }
    }

    public static ArrayList<String> getProvinces() {
        if (provinceList == null) {
            init(MainApplication.getContext());
        }
        ArrayList<String> provinces = new ArrayList<String>();
        for (ProvinceCityData provinceCityDistrict : provinceList) {
            provinces.add(provinceCityDistrict.areaName);
        }
        return provinces;
    }


    public static ArrayList<String> getCitiesByProvince(String province) {
        if (provinceCityMap == null) {
            init(MainApplication.getContext());
        }
        return provinceCityMap.get(province);
    }

    public static ArrayList<String> getCountiesByCity(String city) {
        if (cityCountiesMap==null){
            init(MainApplication.getContext());
        }
        return  cityCountiesMap.get(city);
    }
}
