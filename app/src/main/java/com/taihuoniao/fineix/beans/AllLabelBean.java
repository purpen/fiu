package com.taihuoniao.fineix.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by taihuoniao on 2016/4/11.
 */
public class AllLabelBean implements Serializable {
    private int page = 2;//gridview显示的页码
    private String _id;
    private String title_cn;
    private String parent_id;
    private String type;
    private String stick;
    private String title_en;
    private String left_ref;
    private String right_ref;
    private String used_count;
    private String cover_id;
    private String status;
    private int children_count;
    private String type_str;
    private String level;
    private List<AllLabelBean> children = new ArrayList<>();

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public List<AllLabelBean> getChildren() {
        return children;
    }

    public void setChildren(List<AllLabelBean> children) {
        this.children = children;
    }

    public int getChildren_count() {
        return children_count;
    }

    public void setChildren_count(int children_count) {
        this.children_count = children_count;
    }

    public String getCover_id() {
        return cover_id;
    }

    public void setCover_id(String cover_id) {
        this.cover_id = cover_id;
    }

    public String getLeft_ref() {
        return left_ref;
    }

    public void setLeft_ref(String left_ref) {
        this.left_ref = left_ref;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getParent_id() {
        return parent_id;
    }

    public void setParent_id(String parent_id) {
        this.parent_id = parent_id;
    }

    public String getRight_ref() {
        return right_ref;
    }

    public void setRight_ref(String right_ref) {
        this.right_ref = right_ref;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStick() {
        return stick;
    }

    public void setStick(String stick) {
        this.stick = stick;
    }

    public String getTitle_cn() {
        return title_cn;
    }

    public void setTitle_cn(String title_cn) {
        this.title_cn = title_cn;
    }

    public String getTitle_en() {
        return title_en;
    }

    public void setTitle_en(String title_en) {
        this.title_en = title_en;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType_str() {
        return type_str;
    }

    public void setType_str(String type_str) {
        this.type_str = type_str;
    }

    public String getUsed_count() {
        return used_count;
    }

    public void setUsed_count(String used_count) {
        this.used_count = used_count;
    }

    @Override
    public String toString() {
        return "AllLabelBean{" +
                "children=" + children +
                ", title_cn='" + title_cn + '\'' +
                '}';
    }
}
