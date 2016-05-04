package com.taihuoniao.fineix.beans;

import com.taihuoniao.fineix.base.NetBean;

import java.util.List;

/**
 * Created by taihuoniao on 2016/4/28.
 */
public class BrandListBean extends NetBean {
    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public static class Data {
        private List<BrandItem> rows;

        public List<BrandItem> getRows() {
            return rows;
        }

        public void setRows(List<BrandItem> rows) {
            this.rows = rows;
        }
    }

    public static class BrandItem {
        private Id _id;
        private String title;
        private String des;
        private String cover_url;
        private String brands_size_type;

        public String getBrands_size_type() {
            return brands_size_type;
        }

        public void setBrands_size_type(String brands_size_type) {
            this.brands_size_type = brands_size_type;
        }

        public Id get_id() {
            return _id;
        }

        public void set_id(Id _id) {
            this._id = _id;
        }

        public String getCover_url() {
            return cover_url;
        }

        public void setCover_url(String cover_url) {
            this.cover_url = cover_url;
        }
    }

    public static class Id {
        private String $id;

        public String get$id() {
            return $id;
        }

        public void set$id(String $id) {
            this.$id = $id;
        }
    }
}
