package com.taihuoniao.fineix.beans;

/**
 * Created by taihuoniao on 2016/1/19.
 *
 */
public class TryCommentsBean {
    private String _id;
    private String user_id;
    private String content;
    private String star;
    private String target_id;
    private String target_user_id;
    private String sku_id;
    private String deleted;
    private String reply_user_id;
    private String floor;
    private String type;
    private String sub_type;
    private TryDetailsUserBean user;
    private TryDetailsUserBean target_user;
    private String love_count;
    private String invented_love_count;
    private String is_reply;
    private String reply_id;
    private String created_on;
    private String created_at;
    private String updated_on;

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreated_on() {
        return created_on;
    }

    public void setCreated_on(String created_on) {
        this.created_on = created_on;
    }

    public String getDeleted() {
        return deleted;
    }

    public void setDeleted(String deleted) {
        this.deleted = deleted;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getInvented_love_count() {
        return invented_love_count;
    }

    public void setInvented_love_count(String invented_love_count) {
        this.invented_love_count = invented_love_count;
    }

    public String getIs_reply() {
        return is_reply;
    }

    public void setIs_reply(String is_reply) {
        this.is_reply = is_reply;
    }

    public String getLove_count() {
        return love_count;
    }

    public void setLove_count(String love_count) {
        this.love_count = love_count;
    }

    public String getReply_id() {
        return reply_id;
    }

    public void setReply_id(String reply_id) {
        this.reply_id = reply_id;
    }

    public String getReply_user_id() {
        return reply_user_id;
    }

    public void setReply_user_id(String reply_user_id) {
        this.reply_user_id = reply_user_id;
    }

    public String getSku_id() {
        return sku_id;
    }

    public void setSku_id(String sku_id) {
        this.sku_id = sku_id;
    }

    public String getStar() {
        return star;
    }

    public void setStar(String star) {
        this.star = star;
    }

    public String getSub_type() {
        return sub_type;
    }

    public void setSub_type(String sub_type) {
        this.sub_type = sub_type;
    }

    public String getTarget_id() {
        return target_id;
    }

    public void setTarget_id(String target_id) {
        this.target_id = target_id;
    }

    public TryDetailsUserBean getTarget_user() {
        return target_user;
    }

    public void setTarget_user(TryDetailsUserBean target_user) {
        this.target_user = target_user;
    }

    public String getTarget_user_id() {
        return target_user_id;
    }

    public void setTarget_user_id(String target_user_id) {
        this.target_user_id = target_user_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUpdated_on() {
        return updated_on;
    }

    public void setUpdated_on(String updated_on) {
        this.updated_on = updated_on;
    }

    public TryDetailsUserBean getUser() {
        return user;
    }

    public void setUser(TryDetailsUserBean user) {
        this.user = user;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
