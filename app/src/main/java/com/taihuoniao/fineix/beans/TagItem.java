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
    private int type = 1;//商品是否可购买 1用户添加 2可购买商品
    private String name;
    private String price;
    private int loc = 2;

    public int getLoc() {
        return loc;
    }

    public void setLoc(int loc) {
        this.loc = loc;
    }

    //考虑适配的问题，纪录的是百分比坐标
    private double x = 0.5;
    private double y = 0.5;


    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }


    public TagItem() {

    }

    public TagItem(String name, String price) {
        this.name = name;
        this.price = price;
    }

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

    @Override
    public String toString() {
        return "{\"id\":\"" + id + "\",\"title\":\"" + name + "\",\"x\":" + x + ",\"y\":" + y + ",\"loc\":" + loc +",\"type\":"+type+ "}";
    }
}
