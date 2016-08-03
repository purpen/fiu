package com.taihuoniao.fineix.pay.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author lilin
 *         created at 2016/5/13 17:28
 */
public class ALIPayParams implements Parcelable {
    public String str;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.str);
    }

    public ALIPayParams() {
    }

    protected ALIPayParams(Parcel in) {
        this.str = in.readString();
    }

    public static final Parcelable.Creator<ALIPayParams> CREATOR = new Parcelable.Creator<ALIPayParams>() {
        @Override
        public ALIPayParams createFromParcel(Parcel source) {
            return new ALIPayParams(source);
        }

        @Override
        public ALIPayParams[] newArray(int size) {
            return new ALIPayParams[size];
        }
    };
}
