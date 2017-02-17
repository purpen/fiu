package com.taihuoniao.fineix.zone.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lilin on 2017/2/15.
 */

public class ZoneDetailBean implements Serializable{

    /**
     * _id : 62
     * title : D3IN
     * sub_title : D3IN线下体验店
     * avatar_url : https://p4.taihuoniao.com/scene_scene/170214/58a25ec73ffca202478b47dc-1-p500x500.jpg
     * created_at : 昨天
     * user : {"_id":10,"nickname":"太火鸟","avatar_url":"https://p4.taihuoniao.com/avatar/160321/56efa2e33ffca269098c0846-avb.jpg","is_expert":0,"label":"","expert_label":"","expert_info":""}
     * love_count : 0
     * view_count : 4
     * city :
     * address : 北京 798艺术区
     * location : {"type":"Point","coordinates":[116.506495,39.992067]}
     * is_love : 0
     * banners : ["https://p4.taihuoniao.com/scene_scene/170214/58a25ec73ffca202478b47db-4-p750x422.jpg","https://p4.taihuoniao.com/scene_scene/170214/58a25ec73ffca202478b47db-3-p750x422.jpg","https://p4.taihuoniao.com/scene_scene/170214/58a25ec73ffca202478b47db-2-p750x422.jpg"]
     * tags : ["智能硬件","体验店","798艺术区"]
     * extra : {"shop_hours":"早8:00--晚9:00","tel":"010-12345678"}
     * score_average : 5
     * sights : []
     * products : []
     * current_user_id : 20448
     */

    public String _id;
    public String title;
    public String sub_title;
    public String avatar_url;
    public String created_at;
    public UserBean user;
    public int love_count;
    public int view_count;
    public String des;
    public String city;
    public String address;
    public LocationBean location;
    public int is_love;
    public ExtraBean extra;
    public int score_average;
    public int current_user_id;
    public List<String> banners;
    public List<String> tags;
    public List<?> sights;
    public List<?> products;
    public List<String> bright_spot;
    public static class UserBean {
        /**
         * _id : 10
         * nickname : 太火鸟
         * avatar_url : https://p4.taihuoniao.com/avatar/160321/56efa2e33ffca269098c0846-avb.jpg
         * is_expert : 0
         * label :
         * expert_label :
         * expert_info :
         */

        public int _id;
        public String nickname;
        public String avatar_url;
        public int is_expert;
        public String label;
        public String expert_label;
        public String expert_info;
    }

    public static class LocationBean implements Serializable{
        /**
         * type : Point
         * coordinates : [116.506495,39.992067]
         */

        public String type;
        public List<Double> coordinates;
    }

    public static class ExtraBean implements Serializable{
        /**
         * shop_hours : 早8:00--晚9:00
         * tel : 010-12345678
         */

        public String shop_hours;
        public String tel;
    }
}
