package com.taihuoniao.fineix.beans;

import java.util.List;

/**
 * Created by taihuoniao on 2016/8/18.
 */
public class UsedLabelBean extends NetBean {

    /**
     * is_error : false
     * data : {"has_tag":1,"tags":[190,197,194,189,196,191,39,35,41,42,36,37,568,573,572,567,96,44,33,45,76,78,60,40],"current_user_id":924789}
     */

    private boolean is_error;
    /**
     * has_tag : 1
     * tags : [190,197,194,189,196,191,39,35,41,42,36,37,568,573,572,567,96,44,33,45,76,78,60,40]
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
        private int has_tag;
        private int current_user_id;
        private List<String> tags;

        public int getHas_tag() {
            return has_tag;
        }

        public void setHas_tag(int has_tag) {
            this.has_tag = has_tag;
        }

        public int getCurrent_user_id() {
            return current_user_id;
        }

        public void setCurrent_user_id(int current_user_id) {
            this.current_user_id = current_user_id;
        }

        public List<String> getTags() {
            return tags;
        }

        public void setTags(List<String> tags) {
            this.tags = tags;
        }
    }
}
