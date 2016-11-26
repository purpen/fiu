package com.taihuoniao.fineix.personal.salesevice.bean;

import java.util.List;

/**
 *  退款/ 售后 列表
 *
 * Created by Stephen on 2016/11/25.
 */

public class ChargeBackListBean {

    /**
     * total_rows : 33
     * rows : [{"_id":1112600034,"number":"116112617334","user_id":20448,"target_id":1031789046,"product_id":1011497002,"target_type":1,"stage_label":"退款中","order_rid":"116112506534","sub_order_id":null,"refund_price":0.02,"quantity":2,"type":1,"type_label":"退款","freight":0,"stage":1,"reason":0,"reason_label":null,"content":"停机外婆短","summary":null,"status":1,"deleted":0,"created_on":1480141003,"updated_on":1480141003,"product":{"title":"华米手环","short_title":"华米手环","cover_url":"http://frbird.qiniudn.com/product/160121/56a0934f3ffca26c098ba90e-4-p500x500.jpg","sale_price":0.1,"quantity":2,"sku_name":"白色"},"refund_at":"","created_at":"16-11-26"},{"_id":1112500033,"number":"116112591981","user_id":20448,"target_id":1081052995,"product_id":1081052995,"target_type":2,"stage_label":"退款中","order_rid":"116112506534","sub_order_id":null,"refund_price":0.02,"quantity":2,"type":1,"type_label":"退款","freight":0,"stage":1,"reason":0,"reason_label":null,"content":"ICU空","summary":null,"status":1,"deleted":0,"created_on":1480070033,"updated_on":1480070033,"product":{"title":"这是第一个产品吗","short_title":"这是第一个产品吗","cover_url":"http://frbird.qiniudn.com/stuff/150330/55193e5c3ffca25a6f8b4cd4-1-p500x500.jpg","sale_price":0.01,"quantity":2,"sku_name":""},"refund_at":"","created_at":"16-11-25"},{"_id":1112500032,"number":"116112551708","user_id":20448,"target_id":1011513749,"product_id":1011513749,"target_type":2,"stage_label":"已退款","order_rid":"116112506534","sub_order_id":null,"refund_price":0.15,"quantity":1,"type":1,"type_label":"退款","freight":0,"stage":2,"reason":0,"reason_label":null,"content":"明敏","summary":null,"status":1,"deleted":0,"created_on":1480064915,"updated_on":1480065018,"product":{"title":"a1螺丝刀a1螺丝刀a1螺丝刀","short_title":"a1螺丝刀","cover_url":"http://frbird.qiniudn.com/product/160127/56a858723ffca269098bb039-4-p500x500.jpg","sale_price":0.15,"quantity":1,"sku_name":""},"refund_at":"","created_at":"16-11-25"},{"_id":1112500031,"number":"116112547451","user_id":20448,"target_id":1031789101,"product_id":1011497002,"target_type":1,"stage_label":"退款中","order_rid":"116112506533","sub_order_id":"116112506533-2","refund_price":0.06,"quantity":2,"type":2,"type_label":"退货","freight":0,"stage":1,"reason":0,"reason_label":null,"content":"模棱两可","summary":null,"status":1,"deleted":0,"created_on":1480064696,"updated_on":1480064696,"product":{"title":"华米手环","short_title":"华米手环","cover_url":"http://frbird.qiniudn.com/product/160121/56a0934f3ffca26c098ba90e-4-p500x500.jpg","sale_price":0.1,"quantity":2,"sku_name":"绿色"},"refund_at":"","created_at":"16-11-25"},{"_id":1112500030,"number":"116112533864","user_id":20448,"target_id":1031789053,"product_id":1011497002,"target_type":1,"stage_label":"退款中","order_rid":"116112506533","sub_order_id":"116112506533-1","refund_price":0.04,"quantity":2,"type":2,"type_label":"退货","freight":0,"stage":1,"reason":0,"reason_label":null,"content":"明敏","summary":null,"status":1,"deleted":0,"created_on":1480064391,"updated_on":1480064571,"product":{"title":"华米手环","short_title":"华米手环","cover_url":"http://frbird.qiniudn.com/product/160121/56a0934f3ffca26c098ba90e-4-p500x500.jpg","sale_price":0.1,"quantity":2,"sku_name":"黑色"},"refund_at":"","created_at":"16-11-25"},{"_id":1112500029,"number":"116112557591","user_id":20448,"target_id":1011513749,"product_id":1011513749,"target_type":2,"stage_label":"已退款","order_rid":"116112506532","sub_order_id":null,"refund_price":0.15,"quantity":1,"type":1,"type_label":"退款","freight":0,"stage":2,"reason":1,"reason_label":"不喜欢/不想要了","content":"","summary":null,"status":1,"deleted":0,"created_on":1480063960,"updated_on":1480064341,"product":{"title":"a1螺丝刀a1螺丝刀a1螺丝刀","short_title":"a1螺丝刀","cover_url":"http://frbird.qiniudn.com/product/160127/56a858723ffca269098bb039-4-p500x500.jpg","sale_price":0.15,"quantity":1,"sku_name":""},"refund_at":"","created_at":"16-11-25"},{"_id":1112500028,"number":"116112579701","user_id":20448,"target_id":1031789046,"product_id":1011497002,"target_type":1,"stage_label":"已退款","order_rid":"116112506531","sub_order_id":"116112506531-1","refund_price":0.01,"quantity":1,"type":2,"type_label":"退货","freight":0,"stage":2,"reason":0,"reason_label":null,"content":"匿名给你呀\n","summary":null,"status":1,"deleted":0,"created_on":1480063828,"updated_on":1480063845,"product":{"title":"华米手环","short_title":"华米手环","cover_url":"http://frbird.qiniudn.com/product/160121/56a0934f3ffca26c098ba90e-4-p500x500.jpg","sale_price":0.1,"quantity":1,"sku_name":"白色"},"refund_at":"","created_at":"16-11-25"},{"_id":1112500027,"number":"116112555573","user_id":20448,"target_id":1011513749,"product_id":1011513749,"target_type":2,"stage_label":"退款中","order_rid":"116112506531","sub_order_id":null,"refund_price":0.15,"quantity":1,"type":1,"type_label":"退款","freight":0,"stage":1,"reason":3,"reason_label":"快递/物流没送到","content":"","summary":null,"status":1,"deleted":0,"created_on":1480063706,"updated_on":1480063706,"product":{"title":"a1螺丝刀a1螺丝刀a1螺丝刀","short_title":"a1螺丝刀","cover_url":"http://frbird.qiniudn.com/product/160127/56a858723ffca269098bb039-4-p500x500.jpg","sale_price":0.15,"quantity":1,"sku_name":""},"refund_at":"","created_at":"16-11-25"}]
     * total_page : 5
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
    /**
     * _id : 1112600034
     * number : 116112617334
     * user_id : 20448
     * target_id : 1031789046
     * product_id : 1011497002
     * target_type : 1
     * stage_label : 退款中
     * order_rid : 116112506534
     * sub_order_id : null
     * refund_price : 0.02
     * quantity : 2
     * type : 1
     * type_label : 退款
     * freight : 0
     * stage : 1
     * reason : 0
     * reason_label : null
     * content : 停机外婆短
     * summary : null
     * status : 1
     * deleted : 0
     * created_on : 1480141003
     * updated_on : 1480141003
     * product : {"title":"华米手环","short_title":"华米手环","cover_url":"http://frbird.qiniudn.com/product/160121/56a0934f3ffca26c098ba90e-4-p500x500.jpg","sale_price":0.1,"quantity":2,"sku_name":"白色"}
     * refund_at :
     * created_at : 16-11-26
     */

    private List<RowsBean> rows;

    public int getTotal_rows() {
        return total_rows;
    }

    public void setTotal_rows(int total_rows) {
        this.total_rows = total_rows;
    }

    public int getTotal_page() {
        return total_page;
    }

    public void setTotal_page(int total_page) {
        this.total_page = total_page;
    }

    public int getCurrent_page() {
        return current_page;
    }

    public void setCurrent_page(int current_page) {
        this.current_page = current_page;
    }

    public String getPager() {
        return pager;
    }

    public void setPager(String pager) {
        this.pager = pager;
    }

    public int getNext_page() {
        return next_page;
    }

    public void setNext_page(int next_page) {
        this.next_page = next_page;
    }

    public int getPrev_page() {
        return prev_page;
    }

    public void setPrev_page(int prev_page) {
        this.prev_page = prev_page;
    }

    public int getCurrent_user_id() {
        return current_user_id;
    }

    public void setCurrent_user_id(int current_user_id) {
        this.current_user_id = current_user_id;
    }

    public List<RowsBean> getRows() {
        return rows;
    }

    public void setRows(List<RowsBean> rows) {
        this.rows = rows;
    }

    public static class RowsBean {
        private int _id;
        private String number;
        private int user_id;
        private int target_id;
        private int product_id;
        private int target_type;
        private String stage_label;
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
        private String summary;
        private int status;
        private int deleted;
        private int created_on;
        private int updated_on;
        /**
         * title : 华米手环
         * short_title : 华米手环
         * cover_url : http://frbird.qiniudn.com/product/160121/56a0934f3ffca26c098ba90e-4-p500x500.jpg
         * sale_price : 0.1
         * quantity : 2
         * sku_name : 白色
         */

        private ProductBean product;
        private String refund_at;
        private String created_at;

        public int get_id() {
            return _id;
        }

        public void set_id(int _id) {
            this._id = _id;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }

        public int getTarget_id() {
            return target_id;
        }

        public void setTarget_id(int target_id) {
            this.target_id = target_id;
        }

        public int getProduct_id() {
            return product_id;
        }

        public void setProduct_id(int product_id) {
            this.product_id = product_id;
        }

        public int getTarget_type() {
            return target_type;
        }

        public void setTarget_type(int target_type) {
            this.target_type = target_type;
        }

        public String getStage_label() {
            return stage_label;
        }

        public void setStage_label(String stage_label) {
            this.stage_label = stage_label;
        }

        public String getOrder_rid() {
            return order_rid;
        }

        public void setOrder_rid(String order_rid) {
            this.order_rid = order_rid;
        }

        public String getSub_order_id() {
            return sub_order_id;
        }

        public void setSub_order_id(String sub_order_id) {
            this.sub_order_id = sub_order_id;
        }

        public double getRefund_price() {
            return refund_price;
        }

        public void setRefund_price(double refund_price) {
            this.refund_price = refund_price;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getType_label() {
            return type_label;
        }

        public void setType_label(String type_label) {
            this.type_label = type_label;
        }

        public int getFreight() {
            return freight;
        }

        public void setFreight(int freight) {
            this.freight = freight;
        }

        public int getStage() {
            return stage;
        }

        public void setStage(int stage) {
            this.stage = stage;
        }

        public int getReason() {
            return reason;
        }

        public void setReason(int reason) {
            this.reason = reason;
        }

        public String getReason_label() {
            return reason_label;
        }

        public void setReason_label(String reason_label) {
            this.reason_label = reason_label;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getSummary() {
            return summary;
        }

        public void setSummary(String summary) {
            this.summary = summary;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getDeleted() {
            return deleted;
        }

        public void setDeleted(int deleted) {
            this.deleted = deleted;
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

        public ProductBean getProduct() {
            return product;
        }

        public void setProduct(ProductBean product) {
            this.product = product;
        }

        public String getRefund_at() {
            return refund_at;
        }

        public void setRefund_at(String refund_at) {
            this.refund_at = refund_at;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public static class ProductBean {
            private String title;
            private String short_title;
            private String cover_url;
            private double sale_price;
            private int quantity;
            private String sku_name;

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getShort_title() {
                return short_title;
            }

            public void setShort_title(String short_title) {
                this.short_title = short_title;
            }

            public String getCover_url() {
                return cover_url;
            }

            public void setCover_url(String cover_url) {
                this.cover_url = cover_url;
            }

            public double getSale_price() {
                return sale_price;
            }

            public void setSale_price(double sale_price) {
                this.sale_price = sale_price;
            }

            public int getQuantity() {
                return quantity;
            }

            public void setQuantity(int quantity) {
                this.quantity = quantity;
            }

            public String getSku_name() {
                return sku_name;
            }

            public void setSku_name(String sku_name) {
                this.sku_name = sku_name;
            }
        }
    }
}
