package com.taihuoniao.fineix.beans;

import com.taihuoniao.fineix.base.NetBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by taihuoniao on 2016/4/18.
 */
public class SceneList extends NetBean implements Serializable {

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean implements Serializable {
        private int current_user_id;

        private List<RowsBean> rows;

        public int getCurrent_user_id() {
            return current_user_id;
        }

        public void setCurrent_user_id(int current_user_id) {
            this.current_user_id = current_user_id;
        }

        public List<RowsBean> getRows() {
            return rows;
        }

        public void setRows(List<RowsBean> rows) {
            this.rows = rows;
        }

        public static class RowsBean implements Serializable {
            private String _id;
            private String title;
            private String user_id;
            private String des;
            private String scene_id;

            private LocationBean location;
            private String address;
            private String used_count;
            private String view_count;
            private String love_count;
            private String comment_count;
            private String fine;
            private String is_check;
            private String status;
            private String created_on;
            private String updated_on;
            private String deleted;
            private String tags_s;
            private String cover_url;
            private String created_at;
            private String scene_title;
            /**
             * user_id : 924807
             * nickname : 183****0981
             * avatar_url : http://frstatic.qiniudn.com/images/deavatar/007-m.jpg
             * summary : null
             * counter : {"message_count":0,"notice_count":2,"alert_count":9,"fans_count":12,"comment_count":0,"people_count":0,"fiu_notice_count":6,"fiu_alert_count":4,"fiu_comment_count":21,"order_wait_payment":2,"order_ready_goods":0}
             * follow_count : 0
             * fans_count : 7
             * love_count : 0
             * is_expert : 0
             * label :
             * expert_label :
             * expert_info :
             */

            private UserInfoBean user_info;
            private List<String> tags;
            /**
             * id : 0
             * title :
             * price : 0
             * x : 0
             * y : 0
             */

            private List<ProductBean> product;
            /**
             * _id : 57a016e63ffca2da538b4911
             * content : 有人给面子。在于
             * user_id : 719877
             * user_nickname : Fynn
             * user_avatar_url : http://frbird.qiniudn.com/avatar/160420/5716ff563ffca2e3108bcf89-avn.jpg
             */

            private List<CommentsBean> comments;

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

            public LocationBean getLocation() {
                return location;
            }

            public void setLocation(LocationBean location) {
                this.location = location;
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

            public String getComment_count() {
                return comment_count;
            }

            public void setComment_count(String comment_count) {
                this.comment_count = comment_count;
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

            public String getDeleted() {
                return deleted;
            }

            public void setDeleted(String deleted) {
                this.deleted = deleted;
            }

            public String getTags_s() {
                return tags_s;
            }

            public void setTags_s(String tags_s) {
                this.tags_s = tags_s;
            }

            public String getCover_url() {
                return cover_url;
            }

            public void setCover_url(String cover_url) {
                this.cover_url = cover_url;
            }

            public String getCreated_at() {
                return created_at;
            }

            public void setCreated_at(String created_at) {
                this.created_at = created_at;
            }

            public String getScene_title() {
                return scene_title;
            }

            public void setScene_title(String scene_title) {
                this.scene_title = scene_title;
            }

            public UserInfoBean getUser_info() {
                return user_info;
            }

            public void setUser_info(UserInfoBean user_info) {
                this.user_info = user_info;
            }

            public List<String> getTags() {
                return tags;
            }

            public void setTags(List<String> tags) {
                this.tags = tags;
            }

            public List<ProductBean> getProduct() {
                return product;
            }

            public void setProduct(List<ProductBean> product) {
                this.product = product;
            }

            public List<CommentsBean> getComments() {
                return comments;
            }

            public void setComments(List<CommentsBean> comments) {
                this.comments = comments;
            }

            public static class LocationBean implements Serializable {
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

            public static class UserInfoBean implements Serializable {
                private String user_id;
                private String nickname;
                private String avatar_url;
                private Object summary;
                /**
                 * message_count : 0
                 * notice_count : 2
                 * alert_count : 9
                 * fans_count : 12
                 * comment_count : 0
                 * people_count : 0
                 * fiu_notice_count : 6
                 * fiu_alert_count : 4
                 * fiu_comment_count : 21
                 * order_wait_payment : 2
                 * order_ready_goods : 0
                 */

                private CounterBean counter;
                private String follow_count;
                private String fans_count;
                private String love_count;
                private int is_expert;
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

                public Object getSummary() {
                    return summary;
                }

                public void setSummary(Object summary) {
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

                public int getIs_expert() {
                    return is_expert;
                }

                public void setIs_expert(int is_expert) {
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

                public static class CounterBean implements Serializable {
                    private String message_count;
                    private String notice_count;
                    private String alert_count;
                    private String fans_count;
                    private String comment_count;
                    private String people_count;
                    private String fiu_notice_count;
                    private String fiu_alert_count;
                    private String fiu_comment_count;
                    private String order_wait_payment;
                    private String order_ready_goods;

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

                    public String getFiu_notice_count() {
                        return fiu_notice_count;
                    }

                    public void setFiu_notice_count(String fiu_notice_count) {
                        this.fiu_notice_count = fiu_notice_count;
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
                }
            }

            public static class ProductBean implements Serializable {
                private String id;
                private String title;
                private double price;
                private double x;
                private double y;

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
                }

                public String getTitle() {
                    return title;
                }

                public void setTitle(String title) {
                    this.title = title;
                }

                public double getPrice() {
                    return price;
                }

                public void setPrice(double price) {
                    this.price = price;
                }

                public double getX() {
                    return x;
                }

                public void setX(double x) {
                    this.x = x;
                }

                public double getY() {
                    return y;
                }

                public void setY(double y) {
                    this.y = y;
                }
            }

            public static class CommentsBean implements Serializable {
                private String _id;
                private String content;
                private String user_id;
                private String user_nickname;
                private String user_avatar_url;

                public String get_id() {
                    return _id;
                }

                public void set_id(String _id) {
                    this._id = _id;
                }

                public String getContent() {
                    return content;
                }

                public void setContent(String content) {
                    this.content = content;
                }

                public String getUser_id() {
                    return user_id;
                }

                public void setUser_id(String user_id) {
                    this.user_id = user_id;
                }

                public String getUser_nickname() {
                    return user_nickname;
                }

                public void setUser_nickname(String user_nickname) {
                    this.user_nickname = user_nickname;
                }

                public String getUser_avatar_url() {
                    return user_avatar_url;
                }

                public void setUser_avatar_url(String user_avatar_url) {
                    this.user_avatar_url = user_avatar_url;
                }
            }
        }
    }
}
