package com.taihuoniao.fineix.pay.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author lilin
 *         created at 2016/5/13 15:20
 */
public class WXPayParams implements Parcelable {
    public String appid;
    public String device_info;
    public String mch_id;
    public String nonce_str;
    public String prepay_id;
    public String sign;
    public String trade_type;
    public String partner_id;
    public String key;
    public String time_stamp;
    public String new_sign;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.appid);
        dest.writeString(this.device_info);
        dest.writeString(this.mch_id);
        dest.writeString(this.nonce_str);
        dest.writeString(this.prepay_id);
        dest.writeString(this.sign);
        dest.writeString(this.trade_type);
        dest.writeString(this.partner_id);
        dest.writeString(this.key);
        dest.writeString(this.time_stamp);
        dest.writeString(this.new_sign);
    }

    public WXPayParams() {
    }

    protected WXPayParams(Parcel in) {
        this.appid = in.readString();
        this.device_info = in.readString();
        this.mch_id = in.readString();
        this.nonce_str = in.readString();
        this.prepay_id = in.readString();
        this.sign = in.readString();
        this.trade_type = in.readString();
        this.partner_id = in.readString();
        this.key = in.readString();
        this.time_stamp = in.readString();
        this.new_sign = in.readString();
    }

    public static final Parcelable.Creator<WXPayParams> CREATOR = new Parcelable.Creator<WXPayParams>() {
        @Override
        public WXPayParams createFromParcel(Parcel source) {
            return new WXPayParams(source);
        }

        @Override
        public WXPayParams[] newArray(int size) {
            return new WXPayParams[size];
        }
    };

}
