package com.taihuoniao.fineix.personal.alliance.bean;

import java.util.List;

/**
 * Created by Stephen on 2017/1/20 10:29
 * Email: 895745843@qq.com
 */

public class SettlementRecordeDetailsBean {

    /**
     * total_rows : 4
     * rows : [{"_id":"587f44d53ffca271418b4568","alliance_id":"587dd1343ffca27e2a8baaec","user_id":20448,"balance_id":"587de8ab3ffca234278b54b2","balance_record_id":"587f44d53ffca271418b4567","amount":29.9,"status":1,"created_on":1484735701,"updated_on":1484735701,"balance":{"_id":"587de8ab3ffca234278b54b2","alliance_id":"587dd1343ffca27e2a8baaec","user_id":20448,"order_rid":"117011706880","product_id":1120700276,"sku_id":1120700296,"quantity":1,"commision_percent":0.1,"addition":1,"code":"x518fe","sku_price":299,"unit_price":29.9,"total_price":29.9,"kind":1,"type":2,"from_site":1,"sub_order_id":null,"summary":null,"stage":5,"status":1,"balance_on":1484735701,"created_on":1484646571,"updated_on":1484735701,"type_label":"公司","kind_label":"推广","stage_label":"可结算","status_label":"已结算","commision_percent_p":10,"__extend__":true,"product":{"title":"小蚁1号智能私房音箱","short_title":"小蚁1号智能私房音箱"}},"created_at":"2017-01-18 18:35"},{"_id":"587f44d53ffca271418b4569","alliance_id":"587dd1343ffca27e2a8baaec","user_id":20448,"balance_id":"587f431b3ffca2482f8b4694","balance_record_id":"587f44d53ffca271418b4567","amount":29.9,"status":1,"created_on":1484735701,"updated_on":1484735701,"balance":{"_id":"587f431b3ffca2482f8b4694","alliance_id":"587dd1343ffca27e2a8baaec","user_id":20448,"order_rid":"117011807045","product_id":1120700195,"sku_id":1015010032,"quantity":1,"commision_percent":0.1,"addition":1,"code":"x518fe","sku_price":299,"unit_price":29.9,"total_price":29.9,"kind":1,"type":2,"from_site":1,"sub_order_id":null,"summary":null,"stage":5,"status":1,"balance_on":1484735701,"created_on":1484735259,"updated_on":1484735701,"type_label":"公司","kind_label":"推广","stage_label":"可结算","status_label":"已结算","commision_percent_p":10,"__extend__":true,"product":{"title":"多功能星盘智能插座","short_title":"多功能星盘智能插座"}},"created_at":"2017-01-18 18:35"},{"_id":"587f44d53ffca271418b456a","alliance_id":"587dd1343ffca27e2a8baaec","user_id":20448,"balance_id":"587f438b3ffca2ba3d8b4596","balance_record_id":"587f44d53ffca271418b4567","amount":199.6,"status":1,"created_on":1484735701,"updated_on":1484735701,"balance":{"_id":"587f438b3ffca2ba3d8b4596","alliance_id":"587dd1343ffca27e2a8baaec","user_id":20448,"order_rid":"117011807046","product_id":1120677456,"sku_id":1120677465,"quantity":2,"commision_percent":0.1,"addition":1,"code":"x518fe","sku_price":998,"unit_price":99.8,"total_price":199.6,"kind":1,"type":2,"from_site":1,"sub_order_id":null,"summary":null,"stage":5,"status":1,"balance_on":1484735701,"created_on":1484735371,"updated_on":1484735701,"type_label":"公司","kind_label":"推广","stage_label":"可结算","status_label":"已结算","commision_percent_p":10,"__extend__":true,"product":{"title":"爱牵挂智能腕表老人专用危情报警心率检测触摸按键智能提醒","short_title":"爱牵挂智能腕表老人专用危情报警心率检测触摸按键智能提醒"}},"created_at":"2017-01-18 18:35"}]
     * total_page : 2
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
         * _id : 587f44d53ffca271418b4568
         * alliance_id : 587dd1343ffca27e2a8baaec
         * user_id : 20448
         * balance_id : 587de8ab3ffca234278b54b2
         * balance_record_id : 587f44d53ffca271418b4567
         * amount : 29.9
         * status : 1
         * created_on : 1484735701
         * updated_on : 1484735701
         * balance : {"_id":"587de8ab3ffca234278b54b2","alliance_id":"587dd1343ffca27e2a8baaec","user_id":20448,"order_rid":"117011706880","product_id":1120700276,"sku_id":1120700296,"quantity":1,"commision_percent":0.1,"addition":1,"code":"x518fe","sku_price":299,"unit_price":29.9,"total_price":29.9,"kind":1,"type":2,"from_site":1,"sub_order_id":null,"summary":null,"stage":5,"status":1,"balance_on":1484735701,"created_on":1484646571,"updated_on":1484735701,"type_label":"公司","kind_label":"推广","stage_label":"可结算","status_label":"已结算","commision_percent_p":10,"__extend__":true,"product":{"title":"小蚁1号智能私房音箱","short_title":"小蚁1号智能私房音箱"}}
         * created_at : 2017-01-18 18:35
         */

        private String _id;
        private String alliance_id;
        private String user_id;
        private String balance_id;
        private String balance_record_id;
        private String amount;
        private String status;
        private String created_on;
        private String updated_on;
        private BalanceEntity balance;
        private String created_at;

        public void set_id(String _id) {
            this._id = _id;
        }

        public void setAlliance_id(String alliance_id) {
            this.alliance_id = alliance_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public void setBalance_id(String balance_id) {
            this.balance_id = balance_id;
        }

        public void setBalance_record_id(String balance_record_id) {
            this.balance_record_id = balance_record_id;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public void setCreated_on(String created_on) {
            this.created_on = created_on;
        }

        public void setUpdated_on(String updated_on) {
            this.updated_on = updated_on;
        }

        public void setBalance(BalanceEntity balance) {
            this.balance = balance;
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

        public String getUser_id() {
            return user_id;
        }

        public String getBalance_id() {
            return balance_id;
        }

        public String getBalance_record_id() {
            return balance_record_id;
        }

        public String getAmount() {
            return amount;
        }

        public String getStatus() {
            return status;
        }

        public String getCreated_on() {
            return created_on;
        }

        public String getUpdated_on() {
            return updated_on;
        }

        public BalanceEntity getBalance() {
            return balance;
        }

        public String getCreated_at() {
            return created_at;
        }

        public static class BalanceEntity {
            /**
             * _id : 587de8ab3ffca234278b54b2
             * alliance_id : 587dd1343ffca27e2a8baaec
             * user_id : 20448
             * order_rid : 117011706880
             * product_id : 1120700276
             * sku_id : 1120700296
             * quantity : 1
             * commision_percent : 0.1
             * addition : 1
             * code : x518fe
             * sku_price : 299
             * unit_price : 29.9
             * total_price : 29.9
             * kind : 1
             * type : 2
             * from_site : 1
             * sub_order_id : null
             * summary : null
             * stage : 5
             * status : 1
             * balance_on : 1484735701
             * created_on : 1484646571
             * updated_on : 1484735701
             * type_label : 公司
             * kind_label : 推广
             * stage_label : 可结算
             * status_label : 已结算
             * commision_percent_p : 10
             * __extend__ : true
             * product : {"title":"小蚁1号智能私房音箱","short_title":"小蚁1号智能私房音箱"}
             */

            private String _id;
            private String alliance_id;
            private String user_id;
            private String order_rid;
            private String product_id;
            private String sku_id;
            private String quantity;
            private String commision_percent;
            private String addition;
            private String code;
            private String sku_price;
            private String unit_price;
            private String total_price;
            private String kind;
            private String type;
            private String from_site;
            private Object sub_order_id;
            private Object summary;
            private String stage;
            private String status;
            private String balance_on;
            private String created_on;
            private String updated_on;
            private String type_label;
            private String kind_label;
            private String stage_label;
            private String status_label;
            private String commision_percent_p;
            private boolean __extend__;
            private ProductEntity product;

            public void set_id(String _id) {
                this._id = _id;
            }

            public void setAlliance_id(String alliance_id) {
                this.alliance_id = alliance_id;
            }

            public void setUser_id(String user_id) {
                this.user_id = user_id;
            }

            public void setOrder_rid(String order_rid) {
                this.order_rid = order_rid;
            }

            public void setProduct_id(String product_id) {
                this.product_id = product_id;
            }

            public void setSku_id(String sku_id) {
                this.sku_id = sku_id;
            }

            public void setQuantity(String quantity) {
                this.quantity = quantity;
            }

            public void setCommision_percent(String commision_percent) {
                this.commision_percent = commision_percent;
            }

            public void setAddition(String addition) {
                this.addition = addition;
            }

            public void setCode(String code) {
                this.code = code;
            }

            public void setSku_price(String sku_price) {
                this.sku_price = sku_price;
            }

            public void setUnit_price(String unit_price) {
                this.unit_price = unit_price;
            }

            public void setTotal_price(String total_price) {
                this.total_price = total_price;
            }

            public void setKind(String kind) {
                this.kind = kind;
            }

            public void setType(String type) {
                this.type = type;
            }

            public void setFrom_site(String from_site) {
                this.from_site = from_site;
            }

            public void setSub_order_id(Object sub_order_id) {
                this.sub_order_id = sub_order_id;
            }

            public void setSummary(Object summary) {
                this.summary = summary;
            }

            public void setStage(String stage) {
                this.stage = stage;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public void setBalance_on(String balance_on) {
                this.balance_on = balance_on;
            }

            public void setCreated_on(String created_on) {
                this.created_on = created_on;
            }

            public void setUpdated_on(String updated_on) {
                this.updated_on = updated_on;
            }

            public void setType_label(String type_label) {
                this.type_label = type_label;
            }

            public void setKind_label(String kind_label) {
                this.kind_label = kind_label;
            }

            public void setStage_label(String stage_label) {
                this.stage_label = stage_label;
            }

            public void setStatus_label(String status_label) {
                this.status_label = status_label;
            }

            public void setCommision_percent_p(String commision_percent_p) {
                this.commision_percent_p = commision_percent_p;
            }

            public void set__extend__(boolean __extend__) {
                this.__extend__ = __extend__;
            }

            public void setProduct(ProductEntity product) {
                this.product = product;
            }

            public String get_id() {
                return _id;
            }

            public String getAlliance_id() {
                return alliance_id;
            }

            public String getUser_id() {
                return user_id;
            }

            public String getOrder_rid() {
                return order_rid;
            }

            public String getProduct_id() {
                return product_id;
            }

            public String getSku_id() {
                return sku_id;
            }

            public String getQuantity() {
                return quantity;
            }

            public String getCommision_percent() {
                return commision_percent;
            }

            public String getAddition() {
                return addition;
            }

            public String getCode() {
                return code;
            }

            public String getSku_price() {
                return sku_price;
            }

            public String getUnit_price() {
                return unit_price;
            }

            public String getTotal_price() {
                return total_price;
            }

            public String getKind() {
                return kind;
            }

            public String getType() {
                return type;
            }

            public String getFrom_site() {
                return from_site;
            }

            public Object getSub_order_id() {
                return sub_order_id;
            }

            public Object getSummary() {
                return summary;
            }

            public String getStage() {
                return stage;
            }

            public String getStatus() {
                return status;
            }

            public String getBalance_on() {
                return balance_on;
            }

            public String getCreated_on() {
                return created_on;
            }

            public String getUpdated_on() {
                return updated_on;
            }

            public String getType_label() {
                return type_label;
            }

            public String getKind_label() {
                return kind_label;
            }

            public String getStage_label() {
                return stage_label;
            }

            public String getStatus_label() {
                return status_label;
            }

            public String getCommision_percent_p() {
                return commision_percent_p;
            }

            public boolean get__extend__() {
                return __extend__;
            }

            public ProductEntity getProduct() {
                return product;
            }

            public static class ProductEntity {
                /**
                 * title : 小蚁1号智能私房音箱
                 * short_title : 小蚁1号智能私房音箱
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
    }
}
