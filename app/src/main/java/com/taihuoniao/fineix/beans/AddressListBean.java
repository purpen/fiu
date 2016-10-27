package com.taihuoniao.fineix.beans;

import java.io.Serializable;
import java.util.List;

public class AddressListBean implements Serializable{

    /**
     * _id : 5810847f3ffca2dc4d8b45e0
     * user_id : 20448
     * name : 新家
     * phone : 12345678900
     * province : 重庆
     * city : 永川区
     * county : 吉安镇
     * town :
     * province_id : 4
     * city_id : 48207
     * conty :
     * town_id : 0
     * zip :
     * is_default : 0
     * address : 我的人都有一个人的时候
     */

    public List<RowsEntity> rows;

    public static class RowsEntity implements Serializable{
        public String _id;
        public int user_id;
        public String name;
        public String phone;
        public String province;
        public String city;
        public String county;
        public String town;
        public String province_id;
        public String city_id;
        public String conty;
        public String town_id;
        public String zip;
        public String is_default;
        public String address;
        public boolean isSelected=false;
    }
}
