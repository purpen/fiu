package com.taihuoniao.fineix.zone.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lilin on 2017/5/17.
 */

public class ZoneManageGoodsBean implements Parcelable {

    /**
     * total_rows : 18
     * rows : [{"_id":"58ab9b073ffca2304d8b4577","scene_id":62,"product_id":1120700276,"type":1,"status":1,"product":{"_id":1120700276,"title":"小蚁1号智能私房音箱","short_title":"小蚁1号智能私房音箱","sale_price":299,"market_price":299,"cover_id":"548038f886709431058b7d09","commision_percent":0.1,"view_count":1898,"favorite_count":1,"love_count":4,"comment_count":12,"deleted":0,"created_on":1417689276,"cover_url":"https://p4.taihuoniao.com/product/141204/548038c8621e1957648b5482-1-p500x500.jpg"},"created_on":1487641351},{"_id":"58ab9b003ffca2024e8b4576","scene_id":62,"product_id":1120700709,"type":1,"status":1,"product":{"_id":1120700709,"title":"智能健康踏步机","short_title":"智能健康踏步机","sale_price":599,"market_price":599,"cover_id":"548047c08670942d058b7ceb","commision_percent":0.1,"view_count":476,"favorite_count":2,"love_count":2,"comment_count":0,"deleted":0,"created_on":1417693264,"cover_url":"https://p4.taihuoniao.com/product/141204/5480464c621e1947648b5481-1-p500x500.jpg"},"created_on":1487641344},{"_id":"58ab9aed3ffca2024e8b4574","scene_id":62,"product_id":1011497037,"type":1,"status":1,"product":{"_id":1011497037,"title":"越南进口Tipo友谊牌牛奶味面包干300g","short_title":"移动电源","sale_price":0.01,"market_price":299,"cover_id":"56a731c43ffca26c098baf1f","commision_percent":0.1,"view_count":1515,"favorite_count":1,"love_count":6,"comment_count":6,"deleted":0,"created_on":1453365054,"cover_url":"https://p4.taihuoniao.com/product/160126/56a731833ffca26a098baf71-5-p500x500.jpg"},"created_on":1487641325},{"_id":"58ab9adb3ffca2304d8b4573","scene_id":62,"product_id":1011497069,"type":1,"status":1,"product":{"_id":1011497069,"title":"智能WIFI信号扩大器","short_title":"扩大器","sale_price":29.9,"market_price":29.9,"cover_id":"56a718df3ffca26d098baeac","commision_percent":0.1,"view_count":1622,"favorite_count":1,"love_count":8,"comment_count":27,"deleted":0,"created_on":1453365422,"cover_url":"https://p4.taihuoniao.com/product/160126/56a718b93ffca26a098baf2c-5-p500x500.jpg"},"created_on":1487641307},{"_id":"58ab9ad23ffca2024e8b4571","scene_id":62,"product_id":1011497090,"type":1,"status":1,"product":{"_id":1011497090,"title":"【京东超市】泰国进口 克恩兹原味香脆椰子片40g","short_title":"香脆椰子片","sale_price":188,"market_price":299,"cover_id":"56a718923ffca26b098baee2","commision_percent":0.1,"view_count":482,"favorite_count":1,"love_count":2,"comment_count":0,"deleted":0,"created_on":1453365815,"cover_url":"https://p4.taihuoniao.com/product/160126/56a718823ffca26e098baed1-1-p500x500.jpg"},"created_on":1487641298},{"_id":"58ab9aca3ffca2764b8b457f","scene_id":62,"product_id":1011497097,"type":1,"status":1,"product":{"_id":1011497097,"title":"极路客行车记录仪","short_title":"优胜仕磁吸数据线","sale_price":199,"market_price":199,"cover_id":"56a7185d3ffca27b4b8b70e3","commision_percent":0.1,"view_count":1233,"favorite_count":1,"love_count":3,"comment_count":1,"deleted":0,"created_on":1453365935,"cover_url":"https://p4.taihuoniao.com/product/160126/56a7183c3ffca268098bae83-4-p500x500.jpg"},"created_on":1487641290},{"_id":"58ab9ab03ffca2304d8b4571","scene_id":62,"product_id":1011497183,"type":1,"status":1,"product":{"_id":1011497183,"title":"干衣架","short_title":"干衣架","sale_price":15.01,"market_price":199,"cover_id":"57d219683ffca2c33f8b4782","commision_percent":0.1,"view_count":2793,"favorite_count":4,"love_count":6,"comment_count":3,"deleted":0,"created_on":1453367250,"cover_url":"https://p4.taihuoniao.com/product/160909/57d219193ffca2c53f8b47a0-1-p500x500.jpg"},"created_on":1487641264},{"_id":"58ab9aa33ffca2024e8b456f","scene_id":62,"product_id":1120700195,"type":1,"status":1,"product":{"_id":1120700195,"title":"多功能星盘智能插座","short_title":"多功能星盘智能插座","sale_price":299,"market_price":299,"cover_id":"5480352c86709433058b7c4f","commision_percent":0.1,"view_count":519,"favorite_count":1,"love_count":4,"comment_count":1,"deleted":0,"created_on":1417688382,"cover_url":"https://p4.taihuoniao.com/product/141204/5480334d621e1957648b547e-3-p500x500.jpg"},"created_on":1487641251}]
     */

    public int total_rows;
    public List<RowsBean> rows;

    public static class RowsBean implements Parcelable {
        /**
         * _id : 58ab9b073ffca2304d8b4577
         * scene_id : 62
         * product_id : 1120700276
         * type : 1
         * status : 1
         * product : {"_id":1120700276,"title":"小蚁1号智能私房音箱","short_title":"小蚁1号智能私房音箱","sale_price":299,"market_price":299,"cover_id":"548038f886709431058b7d09","commision_percent":0.1,"view_count":1898,"favorite_count":1,"love_count":4,"comment_count":12,"deleted":0,"created_on":1417689276,"cover_url":"https://p4.taihuoniao.com/product/141204/548038c8621e1957648b5482-1-p500x500.jpg"}
         * created_on : 1487641351
         */

        public String _id;
        public String scene_id;
        public String product_id;
        public int type;
        public int status;
        public ProductBean product;
        public int created_on;

        public static class ProductBean implements Parcelable {
            /**
             * _id : 1120700276
             * title : 小蚁1号智能私房音箱
             * short_title : 小蚁1号智能私房音箱
             * sale_price : 299
             * market_price : 299
             * cover_id : 548038f886709431058b7d09
             * commision_percent : 0.1
             * view_count : 1898
             * favorite_count : 1
             * love_count : 4
             * comment_count : 12
             * deleted : 0
             * created_on : 1417689276
             * cover_url : https://p4.taihuoniao.com/product/141204/548038c8621e1957648b5482-1-p500x500.jpg
             */

            public int _id;
            public String title;
            public String short_title;
            public double sale_price;
            public double market_price;
            public String cover_id;
            public double commision_percent;
            public int view_count;
            public int favorite_count;
            public int love_count;
            public int comment_count;
            public int deleted;
            public String cover_url;

            public ProductBean() {
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
                dest.writeDouble(this.sale_price);
                dest.writeDouble(this.market_price);
                dest.writeString(this.cover_id);
                dest.writeDouble(this.commision_percent);
                dest.writeInt(this.view_count);
                dest.writeInt(this.favorite_count);
                dest.writeInt(this.love_count);
                dest.writeInt(this.comment_count);
                dest.writeInt(this.deleted);
                dest.writeString(this.cover_url);
            }

            protected ProductBean(Parcel in) {
                this._id = in.readInt();
                this.title = in.readString();
                this.short_title = in.readString();
                this.sale_price = in.readDouble();
                this.market_price = in.readDouble();
                this.cover_id = in.readString();
                this.commision_percent = in.readDouble();
                this.view_count = in.readInt();
                this.favorite_count = in.readInt();
                this.love_count = in.readInt();
                this.comment_count = in.readInt();
                this.deleted = in.readInt();
                this.cover_url = in.readString();
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

        public RowsBean() {
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this._id);
            dest.writeString(this.scene_id);
            dest.writeString(this.product_id);
            dest.writeInt(this.type);
            dest.writeInt(this.status);
            dest.writeParcelable(this.product, flags);
            dest.writeInt(this.created_on);
        }

        protected RowsBean(Parcel in) {
            this._id = in.readString();
            this.scene_id = in.readString();
            this.product_id = in.readString();
            this.type = in.readInt();
            this.status = in.readInt();
            this.product = in.readParcelable(ProductBean.class.getClassLoader());
            this.created_on = in.readInt();
        }

        public static final Creator<RowsBean> CREATOR = new Creator<RowsBean>() {
            @Override
            public RowsBean createFromParcel(Parcel source) {
                return new RowsBean(source);
            }

            @Override
            public RowsBean[] newArray(int size) {
                return new RowsBean[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.total_rows);
        dest.writeList(this.rows);
    }

    public ZoneManageGoodsBean() {
    }

    protected ZoneManageGoodsBean(Parcel in) {
        this.total_rows = in.readInt();
        this.rows = new ArrayList<RowsBean>();
        in.readList(this.rows, RowsBean.class.getClassLoader());
    }

    public static final Creator<ZoneManageGoodsBean> CREATOR = new Creator<ZoneManageGoodsBean>() {
        @Override
        public ZoneManageGoodsBean createFromParcel(Parcel source) {
            return new ZoneManageGoodsBean(source);
        }

        @Override
        public ZoneManageGoodsBean[] newArray(int size) {
            return new ZoneManageGoodsBean[size];
        }
    };
}
