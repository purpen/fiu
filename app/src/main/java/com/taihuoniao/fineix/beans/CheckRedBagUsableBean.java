package com.taihuoniao.fineix.beans;

import java.io.Serializable;

/**
 * Created by android on 2016/3/29.
 */
public class CheckRedBagUsableBean implements Serializable {
    private String code,coin_money,useful;

    @Override
    public String toString() {
        return "CheckRedBagUsableBean{" +
                "code='" + code + '\'' +
                ", coin_money='" + coin_money + '\'' +
                ", useful='" + useful + '\'' +
                '}';
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCoin_money() {
        return coin_money;
    }

    public void setCoin_money(String coin_money) {
        this.coin_money = coin_money;
    }

    public String getUseful() {
        return useful;
    }

    public void setUseful(String useful) {
        this.useful = useful;
    }
}
