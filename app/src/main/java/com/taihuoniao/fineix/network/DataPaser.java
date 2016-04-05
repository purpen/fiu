package com.taihuoniao.fineix.network;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.taihuoniao.fineix.beans.JDDetailsBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by taihuoniao on 2016/3/14.
 * 数据解析类
 */
public class DataPaser {
    //产品
    //京东商品详情
    public static void getJDProductData(final Handler handler, String ids) {
        ClientDiscoverAPI.getJDProductsData(ids, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Message msg = handler.obtainMessage();
                msg.what = DataConstants.JINGDONG;
                JDDetailsBean jdDetailsBean = new JDDetailsBean();
                try {
                    JSONObject job = new JSONObject(responseInfo.result);
                    jdDetailsBean.setSuccess(job.optBoolean("success"));
                    jdDetailsBean.setMessage(job.optString("message"));
                    JSONObject data = job.getJSONObject("data");
                    JSONArray listproductbase_result = data.getJSONArray("listproductbase_result");
                    for (int i = 0; i < listproductbase_result.length(); i++) {
                        JSONObject ob = listproductbase_result.getJSONObject(i);
                        jdDetailsBean.setUrl(ob.optString("url"));
                    }
                    msg.obj = jdDetailsBean;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                handler.sendMessage(msg);
            }

            @Override
            public void onFailure(HttpException error, String msg) {
//                handler.sendEmptyMessage(DataConstants.NET_FAIL);
                Log.e("<<<failure>>>", "error = " + error.toString() + ",msg = " + msg);
            }
        });
    }
}
