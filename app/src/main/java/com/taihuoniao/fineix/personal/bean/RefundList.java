package com.taihuoniao.fineix.personal.bean;

/**
 *
 * 退款列表
 * Created by Stephen on 2016/11/23.
 */

public class RefundList {

    /**
     * total_rows : 0
     * rows :
     * total_page : 0
     * current_page : 1
     * pager :
     * next_page : 0
     * prev_page : 0
     * current_user_id : 924912
     */

    private int total_rows;
    private String rows;
    private int total_page;
    private int current_page;
    private String pager;
    private int next_page;
    private int prev_page;
    private int current_user_id;

    public void setTotal_rows(int total_rows) {
        this.total_rows = total_rows;
    }

    public void setRows(String rows) {
        this.rows = rows;
    }

    public void setTotal_page(int total_page) {
        this.total_page = total_page;
    }

    public void setCurrent_page(int current_page) {
        this.current_page = current_page;
    }

    public void setPager(String pager) {
        this.pager = pager;
    }

    public void setNext_page(int next_page) {
        this.next_page = next_page;
    }

    public void setPrev_page(int prev_page) {
        this.prev_page = prev_page;
    }

    public void setCurrent_user_id(int current_user_id) {
        this.current_user_id = current_user_id;
    }

    public int getTotal_rows() {
        return total_rows;
    }

    public String getRows() {
        return rows;
    }

    public int getTotal_page() {
        return total_page;
    }

    public int getCurrent_page() {
        return current_page;
    }

    public String getPager() {
        return pager;
    }

    public int getNext_page() {
        return next_page;
    }

    public int getPrev_page() {
        return prev_page;
    }

    public int getCurrent_user_id() {
        return current_user_id;
    }
}
