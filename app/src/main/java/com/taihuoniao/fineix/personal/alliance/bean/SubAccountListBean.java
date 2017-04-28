package com.taihuoniao.fineix.personal.alliance.bean;

import java.util.List;

/**
 * Created by Stephen on 2017/4/28 9:28
 * Email: 895745843@qq.com
 */

public class SubAccountListBean {

    /**
     * total_rows : 3
     * rows : [{"_id":"590159df5c42ece9700041a9","pid":36,"cid":36,"scene_id":0,"username":"tian","account":"15001120509","remark":null,"amount":23.23,"status":1,"created_on":1493260767,"updated_on":1493260767,"created_at":"10分钟前"}]
     * total_page : 1
     * current_page : 1
     * pager : 
     * next_page : 0
     * prev_page : 0
     * current_user_id : 36
     */

    private String total_rows;
    private String total_page;
    private String current_page;
    private String pager;
    private String next_page;
    private String prev_page;
    private String current_user_id;
    private List<RowsEntity> rows;

    public String getTotal_rows() {
        return total_rows;
    }

    public void setTotal_rows(String total_rows) {
        this.total_rows = total_rows;
    }

    public String getTotal_page() {
        return total_page;
    }

    public void setTotal_page(String total_page) {
        this.total_page = total_page;
    }

    public String getCurrent_page() {
        return current_page;
    }

    public void setCurrent_page(String current_page) {
        this.current_page = current_page;
    }

    public String getPager() {
        return pager;
    }

    public void setPager(String pager) {
        this.pager = pager;
    }

    public String getNext_page() {
        return next_page;
    }

    public void setNext_page(String next_page) {
        this.next_page = next_page;
    }

    public String getPrev_page() {
        return prev_page;
    }

    public void setPrev_page(String prev_page) {
        this.prev_page = prev_page;
    }

    public String getCurrent_user_id() {
        return current_user_id;
    }

    public void setCurrent_user_id(String current_user_id) {
        this.current_user_id = current_user_id;
    }

    public List<RowsEntity> getRows() {
        return rows;
    }

    public void setRows(List<RowsEntity> rows) {
        this.rows = rows;
    }

    public static class RowsEntity {
        /**
         * _id : 590159df5c42ece9700041a9
         * pid : 36
         * cid : 36
         * scene_id : 0
         * username : tian
         * account : 15001120509
         * remark : null
         * amount : 23.23
         * status : 1
         * created_on : 1493260767
         * updated_on : 1493260767
         * created_at : 10分钟前
         */

        private String _id;
        private String pid;
        private String cid;
        private String scene_id;
        private String username;
        private String account;
        private String remark;
        private String amount;
        private String status;
        private String created_on;
        private String updated_on;
        private String created_at;

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

        public String getPid() {
            return pid;
        }

        public void setPid(String pid) {
            this.pid = pid;
        }

        public String getCid() {
            return cid;
        }

        public void setCid(String cid) {
            this.cid = cid;
        }

        public String getScene_id() {
            return scene_id;
        }

        public void setScene_id(String scene_id) {
            this.scene_id = scene_id;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getCreated_on() {
            return created_on;
        }

        public void setCreated_on(String created_on) {
            this.created_on = created_on;
        }

        public String getUpdated_on() {
            return updated_on;
        }

        public void setUpdated_on(String updated_on) {
            this.updated_on = updated_on;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }
    }
}
