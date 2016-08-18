package com.taihuoniao.fineix.beans;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author lilin
 *         created at 2016/8/12 17:27
 */
public class ThemeQJ implements Parcelable {
    public String _id;
    public String title;
    public String back_url;
    public int stick;
    public String sub_count;
    public boolean isSubscribed;
    public ThemeQJ() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this._id);
        dest.writeString(this.title);
        dest.writeString(this.back_url);
        dest.writeInt(this.stick);
        dest.writeString(this.sub_count);
    }

    protected ThemeQJ(Parcel in) {
        this._id = in.readString();
        this.title = in.readString();
        this.back_url = in.readString();
        this.stick = in.readInt();
        this.sub_count = in.readString();
    }

    public static final Creator<ThemeQJ> CREATOR = new Creator<ThemeQJ>() {
        @Override
        public ThemeQJ createFromParcel(Parcel source) {
            return new ThemeQJ(source);
        }

        @Override
        public ThemeQJ[] newArray(int size) {
            return new ThemeQJ[size];
        }
    };
}
