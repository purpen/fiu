package com.taihuoniao.fineix.beans;

import java.io.Serializable;
import java.util.List;

/**
 * @author lilin
 * created at 2016/5/7 15:14
 */
public class MessageDetailData implements Serializable{
    public String _id;
    public List<MessageItem> mailbox;
    public class MessageItem implements Serializable{
        public String r_id;
        public String content;
        public String created_at;
        public int user_type;
    }
}
