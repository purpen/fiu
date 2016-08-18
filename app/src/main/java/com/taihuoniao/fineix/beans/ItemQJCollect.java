package com.taihuoniao.fineix.beans;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author lilin
 *         created at 2016/6/23 16:56
 */
public class ItemQJCollect implements Parcelable {

    public String _id;
    public int user_id;
    public int target_id;
    public int type;
    public int event;

    public UserBean user;

    public SightBean sight;
    public int is_follow;

    public static class UserBean implements Parcelable {
        public int _id;
        public String nickname;
        public String avatar_url;
        public String summary;

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this._id);
            dest.writeString(this.nickname);
            dest.writeString(this.avatar_url);
            dest.writeString(this.summary);
        }

        public UserBean() {
        }

        protected UserBean(Parcel in) {
            this._id = in.readInt();
            this.nickname = in.readString();
            this.avatar_url = in.readString();
            this.summary = in.readString();
        }

        public static final Creator<UserBean> CREATOR = new Creator<UserBean>() {
            @Override
            public UserBean createFromParcel(Parcel source) {
                return new UserBean(source);
            }

            @Override
            public UserBean[] newArray(int size) {
                return new UserBean[size];
            }
        };
    }

    public static class SightBean implements Parcelable {
        public int _id;
        public String cover_url;
        public UserInfoBean user_info;
        public String scene_title;

        public static class UserInfoBean implements Parcelable {
            public int user_id;
            public String nickname;
            public String avatar_url;
            public String summary;
            public int is_expert;
            public String label;
            public String expert_label;
            public String expert_info;

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeInt(this.user_id);
                dest.writeString(this.nickname);
                dest.writeString(this.avatar_url);
                dest.writeString(this.summary);
                dest.writeInt(this.is_expert);
                dest.writeString(this.label);
                dest.writeString(this.expert_label);
                dest.writeString(this.expert_info);
            }

            public UserInfoBean() {
            }

            protected UserInfoBean(Parcel in) {
                this.user_id = in.readInt();
                this.nickname = in.readString();
                this.avatar_url = in.readString();
                this.summary = in.readString();
                this.is_expert = in.readInt();
                this.label = in.readString();
                this.expert_label = in.readString();
                this.expert_info = in.readString();
            }

            public static final Creator<UserInfoBean> CREATOR = new Creator<UserInfoBean>() {
                @Override
                public UserInfoBean createFromParcel(Parcel source) {
                    return new UserInfoBean(source);
                }

                @Override
                public UserInfoBean[] newArray(int size) {
                    return new UserInfoBean[size];
                }
            };
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this._id);
            dest.writeString(this.cover_url);
            dest.writeParcelable(this.user_info, flags);
            dest.writeString(this.scene_title);
        }

        public SightBean() {
        }

        protected SightBean(Parcel in) {
            this._id = in.readInt();
            this.cover_url = in.readString();
            this.user_info = in.readParcelable(UserInfoBean.class.getClassLoader());
            this.scene_title = in.readString();
        }

        public static final Creator<SightBean> CREATOR = new Creator<SightBean>() {
            @Override
            public SightBean createFromParcel(Parcel source) {
                return new SightBean(source);
            }

            @Override
            public SightBean[] newArray(int size) {
                return new SightBean[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this._id);
        dest.writeInt(this.user_id);
        dest.writeInt(this.target_id);
        dest.writeInt(this.type);
        dest.writeInt(this.event);
        dest.writeParcelable(this.user, flags);
        dest.writeParcelable(this.sight, flags);
        dest.writeInt(this.is_follow);
    }

    public ItemQJCollect() {
    }

    protected ItemQJCollect(Parcel in) {
        this._id = in.readString();
        this.user_id = in.readInt();
        this.target_id = in.readInt();
        this.type = in.readInt();
        this.event = in.readInt();
        this.user = in.readParcelable(UserBean.class.getClassLoader());
        this.sight = in.readParcelable(SightBean.class.getClassLoader());
        this.is_follow = in.readInt();
    }

    public static final Parcelable.Creator<ItemQJCollect> CREATOR = new Parcelable.Creator<ItemQJCollect>() {
        @Override
        public ItemQJCollect createFromParcel(Parcel source) {
            return new ItemQJCollect(source);
        }

        @Override
        public ItemQJCollect[] newArray(int size) {
            return new ItemQJCollect[size];
        }
    };
}
