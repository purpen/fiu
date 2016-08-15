package com.taihuoniao.fineix.beans;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author lilin
 *         created at 2016/8/15 19:46
 */
public class RegisterInfo implements Parcelable {
    public String mobile;
    public String password;
    public String verify_code;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mobile);
        dest.writeString(this.password);
        dest.writeString(this.verify_code);
    }

    public RegisterInfo() {
    }

    protected RegisterInfo(Parcel in) {
        this.mobile = in.readString();
        this.password = in.readString();
        this.verify_code = in.readString();
    }

    public static final Parcelable.Creator<RegisterInfo> CREATOR = new Parcelable.Creator<RegisterInfo>() {
        @Override
        public RegisterInfo createFromParcel(Parcel source) {
            return new RegisterInfo(source);
        }

        @Override
        public RegisterInfo[] newArray(int size) {
            return new RegisterInfo[size];
        }
    };
}
