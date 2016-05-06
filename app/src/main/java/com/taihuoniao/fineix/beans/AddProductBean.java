package com.taihuoniao.fineix.beans;

import com.taihuoniao.fineix.base.NetBean;

/**
 * Created by taihuoniao on 2016/5/6.
 */
public class AddProductBean extends NetBean {
    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public static class Data {
        private String id;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }
}
