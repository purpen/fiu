package com.taihuoniao.fineix.beans;

import java.io.Serializable;

/**
 * Created by android on 2016/3/6.
 */
public class CartDoOrderBonus implements Serializable {
    private String code,amount,xname,user_id,used_by,get_at,used_at,used,product_id
            ,order_rid,expired_at,min_amount,status,created_on,updated_on,expired_label,__extend__;

    @Override
    public String toString() {
        return "CartDoOrderBonus{" +
                "code='" + code + '\'' +
                ", amount='" + amount + '\'' +
                ", xname='" + xname + '\'' +
                ", user_id='" + user_id + '\'' +
                ", used_by='" + used_by + '\'' +
                ", get_at='" + get_at + '\'' +
                ", used_at='" + used_at + '\'' +
                ", used='" + used + '\'' +
                ", product_id='" + product_id + '\'' +
                ", order_rid='" + order_rid + '\'' +
                ", expired_at='" + expired_at + '\'' +
                ", min_amount='" + min_amount + '\'' +
                ", status='" + status + '\'' +
                ", created_on='" + created_on + '\'' +
                ", updated_on='" + updated_on + '\'' +
                ", expired_label='" + expired_label + '\'' +
                ", __extend__='" + __extend__ + '\'' +
                '}';
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getXname() {
        return xname;
    }

    public void setXname(String xname) {
        this.xname = xname;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUsed_by() {
        return used_by;
    }

    public void setUsed_by(String used_by) {
        this.used_by = used_by;
    }

    public String getGet_at() {
        return get_at;
    }

    public void setGet_at(String get_at) {
        this.get_at = get_at;
    }

    public String getUsed_at() {
        return used_at;
    }

    public void setUsed_at(String used_at) {
        this.used_at = used_at;
    }

    public String getUsed() {
        return used;
    }

    public void setUsed(String used) {
        this.used = used;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getOrder_rid() {
        return order_rid;
    }

    public void setOrder_rid(String order_rid) {
        this.order_rid = order_rid;
    }

    public String getExpired_at() {
        return expired_at;
    }

    public void setExpired_at(String expired_at) {
        this.expired_at = expired_at;
    }

    public String getMin_amount() {
        return min_amount;
    }

    public void setMin_amount(String min_amount) {
        this.min_amount = min_amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getExpired_label() {
        return expired_label;
    }

    public void setExpired_label(String expired_label) {
        this.expired_label = expired_label;
    }

    public String get__extend__() {
        return __extend__;
    }

    public void set__extend__(String __extend__) {
        this.__extend__ = __extend__;
    }
}
