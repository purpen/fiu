package com.taihuoniao.fineix.beans;

/**
 * Created by taihuoniao on 2016/4/5.
 */
public class JDDetailsBean extends NetBean {
    private String url;
    private String imagePath;
    private String name;
    private String sale_price;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSale_price() {
        return sale_price;
    }

    public void setSale_price(String sale_price) {
        this.sale_price = sale_price;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
