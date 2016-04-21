package com.taihuoniao.fineix.beans;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author lilin
 *         created at 2016/4/21 1403
 */
public class BannerData implements Serializable {
    public int total_rows;
    public ArrayList<Banner> rows;
}
