package com.taihuoniao.fineix.network;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.taihuoniao.fineix.beans.JDDetailsBean;
import com.taihuoniao.fineix.beans.TBDetailsBean;
import com.taihuoniao.fineix.utils.WriteJsonToSD;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by taihuoniao on 2016/3/14.
 * 数据解析类
 */
public class DataPaser {
    //产品
    //列表
    public static void getProductList(final Handler handler) {
        ClientDiscoverAPI.getProductList(new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Message msg = handler.obtainMessage();
                msg.what = DataConstants.ADD_PRODUCT_LIST;
                Log.e("<<<", responseInfo.result);
                WriteJsonToSD.writeToSD("json", responseInfo.result);
                handler.sendMessage(msg);
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                Log.e("<<<failure>>>", "error = " + error.toString() + ",msg = " + msg);
                handler.sendEmptyMessage(DataConstants.NET_FAIL);
            }
        });
    }

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
                    if (jdDetailsBean.isSuccess()) {
                        JSONObject data = job.getJSONObject("data");
                        JSONArray listproductbase_result = data.getJSONArray("listproductbase_result");
//                        for (int i = 0; i < listproductbase_result.length(); i++) {
                        JSONObject ob = listproductbase_result.getJSONObject(0);
                        jdDetailsBean.setUrl(ob.optString("url"));
                        jdDetailsBean.setImagePath(ob.optString("imagePath"));
                        jdDetailsBean.setName(ob.optString("name"));
                        jdDetailsBean.setSale_price(ob.optString("sale_price"));
//                        }
                    }
                    msg.obj = jdDetailsBean;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                handler.sendMessage(msg);
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                Log.e("<<<failure>>>", "error = " + error.toString() + ",msg = " + msg);
                handler.sendEmptyMessage(DataConstants.NET_FAIL);
            }
        });
    }

    //产品
    //淘宝商品详情
    public static void getTBProductData(final Handler handler, String ids) {
        ClientDiscoverAPI.getTBProductsData(ids, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Message msg = handler.obtainMessage();
                msg.what = DataConstants.TAOBAO;
                TBDetailsBean tbDetailsBean = new TBDetailsBean();
                try {
                    JSONObject job = new JSONObject(responseInfo.result);
                    tbDetailsBean.setSuccess(job.optBoolean("success"));
                    tbDetailsBean.setMessage(job.optString("message"));
                    if (tbDetailsBean.isSuccess()) {
                        JSONObject data = job.getJSONObject("data");
                        JSONObject results = data.getJSONObject("results");
                        JSONArray n_tbk_item = results.getJSONArray("n_tbk_item");
                        JSONObject ob = n_tbk_item.getJSONObject(0);
                        tbDetailsBean.setZk_final_price(ob.optString("zk_final_price"));
                        tbDetailsBean.setTitle(ob.optString("title"));
                        tbDetailsBean.setReserve_price(ob.optString("reserve_price"));
                        tbDetailsBean.setPict_url(ob.optString("pict_url"));
                        tbDetailsBean.setItem_url(ob.optString("item_url"));
                    }
                    msg.obj = tbDetailsBean;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                handler.sendMessage(msg);
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                Log.e("<<<failure>>>", "error = " + error.toString() + ",msg = " + msg);
                handler.sendEmptyMessage(DataConstants.NET_FAIL);
            }
        });
    }
}
