package com.taihuoniao.fineix.network;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.taihuoniao.fineix.base.NetBean;
import com.taihuoniao.fineix.beans.AddProductBean;
import com.taihuoniao.fineix.beans.AddressBean;
import com.taihuoniao.fineix.beans.AllLabel;
import com.taihuoniao.fineix.beans.AllLabelBean;
import com.taihuoniao.fineix.beans.ApplyForRefund;
import com.taihuoniao.fineix.beans.BindPhone;
import com.taihuoniao.fineix.beans.BrandDetailBean;
import com.taihuoniao.fineix.beans.BrandListBean;
import com.taihuoniao.fineix.beans.CJHotLabelBean;
import com.taihuoniao.fineix.beans.CartBean;
import com.taihuoniao.fineix.beans.CartDoOrder;
import com.taihuoniao.fineix.beans.CartDoOrderBonus;
import com.taihuoniao.fineix.beans.CartOrderContent;
import com.taihuoniao.fineix.beans.CartOrderContentItem;
import com.taihuoniao.fineix.beans.CategoryBean;
import com.taihuoniao.fineix.beans.CategoryLabelListBean;
import com.taihuoniao.fineix.beans.CategoryListBean;
import com.taihuoniao.fineix.beans.CheckRedBagUsable;
import com.taihuoniao.fineix.beans.CityBean;
import com.taihuoniao.fineix.beans.CommentsBean;
import com.taihuoniao.fineix.beans.CommonBean;
import com.taihuoniao.fineix.beans.FindPasswordInfo;
import com.taihuoniao.fineix.beans.FiuUserListBean;
import com.taihuoniao.fineix.beans.GoodsDetailBean;
import com.taihuoniao.fineix.beans.GoodsDetailsBean;
import com.taihuoniao.fineix.beans.JDProductBean;
import com.taihuoniao.fineix.beans.NowBuyBean;
import com.taihuoniao.fineix.beans.NowBuyItemBean;
import com.taihuoniao.fineix.beans.NowConfirmBean;
import com.taihuoniao.fineix.beans.OrderDetails;
import com.taihuoniao.fineix.beans.OrderDetailsAddress;
import com.taihuoniao.fineix.beans.OrderDetailsProducts;
import com.taihuoniao.fineix.beans.OrderEntity;
import com.taihuoniao.fineix.beans.OrderItem;
import com.taihuoniao.fineix.beans.ProductAndSceneListBean;
import com.taihuoniao.fineix.beans.ProductBean;
import com.taihuoniao.fineix.beans.ProductListBean;
import com.taihuoniao.fineix.beans.ProvinceBean;
import com.taihuoniao.fineix.beans.QingJingListBean;
import com.taihuoniao.fineix.beans.QingjingDetailBean;
import com.taihuoniao.fineix.beans.QingjingSubsBean;
import com.taihuoniao.fineix.beans.RelationProductsBean;
import com.taihuoniao.fineix.beans.SceneDetails;
import com.taihuoniao.fineix.beans.SceneList;
import com.taihuoniao.fineix.beans.SceneListBean;
import com.taihuoniao.fineix.beans.SceneLoveBean;
import com.taihuoniao.fineix.beans.SearchBean;
import com.taihuoniao.fineix.beans.ShopCart;
import com.taihuoniao.fineix.beans.ShopCartInventoryItemBean;
import com.taihuoniao.fineix.beans.ShopCartItem;
import com.taihuoniao.fineix.beans.ShopCartNumber;
import com.taihuoniao.fineix.beans.SkipBind;
import com.taihuoniao.fineix.beans.SkusBean;
import com.taihuoniao.fineix.beans.TBProductBean;
import com.taihuoniao.fineix.beans.ThirdLogin;
import com.taihuoniao.fineix.beans.TryCommentsBean;
import com.taihuoniao.fineix.beans.TryDetailsUserBean;
import com.taihuoniao.fineix.beans.UsedLabel;
import com.taihuoniao.fineix.beans.UsedLabelBean;
import com.taihuoniao.fineix.beans.UserInfo;
import com.taihuoniao.fineix.beans.UserListBean;
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
    //产出用户添加的产品
    public static void deleteProduct(String id, final Handler handler) {
        ClientDiscoverAPI.deleteProduct(id, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Message msg = handler.obtainMessage();
                msg.what = DataConstants.DELETE_PRODUCT;
                msg.obj = new NetBean();
                try {
                    Gson gson = new Gson();
                    Type type = new TypeToken<NetBean>() {
                    }.getType();
                    msg.obj = gson.fromJson(responseInfo.result, type);
                } catch (JsonSyntaxException e) {
                    Log.e("<<<", "数据解析异常" + e.toString());
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
    //列表
    public static void getProductList(String category_id, String brand_id, String category_tag_ids, String page, String size, String ids, String ignore_ids,
                                      String stick, String fine, final Handler handler) {
        ClientDiscoverAPI.getProductList(category_id, brand_id, category_tag_ids, page, size, ids, ignore_ids, stick, fine, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Log.e("<<<商品列表", responseInfo.result);
                WriteJsonToSD.writeToSD("json", responseInfo.result);
                if (handler == null) {
                    return;
                }
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
                            List<String> category_tags = new ArrayList<String>();
                            JSONArray jsonArray = ob.getJSONArray("category_tags");
                            for (int j = 0; j < jsonArray.length(); j++) {
                                category_tags.add(jsonArray.optString(j));
                            }
                            productListBean.setAttrbute(ob.optString("attrbute"));
                            productListBean.setCategory_tags(category_tags);
                            List<String> banner = new ArrayList<String>();
                            JSONArray banner_asset = ob.optJSONArray("banner_asset");
                            for (int j = 0; j < banner_asset.length(); j++) {
                                banner.add(banner_asset.optString(j));
                            }
                            productListBean.banner_asset = (ArrayList<String>) banner;
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
    //用户添加产品
    public static void addProduct(String attrbute, String oid, String sku_id, String title, String market_price, String sale_price,
                                  String link, String cover_url, String banners_url, final Handler handler) {
        ClientDiscoverAPI.addProduct(attrbute, oid, sku_id, title, market_price, sale_price, link, cover_url, banners_url, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Log.e("<<<addproduct", responseInfo.result);
                WriteJsonToSD.writeToSD("json", responseInfo.result);
                Message msg = handler.obtainMessage();
                msg.what = DataConstants.ADD_PRODUCT;
                msg.obj = new AddProductBean();
                try {
                    Gson gson = new Gson();
                    Type type = new TypeToken<AddProductBean>() {
                    }.getType();
                    msg.obj = gson.fromJson(responseInfo.result, type);
                } catch (JsonSyntaxException e) {
                    Log.e("<<<", "数据解析异常" + e.toString());
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
    //产品详情
    public static void goodsDetail(String id, final Handler handler) {
        ClientDiscoverAPI.goodsDetails(id, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Log.e("<<<>>>", responseInfo.result);
                WriteJsonToSD.writeToSD("json", responseInfo.result);
                Message msg = handler.obtainMessage();
                msg.what = DataConstants.GOODS_DETAIL;
                msg.obj = new GoodsDetailBean();
                try {
                    Gson gson = new Gson();
                    Type type = new TypeToken<GoodsDetailBean>() {
                    }.getType();
                    msg.obj = gson.fromJson(responseInfo.result, type);
                } catch (JsonSyntaxException e) {
                    Log.e("<<<>>>", "数据异常" + e.toString());
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
    //自营商品详情
    public static void goodsDetails(String id, final Handler handler) {
        ClientDiscoverAPI.goodsDetailsNet(id, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Message msg = handler.obtainMessage();
                Log.e("<<<chanpin", responseInfo.result);
                WriteJsonToSD.writeToSD("json", responseInfo.result);
                msg.what = DataConstants.GOODS_DETAILS;
                try {
                    JSONObject obj = new JSONObject(responseInfo.result);
                    GoodsDetailsBean goodsDetailsBean = new GoodsDetailsBean();
                    goodsDetailsBean.setSuccess(obj.optBoolean("success"));
                    goodsDetailsBean.setMessage(obj.optString("message"));
                    JSONObject data = obj.getJSONObject("data");
                    if (goodsDetailsBean.isSuccess()) {
                        goodsDetailsBean.set_id(data.optString("_id"));
                        goodsDetailsBean.setTitle(data.optString("title"));
                        goodsDetailsBean.setSale_price(data.optString("sale_price"));
                        goodsDetailsBean.setMarket_price(data.optString("market_price"));
                        goodsDetailsBean.setIs_love(data.optString("is_love"));
                        goodsDetailsBean.setShare_view_url(data.optString("share_view_url"));
                        goodsDetailsBean.setShare_desc(data.optString("share_desc"));
                        JSONArray asset = data.getJSONArray("asset");
                        List<String> imgUrlList = new ArrayList<>();
                        for (int i = 0; i < asset.length(); i++) {
                            String imgUrl = asset.optString(i);
                            imgUrlList.add(imgUrl);
                        }
                        goodsDetailsBean.setImgUrlList(imgUrlList);
                        JSONArray relationProducts = data.getJSONArray("relation_products");
                        List<RelationProductsBean> relationProductsList = new ArrayList<>();
                        for (int i = 0; i < relationProducts.length(); i++) {
                            JSONObject job = relationProducts.optJSONObject(i);
                            RelationProductsBean relationProductsBean = new RelationProductsBean();
                            relationProductsBean.set_id(job.optString("_id"));
                            relationProductsBean.setCover_url(job.optString("cover_url"));
                            relationProductsBean.setTitle(job.optString("title"));
                            relationProductsBean.setSale_price(job.optString("sale_price"));
                            relationProductsList.add(relationProductsBean);
                        }
                        goodsDetailsBean.setRelationProductsList(relationProductsList);
                        goodsDetailsBean.setWap_view_url(data.optString("wap_view_url"));
                        goodsDetailsBean.setContent_view_url(data.optString("content_view_url"));
                        goodsDetailsBean.setCover_url(data.optString("cover_url"));
                        goodsDetailsBean.setSkus_count(data.optString("skus_count"));
                        if (Integer.parseInt(data.optString("skus_count")) > 0) {
                            List<SkusBean> skuList = new ArrayList<>();
                            JSONArray skus = data.getJSONArray("skus");
                            for (int i = 0; i < skus.length(); i++) {
                                JSONObject jsonObject = skus.getJSONObject(i);
                                SkusBean skusBean = new SkusBean();
                                skusBean.set_id(jsonObject.optString("_id"));
                                skusBean.setMode(jsonObject.optString("mode"));
                                skusBean.setPrice(jsonObject.optString("price"));
                                skusBean.setQuantity(jsonObject.optString("quantity"));
                                skuList.add(skusBean);
                            }
                            goodsDetailsBean.setSkus(skuList);
                        }
                        goodsDetailsBean.setInventory(data.optString("inventory"));
                    }
                    msg.obj = goodsDetailsBean;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                handler.sendMessage(msg);
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                handler.sendEmptyMessage(DataConstants.NETWORK_FAILURE);
            }
        });
    }

    //产品
    //京东商品详情
    public static void getJDProductData(final Handler handler, String ids) {
        ClientDiscoverAPI.getJDProductsData(ids, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
//                Log.e("<<<jingdong", responseInfo.result);
//                WriteJsonToSD.writeToSD("json", responseInfo.result);
                Message msg = handler.obtainMessage();
                msg.what = DataConstants.JINGDONG;
                msg.obj = new JDProductBean();
                try {
                    Gson gson = new Gson();
                    Type type = new TypeToken<JDProductBean>() {
                    }.getType();
                    msg.obj = gson.fromJson(responseInfo.result, type);
                } catch (JsonSyntaxException e) {
                    Log.e("<<<", "数据解析异常" + e.toString());
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
//                Log.e("<<<taobao", responseInfo.result);
//                WriteJsonToSD.writeToSD("json", responseInfo.result);
                Message msg = handler.obtainMessage();
                msg.what = DataConstants.TAOBAO;
                msg.obj = new TBProductBean();
                try {
                    Gson gson = new Gson();
                    Type type = new TypeToken<TBProductBean>() {
                    }.getType();
                    msg.obj = gson.fromJson(responseInfo.result, type);
                } catch (JsonSyntaxException e) {
                    Log.e("<<<", "数据解析异常" + e.toString());
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
//                Log.e("<<<tongyong", responseInfo.result);
//                WriteJsonToSD.writeToSD("json", responseInfo.result);
                if (handler == null) {
                    return;
                }
                Message msg = handler.obtainMessage();
                msg.what = DataConstants.COMMON_LIST;
                msg.obj = new CommonBean();
                try {
                    Gson gson = new Gson();
                    Type type = new TypeToken<CommonBean>() {
                    }.getType();
                    msg.obj = gson.fromJson(responseInfo.result, type);
                } catch (JsonSyntaxException e) {
                    Log.e("<<<通用列表>>>", "数据解析异常" + e.toString());
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
//                    Toast.makeText(MainApplication.getContext(), "解析异常" + e.toString(), Toast.LENGTH_SHORT).show();
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
//                    Toast.makeText(MainApplication.getContext(), "解析异常" + e.toString(), Toast.LENGTH_SHORT).show();
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
                AddProductBean netBean = new AddProductBean();
                try {
                    Gson gson = new Gson();
                    Type type = new TypeToken<AddProductBean>() {
                    }.getType();
                    netBean = gson.fromJson(responseInfo.result, type);
                } catch (JsonSyntaxException e) {
                    Log.e("<<<创建情景", "数据解析异常");
                }
                msg.obj = netBean;
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
    public static void qingjingList(String page, String sort, String dis, String lng, String lat, final Handler handler) {
        ClientDiscoverAPI.qingjingList(page, sort, dis, lng, lat, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Log.e("<<<情景列表", responseInfo.result);
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
                        Log.e("<<<创建场景", responseInfo.result);
                        Message msg = handler.obtainMessage();
                        msg.what = DataConstants.CREATE_SCENE;
                        msg.obj = new AddProductBean();
                        try {
                            Gson gson = new Gson();
                            Type type = new TypeToken<AddProductBean>() {
                            }.getType();
                            msg.obj = gson.fromJson(responseInfo.result, type);
                        } catch (JsonSyntaxException e) {
                            Log.e("<<<创建场景", "数据解析异常");
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
                        Log.e("<<<场景详情", responseInfo.result);
                        WriteJsonToSD.writeToSD("json", responseInfo.result);
                        Message msg = handler.obtainMessage();
                        msg.what = DataConstants.SCENE_DETAILS;
                        SceneDetails sceneDetails = new SceneDetails();
                        try {
                            JSONObject jsonObject = new JSONObject(responseInfo.result);
                            sceneDetails.setSuccess(jsonObject.optBoolean("success"));
                            sceneDetails.setMessage(jsonObject.optString("message"));
                            sceneDetails.setCurrent_user_id(jsonObject.optString("current_user_id"));
                            sceneDetails.setStatus(jsonObject.optString("status"));
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
                                sceneDetails.setCreated_at(data.optString("created_at"));
                                sceneDetails.setCover_url(data.optString("cover_url"));
                                sceneDetails.setScene_title(data.optString("scene_title"));
                                SceneDetails.UserInfo userInfo = new SceneDetails.UserInfo();
                                JSONObject user_info = data.getJSONObject("user_info");
                                userInfo.setUser_id(user_info.optString("user_id"));
                                userInfo.setNickname(user_info.optString("nickname"));
                                userInfo.setAvatar_url(user_info.optString("avatar_url"));
                                userInfo.setSummary(user_info.optString("summary"));
                                userInfo.setIs_expert(user_info.optString("is_expert"));
                                sceneDetails.setUser_info(userInfo);
                                JSONObject jsonObject1 = data.getJSONObject("location");
                                JSONArray jsonArray = jsonObject1.getJSONArray("coordinates");
                                String[] location = new String[2];
                                location[0] = jsonArray.getString(1);
                                location[1] = jsonArray.getString(0);
                                sceneDetails.setLocation(location);
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
    //删除场景
    public static void deleteScene(String id, final Handler handler) {
        ClientDiscoverAPI.deleteScene(id, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Message msg = handler.obtainMessage();
                msg.what = DataConstants.DELETE_SCENE;
                msg.obj = new NetBean();
                try {
                    Gson gson = new Gson();
                    Type type = new TypeToken<NetBean>() {
                    }.getType();
                    msg.obj = gson.fromJson(responseInfo.result, type);
                } catch (JsonSyntaxException e) {
                    Log.e("<<<删除场景", "数据解析异常");
                }
                handler.sendMessage(msg);
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                handler.sendEmptyMessage(DataConstants.NET_FAIL);
            }
        });
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
//                    Toast.makeText(MainApplication.getContext(), "解析异常" + e.toString(), Toast.LENGTH_SHORT).show();
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
//                    Toast.makeText(MainApplication.getContext(), "解析异常" + e.toString(), Toast.LENGTH_SHORT).show();
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
    public static void getSceneList(String page, String size, String scene_id, String sort, String dis, String lng, String lat, final Handler handler) {
        ClientDiscoverAPI.getSceneList(page, size, scene_id, sort, dis, lng, lat, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Log.e("<<<场景列表", responseInfo.result);
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
                            user.setIs_expert(us.optString("is_expert"));
                            user.setUser_id(us.optString("user_id"));
                            user.setSummary(us.optString("summary"));
                            user.setNickname(us.optString("nickname"));
                            user.setLove_count(us.optString("love_count"));
                            user.setFollow_count(us.optString("follow_count"));
                            user.setFans_count(us.optString("fans_count"));
//                            user.setCounter(us.optString("counter"));
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

    //个人中心
    //用户列表
    public static void userList(String page, String size, String sort, String has_scene, final Handler handler) {
        ClientDiscoverAPI.userList(page, size, sort, has_scene, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Message msg = handler.obtainMessage();
                msg.what = DataConstants.USER_LIST;
                try {
                    Gson gson = new Gson();
                    Type type = new TypeToken<UserListBean>() {
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


    //标签
    //最近使用的标签
    public static void usedLabelList(final Handler handler) {
        ClientDiscoverAPI.usedLabelList(new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Log.e("<<<用过的标签", responseInfo.result);
                WriteJsonToSD.writeToSD("json", responseInfo.result);
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
                Log.e("<<<全部标签", responseInfo.result);
                WriteJsonToSD.writeToSD("quanbubiaoqian", responseInfo.result);
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

    //标签
    //场景页热门标签
    public static void cjHotLabel(boolean isCJ, final Handler handler) {
        ClientDiscoverAPI.cjHotLabel(isCJ, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Message msg = handler.obtainMessage();
                msg.what = DataConstants.CJ_HOTLABEL;
                msg.obj = new CJHotLabelBean();
                try {
                    Gson gson = new Gson();
                    Type type = new TypeToken<CJHotLabelBean>() {
                    }.getType();
                    msg.obj = gson.fromJson(responseInfo.result, type);
                } catch (JsonSyntaxException e) {
                    Log.e("<<<场景热门标签", "数据解析异常");
                }
                handler.sendMessage(msg);
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                handler.sendEmptyMessage(DataConstants.NET_FAIL);
            }
        });
    }

    //收货地址
    //获取省市列表
    public static void getProvinceList(final Handler handler) {
        ClientDiscoverAPI.getProvinceList(new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Message msg = handler.obtainMessage();
                msg.what = DataConstants.PROVINCE_LIST;
                List<ProvinceBean> list = new ArrayList<ProvinceBean>();
                try {
                    JSONObject job = new JSONObject(responseInfo.result);
                    JSONObject data = job.getJSONObject("data");
                    JSONArray rows = data.getJSONArray("rows");
                    for (int i = 0; i < rows.length(); i++) {
                        JSONObject pro = rows.getJSONObject(i);
                        ProvinceBean provinceBean = new ProvinceBean();
                        provinceBean.set_id(pro.optString("_id"));
                        provinceBean.setName(pro.optString("city"));
                        JSONArray cities = pro.getJSONArray("cities");
                        List<CityBean> cityBeanList = new ArrayList<CityBean>();
                        for (int j = 0; j < cities.length(); j++) {
                            JSONObject city = cities.getJSONObject(j);
                            CityBean cityBean = new CityBean();
                            cityBean.set_id(city.optString("_id"));
                            cityBean.setName(city.optString("city"));
                            cityBean.setParent_id(city.optString("parent_id"));
                            cityBeanList.add(cityBean);
                        }
                        provinceBean.setCityList(cityBeanList);
                        list.add(provinceBean);
                    }
                    msg.obj = list;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                handler.sendMessage(msg);

            }

            @Override
            public void onFailure(HttpException error, String msg) {
                handler.sendEmptyMessage(DataConstants.NETWORK_FAILURE);
            }

        });
    }

    //收货地址
    //  默认收货地址的解析
    public static void getDefaultAddress(final Handler handler) {
        ClientDiscoverAPI.getDefaultAddressNet(new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Message msg = handler.obtainMessage();
                msg.what = DataConstants.DEFAULT_ADDRESS;
                AddressBean addressBean = null;
                try {
                    JSONObject obj = new JSONObject(responseInfo.result);
                    JSONObject data = obj.getJSONObject("data");
                    addressBean = new AddressBean();
                    addressBean.setHas_default(data.optString("has_default"));
                    if ("1".equals(addressBean.getHas_default())) {
                        addressBean.set_id(data.optString("_id"));
                        addressBean.setName(data.optString("name"));
                        addressBean.setPhone(data.optString("phone"));
                        addressBean.setAddress(data.optString("address"));
                        addressBean.setZip(data.optString("zip"));
                        addressBean.setProvince(data.optString("province"));
                        addressBean.setCity(data.optString("city"));
                        addressBean.setProvince_name(data.optString("province_name"));
                        addressBean.setCity_name(data.optString("city_name"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                msg.obj = addressBean;
                handler.sendMessage(msg);
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                handler.sendEmptyMessage(DataConstants.NETWORK_FAILURE);
            }
        });
    }


    //收货地址
    //  获取用户的收货地址列表
    public static void getAddressList(String page, final Handler handler) {
        ClientDiscoverAPI.getAddressList(page, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Message msg = handler.obtainMessage();
                msg.what = DataConstants.GET_ADDRESS_LIST;
                List<AddressBean> list = new ArrayList<AddressBean>();
                try {
                    JSONObject job = new JSONObject(responseInfo.result);
                    JSONObject data = job.getJSONObject("data");
                    JSONArray rows = data.getJSONArray("rows");
                    for (int i = 0; i < rows.length(); i++) {
                        JSONObject ob = rows.getJSONObject(i);
                        AddressBean addressBean = new AddressBean();
                        addressBean.set_id(ob.optString("_id"));
                        addressBean.setName(ob.optString("name"));
                        addressBean.setPhone(ob.optString("phone"));
                        addressBean.setProvince(ob.optString("province"));
                        addressBean.setCity(ob.optString("city"));
                        addressBean.setAddress(ob.optString("address"));
                        addressBean.setZip(ob.optString("zip"));
                        addressBean.setIs_default(ob.optString("is_default"));
                        addressBean.setProvince(ob.optString("province"));
                        addressBean.setCity(ob.optString("city"));
                        addressBean.setProvince_name(ob.optString("province_name"));
                        addressBean.setCity_name(ob.optString("city_name"));
                        list.add(addressBean);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                msg.obj = list;
                handler.sendMessage(msg);
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                handler.sendEmptyMessage(DataConstants.NETWORK_FAILURE);
            }
        });
    }


    //收货地址
//提交编辑的收货地址返回值解析
    public static void commitAddressParser(String id, String name, String phone, String province, String city, String address, String zip, String is_default, final Handler handler) {
        ClientDiscoverAPI.commitAddressNet(id, name, phone, province, city, address, zip, is_default, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Message msg = handler.obtainMessage();
                msg.what = DataConstants.COMMIT_NEW_ADDRESS;
                msg.obj = new NetBean();
                try {
                    Gson gson = new Gson();
                    Type type = new TypeToken<NetBean>() {
                    }.getType();
                    msg.obj = gson.fromJson(responseInfo.result, type);
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                }
                handler.sendMessage(msg);
            }

            @Override
            public void onFailure(HttpException e, String s) {
                handler.sendEmptyMessage(DataConstants.NETWORK_FAILURE);
            }
        });
    }

    //收货地址
    //  删除收货地址返回值解析
    public static void deleteAddress(String id, final Handler handler) {
        ClientDiscoverAPI.deleteAddressNet(id, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Message msg = handler.obtainMessage();
                msg.what = DataConstants.DELETE_ADDRESS;
                try {
                    Gson gson = new Gson();
                    Type type = new TypeToken<NetBean>(){}.getType();
                    msg.obj = gson.fromJson(responseInfo.result,type);
//                    JSONObject ojb = new JSONObject(responseInfo.result);
//                    msg.obj = ojb.optBoolean("success");
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                }
                handler.sendMessage(msg);
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                handler.sendEmptyMessage(DataConstants.NETWORK_FAILURE);
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
                Log.e("<<<>>", responseInfo.result);
                WriteJsonToSD.writeToSD("json", responseInfo.result);
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
                handler.sendMessageDelayed(msg, 300);
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                Log.e("<<<failure>>>", "error = " + error.toString() + ",msg = " + msg);
                handler.sendEmptyMessage(DataConstants.NET_FAIL);
            }
        });
    }

    //公共
    //品牌详情
    public static void brandDetail(String id, final Handler handler) {
        ClientDiscoverAPI.brandDetail(id, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Message msg = handler.obtainMessage();
                msg.what = DataConstants.BRAND_DETAIL;
                msg.obj = new BrandDetailBean();
                try {
                    Gson gson = new Gson();
                    Type type = new TypeToken<BrandDetailBean>() {
                    }.getType();
                    msg.obj = gson.fromJson(responseInfo.result, type);
                } catch (JsonSyntaxException e) {
                    Log.e("<<<", "数据解析异常" + e.toString());
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
    public static void categoryList(String page, String domain, final Handler handler) {
        ClientDiscoverAPI.categoryList(page, domain, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Log.e("<<<>", responseInfo.result);
                WriteJsonToSD.writeToSD("json", responseInfo.result);
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
                            categoryListBean.setTag_id(ob.optString("tag_id"));
                            categoryListBean.setApp_cover_s_url(ob.optString("app_cover_s_url"));
                            categoryListBean.setApp_cover_url(ob.optString("app_cover_url"));
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

    //公共
    //搜索列表
    public static void search(String q, String t, String page, String evt, String sort, final Handler handler) {
        ClientDiscoverAPI.search(q, t, page, evt, sort, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
//                Log.e("<<<搜索", responseInfo.result);
//                WriteJsonToSD.writeToSD("json", responseInfo.result);
                Message msg = handler.obtainMessage();
                msg.what = DataConstants.SEARCH_LIST;
                msg.obj = new SearchBean();
                try {
                    Gson gson = new Gson();
                    Type type = new TypeToken<SearchBean>() {
                    }.getType();
                    msg.obj = gson.fromJson(responseInfo.result, type);
                } catch (JsonSyntaxException e) {
                    Log.e("<<<", "数据解析异常" + e.toString());
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
    //场景商品关联列表
    public static void productAndScene(String page, String size, String sight_id, String product_id, final Handler handler) {
        ClientDiscoverAPI.productAndScene(page, size, sight_id, product_id, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Log.e("<<<proScene", responseInfo.result);
                WriteJsonToSD.writeToSD("json", responseInfo.result);
                Message msg = handler.obtainMessage();
                msg.what = DataConstants.PRODUCT_AND_SCENE;
                msg.obj = new ProductAndSceneListBean();
                try {
                    Gson gson = new Gson();
                    Type type = new TypeToken<ProductAndSceneListBean>() {
                    }.getType();
                    msg.obj = gson.fromJson(responseInfo.result, type);
                } catch (JsonSyntaxException e) {
                    Log.e("<<<场景商品关联列表>>>", "数据异常" + e.toString());
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
                    Log.e("<<<>>>", "数据异常" + e.toString());
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
    public static void sendComment(String target_id, String content, String type, String target_user_id, String is_reply, String reply_id, String reply_user_id, final Handler handler) {
        ClientDiscoverAPI.sendComment(target_id, content, type, target_user_id, is_reply, reply_id, reply_user_id, new RequestCallBack<String>() {
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
//                    Toast.makeText(MainApplication.getContext(), "数据解析异常" + e.toString(), Toast.LENGTH_SHORT).show();
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
    public static void commentsList(String page, String size, String target_id, String target_user_id, String type, final Handler handler) {
        ClientDiscoverAPI.commentsList(page, size, target_id, target_user_id, type, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Log.e("<<<评论", responseInfo.result);
                WriteJsonToSD.writeToSD("json", responseInfo.result);
                Message msg = handler.obtainMessage();
                msg.what = DataConstants.COMMENTS_LIST;
                msg.obj = new CommentsBean();
                try {
                    Gson gson = new Gson();
                    Type type = new TypeToken<CommentsBean>() {
                    }.getType();
                    msg.obj = gson.<CommentsBean>fromJson(responseInfo.result, type);
                } catch (JsonSyntaxException e) {
                    Log.e("<<<评论列表>>>", "数据解析异常" + e.toString());
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
    //删除评论
    public static void deleteComment(String id, final Handler handler) {
        ClientDiscoverAPI.deleteComment(id, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Message msg = handler.obtainMessage();
                msg.what = DataConstants.DELETE_COMMENT;
                msg.obj = new NetBean();
                try {
                    Gson gson = new Gson();
                    Type type = new TypeToken<NetBean>() {
                    }.getType();
                    msg.obj = gson.fromJson(responseInfo.result, type);
                } catch (JsonSyntaxException e) {
                    Log.e("<<<删除评论", "数据解析异常" + e.toString());
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

    //评论列表
    //  商品详情界面的评论列表的解析
    public static void getGoodsDetailsCommentsList(String target_id, String page, final Handler handler) {
        ClientDiscoverAPI.getGoodsDetailsCommentsList(target_id, page, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {

                Message msg = handler.obtainMessage();
                msg.what = DataConstants.GOODS_DETAILS_COMMENTS;
                msg.obj = parserTryDetailsCommentsList(responseInfo.result);
                handler.sendMessage(msg);
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                handler.sendEmptyMessage(DataConstants.NETWORK_FAILURE);
            }
        });
    }

    private static List<TryCommentsBean> parserTryDetailsCommentsList(String jsonString) {
        List<TryCommentsBean> list = null;
        try {
            list = new ArrayList<>();
            JSONObject obj = new JSONObject(jsonString);
            JSONObject data = obj.getJSONObject("data");
            JSONArray rows = data.getJSONArray("rows");
            for (int i = 0; i < rows.length(); i++) {
                JSONObject job = rows.getJSONObject(i);
                TryCommentsBean bean = new TryCommentsBean();
                bean.set_id(job.optString("_id"));
                bean.setUser_id(job.optString("user_id"));
                bean.setContent(job.optString("content"));
                bean.setStar(job.optString("star"));
                bean.setTarget_id(job.optString("target_id"));
                bean.setTarget_user_id(job.optString("target_user_id"));
                bean.setSku_id(job.optString("sku_id"));
                bean.setDeleted(job.optString("deleted"));
                bean.setReply_user_id(job.optString("reply_user_id"));
                bean.setFloor(job.optString("floor"));
                bean.setType(job.optString("type"));
                bean.setSub_type(job.optString("sub_type"));
                JSONObject userJob = job.getJSONObject("user");
                TryDetailsUserBean user = new TryDetailsUserBean();
                user.set_id(userJob.optString("_id"));
                user.setNickname(userJob.optString("nickname"));
                user.setHome_url(userJob.optString("home_url"));
                user.setSmall_avatar_url(userJob.optString("small_avatar_url"));
                user.setSymbol(userJob.optString("symbol"));
                user.setMini_avatar_url(userJob.optString("mini_avatar_url"));
                user.setMedium_avatar_url(userJob.optString("medium_avatar_url"));
                user.setBig_avatar_url(userJob.optString("big_avatar_url"));
                bean.setUser(user);
                if (!job.optString("target_user").equals("null")) {
                    JSONObject targetUserJob = job.getJSONObject("target_user");
                    TryDetailsUserBean targetUser = new TryDetailsUserBean();
                    targetUser.set_id(targetUserJob.optString("_id"));
                    targetUser.setNickname(targetUserJob.optString("nickname"));
                    targetUser.setHome_url(targetUserJob.optString("home_url"));
                    targetUser.setSmall_avatar_url(targetUserJob.optString("small_avatar_url"));
                    targetUser.setSymbol(targetUserJob.optString("symbol"));
                    targetUser.setMini_avatar_url(targetUserJob.optString("mini_avatar_url"));
                    targetUser.setMedium_avatar_url(targetUserJob.optString("medium_avatar_url"));
                    targetUser.setBig_avatar_url(targetUserJob.optString("big_avatar_url"));
                    bean.setTarget_user(targetUser);
                }
                bean.setLove_count(job.optString("love_count"));
                bean.setInvented_love_count(job.optString("invented_love_count"));
                bean.setIs_reply(job.optString("is_reply"));
                bean.setReply_id(job.optString("reply_id"));
                bean.setCreated_at(job.optString("created_at"));
                bean.setCreated_on(job.optString("created_on"));
                bean.setUpdated_on(job.optString("updated_on"));
                list.add(bean);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    //分类
    //分类标签
    public static void categoryLabel(String tag_id, final Handler handler) {
        ClientDiscoverAPI.categoryLabel(tag_id, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
//                Log.e("<<<..", responseInfo.result);
//                WriteJsonToSD.writeToSD("json", responseInfo.result);
                Message msg = handler.obtainMessage();
                msg.what = DataConstants.CATEGORY_LABEL;
                msg.obj = new CategoryLabelListBean();
                try {
                    Gson gson = new Gson();
                    Type type = new TypeToken<CategoryLabelListBean>() {
                    }.getType();
                    msg.obj = gson.fromJson(responseInfo.result, type);
                } catch (JsonSyntaxException e) {
                    Log.e("<<<", "数据解析异常" + e.toString());
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


    //登录的解析
    public static void loginParser(String uuid, final Handler handler, final String phone, final String password) {
//        ClientDiscoverAPI.clickLoginNet(uuid, phone, password, new RequestCallBack<String>() {
//            Context context = MainApplication.getContext();
//
//            @Override
//            public void onSuccess(ResponseInfo<String> responseInfo) {
//
//                if (responseInfo == null) {
//                    return;
//                }
//
//                if (TextUtils.isEmpty(responseInfo.result)) {
//                    return;
//                }
//
//                //TODO 保存登录信息
//                SPUtil.write(MainApplication.getContext(), DataConstants.LOGIN_INFO, responseInfo.result);
//
//                LogUtil.e("LOGIN_INFO", responseInfo.result);
//
//                //TODO 后期改造
//                LoginInfo loginInfo = null;
//                Message msg = new Message();
//                msg.what = DataConstants.PARSER_LOGIN;
//                try {
//                    JSONObject obj = new JSONObject(responseInfo.result);
//                    JSONObject loginObj = obj.getJSONObject("data");
//
//                    loginInfo = LoginInfo.getInstance();
//                    loginInfo.setId(obj.optLong("_id"));
//                    loginInfo.setSuccess(obj.optString("success"));
//                    loginInfo.setMessage(obj.optString("message"));
//                    loginInfo.setAccount(loginObj.optString("account"));
//                    loginInfo.setPhone(loginObj.optString("phone"));
//                    loginInfo.setNickname(loginObj.optString("nickname"));
//                    loginInfo.setTrue_nickname(loginObj.optString("true_nickname"));
//                    loginInfo.setAddress(loginObj.optString("address"));
//                    loginInfo.setBirthday(loginObj.optString("birthday"));
//                    loginInfo.setCompany(loginObj.optString("company"));
//                    loginInfo.setIm_qq(loginObj.optString("im_qq"));
//                    loginInfo.setSex(loginObj.optString("sex"));
//                    loginInfo.setRealname(loginObj.optString("realname"));
//                    loginInfo.setZip(loginObj.optString("zip"));
//                    loginInfo.setWeixin(loginObj.optString("weixin"));
//                    loginInfo.setMedium_avatar_url(loginObj.optString("medium_avatar_url"));
////                    loginInfo.setFirst_login(Integer.parseInt(loginObj.optString("first_login")));奇怪，一加这句就会崩，可我怎么看也没解析错啊？待查……
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                msg.obj = loginInfo;
//
//                SharedPreferences sp = context.getSharedPreferences(DataConstants.USERDATA_SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE);
//                // TODO 向本地保存信息
//                SharedPreferences.Editor edit = sp.edit();
//                edit.putString(MainApplication.THN_MOBILE, phone);
//                edit.putString(MainApplication.THN_PASSWORD, password);
//                edit.commit();
//                handler.sendMessage(msg);
//            }
//
//            @Override
//            public void onFailure(HttpException e, String s) {
//                handler.sendEmptyMessage(DataConstants.NETWORK_FAILURE);
//            }
//        });
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
//                    thirdLogin.setSuccess(obj.optString("success"));
//                    thirdLogin.setHas_user(thirdObj.optString("has_user"));
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
        ClientDiscoverAPI.skipBindNet(oid, union_id, access_token, nickname, sex, avatar_url, type, new RequestCallBack<String>() {
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

    //验证红包是否可用接口
    public static void checkRedbagUsableParser(String rid, String code, final Handler handler) {
        ClientDiscoverAPI.checkRedBagUsableNet(rid, code, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                CheckRedBagUsable checkRedBagUsable = null;
                Message msg = new Message();

                msg.what = DataConstants.PARSER_CHECK_REDBAG_USABLE;

                try {
                    checkRedBagUsable = new CheckRedBagUsable();
                    JSONObject obj = new JSONObject(responseInfo.result);
                    JSONObject redbagObj = obj.getJSONObject("data");
                    checkRedBagUsable.setCode(redbagObj.optString("code"));
                    checkRedBagUsable.setCoin_money(redbagObj.optString("coin_money"));
                    checkRedBagUsable.setUseful(redbagObj.optString("useful"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                msg.obj = checkRedBagUsable;
                handler.sendMessage(msg);
            }

            @Override
            public void onFailure(HttpException e, String s) {
                handler.sendEmptyMessage(DataConstants.NETWORK_FAILURE);
            }
        });
    }


    //帐户处未过期红包
    public static void unTimeoutParser(final String used, final String time, final Handler handler) {
//        ClientDiscoverAPI.myRedBagNet(used, time, new RequestCallBack<String>() {
//            @Override
//            public void onSuccess(ResponseInfo<String> responseInfo) {
//                List<RedBagUntimeout> list = null;
//                Message msg = new Message();
//                if ("0".equals(used)) {
//                    msg.what = DataConstants.PARSER_MY_REDBAG_TIMEOUT;
//                } else {
//                    msg.what = DataConstants.PARSER_MY_REDBAG_UNTIMEOUT;
//                }
//                try {
//                    list = new ArrayList<RedBagUntimeout>();
//                    JSONObject obj = new JSONObject(responseInfo.result);
//                    JSONObject redbagObj = obj.getJSONObject("data");
//                    JSONArray redbagArrs = redbagObj.getJSONArray("rows");
//                    for (int i = 0; i < redbagArrs.length(); i++) {
//                        JSONObject redbagArr = redbagArrs.getJSONObject(i);
//                        RedBagUntimeout untimeout = new RedBagUntimeout();
//                        untimeout.setAmount(redbagArr.optInt("amount"));
//                        untimeout.setMin_amount(redbagArr.optInt("min_amount"));
//                        untimeout.setCode(redbagArr.optString("code"));
//                        list.add(untimeout);
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                msg.obj = list;
//                handler.sendMessage(msg);
//            }
//
//            @Override
//            public void onFailure(HttpException e, String s) {
//                handler.sendEmptyMessage(DataConstants.NETWORK_FAILURE);
//            }
//        });
    }

    //立即下单
    public static void nowConfirmOrder(String rrid, String addbook_id, String is_nowbuy, String summary, String transfer_time, String bonus_code, final Handler handler) {
        ClientDiscoverAPI.nowConfirmOrder(rrid, addbook_id, is_nowbuy, summary, transfer_time, bonus_code, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Message msg = handler.obtainMessage();
                msg.what = DataConstants.NOW_CONFIRM_ORDER;
                NowConfirmBean nowConfirmBean = new NowConfirmBean();
                try {
                    JSONObject job = new JSONObject(responseInfo.result);
                    nowConfirmBean.setSuccess(job.optBoolean("success"));
                    nowConfirmBean.setMessage(job.optString("message"));
                    JSONObject data = job.getJSONObject("data");
                    if (nowConfirmBean.isSuccess()) {
                        nowConfirmBean.setRid(data.optString("rid"));
                        nowConfirmBean.setPay_money(data.optString("pay_money"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                msg.obj = nowConfirmBean;
                handler.sendMessage(msg);
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                handler.sendEmptyMessage(DataConstants.NETWORK_FAILURE);
            }
        });
    }

    //购物车
    public static void shopCartParser(final Handler handler) {
        ClientDiscoverAPI.shopCartNet(new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                List<ShopCart> list = null;
                Message msg = new Message();
                msg.what = DataConstants.PARSER_SHOP_CART;
                try {
                    list = new ArrayList<ShopCart>();
                    JSONObject obj = new JSONObject(responseInfo.result);
                    JSONObject shopCartObj = obj.getJSONObject("data");
                    ShopCart shopCart = new ShopCart();
                    shopCart.set_id(shopCartObj.optString("_id"));
                    shopCart.setTotal_price(shopCartObj.optString("total_price"));

                    List<ShopCartItem> itemList = new ArrayList<ShopCartItem>();
                    JSONArray shopCartArrs = shopCartObj.getJSONArray("items");
                    for (int i = 0; i < shopCartArrs.length(); i++) {
                        JSONObject shopCartArr = shopCartArrs.getJSONObject(i);
                        ShopCartItem shopCartItem = new ShopCartItem();
                        shopCartItem.setTotal_price(shopCartArr.optString("total_price"));
                        shopCartItem.setCover(shopCartArr.optString("cover"));
                        shopCartItem.setN(shopCartArr.optString("n"));
                        shopCartItem.setTarget_id(shopCartArr.optString("target_id"));
                        shopCartItem.setProduct_id(shopCartArr.optString("product_id"));
                        shopCartItem.setType(shopCartArr.optString("type"));
                        shopCartItem.setSku_mode(shopCartArr.optString("sku_mode"));
                        shopCartItem.setTitle(shopCartArr.optString("title"));
                        itemList.add(shopCartItem);
                    }
                    shopCart.setShopCartItemList(itemList);
                    list.add(shopCart);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                msg.obj = list;
                handler.sendMessage(msg);
            }

            @Override
            public void onFailure(HttpException e, String s) {
                handler.sendEmptyMessage(DataConstants.NETWORK_FAILURE);
            }
        });
    }

    //购物车数量
    public static void shopCartNumberParser(final Handler handler) {
        ClientDiscoverAPI.shopCartNumberNet(new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                ShopCartNumber shopCartNumber = null;
                Message msg = new Message();
                msg.what = DataConstants.PARSER_SHOP_CART_NUMBER;
                try {
                    shopCartNumber = new ShopCartNumber();
                    JSONObject obj = new JSONObject(responseInfo.result);
                    shopCartNumber.setSuccess(obj.optBoolean("success"));
                    JSONObject cartNumberObj = obj.getJSONObject("data");
                    shopCartNumber.setCount(cartNumberObj.optString("count"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                msg.obj = shopCartNumber;
                handler.sendMessage(msg);
            }

            @Override
            public void onFailure(HttpException e, String s) {
                handler.sendEmptyMessage(DataConstants.NETWORK_FAILURE);
            }
        });
    }

    //购物车结算获取数据传给确认订单界面
    public static void shopCartCalculateParser(String array, final Handler handler) {
        ClientDiscoverAPI.calculateShopCartNet(array, new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        List<CartDoOrder> list = null;
                        Message msg = new Message();
                        msg.what = DataConstants.PARSER_SHOP_CART_CALCULATE;
                        try {
                            list = new ArrayList<CartDoOrder>();
                            JSONObject obj = new JSONObject(responseInfo.result);
                            JSONObject cartDoOrderObjs = obj.getJSONObject("data");
                            CartDoOrder cartDoOrder = new CartDoOrder();
                            cartDoOrder.setSuccess(obj.optString("success"));
                            cartDoOrder.setIs_nowbuy(cartDoOrderObjs.optString("is_nowbuy"));
                            cartDoOrder.setPay_money(cartDoOrderObjs.optString("pay_money"));
                            List<CartDoOrderBonus> bonusList = new ArrayList<CartDoOrderBonus>();
                            JSONArray bonusArray = cartDoOrderObjs.getJSONArray("bonus");
                            for (int j = 0; j < bonusArray.length(); j++) {
                                JSONObject bonusObj = bonusArray.getJSONObject(j);
                                CartDoOrderBonus cartDoOrderBonus = new CartDoOrderBonus();
                                cartDoOrderBonus.set__extend__(bonusObj.optString("__extend__"));
                                cartDoOrderBonus.setAmount(bonusObj.optString("amount"));
                                cartDoOrderBonus.setCode(bonusObj.optString("code"));
                                cartDoOrderBonus.setCreated_on(bonusObj.optString("created_on"));
                                cartDoOrderBonus.setExpired_at(bonusObj.optString("expired_at"));
                                cartDoOrderBonus.setExpired_label(bonusObj.optString("expired_label"));
                                cartDoOrderBonus.setGet_at(bonusObj.optString("get_at"));
                                cartDoOrderBonus.setMin_amount(bonusObj.optString("min_amount"));
                                cartDoOrderBonus.setOrder_rid(bonusObj.optString("order_rid"));
                                cartDoOrderBonus.setProduct_id(bonusObj.optString("product_id"));
                                cartDoOrderBonus.setStatus(bonusObj.optString("status"));
                                cartDoOrderBonus.setUpdated_on(bonusObj.optString("updated_on"));
                                cartDoOrderBonus.setUsed(bonusObj.optString("used"));
                                cartDoOrderBonus.setUsed_at(bonusObj.optString("used_at"));
                                cartDoOrderBonus.setUsed_by(bonusObj.optString("used_by"));
                                cartDoOrderBonus.setUser_id(bonusObj.optString("user_id"));
                                cartDoOrderBonus.setXname(bonusObj.optString("xname"));
                                bonusList.add(cartDoOrderBonus);
                            }
                            cartDoOrder.setBonus(bonusList);
                            JSONObject carDoOrderObj = cartDoOrderObjs.getJSONObject("order_info");
                            cartDoOrder.setRid(carDoOrderObj.optString("rid"));
                            cartDoOrder.set_id(carDoOrderObj.optString("_id"));
                            cartDoOrder.setCreated_on(carDoOrderObj.optString("created_on"));
                            cartDoOrder.setExpired(carDoOrderObj.optString("expired"));
                            cartDoOrder.setUpdated_on(carDoOrderObj.optString("updated_on"));
                            cartDoOrder.setUser_id(carDoOrderObj.optString("user_id"));
                            cartDoOrder.setIs_cart(carDoOrderObj.optString("is_cart"));

                            List<CartOrderContent> contentList = new ArrayList<CartOrderContent>();
                            JSONObject contentObjs = carDoOrderObj.getJSONObject("dict");
                            CartOrderContent cartOrderContent = new CartOrderContent();
                            cartOrderContent.setCard_money(contentObjs.optString("card_money"));
                            cartOrderContent.setCoin_money(contentObjs.optString("coin_money"));
                            cartOrderContent.setFreight(contentObjs.optString("freight"));
                            cartOrderContent.setInvoice_caty(contentObjs.optString("invoice_caty"));
                            cartOrderContent.setInvoice_type(contentObjs.optString("invoice_type"));
                            cartOrderContent.setItems_count(contentObjs.optString("items_count"));
                            cartOrderContent.setSummary(contentObjs.optString("summary"));
                            cartOrderContent.setPayment_method(contentObjs.optString("payment_method"));
                            cartOrderContent.setTotal_money(contentObjs.optString("total_money"));
                            cartOrderContent.setTransfer(contentObjs.optString("transfer"));
                            cartOrderContent.setTransfer_time(contentObjs.optString("transfer_time"));
                            cartOrderContent.setInvoice_content(contentObjs.optString("invoice_content"));
                            contentList.add(cartOrderContent);
                            cartDoOrder.setCartOrderContents(contentList);

                            JSONArray itemArray = contentObjs.getJSONArray("items");
                            List<CartOrderContentItem> itemList = new ArrayList<CartOrderContentItem>();
                            for (int i = 0; i < itemArray.length(); i++) {
                                JSONObject itemObj = itemArray.getJSONObject(i);
                                CartOrderContentItem item = new CartOrderContentItem();
                                item.setCover(itemObj.optString("cover"));
                                item.setPrice(itemObj.optString("price"));
                                item.setProduct_id(itemObj.optString("product_id"));
                                item.setQuantity(itemObj.optString("quantity"));
                                item.setSale_price(itemObj.optString("sale_price"));
                                item.setSku(itemObj.optString("sku"));
                                item.setSku_mode(itemObj.optString("sku_mode"));
                                item.setSubtotal(itemObj.optString("subtotal"));
                                item.setTarget_id(itemObj.optString("target_id"));
                                item.setTitle(itemObj.optString("title"));
                                item.setType(itemObj.optString("type"));
                                item.setView_url(itemObj.optString("view_url"));
                                itemList.add(item);
                            }
                            cartDoOrder.setCartOrderContentItems(itemList);
                            list.add(cartDoOrder);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        msg.obj = list;
                        handler.sendMessage(msg);
                    }

                    @Override
                    public void onFailure(HttpException e, String s) {
                        handler.sendEmptyMessage(DataConstants.NETWORK_FAILURE);
                    }
                }
        );
    }

    //取消点赞
    public static void parserCancelLove(String id, String type, final Handler handler) {
        ClientDiscoverAPI.cancelLoveNet(id, type, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Message msg = handler.obtainMessage();
                msg.what = DataConstants.CANCEL_LOVE;
                try {
                    JSONObject job = new JSONObject(responseInfo.result);
                    msg.obj = job.optBoolean("success", false);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                handler.sendMessage(msg);

            }

            @Override
            public void onFailure(HttpException error, String msg) {
//                Toast.makeText(THNApplication.getContext(), "取消点赞失败", Toast.LENGTH_SHORT).show();
                handler.sendEmptyMessage(DataConstants.NETWORK_FAILURE);
            }
        });
    }

    //点赞
    public static void parserLove(String id, String type, final Handler handler) {
        ClientDiscoverAPI.loveNet(id, type, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Message msg = handler.obtainMessage();
                msg.what = DataConstants.LOVE;
                try {
                    JSONObject job = new JSONObject(responseInfo.result);
                    msg.obj = job.optBoolean("success", false);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                handler.sendMessage(msg);
            }

            @Override
            public void onFailure(HttpException error, String msg) {
//                Toast.makeText(THNApplication.getContext(), "点赞失败", Toast.LENGTH_SHORT).show();
                handler.sendEmptyMessage(DataConstants.NETWORK_FAILURE);
            }
        });
    }

    //添加购物车
    public static void addToCart(String target_id, String type, String n, final Handler handler) {
        ClientDiscoverAPI.addToCartNet(target_id, type, n, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Message msg = handler.obtainMessage();
                msg.what = DataConstants.ADD_TO_CART;
                msg.obj = new NetBean();
                try {
                    Gson gson = new Gson();
                    Type type1 = new TypeToken<NetBean>() {
                    }.getType();
                    msg.obj = gson.fromJson(responseInfo.result, type1);
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                }
                handler.sendMessage(msg);
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                handler.sendEmptyMessage(DataConstants.NETWORK_FAILURE);
            }
        });
    }

    //立即购买(验证数据并生成临时订单)
    public static void buyNow(String target_id, String type, String n, final Handler handler) {
        ClientDiscoverAPI.buyNow(target_id, type, n, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Message msg = handler.obtainMessage();
                msg.what = DataConstants.BUY_NOW;

                try {
                    JSONObject obj = new JSONObject(responseInfo.result);
                    JSONObject data = obj.getJSONObject("data");
                    NowBuyBean nowBuyBean = new NowBuyBean();
                    nowBuyBean.setSuccess(obj.optBoolean("success"));
                    nowBuyBean.setMessage(obj.optString("message"));
                    if (nowBuyBean.getSuccess()) {
                        nowBuyBean.setBonus_number(data.getJSONArray("bonus").length());
                        nowBuyBean.setIs_nowbuy(data.optString("is_nowbuy"));
                        nowBuyBean.setPay_money(data.optString("pay_money"));
                        JSONObject order_info = data.getJSONObject("order_info");
                        nowBuyBean.setRid(order_info.optString("rid"));
                        JSONObject dict = order_info.getJSONObject("dict");
                        nowBuyBean.setTransfer_time(dict.optString("transfer_time"));
                        nowBuyBean.setSummary(dict.optString("summary"));
                        nowBuyBean.setPayment_method(dict.optString("payment_method"));
                        JSONArray items = dict.getJSONArray("items");
                        List<NowBuyItemBean> itemList = new ArrayList<NowBuyItemBean>();
                        for (int i = 0; i < items.length(); i++) {
                            JSONObject job = items.getJSONObject(i);
                            NowBuyItemBean nowBuyItemBean = new NowBuyItemBean();
                            nowBuyItemBean.setCover(job.optString("cover"));
                            nowBuyItemBean.setType(job.optString("type"));
                            nowBuyItemBean.setTitle(job.optString("title"));
                            nowBuyItemBean.setSubtotal(job.optString("subtotal"));
                            nowBuyItemBean.setSku_name(job.optString("sku_name"));
                            nowBuyItemBean.setSku_mode(job.optString("sku_mode"));
                            nowBuyItemBean.setQuantity(job.optString("quantity"));
                            itemList.add(nowBuyItemBean);
                        }
                        nowBuyBean.setItemList(itemList);
                        nowBuyBean.set_id(order_info.optString("_id"));
                    }
                    msg.obj = nowBuyBean;

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                handler.sendMessage(msg);
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                handler.sendEmptyMessage(DataConstants.NETWORK_FAILURE);
            }
        });
    }

    //订单支付详情和订单详情都是这，发表评价界面的产品图片也从这获取
    public static void orderPayDetailsParser(String rid, final Handler handler) {
        ClientDiscoverAPI.OrderPayNet(rid, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
//                Log.e(">>>", ">>>OOO>>>" + responseInfo.result);
                List<OrderDetails> ordersList = null;
                Message msg = new Message();
                msg.what = DataConstants.PARSER_ORDER_DETAILS;
                try {
                    ordersList = new ArrayList<OrderDetails>();
                    JSONObject obj = new JSONObject(responseInfo.result);
                    JSONObject orderObj = obj.getJSONObject("data");
                    OrderDetails details = new OrderDetails();
                    details.setRid(orderObj.optString("rid"));
                    details.setExpress_company(orderObj.optString("express_company"));
                    details.setExpress_no(orderObj.optString("express_no"));
                    details.setCreated_at(orderObj.optString("created_at"));
                    details.setFreight(orderObj.optString("freight"));
                    details.setItems_count(orderObj.optString("items_count"));
                    details.setPay_money(orderObj.optString("pay_money"));
                    details.setPayment_method(orderObj.optString("payment_method"));
                    details.setTotal_money(orderObj.optString("total_money"));
                    details.setStatus(orderObj.optString("status"));
                    JSONObject arr = orderObj.getJSONObject("express_info");
                    List<OrderDetailsAddress> addressList = new ArrayList<OrderDetailsAddress>();
                    OrderDetailsAddress address = new OrderDetailsAddress();
                    address.setAddress(arr.optString("address"));
                    address.setName(arr.optString("name"));
                    address.setCity(arr.optString("city"));
                    address.setPhone(arr.optString("phone"));
                    address.setProvince(arr.optString("province"));
                    addressList.add(address);

                    details.setAddresses(addressList);
                    JSONArray productsArrays = orderObj.getJSONArray("items");
                    List<OrderDetailsProducts> productsList = new ArrayList<OrderDetailsProducts>();
                    for (int j = 0; j < productsArrays.length(); j++) {
                        JSONObject productsArr = productsArrays.getJSONObject(j);
                        OrderDetailsProducts products = new OrderDetailsProducts();
                        products.setName(productsArr.optString("name"));
                        products.setCover_url(productsArr.optString("cover_url"));
                        products.setPrice(productsArr.optString("price"));
                        products.setProduct_id(productsArr.optString("product_id"));
                        products.setQuantity(productsArr.optString("quantity"));
                        products.setSale_price(productsArr.optString("sale_price"));
                        products.setSku(productsArr.optString("sku"));
                        products.setSku_name(productsArr.optString("sku_name"));
                        productsList.add(products);
                    }
                    details.setProducts(productsList);
                    ordersList.add(details);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                msg.obj = ordersList;
                handler.sendMessage(msg);
            }

            @Override
            public void onFailure(HttpException e, String s) {
                handler.sendEmptyMessage(DataConstants.NETWORK_FAILURE);
            }
        });
    }

    //申请退款
    public static void applyForRefundParser(String rid, String option, String content, final Handler handler) {
        ClientDiscoverAPI.applyForRefundNet(rid, option, content, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Log.e("<<<申请退款", responseInfo.result);
                ApplyForRefund refund = null;
                Message msg = new Message();
                msg.what = DataConstants.PARSER_APPLY_REFUND;
                try {
                    JSONObject obj = new JSONObject(responseInfo.result);
                    JSONObject refundObj = obj.getJSONObject("data");
                    refund = new ApplyForRefund();
                    refund.setSuccess(obj.optString("success"));
                    refund.setMessage(obj.optString("message"));
                    refund.setRid(refundObj.optString("rid"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                msg.obj = refund;
                handler.sendMessage(msg);
            }

            @Override
            public void onFailure(HttpException e, String s) {
                handler.sendEmptyMessage(DataConstants.NETWORK_FAILURE);
            }
        });
    }

    //账户处的订单列表
    public static void orderListParser(String status, String page, String size, final Handler handler) {
        ClientDiscoverAPI.orderListNet(status, page, size, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                List<OrderEntity> list = null;
                Message msg = new Message();
                msg.what = DataConstants.PARSER_ORDER;
                try {
                    list = new ArrayList<OrderEntity>();
                    JSONObject obj = new JSONObject(responseInfo.result);
                    JSONObject orderObj = obj.getJSONObject("data");
                    JSONArray orderArrs = orderObj.getJSONArray("rows");
                    for (int i = 0; i < orderArrs.length(); i++) {
                        JSONObject orderArr = orderArrs.getJSONObject(i);
                        OrderEntity orderEntity = new OrderEntity();
                        orderEntity.setRid(orderArr.optString("rid"));
                        orderEntity.setItems_count(orderArr.optString("items_count"));
                        orderEntity.setTotal_money(orderArr.optString("total_money"));
                        orderEntity.setPay_money(orderArr.optString("pay_money"));
                        orderEntity.setFreight(orderArr.optString("freight"));
                        orderEntity.setStatus_label(orderArr.optString("status_label"));
                        orderEntity.setCreated_at(orderArr.optString("created_at"));
                        orderEntity.setStatus(orderArr.optString("status"));
                        JSONArray array = orderArr.getJSONArray("items");
                        List<OrderItem> itemList = new ArrayList<OrderItem>();
                        for (int j = 0; j < array.length(); j++) {
                            JSONObject arr = array.getJSONObject(j);
                            OrderItem item = new OrderItem();
                            item.setSku(arr.optString("sku"));
                            item.setProduct_id(arr.optString("product_id"));
                            item.setQuantity(arr.optString("quantity"));
                            item.setSale_price(arr.optString("sale_price"));
                            item.setName(arr.optString("name"));
                            item.setSku_name(arr.optString("sku_name"));
                            item.setCover_url(arr.optString("cover_url"));
                            itemList.add(item);
                        }
                        orderEntity.setOrderItem(itemList);
                        list.add(orderEntity);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                msg.obj = list;
                handler.sendMessage(msg);
            }

            @Override
            public void onFailure(HttpException e, String s) {
                handler.sendEmptyMessage(DataConstants.NETWORK_FAILURE);
            }
        });
    }

    //账户处的用户个人信息的解析
    public static void userInfoParser(final Handler handler) {
        ClientDiscoverAPI.userInfoNet(new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
//                Log.e("<<<",responseInfo.result);
                UserInfo userInfo = null;
                Message msg = new Message();
                msg.what = DataConstants.PARSER_USER_INFO;
                try {
                    JSONObject obj = new JSONObject(responseInfo.result);
                    JSONObject userObj = obj.getJSONObject("data");
                    userInfo = UserInfo.getInstance();
                    userInfo.setSuccess(obj.optBoolean("success"));
                    userInfo.setAccount(userObj.optString("account"));
                    userInfo.setNickname(userObj.optString("nickname"));
                    userInfo.setTrue_nickname(userObj.optString("true_nickname"));
                    userInfo.setSex(userObj.optString("sex"));
                    userInfo.setBirthday(userObj.optString("birthday"));
                    userInfo.setMedium_avatar_url(userObj.optString("medium_avatar_url"));
                    userInfo.setRealname(userObj.optString("realname"));
                    userInfo.setPhone(userObj.optString("phone"));
                    userInfo.setAddress(userObj.optString("address"));
                    userInfo.setZip(userObj.optString("zip"));
                    userInfo.setIm_qq(userObj.optString("im_qq"));
                    userInfo.setWeixin(userObj.optString("weixin"));
                    userInfo.setCompany(userObj.optString("company"));
                    userInfo.setProvince_id(userObj.optString("province_id"));
                    userInfo.setDistrict_id(userObj.optString("district_id"));
                    userInfo.setRank_id(userObj.optString("rank_id"));
                    userInfo.setRank_title(userObj.optString("rank_title"));
                    userInfo.setBird_coin(userObj.optString("bird_coin"));
                    userInfo.set_id(userObj.optString("_id"));
                    JSONObject counterObj = userObj.getJSONObject("counter");
                    userInfo.setOrder_wait_payment(counterObj.optString("order_wait_payment"));
                    userInfo.setOrder_ready_goods(counterObj.optString("order_ready_goods"));
                    userInfo.setOrder_evaluate(counterObj.optString("order_evaluate"));
                    userInfo.setOrder_total_count(counterObj.optString("order_total_count"));
                    userInfo.setOrder_sended_goods(counterObj.optString("order_sended_goods"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                msg.obj = userInfo;
                handler.sendMessage(msg);
            }

            @Override
            public void onFailure(HttpException e, String s) {
                handler.sendEmptyMessage(DataConstants.NETWORK_FAILURE);
            }
        });
    }

    //购物车中单个商品的库存（即最大加减数）
    public static void shopcartInventoryParser(final Handler handler) {
        ClientDiscoverAPI.shopcartInventoryNet(new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                List<ShopCartInventoryItemBean> list = null;
                Message msg = new Message();
                msg.what = DataConstants.PASER_SHOPCART_INVENTORY_ITEM;
                try {
                    list = new ArrayList<ShopCartInventoryItemBean>();
                    JSONObject obj = new JSONObject(responseInfo.result);
                    JSONObject jsonObj = obj.getJSONObject("data");
                    JSONArray jsonArray = jsonObj.getJSONArray("items");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        ShopCartInventoryItemBean bean = new ShopCartInventoryItemBean();
                        bean.setN(jsonObject.optString("n"));
                        bean.setProduct_id(jsonObject.optString("product_id"));
                        bean.setQuantity(jsonObject.optString("quantity"));
                        bean.setTarget_id(jsonObject.optString("target_id"));
                        bean.setType(jsonObject.optString("type"));
                        list.add(bean);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                msg.obj = list;
                handler.sendMessage(msg);
            }

            @Override
            public void onFailure(HttpException e, String s) {
                handler.sendEmptyMessage(DataConstants.NETWORK_FAILURE);
            }
        });
    }

    //最fiu伙伴
    public static void fiuUserList(String page, String size, String type, String sight_count, String sort, final Handler handler) {
        ClientDiscoverAPI.fiuUserList(page, size, type, sight_count, sort, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
//                Log.e("<<<fiu",responseInfo.result);
                Message msg = handler.obtainMessage();
                msg.what = DataConstants.FIU_USER;
                msg.obj = new FiuUserListBean();
                try {
                    Gson gson = new Gson();
                    Type type1 = new TypeToken<FiuUserListBean>() {
                    }.getType();
                    msg.obj = gson.fromJson(responseInfo.result, type1);
                } catch (JsonSyntaxException e) {
                    Log.e("<<<", "数据解析异常" + e.toString());
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

    //场景分享语境次数接口
    public static void commitShareCJ(String id, final Handler handler) {
        ClientDiscoverAPI.commitShareCJ(id, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Message msg = handler.obtainMessage();
                msg.what = DataConstants.SHARECJ;
                msg.obj = new NetBean();
                try {
                    Gson gson = new Gson();
                    Type type = new TypeToken<NetBean>() {
                    }.getType();
                    msg.obj = gson.fromJson(responseInfo.result, type);
                } catch (JsonSyntaxException e) {
                    Log.e("<<<场景分享语境", "数据解析异常");
                }
                handler.sendMessage(msg);
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                handler.sendEmptyMessage(DataConstants.NET_FAIL);
            }
        });
    }

}
