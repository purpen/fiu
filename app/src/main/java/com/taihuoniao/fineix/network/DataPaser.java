package com.taihuoniao.fineix.network;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.taihuoniao.fineix.base.NetBean;
import com.taihuoniao.fineix.beans.AllLabel;
import com.taihuoniao.fineix.beans.AllLabelBean;
import com.taihuoniao.fineix.beans.CategoryBean;
import com.taihuoniao.fineix.beans.CategoryListBean;
import com.taihuoniao.fineix.beans.HotLabel;
import com.taihuoniao.fineix.beans.HotLabelBean;
import com.taihuoniao.fineix.beans.JDDetailsBean;
import com.taihuoniao.fineix.beans.ProductBean;
import com.taihuoniao.fineix.beans.ProductListBean;
import com.taihuoniao.fineix.beans.TBDetailsBean;
import com.taihuoniao.fineix.beans.UsedLabel;
import com.taihuoniao.fineix.beans.UsedLabelBean;
import com.taihuoniao.fineix.utils.WriteJsonToSD;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
                    productBean.setStatus(job.optString("status"));
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
                    jdDetailsBean.setStatus(job.optString("status"));
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
                    tbDetailsBean.setStatus(job.optString("status"));
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
                            netBean.setStatus(job.optString("status"));
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
    //列表数据
    public static void getSceneList(String page, String stick, String dis, String lng, String lat, final Handler handler) {
        ClientDiscoverAPI.getSceneList(page, stick, dis, lng, lat, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Log.e("<<<", responseInfo.result);
                WriteJsonToSD.writeToSD("json", responseInfo.result);
                Message msg = handler.obtainMessage();
                msg.what = DataConstants.SCENE_LIST;

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
                    usedLabel.setStatus(job.optString("status"));
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
                    msg.obj = usedLabel;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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
    public static void labelList(String parent_id, int page, String size, final Handler handler) {
        ClientDiscoverAPI.labelList(parent_id, page, size, new RequestCallBack<String>() {
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
                    allLabel.setStatus(job.optString("status"));
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

    //标签
    //热门标签
    public static void hotLabelList(String page, final Handler handler) {
        ClientDiscoverAPI.hotLabelList(page, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Message msg = handler.obtainMessage();
                msg.what = DataConstants.HOT_LABEL_LIST;
                HotLabel hotLabel = new HotLabel();
                try {
                    JSONObject job = new JSONObject(responseInfo.result);
                    hotLabel.setSuccess(job.optBoolean("success"));
                    hotLabel.setStatus(job.optString("status"));
                    hotLabel.setMessage(job.optString("message"));
                    if (hotLabel.isSuccess()) {
                        List<HotLabelBean> hotLabelBeanList = new ArrayList<HotLabelBean>();
                        JSONObject data = job.getJSONObject("data");
                        JSONArray rows = data.getJSONArray("rows");
                        for (int i = 0; i < rows.length(); i++) {
                            JSONObject ob = rows.getJSONObject(i);
                            HotLabelBean hotLabelBean = new HotLabelBean();
                            hotLabelBean.set_id(ob.optString("_id"));
                            hotLabelBean.setUsed_count(ob.optString("used_count"));
                            hotLabelBean.setType_str(ob.optString("type_str"));
                            hotLabelBean.setTitle_en(ob.optString("title_en"));
                            hotLabelBean.setTitle_cn(ob.optString("title_cn"));
                            hotLabelBean.setStick(ob.optString("stick"));
                            hotLabelBean.setRight_ref(ob.optString("right_ref"));
                            hotLabelBean.setParent_id(ob.optString("parent_id"));
                            hotLabelBean.setLevel(ob.optString("level"));
                            hotLabelBean.setLeft_ref(ob.optString("left_ref"));
                            hotLabelBean.setParent_id(ob.optString("parent_id"));
                            hotLabelBean.setCover_id(ob.optString("cover_id"));
                            hotLabelBean.setChildren_count(ob.optString("children_count"));
                            hotLabelBean.setStatus(ob.optString("status"));
                            hotLabelBeanList.add(hotLabelBean);
                        }
                        hotLabel.setHotLabelBeanList(hotLabelBeanList);
                    }
                    msg.obj = hotLabel;
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
                    categoryBean.setStatus(job.optString("status"));
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
}
