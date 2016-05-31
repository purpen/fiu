package com.taihuoniao.fineix.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.taihuoniao.fineix.beans.PhotoItem;
import com.taihuoniao.fineix.main.MainApplication;
import com.taihuoniao.fineix.scene.CropPictureActivity;
import com.taihuoniao.fineix.scene.EditPictureActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by taihuoniao on 2016/3/15.
 */
public class ImageUtils {
    public static double[] location = null;//图片经纬度

    //保存图片文件
    public static String saveToFile(String fileFolderStr, boolean isDir, Bitmap croppedImage) throws IOException {
        File jpgFile;
        if (isDir) {
            File fileFolder = new File(fileFolderStr);
            Date date = new Date();
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA); // 格式化时间
            String filename = format.format(date) + ".jpg";
            if (!fileFolder.exists()) { // 如果目录不存在，则创建一个名为"finger"的目录
                mkdir(fileFolder);
            }
            jpgFile = new File(fileFolder, filename);
        } else {
            jpgFile = new File(fileFolderStr);
            Log.e("<<<", "jpgfile.path = " + jpgFile.getAbsolutePath());
            if (!jpgFile.getParentFile().exists()) { // 如果目录不存在，则创建一个名为"finger"的目录
                mkdir(jpgFile.getParentFile());
            }
        }
        FileOutputStream outputStream = new FileOutputStream(jpgFile); // 文件输出流
        croppedImage.compress(Bitmap.CompressFormat.JPEG, 50, outputStream);
        outputStream.close();
        Log.e("<<<", "path = " + jpgFile.getPath());
        return jpgFile.getPath();
    }

    //创建文件夹
    private static boolean mkdir(File file) {
        while (!file.getParentFile().exists()) {
            mkdir(file.getParentFile());
        }
        return file.mkdir();
    }


    //获取图片的位置信息
    public static double[] picLocation(String fileName) {
        Log.e("<<<", "getLocation.fileName = " + fileName);
        ExifInterface exifInterface = null;
        try {
            exifInterface = new ExifInterface(fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (exifInterface == null) {
            return null;
        }
        String latitude = exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE);
        String longitude = exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE);
        if (latitude != null && longitude != null) {
            String[] latitudes = latitude.split(",");
            double du = Integer.parseInt(latitudes[0].substring(0, latitudes[0].indexOf("/")));
            double fen = Integer.parseInt(latitudes[1].substring(0, latitudes[1].indexOf("/")));
            double miao = Integer.parseInt(latitudes[2].substring(0, latitudes[2].indexOf("/")));
            double weidu = du + fen / 60 + miao / 1000000 / 60 / 60;
            String[] longitudes = longitude.split(",");
            du = Integer.parseInt(longitudes[0].substring(0, longitudes[0].indexOf("/")));
            fen = Integer.parseInt(longitudes[1].substring(0, longitudes[1].indexOf("/")));
            miao = Integer.parseInt(longitudes[2].substring(0, longitudes[2].indexOf("/")));
            double jingdu = du + fen / 60 + miao / 1000000 / 60 / 60;
            return new double[]{jingdu, weidu};
        }
        return null;
    }

    //判断图片是否需要裁剪
    public static void processPhotoItem(Activity activity, PhotoItem photo) {
        location = picLocation(photo.getImageUri());
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

    //判断图片是不是16:9
    public static boolean isFourToThree(String imagePath) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imagePath, options);
        return (options.outHeight * 9) == (options.outWidth * 16);
    }

    //从文件中读取照片

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
        options.inJustDecodeBounds = true;//只加载宽高
        options.inInputShareable = true;//用于图片清空之后恢复
        options.inPurgeable = true;//当内存不足时可以清楚
        BitmapFactory.decodeFile(pathName, options);
        if (useBigger) {
            options.inSampleSize = (int) Math.min(((float) options.outWidth / width),
                    ((float) options.outHeight / height));
        } else {
            float scale = Math.min(((float) options.outWidth / width),
                    ((float) options.outHeight / height));
            if ((int) (scale * 10) % 10 != 0) {
                options.inSampleSize = (int) scale + 1;
            } else {
                options.inSampleSize = (int) scale;
            }


        }
        options.inJustDecodeBounds = false;
        Bitmap sourceBm = BitmapFactory.decodeFile(pathName, options);
        return sourceBm;
    }

    /**
     * 获取圆角位图的方法
     *
     * @param bitmap 需要转化成圆角的位图
     * @param pixels 圆角的度数，数值越大，圆角越大
     * @return 处理后的圆角位图
     */
    public static Bitmap toRoundCorner(Bitmap bitmap, int pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    //异步加载本地图片
    public static void asyncLoadImage(Context context, Uri imageUri, LoadImageCallback callback) {
        new LoadImageUriTask(context, imageUri, callback).execute();
    }

    //异步加载图片
    public interface LoadImageCallback {
        void callback(Bitmap result);
    }

    private static class LoadImageUriTask extends AsyncTask<Void, Void, Bitmap> {
        private final Uri imageUri;
        private final Context context;
        private LoadImageCallback callback;

        public LoadImageUriTask(Context context, Uri imageUri, LoadImageCallback callback) {
            this.imageUri = imageUri;
            this.context = context;
            this.callback = callback;
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            try {
                InputStream inputStream;
                if (imageUri.getScheme().startsWith("http")
                        || imageUri.getScheme().startsWith("https")) {
                    inputStream = new URL(imageUri.toString()).openStream();
                } else {
                    inputStream = context.getContentResolver().openInputStream(imageUri);
                }
                return BitmapFactory.decodeStream(inputStream);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            callback.callback(result);
        }
    }


    public static void loadBgImg(String url, View view) {
        new MyAsyncTask(url, view).execute();
    }

    private static class MyAsyncTask extends AsyncTask<String, Void, Bitmap> {
        private View view;
        private String url;

        public MyAsyncTask(String url, View view) {
            super();
            this.view = view;
            this.url = url;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            return ImageLoader.getInstance().loadImageSync(url);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            view.setBackgroundDrawable(new BitmapDrawable(bitmap));
        }
    }

    public static Bitmap decodeUriAsBitmap(Uri uri) {

        Bitmap bitmap = null;
        try {

            bitmap = BitmapFactory.decodeStream(MainApplication.getContext().getContentResolver().openInputStream(uri));

        } catch (FileNotFoundException e) {

            e.printStackTrace();

            return null;

        }

        return bitmap;

    }

    public static Bitmap convertViewToBitmap(View view) {
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
//        view.layout(view.getLeft(),view.getTop(), view.getLeft()+view.getMeasuredWidth(),view.getTop()+view.getMeasuredHeight());
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();
        return bitmap;
    }


}
