package com.taihuoniao.fineix.beans;

import java.io.Serializable;
import java.util.List;

/**
 * Created by android on 2016/2/22.
 */
public class OrderEntity implements Serializable {
    private String items_count;
    private String rid;
    private String total_money;
    private String pay_money;
    private String freight;
    private String created_at;
    private String status_label;
    private String status;

    public String getDiscount_money() {
        return discount_money;
    }

    public void setDiscount_money(String discount_money) {
        this.discount_money = discount_money;
    }

    private String discount_money;
    private List<OrderItem> orderItem;

    @Override
    public String toString() {
        return "OrderEntity{" +
                "items_count='" + items_count + '\'' +
                ", rid='" + rid + '\'' +
                ", total_money='" + total_money + '\'' +
                ", pay_money='" + pay_money + '\'' +
                ", freight='" + freight + '\'' +
                ", created_at='" + created_at + '\'' +
                ", status_label='" + status_label + '\'' +
                ", status='" + status + '\'' +
                ", orderItem=" + orderItem +
                '}';
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getItems_count() {
        return items_count;
    }

    public void setItems_count(String items_count) {
        this.items_count = items_count;
    }

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public String getTotal_money() {
        return total_money;
    }

    public void setTotal_money(String total_money) {
        this.total_money = total_money;
    }

    public String getPay_money() {
        return pay_money;
    }

    public void setPay_money(String pay_money) {
        this.pay_money = pay_money;
    }

    public String getFreight() {
        return freight;
    }

    public void setFreight(String freight) {
        this.freight = freight;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getStatus_label() {
        return status_label;
    }

    public void setStatus_label(String status_label) {
        this.status_label = status_label;
    }

    public List<OrderItem> getOrderItem() {
        return orderItem;
    }

    public void setOrderItem(List<OrderItem> orderItem) {
        this.orderItem = orderItem;
    }
}
