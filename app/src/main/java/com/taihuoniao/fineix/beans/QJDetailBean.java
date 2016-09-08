package com.taihuoniao.fineix.beans;

import com.taihuoniao.fineix.base.NetBean;

import java.util.List;

/**
 * Created by taihuoniao on 2016/8/25.
 */
public class QJDetailBean extends NetBean {

    /**
     * is_error : false
     * data : {"_id":448,"title":"重新遇见大海","user_id":1122324,"des":"看眼前这水天一色的遥远大海，我很想自己能像一只飞鸟一样在海面上飞行，这样能更清楚的看到海上的一切。看茫茫大海，正在我脚下，喜悦如浪花袭入我的心间，徜徉于海面上，行走在海天之间，看海上壮观场面，看海上行走的鱼舟，看海上飞翔的海鸥，感觉此时此刻的自己，已和天，海合为一体了。","scene_id":40,"category_id":0,"tags":["舒畅","欢愉"],"product":[{"id":1002,"title":"AutoBot eye 智能行车记录仪","price":499,"x":0,"y":0.71302083333333}],"location":{"type":"Point","coordinates":[0,0]},"city":"添加情景地点","address":"添加情景地点","used_count":0,"view_count":20,"love_count":0,"comment_count":0,"true_view_count":7,"web_view_count":0,"wap_view_count":0,"app_view_count":7,"stick":0,"fine":0,"is_check":1,"status":1,"deleted":0,"created_on":1469971585,"updated_on":1469971585,"tags_s":"舒畅,欢愉","subject_ids_s":"","created_at":"2016-07-31","user_info":{"user_id":1122324,"nickname":"阿森森林","avatar_url":"http://frbird.qiniudn.com/avatar/160729/579b03e7c0007670748b4ea9-avm.jpg","summary":"没有没有没有。。。","counter":{"message_count":0,"notice_count":0,"alert_count":0,"fans_count":1,"comment_count":0,"people_count":0,"fiu_alert_count":0,"fiu_comment_count":0,"fiu_notice_count":0,"sight_love_count":0,"order_wait_payment":0,"order_ready_goods":0,"order_sended_goods":1,"order_evaluate":0},"follow_count":0,"fans_count":1,"love_count":0,"is_expert":0,"label":"设计师","expert_label":"","expert_info":"","is_follow":0},"cover_url":"http://frbird.qiniudn.com/scene_sight/160731/579dfc7fc00076a9038b45e5-hu.jpg","is_favorite":0,"is_love":0,"comments":[],"current_user_id":955832}
     */

    private boolean is_error;
    /**
     * _id : 448
     * title : 重新遇见大海
     * user_id : 1122324
     * des : 看眼前这水天一色的遥远大海，我很想自己能像一只飞鸟一样在海面上飞行，这样能更清楚的看到海上的一切。看茫茫大海，正在我脚下，喜悦如浪花袭入我的心间，徜徉于海面上，行走在海天之间，看海上壮观场面，看海上行走的鱼舟，看海上飞翔的海鸥，感觉此时此刻的自己，已和天，海合为一体了。
     * scene_id : 40
     * category_id : 0
     * tags : ["舒畅","欢愉"]
     * product : [{"id":1002,"title":"AutoBot eye 智能行车记录仪","price":499,"x":0,"y":0.71302083333333}]
     * location : {"type":"Point","coordinates":[0,0]}
     * city : 添加情景地点
     * address : 添加情景地点
     * used_count : 0
     * view_count : 20
     * love_count : 0
     * comment_count : 0
     * true_view_count : 7
     * web_view_count : 0
     * wap_view_count : 0
     * app_view_count : 7
     * stick : 0
     * fine : 0
     * is_check : 1
     * status : 1
     * deleted : 0
     * created_on : 1469971585
     * updated_on : 1469971585
     * tags_s : 舒畅,欢愉
     * subject_ids_s :
     * created_at : 2016-07-31
     * user_info : {"user_id":1122324,"nickname":"阿森森林","avatar_url":"http://frbird.qiniudn.com/avatar/160729/579b03e7c0007670748b4ea9-avm.jpg","summary":"没有没有没有。。。","counter":{"message_count":0,"notice_count":0,"alert_count":0,"fans_count":1,"comment_count":0,"people_count":0,"fiu_alert_count":0,"fiu_comment_count":0,"fiu_notice_count":0,"sight_love_count":0,"order_wait_payment":0,"order_ready_goods":0,"order_sended_goods":1,"order_evaluate":0},"follow_count":0,"fans_count":1,"love_count":0,"is_expert":0,"label":"设计师","expert_label":"","expert_info":"","is_follow":0}
     * cover_url : http://frbird.qiniudn.com/scene_sight/160731/579dfc7fc00076a9038b45e5-hu.jpg
     * is_favorite : 0
     * is_love : 0
     * comments : []
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

    public static class DataBean {
        private String _id;
        private String title;
        private String user_id;
        private String des;
        private String scene_id;
        private String category_id;
        /**
         * type : Point
         * coordinates : [0,0]
         */

        private LocationBean location;
        private String city;
        private String address;
        private String used_count;
        private String view_count;
        private String love_count;
        private int comment_count;
        private String true_view_count;
        private String web_view_count;
        private String wap_view_count;
        private String app_view_count;
        private String stick;
        private String fine;
        private String is_check;
        private String status;
        private String deleted;
        private String created_on;
        private String updated_on;
        private String tags_s;
        private String subject_ids_s;
        private String created_at;
        /**
         * user_id : 1122324
         * nickname : 阿森森林
         * avatar_url : http://frbird.qiniudn.com/avatar/160729/579b03e7c0007670748b4ea9-avm.jpg
         * summary : 没有没有没有。。。
         * counter : {"message_count":0,"notice_count":0,"alert_count":0,"fans_count":1,"comment_count":0,"people_count":0,"fiu_alert_count":0,"fiu_comment_count":0,"fiu_notice_count":0,"sight_love_count":0,"order_wait_payment":0,"order_ready_goods":0,"order_sended_goods":1,"order_evaluate":0}
         * follow_count : 0
         * fans_count : 1
         * love_count : 0
         * is_expert : 0
         * label : 设计师
         * expert_label :
         * expert_info :
         * is_follow : 0
         */

        private UserInfoBean user_info;
        private String cover_url;
        private int is_favorite;
        private int is_love;
        private String current_user_id;
        private List<String> tags;
        /**
         * id : 1002
         * title : AutoBot eye 智能行车记录仪
         * price : 499
         * x : 0
         * y : 0.71302083333333
         */

        private List<SceneList.DataBean.RowsBean.ProductBean> product;
        private List<SceneList.DataBean.RowsBean.CommentsBean> comments;

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getDes() {
            return des;
        }

        public void setDes(String des) {
            this.des = des;
        }

        public String getScene_id() {
            return scene_id;
        }

        public void setScene_id(String scene_id) {
            this.scene_id = scene_id;
        }

        public String getCategory_id() {
            return category_id;
        }

        public void setCategory_id(String category_id) {
            this.category_id = category_id;
        }

        public LocationBean getLocation() {
            return location;
        }

        public void setLocation(LocationBean location) {
            this.location = location;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getUsed_count() {
            return used_count;
        }

        public void setUsed_count(String used_count) {
            this.used_count = used_count;
        }

        public String getView_count() {
            return view_count;
        }

        public void setView_count(String view_count) {
            this.view_count = view_count;
        }

        public String getLove_count() {
            return love_count;
        }

        public void setLove_count(String love_count) {
            this.love_count = love_count;
        }

        public int getComment_count() {
            return comment_count;
        }

        public void setComment_count(int comment_count) {
            this.comment_count = comment_count;
        }

        public String getTrue_view_count() {
            return true_view_count;
        }

        public void setTrue_view_count(String true_view_count) {
            this.true_view_count = true_view_count;
        }

        public String getWeb_view_count() {
            return web_view_count;
        }

        public void setWeb_view_count(String web_view_count) {
            this.web_view_count = web_view_count;
        }

        public String getWap_view_count() {
            return wap_view_count;
        }

        public void setWap_view_count(String wap_view_count) {
            this.wap_view_count = wap_view_count;
        }

        public String getApp_view_count() {
            return app_view_count;
        }

        public void setApp_view_count(String app_view_count) {
            this.app_view_count = app_view_count;
        }

        public String getStick() {
            return stick;
        }

        public void setStick(String stick) {
            this.stick = stick;
        }

        public String getFine() {
            return fine;
        }

        public void setFine(String fine) {
            this.fine = fine;
        }

        public String getIs_check() {
            return is_check;
        }

        public void setIs_check(String is_check) {
            this.is_check = is_check;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getDeleted() {
            return deleted;
        }

        public void setDeleted(String deleted) {
            this.deleted = deleted;
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

        public String getTags_s() {
            return tags_s;
        }

        public void setTags_s(String tags_s) {
            this.tags_s = tags_s;
        }

        public String getSubject_ids_s() {
            return subject_ids_s;
        }

        public void setSubject_ids_s(String subject_ids_s) {
            this.subject_ids_s = subject_ids_s;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public UserInfoBean getUser_info() {
            return user_info;
        }

        public void setUser_info(UserInfoBean user_info) {
            this.user_info = user_info;
        }

        public String getCover_url() {
            return cover_url;
        }

        public void setCover_url(String cover_url) {
            this.cover_url = cover_url;
        }

        public int getIs_favorite() {
            return is_favorite;
        }

        public void setIs_favorite(int is_favorite) {
            this.is_favorite = is_favorite;
        }

        public int getIs_love() {
            return is_love;
        }

        public void setIs_love(int is_love) {
            this.is_love = is_love;
        }

        public String getCurrent_user_id() {
            return current_user_id;
        }

        public void setCurrent_user_id(String current_user_id) {
            this.current_user_id = current_user_id;
        }

        public List<String> getTags() {
            return tags;
        }

        public void setTags(List<String> tags) {
            this.tags = tags;
        }

        public List<SceneList.DataBean.RowsBean.ProductBean> getProduct() {
            return product;
        }

        public void setProduct(List<SceneList.DataBean.RowsBean.ProductBean> product) {
            this.product = product;
        }

        public List<SceneList.DataBean.RowsBean.CommentsBean> getComments() {
            return comments;
        }

        public void setComments(List<SceneList.DataBean.RowsBean.CommentsBean> comments) {
            this.comments = comments;
        }

        public static class LocationBean {
            private String type;
            private List<Double> coordinates;

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public List<Double> getCoordinates() {
                return coordinates;
            }

            public void setCoordinates(List<Double> coordinates) {
                this.coordinates = coordinates;
            }
        }

        public static class UserInfoBean {
            private String user_id;
            private String nickname;
            private String avatar_url;
            private String summary;
            /**
             * message_count : 0
             * notice_count : 0
             * alert_count : 0
             * fans_count : 1
             * comment_count : 0
             * people_count : 0
             * fiu_alert_count : 0
             * fiu_comment_count : 0
             * fiu_notice_count : 0
             * sight_love_count : 0
             * order_wait_payment : 0
             * order_ready_goods : 0
             * order_sended_goods : 1
             * order_evaluate : 0
             */

            private CounterBean counter;
            private String follow_count;
            private String fans_count;
            private String love_count;
            private int is_expert;
            private String label;
            private String expert_label;
            private String expert_info;
            private int is_follow;

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

            public CounterBean getCounter() {
                return counter;
            }

            public void setCounter(CounterBean counter) {
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

            public int getIs_expert() {
                return is_expert;
            }

            public void setIs_expert(int is_expert) {
                this.is_expert = is_expert;
            }

            public int getIs_follow() {
                return is_follow;
            }

            public void setIs_follow(int is_follow) {
                this.is_follow = is_follow;
            }

            public static class CounterBean {
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

                public String getFiu_alert_count() {
                    return fiu_alert_count;
                }

                public void setFiu_alert_count(String fiu_alert_count) {
                    this.fiu_alert_count = fiu_alert_count;
                }

                public String getFiu_comment_count() {
                    return fiu_comment_count;
                }

                public void setFiu_comment_count(String fiu_comment_count) {
                    this.fiu_comment_count = fiu_comment_count;
                }

                public String getFiu_notice_count() {
                    return fiu_notice_count;
                }

                public void setFiu_notice_count(String fiu_notice_count) {
                    this.fiu_notice_count = fiu_notice_count;
                }

                public String getSight_love_count() {
                    return sight_love_count;
                }

                public void setSight_love_count(String sight_love_count) {
                    this.sight_love_count = sight_love_count;
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
