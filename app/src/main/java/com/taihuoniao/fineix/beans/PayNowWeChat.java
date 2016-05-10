package com.taihuoniao.fineix.beans;

import java.io.Serializable;

/**
 * Created by android on 2016/3/14.
 */
public class PayNowWeChat implements Serializable {
    private String appid,device_info,mch_id,nonce_str,prepay_id,
            result_code,return_code,return_msg,sign,trade_type,partner_id,key,time_stamp,new_sign;

    @Override
    public String toString() {
        return "PayNowWeChat{" +
                "appid='" + appid + '\'' +
                ", device_info='" + device_info + '\'' +
                ", mch_id='" + mch_id + '\'' +
                ", nonce_str='" + nonce_str + '\'' +
                ", prepay_id='" + prepay_id + '\'' +
                ", result_code='" + result_code + '\'' +
                ", return_code='" + return_code + '\'' +
                ", return_msg='" + return_msg + '\'' +
                ", sign='" + sign + '\'' +
                ", trade_type='" + trade_type + '\'' +
                ", partner_id='" + partner_id + '\'' +
                ", key='" + key + '\'' +
                ", time_stamp='" + time_stamp + '\'' +
                ", new_sign='" + new_sign + '\'' +
                '}';
    }

    public String getNew_sign() {
        return new_sign;
    }

    public void setNew_sign(String new_sign) {
        this.new_sign = new_sign;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getDevice_info() {
        return device_info;
    }

    public void setDevice_info(String device_info) {
        this.device_info = device_info;
    }

    public String getMch_id() {
        return mch_id;
    }

    public void setMch_id(String mch_id) {
        this.mch_id = mch_id;
    }

    public String getNonce_str() {
        return nonce_str;
    }

    public void setNonce_str(String nonce_str) {
        this.nonce_str = nonce_str;
    }

    public String getPrepay_id() {
        return prepay_id;
    }

    public void setPrepay_id(String prepay_id) {
        this.prepay_id = prepay_id;
    }

    public String getResult_code() {
        return result_code;
    }

    public void setResult_code(String result_code) {
        this.result_code = result_code;
    }

    public String getReturn_code() {
        return return_code;
    }

    public void setReturn_code(String return_code) {
        this.return_code = return_code;
    }

    public String getReturn_msg() {
        return return_msg;
    }

    public void setReturn_msg(String return_msg) {
        this.return_msg = return_msg;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getTrade_type() {
        return trade_type;
    }

    public void setTrade_type(String trade_type) {
        this.trade_type = trade_type;
    }

    public String getPartner_id() {
        return partner_id;
    }

    public void setPartner_id(String partner_id) {
        this.partner_id = partner_id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTime_stamp() {
        return time_stamp;
    }

    public void setTime_stamp(String time_stamp) {
        this.time_stamp = time_stamp;
    }
}
