package com.taihuoniao.fineix.utils;

import com.taihuoniao.fineix.beans.PhotoItem;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by taihuoniao on 2016/3/14.
 */
public class FileUtils {
    //获取path路径下的图片
    public static ArrayList<PhotoItem> findPicsInDir(String path) {
        ArrayList<PhotoItem> photos = new ArrayList<PhotoItem>();
        File dir = new File(path);
        if (dir.exists() && dir.isDirectory()) {
            for (File file : dir.listFiles(new FileFilter() {

                @Override
                public boolean accept(File pathname) {
                    String filePath = pathname.getAbsolutePath();
                    return (filePath.endsWith(".png") || filePath.endsWith(".jpg") || filePath
                            .endsWith(".jepg"));
                }
            })) {
                photos.add(new PhotoItem(file.getAbsolutePath(), file.lastModified()));
            }
        }
        Collections.sort(photos);
        return photos;
    }


}
