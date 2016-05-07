package com.taihuoniao.fineix.beans;

import com.taihuoniao.fineix.base.NetBean;

import java.util.List;

/**
 * Created by taihuoniao on 2016/5/7.
 */
public class SearchBean extends NetBean {
    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public static class Data {
        private List<SearchItem> rows;

        public List<SearchItem> getRows() {
            return rows;
        }

        public void setRows(List<SearchItem> rows) {
            this.rows = rows;
        }
    }

    public static class SearchItem {
        private String _id;
        private String cover_url;
        private String title;
        private String address;
        private User user_info;
        private String view_count;
        private String love_count;
        private String scene_title;
        private String created_at;
        private List<String> banners;
        private String sale_price;
        private String attrbute;

        public String getAttrbute() {
            return attrbute;
        }

        public void setAttrbute(String attrbute) {
            this.attrbute = attrbute;
        }

        public List<String> getBanners() {
            return banners;
        }

        public void setBanners(List<String> banners) {
            this.banners = banners;
        }

        public String getSale_price() {
            return sale_price;
        }

        public void setSale_price(String sale_price) {
            this.sale_price = sale_price;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public String getLove_count() {
            return love_count;
        }

        public void setLove_count(String love_count) {
            this.love_count = love_count;
        }

        public String getScene_title() {
            return scene_title;
        }

        public void setScene_title(String scene_title) {
            this.scene_title = scene_title;
        }

        public String getView_count() {
            return view_count;
        }

        public void setView_count(String view_count) {
            this.view_count = view_count;
        }

        public User getUser_info() {
            return user_info;
        }

        public void setUser_info(User user_info) {
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

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }

    public static class User {
        private String user_id;
        private String nickname;
        private String avatar_url;
        private String summary;
        private String is_expert;

        public String getAvatar_url() {
            return avatar_url;
        }

        public void setAvatar_url(String avatar_url) {
            this.avatar_url = avatar_url;
        }

        public String getIs_expert() {
            return is_expert;
        }

        public void setIs_expert(String is_expert) {
            this.is_expert = is_expert;
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
