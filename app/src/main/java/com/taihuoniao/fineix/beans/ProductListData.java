package com.taihuoniao.fineix.beans;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author lilin
 * created at 2016/4/22 15:00
 */
public class ProductListData implements Serializable{
    public int total_rows;
    public ArrayList<ProductListBean> rows;
}
