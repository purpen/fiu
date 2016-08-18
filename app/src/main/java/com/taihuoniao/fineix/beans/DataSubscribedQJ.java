package com.taihuoniao.fineix.beans;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * @author lilin
 *         created at 2016/6/23 16:54
 */
public class DataSubscribedQJ implements Parcelable {
    public ArrayList<ItemSubscribedQJ> rows;

    public DataSubscribedQJ() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.rows);
    }

    protected DataSubscribedQJ(Parcel in) {
        this.rows = in.createTypedArrayList(ItemSubscribedQJ.CREATOR);
    }

    public static final Creator<DataSubscribedQJ> CREATOR = new Creator<DataSubscribedQJ>() {
        @Override
        public DataSubscribedQJ createFromParcel(Parcel source) {
            return new DataSubscribedQJ(source);
        }

        @Override
        public DataSubscribedQJ[] newArray(int size) {
            return new DataSubscribedQJ[size];
        }
    };
}
