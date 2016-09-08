package com.taihuoniao.fineix.beans;

import java.io.Serializable;

/**
 * @author lilin
 * created at 2016/4/21 14:11
 */
public class Banner implements Serializable{
        public long _id;
        public String title;
        public int space_id;
        public String sub_title;
        public String web_url;
        public String summary;
        public String cover_id;
        public int type;
        public int ordby;
        public int kind;
        public long created_on;
        public int state;
        public String cover_url;

    @Override
    public String toString() {
        return "Banner{" +
                "_id=" + _id +
                ", title='" + title + '\'' +
                ", space_id=" + space_id +
                ", sub_title='" + sub_title + '\'' +
                ", web_url='" + web_url + '\'' +
                ", summary='" + summary + '\'' +
                ", cover_id='" + cover_id + '\'' +
                ", type=" + type +
                ", ordby=" + ordby +
                ", kind=" + kind +
                ", created_on=" + created_on +
                ", state=" + state +
                ", cover_url='" + cover_url + '\'' +
                '}';
    }
}
