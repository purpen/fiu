package com.taihuoniao.fineix.beans;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author lilin
 *         created at 2016/6/23 17:19
 */
public class CjBean implements Parcelable {
    public String id;
    public String title;
    public String cover_url;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.title);
        dest.writeString(this.cover_url);
    }

    public CjBean() {
    }

    protected CjBean(Parcel in) {
        this.id = in.readString();
        this.title = in.readString();
        this.cover_url = in.readString();
    }

    public static final Parcelable.Creator<CjBean> CREATOR = new Parcelable.Creator<CjBean>() {
        @Override
        public CjBean createFromParcel(Parcel source) {
            return new CjBean(source);
        }

        @Override
        public CjBean[] newArray(int size) {
            return new CjBean[size];
        }
    };
}
