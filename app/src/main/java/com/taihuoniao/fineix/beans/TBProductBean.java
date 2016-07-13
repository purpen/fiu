package com.taihuoniao.fineix.beans;

import com.taihuoniao.fineix.base.NetBean;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by taihuoniao on 2016/5/6.
 */
public class TBProductBean extends NetBean {
    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public static class Data {
        private List<TBProductItem> rows;

        public List<TBProductItem> getRows() {
            return rows;
        }

        public void setRows(List<TBProductItem> rows) {
            this.rows = rows;
        }
        //        private Results results;
//
//        public Results getResults() {
//            return results;
//        }
//
//        public void setResults(Results results) {
//            this.results = results;
//        }
    }

    public static class Results {
        private List<TBProductItem> n_tbk_item;

        public List<TBProductItem> getN_tbk_item() {
            return n_tbk_item;
        }

        public void setN_tbk_item(List<TBProductItem> n_tbk_item) {
            this.n_tbk_item = n_tbk_item;
        }
    }

    public static class TBProductItem {
       private String oid;
        private String title;
        private String cover_url;
        private List<String> banners_url;
        private double market_price;
        private double sale_price;
        private String link;

        public List<String> getBanners_url() {
            return banners_url;
        }

        public void setBanners_url(List<String> banners_url) {
            this.banners_url = banners_url;
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
            return new DecimalFormat("######0.00").format(market_price);
        }

        public void setMarket_price(double market_price) {
            this.market_price = market_price;
        }

        public String getOid() {
            return oid;
        }

        public void setOid(String oid) {
            this.oid = oid;
        }

        public String getSale_price() {
            return new DecimalFormat("######0.00").format(sale_price);
        }

        public void setSale_price(double sale_price) {
            this.sale_price = sale_price;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}
