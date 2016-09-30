package com.taihuoniao.fineix.beans;

import com.taihuoniao.fineix.base.NetBean;

/**
 * Created by taihuoniao on 2016/9/27.
 */

public class IsEditorBean extends NetBean {

    /**
     * is_error : false
     * data : {"is_editor":0,"current_user_id":924789}
     */

    private boolean is_error;
    /**
     * is_editor : 0
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
        private int is_editor;
        private String current_user_id;

        public int getIs_editor() {
            return is_editor;
        }

        public void setIs_editor(int is_editor) {
            this.is_editor = is_editor;
        }

        public String getCurrent_user_id() {
            return current_user_id;
        }

        public void setCurrent_user_id(String current_user_id) {
            this.current_user_id = current_user_id;
        }
    }
}
