package com.taihuoniao.fineix.user.bean;

import java.util.List;

/**
 * 订单详情
 * Created by Stephen on 2016/11/23.
 */

public class ShoppingDetailBean {

    /**
     * _id : 5832a9973ffca2c6058b45bc
     * rid : 116112106500
     * items : [{"sku":1031789046,"product_id":1011497002,"quantity":1,"price":0.01,"sale_price":0.01,"kind":0,"vop_id":"","name":"华米手环","sku_name":"白色","cover_url":"http://frbird.qiniudn.com/product/160121/56a0934f3ffca26c098ba90e-4-p500x500.jpg","refund_type":0,"refund_status":0}]
     * items_count : 1
     * total_money : 0.01
     * pay_money : 0.01
     * card_money : 0
     * coin_money : 0
     * freight : 0
     * discount : 0
     * user_id : 924912
     * addbook_id : 5821a8a93ffca265558b546b
     * addbook : null
     * express_info : {"name":"胡先生","phone":"15201256092","province":"北京","city":"朝阳区","county":"三环到四环之间","town":"","area":"","address":"朝阳区太阳宫南街7号院4号楼1064","zip":"100020","email":"","province_id":1,"city_id":72,"county_id":2819,"town_id":0}
     * invoice_type : 0
     * invoice_caty : 1
     * invoice_title :
     * invoice_content : d
     * payment_method : a
     * express_caty :
     * express_company : null
     * express_no :
     * sended_date : 0
     * card_code :
     * is_presaled : 0
     * expired_time : 1479888023
     * from_site : 8
     * status : 1
     * gift_code :
     * bird_coin_count : 0
     * bird_coin_money : 0
     * gift_money : 0
     * status_label : 等待付款
     * created_on : 1479715223
     * updated_on : 1479715223
     * exist_sub_order : null
     * sub_orders : null
     * created_at : 2016-11-21 16:00
     * current_user_id : 924912
     */

    private String _id;
    private String rid;
    private int items_count;
    private double total_money;
    private String pay_money;
    private int card_money;
    private int coin_money;
    private int freight;
    private int discount;
    private int user_id;
    private String addbook_id;
    private String addbook;
    private ExpressInfoEntity express_info;
    private int invoice_type;
    private int invoice_caty;
    private String invoice_title;
    private String invoice_content;
    private String payment_method;
    private String express_caty;
    private String express_company;
    private String express_no;
    private int sended_date;
    private String card_code;
    private int is_presaled;
    private int expired_time;
    private int from_site;
    private int status;
    private String gift_code;
    private int bird_coin_count;
    private int bird_coin_money;
    private int gift_money;
    private String status_label;
    private int created_on;
    private int updated_on;
    private String exist_sub_order;
//    private String sub_orders;
    private String created_at;
    private int current_user_id;
    private List<ItemsEntity> items;

    public void set_id(String _id) {
        this._id = _id;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public void setItems_count(int items_count) {
        this.items_count = items_count;
    }

    public void setTotal_money(double total_money) {
        this.total_money = total_money;
    }

    public void setPay_money(String pay_money) {
        this.pay_money = pay_money;
    }

    public void setCard_money(int card_money) {
        this.card_money = card_money;
    }

    public void setCoin_money(int coin_money) {
        this.coin_money = coin_money;
    }

    public void setFreight(int freight) {
        this.freight = freight;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public void setAddbook_id(String addbook_id) {
        this.addbook_id = addbook_id;
    }

    public void setAddbook(String addbook) {
        this.addbook = addbook;
    }

    public void setExpress_info(ExpressInfoEntity express_info) {
        this.express_info = express_info;
    }

    public void setInvoice_type(int invoice_type) {
        this.invoice_type = invoice_type;
    }

    public void setInvoice_caty(int invoice_caty) {
        this.invoice_caty = invoice_caty;
    }

    public void setInvoice_title(String invoice_title) {
        this.invoice_title = invoice_title;
    }

    public void setInvoice_content(String invoice_content) {
        this.invoice_content = invoice_content;
    }

    public void setPayment_method(String payment_method) {
        this.payment_method = payment_method;
    }

    public void setExpress_caty(String express_caty) {
        this.express_caty = express_caty;
    }

    public void setExpress_company(String express_company) {
        this.express_company = express_company;
    }

    public void setExpress_no(String express_no) {
        this.express_no = express_no;
    }

    public void setSended_date(int sended_date) {
        this.sended_date = sended_date;
    }

    public void setCard_code(String card_code) {
        this.card_code = card_code;
    }

    public void setIs_presaled(int is_presaled) {
        this.is_presaled = is_presaled;
    }

    public void setExpired_time(int expired_time) {
        this.expired_time = expired_time;
    }

    public void setFrom_site(int from_site) {
        this.from_site = from_site;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setGift_code(String gift_code) {
        this.gift_code = gift_code;
    }

    public void setBird_coin_count(int bird_coin_count) {
        this.bird_coin_count = bird_coin_count;
    }

    public void setBird_coin_money(int bird_coin_money) {
        this.bird_coin_money = bird_coin_money;
    }

    public void setGift_money(int gift_money) {
        this.gift_money = gift_money;
    }

    public void setStatus_label(String status_label) {
        this.status_label = status_label;
    }

    public void setCreated_on(int created_on) {
        this.created_on = created_on;
    }

    public void setUpdated_on(int updated_on) {
        this.updated_on = updated_on;
    }

    public void setExist_sub_order(String exist_sub_order) {
        this.exist_sub_order = exist_sub_order;
    }


    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public void setCurrent_user_id(int current_user_id) {
        this.current_user_id = current_user_id;
    }

    protected void setItems(List<ItemsEntity> items) {
        this.items = items;
    }

    public String get_id() {
        return _id;
    }

    public String getRid() {
        return rid;
    }

    public int getItems_count() {
        return items_count;
    }

    public double getTotal_money() {
        return total_money;
    }

    public String getPay_money() {
        return pay_money;
    }

    public int getCard_money() {
        return card_money;
    }

    public int getCoin_money() {
        return coin_money;
    }

    public int getFreight() {
        return freight;
    }

    public int getDiscount() {
        return discount;
    }

    public int getUser_id() {
        return user_id;
    }

    public String getAddbook_id() {
        return addbook_id;
    }

    public String getAddbook() {
        return addbook;
    }

    public ExpressInfoEntity getExpress_info() {
        return express_info;
    }

    public int getInvoice_type() {
        return invoice_type;
    }

    public int getInvoice_caty() {
        return invoice_caty;
    }

    public String getInvoice_title() {
        return invoice_title;
    }

    public String getInvoice_content() {
        return invoice_content;
    }

    public String getPayment_method() {
        return payment_method;
    }

    public String getExpress_caty() {
        return express_caty;
    }

    public String getExpress_company() {
        return express_company;
    }

    public String getExpress_no() {
        return express_no;
    }

    public int getSended_date() {
        return sended_date;
    }

    public String getCard_code() {
        return card_code;
    }

    public int getIs_presaled() {
        return is_presaled;
    }

    public int getExpired_time() {
        return expired_time;
    }

    public int getFrom_site() {
        return from_site;
    }

    public int getStatus() {
        return status;
    }

    public String getGift_code() {
        return gift_code;
    }

    public int getBird_coin_count() {
        return bird_coin_count;
    }

    public int getBird_coin_money() {
        return bird_coin_money;
    }

    public int getGift_money() {
        return gift_money;
    }

    public String getStatus_label() {
        return status_label;
    }

    public int getCreated_on() {
        return created_on;
    }

    public int getUpdated_on() {
        return updated_on;
    }

    public String getExist_sub_order() {
        return exist_sub_order;
    }

    public String getCreated_at() {
        return created_at;
    }

    public int getCurrent_user_id() {
        return current_user_id;
    }

    public List<ItemsEntity> getItems() {
        return items;
    }

    public static class ExpressInfoEntity {
        /**
         * name : 胡先生
         * phone : 15201256092
         * province : 北京
         * city : 朝阳区
         * county : 三环到四环之间
         * town :
         * area :
         * address : 朝阳区太阳宫南街7号院4号楼1064
         * zip : 100020
         * email :
         * province_id : 1
         * city_id : 72
         * county_id : 2819
         * town_id : 0
         */

        private String name;
        private String phone;
        private String province;
        private String city;
        private String county;
        private String town;
        private String area;
        private String address;
        private String zip;
        private String email;
        private int province_id;
        private int city_id;
        private int county_id;
        private int town_id;

        public void setName(String name) {
            this.name = name;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public void setCounty(String county) {
            this.county = county;
        }

        public void setTown(String town) {
            this.town = town;
        }

        public void setArea(String area) {
            this.area = area;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public void setZip(String zip) {
            this.zip = zip;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public void setProvince_id(int province_id) {
            this.province_id = province_id;
        }

        public void setCity_id(int city_id) {
            this.city_id = city_id;
        }

        public void setCounty_id(int county_id) {
            this.county_id = county_id;
        }

        public void setTown_id(int town_id) {
            this.town_id = town_id;
        }

        public String getName() {
            return name;
        }

        public String getPhone() {
            return phone;
        }

        public String getProvince() {
            return province;
        }

        public String getCity() {
            return city;
        }

        public String getCounty() {
            return county;
        }

        public String getTown() {
            return town;
        }

        public String getArea() {
            return area;
        }

        public String getAddress() {
            return address;
        }

        public String getZip() {
            return zip;
        }

        public String getEmail() {
            return email;
        }

        public int getProvince_id() {
            return province_id;
        }

        public int getCity_id() {
            return city_id;
        }

        public int getCounty_id() {
            return county_id;
        }

        public int getTown_id() {
            return town_id;
        }
    }

    public static class ItemsEntity {
        /**
         * sku : 1031789046
         * product_id : 1011497002
         * quantity : 1
         * price : 0.01
         * sale_price : 0.01
         * kind : 0
         * vop_id :
         * name : 华米手环
         * sku_name : 白色
         * cover_url : http://frbird.qiniudn.com/product/160121/56a0934f3ffca26c098ba90e-4-p500x500.jpg
         * refund_type : 0
         * refund_status : 0
         */

        private int sku;
        private int product_id;
        private int quantity;
        private double price;
        private double sale_price;
        private int kind;
        private String vop_id;
        private String name;
        private String sku_name;
        private String cover_url;
        private int refund_type;
        private int refund_status;

        public void setSku(int sku) {
            this.sku = sku;
        }

        public void setProduct_id(int product_id) {
            this.product_id = product_id;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public void setSale_price(double sale_price) {
            this.sale_price = sale_price;
        }

        public void setKind(int kind) {
            this.kind = kind;
        }

        public void setVop_id(String vop_id) {
            this.vop_id = vop_id;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setSku_name(String sku_name) {
            this.sku_name = sku_name;
        }

        public void setCover_url(String cover_url) {
            this.cover_url = cover_url;
        }

        public void setRefund_type(int refund_type) {
            this.refund_type = refund_type;
        }

        public void setRefund_status(int refund_status) {
            this.refund_status = refund_status;
        }

        public int getSku() {
            return sku;
        }

        public int getProduct_id() {
            return product_id;
        }

        public int getQuantity() {
            return quantity;
        }

        public double getPrice() {
            return price;
        }

        public double getSale_price() {
            return sale_price;
        }

        public int getKind() {
            return kind;
        }

        public String getVop_id() {
            return vop_id;
        }

        public String getName() {
            return name;
        }

        public String getSku_name() {
            return sku_name;
        }

        public String getCover_url() {
            return cover_url;
        }

        public int getRefund_type() {
            return refund_type;
        }

        public int getRefund_status() {
            return refund_status;
        }
    }
}
