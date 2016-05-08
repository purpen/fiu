package com.taihuoniao.fineix.beans;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author lilin
 * created at 2016/5/8 19:04
 */
public class FindFriendData implements Serializable{
    public int total_rows;
    public ArrayList<FindFriendItem> rows;
    public class FindFriendItem implements Serializable{
        public long _id;
        public String nickname;
        public String medium_avatar_url;
        public int is_love;
        public ArrayList<FriendSceneItem> scene;
    }
    public class FriendSceneItem implements Serializable{
        public int _id;
        public String title;
        public String address;
        public String cover_url;
    }
}
