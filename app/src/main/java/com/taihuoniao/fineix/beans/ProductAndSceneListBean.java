package com.taihuoniao.fineix.beans;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by taihuoniao on 2016/5/3.
 * 产品场景关联列表
 */
public class ProductAndSceneListBean {

    /**
     * total_rows : 5
     * rows : [{"_id":"58118e25c0007684488b7e95","sight_id":2997,"product_id":1049278699,"product_kind":9,"brand_id":"57d6bb09fc8b1276048b6777","created_on":1477545509,"updated_on":1477545509,"product":null,"sight":{"_id":2997,"user_info":{"user_id":91017,"nickname":"巧克力工厂","avatar_url":"https://p4.taihuoniao.com/avatar/150508/554c9860fc8b12b63c8c024e-avm.jpg","summary":"","counter":{"message_count":1,"notice_count":16,"alert_count":55,"fans_count":30,"comment_count":1,"people_count":0,"fiu_comment_count":2,"fiu_alert_count":91,"order_wait_payment":0,"order_ready_goods":0,"order_sended_goods":0,"fiu_bonus_count":0,"order_evaluate":0},"follow_count":8,"fans_count":109,"love_count":0,"is_expert":0,"label":"","expert_label":"","expert_info":""},"cover_url":"https://p4.taihuoniao.com/scene_sight/161027/58118e1dc0007684488b7e93-hu.jpg","title":"爱花爱草","address":"北京SKP","scene_title":null,"is_love":0}},{"_id":"5819f150c0007681488b8860","sight_id":3078,"product_id":1049278699,"product_kind":9,"brand_id":"57d6bb09fc8b1276048b6777","created_on":1478095184,"updated_on":1478095184,"product":null,"sight":{"_id":3078,"user_info":{"user_id":1124507,"nickname":"二呆欧巴","avatar_url":"https://p4.taihuoniao.com/avatar/161002/57f0cd43c000762a578b8763-avm.jpg","summary":"没有人能随随便便成功","counter":{"message_count":0,"notice_count":0,"alert_count":0,"fans_count":0,"comment_count":0,"people_count":0,"fiu_alert_count":0,"fiu_comment_count":0,"fiu_notice_count":0,"sight_love_count":0,"order_wait_payment":0,"order_ready_goods":0,"order_sended_goods":0,"order_evaluate":0,"fiu_bonus_count":0},"follow_count":56,"fans_count":16,"love_count":0,"is_expert":0,"label":"单身","expert_label":"","expert_info":""},"cover_url":"https://p4.taihuoniao.com/scene_sight/161102/5819f14fc0007681488b885e-hu.jpg","title":"心在旅行","address":"河南城建学院后勤招待所","scene_title":null,"is_love":0}},{"_id":"581ca0dec0007699488b8a1a","sight_id":3155,"product_id":1049277857,"product_kind":9,"brand_id":"57d6bb09fc8b1276048b6777","created_on":1478271198,"updated_on":1478271198,"product":null,"sight":{"_id":3155,"user_info":{"user_id":776981,"nickname":"离开天空的鸟","avatar_url":"https://p4.taihuoniao.com/avatar/150909/55f0476cfc8b12ea098c271d-avm.jpg","summary":"你觉得我会说什么","counter":{"message_count":0,"notice_count":16,"alert_count":1,"fans_count":0,"comment_count":0,"people_count":0,"fiu_comment_count":0,"fiu_alert_count":0},"follow_count":21,"fans_count":13,"love_count":0,"is_expert":-1,"label":"创业先锋","expert_label":"","expert_info":""},"cover_url":"https://p4.taihuoniao.com/scene_sight/161104/581ca0dac0007699488b8a18-hu.jpg","title":"独享孤独","address":"","scene_title":null,"is_love":0}},{"_id":"582ac40fc00076b8538b47e4","sight_id":3647,"product_id":1049278699,"product_kind":9,"brand_id":"57d6bb09fc8b1276048b6777","created_on":1479197711,"updated_on":1479197711,"product":null,"sight":{"_id":3647,"user_info":{"user_id":1127437,"nickname":"匆匆过往","avatar_url":"https://p4.taihuoniao.com/avatar/161008/57f8f5e4c000767d468be407-avm.jpg","summary":"冬天就要到了","counter":{"message_count":0,"notice_count":0,"alert_count":0,"fans_count":3,"comment_count":0,"people_count":0,"fiu_alert_count":1,"fiu_comment_count":0,"fiu_notice_count":0,"sight_love_count":0,"order_wait_payment":0,"order_ready_goods":0,"order_sended_goods":0,"order_evaluate":0},"follow_count":7,"fans_count":5,"love_count":0,"is_expert":0,"label":"驴友","expert_label":"","expert_info":""},"cover_url":"https://p4.taihuoniao.com/scene_sight/161115/582ac40fc00076b8538b47e2-hu.jpg","title":"年轻的岁月","address":"常绿·大悦城(2期)","scene_title":null,"is_love":0}},{"_id":"5835975dc0007691348b4a6b","sight_id":4310,"product_id":1049278699,"product_kind":9,"brand_id":"57d6bb09fc8b1276048b6777","created_on":1479907165,"updated_on":1479907165,"product":null,"sight":{"_id":4310,"user_info":{"user_id":1126927,"nickname":"陌墨521","avatar_url":"https://p4.taihuoniao.com/avatar/161004/57f31c7cc000762a578b9065-avm.jpg","summary":"好好学习，多挣毛爷爷","counter":{"message_count":0,"notice_count":0,"alert_count":0,"fans_count":0,"comment_count":0,"people_count":0,"fiu_alert_count":0,"fiu_comment_count":0,"fiu_notice_count":0,"sight_love_count":0,"order_wait_payment":0,"order_ready_goods":0,"order_sended_goods":0,"order_evaluate":0},"follow_count":11,"fans_count":9,"love_count":0,"is_expert":0,"label":"学生","expert_label":"","expert_info":""},"cover_url":"https://p4.taihuoniao.com/scene_sight/161123/5835975cc0007691348b4a69-hu.jpg","title":"路途有多远","address":"鸿洲超市","scene_title":null,"is_love":0}}]
     * total_page : 1
     * current_page : 1
     * pager : 
     * next_page : 0
     * prev_page : 0
     * current_user_id : 1136552
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

    public static class RowsEntity {
        /**
         * _id : 58118e25c0007684488b7e95
         * sight_id : 2997
         * product_id : 1049278699
         * product_kind : 9
         * brand_id : 57d6bb09fc8b1276048b6777
         * created_on : 1477545509
         * updated_on : 1477545509
         * product : null
         * sight : {"_id":2997,"user_info":{"user_id":91017,"nickname":"巧克力工厂","avatar_url":"https://p4.taihuoniao.com/avatar/150508/554c9860fc8b12b63c8c024e-avm.jpg","summary":"","counter":{"message_count":1,"notice_count":16,"alert_count":55,"fans_count":30,"comment_count":1,"people_count":0,"fiu_comment_count":2,"fiu_alert_count":91,"order_wait_payment":0,"order_ready_goods":0,"order_sended_goods":0,"fiu_bonus_count":0,"order_evaluate":0},"follow_count":8,"fans_count":109,"love_count":0,"is_expert":0,"label":"","expert_label":"","expert_info":""},"cover_url":"https://p4.taihuoniao.com/scene_sight/161027/58118e1dc0007684488b7e93-hu.jpg","title":"爱花爱草","address":"北京SKP","scene_title":null,"is_love":0}
         */

        private String _id;
        private String sight_id;
        private String product_id;
        private String product_kind;
        private String brand_id;
        private String created_on;
        private String updated_on;
        private ProductEntity product;
        private SightEntity sight;

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

        public String getSight_id() {
            return sight_id;
        }

        public void setSight_id(String sight_id) {
            this.sight_id = sight_id;
        }

        public String getProduct_id() {
            return product_id;
        }

        public void setProduct_id(String product_id) {
            this.product_id = product_id;
        }

        public String getProduct_kind() {
            return product_kind;
        }

        public void setProduct_kind(String product_kind) {
            this.product_kind = product_kind;
        }

        public String getBrand_id() {
            return brand_id;
        }

        public void setBrand_id(String brand_id) {
            this.brand_id = brand_id;
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

        public ProductEntity getProduct() {
            return product;
        }

        public void setProduct(ProductEntity product) {
            this.product = product;
        }

        public SightEntity getSight() {
            return sight;
        }

        public void setSight(SightEntity sight) {
            this.sight = sight;
        }

        public static class ProductEntity {
            private String _id;
            private String title;
            private String oid;
            private double sale_price;
            private double market_price;
            private String link;
            private String attrbute;
            private String cover_url;
            private List<String> banner_asset;

            public String get_id() {
                return _id;
            }

            public void set_id(String _id) {
                this._id = _id;
            }

            public String getAttrbute() {
                return attrbute;
            }

            public void setAttrbute(String attrbute) {
                this.attrbute = attrbute;
            }

            public List<String> getBanner_asset() {
                return banner_asset;
            }

            public void setBanner_asset(List<String> banner_asset) {
                this.banner_asset = banner_asset;
            }

            public String getCover_url() {
                return cover_url;
            }

            public void setCover_url(String cover_url) {
                this.cover_url = cover_url;
            }

            public String getLink() {
                return link;
            }

            public void setLink(String link) {
                this.link = link;
            }

            public String getMarket_price() {
                return new DecimalFormat("######0.00").format(market_price);
            }

            public void setMarket_price(double market_price) {
                this.market_price = market_price;
            }

            public String getSale_price() {
                return new DecimalFormat("######0.00").format(sale_price);
            }

            public void setSale_price(double sale_price) {
                this.sale_price = sale_price;
            }

            public String getOid() {
                return oid;
            }

            public void setOid(String oid) {
                this.oid = oid;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }
        }

        public static class SightEntity {
            /**
             * _id : 2997
             * user_info : {"user_id":91017,"nickname":"巧克力工厂","avatar_url":"https://p4.taihuoniao.com/avatar/150508/554c9860fc8b12b63c8c024e-avm.jpg","summary":"","counter":{"message_count":1,"notice_count":16,"alert_count":55,"fans_count":30,"comment_count":1,"people_count":0,"fiu_comment_count":2,"fiu_alert_count":91,"order_wait_payment":0,"order_ready_goods":0,"order_sended_goods":0,"fiu_bonus_count":0,"order_evaluate":0},"follow_count":8,"fans_count":109,"love_count":0,"is_expert":0,"label":"","expert_label":"","expert_info":""}
             * cover_url : https://p4.taihuoniao.com/scene_sight/161027/58118e1dc0007684488b7e93-hu.jpg
             * title : 爱花爱草
             * address : 北京SKP
             * scene_title : null
             * is_love : 0
             */

            private String _id;
            private UserInfoEntity user_info;
            private String cover_url;
            private String title;
            private String address;
            private String scene_title;
            private String is_love;

            public String get_id() {
                return _id;
            }

            public void set_id(String _id) {
                this._id = _id;
            }

            public UserInfoEntity getUser_info() {
                return user_info;
            }

            public void setUser_info(UserInfoEntity user_info) {
                this.user_info = user_info;
            }

            public String getCover_url() {
                return cover_url;
            }

            public void setCover_url(String cover_url) {
                this.cover_url = cover_url;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getAddress() {
                return address;
            }

            public void setAddress(String address) {
                this.address = address;
            }

            public String getScene_title() {
                return scene_title;
            }

            public void setScene_title(String scene_title) {
                this.scene_title = scene_title;
            }

            public String getIs_love() {
                return is_love;
            }

            public void setIs_love(String is_love) {
                this.is_love = is_love;
            }

            public static class UserInfoEntity {
                /**
                 * user_id : 91017
                 * nickname : 巧克力工厂
                 * avatar_url : https://p4.taihuoniao.com/avatar/150508/554c9860fc8b12b63c8c024e-avm.jpg
                 * summary : 
                 * counter : {"message_count":1,"notice_count":16,"alert_count":55,"fans_count":30,"comment_count":1,"people_count":0,"fiu_comment_count":2,"fiu_alert_count":91,"order_wait_payment":0,"order_ready_goods":0,"order_sended_goods":0,"fiu_bonus_count":0,"order_evaluate":0}
                 * follow_count : 8
                 * fans_count : 109
                 * love_count : 0
                 * is_expert : 0
                 * label : 
                 * expert_label : 
                 * expert_info : 
                 */

                private String user_id;
                private String nickname;
                private String avatar_url;
                private String summary;
                private CounterEntity counter;
                private String follow_count;
                private String fans_count;
                private String love_count;
                private String is_expert;
                private String label;
                private String expert_label;
                private String expert_info;

                public String getUser_id() {
                    return user_id;
                }

                public void setUser_id(String user_id) {
                    this.user_id = user_id;
                }

                public String getNickname() {
                    return nickname;
                }

                public void setNickname(String nickname) {
                    this.nickname = nickname;
                }

                public String getAvatar_url() {
                    return avatar_url;
                }

                public void setAvatar_url(String avatar_url) {
                    this.avatar_url = avatar_url;
                }

                public String getSummary() {
                    return summary;
                }

                public void setSummary(String summary) {
                    this.summary = summary;
                }

                public CounterEntity getCounter() {
                    return counter;
                }

                public void setCounter(CounterEntity counter) {
                    this.counter = counter;
                }

                public String getFollow_count() {
                    return follow_count;
                }

                public void setFollow_count(String follow_count) {
                    this.follow_count = follow_count;
                }

                public String getFans_count() {
                    return fans_count;
                }

                public void setFans_count(String fans_count) {
                    this.fans_count = fans_count;
                }

                public String getLove_count() {
                    return love_count;
                }

                public void setLove_count(String love_count) {
                    this.love_count = love_count;
                }

                public String getIs_expert() {
                    return is_expert;
                }

                public void setIs_expert(String is_expert) {
                    this.is_expert = is_expert;
                }

                public String getLabel() {
                    return label;
                }

                public void setLabel(String label) {
                    this.label = label;
                }

                public String getExpert_label() {
                    return expert_label;
                }

                public void setExpert_label(String expert_label) {
                    this.expert_label = expert_label;
                }

                public String getExpert_info() {
                    return expert_info;
                }

                public void setExpert_info(String expert_info) {
                    this.expert_info = expert_info;
                }

                public static class CounterEntity {
                    /**
                     * message_count : 1
                     * notice_count : 16
                     * alert_count : 55
                     * fans_count : 30
                     * comment_count : 1
                     * people_count : 0
                     * fiu_comment_count : 2
                     * fiu_alert_count : 91
                     * order_wait_payment : 0
                     * order_ready_goods : 0
                     * order_sended_goods : 0
                     * fiu_bonus_count : 0
                     * order_evaluate : 0
                     */

                    private String message_count;
                    private String notice_count;
                    private String alert_count;
                    private String fans_count;
                    private String comment_count;
                    private String people_count;
                    private String fiu_comment_count;
                    private String fiu_alert_count;
                    private String order_wait_payment;
                    private String order_ready_goods;
                    private String order_sended_goods;
                    private String fiu_bonus_count;
                    private String order_evaluate;

                    public String getMessage_count() {
                        return message_count;
                    }

                    public void setMessage_count(String message_count) {
                        this.message_count = message_count;
                    }

                    public String getNotice_count() {
                        return notice_count;
                    }

                    public void setNotice_count(String notice_count) {
                        this.notice_count = notice_count;
                    }

                    public String getAlert_count() {
                        return alert_count;
                    }

                    public void setAlert_count(String alert_count) {
                        this.alert_count = alert_count;
                    }

                    public String getFans_count() {
                        return fans_count;
                    }

                    public void setFans_count(String fans_count) {
                        this.fans_count = fans_count;
                    }

                    public String getComment_count() {
                        return comment_count;
                    }

                    public void setComment_count(String comment_count) {
                        this.comment_count = comment_count;
                    }

                    public String getPeople_count() {
                        return people_count;
                    }

                    public void setPeople_count(String people_count) {
                        this.people_count = people_count;
                    }

                    public String getFiu_comment_count() {
                        return fiu_comment_count;
                    }

                    public void setFiu_comment_count(String fiu_comment_count) {
                        this.fiu_comment_count = fiu_comment_count;
                    }

                    public String getFiu_alert_count() {
                        return fiu_alert_count;
                    }

                    public void setFiu_alert_count(String fiu_alert_count) {
                        this.fiu_alert_count = fiu_alert_count;
                    }

                    public String getOrder_wait_payment() {
                        return order_wait_payment;
                    }

                    public void setOrder_wait_payment(String order_wait_payment) {
                        this.order_wait_payment = order_wait_payment;
                    }

                    public String getOrder_ready_goods() {
                        return order_ready_goods;
                    }

                    public void setOrder_ready_goods(String order_ready_goods) {
                        this.order_ready_goods = order_ready_goods;
                    }

                    public String getOrder_sended_goods() {
                        return order_sended_goods;
                    }

                    public void setOrder_sended_goods(String order_sended_goods) {
                        this.order_sended_goods = order_sended_goods;
                    }

                    public String getFiu_bonus_count() {
                        return fiu_bonus_count;
                    }

                    public void setFiu_bonus_count(String fiu_bonus_count) {
                        this.fiu_bonus_count = fiu_bonus_count;
                    }

                    public String getOrder_evaluate() {
                        return order_evaluate;
                    }

                    public void setOrder_evaluate(String order_evaluate) {
                        this.order_evaluate = order_evaluate;
                    }
                }
            }
        }
    }
}
