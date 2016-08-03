package com.taihuoniao.fineix.pay.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author lilin
 *         created at 2016/8/1 17:10
 */
public class Params implements Parcelable {
    public String version;
    public String merchant;
    public String device;
    public String tradeNum;
    public String tradeName;
    public String tradeDesc;
    public String tradeTime;
    public String amount;
    public String currency;
    public String note;
    public String callbackUrl;
    public String notifyUrl;
    public String ip;
    public String specCardNo;
    public String specId;
    public String specName;
    public String userType;
    public String userId;
    public String expireTime;
    public String orderType;
    public String industryCategoryCode;
    public String sign;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.version);
        dest.writeString(this.merchant);
        dest.writeString(this.device);
        dest.writeString(this.tradeNum);
        dest.writeString(this.tradeName);
        dest.writeString(this.tradeDesc);
        dest.writeString(this.tradeTime);
        dest.writeString(this.amount);
        dest.writeString(this.currency);
        dest.writeString(this.note);
        dest.writeString(this.callbackUrl);
        dest.writeString(this.notifyUrl);
        dest.writeString(this.ip);
        dest.writeString(this.specCardNo);
        dest.writeString(this.specId);
        dest.writeString(this.specName);
        dest.writeString(this.userType);
        dest.writeString(this.userId);
        dest.writeString(this.expireTime);
        dest.writeString(this.orderType);
        dest.writeString(this.industryCategoryCode);
        dest.writeString(this.sign);
    }

    public Params() {
    }

    protected Params(Parcel in) {
        this.version = in.readString();
        this.merchant = in.readString();
        this.device = in.readString();
        this.tradeNum = in.readString();
        this.tradeName = in.readString();
        this.tradeDesc = in.readString();
        this.tradeTime = in.readString();
        this.amount = in.readString();
        this.currency = in.readString();
        this.note = in.readString();
        this.callbackUrl = in.readString();
        this.notifyUrl = in.readString();
        this.ip = in.readString();
        this.specCardNo = in.readString();
        this.specId = in.readString();
        this.specName = in.readString();
        this.userType = in.readString();
        this.userId = in.readString();
        this.expireTime = in.readString();
        this.orderType = in.readString();
        this.industryCategoryCode = in.readString();
        this.sign = in.readString();
    }

    public static final Parcelable.Creator<Params> CREATOR = new Parcelable.Creator<Params>() {
        @Override
        public Params createFromParcel(Parcel source) {
            return new Params(source);
        }

        @Override
        public Params[] newArray(int size) {
            return new Params[size];
        }
    };
}
