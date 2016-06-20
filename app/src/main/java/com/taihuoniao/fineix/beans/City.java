package com.taihuoniao.fineix.beans;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author lilin
 * created at 2016/4/5 18:05
 */
public class City implements Parcelable {
    public String id;
    public String name;
    public String image_url;
    public float lng;
    public float lat;
    public boolean is_estore;
    public boolean is_scene;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.image_url);
        dest.writeFloat(this.lng);
        dest.writeFloat(this.lat);
        dest.writeByte(this.is_estore ? (byte) 1 : (byte) 0);
        dest.writeByte(this.is_scene ? (byte) 1 : (byte) 0);
    }

    public City() {
    }

    protected City(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.image_url = in.readString();
        this.lng = in.readFloat();
        this.lat = in.readFloat();
        this.is_estore = in.readByte() != 0;
        this.is_scene = in.readByte() != 0;
    }

    public static final Parcelable.Creator<City> CREATOR = new Parcelable.Creator<City>() {
        @Override
        public City createFromParcel(Parcel source) {
            return new City(source);
        }

        @Override
        public City[] newArray(int size) {
            return new City[size];
        }
    };
}
