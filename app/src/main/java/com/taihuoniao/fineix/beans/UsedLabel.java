package com.taihuoniao.fineix.beans;

import com.taihuoniao.fineix.base.NetBean;

import java.util.List;

/**
 * Created by taihuoniao on 2016/4/8.
 */
public class UsedLabel extends NetBean {
    private int has_tag;
    private List<UsedLabelBean> usedLabelList;


    public int getHas_tag() {
        return has_tag;
    }

    public void setHas_tag(int has_tag) {
        this.has_tag = has_tag;
    }

    public List<UsedLabelBean> getUsedLabelList() {
        return usedLabelList;
    }

    public void setUsedLabelList(List<UsedLabelBean> usedLabelList) {
        this.usedLabelList = usedLabelList;
    }
}

