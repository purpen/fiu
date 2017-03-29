package com.taihuoniao.fineix.zone.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by lilin on 2017/3/24.
 */

public class LightSpotImageBean implements Parcelable {

    /**
     * id : 136
     * asset_id : 58d4c4dac00076467f8d2faa
     * filepath : {"huge":"https://p4.taihuoniao.com/scene_scene/170324/58d4c4dac00076467f8d2fa9-hu.jpg"}
     * current_user_id : 20448
     */

    public int id;
    public String asset_id;
    public FilepathBean filepath;

    public static class FilepathBean implements Parcelable {
        /**
         * huge : https://p4.taihuoniao.com/scene_scene/170324/58d4c4dac00076467f8d2fa9-hu.jpg
         */
        public String huge;

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.huge);
        }

        public FilepathBean() {
        }

        protected FilepathBean(Parcel in) {
            this.huge = in.readString();
        }

        public static final Creator<FilepathBean> CREATOR = new Creator<FilepathBean>() {
            @Override
            public FilepathBean createFromParcel(Parcel source) {
                return new FilepathBean(source);
            }

            @Override
            public FilepathBean[] newArray(int size) {
                return new FilepathBean[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.asset_id);
        dest.writeParcelable(this.filepath, flags);
    }

    public LightSpotImageBean() {
    }

    protected LightSpotImageBean(Parcel in) {
        this.id = in.readInt();
        this.asset_id = in.readString();
        this.filepath = in.readParcelable(FilepathBean.class.getClassLoader());
    }

    public static final Parcelable.Creator<LightSpotImageBean> CREATOR = new Parcelable.Creator<LightSpotImageBean>() {
        @Override
        public LightSpotImageBean createFromParcel(Parcel source) {
            return new LightSpotImageBean(source);
        }

        @Override
        public LightSpotImageBean[] newArray(int size) {
            return new LightSpotImageBean[size];
        }
    };
}
