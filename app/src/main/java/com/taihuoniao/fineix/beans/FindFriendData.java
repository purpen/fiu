package com.taihuoniao.fineix.beans;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * @author lilin
 * created at 2016/5/8 19:04
 */
public class FindFriendData implements Parcelable {
    public ArrayList<User> users;

    public static class User implements Parcelable {
        public long _id;
        public String nickname;
        public String sex;
        public String medium_avatar_url;
        public int is_love;
        public ArrayList<CJItem> scene_sight;
        public String summary;
        public ArrayList<String> areas;

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeLong(this._id);
            dest.writeString(this.nickname);
            dest.writeString(this.sex);
            dest.writeString(this.medium_avatar_url);
            dest.writeInt(this.is_love);
            dest.writeList(this.scene_sight);
            dest.writeString(this.summary);
            dest.writeStringList(this.areas);
        }

        public User() {
        }

        protected User(Parcel in) {
            this._id = in.readLong();
            this.nickname = in.readString();
            this.sex = in.readString();
            this.medium_avatar_url = in.readString();
            this.is_love = in.readInt();
            this.scene_sight = new ArrayList<CJItem>();
            in.readList(this.scene_sight, CJItem.class.getClassLoader());
            this.summary = in.readString();
            this.areas = in.createStringArrayList();
        }

        public static final Creator<User> CREATOR = new Creator<User>() {
            @Override
            public User createFromParcel(Parcel source) {
                return new User(source);
            }

            @Override
            public User[] newArray(int size) {
                return new User[size];
            }
        };
    }

    public static class CJItem implements Parcelable {
        public String _id;
        public String title;
        public String address;
        public String cover_url;

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this._id);
            dest.writeString(this.title);
            dest.writeString(this.address);
            dest.writeString(this.cover_url);
        }

        public CJItem() {
        }

        protected CJItem(Parcel in) {
            this._id = in.readString();
            this.title = in.readString();
            this.address = in.readString();
            this.cover_url = in.readString();
        }

        public static final Creator<CJItem> CREATOR = new Creator<CJItem>() {
            @Override
            public CJItem createFromParcel(Parcel source) {
                return new CJItem(source);
            }

            @Override
            public CJItem[] newArray(int size) {
                return new CJItem[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(this.users);
    }

    public FindFriendData() {
    }

    protected FindFriendData(Parcel in) {
        this.users = new ArrayList<User>();
        in.readList(this.users, User.class.getClassLoader());
    }

    public static final Parcelable.Creator<FindFriendData> CREATOR = new Parcelable.Creator<FindFriendData>() {
        @Override
        public FindFriendData createFromParcel(Parcel source) {
            return new FindFriendData(source);
        }

        @Override
        public FindFriendData[] newArray(int size) {
            return new FindFriendData[size];
        }
    };
}
