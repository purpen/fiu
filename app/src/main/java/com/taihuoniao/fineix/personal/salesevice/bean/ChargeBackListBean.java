package com.taihuoniao.fineix.personal.salesevice.bean;

import java.util.List;

/**
 *  退款/ 售后 列表
 *
 * Created by Stephen on 2016/11/25.
 */

public class ChargeBackListBean {

    /**
     * total_rows : 24
     * rows : [{"_id":1112500024,"number":"116112502713","user_id":20448,"target_id":1071045007,"product_id":1071045007,"target_type":2,"order_rid":"116112506528","sub_order_id":"116112506528-2","refund_price":0.02,"quantity":2,"type":1,"type_label":"退款","freight":0,"stage":1,"reason":3,"reason_label":"快递/物流没送到","content":"","summary":null,"status":1,"deleted":0,"created_on":1480058098,"updated_on":1480058098,"product":{"title":"测试产品","short_title":"测试产品","cover_url":"http://frbird.qiniudn.com/product/150730/55b9fb6c3ffca2235f8b4f73-1-p500x500.jpg","sku_name":""},"refund_at":"","created_at":"16-11-25"},{"_id":1112500023,"number":"116112529561","user_id":20448,"target_id":1011513749,"product_id":1011513749,"target_type":2,"order_rid":"116112506528","sub_order_id":"116112506528-1","refund_price":0.15,"quantity":1,"type":1,"type_label":"退款","freight":0,"stage":1,"reason":0,"reason_label":null,"content":"sssssss","summary":null,"status":1,"deleted":0,"created_on":1480058047,"updated_on":1480058047,"product":{"title":"a1螺丝刀a1螺丝刀a1螺丝刀","short_title":"a1螺丝刀","cover_url":"http://frbird.qiniudn.com/product/160127/56a858723ffca269098bb039-4-p500x500.jpg","sku_name":""},"refund_at":"","created_at":"16-11-25"},{"_id":1112500022,"number":"116112563410","user_id":20448,"target_id":1011513749,"product_id":1011513749,"target_type":2,"order_rid":"116112506529","sub_order_id":null,"refund_price":0.15,"quantity":1,"type":2,"type_label":"退货","freight":0,"stage":1,"reason":1,"reason_label":"收到商品破损","content":"测试","summary":null,"status":1,"deleted":0,"created_on":1480058031,"updated_on":1480058031,"product":{"title":"a1螺丝刀a1螺丝刀a1螺丝刀","short_title":"a1螺丝刀","cover_url":"http://frbird.qiniudn.com/product/160127/56a858723ffca269098bb039-4-p500x500.jpg","sku_name":""},"refund_at":"","created_at":"16-11-25"},{"_id":1112500021,"number":"116112500970","user_id":20448,"target_id":1011513749,"product_id":1011513749,"target_type":2,"order_rid":"116112506525","sub_order_id":null,"refund_price":0.15,"quantity":1,"type":2,"type_label":"退货","freight":0,"stage":1,"reason":0,"reason_label":null,"content":"泡沫","summary":null,"status":1,"deleted":0,"created_on":1480057783,"updated_on":1480057783,"product":{"title":"a1螺丝刀a1螺丝刀a1螺丝刀","short_title":"a1螺丝刀","cover_url":"http://frbird.qiniudn.com/product/160127/56a858723ffca269098bb039-4-p500x500.jpg","sku_name":""},"refund_at":"","created_at":"16-11-25"},{"_id":1112500020,"number":"116112566835","user_id":20448,"target_id":1120686901,"product_id":1120686199,"target_type":1,"order_rid":"116112506525","sub_order_id":null,"refund_price":0.1,"quantity":1,"type":2,"type_label":"退货","freight":0,"stage":1,"reason":0,"reason_label":null,"content":"咯哈我一下人在使用哦明眸哦弄哦我今年我们是朋友圈了，咯啦咯了起来家里家里人，咯啦咯了起来家里家里了。咯啦咯了起来家里家里，咯啦咯了起来家里吵架了讲两句咯啦咯了起来家里家里人多的身体啊。咯啦咯了起来家里吵架了咯啦咯了起来家里家里讲两句咯啦咯了起来家里家里讲两句讲两句咯啦咯了起来家里家里人是不是傻，咯啦咯了起来家里吵架了讲两句咯啦咯了起来家里家里讲两句讲两句咯啦咯了起来家里家里，咯啦咯了起来家里家里的","summary":null,"status":1,"deleted":0,"created_on":1480056345,"updated_on":1480056345,"product":{"title":"TORO儿童云手机 GPS定位低辐射女生男生手机","short_title":"TORO儿童云手机 GPS定位低辐射女生男生手机","cover_url":"http://frbird.qiniudn.com/product/141203/547eabfd621e1947648b5356-1-p500x500.jpg","sku_name":"黑色"},"refund_at":"","created_at":"16-11-25"},{"_id":1112500019,"number":"116112541999","user_id":20448,"target_id":1011513749,"product_id":1011513749,"target_type":2,"order_rid":"116112506526","sub_order_id":"116112506526-2","refund_price":0.15,"quantity":1,"type":1,"type_label":"退款","freight":0,"stage":1,"reason":0,"reason_label":null,"content":"听哦look","summary":null,"status":1,"deleted":0,"created_on":1480055145,"updated_on":1480055145,"product":{"title":"a1螺丝刀a1螺丝刀a1螺丝刀","short_title":"a1螺丝刀","cover_url":"http://frbird.qiniudn.com/product/160127/56a858723ffca269098bb039-4-p500x500.jpg","sku_name":""},"refund_at":"","created_at":"16-11-25"},{"_id":1112500018,"number":"116112508037","user_id":20448,"target_id":1071045007,"product_id":1071045007,"target_type":2,"order_rid":"116112506526","sub_order_id":"116112506526-1","refund_price":0.03,"quantity":3,"type":1,"type_label":"退款","freight":0,"stage":1,"reason":0,"reason_label":null,"content":"他可以","summary":null,"status":1,"deleted":0,"created_on":1480054515,"updated_on":1480054515,"product":{"title":"测试产品","short_title":"测试产品","cover_url":"http://frbird.qiniudn.com/product/150730/55b9fb6c3ffca2235f8b4f73-1-p500x500.jpg","sku_name":""},"refund_at":"","created_at":"16-11-25"},{"_id":1112500017,"number":"116112592284","user_id":20448,"target_id":1071045007,"product_id":1071045007,"target_type":2,"order_rid":"116112506527","sub_order_id":null,"refund_price":0.02,"quantity":2,"type":1,"type_label":"退款","freight":0,"stage":1,"reason":0,"reason_label":null,"content":"老婆肉X5","summary":null,"status":1,"deleted":0,"created_on":1480053846,"updated_on":1480053846,"product":{"title":"测试产品","short_title":"测试产品","cover_url":"http://frbird.qiniudn.com/product/150730/55b9fb6c3ffca2235f8b4f73-1-p500x500.jpg","sku_name":""},"refund_at":"","created_at":"16-11-25"}]
     * total_page : 3
     * current_page : 1
     * pager :
     * next_page : 2
     * prev_page : 0
     * current_user_id : 20448
     */

    private int total_rows;
    private int total_page;
    private int current_page;
    private String pager;
    private int next_page;
    private int prev_page;
    private int current_user_id;
    private List<RowsEntity> rows;

    public void setTotal_rows(int total_rows) {
        this.total_rows = total_rows;
    }

    public void setTotal_page(int total_page) {
        this.total_page = total_page;
    }

    public void setCurrent_page(int current_page) {
        this.current_page = current_page;
    }

    public void setPager(String pager) {
        this.pager = pager;
    }

    public void setNext_page(int next_page) {
        this.next_page = next_page;
    }

    public void setPrev_page(int prev_page) {
        this.prev_page = prev_page;
    }

    public void setCurrent_user_id(int current_user_id) {
        this.current_user_id = current_user_id;
    }

    public void setRows(List<RowsEntity> rows) {
        this.rows = rows;
    }

    public int getTotal_rows() {
        return total_rows;
    }

    public int getTotal_page() {
        return total_page;
    }

    public int getCurrent_page() {
        return current_page;
    }

    public String getPager() {
        return pager;
    }

    public int getNext_page() {
        return next_page;
    }

    public int getPrev_page() {
        return prev_page;
    }

    public int getCurrent_user_id() {
        return current_user_id;
    }

    public List<RowsEntity> getRows() {
        return rows;
    }

    public static class RowsEntity {
        /**
         * _id : 1112500024
         * number : 116112502713
         * user_id : 20448
         * target_id : 1071045007
         * product_id : 1071045007
         * target_type : 2
         * order_rid : 116112506528
         * sub_order_id : 116112506528-2
         * refund_price : 0.02
         * quantity : 2
         * type : 1
         * type_label : 退款
         * freight : 0
         * stage : 1
         * reason : 3
         * reason_label : 快递/物流没送到
         * content :
         * summary : null
         * status : 1
         * deleted : 0
         * created_on : 1480058098
         * updated_on : 1480058098
         * product : {"title":"测试产品","short_title":"测试产品","cover_url":"http://frbird.qiniudn.com/product/150730/55b9fb6c3ffca2235f8b4f73-1-p500x500.jpg","sku_name":""}
         * refund_at :
         * created_at : 16-11-25
         */

        private int _id;
        private String number;
        private int user_id;
        private int target_id;
        private int product_id;
        private int target_type;
        private String order_rid;
        private String sub_order_id;
        private double refund_price;
        private int quantity;
        private int type;
        private String type_label;
        private int freight;
        private int stage;
        private int reason;
        private String reason_label;
        private String content;
        private Object summary;
        private int status;
        private int deleted;
        private int created_on;
        private int updated_on;
        private ProductEntity product;
        private String refund_at;
        private String created_at;

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

        public void setProduct_id(int product_id) {
            this.product_id = product_id;
        }

        public void setTarget_type(int target_type) {
            this.target_type = target_type;
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

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public void setType(int type) {
            this.type = type;
        }

        public void setType_label(String type_label) {
            this.type_label = type_label;
        }

        public void setFreight(int freight) {
            this.freight = freight;
        }

        public void setStage(int stage) {
            this.stage = stage;
        }

        public void setReason(int reason) {
            this.reason = reason;
        }

        public void setReason_label(String reason_label) {
            this.reason_label = reason_label;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public void setSummary(Object summary) {
            this.summary = summary;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public void setDeleted(int deleted) {
            this.deleted = deleted;
        }

        public void setCreated_on(int created_on) {
            this.created_on = created_on;
        }

        public void setUpdated_on(int updated_on) {
            this.updated_on = updated_on;
        }

        public void setProduct(ProductEntity product) {
            this.product = product;
        }

        public void setRefund_at(String refund_at) {
            this.refund_at = refund_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
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

        public int getProduct_id() {
            return product_id;
        }

        public int getTarget_type() {
            return target_type;
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

        public int getQuantity() {
            return quantity;
        }

        public int getType() {
            return type;
        }

        public String getType_label() {
            return type_label;
        }

        public int getFreight() {
            return freight;
        }

        public int getStage() {
            return stage;
        }

        public int getReason() {
            return reason;
        }

        public String getReason_label() {
            return reason_label;
        }

        public String getContent() {
            return content;
        }

        public Object getSummary() {
            return summary;
        }

        public int getStatus() {
            return status;
        }

        public int getDeleted() {
            return deleted;
        }

        public int getCreated_on() {
            return created_on;
        }

        public int getUpdated_on() {
            return updated_on;
        }

        public ProductEntity getProduct() {
            return product;
        }

        public String getRefund_at() {
            return refund_at;
        }

        public String getCreated_at() {
            return created_at;
        }

        public static class ProductEntity {
            /**
             * title : 测试产品
             * short_title : 测试产品
             * cover_url : http://frbird.qiniudn.com/product/150730/55b9fb6c3ffca2235f8b4f73-1-p500x500.jpg
             * sku_name :
             */

            private String title;
            private String short_title;
            private String cover_url;
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

            public String getSku_name() {
                return sku_name;
            }
        }
    }
}
