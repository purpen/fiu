package com.taihuoniao.fineix.zone.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.baidu.mapapi.model.LatLng;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lilin on 2017/2/15.
 */

public class ZoneDetailBean implements Parcelable {


    /**
     * _id : 133
     * title : D³IN 铟立方未来商店
     * sub_title : 科技美学产品情境式体验店
     * avatar_url : https://p4.taihuoniao.com/scene_scene/170221/58ac12bc20de8dbb068b56a5-1-p500x500.jpg
     * created_at : 2017-02-14
     * user : {"_id":2,"nickname":"甜小橙","avatar_url":"https://p4.taihuoniao.com/avatar/150909/55ef0e51fc8b12a5088c1f25-avb.jpg","is_expert":0,"label":"","expert_label":"","expert_info":""}
     * love_count : 0
     * view_count : 302
     * city :
     * address : 北京市朝阳区酒仙桥路4号751时尚设计广场B7栋
     * location : {"type":"Point","coordinates":[116.50562,39.989573]}
     * is_love : 0
     * is_favorite : 1
     * banners : ["https://p4.taihuoniao.com/scene_scene/170214/58a2a58620de8d2d7e8b526d-3-p750x422.jpg","https://p4.taihuoniao.com/scene_scene/170214/58a2a58620de8d2d7e8b526d-2-p750x422.jpg"]
     * covers : ["https://p4.taihuoniao.com/scene_scene/170221/58ac12bc20de8dbb068b56a3-2-p500x500.jpg","https://p4.taihuoniao.com/scene_scene/170221/58ac12bc20de8dbb068b56a3-9-p500x500.jpg","https://p4.taihuoniao.com/scene_scene/170221/58ac12bc20de8dbb068b56a3-10-p500x500.jpg","https://p4.taihuoniao.com/scene_scene/170221/58ac12bc20de8dbb068b56a3-11-p500x500.jpg","https://p4.taihuoniao.com/scene_scene/170221/58ac12bc20de8dbb068b56a3-8-p500x500.jpg","https://p4.taihuoniao.com/scene_scene/170221/58ac12bc20de8dbb068b56a3-7-p500x500.jpg","https://p4.taihuoniao.com/scene_scene/170221/58ac12bc20de8dbb068b56a3-4-p500x500.jpg","https://p4.taihuoniao.com/scene_scene/170221/58ac12bc20de8dbb068b56a3-6-p500x500.jpg","https://p4.taihuoniao.com/scene_scene/170221/58ac12bc20de8dbb068b56a3-5-p500x500.jpg","https://p4.taihuoniao.com/scene_scene/170221/58ac12bc20de8dbb068b56a3-3-p500x500.jpg"]
     * des : D³IN 铟立方未来商店是中国首个科技美学产品的情境式体验店，通过用户场景式体验，增加人与物的交互。目前已经整合了国内外200多个智能硬件品牌和500多个SKU产品，处在流量巨大的751创意产业园区。从地理位置、太火鸟行业资源、强大的SKU以及未来感十足的店铺形象设计来看，未来大有可为。“未来已来”的主题设定也使其科技美学定位与和时尚生活形象更具有先锋概念。顾客可以在店铺设定的场景中与摆设的智能产品互动，也可以体验新美学生活方式，寻找属于自己的产品社群。
     * tags : ["D3IN","智能硬件","科技美学","体验店"]
     * extra : {"shop_hours":"10:00-19:00","tel":"010-84599328"}
     * score_average : 5
     * bright_spot : ["[text]:!D3IN铟立方未来商店是中国首个科技美学产品的情境式体验店，旨在通过用户场景式体验，增加人与物的交互。整体设计以未来蓝、钢琴黑、原色白为主，你可以在店铺设定的场景中与摆设的智能产品互动，也可以体验新美学生活方式，寻找属于自己的产品社群。国内外一线原创设计品牌触手可及，其中包括朱哲琴创立的中国原创设计代表看见造物、丹麦高端时尚音响Libratone小鸟音响、soocare素士电动声波牙刷、云马电动自行车等备受瞩目的科技产品和家居新品\u2026\u2026这里将是新兴中产阶层消费升级风向标，科技和设计驱动的新品聚集地。","[img]:!https://p4.taihuoniao.com/asset/170221/58ac142820de8dca448b5c04-1-hu.jpg","[text]:!一楼主要展示品牌最设计和科技最先锋的创意产品，人流量最大的地方是VR虚拟现实游戏体验区，大家都知道798艺术区、751D·PARK、望京周围聚集了大量工程师和科技宅男，所以未来店的这部分基本都被他们包围了。","[img]:!https://p4.taihuoniao.com/asset/170221/58ac142820de8dca448b5c04-2-hu.jpg","[text]:!二楼则主要陈列生活美学创意家居产品。顾客可以看到中国原创设计代表品牌\u201c看见造物\u201d的苗银素包、茶盘、昆虫折扇、丝巾等，这个系列集合了中国民族的传统文化与工匠精神，而顾客购买的产品也将有部分用来支持民族技艺和手艺人的生存；也可以看到文艺女青年们喜欢的，价格亲民、手感舒适的橙舍茶具系列；除了东方韵味的创意设计产品，靠近玻璃围墙的二楼两侧分别设置了现代简欧风格的茶歇休息区，而顾客看到、触摸的产品在Fiu浮游APP上都可以购买。","[img]:!https://p4.taihuoniao.com/asset/170221/58ac142820de8dca448b5c04-3-hu.jpg","[text]:!D3IN铟立方未来商店还将依托情境式电商平台D3IN以及太火鸟强大的行业资源优势，打造\u201c线上消费+线下体验\u201d的全新O2O模式商业服务体验。超过200家智能品牌及生活美学产品入驻，500+SKU实景体验，真正做到看到即买到，使便捷的现代科技与生活美学以更自如的方式应用到实际生活中去。","[img]:!https://p4.taihuoniao.com/asset/170221/58ac142820de8dca448b5c04-4-hu.jpg"]
     * view_url : https://m.taihuoniao.com/storage/view?id=133
     * current_user_id : 20448
     */

    public int _id;
    public String title;
    public String sub_title;
    public String avatar_url;
    public String created_at;
    public UserBean user;
    public int love_count;
    public int view_count;
    public String city;
    public String address;
    public LocationBean location;
    public int is_love;
    public int is_favorite;
    public String des;
    public ExtraBean extra;
    public int score_average;
    public String view_url;
    public int current_user_id;
    public List<String> banners;
    public List<String> covers;
    public List<String> tags;
    public List<String> bright_spot;

    public static class UserBean implements Parcelable {
        /**
         * _id : 2
         * nickname : 甜小橙
         * avatar_url : https://p4.taihuoniao.com/avatar/150909/55ef0e51fc8b12a5088c1f25-avb.jpg
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
         * coordinates : [116.50562,39.989573]
         */

        public String type;
        public List<Double> coordinates;
        public LatLng myLocation;

        public LocationBean() {
        }

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

    public static class ExtraBean implements Parcelable {
        /**
         * shop_hours : 10:00-19:00
         * tel : 010-84599328
         */

        public String shop_hours;
        public String tel;

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.shop_hours);
            dest.writeString(this.tel);
        }

        public ExtraBean() {
        }

        protected ExtraBean(Parcel in) {
            this.shop_hours = in.readString();
            this.tel = in.readString();
        }

        public static final Creator<ExtraBean> CREATOR = new Creator<ExtraBean>() {
            @Override
            public ExtraBean createFromParcel(Parcel source) {
                return new ExtraBean(source);
            }

            @Override
            public ExtraBean[] newArray(int size) {
                return new ExtraBean[size];
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
        dest.writeString(this.sub_title);
        dest.writeString(this.avatar_url);
        dest.writeString(this.created_at);
        dest.writeParcelable(this.user, flags);
        dest.writeInt(this.love_count);
        dest.writeInt(this.view_count);
        dest.writeString(this.city);
        dest.writeString(this.address);
        dest.writeParcelable(this.location, flags);
        dest.writeInt(this.is_love);
        dest.writeInt(this.is_favorite);
        dest.writeString(this.des);
        dest.writeParcelable(this.extra, flags);
        dest.writeInt(this.score_average);
        dest.writeString(this.view_url);
        dest.writeInt(this.current_user_id);
        dest.writeStringList(this.banners);
        dest.writeStringList(this.covers);
        dest.writeStringList(this.tags);
        dest.writeStringList(this.bright_spot);
    }

    public ZoneDetailBean() {
    }

    protected ZoneDetailBean(Parcel in) {
        this._id = in.readInt();
        this.title = in.readString();
        this.sub_title = in.readString();
        this.avatar_url = in.readString();
        this.created_at = in.readString();
        this.user = in.readParcelable(UserBean.class.getClassLoader());
        this.love_count = in.readInt();
        this.view_count = in.readInt();
        this.city = in.readString();
        this.address = in.readString();
        this.location = in.readParcelable(LocationBean.class.getClassLoader());
        this.is_love = in.readInt();
        this.is_favorite = in.readInt();
        this.des = in.readString();
        this.extra = in.readParcelable(ExtraBean.class.getClassLoader());
        this.score_average = in.readInt();
        this.view_url = in.readString();
        this.current_user_id = in.readInt();
        this.banners = in.createStringArrayList();
        this.covers = in.createStringArrayList();
        this.tags = in.createStringArrayList();
        this.bright_spot = in.createStringArrayList();
    }

    public static final Creator<ZoneDetailBean> CREATOR = new Creator<ZoneDetailBean>() {
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
