package com.taihuoniao.fineix.beans;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author lilin
 * created at 2016/4/7 19:40
 */
public class QingJingData implements Serializable {
    public int total_rows;
    public ArrayList<QingJingItem> rows;
}
