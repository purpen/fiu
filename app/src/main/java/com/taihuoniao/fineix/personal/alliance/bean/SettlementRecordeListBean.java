package com.taihuoniao.fineix.personal.alliance.bean;

import java.util.List;

/**
 * Created by Stephen on 2017/1/19 18:47
 * Email: 895745843@qq.com
 */

public class SettlementRecordeListBean {

    /**
     * total_rows : 3
     * rows : [{"_id":"587f44d53ffca271418b4567","alliance_id":"587dd1343ffca27e2a8baaec","user_id":20448,"balance_count":4,"amount":558.8,"status":1,"created_on":1484735701,"updated_on":1484735701,"created_at":"2017-01-18 18:35"},{"_id":"587de8c93ffca26b478b4567","alliance_id":"587dd1343ffca27e2a8baaec","user_id":20448,"balance_count":5,"amount":291.8,"status":1,"created_on":1484646601,"updated_on":1484646601,"created_at":"2017-01-17 17:50"},{"_id":"587de0953ffca2b2448b4567","alliance_id":"587dd1343ffca27e2a8baaec","user_id":20448,"balance_count":2,"amount":299.7,"status":1,"created_on":1484644501,"updated_on":1484644501,"created_at":"2017-01-17 17:15"}]
     * total_page : 1
     * current_page : 1
     * pager : 
     * next_page : 0
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
         * _id : 587f44d53ffca271418b4567
         * alliance_id : 587dd1343ffca27e2a8baaec
         * user_id : 20448
         * balance_count : 4
         * amount : 558.8
         * status : 1
         * created_on : 1484735701
         * updated_on : 1484735701
         * created_at : 2017-01-18 18:35
         */

        private String _id;
        private String alliance_id;
        private String user_id;
        private String balance_count;
        private String amount;
        private String status;
        private String created_on;
        private String updated_on;
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

        public void setBalance_count(String balance_count) {
            this.balance_count = balance_count;
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

        public String getBalance_count() {
            return balance_count;
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

        public String getCreated_at() {
            return created_at;
        }
    }
}
