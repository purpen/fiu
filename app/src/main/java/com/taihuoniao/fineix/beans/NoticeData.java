package com.taihuoniao.fineix.beans;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author lilin
 * created at 2016/5/10 17:10
 */
public class NoticeData implements Serializable{
    public int total_rows;
    public ArrayList<NoticeItem> rows;
    public class NoticeItem implements Serializable{
        public int kind;
        public int related_id;
        public int is_read;
        public Who s_user;
        public String target_cover_url;
        public String info;
        public String kind_str;
        public String created_at;
    }

    public class Who{
        public long _id;
        public String nickname;
        public String medium_avatar_url;
    }
}
