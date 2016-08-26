package com.taihuoniao.fineix.beans;

import com.taihuoniao.fineix.base.NetBean;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by taihuoniao on 2016/5/7.
 */
public class SearchBean extends NetBean {
    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public static class Data {
        private int current_page;
        private List<SearchItem> rows;

        public int getCurrent_page() {
            return current_page;
        }

        public void setCurrent_page(int current_page) {
            this.current_page = current_page;
        }

        public List<SearchItem> getRows() {
            return rows;
        }

        public void setRows(List<SearchItem> rows) {
            this.rows = rows;
        }

        public static class SearchItem implements Serializable {
            //情景
            public int pos;
            public boolean isSelect;
            private String address;
            private User user_info;
            private String view_count;
            private String love_count;
            private String scene_title;
            private String created_at;
            private List<String> banners;
            private String attrbute;
            private String des;
            private int is_love;
            //用户
            private String nickname;
            private String summary;
            private String avatar_url;
            private int is_export;
            private int is_follow;
            //产品
            private double market_price;
            private double sale_price;
            private String tips_label;
            //品牌
            //主题
            private String pid;
            private String oid;
            private String tid;
            private String cid;
            private String kind;
            private String title;
            private String cover_id;
            private String content;
            private String user_id;
            private List<String> tags;
            private String created_on;
            private String updated_on;
            private String high_title;
            private String high_content;
            private String _id;
            private String cover_url;
            private String kind_name;
            private String asset_type;
            private String short_title;
            private int type;

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public String getShort_title() {
                return short_title;
            }

            public void setShort_title(String short_title) {
                this.short_title = short_title;
            }

            public String getTips_label() {
                return tips_label;
            }

            public void setTips_label(String tips_label) {
                this.tips_label = tips_label;
            }

            public int getIs_export() {
                return is_export;
            }

            public void setIs_export(int is_export) {
                this.is_export = is_export;
            }

            public int getIs_follow() {
                return is_follow;
            }

            public void setIs_follow(int is_follow) {
                this.is_follow = is_follow;
            }

            public String getAsset_type() {
                return asset_type;
            }

            public void setAsset_type(String asset_type) {
                this.asset_type = asset_type;
            }

            public String getAvatar_url() {
                return avatar_url;
            }

            public void setAvatar_url(String avatar_url) {
                this.avatar_url = avatar_url;
            }

            public String getCid() {
                return cid;
            }

            public void setCid(String cid) {
                this.cid = cid;
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

            public String getHigh_content() {
                return high_content;
            }

            public void setHigh_content(String high_content) {
                this.high_content = high_content;
            }

            public String getHigh_title() {
                return high_title;
            }

            public void setHigh_title(String high_title) {
                this.high_title = high_title;
            }

            public void setSelect(boolean select) {
                isSelect = select;
            }

            public String getKind() {
                return kind;
            }

            public void setKind(String kind) {
                this.kind = kind;
            }

            public String getKind_name() {
                return kind_name;
            }

            public void setKind_name(String kind_name) {
                this.kind_name = kind_name;
            }

            public String getNickname() {
                return nickname;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
            }

            public String getPid() {
                return pid;
            }

            public void setPid(String pid) {
                this.pid = pid;
            }

            public int getPos() {
                return pos;
            }

            public void setPos(int pos) {
                this.pos = pos;
            }

            public String getSummary() {
                return summary;
            }

            public void setSummary(String summary) {
                this.summary = summary;
            }

            public String getTid() {
                return tid;
            }

            public void setTid(String tid) {
                this.tid = tid;
            }

            public String getUpdated_on() {
                return updated_on;
            }

            public void setUpdated_on(String updated_on) {
                this.updated_on = updated_on;
            }

            public String getUser_id() {
                return user_id;
            }

            public void setUser_id(String user_id) {
                this.user_id = user_id;
            }

            public int getIs_love() {
                return is_love;
            }

            public void setIs_love(int is_love) {
                this.is_love = is_love;
            }

            public List<String> getTags() {
                if (tags == null) {
                    tags = new ArrayList<>();
                }
                return tags;
            }

            public void setTags(List<String> tags) {
                this.tags = tags;
            }

            public String getDes() {
                return des;
            }

            public void setDes(String des) {
                this.des = des;
            }

            public String getOid() {
                return oid;
            }

            public void setOid(String oid) {
                this.oid = oid;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public String getMarket_price() {
                return new DecimalFormat("######0.00").format(market_price);
            }

            public void setMarket_price(double market_price) {
                this.market_price = market_price;
            }

            public boolean isSelect() {
                return isSelect;
            }

            public void setIsSelect(boolean isSelect) {
                this.isSelect = isSelect;
            }

            public String getAttrbute() {
                return attrbute;
            }

            public void setAttrbute(String attrbute) {
                this.attrbute = attrbute;
            }

            public List<String> getBanners() {
                return banners;
            }

            public void setBanners(List<String> banners) {
                this.banners = banners;
            }

            public String getSale_price() {
                return new DecimalFormat("######0.00").format(sale_price);
            }

            public void setSale_price(double sale_price) {
                this.sale_price = sale_price;
            }

            public String getCreated_at() {
                return created_at;
            }

            public void setCreated_at(String created_at) {
                this.created_at = created_at;
            }

            public String getLove_count() {
                return love_count;
            }

            public void setLove_count(String love_count) {
                this.love_count = love_count;
            }

            public String getScene_title() {
                return scene_title;
            }

            public void setScene_title(String scene_title) {
                this.scene_title = scene_title;
            }

            public String getView_count() {
                return view_count;
            }

            public void setView_count(String view_count) {
                this.view_count = view_count;
            }

            public User getUser_info() {
                return user_info;
            }

            public void setUser_info(User user_info) {
                this.user_info = user_info;
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

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public static class User {
                private String user_id;
                private String nickname;
                private String avatar_url;
                private String summary;
                private int is_expert;
                private String expert_label;
                private String expert_info;

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

                public int getIs_expert() {
                    return is_expert;
                }

                public void setIs_expert(int is_expert) {
                    this.is_expert = is_expert;
                }

                public String getAvatar_url() {
                    return avatar_url;
                }

                public void setAvatar_url(String avatar_url) {
                    this.avatar_url = avatar_url;
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
        }
    }


}
