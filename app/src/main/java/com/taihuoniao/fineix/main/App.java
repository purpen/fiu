package com.taihuoniao.fineix.main;

import android.content.Context;

/**
 * Created by Stephen on 2016/11/29 15:59
 * Email: 895745843@qq.com
 */

public class App {

    public App(Context mContext) {
        Context mContext1 = mContext;
    }

    public static Context getContext(){
        return MainApplication.getContext();
    }

    public static String getString(int id){
        return MainApplication.getContext().getResources().getString(id);
    }

    public static String[] getStringArray(int id){
        return MainApplication.getContext().getResources().getStringArray(id);
    }
}
