package com.taihuoniao.fineix.beans;

import com.taihuoniao.fineix.base.NetBean;

import java.util.List;

/**
 * Created by taihuoniao on 2016/7/15.
 */
public class SearchLabelBean extends NetBean {

    /**
     * is_error : false
     * data : {"word":["鼓舞","满足"],"current_user_id":20448}
     */

    private boolean is_error;
    /**
     * word : ["鼓舞","满足"]
     * current_user_id : 20448
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
        private int current_user_id;
        private List<String> word;

        public int getCurrent_user_id() {
            return current_user_id;
        }

        public void setCurrent_user_id(int current_user_id) {
            this.current_user_id = current_user_id;
        }

        public List<String> getWord() {
            return word;
        }

        public void setWord(List<String> word) {
            this.word = word;
        }
    }
}
