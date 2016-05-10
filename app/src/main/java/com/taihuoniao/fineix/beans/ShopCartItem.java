package com.taihuoniao.fineix.beans;

import java.io.Serializable;

/**
 * Created by android on 2016/3/2.
 */
public class ShopCartItem implements Serializable {
    private String price;
    private String sku_mode;
    private String cover;
    private String total_price;
    private String n;
    private String title;
    private String target_id;
    private String type;
    private String product_id;

    @Override
    public String toString() {
        return "ShopCartItem{" +
                "price='" + price + '\'' +
                ", sku_mode='" + sku_mode + '\'' +
                ", cover='" + cover + '\'' +
                ", total_price='" + total_price + '\'' +
                ", n='" + n + '\'' +
                ", title='" + title + '\'' +
                ", target_id='" + target_id + '\'' +
                ", type='" + type + '\'' +
                ", product_id='" + product_id + '\'' +
                '}';
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getSku_mode() {
        return sku_mode;
    }

    public void setSku_mode(String sku_mode) {
        this.sku_mode = sku_mode;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getTotal_price() {
        return total_price;
    }

    public void setTotal_price(String total_price) {
        this.total_price = total_price;
    }

    public String getN() {
        return n;
    }

    public void setN(String n) {
        this.n = n;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTarget_id() {
        return target_id;
    }

    public void setTarget_id(String target_id) {
        this.target_id = target_id;
    }
}
