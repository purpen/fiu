package com.taihuoniao.fineix.beans;

/**
 * Created by taihuoniao on 2016/2/23.
 */
public class RelationProductsBean {
    private String _id;
    private String title;
    private String cover_url;
    private String sale_price;

    public String getCover_url() {
        return cover_url;
    }

    public void setCover_url(String cover_url) {
        this.cover_url = cover_url;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
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
}
