package com.taihuoniao.fineix.zone.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by lilin on 2017/3/4.
 */

public class ShareH5Url implements Parcelable {

    /**
     * url : https://s.taihuoniao.com/s/Wiai6c
     * o_url : https://s.taihuoniao.com/qr?infoType=10&infoId=133&referral_code=ZzEJfc
     * current_user_id : 20448
     */

    public String url;
    public String o_url;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.url);
        dest.writeString(this.o_url);
    }

    public ShareH5Url() {
    }

    protected ShareH5Url(Parcel in) {
        this.url = in.readString();
        this.o_url = in.readString();
    }

    public static final Parcelable.Creator<ShareH5Url> CREATOR = new Parcelable.Creator<ShareH5Url>() {
        @Override
        public ShareH5Url createFromParcel(Parcel source) {
            return new ShareH5Url(source);
        }

        @Override
        public ShareH5Url[] newArray(int size) {
            return new ShareH5Url[size];
        }
    };
}
