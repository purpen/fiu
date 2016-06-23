package com.taihuoniao.fineix.beans;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author lilin
 *         created at 2016/6/23 16:56
 */
public class CollectionItem implements Parcelable {
    public int pos;
    public String _id;
    public String user_id;
    public String target_id;
    public SceneProduct scene_product;

    public CollectionItem() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.pos);
        dest.writeString(this._id);
        dest.writeString(this.user_id);
        dest.writeString(this.target_id);
        dest.writeParcelable(this.scene_product, flags);
    }

    protected CollectionItem(Parcel in) {
        this.pos = in.readInt();
        this._id = in.readString();
        this.user_id = in.readString();
        this.target_id = in.readString();
        this.scene_product = in.readParcelable(SceneProduct.class.getClassLoader());
    }

    public static final Creator<CollectionItem> CREATOR = new Creator<CollectionItem>() {
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
