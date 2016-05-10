package com.taihuoniao.fineix.beans;

import java.util.List;

/**
 * Created by taihuoniao on 2016/2/23.
 */
public class GoodsDetailsBean {
    private String _id;
    private String title;
    private String sale_price;
    private String market_price;
    private List<RelationProductsBean> relationProductsList;
    private String is_love;
    private List<String> imgUrlList;
    private String wap_view_url;
    private String content_view_url;
    private String cover_url;
    private String inventory;
    private String skus_count;
    private List<SkusBean> skus;
    private String share_view_url;
    private String share_desc;
    private boolean success;
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getShare_desc() {
        return share_desc;
    }

    public void setShare_desc(String share_desc) {
        this.share_desc = share_desc;
    }

    public String getShare_view_url() {
        return share_view_url;
    }

    public void setShare_view_url(String share_view_url) {
        this.share_view_url = share_view_url;
    }

    public List<SkusBean> getSkus() {
        return skus;
    }

    public void setSkus(List<SkusBean> skus) {
        this.skus = skus;
    }

    public String getContent_view_url() {
        return content_view_url;
    }

    public void setContent_view_url(String content_view_url) {
        this.content_view_url = content_view_url;
    }


    public String getSkus_count() {
        return skus_count;
    }

    public void setSkus_count(String skus_count) {
        this.skus_count = skus_count;
    }

    public String getInventory() {
        return inventory;
    }

    public void setInventory(String inventory) {
        this.inventory = inventory;
    }

    public String getCover_url() {
        return cover_url;
    }

    public void setCover_url(String cover_url) {
        this.cover_url = cover_url;
    }

    public String getWap_view_url() {
        return wap_view_url;
    }

    public void setWap_view_url(String wap_view_url) {
        this.wap_view_url = wap_view_url;
    }

    public List<String> getImgUrlList() {
        return imgUrlList;
    }

    public void setImgUrlList(List<String> imgUrlList) {
        this.imgUrlList = imgUrlList;
    }

    public String getIs_love() {
        return is_love;
    }

    public void setIs_love(String is_love) {
        this.is_love = is_love;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getMarket_price() {
        return market_price;
    }

    public void setMarket_price(String market_price) {
        this.market_price = market_price;
    }

    public List<RelationProductsBean> getRelationProductsList() {
        return relationProductsList;
    }

    public void setRelationProductsList(List<RelationProductsBean> relationProductsList) {
        this.relationProductsList = relationProductsList;
    }

    public String getSale_price() {
        return sale_price;
    }

    public void setSale_price(String sale_price) {
        this.sale_price = sale_price;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
