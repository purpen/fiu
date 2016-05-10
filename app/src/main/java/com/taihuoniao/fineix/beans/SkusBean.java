package com.taihuoniao.fineix.beans;

/**
 * Created by taihuoniao on 2016/2/26.
 */
public class SkusBean {
    private String _id;
    private String mode;
    private String price;
    private String quantity;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
}
