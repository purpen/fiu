package com.taihuoniao.fineix.beans;

import java.io.Serializable;

/**
 * Created by android on 2016/3/7.
 */
public class CartOrderContent implements Serializable {
    private String payment_method,transfer,transfer_time,
            summary,invoice_type,freight,card_money,
            coin_money,invoice_caty,invoice_content,total_money,items_count;
//    private List<CartOrderContentItem> cartOrderContentItem;


    @Override
    public String toString() {
        return "CartOrderContent{" +
                "payment_method='" + payment_method + '\'' +
                ", transfer='" + transfer + '\'' +
                ", transfer_time='" + transfer_time + '\'' +
                ", summary='" + summary + '\'' +
                ", invoice_type='" + invoice_type + '\'' +
                ", freight='" + freight + '\'' +
                ", card_money='" + card_money + '\'' +
                ", coin_money='" + coin_money + '\'' +
                ", invoice_caty='" + invoice_caty + '\'' +
                ", invoice_content='" + invoice_content + '\'' +
                ", total_money='" + total_money + '\'' +
                ", items_count='" + items_count + '\'' +
                '}';
    }

    public String getPayment_method() {
        return payment_method;
    }

    public void setPayment_method(String payment_method) {
        this.payment_method = payment_method;
    }

    public String getTransfer() {
        return transfer;
    }

    public void setTransfer(String transfer) {
        this.transfer = transfer;
    }

    public String getTransfer_time() {
        return transfer_time;
    }

    public void setTransfer_time(String transfer_time) {
        this.transfer_time = transfer_time;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getInvoice_type() {
        return invoice_type;
    }

    public void setInvoice_type(String invoice_type) {
        this.invoice_type = invoice_type;
    }

    public String getFreight() {
        return freight;
    }

    public void setFreight(String freight) {
        this.freight = freight;
    }

    public String getCard_money() {
        return card_money;
    }

    public void setCard_money(String card_money) {
        this.card_money = card_money;
    }

    public String getCoin_money() {
        return coin_money;
    }

    public void setCoin_money(String coin_money) {
        this.coin_money = coin_money;
    }

    public String getInvoice_caty() {
        return invoice_caty;
    }

    public void setInvoice_caty(String invoice_caty) {
        this.invoice_caty = invoice_caty;
    }

    public String getInvoice_content() {
        return invoice_content;
    }

    public void setInvoice_content(String invoice_content) {
        this.invoice_content = invoice_content;
    }

    public String getTotal_money() {
        return total_money;
    }

    public void setTotal_money(String total_money) {
        this.total_money = total_money;
    }

    public String getItems_count() {
        return items_count;
    }

    public void setItems_count(String items_count) {
        this.items_count = items_count;
    }
}
