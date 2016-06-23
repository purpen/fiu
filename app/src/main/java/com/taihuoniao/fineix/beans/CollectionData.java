package com.taihuoniao.fineix.beans;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lilin
 *         created at 2016/6/23 16:54
 */
public class CollectionData implements Parcelable {
    public List<CollectionItem> rows;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(this.rows);
    }

    public CollectionData() {
    }

    protected CollectionData(Parcel in) {
        this.rows = new ArrayList<CollectionItem>();
        in.readList(this.rows, CollectionItem.class.getClassLoader());
    }

    public static final Parcelable.Creator<CollectionData> CREATOR = new Parcelable.Creator<CollectionData>() {
        @Override
        public CollectionData createFromParcel(Parcel source) {
            return new CollectionData(source);
        }

        @Override
        public CollectionData[] newArray(int size) {
            return new CollectionData[size];
        }
    };
}
