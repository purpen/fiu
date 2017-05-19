package com.taihuoniao.fineix.beans;

import java.io.Serializable;
import java.util.List;

/**
 * Created by taihuoniao on 2016/4/13.
 */
public class CategoryListBean implements Serializable {

    /**
     * total_rows : 10
     * rows : [{"_id":32,"title":"先锋智能","name":"pioneer","gid":0,"pid":0,"order_by":1,"sub_count":35,"tag_id":0,"domain":1,"total_count":641,"reply_count":315,"app_cover_url":"https://p4.taihuoniao.com/asset/160127/56a8439a3ffca26a098bb055-9","tags":["黑科技"],"back_url":"https://p4.taihuoniao.com/asset/160901/57c79feafc8b12a6178b5d8b-1-hu.jpg","stick":0,"app_cover_s_url":"https://p4.taihuoniao.com/asset/160127/56a8439a3ffca26a098bb055-9-p325x200.jpg","sub_categories":[]},{"_id":31,"title":"数码电子","name":"digital","gid":0,"pid":0,"order_by":3,"sub_count":399,"tag_id":0,"domain":1,"total_count":1117,"reply_count":710,"app_cover_url":"https://p4.taihuoniao.com/asset/160127/56a8439a3ffca26a098bb055-10","tags":["机器人","数码配件","影音娱乐","摄影摄像","生活电器"],"back_url":"https://p4.taihuoniao.com/asset/160901/57c79feafc8b12a6178b5d8b-5-hu.jpg","stick":0,"app_cover_s_url":"https://p4.taihuoniao.com/asset/160127/56a8439a3ffca26a098bb055-10-p325x200.jpg","sub_categories":[]},{"_id":34,"title":"户外出行","name":"outdoor","gid":0,"pid":0,"order_by":5,"sub_count":91,"tag_id":0,"domain":1,"total_count":275,"reply_count":192,"app_cover_url":"https://p4.taihuoniao.com/asset/160127/56a8439a3ffca26a098bb055-8","tags":["无人机","运动相机","出行工具","车载设备","户外装备"],"back_url":"https://p4.taihuoniao.com/asset/160901/57c79feafc8b12a6178b5d8b-9-hu.jpg","stick":0,"app_cover_s_url":"https://p4.taihuoniao.com/asset/160127/56a8439a3ffca26a098bb055-8-p325x200.jpg","sub_categories":[]},{"_id":76,"title":"运动健康","name":"sports","gid":0,"pid":0,"order_by":7,"sub_count":57,"tag_id":0,"domain":1,"total_count":230,"reply_count":55,"app_cover_url":"https://p4.taihuoniao.com/asset/160202/56b04f38fc8b12fb018b79e3-1","tags":["骑行运动","智能穿戴","个护健康","健身训练"],"back_url":"https://p4.taihuoniao.com/asset/160901/57c79feafc8b12a6178b5d8b-2-hu.jpg","stick":0,"app_cover_s_url":"https://p4.taihuoniao.com/asset/160202/56b04f38fc8b12fb018b79e3-1-p325x200.jpg","sub_categories":[]},{"_id":33,"title":"文创玩品","name":"originality","gid":0,"pid":0,"order_by":9,"sub_count":86,"tag_id":0,"domain":1,"total_count":106,"reply_count":126,"app_cover_url":"https://p4.taihuoniao.com/asset/160202/56b04c68fc8b1206028b7a3b-1","tags":["礼品文具","创意玩具","首饰配饰"],"back_url":"https://p4.taihuoniao.com/asset/160901/57c79feafc8b12a6178b5d8b-4-hu.jpg","stick":0,"app_cover_s_url":"https://p4.taihuoniao.com/asset/160202/56b04c68fc8b1206028b7a3b-1-p325x200.jpg","sub_categories":[]},{"_id":30,"title":"先锋设计","name":"design","gid":0,"pid":0,"order_by":11,"sub_count":32,"tag_id":0,"domain":1,"total_count":266,"reply_count":268,"app_cover_url":"https://p4.taihuoniao.com/asset/160202/56b04ea4fc8b12fa018b7a99-1","tags":["红设计"],"back_url":"https://p4.taihuoniao.com/asset/160901/57c79feafc8b12a6178b5d8b-3-hu.jpg","stick":0,"app_cover_s_url":"https://p4.taihuoniao.com/asset/160202/56b04ea4fc8b12fa018b7a99-1-p325x200.jpg","sub_categories":[]},{"_id":81,"title":"家居日用","name":"household","gid":0,"pid":0,"order_by":13,"sub_count":196,"tag_id":0,"domain":1,"total_count":1029,"reply_count":146,"app_cover_url":"https://p4.taihuoniao.com/asset/160127/56a8439a3ffca26a098bb055-2","tags":["茶具","灯具","摆件","箱包","日杂","家具"],"back_url":"https://p4.taihuoniao.com/asset/160901/57c79feafc8b12a6178b5d8b-8-hu.jpg","stick":0,"app_cover_s_url":"https://p4.taihuoniao.com/asset/160127/56a8439a3ffca26a098bb055-2-p325x200.jpg","sub_categories":[]},{"_id":82,"title":"厨房卫浴","name":"bathroom","gid":0,"pid":0,"order_by":15,"sub_count":78,"tag_id":0,"domain":1,"total_count":265,"reply_count":134,"app_cover_url":"https://p4.taihuoniao.com/asset/160127/56a8439a3ffca26a098bb055-1","tags":["智能牙刷","厨房配件","水具酒具","厨房电器","浴室用品"],"back_url":"https://p4.taihuoniao.com/asset/160901/57c79feafc8b12a6178b5d8b-10-hu.jpg","stick":0,"app_cover_s_url":"https://p4.taihuoniao.com/asset/160127/56a8439a3ffca26a098bb055-1-p325x200.jpg","sub_categories":[]},{"_id":78,"title":"母婴成长","name":"growth","gid":0,"pid":0,"order_by":17,"sub_count":20,"tag_id":0,"domain":1,"total_count":71,"reply_count":26,"app_cover_url":"https://p4.taihuoniao.com/asset/160127/56a8439a3ffca26a098bb055-5","tags":["电子教育","喂养用品"],"back_url":"https://p4.taihuoniao.com/asset/160901/57c79feafc8b12a6178b5d8b-7-hu.jpg","stick":0,"app_cover_s_url":"https://p4.taihuoniao.com/asset/160127/56a8439a3ffca26a098bb055-5-p325x200.jpg","sub_categories":[]},{"_id":79,"title":"品质饮食","name":"drink","gid":0,"pid":0,"order_by":19,"sub_count":12,"tag_id":0,"domain":1,"total_count":12,"reply_count":8,"app_cover_url":"https://p4.taihuoniao.com/asset/160127/56a8439a3ffca26a098bb055-6","tags":["休闲食品","酒水饮料","茶叶茶点"],"back_url":"https://p4.taihuoniao.com/asset/160901/57c79feafc8b12a6178b5d8b-6-hu.jpg","stick":0,"app_cover_s_url":"https://p4.taihuoniao.com/asset/160127/56a8439a3ffca26a098bb055-6-p325x200.jpg","sub_categories":[]}]
     * total_page : 1
     * current_page : 1
     * pager : 
     * next_page : 0
     * prev_page : 0
     * current_user_id : 1136552
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

    public static class RowsEntity implements Serializable {
        /**
         * _id : 32
         * title : 先锋智能
         * name : pioneer
         * gid : 0
         * pid : 0
         * order_by : 1
         * sub_count : 35
         * tag_id : 0
         * domain : 1
         * total_count : 641
         * reply_count : 315
         * app_cover_url : https://p4.taihuoniao.com/asset/160127/56a8439a3ffca26a098bb055-9
         * tags : ["黑科技"]
         * back_url : https://p4.taihuoniao.com/asset/160901/57c79feafc8b12a6178b5d8b-1-hu.jpg
         * stick : 0
         * app_cover_s_url : https://p4.taihuoniao.com/asset/160127/56a8439a3ffca26a098bb055-9-p325x200.jpg
         * sub_categories : []
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
        private String app_cover_s_url;
        private List<String> tags;
        private List<String> sub_categories;
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

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getGid() {
            return gid;
        }

        public void setGid(String gid) {
            this.gid = gid;
        }

        public String getPid() {
            return pid;
        }

        public void setPid(String pid) {
            this.pid = pid;
        }

        public String getOrder_by() {
            return order_by;
        }

        public void setOrder_by(String order_by) {
            this.order_by = order_by;
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

        public String getDomain() {
            return domain;
        }

        public void setDomain(String domain) {
            this.domain = domain;
        }

        public String getTotal_count() {
            return total_count;
        }

        public void setTotal_count(String total_count) {
            this.total_count = total_count;
        }

        public String getReply_count() {
            return reply_count;
        }

        public void setReply_count(String reply_count) {
            this.reply_count = reply_count;
        }

        public String getApp_cover_url() {
            return app_cover_url;
        }

        public void setApp_cover_url(String app_cover_url) {
            this.app_cover_url = app_cover_url;
        }

        public String getBack_url() {
            return back_url;
        }

        public void setBack_url(String back_url) {
            this.back_url = back_url;
        }

        public String getStick() {
            return stick;
        }

        public void setStick(String stick) {
            this.stick = stick;
        }

        public String getApp_cover_s_url() {
            return app_cover_s_url;
        }

        public void setApp_cover_s_url(String app_cover_s_url) {
            this.app_cover_s_url = app_cover_s_url;
        }

        public List<String> getTags() {
            return tags;
        }

        public void setTags(List<String> tags) {
            this.tags = tags;
        }

        public List<String> getSub_categories() {
            return sub_categories;
        }

        public void setSub_categories(List<String> sub_categories) {
            this.sub_categories = sub_categories;
        }
    }
}
