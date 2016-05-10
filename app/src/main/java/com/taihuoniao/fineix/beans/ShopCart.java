package com.taihuoniao.fineix.beans;

import java.io.Serializable;
import java.util.List;

/**
 * Created by android on 2016/3/2.
 */
public class ShopCart implements Serializable {
    private String _id;
    private String total_price;
    private List<ShopCartItem> shopCartItemList;

    @Override
    public String toString() {
        return "ShopCart{" +
                "_id='" + _id + '\'' +
                ", total_price='" + total_price + '\'' +
                ", shopCartItemList=" + shopCartItemList +
                '}';
    }

    public List<ShopCartItem> getShopCartItemList() {
        return shopCartItemList;
    }

    public void setShopCartItemList(List<ShopCartItem> shopCartItemList) {
        this.shopCartItemList = shopCartItemList;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getTotal_price() {
        return total_price;
    }

    public void setTotal_price(String total_price) {
        this.total_price = total_price;
    }


}
