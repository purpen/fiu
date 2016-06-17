package com.taihuoniao.fineix.beans;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author lilin
 *         created at 2016/6/16 14:20
 */
public class SupportData implements Parcelable {
    public String love_count;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.love_count);
    }

    public SupportData() {
    }

    protected SupportData(Parcel in) {
        this.love_count = in.readString();
    }

    public static final Parcelable.Creator<SupportData> CREATOR = new Parcelable.Creator<SupportData>() {
        @Override
        public SupportData createFromParcel(Parcel source) {
            return new SupportData(source);
        }

        @Override
        public SupportData[] newArray(int size) {
            return new SupportData[size];
        }
    };
}
