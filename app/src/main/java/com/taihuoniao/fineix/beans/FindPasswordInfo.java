package com.taihuoniao.fineix.beans;

import java.io.Serializable;

/**
 * Created by android on 2016/1/21.
 */
public class FindPasswordInfo implements Serializable {
    private Boolean success;
    private String message;
    private String account;

    @Override
    public String toString() {
        return "FindPasswordInfo{" +
                "success=" + success +
                ", message='" + message + '\'' +
                ", account='" + account + '\'' +
                '}';
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }
}
