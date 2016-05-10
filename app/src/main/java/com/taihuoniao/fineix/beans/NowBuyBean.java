package com.taihuoniao.fineix.beans;

import java.io.Serializable;
import java.util.List;

/**
 * Created by taihuoniao on 2016/2/26.
 * 立即购买网络请求返回值的javabean
 */
public class NowBuyBean implements Serializable {
    private String _id;
    private String is_nowbuy;
    private String pay_money;
    private String summary;
    private String payment_method;
    private String transfer_time;
    private List<NowBuyItemBean> itemList;
    private Boolean success;
    private String message;
    private String rid;
    private int bonus_number;

    public int getBonus_number() {
        return bonus_number;
    }

    public void setBonus_number(int bonus_number) {
        this.bonus_number = bonus_number;
    }

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getIs_nowbuy() {
        return is_nowbuy;
    }

    public void setIs_nowbuy(String is_nowbuy) {
        this.is_nowbuy = is_nowbuy;
    }

    public List<NowBuyItemBean> getItemList() {
        return itemList;
    }

    public void setItemList(List<NowBuyItemBean> itemList) {
        this.itemList = itemList;
    }

    public String getPay_money() {
        return pay_money;
    }

    public void setPay_money(String pay_money) {
        this.pay_money = pay_money;
    }

    public String getPayment_method() {
        return payment_method;
    }

    public void setPayment_method(String payment_method) {
        this.payment_method = payment_method;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getTransfer_time() {
        return transfer_time;
    }

    public void setTransfer_time(String transfer_time) {
        this.transfer_time = transfer_time;
    }

    @Override
    public String toString() {
        return "NowBuyBean{" +
                "_id='" + _id + '\'' +
                ", is_nowbuy='" + is_nowbuy + '\'' +
                ", pay_money='" + pay_money + '\'' +
                ", summary='" + summary + '\'' +
                ", payment_method='" + payment_method + '\'' +
                ", transfer_time='" + transfer_time + '\'' +
                ", itemList=" + itemList +
                '}';
    }
}
