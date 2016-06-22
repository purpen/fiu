package com.taihuoniao.fineix.beans;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author lilin
 *         created at 2016/6/21 23:50
 */
public class IsInviteData implements Parcelable {
    public int status;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.status);
    }

    public IsInviteData() {
    }

    protected IsInviteData(Parcel in) {
        this.status = in.readInt();
    }

    public static final Parcelable.Creator<IsInviteData> CREATOR = new Parcelable.Creator<IsInviteData>() {
        @Override
        public IsInviteData createFromParcel(Parcel source) {
            return new IsInviteData(source);
        }

        @Override
        public IsInviteData[] newArray(int size) {
            return new IsInviteData[size];
        }
    };
}
