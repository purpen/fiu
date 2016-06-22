package com.taihuoniao.fineix.beans;

import com.taihuoniao.fineix.base.NetBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by taihuoniao on 2016/4/25.
 */
public class QingjingDetailBean extends NetBean implements Serializable {
    private Data data;

    @Override
    public String toString() {
        return "QingjingDetailBean{" +
                "data=" + data +
                '}';
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public static class Data implements Serializable{
        private String _id;
        private String title;
        private String user_id;
        private String des;
        private List<Integer> tags;
        private String address;
        private String used_count;
        private String view_count;
        private int subscription_count;
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

        @Override
        public String toString() {
            return "Data{" +
                    "_id='" + _id + '\'' +
                    ", title='" + title + '\'' +
                    ", user_id='" + user_id + '\'' +
                    ", des='" + des + '\'' +
                    ", tags=" + tags +
                    ", address='" + address + '\'' +
                    ", used_count='" + used_count + '\'' +
                    ", view_count='" + view_count + '\'' +
                    ", subscription_count=" + subscription_count +
                    ", love_count='" + love_count + '\'' +
                    ", comment_count='" + comment_count + '\'' +
                    ", is_check='" + is_check + '\'' +
                    ", stick='" + stick + '\'' +
                    ", status='" + status + '\'' +
                    ", created_on='" + created_on + '\'' +
                    ", updated_on='" + updated_on + '\'' +
                    ", cover_url='" + cover_url + '\'' +
                    ", created_at='" + created_at + '\'' +
                    ", user_info=" + user_info +
                    ", tag_titles=" + tag_titles +
                    ", is_subscript=" + is_subscript +
                    ", location=" + location +
                    '}';
        }

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

        public int getSubscription_count() {
            return subscription_count;
        }

        public void setSubscription_count(int subscription_count) {
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
        private Location location;

        public Location getLocation() {
            return location;
        }

        public void setLocation(Location location) {
            this.location = location;
        }
    }

    public static class Location implements Serializable{
        private List<Double> coordinates;

        @Override
        public String toString() {
            return "Location{" +
                    "coordinates=" + coordinates +
                    '}';
        }

        public List<Double> getCoordinates() {
            return coordinates;
        }

        public void setCoordinates(List<Double> coordinates) {
            this.coordinates = coordinates;
        }
    }

    public static class UserInfo implements Serializable{
        private String user_id;
        private String nickname;
        private String avatar_url;
        private String summary;
        private int is_expert;
        private String expert_label;
        private String expert_info;

        public int getIs_expert() {
            return is_expert;
        }

        public String getExpert_info() {
            return expert_info;
        }

        public void setExpert_info(String expert_info) {
            this.expert_info = expert_info;
        }

        public String getExpert_label() {
            return expert_label;
        }

        public void setExpert_label(String expert_label) {
            this.expert_label = expert_label;
        }

        public void setIs_expert(int is_expert) {
            this.is_expert = is_expert;
        }

        @Override
        public String toString() {
            return "UserInfo{" +
                    "avatar_url='" + avatar_url + '\'' +
                    ", user_id='" + user_id + '\'' +
                    ", nickname='" + nickname + '\'' +
                    ", summary='" + summary + '\'' +
                    ", is_expert='" + is_expert + '\'' +
                    '}';
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
