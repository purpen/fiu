package com.taihuoniao.fineix.qingjingOrSceneDetails.bean;


import java.io.Serializable;
import java.util.List;

/**
 * Created by Stephen on 2017/3/2 15:52
 * Email: 895745843@qq.com
 */

public class SceneListBean2 implements Serializable {

    /**
     * total_rows : 20
     * rows : [{"_id":441,"title":"Go to itzzzzz","user_id":924912,"des":"Victory won't come to me unless I go to it.","scene_id":0,"category_id":0,"category_ids":[89],"tags":["tewst"],"product":[{"id":1102511135,"title":"HC-7020B 高端工程级HDMI线","x":0.35955293443468,"y":0.59118054707845,"loc":2,"type":2,"price":98}],"location":{"type":"Point","coordinates":[0,0]},"city":"","address":"","used_count":0,"view_count":9393,"love_count":4,"comment_count":1,"subject_ids":["value"],"stick":1,"stick_on":1481770935,"fine":1,"fine_on":1481770937,"is_check":1,"status":1,"deleted":0,"created_on":1480932742,"updated_on":1487754511,"is_product":1,"scene":["value"],"tags_s":"tewst","category_ids_s":"89","subject_ids_s":"","cover_url":"https://p4.taihuoniao.com/scene_sight/161205/58453d863ffca2976d8b457c-hu.jpg","created_at":"2016-12-05","user_info":{"user_id":924912,"nickname":"Test_android","avatar_url":"https://s3.taihuoniao.com/images/deavatar/002-m.jpg","summary":null,"counter":{"message_count":0,"notice_count":0,"alert_count":0,"fans_count":2,"comment_count":0,"people_count":0,"fiu_alert_count":7,"fiu_comment_count":1,"fiu_notice_count":0,"sight_love_count":0,"order_wait_payment":3,"order_ready_goods":2,"order_sended_goods":1,"order_evaluate":0,"fiu_bonus_count":4},"follow_count":13,"fans_count":1,"love_count":0,"is_expert":0,"label":"","expert_label":"","expert_info":"","is_follow":0},"comments":[{"_id":"58ae8c743ffca2995e8b4571","content":" 风风光光 vv","user_id":924893,"user_nickname":"微信用户董永胜8157","user_avatar_url":"https://p4.taihuoniao.com/avatar/160914/57d925d83ffca25c018b45e6-avn.jpg"}],"is_love":1,"is_favorite":0}]
     * total_page : 2
     * current_page : 1
     * pager : 
     * next_page : 2
     * prev_page : 0
     * current_user_id : 924912
     */

    private String total_rows;
    private String total_page;
    private String current_page;
    private String pager;
    private String next_page;
    private String prev_page;
    private String current_user_id;
    private List<RowsEntity> rows;

    public void setTotal_rows(String total_rows) {
        this.total_rows = total_rows;
    }

    public void setTotal_page(String total_page) {
        this.total_page = total_page;
    }

    public void setCurrent_page(String current_page) {
        this.current_page = current_page;
    }

    public void setPager(String pager) {
        this.pager = pager;
    }

    public void setNext_page(String next_page) {
        this.next_page = next_page;
    }

    public void setPrev_page(String prev_page) {
        this.prev_page = prev_page;
    }

    public void setCurrent_user_id(String current_user_id) {
        this.current_user_id = current_user_id;
    }

    public void setRows(List<RowsEntity> rows) {
        this.rows = rows;
    }

    public String getTotal_rows() {
        return total_rows;
    }

    public String getTotal_page() {
        return total_page;
    }

    public String getCurrent_page() {
        return current_page;
    }

    public String getPager() {
        return pager;
    }

    public String getNext_page() {
        return next_page;
    }

    public String getPrev_page() {
        return prev_page;
    }

    public String getCurrent_user_id() {
        return current_user_id;
    }

    public List<RowsEntity> getRows() {
        return rows;
    }

    public static class RowsEntity implements Serializable {
        /**
         * _id : 441
         * title : Go to itzzzzz
         * user_id : 924912
         * des : Victory won't come to me unless I go to it.
         * scene_id : 0
         * category_id : 0
         * category_ids : [89]
         * tags : ["tewst"]
         * product : [{"id":1102511135,"title":"HC-7020B 高端工程级HDMI线","x":0.35955293443468,"y":0.59118054707845,"loc":2,"type":2,"price":98}]
         * location : {"type":"Point","coordinates":[0,0]}
         * city : 
         * address : 
         * used_count : 0
         * view_count : 9393
         * love_count : 4
         * comment_count : 1
         * subject_ids : ["value"]
         * stick : 1
         * stick_on : 1481770935
         * fine : 1
         * fine_on : 1481770937
         * is_check : 1
         * status : 1
         * deleted : 0
         * created_on : 1480932742
         * updated_on : 1487754511
         * is_product : 1
         * scene : ["value"]
         * tags_s : tewst
         * category_ids_s : 89
         * subject_ids_s : 
         * cover_url : https://p4.taihuoniao.com/scene_sight/161205/58453d863ffca2976d8b457c-hu.jpg
         * created_at : 2016-12-05
         * user_info : {"user_id":924912,"nickname":"Test_android","avatar_url":"https://s3.taihuoniao.com/images/deavatar/002-m.jpg","summary":null,"counter":{"message_count":0,"notice_count":0,"alert_count":0,"fans_count":2,"comment_count":0,"people_count":0,"fiu_alert_count":7,"fiu_comment_count":1,"fiu_notice_count":0,"sight_love_count":0,"order_wait_payment":3,"order_ready_goods":2,"order_sended_goods":1,"order_evaluate":0,"fiu_bonus_count":4},"follow_count":13,"fans_count":1,"love_count":0,"is_expert":0,"label":"","expert_label":"","expert_info":"","is_follow":0}
         * comments : [{"_id":"58ae8c743ffca2995e8b4571","content":" 风风光光 vv","user_id":924893,"user_nickname":"微信用户董永胜8157","user_avatar_url":"https://p4.taihuoniao.com/avatar/160914/57d925d83ffca25c018b45e6-avn.jpg"}]
         * is_love : 1
         * is_favorite : 0
         */

        private String _id;
        private String title;
        private String user_id;
        private String des;
        private String scene_id;
        private String category_id;
        private LocationEntity location;
        private String city;
        private String address;
        private String used_count;
        private String view_count;
        private String love_count;
        private String comment_count;
        private String stick;
        private String stick_on;
        private String fine;
        private String fine_on;
        private String is_check;
        private String status;
        private String deleted;
        private String created_on;
        private String updated_on;
        private String is_product;
        private String tags_s;
        private String category_ids_s;
        private String subject_ids_s;
        private String cover_url;
        private String created_at;
        private UserInfoEntity user_info;
        private String is_love;
        private String is_favorite;
        private List<String> category_ids;
        private List<String> tags;
        private List<ProductEntity> product;
        private List<String> subject_ids;
        private SceneEntity scene;
        private List<CommentsEntity> comments;

        public void set_id(String _id) {
            this._id = _id;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public void setDes(String des) {
            this.des = des;
        }

        public void setScene_id(String scene_id) {
            this.scene_id = scene_id;
        }

        public void setCategory_id(String category_id) {
            this.category_id = category_id;
        }

        public void setLocation(LocationEntity location) {
            this.location = location;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public void setUsed_count(String used_count) {
            this.used_count = used_count;
        }

        public void setView_count(String view_count) {
            this.view_count = view_count;
        }

        public void setLove_count(String love_count) {
            this.love_count = love_count;
        }

        public void setComment_count(String comment_count) {
            this.comment_count = comment_count;
        }

        public void setStick(String stick) {
            this.stick = stick;
        }

        public void setStick_on(String stick_on) {
            this.stick_on = stick_on;
        }

        public void setFine(String fine) {
            this.fine = fine;
        }

        public void setFine_on(String fine_on) {
            this.fine_on = fine_on;
        }

        public void setIs_check(String is_check) {
            this.is_check = is_check;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public void setDeleted(String deleted) {
            this.deleted = deleted;
        }

        public void setCreated_on(String created_on) {
            this.created_on = created_on;
        }

        public void setUpdated_on(String updated_on) {
            this.updated_on = updated_on;
        }

        public void setIs_product(String is_product) {
            this.is_product = is_product;
        }

        public void setTags_s(String tags_s) {
            this.tags_s = tags_s;
        }

        public void setCategory_ids_s(String category_ids_s) {
            this.category_ids_s = category_ids_s;
        }

        public void setSubject_ids_s(String subject_ids_s) {
            this.subject_ids_s = subject_ids_s;
        }

        public void setCover_url(String cover_url) {
            this.cover_url = cover_url;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public void setUser_info(UserInfoEntity user_info) {
            this.user_info = user_info;
        }

        public void setIs_love(String is_love) {
            this.is_love = is_love;
        }

        public void setIs_favorite(String is_favorite) {
            this.is_favorite = is_favorite;
        }

        public void setCategory_ids(List<String> category_ids) {
            this.category_ids = category_ids;
        }

        public void setTags(List<String> tags) {
            this.tags = tags;
        }

        public void setProduct(List<ProductEntity> product) {
            this.product = product;
        }

        public void setSubject_ids(List<String> subject_ids) {
            this.subject_ids = subject_ids;
        }

        public void setScene(SceneEntity scene) {
            this.scene = scene;
        }

        public void setComments(List<CommentsEntity> comments) {
            this.comments = comments;
        }

        public String get_id() {
            return _id;
        }

        public String getTitle() {
            return title;
        }

        public String getUser_id() {
            return user_id;
        }

        public String getDes() {
            return des;
        }

        public String getScene_id() {
            return scene_id;
        }

        public String getCategory_id() {
            return category_id;
        }

        public LocationEntity getLocation() {
            return location;
        }

        public String getCity() {
            return city;
        }

        public String getAddress() {
            return address;
        }

        public String getUsed_count() {
            return used_count;
        }

        public String getView_count() {
            return view_count;
        }

        public String getLove_count() {
            return love_count;
        }

        public String getComment_count() {
            return comment_count;
        }

        public String getStick() {
            return stick;
        }

        public String getStick_on() {
            return stick_on;
        }

        public String getFine() {
            return fine;
        }

        public String getFine_on() {
            return fine_on;
        }

        public String getIs_check() {
            return is_check;
        }

        public String getStatus() {
            return status;
        }

        public String getDeleted() {
            return deleted;
        }

        public String getCreated_on() {
            return created_on;
        }

        public String getUpdated_on() {
            return updated_on;
        }

        public String getIs_product() {
            return is_product;
        }

        public String getTags_s() {
            return tags_s;
        }

        public String getCategory_ids_s() {
            return category_ids_s;
        }

        public String getSubject_ids_s() {
            return subject_ids_s;
        }

        public String getCover_url() {
            return cover_url;
        }

        public String getCreated_at() {
            return created_at;
        }

        public UserInfoEntity getUser_info() {
            return user_info;
        }

        public String getIs_love() {
            return is_love;
        }

        public String getIs_favorite() {
            return is_favorite;
        }

        public List<String> getCategory_ids() {
            return category_ids;
        }

        public List<String> getTags() {
            return tags;
        }

        public List<ProductEntity> getProduct() {
            return product;
        }

        public List<String> getSubject_ids() {
            return subject_ids;
        }

        public SceneEntity getScene() {
            return scene;
        }

        public List<CommentsEntity> getComments() {
            return comments;
        }

        public static class SceneEntity implements Serializable {
            private String _id;
            private String title;

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String get_id() {
                return _id;
            }

            public void set_id(String _id) {
                this._id = _id;
            }
        }

        public static class LocationEntity implements Serializable {
            /**
             * type : Point
             * coordinates : [0,0]
             */

            private String type;
            private List<String> coordinates;

            public void setType(String type) {
                this.type = type;
            }

            public void setCoordinates(List<String> coordinates) {
                this.coordinates = coordinates;
            }

            public String getType() {
                return type;
            }

            public List<String> getCoordinates() {
                return coordinates;
            }
        }

        public static class UserInfoEntity implements Serializable {
            /**
             * user_id : 924912
             * nickname : Test_android
             * avatar_url : https://s3.taihuoniao.com/images/deavatar/002-m.jpg
             * summary : null
             * counter : {"message_count":0,"notice_count":0,"alert_count":0,"fans_count":2,"comment_count":0,"people_count":0,"fiu_alert_count":7,"fiu_comment_count":1,"fiu_notice_count":0,"sight_love_count":0,"order_wait_payment":3,"order_ready_goods":2,"order_sended_goods":1,"order_evaluate":0,"fiu_bonus_count":4}
             * follow_count : 13
             * fans_count : 1
             * love_count : 0
             * is_expert : 0
             * label : 
             * expert_label : 
             * expert_info : 
             * is_follow : 0
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
            private String is_follow;

            public void setUser_id(String user_id) {
                this.user_id = user_id;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
            }

            public void setAvatar_url(String avatar_url) {
                this.avatar_url = avatar_url;
            }

            public void setSummary(String summary) {
                this.summary = summary;
            }

            public void setCounter(CounterEntity counter) {
                this.counter = counter;
            }

            public void setFollow_count(String follow_count) {
                this.follow_count = follow_count;
            }

            public void setFans_count(String fans_count) {
                this.fans_count = fans_count;
            }

            public void setLove_count(String love_count) {
                this.love_count = love_count;
            }

            public void setIs_expert(String is_expert) {
                this.is_expert = is_expert;
            }

            public void setLabel(String label) {
                this.label = label;
            }

            public void setExpert_label(String expert_label) {
                this.expert_label = expert_label;
            }

            public void setExpert_info(String expert_info) {
                this.expert_info = expert_info;
            }

            public void setIs_follow(String is_follow) {
                this.is_follow = is_follow;
            }

            public String getUser_id() {
                return user_id;
            }

            public String getNickname() {
                return nickname;
            }

            public String getAvatar_url() {
                return avatar_url;
            }

            public String getSummary() {
                return summary;
            }

            public CounterEntity getCounter() {
                return counter;
            }

            public String getFollow_count() {
                return follow_count;
            }

            public String getFans_count() {
                return fans_count;
            }

            public String getLove_count() {
                return love_count;
            }

            public String getIs_expert() {
                return is_expert;
            }

            public String getLabel() {
                return label;
            }

            public String getExpert_label() {
                return expert_label;
            }

            public String getExpert_info() {
                return expert_info;
            }

            public String getIs_follow() {
                return is_follow;
            }

            public static class CounterEntity implements Serializable {
                /**
                 * message_count : 0
                 * notice_count : 0
                 * alert_count : 0
                 * fans_count : 2
                 * comment_count : 0
                 * people_count : 0
                 * fiu_alert_count : 7
                 * fiu_comment_count : 1
                 * fiu_notice_count : 0
                 * sight_love_count : 0
                 * order_wait_payment : 3
                 * order_ready_goods : 2
                 * order_sended_goods : 1
                 * order_evaluate : 0
                 * fiu_bonus_count : 4
                 */

                private String message_count;
                private String notice_count;
                private String alert_count;
                private String fans_count;
                private String comment_count;
                private String people_count;
                private String fiu_alert_count;
                private String fiu_comment_count;
                private String fiu_notice_count;
                private String sight_love_count;
                private String order_wait_payment;
                private String order_ready_goods;
                private String order_sended_goods;
                private String order_evaluate;
                private String fiu_bonus_count;

                public void setMessage_count(String message_count) {
                    this.message_count = message_count;
                }

                public void setNotice_count(String notice_count) {
                    this.notice_count = notice_count;
                }

                public void setAlert_count(String alert_count) {
                    this.alert_count = alert_count;
                }

                public void setFans_count(String fans_count) {
                    this.fans_count = fans_count;
                }

                public void setComment_count(String comment_count) {
                    this.comment_count = comment_count;
                }

                public void setPeople_count(String people_count) {
                    this.people_count = people_count;
                }

                public void setFiu_alert_count(String fiu_alert_count) {
                    this.fiu_alert_count = fiu_alert_count;
                }

                public void setFiu_comment_count(String fiu_comment_count) {
                    this.fiu_comment_count = fiu_comment_count;
                }

                public void setFiu_notice_count(String fiu_notice_count) {
                    this.fiu_notice_count = fiu_notice_count;
                }

                public void setSight_love_count(String sight_love_count) {
                    this.sight_love_count = sight_love_count;
                }

                public void setOrder_wait_payment(String order_wait_payment) {
                    this.order_wait_payment = order_wait_payment;
                }

                public void setOrder_ready_goods(String order_ready_goods) {
                    this.order_ready_goods = order_ready_goods;
                }

                public void setOrder_sended_goods(String order_sended_goods) {
                    this.order_sended_goods = order_sended_goods;
                }

                public void setOrder_evaluate(String order_evaluate) {
                    this.order_evaluate = order_evaluate;
                }

                public void setFiu_bonus_count(String fiu_bonus_count) {
                    this.fiu_bonus_count = fiu_bonus_count;
                }

                public String getMessage_count() {
                    return message_count;
                }

                public String getNotice_count() {
                    return notice_count;
                }

                public String getAlert_count() {
                    return alert_count;
                }

                public String getFans_count() {
                    return fans_count;
                }

                public String getComment_count() {
                    return comment_count;
                }

                public String getPeople_count() {
                    return people_count;
                }

                public String getFiu_alert_count() {
                    return fiu_alert_count;
                }

                public String getFiu_comment_count() {
                    return fiu_comment_count;
                }

                public String getFiu_notice_count() {
                    return fiu_notice_count;
                }

                public String getSight_love_count() {
                    return sight_love_count;
                }

                public String getOrder_wait_payment() {
                    return order_wait_payment;
                }

                public String getOrder_ready_goods() {
                    return order_ready_goods;
                }

                public String getOrder_sended_goods() {
                    return order_sended_goods;
                }

                public String getOrder_evaluate() {
                    return order_evaluate;
                }

                public String getFiu_bonus_count() {
                    return fiu_bonus_count;
                }
            }
        }

        public static class ProductEntity implements Serializable {
            /**
             * id : 1102511135
             * title : HC-7020B 高端工程级HDMI线
             * x : 0.35955293443468
             * y : 0.59118054707845
             * loc : 2
             * type : 2
             * price : 98
             */

            private String id;
            private String title;
            private double x;
            private double y;
            private String loc;
            private String type;
            private String price;

            public void setId(String id) {
                this.id = id;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public void setX(double x) {
                this.x = x;
            }

            public void setY(double y) {
                this.y = y;
            }

            public void setLoc(String loc) {
                this.loc = loc;
            }

            public void setType(String type) {
                this.type = type;
            }

            public void setPrice(String price) {
                this.price = price;
            }

            public String getId() {
                return id;
            }

            public String getTitle() {
                return title;
            }

            public double getX() {
                return x;
            }

            public double getY() {
                return y;
            }

            public String getLoc() {
                return loc;
            }

            public String getType() {
                return type;
            }

            public String getPrice() {
                return price;
            }
        }

        public static class CommentsEntity implements Serializable {
            /**
             * _id : 58ae8c743ffca2995e8b4571
             * content :  风风光光 vv
             * user_id : 924893
             * user_nickname : 微信用户董永胜8157
             * user_avatar_url : https://p4.taihuoniao.com/avatar/160914/57d925d83ffca25c018b45e6-avn.jpg
             */

            private String _id;
            private String content;
            private String user_id;
            private String user_nickname;
            private String user_avatar_url;

            public void set_id(String _id) {
                this._id = _id;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public void setUser_id(String user_id) {
                this.user_id = user_id;
            }

            public void setUser_nickname(String user_nickname) {
                this.user_nickname = user_nickname;
            }

            public void setUser_avatar_url(String user_avatar_url) {
                this.user_avatar_url = user_avatar_url;
            }

            public String get_id() {
                return _id;
            }

            public String getContent() {
                return content;
            }

            public String getUser_id() {
                return user_id;
            }

            public String getUser_nickname() {
                return user_nickname;
            }

            public String getUser_avatar_url() {
                return user_avatar_url;
            }
        }
    }
}
