package com.taihuoniao.fineix.utils;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.main.MainApplication;

import java.io.File;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util {

	public static String ToSBC(String input) {
		char c[] = input.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == ' ') {
				c[i] = '\u3000';
			} else if (c[i] < '\177') {
				c[i] = (char) (c[i] + 65248);
			}
		}
		return new String(c);
	}

	public static String getImei(Context context) {
		TelephonyManager phoneMgr = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		return phoneMgr.getDeviceId();
	}

	public static String getChannelID(Context context) {

		Bundle metaData = null;
		String metaValue = null;

		if (context == null) {
			return null;
		}
		try {

			ApplicationInfo ai = context.getPackageManager()
					.getApplicationInfo(context.getPackageName(),
							PackageManager.GET_META_DATA);

			if (null != ai) {
				metaData = ai.metaData;
			}
			if (null != metaData) {
				metaValue = metaData.getString("UMENG_CHANNEL");
			}

		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}

		return metaValue;// xxx

	}
	public static void makeToast(String content) {
		Toast result = new Toast(MainApplication.getContext());
		result.setGravity(Gravity.CENTER,0,0);
		result.setDuration(Toast.LENGTH_SHORT);
		View v = inflateView(MainApplication.getContext(),R.layout.transient_notification, null);
		TextView tv = (TextView)v.findViewById(R.id.message);
		tv.setText(content);
		result.setView(v);
		result.show();
	}

	public static void makeToast(Context context,String content) {
		Toast result = new Toast(context);
		result.setGravity(Gravity.CENTER,0,0);
		result.setDuration(Toast.LENGTH_SHORT);
		View v = inflateView(context, R.layout.transient_notification, null);
		TextView tv = (TextView)v.findViewById(R.id.message);
		tv.setText(content);
		result.setView(v);
		result.show();
	}
	public static void makeToast(Context context,int resId) {
		makeToast(context, context.getString(resId));
	}

	public static float getScreenHeightDPI() {
		WindowManager wm = (WindowManager) MainApplication.getContext().getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics displayMetrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(displayMetrics);
		return displayMetrics.ydpi;
	}

	public static float getScreenWidthDPI() {
		WindowManager wm = (WindowManager) MainApplication.getContext().getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics displayMetrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(displayMetrics);
		return displayMetrics.xdpi;
	}

	public static int getScreenWidth() {
			WindowManager wm = (WindowManager) MainApplication.getContext().getSystemService(Context.WINDOW_SERVICE);
			DisplayMetrics displayMetrics = new DisplayMetrics();
			wm.getDefaultDisplay().getMetrics(displayMetrics);
		return displayMetrics.widthPixels;
	}


	public static int getScreenHeight() {
		WindowManager wm = (WindowManager)MainApplication.getContext().getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics displayMetrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(displayMetrics);
		return displayMetrics.heightPixels;
	}
	public static View inflateView(Context context,int resourceId,ViewGroup root){
		LayoutInflater inflater = (LayoutInflater)context.getSystemService
			      (Context.LAYOUT_INFLATER_SERVICE);
		return inflater.inflate(resourceId,root);
	}

	public static int getScaleHeight(Activity activity,int w,int h){

		return getScreenWidth()*h/w;
	}

	public static void traverseFolder(String path) {
		File file = new File(path);
		if (file.exists()) {
			File[] files = file.listFiles();
			if (files.length == 0) {
				LogUtil.e("traverseFolder","文件夹是空的");
			} else {
				for (File file2 : files) {
					if (file2.isDirectory()) {
						LogUtil.e("文件夹:",file2.getAbsolutePath());
						traverseFolder(file2.getAbsolutePath());
					} else {
						LogUtil.e("文件：",file2.getAbsolutePath());
					}
				}
			}
		} else {
			LogUtil.e("文件：","文件不存在");
		}
	}

	public static void deleteFile(String path) {
		File file = new File(path);
		if (file.exists()) {
			File[] files = file.listFiles();
			if (files.length == 0) {
				LogUtil.e("traverseFolder","文件夹是空的");
			} else {
				for (File file2 : files) {
					if (file2.isDirectory()) {
						LogUtil.e("文件夹:",file2.getAbsolutePath());
						traverseFolder(file2.getAbsolutePath());
					} else {
						LogUtil.e("删除文件：",file2.getAbsolutePath());
						file2.delete();
					}
				}
			}
			file.delete();
			LogUtil.e("删除完毕","fsafdsaf");
		} else {
			LogUtil.e("文件：","文件不存在");
		}
	}


	public static String replaceChinese2UTF8(String url){
		Pattern pattern=Pattern.compile("[\u3400-\u9FFF]+");
		Matcher matcher = pattern.matcher(url);
		while (matcher.find()){
			try {
				String str=matcher.group();
				url=url.replace(str, URLEncoder.encode(str,Constants.CHARSET));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return url;
	}

	public static File saveBitmapToFile(Bitmap bitmap) {
		File file = new File(Environment.getExternalStorageDirectory(),
				"tmp_avatar_" + String.valueOf(System.currentTimeMillis()) + ".png");
		if (file.exists()) {
			file.delete();
		}
		try {
			FileOutputStream fops = new FileOutputStream(file);
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, fops);
			fops.flush();
			fops.close();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			bitmap.recycle();
		}
		return file;
	}

	public static String formatDouble(String price) throws NumberFormatException{
		DecimalFormat decimalFormat = new DecimalFormat("#.0");
		return decimalFormat.format(Double.parseDouble(price));
	}

	public static String formatFloat(float price) throws NumberFormatException{
		DecimalFormat decimalFormat = new DecimalFormat("#.00");
		return decimalFormat.format(price);
	}
}
