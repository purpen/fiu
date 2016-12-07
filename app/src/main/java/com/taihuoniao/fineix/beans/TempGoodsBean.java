package com.taihuoniao.fineix.beans;

/**
 * Created by taihuoniao on 2016/8/29.
 */
public class TempGoodsBean extends NetBean {

    /**
     * is_error : false
     * data : {"_id":5,"title":"苹果","type":1,"target_id":0,"status":1,"is_check":1,"view_count":0,"user_id":20448,"brand_id":"4","brand_name":"苹果","created_on":1472462391,"updated_on":1472462391,"type_label":"产品","__extend__":true,"current_user_id":20448}
     */

    private boolean is_error;
    /**
     * _id : 5
     * title : 苹果
     * type : 1
     * target_id : 0
     * status : 1
     * is_check : 1
     * view_count : 0
     * user_id : 20448
     * brand_id : 4
     * brand_name : 苹果
     * created_on : 1472462391
     * updated_on : 1472462391
     * type_label : 产品
     * __extend__ : true
     * current_user_id : 20448
     */

    private DataBean data;

    public boolean isIs_error() {
        return is_error;
    }

    public void setIs_error(boolean is_error) {
        this.is_error = is_error;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private String _id;
        private String title;
        private int type;
        private String target_id;
        private int status;
        private int is_check;
        private int view_count;
        private int user_id;
        private String brand_id;
        private String brand_name;
        private int created_on;
        private int updated_on;
        private String type_label;
        private boolean __extend__;
        private String current_user_id;

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getTarget_id() {
            return target_id;
        }

        public void setTarget_id(String target_id) {
            this.target_id = target_id;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getIs_check() {
            return is_check;
        }

        public void setIs_check(int is_check) {
            this.is_check = is_check;
        }

        public int getView_count() {
            return view_count;
        }

        public void setView_count(int view_count) {
            this.view_count = view_count;
        }

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }

        public String getBrand_id() {
            return brand_id;
        }

        public void setBrand_id(String brand_id) {
            this.brand_id = brand_id;
        }

        public String getBrand_name() {
            return brand_name;
        }

        public void setBrand_name(String brand_name) {
            this.brand_name = brand_name;
        }

        public int getCreated_on() {
            return created_on;
        }

        public void setCreated_on(int created_on) {
            this.created_on = created_on;
        }

        public int getUpdated_on() {
            return updated_on;
        }

        public void setUpdated_on(int updated_on) {
            this.updated_on = updated_on;
        }

        public String getType_label() {
            return type_label;
        }

        public void setType_label(String type_label) {
            this.type_label = type_label;
        }

        public boolean is__extend__() {
            return __extend__;
        }

        public void set__extend__(boolean __extend__) {
            this.__extend__ = __extend__;
        }

        public String getCurrent_user_id() {
            return current_user_id;
        }

        public void setCurrent_user_id(String current_user_id) {
            this.current_user_id = current_user_id;
        }
    }
}
