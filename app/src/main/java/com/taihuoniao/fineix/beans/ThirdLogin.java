package com.taihuoniao.fineix.beans;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by android on 2016/3/19.
 */
public class ThirdLogin implements Serializable {
    public int has_user;

    public User user;

    public class User implements Serializable{
        public long _id;
        public String  nickname;
        public String sex;
        public String summary;
        public String birthday;
        public String medium_avatar_url;
        public LoginInfo.Identify identify;
        public ArrayList<String> areas;
    }

}
