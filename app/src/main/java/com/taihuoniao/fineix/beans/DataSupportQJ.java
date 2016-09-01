package com.taihuoniao.fineix.beans;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * @author lilin
 *         created at 2016/6/23 16:54
 */
public class DataSupportQJ implements Parcelable {
    public ArrayList<ItemSupportQJ> rows;

    public static class ItemSupportQJ implements Parcelable {
        public String _id;
        public String user_id;
        public String target_id;
        public UserBean user;
        public SightBean sight;

        public static class UserBean implements Parcelable {
            public String _id;
            public String nickname;
            public String summary;

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(this._id);
                dest.writeString(this.nickname);
                dest.writeString(this.summary);
            }

            public UserBean() {
            }

            protected UserBean(Parcel in) {
                this._id = in.readString();
                this.nickname = in.readString();
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
            public String _id;
            public String cover_url;
            public String created_at;
            public UserInfoBean user_info;
            public String title;
            public int is_love;
            public static class UserInfoBean implements Parcelable {
                public String user_id;
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
                    dest.writeString(this.user_id);
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
                    this.user_id = in.readString();
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

            public SightBean() {
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(this._id);
                dest.writeString(this.cover_url);
                dest.writeString(this.created_at);
                dest.writeParcelable(this.user_info, flags);
                dest.writeString(this.title);
                dest.writeInt(this.is_love);
            }

            protected SightBean(Parcel in) {
                this._id = in.readString();
                this.cover_url = in.readString();
                this.created_at = in.readString();
                this.user_info = in.readParcelable(UserInfoBean.class.getClassLoader());
                this.title = in.readString();
                this.is_love = in.readInt();
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
            dest.writeString(this.user_id);
            dest.writeString(this.target_id);
            dest.writeParcelable(this.user, flags);
            dest.writeParcelable(this.sight, flags);
        }

        public ItemSupportQJ() {
        }

        protected ItemSupportQJ(Parcel in) {
            this._id = in.readString();
            this.user_id = in.readString();
            this.target_id = in.readString();
            this.user = in.readParcelable(UserBean.class.getClassLoader());
            this.sight = in.readParcelable(SightBean.class.getClassLoader());
        }

        public static final Creator<ItemSupportQJ> CREATOR = new Creator<ItemSupportQJ>() {
            @Override
            public ItemSupportQJ createFromParcel(Parcel source) {
                return new ItemSupportQJ(source);
            }

            @Override
            public ItemSupportQJ[] newArray(int size) {
                return new ItemSupportQJ[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.rows);
    }

    public DataSupportQJ() {
    }

    protected DataSupportQJ(Parcel in) {
        this.rows = in.createTypedArrayList(ItemSupportQJ.CREATOR);
    }

    public static final Creator<DataSupportQJ> CREATOR = new Creator<DataSupportQJ>() {
        @Override
        public DataSupportQJ createFromParcel(Parcel source) {
            return new DataSupportQJ(source);
        }

        @Override
        public DataSupportQJ[] newArray(int size) {
            return new DataSupportQJ[size];
        }
    };
}
