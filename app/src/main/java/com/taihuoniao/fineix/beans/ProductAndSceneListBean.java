package com.taihuoniao.fineix.beans;

import com.taihuoniao.fineix.base.NetBean;

import java.util.List;

/**
 * Created by taihuoniao on 2016/5/3.
 * 产品场景关联列表
 */
public class ProductAndSceneListBean extends NetBean {
    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public static class Data {
        private List<ProductAndSceneItem> rows;

        public List<ProductAndSceneItem> getRows() {
            return rows;
        }

        public void setRows(List<ProductAndSceneItem> rows) {
            this.rows = rows;
        }
    }

    public static class ProductAndSceneItem {
        private Product product;
        private Sight sight;


        public Product getProduct() {
            return product;
        }

        public void setProduct(Product product) {
            this.product = product;
        }

        public Sight getSight() {
            return sight;
        }

        public void setSight(Sight sight) {
            this.sight = sight;
        }
    }
    public static class Sight{
        private String title;
        private String _id;

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

    }
    public static class Product{
        private String _id;
        private String title;
        private String oid;
        private String sale_price;
        private String market_price;
        private String link;
        private String attrbute;
        private String cover_url;
        private List<String> banner_asset;

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

        public String getCover_url() {
            return cover_url;
        }

        public void setCover_url(String cover_url) {
            this.cover_url = cover_url;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public String getMarket_price() {
            return market_price;
        }

        public void setMarket_price(String market_price) {
            this.market_price = market_price;
        }

        public String getSale_price() {
            return sale_price;
        }

        public void setSale_price(String sale_price) {
            this.sale_price = sale_price;
        }

        public String getOid() {
            return oid;
        }

        public void setOid(String oid) {
            this.oid = oid;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}
