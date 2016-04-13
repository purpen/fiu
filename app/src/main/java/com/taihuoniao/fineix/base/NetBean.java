package com.taihuoniao.fineix.base;

/**
 * Created by taihuoniao on 2016/3/23.
 */
public class NetBean {
    protected boolean success;
    protected String message;
    protected String status;


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
