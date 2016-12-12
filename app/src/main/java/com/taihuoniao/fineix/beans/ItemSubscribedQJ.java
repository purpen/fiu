package com.taihuoniao.fineix.beans;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lilin
 *         created at 2016/6/23 16:56
 */
public class ItemSubscribedQJ implements Parcelable {
    public int _id;
    public String title;
    public int user_id;
    public String des;
    public int scene_id;
    public int category_id;

    public LocationBean location;
    public String city;
    public String address;
    public int used_count;
    public int view_count;
    public int love_count;
    public int comment_count;
    public int stick;
    public int fine;
    public int is_check;
    public int status;
    public int deleted;
    public int created_on;
    public int updated_on;
    public String tags_s;
    public String category_ids_s;
    public String cover_url;
    public String created_at;
    public String scene_title;
    public UserInfoBean user_info;
    public int is_love;
    public List<Integer> category_ids;
    public List<String> tags;
    public List<ProductBean> product;

    public static class LocationBean implements Parcelable {
        public String type;
        public List<Double> coordinates;

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.type);
            dest.writeList(this.coordinates);
        }

        public LocationBean() {
        }

        protected LocationBean(Parcel in) {
            this.type = in.readString();
            this.coordinates = new ArrayList<>();
            in.readList(this.coordinates, Double.class.getClassLoader());
        }

        public static final Creator<LocationBean> CREATOR = new Creator<LocationBean>() {
            @Override
            public LocationBean createFromParcel(Parcel source) {
                return new LocationBean(source);
            }

            @Override
            public LocationBean[] newArray(int size) {
                return new LocationBean[size];
            }
        };
    }

    public static class UserInfoBean implements Parcelable {
        public int user_id;
        public String nickname;
        public String avatar_url;
        public String summary;
        public CounterBean counter;
        public int follow_count;
        public int fans_count;
        public int love_count;
        public int is_expert;
        public String label;
        public String expert_label;
        public String expert_info;

        public static class CounterBean implements Parcelable {
            public int message_count;
            public int notice_count;
            public int alert_count;
            public int fans_count;
            public int comment_count;
            public int people_count;
            public int fiu_comment_count;
            public int fiu_alert_count;
            public int fiu_notice_count;
            public int order_wait_payment;
            public int order_ready_goods;

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeInt(this.message_count);
                dest.writeInt(this.notice_count);
                dest.writeInt(this.alert_count);
                dest.writeInt(this.fans_count);
                dest.writeInt(this.comment_count);
                dest.writeInt(this.people_count);
                dest.writeInt(this.fiu_comment_count);
                dest.writeInt(this.fiu_alert_count);
                dest.writeInt(this.fiu_notice_count);
                dest.writeInt(this.order_wait_payment);
                dest.writeInt(this.order_ready_goods);
            }

            public CounterBean() {
            }

            protected CounterBean(Parcel in) {
                this.message_count = in.readInt();
                this.notice_count = in.readInt();
                this.alert_count = in.readInt();
                this.fans_count = in.readInt();
                this.comment_count = in.readInt();
                this.people_count = in.readInt();
                this.fiu_comment_count = in.readInt();
                this.fiu_alert_count = in.readInt();
                this.fiu_notice_count = in.readInt();
                this.order_wait_payment = in.readInt();
                this.order_ready_goods = in.readInt();
            }

            public static final Creator<CounterBean> CREATOR = new Creator<CounterBean>() {
                @Override
                public CounterBean createFromParcel(Parcel source) {
                    return new CounterBean(source);
                }

                @Override
                public CounterBean[] newArray(int size) {
                    return new CounterBean[size];
                }
            };
        }

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
            dest.writeParcelable(this.counter, flags);
            dest.writeInt(this.follow_count);
            dest.writeInt(this.fans_count);
            dest.writeInt(this.love_count);
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
            this.counter = in.readParcelable(CounterBean.class.getClassLoader());
            this.follow_count = in.readInt();
            this.fans_count = in.readInt();
            this.love_count = in.readInt();
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

    public static class ProductBean {
        public int id;
        public String title;
        public double x;
        public double y;
        public int loc;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this._id);
        dest.writeString(this.title);
        dest.writeInt(this.user_id);
        dest.writeString(this.des);
        dest.writeInt(this.scene_id);
        dest.writeInt(this.category_id);
        dest.writeParcelable(this.location, flags);
        dest.writeString(this.city);
        dest.writeString(this.address);
        dest.writeInt(this.used_count);
        dest.writeInt(this.view_count);
        dest.writeInt(this.love_count);
        dest.writeInt(this.comment_count);
        dest.writeInt(this.stick);
        dest.writeInt(this.fine);
        dest.writeInt(this.is_check);
        dest.writeInt(this.status);
        dest.writeInt(this.deleted);
        dest.writeInt(this.created_on);
        dest.writeInt(this.updated_on);
        dest.writeString(this.tags_s);
        dest.writeString(this.category_ids_s);
        dest.writeString(this.cover_url);
        dest.writeString(this.created_at);
        dest.writeString(this.scene_title);
        dest.writeParcelable(this.user_info, flags);
        dest.writeInt(this.is_love);
        dest.writeList(this.category_ids);
        dest.writeStringList(this.tags);
        dest.writeList(this.product);
    }

    public ItemSubscribedQJ() {
    }

    protected ItemSubscribedQJ(Parcel in) {
        this._id = in.readInt();
        this.title = in.readString();
        this.user_id = in.readInt();
        this.des = in.readString();
        this.scene_id = in.readInt();
        this.category_id = in.readInt();
        this.location = in.readParcelable(LocationBean.class.getClassLoader());
        this.city = in.readString();
        this.address = in.readString();
        this.used_count = in.readInt();
        this.view_count = in.readInt();
        this.love_count = in.readInt();
        this.comment_count = in.readInt();
        this.stick = in.readInt();
        this.fine = in.readInt();
        this.is_check = in.readInt();
        this.status = in.readInt();
        this.deleted = in.readInt();
        this.created_on = in.readInt();
        this.updated_on = in.readInt();
        this.tags_s = in.readString();
        this.category_ids_s = in.readString();
        this.cover_url = in.readString();
        this.created_at = in.readString();
        this.scene_title = in.readString();
        this.user_info = in.readParcelable(UserInfoBean.class.getClassLoader());
        this.is_love = in.readInt();
        this.category_ids = new ArrayList<>();
        in.readList(this.category_ids, Integer.class.getClassLoader());
        this.tags = in.createStringArrayList();
        this.product = new ArrayList<>();
        in.readList(this.product, ProductBean.class.getClassLoader());
    }

    public static final Creator<ItemSubscribedQJ> CREATOR = new Creator<ItemSubscribedQJ>() {
        @Override
        public ItemSubscribedQJ createFromParcel(Parcel source) {
            return new ItemSubscribedQJ(source);
        }

        @Override
        public ItemSubscribedQJ[] newArray(int size) {
            return new ItemSubscribedQJ[size];
        }
    };
}
