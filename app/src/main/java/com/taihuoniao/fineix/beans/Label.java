package com.taihuoniao.fineix.beans;

import java.io.Serializable;
import java.util.List;

/**
 * Created by taihuoniao on 2016/5/17.
 */
public class Label implements Serializable{
    private List<UsedLabelBean> usedLabelList;
    private List<HotLabel.HotLabelBean> hotLabelList;

    public Label(List<HotLabel.HotLabelBean> hotLabelList, List<UsedLabelBean> usedLabelList) {
        this.hotLabelList = hotLabelList;
        this.usedLabelList = usedLabelList;
    }

    public List<HotLabel.HotLabelBean> getHotLabelList() {
        return hotLabelList;
    }

    public void setHotLabelList(List<HotLabel.HotLabelBean> hotLabelList) {
        this.hotLabelList = hotLabelList;
    }

    public List<UsedLabelBean> getUsedLabelList() {
        return usedLabelList;
    }

    public void setUsedLabelList(List<UsedLabelBean> usedLabelList) {
        this.usedLabelList = usedLabelList;
    }
}
