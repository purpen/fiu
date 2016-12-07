package com.taihuoniao.fineix.beans;

import java.io.Serializable;
import java.util.List;

/**
 * Created by taihuoniao on 2016/8/25.
 */
public class BuyGoodDetailsBean extends NetBean implements Serializable {

    private boolean is_error;
    private DataBean data;

    public boolean isIs_error() {
        return is_error;
    }

    public void setIs_error(boolean is_error) {
        this.is_error = is_error;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean implements Serializable {
        private String _id;
        private String title;
        private String short_title;
        private String advantage;
        private double sale_price;
        private double market_price;
        private String cover_id;
        private String category_id;
        private int stage;
        private String summary;
        private String tags_s;
        private String snatched_time;
        private int inventory;
        private String snatched;
        private String wap_view_url;
        private String brand_id;
        private String extra_info;
        private String stick;
        private String love_count;
        private String favorite_count;
        private String view_count;
        private String comment_count;
        private String comment_star;
        private String snatched_end_time;
        private String snatched_price;
        private String snatched_count;
        private String app_snatched;
        private String app_snatched_time;
        private String app_snatched_end_time;
        private String app_snatched_price;
        private String app_snatched_count;
        private String app_snatched_total_count;
        private String app_appoint_count;
        private String app_snatched_limit_count;
        private String content_view_url;
        private int is_favorite;
        private int is_love;
        private int is_try;
        private String share_view_url;
        private String share_desc;
        private String cover_url;
        private String can_saled;
        private int skus_count;
        private String is_app_snatched;
        private String app_snatched_rest_count;
        private String app_snatched_stat;
        private String app_snatched_time_lag;
        private String is_app_snatched_alert;
        /**
         * _id : 57a2e59afc8b124a0a8b79b3
         * title : 卡乐多
         * cover_url : http://frbird.qiniudn.com/scene_brands/160804/57a2e59ffc8b12b6158b60e7-1-ava.jpg
         */

        private BrandBean brand;
        private String current_user_id;
        private List<String> tags;
        private List<String> category_tags;
        private List<String> asset;
        private List<PngBean> png_asset;

        private List<SkusBean> skus;
        private List<RelationProductsBean> relation_products;
        private ActiveSummary active_summary;

        public ActiveSummary getActive_summary() {
            return active_summary;
        }

        public void setActive_summary(ActiveSummary active_summary) {
            this.active_summary = active_summary;
        }

        public static class ActiveSummary implements Serializable {
            private String order_reduce;
            private String other;

            public String getOrder_reduce() {
                return order_reduce;
            }

            public void setOrder_reduce(String order_reduce) {
                this.order_reduce = order_reduce;
            }

            public String getOther() {
                return other;
            }

            public void setOther(String other) {
                this.other = other;
            }
        }

        public static class PngBean implements Serializable{
            private String url;
            private int width;
            private int height;

            public int getHeight() {
                return height;
            }

            public void setHeight(int height) {
                this.height = height;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public int getWidth() {
                return width;
            }

            public void setWidth(int width) {
                this.width = width;
            }
        }
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

        public String getShort_title() {
            return short_title;
        }

        public void setShort_title(String short_title) {
            this.short_title = short_title;
        }

        public String getAdvantage() {
            return advantage;
        }

        public void setAdvantage(String advantage) {
            this.advantage = advantage;
        }

        public double getSale_price() {
            return sale_price;
        }

        public void setSale_price(double sale_price) {
            this.sale_price = sale_price;
        }

        public double getMarket_price() {
            return market_price;
        }

        public void setMarket_price(double market_price) {
            this.market_price = market_price;
        }

        public String getCover_id() {
            return cover_id;
        }

        public void setCover_id(String cover_id) {
            this.cover_id = cover_id;
        }

        public String getCategory_id() {
            return category_id;
        }

        public void setCategory_id(String category_id) {
            this.category_id = category_id;
        }

        public int getStage() {
            return stage;
        }

        public void setStage(int stage) {
            this.stage = stage;
        }

        public String getSummary() {
            return summary;
        }

        public void setSummary(String summary) {
            this.summary = summary;
        }

        public String getTags_s() {
            return tags_s;
        }

        public void setTags_s(String tags_s) {
            this.tags_s = tags_s;
        }

        public int getInventory() {
            return inventory;
        }

        public void setInventory(int inventory) {
            this.inventory = inventory;
        }

        public String getSnatched() {
            return snatched;
        }

        public void setSnatched(String snatched) {
            this.snatched = snatched;
        }

        public String getWap_view_url() {
            return wap_view_url;
        }

        public void setWap_view_url(String wap_view_url) {
            this.wap_view_url = wap_view_url;
        }

        public String getBrand_id() {
            return brand_id;
        }

        public void setBrand_id(String brand_id) {
            this.brand_id = brand_id;
        }

        public String getExtra_info() {
            return extra_info;
        }

        public void setExtra_info(String extra_info) {
            this.extra_info = extra_info;
        }

        public String getStick() {
            return stick;
        }

        public void setStick(String stick) {
            this.stick = stick;
        }

        public String getLove_count() {
            return love_count;
        }

        public void setLove_count(String love_count) {
            this.love_count = love_count;
        }

        public String getFavorite_count() {
            return favorite_count;
        }

        public void setFavorite_count(String favorite_count) {
            this.favorite_count = favorite_count;
        }

        public String getView_count() {
            return view_count;
        }

        public void setView_count(String view_count) {
            this.view_count = view_count;
        }

        public String getComment_count() {
            return comment_count;
        }

        public void setComment_count(String comment_count) {
            this.comment_count = comment_count;
        }

        public String getComment_star() {
            return comment_star;
        }

        public void setComment_star(String comment_star) {
            this.comment_star = comment_star;
        }


        public String getSnatched_price() {
            return snatched_price;
        }

        public void setSnatched_price(String snatched_price) {
            this.snatched_price = snatched_price;
        }

        public String getSnatched_count() {
            return snatched_count;
        }

        public void setSnatched_count(String snatched_count) {
            this.snatched_count = snatched_count;
        }

        public String getApp_snatched() {
            return app_snatched;
        }

        public void setApp_snatched(String app_snatched) {
            this.app_snatched = app_snatched;
        }


        public String getApp_snatched_price() {
            return app_snatched_price;
        }

        public void setApp_snatched_price(String app_snatched_price) {
            this.app_snatched_price = app_snatched_price;
        }

        public String getApp_snatched_count() {
            return app_snatched_count;
        }

        public void setApp_snatched_count(String app_snatched_count) {
            this.app_snatched_count = app_snatched_count;
        }

        public String getApp_snatched_total_count() {
            return app_snatched_total_count;
        }

        public void setApp_snatched_total_count(String app_snatched_total_count) {
            this.app_snatched_total_count = app_snatched_total_count;
        }

        public String getApp_appoint_count() {
            return app_appoint_count;
        }

        public void setApp_appoint_count(String app_appoint_count) {
            this.app_appoint_count = app_appoint_count;
        }

        public String getApp_snatched_limit_count() {
            return app_snatched_limit_count;
        }

        public void setApp_snatched_limit_count(String app_snatched_limit_count) {
            this.app_snatched_limit_count = app_snatched_limit_count;
        }

        public String getContent_view_url() {
            return content_view_url;
        }

        public void setContent_view_url(String content_view_url) {
            this.content_view_url = content_view_url;
        }

        public int getIs_favorite() {
            return is_favorite;
        }

        public void setIs_favorite(int is_favorite) {
            this.is_favorite = is_favorite;
        }

        public int getIs_love() {
            return is_love;
        }

        public void setIs_love(int is_love) {
            this.is_love = is_love;
        }

        public int getIs_try() {
            return is_try;
        }

        public void setIs_try(int is_try) {
            this.is_try = is_try;
        }

        public String getShare_view_url() {
            return share_view_url;
        }

        public void setShare_view_url(String share_view_url) {
            this.share_view_url = share_view_url;
        }

        public String getShare_desc() {
            return share_desc;
        }

        public void setShare_desc(String share_desc) {
            this.share_desc = share_desc;
        }

        public String getCover_url() {
            return cover_url;
        }

        public void setCover_url(String cover_url) {
            this.cover_url = cover_url;
        }

        public String getCan_saled() {
            return can_saled;
        }

        public void setCan_saled(String can_saled) {
            this.can_saled = can_saled;
        }

        public String getApp_snatched_end_time() {
            return app_snatched_end_time;
        }

        public void setApp_snatched_end_time(String app_snatched_end_time) {
            this.app_snatched_end_time = app_snatched_end_time;
        }

        public String getApp_snatched_rest_count() {
            return app_snatched_rest_count;
        }

        public void setApp_snatched_rest_count(String app_snatched_rest_count) {
            this.app_snatched_rest_count = app_snatched_rest_count;
        }

        public String getApp_snatched_stat() {
            return app_snatched_stat;
        }

        public void setApp_snatched_stat(String app_snatched_stat) {
            this.app_snatched_stat = app_snatched_stat;
        }

        public String getApp_snatched_time() {
            return app_snatched_time;
        }

        public void setApp_snatched_time(String app_snatched_time) {
            this.app_snatched_time = app_snatched_time;
        }

        public String getApp_snatched_time_lag() {
            return app_snatched_time_lag;
        }

        public void setApp_snatched_time_lag(String app_snatched_time_lag) {
            this.app_snatched_time_lag = app_snatched_time_lag;
        }

        public String getIs_app_snatched() {
            return is_app_snatched;
        }

        public void setIs_app_snatched(String is_app_snatched) {
            this.is_app_snatched = is_app_snatched;
        }

        public String getIs_app_snatched_alert() {
            return is_app_snatched_alert;
        }

        public void setIs_app_snatched_alert(String is_app_snatched_alert) {
            this.is_app_snatched_alert = is_app_snatched_alert;
        }

        public int getSkus_count() {
            return skus_count;
        }

        public void setSkus_count(int skus_count) {
            this.skus_count = skus_count;
        }

        public String getSnatched_end_time() {
            return snatched_end_time;
        }

        public void setSnatched_end_time(String snatched_end_time) {
            this.snatched_end_time = snatched_end_time;
        }

        public String getSnatched_time() {
            return snatched_time;
        }

        public void setSnatched_time(String snatched_time) {
            this.snatched_time = snatched_time;
        }

        public BrandBean getBrand() {
            return brand;
        }

        public void setBrand(BrandBean brand) {
            this.brand = brand;
        }

        public String getCurrent_user_id() {
            return current_user_id;
        }

        public void setCurrent_user_id(String current_user_id) {
            this.current_user_id = current_user_id;
        }

        public List<String> getTags() {
            return tags;
        }

        public void setTags(List<String> tags) {
            this.tags = tags;
        }

        public List<String> getCategory_tags() {
            return category_tags;
        }

        public void setCategory_tags(List<String> category_tags) {
            this.category_tags = category_tags;
        }

        public List<String> getAsset() {
            return asset;
        }

        public void setAsset(List<String> asset) {
            this.asset = asset;
        }

        public List<PngBean> getPng_asset() {
            return png_asset;
        }

        public void setPng_asset(List<PngBean> png_asset) {
            this.png_asset = png_asset;
        }

        public List<SkusBean> getSkus() {
            return skus;
        }

        public void setSkus(List<SkusBean> skus) {
            this.skus = skus;
        }

        public List<RelationProductsBean> getRelation_products() {
            return relation_products;
        }

        public void setRelation_products(List<RelationProductsBean> relation_products) {
            this.relation_products = relation_products;
        }

        public static class BrandBean implements Serializable {
            private String _id;
            private String title;
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

            public String getCover_url() {
                return cover_url;
            }

            public void setCover_url(String cover_url) {
                this.cover_url = cover_url;
            }
        }

        public static class SkusBean implements Serializable {
            private String _id;
            private String product_id;
            private String name;
            private String mode;
            private double price;
            private String quantity;
            private int sold;
            private int limited_count;
            private int sync_count;
            private String summary;
            private int bad_count;
            private String bad_tag;
            private int revoke_count;
            private int shelf;
            private int stage;
            private int status;
            private int created_on;
            private int updated_on;

            public String getCover_url() {
                return cover_url;
            }

            public void setCover_url(String cover_url) {
                this.cover_url = cover_url;
            }

            /**
             * "cover_url" : "http://img_url"        //  sku封面图地址
             */
            private String cover_url;

            public String get_id() {
                return _id;
            }

            public void set_id(String _id) {
                this._id = _id;
            }

            public String getProduct_id() {
                return product_id;
            }

            public void setProduct_id(String product_id) {
                this.product_id = product_id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getMode() {
                return mode;
            }

            public void setMode(String mode) {
                this.mode = mode;
            }

            public double getPrice() {
                return price;
            }

            public void setPrice(double price) {
                this.price = price;
            }

            public String getQuantity() {
                return quantity;
            }

            public void setQuantity(String quantity) {
                this.quantity = quantity;
            }

            public int getSold() {
                return sold;
            }

            public void setSold(int sold) {
                this.sold = sold;
            }

            public int getLimited_count() {
                return limited_count;
            }

            public void setLimited_count(int limited_count) {
                this.limited_count = limited_count;
            }

            public int getSync_count() {
                return sync_count;
            }

            public void setSync_count(int sync_count) {
                this.sync_count = sync_count;
            }

            public String getSummary() {
                return summary;
            }

            public void setSummary(String summary) {
                this.summary = summary;
            }

            public int getBad_count() {
                return bad_count;
            }

            public void setBad_count(int bad_count) {
                this.bad_count = bad_count;
            }

            public String getBad_tag() {
                return bad_tag;
            }

            public void setBad_tag(String bad_tag) {
                this.bad_tag = bad_tag;
            }

            public int getRevoke_count() {
                return revoke_count;
            }

            public void setRevoke_count(int revoke_count) {
                this.revoke_count = revoke_count;
            }

            public int getShelf() {
                return shelf;
            }

            public void setShelf(int shelf) {
                this.shelf = shelf;
            }

            public int getStage() {
                return stage;
            }

            public void setStage(int stage) {
                this.stage = stage;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public int getCreated_on() {
                return created_on;
            }

            public void setCreated_on(int created_on) {
                this.created_on = created_on;
            }

            public int getUpdated_on() {
                return updated_on;
            }

            public void setUpdated_on(int updated_on) {
                this.updated_on = updated_on;
            }
        }

        public static class RelationProductsBean implements Serializable {
            private String _id;
            private String title;
            private String short_title;
            private String advantage;
            private double sale_price;
            private double market_price;
            private String cover_id;
            private String category_id;
            private String stage;
            private String summary;
            private String tags_s;
            private String snatched_time;
            private String inventory;
            private String snatched;
            private String wap_view_url;
            private String brand_id;
            private String extra_info;
            private String stick;
            private String love_count;
            private String favorite_count;
            private String view_count;
            private String comment_count;
            private String comment_star;
            private String snatched_end_time;
            private String snatched_price;
            private String snatched_count;
            private String app_snatched;
            private String app_snatched_time;
            private String app_snatched_end_time;
            private String app_snatched_price;
            private String app_snatched_count;
            private String app_snatched_total_count;
            private String app_appoint_count;
            private String app_snatched_limit_count;
            private String cover_url;
            private List<String> tags;
            private List<String> category_tags;

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

            public String getShort_title() {
                return short_title;
            }

            public void setShort_title(String short_title) {
                this.short_title = short_title;
            }

            public String getAdvantage() {
                return advantage;
            }

            public void setAdvantage(String advantage) {
                this.advantage = advantage;
            }

            public double getSale_price() {
                return sale_price;
            }

            public void setSale_price(double sale_price) {
                this.sale_price = sale_price;
            }

            public double getMarket_price() {
                return market_price;
            }

            public void setMarket_price(double market_price) {
                this.market_price = market_price;
            }

            public String getCover_id() {
                return cover_id;
            }

            public void setCover_id(String cover_id) {
                this.cover_id = cover_id;
            }

            public String getCategory_id() {
                return category_id;
            }

            public void setCategory_id(String category_id) {
                this.category_id = category_id;
            }

            public String getStage() {
                return stage;
            }

            public void setStage(String stage) {
                this.stage = stage;
            }

            public String getSummary() {
                return summary;
            }

            public void setSummary(String summary) {
                this.summary = summary;
            }

            public String getTags_s() {
                return tags_s;
            }

            public void setTags_s(String tags_s) {
                this.tags_s = tags_s;
            }

            public String getInventory() {
                return inventory;
            }

            public void setInventory(String inventory) {
                this.inventory = inventory;
            }

            public String getSnatched() {
                return snatched;
            }

            public void setSnatched(String snatched) {
                this.snatched = snatched;
            }

            public String getWap_view_url() {
                return wap_view_url;
            }

            public void setWap_view_url(String wap_view_url) {
                this.wap_view_url = wap_view_url;
            }

            public String getBrand_id() {
                return brand_id;
            }

            public void setBrand_id(String brand_id) {
                this.brand_id = brand_id;
            }

            public Object getExtra_info() {
                return extra_info;
            }

            public String getApp_appoint_count() {
                return app_appoint_count;
            }

            public void setApp_appoint_count(String app_appoint_count) {
                this.app_appoint_count = app_appoint_count;
            }

            public String getApp_snatched() {
                return app_snatched;
            }

            public void setApp_snatched(String app_snatched) {
                this.app_snatched = app_snatched;
            }

            public String getApp_snatched_count() {
                return app_snatched_count;
            }

            public void setApp_snatched_count(String app_snatched_count) {
                this.app_snatched_count = app_snatched_count;
            }

            public String getApp_snatched_end_time() {
                return app_snatched_end_time;
            }

            public void setApp_snatched_end_time(String app_snatched_end_time) {
                this.app_snatched_end_time = app_snatched_end_time;
            }

            public String getApp_snatched_limit_count() {
                return app_snatched_limit_count;
            }

            public void setApp_snatched_limit_count(String app_snatched_limit_count) {
                this.app_snatched_limit_count = app_snatched_limit_count;
            }

            public String getApp_snatched_price() {
                return app_snatched_price;
            }

            public void setApp_snatched_price(String app_snatched_price) {
                this.app_snatched_price = app_snatched_price;
            }

            public String getApp_snatched_time() {
                return app_snatched_time;
            }

            public void setApp_snatched_time(String app_snatched_time) {
                this.app_snatched_time = app_snatched_time;
            }

            public String getApp_snatched_total_count() {
                return app_snatched_total_count;
            }

            public void setApp_snatched_total_count(String app_snatched_total_count) {
                this.app_snatched_total_count = app_snatched_total_count;
            }

            public String getComment_count() {
                return comment_count;
            }

            public void setComment_count(String comment_count) {
                this.comment_count = comment_count;
            }

            public String getComment_star() {
                return comment_star;
            }

            public void setComment_star(String comment_star) {
                this.comment_star = comment_star;
            }

            public void setExtra_info(String extra_info) {
                this.extra_info = extra_info;
            }

            public String getFavorite_count() {
                return favorite_count;
            }

            public void setFavorite_count(String favorite_count) {
                this.favorite_count = favorite_count;
            }

            public String getLove_count() {
                return love_count;
            }

            public void setLove_count(String love_count) {
                this.love_count = love_count;
            }

            public String getSnatched_count() {
                return snatched_count;
            }

            public void setSnatched_count(String snatched_count) {
                this.snatched_count = snatched_count;
            }

            public String getSnatched_end_time() {
                return snatched_end_time;
            }

            public void setSnatched_end_time(String snatched_end_time) {
                this.snatched_end_time = snatched_end_time;
            }

            public String getSnatched_price() {
                return snatched_price;
            }

            public void setSnatched_price(String snatched_price) {
                this.snatched_price = snatched_price;
            }

            public String getSnatched_time() {
                return snatched_time;
            }

            public void setSnatched_time(String snatched_time) {
                this.snatched_time = snatched_time;
            }

            public String getStick() {
                return stick;
            }

            public void setStick(String stick) {
                this.stick = stick;
            }

            public String getView_count() {
                return view_count;
            }

            public void setView_count(String view_count) {
                this.view_count = view_count;
            }

            public String getCover_url() {
                return cover_url;
            }

            public void setCover_url(String cover_url) {
                this.cover_url = cover_url;
            }

            public List<String> getTags() {
                return tags;
            }

            public void setTags(List<String> tags) {
                this.tags = tags;
            }

            public List<String> getCategory_tags() {
                return category_tags;
            }

            public void setCategory_tags(List<String> category_tags) {
                this.category_tags = category_tags;
            }
        }
    }
}
