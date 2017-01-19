package com.taihuoniao.fineix.personal.alliance.bean;

import java.util.List;

/**
 * Created by Stephen on 2017/1/19 19:13
 * Email: 895745843@qq.com
 */

public class TradeRecordBean {

    /**
     * total_rows : 11
     * rows : [{"_id":"587f438b3ffca2ba3d8b4596","alliance_id":"587dd1343ffca27e2a8baaec","order_rid":"117011807046","sub_order_id":0,"product_id":1120677456,"sku_id":1120677465,"user_id":20448,"quantity":2,"commision_percent":0.1,"unit_price":99.8,"total_price":199.6,"code":"x518fe","summary":0,"type":2,"kind":1,"stage":5,"status":1,"status_label":"已结算","balance_on":1484735701,"from_site":1,"created_on":1484735371,"updated_on":1484735701,"created_at":"2017-01-18 18:29"},{"_id":"587f438b3ffca2ba3d8b4597","alliance_id":"587dd1343ffca27e2a8baaec","order_rid":"117011807046","sub_order_id":0,"product_id":1120677456,"sku_id":1120677459,"user_id":20448,"quantity":3,"commision_percent":0.1,"unit_price":99.8,"total_price":299.4,"code":"x518fe","summary":0,"type":2,"kind":1,"stage":5,"status":1,"status_label":"已结算","balance_on":1484735701,"from_site":1,"created_on":1484735371,"updated_on":1484735701,"created_at":"2017-01-18 18:29"},{"_id":"587f431b3ffca2482f8b4694","alliance_id":"587dd1343ffca27e2a8baaec","order_rid":"117011807045","sub_order_id":0,"product_id":1120700195,"sku_id":1015010032,"user_id":20448,"quantity":1,"commision_percent":0.1,"unit_price":29.9,"total_price":29.9,"code":"x518fe","summary":0,"type":2,"kind":1,"stage":5,"status":1,"status_label":"已结算","balance_on":1484735701,"from_site":1,"created_on":1484735259,"updated_on":1484735701,"created_at":"2017-01-18 18:27"}]
     * total_page : 4
     * current_page : 1
     * pager : 
     * next_page : 2
     * prev_page : 0
     * current_user_id : 20448
     */

    private String total_rows;
    private String total_page;
    private String current_page;
    private String pager;
    private String next_page;
    private String prev_page;
    private String current_user_id;
    private List<RowsEntity> rows;

    public void setTotal_rows(String total_rows) {
        this.total_rows = total_rows;
    }

    public void setTotal_page(String total_page) {
        this.total_page = total_page;
    }

    public void setCurrent_page(String current_page) {
        this.current_page = current_page;
    }

    public void setPager(String pager) {
        this.pager = pager;
    }

    public void setNext_page(String next_page) {
        this.next_page = next_page;
    }

    public void setPrev_page(String prev_page) {
        this.prev_page = prev_page;
    }

    public void setCurrent_user_id(String current_user_id) {
        this.current_user_id = current_user_id;
    }

    public void setRows(List<RowsEntity> rows) {
        this.rows = rows;
    }

    public String getTotal_rows() {
        return total_rows;
    }

    public String getTotal_page() {
        return total_page;
    }

    public String getCurrent_page() {
        return current_page;
    }

    public String getPager() {
        return pager;
    }

    public String getNext_page() {
        return next_page;
    }

    public String getPrev_page() {
        return prev_page;
    }

    public String getCurrent_user_id() {
        return current_user_id;
    }

    public List<RowsEntity> getRows() {
        return rows;
    }

    public static class RowsEntity {
        /**
         * _id : 587f438b3ffca2ba3d8b4596
         * alliance_id : 587dd1343ffca27e2a8baaec
         * order_rid : 117011807046
         * sub_order_id : 0
         * product_id : 1120677456
         * sku_id : 1120677465
         * user_id : 20448
         * quantity : 2
         * commision_percent : 0.1
         * unit_price : 99.8
         * total_price : 199.6
         * code : x518fe
         * summary : 0
         * type : 2
         * kind : 1
         * stage : 5
         * status : 1
         * status_label : 已结算
         * balance_on : 1484735701
         * from_site : 1
         * created_on : 1484735371
         * updated_on : 1484735701
         * created_at : 2017-01-18 18:29
         */

        private String _id;
        private String alliance_id;
        private String order_rid;
        private String sub_order_id;
        private String product_id;
        private String sku_id;
        private String user_id;
        private String quantity;
        private String commision_percent;
        private String unit_price;
        private String total_price;
        private String code;
        private String summary;
        private String type;
        private String kind;
        private String stage;
        private String status;
        private String status_label;
        private String balance_on;
        private String from_site;
        private String created_on;
        private String updated_on;
        private String created_at;

        public void set_id(String _id) {
            this._id = _id;
        }

        public void setAlliance_id(String alliance_id) {
            this.alliance_id = alliance_id;
        }

        public void setOrder_rid(String order_rid) {
            this.order_rid = order_rid;
        }

        public void setSub_order_id(String sub_order_id) {
            this.sub_order_id = sub_order_id;
        }

        public void setProduct_id(String product_id) {
            this.product_id = product_id;
        }

        public void setSku_id(String sku_id) {
            this.sku_id = sku_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public void setQuantity(String quantity) {
            this.quantity = quantity;
        }

        public void setCommision_percent(String commision_percent) {
            this.commision_percent = commision_percent;
        }

        public void setUnit_price(String unit_price) {
            this.unit_price = unit_price;
        }

        public void setTotal_price(String total_price) {
            this.total_price = total_price;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public void setSummary(String summary) {
            this.summary = summary;
        }

        public void setType(String type) {
            this.type = type;
        }

        public void setKind(String kind) {
            this.kind = kind;
        }

        public void setStage(String stage) {
            this.stage = stage;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public void setStatus_label(String status_label) {
            this.status_label = status_label;
        }

        public void setBalance_on(String balance_on) {
            this.balance_on = balance_on;
        }

        public void setFrom_site(String from_site) {
            this.from_site = from_site;
        }

        public void setCreated_on(String created_on) {
            this.created_on = created_on;
        }

        public void setUpdated_on(String updated_on) {
            this.updated_on = updated_on;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public String get_id() {
            return _id;
        }

        public String getAlliance_id() {
            return alliance_id;
        }

        public String getOrder_rid() {
            return order_rid;
        }

        public String getSub_order_id() {
            return sub_order_id;
        }

        public String getProduct_id() {
            return product_id;
        }

        public String getSku_id() {
            return sku_id;
        }

        public String getUser_id() {
            return user_id;
        }

        public String getQuantity() {
            return quantity;
        }

        public String getCommision_percent() {
            return commision_percent;
        }

        public String getUnit_price() {
            return unit_price;
        }

        public String getTotal_price() {
            return total_price;
        }

        public String getCode() {
            return code;
        }

        public String getSummary() {
            return summary;
        }

        public String getType() {
            return type;
        }

        public String getKind() {
            return kind;
        }

        public String getStage() {
            return stage;
        }

        public String getStatus() {
            return status;
        }

        public String getStatus_label() {
            return status_label;
        }

        public String getBalance_on() {
            return balance_on;
        }

        public String getFrom_site() {
            return from_site;
        }

        public String getCreated_on() {
            return created_on;
        }

        public String getUpdated_on() {
            return updated_on;
        }

        public String getCreated_at() {
            return created_at;
        }
    }
}
