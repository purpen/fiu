package com.taihuoniao.fineix.beans;

import java.io.Serializable;

/**
 * Created by taihuoniao on 2016/3/9.
 */
public class CityBean implements Serializable {
    private String _id;
    private String name;
    private String parent_id;

    public String getParent_id() {
        return parent_id;
    }

    public void setParent_id(String parent_id) {
        this.parent_id = parent_id;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
