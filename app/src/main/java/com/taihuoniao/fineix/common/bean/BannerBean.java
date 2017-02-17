package com.taihuoniao.fineix.common.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Stephen on 2017/2/15 16:57
 * Email: 895745843@qq.com
 */

public class BannerBean implements Parcelable {

    /**
     * total_rows : 2
     * rows : [{"_id":101931301,"title":"test","space_id":25,"sub_title":"","web_url":"3","summary":"","cover_id":"572386a83ffca27d078bdc06","type":"8","ordby":0,"kind":1,"created_on":1461946032,"state":2,"cover_url":"https://p4.taihuoniao.com/asset/160430/572386993ffca268098c4bd3-1"},{"_id":101931304,"title":"test222","space_id":25,"sub_title":"","web_url":"4","summary":"","cover_id":"5851259b3ffca20a328b46a1","type":"8","ordby":0,"kind":1,"created_on":1461946061,"state":2,"cover_url":"https://p4.taihuoniao.com/asset/161214/585125943ffca20a328b469f-1"}]
     * total_page : 1
     * current_page : 1
     * pager : 
     * next_page : 0
     * prev_page : 0
     * current_user_id : 924912
     */

    private String total_rows;
    private String total_page;
    private String current_page;
    private String pager;
    private String next_page;
    private String prev_page;
    private String current_user_id;
    private List<RowsEntity> rows;

    public void setTotal_rows(String total_rows) {
        this.total_rows = total_rows;
    }

    public void setTotal_page(String total_page) {
        this.total_page = total_page;
    }

    public void setCurrent_page(String current_page) {
        this.current_page = current_page;
    }

    public void setPager(String pager) {
        this.pager = pager;
    }

    public void setNext_page(String next_page) {
        this.next_page = next_page;
    }

    public void setPrev_page(String prev_page) {
        this.prev_page = prev_page;
    }

    public void setCurrent_user_id(String current_user_id) {
        this.current_user_id = current_user_id;
    }

    public void setRows(List<RowsEntity> rows) {
        this.rows = rows;
    }

    public String getTotal_rows() {
        return total_rows;
    }

    public String getTotal_page() {
        return total_page;
    }

    public String getCurrent_page() {
        return current_page;
    }

    public String getPager() {
        return pager;
    }

    public String getNext_page() {
        return next_page;
    }

    public String getPrev_page() {
        return prev_page;
    }

    public String getCurrent_user_id() {
        return current_user_id;
    }

    public List<RowsEntity> getRows() {
        return rows;
    }

    public static class RowsEntity {
        /**
         * _id : 101931301
         * title : test
         * space_id : 25
         * sub_title : 
         * web_url : 3
         * summary : 
         * cover_id : 572386a83ffca27d078bdc06
         * type : 8
         * ordby : 0
         * kind : 1
         * created_on : 1461946032
         * state : 2
         * cover_url : https://p4.taihuoniao.com/asset/160430/572386993ffca268098c4bd3-1
         */

        private String _id;
        private String title;
        private String space_id;
        private String sub_title;
        private String web_url;
        private String summary;
        private String cover_id;
        private String type;
        private String ordby;
        private String kind;
        private String created_on;
        private String state;
        private String cover_url;

        public void set_id(String _id) {
            this._id = _id;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public void setSpace_id(String space_id) {
            this.space_id = space_id;
        }

        public void setSub_title(String sub_title) {
            this.sub_title = sub_title;
        }

        public void setWeb_url(String web_url) {
            this.web_url = web_url;
        }

        public void setSummary(String summary) {
            this.summary = summary;
        }

        public void setCover_id(String cover_id) {
            this.cover_id = cover_id;
        }

        public void setType(String type) {
            this.type = type;
        }

        public void setOrdby(String ordby) {
            this.ordby = ordby;
        }

        public void setKind(String kind) {
            this.kind = kind;
        }

        public void setCreated_on(String created_on) {
            this.created_on = created_on;
        }

        public void setState(String state) {
            this.state = state;
        }

        public void setCover_url(String cover_url) {
            this.cover_url = cover_url;
        }

        public String get_id() {
            return _id;
        }

        public String getTitle() {
            return title;
        }

        public String getSpace_id() {
            return space_id;
        }

        public String getSub_title() {
            return sub_title;
        }

        public String getWeb_url() {
            return web_url;
        }

        public String getSummary() {
            return summary;
        }

        public String getCover_id() {
            return cover_id;
        }

        public String getType() {
            return type;
        }

        public String getOrdby() {
            return ordby;
        }

        public String getKind() {
            return kind;
        }

        public String getCreated_on() {
            return created_on;
        }

        public String getState() {
            return state;
        }

        public String getCover_url() {
            return cover_url;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.total_rows);
        dest.writeString(this.total_page);
        dest.writeString(this.current_page);
        dest.writeString(this.pager);
        dest.writeString(this.next_page);
        dest.writeString(this.prev_page);
        dest.writeString(this.current_user_id);
        dest.writeList(this.rows);
    }

    public BannerBean() {
    }

    protected BannerBean(Parcel in) {
        this.total_rows = in.readString();
        this.total_page = in.readString();
        this.current_page = in.readString();
        this.pager = in.readString();
        this.next_page = in.readString();
        this.prev_page = in.readString();
        this.current_user_id = in.readString();
        this.rows = new ArrayList<RowsEntity>();
        in.readList(this.rows, List.class.getClassLoader());
    }

    public static final Parcelable.Creator<BannerBean> CREATOR = new Parcelable.Creator<BannerBean>() {
        public BannerBean createFromParcel(Parcel source) {
            return new BannerBean(source);
        }

        public BannerBean[] newArray(int size) {
            return new BannerBean[size];
        }
    };
}
