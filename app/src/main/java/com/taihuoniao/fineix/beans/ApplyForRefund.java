package com.taihuoniao.fineix.beans;

import java.io.Serializable;

/**
 * Created by android on 2016/3/22.
 */
public class ApplyForRefund implements Serializable {
    private String success,message,rid;

    @Override
    public String toString() {
        return "ApplyForRefund{" +
                "success='" + success + '\'' +
                ", message='" + message + '\'' +
                ", rid='" + rid + '\'' +
                '}';
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
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
}
