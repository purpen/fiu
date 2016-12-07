package com.taihuoniao.fineix.beans;

import java.util.List;

/**
 * Created by taihuoniao on 2016/5/4.
 */
public class UserListBean extends NetBean {
    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public static class Data {
        private List<UserListItem> rows;

        public List<UserListItem> getRows() {
            return rows;
        }

        public void setRows(List<UserListItem> rows) {
            this.rows = rows;
        }
    }

    public static class UserListItem {
        private String _id;
        private String medium_avatar_url;
        private String avatar_size_type;

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

        public String getAvatar_size_type() {
            return avatar_size_type;
        }

        public void setAvatar_size_type(String avatar_size_type) {
            this.avatar_size_type = avatar_size_type;
        }

        public String getMedium_avatar_url() {
            return medium_avatar_url;
        }

        public void setMedium_avatar_url(String medium_avatar_url) {
            this.medium_avatar_url = medium_avatar_url;
        }
    }
}
