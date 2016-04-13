package com.taihuoniao.fineix.beans;

import com.taihuoniao.fineix.base.NetBean;

import java.util.List;

/**
 * Created by taihuoniao on 2016/4/13.
 */
public class ProductBean extends NetBean {
    private List<ProductListBean> list;

    public List<ProductListBean> getList() {
        return list;
    }

    public void setList(List<ProductListBean> list) {
        this.list = list;
    }
}
