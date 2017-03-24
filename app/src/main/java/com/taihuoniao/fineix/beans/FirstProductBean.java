package com.taihuoniao.fineix.beans;

import java.util.List;

/**
 * Created by taihuoniao on 2016/8/23.
 */
public class FirstProductBean {

    /**
     * items : [{"_id":1048989219,"title":"车听宝 智能车载","cover_url":"https://p4.taihuoniao.com/product/161125/5837ec65fc8b12d1058b79cb-1-p500x500.jpg","brand_id":"","brand_cover_url":"","sale_price":199},{"_id":1156412102,"title":"飞利浦蓝牙音箱","cover_url":"https://p4.taihuoniao.com//161208/5848ef74fc8b12c7408b9461-1-p500x500.jpg","brand_id":"56f0e3b4fc8b127c6f8b4e2f","brand_cover_url":"https://p4.taihuoniao.com/scene_brands/160713/57860379fc8b12bd1c8bc861-1-ava.jpg","sale_price":199},{"_id":1049278575,"title":"Autobot智能金刚支架","cover_url":"https://p4.taihuoniao.com/product/161125/5837c526fc8b1258658b4e68-1-p500x500.jpg","brand_id":"576cee2efc8b12654d8b4aaa","brand_cover_url":"https://p4.taihuoniao.com/scene_brands/160624/576ced22fc8b1269368b57f4-1-ava.jpg","sale_price":59},{"_id":1049277857,"title":"喜马拉雅喜马拉雅","cover_url":"https://p4.taihuoniao.com/product/161124/58369f94fc8b12cd058b70d7-1-p500x500.jpg","brand_id":"57d6bb09fc8b1276048b6777","brand_cover_url":"https://p4.taihuoniao.com/scene_brands/160912/57d6ba3cfc8b12e47a8b5f81-1-ava.jpg","sale_price":118},{"_id":1048989219,"title":"车听宝 智能车载","cover_url":"https://p4.taihuoniao.com/product/161125/5837ec65fc8b12d1058b79cb-1-p500x500.jpg","brand_id":"","brand_cover_url":"","sale_price":199},{"_id":1023327936,"title":"卡蛙酷博车载空气净化器","cover_url":"https://p4.taihuoniao.com/product/160325/56f52c05fc8b12606f8b8d26-1-p500x500.jpg","brand_id":"576cd774fc8b12f2248be895","brand_cover_url":"https://p4.taihuoniao.com/scene_brands/160624/576cd6e5fc8b12654d8b463f-1-ava.jpg","sale_price":99}]
     * current_user_id : 1136552
     */

    private String current_user_id;
    private List<ItemsEntity> items;

    public String getCurrent_user_id() {
        return current_user_id;
    }

    public void setCurrent_user_id(String current_user_id) {
        this.current_user_id = current_user_id;
    }

    public List<ItemsEntity> getItems() {
        return items;
    }

    public void setItems(List<ItemsEntity> items) {
        this.items = items;
    }

    public static class ItemsEntity {
        /**
         * _id : 1048989219
         * title : 车听宝 智能车载
         * cover_url : https://p4.taihuoniao.com/product/161125/5837ec65fc8b12d1058b79cb-1-p500x500.jpg
         * brand_id : 
         * brand_cover_url : 
         * sale_price : 199
         */

        private String _id;
        private String title;
        private String cover_url;
        private String brand_id;
        private String brand_cover_url;
        private String sale_price;

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

        public String getBrand_id() {
            return brand_id;
        }

        public void setBrand_id(String brand_id) {
            this.brand_id = brand_id;
        }

        public String getBrand_cover_url() {
            return brand_cover_url;
        }

        public void setBrand_cover_url(String brand_cover_url) {
            this.brand_cover_url = brand_cover_url;
        }

        public String getSale_price() {
            return sale_price;
        }

        public void setSale_price(String sale_price) {
            this.sale_price = sale_price;
        }
    }
}
