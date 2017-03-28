package com.taihuoniao.fineix.beans;

import java.io.Serializable;

/**
 * Created by android on 2016/1/21.
 */
public class FindPasswordInfoBean implements Serializable {
    private String account;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }
}
