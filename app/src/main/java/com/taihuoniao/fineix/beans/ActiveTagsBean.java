package com.taihuoniao.fineix.beans;

import com.taihuoniao.fineix.base.NetBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by taihuoniao on 2016/8/18.
 */
public class ActiveTagsBean extends NetBean implements Serializable{


    /**
     * is_error : false
     * data : {"items":[["一次有意义的旅行","3"],["最爱的美食","2"]],"current_user_id":955832}
     */

    private boolean is_error;
    /**
     * items : [["一次有意义的旅行","3"],["最爱的美食","2"]]
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

    public static class DataBean implements Serializable {
        private String current_user_id;
        private List<List<String>> items;

        public String getCurrent_user_id() {
            return current_user_id;
        }

        public void setCurrent_user_id(String current_user_id) {
            this.current_user_id = current_user_id;
        }

        public List<List<String>> getItems() {
            return items;
        }

        public void setItems(List<List<String>> items) {
            this.items = items;
        }
    }
}
