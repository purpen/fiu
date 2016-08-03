package com.taihuoniao.fineix.pay.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author lilin
 *         created at 2016/8/1 14:30
 */
public class JdPayParams implements Parcelable {
    public Params params;
    public String url;
    public String rid;

    public JdPayParams() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.params, flags);
        dest.writeString(this.url);
        dest.writeString(this.rid);
    }

    protected JdPayParams(Parcel in) {
        this.params = in.readParcelable(Params.class.getClassLoader());
        this.url = in.readString();
        this.rid = in.readString();
    }

    public static final Creator<JdPayParams> CREATOR = new Creator<JdPayParams>() {
        @Override
        public JdPayParams createFromParcel(Parcel source) {
            return new JdPayParams(source);
        }

        @Override
        public JdPayParams[] newArray(int size) {
            return new JdPayParams[size];
        }
    };
}
