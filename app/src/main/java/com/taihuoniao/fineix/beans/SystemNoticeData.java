package com.taihuoniao.fineix.beans;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author lilin
 *         created at 2016/5/5 23:51
 */
public class SystemNoticeData{
    public int total_rows;
    public ArrayList<SystemNoticeItem> rows;
    public long current_user_id;

    public class SystemNoticeItem implements Serializable{
        public String title;
        public String content;
        public String created_at;
        public String url;
        public String cover_url;
        public int state;
        public int evt;
        public int send_count;
    }
}
