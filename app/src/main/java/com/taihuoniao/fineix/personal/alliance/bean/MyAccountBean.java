package com.taihuoniao.fineix.personal.alliance.bean;

/**
 * Created by Stephen on 2017/1/17 10:25
 * Email: 895745843@qq.com
 */

public class MyAccountBean {

    /**
     * _id : 58624d9c5c42ecae0a0041b2
     * name : null
     * code : tian
     * kind : 2
     * type : 1
     * status : 5
     * contact : {"name":"田帅","phone":"15001120509","email":"tianshuai@taihuoniao.com","position":"PHP","company_name":"太火鸟"}
     * summary : 不想说话
     * total_balance_amount : 0
     * total_cash_amount : 0
     * wait_cash_amount : 0
     * whether_apply_cash : 0
     * whether_balance_stat : 0
     * total_count : 7
     * success_count : 2
     * created_on : 1482837404
     * updated_on : 1483499040
     * current_user_id : 36
     */

    private String _id;
    private Object name;
    private String code;
    private int kind;
    private int type;
    private int status;
    private ContactEntity contact;
    private String summary;
    private int total_balance_amount;
    private int total_cash_amount;
    private int wait_cash_amount;
    private int whether_apply_cash;
    private int whether_balance_stat;
    private int total_count;
    private int success_count;
    private int created_on;
    private int updated_on;
    private int current_user_id;

    public void set_id(String _id) {
        this._id = _id;
    }

    public void setName(Object name) {
        this.name = name;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setKind(int kind) {
        this.kind = kind;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setContact(ContactEntity contact) {
        this.contact = contact;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public void setTotal_balance_amount(int total_balance_amount) {
        this.total_balance_amount = total_balance_amount;
    }

    public void setTotal_cash_amount(int total_cash_amount) {
        this.total_cash_amount = total_cash_amount;
    }

    public void setWait_cash_amount(int wait_cash_amount) {
        this.wait_cash_amount = wait_cash_amount;
    }

    public void setWhether_apply_cash(int whether_apply_cash) {
        this.whether_apply_cash = whether_apply_cash;
    }

    public void setWhether_balance_stat(int whether_balance_stat) {
        this.whether_balance_stat = whether_balance_stat;
    }

    public void setTotal_count(int total_count) {
        this.total_count = total_count;
    }

    public void setSuccess_count(int success_count) {
        this.success_count = success_count;
    }

    public void setCreated_on(int created_on) {
        this.created_on = created_on;
    }

    public void setUpdated_on(int updated_on) {
        this.updated_on = updated_on;
    }

    public void setCurrent_user_id(int current_user_id) {
        this.current_user_id = current_user_id;
    }

    public String get_id() {
        return _id;
    }

    public Object getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public int getKind() {
        return kind;
    }

    public int getType() {
        return type;
    }

    public int getStatus() {
        return status;
    }

    public ContactEntity getContact() {
        return contact;
    }

    public String getSummary() {
        return summary;
    }

    public int getTotal_balance_amount() {
        return total_balance_amount;
    }

    public int getTotal_cash_amount() {
        return total_cash_amount;
    }

    public int getWait_cash_amount() {
        return wait_cash_amount;
    }

    public int getWhether_apply_cash() {
        return whether_apply_cash;
    }

    public int getWhether_balance_stat() {
        return whether_balance_stat;
    }

    public int getTotal_count() {
        return total_count;
    }

    public int getSuccess_count() {
        return success_count;
    }

    public int getCreated_on() {
        return created_on;
    }

    public int getUpdated_on() {
        return updated_on;
    }

    public int getCurrent_user_id() {
        return current_user_id;
    }

    public static class ContactEntity {
        /**
         * name : 田帅
         * phone : 15001120509
         * email : tianshuai@taihuoniao.com
         * position : PHP
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
