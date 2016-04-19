package com.taihuoniao.fineix.beans;

import java.io.Serializable;

/**
 * Created by android on 2016/3/19.
 */
public class ThirdLogin implements Serializable {
    private String has_user,success;

    @Override
    public String toString() {
        return "ThirdLogin{" +
                "has_user='" + has_user + '\'' +
                ", success='" + success + '\'' +
                '}';
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getHas_user() {
        return has_user;
    }

    public void setHas_user(String has_user) {
        this.has_user = has_user;
    }
}
