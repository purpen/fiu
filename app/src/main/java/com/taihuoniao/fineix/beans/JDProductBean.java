package com.taihuoniao.fineix.beans;

import com.taihuoniao.fineix.base.NetBean;

import java.util.List;

/**
 * Created by taihuoniao on 2016/5/6.
 */
public class JDProductBean extends NetBean {

    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public static class Data{
        private List<JDProductItem> rows;

        public List<JDProductItem> getRows() {
            return rows;
        }

        public void setRows(List<JDProductItem> rows) {
            this.rows = rows;
        }
        //        private List<JDProductItem> listproductbase_result;
//
//        public List<JDProductItem> getListproductbase_result() {
//            return listproductbase_result;
//        }
//
//        public void setListproductbase_result(List<JDProductItem> listproductbase_result) {
//            this.listproductbase_result = listproductbase_result;
//        }
    }
    public static class JDProductItem{
private String oid;
        private String title;
        private String cover_url;
        private List<String> banners_url;
        private String market_price;
        private String sale_price;
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
        //        private String skuId;
//        private String name;
//        private String market_price;
//        private String sale_price;
//        private String url;
//        private String imagePath;
//
//        public String getImagePath() {
//            return imagePath;
//        }
//
//        public void setImagePath(String imagePath) {
//            this.imagePath = imagePath;
//        }
//
//        public String getMarket_price() {
//            return market_price;
//        }
//
//        public void setMarket_price(String market_price) {
//            this.market_price = market_price;
//        }
//
//        public String getName() {
//            return name;
//        }
//
//        public void setName(String name) {
//            this.name = name;
//        }
//
//        public String getSale_price() {
//            return sale_price;
//        }
//
//        public void setSale_price(String sale_price) {
//            this.sale_price = sale_price;
//        }
//
//        public String getSkuId() {
//            return skuId;
//        }
//
//        public void setSkuId(String skuId) {
//            this.skuId = skuId;
//        }
//
//        public String getUrl() {
//            return url;
//        }
//
//        public void setUrl(String url) {
//            this.url = url;
//        }
    }
}
