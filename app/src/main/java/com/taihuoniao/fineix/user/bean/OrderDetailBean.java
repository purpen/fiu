package com.taihuoniao.fineix.user.bean;


import java.util.List;

/**
 * 订单详情2 （有子订单）
 * Created by Stephen on 2016/11/23.
 */

public class OrderDetailBean {
    /**
     * _id : 5837fceb3ffca2d87f8b457c
     * rid : 116112506533
     * items : [{"sku":1031789053,"product_id":1011497002,"quantity":2,"price":0.02,"sale_price":0.02,"kind":0,"vop_id":"","refund_type":2,"refund_status":1,"refund_label":"退款中","refund_button":0,"name":"华米手环","sku_name":"黑色","cover_url":"http://frbird.qiniudn.com/product/160121/56a0934f3ffca26c098ba90e-4-p500x500.jpg"},{"sku":1031789101,"product_id":1011497002,"quantity":2,"price":0.03,"sale_price":0.03,"kind":0,"vop_id":"","refund_type":2,"refund_status":1,"refund_label":"退款中","refund_button":0,"name":"华米手环","sku_name":"绿色","cover_url":"http://frbird.qiniudn.com/product/160121/56a0934f3ffca26c098ba90e-4-p500x500.jpg"},{"sku":1011513749,"product_id":1011513749,"quantity":1,"price":0.15,"sale_price":0.15,"kind":0,"vop_id":"","refund_type":0,"refund_status":0,"refund_label":"","refund_button":0,"name":"a1螺丝刀a1螺丝刀a1螺丝刀","sku_name":null,"cover_url":"http://frbird.qiniudn.com/product/160127/56a858723ffca269098bb039-4-p500x500.jpg"}]
     * items_count : 3
     * total_money : 0.25
     * pay_money : 0.25
     * card_money : 0
     * coin_money : 0
     * freight : 0
     * discount : 0
     * user_id : 20448
     * addbook_id : 58199df03ffca24f558b4864
     * addbook : null
     * express_info : {"name":"田帅","phone":"15001120509","province":"北京","city":"朝阳区","county":"四环到五环之间","town":"","area":"","address":"酒仙桥北路798 751 太火鸟","zip":"","email":"","province_id":1,"city_id":72,"county_id":2839,"town_id":0}
     * invoice_type : 0
     * invoice_caty : 1
     * invoice_title :
     * invoice_content : d
     * trade_site_name : 支付宝
     * payment_method : a
     * express_caty : z
     * express_company : 中通快递
     * express_no : wewe
     * sended_date : 1480064350
     * card_code :
     * is_presaled : 0
     * expired_time : 1480237035
     * from_site : 1
     * status : 20
     * gift_code :
     * bird_coin_count : 0
     * bird_coin_money : 0
     * gift_money : 0
     * status_label : 已完成
     * created_on : 1480064235
     * updated_on : 1480065259
     * exist_sub_order : 1
     * sub_orders : [{"id":"116112506533-1","items":[{"sku":1031789053,"product_id":1011497002,"quantity":2,"price":0.02,"sale_price":0.02,"kind":0,"vop_id":"","refund_type":2,"refund_status":1,"refund_label":"退款中","refund_button":0,"name":"华米手环","sku_name":"黑色","cover_url":"http://frbird.qiniudn.com/product/160121/56a0934f3ffca26c098ba90e-4-p500x500.jpg"},{"sku":1011513749,"product_id":1011513749,"quantity":1,"price":0.15,"sale_price":0.15,"kind":0,"vop_id":"","refund_type":0,"refund_status":0,"refund_label":"","refund_button":0,"name":"a1螺丝刀a1螺丝刀a1螺丝刀","sku_name":null,"cover_url":"http://frbird.qiniudn.com/product/160127/56a858723ffca269098bb039-4-p500x500.jpg"}],"items_count":2,"split_on":1480064335,"is_sended":1,"sended_on":1480064344,"express_caty":"z","express_no":"wewe","supplier_id":"","split_at":"2016-11-25 16:58","sended_at":"2016-11-25 16:59","express_company":"中通快递"},{"id":"116112506533-2","items":[{"sku":1031789101,"product_id":1011497002,"quantity":2,"price":0.03,"sale_price":0.03,"kind":0,"vop_id":"","refund_type":2,"refund_status":1,"refund_label":"退款中","refund_button":0,"name":"华米手环","sku_name":"绿色","cover_url":"http://frbird.qiniudn.com/product/160121/56a0934f3ffca26c098ba90e-4-p500x500.jpg"}],"items_count":1,"split_on":1480064335,"is_sended":1,"sended_on":1480064350,"express_caty":"z","express_no":"wewe","supplier_id":"","split_at":"2016-11-25 16:58","sended_at":"2016-11-25 16:59","express_company":"中通快递"}]
     * created_at : 2016-11-25 16:57
     * current_user_id : 20448
     */

    private String _id;
    private String rid;
    private int items_count;
    private double total_money;
    private double pay_money;
    private int card_money;
    private int coin_money;
    private int freight;
    private int discount;
    private int user_id;
    private String addbook_id;
    private String addbook;
    private String discount_money;
    /**
     * name : 田帅
     * phone : 15001120509
     * province : 北京
     * city : 朝阳区
     * county : 四环到五环之间
     * town :
     * area :
     * address : 酒仙桥北路798 751 太火鸟
     * zip :
     * email :
     * province_id : 1
     * city_id : 72
     * county_id : 2839
     * town_id : 0
     * "summary": "请注意发顺风",    // 备注
     */

    private ExpressInfoBean express_info;
    private int invoice_type;
    private int invoice_caty;
    private String invoice_title;
    private String invoice_content;
    private String trade_site_name;
    private String summary;
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
    private int exist_sub_order;
    private String created_at;
    private int current_user_id;
    /**
     * sku : 1031789053
     * product_id : 1011497002
     * quantity : 2
     * price : 0.02
     * sale_price : 0.02
     * kind : 0
     * vop_id :
     * refund_type : 2
     * refund_status : 1
     * refund_label : 退款中
     * refund_button : 0
     * name : 华米手环
     * sku_name : 黑色
     * cover_url : http://frbird.qiniudn.com/product/160121/56a0934f3ffca26c098ba90e-4-p500x500.jpg
     */

    private List<ItemsBean> items;
    /**
     * id : 116112506533-1
     * items : [{"sku":1031789053,"product_id":1011497002,"quantity":2,"price":0.02,"sale_price":0.02,"kind":0,"vop_id":"","refund_type":2,"refund_status":1,"refund_label":"退款中","refund_button":0,"name":"华米手环","sku_name":"黑色","cover_url":"http://frbird.qiniudn.com/product/160121/56a0934f3ffca26c098ba90e-4-p500x500.jpg"},{"sku":1011513749,"product_id":1011513749,"quantity":1,"price":0.15,"sale_price":0.15,"kind":0,"vop_id":"","refund_type":0,"refund_status":0,"refund_label":"","refund_button":0,"name":"a1螺丝刀a1螺丝刀a1螺丝刀","sku_name":null,"cover_url":"http://frbird.qiniudn.com/product/160127/56a858723ffca269098bb039-4-p500x500.jpg"}]
     * items_count : 2
     * split_on : 1480064335
     * is_sended : 1
     * sended_on : 1480064344
     * express_caty : z
     * express_no : wewe
     * supplier_id :
     * split_at : 2016-11-25 16:58
     * sended_at : 2016-11-25 16:59
     * express_company : 中通快递
     */

    private List<SubOrdersBean> sub_orders;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public int getItems_count() {
        return items_count;
    }

    public void setItems_count(int items_count) {
        this.items_count = items_count;
    }

    public double getTotal_money() {
        return total_money;
    }

    public void setTotal_money(double total_money) {
        this.total_money = total_money;
    }

    public double getPay_money() {
        return pay_money;
    }

    public void setPay_money(double pay_money) {
        this.pay_money = pay_money;
    }

    public int getCard_money() {
        return card_money;
    }

    public void setCard_money(int card_money) {
        this.card_money = card_money;
    }

    public int getCoin_money() {
        return coin_money;
    }

    public void setCoin_money(int coin_money) {
        this.coin_money = coin_money;
    }

    public int getFreight() {
        return freight;
    }

    public void setFreight(int freight) {
        this.freight = freight;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getAddbook_id() {
        return addbook_id;
    }

    public void setAddbook_id(String addbook_id) {
        this.addbook_id = addbook_id;
    }

    public Object getAddbook() {
        return addbook;
    }

    public void setAddbook(String addbook) {
        this.addbook = addbook;
    }

    public ExpressInfoBean getExpress_info() {
        return express_info;
    }

    public void setExpress_info(ExpressInfoBean express_info) {
        this.express_info = express_info;
    }

    public int getInvoice_type() {
        return invoice_type;
    }

    public void setInvoice_type(int invoice_type) {
        this.invoice_type = invoice_type;
    }

    public int getInvoice_caty() {
        return invoice_caty;
    }

    public void setInvoice_caty(int invoice_caty) {
        this.invoice_caty = invoice_caty;
    }

    public String getInvoice_title() {
        return invoice_title;
    }

    public void setInvoice_title(String invoice_title) {
        this.invoice_title = invoice_title;
    }

    public String getInvoice_content() {
        return invoice_content;
    }

    public void setInvoice_content(String invoice_content) {
        this.invoice_content = invoice_content;
    }

    public String getTrade_site_name() {
        return trade_site_name;
    }

    public void setTrade_site_name(String trade_site_name) {
        this.trade_site_name = trade_site_name;
    }

    public String getPayment_method() {
        return payment_method;
    }

    public void setPayment_method(String payment_method) {
        this.payment_method = payment_method;
    }

    public String getExpress_caty() {
        return express_caty;
    }

    public void setExpress_caty(String express_caty) {
        this.express_caty = express_caty;
    }

    public String getExpress_company() {
        return express_company;
    }

    public void setExpress_company(String express_company) {
        this.express_company = express_company;
    }

    public String getExpress_no() {
        return express_no;
    }

    public void setExpress_no(String express_no) {
        this.express_no = express_no;
    }

    public int getSended_date() {
        return sended_date;
    }

    public void setSended_date(int sended_date) {
        this.sended_date = sended_date;
    }

    public String getCard_code() {
        return card_code;
    }

    public void setCard_code(String card_code) {
        this.card_code = card_code;
    }

    public int getIs_presaled() {
        return is_presaled;
    }

    public void setIs_presaled(int is_presaled) {
        this.is_presaled = is_presaled;
    }

    public int getExpired_time() {
        return expired_time;
    }

    public void setExpired_time(int expired_time) {
        this.expired_time = expired_time;
    }

    public int getFrom_site() {
        return from_site;
    }

    public void setFrom_site(int from_site) {
        this.from_site = from_site;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getGift_code() {
        return gift_code;
    }

    public void setGift_code(String gift_code) {
        this.gift_code = gift_code;
    }

    public int getBird_coin_count() {
        return bird_coin_count;
    }

    public void setBird_coin_count(int bird_coin_count) {
        this.bird_coin_count = bird_coin_count;
    }

    public int getBird_coin_money() {
        return bird_coin_money;
    }

    public void setBird_coin_money(int bird_coin_money) {
        this.bird_coin_money = bird_coin_money;
    }

    public int getGift_money() {
        return gift_money;
    }

    public void setGift_money(int gift_money) {
        this.gift_money = gift_money;
    }

    public String getStatus_label() {
        return status_label;
    }

    public void setStatus_label(String status_label) {
        this.status_label = status_label;
    }

    public int getCreated_on() {
        return created_on;
    }

    public void setCreated_on(int created_on) {
        this.created_on = created_on;
    }

    public int getUpdated_on() {
        return updated_on;
    }

    public void setUpdated_on(int updated_on) {
        this.updated_on = updated_on;
    }

    public int getExist_sub_order() {
        return exist_sub_order;
    }

    public void setExist_sub_order(int exist_sub_order) {
        this.exist_sub_order = exist_sub_order;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public int getCurrent_user_id() {
        return current_user_id;
    }

    public void setCurrent_user_id(int current_user_id) {
        this.current_user_id = current_user_id;
    }

    public List<ItemsBean> getItems() {
        return items;
    }

    public void setItems(List<ItemsBean> items) {
        this.items = items;
    }

    public List<SubOrdersBean> getSub_orders() {
        return sub_orders;
    }

    public void setSub_orders(List<SubOrdersBean> sub_orders) {
        this.sub_orders = sub_orders;
    }

    public String getDiscount_money() {
        return discount_money;
    }

    public void setDiscount_money(String discount_money) {
        this.discount_money = discount_money;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public static class ExpressInfoBean {
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

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getCounty() {
            return county;
        }

        public void setCounty(String county) {
            this.county = county;
        }

        public String getTown() {
            return town;
        }

        public void setTown(String town) {
            this.town = town;
        }

        public String getArea() {
            return area;
        }

        public void setArea(String area) {
            this.area = area;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getZip() {
            return zip;
        }

        public void setZip(String zip) {
            this.zip = zip;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public int getProvince_id() {
            return province_id;
        }

        public void setProvince_id(int province_id) {
            this.province_id = province_id;
        }

        public int getCity_id() {
            return city_id;
        }

        public void setCity_id(int city_id) {
            this.city_id = city_id;
        }

        public int getCounty_id() {
            return county_id;
        }

        public void setCounty_id(int county_id) {
            this.county_id = county_id;
        }

        public int getTown_id() {
            return town_id;
        }

        public void setTown_id(int town_id) {
            this.town_id = town_id;
        }
    }

    public static class ItemsBean {
        private int sku;
        private int product_id;
        private int quantity;
        private double price;
        private double sale_price;
        private int kind;
        private String vop_id;
        private int refund_type;
        private int refund_status;
        private String refund_label;
        private int refund_button;
        private String name;
        private String sku_name;
        private String cover_url;

        public int getSku() {
            return sku;
        }

        public void setSku(int sku) {
            this.sku = sku;
        }

        public int getProduct_id() {
            return product_id;
        }

        public void setProduct_id(int product_id) {
            this.product_id = product_id;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public double getSale_price() {
            return sale_price;
        }

        public void setSale_price(double sale_price) {
            this.sale_price = sale_price;
        }

        public int getKind() {
            return kind;
        }

        public void setKind(int kind) {
            this.kind = kind;
        }

        public String getVop_id() {
            return vop_id;
        }

        public void setVop_id(String vop_id) {
            this.vop_id = vop_id;
        }

        public int getRefund_type() {
            return refund_type;
        }

        public void setRefund_type(int refund_type) {
            this.refund_type = refund_type;
        }

        public int getRefund_status() {
            return refund_status;
        }

        public void setRefund_status(int refund_status) {
            this.refund_status = refund_status;
        }

        public String getRefund_label() {
            return refund_label;
        }

        public void setRefund_label(String refund_label) {
            this.refund_label = refund_label;
        }

        public int getRefund_button() {
            return refund_button;
        }

        public void setRefund_button(int refund_button) {
            this.refund_button = refund_button;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSku_name() {
            return sku_name;
        }

        public void setSku_name(String sku_name) {
            this.sku_name = sku_name;
        }

        public String getCover_url() {
            return cover_url;
        }

        public void setCover_url(String cover_url) {
            this.cover_url = cover_url;
        }
    }

    public static class SubOrdersBean {
        private String id;
        private int items_count;
        private int split_on;
        private int is_sended;
        private int sended_on;
        private String express_caty;
        private String express_no;
        private String supplier_id;
        private String split_at;
        private String sended_at;
        private String express_company;
        /**
         * sku : 1031789053
         * product_id : 1011497002
         * quantity : 2
         * price : 0.02
         * sale_price : 0.02
         * kind : 0
         * vop_id :
         * refund_type : 2
         * refund_status : 1
         * refund_label : 退款中
         * refund_button : 0
         * name : 华米手环
         * sku_name : 黑色
         * cover_url : http://frbird.qiniudn.com/product/160121/56a0934f3ffca26c098ba90e-4-p500x500.jpg
         */

        private List<ItemsBean> items;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public int getItems_count() {
            return items_count;
        }

        public void setItems_count(int items_count) {
            this.items_count = items_count;
        }

        public int getSplit_on() {
            return split_on;
        }

        public void setSplit_on(int split_on) {
            this.split_on = split_on;
        }

        public int getIs_sended() {
            return is_sended;
        }

        public void setIs_sended(int is_sended) {
            this.is_sended = is_sended;
        }

        public int getSended_on() {
            return sended_on;
        }

        public void setSended_on(int sended_on) {
            this.sended_on = sended_on;
        }

        public String getExpress_caty() {
            return express_caty;
        }

        public void setExpress_caty(String express_caty) {
            this.express_caty = express_caty;
        }

        public String getExpress_no() {
            return express_no;
        }

        public void setExpress_no(String express_no) {
            this.express_no = express_no;
        }

        public String getSupplier_id() {
            return supplier_id;
        }

        public void setSupplier_id(String supplier_id) {
            this.supplier_id = supplier_id;
        }

        public String getSplit_at() {
            return split_at;
        }

        public void setSplit_at(String split_at) {
            this.split_at = split_at;
        }

        public String getSended_at() {
            return sended_at;
        }

        public void setSended_at(String sended_at) {
            this.sended_at = sended_at;
        }

        public String getExpress_company() {
            return express_company;
        }

        public void setExpress_company(String express_company) {
            this.express_company = express_company;
        }

        public List<ItemsBean> getItems() {
            return items;
        }

        public void setItems(List<ItemsBean> items) {
            this.items = items;
        }

        public static class ItemsBean {
            private int sku;
            private int product_id;
            private int quantity;
            private double price;
            private double sale_price;
            private int kind;
            private String vop_id;
            private int refund_type;
            private int refund_status;
            private String refund_label;
            private int refund_button;
            private String name;
            private String sku_name;
            private String cover_url;

            public int getSku() {
                return sku;
            }

            public void setSku(int sku) {
                this.sku = sku;
            }

            public int getProduct_id() {
                return product_id;
            }

            public void setProduct_id(int product_id) {
                this.product_id = product_id;
            }

            public int getQuantity() {
                return quantity;
            }

            public void setQuantity(int quantity) {
                this.quantity = quantity;
            }

            public double getPrice() {
                return price;
            }

            public void setPrice(double price) {
                this.price = price;
            }

            public double getSale_price() {
                return sale_price;
            }

            public void setSale_price(double sale_price) {
                this.sale_price = sale_price;
            }

            public int getKind() {
                return kind;
            }

            public void setKind(int kind) {
                this.kind = kind;
            }

            public String getVop_id() {
                return vop_id;
            }

            public void setVop_id(String vop_id) {
                this.vop_id = vop_id;
            }

            public int getRefund_type() {
                return refund_type;
            }

            public void setRefund_type(int refund_type) {
                this.refund_type = refund_type;
            }

            public int getRefund_status() {
                return refund_status;
            }

            public void setRefund_status(int refund_status) {
                this.refund_status = refund_status;
            }

            public String getRefund_label() {
                return refund_label;
            }

            public void setRefund_label(String refund_label) {
                this.refund_label = refund_label;
            }

            public int getRefund_button() {
                return refund_button;
            }

            public void setRefund_button(int refund_button) {
                this.refund_button = refund_button;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getSku_name() {
                return sku_name;
            }

            public void setSku_name(String sku_name) {
                this.sku_name = sku_name;
            }

            public String getCover_url() {
                return cover_url;
            }

            public void setCover_url(String cover_url) {
                this.cover_url = cover_url;
            }
        }
    }
}
