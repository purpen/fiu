package com.taihuoniao.fineix.beans;

import com.taihuoniao.fineix.base.NetBean;

import java.util.List;

/**
 * Created by taihuoniao on 2016/8/23.
 */
public class FirstProductBean extends NetBean {


    /**
     * is_error : false
     * data : {"items":[{"_id":1011513749,"title":"a1螺丝刀","cover_url":"http://frbird.qiniudn.com/product/160127/56a858723ffca269098bb039-4-p500x500.jpg","brand_id":"5764f2443ffca227798b47f0","brand_cover_url":"","sale_price":0.15},{"_id":1011497183,"title":"干衣架","cover_url":"http://frbird.qiniudn.com/product/160126/56a717da3ffca27b4b8b70e0-1-p500x500.jpg","brand_id":"","brand_cover_url":"","sale_price":15.01},{"_id":1011497097,"title":"优胜仕磁吸数据线","cover_url":"http://frbird.qiniudn.com/product/160126/56a7183c3ffca268098bae83-4-p500x500.jpg","brand_id":"5764f2443ffca227798b47f0","brand_cover_url":"","sale_price":199},{"_id":1011497090,"title":"化妆镜","cover_url":"http://frbird.qiniudn.com/product/160126/56a718823ffca26e098baed1-1-p500x500.jpg","brand_id":"","brand_cover_url":"","sale_price":188},{"_id":1011497069,"title":"扩大器","cover_url":"http://frbird.qiniudn.com/product/160126/56a718b93ffca26a098baf2c-5-p500x500.jpg","brand_id":"5764f2063ffca227798b47ef","brand_cover_url":"","sale_price":19}],"current_user_id":924789}
     */

    private boolean is_error;
    /**
     * items : [{"_id":1011513749,"title":"a1螺丝刀","cover_url":"http://frbird.qiniudn.com/product/160127/56a858723ffca269098bb039-4-p500x500.jpg","brand_id":"5764f2443ffca227798b47f0","brand_cover_url":"","sale_price":0.15},{"_id":1011497183,"title":"干衣架","cover_url":"http://frbird.qiniudn.com/product/160126/56a717da3ffca27b4b8b70e0-1-p500x500.jpg","brand_id":"","brand_cover_url":"","sale_price":15.01},{"_id":1011497097,"title":"优胜仕磁吸数据线","cover_url":"http://frbird.qiniudn.com/product/160126/56a7183c3ffca268098bae83-4-p500x500.jpg","brand_id":"5764f2443ffca227798b47f0","brand_cover_url":"","sale_price":199},{"_id":1011497090,"title":"化妆镜","cover_url":"http://frbird.qiniudn.com/product/160126/56a718823ffca26e098baed1-1-p500x500.jpg","brand_id":"","brand_cover_url":"","sale_price":188},{"_id":1011497069,"title":"扩大器","cover_url":"http://frbird.qiniudn.com/product/160126/56a718b93ffca26a098baf2c-5-p500x500.jpg","brand_id":"5764f2063ffca227798b47ef","brand_cover_url":"","sale_price":19}]
     * current_user_id : 924789
     */

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

    public static class DataBean {
        private String current_user_id;
        /**
         * _id : 1011513749
         * title : a1螺丝刀
         * cover_url : http://frbird.qiniudn.com/product/160127/56a858723ffca269098bb039-4-p500x500.jpg
         * brand_id : 5764f2443ffca227798b47f0
         * brand_cover_url :
         * sale_price : 0.15
         */

        private List<ItemsBean> items;

        public String getCurrent_user_id() {
            return current_user_id;
        }

        public void setCurrent_user_id(String current_user_id) {
            this.current_user_id = current_user_id;
        }

        public List<ItemsBean> getItems() {
            return items;
        }

        public void setItems(List<ItemsBean> rows) {
            this.items = rows;
        }

        public static class ItemsBean {
            private String _id;
            private String title;
            private String cover_url;
            private String brand_id;
            private String brand_cover_url;
            private double sale_price;

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

            public double getSale_price() {
                return sale_price;
            }

            public void setSale_price(double sale_price) {
                this.sale_price = sale_price;
            }
        }
    }
}
