package com.taihuoniao.fineix.base;

/**
 * Created by taihuoniao on 2016/3/23.
 */
public class NetBean {
    protected boolean success;
    protected String message;
    protected String current_user_id;
//    protected String status;
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

    public String getCurrent_user_id() {
        return current_user_id;
    }

    public void setCurrent_user_id(String current_user_id) {
        this.current_user_id = current_user_id;
    }
}
