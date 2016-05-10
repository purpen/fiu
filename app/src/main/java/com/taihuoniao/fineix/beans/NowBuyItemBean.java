package com.taihuoniao.fineix.beans;

import java.io.Serializable;

/**
 * Created by taihuoniao on 2016/2/29.
 */
public class NowBuyItemBean implements Serializable{
    private String cover;
    private String title;
    private String sku_mode;
    private String sku_name;
    private String quantity;
    private String subtotal;
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getSku_mode() {
        return sku_mode;
    }

    public void setSku_mode(String sku_mode) {
        this.sku_mode = sku_mode;
    }

    public String getSku_name() {
        return sku_name;
    }

    public void setSku_name(String sku_name) {
        this.sku_name = sku_name;
    }

    public String getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(String subtotal) {
        this.subtotal = subtotal;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
