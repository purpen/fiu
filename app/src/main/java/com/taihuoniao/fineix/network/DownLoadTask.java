package com.taihuoniao.fineix.network;

import android.content.Context;
import android.os.AsyncTask;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Created by Stephen on 2016/12/6 19:31
 * Email: 895745843@qq.com
 */

public class DownLoadTask extends AsyncTask<String, Long, File> {
    private Context context;
    private String filepath;

    public DownLoadTask() {
        super();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onProgressUpdate(Long... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(File file) {
        super.onPostExecute(file);


    }

    @Override
    protected File doInBackground(String... params) {
        URL url = null;
        try {
            url = new URL(params[0]);
            File file = new File(filepath);
            FileOutputStream fos = new FileOutputStream(file);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            int max = conn.getContentLength();
            InputStream is = conn.getInputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            int process = 0;
            while ((len = is.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
                process+=len;
//                pd.setProgress(process);
            }
            fos.flush();
            fos.close();
            is.close();
            return file;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
