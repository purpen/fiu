package com.taihuoniao.fineix.utils;
import android.content.Context;
import android.content.SharedPreferences;

public class SPUtil {
	public static void write(Context context,String key,String value){
		SharedPreferences sp = context.getSharedPreferences(key,Context.MODE_PRIVATE);
		sp.edit().putString(key,value).commit();
	}
	public static String read(Context context,String key){
		SharedPreferences sp = context.getSharedPreferences(key,Context.MODE_PRIVATE);
		return sp.getString(key,"");
	}
	public static void remove(Context context,String key){
		SharedPreferences sp = context.getSharedPreferences(key,Context.MODE_PRIVATE);
		sp.edit().remove(key).commit();
	}
	public static void clear(Context context,String key){
		SharedPreferences sp = context.getSharedPreferences(key,Context.MODE_PRIVATE);
		sp.edit().clear().commit();
	}
}
