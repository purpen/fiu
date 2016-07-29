package com.taihuoniao.fineix.beans;

import java.io.Serializable;

/**
 * Created by taihuoniao on 2016/3/3.
 * 立即下单网络返回值javabean
 */
public class NowConfirmBean implements Serializable{
    private String rid;
    private String message;
    private boolean success;
    private String pay_money;
    public int status;
    public String getPay_money() {
        return pay_money;
    }

    public void setPay_money(String pay_money) {
        this.pay_money = pay_money;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
