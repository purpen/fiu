package com.taihuoniao.fineix.beans;

import java.io.Serializable;

/**
 * @author lilin
 * created at 2016/4/7 19:52
 */
public class QJLoation implements Serializable {
    public QJCoordinate coordinates;
    public class QJCoordinate{
        public String lat;
        public String lng;
    }
}
