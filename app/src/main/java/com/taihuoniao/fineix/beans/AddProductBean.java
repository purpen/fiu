package com.taihuoniao.fineix.beans;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by taihuoniao on 2016/5/6.
 */
public class AddProductBean implements Parcelable {
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
    }

    public AddProductBean() {
    }

    protected AddProductBean(Parcel in) {
        this.id = in.readString();
    }

    public static final Parcelable.Creator<AddProductBean> CREATOR = new Parcelable.Creator<AddProductBean>() {
        @Override
        public AddProductBean createFromParcel(Parcel source) {
            return new AddProductBean(source);
        }

        @Override
        public AddProductBean[] newArray(int size) {
            return new AddProductBean[size];
        }
    };
}
