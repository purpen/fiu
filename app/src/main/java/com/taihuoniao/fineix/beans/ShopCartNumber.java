package com.taihuoniao.fineix.beans;

import java.io.Serializable;

/**
 * Created by android on 2016/3/5.
 */
public class ShopCartNumber implements Serializable {
    private boolean success;
    private int count;

    @Override
    public String toString() {
        return "ShopCartNumber{" +
                "count='" + count + '\'' +
                ", success='" + success + '\'' +
                '}';
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
