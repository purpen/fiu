package com.taihuoniao.fineix.beans;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * @author lilin
 *         created at 2016/8/13 17:15
 */
public class InterestUserData implements Parcelable {
    public ArrayList<User> users;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(this.users);
    }

    public InterestUserData() {
    }

    protected InterestUserData(Parcel in) {
        this.users = new ArrayList<>();
        in.readList(this.users, User.class.getClassLoader());
    }

    public static final Parcelable.Creator<InterestUserData> CREATOR = new Parcelable.Creator<InterestUserData>() {
        @Override
        public InterestUserData createFromParcel(Parcel source) {
            return new InterestUserData(source);
        }

        @Override
        public InterestUserData[] newArray(int size) {
            return new InterestUserData[size];
        }
    };
}
