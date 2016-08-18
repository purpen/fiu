package com.taihuoniao.fineix.beans;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * @author lilin
 *         created at 2016/6/23 16:54
 */
public class DataUserSearch implements Parcelable {
    public ArrayList<ItemUserSearch> rows;

    public DataUserSearch() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.rows);
    }

    protected DataUserSearch(Parcel in) {
        this.rows = in.createTypedArrayList(ItemUserSearch.CREATOR);
    }

    public static final Creator<DataUserSearch> CREATOR = new Creator<DataUserSearch>() {
        @Override
        public DataUserSearch createFromParcel(Parcel source) {
            return new DataUserSearch(source);
        }

        @Override
        public DataUserSearch[] newArray(int size) {
            return new DataUserSearch[size];
        }
    };
}
