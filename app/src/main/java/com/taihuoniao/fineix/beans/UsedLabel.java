package com.taihuoniao.fineix.beans;

import com.taihuoniao.fineix.base.NetBean;

import java.util.List;

/**
 * Created by taihuoniao on 2016/4/8.
 */
public class UsedLabel extends NetBean {
    private List<UsedLabelBean> usedLabelList;

    public List<UsedLabelBean> getUsedLabelList() {

        return usedLabelList;
    }

    public void setUsedLabelList(List<UsedLabelBean> usedLabelList) {
        this.usedLabelList = usedLabelList;
    }
}

