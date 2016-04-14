package com.taihuoniao.fineix.beans;


import java.io.Serializable;

public class TagItem implements Serializable {
    private static final long serialVersionUID = 2685507991821634905L;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    //    private long id;
    private String id;//商品id
    private int type;//商品属于哪个商城
    private String name;
    private String price;
    private String imagePath;

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    //考虑适配的问题，纪录的是百分比坐标
    private double x = -1;
    private double y = -1;

    private int recordCount;
    private boolean left = true;


    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public boolean isLeft() {
        return left;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public int getRecordCount() {
        return recordCount;
    }

    public void setRecordCount(int recordCount) {
        this.recordCount = recordCount;
    }

    public TagItem() {

    }

    public TagItem(String name, String price) {
        this.name = name;
        this.price = price;
    }

//    public long getId() {
//        return id;
//    }

//    public void setId(long id) {
//        this.id = id;
//    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

}
