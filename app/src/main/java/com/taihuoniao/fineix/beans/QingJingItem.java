package com.taihuoniao.fineix.beans;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author lilin
 *         created at 2016/4/7 19:42
 */
public class QingJingItem implements Serializable {
    public int _id;
    public String title;
    public int user_id;
    public String des;
    public ArrayList sight;
    public ArrayList<Integer> tags;
    public Location location;
    public String address;
    public int used_count;
    public int view_count;
    public int subscription_count;
    public int love_count;
    public int comment_count;
    public int is_check;
    public int stick;
    public int status;
    public long created_on;
    public long updated_on;
    public String cover_url;

    @Override
    public String toString() {
        return "QingJingItem{" +
                "_id=" + _id +
                ", title='" + title + '\'' +
                '}';
    }
}
