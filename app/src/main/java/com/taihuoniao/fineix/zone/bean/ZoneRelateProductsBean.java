package com.taihuoniao.fineix.zone.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lilin on 2017/2/16.
 */

public class ZoneRelateProductsBean implements Serializable{

    /**
     * total_rows : 2
     * rows : [{"_id":"58a531b93ffca2840d8b4567","scene_id":62,"product_id":1011497002,"status":1,"created_on":1487221177,"updated_on":1487221177,"product":{"_id":1011497002,"title":"华米手环","short_title":"华米手环","oid":null,"sale_price":0.1,"market_price":299,"brand_id":"","kind":null,"cover_id":"56a093dd3ffca26e098ba8f3","category_id":0,"fid":null,"summary":"产品是指能够提供给市场，被人们使用和消费，并能满足人们某种需求的任何东西，包括有形的物品、无形的服务、组织、观念或它们的组合。产品一般可以分为三个层次，即核心产品、形式产品、延伸产品。","link":null,"stick":0,"fine":null,"banner_asset_ids":null,"asset_ids":null,"category_tags":["运动健康"],"view_count":2243,"favorite_count":5,"love_count":3,"comment_count":19,"buy_count":null,"deleted":0,"published":1,"attrbute":null,"state":0,"tags":["test"],"tags_s":"test","created_on":1453364461,"updated_on":1487212914,"created_at":null,"cover_url":"https://p4.taihuoniao.com/product/160121/56a0934f3ffca26c098ba90e-4-p500x500.jpg","banner_asset":[]},"scene":[]},{"_id":"58a531f33ffca2ba0d8b4567","scene_id":62,"product_id":1120677456,"status":1,"created_on":1487221235,"updated_on":1487221235,"product":{"_id":1120677456,"title":"爱牵挂智能腕表老人专用危情报警心率检测触摸按键智能提醒","short_title":"爱牵挂智能腕表老人专用危情报警心率检测触摸按键智能提醒","oid":null,"sale_price":998,"market_price":998,"brand_id":"","kind":null,"cover_id":"547da06e989a6a2c508b7fa8","category_id":30,"fid":null,"summary":"爱牵挂！为父母考虑最周全的智能腕表！首款专为老人设计的智能腕表，具有紧急呼救、跌倒判定、心率监测、定位服务以及亲情沟通等核心功能。","link":null,"stick":0,"fine":null,"banner_asset_ids":null,"asset_ids":null,"category_tags":[""],"view_count":661,"favorite_count":2,"love_count":5,"comment_count":2,"buy_count":null,"deleted":0,"published":1,"attrbute":null,"state":0,"tags":["心率检测","智能提醒","睡眠监测","计步"],"tags_s":"心率检测,智能提醒,睡眠监测,计步","created_on":1417518824,"updated_on":1484735365,"created_at":null,"cover_url":"https://p4.taihuoniao.com/product/141202/547da011621e1963648b5227-1-p500x500.jpg","banner_asset":[]},"scene":[]}]
     * total_page : 1
     * current_page : 1
     * pager :
     * next_page : 0
     * prev_page : 0
     * current_user_id : 20448
     */

    public int total_rows;
    public int total_page;
    public int current_page;
    public String pager;
    public int next_page;
    public int prev_page;
    public int current_user_id;
    public List<RowsBean> rows;

    public static class RowsBean implements Serializable{
        /**
         * _id : 58a531b93ffca2840d8b4567
         * scene_id : 62
         * product_id : 1011497002
         * status : 1
         * created_on : 1487221177
         * updated_on : 1487221177
         * product : {"_id":1011497002,"title":"华米手环","short_title":"华米手环","oid":null,"sale_price":0.1,"market_price":299,"brand_id":"","kind":null,"cover_id":"56a093dd3ffca26e098ba8f3","category_id":0,"fid":null,"summary":"产品是指能够提供给市场，被人们使用和消费，并能满足人们某种需求的任何东西，包括有形的物品、无形的服务、组织、观念或它们的组合。产品一般可以分为三个层次，即核心产品、形式产品、延伸产品。","link":null,"stick":0,"fine":null,"banner_asset_ids":null,"asset_ids":null,"category_tags":["运动健康"],"view_count":2243,"favorite_count":5,"love_count":3,"comment_count":19,"buy_count":null,"deleted":0,"published":1,"attrbute":null,"state":0,"tags":["test"],"tags_s":"test","created_on":1453364461,"updated_on":1487212914,"created_at":null,"cover_url":"https://p4.taihuoniao.com/product/160121/56a0934f3ffca26c098ba90e-4-p500x500.jpg","banner_asset":[]}
         * scene : []
         */

        public String _id;
        public int scene_id;
        public int product_id;
        public int status;
        public int created_on;
        public int updated_on;
        public ProductBean product;
        public List<?> scene;

        public static class ProductBean implements Serializable{
            /**
             * _id : 1011497002
             * title : 华米手环
             * short_title : 华米手环
             * oid : null
             * sale_price : 0.1
             * market_price : 299
             * brand_id :
             * kind : null
             * cover_id : 56a093dd3ffca26e098ba8f3
             * category_id : 0
             * fid : null
             * summary : 产品是指能够提供给市场，被人们使用和消费，并能满足人们某种需求的任何东西，包括有形的物品、无形的服务、组织、观念或它们的组合。产品一般可以分为三个层次，即核心产品、形式产品、延伸产品。
             * link : null
             * stick : 0
             * fine : null
             * banner_asset_ids : null
             * asset_ids : null
             * category_tags : ["运动健康"]
             * view_count : 2243
             * favorite_count : 5
             * love_count : 3
             * comment_count : 19
             * buy_count : null
             * deleted : 0
             * published : 1
             * attrbute : null
             * state : 0
             * tags : ["test"]
             * tags_s : test
             * created_on : 1453364461
             * updated_on : 1487212914
             * created_at : null
             * cover_url : https://p4.taihuoniao.com/product/160121/56a0934f3ffca26c098ba90e-4-p500x500.jpg
             * banner_asset : []
             */

            public int _id;
            public String title;
            public String short_title;
            public Object oid;
            public double sale_price;
            public int market_price;
            public String brand_id;
            public Object kind;
            public String cover_id;
            public int category_id;
            public Object fid;
            public String summary;
            public Object link;
            public int stick;
            public Object fine;
            public Object banner_asset_ids;
            public Object asset_ids;
            public int view_count;
            public int favorite_count;
            public int love_count;
            public int comment_count;
            public Object buy_count;
            public int deleted;
            public int published;
            public Object attrbute;
            public int state;
            public String tags_s;
            public int created_on;
            public int updated_on;
            public Object created_at;
            public String cover_url;
            public List<String> category_tags;
            public List<String> tags;
            public List<?> banner_asset;
        }
    }
}
