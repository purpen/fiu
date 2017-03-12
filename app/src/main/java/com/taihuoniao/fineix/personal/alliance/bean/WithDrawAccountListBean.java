package com.taihuoniao.fineix.personal.alliance.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Stephen on 2017/3/9 14:36
 * Email: 895745843@qq.com
 */

public class WithDrawAccountListBean implements Parcelable {

    /**
     * total_rows : 3
     * rows : [{"_id":"58bd48dd5c42ec090500424d","user_id":36,"alliance":0,"phone":"15001120508","type":1,"kind":2,"kind_label":"支付宝","pay_type":3,"pay_type_label":"","account":"4343434343434","username":"小力","is_default":0,"bank_address":"","status":1,"created_on":1488799965,"updated_on":1488799965,"created_at":"2017-03-06 19:32"},{"_id":"58bd441c5c42ec0a05004223","user_id":36,"alliance":0,"phone":"15001120509","type":1,"kind":2,"kind_label":"支付宝","pay_type":2,"pay_type_label":"","account":"420098473847384","username":"李明","is_default":0,"bank_address":"","status":1,"created_on":1488798748,"updated_on":1488798748,"created_at":"2017-03-06 19:12"},{"_id":"58bd438c5c42ec090500424c","user_id":36,"alliance":0,"phone":"15001120509","type":1,"kind":1,"kind_label":"银行卡","pay_type":1,"pay_type_label":"中国建设银行","account":"tianshuai","username":"男帅","is_default":0,"bank_address":"","status":1,"created_on":1488798604,"updated_on":1488798604,"created_at":"2017-03-06 19:10"}]
     * total_page : 1
     * current_page : 1
     * pager : 
     * next_page : 0
     * prev_page : 0
     * current_user_id : 36
     */

    private String total_rows;
    private String total_page;
    private String current_page;
    private String pager;
    private String next_page;
    private String prev_page;
    private String current_user_id;
    private List<RowsEntity> rows;

    public String getTotal_rows() {
        return total_rows;
    }

    public void setTotal_rows(String total_rows) {
        this.total_rows = total_rows;
    }

    public String getTotal_page() {
        return total_page;
    }

    public void setTotal_page(String total_page) {
        this.total_page = total_page;
    }

    public String getCurrent_page() {
        return current_page;
    }

    public void setCurrent_page(String current_page) {
        this.current_page = current_page;
    }

    public String getPager() {
        return pager;
    }

    public void setPager(String pager) {
        this.pager = pager;
    }

    public String getNext_page() {
        return next_page;
    }

    public void setNext_page(String next_page) {
        this.next_page = next_page;
    }

    public String getPrev_page() {
        return prev_page;
    }

    public void setPrev_page(String prev_page) {
        this.prev_page = prev_page;
    }

    public String getCurrent_user_id() {
        return current_user_id;
    }

    public void setCurrent_user_id(String current_user_id) {
        this.current_user_id = current_user_id;
    }

    public List<RowsEntity> getRows() {
        return rows;
    }

    public void setRows(List<RowsEntity> rows) {
        this.rows = rows;
    }

    public static class RowsEntity implements Parcelable {
        /**
         * _id : 58bd48dd5c42ec090500424d
         * user_id : 36
         * alliance : 0
         * phone : 15001120508
         * type : 1
         * kind : 2
         * kind_label : 支付宝
         * pay_type : 3
         * pay_type_label : 
         * account : 4343434343434
         * username : 小力
         * is_default : 0
         * bank_address : 
         * status : 1
         * created_on : 1488799965
         * updated_on : 1488799965
         * created_at : 2017-03-06 19:32
         */

        private String _id;
        private String user_id;
        private String alliance;
        private String phone;
        private String type;
        private String kind;
        private String kind_label;
        private String pay_type;
        private String pay_type_label;
        private String account;
        private String username;
        private String is_default;
        private String bank_address;
        private String status;
        private String created_on;
        private String updated_on;
        private String created_at;

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getAlliance() {
            return alliance;
        }

        public void setAlliance(String alliance) {
            this.alliance = alliance;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getKind() {
            return kind;
        }

        public void setKind(String kind) {
            this.kind = kind;
        }

        public String getKind_label() {
            return kind_label;
        }

        public void setKind_label(String kind_label) {
            this.kind_label = kind_label;
        }

        public String getPay_type() {
            return pay_type;
        }

        public void setPay_type(String pay_type) {
            this.pay_type = pay_type;
        }

        public String getPay_type_label() {
            return pay_type_label;
        }

        public void setPay_type_label(String pay_type_label) {
            this.pay_type_label = pay_type_label;
        }

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getIs_default() {
            return is_default;
        }

        public void setIs_default(String is_default) {
            this.is_default = is_default;
        }

        public String getBank_address() {
            return bank_address;
        }

        public void setBank_address(String bank_address) {
            this.bank_address = bank_address;
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

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this._id);
            dest.writeString(this.user_id);
            dest.writeString(this.alliance);
            dest.writeString(this.phone);
            dest.writeString(this.type);
            dest.writeString(this.kind);
            dest.writeString(this.kind_label);
            dest.writeString(this.pay_type);
            dest.writeString(this.pay_type_label);
            dest.writeString(this.account);
            dest.writeString(this.username);
            dest.writeString(this.is_default);
            dest.writeString(this.bank_address);
            dest.writeString(this.status);
            dest.writeString(this.created_on);
            dest.writeString(this.updated_on);
            dest.writeString(this.created_at);
        }

        public RowsEntity() {
        }

        protected RowsEntity(Parcel in) {
            this._id = in.readString();
            this.user_id = in.readString();
            this.alliance = in.readString();
            this.phone = in.readString();
            this.type = in.readString();
            this.kind = in.readString();
            this.kind_label = in.readString();
            this.pay_type = in.readString();
            this.pay_type_label = in.readString();
            this.account = in.readString();
            this.username = in.readString();
            this.is_default = in.readString();
            this.bank_address = in.readString();
            this.status = in.readString();
            this.created_on = in.readString();
            this.updated_on = in.readString();
            this.created_at = in.readString();
        }

        public static final Creator<RowsEntity> CREATOR = new Creator<RowsEntity>() {
            @Override
            public RowsEntity createFromParcel(Parcel source) {
                return new RowsEntity(source);
            }

            @Override
            public RowsEntity[] newArray(int size) {
                return new RowsEntity[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.total_rows);
        dest.writeString(this.total_page);
        dest.writeString(this.current_page);
        dest.writeString(this.pager);
        dest.writeString(this.next_page);
        dest.writeString(this.prev_page);
        dest.writeString(this.current_user_id);
        dest.writeList(this.rows);
    }

    public WithDrawAccountListBean() {
    }

    protected WithDrawAccountListBean(Parcel in) {
        this.total_rows = in.readString();
        this.total_page = in.readString();
        this.current_page = in.readString();
        this.pager = in.readString();
        this.next_page = in.readString();
        this.prev_page = in.readString();
        this.current_user_id = in.readString();
        this.rows = new ArrayList<RowsEntity>();
        in.readList(this.rows, RowsEntity.class.getClassLoader());
    }

    public static final Parcelable.Creator<WithDrawAccountListBean> CREATOR = new Parcelable.Creator<WithDrawAccountListBean>() {
        @Override
        public WithDrawAccountListBean createFromParcel(Parcel source) {
            return new WithDrawAccountListBean(source);
        }

        @Override
        public WithDrawAccountListBean[] newArray(int size) {
            return new WithDrawAccountListBean[size];
        }
    };
}
