package com.taihuoniao.fineix.beans;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author lilin
 *         created at 2016/6/17 13:27
 */
public class ShareContent implements Parcelable {
    public String shareTxt;
    public String title;
    public String url;
    public String titleUrl;
    public String imageUrl;
    public String site;
    public String siteUrl;

    public ShareContent() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.shareTxt);
        dest.writeString(this.title);
        dest.writeString(this.url);
        dest.writeString(this.titleUrl);
        dest.writeString(this.imageUrl);
        dest.writeString(this.site);
        dest.writeString(this.siteUrl);
    }

    protected ShareContent(Parcel in) {
        this.shareTxt = in.readString();
        this.title = in.readString();
        this.url = in.readString();
        this.titleUrl = in.readString();
        this.imageUrl = in.readString();
        this.site = in.readString();
        this.siteUrl = in.readString();
    }

    public static final Creator<ShareContent> CREATOR = new Creator<ShareContent>() {
        @Override
        public ShareContent createFromParcel(Parcel source) {
            return new ShareContent(source);
        }

        @Override
        public ShareContent[] newArray(int size) {
            return new ShareContent[size];
        }
    };
}
