package com.taihuoniao.fineix.beans;

import com.taihuoniao.fineix.network.HttpResponse;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author lilin
 *         created at 2016/5/5 23:51
 */
public class SystemNoticeData extends HttpResponse {
    public int total_rows;
    public ArrayList<SystemNoticeItem> rows;
    public long current_user_id;

    public class SystemNoticeItem implements Serializable{
        public String title;
        public String content;
        public String created_at;
        public String url;
        public int state;
        public int send_count;
    }
}
