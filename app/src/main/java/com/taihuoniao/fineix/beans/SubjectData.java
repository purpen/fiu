package com.taihuoniao.fineix.beans;

import java.io.Serializable;
import java.util.List;

/**
 * @author lilin
 *         created at 2016/6/15 23:33
 */
public class SubjectData implements Serializable {
    public int _id;
    public String title;
    public String tags_s;
    public int kind;
    public String cover_id;
    public int category_id;
    public String summary;
    public int status;
    public int publish;
    public int user_id;
    public int stick;
    public int love_count;
    public int favorite_count;
    public int view_count;
    public int comment_count;
    public String cover_url;
    public int is_love;
    public String content_view_url;
    public String share_view_url;
    public String share_desc;
    public List<String> tags;
}
