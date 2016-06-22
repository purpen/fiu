package com.taihuoniao.fineix.beans;

import com.taihuoniao.fineix.base.NetBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by taihuoniao on 2016/6/14.
 */
public class SceneDetailsBean extends NetBean implements Serializable {
    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public static class Data implements Serializable {
        private String _id;
        private String title;
        private String oid;
        private String user_id;
        private String des;
        private String scene_id;
        private List<SceneProductItem> product;
        private Locat location;
        private String address;
        private String cover_url;
        private String scene_title;
        private List<Integer> tags;
        private List<String> tag_titles;
        private int is_love;
        private UserInfo user_info;
        private String created_at;
        private String view_count;
        private int love_count;
        private String comment_count;

        public String getOid() {
            return oid;
        }

        public void setOid(String oid) {
            this.oid = oid;
        }

        public int getLove_count() {
            return love_count;
        }

        public void setLove_count(int love_count) {
            this.love_count = love_count;
        }

        public String getView_count() {
            return view_count;
        }

        public void setView_count(String view_count) {
            this.view_count = view_count;
        }

        public String getComment_count() {
            return comment_count;
        }

        public void setComment_count(String comment_count) {
            this.comment_count = comment_count;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public UserInfo getUser_info() {
            return user_info;
        }

        public void setUser_info(UserInfo user_info) {
            this.user_info = user_info;
        }

        public int getIs_love() {
            return is_love;
        }

        public void setIs_love(int is_love) {
            this.is_love = is_love;
        }

        public List<Integer> getTags() {
            return tags;
        }

        public void setTags(List<Integer> tags) {
            this.tags = tags;
        }

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getCover_url() {
            return cover_url;
        }

        public void setCover_url(String cover_url) {
            this.cover_url = cover_url;
        }

        public String getDes() {
            return des;
        }

        public void setDes(String des) {
            this.des = des;
        }

        public Locat getLocation() {
            return location;
        }

        public void setLocation(Locat location) {
            this.location = location;
        }

        public List<SceneProductItem> getProduct() {
            return product;
        }

        public void setProduct(List<SceneProductItem> product) {
            this.product = product;
        }

        public String getScene_id() {
            return scene_id;
        }

        public void setScene_id(String scene_id) {
            this.scene_id = scene_id;
        }

        public String getScene_title() {
            return scene_title;
        }

        public void setScene_title(String scene_title) {
            this.scene_title = scene_title;
        }

        public List<String> getTag_titles() {
            return tag_titles;
        }

        public void setTag_titles(List<String> tag_titles) {
            this.tag_titles = tag_titles;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }
    }

    public static class UserInfo implements Serializable {
        private String user_id;
        private String account;
        private String nickname;
        private String avatar_url;
        private String summary;
        //        "counter": {
//            "message_count": 0,
//                    "notice_count": 0,
//                    "alert_count": 0,
//                    "fans_count": 0,
//                    "comment_count": 0,
//                    "people_count": 0
//        },
        private String follow_count;
        private String fans_count;
        private String love_count;
        private int is_expert;
        private String expert_label;
        private String expert_info;

        public int getIs_expert() {
            return is_expert;
        }

        public String getExpert_info() {
            return expert_info;
        }

        public void setExpert_info(String expert_info) {
            this.expert_info = expert_info;
        }

        public String getExpert_label() {
            return expert_label;
        }

        public void setExpert_label(String expert_label) {
            this.expert_label = expert_label;
        }

        public void setIs_expert(int is_expert) {
            this.is_expert = is_expert;
        }

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        public String getAvatar_url() {
            return avatar_url;
        }

        public void setAvatar_url(String avatar_url) {
            this.avatar_url = avatar_url;
        }

        public String getFans_count() {
            return fans_count;
        }

        public void setFans_count(String fans_count) {
            this.fans_count = fans_count;
        }

        public String getFollow_count() {
            return follow_count;
        }

        public void setFollow_count(String follow_count) {
            this.follow_count = follow_count;
        }

        public String getLove_count() {
            return love_count;
        }

        public void setLove_count(String love_count) {
            this.love_count = love_count;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getSummary() {
            return summary;
        }

        public void setSummary(String summary) {
            this.summary = summary;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

    }

    public static class Locat implements Serializable {
        private List<Double> coordinates;

        public List<Double> getCoordinates() {
            return coordinates;
        }

        public void setCoordinates(List<Double> coordinates) {
            this.coordinates = coordinates;
        }
    }

    public static class SceneProductItem implements Serializable {
        private String id;
        private String title;
        private String price;
        private double x;
        private double y;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public double getX() {
            return x;
        }

        public void setX(double x) {
            this.x = x;
        }

        public double getY() {
            return y;
        }

        public void setY(double y) {
            this.y = y;
        }
    }
}
