package com.taihuoniao.fineix.beans;

import com.taihuoniao.fineix.base.NetBean;

import java.util.List;

/**
 * Created by taihuoniao on 2016/5/4.
 */
public class LoveSceneBean extends NetBean {
    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public static class Data {
        private List<LoveSceneItem> rows;

        public List<LoveSceneItem> getRows() {
            return rows;
        }

        public void setRows(List<LoveSceneItem> rows) {
            this.rows = rows;
        }
    }

    public static class LoveSceneItem {
        private String _id;
        private String title;
        private String address;
        private String view_count;
        private String love_count;
        private String cover_url;
        private String created_at;
        private String scene_title;
        private User user_info;

        public String getScene_title() {
            return scene_title;
        }

        public void setScene_title(String scene_title) {
            this.scene_title = scene_title;
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

        public String getLove_count() {
            return love_count;
        }

        public void setLove_count(String love_count) {
            this.love_count = love_count;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public User getUser_info() {
            return user_info;
        }

        public void setUser_info(User user_info) {
            this.user_info = user_info;
        }

        public String getView_count() {
            return view_count;
        }

        public void setView_count(String view_count) {
            this.view_count = view_count;
        }
    }

    public static class User {
        private String nickname;
        private String avatar_url;
        private String is_expert;
        private String summary;

        public String getAvatar_ur() {
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

        public String getIs_expert() {
            return is_expert;
        }

        public String getAvatar_url() {
            return avatar_url;
        }

        public void setIs_expert(String is_expert) {
            this.is_expert = is_expert;
        }
    }
}
