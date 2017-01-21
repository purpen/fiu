package com.taihuoniao.fineix.personal.alliance.bean;

/**
 * Created by Stephen on 2017/1/20 10:24
 * Email: 895745843@qq.com
 */

public class TradeRecordDetailsBean {

    /**
     * _id : 587f438b3ffca2ba3d8b4596
     * alliance_id : 587dd1343ffca27e2a8baaec
     * order_rid : 117011807046
     * sub_order_id : null
     * product_id : 1120677456
     * sku_id : 1120677465
     * sku_price : 998
     * user_id : 20448
     * quantity : 2
     * commision_percent : 0.1
     * unit_price : 99.8
     * total_price : 199.6
     * code : x518fe
     * summary : null
     * type : 2
     * kind : 1
     * stage : 5
     * status : 1
     * status_label : 已结算
     * balance_on : 1484735701
     * from_site : 1
     * created_on : 1484735371
     * updated_on : 1484735701
     * product : {"title":"爱牵挂智能腕表老人专用危情报警心率检测触摸按键智能提醒","short_title":"爱牵挂智能腕表老人专用危情报警心率检测触摸按键智能提醒"}
     * created_at : 2017-01-18 18:29
     * current_user_id : 20448
     */

    private String _id;
    private String alliance_id;
    private String order_rid;
    private Object sub_order_id;
    private String product_id;
    private String sku_id;
    private String sku_price;
    private String user_id;
    private String quantity;
    private String commision_percent;
    private String unit_price;
    private String total_price;
    private String code;
    private Object summary;
    private String type;
    private String kind;
    private String stage;
    private String status;
    private String status_label;
    private String balance_on;
    private String from_site;
    private String created_on;
    private String updated_on;
    private ProductEntity product;
    private String created_at;
    private String current_user_id;

    public void set_id(String _id) {
        this._id = _id;
    }

    public void setAlliance_id(String alliance_id) {
        this.alliance_id = alliance_id;
    }

    public void setOrder_rid(String order_rid) {
        this.order_rid = order_rid;
    }

    public void setSub_order_id(Object sub_order_id) {
        this.sub_order_id = sub_order_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public void setSku_id(String sku_id) {
        this.sku_id = sku_id;
    }

    public void setSku_price(String sku_price) {
        this.sku_price = sku_price;
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

    public void setSummary(Object summary) {
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

    public void setProduct(ProductEntity product) {
        this.product = product;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public void setCurrent_user_id(String current_user_id) {
        this.current_user_id = current_user_id;
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

    public Object getSub_order_id() {
        return sub_order_id;
    }

    public String getProduct_id() {
        return product_id;
    }

    public String getSku_id() {
        return sku_id;
    }

    public String getSku_price() {
        return sku_price;
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

    public Object getSummary() {
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

    public ProductEntity getProduct() {
        return product;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getCurrent_user_id() {
        return current_user_id;
    }

    public static class ProductEntity {
        /**
         * title : 爱牵挂智能腕表老人专用危情报警心率检测触摸按键智能提醒
         * short_title : 爱牵挂智能腕表老人专用危情报警心率检测触摸按键智能提醒
         */

        private String title;
        private String short_title;

        public void setTitle(String title) {
            this.title = title;
        }

        public void setShort_title(String short_title) {
            this.short_title = short_title;
        }

        public String getTitle() {
            return title;
        }

        public String getShort_title() {
            return short_title;
        }
    }
}
