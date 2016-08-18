package com.taihuoniao.fineix.beans;

import com.taihuoniao.fineix.base.NetBean;

/**
 * Created by taihuoniao on 2016/8/18.
 */
public class AddBrandBean extends NetBean {

    /**
     * is_error : false
     * data : {"id":"57b55b853ffca26a0c8b4a27","current_user_id":924789}
     */

    private boolean is_error;
    /**
     * id : 57b55b853ffca26a0c8b4a27
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
        private int current_user_id;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public int getCurrent_user_id() {
            return current_user_id;
        }

        public void setCurrent_user_id(int current_user_id) {
            this.current_user_id = current_user_id;
        }
    }
}
