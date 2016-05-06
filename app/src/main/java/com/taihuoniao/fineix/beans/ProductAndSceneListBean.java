package com.taihuoniao.fineix.beans;

import com.taihuoniao.fineix.base.NetBean;

import java.util.ArrayList;
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
        private List<SceneItem> rows;

        public List<SceneItem> getRows() {
            return rows;
        }

        public void setRows(List<SceneItem> rows) {
            this.rows = rows;
        }
    }

    public static class SceneItem {
        private String _id;
        private String title;
        private String market_price;
        private String attrbute;
        private String sale_price;
        private List<String> banner_asset = new ArrayList<>();

        public String getSale_price() {
            return sale_price;
        }

        public void setSale_price(String sale_price) {
            this.sale_price = sale_price;
        }

        public List<String> getBanner_asset() {
            return banner_asset;
        }

        public void setBanner_asset(List<String> banner_asset) {
            this.banner_asset = banner_asset;
        }

        public String getAttrbute() {
            return attrbute;
        }

        public void setAttrbute(String attrbute) {
            this.attrbute = attrbute;
        }

        public String getMarket_price() {
            return market_price;
        }

        public void setMarket_price(String market_price) {
            this.market_price = market_price;
        }

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
}
