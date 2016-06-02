package com.taihuoniao.fineix.beans;

import com.taihuoniao.fineix.base.NetBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by taihuoniao on 2016/5/3.
 */
public class GoodsDetailBean extends NetBean implements Serializable{
    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public static class Data implements Serializable{
        private String _id;
        private String title;
        private String short_title;
        private String oid;
        private String sale_price;
        private String market_price;
        private String brand_id;
        private Brand brand;
        private String kind;
        private String cover_id;
        private String category_id;
        private String fid;
        private String summary;
        private String link;
        private String description;
        private String stick;
        private String fine;
        private String view_count;
        private String favorite_count;
        private String love_count;
        private String comment_count;
        private String buy_count;
        private String deleted;
        private String published;
        private String attrbute;
        private String state;
        private List<String> tags;
        private String tags_s;
        private String created_at;
        private String cover_url;
        private String is_favorite;
        private String is_love;
        private List<String> banner_asset;
        private List<PNG> png_asset;

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

        public String getAttrbute() {
            return attrbute;
        }

        public void setAttrbute(String attrbute) {
            this.attrbute = attrbute;
        }

        public List<String> getBanner_asset() {
            return banner_asset;
        }

        public void setBanner_asset(List<String> banner_asset) {
            this.banner_asset = banner_asset;
        }

        public Brand getBrand() {
            return brand;
        }

        public void setBrand(Brand brand) {
            this.brand = brand;
        }

        public String getBrand_id() {
            return brand_id;
        }

        public void setBrand_id(String brand_id) {
            this.brand_id = brand_id;
        }

        public String getBuy_count() {
            return buy_count;
        }

        public void setBuy_count(String buy_count) {
            this.buy_count = buy_count;
        }

        public String getCategory_id() {
            return category_id;
        }

        public void setCategory_id(String category_id) {
            this.category_id = category_id;
        }

        public String getComment_count() {
            return comment_count;
        }

        public void setComment_count(String comment_count) {
            this.comment_count = comment_count;
        }

        public String getCover_id() {
            return cover_id;
        }

        public void setCover_id(String cover_id) {
            this.cover_id = cover_id;
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

        public String getDeleted() {
            return deleted;
        }

        public void setDeleted(String deleted) {
            this.deleted = deleted;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getFavorite_count() {
            return favorite_count;
        }

        public void setFavorite_count(String favorite_count) {
            this.favorite_count = favorite_count;
        }

        public String getFid() {
            return fid;
        }

        public void setFid(String fid) {
            this.fid = fid;
        }

        public String getFine() {
            return fine;
        }

        public void setFine(String fine) {
            this.fine = fine;
        }

        public String getIs_favorite() {
            return is_favorite;
        }

        public void setIs_favorite(String is_favorite) {
            this.is_favorite = is_favorite;
        }

        public String getIs_love() {
            return is_love;
        }

        public void setIs_love(String is_love) {
            this.is_love = is_love;
        }

        public String getKind() {
            return kind;
        }

        public void setKind(String kind) {
            this.kind = kind;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public String getLove_count() {
            return love_count;
        }

        public void setLove_count(String love_count) {
            this.love_count = love_count;
        }

        public String getMarket_price() {
            return market_price;
        }

        public void setMarket_price(String market_price) {
            this.market_price = market_price;
        }

        public String getOid() {
            return oid;
        }

        public void setOid(String oid) {
            this.oid = oid;
        }

        public List<PNG> getPng_asset() {
            return png_asset;
        }

        public void setPng_asset(List<PNG> png_asset) {
            this.png_asset = png_asset;
        }

        public String getPublished() {
            return published;
        }

        public void setPublished(String published) {
            this.published = published;
        }

        public String getSale_price() {
            return sale_price;
        }

        public void setSale_price(String sale_price) {
            this.sale_price = sale_price;
        }

        public String getShort_title() {
            return short_title;
        }

        public void setShort_title(String short_title) {
            this.short_title = short_title;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getStick() {
            return stick;
        }

        public void setStick(String stick) {
            this.stick = stick;
        }

        public String getSummary() {
            return summary;
        }

        public void setSummary(String summary) {
            this.summary = summary;
        }

        public List<String> getTags() {
            return tags;
        }

        public void setTags(List<String> tags) {
            this.tags = tags;
        }

        public String getTags_s() {
            return tags_s;
        }

        public void setTags_s(String tags_s) {
            this.tags_s = tags_s;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getView_count() {
            return view_count;
        }

        public void setView_count(String view_count) {
            this.view_count = view_count;
        }
    }

    public static class PNG implements Serializable{
        private String url;
        private String width;
        private String height;

        public String getHeight() {
            return height;
        }

        public void setHeight(String height) {
            this.height = height;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getWidth() {
            return width;
        }

        public void setWidth(String width) {
            this.width = width;
        }
    }

    public static class Brand implements Serializable{
        private String cover_url;
        private String _id;
        private String title;
        private String des;

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

        public String getCover_url() {
            return cover_url;
        }

        public void setCover_url(String cover_url) {
            this.cover_url = cover_url;
        }

        public String getDes() {
            return des;
        }

        public void setDes(String des) {
            this.des = des;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }


//    public static class Cover {
//        private Id _id;
//        private String parent_id;
//        private String filepath;
//        private String filename;
//        private String size;
//        private String width;
//        private String height;
//        private String desc;
//        private Thumbnails thumbnails;
//        private String kind;
//        private String asset_type;
//        private String id;
//        private String fileurl;
//
//        public String getAsset_type() {
//            return asset_type;
//        }
//
//        public void setAsset_type(String asset_type) {
//            this.asset_type = asset_type;
//        }
//
//        public String getDesc() {
//            return desc;
//        }
//
//        public void setDesc(String desc) {
//            this.desc = desc;
//        }
//
//        public String getFilename() {
//            return filename;
//        }
//
//        public void setFilename(String filename) {
//            this.filename = filename;
//        }
//
//        public String getFilepath() {
//            return filepath;
//        }
//
//        public void setFilepath(String filepath) {
//            this.filepath = filepath;
//        }
//
//        public String getFileurl() {
//            return fileurl;
//        }
//
//        public void setFileurl(String fileurl) {
//            this.fileurl = fileurl;
//        }
//
//        public String getHeight() {
//            return height;
//        }
//
//        public void setHeight(String height) {
//            this.height = height;
//        }
//
//        public String getId() {
//            return id;
//        }
//
//        public void setId(String id) {
//            this.id = id;
//        }
//
//        public String getKind() {
//            return kind;
//        }
//
//        public void setKind(String kind) {
//            this.kind = kind;
//        }
//
//        public String getParent_id() {
//            return parent_id;
//        }
//
//        public void setParent_id(String parent_id) {
//            this.parent_id = parent_id;
//        }
//
//        public String getSize() {
//            return size;
//        }
//
//        public void setSize(String size) {
//            this.size = size;
//        }
//
//        public Thumbnails getThumbnails() {
//            return thumbnails;
//        }
//
//        public void setThumbnails(Thumbnails thumbnails) {
//            this.thumbnails = thumbnails;
//        }
//
//        public String getWidth() {
//            return width;
//        }
//
//        public void setWidth(String width) {
//            this.width = width;
//        }
//
//        public Id get_id() {
//            return _id;
//        }
//
//        public void set_id(Id _id) {
//            this._id = _id;
//        }
//    }

//    public static class Thumbnails {
//        private Aub aub;
//        private Apc apc;
//
//        public Apc getApc() {
//            return apc;
//        }
//
//        public void setApc(Apc apc) {
//            this.apc = apc;
//        }
//
//        public Aub getAub() {
//            return aub;
//        }
//
//        public void setAub(Aub aub) {
//            this.aub = aub;
//        }
//    }

//    public static class Apc {
//        private String view_url;
//
//        public String getView_url() {
//            return view_url;
//        }
//
//        public void setView_url(String view_url) {
//            this.view_url = view_url;
//        }
//    }

//    public static class Aub {
//        private String view_url;
//
//        public String getView_url() {
//            return view_url;
//        }
//
//        public void setView_url(String view_url) {
//            this.view_url = view_url;
//        }
//    }

//    public static class Id {
//        private String $id;
//
//        public String get$id() {
//            return $id;
//        }
//
//        public void set$id(String $id) {
//            this.$id = $id;
//        }
//    }

}
