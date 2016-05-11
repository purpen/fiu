package com.taihuoniao.fineix.beans;

import java.io.Serializable;
import java.util.List;

/**
 * Created by android on 2016/3/13.
 */
public class OrderDetails implements Serializable {
    private String rid,created_at,status,payment_method,total_money,pay_money,freight,items_count,express_company,express_no;
    private List<OrderDetailsAddress> addresses;
    private List<OrderDetailsProducts> products;


    public String getExpress_company() {
        return express_company;
    }

    public void setExpress_company(String express_company) {
        this.express_company = express_company;
    }

    public String getExpress_no() {
        return express_no;
    }

    public void setExpress_no(String express_no) {
        this.express_no = express_no;
    }

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPayment_method() {
        return payment_method;
    }

    public void setPayment_method(String payment_method) {
        this.payment_method = payment_method;
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

    public String getItems_count() {
        return items_count;
    }

    public void setItems_count(String items_count) {
        this.items_count = items_count;
    }

    public List<OrderDetailsAddress> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<OrderDetailsAddress> addresses) {
        this.addresses = addresses;
    }

    public List<OrderDetailsProducts> getProducts() {
        return products;
    }

    public void setProducts(List<OrderDetailsProducts> products) {
        this.products = products;
    }
}
