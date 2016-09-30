package com.taihuoniao.fineix.beans;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lilin
 *         created at 2016/6/15 23:33
 */
public class ActivityPrizeData{


    public String _id;
    public String title;
    public String short_title;
    public String tags_s;
    public String kind;
    public int evt;
    public String attend_count;
    public String type;
    public String cover_id;
    public String category_id;
    public String summary;
    public String status;
    public String publish;
    public String user_id;
    public String stick;
    public String fine;
    public String love_count;
    public String favorite_count;
    public String view_count;
    public String comment_count;
    public String share_count;
    public String begin_time;
    public String end_time;
    public String product_id;
    public String prize_sight_ids;
    public String cover_url;
    public String banner_url;
    public Object product;
    public String begin_time_at;
    public String end_time_at;
    public String is_love;
    public String content_view_url;
    public String share_view_url;
    public String share_desc;
    public String current_user_id;
    public List<String> tags;
    /**
     * _id : 433
     * title : Go to it
     * cover_url : http://frbird.qiniudn.com/scene_sight/160927/57e9e0b23ffca248418b51ac-hu.jpg
     * created_at : 4天前
     * address : 北京798艺术区
     * city : 北京市
     * location : {"type":"PoString","coordinates":[116.50180750169,39.989746705423]}
     * product : []
     * prize : 五等奖
     * user : {"_id":924789,"nickname":"铁锤","avatar_url":"http://frbird.qiniudn.com/avatar/160511/5732f0393ffca28a0c8b51af-avm.jpg","is_expert":0,"label":0,"expert_label":"","expert_info":"","is_follow":0}
     */

    public List<SightsEntity> sights;
    /**
     * prize : 一等奖
     * data : [{"_id":383,"title":"Gotoit","cover_url":"http://frbird.qiniudn.com/scene_sight/160819/57b6a27c3ffca266518b473d-hu.jpg","created_at":"2016-08-19","address":"751D·PARK北京时尚设计广场","city":"北京市","location":{"type":"PoString","coordinates":[116.506497,39.992428]},"product":[{"id":304,"title":"华米手环","x":0.144,"y":0.672,"loc":2}],"user":{"_id":924819,"nickname":"公民","avatar_url":"http://frbird.qiniudn.com/avatar/160524/574444453ffca24a598b57f9-avm.jpg","is_expert":0,"label":0,"expert_label":"","expert_info":"","is_follow":0}}]
     */

    public ArrayList<PrizeSightsEntity> prize_sights;

    public static class SightsEntity {
        public String _id;
        public String title;
        public String cover_url;
        public String created_at;
        public String address;
        public String city;
        /**
         * type : PoString
         * coordinates : [116.50180750169,39.989746705423]
         */

        public LocationEntity location;
        public String prize;
        /**
         * _id : 924789
         * nickname : 铁锤
         * avatar_url : http://frbird.qiniudn.com/avatar/160511/5732f0393ffca28a0c8b51af-avm.jpg
         * is_expert : 0
         * label : 0
         * expert_label : 
         * expert_info : 
         * is_follow : 0
         */

        public UserEntity user;
        public List<?> product;

        public static class LocationEntity {
            public String type;
            public List<Double> coordinates;
        }

        public static class UserEntity {
            public String _id;
            public String nickname;
            public String avatar_url;
            public String is_expert;
            public String label;
            public String expert_label;
            public String expert_info;
            public String is_follow;
        }
    }

    public static class PrizeSightsEntity {
        public String prize;
        /**
         * _id : 383
         * title : Gotoit
         * cover_url : http://frbird.qiniudn.com/scene_sight/160819/57b6a27c3ffca266518b473d-hu.jpg
         * created_at : 2016-08-19
         * address : 751D·PARK北京时尚设计广场
         * city : 北京市
         * location : {"type":"PoString","coordinates":[116.506497,39.992428]}
         * product : [{"id":304,"title":"华米手环","x":0.144,"y":0.672,"loc":2}]
         * user : {"_id":924819,"nickname":"公民","avatar_url":"http://frbird.qiniudn.com/avatar/160524/574444453ffca24a598b57f9-avm.jpg","is_expert":0,"label":0,"expert_label":"","expert_info":"","is_follow":0}
         */

        public List<DataEntity> data;

        public static class DataEntity {
            public String _id;
            public String title;
            public String cover_url;
            public String created_at;
            public String address;
            public String city;
            public String prizeGrade;
            public int prizeNum;
            public boolean flagHead;
            /**
             * type : PoString
             * coordinates : [116.506497,39.992428]
             */

            public LocationEntity location;
            /**
             * _id : 924819
             * nickname : 公民
             * avatar_url : http://frbird.qiniudn.com/avatar/160524/574444453ffca24a598b57f9-avm.jpg
             * is_expert : 0
             * label : 0
             * expert_label : 
             * expert_info : 
             * is_follow : 0
             */

            public UserEntity user;
            /**
             * id : 304
             * title : 华米手环
             * x : 0.144
             * y : 0.672
             * loc : 2
             */

            public List<ProductEntity> product;

            public static class LocationEntity {
                public String type;
                public List<Double> coordinates;
            }

            public static class UserEntity {
                public String _id;
                public String nickname;
                public String avatar_url;
                public int is_expert;
                public String label;
                public String expert_label;
                public String expert_info;
                public int is_follow;
            }

            public static class ProductEntity {
                public String id;
                public String title;
                public double x;
                public double y;
                public int loc;
            }
        }
    }
}
