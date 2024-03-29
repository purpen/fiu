package com.taihuoniao.fineix.personal.salesevice.bean;

/**
 * Created by Stephen on 2016/11/25.
 */

public class ChargeBackResultDetailsBean {

    /**
     * _id : 1112800040
     * number : 116112853983
     * user_id : 20448
     * target_id : 1031789053
     * target_type : 1
     * product_id : 1011497002
     * order_rid : 116112506532
     * sub_order_id : 116112506532-2
     * refund_price : 0.04
     * pay_type : 1
     * freight : 0
     * quantity : 2
     * type : 1
     * stage : 1
     * reason : 0
     * content : 出么👄
     * summary : null
     * status : 1
     * deleted : 0
     * refund_on : 0
     * batch_no : null
     * created_on : 1480296464
     * updated_on : 1480296464
     * reason_label : null
     * type_label : 退款
     * stage_label : 退款中
     * pay_label : 支付宝
     * __extend__ : true
     * refund_at : 
     * created_at : 2016-11-28 09:27
     * product : {"title":"华米手环","short_title":"华米手环","cover_url":"http://frbird.qiniudn.com/product/160121/56a0934f3ffca26c098ba90e-4-p500x500.jpg","sale_price":0.02,"sku_name":"黑色"}
     * current_user_id : 20448
     */

    private int _id;
    private String number;
    private int user_id;
    private int target_id;
    private int target_type;
    private int product_id;
    private String order_rid;
    private String sub_order_id;
    private double refund_price;
    private int pay_type;
    private int freight;
    private int quantity;
    private int type;
    private int stage;
    private int reason;
    private String content;
    private String summary;
    private int status;
    private int deleted;
    private int refund_on;
    private String batch_no;
    private int created_on;
    private int updated_on;
    private String reason_label;
    private String type_label;
    private String stage_label;
    private String pay_label;
    private boolean __extend__;
    private String refund_at;
    private String created_at;
    private ProductEntity product;
    private int current_user_id;

    public void set_id(int _id) {
        this._id = _id;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public void setTarget_id(int target_id) {
        this.target_id = target_id;
    }

    public void setTarget_type(int target_type) {
        this.target_type = target_type;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public void setOrder_rid(String order_rid) {
        this.order_rid = order_rid;
    }

    public void setSub_order_id(String sub_order_id) {
        this.sub_order_id = sub_order_id;
    }

    public void setRefund_price(double refund_price) {
        this.refund_price = refund_price;
    }

    public void setPay_type(int pay_type) {
        this.pay_type = pay_type;
    }

    public void setFreight(int freight) {
        this.freight = freight;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setStage(int stage) {
        this.stage = stage;
    }

    public void setReason(int reason) {
        this.reason = reason;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setDeleted(int deleted) {
        this.deleted = deleted;
    }

    public void setRefund_on(int refund_on) {
        this.refund_on = refund_on;
    }

    public void setBatch_no(String batch_no) {
        this.batch_no = batch_no;
    }

    public void setCreated_on(int created_on) {
        this.created_on = created_on;
    }

    public void setUpdated_on(int updated_on) {
        this.updated_on = updated_on;
    }

    public void setReason_label(String reason_label) {
        this.reason_label = reason_label;
    }

    public void setType_label(String type_label) {
        this.type_label = type_label;
    }

    public void setStage_label(String stage_label) {
        this.stage_label = stage_label;
    }

    public void setPay_label(String pay_label) {
        this.pay_label = pay_label;
    }

    public void set__extend__(boolean __extend__) {
        this.__extend__ = __extend__;
    }

    public void setRefund_at(String refund_at) {
        this.refund_at = refund_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public void setProduct(ProductEntity product) {
        this.product = product;
    }

    public void setCurrent_user_id(int current_user_id) {
        this.current_user_id = current_user_id;
    }

    public int get_id() {
        return _id;
    }

    public String getNumber() {
        return number;
    }

    public int getUser_id() {
        return user_id;
    }

    public int getTarget_id() {
        return target_id;
    }

    public int getTarget_type() {
        return target_type;
    }

    public int getProduct_id() {
        return product_id;
    }

    public String getOrder_rid() {
        return order_rid;
    }

    public String getSub_order_id() {
        return sub_order_id;
    }

    public double getRefund_price() {
        return refund_price;
    }

    public int getPay_type() {
        return pay_type;
    }

    public int getFreight() {
        return freight;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getType() {
        return type;
    }

    public int getStage() {
        return stage;
    }

    public int getReason() {
        return reason;
    }

    public String getContent() {
        return content;
    }

    public String getSummary() {
        return summary;
    }

    public int getStatus() {
        return status;
    }

    public int getDeleted() {
        return deleted;
    }

    public int getRefund_on() {
        return refund_on;
    }

    public String getBatch_no() {
        return batch_no;
    }

    public int getCreated_on() {
        return created_on;
    }

    public int getUpdated_on() {
        return updated_on;
    }

    public String getReason_label() {
        return reason_label;
    }

    public String getType_label() {
        return type_label;
    }

    public String getStage_label() {
        return stage_label;
    }

    public String getPay_label() {
        return pay_label;
    }

    public boolean get__extend__() {
        return __extend__;
    }

    public String getRefund_at() {
        return refund_at;
    }

    public String getCreated_at() {
        return created_at;
    }

    public ProductEntity getProduct() {
        return product;
    }

    public int getCurrent_user_id() {
        return current_user_id;
    }

    public static class ProductEntity {
        /**
         * title : 华米手环
         * short_title : 华米手环
         * cover_url : http://frbird.qiniudn.com/product/160121/56a0934f3ffca26c098ba90e-4-p500x500.jpg
         * sale_price : 0.02
         * sku_name : 黑色
         */

        private String title;
        private String short_title;
        private String cover_url;
        private double sale_price;
        private String sku_name;

        public void setTitle(String title) {
            this.title = title;
        }

        public void setShort_title(String short_title) {
            this.short_title = short_title;
        }

        public void setCover_url(String cover_url) {
            this.cover_url = cover_url;
        }

        public void setSale_price(double sale_price) {
            this.sale_price = sale_price;
        }

        public void setSku_name(String sku_name) {
            this.sku_name = sku_name;
        }

        public String getTitle() {
            return title;
        }

        public String getShort_title() {
            return short_title;
        }

        public String getCover_url() {
            return cover_url;
        }

        public double getSale_price() {
            return sale_price;
        }

        public String getSku_name() {
            return sku_name;
        }
    }
}
