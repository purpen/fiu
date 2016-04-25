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

}
