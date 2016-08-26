package com.taihuoniao.fineix.beans;

import com.taihuoniao.fineix.base.NetBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by taihuoniao on 2016/2/26.
 * 立即购买网络请求返回值的javabean
 */
public class NowBuyBean extends NetBean implements Serializable {

    /**
     * is_error : false
     * data : {"is_nowbuy":1,"pay_money":199,"order_info":{"rid":"116082547690","user_id":955832,"dict":{"payment_method":"a","transfer":"a","transfer_time":"a","summary":"","invoice_type":0,"freight":0,"card_money":0,"coin_money":0,"gift_money":0,"bird_coin_money":0,"bird_coin_count":0,"invoice_caty":"p","invoice_content":"d","items":[{"target_id":1046252913,"sku":1046252913,"product_id":1046252893,"type":2,"quantity":1,"price":199,"sale_price":199,"title":"双子座插卡手机壳","sku_mode":"黑色","cover":"http://frbird.qiniudn.com/product/160804/57a2d823fc8b12314c8b8c07-1-s.jpg","view_url":"http://www.taihuoniao.com/shop/view-1046252893-1.html","subtotal":199,"kind":0,"sku_name":"黑色"}],"total_money":199,"items_count":1,"addbook_id":"576ba3f6fc8b125f428bcdce"},"expired":1472173969,"is_cart":0,"is_presaled":0,"kind":0,"created_on":1472137969,"updated_on":1472137969,"_id":47690},"bonus":[{"_id":{"$id":"57bc40c4c0007614538b8cb3"},"code":"HKB8S8l8","amount":100,"xname":"DA","min_amount":399,"product_id":0,"user_id":955832,"get_at":1471961484,"used_by":0,"used_at":0,"used":1,"order_rid":"","expired_at":1474553484,"status":4,"created_on":1471955140,"updated_on":1471961484,"expired_label":"将于 2016-09-22 22:11:24 过期","__extend__":true}],"current_user_id":955832}
     */

    private boolean is_error;
    /**
     * is_nowbuy : 1
     * pay_money : 199
     * order_info : {"rid":"116082547690","user_id":955832,"dict":{"payment_method":"a","transfer":"a","transfer_time":"a","summary":"","invoice_type":0,"freight":0,"card_money":0,"coin_money":0,"gift_money":0,"bird_coin_money":0,"bird_coin_count":0,"invoice_caty":"p","invoice_content":"d","items":[{"target_id":1046252913,"sku":1046252913,"product_id":1046252893,"type":2,"quantity":1,"price":199,"sale_price":199,"title":"双子座插卡手机壳","sku_mode":"黑色","cover":"http://frbird.qiniudn.com/product/160804/57a2d823fc8b12314c8b8c07-1-s.jpg","view_url":"http://www.taihuoniao.com/shop/view-1046252893-1.html","subtotal":199,"kind":0,"sku_name":"黑色"}],"total_money":199,"items_count":1,"addbook_id":"576ba3f6fc8b125f428bcdce"},"expired":1472173969,"is_cart":0,"is_presaled":0,"kind":0,"created_on":1472137969,"updated_on":1472137969,"_id":47690}
     * bonus : [{"_id":{"$id":"57bc40c4c0007614538b8cb3"},"code":"HKB8S8l8","amount":100,"xname":"DA","min_amount":399,"product_id":0,"user_id":955832,"get_at":1471961484,"used_by":0,"used_at":0,"used":1,"order_rid":"","expired_at":1474553484,"status":4,"created_on":1471955140,"updated_on":1471961484,"expired_label":"将于 2016-09-22 22:11:24 过期","__extend__":true}]
     * current_user_id : 955832
     */

    private DataBean data;

    public boolean isIs_error() {
        return is_error;
    }

    public void setIs_error(boolean is_error) {
        this.is_error = is_error;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean implements Serializable {
        private int is_nowbuy;
        private double pay_money;
        /**
         * rid : 116082547690
         * user_id : 955832
         * dict : {"payment_method":"a","transfer":"a","transfer_time":"a","summary":"","invoice_type":0,"freight":0,"card_money":0,"coin_money":0,"gift_money":0,"bird_coin_money":0,"bird_coin_count":0,"invoice_caty":"p","invoice_content":"d","items":[{"target_id":1046252913,"sku":1046252913,"product_id":1046252893,"type":2,"quantity":1,"price":199,"sale_price":199,"title":"双子座插卡手机壳","sku_mode":"黑色","cover":"http://frbird.qiniudn.com/product/160804/57a2d823fc8b12314c8b8c07-1-s.jpg","view_url":"http://www.taihuoniao.com/shop/view-1046252893-1.html","subtotal":199,"kind":0,"sku_name":"黑色"}],"total_money":199,"items_count":1,"addbook_id":"576ba3f6fc8b125f428bcdce"}
         * expired : 1472173969
         * is_cart : 0
         * is_presaled : 0
         * kind : 0
         * created_on : 1472137969
         * updated_on : 1472137969
         * _id : 47690
         */

        private OrderInfoBean order_info;
        private String current_user_id;
        /**
         * _id : {"$id":"57bc40c4c0007614538b8cb3"}
         * code : HKB8S8l8
         * amount : 100
         * xname : DA
         * min_amount : 399
         * product_id : 0
         * user_id : 955832
         * get_at : 1471961484
         * used_by : 0
         * used_at : 0
         * used : 1
         * order_rid :
         * expired_at : 1474553484
         * status : 4
         * created_on : 1471955140
         * updated_on : 1471961484
         * expired_label : 将于 2016-09-22 22:11:24 过期
         * __extend__ : true
         */

        private List<BonusBean> bonus;

        public int getIs_nowbuy() {
            return is_nowbuy;
        }

        public void setIs_nowbuy(int is_nowbuy) {
            this.is_nowbuy = is_nowbuy;
        }

        public double getPay_money() {
            return pay_money;
        }

        public void setPay_money(double pay_money) {
            this.pay_money = pay_money;
        }

        public OrderInfoBean getOrder_info() {
            return order_info;
        }

        public void setOrder_info(OrderInfoBean order_info) {
            this.order_info = order_info;
        }

        public String getCurrent_user_id() {
            return current_user_id;
        }

        public void setCurrent_user_id(String current_user_id) {
            this.current_user_id = current_user_id;
        }

        public List<BonusBean> getBonus() {
            return bonus;
        }

        public void setBonus(List<BonusBean> bonus) {
            this.bonus = bonus;
        }

        public static class OrderInfoBean implements Serializable{
            private String rid;
            private String user_id;
            /**
             * payment_method : a
             * transfer : a
             * transfer_time : a
             * summary :
             * invoice_type : 0
             * freight : 0
             * card_money : 0
             * coin_money : 0
             * gift_money : 0
             * bird_coin_money : 0
             * bird_coin_count : 0
             * invoice_caty : p
             * invoice_content : d
             * items : [{"target_id":1046252913,"sku":1046252913,"product_id":1046252893,"type":2,"quantity":1,"price":199,"sale_price":199,"title":"双子座插卡手机壳","sku_mode":"黑色","cover":"http://frbird.qiniudn.com/product/160804/57a2d823fc8b12314c8b8c07-1-s.jpg","view_url":"http://www.taihuoniao.com/shop/view-1046252893-1.html","subtotal":199,"kind":0,"sku_name":"黑色"}]
             * total_money : 199
             * items_count : 1
             * addbook_id : 576ba3f6fc8b125f428bcdce
             */

            private DictBean dict;
            private String expired;
            private int is_cart;
            private int is_presaled;
            private String kind;
            private String created_on;
            private String updated_on;
            private String _id;

            public String getRid() {
                return rid;
            }

            public void setRid(String rid) {
                this.rid = rid;
            }

            public String getUser_id() {
                return user_id;
            }

            public void setUser_id(String user_id) {
                this.user_id = user_id;
            }

            public DictBean getDict() {
                return dict;
            }

            public void setDict(DictBean dict) {
                this.dict = dict;
            }

            public String getExpired() {
                return expired;
            }

            public void setExpired(String expired) {
                this.expired = expired;
            }

            public int getIs_cart() {
                return is_cart;
            }

            public void setIs_cart(int is_cart) {
                this.is_cart = is_cart;
            }

            public int getIs_presaled() {
                return is_presaled;
            }

            public void setIs_presaled(int is_presaled) {
                this.is_presaled = is_presaled;
            }

            public String getKind() {
                return kind;
            }

            public void setKind(String kind) {
                this.kind = kind;
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

            public String get_id() {
                return _id;
            }

            public void set_id(String _id) {
                this._id = _id;
            }

            public static class DictBean implements Serializable{
                private String payment_method;
                private String transfer;
                private String transfer_time;
                private String summary;
                private String invoice_type;
                private String freight;
                private String card_money;
                private String coin_money;
                private String gift_money;
                private String bird_coin_money;
                private String bird_coin_count;
                private String invoice_caty;
                private String invoice_content;
                private double total_money;
                private int items_count;
                private String addbook_id;
                /**
                 * target_id : 1046252913
                 * sku : 1046252913
                 * product_id : 1046252893
                 * type : 2
                 * quantity : 1
                 * price : 199
                 * sale_price : 199
                 * title : 双子座插卡手机壳
                 * sku_mode : 黑色
                 * cover : http://frbird.qiniudn.com/product/160804/57a2d823fc8b12314c8b8c07-1-s.jpg
                 * view_url : http://www.taihuoniao.com/shop/view-1046252893-1.html
                 * subtotal : 199
                 * kind : 0
                 * sku_name : 黑色
                 */

                private List<ItemsBean> items;

                public String getPayment_method() {
                    return payment_method;
                }

                public void setPayment_method(String payment_method) {
                    this.payment_method = payment_method;
                }

                public String getTransfer() {
                    return transfer;
                }

                public void setTransfer(String transfer) {
                    this.transfer = transfer;
                }

                public String getTransfer_time() {
                    return transfer_time;
                }

                public void setTransfer_time(String transfer_time) {
                    this.transfer_time = transfer_time;
                }

                public String getSummary() {
                    return summary;
                }

                public void setSummary(String summary) {
                    this.summary = summary;
                }

                public String getInvoice_type() {
                    return invoice_type;
                }

                public void setInvoice_type(String invoice_type) {
                    this.invoice_type = invoice_type;
                }

                public String getFreight() {
                    return freight;
                }

                public void setFreight(String freight) {
                    this.freight = freight;
                }

                public String getCard_money() {
                    return card_money;
                }

                public void setCard_money(String card_money) {
                    this.card_money = card_money;
                }

                public String getCoin_money() {
                    return coin_money;
                }

                public void setCoin_money(String coin_money) {
                    this.coin_money = coin_money;
                }

                public String getGift_money() {
                    return gift_money;
                }

                public void setGift_money(String gift_money) {
                    this.gift_money = gift_money;
                }

                public String getBird_coin_money() {
                    return bird_coin_money;
                }

                public void setBird_coin_money(String bird_coin_money) {
                    this.bird_coin_money = bird_coin_money;
                }

                public String getBird_coin_count() {
                    return bird_coin_count;
                }

                public void setBird_coin_count(String bird_coin_count) {
                    this.bird_coin_count = bird_coin_count;
                }

                public String getInvoice_caty() {
                    return invoice_caty;
                }

                public void setInvoice_caty(String invoice_caty) {
                    this.invoice_caty = invoice_caty;
                }

                public String getInvoice_content() {
                    return invoice_content;
                }

                public void setInvoice_content(String invoice_content) {
                    this.invoice_content = invoice_content;
                }

                public double getTotal_money() {
                    return total_money;
                }

                public void setTotal_money(double total_money) {
                    this.total_money = total_money;
                }

                public int getItems_count() {
                    return items_count;
                }

                public void setItems_count(int items_count) {
                    this.items_count = items_count;
                }

                public String getAddbook_id() {
                    return addbook_id;
                }

                public void setAddbook_id(String addbook_id) {
                    this.addbook_id = addbook_id;
                }

                public List<ItemsBean> getItems() {
                    return items;
                }

                public void setItems(List<ItemsBean> items) {
                    this.items = items;
                }

                public static class ItemsBean implements Serializable{
                    private String target_id;
                    private String sku;
                    private String product_id;
                    private String type;
                    private String quantity;
                    private double price;
                    private double sale_price;
                    private String title;
                    private String sku_mode;
                    private String cover;
                    private String view_url;
                    private double subtotal;
                    private int kind;
                    private String sku_name;

                    public String getTarget_id() {
                        return target_id;
                    }

                    public void setTarget_id(String target_id) {
                        this.target_id = target_id;
                    }

                    public String getSku() {
                        return sku;
                    }

                    public void setSku(String sku) {
                        this.sku = sku;
                    }

                    public String getProduct_id() {
                        return product_id;
                    }

                    public void setProduct_id(String product_id) {
                        this.product_id = product_id;
                    }

                    public String getType() {
                        return type;
                    }

                    public void setType(String type) {
                        this.type = type;
                    }

                    public String getQuantity() {
                        return quantity;
                    }

                    public void setQuantity(String quantity) {
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

                    public String getTitle() {
                        return title;
                    }

                    public void setTitle(String title) {
                        this.title = title;
                    }

                    public String getSku_mode() {
                        return sku_mode;
                    }

                    public void setSku_mode(String sku_mode) {
                        this.sku_mode = sku_mode;
                    }

                    public String getCover() {
                        return cover;
                    }

                    public void setCover(String cover) {
                        this.cover = cover;
                    }

                    public String getView_url() {
                        return view_url;
                    }

                    public void setView_url(String view_url) {
                        this.view_url = view_url;
                    }

                    public double getSubtotal() {
                        return subtotal;
                    }

                    public void setSubtotal(double subtotal) {
                        this.subtotal = subtotal;
                    }

                    public int getKind() {
                        return kind;
                    }

                    public void setKind(int kind) {
                        this.kind = kind;
                    }

                    public String getSku_name() {
                        return sku_name;
                    }

                    public void setSku_name(String sku_name) {
                        this.sku_name = sku_name;
                    }
                }
            }
        }

        public static class BonusBean implements Serializable{
            /**
             * $id : 57bc40c4c0007614538b8cb3
             */

            private IdBean _id;
            private String code;
            private int amount;
            private String xname;
            private int min_amount;
            private int product_id;
            private int user_id;
            private int get_at;
            private int used_by;
            private int used_at;
            private int used;
            private String order_rid;
            private int expired_at;
            private int status;
            private int created_on;
            private int updated_on;
            private String expired_label;
            private boolean __extend__;

            public IdBean get_id() {
                return _id;
            }

            public void set_id(IdBean _id) {
                this._id = _id;
            }

            public String getCode() {
                return code;
            }

            public void setCode(String code) {
                this.code = code;
            }

            public int getAmount() {
                return amount;
            }

            public void setAmount(int amount) {
                this.amount = amount;
            }

            public String getXname() {
                return xname;
            }

            public void setXname(String xname) {
                this.xname = xname;
            }

            public int getMin_amount() {
                return min_amount;
            }

            public void setMin_amount(int min_amount) {
                this.min_amount = min_amount;
            }

            public int getProduct_id() {
                return product_id;
            }

            public void setProduct_id(int product_id) {
                this.product_id = product_id;
            }

            public int getUser_id() {
                return user_id;
            }

            public void setUser_id(int user_id) {
                this.user_id = user_id;
            }

            public int getGet_at() {
                return get_at;
            }

            public void setGet_at(int get_at) {
                this.get_at = get_at;
            }

            public int getUsed_by() {
                return used_by;
            }

            public void setUsed_by(int used_by) {
                this.used_by = used_by;
            }

            public int getUsed_at() {
                return used_at;
            }

            public void setUsed_at(int used_at) {
                this.used_at = used_at;
            }

            public int getUsed() {
                return used;
            }

            public void setUsed(int used) {
                this.used = used;
            }

            public String getOrder_rid() {
                return order_rid;
            }

            public void setOrder_rid(String order_rid) {
                this.order_rid = order_rid;
            }

            public int getExpired_at() {
                return expired_at;
            }

            public void setExpired_at(int expired_at) {
                this.expired_at = expired_at;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
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

            public String getExpired_label() {
                return expired_label;
            }

            public void setExpired_label(String expired_label) {
                this.expired_label = expired_label;
            }

            public boolean is__extend__() {
                return __extend__;
            }

            public void set__extend__(boolean __extend__) {
                this.__extend__ = __extend__;
            }

            public static class IdBean implements Serializable{
                private String $id;

                public String get$id() {
                    return $id;
                }

                public void set$id(String $id) {
                    this.$id = $id;
                }
            }
        }
    }
}
