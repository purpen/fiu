package com.taihuoniao.fineix.beans;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * @author lilin
 *         created at 2016/6/23 16:54
 */
public class DataQJCollect implements Parcelable {
    public ArrayList<ItemQJCollect> rows;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(this.rows);
    }

    public DataQJCollect() {
    }

    protected DataQJCollect(Parcel in) {
        this.rows = new ArrayList<ItemQJCollect>();
        in.readList(this.rows, ItemQJCollect.class.getClassLoader());
    }

    public static final Parcelable.Creator<DataQJCollect> CREATOR = new Parcelable.Creator<DataQJCollect>() {
        @Override
        public DataQJCollect createFromParcel(Parcel source) {
            return new DataQJCollect(source);
        }

        @Override
        public DataQJCollect[] newArray(int size) {
            return new DataQJCollect[size];
        }
    };
}
