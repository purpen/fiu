package com.taihuoniao.fineix.beans;

import com.taihuoniao.fineix.base.NetBean;

import java.util.List;

/**
 * Created by taihuoniao on 2016/5/11.
 */
public class FiuUserListBean extends NetBean {
    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public static class Data {
        private List<FiuUserItem> users;

        public List<FiuUserItem> getUsers() {
            return users;
        }

        public void setUsers(List<FiuUserItem> users) {
            this.users = users;
        }
    }

    public static class FiuUserItem {
        private String _id;
        private String medium_avatar_url;

        public String getMedium_avatar_url() {
            return medium_avatar_url;
        }

        public void setMedium_avatar_url(String medium_avatar_url) {
            this.medium_avatar_url = medium_avatar_url;
        }

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }
    }
}
