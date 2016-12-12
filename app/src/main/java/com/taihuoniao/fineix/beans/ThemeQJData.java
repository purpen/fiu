package com.taihuoniao.fineix.beans;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * @author lilin
 *         created at 2016/8/12 17:42
 */
public class ThemeQJData implements Parcelable {
    public ArrayList<ThemeQJ> rows;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(this.rows);
    }

    public ThemeQJData() {
    }

    protected ThemeQJData(Parcel in) {
        this.rows = new ArrayList<>();
        in.readList(this.rows, ThemeQJ.class.getClassLoader());
    }

    public static final Parcelable.Creator<ThemeQJData> CREATOR = new Parcelable.Creator<ThemeQJData>() {
        @Override
        public ThemeQJData createFromParcel(Parcel source) {
            return new ThemeQJData(source);
        }

        @Override
        public ThemeQJData[] newArray(int size) {
            return new ThemeQJData[size];
        }
    };
}
