package com.taihuoniao.fineix.network;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.taihuoniao.fineix.base.NetBean;
import com.taihuoniao.fineix.beans.AllLabel;
import com.taihuoniao.fineix.beans.AllLabelBean;
import com.taihuoniao.fineix.beans.BindPhone;
import com.taihuoniao.fineix.beans.BrandListBean;
import com.taihuoniao.fineix.beans.CartBean;
import com.taihuoniao.fineix.beans.CategoryBean;
import com.taihuoniao.fineix.beans.CategoryListBean;
import com.taihuoniao.fineix.beans.CommentsBean;
import com.taihuoniao.fineix.beans.CommonBean;
import com.taihuoniao.fineix.beans.FindPasswordInfo;
import com.taihuoniao.fineix.beans.JDDetailsBean;
import com.taihuoniao.fineix.beans.LoginInfo;
import com.taihuoniao.fineix.beans.ProductBean;
import com.taihuoniao.fineix.beans.ProductListBean;
import com.taihuoniao.fineix.beans.QingJingListBean;
import com.taihuoniao.fineix.beans.QingjingDetailBean;
import com.taihuoniao.fineix.beans.QingjingSubsBean;
import com.taihuoniao.fineix.beans.SceneDetails;
import com.taihuoniao.fineix.beans.SceneList;
import com.taihuoniao.fineix.beans.SceneListBean;
import com.taihuoniao.fineix.beans.SceneLoveBean;
import com.taihuoniao.fineix.beans.SkipBind;
import com.taihuoniao.fineix.beans.TBDetailsBean;
import com.taihuoniao.fineix.beans.ThirdLogin;
import com.taihuoniao.fineix.beans.UsedLabel;
import com.taihuoniao.fineix.beans.UsedLabelBean;
import com.taihuoniao.fineix.main.MainApplication;
import com.taihuoniao.fineix.utils.LogUtil;
import com.taihuoniao.fineix.utils.SPUtil;
import com.taihuoniao.fineix.utils.WriteJsonToSD;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by taihuoniao on 2016/3/14.
 * 数据解析类
 */
public class DataPaser {
    //产品
    //列表
    public static void getProductList(String category, String page, final Handler handler) {
        ClientDiscoverAPI.getProductList(category, page, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Message msg = handler.obtainMessage();
                msg.what = DataConstants.ADD_PRODUCT_LIST;
                ProductBean productBean = new ProductBean();
                try {
                    JSONObject job = new JSONObject(responseInfo.result);
                    productBean.setSuccess(job.optBoolean("success"));
                    productBean.setMessage(job.optString("message"));
//                    productBean.setStatus(job.optString("status"));
                    if (productBean.isSuccess()) {
                        JSONObject data = job.getJSONObject("data");
                        JSONArray rows = data.getJSONArray("rows");
                        List<ProductListBean> list = new ArrayList<ProductListBean>();
                        for (int i = 0; i < rows.length(); i++) {
                            JSONObject ob = rows.getJSONObject(i);
                            ProductListBean productListBean = new ProductListBean();
                            productListBean.set_id(ob.optString("_id"));
                            productListBean.setTitle(ob.optString("title"));
                            productListBean.setSale_price(ob.optString("sale_price"));
                            productListBean.setMarket_price(ob.optString("market_price"));
                            productListBean.setLove_count(ob.optString("love_count"));
                            productListBean.setCover_url(ob.optString("cover_url"));
                            list.add(productListBean);
                        }
                        productBean.setList(list);
                    }
                    msg.obj = productBean;
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
//                    jdDetailsBean.setStatus(job.optString("status"));
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
//                    tbDetailsBean.setStatus(job.optString("status"));
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

    //情景
    //点赞，订阅，收藏，关注通用列表
    public static void commonList(String page, String size, String id, String user_id, String type, String event, final Handler handler) {
        ClientDiscoverAPI.commonList(page, size, id, user_id, type, event, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Log.e("<<<", responseInfo.result);
                WriteJsonToSD.writeToSD("json", responseInfo.result);
                Message msg = handler.obtainMessage();
                msg.what = DataConstants.COMMON_LIST;
                try {
                    Gson gson = new Gson();
                    Type type = new TypeToken<CommonBean>() {
                    }.getType();
                    msg.obj = gson.fromJson(responseInfo.result, type);
                } catch (JsonSyntaxException e) {
                    Toast.makeText(MainApplication.getContext(), "数据异常", Toast.LENGTH_SHORT).show();
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

    //情景
    //情景订阅
    public static void subsQingjing(String id, final Handler handler) {
        ClientDiscoverAPI.subsQingjing(id, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Log.e("<<<", responseInfo.result);
                Message msg = handler.obtainMessage();
                msg.what = DataConstants.SUBS_QINGJING;
                msg.obj = new QingjingSubsBean();
                try {
                    Gson gson = new Gson();
                    Type type = new TypeToken<QingjingSubsBean>() {
                    }.getType();
                    msg.obj = gson.fromJson(responseInfo.result, type);
                } catch (JsonSyntaxException e) {
                    Toast.makeText(MainApplication.getContext(), "解析异常" + e.toString(), Toast.LENGTH_SHORT).show();
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

    //情景
    //取消订阅情景
    public static void cancelSubsQingjing(String id, final Handler handler) {
        ClientDiscoverAPI.cancelSubsQingjing(id, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Log.e("<<<", responseInfo.result);
                Message msg = handler.obtainMessage();
                msg.what = DataConstants.CANCEL_SUBS_QINGJING;
                msg.obj = new QingjingSubsBean();
                try {
                    Gson gson = new Gson();
                    Type type = new TypeToken<QingjingSubsBean>() {
                    }.getType();
                    msg.obj = gson.fromJson(responseInfo.result, type);
                } catch (JsonSyntaxException e) {
                    Toast.makeText(MainApplication.getContext(), "解析异常" + e.toString(), Toast.LENGTH_SHORT).show();
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

    //情景
    //情景新增
    public static void createQingjing(String id, String title, String des, String tags, String address, String tmp, String lat, String lng, final Handler handler) {
        ClientDiscoverAPI.createQingjing(id, title, des, tags, address, tmp, lat, lng, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Message msg = handler.obtainMessage();
                msg.what = DataConstants.CREATE_QINGJING;
                NetBean netBean = new NetBean();
                try {
                    JSONObject job = new JSONObject(responseInfo.result);
                    netBean.setSuccess(job.optBoolean("success"));
                    netBean.setMessage(job.optString("message"));
//                    netBean.setStatus(job.optString("status"));
                    msg.obj = netBean;
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

    //情景
    //情景详情
    public static void qingjingDetails(String id, final Handler handler) {
        ClientDiscoverAPI.qingjingDetails(id, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Log.e("<<<>>>", responseInfo.result);
                WriteJsonToSD.writeToSD("json", responseInfo.result);
                Message msg = handler.obtainMessage();
                msg.what = DataConstants.QINGJING_DETAILS;
                msg.obj = new QingjingDetailBean();
                try {
                    Gson gson = new Gson();
                    Type type = new TypeToken<QingjingDetailBean>() {
                    }.getType();
                    msg.obj = gson.fromJson(responseInfo.result, type);
                } catch (JsonSyntaxException e) {
                    Log.e("<<<", "数据异常：" + e.toString());
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

    //情景
    //列表数据
    public static void qingjingList(String page, String stick, String dis, String lng, String lat, final Handler handler) {
        ClientDiscoverAPI.qingjingList(page, stick, dis, lng, lat, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Message msg = handler.obtainMessage();
                msg.what = DataConstants.QINGJING_LIST;
                msg.obj = new QingJingListBean();
                try {
                    Gson gson = new Gson();
                    Type type = new TypeToken<QingJingListBean>() {
                    }.getType();
                    msg.obj = gson.fromJson(responseInfo.result, type);
                } catch (JsonSyntaxException e) {
                    Log.e("<<<", "数据异常：" + e.toString());
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

    //场景
    //新增场景
    public static void createScene(String id, String tmp, String title, String des, String scene_id, String tags, String product_id,
                                   String product_title, String product_price, String product_x, String product_y,
                                   String address, String lat, String lng, final Handler handler) {
        ClientDiscoverAPI.createScene(id, tmp, title, des, scene_id, tags, product_id, product_title, product_price, product_x, product_y, address,
                lat, lng, new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        Message msg = handler.obtainMessage();
                        msg.what = DataConstants.CREATE_SCENE;
                        NetBean netBean = new NetBean();
                        try {
                            JSONObject job = new JSONObject(responseInfo.result);
                            netBean.setSuccess(job.optBoolean("success"));
                            netBean.setMessage(job.optString("message"));
//                            netBean.setStatus(job.optString("status"));
                            msg.obj = netBean;
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

    //场景
    //场景详情
    public static void sceneDetails(String id, final Handler handler) {
        ClientDiscoverAPI.sceneDetails(id, new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        Log.e("<<<", responseInfo.result);
                        WriteJsonToSD.writeToSD("json", responseInfo.result);
                        Message msg = handler.obtainMessage();
                        msg.what = DataConstants.SCENE_DETAILS;
                        SceneDetails sceneDetails = new SceneDetails();
                        try {
                            JSONObject jsonObject = new JSONObject(responseInfo.result);
                            sceneDetails.setSuccess(jsonObject.optBoolean("success"));
                            sceneDetails.setMessage(jsonObject.optString("message"));
//                            sceneDetails.setStatus(jsonObject.optString("status"));
                            if (sceneDetails.isSuccess()) {
                                JSONObject data = jsonObject.getJSONObject("data");
                                sceneDetails.set_id(data.optString("_id"));
                                sceneDetails.setTitle(data.optString("title"));
                                sceneDetails.setDes(data.optString("des"));
                                sceneDetails.setIs_love(data.optInt("is_love"));
                                List<Integer> tagsList = new ArrayList<Integer>();
                                JSONArray tags = data.getJSONArray("tags");
                                for (int i = 0; i < tags.length(); i++) {
                                    int in = tags.optInt(i);
                                    tagsList.add(in);
                                }
                                List<String> tagsTitleList = new ArrayList<String>();
                                JSONArray tagsTitle = data.getJSONArray("tag_titles");
                                for (int i = 0; i < tagsTitle.length(); i++) {
                                    String str = tagsTitle.optString(i, null);
                                    tagsTitleList.add(str);
                                }
                                sceneDetails.setTag_titles(tagsTitleList);
                                sceneDetails.setTags(tagsList);
                                List<SceneDetails.Product> products = new ArrayList<SceneDetails.Product>();
                                JSONArray product = data.getJSONArray("product");
                                for (int i = 0; i < product.length(); i++) {
                                    JSONObject job = product.getJSONObject(i);
                                    SceneDetails.Product pro = new SceneDetails.Product();
                                    pro.setId(job.optString("id"));
                                    pro.setTitle(job.optString("title"));
                                    pro.setPrice(job.optString("price"));
                                    pro.setX(job.optDouble("x"));
                                    pro.setY(job.optDouble("y"));
                                    if (pro.getId().equals("0")) {
                                        break;
                                    }
                                    products.add(pro);
                                }
                                sceneDetails.setProduct(products);
                                sceneDetails.setAddress(data.optString("address"));
                                sceneDetails.setView_count(data.optString("view_count"));
                                sceneDetails.setLove_count(data.optInt("love_count"));
                                sceneDetails.setComment_count(data.optString("comment_count"));
                                sceneDetails.setCreated_on(data.optString("created_on"));
                                sceneDetails.setCover_url(data.optString("cover_url"));
                                sceneDetails.setScene_title(data.optString("scene_title"));
                                SceneDetails.UserInfo userInfo = new SceneDetails.UserInfo();
                                JSONObject user_info = data.getJSONObject("user_info");
                                userInfo.setUser_id(user_info.optString("user_id"));
                                userInfo.setNickname(user_info.optString("nickname"));
                                userInfo.setAvatar_url(user_info.optString("avatar_url"));
                                userInfo.setSummary(user_info.optString("summary"));
                                userInfo.setUser_rank(user_info.optString("user_rank"));
                                sceneDetails.setUser_info(userInfo);
                            }
                        } catch (
                                JSONException e
                                )

                        {
                            Log.e("<<<", "解析异常");
                        }

                        msg.obj = sceneDetails;
                        handler.sendMessage(msg);
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        Log.e("<<<failure>>>", "error = " + error.toString() + ",msg = " + msg);
                        handler.sendEmptyMessage(DataConstants.NET_FAIL);
                    }
                }

        );
    }

    //场景
    //场景点赞
    public static void loveScene(String id, final Handler handler) {
        ClientDiscoverAPI.loveScene(id, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Log.e("<<<", responseInfo.result);
                Message msg = handler.obtainMessage();
                msg.what = DataConstants.LOVE_SCENE;
                msg.obj = new SceneLoveBean();
                try {
                    Gson gson = new Gson();
                    Type type = new TypeToken<SceneLoveBean>() {
                    }.getType();
                    msg.obj = gson.fromJson(responseInfo.result, type);
                } catch (JsonSyntaxException e) {
                    Toast.makeText(MainApplication.getContext(), "解析异常" + e.toString(), Toast.LENGTH_SHORT).show();
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

    //场景
    //取消场景点赞
    public static void cancelLoveScene(String id, final Handler handler) {
        ClientDiscoverAPI.cancelLoveScene(id, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Log.e("<<<", responseInfo.result);
                Message msg = handler.obtainMessage();
                msg.what = DataConstants.CANCEL_LOVE_SCENE;
                msg.obj = new SceneLoveBean();
                try {
                    Gson gson = new Gson();
                    Type type = new TypeToken<SceneLoveBean>() {
                    }.getType();
                    msg.obj = gson.fromJson(responseInfo.result, type);
                } catch (JsonSyntaxException e) {
                    Toast.makeText(MainApplication.getContext(), "解析异常" + e.toString(), Toast.LENGTH_SHORT).show();
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

    //场景
    //列表数据
    public static void getSceneList(String page, String size, String scene_id, String stick, String dis, String lng, String lat, final Handler handler) {
        ClientDiscoverAPI.getSceneList(page, size, scene_id, stick, dis, lng, lat, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Log.e("<<<", responseInfo.result);
                WriteJsonToSD.writeToSD("json", responseInfo.result);
                Message msg = handler.obtainMessage();
                msg.what = DataConstants.SCENE_LIST;
                SceneList sceneList = new SceneList();
                try {
                    JSONObject jsonObject = new JSONObject(responseInfo.result);
                    sceneList.setSuccess(jsonObject.optBoolean("success"));
                    sceneList.setMessage(jsonObject.optString("message"));
//                    sceneList.setStatus(jsonObject.optString("status"));
                    if (sceneList.isSuccess()) {
                        JSONObject data = jsonObject.getJSONObject("data");
                        JSONArray rows = data.getJSONArray("rows");
                        List<SceneListBean> list = new ArrayList<SceneListBean>();
                        for (int i = 0; i < rows.length(); i++) {
                            JSONObject job = rows.getJSONObject(i);
                            SceneListBean sceneListBean = new SceneListBean();
                            sceneListBean.set_id(job.optString("_id"));
                            sceneListBean.setAddress(job.optString("address"));
                            sceneListBean.setScene_title(job.optString("scene_title"));
                            sceneListBean.setView_count(job.optString("view_count"));
                            sceneListBean.setCreated_at(job.optString("created_at"));
                            sceneListBean.setLove_count(job.optString("love_count"));
                            sceneListBean.setCover_url(job.optString("cover_url"));
                            sceneListBean.setTitle(job.optString("title"));
                            sceneListBean.setDes(job.optString("des"));
                            JSONObject us = job.getJSONObject("user_info");
                            SceneListBean.User user = new SceneListBean.User();
                            user.setAccount(us.optString("account"));
                            user.setUser_rank(us.optString("user_rank"));
                            user.setUser_id(us.optString("user_id"));
                            user.setSummary(us.optString("summary"));
                            user.setNickname(us.optString("nickname"));
                            user.setLove_count(us.optString("love_count"));
                            user.setFollow_count(us.optString("follow_count"));
                            user.setFans_count(us.optString("fans_count"));
                            user.setCounter(us.optString("counter"));
                            user.setAvatar_url(us.optString("avatar_url"));
                            sceneListBean.setUser_info(user);
                            JSONArray product = job.getJSONArray("product");
                            List<SceneListBean.Products> productsList = new ArrayList<SceneListBean.Products>();
                            for (int j = 0; j < product.length(); j++) {
                                JSONObject ob = product.getJSONObject(j);
                                SceneListBean.Products products = new SceneListBean.Products();
                                products.setId(ob.optString("id"));
                                products.setTitle(ob.optString("title"));
                                products.setPrice(ob.optString("price"));
                                products.setX(ob.optString("x"));
                                products.setY(ob.optString("y"));
                                productsList.add(products);
                            }
                            sceneListBean.setProductsList(productsList);
                            list.add(sceneListBean);
                        }
                        sceneList.setSceneListBeanList(list);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                msg.obj = sceneList;
                handler.sendMessage(msg);
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                Log.e("<<<failure>>>", "error = " + error.toString() + ",msg = " + msg);
                handler.sendEmptyMessage(DataConstants.NET_FAIL);
            }
        });
    }


    //标签
    //最近使用的标签
    public static void usedLabelList(final Handler handler) {
        ClientDiscoverAPI.usedLabelList(new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Message msg = handler.obtainMessage();
                msg.what = DataConstants.USED_LABEL_LIST;
                UsedLabel usedLabel = new UsedLabel();
                try {
                    JSONObject job = new JSONObject(responseInfo.result);
                    usedLabel.setSuccess(job.optBoolean("success"));
                    usedLabel.setMessage(job.optString("message"));
//                    usedLabel.setStatus(job.optString("status"));
                    if (usedLabel.isSuccess()) {
                        JSONObject data = job.getJSONObject("data");
                        usedLabel.setHas_tag(data.optInt("has_tag"));
                        if (usedLabel.getHas_tag() != 0) {
                            List<UsedLabelBean> list = new ArrayList<UsedLabelBean>();
                            JSONArray tags = data.getJSONArray("tags");
                            for (int i = 0; i < tags.length(); i++) {
                                JSONObject ob = tags.getJSONObject(i);
                                UsedLabelBean usedLabelBean = new UsedLabelBean();
                                usedLabelBean.set_id(ob.optString("_id"));
                                usedLabelBean.setUser_id(ob.optString("user_id"));
                                usedLabelBean.setUsed_count(ob.optString("used_count"));
                                usedLabelBean.setUpdated_on(ob.optString("updated_on"));
                                usedLabelBean.setType(ob.optString("type"));
                                usedLabelBean.setTitle_en(ob.optString("title_en"));
                                usedLabelBean.setTitle_cn(ob.optString("title_cn"));
                                usedLabelBean.setStick(ob.optString("stick"));
                                usedLabelBean.setStatus(ob.optString("status"));
                                list.add(usedLabelBean);
                            }
                            usedLabel.setUsedLabelList(list);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                msg.obj = usedLabel;
                //需要登录
                handler.sendMessage(msg);
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                Log.e("<<<failure>>>", "error = " + error.toString() + ",msg = " + msg);
                handler.sendEmptyMessage(DataConstants.NET_FAIL);
            }
        });
    }

    //标签
    //标签列表
    public static void labelList(String parent_id, int page, String size, int sort, int is_hot, final Handler handler) {
        ClientDiscoverAPI.labelList(parent_id, page, size, sort, is_hot, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Log.e("<<<", responseInfo.result);
                WriteJsonToSD.writeToSD("json", responseInfo.result);
                Message msg = handler.obtainMessage();
                msg.what = DataConstants.LABEL_LIST;
                AllLabel allLabel = new AllLabel();
                try {
                    JSONObject job = new JSONObject(responseInfo.result);
                    allLabel.setSuccess(job.optBoolean("success"));
                    allLabel.setMessage(job.optString("message"));
//                    allLabel.setStatus(job.optString("status"));
                    if (allLabel.isSuccess()) {
                        JSONObject data = job.getJSONObject("data");
                        JSONObject object = data.getJSONObject("1");
                        if (object.optInt("children_count") > 0) {
                            JSONArray children = object.getJSONArray("children");
                            List<AllLabelBean> childrenList = new ArrayList<AllLabelBean>();
                            for (int i = 0; i < children.length(); i++) {
                                JSONObject ob = children.getJSONObject(i);
                                AllLabelBean allLabelBean = new AllLabelBean();
                                allLabelBean.set_id(ob.optString("_id"));
                                allLabelBean.setTitle_cn(ob.optString("title_cn"));
                                allLabelBean.setChildren_count(ob.optInt("children_count"));
                                if (allLabelBean.getChildren_count() > 0) {
                                    JSONArray children2 = ob.getJSONArray("children");
                                    List<AllLabelBean> children2List = new ArrayList<AllLabelBean>();
                                    for (int j = 0; j < children2.length(); j++) {
                                        JSONObject ob2 = children2.getJSONObject(j);
                                        AllLabelBean allLabelBean2 = new AllLabelBean();
                                        allLabelBean2.set_id(ob2.optString("_id"));
                                        allLabelBean2.setTitle_cn(ob2.optString("title_cn"));
                                        allLabelBean2.setChildren_count(ob2.optInt("children_count"));
                                        if (allLabelBean2.getChildren_count() > 0) {
                                            JSONArray children3 = ob2.getJSONArray("children");
                                            List<AllLabelBean> children3List = new ArrayList<AllLabelBean>();
                                            for (int k = 0; k < children3.length(); k++) {
                                                JSONObject ob3 = children3.getJSONObject(k);
                                                AllLabelBean allLabelBean3 = new AllLabelBean();
                                                allLabelBean3.set_id(ob3.optString("_id"));
                                                allLabelBean3.setTitle_cn(ob3.optString("title_cn"));
                                                children3List.add(allLabelBean3);
                                            }
                                            allLabelBean2.setChildren(children3List);
                                        }
                                        children2List.add(allLabelBean2);
                                    }
                                    allLabelBean.setChildren(children2List);
                                }
                                childrenList.add(allLabelBean);
                            }
                            allLabel.setChildren(childrenList);
                        }
                    }
                    msg.obj = allLabel;
                } catch (JSONException e) {
                    Log.e("<<<", "解析异常");
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

    //公共
    //举报
    public static void report(String target_id, String type, String evt, final Handler handler) {
        ClientDiscoverAPI.report(target_id, type, evt, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
//                Log.e("<<<", responseInfo.result);
                Message msg = handler.obtainMessage();
                msg.what = DataConstants.REPORT;
                msg.obj = new NetBean();
                try {
                    Gson gson = new Gson();
                    Type type = new TypeToken<NetBean>() {
                    }.getType();
                    msg.obj = gson.fromJson(responseInfo.result, type);
                } catch (JsonSyntaxException e) {
                    Log.e("<<<", "解析异常" + e.toString());
                }
                handler.sendMessage(msg);
//                Gson gson = new Gson();
//                gson.fromJson()
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                Log.e("<<<failure>>>", "error = " + error.toString() + ",msg = " + msg);
                handler.sendEmptyMessage(DataConstants.NET_FAIL);
            }
        });
    }

    //公共
    //品牌列表
    public static void brandList(int page, int size, final Handler handler) {
        ClientDiscoverAPI.brandList(page, size, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Message msg = handler.obtainMessage();
                msg.what = DataConstants.BRAND_LIST;
                msg.obj = new BrandListBean();
                try {
                    Gson gson = new Gson();
                    Type type = new TypeToken<BrandListBean>() {
                    }.getType();
                    msg.obj = gson.fromJson(responseInfo.result, type);
                } catch (JsonSyntaxException e) {
                    Log.e("<<<", "数据异常" + e.toString());
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

    //公共
    //分类列表
    public static void categoryList(String page, String domin, final Handler handler) {
        ClientDiscoverAPI.categoryList(page, domin, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Message msg = handler.obtainMessage();
                msg.what = DataConstants.CATEGORY_LIST;
                CategoryBean categoryBean = new CategoryBean();
                try {
                    JSONObject job = new JSONObject(responseInfo.result);
                    categoryBean.setSuccess(job.optBoolean("success"));
                    categoryBean.setMessage(job.optString("message"));
//                    categoryBean.setStatus(job.optString("status"));
                    if (categoryBean.isSuccess()) {
                        JSONObject data = job.getJSONObject("data");
                        JSONArray rows = data.getJSONArray("rows");
                        List<CategoryListBean> list = new ArrayList<CategoryListBean>();
                        for (int i = 0; i < rows.length(); i++) {
                            JSONObject ob = rows.getJSONObject(i);
                            CategoryListBean categoryListBean = new CategoryListBean();
                            categoryListBean.set_id(ob.optString("_id"));
                            categoryListBean.setTitle(ob.optString("title"));
                            categoryListBean.setName(ob.optString("name"));
                            categoryListBean.setApp_cover_s_url(ob.optString("app_cover_s_url"));
                            list.add(categoryListBean);
                        }
                        categoryBean.setList(list);
                    }
                    msg.obj = categoryBean;
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

    //购物车
    //购物车商品数量
    public static void cartNum(final Handler handler) {
        ClientDiscoverAPI.cartNum(new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Message msg = handler.obtainMessage();
                msg.what = DataConstants.CART_NUM;
                msg.obj = new CartBean();
                try {
                    Gson gson = new Gson();
                    Type type = new TypeToken<CartBean>() {
                    }.getType();
                    msg.obj = gson.fromJson(responseInfo.result, type);
                } catch (JsonSyntaxException e) {
                    Toast.makeText(MainApplication.getContext(), "数据解析异常" + e.toString(), Toast.LENGTH_SHORT).show();
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

    //评论
    //提交评论
    public static void sendComment(String target_id, String content, String type, String is_reply, String reply_id, String reply_user_id, final Handler handler) {
        ClientDiscoverAPI.sendComment(target_id, content, type, is_reply, reply_id, reply_user_id, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Message msg = handler.obtainMessage();
                msg.what = DataConstants.SEND_COMMENT;
                msg.obj = new NetBean();
                try {
                    Gson gson = new Gson();
                    Type type = new TypeToken<NetBean>() {
                    }.getType();
                    NetBean netBean = gson.fromJson(responseInfo.result, type);
                    msg.obj = netBean;
                } catch (JsonSyntaxException e) {
                    Toast.makeText(MainApplication.getContext(), "数据解析异常" + e.toString(), Toast.LENGTH_SHORT).show();
                }
                handler.sendMessage(msg);
            }

            @Override
            public void onFailure(HttpException e, String s) {
                Log.e("<<<failure>>>", "error = " + e.toString() + ",msg = " + s);
                handler.sendEmptyMessage(DataConstants.NET_FAIL);
            }
        });
    }

    //评论
    //列表
    public static void commentsList(String page, String target_id, String type, final Handler handler) {
        ClientDiscoverAPI.commentsList(page, target_id, type, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Log.e("<<<", responseInfo.result);
                WriteJsonToSD.writeToSD("json", responseInfo.result);
                Message msg = handler.obtainMessage();
                msg.what = DataConstants.COMMENTS_LIST;
                Gson gson = new Gson();
                Type type = new TypeToken<CommentsBean>() {
                }.getType();
                msg.obj = gson.<CommentsBean>fromJson(responseInfo.result, type);
                handler.sendMessage(msg);
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                Log.e("<<<failure>>>", "error = " + error.toString() + ",msg = " + msg);
                handler.sendEmptyMessage(DataConstants.NET_FAIL);
            }
        });
    }

    //登录的解析
    public static void loginParser(String uuid, final Handler handler, final String phone, final String password) {
        ClientDiscoverAPI.clickLoginNet(uuid, phone, password, new RequestCallBack<String>() {
            Context context = MainApplication.getContext();

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {

                if (responseInfo == null) {
                    return;
                }

                if (TextUtils.isEmpty(responseInfo.result)) {
                    return;
                }

                //TODO 保存登录信息
                SPUtil.write(MainApplication.getContext(), DataConstants.LOGIN_INFO, responseInfo.result);

                LogUtil.e("ResponseInfo", responseInfo.result);

                //TODO 后期改造
                LoginInfo loginInfo = null;
                Message msg = new Message();
                msg.what = DataConstants.PARSER_LOGIN;
                try {
                    JSONObject obj = new JSONObject(responseInfo.result);
                    JSONObject loginObj = obj.getJSONObject("data");

                    loginInfo = LoginInfo.getInstance();
                    loginInfo.setId(obj.optLong("_id"));
                    loginInfo.setSuccess(obj.optString("success"));
                    loginInfo.setMessage(obj.optString("message"));
                    loginInfo.setAccount(loginObj.optString("account"));
                    loginInfo.setPhone(loginObj.optString("phone"));
                    loginInfo.setNickname(loginObj.optString("nickname"));
                    loginInfo.setTrue_nickname(loginObj.optString("true_nickname"));
                    loginInfo.setAddress(loginObj.optString("address"));
                    loginInfo.setBirthday(loginObj.optString("birthday"));
                    loginInfo.setCompany(loginObj.optString("company"));
                    loginInfo.setIm_qq(loginObj.optString("im_qq"));
                    loginInfo.setSex(loginObj.optString("sex"));
                    loginInfo.setRealname(loginObj.optString("realname"));
                    loginInfo.setZip(loginObj.optString("zip"));
                    loginInfo.setWeixin(loginObj.optString("weixin"));
                    loginInfo.setMedium_avatar_url(loginObj.optString("medium_avatar_url"));
//                    loginInfo.setFirst_login(Integer.parseInt(loginObj.optString("first_login")));奇怪，一加这句就会崩，可我怎么看也没解析错啊？待查……

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                msg.obj = loginInfo;

                SharedPreferences sp = context.getSharedPreferences(DataConstants.USERDATA_SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE);
                // TODO 向本地保存信息
                SharedPreferences.Editor edit = sp.edit();
                edit.putString(MainApplication.THN_MOBILE, phone);
                edit.putString(MainApplication.THN_PASSWORD, password);
                edit.commit();
                handler.sendMessage(msg);
            }

            @Override
            public void onFailure(HttpException e, String s) {
                handler.sendEmptyMessage(DataConstants.NETWORK_FAILURE);
            }
        });
    }

    //第三方登录
    public static void thirdLoginParser(final String oid, String access_token, String type, final Handler handler) {
        ClientDiscoverAPI.thirdLoginNet(oid, access_token, type, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                ThirdLogin thirdLogin = null;
                Message msg = new Message();
                msg.what = DataConstants.PARSER_THIRD_LOGIN;
                try {
                    JSONObject obj = new JSONObject(responseInfo.result);
                    JSONObject thirdObj = obj.getJSONObject("data");
                    thirdLogin = new ThirdLogin();
                    thirdLogin.setSuccess(obj.optString("success"));
                    thirdLogin.setHas_user(thirdObj.optString("has_user"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                msg.obj = thirdLogin;
                handler.sendMessage(msg);
            }

            @Override
            public void onFailure(HttpException e, String s) {
                handler.sendEmptyMessage(DataConstants.NETWORK_FAILURE);
            }
        });
    }

    //第三方登录之快捷注册(绑定手机号)
    public static void bindPhoneParser(String oid, String union_id, String access_token, String account, String password, String type, final Handler handler) {
        ClientDiscoverAPI.bindPhoneNet(oid, union_id, access_token, account, password, type, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                BindPhone bindPhone = null;
                Message msg = new Message();
                msg.what = DataConstants.PARSER_THIRD_LOGIN_BIND_PHONE;
                try {
                    JSONObject obj = new JSONObject(responseInfo.result);
                    bindPhone = new BindPhone();
                    bindPhone.setSuccess(obj.optString("success"));
                    bindPhone.setMessage(obj.optString("message"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                msg.obj = bindPhone;
                handler.sendMessage(msg);
            }

            @Override
            public void onFailure(HttpException e, String s) {
                handler.sendEmptyMessage(DataConstants.NETWORK_FAILURE);
            }
        });
    }

    //第三方登录之快捷注册(不绑定手机号)
    public static void skipBindParser(String uuid, String oid, String union_id, String access_token, String nickname, String sex, String avatar_url, String type, final Handler handler) {
        ClientDiscoverAPI.skipBindNet(uuid, oid, union_id, access_token, nickname, sex, avatar_url, type, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                SkipBind skipBind = null;
                Message msg = new Message();
                msg.what = DataConstants.PARSER_THIRD_LOGIN_SKIP_PHONE;
                try {
                    JSONObject obj = new JSONObject(responseInfo.result);
                    skipBind = new SkipBind();
                    skipBind.setSuccess(obj.optString("success"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                msg.obj = skipBind;
                handler.sendMessage(msg);
            }

            @Override
            public void onFailure(HttpException e, String s) {
                handler.sendEmptyMessage(DataConstants.NETWORK_FAILURE);
            }
        });
    }


    //找回密码的解析
    public static void findPasswordParser(final Handler handler, String phone, String password, String code) {
        ClientDiscoverAPI.findPasswordNet(phone, password, code, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                FindPasswordInfo findPasswordInfo = null;
                Message msg = new Message();
                msg.what = DataConstants.PARSER_FIND_PASSWORD;
                try {
                    JSONObject obj = new JSONObject(responseInfo.result);
                    JSONObject findPasswordObj = obj.getJSONObject("data");

                    findPasswordInfo = new FindPasswordInfo();
                    findPasswordInfo.setSuccess(Boolean.parseBoolean(obj.optString("success")));
                    findPasswordInfo.setMessage(obj.optString("message"));
                    findPasswordInfo.setAccount(findPasswordObj.optString("account"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                msg.obj = findPasswordInfo;
                handler.sendMessage(msg);
            }

            @Override
            public void onFailure(HttpException e, String s) {
                handler.sendEmptyMessage(DataConstants.NETWORK_FAILURE);
            }
        });
    }


}
