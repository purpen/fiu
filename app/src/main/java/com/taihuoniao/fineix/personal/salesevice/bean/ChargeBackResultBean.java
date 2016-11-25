package com.taihuoniao.fineix.personal.salesevice.bean;

/**
 * Created by Stephen on 2016/11/25.
 */

public class ChargeBackResultBean {

    /**
     * id : null
     * rid : 116112406520
     * current_user_id : 20448
     */

    private String id;
    private String rid;
    private int current_user_id;

    public void setId(String id) {
        this.id = id;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public void setCurrent_user_id(int current_user_id) {
        this.current_user_id = current_user_id;
    }

    public String getId() {
        return id;
    }

    public String getRid() {
        return rid;
    }

    public int getCurrent_user_id() {
        return current_user_id;
    }
}
