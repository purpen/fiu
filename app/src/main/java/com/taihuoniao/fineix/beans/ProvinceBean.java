package com.taihuoniao.fineix.beans;

import java.io.Serializable;
import java.util.List;

/**
 * Created by taihuoniao on 2016/3/9.
 */
public class ProvinceBean implements Serializable {
    private String _id;
    private String name;
    private List<CityBean> cityList;


    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<CityBean> getCityList() {
        return cityList;
    }

    public void setCityList(List<CityBean> cityList) {
        this.cityList = cityList;
    }

    @Override
    public String toString() {
        return "ProvinceBean{" +
                "_id='" + _id + '\'' +
                ", name='" + name + '\'' +
                ", cityList=" + cityList +
                '}';
    }
}
