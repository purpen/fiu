package com.taihuoniao.fineix.beans;

import java.io.Serializable;
import java.util.List;

/**
 * Created by taihuoniao on 2016/4/13.
 */
public class CategoryListBean extends NetBean implements Serializable {
    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public static class Data implements Serializable {
        private List<CategoryListItem> rows;

        public List<CategoryListItem> getRows() {
            return rows;
        }

        public void setRows(List<CategoryListItem> rows) {
            this.rows = rows;
        }
    }

    public static class CategoryListItem implements Serializable {
        private boolean isSelect;
        private String _id;
        private String title;
        private String name;
        private String gid;
        private String pid;
        private String sub_count;
        private String total_count;
        private String reply_count;
        private String tag_id;
        private String domain;
        private String app_cover_url;
        private String order_by;
        private List<String> tags;
        private String app_cover_s_url;
        private String back_url;

        public boolean isSelect() {
            return isSelect;
        }

        public void setIsSelect(boolean isSelect) {
            this.isSelect = isSelect;
        }

        public String getBack_url() {
            return back_url;
        }

        public void setBack_url(String back_url) {
            this.back_url = back_url;
        }

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

        public String getApp_cover_s_url() {
            return app_cover_s_url;
        }

        public void setApp_cover_s_url(String app_cover_s_url) {
            this.app_cover_s_url = app_cover_s_url;
        }

        public String getApp_cover_url() {
            return app_cover_url;
        }

        public void setApp_cover_url(String app_cover_url) {
            this.app_cover_url = app_cover_url;
        }

        public String getDomain() {
            return domain;
        }

        public void setDomain(String domain) {
            this.domain = domain;
        }

        public String getGid() {
            return gid;
        }

        public void setGid(String gid) {
            this.gid = gid;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getOrder_by() {
            return order_by;
        }

        public void setOrder_by(String order_by) {
            this.order_by = order_by;
        }

        public String getPid() {
            return pid;
        }

        public void setPid(String pid) {
            this.pid = pid;
        }

        public String getReply_count() {
            return reply_count;
        }

        public void setReply_count(String reply_count) {
            this.reply_count = reply_count;
        }

        public String getSub_count() {
            return sub_count;
        }

        public void setSub_count(String sub_count) {
            this.sub_count = sub_count;
        }

        public String getTag_id() {
            return tag_id;
        }

        public void setTag_id(String tag_id) {
            this.tag_id = tag_id;
        }

        public List<String> getTags() {
            return tags;
        }

        public void setTags(List<String> tags) {
            this.tags = tags;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getTotal_count() {
            return total_count;
        }

        public void setTotal_count(String total_count) {
            this.total_count = total_count;
        }
    }
}
