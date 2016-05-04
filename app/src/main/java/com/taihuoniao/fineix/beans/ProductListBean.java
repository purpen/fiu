package com.taihuoniao.fineix.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by taihuoniao on 2016/4/13.
 */
public class ProductListBean implements Serializable {
    private String _id;
    private String title;
    private String sale_price;
    private String market_price;
    private String love_count;
    private String cover_url;
    private List<String> category_tags;
    public ArrayList<String> banner_asset;

    public List<String> getCategory_tags() {
        return category_tags;
    }

    public void setCategory_tags(List<String> category_tags) {
        this.category_tags = category_tags;
    }

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

    public String getLove_count() {
        return love_count;
    }

    public void setLove_count(String love_count) {
        this.love_count = love_count;
    }

    public String getSale_price() {
        return sale_price;
    }

    public void setSale_price(String sale_price) {
        this.sale_price = sale_price;
    }

    public String getMarket_price() {
        return market_price;
    }

    public void setMarket_price(String market_price) {
        this.market_price = market_price;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
