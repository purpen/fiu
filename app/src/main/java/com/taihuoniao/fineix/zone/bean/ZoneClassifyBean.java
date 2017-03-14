package com.taihuoniao.fineix.zone.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lilin on 2017/3/9.
 */

public class ZoneClassifyBean implements Parcelable {

    public List<RowsBean> rows;

    public static class RowsBean implements Parcelable {
        /**
         * _id : 85
         * title : 体验店
         * name : scene_tyd
         * gid : 0
         * pid : 83
         * order_by : 0
         * sub_count : 0
         * tag_id : 0
         * domain : 12
         * total_count : 1
         * reply_count : 0
         * app_cover_url : null
         * tags : [""]
         * back_url : https://p4.taihuoniao.com/asset/170213/58a1260120de8d73298b6fd1-1-hu.jpg
         * stick : 0
         * sub_categories : []
         */

        public String _id;
        public String title;
        public String name;
        public boolean isSelected;
        public int gid;
        public int pid;
        public int order_by;
        public int sub_count;
        public int tag_id;
        public int domain;
        public int total_count;
        public int reply_count;
        public String back_url;
        public int stick;
        public List<String> tags;

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this._id);
            dest.writeString(this.title);
            dest.writeString(this.name);
            dest.writeInt(this.gid);
            dest.writeInt(this.pid);
            dest.writeInt(this.order_by);
            dest.writeInt(this.sub_count);
            dest.writeInt(this.tag_id);
            dest.writeInt(this.domain);
            dest.writeInt(this.total_count);
            dest.writeInt(this.reply_count);
            dest.writeString(this.back_url);
            dest.writeInt(this.stick);
            dest.writeStringList(this.tags);
        }

        public RowsBean() {
        }

        protected RowsBean(Parcel in) {
            this._id = in.readString();
            this.title = in.readString();
            this.name = in.readString();
            this.gid = in.readInt();
            this.pid = in.readInt();
            this.order_by = in.readInt();
            this.sub_count = in.readInt();
            this.tag_id = in.readInt();
            this.domain = in.readInt();
            this.total_count = in.readInt();
            this.reply_count = in.readInt();
            this.back_url = in.readString();
            this.stick = in.readInt();
            this.tags = in.createStringArrayList();
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
        dest.writeList(this.rows);
    }

    public ZoneClassifyBean() {
    }

    protected ZoneClassifyBean(Parcel in) {
        this.rows = new ArrayList<RowsBean>();
        in.readList(this.rows, RowsBean.class.getClassLoader());
    }

    public static final Parcelable.Creator<ZoneClassifyBean> CREATOR = new Parcelable.Creator<ZoneClassifyBean>() {
        @Override
        public ZoneClassifyBean createFromParcel(Parcel source) {
            return new ZoneClassifyBean(source);
        }

        @Override
        public ZoneClassifyBean[] newArray(int size) {
            return new ZoneClassifyBean[size];
        }
    };
}
