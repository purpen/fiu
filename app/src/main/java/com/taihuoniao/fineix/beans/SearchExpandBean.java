package com.taihuoniao.fineix.beans;

import java.util.List;

/**
 * Created by taihuoniao on 2016/8/18.
 */
public class SearchExpandBean extends NetBean {

    /**
     * is_error : false
     * data : {"success":true,"data":{"swords":["http","html","hellip","获得","合作","很大","还是","或者","很多","还有","盒子","或许","核心","孩子","会有","互联","行业","合理","合适","互动"]},"total_count":20,"total_page":1,"msg":"success","current_user_id":924789}
     */

    private boolean is_error;
    /**
     * success : true
     * data : {"swords":["http","html","hellip","获得","合作","很大","还是","或者","很多","还有","盒子","或许","核心","孩子","会有","互联","行业","合理","合适","互动"]}
     * total_count : 20
     * total_page : 1
     * msg : success
     * current_user_id : 924789
     */

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
        private boolean success;
        private InnerDataBean data;
        private int total_count;
        private int total_page;
        private String msg;
        private int current_user_id;

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }

        public InnerDataBean getData() {
            return data;
        }

        public void setData(InnerDataBean data) {
            this.data = data;
        }

        public int getTotal_count() {
            return total_count;
        }

        public void setTotal_count(int total_count) {
            this.total_count = total_count;
        }

        public int getTotal_page() {
            return total_page;
        }

        public void setTotal_page(int total_page) {
            this.total_page = total_page;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public int getCurrent_user_id() {
            return current_user_id;
        }

        public void setCurrent_user_id(int current_user_id) {
            this.current_user_id = current_user_id;
        }

        public static class InnerDataBean {
            private List<String> swords;

            public List<String> getSwords() {
                return swords;
            }

            public void setSwords(List<String> swords) {
                this.swords = swords;
            }
        }
    }
}
