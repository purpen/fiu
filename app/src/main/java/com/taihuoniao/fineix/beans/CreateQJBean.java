package com.taihuoniao.fineix.beans;

import com.taihuoniao.fineix.base.NetBean;

/**
 * Created by taihuoniao on 2016/8/18.
 */
public class CreateQJBean extends NetBean {

    /**
     * is_error : false
     * data : {"id":374,"current_user_id":924789}
     */

    private boolean is_error;
    /**
     * id : 374
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
        private String id;
        private String current_user_id;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCurrent_user_id() {
            return current_user_id;
        }

        public void setCurrent_user_id(String current_user_id) {
            this.current_user_id = current_user_id;
        }
    }
}
