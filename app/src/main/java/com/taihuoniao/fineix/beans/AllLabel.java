package com.taihuoniao.fineix.beans;

import com.taihuoniao.fineix.base.NetBean;

import java.util.List;

/**
 * Created by taihuoniao on 2016/4/11.
 */
public class AllLabel extends NetBean {
    private List<AllLabelBean> children;

    public List<AllLabelBean> getChildren() {
        return children;
    }

    public void setChildren(List<AllLabelBean> children) {
        this.children = children;
    }
}
