package com.taihuoniao.fineix.beans;

import java.io.Serializable;

/**
 * @author lilin
 * created at 2016/4/5 18:05
 */
public class City implements Serializable{
    public String id;
    public String name;
    public String image_url;
    public float lng;
    public float lat;
    public boolean is_estore;
    public boolean is_scene;
}
