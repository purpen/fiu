package com.taihuoniao.fineix.beans;

import java.io.Serializable;

/**
 * Created by android on 2016/3/19.
 */
public class SkipBind implements Serializable {
    private String success;

    @Override
    public String toString() {
        return "SkipBind{" +
                "success='" + success + '\'' +
                '}';
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }
}
