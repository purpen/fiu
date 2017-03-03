package com.taihuoniao.fineix.main.tab3;

import java.util.List;

/**
 *  好货最热推荐接口Bean
 * Created by Stephen on 2017/3/4 4:56
 * Email: 895745843@qq.com
 */

public class Product3Bean {

    /**
     * items : [{"_id":8,"title":"test1","cover_url":"http://frbird.qiniudn.com/scene_product/160226/56cfb60b8ead0ee567dd5bf5-p500x500.jpg","brand_id":"5747e0a70764cde04b0041b8","brand_cover_url":"http://frbird.qiniudn.com/scene_brands/160527/5747e09e0764cde04b0041b6-ava.jpg"}]
     * current_user_id : 0
     */

    private String current_user_id;
    private List<ItemsEntity> items;

    public void setCurrent_user_id(String current_user_id) {
        this.current_user_id = current_user_id;
    }

    public void setItems(List<ItemsEntity> items) {
        this.items = items;
    }

    public String getCurrent_user_id() {
        return current_user_id;
    }

    public List<ItemsEntity> getItems() {
        return items;
    }

    public static class ItemsEntity {
        /**
         * _id : 8
         * title : test1
         * sale_price
         * cover_url : http://frbird.qiniudn.com/scene_product/160226/56cfb60b8ead0ee567dd5bf5-p500x500.jpg
         * brand_id : 5747e0a70764cde04b0041b8
         * brand_cover_url : http://frbird.qiniudn.com/scene_brands/160527/5747e09e0764cde04b0041b6-ava.jpg
         */

        private String _id;
        private String title;
        private String cover_url;
        private String brand_id;
        private String sale_price;
        private String brand_cover_url;

        public void set_id(String _id) {
            this._id = _id;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public void setCover_url(String cover_url) {
            this.cover_url = cover_url;
        }

        public void setBrand_id(String brand_id) {
            this.brand_id = brand_id;
        }

        public void setBrand_cover_url(String brand_cover_url) {
            this.brand_cover_url = brand_cover_url;
        }

        public String get_id() {
            return _id;
        }

        public String getTitle() {
            return title;
        }

        public String getCover_url() {
            return cover_url;
        }

        public String getBrand_id() {
            return brand_id;
        }

        public String getBrand_cover_url() {
            return brand_cover_url;
        }

        public String getSale_price() {
            return sale_price;
        }

        public void setSale_price(String sale_price) {
            this.sale_price = sale_price;
        }
    }
}
