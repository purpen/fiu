package com.taihuoniao.fineix.beans;

import com.taihuoniao.fineix.base.NetBean;

/**
 * Created by taihuoniao on 2016/4/27.
 */
public class SceneLoveBean extends NetBean {
    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public static class Data {
        private int love_count;

        public int getLove_count() {
            return love_count;
        }

        public void setLove_count(int love_count) {
            this.love_count = love_count;
        }
    }
}
