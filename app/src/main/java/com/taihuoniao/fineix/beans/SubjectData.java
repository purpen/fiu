package com.taihuoniao.fineix.beans;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * @author lilin
 *         created at 2016/6/15 23:33
 */
public class SubjectData implements Parcelable {
    public int _id;
    public String title;
    public String tags_s;
    public int kind;
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
    public int comment_count;
    public String cover_url;
    public int is_love;
    public String content_view_url;
    public String share_view_url;
    public String share_desc;
    public List<String> tags;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this._id);
        dest.writeString(this.title);
        dest.writeString(this.tags_s);
        dest.writeInt(this.kind);
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
        dest.writeInt(this.comment_count);
        dest.writeString(this.cover_url);
        dest.writeInt(this.is_love);
        dest.writeString(this.content_view_url);
        dest.writeString(this.share_view_url);
        dest.writeString(this.share_desc);
        dest.writeStringList(this.tags);
    }

    public SubjectData() {
    }

    protected SubjectData(Parcel in) {
        this._id = in.readInt();
        this.title = in.readString();
        this.tags_s = in.readString();
        this.kind = in.readInt();
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
        this.comment_count = in.readInt();
        this.cover_url = in.readString();
        this.is_love = in.readInt();
        this.content_view_url = in.readString();
        this.share_view_url = in.readString();
        this.share_desc = in.readString();
        this.tags = in.createStringArrayList();
    }

    public static final Parcelable.Creator<SubjectData> CREATOR = new Parcelable.Creator<SubjectData>() {
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
