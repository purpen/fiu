package com.taihuoniao.fineix.beans;

import java.io.Serializable;

/**
 * Created by android on 2016/3/5.
 */
public class ShopCartNumber implements Serializable {
    private String count,success;

    @Override
    public String toString() {
        return "ShopCartNumber{" +
                "count='" + count + '\'' +
                ", success='" + success + '\'' +
                '}';
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }
}
