package com.taihuoniao.fineix.beans;

import java.io.Serializable;

/**
 * Created by android on 2016/3/9.
 */
public class OrderItem implements Serializable {
    private String quantity,product_id,name,cover_url,sale_price,sku_name,sku;

    @Override
    public String toString() {
        return "OrderItem{" +
                "quantity='" + quantity + '\'' +
                ", product_id='" + product_id + '\'' +
                ", name='" + name + '\'' +
                ", cover_url='" + cover_url + '\'' +
                ", sale_price='" + sale_price + '\'' +
                ", sku_name='" + sku_name + '\'' +
                ", sku='" + sku + '\'' +
                '}';
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCover_url() {
        return cover_url;
    }

    public void setCover_url(String cover_url) {
        this.cover_url = cover_url;
    }

    public String getSale_price() {
        return sale_price;
    }

    public void setSale_price(String sale_price) {
        this.sale_price = sale_price;
    }

    public String getSku_name() {
        return sku_name;
    }

    public void setSku_name(String sku_name) {
        this.sku_name = sku_name;
    }
}
