package com.taihuoniao.fineix.beans;

import java.io.Serializable;

/**
 * @author lilin
 *         created at 2016/4/22 1746
 */
public class FocusFansItem implements Serializable {
    public int _id;
    public int user_id;
    public int follow_id;
    public int group_id;
    public int type;
    public boolean focus_flag;
    public int is_read;
    public Follow follows;
    public class Follow implements Serializable {
        public String user_id;
        public String account;
        public String nickname;
        public String avatar_url;
        public String summary;
        public int is_love;
        public int is_expert;
        public String label;
        public String expert_label;
        public String expert_info;
    }
}
