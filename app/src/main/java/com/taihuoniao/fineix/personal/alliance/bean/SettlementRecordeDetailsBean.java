package com.taihuoniao.fineix.personal.alliance.bean;

import java.util.List;

/**
 * Created by Stephen on 2017/1/20 10:29
 * Email: 895745843@qq.com
 */

public class SettlementRecordeDetailsBean {

    /**
     * total_rows : 1
     * rows : [{"_id":"5894aa2d1a1de7535bc8682d","alliance_id":"586f6e1f20de8d8e608bc612","user_id":20448,"balance_id":"588167f520de8d79238b721b","balance_record_id":"5894aa2d1a1de7535bc8682c","amount":1.5,"status":1,"created_on":1486137901,"updated_on":1486137901,"balance":{"_id":"588167f520de8d79238b721b","alliance_id":"586f6e1f20de8d8e608bc612","user_id":20448,"order_rid":"117012049852","product_id":1050370726,"sku_id":1050385339,"quantity":5,"commision_percent":0.05,"addition":1,"code":"ZzEJfc","sku_price":6,"unit_price":0.3,"total_price":1.5,"kind":1,"type":2,"from_site":1,"sub_order_id":null,"summary":null,"stage":5,"status":1,"balance_on":1486137901,"created_on":1484875765,"updated_on":1486137901,"type_label":"公司","kind_label":"推广佣金","stage_label":"可结算","status_label":"已结算","commision_percent_p":5,"__extend__":true,"title":"订单[117012049852]"},"created_at":"2017-02-04 00:05"}]
     * total_page : 1
     * current_page : 1
     * pager :
     * next_page : 0
     * prev_page : 0
     * current_user_id : 20448
     */

    public int total_rows;
    public int total_page;
    public int current_page;
    public String pager;
    public int next_page;
    public int prev_page;
    public int current_user_id;
    public List<RowsBean> rows;

    public static class RowsBean {
        /**
         * _id : 5894aa2d1a1de7535bc8682d
         * alliance_id : 586f6e1f20de8d8e608bc612
         * user_id : 20448
         * balance_id : 588167f520de8d79238b721b
         * balance_record_id : 5894aa2d1a1de7535bc8682c
         * amount : 1.5
         * status : 1
         * created_on : 1486137901
         * updated_on : 1486137901
         * balance : {"_id":"588167f520de8d79238b721b","alliance_id":"586f6e1f20de8d8e608bc612","user_id":20448,"order_rid":"117012049852","product_id":1050370726,"sku_id":1050385339,"quantity":5,"commision_percent":0.05,"addition":1,"code":"ZzEJfc","sku_price":6,"unit_price":0.3,"total_price":1.5,"kind":1,"type":2,"from_site":1,"sub_order_id":null,"summary":null,"stage":5,"status":1,"balance_on":1486137901,"created_on":1484875765,"updated_on":1486137901,"type_label":"公司","kind_label":"推广佣金","stage_label":"可结算","status_label":"已结算","commision_percent_p":5,"__extend__":true,"title":"订单[117012049852]"}
         * created_at : 2017-02-04 00:05
         */

        public String _id;
        public String alliance_id;
        public int user_id;
        public String balance_id;
        public String balance_record_id;
        public double amount;
        public int status;
        public int created_on;
        public int updated_on;
        public BalanceBean balance;
        public String created_at;

        public static class BalanceBean {
            /**
             * _id : 588167f520de8d79238b721b
             * alliance_id : 586f6e1f20de8d8e608bc612
             * user_id : 20448
             * order_rid : 117012049852
             * product_id : 1050370726
             * sku_id : 1050385339
             * quantity : 5
             * commision_percent : 0.05
             * addition : 1
             * code : ZzEJfc
             * sku_price : 6
             * unit_price : 0.3
             * total_price : 1.5
             * kind : 1
             * type : 2
             * from_site : 1
             * sub_order_id : null
             * summary : null
             * stage : 5
             * status : 1
             * balance_on : 1486137901
             * created_on : 1484875765
             * updated_on : 1486137901
             * type_label : 公司
             * kind_label : 推广佣金
             * stage_label : 可结算
             * status_label : 已结算
             * commision_percent_p : 5
             * __extend__ : true
             * title : 订单[117012049852]
             */

            public String _id;
            public String alliance_id;
            public int user_id;
            public String order_rid;
            public int product_id;
            public int sku_id;
            public int quantity;
            public double commision_percent;
            public int addition;
            public String code;
            public int sku_price;
            public double unit_price;
            public double total_price;
            public int kind;
            public int type;
            public int from_site;
            public int stage;
            public int status;
            public int balance_on;
            public int created_on;
            public int updated_on;
            public String type_label;
            public String kind_label;
            public String stage_label;
            public String status_label;
            public int commision_percent_p;
            public boolean __extend__;
            public String title;
        }
    }
}
