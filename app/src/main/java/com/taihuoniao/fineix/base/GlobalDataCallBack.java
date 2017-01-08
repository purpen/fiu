package com.taihuoniao.fineix.base;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

/**
 * Created by Stephen on 2017/1/4 16:23
 * Email: 895745843@qq.com
 */

public abstract class GlobalDataCallBack {
    public void onStart(){}

    public abstract void onSuccess(ResponseInfo<String> responseInfo, String json);

    public abstract void onFailure(HttpException e, String s);
}
