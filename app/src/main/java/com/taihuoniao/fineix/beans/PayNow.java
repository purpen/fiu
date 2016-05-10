package com.taihuoniao.fineix.beans;

import java.io.Serializable;

/**
 * Created by android on 2016/3/13.
 */
public class PayNow implements Serializable {
    private String str;

    @Override
    public String toString() {
        return "PayNow{" +
                "str='" + str + '\'' +
                '}';
    }

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }
}
