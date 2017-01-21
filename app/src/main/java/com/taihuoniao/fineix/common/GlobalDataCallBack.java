package com.taihuoniao.fineix.common;

/**
 * Created by Stephen on 2017/1/4 16:23
 * Email: 895745843@qq.com
 */

public abstract class GlobalDataCallBack {
    public void onStart(){}

    public abstract void onSuccess(String json);

    public abstract void onFailure(String error);
}
