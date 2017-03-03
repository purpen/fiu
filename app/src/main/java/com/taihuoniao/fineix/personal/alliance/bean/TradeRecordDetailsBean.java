package com.taihuoniao.fineix.personal.alliance.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Stephen on 2017/1/20 10:24
 * Email: 895745843@qq.com
 */

public class TradeRecordDetailsBean implements Parcelable {


    /**
     * _id : 58aaaf9afc8b1292678b4b49
     * alliance_id : 586f6e1f20de8d8e608bc612
     * target_id : 117022050911
     * order_rid : 117022050911
     * sub_order_id : null
     * product_id : 0
     * sku_id : 0
     * sku_price : 69
     * user_id : 20448
     * quantity : 1
     * commision_percent : 0.05
     * unit_price : 3.45
     * total_price : 3.45
     * code : ZzEJfc
     * summary : null
     * type : 2
     * kind : 1
     * stage : 5
     * status : 1
     * status_label : 已结算
     * balance_on : 1487952301
     * from_site : 1
     * created_on : 1487581082
     * updated_on : 1487952301
     * title : 订单[117022050911]
     * created_at : 2017-02-20 16:58
     * current_user_id : 20448
     */

    public String _id;
    public String alliance_id;
    public String target_id;
    public String order_rid;
    public int product_id;
    public int sku_id;
    public int sku_price;
    public int user_id;
    public int quantity;
    public double commision_percent;
    public double unit_price;
    public double total_price;
    public String code;
    public int type;
    public int kind;
    public int stage;
    public int status;
    public String status_label;
    public int balance_on;
    public int from_site;
    public int created_on;
    public int updated_on;
    public String title;
    public String created_at;
    public int current_user_id;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this._id);
        dest.writeString(this.alliance_id);
        dest.writeString(this.target_id);
        dest.writeString(this.order_rid);
        dest.writeInt(this.product_id);
        dest.writeInt(this.sku_id);
        dest.writeInt(this.sku_price);
        dest.writeInt(this.user_id);
        dest.writeInt(this.quantity);
        dest.writeDouble(this.commision_percent);
        dest.writeDouble(this.unit_price);
        dest.writeDouble(this.total_price);
        dest.writeString(this.code);
        dest.writeInt(this.type);
        dest.writeInt(this.kind);
        dest.writeInt(this.stage);
        dest.writeInt(this.status);
        dest.writeString(this.status_label);
        dest.writeInt(this.balance_on);
        dest.writeInt(this.from_site);
        dest.writeInt(this.created_on);
        dest.writeInt(this.updated_on);
        dest.writeString(this.title);
        dest.writeString(this.created_at);
        dest.writeInt(this.current_user_id);
    }

    public TradeRecordDetailsBean() {
    }

    protected TradeRecordDetailsBean(Parcel in) {
        this._id = in.readString();
        this.alliance_id = in.readString();
        this.target_id = in.readString();
        this.order_rid = in.readString();
        this.product_id = in.readInt();
        this.sku_id = in.readInt();
        this.sku_price = in.readInt();
        this.user_id = in.readInt();
        this.quantity = in.readInt();
        this.commision_percent = in.readDouble();
        this.unit_price = in.readDouble();
        this.total_price = in.readDouble();
        this.code = in.readString();
        this.type = in.readInt();
        this.kind = in.readInt();
        this.stage = in.readInt();
        this.status = in.readInt();
        this.status_label = in.readString();
        this.balance_on = in.readInt();
        this.from_site = in.readInt();
        this.created_on = in.readInt();
        this.updated_on = in.readInt();
        this.title = in.readString();
        this.created_at = in.readString();
        this.current_user_id = in.readInt();
    }

    public static final Parcelable.Creator<TradeRecordDetailsBean> CREATOR = new Parcelable.Creator<TradeRecordDetailsBean>() {
        @Override
        public TradeRecordDetailsBean createFromParcel(Parcel source) {
            return new TradeRecordDetailsBean(source);
        }

        @Override
        public TradeRecordDetailsBean[] newArray(int size) {
            return new TradeRecordDetailsBean[size];
        }
    };
}
