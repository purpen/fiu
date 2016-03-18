package com.taihuoniao.fineix.utils;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import com.taihuoniao.fineix.beans.PhotoItem;
import com.taihuoniao.fineix.scene.CropPictureActivity;
import com.taihuoniao.fineix.scene.EditPictureActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by taihuoniao on 2016/3/15.
 */
public class ImageUtils {


    //保存图片文件
    public static String saveToFile(String fileFolderStr, boolean isDir, Bitmap croppedImage) throws IOException {
        File jpgFile;
        if (isDir) {
            File fileFolder = new File(fileFolderStr);
            Date date = new Date();
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss"); // 格式化时间
            String filename = format.format(date) + ".jpg";
            if (!fileFolder.exists()) { // 如果目录不存在，则创建一个名为"finger"的目录
                mkdir(fileFolder);
            }
            jpgFile = new File(fileFolder, filename);
        } else {
            jpgFile = new File(fileFolderStr);
            if (!jpgFile.getParentFile().exists()) { // 如果目录不存在，则创建一个名为"finger"的目录
                mkdir(jpgFile.getParentFile());
            }
        }
        FileOutputStream outputStream = new FileOutputStream(jpgFile); // 文件输出流
        croppedImage.compress(Bitmap.CompressFormat.JPEG, 70, outputStream);
        outputStream.close();
        return jpgFile.getPath();
    }

    //创建文件夹
    private static boolean mkdir(File file) {
        while (!file.getParentFile().exists()) {
            mkdir(file.getParentFile());
        }
        return file.mkdir();
    }

    //判断图片是否需要裁剪
    public static void processPhotoItem(Activity activity, PhotoItem photo) {
        Uri uri = photo.getImageUri().startsWith("file:") ? Uri.parse(photo
                .getImageUri()) : Uri.parse("file://" + photo.getImageUri());
        if (isFourToThree(photo.getImageUri())) {
            Intent intent = new Intent(activity, EditPictureActivity.class);
            intent.setData(uri);
            activity.startActivity(intent);
        } else {
            Intent intent = new Intent(activity, CropPictureActivity.class);
            intent.setData(uri);
            activity.startActivity(intent);
        }
    }

    //判断图片是不是4：3
    public static boolean isFourToThree(String imagePath) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imagePath, options);
        return options.outHeight * 3 / 4 == options.outWidth;
    }

    //从手机中读取照片

    /**
     * @param pathName
     * @param width
     * @param height
     * @param useBigger 图片会进行压缩。这个参数是选择使用较大的图还是较小的图，较大的图会比指定的宽高大，较小的图会比指定的宽高小
     * @return
     */
    public static Bitmap decodeBitmapWithSize(String pathName, int width, int height,
                                              boolean useBigger) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;//加载不消耗内存
        options.inInputShareable = true;//用于图片清空之后恢复
        options.inPurgeable = true;//当内存不足时可以清楚
        BitmapFactory.decodeFile(pathName, options);
        if (useBigger) {
            options.inSampleSize = (int) Math.min(((float) options.outWidth / width),
                    ((float) options.outHeight / height));
        } else {
            float scale = Math.max(((float) options.outWidth / width),
                    ((float) options.outHeight / height));
            if (scale * 10 % 10 != 0) {
                options.inSampleSize = (int) scale + 1;
            } else {
                options.inSampleSize = (int) scale;
            }

        }
        options.inJustDecodeBounds = false;
        Bitmap sourceBm = BitmapFactory.decodeFile(pathName, options);
        return sourceBm;
    }


}
