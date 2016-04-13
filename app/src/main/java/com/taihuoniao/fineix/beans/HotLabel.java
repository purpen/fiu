package com.taihuoniao.fineix.beans;

import com.taihuoniao.fineix.base.NetBean;

import java.util.List;

/**
 * Created by taihuoniao on 2016/4/8.
 */
public class HotLabel extends NetBean {
        private List<HotLabelBean> hotLabelBeanList;

    public List<HotLabelBean> getHotLabelBeanList() {
        return hotLabelBeanList;
    }

    public void setHotLabelBeanList(List<HotLabelBean> hotLabelBeanList) {
        this.hotLabelBeanList = hotLabelBeanList;
    }
}
