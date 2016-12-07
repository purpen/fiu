package com.taihuoniao.fineix.beans;

import java.io.Serializable;
import java.util.List;

/**
 * Created by taihuoniao on 2016/4/21.
 */
public class QingJingListBean extends NetBean {
    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public static class Data {
        private List<QingJingItem> rows;

        public List<QingJingItem> getRows() {
            return rows;
        }

        public void setRows(List<QingJingItem> rows) {
            this.rows = rows;
        }
    }

    public static class QingJingItem implements Serializable {
        public boolean isOrdered;
        private boolean isSelect;
        private String _id;
        private String title;
        private String des;
        private List<Integer> sight;
        private List<String> tags;
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
        private String cover_url;

        @Override
        public String toString() {
            return "QingJingItem{" +
                    "title='" + title + '\'' +
                    '}';
        }

        public boolean isSelect() {
            return isSelect;
        }

        public void setIsSelect(boolean isSelect) {
            this.isSelect = isSelect;
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

        public String getCreated_on() {
            return created_on;
        }

        public void setCreated_on(String created_on) {
            this.created_on = created_on;
        }

        public String getDes() {
            return des;
        }

        public void setDes(String des) {
            this.des = des;
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
    }
}
