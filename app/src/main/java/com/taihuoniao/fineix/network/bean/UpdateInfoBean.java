package com.taihuoniao.fineix.network.bean;

/**
 * Created by Stephen on 2016/12/6 17:16
 * Email: 895745843@qq.com
 */

public class UpdateInfoBean {

    /**
     * version : 1.9.1
     * download : http://a.app.qq.com/o/simple.jsp?pkgname=com.taihuoniao.fineix
     * current_user_id : 924912
     */

    private String version;
    private String download;
    private int current_user_id;

    public void setVersion(String version) {
        this.version = version;
    }

    public void setDownload(String download) {
        this.download = download;
    }

    public void setCurrent_user_id(int current_user_id) {
        this.current_user_id = current_user_id;
    }

    public String getVersion() {
        return version;
    }

    public String getDownload() {
        return download;
    }

    public int getCurrent_user_id() {
        return current_user_id;
    }
}
