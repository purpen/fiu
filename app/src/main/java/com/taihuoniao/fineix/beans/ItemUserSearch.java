package com.taihuoniao.fineix.beans;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author lilin
 *         created at 2016/8/18 17:57
 */
public class ItemUserSearch implements Parcelable {
    public String pid;
    public String oid;
    public String cid;
    public String kind;
    public String title;
    public String content;
    public String user_id;
    public String created_on;
    public String updated_on;
    public String high_title;
    public String high_content;
    public String _id;
    public String nickname;
    public String summary;
    public String avatar_url;
    public String kind_name;
    public int asset_type;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.pid);
        dest.writeString(this.oid);
        dest.writeString(this.cid);
        dest.writeString(this.kind);
        dest.writeString(this.title);
        dest.writeString(this.content);
        dest.writeString(this.user_id);
        dest.writeString(this.created_on);
        dest.writeString(this.updated_on);
        dest.writeString(this.high_title);
        dest.writeString(this.high_content);
        dest.writeString(this._id);
        dest.writeString(this.nickname);
        dest.writeString(this.summary);
        dest.writeString(this.avatar_url);
        dest.writeString(this.kind_name);
        dest.writeInt(this.asset_type);
    }

    public ItemUserSearch() {
    }

    protected ItemUserSearch(Parcel in) {
        this.pid = in.readString();
        this.oid = in.readString();
        this.cid = in.readString();
        this.kind = in.readString();
        this.title = in.readString();
        this.content = in.readString();
        this.user_id = in.readString();
        this.created_on = in.readString();
        this.updated_on = in.readString();
        this.high_title = in.readString();
        this.high_content = in.readString();
        this._id = in.readString();
        this.nickname = in.readString();
        this.summary = in.readString();
        this.avatar_url = in.readString();
        this.kind_name = in.readString();
        this.asset_type = in.readInt();
    }

    public static final Creator<ItemUserSearch> CREATOR = new Creator<ItemUserSearch>() {
        @Override
        public ItemUserSearch createFromParcel(Parcel source) {
            return new ItemUserSearch(source);
        }

        @Override
        public ItemUserSearch[] newArray(int size) {
            return new ItemUserSearch[size];
        }
    };
}
