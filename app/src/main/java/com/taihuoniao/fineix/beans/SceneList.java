package com.taihuoniao.fineix.beans;

import com.taihuoniao.fineix.base.NetBean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by taihuoniao on 2016/4/18.
 */
public class SceneList extends NetBean implements Serializable {
    private boolean is_error;
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
        private int total_rows;
        private int total_page;
        private int current_page;
        private String pager;
        private int next_page;
        private int prev_page;
        private String current_user_id;
        private List<RowsBean> rows;

        public int getTotal_rows() {
            return total_rows;
        }

        public void setTotal_rows(int total_rows) {
            this.total_rows = total_rows;
        }

        public int getTotal_page() {
            return total_page;
        }

        public void setTotal_page(int total_page) {
            this.total_page = total_page;
        }

        public int getCurrent_page() {
            return current_page;
        }

        public void setCurrent_page(int current_page) {
            this.current_page = current_page;
        }

        public String getPager() {
            return pager;
        }

        public void setPager(String pager) {
            this.pager = pager;
        }

        public int getNext_page() {
            return next_page;
        }

        public void setNext_page(int next_page) {
            this.next_page = next_page;
        }

        public int getPrev_page() {
            return prev_page;
        }

        public void setPrev_page(int prev_page) {
            this.prev_page = prev_page;
        }

        public String getCurrent_user_id() {
            return current_user_id;
        }

        public void setCurrent_user_id(String current_user_id) {
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
            private String category_id;
            private List<String> tags;

            private LocationBean location;
            private String city;
            private String address;
            private String used_count;
            private String view_count;
            private String love_count;
            private int comment_count;
            private int stick;//0未推荐 1已推荐
            private int fine;//0 未精选 1已精选
            private int is_check;// 1 没有屏蔽 0屏蔽
            private String status;
            private String deleted;
            private String created_on;
            private String updated_on;
            private String tags_s;
            private String cover_url;
            private String created_at;

            private UserInfoBean user_info;
            private int is_love;
            private int is_favorite;
            private List<String> category_ids;
            private List<ProductBean> product;
            private List<CommentsBean> comments;

            public int getIs_favorite() {
                return is_favorite;
            }

            public void setIs_favorite(int is_favorite) {
                this.is_favorite = is_favorite;
            }

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

            public List<String> getTags() {
                return tags;
            }

            public void setTags(List<String> tags) {
                this.tags = tags;
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

            public int getStick() {
                return stick;
            }

            public void setStick(int stick) {
                this.stick = stick;
            }

            public int getIs_check() {
                return is_check;
            }

            public void setIs_check(int is_check) {
                this.is_check = is_check;
            }

            public int getFine() {
                return fine;
            }

            public void setFine(int fine) {
                this.fine = fine;
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

            public UserInfoBean getUser_info() {
                return user_info;
            }

            public void setUser_info(UserInfoBean user_info) {
                this.user_info = user_info;
            }

            public int getIs_love() {
                return is_love;
            }

            public void setIs_love(int is_love) {
                this.is_love = is_love;
            }

            public List<String> getCategory_ids() {
                return category_ids;
            }

            public void setCategory_ids(List<String> category_ids) {
                this.category_ids = category_ids;
            }

            public List<ProductBean> getProduct() {
                if (product == null) {
                    product = new ArrayList<>();
                }
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
                private String summary;

                private CounterBean counter;
                private String follow_count;
                private String fans_count;
                private String love_count;
                private int is_expert;
                private int is_follow;
                private String label;
                private String expert_label;
                private String expert_info;

                public int getIs_follow() {
                    return is_follow;
                }

                public void setIs_follow(int is_follow) {
                    this.is_follow = is_follow;
                }

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
                    private String order_wait_payment;
                    private String order_ready_goods;
                    private String order_sended_goods;
                    private String order_evaluate;
                    private String fiu_comment_count;
                    private String fiu_alert_count;
                    private String fiu_notice_count;

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

                    public String getFiu_notice_count() {
                        return fiu_notice_count;
                    }

                    public void setFiu_notice_count(String fiu_notice_count) {
                        this.fiu_notice_count = fiu_notice_count;
                    }
                }
            }

            public static class ProductBean implements Serializable {
                private String id;
                private String title;
                private double x;
                private double y;
                private int loc;//1 点在右边 2点在左边
//                private int type;//1 自建 2产品

//                public int getType() {
//                    return type;
//                }
//
//                public void setType(int type) {
//                    this.type = type;
//                }

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

                public int getLoc() {
                    return loc;
                }

                public void setLoc(int loc) {
                    this.loc = loc;
                }
            }
        }
    }
}
