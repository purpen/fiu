package com.taihuoniao.fineix.beans;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * @author lilin
 *         created at 2016/6/23 16:54
 */
public class DataChooseSubject implements Parcelable {
    public ArrayList<ItemChoosenSubject> rows;

    public static class ItemChoosenSubject implements Parcelable {
        public String _id;
        public String title;
        public String category_id;
        public String publish;
        public String type;
        public String cover_id;
        public String banner_id;
        public String summary;
        public String user_id;
        public String evt;
        public String kind;
        public String stick;
        public String fine;
        public String stick_on;
        public String fine_on;
        public String status;
        public String view_count;
        public String comment_count;
        public String love_count;
        public String favorite_count;
        public String attend_count;
        public String share_count;
        public String cover_url;
        public String begin_time_at;
        public String end_time_at;
        public ArrayList<ItemProductCollect> products;

        public ItemChoosenSubject() {
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this._id);
            dest.writeString(this.title);
            dest.writeString(this.category_id);
            dest.writeString(this.publish);
            dest.writeString(this.type);
            dest.writeString(this.cover_id);
            dest.writeString(this.banner_id);
            dest.writeString(this.summary);
            dest.writeString(this.user_id);
            dest.writeString(this.evt);
            dest.writeString(this.kind);
            dest.writeString(this.stick);
            dest.writeString(this.fine);
            dest.writeString(this.stick_on);
            dest.writeString(this.fine_on);
            dest.writeString(this.status);
            dest.writeString(this.view_count);
            dest.writeString(this.comment_count);
            dest.writeString(this.love_count);
            dest.writeString(this.favorite_count);
            dest.writeString(this.attend_count);
            dest.writeString(this.share_count);
            dest.writeString(this.cover_url);
            dest.writeString(this.begin_time_at);
            dest.writeString(this.end_time_at);
            dest.writeTypedList(this.products);
        }

        protected ItemChoosenSubject(Parcel in) {
            this._id = in.readString();
            this.title = in.readString();
            this.category_id = in.readString();
            this.publish = in.readString();
            this.type = in.readString();
            this.cover_id = in.readString();
            this.banner_id = in.readString();
            this.summary = in.readString();
            this.user_id = in.readString();
            this.evt = in.readString();
            this.kind = in.readString();
            this.stick = in.readString();
            this.fine = in.readString();
            this.stick_on = in.readString();
            this.fine_on = in.readString();
            this.status = in.readString();
            this.view_count = in.readString();
            this.comment_count = in.readString();
            this.love_count = in.readString();
            this.favorite_count = in.readString();
            this.attend_count = in.readString();
            this.share_count = in.readString();
            this.cover_url = in.readString();
            this.begin_time_at = in.readString();
            this.end_time_at = in.readString();
            this.products = in.createTypedArrayList(ItemProductCollect.CREATOR);
        }

        public static final Creator<ItemChoosenSubject> CREATOR = new Creator<ItemChoosenSubject>() {
            @Override
            public ItemChoosenSubject createFromParcel(Parcel source) {
                return new ItemChoosenSubject(source);
            }

            @Override
            public ItemChoosenSubject[] newArray(int size) {
                return new ItemChoosenSubject[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.rows);
    }

    public DataChooseSubject() {
    }

    protected DataChooseSubject(Parcel in) {
        this.rows = in.createTypedArrayList(ItemChoosenSubject.CREATOR);
    }

    public static final Creator<DataChooseSubject> CREATOR = new Creator<DataChooseSubject>() {
        @Override
        public DataChooseSubject createFromParcel(Parcel source) {
            return new DataChooseSubject(source);
        }

        @Override
        public DataChooseSubject[] newArray(int size) {
            return new DataChooseSubject[size];
        }
    };
}
