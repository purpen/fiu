package com.taihuoniao.fineix.beans;

import java.util.List;

/**
 * Created by Stephen on 2017/4/12 17:23
 * Email: 895745843@qq.com
 */

public class BrandListBean {

    /**
     * total_rows : 1
     * rows : [{"_id":"56cff82316c149a0066d5648","title":"小米","des":"小米156156","mark":"a","used_count":0,"status":1,"from_to":2,"self_run":1,"kind":1,"item_count":100,"created_on":1456470051,"updated_on":1461146043,"stick":"0","cover_url":"http://frbird.qiniudn.com/scene_brands/160226/56cff89116c149a0066d5649-hu.jpg"}]
     * total_page : 1
     * current_page : 1
     * pager : 
     * next_page : 0
     * prev_page : 0
     * current_user_id : 0
     */

    private String total_rows;
    private String total_page;
    private String current_page;
    private String pager;
    private String next_page;
    private String prev_page;
    private String current_user_id;
    private List<RowsEntity> rows;

    public String getTotal_rows() {
        return total_rows;
    }

    public void setTotal_rows(String total_rows) {
        this.total_rows = total_rows;
    }

    public String getTotal_page() {
        return total_page;
    }

    public void setTotal_page(String total_page) {
        this.total_page = total_page;
    }

    public String getCurrent_page() {
        return current_page;
    }

    public void setCurrent_page(String current_page) {
        this.current_page = current_page;
    }

    public String getPager() {
        return pager;
    }

    public void setPager(String pager) {
        this.pager = pager;
    }

    public String getNext_page() {
        return next_page;
    }

    public void setNext_page(String next_page) {
        this.next_page = next_page;
    }

    public String getPrev_page() {
        return prev_page;
    }

    public void setPrev_page(String prev_page) {
        this.prev_page = prev_page;
    }

    public String getCurrent_user_id() {
        return current_user_id;
    }

    public void setCurrent_user_id(String current_user_id) {
        this.current_user_id = current_user_id;
    }

    public List<RowsEntity> getRows() {
        return rows;
    }

    public void setRows(List<RowsEntity> rows) {
        this.rows = rows;
    }

    public static class RowsEntity {
        /**
         * _id : 56cff82316c149a0066d5648
         * title : 小米
         * des : 小米156156
         * mark : a
         * used_count : 0
         * status : 1
         * from_to : 2
         * self_run : 1
         * kind : 1
         * item_count : 100
         * created_on : 1456470051
         * updated_on : 1461146043
         * stick : 0
         * cover_url : http://frbird.qiniudn.com/scene_brands/160226/56cff89116c149a0066d5649-hu.jpg
         */

        private String _id;
        private String title;
        private String des;
        private String mark;
        private String used_count;
        private String status;
        private String from_to;
        private String self_run;
        private String kind;
        private String item_count;
        private String created_on;
        private String updated_on;
        private String stick;
        private String cover_url;

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDes() {
            return des;
        }

        public void setDes(String des) {
            this.des = des;
        }

        public String getMark() {
            return mark;
        }

        public void setMark(String mark) {
            this.mark = mark;
        }

        public String getUsed_count() {
            return used_count;
        }

        public void setUsed_count(String used_count) {
            this.used_count = used_count;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getFrom_to() {
            return from_to;
        }

        public void setFrom_to(String from_to) {
            this.from_to = from_to;
        }

        public String getSelf_run() {
            return self_run;
        }

        public void setSelf_run(String self_run) {
            this.self_run = self_run;
        }

        public String getKind() {
            return kind;
        }

        public void setKind(String kind) {
            this.kind = kind;
        }

        public String getItem_count() {
            return item_count;
        }

        public void setItem_count(String item_count) {
            this.item_count = item_count;
        }

        public String getCreated_on() {
            return created_on;
        }

        public void setCreated_on(String created_on) {
            this.created_on = created_on;
        }

        public String getUpdated_on() {
            return updated_on;
        }

        public void setUpdated_on(String updated_on) {
            this.updated_on = updated_on;
        }

        public String getStick() {
            return stick;
        }

        public void setStick(String stick) {
            this.stick = stick;
        }

        public String getCover_url() {
            return cover_url;
        }

        public void setCover_url(String cover_url) {
            this.cover_url = cover_url;
        }
    }
}
