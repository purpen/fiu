package com.taihuoniao.fineix.beans;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * @author lilin
 *         created at 2016/8/17 17:11
 */
public class ItemProductCollect implements Parcelable {
    public String _id;
    public int user_id;
    public int target_id;
    public int type;
    public int event;
    public UserBean user;
    public SceneProductBean product;
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

    public static class SceneProductBean implements Parcelable {
        public int _id;
        public String title;
        public String cover_url;
        public int attrbute;
        public String sale_price;
        public String market_price;
        public List<String> banner_asset;

        public SceneProductBean() {
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this._id);
            dest.writeString(this.title);
            dest.writeString(this.cover_url);
            dest.writeInt(this.attrbute);
            dest.writeString(this.sale_price);
            dest.writeString(this.market_price);
            dest.writeStringList(this.banner_asset);
        }

        protected SceneProductBean(Parcel in) {
            this._id = in.readInt();
            this.title = in.readString();
            this.cover_url = in.readString();
            this.attrbute = in.readInt();
            this.sale_price = in.readString();
            this.market_price = in.readString();
            this.banner_asset = in.createStringArrayList();
        }

        public static final Creator<SceneProductBean> CREATOR = new Creator<SceneProductBean>() {
            @Override
            public SceneProductBean createFromParcel(Parcel source) {
                return new SceneProductBean(source);
            }

            @Override
            public SceneProductBean[] newArray(int size) {
                return new SceneProductBean[size];
            }
        };
    }

    public ItemProductCollect() {
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
        dest.writeParcelable(this.product, flags);
        dest.writeInt(this.is_follow);
    }

    protected ItemProductCollect(Parcel in) {
        this._id = in.readString();
        this.user_id = in.readInt();
        this.target_id = in.readInt();
        this.type = in.readInt();
        this.event = in.readInt();
        this.user = in.readParcelable(UserBean.class.getClassLoader());
        this.product = in.readParcelable(SceneProductBean.class.getClassLoader());
        this.is_follow = in.readInt();
    }

    public static final Creator<ItemProductCollect> CREATOR = new Creator<ItemProductCollect>() {
        @Override
        public ItemProductCollect createFromParcel(Parcel source) {
            return new ItemProductCollect(source);
        }

        @Override
        public ItemProductCollect[] newArray(int size) {
            return new ItemProductCollect[size];
        }
    };
}
