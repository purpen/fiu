package com.taihuoniao.fineix.beans;

import java.io.Serializable;

/**
 * Created by android on 2016/3/7.
 */
public class CartOrderContentItem implements Serializable {
    private String target_id,type,sku,product_id,quantity,
            price,sku_mode,sale_price,title,cover,view_url,subtotal;

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

    public String getSku_mode() {
        return sku_mode;
    }

    public void setSku_mode(String sku_mode) {
        this.sku_mode = sku_mode;
    }

    public String getSale_price() {
        return sale_price;
    }

    public void setSale_price(String sale_price) {
        this.sale_price = sale_price;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getView_url() {
        return view_url;
    }

    public void setView_url(String view_url) {
        this.view_url = view_url;
    }

    public String getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(String subtotal) {
        this.subtotal = subtotal;
    }

    @Override
    public String toString() {
        return "CartOrderContentItem{" +
                "target_id='" + target_id + '\'' +
                ", type='" + type + '\'' +
                ", sku='" + sku + '\'' +
                ", product_id='" + product_id + '\'' +
                ", quantity='" + quantity + '\'' +
                ", price='" + price + '\'' +
                ", sku_mode='" + sku_mode + '\'' +
                ", sale_price='" + sale_price + '\'' +
                ", title='" + title + '\'' +
                ", cover='" + cover + '\'' +
                ", view_url='" + view_url + '\'' +
                ", subtotal='" + subtotal + '\'' +
                '}';
    }
}
