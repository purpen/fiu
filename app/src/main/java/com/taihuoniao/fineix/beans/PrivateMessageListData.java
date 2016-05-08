package com.taihuoniao.fineix.beans;

import java.io.Serializable;
import java.util.List;

/**
 * 私信列表
 * @author lilin
 * created at 2016/5/7 0:06
 */
public class PrivateMessageListData implements Serializable{
    public int total_rows;
    public List<RowItem> rows;

    public class RowItem{
        public String last_time_at;
        public Users users;
        public LastContent last_content;
        public int is_read;
    }

    public class Users implements Serializable{
        public User from_user;
        public User to_user;
    }

    public class  User implements Serializable{
        public long id;
        public String account;
        public String nickname;
        public String big_avatar_url;
    }

    public class LastContent{
        public String content;
    }
}
