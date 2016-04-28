package com.taihuoniao.fineix.beans;

import com.taihuoniao.fineix.base.NetBean;

import java.util.List;

/**
 * Created by taihuoniao on 2016/4/25.
 */
public class QingjingDetailBean extends NetBean {
    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public static class Data {
        private String _id;
        private String title;
        private String user_id;
        private String des;
        private List<Integer> tags;
        private String address;
        private String used_count;
        private String view_count;
        private String subscription_count;
        private String love_count;
        private String comment_count;
        private String is_check;
        private String stick;
        private String status;
        private String created_on;
        private String updated_on;
        private String cover_url;
        private String created_at;
        private UserInfo user_info;
        private List<String> tag_titles;
        private int is_subscript;

        public int getIs_subscript() {
            return is_subscript;
        }

        public void setIs_subscript(int is_subscript) {
            this.is_subscript = is_subscript;
        }

        public List<String> getTag_titles() {
            return tag_titles;
        }

        public void setTag_titles(List<String> tag_titles) {
            this.tag_titles = tag_titles;
        }

        public UserInfo getUser_info() {
            return user_info;
        }

        public void setUser_info(UserInfo user_info) {
            this.user_info = user_info;
        }

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
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

        public String getDes() {
            return des;
        }

        public void setDes(String des) {
            this.des = des;
        }

        public String getSubscription_count() {
            return subscription_count;
        }

        public void setSubscription_count(String subscription_count) {
            this.subscription_count = subscription_count;
        }

        public List<Integer> getTags() {
            return tags;
        }

        public void setTags(List<Integer> tags) {
            this.tags = tags;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }

    public static class UserInfo {
        private String user_id;
        private String nickname;
        private String avatar_url;
        private String summary;
        private String user_rank;

        public String getUser_rank() {
            return user_rank;
        }

        public void setUser_rank(String user_rank) {
            this.user_rank = user_rank;
        }

        public String getAvatar_url() {
            return avatar_url;
        }

        public void setAvatar_url(String avatar_url) {
            this.avatar_url = avatar_url;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getSummary() {
            return summary;
        }

        public void setSummary(String summary) {
            this.summary = summary;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }
    }


}
