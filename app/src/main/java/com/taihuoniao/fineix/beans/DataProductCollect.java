package com.taihuoniao.fineix.beans;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * @author lilin
 *         created at 2016/6/23 16:54
 */
public class DataProductCollect implements Parcelable {
    public ArrayList<ItemProductCollect> rows;

    public DataProductCollect() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.rows);
    }

    protected DataProductCollect(Parcel in) {
        this.rows = in.createTypedArrayList(ItemProductCollect.CREATOR);
    }

    public static final Creator<DataProductCollect> CREATOR = new Creator<DataProductCollect>() {
        @Override
        public DataProductCollect createFromParcel(Parcel source) {
            return new DataProductCollect(source);
        }

        @Override
        public DataProductCollect[] newArray(int size) {
            return new DataProductCollect[size];
        }
    };
}
