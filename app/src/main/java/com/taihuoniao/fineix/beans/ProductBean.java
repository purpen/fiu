package com.taihuoniao.fineix.beans;

import com.taihuoniao.fineix.base.NetBean;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by taihuoniao on 2016/4/13.
 */
public class ProductBean extends NetBean implements Serializable {
    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public static class Data {
        private List<ProductListItem> rows;

        public List<ProductListItem> getRows() {
            return rows;
        }

        public void setRows(List<ProductListItem> rows) {
            this.rows = rows;
        }
    }

    public static class ProductListItem {
        private int pos;
        private String _id;
        private String title;
        private double sale_price;
        private double market_price;
        private String love_count;
        private String cover_url;
        private String attrbute;
        private List<String> category_tags;
        private List<String> banner_asset = new ArrayList<>();
        private List<Sight> sights;

        @Override
        public String toString() {
            return "ProductListItem{" +
                    "_id='" + _id + '\'' +
                    ", pos=" + pos +
                    ", title='" + title + '\'' +
                    ", sale_price='" + sale_price + '\'' +
                    ", market_price='" + market_price + '\'' +
                    ", love_count='" + love_count + '\'' +
                    ", cover_url='" + cover_url + '\'' +
                    ", attrbute='" + attrbute + '\'' +
                    ", category_tags=" + category_tags +
                    ", banner_asset=" + banner_asset +
                    ", sights=" + sights +
                    '}';
        }

        public int getPos() {
            return pos;
        }

        public void setPos(int pos) {
            this.pos = pos;
        }

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

        public List<String> getCategory_tags() {
            return category_tags;
        }

        public void setCategory_tags(List<String> category_tags) {
            this.category_tags = category_tags;
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

        public String getMarket_price() {
            return new DecimalFormat("######0.00").format(market_price);
        }

        public void setMarket_price(double market_price) {
            this.market_price = market_price;
        }

        public String getSale_price() {
            return new DecimalFormat("######0.00").format(sale_price);
        }

        public void setSale_price(double sale_price) {
            this.sale_price = sale_price;
        }

        public List<Sight> getSights() {
            return sights;
        }

        public void setSights(List<Sight> sights) {
            this.sights = sights;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }

    public static class Sight {
        private String id;
        private String title;
        private String cover_url;

        public String getCover_url() {
            return cover_url;
        }

        public void setCover_url(String cover_url) {
            this.cover_url = cover_url;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}
