package com.taihuoniao.fineix.beans;

import com.taihuoniao.fineix.base.NetBean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by taihuoniao on 2016/4/13.
 */
public class CategoryBean extends NetBean implements Serializable {
    private List<CategoryListBean> list = new ArrayList<>();

    public List<CategoryListBean> getList() {
        return list;
    }

    public void setList(List<CategoryListBean> list) {
        this.list = list;
    }
}
