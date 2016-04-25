package com.taihuoniao.fineix.beans;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author lilin
 * created at 2016/4/22 17:44
 */
public class FocusFansData implements Serializable {
    public int total_rows;
    public ArrayList<FocusFansItem> rows;
    public int total_page;
    public int current_page;
    public String pager;
    public int next_page;
    public int prev_page;
    public int current_user_id;
}
