package com.taihuoniao.fineix.beans;

import java.util.List;

/**
 * Created by taihuoniao on 2016/8/21.
 */
public class SubjectListBean extends NetBean {

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
        private String total_rows;
        private String total_page;
        private String current_page;
        private String pager;
        private String next_page;
        private String prev_page;
        private String current_user_id;

        private List<RowsBean> rows;

        public String getTotal_rows() {
            return total_rows;
        }

        public void setTotal_rows(String total_rows) {
            this.total_rows = total_rows;
        }

        public String getTotal_page() {
            return total_page;
        }

        public void setTotal_page(String total_page) {
            this.total_page = total_page;
        }

        public String getCurrent_page() {
            return current_page;
        }

        public void setCurrent_page(String current_page) {
            this.current_page = current_page;
        }

        public String getPager() {
            return pager;
        }

        public void setPager(String pager) {
            this.pager = pager;
        }

        public String getNext_page() {
            return next_page;
        }

        public void setNext_page(String next_page) {
            this.next_page = next_page;
        }

        public String getPrev_page() {
            return prev_page;
        }

        public void setPrev_page(String prev_page) {
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
            private String category_id;
            private String publish;
            private int type;
            private String cover_id;
            private String banner_id;
            private String summary;
            private String user_id;
            private String evt;
            private String kind;
            private String stick;
            private String fine;
            private int stick_on;
            private String fine_on;
            private String status;
            private String view_count;
            private String comment_count;
            private String love_count;
            private String favorite_count;
            private int attend_count;
            private int share_count;
            private String cover_url;
            private String begin_time_at;
            private String end_time_at;
            private List<String> tags;
            private List<Integer> product_ids;
            /**
             * _id : 1046252893
             * title : 双子座手机壳
             * banner_url : null
             * summary : 一款让iPhone双卡双待的手机壳
             * sale_price : 199
             */

            private List<ProductsBean> products;

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

            public String getCategory_id() {
                return category_id;
            }

            public void setCategory_id(String category_id) {
                this.category_id = category_id;
            }

            public String getPublish() {
                return publish;
            }

            public void setPublish(String publish) {
                this.publish = publish;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public String getCover_id() {
                return cover_id;
            }

            public void setCover_id(String cover_id) {
                this.cover_id = cover_id;
            }

            public String getBanner_id() {
                return banner_id;
            }

            public void setBanner_id(String banner_id) {
                this.banner_id = banner_id;
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

            public String getEvt() {
                return evt;
            }

            public void setEvt(String evt) {
                this.evt = evt;
            }

            public String getKind() {
                return kind;
            }

            public void setKind(String kind) {
                this.kind = kind;
            }

            public String getStick() {
                return stick;
            }

            public void setStick(String stick) {
                this.stick = stick;
            }

            public String getFine() {
                return fine;
            }

            public void setFine(String fine) {
                this.fine = fine;
            }

            public String getFine_on() {
                return fine_on;
            }

            public void setFine_on(String fine_on) {
                this.fine_on = fine_on;
            }


            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
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

            public int getAttend_count() {
                return attend_count;
            }

            public void setAttend_count(int attend_count) {
                this.attend_count = attend_count;
            }

            public int getShare_count() {
                return share_count;
            }

            public void setShare_count(int share_count) {
                this.share_count = share_count;
            }

            public int getStick_on() {
                return stick_on;
            }

            public void setStick_on(int stick_on) {
                this.stick_on = stick_on;
            }

            public String getCover_url() {
                return cover_url;
            }

            public void setCover_url(String cover_url) {
                this.cover_url = cover_url;
            }

            public String getBegin_time_at() {
                return begin_time_at;
            }

            public void setBegin_time_at(String begin_time_at) {
                this.begin_time_at = begin_time_at;
            }

            public String getEnd_time_at() {
                return end_time_at;
            }

            public void setEnd_time_at(String end_time_at) {
                this.end_time_at = end_time_at;
            }

            public List<String> getTags() {
                return tags;
            }

            public void setTags(List<String> tags) {
                this.tags = tags;
            }

            public List<Integer> getProduct_ids() {
                return product_ids;
            }

            public void setProduct_ids(List<Integer> product_ids) {
                this.product_ids = product_ids;
            }

            public List<ProductsBean> getProducts() {
                return products;
            }

            public void setProducts(List<ProductsBean> products) {
                this.products = products;
            }

            public static class ProductsBean {
                private String _id;
                private String title;
                private String banner_url;
                private String summary;
                private String sale_price;

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

                public String getBanner_url() {
                    return banner_url;
                }

                public void setBanner_url(String banner_url) {
                    this.banner_url = banner_url;
                }

                public String getSummary() {
                    return summary;
                }

                public void setSummary(String summary) {
                    this.summary = summary;
                }

                public String getSale_price() {
                    return sale_price;
                }

                public void setSale_price(String sale_price) {
                    this.sale_price = sale_price;
                }
            }
        }
    }
}
