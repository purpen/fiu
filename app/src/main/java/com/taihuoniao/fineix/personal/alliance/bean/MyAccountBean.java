package com.taihuoniao.fineix.personal.alliance.bean;

/**
 * Created by Stephen on 2017/1/17 10:25
 * Email: 895745843@qq.com
 */

public class MyAccountBean {

    /**
     * _id : 587dd1343ffca27e2a8baaec
     * name : 田帅
     * code : x518fe
     * kind : 0
     * type : 2
     * status : 5
     * contact : {"name":"田帅 ","phone":"15001120509","email":"tianshuai@taihuoniao.com","position":"设计","company_name":"太火鸟"}
     * summary : 尽供测试！！
     * total_balance_amount : 591.5
     * total_cash_amount : 0
     * wait_cash_amount : 591.5
     * whether_apply_cash : 0
     * whether_balance_stat : 0
     * total_count : 11
     * success_count : 7
     * created_on : 1484640564
     * updated_on : 1484646601
     * current_user_id : 20448
     */

    private String _id;
    private String name;
    private String code;
    private String kind;
    private String type;
    private String status;
    private ContactEntity contact;
    private String summary;
    private String total_balance_amount;
    private String total_cash_amount;
    private String wait_cash_amount;
    private String whether_apply_cash;
    private String whether_balance_stat;
    private String total_count;
    private String success_count;
    private String created_on;
    private String updated_on;
    private String current_user_id;

    public void set_id(String _id) {
        this._id = _id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setContact(ContactEntity contact) {
        this.contact = contact;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public void setTotal_balance_amount(String total_balance_amount) {
        this.total_balance_amount = total_balance_amount;
    }

    public void setTotal_cash_amount(String total_cash_amount) {
        this.total_cash_amount = total_cash_amount;
    }

    public void setWait_cash_amount(String wait_cash_amount) {
        this.wait_cash_amount = wait_cash_amount;
    }

    public void setWhether_apply_cash(String whether_apply_cash) {
        this.whether_apply_cash = whether_apply_cash;
    }

    public void setWhether_balance_stat(String whether_balance_stat) {
        this.whether_balance_stat = whether_balance_stat;
    }

    public void setTotal_count(String total_count) {
        this.total_count = total_count;
    }

    public void setSuccess_count(String success_count) {
        this.success_count = success_count;
    }

    public void setCreated_on(String created_on) {
        this.created_on = created_on;
    }

    public void setUpdated_on(String updated_on) {
        this.updated_on = updated_on;
    }

    public void setCurrent_user_id(String current_user_id) {
        this.current_user_id = current_user_id;
    }

    public String get_id() {
        return _id;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public String getKind() {
        return kind;
    }

    public String getType() {
        return type;
    }

    public String getStatus() {
        return status;
    }

    public ContactEntity getContact() {
        return contact;
    }

    public String getSummary() {
        return summary;
    }

    public String getTotal_balance_amount() {
        return total_balance_amount;
    }

    public String getTotal_cash_amount() {
        return total_cash_amount;
    }

    public String getWait_cash_amount() {
        return wait_cash_amount;
    }

    public String getWhether_apply_cash() {
        return whether_apply_cash;
    }

    public String getWhether_balance_stat() {
        return whether_balance_stat;
    }

    public String getTotal_count() {
        return total_count;
    }

    public String getSuccess_count() {
        return success_count;
    }

    public String getCreated_on() {
        return created_on;
    }

    public String getUpdated_on() {
        return updated_on;
    }

    public String getCurrent_user_id() {
        return current_user_id;
    }

    public static class ContactEntity {
        /**
         * name : 田帅 
         * phone : 15001120509
         * email : tianshuai@taihuoniao.com
         * position : 设计
         * company_name : 太火鸟
         */

        private String name;
        private String phone;
        private String email;
        private String position;
        private String company_name;

        public void setName(String name) {
            this.name = name;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public void setPosition(String position) {
            this.position = position;
        }

        public void setCompany_name(String company_name) {
            this.company_name = company_name;
        }

        public String getName() {
            return name;
        }

        public String getPhone() {
            return phone;
        }

        public String getEmail() {
            return email;
        }

        public String getPosition() {
            return position;
        }

        public String getCompany_name() {
            return company_name;
        }
    }
}
