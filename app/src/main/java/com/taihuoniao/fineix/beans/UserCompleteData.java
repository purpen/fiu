package com.taihuoniao.fineix.beans;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * @author lilin
 *         created at 2016/8/12 12:20
 */
public class UserCompleteData implements Parcelable {
    public String nickName;
    public int gender;
    public int decade;
    public int consume;
    public ArrayList<Integer> theme;
    public ArrayList<Integer> focus;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.nickName);
        dest.writeInt(this.gender);
        dest.writeInt(this.decade);
        dest.writeInt(this.consume);
        dest.writeList(this.theme);
        dest.writeList(this.focus);
    }

    public UserCompleteData() {
    }

    protected UserCompleteData(Parcel in) {
        this.nickName = in.readString();
        this.gender = in.readInt();
        this.decade = in.readInt();
        this.consume = in.readInt();
        this.theme = new ArrayList<Integer>();
        in.readList(this.theme, Integer.class.getClassLoader());
        this.focus = new ArrayList<Integer>();
        in.readList(this.focus, Integer.class.getClassLoader());
    }

    public static final Parcelable.Creator<UserCompleteData> CREATOR = new Parcelable.Creator<UserCompleteData>() {
        @Override
        public UserCompleteData createFromParcel(Parcel source) {
            return new UserCompleteData(source);
        }

        @Override
        public UserCompleteData[] newArray(int size) {
            return new UserCompleteData[size];
        }
    };
}
