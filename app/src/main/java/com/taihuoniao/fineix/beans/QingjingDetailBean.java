package com.taihuoniao.fineix.beans;

import java.io.Serializable;
import java.util.List;

/**
 * Created by taihuoniao on 2016/4/25.
 */
public class QingjingDetailBean extends NetBean implements Serializable {


    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private int _id;
        private String title;
        private int user_id;
        private String des;
        /**
         * type : Point
         * coordinates : [116.467016,39.933717]
         */

        private LocationBean location;
        private String address;
        private int used_count;
        private int view_count;
        private int subscription_count;
        private int love_count;
        private int comment_count;
        private int sight_count;
        private int true_view_count;
        private int web_view_count;
        private int wap_view_count;
        private int app_view_count;
        private int is_check;
        private int stick;
        private int fine;
        private int status;
        private int deleted;
        private int created_on;
        private int updated_on;
        private String tags_s;
        private String cover_url;
        private String created_at;
        /**
         * user_id : 544066
         * nickname : 太火鸟社区组
         * avatar_url : http://frbird.qiniudn.com/avatar/150518/5559d439fc8b12f2358c3cf3-avb.jpg
         * summary : 太火鸟社区官方账号，发布最新活动信息，快来关注我吧！
         * counter : {"message_count":5,"notice_count":0,"alert_count":1,"fans_count":4,"comment_count":0,"people_count":0,"fiu_alert_count":11}
         * follow_count : 7
         * fans_count : 368
         * love_count : 0
         * is_expert : 0
         * label :
         * expert_label :
         * expert_info :
         */

        private UserInfoBean user_info;
        private int is_subscript;
        private int current_user_id;
        private List<String> tags;
        private List<String> old_tags;
        private List<String> tag_titles;

        public int get_id() {
            return _id;
        }

        public void set_id(int _id) {
            this._id = _id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }

        public String getDes() {
            return des;
        }

        public void setDes(String des) {
            this.des = des;
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

        public int getUsed_count() {
            return used_count;
        }

        public void setUsed_count(int used_count) {
            this.used_count = used_count;
        }

        public int getView_count() {
            return view_count;
        }

        public void setView_count(int view_count) {
            this.view_count = view_count;
        }

        public int getSubscription_count() {
            return subscription_count;
        }

        public void setSubscription_count(int subscription_count) {
            this.subscription_count = subscription_count;
        }

        public int getLove_count() {
            return love_count;
        }

        public void setLove_count(int love_count) {
            this.love_count = love_count;
        }

        public int getComment_count() {
            return comment_count;
        }

        public void setComment_count(int comment_count) {
            this.comment_count = comment_count;
        }

        public int getSight_count() {
            return sight_count;
        }

        public void setSight_count(int sight_count) {
            this.sight_count = sight_count;
        }

        public int getTrue_view_count() {
            return true_view_count;
        }

        public void setTrue_view_count(int true_view_count) {
            this.true_view_count = true_view_count;
        }

        public int getWeb_view_count() {
            return web_view_count;
        }

        public void setWeb_view_count(int web_view_count) {
            this.web_view_count = web_view_count;
        }

        public int getWap_view_count() {
            return wap_view_count;
        }

        public void setWap_view_count(int wap_view_count) {
            this.wap_view_count = wap_view_count;
        }

        public int getApp_view_count() {
            return app_view_count;
        }

        public void setApp_view_count(int app_view_count) {
            this.app_view_count = app_view_count;
        }

        public int getIs_check() {
            return is_check;
        }

        public void setIs_check(int is_check) {
            this.is_check = is_check;
        }

        public int getStick() {
            return stick;
        }

        public void setStick(int stick) {
            this.stick = stick;
        }

        public int getFine() {
            return fine;
        }

        public void setFine(int fine) {
            this.fine = fine;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getDeleted() {
            return deleted;
        }

        public void setDeleted(int deleted) {
            this.deleted = deleted;
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

        public int getIs_subscript() {
            return is_subscript;
        }

        public void setIs_subscript(int is_subscript) {
            this.is_subscript = is_subscript;
        }

        public int getCurrent_user_id() {
            return current_user_id;
        }

        public void setCurrent_user_id(int current_user_id) {
            this.current_user_id = current_user_id;
        }

        public List<String> getTags() {
            return tags;
        }

        public void setTags(List<String> tags) {
            this.tags = tags;
        }

        public List<String> getOld_tags() {
            return old_tags;
        }

        public void setOld_tags(List<String> old_tags) {
            this.old_tags = old_tags;
        }

        public List<String> getTag_titles() {
            return tag_titles;
        }

        public void setTag_titles(List<String> tag_titles) {
            this.tag_titles = tag_titles;
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
            private int user_id;
            private String nickname;
            private String avatar_url;
            private String summary;
            /**
             * message_count : 5
             * notice_count : 0
             * alert_count : 1
             * fans_count : 4
             * comment_count : 0
             * people_count : 0
             * fiu_alert_count : 11
             */

            private CounterBean counter;
            private int follow_count;
            private int fans_count;
            private int love_count;
            private int is_expert;
            private String label;
            private String expert_label;
            private String expert_info;

            public int getUser_id() {
                return user_id;
            }

            public void setUser_id(int user_id) {
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

            public int getFollow_count() {
                return follow_count;
            }

            public void setFollow_count(int follow_count) {
                this.follow_count = follow_count;
            }

            public int getFans_count() {
                return fans_count;
            }

            public void setFans_count(int fans_count) {
                this.fans_count = fans_count;
            }

            public int getLove_count() {
                return love_count;
            }

            public void setLove_count(int love_count) {
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

            public static class CounterBean {
                private int message_count;
                private int notice_count;
                private int alert_count;
                private int fans_count;
                private int comment_count;
                private int people_count;
                private int fiu_alert_count;

                public int getMessage_count() {
                    return message_count;
                }

                public void setMessage_count(int message_count) {
                    this.message_count = message_count;
                }

                public int getNotice_count() {
                    return notice_count;
                }

                public void setNotice_count(int notice_count) {
                    this.notice_count = notice_count;
                }

                public int getAlert_count() {
                    return alert_count;
                }

                public void setAlert_count(int alert_count) {
                    this.alert_count = alert_count;
                }

                public int getFans_count() {
                    return fans_count;
                }

                public void setFans_count(int fans_count) {
                    this.fans_count = fans_count;
                }

                public int getComment_count() {
                    return comment_count;
                }

                public void setComment_count(int comment_count) {
                    this.comment_count = comment_count;
                }

                public int getPeople_count() {
                    return people_count;
                }

                public void setPeople_count(int people_count) {
                    this.people_count = people_count;
                }

                public int getFiu_alert_count() {
                    return fiu_alert_count;
                }

                public void setFiu_alert_count(int fiu_alert_count) {
                    this.fiu_alert_count = fiu_alert_count;
                }
            }
        }
    }
}
