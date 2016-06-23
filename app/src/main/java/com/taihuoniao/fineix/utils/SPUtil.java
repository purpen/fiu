package com.taihuoniao.fineix.utils;
import android.content.Context;
import android.content.SharedPreferences;

import com.taihuoniao.fineix.main.MainApplication;

public class SPUtil {
	public static void write(String key, String value) {
		SharedPreferences sp = MainApplication.getContext().getSharedPreferences(key, Context.MODE_PRIVATE);
		sp.edit().putString(key,value).commit();
	}

	public static void write(String key, boolean value) {
		SharedPreferences sp = MainApplication.getContext().getSharedPreferences(key, Context.MODE_PRIVATE);
		sp.edit().putBoolean(key, value).commit();
	}

	public static String read(String key) {
		SharedPreferences sp = MainApplication.getContext().getSharedPreferences(key, Context.MODE_PRIVATE);
		return sp.getString(key,"");
	}

	public static boolean readBool(String key) {
		SharedPreferences sp = MainApplication.getContext().getSharedPreferences(key, Context.MODE_PRIVATE);
		return sp.getBoolean(key, false);
	}

	public static void remove(String key) {
		SharedPreferences sp = MainApplication.getContext().getSharedPreferences(key, Context.MODE_PRIVATE);
		sp.edit().remove(key).commit();
	}

	public static void clear(String key) {
		SharedPreferences sp = MainApplication.getContext().getSharedPreferences(key, Context.MODE_PRIVATE);
		sp.edit().clear().commit();
	}
}
