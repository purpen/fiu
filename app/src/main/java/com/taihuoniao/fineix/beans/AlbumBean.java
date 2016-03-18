package com.taihuoniao.fineix.beans;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by taihuoniao on 2016/3/14.
 */
public class AlbumBean implements Serializable {
    private String albumUri;//相册路径
    private String title;//相册名称
    private ArrayList<PhotoItem> photos;//相册中的相片列表

    public AlbumBean(String title, String uri, ArrayList<PhotoItem> photos) {
        this.title = title;
        this.albumUri = uri;
        this.photos = photos;
    }

    public String getAlbumUri() {
        return albumUri;
    }

    public void setAlbumUri(String albumUri) {
        this.albumUri = albumUri;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<PhotoItem> getPhotos() {
        return photos;
    }

    public void setPhotos(ArrayList<PhotoItem> photos) {
        this.photos = photos;
    }

    @Override
    public int hashCode() {
        if (albumUri == null) {
            return super.hashCode();
        } else {
            return albumUri.hashCode();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o != null && (o instanceof AlbumBean)) {
            return (albumUri == null ? ((AlbumBean) o).getAlbumUri() == null : albumUri.equals(((AlbumBean) o).getAlbumUri()));
        }
        return false;
    }
}
