package com.taihuoniao.fineix.network.bean;

/**
 * Created by Stephen on 2016/12/6 17:24
 * Email: 895745843@qq.com
 */

public class CheckVersionBean {

    /**
     * code : 0
     * msg :
     * download : http://a.app.qq.com/o/simple.jsp?pkgname=com.taihuoniao.fineix
     * current_user_id : 924912
     */

    private int code;
    private String msg;
    private String download;
    private int current_user_id;

    public void setCode(int code) {
        this.code = code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setDownload(String download) {
        this.download = download;
    }

    public void setCurrent_user_id(int current_user_id) {
        this.current_user_id = current_user_id;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public String getDownload() {
        return download;
    }

    public int getCurrent_user_id() {
        return current_user_id;
    }
}
