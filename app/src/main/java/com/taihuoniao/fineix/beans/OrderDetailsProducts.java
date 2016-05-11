package com.taihuoniao.fineix.beans;

import java.io.Serializable;

/**
 * Created by android on 2016/3/13.
 */
public class OrderDetailsProducts implements Serializable {
    private String sku, product_id, quantity, price, sale_price, name, sku_name, cover_url;

    @Override
    public String toString() {
        return "OrderDetailsProducts{" +
                "sku='" + sku + '\'' +
                ", product_id='" + product_id + '\'' +
                ", quantity='" + quantity + '\'' +
                ", price='" + price + '\'' +
                ", sale_price='" + sale_price + '\'' +
                ", name='" + name + '\'' +
                ", sku_name='" + sku_name + '\'' +
                ", cover_url='" + cover_url + '\'' +
                '}';
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getSale_price() {
        return sale_price;
    }

    public void setSale_price(String sale_price) {
        this.sale_price = sale_price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSku_name() {
        return sku_name;
    }

    public void setSku_name(String sku_name) {
        this.sku_name = sku_name;
    }

    public String getCover_url() {
        return cover_url;
    }

    public void setCover_url(String cover_url) {
        this.cover_url = cover_url;
    }
}
