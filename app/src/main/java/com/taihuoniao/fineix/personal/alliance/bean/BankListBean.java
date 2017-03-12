package com.taihuoniao.fineix.personal.alliance.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Stephen on 2017/3/10 15:54
 * Email: 895745843@qq.com
 */

public class BankListBean implements Parcelable {

    /**
     * banks : [{"id":1,"name":"中国建设银行","mark":"js","logo":""},{"id":2,"name":"中国银行","mark":"zh","logo":""},{"id":3,"name":"中国农业银行","mark":"ny","logo":""},{"id":4,"name":"中国工商银行","mark":"gs","logo":""},{"id":5,"name":"中国建设银行","mark":"js","logo":""},{"id":6,"name":"民生银行","mark":"ms","logo":""},{"id":7,"name":"招商银行","mark":"zs","logo":""},{"id":8,"name":"兴业银行","mark":"xy","logo":""},{"id":9,"name":"国家开发银行","mark":"gjkf","logo":""},{"id":10,"name":"汇丰银行","mark":"hf","logo":""},{"id":11,"name":"中国人民银行","mark":"rm","logo":""},{"id":12,"name":"中国光大银行","mark":"gd","logo":""},{"id":13,"name":"中信银行","mark":"zx","logo":""},{"id":14,"name":"交通银行","mark":"jt","logo":""},{"id":15,"name":"华夏银行","mark":"hx","logo":""},{"id":16,"name":"深圳发展银行","mark":"szfz","logo":""},{"id":17,"name":"浦发银行","mark":"pf","logo":""},{"id":18,"name":"中国邮政储蓄银行","mark":"yzcx","logo":""}]
     * current_user_id : 0
     */

    private String current_user_id;
    private List<BanksEntity> banks;

    public String getCurrent_user_id() {
        return current_user_id;
    }

    public void setCurrent_user_id(String current_user_id) {
        this.current_user_id = current_user_id;
    }

    public List<BanksEntity> getBanks() {
        return banks;
    }

    public void setBanks(List<BanksEntity> banks) {
        this.banks = banks;
    }

    public static class BanksEntity implements Parcelable {
        /**
         * id : 1
         * name : 中国建设银行
         * mark : js
         * logo : 
         */

        private String id;
        private String name;
        private String mark;
        private String logo;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getMark() {
            return mark;
        }

        public void setMark(String mark) {
            this.mark = mark;
        }

        public String getLogo() {
            return logo;
        }

        public void setLogo(String logo) {
            this.logo = logo;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.id);
            dest.writeString(this.name);
            dest.writeString(this.mark);
            dest.writeString(this.logo);
        }

        public BanksEntity() {
        }

        protected BanksEntity(Parcel in) {
            this.id = in.readString();
            this.name = in.readString();
            this.mark = in.readString();
            this.logo = in.readString();
        }

        public static final Parcelable.Creator<BanksEntity> CREATOR = new Parcelable.Creator<BanksEntity>() {
            @Override
            public BanksEntity createFromParcel(Parcel source) {
                return new BanksEntity(source);
            }

            @Override
            public BanksEntity[] newArray(int size) {
                return new BanksEntity[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.current_user_id);
        dest.writeTypedList(banks);
    }

    public BankListBean() {
    }

    protected BankListBean(Parcel in) {
        this.current_user_id = in.readString();
        this.banks = in.createTypedArrayList(BanksEntity.CREATOR);
    }

    public static final Parcelable.Creator<BankListBean> CREATOR = new Parcelable.Creator<BankListBean>() {
        @Override
        public BankListBean createFromParcel(Parcel source) {
            return new BankListBean(source);
        }

        @Override
        public BankListBean[] newArray(int size) {
            return new BankListBean[size];
        }
    };
}
