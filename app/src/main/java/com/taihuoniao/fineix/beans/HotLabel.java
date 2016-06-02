package com.taihuoniao.fineix.beans;

import com.taihuoniao.fineix.base.NetBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by taihuoniao on 2016/4/8.
 */
public class HotLabel extends NetBean {
    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public static class Data {
        private List<HotLabelBean> rows;

        public List<HotLabelBean> getRows() {
            return rows;
        }

        public void setRows(List<HotLabelBean> rows) {
            this.rows = rows;
        }
    }

    public static class HotLabelBean implements Serializable {
        private String _id;
        private String title_cn;
        private boolean isSelect;

        public boolean isSelect() {
            return isSelect;
        }

        public void setIsSelect(boolean isSelect) {
            this.isSelect = isSelect;
        }

        public HotLabelBean() {
        }

        public HotLabelBean(String _id, String title_cn) {
            this._id = _id;
            this.title_cn = title_cn;
        }

        public int line = 3;

        public String getTitle_cn() {
            return title_cn;
        }

        public void setTitle_cn(String title_cn) {
            this.title_cn = title_cn;
        }

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }
    }

}
