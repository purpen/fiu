package com.taihuoniao.fineix.beans;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author lilin
 *         created at 2016/4/7 19:52
 */
public class Location implements Serializable {
    public String type;
    public ArrayList<Double> coordinates;
}
