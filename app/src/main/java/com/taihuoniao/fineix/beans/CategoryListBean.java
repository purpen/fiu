package com.taihuoniao.fineix.beans;

/**
 * Created by taihuoniao on 2016/4/13.
 */
public class CategoryListBean {
    private String _id;
    private String title;
    private String name;
    private String app_cover_s_url;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getApp_cover_s_url() {
        return app_cover_s_url;
    }

    public void setApp_cover_s_url(String app_cover_s_url) {
        this.app_cover_s_url = app_cover_s_url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}