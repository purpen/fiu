package com.taihuoniao.fineix.personal.salesevice.bean;

import java.util.List;

/**
 * Created by Stephen on 2016/11/25.
     */

    public class ChargeBackBean {

        /**
         * product_id : 1081052995
     * quantity : 2
     * refund_price : 0.02
     * title : 这是第一个产品吗
     * short_title : 这是第一个产品吗
     * cover_url : http://frbird.qiniudn.com/stuff/150330/55193e5c3ffca25a6f8b4cd4-1-p500x500.jpg
     * sale_price : 0.01
     * sku_name : 默认
     * refund_reason : [{"id":1,"title":"不喜欢/不想要了"},{"id":2,"title":"未按约定时间发货"},{"id":3,"title":"快递/物流没送到"}]
     * return_reason : [{"id":1,"title":"收到商品破损"},{"id":2,"title":"商品错发/漏发"},{"id":3,"title":"商品需要维修"},{"id":4,"title":"收到商品与描述不符"},{"id":5,"title":"商品质量问题"}]
     * current_user_id : 20448
     */

    private int product_id;
    private int quantity;
    private double refund_price;
    private String title;
    private String short_title;
    private String cover_url;
    private double sale_price;
    private String sku_name;
    private int current_user_id;
    private List<RefundReasonEntity> refund_reason;
    private List<ReturnReasonEntity> return_reason;

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setRefund_price(double refund_price) {
        this.refund_price = refund_price;
    }

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

    public void setCurrent_user_id(int current_user_id) {
        this.current_user_id = current_user_id;
    }

    public void setRefund_reason(List<RefundReasonEntity> refund_reason) {
        this.refund_reason = refund_reason;
    }

    public void setReturn_reason(List<ReturnReasonEntity> return_reason) {
        this.return_reason = return_reason;
    }

    public int getProduct_id() {
        return product_id;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getRefund_price() {
        return refund_price;
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

    public int getCurrent_user_id() {
        return current_user_id;
    }

    public List<RefundReasonEntity> getRefund_reason() {
        return refund_reason;
    }

    public List<ReturnReasonEntity> getReturn_reason() {
        return return_reason;
    }

    public static class RefundReasonEntity {
        /**
         * id : 1
         * title : 不喜欢/不想要了
         */

        private int id;
        private String title;

        public void setId(int id) {
            this.id = id;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }
    }

    public static class ReturnReasonEntity {
        /**
         * id : 1
         * title : 收到商品破损
         */

        private int id;
        private String title;

        public void setId(int id) {
            this.id = id;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }
    }
}
