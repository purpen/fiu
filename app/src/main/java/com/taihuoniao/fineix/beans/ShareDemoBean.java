package com.taihuoniao.fineix.beans;

/**
 * Created by taihuoniao on 2016/5/24.
 */
public class ShareDemoBean {
    private boolean isSelect;
    private int imgId;

    public ShareDemoBean() {
    }

    public ShareDemoBean(int imgId, boolean isSelect) {
        this.imgId = imgId;
        this.isSelect = isSelect;
    }

    public int getImgId() {
        return imgId;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setIsSelect(boolean isSelect) {
        this.isSelect = isSelect;
    }
}
