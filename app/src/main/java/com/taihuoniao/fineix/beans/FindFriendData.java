package com.taihuoniao.fineix.beans;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author lilin
 * created at 2016/5/8 19:04
 */
public class FindFriendData implements Serializable{
    public ArrayList<User> users;

    public class User implements Serializable{
        public long _id;
        public String nickname;
        public String sex;
        public String medium_avatar_url;
        public int is_love;
        public ArrayList<CJItem> scene_sight;
        public String summary;
        public ArrayList<String> areas;
    }

    public class CJItem implements Serializable{
        public String _id;
        public String title;
        public String address;
        public String cover_url;
    }
}
