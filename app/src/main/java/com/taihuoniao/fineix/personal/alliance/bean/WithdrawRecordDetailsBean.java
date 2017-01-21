package com.taihuoniao.fineix.personal.alliance.bean;

/**
 * Created by Stephen on 2017/1/20 10:32
 * Email: 895745843@qq.com
 */

public class WithdrawRecordDetailsBean {

    /**
     * _id : 588021963ffca2482f8b491b
     * alliance_id : 587dd1343ffca27e2a8baaec
     * user_id : 20448
     * present_on : 1484792275
     * amount : 100.01
     * status : 5
     * created_on : 1484792214
     * updated_on : 1484792275
     * created_at : 2017-01-19 10:16
     * current_user_id : 20448
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
    private String current_user_id;

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

    public String getCurrent_user_id() {
        return current_user_id;
    }
}
