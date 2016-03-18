package com.taihuoniao.fineix.beans;

/**
 * Created by taihuoniao on 2016/3/14.
 */
public class PhotoItem implements Comparable<PhotoItem> {
    private String imageUri;
    private long date;
    private boolean isChecked;

    public PhotoItem(String imageUri, long date) {
        this.imageUri = imageUri;
        this.date = date;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setIsChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    @Override
    public int compareTo(PhotoItem another) {
        if (another == null) {
            return 1;
        }
        return (int) ((another.getDate() - date) / 1000);
    }
}
