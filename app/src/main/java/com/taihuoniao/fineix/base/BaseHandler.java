package com.taihuoniao.fineix.base;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

/**
 * Created by Stephen on 2017/1/9 15:00
 * Email: 895745843@qq.com
 */

public class BaseHandler extends Handler {
    public static final int CALLBACK_SUCCESS = 200;
    public static final int CALLBACK_FAILURE = 201;
    private GlobalDataCallBack callBack;

    public BaseHandler (GlobalDataCallBack callBack) {
        super(Looper.getMainLooper());
        this.callBack = callBack;
    }

    public BaseHandler(Context context, GlobalDataCallBack callBack) {
        super(Looper.getMainLooper());
        this.callBack = callBack;
    }

    public void handleMessage(Message msg) {
        switch (msg.what) {
            case CALLBACK_SUCCESS:
                callBack.onSuccess((String) msg.obj);
                break;
            case CALLBACK_FAILURE:
                callBack.onFailure((String) msg.obj);
                break;
        }
    }
}
