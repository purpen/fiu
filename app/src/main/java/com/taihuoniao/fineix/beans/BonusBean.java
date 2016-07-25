package com.taihuoniao.fineix.beans;

import com.taihuoniao.fineix.base.NetBean;

/**
 * Created by taihuoniao on 2016/7/19.
 */
public class BonusBean extends NetBean {

    /**
     * is_error : false
     * data : {"exp":5,"current_user_id":955832}
     */

    private boolean is_error;
    /**
     * exp : 5
     * current_user_id : 955832
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
        private int exp;
        private int current_user_id;

        public int getExp() {
            return exp;
        }

        public void setExp(int exp) {
            this.exp = exp;
        }

        public int getCurrent_user_id() {
            return current_user_id;
        }

        public void setCurrent_user_id(int current_user_id) {
            this.current_user_id = current_user_id;
        }
    }
}
