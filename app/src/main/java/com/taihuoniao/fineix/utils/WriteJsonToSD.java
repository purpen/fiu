package com.taihuoniao.fineix.utils;

import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by taihuoniao on 2016/1/14.
 */
public class WriteJsonToSD {

    public static void writeToSD(String name, String values) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            try {
                File file = new File(Environment.getExternalStorageDirectory(), name);
                FileOutputStream outStream;
                outStream = new FileOutputStream(file);
                outStream.write(values.getBytes());
                outStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

//    public static void writeCacheToSD(String name, String values) {
//        File file = new File(MainApplication.getContext().getCacheDir(), name);
//        FileWriter out = null;
//        try {
//            out = new FileWriter(file);
//            out.write(values);
//            out.close();
//        } catch (IOException e) {
//            if (file.exists())
//                file.delete();
//        } finally {
//            if (out != null) {
//                try {
//                    out.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }
//
//    public static String readFromCache(String name) throws IOException {
//        String result = null;
//        File file = new File(MainApplication.getContext().getCacheDir(), name);
//        FileReader
//        in = new FileReader(file);
//        BufferedReader reader = new BufferedReader(in);
//        StringBuilder buffer = new StringBuilder();
//        String line;
//        while ((line = reader.readLine()) != null) {
//            buffer.append(line);
//        }
//        in.close();
//        result = buffer.toString();
//        in.close();
//        return result;
//    }


}
