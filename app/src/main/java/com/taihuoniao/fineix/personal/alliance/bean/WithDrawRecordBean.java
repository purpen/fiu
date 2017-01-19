package com.taihuoniao.fineix.personal.alliance.bean;

import java.util.List;

/**
 * Created by Stephen on 2017/1/19 19:46
 * Email: 895745843@qq.com
 */

public class WithDrawRecordBean {

    /**
     * total_rows : 7
     * rows : [{"_id":"588021963ffca2482f8b491b","alliance_id":"587dd1343ffca27e2a8baaec","user_id":20448,"present_on":1484792275,"amount":100.01,"status":5,"created_on":1484792214,"updated_on":1484792275},{"_id":"58801f883ffca2641b8b4992","alliance_id":"587dd1343ffca27e2a8baaec","user_id":20448,"present_on":1484792281,"amount":100.8,"status":5,"created_on":1484791688,"updated_on":1484792281},{"_id":"587f37b83ffca2641b8b46dc","alliance_id":"587dd1343ffca27e2a8baaec","user_id":20448,"present_on":1484792282,"amount":121,"status":5,"created_on":1484732344,"updated_on":1484792282}]
     * total_page : 3
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
         * _id : 588021963ffca2482f8b491b
         * alliance_id : 587dd1343ffca27e2a8baaec
         * user_id : 20448
         * present_on : 1484792275
         * amount : 100.01
         * status : 5
         * created_on : 1484792214
         * updated_on : 1484792275
         */

        private String _id;
        private String alliance_id;
        private String user_id;
        private String present_on;
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

        public void setPresent_on(String present_on) {
            this.present_on = present_on;
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

        public String get_id() {
            return _id;
        }

        public String getAlliance_id() {
            return alliance_id;
        }

        public String getUser_id() {
            return user_id;
        }

        public String getPresent_on() {
            return present_on;
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

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }
    }
}
