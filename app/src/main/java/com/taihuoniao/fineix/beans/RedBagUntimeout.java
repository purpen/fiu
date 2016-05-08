package com.taihuoniao.fineix.beans;

import java.io.Serializable;

/**
 * Created by android on 2016/2/23.
 */
public class RedBagUntimeout implements Serializable{
    private int amount;
    private String code;
    private int min_amount;

    @Override
    public String toString() {
        return "RedBagUntimeout{" +
                "amount=" + amount +
                ", code='" + code + '\'' +
                ", min_amount=" + min_amount +
                '}';
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getMin_amount() {
        return min_amount;
    }

    public void setMin_amount(int min_amount) {
        this.min_amount = min_amount;
    }
}
