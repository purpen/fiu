package com.taihuoniao.fineix.beans;

import java.io.Serializable;

/**
 * Created by android on 2016/4/27.
 */
public class ShopCartInventoryItemBean implements Serializable {
    private String target_id,type,n,quantity,product_id;

    @Override
    public String toString() {
        return "ShopCartInventoryItemBean{" +
                "target_id='" + target_id + '\'' +
                ", type='" + type + '\'' +
                ", n='" + n + '\'' +
                ", quantity='" + quantity + '\'' +
                ", product_id='" + product_id + '\'' +
                '}';
    }

    public String getTarget_id() {
        return target_id;
    }

    public void setTarget_id(String target_id) {
        this.target_id = target_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getN() {
        return n;
    }

    public void setN(String n) {
        this.n = n;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }
}
