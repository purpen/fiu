package com.taihuoniao.fineix.zone.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by lilin on 2017/3/21.
 */

public class BrightItemBean implements Parcelable {
    public int id;
    public String img;
    public String content;

    public BrightItemBean() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.img);
        dest.writeString(this.content);
    }

    protected BrightItemBean(Parcel in) {
        this.id = in.readInt();
        this.img = in.readString();
        this.content = in.readString();
    }

    public static final Creator<BrightItemBean> CREATOR = new Creator<BrightItemBean>() {
        @Override
        public BrightItemBean createFromParcel(Parcel source) {
            return new BrightItemBean(source);
        }

        @Override
        public BrightItemBean[] newArray(int size) {
            return new BrightItemBean[size];
        }
    };
}
