package com.taihuoniao.fineix.zone.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.baidu.mapapi.model.LatLng;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lilin on 2017/2/15.
 */

public class ZoneDetailBean implements Parcelable {

    /**
     * _id : 62
     * title : D3IN
     * sub_title : D3IN线下体验店
     * avatar_url : https://p4.taihuoniao.com/scene_scene/170214/58a25ec73ffca202478b47dc-1-p500x500.jpg
     * created_at : 昨天
     * user : {"_id":10,"nickname":"太火鸟","avatar_url":"https://p4.taihuoniao.com/avatar/160321/56efa2e33ffca269098c0846-avb.jpg","is_expert":0,"label":"","expert_label":"","expert_info":""}
     * love_count : 0
     * view_count : 4
     * city :
     * address : 北京 798艺术区
     * location : {"type":"Point","coordinates":[116.506495,39.992067]}
     * is_love : 0
     * banners : ["https://p4.taihuoniao.com/scene_scene/170214/58a25ec73ffca202478b47db-4-p750x422.jpg","https://p4.taihuoniao.com/scene_scene/170214/58a25ec73ffca202478b47db-3-p750x422.jpg","https://p4.taihuoniao.com/scene_scene/170214/58a25ec73ffca202478b47db-2-p750x422.jpg"]
     * tags : ["智能硬件","体验店","798艺术区"]
     * extra : {"shop_hours":"早8:00--晚9:00","tel":"010-12345678"}
     * score_average : 5
     * sights : []
     * products : []
     * current_user_id : 20448
     */

    public String _id;
    public String title;
    public String sub_title;
    public String avatar_url;
    public String created_at;
    public UserBean user;
    public int love_count;
    public int view_count;
    public String des;
    public String city;
    public String address;
    public LocationBean location;
    public int is_love;
    public int is_favorite;
    public ExtraBean extra;
    public int score_average;
    public int current_user_id;
    public List<String> banners;
    public List<String> tags;
    public List<String> bright_spot;
    public static class UserBean implements Parcelable {
        /**
         * _id : 10
         * nickname : 太火鸟
         * avatar_url : https://p4.taihuoniao.com/avatar/160321/56efa2e33ffca269098c0846-avb.jpg
         * is_expert : 0
         * label :
         * expert_label :
         * expert_info :
         */

        public int _id;
        public String nickname;
        public String avatar_url;
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
            dest.writeInt(this._id);
            dest.writeString(this.nickname);
            dest.writeString(this.avatar_url);
            dest.writeInt(this.is_expert);
            dest.writeString(this.label);
            dest.writeString(this.expert_label);
            dest.writeString(this.expert_info);
        }

        public UserBean() {
        }

        protected UserBean(Parcel in) {
            this._id = in.readInt();
            this.nickname = in.readString();
            this.avatar_url = in.readString();
            this.is_expert = in.readInt();
            this.label = in.readString();
            this.expert_label = in.readString();
            this.expert_info = in.readString();
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

    public static class LocationBean implements Parcelable {
        /**
         * type : Point
         * coordinates : [116.506495,39.992067]
         */

        public String type;
        public List<Double> coordinates;
        public LatLng myLocation;

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.type);
            dest.writeList(this.coordinates);
            dest.writeParcelable(this.myLocation, flags);
        }

        public LocationBean() {
        }

        protected LocationBean(Parcel in) {
            this.type = in.readString();
            this.coordinates = new ArrayList<Double>();
            in.readList(this.coordinates, Double.class.getClassLoader());
            this.myLocation = in.readParcelable(LatLng.class.getClassLoader());
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

    public static class ExtraBean implements Serializable{
        /**
         * shop_hours : 早8:00--晚9:00
         * tel : 010-12345678
         */

        public String shop_hours;
        public String tel;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this._id);
        dest.writeString(this.title);
        dest.writeString(this.sub_title);
        dest.writeString(this.avatar_url);
        dest.writeString(this.created_at);
        dest.writeParcelable(this.user, flags);
        dest.writeInt(this.love_count);
        dest.writeInt(this.view_count);
        dest.writeString(this.des);
        dest.writeString(this.city);
        dest.writeString(this.address);
        dest.writeParcelable(this.location, flags);
        dest.writeInt(this.is_love);
        dest.writeInt(this.is_favorite);
        dest.writeSerializable(this.extra);
        dest.writeInt(this.score_average);
        dest.writeInt(this.current_user_id);
        dest.writeStringList(this.banners);
        dest.writeStringList(this.tags);
        dest.writeStringList(this.bright_spot);
    }

    public ZoneDetailBean() {
    }

    protected ZoneDetailBean(Parcel in) {
        this._id = in.readString();
        this.title = in.readString();
        this.sub_title = in.readString();
        this.avatar_url = in.readString();
        this.created_at = in.readString();
        this.user = in.readParcelable(UserBean.class.getClassLoader());
        this.love_count = in.readInt();
        this.view_count = in.readInt();
        this.des = in.readString();
        this.city = in.readString();
        this.address = in.readString();
        this.location = in.readParcelable(LocationBean.class.getClassLoader());
        this.is_love = in.readInt();
        this.is_favorite = in.readInt();
        this.extra = (ExtraBean) in.readSerializable();
        this.score_average = in.readInt();
        this.current_user_id = in.readInt();
        this.banners = in.createStringArrayList();
        this.tags = in.createStringArrayList();
        this.bright_spot = in.createStringArrayList();
    }

    public static final Parcelable.Creator<ZoneDetailBean> CREATOR = new Parcelable.Creator<ZoneDetailBean>() {
        @Override
        public ZoneDetailBean createFromParcel(Parcel source) {
            return new ZoneDetailBean(source);
        }

        @Override
        public ZoneDetailBean[] newArray(int size) {
            return new ZoneDetailBean[size];
        }
    };
}
