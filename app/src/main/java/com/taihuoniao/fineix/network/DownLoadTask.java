package com.taihuoniao.fineix.network;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;

import com.taihuoniao.fineix.utils.ToastUtils;

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

public class DownLoadTask extends AsyncTask<String, Integer, File> {
    private ProgressDialog progressDialog;
    private String filepath;
    private boolean isShow;
    private Handler mHandler;

    public DownLoadTask(Context context, String filepath , Handler handler) {
        this(context, filepath, handler, true);
    }


    public DownLoadTask(Context context, String filepath, Handler handler , boolean showProgressDialog) {
        this.filepath = filepath;
        this.isShow = showProgressDialog;
        this.mHandler = handler;
        if (showProgressDialog) {
            this.progressDialog = new ProgressDialog(context);
            this.progressDialog.setMessage("正在下载");
            this.progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        }
    }

    @Override
    protected void onPreExecute() {
        if (progressDialog != null && isShow) {
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
        }
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        if (progressDialog != null && isShow) {
            progressDialog.setProgress(values[0]);
        }

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
            int total = conn.getContentLength();
            InputStream is = conn.getInputStream();
            byte[] buffer = new byte[512];
            int len = 0;
            int count = 0;
            while ((len = is.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
                count+=len;
                publishProgress((int) ((count / (float) total) * 100));
            }
            fos.flush();
            fos.close();
            is.close();
            return file;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    protected void onPostExecute(File file) {
        if (file != null && file.exists()) {
            mHandler.sendEmptyMessage(NetWorkUtils.UPDATE_APK);
            if (progressDialog != null && isShow) {
                progressDialog.cancel();
            }
        } else {
            if(isShow){
                ToastUtils.showError("下载失败！");
            }
        }
    }
}
