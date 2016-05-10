package com.taihuoniao.fineix.beans;

import java.io.Serializable;
import java.util.List;

/**
 * Created by android on 2016/3/6.
 */
public class CartDoOrder implements Serializable {
    private String pay_money, is_nowbuy, rid, user_id, expired, is_cart, created_on, updated_on, _id, success, message, kind;
    private List<CartDoOrderBonus> bonus;
    private List<CartOrderContent> cartOrderContents;
    private List<CartOrderContentItem> cartOrderContentItems;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getPay_money() {
        return pay_money;
    }

    public void setPay_money(String pay_money) {
        this.pay_money = pay_money;
    }

    public String getIs_nowbuy() {
        return is_nowbuy;
    }

    public void setIs_nowbuy(String is_nowbuy) {
        this.is_nowbuy = is_nowbuy;
    }

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getExpired() {
        return expired;
    }

    public void setExpired(String expired) {
        this.expired = expired;
    }

    public String getIs_cart() {
        return is_cart;
    }

    public void setIs_cart(String is_cart) {
        this.is_cart = is_cart;
    }

    public String getCreated_on() {
        return created_on;
    }

    public void setCreated_on(String created_on) {
        this.created_on = created_on;
    }

    public String getUpdated_on() {
        return updated_on;
    }

    public void setUpdated_on(String updated_on) {
        this.updated_on = updated_on;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public List<CartDoOrderBonus> getBonus() {
        return bonus;
    }

    public void setBonus(List<CartDoOrderBonus> bonus) {
        this.bonus = bonus;
    }

    public List<CartOrderContent> getCartOrderContents() {
        return cartOrderContents;
    }

    public void setCartOrderContents(List<CartOrderContent> cartOrderContents) {
        this.cartOrderContents = cartOrderContents;
    }

    public List<CartOrderContentItem> getCartOrderContentItems() {
        return cartOrderContentItems;
    }

    public void setCartOrderContentItems(List<CartOrderContentItem> cartOrderContentItems) {
        this.cartOrderContentItems = cartOrderContentItems;
    }
}
