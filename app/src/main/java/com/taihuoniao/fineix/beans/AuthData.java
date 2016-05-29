package com.taihuoniao.fineix.beans;

import java.io.Serializable;

/**
 * @author lilin
 * created at 2016/5/29 17:10
 */
public class AuthData implements Serializable{
    public int verified; //审核状态：0.未审核；1.拒绝；2.通过
}
