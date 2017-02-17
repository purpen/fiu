package com.taihuoniao.fineix.home.beans;

import java.util.List;

/**
 * Created by Stephen on 2017/2/16 17:05
 * Email: 895745843@qq.com
 */

public class CategoryListBean {


    /**
     * total_rows : 6
     * rows : [{"_id":85,"title":"体验店","name":"scene_tyd","gid":0,"pid":83,"order_by":0,"sub_count":0,"tag_id":0,"domain":12,"total_count":1,"reply_count":0,"app_cover_url":null,"tags":[{}],"back_url":"https://p4.taihuoniao.com/asset/170213/58a1260120de8d73298b6fd1-1-hu.jpg","stick":0,"sub_categories":[{}]}]
     * total_page : 1
     * current_page : 1
     * pager : 
     * next_page : 0
     * prev_page : 0
     * current_user_id : 924912
     */

    private String total_rows;
    private String total_page;
    private String current_page;
    private String pager;
    private String next_page;
    private String prev_page;
    private String current_user_id;
    private List<RowsEntity> rows;

    public void setTotal_rows(String total_rows) {
        this.total_rows = total_rows;
    }

    public void setTotal_page(String total_page) {
        this.total_page = total_page;
    }

    public void setCurrent_page(String current_page) {
        this.current_page = current_page;
    }

    public void setPager(String pager) {
        this.pager = pager;
    }

    public void setNext_page(String next_page) {
        this.next_page = next_page;
    }

    public void setPrev_page(String prev_page) {
        this.prev_page = prev_page;
    }

    public void setCurrent_user_id(String current_user_id) {
        this.current_user_id = current_user_id;
    }

    public void setRows(List<RowsEntity> rows) {
        this.rows = rows;
    }

    public String getTotal_rows() {
        return total_rows;
    }

    public String getTotal_page() {
        return total_page;
    }

    public String getCurrent_page() {
        return current_page;
    }

    public String getPager() {
        return pager;
    }

    public String getNext_page() {
        return next_page;
    }

    public String getPrev_page() {
        return prev_page;
    }

    public String getCurrent_user_id() {
        return current_user_id;
    }

    public List<RowsEntity> getRows() {
        return rows;
    }

    public static class RowsEntity {
        /**
         * _id : 85
         * title : 体验店
         * name : scene_tyd
         * gid : 0
         * pid : 83
         * order_by : 0
         * sub_count : 0
         * tag_id : 0
         * domain : 12
         * total_count : 1
         * reply_count : 0
         * app_cover_url : null
         * tags : [{}]
         * back_url : https://p4.taihuoniao.com/asset/170213/58a1260120de8d73298b6fd1-1-hu.jpg
         * stick : 0
         * sub_categories : [{}]
         */

        private String _id;
        private String title;
        private String name;
        private String gid;
        private String pid;
        private String order_by;
        private String sub_count;
        private String tag_id;
        private String domain;
        private String total_count;
        private String reply_count;
        private String app_cover_url;
        private String back_url;
        private String stick;
//        private List<TagsEntity> tags;
//        private List<SubCategoriesEntity> sub_categories;

        public void set_id(String _id) {
            this._id = _id;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setGid(String gid) {
            this.gid = gid;
        }

        public void setPid(String pid) {
            this.pid = pid;
        }

        public void setOrder_by(String order_by) {
            this.order_by = order_by;
        }

        public void setSub_count(String sub_count) {
            this.sub_count = sub_count;
        }

        public void setTag_id(String tag_id) {
            this.tag_id = tag_id;
        }

        public void setDomain(String domain) {
            this.domain = domain;
        }

        public void setTotal_count(String total_count) {
            this.total_count = total_count;
        }

        public void setReply_count(String reply_count) {
            this.reply_count = reply_count;
        }

        public void setApp_cover_url(String app_cover_url) {
            this.app_cover_url = app_cover_url;
        }

        public void setBack_url(String back_url) {
            this.back_url = back_url;
        }

        public void setStick(String stick) {
            this.stick = stick;
        }

//        public void setTags(List<TagsEntity> tags) {
//            this.tags = tags;
//        }
//
//        public void setSub_categories(List<SubCategoriesEntity> sub_categories) {
//            this.sub_categories = sub_categories;
//        }

        public String get_id() {
            return _id;
        }

        public String getTitle() {
            return title;
        }

        public String getName() {
            return name;
        }

        public String getGid() {
            return gid;
        }

        public String getPid() {
            return pid;
        }

        public String getOrder_by() {
            return order_by;
        }

        public String getSub_count() {
            return sub_count;
        }

        public String getTag_id() {
            return tag_id;
        }

        public String getDomain() {
            return domain;
        }

        public String getTotal_count() {
            return total_count;
        }

        public String getReply_count() {
            return reply_count;
        }

        public String getApp_cover_url() {
            return app_cover_url;
        }

        public String getBack_url() {
            return back_url;
        }

        public String getStick() {
            return stick;
        }

//        public List<TagsEntity> getTags() {
//            return tags;
//        }
//
//        public List<SubCategoriesEntity> getSub_categories() {
//            return sub_categories;
//        }
//
//        public static class TagsEntity {
//        }
//
//        public static class SubCategoriesEntity {
//        }
    }
}
