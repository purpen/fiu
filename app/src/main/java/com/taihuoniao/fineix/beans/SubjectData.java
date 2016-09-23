package com.taihuoniao.fineix.beans;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lilin
 *         created at 2016/6/15 23:33
 */
public class SubjectData implements Parcelable {
    public int _id;
    public String title;
    public String short_title;
    public String tags_s;
    public int kind;
    public int evt;
    public int type;
    public String cover_id;
    public int category_id;
    public String summary;
    public int status;
    public int publish;
    public int user_id;
    public int stick;
    public int love_count;
    public int favorite_count;
    public int view_count;
    public int share_count;
    public int comment_count;
    public String cover_url;
    public String banner_url;
    public int is_love;
    public String content_view_url;
    public String share_view_url;
    public String share_desc;
    public String begin_time_at;
    public String end_time_at;
    public List<String> tags;
    public Product product;
    public ArrayList<SightBean> sights;
    public ArrayList<ProductBean> products;

    public SubjectData() {
    }

    public static class SightBean implements Parcelable {
        public String _id;
        public String title;
        public String cover_url;
        public String created_at;
        public UserBean user;
        public String prize;
        public ArrayList<ProductBean> product;

        public static class ProductBean implements Parcelable {
            public String id;
            public String title;
            public double x;
            public double y;
            public int loc;

            public ProductBean() {
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(this.id);
                dest.writeString(this.title);
                dest.writeDouble(this.x);
                dest.writeDouble(this.y);
                dest.writeInt(this.loc);
            }

            protected ProductBean(Parcel in) {
                this.id = in.readString();
                this.title = in.readString();
                this.x = in.readDouble();
                this.y = in.readDouble();
                this.loc = in.readInt();
            }

            public static final Creator<ProductBean> CREATOR = new Creator<ProductBean>() {
                @Override
                public ProductBean createFromParcel(Parcel source) {
                    return new ProductBean(source);
                }

                @Override
                public ProductBean[] newArray(int size) {
                    return new ProductBean[size];
                }
            };
        }
        public static class UserBean implements Parcelable {
            public long _id;
            public String nickname;
            public int is_expert;
            public String avatar_url;
            public int is_follow;
            public String address;
            public String city;
            public String prize;
            public LocationBean location;

            public static class LocationBean implements Parcelable {
                public String type;
                public String prize;
                public List<Double> coordinates;

                @Override
                public int describeContents() {
                    return 0;
                }

                @Override
                public void writeToParcel(Parcel dest, int flags) {
                    dest.writeString(this.type);
                    dest.writeString(this.prize);
                    dest.writeList(this.coordinates);
                }

                public LocationBean() {
                }

                protected LocationBean(Parcel in) {
                    this.type = in.readString();
                    this.prize = in.readString();
                    this.coordinates = new ArrayList<Double>();
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

            public UserBean() {
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeLong(this._id);
                dest.writeString(this.nickname);
                dest.writeInt(this.is_expert);
                dest.writeString(this.avatar_url);
                dest.writeInt(this.is_follow);
                dest.writeString(this.address);
                dest.writeString(this.city);
                dest.writeParcelable(this.location, flags);
            }

            protected UserBean(Parcel in) {
                this._id = in.readLong();
                this.nickname = in.readString();
                this.is_expert = in.readInt();
                this.avatar_url = in.readString();
                this.is_follow = in.readInt();
                this.address = in.readString();
                this.city = in.readString();
                this.location = in.readParcelable(LocationBean.class.getClassLoader());
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

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this._id);
            dest.writeString(this.title);
            dest.writeString(this.cover_url);
            dest.writeString(this.created_at);
            dest.writeParcelable(this.user, flags);
        }

        public SightBean() {
        }

        protected SightBean(Parcel in) {
            this._id = in.readString();
            this.title = in.readString();
            this.cover_url = in.readString();
            this.created_at = in.readString();
            this.user = in.readParcelable(UserBean.class.getClassLoader());
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

    public static class ProductBean implements Parcelable {
        public String _id;
        public String title;
        public String banner_url;
        public String summary;
        public String market_price;
        public String sale_price;

        public ProductBean() {
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this._id);
            dest.writeString(this.title);
            dest.writeString(this.banner_url);
            dest.writeString(this.summary);
            dest.writeString(this.market_price);
            dest.writeString(this.sale_price);
        }

        protected ProductBean(Parcel in) {
            this._id = in.readString();
            this.title = in.readString();
            this.banner_url = in.readString();
            this.summary = in.readString();
            this.market_price = in.readString();
            this.sale_price = in.readString();
        }

        public static final Creator<ProductBean> CREATOR = new Creator<ProductBean>() {
            @Override
            public ProductBean createFromParcel(Parcel source) {
                return new ProductBean(source);
            }

            @Override
            public ProductBean[] newArray(int size) {
                return new ProductBean[size];
            }
        };
    }

    public static class Product implements Parcelable {
        public String _id;
        public int is_favorite;

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this._id);
            dest.writeInt(this.is_favorite);
        }

        public Product() {
        }

        protected Product(Parcel in) {
            this._id = in.readString();
            this.is_favorite = in.readInt();
        }

        public static final Creator<Product> CREATOR = new Creator<Product>() {
            @Override
            public Product createFromParcel(Parcel source) {
                return new Product(source);
            }

            @Override
            public Product[] newArray(int size) {
                return new Product[size];
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
        dest.writeString(this.title);
        dest.writeString(this.short_title);
        dest.writeString(this.tags_s);
        dest.writeInt(this.kind);
        dest.writeInt(this.evt);
        dest.writeString(this.cover_id);
        dest.writeInt(this.category_id);
        dest.writeString(this.summary);
        dest.writeInt(this.status);
        dest.writeInt(this.publish);
        dest.writeInt(this.user_id);
        dest.writeInt(this.stick);
        dest.writeInt(this.love_count);
        dest.writeInt(this.favorite_count);
        dest.writeInt(this.view_count);
        dest.writeInt(this.share_count);
        dest.writeInt(this.comment_count);
        dest.writeString(this.cover_url);
        dest.writeString(this.banner_url);
        dest.writeInt(this.is_love);
        dest.writeString(this.content_view_url);
        dest.writeString(this.share_view_url);
        dest.writeString(this.share_desc);
        dest.writeString(this.begin_time_at);
        dest.writeString(this.end_time_at);
        dest.writeStringList(this.tags);
        dest.writeParcelable(this.product, flags);
        dest.writeTypedList(this.sights);
        dest.writeTypedList(this.products);
    }

    protected SubjectData(Parcel in) {
        this._id = in.readInt();
        this.title = in.readString();
        this.short_title = in.readString();
        this.tags_s = in.readString();
        this.kind = in.readInt();
        this.evt = in.readInt();
        this.cover_id = in.readString();
        this.category_id = in.readInt();
        this.summary = in.readString();
        this.status = in.readInt();
        this.publish = in.readInt();
        this.user_id = in.readInt();
        this.stick = in.readInt();
        this.love_count = in.readInt();
        this.favorite_count = in.readInt();
        this.view_count = in.readInt();
        this.share_count = in.readInt();
        this.comment_count = in.readInt();
        this.cover_url = in.readString();
        this.banner_url = in.readString();
        this.is_love = in.readInt();
        this.content_view_url = in.readString();
        this.share_view_url = in.readString();
        this.share_desc = in.readString();
        this.begin_time_at = in.readString();
        this.end_time_at = in.readString();
        this.tags = in.createStringArrayList();
        this.product = in.readParcelable(Product.class.getClassLoader());
        this.sights = in.createTypedArrayList(SightBean.CREATOR);
        this.products = in.createTypedArrayList(ProductBean.CREATOR);
    }

    public static final Creator<SubjectData> CREATOR = new Creator<SubjectData>() {
        @Override
        public SubjectData createFromParcel(Parcel source) {
            return new SubjectData(source);
        }

        @Override
        public SubjectData[] newArray(int size) {
            return new SubjectData[size];
        }
    };
}
