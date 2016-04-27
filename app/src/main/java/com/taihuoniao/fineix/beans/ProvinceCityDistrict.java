package com.taihuoniao.fineix.beans;

import java.util.ArrayList;

/**
 * @author lilin
 * created at 2016/4/27 13:29
 */
public class ProvinceCityDistrict {
    public String areaId;
    public String areaName;
    public ArrayList<City> cities;
    public class City {
        public String areaId;
        public String areaName;
        public ArrayList<County> counties;
        public class County {
            public String areaId;
            public String areaName;
        }
    }
}
