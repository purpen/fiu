package com.taihuoniao.fineix.beans;

import com.taihuoniao.fineix.base.NetBean;

import java.util.List;

/**
 * Created by taihuoniao on 2016/4/20.
 */
public class SceneDetails extends NetBean {
    private String _id;
    private String title;
    private String user_id;
    private String des;
    private String scene_id;
    private List<Integer> tags;
    private List<String> tag_titles;
    private List<Product> product;
    private String address;
    private String used_count;
    private String view_count;
    private String love_count;
    private String comment_count;
    private String fine;
    private String is_check;
    private String status;
    private String created_on;
    private String updated_on;
    private String cover_url;
    private String scene_title;
    private UserInfo user_info;

    public List<String> getTag_titles() {
        return tag_titles;
    }

    public void setTag_titles(List<String> tag_titles) {
        this.tag_titles = tag_titles;
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

    public String getComment_count() {
        return comment_count;
    }

    public void setComment_count(String comment_count) {
        this.comment_count = comment_count;
    }

    public String getCover_url() {
        return cover_url;
    }

    public void setCover_url(String cover_url) {
        this.cover_url = cover_url;
    }

    public String getCreated_on() {
        return created_on;
    }

    public void setCreated_on(String created_on) {
        this.created_on = created_on;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getLove_count() {
        return love_count;
    }

    public void setLove_count(String love_count) {
        this.love_count = love_count;
    }

    public List<Product> getProduct() {
        return product;
    }

    public void setProduct(List<Product> product) {
        this.product = product;
    }

    public String getScene_title() {
        return scene_title;
    }

    public void setScene_title(String scene_title) {
        this.scene_title = scene_title;
    }

    public List<Integer> getTags() {
        return tags;
    }

    public void setTags(List<Integer> tags) {
        this.tags = tags;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public UserInfo getUser_info() {
        return user_info;
    }

    public void setUser_info(UserInfo user_info) {
        this.user_info = user_info;
    }

    public String getView_count() {
        return view_count;
    }

    public void setView_count(String view_count) {
        this.view_count = view_count;
    }

    public static class UserInfo {
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
        private String user_rank;

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

        public String getUser_rank() {
            return user_rank;
        }

        public void setUser_rank(String user_rank) {
            this.user_rank = user_rank;
        }
    }

    public static class Product {
        private String id;
        private String title;
        private String price;
        private String x;
        private String y;

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

        public String getX() {
            return x;
        }

        public void setX(String x) {
            this.x = x;
        }

        public String getY() {
            return y;
        }

        public void setY(String y) {
            this.y = y;
        }
    }
//            "location": { 位置信息
//        "type": "Point",
//                "coordinates": [
//        116.403963,
//                39.915119
//        ]
//    },

}
