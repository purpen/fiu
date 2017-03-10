package com.taihuoniao.fineix.beans;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by lilin on 2017/3/10.
 */

public class ZoneUploadCoverBean implements Parcelable {
    public String id;
    public String asset_id;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.asset_id);
    }

    public ZoneUploadCoverBean() {
    }

    protected ZoneUploadCoverBean(Parcel in) {
        this.id = in.readString();
        this.asset_id = in.readString();
    }

    public static final Parcelable.Creator<ZoneUploadCoverBean> CREATOR = new Parcelable.Creator<ZoneUploadCoverBean>() {
        @Override
        public ZoneUploadCoverBean createFromParcel(Parcel source) {
            return new ZoneUploadCoverBean(source);
        }

        @Override
        public ZoneUploadCoverBean[] newArray(int size) {
            return new ZoneUploadCoverBean[size];
        }
    };
}
