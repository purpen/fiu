package com.taihuoniao.fineix.network;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;

import com.taihuoniao.fineix.utils.LogUtil;
import com.taihuoniao.fineix.utils.ToastUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * Created by Stephen on 2016/12/6 19:31
 * Email: 895745843@qq.com
 */

public class DownLoadTask extends AsyncTask<String, Integer, File> {
    private Context mContext;
    private ProgressDialog progressDialog;
    private File apkFile;
    private boolean isShow;
    private Handler mHandler;

    public DownLoadTask(Context context, File apkFile, Handler handler) {
        this(context, apkFile, handler, true);
    }


    public DownLoadTask(Context context, File apkFile, Handler handler , boolean showProgressDialog) {
        LogUtil.e("DownLoadTask filepath :" + apkFile);
        this.mContext = context;
        this.apkFile = apkFile;
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
        HttpURLConnection conn = null;
        try {
            url = new URL(params[0]);
            File file = new File(apkFile.getAbsolutePath());
            FileOutputStream fos = new FileOutputStream(file);
            trustAllHosts();
            HttpsURLConnection https = (HttpsURLConnection)url.openConnection();
            if (url.getProtocol().toLowerCase().equals("https")) {
                https.setHostnameVerifier(DO_NOT_VERIFY);
                conn = https;
            } else {
                conn = (HttpURLConnection)url.openConnection();
            }
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
            if (progressDialog != null && isShow) {
                progressDialog.cancel();
            }
            NetWorkUtils.installApk(file, mContext);
        } else {
            if(isShow){
                ToastUtils.showError("下载失败！");
                progressDialog.cancel();
            }
        }
    }

    private final static HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {

        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    };

    /**
     * Trust every server - dont check for any certificate
     */
    private static void trustAllHosts() {
        final String TAG = "trustAllHosts";
        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {

            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return new java.security.cert.X509Certificate[] {};
            }

            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }
        } };

        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
