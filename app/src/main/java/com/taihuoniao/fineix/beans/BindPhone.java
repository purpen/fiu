package com.taihuoniao.fineix.beans;

import java.io.Serializable;

/**
 * Created by android on 2016/3/20.
 */
public class BindPhone implements Serializable {
    private String success,message;

    @Override
    public String toString() {
        return "BindPhone{" +
                "success='" + success + '\'' +
                ", message='" + message + '\'' +
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
}
