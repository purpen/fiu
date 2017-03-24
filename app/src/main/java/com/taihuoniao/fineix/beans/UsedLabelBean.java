package com.taihuoniao.fineix.beans;

import java.util.List;

/**
 * Created by taihuoniao on 2016/8/18.
 */
public class UsedLabelBean {

    private int has_tag;
    private int current_user_id;
    private List<String> tags;

    public int getHas_tag() {
        return has_tag;
    }

    public void setHas_tag(int has_tag) {
        this.has_tag = has_tag;
    }

    public int getCurrent_user_id() {
        return current_user_id;
    }

    public void setCurrent_user_id(int current_user_id) {
        this.current_user_id = current_user_id;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }
}
