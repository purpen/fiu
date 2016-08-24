package com.taihuoniao.fineix.beans;

import com.taihuoniao.fineix.base.NetBean;

import java.util.List;

/**
 * Created by taihuoniao on 2016/8/23.
 */
public class FirstProductBean extends NetBean {


    private boolean is_error;
    private DataBean data;

    public boolean isIs_error() {
        return is_error;
    }

    public void setIs_error(boolean is_error) {
        this.is_error = is_error;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private int total_rows;
        private int total_page;
        private int current_page;
        private String pager;
        private int next_page;
        private int prev_page;
        private String current_user_id;
        private List<RowsBean> rows;

        public int getTotal_rows() {
            return total_rows;
        }

        public void setTotal_rows(int total_rows) {
            this.total_rows = total_rows;
        }

        public int getTotal_page() {
            return total_page;
        }

        public void setTotal_page(int total_page) {
            this.total_page = total_page;
        }

        public int getCurrent_page() {
            return current_page;
        }

        public void setCurrent_page(int current_page) {
            this.current_page = current_page;
        }

        public String getPager() {
            return pager;
        }

        public void setPager(String pager) {
            this.pager = pager;
        }

        public int getNext_page() {
            return next_page;
        }

        public void setNext_page(int next_page) {
            this.next_page = next_page;
        }

        public int getPrev_page() {
            return prev_page;
        }

        public void setPrev_page(int prev_page) {
            this.prev_page = prev_page;
        }

        public String getCurrent_user_id() {
            return current_user_id;
        }

        public void setCurrent_user_id(String current_user_id) {
            this.current_user_id = current_user_id;
        }

        public List<RowsBean> getRows() {
            return rows;
        }

        public void setRows(List<RowsBean> rows) {
            this.rows = rows;
        }

        public static class RowsBean {
            private String _id;
            private String title;
            private String short_title;
            private String advantage;
            private String sale_price;
            private String market_price;
            private String presale_people;
            private String tags_s;
            private String created_on;
            private String updated_on;
            private String presale_percent;
            private String cover_id;
            private String category_id;
            private String stage;
            private String vote_favor_count;
            private String vote_oppose_count;
            private String summary;
            private String succeed;
            private String voted_finish_time;
            private String presale_finish_time;
            private boolean snatched_time;
            private String inventory;
            private String topic_count;
            private String presale_money;
            private String snatched;
            private String presale_goals;
            private String stick;
            private String featured;
            private String love_count;
            private String favorite_count;
            private String view_count;
            private String comment_count;
            private String comment_star;
            private boolean snatched_end_time;
            private String snatched_price;
            private String snatched_count;
            private String tips_label;
            private String app_snatched;
            private boolean app_snatched_time;
            private boolean app_snatched_end_time;
            private String app_snatched_price;
            private String app_snatched_count;
            private String app_appoint_count;
            private String app_snatched_total_count;
            private String cover_url;
            private String content_view_url;
            private String is_app_snatched;
            private List<String> tags;
            private List<String> category_tags;

            public String get_id() {
                return _id;
            }

            public void set_id(String _id) {
                this._id = _id;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getShort_title() {
                return short_title;
            }

            public void setShort_title(String short_title) {
                this.short_title = short_title;
            }

            public String getAdvantage() {
                return advantage;
            }

            public void setAdvantage(String advantage) {
                this.advantage = advantage;
            }

            public String getSale_price() {
                return sale_price;
            }

            public void setSale_price(String sale_price) {
                this.sale_price = sale_price;
            }

            public String getMarket_price() {
                return market_price;
            }

            public void setMarket_price(String market_price) {
                this.market_price = market_price;
            }

            public String getPresale_people() {
                return presale_people;
            }

            public void setPresale_people(String presale_people) {
                this.presale_people = presale_people;
            }

            public String getTags_s() {
                return tags_s;
            }

            public void setTags_s(String tags_s) {
                this.tags_s = tags_s;
            }

            public String getCreated_on() {
                return created_on;
            }

            public void setCreated_on(String created_on) {
                this.created_on = created_on;
            }

            public String getUpdated_on() {
                return updated_on;
            }

            public void setUpdated_on(String updated_on) {
                this.updated_on = updated_on;
            }

            public String getPresale_percent() {
                return presale_percent;
            }

            public void setPresale_percent(String presale_percent) {
                this.presale_percent = presale_percent;
            }

            public String getCover_id() {
                return cover_id;
            }

            public void setCover_id(String cover_id) {
                this.cover_id = cover_id;
            }

            public String getCategory_id() {
                return category_id;
            }

            public void setCategory_id(String category_id) {
                this.category_id = category_id;
            }

            public String getStage() {
                return stage;
            }

            public void setStage(String stage) {
                this.stage = stage;
            }

            public String getVote_favor_count() {
                return vote_favor_count;
            }

            public void setVote_favor_count(String vote_favor_count) {
                this.vote_favor_count = vote_favor_count;
            }

            public String getVote_oppose_count() {
                return vote_oppose_count;
            }

            public void setVote_oppose_count(String vote_oppose_count) {
                this.vote_oppose_count = vote_oppose_count;
            }

            public String getSummary() {
                return summary;
            }

            public void setSummary(String summary) {
                this.summary = summary;
            }

            public String getSucceed() {
                return succeed;
            }

            public void setSucceed(String succeed) {
                this.succeed = succeed;
            }

            public String getVoted_finish_time() {
                return voted_finish_time;
            }

            public void setVoted_finish_time(String voted_finish_time) {
                this.voted_finish_time = voted_finish_time;
            }

            public String getPresale_finish_time() {
                return presale_finish_time;
            }

            public void setPresale_finish_time(String presale_finish_time) {
                this.presale_finish_time = presale_finish_time;
            }

            public boolean isSnatched_time() {
                return snatched_time;
            }

            public void setSnatched_time(boolean snatched_time) {
                this.snatched_time = snatched_time;
            }

            public String getInventory() {
                return inventory;
            }

            public void setInventory(String inventory) {
                this.inventory = inventory;
            }

            public String getTopic_count() {
                return topic_count;
            }

            public void setTopic_count(String topic_count) {
                this.topic_count = topic_count;
            }

            public String getPresale_money() {
                return presale_money;
            }

            public void setPresale_money(String presale_money) {
                this.presale_money = presale_money;
            }

            public String getSnatched() {
                return snatched;
            }

            public void setSnatched(String snatched) {
                this.snatched = snatched;
            }

            public String getPresale_goals() {
                return presale_goals;
            }

            public void setPresale_goals(String presale_goals) {
                this.presale_goals = presale_goals;
            }

            public String getStick() {
                return stick;
            }

            public void setStick(String stick) {
                this.stick = stick;
            }

            public String getFeatured() {
                return featured;
            }

            public void setFeatured(String featured) {
                this.featured = featured;
            }

            public String getLove_count() {
                return love_count;
            }

            public void setLove_count(String love_count) {
                this.love_count = love_count;
            }

            public String getFavorite_count() {
                return favorite_count;
            }

            public void setFavorite_count(String favorite_count) {
                this.favorite_count = favorite_count;
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

            public String getComment_star() {
                return comment_star;
            }

            public void setComment_star(String comment_star) {
                this.comment_star = comment_star;
            }

            public boolean isSnatched_end_time() {
                return snatched_end_time;
            }

            public void setSnatched_end_time(boolean snatched_end_time) {
                this.snatched_end_time = snatched_end_time;
            }

            public String getSnatched_price() {
                return snatched_price;
            }

            public void setSnatched_price(String snatched_price) {
                this.snatched_price = snatched_price;
            }

            public String getSnatched_count() {
                return snatched_count;
            }

            public void setSnatched_count(String snatched_count) {
                this.snatched_count = snatched_count;
            }

            public String getTips_label() {
                return tips_label;
            }

            public void setTips_label(String tips_label) {
                this.tips_label = tips_label;
            }

            public String getApp_snatched() {
                return app_snatched;
            }

            public void setApp_snatched(String app_snatched) {
                this.app_snatched = app_snatched;
            }

            public boolean isApp_snatched_time() {
                return app_snatched_time;
            }

            public void setApp_snatched_time(boolean app_snatched_time) {
                this.app_snatched_time = app_snatched_time;
            }

            public boolean isApp_snatched_end_time() {
                return app_snatched_end_time;
            }

            public void setApp_snatched_end_time(boolean app_snatched_end_time) {
                this.app_snatched_end_time = app_snatched_end_time;
            }

            public String getApp_snatched_price() {
                return app_snatched_price;
            }

            public void setApp_snatched_price(String app_snatched_price) {
                this.app_snatched_price = app_snatched_price;
            }

            public String getApp_snatched_count() {
                return app_snatched_count;
            }

            public void setApp_snatched_count(String app_snatched_count) {
                this.app_snatched_count = app_snatched_count;
            }

            public String getApp_appoint_count() {
                return app_appoint_count;
            }

            public void setApp_appoint_count(String app_appoint_count) {
                this.app_appoint_count = app_appoint_count;
            }

            public String getApp_snatched_total_count() {
                return app_snatched_total_count;
            }

            public void setApp_snatched_total_count(String app_snatched_total_count) {
                this.app_snatched_total_count = app_snatched_total_count;
            }

            public String getCover_url() {
                return cover_url;
            }

            public void setCover_url(String cover_url) {
                this.cover_url = cover_url;
            }

            public String getContent_view_url() {
                return content_view_url;
            }

            public void setContent_view_url(String content_view_url) {
                this.content_view_url = content_view_url;
            }

            public String getIs_app_snatched() {
                return is_app_snatched;
            }

            public void setIs_app_snatched(String is_app_snatched) {
                this.is_app_snatched = is_app_snatched;
            }

            public List<String> getTags() {
                return tags;
            }

            public void setTags(List<String> tags) {
                this.tags = tags;
            }

            public List<String> getCategory_tags() {
                return category_tags;
            }

            public void setCategory_tags(List<String> category_tags) {
                this.category_tags = category_tags;
            }
        }
    }
}
