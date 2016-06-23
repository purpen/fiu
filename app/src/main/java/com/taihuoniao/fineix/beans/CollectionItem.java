package com.taihuoniao.fineix.beans;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * @author lilin
 *         created at 2016/6/23 16:56
 */
public class CollectionItem implements Parcelable {
    public int pos;
    public String _id;
    public String title;
    public String sale_price;
    public String market_price;
    public String love_count;
    public String cover_url;
    public String attrbute;
    public List<String> category_tags;
    public List<String> banner_asset;
    public List<CjBean> sights;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.pos);
        dest.writeString(this._id);
        dest.writeString(this.title);
        dest.writeString(this.sale_price);
        dest.writeString(this.market_price);
        dest.writeString(this.love_count);
        dest.writeString(this.cover_url);
        dest.writeString(this.attrbute);
        dest.writeStringList(this.category_tags);
        dest.writeStringList(this.banner_asset);
        dest.writeTypedList(this.sights);
    }

    public CollectionItem() {
    }

    protected CollectionItem(Parcel in) {
        this.pos = in.readInt();
        this._id = in.readString();
        this.title = in.readString();
        this.sale_price = in.readString();
        this.market_price = in.readString();
        this.love_count = in.readString();
        this.cover_url = in.readString();
        this.attrbute = in.readString();
        this.category_tags = in.createStringArrayList();
        this.banner_asset = in.createStringArrayList();
        this.sights = in.createTypedArrayList(CjBean.CREATOR);
    }

    public static final Parcelable.Creator<CollectionItem> CREATOR = new Parcelable.Creator<CollectionItem>() {
        @Override
        public CollectionItem createFromParcel(Parcel source) {
            return new CollectionItem(source);
        }

        @Override
        public CollectionItem[] newArray(int size) {
            return new CollectionItem[size];
        }
    };
}
