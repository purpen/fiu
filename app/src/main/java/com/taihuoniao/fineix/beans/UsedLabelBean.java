package com.taihuoniao.fineix.beans;

/**
 * Created by taihuoniao on 2016/4/11.
 */
public class UsedLabelBean {
    private String _id;
    private String title_cn;
    private String parent_id;
    private String type;
    private String stick;
    private String user_id;
    private String title_en;
    private String likename;
    private String left_ref;
    private String right_ref;
    private String used_count;
    private String cover_id;
    private String status;
    private String created_on;
    private String updated_on;

    public UsedLabelBean() {
    }

    public UsedLabelBean(String _id, String title_cn) {
        this._id = _id;
        this.title_cn = title_cn;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUsed_count() {
        return used_count;
    }

    public void setUsed_count(String used_count) {
        this.used_count = used_count;
    }

    public String getUpdated_on() {
        return updated_on;
    }

    public void setUpdated_on(String updated_on) {
        this.updated_on = updated_on;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle_en() {
        return title_en;
    }

    public void setTitle_en(String title_en) {
        this.title_en = title_en;
    }

    public String getTitle_cn() {
        return title_cn;
    }

    public void setTitle_cn(String title_cn) {
        this.title_cn = title_cn;
    }

    public String getStick() {
        return stick;
    }

    public void setStick(String stick) {
        this.stick = stick;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRight_ref() {
        return right_ref;
    }

    public void setRight_ref(String right_ref) {
        this.right_ref = right_ref;
    }

    public String getParent_id() {
        return parent_id;
    }

    public void setParent_id(String parent_id) {
        this.parent_id = parent_id;
    }

    public String getLikename() {
        return likename;
    }

    public void setLikename(String likename) {
        this.likename = likename;
    }

    public String getLeft_ref() {
        return left_ref;
    }

    public void setLeft_ref(String left_ref) {
        this.left_ref = left_ref;
    }

    public String getCover_id() {
        return cover_id;
    }

    public void setCover_id(String cover_id) {
        this.cover_id = cover_id;
    }

    public String getCreated_on() {
        return created_on;
    }

    public void setCreated_on(String created_on) {
        this.created_on = created_on;
    }
}
