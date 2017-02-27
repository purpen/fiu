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
     * rows : [{"_id":105112336,"title":"D3IN线下体验训","space_id":42,"sub_title":"62","cate_title":"3","web_url":"62","summary":"","cover_id":"58a3bc6a3ffca2c92f8b489d","type":"12","ordby":0,"kind":1,"created_on":1487125620,"state":2,"cover_url":"https://p4.taihuoniao.com/asset/170215/58a3bc4a3ffca231378b4811-1"},{"_id":105112342,"title":"首页地盘推荐说明","space_id":42,"sub_title":"","cate_title":"1","web_url":"63","summary":"","cover_id":"58a3bcfe3ffca231378b4814","type":"12","ordby":0,"kind":1,"created_on":1487125787,"state":2,"cover_url":"https://p4.taihuoniao.com/asset/170215/58a3bc793ffca26a378b4816-2"}]
     */

    public int total_rows;
    public List<RowsBean> rows;

    public static class RowsBean implements Parcelable {
        /**
         * _id : 105112336
         * title : D3IN线下体验训
         * space_id : 42
         * sub_title : 62
         * cate_title : 3
         * web_url : 62
         * summary :
         * cover_id : 58a3bc6a3ffca2c92f8b489d
         * type : 12
         * ordby : 0
         * kind : 1
         * created_on : 1487125620
         * state : 2
         * cover_url : https://p4.taihuoniao.com/asset/170215/58a3bc4a3ffca231378b4811-1
         */

        public int _id;
        public String title;
        public int space_id;
        public String sub_title;
        public String cate_title;
        public String web_url;
        public String summary;
        public String cover_id;
        public String type;
        public int ordby;
        public int kind;
        public int created_on;
        public int state;
        public String cover_url;

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this._id);
            dest.writeString(this.title);
            dest.writeInt(this.space_id);
            dest.writeString(this.sub_title);
            dest.writeString(this.cate_title);
            dest.writeString(this.web_url);
            dest.writeString(this.summary);
            dest.writeString(this.cover_id);
            dest.writeString(this.type);
            dest.writeInt(this.ordby);
            dest.writeInt(this.kind);
            dest.writeInt(this.created_on);
            dest.writeInt(this.state);
            dest.writeString(this.cover_url);
        }

        public RowsBean() {
        }

        protected RowsBean(Parcel in) {
            this._id = in.readInt();
            this.title = in.readString();
            this.space_id = in.readInt();
            this.sub_title = in.readString();
            this.cate_title = in.readString();
            this.web_url = in.readString();
            this.summary = in.readString();
            this.cover_id = in.readString();
            this.type = in.readString();
            this.ordby = in.readInt();
            this.kind = in.readInt();
            this.created_on = in.readInt();
            this.state = in.readInt();
            this.cover_url = in.readString();
        }

        public static final Creator<RowsBean> CREATOR = new Creator<RowsBean>() {
            @Override
            public RowsBean createFromParcel(Parcel source) {
                return new RowsBean(source);
            }

            @Override
            public RowsBean[] newArray(int size) {
                return new RowsBean[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.total_rows);
        dest.writeList(this.rows);
    }

    public BannerBean() {
    }

    protected BannerBean(Parcel in) {
        this.total_rows = in.readInt();
        this.rows = new ArrayList<RowsBean>();
        in.readList(this.rows, RowsBean.class.getClassLoader());
    }

    public static final Creator<BannerBean> CREATOR = new Creator<BannerBean>() {
        @Override
        public BannerBean createFromParcel(Parcel source) {
            return new BannerBean(source);
        }

        @Override
        public BannerBean[] newArray(int size) {
            return new BannerBean[size];
        }
    };
}
