package com.taihuoniao.fineix.network;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.baidu.mapapi.model.LatLng;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.taihuoniao.fineix.base.HttpRequest;
import com.taihuoniao.fineix.beans.LoginInfo;
import com.taihuoniao.fineix.user.EditUserInfoActivity;
import com.taihuoniao.fineix.utils.Constants;
import com.taihuoniao.fineix.utils.LogUtil;
import com.taihuoniao.fineix.utils.MD5Utils;
import com.taihuoniao.fineix.utils.SPUtil;

/**
 * Created by android on 2015/12/27.
 * 参数设置
 */
public class ClientDiscoverAPI {

    //产品
    //删除用户添加的产品
    public static void deleteProduct(String id) {
        deleteProduct(id, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {

            }

            @Override
            public void onFailure(HttpException error, String msg) {

            }
        });
    }

    public static void deleteProduct(String id, RequestCallBack<String> callBack) {
        String url = URL.DELETE_PRODUCT;
        RequestParams params = getdeleteProductRequestParams(id);
        HttpHandler<String> httpHandler = HttpRequest.sign(params, url, callBack);
    }

    @NonNull
    public static RequestParams getdeleteProductRequestParams(String id) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("id", id);
        return params;
    }

    //产品
    //列表
    public static HttpHandler<String> getProductList(String title, String sort, String category_id, String brand_id, String category_tag_ids, String page, String size, String ids, String ignore_ids,
                                                     String stick, String fine, String stage, RequestCallBack<String> callBack) {
        String url = URL.URLSTRING_PRODUCTSLIST;
        RequestParams params = getgetProductListRequestParams(title, sort, category_id, brand_id, category_tag_ids, page, size, ids, ignore_ids, stick, fine, stage);
        HttpHandler<String> httpHandler = HttpRequest.sign(params, url, callBack);
        return httpHandler;
    }

    @NonNull
    public static RequestParams getgetProductListRequestParams(String title, String sort, String category_id, String brand_id, String category_tag_ids, String page, String size, String ids, String ignore_ids, String stick, String fine, String stage) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("title", title);
        params.addQueryStringParameter("sort", sort);
        params.addQueryStringParameter("category_id", category_id);
        params.addQueryStringParameter("brand_id", brand_id);
        params.addQueryStringParameter("category_tags", category_tag_ids);
        params.addQueryStringParameter("page", page);
        params.addQueryStringParameter("size", size);
        params.addQueryStringParameter("ids", ids);
        params.addQueryStringParameter("ignore_ids", ignore_ids);
        params.addQueryStringParameter("stick", stick);
        params.addQueryStringParameter("fine", fine);
        params.addQueryStringParameter("stage", stage);
        return params;
    }

    //产品
    //添加产品
    public static void addProduct(String attrbute, String oid, String sku_id, String title, String market_price, String sale_price,
                                  String link, String cover_url, String banners_url, RequestCallBack<String> callBack) {
        String url = URL.ADD_PRODUCT;
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("attrbute", attrbute);
        params.addQueryStringParameter("oid", oid);
        params.addQueryStringParameter("sku_id", sku_id);
        params.addQueryStringParameter("title", title);
        params.addQueryStringParameter("market_price", market_price);
        params.addQueryStringParameter("sale_price", sale_price);
        params.addQueryStringParameter("link", link);
        params.addQueryStringParameter("cover_url", cover_url);
        params.addQueryStringParameter("banners_url", banners_url);
        HttpHandler<String> httpHandler = HttpRequest.sign(params, url, callBack);
    }

    //产品
    //添加产品
    public static HttpHandler<String> addProduct(String title, String brand_id, RequestCallBack<String> callBack) {
        String url = URL.ADD_PRODUCT;
        RequestParams params = getaddProductRequestParams(title, brand_id);
        HttpHandler<String> httpHandler = HttpRequest.sign(params, url, callBack);
        return httpHandler;
    }

    @NonNull
    public static RequestParams getaddProductRequestParams(String title, String brand_id) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("title", title);
        params.addQueryStringParameter("brand_id", brand_id);
        return params;
    }

    //产品
    //产品详情
    public static HttpHandler<String> goodsDetails(String id, RequestCallBack<String> callBack) {
        String url = URL.GOOD_DETAILS;
        RequestParams params = getgoodsDetailsRequestParams(id);
        HttpHandler<String> httpHandler = HttpRequest.sign(params, url, callBack);
        return httpHandler;
    }

    @NonNull
    public static RequestParams getgoodsDetailsRequestParams(String id) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("id", id);
        return params;
    }

    //产品
    //获取京东商品信息
    public static void getJDProductsData(String ids, RequestCallBack<String> callBack) {
        String url = URL.URLSTRING_JD_PRODUCTSDATA;
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("ids", ids);
        HttpHandler<String> httpHandler = HttpRequest.sign(params, url, callBack);
    }

    //产品
    //获取淘宝商品详情
    public static void getTBProductsData(String ids, RequestCallBack<String> callBack) {
        String url = URL.URLSTRING_TB_PRODUCTSDATA;
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("ids", ids);
        HttpHandler<String> httpHandler = HttpRequest.sign(params, url, callBack);
    }

    //情景
    //点赞，订阅，收藏，关注列表
    public static void commonList(String page, String size, String id, String user_id, String type, String event, RequestCallBack<String> callBack) {
        String url = URL.COMMON_LISTS;
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("page", page);
        params.addQueryStringParameter("size", size);
        params.addQueryStringParameter("id", id);
        params.addQueryStringParameter("user_id", user_id);
        params.addQueryStringParameter("type", type);
        params.addQueryStringParameter("event", event);
        HttpHandler<String> httpHandler = HttpRequest.sign(params, url, callBack);
    }


    /**
     * 赞过的情境
     *
     * @param page
     * @param type
     * @param event
     * @param callBack
     */
    public static void getSupportQJ(String page, String type, String event, RequestCallBack<String> callBack) {
        RequestParams params = getgetSupportQJRequestParams(page, type, event);
        HttpHandler<String> httpHandler = HttpRequest.sign(params, URL.FAVORITE_GET_NEW_LIST, callBack);
    }

    @NonNull
    public static RequestParams getgetSupportQJRequestParams(String page, String type, String event) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("page", page);
        params.addQueryStringParameter("size", Constants.PAGE_SIZE);
        params.addQueryStringParameter("user_id", LoginInfo.getUserId() + "");
        params.addQueryStringParameter("type", type);
        params.addQueryStringParameter("event", event);
        return params;
    }

    //情景
    //情景订阅
    public static void subsQingjing(String id, RequestCallBack<String> callBack) {
        String url = URL.SUBS_QINGJING;
        RequestParams params = getsubsQingjingRequestParams(id);
        HttpHandler<String> httpHandler = HttpRequest.sign(params, url, callBack);
    }

    @NonNull
    public static RequestParams getsubsQingjingRequestParams(String id) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("id", id);
        return params;
    }

    //情景
    //取消情景订阅
    public static void cancelSubsQingjing(String id, RequestCallBack<String> callBack) {
        String url = URL.CANCEL_SUBS_QINGJING;
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("id", id);
        HttpHandler<String> httpHandler = HttpRequest.sign(params, url, callBack);
    }

    //情景
    //情景新增
    public static void createQingjing(String id, String title, String des, String tags, String address, String tmp, String lat, String lng, RequestCallBack<String> callBack) {
        String url = URL.CREATE_QINGJING;
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("id", id);
        params.addQueryStringParameter("title", title);
        params.addQueryStringParameter("des", des);
        params.addQueryStringParameter("tags", tags);
        params.addQueryStringParameter("address", address);
        params.addQueryStringParameter("tmp", tmp);
        params.addQueryStringParameter("lat", lat);
        params.addQueryStringParameter("lng", lng);
        HttpHandler<String> httpHandler = HttpRequest.sign(params, url, callBack);
    }

    //情景
    //删除情景
    public static void deleteQingjing(String id, RequestCallBack<String> callBack) {
        String url = URL.DELETE_QINGJING;
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("id", id);
        HttpHandler<String> httpHandler = HttpRequest.sign(params, url, callBack);
    }

    //情景
    //情景详情
    public static void qingjingDetails(String id, RequestCallBack<String> callBack) {
        String url = URL.QINGJING_DETAILS;
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("id", id);
        HttpHandler<String> httpHandler = HttpRequest.sign(params, url, callBack);
    }


    //情景
    //列表数据
    public static void qingjingList(String page, String category_id, String sort, String fine, String dis, String lng, String lat, RequestCallBack<String> callBack) {
        String url = URL.QINGJING_LISTS;
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("page", page);
        params.addQueryStringParameter("category_id", category_id);
        params.addQueryStringParameter("size", 10 + "");
        params.addQueryStringParameter("sort", sort);
        params.addQueryStringParameter("fine", fine);
        params.addQueryStringParameter("dis", 10000 + "");
        params.addQueryStringParameter("lng", lng);
        params.addQueryStringParameter("lat", lat);
        HttpHandler<String> httpHandler = HttpRequest.sign(params, url, callBack);
    }

    //场景
    //新增情景
    public static HttpHandler<String> createScene(String id, String title, String des, String scene_id, String tags,
                                                  String products, String address, String city, String tmp, String lat, String lng,
                                                  String subject_ids, RequestCallBack<String> callBack) {
        String url = URL.CREATE_SCENE;
        RequestParams params = getcreateSceneRequestParams(id, title, des, scene_id, tags, products, address, city, tmp, lat, lng, subject_ids);
        HttpHandler<String> httpHandler = HttpRequest.sign(params, url, callBack);
        return httpHandler;
    }

    @NonNull
    public static RequestParams getcreateSceneRequestParams(String id, String title, String des, String scene_id, String tags, String products, String address, String city, String tmp, String lat, String lng, String subject_ids) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("id", id);
        params.addQueryStringParameter("tmp", tmp);
        params.addQueryStringParameter("title", title);
        params.addQueryStringParameter("des", des);
        params.addQueryStringParameter("city", city);
        params.addQueryStringParameter("scene_id", scene_id);
        params.addQueryStringParameter("tags", tags);
        params.addQueryStringParameter("products", products);
        params.addQueryStringParameter("address", address);
        params.addQueryStringParameter("lat", lat);
        params.addQueryStringParameter("lng", lng);
        params.addQueryStringParameter("subject_ids", subject_ids);
        return params;
    }

    //场景
    //删除场景
    public static HttpHandler<String> deleteScene(String id, RequestCallBack<String> callBack) {
        String url = URL.DELETE_SCENE;
        RequestParams params = getdeleteSceneRequestParams(id);
        HttpHandler<String> httpHandler = HttpRequest.sign(params, url, callBack);
        return httpHandler;
    }

    @NonNull
    public static RequestParams getdeleteSceneRequestParams(String id) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("id", id);
        return params;
    }

    //情境
    //情境点赞
    public static HttpHandler<String> loveQJ(String id, RequestCallBack<String> callBack) {
        String url = URL.LOVE_SCENE;
        RequestParams params = getloveQJRequestParams(id);
        HttpHandler<String> httpHandler = HttpRequest.sign(params, url, callBack);
        return httpHandler;
    }

    @NonNull
    public static RequestParams getloveQJRequestParams(String id) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("id", id);
        return params;
    }

    //情境
    //取消情境点赞
    public static HttpHandler<String> cancelLoveQJ(String id, RequestCallBack<String> callBack) {
        String url = URL.CANCEL_LOVE_SCENE;
        RequestParams params = getcancelLoveQJRequestParams(id);
        HttpHandler<String> httpHandler = HttpRequest.sign(params, url, callBack);
        return httpHandler;
    }

    @NonNull
    public static RequestParams getcancelLoveQJRequestParams(String id) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("id", id);
        return params;
    }

    //场景
    //场景详情
    public static HttpHandler<String> sceneDetails(String id, RequestCallBack<String> callBack) {
        String url = URL.SCENE_DETAILS;
        RequestParams params = getsceneDetailsRequestParams(id);
        HttpHandler<String> httpHandler = HttpRequest.sign(params, url, callBack);
        return httpHandler;
    }

    @NonNull
    public static RequestParams getsceneDetailsRequestParams(String id) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("id", id);
        return params;
    }

    //场景
    //列表数据
    public static HttpHandler<String> getSceneList(String page, String size, String scene_id, String category_ids, String sort, String fine, String dis, String lng, String lat, RequestCallBack<String> callBack) {
        String url = URL.SCENE_LIST;
        RequestParams params = getSceneListRequestParams(page, size, scene_id, category_ids, sort, fine, lng, lat);
        HttpHandler<String> httpHandler = HttpRequest.sign(params, url, callBack);
        return httpHandler;
    }

    @NonNull
    public static RequestParams getSceneListRequestParams(String page, String size, String scene_id, String category_ids, String sort, String fine, String lng, String lat) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("page", page);
        params.addQueryStringParameter("size", size);
        params.addQueryStringParameter("scene_id", scene_id);
        params.addQueryStringParameter("category_ids", category_ids);
        params.addQueryStringParameter("sort", sort);
        params.addQueryStringParameter("fine", fine);
        params.addQueryStringParameter("dis", 10000 + "");
        params.addQueryStringParameter("lng", lng);
        params.addQueryStringParameter("lat", lat);
        return params;
    }

//    public static void getSceneList(LatLng ll, String page, String size, String dis, RequestCallBack<String> callBack) {
//        getSceneList(page, size, null, null, null, null, dis, String.valueOf(ll.longitude), String.valueOf(ll.latitude), callBack);
//    }


//    /**
//     * 获取订阅情境的列表
//     *
//     * @param page
//     * @param category_ids
//     * @param callBack
//     */
//    public static void getQJList(String page, String category_ids, RequestCallBack<String> callBack) {
//        String url = URL.SCENE_LIST;
//        RequestParams params = getQJListRequestParams(page, category_ids);
//        HttpHandler<String> httpHandler = HttpRequest.sign(params, url, callBack);
//    }

    @NonNull
    public static RequestParams getQJListRequestParams(String page, String category_ids) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("page", page);
        params.addQueryStringParameter("size", Constants.PAGE_SIZE);
        params.addQueryStringParameter("category_ids", category_ids);
        return params;
    }

//    /**
//     * 首页精选情境
//     * 参与的情境
//     *
//     * @param subject_id
//     * @param callBack
//     */
//    public static void participateActivity(String page, String subject_id, RequestCallBack<String> callBack) {
//        String url = URL.SCENE_LIST;
//        RequestParams params = getparticipateActivityRequestParams(page, subject_id);
//        HttpHandler<String> httpHandler = HttpRequest.sign(params, url, callBack);
//    }

    @NonNull
    public static RequestParams getparticipateActivityRequestParams(String page, String subject_id) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("page", page);
        params.addQueryStringParameter("size", Constants.PAGE_SIZE);
        params.addQueryStringParameter("subject_id", subject_id);
        params.addQueryStringParameter("use_cache", "1");
        return params;
    }

    /**
     * 根据用户ID查找情境
     *
     * @param page
     * @param size
     * @param userId
     * @param callBack
     */
//    public static void getSceneList(String page, String size, String userId,String show_all,RequestCallBack<String> callBack) {
//        String url = URL.SCENE_LIST;
//        RequestParams params = getSceneListRequestParams(page, size, userId, show_all);
//        HttpHandler<String> httpHandler = HttpRequest.sign(params, url, callBack);
//    }

    @NonNull
    public static RequestParams getSceneListRequestParams(String page, String size, String userId, String show_all) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("page", page);
        params.addQueryStringParameter("size", size);
        params.addQueryStringParameter("user_id", userId);
        params.addQueryStringParameter("show_all", show_all);
        return params;
    }

    //个人中心
    //用户列表
    public static void userList(String page, String size, String sort, String has_scene, RequestCallBack<String> callBack) {
        String url = URL.USER_LIST;
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("page", page);
        params.addQueryStringParameter("size", size);
        params.addQueryStringParameter("sort", sort);
        params.addQueryStringParameter("has_scene", has_scene);
        HttpHandler<String> httpHandler = HttpRequest.sign(params, url, callBack);
    }

    //标签
    //使用过的标签
    public static HttpHandler<String> usedLabelList(RequestCallBack<String> callBack) {
        String url = URL.USED_LABEL_LIST;
        RequestParams params = getusedLabelListRequestParams();
        HttpHandler<String> httpHandler = HttpRequest.sign(params, url, callBack);
        return httpHandler;
    }

    @NonNull
    public static RequestParams getusedLabelListRequestParams() {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("type", 1 + "");
        return params;
    }

    //标签
    //标签列表
    public static void labelList(String parent_id, int page, String size, int sort, int is_hot, RequestCallBack<String> callBack) {
        String url = URL.LABEL_LIST;
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("parent_id", parent_id);
        params.addQueryStringParameter("page", page + "");
        params.addQueryStringParameter("sort", sort + "");
        params.addQueryStringParameter("size", size);
        params.addQueryStringParameter("is_hot", is_hot + "");
        HttpHandler<String> httpHandler = HttpRequest.sign(params, url, callBack);
    }

    //标签
    //场景页热门标签
    public static void cjHotLabel(boolean isCJ, RequestCallBack<String> callBack) {
        String url;
        if (isCJ) {
            url = URL.CJ_HOT_LABEL;
        } else {
            url = URL.PRODUCT_HOT_LABEL;
        }
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        HttpHandler<String> httpHandler = HttpRequest.sign(params, url, callBack);
    }

    //标签
    //热门标签
    public static void hotLabelList(String page, RequestCallBack<String> callBack) {
        String url = URL.HOT_LABEL_LIST;
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("page", page);
        HttpHandler<String> httpHandler = HttpRequest.sign(params, url, callBack);
    }

    //收货地址
    //获取省市列表
    public static HttpHandler<String> getProvinceList(RequestCallBack<String> callBack) {
        String url = URL.URLSTRING_PROVINCE_CITIES;
        RequestParams params = getProvinceListRequestParams();
        HttpHandler<String> httpHandler = HttpRequest.sign(params, url, callBack);
        return httpHandler;
    }

    @NonNull
    public static RequestParams getProvinceListRequestParams() {
        return new RequestParams(ConstantCfg.CHARSET);
    }

    //获取用户默认收货地址
    public static HttpHandler<String> getDefaultAddressNet(RequestCallBack<String> callBack) {
        String url = URL.URLSTRING_DEFAULT_ADDRESS;
        RequestParams params = getgetDefaultAddressNetRequestParams();
        HttpHandler<String> httpHandler = HttpRequest.sign(params, url, callBack);
        return httpHandler;
    }

    @NonNull
    public static RequestParams getgetDefaultAddressNetRequestParams() {
        return new RequestParams(ConstantCfg.CHARSET);
    }

    //收货地址
    //获取用户收货地址列表
    public static void getAddressList(String page, RequestCallBack<String> callBack) {
        String url = URL.URLSTRING_ADDRESS_LISTS;
        RequestParams params = getgetAddressListRequestParams(page);
        HttpHandler<String> httpHandler = HttpRequest.sign(params, url, callBack);
//        NetworkManager.getInstance().add("getAddressList", httpHandler);
    }

    @NonNull
    public static RequestParams getgetAddressListRequestParams(String page) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("page", page);
        return params;
    }

    //收货地址
    //提交正在编辑的收货地址
    public static HttpHandler<String> commitAddressNet(String id, String name, String phone, String province_id, String city_id,String county_id,String town_id,String address, String zip, String is_default, RequestCallBack<String> callBack) {
        String url = URL.URLSTRING_NEW_ADDRESS;
        RequestParams params = getcommitAddressNetRequestParams(id, name, phone, province_id, city_id, county_id, town_id, address, zip, is_default);
        HttpHandler<String> httpHandler = HttpRequest.sign(params, url, callBack);
        return httpHandler;
    }

    @NonNull
    public static RequestParams getcommitAddressNetRequestParams(String id, String name, String phone, String province_id, String city_id, String county_id, String town_id, String address, String zip, String is_default) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("id", id);
        params.addQueryStringParameter("name", name);
        params.addQueryStringParameter("phone", phone);
        params.addQueryStringParameter("province_id", province_id);
        params.addQueryStringParameter("city_id", city_id);
        params.addQueryStringParameter("county_id", county_id);
        params.addQueryStringParameter("town_id", town_id);
        params.addQueryStringParameter("address", address);
        params.addQueryStringParameter("zip", zip);
        params.addQueryStringParameter("is_default", is_default);
        return params;
    }

//    //收货地址
//    //删除某个收货地址
//    public static HttpHandler<String> deleteAddressNet(String id, RequestCallBack<String> callBack) {
//        String url = URL.URLSTRING_DELETE_ADDRESS;
//        RequestParams params = getdeleteAddressNetRequestParams(id);
//        HttpHandler<String> httpHandler = HttpRequest.sign(params, url, callBack);
//        return httpHandler;
////        NetworkManager.getInstance().add("deleteAddress", httpHandler);
//    }

    @NonNull
    public static RequestParams getdeleteAddressNetRequestParams(String id) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("id", id);
        return params;
    }

//    //公共
//    //举报
//    public static HttpHandler<String> report(String target_id, String type, String evt, RequestCallBack<String> callBack) {
//        String url = URL.REPORT;
//        RequestParams params = getreportRequestParams(target_id, type, evt);
//        HttpHandler<String> httpHandler = HttpRequest.sign(params, url, callBack);
//        return httpHandler;
//    }

    @NonNull
    public static RequestParams getreportRequestParams(String target_id, String type, String evt) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("target_id", target_id);
        params.addQueryStringParameter("type", type);
        params.addQueryStringParameter("evt", evt);
        params.addQueryStringParameter("application", 3 + "");
        params.addQueryStringParameter("from_to", 4 + "");
        return params;
    }

//    //公共
//    //品牌详情
//    public static HttpHandler<String> brandDetail(String id, RequestCallBack<String> callBack) {
//        String url = URL.BRAND_DETAIL;
//        RequestParams params = getbrandDetailRequestParams(id);
//        HttpHandler<String> httpHandler = HttpRequest.sign(params, url, callBack);
//        return httpHandler;
//    }

    @NonNull
    public static RequestParams getbrandDetailRequestParams(String id) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("id", id);
        return params;
    }

//    //公共
//    //分类列表
//    public static HttpHandler<String> categoryList(String page, String domain, String show_all, RequestCallBack<String> callBack) {
//        String url = URL.CATEGORY_LIST;
//        RequestParams params = getcategoryListRequestParams(page, domain, show_all);
//        HttpHandler<String> httpHandler = HttpRequest.sign(params, url, callBack);
//        return httpHandler;
//    }

    @NonNull
    public static RequestParams getcategoryListRequestParams(String page, String domain, String show_all) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("page", page);
        params.addQueryStringParameter("size", 300 + "");
        params.addQueryStringParameter("show_all", show_all);
        params.addQueryStringParameter("domain", domain);
        params.addQueryStringParameter("use_cache", "1");
        return params;
    }


//    /**
//     * 获得情景主题
//     *
//     * @param callBack
//     */
//    public static void categoryList(RequestCallBack<String> callBack) {
//        String url = URL.CATEGORY_LIST;
//        RequestParams params = getcategoryListRequestParams();
//        HttpHandler<String> httpHandler = HttpRequest.sign(params, url, callBack);
//    }

    @NonNull
    public static RequestParams getcategoryListRequestParams() {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("page", "1");
        params.addQueryStringParameter("size", "10");
        params.addQueryStringParameter("domain", "13");//情景主题
        params.addQueryStringParameter("use_cache", "1");
        return params;
    }
//
//    //公共
//    //搜索列表
//    public static HttpHandler<String> search(String q, String t, String cid, String page, String size, String evt, String sort, RequestCallBack<String> callBack) {
//        String url = URL.SEARCH;
//        RequestParams params = getsearchRequestParams(q, t, cid, page, size, evt, sort);
//        HttpHandler<String> httpHandler = HttpRequest.sign(params, url, callBack);
//        return httpHandler;
//    }

    @NonNull
    public static RequestParams getsearchRequestParams(String q, String t, String cid, String page, String size, String evt, String sort) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("q", q);
        params.addQueryStringParameter("t", t);
        params.addQueryStringParameter("tid", cid);
        params.addQueryStringParameter("page", page);
        params.addQueryStringParameter("evt", evt);
        params.addQueryStringParameter("sort", sort);
        params.addQueryStringParameter("size", size);
        return params;
    }

/*    public static void searchUser(String q, String page, RequestCallBack<String> callBack) {
        String url = URL.SEARCH;
        RequestParams params = getsearchUserRequestParams(q, page);
        HttpHandler<String> httpHandler = HttpRequest.sign(params, url, callBack);
    }*/

    @NonNull
    public static RequestParams getsearchUserRequestParams(String q, String page) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("q", q);
        params.addQueryStringParameter("t", "14"); //14.用户
        params.addQueryStringParameter("page", page);
        params.addQueryStringParameter("size", Constants.PAGE_SIZE);
        return params;
    }

//    //公共
//    //商品和场景关联列表
//    public static HttpHandler<String> productAndScene(String page, String size, String sight_id, String product_id, String brand_id, RequestCallBack<String> callBack) {
//        String url = URL.PRODUCT_AND_SCENELIST;
//        RequestParams params = getproductAndSceneRequestParams(page, size, sight_id, product_id, brand_id);
//        return HttpRequest.sign(params, url, callBack);
//    }

    @NonNull
    public static RequestParams getproductAndSceneRequestParams(String page, String size, String sight_id, String product_id, String brand_id) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("page", page);
        params.addQueryStringParameter("size", size);
        params.addQueryStringParameter("sight_id", sight_id);
        params.addQueryStringParameter("product_id", product_id);
        params.addQueryStringParameter("brand_id", brand_id);
        return params;
    }

//    //购物车
//    //商品数量
//    public static HttpHandler<String> cartNum(RequestCallBack<String> callBack) {
//        String url = URL.CART_NUMBER;
//        RequestParams params = getcartNumRequestParams();
//        HttpHandler<String> httpHandler = HttpRequest.sign(params, url, callBack);
//        return httpHandler;
//    }

    @NonNull
    public static RequestParams getcartNumRequestParams() {
        return new RequestParams(ConstantCfg.CHARSET);
    }

//    //评论
//    //提交评论
//    public static HttpHandler<String> sendComment(String target_id, String content, String type, String target_user_id, String is_reply, String reply_id, String reply_user_id, RequestCallBack<String> callBack) {
//        String url = URL.SEND_COMMENT;
//        RequestParams params = getsendCommentRequestParams(target_id, content, type, target_user_id, is_reply, reply_id, reply_user_id);
//        HttpHandler<String> httpHandler = HttpRequest.sign(params, url, callBack);
//        return httpHandler;
//    }

    @NonNull
    public static RequestParams getsendCommentRequestParams(String target_id, String content, String type, String target_user_id, String is_reply, String reply_id, String reply_user_id) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("target_id", target_id);
        params.addQueryStringParameter("content", content);
        params.addQueryStringParameter("target_user_id", target_user_id);
        params.addQueryStringParameter("is_reply", is_reply);
        params.addQueryStringParameter("reply_id", reply_id);
        params.addQueryStringParameter("reply_user_id", reply_user_id);
        params.addQueryStringParameter("type", type);
        params.addQueryStringParameter("from_site", 4 + "");
        return params;
    }

//    //评论
//    //列表
//    public static HttpHandler<String> commentsList(String page, String size, String target_id, String target_user_id, String type, RequestCallBack<String> callBack) {
//        String url = URL.COMMENTS_LIST;
//        RequestParams params = getcommentsListRequestParams(page, size, target_id, target_user_id, type);
//        HttpHandler<String> httpHandler = HttpRequest.sign(params, url, callBack);
//        return httpHandler;
//    }

    @NonNull
    public static RequestParams getcommentsListRequestParams(String page, String size, String target_id, String target_user_id, String type) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("page", page);
        params.addQueryStringParameter("size", size);
        params.addQueryStringParameter("sort", "1");
        params.addQueryStringParameter("target_id", target_id);
        params.addQueryStringParameter("target_user_id", target_user_id);
        params.addQueryStringParameter("type", type);
        return params;
    }

//    //评论
//    //列表
//    public static void mycommentsList(String page, String size, String target_id, String target_user_id, String type, RequestCallBack<String> callBack) {
//        String url = URL.MY_COMMENTS_LIST;
//        RequestParams params = getmycommentsListRequestParams(page, size, target_id, target_user_id, type);
//        HttpHandler<String> httpHandler = HttpRequest.sign(params, url, callBack);
//    }

    @NonNull
    public static RequestParams getmycommentsListRequestParams(String page, String size, String target_id, String target_user_id, String type) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("page", page);
        params.addQueryStringParameter("size", size);
        params.addQueryStringParameter("target_id", target_id);
        params.addQueryStringParameter("target_user_id", target_user_id);
        params.addQueryStringParameter("type", type);
        return params;
    }
//
//    //评论
//    //删除评论
//    public static HttpHandler<String> deleteComment(String id, RequestCallBack<String> callBack) {
//        String url = URL.DELETE_COMMENT;
//        RequestParams params = getdeleteCommentRequestParams(id);
//        HttpHandler<String> httpHandler = HttpRequest.sign(params, url, callBack);
//        return httpHandler;
//    }

    @NonNull
    public static RequestParams getdeleteCommentRequestParams(String id) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("id", id);
        return params;
    }

    //评论列表
    //商品详情评论列表
    public static HttpHandler<String> getGoodsDetailsCommentsList(String target_id, String page, RequestCallBack<String> callBack) {
        String url = URL.COMMENTS_LIST;
        RequestParams params = getGoodsDetailsCommentsListRequestParams(target_id, page);
        HttpHandler<String> httpHandler = HttpRequest.sign(params, url, callBack);
        return httpHandler;
    }

    @NonNull
    public static RequestParams getGoodsDetailsCommentsListRequestParams(String target_id, String page) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("page", page);
        params.addQueryStringParameter("target_id", target_id);
        params.addQueryStringParameter("type", "4");
        return params;
    }

    //点击登录按钮
    public static void clickLoginNet(String phone, String password, RequestCallBack<String> callBack) {
        RequestParams params = getclickLoginNetRequestParams(phone, password);
        HttpHandler<String> httpHandler = HttpRequest.sign(params, URL.AUTH_LOGIN, callBack, false);
    }

    @NonNull
    public static RequestParams getclickLoginNetRequestParams(String phone, String password) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("mobile", phone);
        params.addQueryStringParameter("from_to", "2");     //登录渠道
        params.addQueryStringParameter("password", password);
        return params;
    }

    //第三方登录
    public static void thirdLoginNet(String oid, String access_token, String type, RequestCallBack<String> callBack) {
        RequestParams params = getthirdLoginNetRequestParams(oid, access_token, type);
        HttpHandler<String> httpHandler = HttpRequest.sign(params, URL.AUTH_THIRD_SIGN, callBack, false);
    }

    @NonNull
    public static RequestParams getthirdLoginNetRequestParams(String oid, String access_token, String type) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("oid", oid);
        params.addQueryStringParameter("access_token", access_token);
        params.addQueryStringParameter("type", type);
        params.addQueryStringParameter("from_to", "2");
        return params;
    }

    //注册及找回密码中的获取验证码
    public static void getVerifyCodeNet(RequestCallBack<String> callBack, String phone) {
        RequestParams params = getgetVerifyCodeNetRequestParams(phone);
        HttpHandler<String> httpHandler = HttpRequest.sign(params, URL.AUTH_VERIFY_CODE, callBack, false);
    }

    @NonNull
    public static RequestParams getgetVerifyCodeNetRequestParams(String phone) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("mobile", phone);
        return params;
    }

    //第三方登录之快捷注册(绑定手机号)
    public static void bindPhoneNet(String oid, String union_id, String access_token, String account, String password, String type, RequestCallBack<String> callBack) {
        RequestParams params = getbindPhoneNetRequestParams(oid, union_id, access_token, account, password, type);
        HttpHandler<String> httpHandler = HttpRequest.sign(params, URL.AUTH_THIRD_REGISTER_WITH_PHONE, callBack, false);
    }

    @NonNull
    public static RequestParams getbindPhoneNetRequestParams(String oid, String union_id, String access_token, String account, String password, String type) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
////        params.addQueryStringParameter("uuid", uuid);
        params.addQueryStringParameter("account", account);
        params.addQueryStringParameter("password", password);
        params.addQueryStringParameter("oid", oid);
        params.addQueryStringParameter("union_id", union_id);
        params.addQueryStringParameter("access_token", access_token);
        params.addQueryStringParameter("third_source", type);
        params.addQueryStringParameter("from_to", "2"); //Android
        return params;
    }

    //第三方登录之快捷注册(不绑定手机号)
    public static void skipBindNet(String oid, String union_id, String access_token, String nickname, String sex, String avatar_url, String type, RequestCallBack<String> callBack) {
        RequestParams params = getskipBindNetRequestParams(oid, union_id, access_token, nickname, sex, avatar_url, type);
        HttpHandler<String> httpHandler = HttpRequest.sign(params, URL.AUTH_THIRD_REGISTER_WITHOUT_PHONE, callBack, false);
    }

    @NonNull
    public static RequestParams getskipBindNetRequestParams(String oid, String union_id, String access_token, String nickname, String sex, String avatar_url, String type) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("nickname", nickname);
        params.addQueryStringParameter("sex", sex);
        params.addQueryStringParameter("oid", oid);
        params.addQueryStringParameter("union_id", union_id);
        params.addQueryStringParameter("avatar_url", avatar_url);
        params.addQueryStringParameter("access_token", access_token);
        params.addQueryStringParameter("third_source", type);
        params.addQueryStringParameter("from_to", "2");
        return params;
    }

    //stick
    public static void getQJData(LatLng ll, int radius, String page, String pageSize, String stick, RequestCallBack<String> callBack) {
        RequestParams params = getQJDataRequestParams(ll, radius, page, pageSize, stick);
//        HttpUtils httpUtils = new HttpUtils(URL.CONN_TIMEOUT);
//        HttpHandler<String> handler = httpUtils.send(HttpRequest.HttpMethod.POST, URL.QING_JING, params, callBack);
        HttpRequest.sign(params, URL.QING_JING, callBack);
    }

    @NonNull
    public static RequestParams getQJDataRequestParams(LatLng ll, int radius, String page, String pageSize, String stick) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("page", page);
        params.addQueryStringParameter("size", pageSize);
        params.addQueryStringParameter("sort", "0");
        params.addQueryStringParameter("stick", stick);
        if (radius > 0) {
            params.addQueryStringParameter("dis", String.valueOf(radius));
        }
        if (ll != null) {
            params.addQueryStringParameter("lat", String.valueOf(ll.latitude));
            params.addQueryStringParameter("lng", String.valueOf(ll.longitude));
        }
        return params;
    }

    /**
     * 获取用户的情景列表
     *
     * @param page
     * @param pageSize
     * @param userId
     * @param callBack
     */
    public static void getQJList(String page, String pageSize, String userId, RequestCallBack<String> callBack) {
        RequestParams params = getQJListRequestParams(page, pageSize, userId);
        HttpRequest.sign(params, URL.QING_JING, callBack);
    }

    @NonNull
    public static RequestParams getQJListRequestParams(String page, String pageSize, String userId) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("page", page);
        params.addQueryStringParameter("size", pageSize);
        params.addQueryStringParameter("sort", "0");
        params.addQueryStringParameter("stick", "0");
        params.addQueryStringParameter("user_id", userId);
        return params;
    }

    //找回忘记的密码
    public static void findPasswordNet(String phone, String newpassword, String code, RequestCallBack<String> callBack) {
        RequestParams params = getfindPasswordNetRequestParams(phone, newpassword, code);
        HttpHandler<String> httpHandler = HttpRequest.sign(params, URL.AUTH_FIND_PWD, callBack, false);
    }

    @NonNull
    public static RequestParams getfindPasswordNetRequestParams(String phone, String newpassword, String code) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("mobile", phone);
        params.addQueryStringParameter("password", newpassword);
        params.addQueryStringParameter("verify_code", code);
        return params;
    }


    //账户处的用户个人信息
    public static void getMineInfo(String userId, RequestCallBack<String> callBack) {
        RequestParams params = getgetMineInfoRequestParams(userId);
        HttpHandler<String> httpHandler = HttpRequest.sign(params, URL.MINE_INFO, callBack, false);
    }

    @NonNull
    public static RequestParams getgetMineInfoRequestParams(String userId) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("user_id", userId);
        LogUtil.e("getMineInfo", userId);
        return params;
    }

    //获取个人中心
    public static HttpHandler<String> getUserCenterData(RequestCallBack<String> callBack) {
        RequestParams params = getgetUserCenterDataRequestParams();
        HttpHandler<String> httpHandler = HttpRequest.sign(params, URL.USER_CENTER, callBack, false);
        return httpHandler;
    }

    @NonNull
    public static RequestParams getgetUserCenterDataRequestParams() {
        return new RequestParams(ConstantCfg.CHARSET);
    }

    /**
     * 获取Banner
     *
     * @param page_name
     * @param callBack
     */
    public static HttpHandler<String> getBanners(String page_name, RequestCallBack<String> callBack) {
        RequestParams params = getgetBannersRequestParams(page_name);
        return HttpRequest.sign(params, URL.BANNERS_URL, callBack, false);
    }

    @NonNull
    public static RequestParams getgetBannersRequestParams(String page_name) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("page", String.valueOf(1));
        params.addQueryStringParameter("size", String.valueOf(5));
        params.addQueryStringParameter("name", page_name);
        params.addQueryStringParameter("use_cache", "1");
        return params;
    }

    /**
     * 获取粉丝和可关注列表
     *
     * @param page
     * @param size
     * @param find_type
     * @param callBack
     */
    public static void getFocusFansList(String userId, String page, String size, String find_type, String clean_remind, RequestCallBack<String> callBack) {
        RequestParams params = getFocusFansListRequestParams(userId, page, size, find_type, clean_remind);
        HttpRequest.sign(params, URL.FOCUS_FAVORITE_URL, callBack, false);
    }

    @NonNull
    public static RequestParams getFocusFansListRequestParams(String userId, String page, String size, String find_type, String clean_remind) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("page", page);
        params.addQueryStringParameter("size", size);
//        params.addBodyParameter("user_id", LoginInfo.getInstance().getId()+"");
        params.addQueryStringParameter("user_id", userId);//924808
//        LogUtil.e("userId",LoginInfo.getInstance().getId()+"");
        params.addQueryStringParameter("find_type", find_type);
        params.addQueryStringParameter("clean_remind", clean_remind);
        return params;
    }

    /**
     * 关注操作
     *
     * @param follow_id
     * @param callBack
     */
    public static HttpHandler<String> focusOperate(String follow_id, RequestCallBack<String> callBack) {
        RequestParams params = getfocusOperateRequestParams(follow_id);
        return HttpRequest.sign(params, URL.FOCUS_OPRATE_URL, callBack, false);
    }

    @NonNull
    public static RequestParams getfocusOperateRequestParams(String follow_id) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("follow_id", follow_id);
        return params;
    }

    /**
     * 取消关注
     *
     * @param follow_id
     * @param callBack
     */
    public static HttpHandler<String> cancelFocusOperate(String follow_id, RequestCallBack<String> callBack) {
        RequestParams params = getcancelFocusOperateRequestParams(follow_id);
        return HttpRequest.sign(params, URL.CANCEL_FOCUS_URL, callBack, false);
    }

    @NonNull
    public static RequestParams getcancelFocusOperateRequestParams(String follow_id) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("follow_id", follow_id);
        return params;
    }

    /**
     * 意见反馈
     *
     * @param content
     * @param contact
     * @param callBack
     */
    public static void commitSuggestion(String content, String contact, RequestCallBack<String> callBack) {
        RequestParams params = getcommitSuggestionRequestParams(content, contact);
        HttpRequest.sign(params, URL.SUGGESTION_URL, callBack, false);
    }

    @NonNull
    public static RequestParams getcommitSuggestionRequestParams(String content, String contact) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("content", content);
        params.addQueryStringParameter("contact", contact);
        params.addQueryStringParameter("from_to", "2"); //1.ios;2.android;3.ipad;4.win;5.web;6.wap
        params.addQueryStringParameter("kind", "3");   //1.网页版; 2.商城app; 3.Fiu
        return params;
    }

    /**
     * 更新用户信息
     *
     * @param key
     * @param value
     * @param callBack
     */
    public static void updateUserInfo(String key, String value, RequestCallBack<String> callBack) {
        RequestParams params = getupdateUserInfoRequestParams(key, value);
        HttpRequest.sign(params, URL.UPDATE_USERINFO_URL, callBack, false);
    }

    @NonNull
    public static RequestParams getupdateUserInfoRequestParams(String key, String value) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        if (EditUserInfoActivity.isSubmitAddress) {
            params.addQueryStringParameter("province_id", key);
            params.addQueryStringParameter("district_id", value);
        } else {
            params.addQueryStringParameter(key, value);
        }
        return params;
    }

    /**
     * 更新用户信息
     *
     * @param nickname
     * @param sex
     * @param callBack
     */
    public static void updateNickNameSex(String nickname, String sex, RequestCallBack<String> callBack) {
        RequestParams params = getupdateNickNameSexRequestParams(nickname, sex);
        HttpRequest.sign(params, URL.UPDATE_USERINFO_URL, callBack, false);
    }

    @NonNull
    public static RequestParams getupdateNickNameSexRequestParams(String nickname, String sex) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("nickname", nickname);
        params.addQueryStringParameter("sex", sex);
        return params;
    }

    /**
     * 更新用户信息
     *
     * @param age_group
     * @param assets
     * @param callBack
     */
    public static void updateAgeAssets(String age_group, String assets, RequestCallBack<String> callBack) {
        RequestParams params = getupdateAgeAssetsRequestParams(age_group, assets);
        HttpRequest.sign(params, URL.UPDATE_USERINFO_URL, callBack, false);
    }

    @NonNull
    public static RequestParams getupdateAgeAssetsRequestParams(String age_group, String assets) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("age_group", age_group);
        params.addQueryStringParameter("assets", assets);
        return params;
    }

    /**
     * 提交订阅的主题
     *
     * @param interest_scene_cate
     * @param callBack
     */
    public static void subscribeTheme(String interest_scene_cate, RequestCallBack<String> callBack) {
        RequestParams params = getsubscribeThemeRequestParams(interest_scene_cate);
        HttpRequest.sign(params, URL.UPDATE_USERINFO_URL, callBack, false);
    }

    @NonNull
    public static RequestParams getsubscribeThemeRequestParams(String interest_scene_cate) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("interest_scene_cate", interest_scene_cate);
        return params;
    }

    /**
     * 更新签名和label
     *
     * @param label
     * @param summary
     * @param callBack
     */
    public static void updateSignatrueLabel(String label, String summary, RequestCallBack<String> callBack) {
        RequestParams params = getupdateSignatrueLabelRequestParams(label, summary);
        HttpRequest.sign(params, URL.UPDATE_USERINFO_URL, callBack, false);
    }

    @NonNull
    public static RequestParams getupdateSignatrueLabelRequestParams(String label, String summary) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("label", label);
        params.addQueryStringParameter("summary", summary);
        return params;
    }

    /**
     * 获取所有城市
     *
     * @param callBack
     */
    public static void getAllCities(RequestCallBack<String> callBack) {
        RequestParams params = getgetAllCitiesRequestParams();
        HttpRequest.sign(params, URL.ALL_CITY_URL, callBack, false);
    }

    @NonNull
    public static RequestParams getgetAllCitiesRequestParams() {
        return new RequestParams(ConstantCfg.CHARSET);
    }

    /**
     * 上传头像
     *
     * @param callBack
     */
    public static void uploadImg(String tmp, String type, RequestCallBack<String> callBack) {
        RequestParams params = getuploadImgRequestParams(tmp, type);
        HttpRequest.sign(params, URL.UPLOAD_IMG_URL, callBack, false);
    }

    @NonNull
    public static RequestParams getuploadImgRequestParams(String tmp, String type) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("tmp", tmp);
        params.addQueryStringParameter("type", type);
        return params;
    }

    /**
     * 上传个人中心背景
     *
     * @param callBack
     */
    public static HttpHandler<String> uploadBgImg(String tmp, RequestCallBack<String> callBack) {
        RequestParams params = getuploadBgImgRequestParams(tmp);
        HttpHandler<String> handler = HttpRequest.sign(params, URL.UPLOAD_BG_URL, callBack, false);
        NetworkManager.getInstance().add(URL.UPLOAD_BG_URL, handler);
        return handler;
    }

    @NonNull
    public static RequestParams getuploadBgImgRequestParams(String tmp) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("tmp", tmp);
        return params;
    }

    /**
     * 获取系统通知
     *
     * @param page
     * @param pageSize
     * @param callBack
     */
    public static void getSystemNotice(String page, String pageSize, RequestCallBack<String> callBack) {
        RequestParams params = getgetSystemNoticeRequestParams(page, pageSize);
        HttpRequest.sign(params, URL.SYSTEM_NOTICE, callBack, false);
    }

    @NonNull
    public static RequestParams getgetSystemNoticeRequestParams(String page, String pageSize) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("page", page);
        params.addQueryStringParameter("size", pageSize);
        return params;
    }

    /**
     * 获取聊天记录
     *
     * @param page
     * @param pageSize
     * @param type
     * @param callBack
     */
    public static void getPrivateMessageList(String page, String pageSize, String type, RequestCallBack<String> callBack) {
        RequestParams params = getgetPrivateMessageListRequestParams(page, pageSize, type);
        HttpRequest.sign(params, URL.MESSAGE_RECORD, callBack, false);
    }

    @NonNull
    public static RequestParams getgetPrivateMessageListRequestParams(String page, String pageSize, String type) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("page", page);
        params.addQueryStringParameter("size", pageSize);
        params.addQueryStringParameter("type", type);
        return params;
    }

    /**
     * 发送消息
     *
     * @param to_user_id
     * @param content
     * @param callBack
     */
    public static void sendMessage(String to_user_id, String content, RequestCallBack<String> callBack) {
        RequestParams params = getsendMessageRequestParams(to_user_id, content);
        HttpRequest.sign(params, URL.SEND_MESSAGE, callBack, false);
    }

    @NonNull
    public static RequestParams getsendMessageRequestParams(String to_user_id, String content) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("to_user_id", to_user_id);
        params.addQueryStringParameter("content", content);
        return params;
    }

    /**
     * 私信详情
     *
     * @param to_user_id
     * @param callBack
     */
    public static void messageDetailList(String to_user_id, RequestCallBack<String> callBack) {
        RequestParams params = getmessageDetailListRequestParams(to_user_id);
        HttpRequest.sign(params, URL.MESSAGE_DETAIL, callBack, false);
    }

    @NonNull
    public static RequestParams getmessageDetailListRequestParams(String to_user_id) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("to_user_id", to_user_id);
        return params;
    }

    //验证红包是否可用
    public static void checkRedBagUsableNet(String rid, String code, RequestCallBack<String> callBack) {
        RequestParams params = getcheckRedBagUsableNetRequestParams(rid, code);
        HttpRequest.sign(params, URL.SHOPPING_USE_BONUS, callBack, false);
    }

    @NonNull
    public static RequestParams getcheckRedBagUsableNetRequestParams(String rid, String code) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
//        params.addQueryStringParameter("uuid", uuid);
        params.addQueryStringParameter("rid", rid);
        params.addQueryStringParameter("code", code);
        return params;
    }


    //红包
    public static void myRedBagNet(String page, String size, String used, String time, RequestCallBack<String> callBack) {
        RequestParams params = getmyRedBagNetRequestParams(page, size, used, time);
        HttpRequest.sign(params, URL.MY_BONUS, callBack, false);
    }

    @NonNull
    public static RequestParams getmyRedBagNetRequestParams(String page, String size, String used, String time) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("page", page);
        params.addQueryStringParameter("size", size);
        params.addQueryStringParameter("used", used);
        params.addQueryStringParameter("is_expired", time);
        return params;
    }

    /**
     * 修改密码
     *
     * @param password
     * @param new_password
     * @param callBack
     */
    public static void updatePassword(String password, String new_password, RequestCallBack<String> callBack) {
        RequestParams params = getupdatePasswordRequestParams(password, new_password);
        HttpRequest.sign(params, URL.MY_MODIFY_PASSWORD, callBack, false);
    }

    @NonNull
    public static RequestParams getupdatePasswordRequestParams(String password, String new_password) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("password", password);
        params.addQueryStringParameter("new_password", new_password);
        return params;
    }

    //立即下单
    public static HttpHandler<String> nowConfirmOrder(String rrid, String addbook_id, String is_nowbuy, String summary, String transfer_time, String bonus_code, RequestCallBack<String> callBack) {
        String url = URL.URLSTRING_NOW_CONFIRMORDER;
        RequestParams params = getnowConfirmOrderRequestParams(rrid, addbook_id, is_nowbuy, summary, transfer_time, bonus_code);
        HttpHandler<String> httpHandler = HttpRequest.sign(params, url, callBack);
//        NetworkManager.getInstance().add("nowConfirmOrder", httpHandler);
        return httpHandler;
    }

    @NonNull
    public static RequestParams getnowConfirmOrderRequestParams(String rrid, String addbook_id, String is_nowbuy, String summary, String transfer_time, String bonus_code) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("rrid", rrid);
        params.addQueryStringParameter("from_site", "8");
        params.addQueryStringParameter("addbook_id", addbook_id);
        params.addQueryStringParameter("is_nowbuy", is_nowbuy);
        params.addQueryStringParameter("summary", summary);
        params.addQueryStringParameter("transfer_time", transfer_time);
        params.addQueryStringParameter("bonus_code", bonus_code);
        return params;
    }

    //购物车
    public static HttpHandler<String> shopCartNet(RequestCallBack<String> callBack) {
        RequestParams params = getshopCartNetRequestParams();
        HttpHandler<String> httpHandler = HttpRequest.sign(params, URL.SHOPPING_FETCH_CART, callBack);
        return httpHandler;
    }

    @NonNull
    public static RequestParams getshopCartNetRequestParams() {
        return new RequestParams(ConstantCfg.CHARSET);
    }

    //购物车数量
    public static HttpHandler<String> shopCartNumberNet(RequestCallBack<String> callBack) {
        RequestParams params = getshopCartNumberNetRequestParams();
        HttpHandler<String> httpHandler = HttpRequest.sign(params, URL.SHOPPING_FETCH_CART_COUNT, callBack);
        return httpHandler;
    }

    @NonNull
    public static RequestParams getshopCartNumberNetRequestParams() {
        return new RequestParams(ConstantCfg.CHARSET);
    }

    //购物车结算下单
    public static void calculateShopCartNet(String array, RequestCallBack<String> callBack) {
        RequestParams params = getcalculateShopCartNetRequestParams(array);
        HttpHandler<String> httpHandler = HttpRequest.sign(params, URL.SHOPING_CHECKOUT, callBack);
    }

    @NonNull
    public static RequestParams getcalculateShopCartNetRequestParams(String array) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("array", array);
        params.addQueryStringParameter("referral_code", SPUtil.read("referral_code" ));
        return params;
    }

    //删除购物车
    public static void deletShopCartNet(String array, RequestCallBack<String> callBack) {
        RequestParams params = getdeletShopCartNetRequestParams(array);
        HttpHandler<String> httpHandler = HttpRequest.sign(params, URL.SHOPPING_REMOVE_CART, callBack);
    }

    @NonNull
    public static RequestParams getdeletShopCartNetRequestParams(String array) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("array", array);
        return params;
    }

    //取消点赞
    public static HttpHandler<String> cancelLoveNet(String id, String type, RequestCallBack<String> callBack) {
        String url = URL.URLSTRING_CANCELLOVE;
        RequestParams params = getcancelLoveNetRequestParams(id, type);
        HttpHandler<String> httpHandler = HttpRequest.sign(params, url, callBack);
        return httpHandler;
    }

    @NonNull
    public static RequestParams getcancelLoveNetRequestParams(String id, String type) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("id", id);
        params.addQueryStringParameter("type", type);
        return params;
    }

    //点赞
    public static HttpHandler<String> loveNet(String id, String type, RequestCallBack<String> callBack) {
        String url = URL.URLSTRING_LOVE;
        RequestParams params = getloveNetRequestParams(id, type);
        HttpHandler<String> httpHandler = HttpRequest.sign(params, url, callBack);
        return httpHandler;
    }

    @NonNull
    public static RequestParams getloveNetRequestParams(String id, String type) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("id", id);
        params.addQueryStringParameter("type", type);
        return params;
    }

    //添加购物车
    public static HttpHandler<String> addToCartNet(String target_id, String type, String n, RequestCallBack<String> callBack) {
        String url = URL.URLSTRING_ADD_TO_CART;
        RequestParams params = getaddToCartNetRequestParams(target_id, type, n);
        HttpHandler<String> httpHandler = HttpRequest.sign(params, url, callBack);
        return httpHandler;
    }

    @NonNull
    public static RequestParams getaddToCartNetRequestParams(String target_id, String type, String n) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("target_id", target_id);
        params.addQueryStringParameter("type", type);
        params.addQueryStringParameter("n", n);
        params.addQueryStringParameter("storage_id", "店铺ID");
        params.addQueryStringParameter("referral_code", SPUtil.read("referral_code" ));
        return params;
    }

    //立即购买(验证数据并会生成临时订单)
    public static HttpHandler<String> buyNow(String target_id, String type, String n, RequestCallBack<String> callBack) {
        String url = URL.URLSTRING_BUY_NOW;
        RequestParams params = getbuyNowRequestParams(target_id, type, n);
        HttpHandler<String> httpHandler = HttpRequest.sign(params, url, callBack);
        return httpHandler;
    }

    @NonNull
    public static RequestParams getbuyNowRequestParams(String target_id, String type, String n) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("target_id", target_id);
        params.addQueryStringParameter("type", type);
        params.addQueryStringParameter("n", n);
        params.addQueryStringParameter("storage_id", "店铺ID");
        params.addQueryStringParameter("referral_code", SPUtil.read("referral_code" ));
        return params;
    }

    //删除订单/my/delete_order
    public static void deleteOrderNet(String rid, RequestCallBack<String> callBack) {
        RequestParams params = getdeleteOrderNetRequestParams(rid);
        HttpHandler<String> httpHandler = HttpRequest.sign(params, URL.MY_DELETE_ORDER, callBack);
    }

    @NonNull
    public static RequestParams getdeleteOrderNetRequestParams(String rid) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("rid", rid);
        return params;
    }

    //取消订单
    public static void cancelOrderNet(String rid, RequestCallBack<String> callBack) {
        RequestParams params = getcancelOrderNetRequestParams(rid);
        HttpHandler<String> httpHandler = HttpRequest.sign(params, URL.MY_CANCEL_ORDER, callBack);
    }

    @NonNull
    public static RequestParams getcancelOrderNetRequestParams(String rid) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("rid", rid);
        return params;
    }

    //订单支付详情和订单详情都是这,发表评价界面的产品图片也从这获取
    public static HttpHandler<String> OrderPayNet(String rid, RequestCallBack<String> callBack) {
        RequestParams params = getOrderPayNetRequestParams(rid);
        return HttpRequest.sign(params, URL.SHOPPING_DETAILS, callBack);
    }

    @NonNull
    public static RequestParams getOrderPayNetRequestParams(String rid) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("rid", rid);
        return params;
    }

    //确认收货
    public static void confirmReceiveNet(String rid, RequestCallBack<String> callBack) {
        RequestParams params = getconfirmReceiveNetRequestParams(rid);
        HttpHandler<String> httpHandler = HttpRequest.sign(params, URL.SHOPPING_TAKE_DELIVERY, callBack);
    }

    @NonNull
    public static RequestParams getconfirmReceiveNetRequestParams(String rid) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("rid", rid);
        return params;
    }

    //发表评价
    public static void publishEvaluateNet(String rid, String array, RequestCallBack<String> callBack) {
        RequestParams params = getpublishEvaluateNetRequestParams(rid, array);
        HttpHandler<String> httpHandler = HttpRequest.sign(params, URL.PRODUCT_AJAX_COMMENT, callBack);
    }

    @NonNull
    public static RequestParams getpublishEvaluateNetRequestParams(String rid, String array) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("rid", rid);
        params.addQueryStringParameter("array", array);
        params.addQueryStringParameter("from_site", "4");
        return params;
    }

    //账户处的订单列表
    public static void orderListNet(String status, String page, String size, RequestCallBack<String> callBack) {
        RequestParams params = getorderListNetRequestParams(status, page, size);
        HttpHandler<String> httpHandler = HttpRequest.sign(params, URL.SHOPPING_ORDERS, callBack);
    }

    @NonNull
    public static RequestParams getorderListNetRequestParams(String status, String page, String size) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("status", status);
        params.addQueryStringParameter("page", page);
        params.addQueryStringParameter("size", size);
        return params;
    }

    //购物车中单个商品的库存（即最大加减数）
    public static void shopcartInventoryNet(RequestCallBack<String> callBack) {
        RequestParams params = getshopcartInventoryNetRequestParams();
        HttpHandler<String> httpHandler = HttpRequest.sign(params, URL.SHOPPING_FETCH_CART_PRODUCT_COUUNT, callBack);
    }

    @NonNull
    public static RequestParams getshopcartInventoryNetRequestParams() {
        return new RequestParams(ConstantCfg.CHARSET);
    }

    //购物车中商品加减数量
    public static void shopcartAddSubtractNet(String array, RequestCallBack<String> callBack) {
        RequestParams params = getshopcartAddSubtractNetRequestParams(array);
        HttpHandler<String> httpHandler = HttpRequest.sign(params, URL.SHOPPINGN_EDIT_CART, callBack);
    }

    @NonNull
    public static RequestParams getshopcartAddSubtractNetRequestParams(String array) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("array", array);
        return params;
    }

    //最fiu伙伴
    public static HttpHandler<String> fiuUserList(String page, String size, String sort, RequestCallBack<String> callBack) {
        String url = URL.FIU_USER_LIST;
        RequestParams params = getfiuUserListRequestParams(page, size, sort);
        HttpHandler<String> httpHandler = HttpRequest.sign(params, url, callBack);
        return httpHandler;
    }

    @NonNull
    public static RequestParams getfiuUserListRequestParams(String page, String size, String sort) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("page", page);
        params.addQueryStringParameter("size", size);
        params.addQueryStringParameter("sort", sort);
        return params;
    }

    /**
     * 更新用户是否首次登录的标识
     *
     * @param type
     * @param callBack
     */
    public static void updateUserIdentify(String type, RequestCallBack<String> callBack) {
        String url = URL.UPDATE_USER_IDENTIFY;
        RequestParams params = getupdateUserIdentifyRequestParams(type);
        HttpHandler<String> httpHandler = HttpRequest.sign(params, url, callBack);
    }

    @NonNull
    public static RequestParams getupdateUserIdentifyRequestParams(String type) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("type", type);
        return params;
    }

    /**
     * 获得提醒列表
     *
     * @param page
     * @param size
     * @param type
     * @param callBack
     */
    public static void getNoticeList(String page, String size, String type, RequestCallBack<String> callBack) {
        String url = URL.NOTICE_LIST;
        RequestParams params = getgetNoticeListRequestParams(page, size, type);
        HttpHandler<String> httpHandler = HttpRequest.sign(params, url, callBack);
    }

    @NonNull
    public static RequestParams getgetNoticeListRequestParams(String page, String size, String type) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("page", page);
        params.addQueryStringParameter("size", size);
        params.addQueryStringParameter("type", type);
        return params;
    }


    /**
     * 发现好友列表
     *
     * @param callBack
     */
    public static void findFriends(String page, String size, String sight_count, String sort, RequestCallBack<String> callBack) {
        RequestParams params = getfindFriendsRequestParams(page, size, sight_count, sort);
        HttpHandler<String> httpHandler = HttpRequest.sign(params, URL.FIND_FRIENDS, callBack);
    }

    @NonNull
    public static RequestParams getfindFriendsRequestParams(String page, String size, String sight_count, String sort) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("page", page);
        params.addQueryStringParameter("size", size);
        params.addQueryStringParameter("type", "1"); //过滤已关注
        params.addQueryStringParameter("sight_count", sight_count); //场景数量
        params.addQueryStringParameter("sort", sort); //0是最新 1是随机
        params.addQueryStringParameter("use_cache", "1");
        return params;
    }

    /**
     * 退出登录
     *
     * @param callBack
     */
    public static void logout(RequestCallBack<String> callBack) {
        RequestParams params = getlogoutRequestParams();
        HttpHandler<String> httpHandler = HttpRequest.sign(params, URL.LOGOUT, callBack);
    }

    @NonNull
    public static RequestParams getlogoutRequestParams() {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("from_to", "2"); // 1.ios;2.android;3.win;4.ipad;
        return params;
    }

    /**
     * 获取手机状态
     *
     * @param account
     * @param callBack
     */
    public static void getPhoneState(String account, RequestCallBack<String> callBack) {
        RequestParams params = getgetPhoneStateRequestParams(account);
        HttpHandler<String> httpHandler = HttpRequest.sign(params, URL.GET_REGIST_STATE, callBack);
    }

    @NonNull
    public static RequestParams getgetPhoneStateRequestParams(String account) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("account", account);
        return params;
    }

    /**
     * 获取支付参数
     *
     * @param rid
     * @param payaway
     * @param callBack
     */
    public static void getPayParams(String rid, String payaway, RequestCallBack<String> callBack) {
        RequestParams params = getgetPayParamsRequestParams(rid, payaway);
        HttpHandler<String> httpHandler = HttpRequest.sign(params, URL.PAY_URL, callBack, true);
    }

    @NonNull
    public static RequestParams getgetPayParamsRequestParams(String rid, String payaway) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("rid", rid);
        params.addQueryStringParameter("payaway", payaway);
        return params;
    }

    public static void uploadIdentityInfo(String id, String info, String label, String contact, String id_card_a_tmp, String business_card_tmp, RequestCallBack<String> callBack) {
        RequestParams params = getuploadIdentityInfoRequestParams(id, info, label, contact, id_card_a_tmp, business_card_tmp);
        HttpHandler<String> httpHandler = HttpRequest.sign(params, URL.UPLOAD_IDENTIFY_URL, callBack);
        NetworkManager.getInstance().add(URL.UPLOAD_IDENTIFY_URL, httpHandler);
    }

    @NonNull
    public static RequestParams getuploadIdentityInfoRequestParams(String id, String info, String label, String contact, String id_card_a_tmp, String business_card_tmp) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("id", id);
        params.addQueryStringParameter("info", info);
        params.addQueryStringParameter("label", label);
        params.addQueryStringParameter("contact", contact);
        params.addQueryStringParameter("id_card_a_tmp", id_card_a_tmp);
        params.addQueryStringParameter("business_card_tmp", business_card_tmp);
        return params;
    }

    public static void tixingFahuo(String rid, RequestCallBack<String> callBack) {
        RequestParams params = gettixingFahuoRequestParams(rid);
        HttpHandler<String> httpHandler = HttpRequest.sign(params, URL.SHOPPING_ALERT_SEND_GOODS, callBack);
    }

    @NonNull
    public static RequestParams gettixingFahuoRequestParams(String rid) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("rid", rid);
        return params;
    }

    /**
     * 获得认证信息
     *
     * @param callBack
     */
    public static void getAuthStatus(RequestCallBack<String> callBack) {
        RequestParams params = getgetAuthStatusRequestParams();
        HttpHandler<String> httpHandler = HttpRequest.sign(params, URL.MY_FETCH_TALENT, callBack);
    }

    @NonNull
    public static RequestParams getgetAuthStatusRequestParams() {
        return new RequestParams(ConstantCfg.CHARSET);
    }

    //送积分
    public static HttpHandler<String> getBonus(String type, String evt, String target_id, RequestCallBack<String> callBack) {
        String url = URL.GET_BONUS;
        RequestParams params = getgetBonusRequestParams(type, evt, target_id);
        HttpHandler<String> httpHandler = HttpRequest.sign(params, url, callBack);
        return httpHandler;
    }

    @NonNull
    public static RequestParams getgetBonusRequestParams(String type, String evt, String target_id) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("type", type);
        params.addQueryStringParameter("evt", evt);
        params.addQueryStringParameter("target_id", target_id);
        return params;
    }

    /**
     * 获取专题信息
     *
     * @param id
     * @param callBack
     */
    public static void getSubjectData(String id, RequestCallBack<String> callBack) {
        RequestParams params = getgetSubjectDataRequestParams(id);
        HttpHandler<String> httpHandler = HttpRequest.sign(params, URL.SCENE_SUBJECT_VIEW, callBack);
    }

    @NonNull
    public static RequestParams getgetSubjectDataRequestParams(String id) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("id", id);
        return params;
    }


    //收藏产品和取消收藏
    public static void favorite(String id, String type, RequestCallBack<String> callBack) {
        String url = URL.FAVORITE_PRODUCT;
        RequestParams params = getfavoriteRequestParams(id, type);
        HttpHandler<String> httpHandler = HttpRequest.sign(params, url, callBack);
    }

    @NonNull
    public static RequestParams getfavoriteRequestParams(String id, String type) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("id", id);
        params.addQueryStringParameter("type", type);
        return params;
    }

    public static void cancelFavorite(String id, String type, RequestCallBack<String> callBack) {
        String url = URL.CANCEL_FAVORITE_PRODUCT;
        RequestParams params = getcancelFavoriteRequestParams(id, type);
        HttpHandler<String> httpHandler = HttpRequest.sign(params, url, callBack);
    }

    @NonNull
    public static RequestParams getcancelFavoriteRequestParams(String id, String type) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("id", id);
        params.addQueryStringParameter("type", type);
        return params;
    }


    public static void isInvited(RequestCallBack<String> callBack) {
        RequestParams params = getisInvitedRequestParams();
        HttpHandler<String> httpHandler = HttpRequest.sign(params, URL.GATEWAY_IS_INVITED, callBack);
    }

    @NonNull
    public static RequestParams getisInvitedRequestParams() {
        return new RequestParams(ConstantCfg.CHARSET);
    }


    public static void submitInviteCode(String code, RequestCallBack<String> callBack) {
        RequestParams params = getsubmitInviteCodeRequestParams(code);
        HttpHandler<String> httpHandler = HttpRequest.sign(params, URL.GATEWAY_VALIDE_INVITE_CODE, callBack);
    }

    @NonNull
    public static RequestParams getsubmitInviteCodeRequestParams(String code) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("code", code);
        return params;
    }

    public static void updateInviteCodeStatus(String code, RequestCallBack<String> callBack) {
        RequestParams params = getupdateInviteCodeStatusRequestParams(code);
        HttpHandler<String> httpHandler = HttpRequest.sign(params, URL.GATEWAY_DEL_INVITE_CODE, callBack);
    }

    @NonNull
    public static RequestParams getupdateInviteCodeStatusRequestParams(String code) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("code", code);
        return params;
    }

    /**
     * @param page
     * @param size
     * @param type
     * @param event
     * @param callBack
     */
    public static void getCollectOrdered(String page, String size, String type, String event, RequestCallBack<String> callBack) {
        RequestParams params = getgetCollectOrderedRequestParams(page, size, type, event);
        HttpHandler<String> httpHandler = HttpRequest.sign(params, URL.FAVORITE_GET_NEW_LIST, callBack);
    }

    @NonNull
    public static RequestParams getgetCollectOrderedRequestParams(String page, String size, String type, String event) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("page", page);
        params.addQueryStringParameter("size", size);
        params.addQueryStringParameter("user_id", String.valueOf(LoginInfo.getUserId()));
        params.addQueryStringParameter("type", type);
        params.addQueryStringParameter("event", event);
        return params;
    }

    //语境列表
    public static HttpHandler<String> envirList(String page, String size, String sort, String category_id, String stick, RequestCallBack<String> callBack) {
        RequestParams params = getenvirListRequestParams(page, size, sort, category_id, stick);
        HttpHandler<String> httpHandler = HttpRequest.sign(params, URL.SCENE_CONTEXT_GETLIST, callBack);
        return httpHandler;
    }

    @NonNull
    public static RequestParams getenvirListRequestParams(String page, String size, String sort, String category_id, String stick) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("page", page);
        params.addQueryStringParameter("size", size);
        params.addQueryStringParameter("sort", sort);
        params.addQueryStringParameter("category_id", category_id);
        params.addQueryStringParameter("stick", stick);
        return params;
    }

    //用户激活状态
    public static void activeStatus(RequestCallBack<String> callBack) {
        RequestParams params = getactiveStatusRequestParams();
        HttpHandler<String> httpHandler = HttpRequest.sign(params, URL.GATEWAY_RECORD_FIU_USER_ACTIVE, callBack);
    }

    @NonNull
    public static RequestParams getactiveStatusRequestParams() {
        return new RequestParams(ConstantCfg.CHARSET);
    }

    //验证验证码
    public static void submitCheckCode(String phone, String code, RequestCallBack<String> callBack) {
        RequestParams params = getsubmitCheckCodeRequestParams(phone, code);
        HttpHandler<String> httpHandler = HttpRequest.sign(params, URL.AUTH_CHECK_VERIFY_CODE, callBack);
    }

    @NonNull
    public static RequestParams getsubmitCheckCodeRequestParams(String phone, String code) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("phone", phone);
        params.addQueryStringParameter("code", code);
        return params;
    }

    //注册用户
    public static void registerUser(String mobile, String password, String verify_code, RequestCallBack<String> callBack) {
        RequestParams params = getregisterUserRequestParams(mobile, password, verify_code);
        HttpHandler<String> httpHandler = HttpRequest.sign(params, URL.AUTH_REGISTER, callBack);
    }

    @NonNull
    public static RequestParams getregisterUserRequestParams(String mobile, String password, String verify_code) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("mobile", mobile);
        params.addQueryStringParameter("password", password);
        params.addQueryStringParameter("verify_code", verify_code);
        params.addQueryStringParameter("from_to", "2"); //1.ios;2.android;3.win;
        return params;
    }

    //完善资料--->关注感兴趣的人
    public static void focusInterestUser(RequestCallBack<String> callBack) {
        RequestParams params = getfocusInterestUserRequestParams();
        HttpHandler<String> httpHandler = HttpRequest.sign(params, URL.USER_FIND_USER, callBack);
    }

    @NonNull
    public static RequestParams getfocusInterestUserRequestParams() {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("page", "1");
        params.addQueryStringParameter("size", "18");
        params.addQueryStringParameter("type", "1");
        params.addQueryStringParameter("edit_stick", "1");
        return params;
    }

    //开始关注感兴趣的人
    public static void focusUsers(String follow_ids, RequestCallBack<String> callBack) {
        RequestParams params = getfocusUsersRequestParams(follow_ids);
        HttpHandler<String> httpHandler = HttpRequest.sign(params, URL.FOLLOW_BATCH_FOLLOW, callBack);
    }

    @NonNull
    public static RequestParams getfocusUsersRequestParams(String follow_ids) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("follow_ids", follow_ids);
        return params;
    }

//    //获取推荐活动标签
//    public static HttpHandler<String> activeTags(RequestCallBack<String> callBack) {
//        RequestParams params = getactiveTagsRequestParams();
//        HttpHandler<String> httpHandler = HttpRequest.sign(params, URL.SCENE_SIGHT_STICK_ACTIVE_TAGS, callBack);
//        return httpHandler;
//    }

    @NonNull
     public static RequestParams getactiveTagsRequestParams() {
        return new RequestParams(ConstantCfg.CHARSET);
    }

    //添加品牌
    public static HttpHandler<String> addBrand(String title, RequestCallBack<String> callBack) {
        RequestParams params = getaddBrandRequestParams(title);
        HttpHandler<String> httpHandler = HttpRequest.sign(params, URL.SCENE_BRANDS_SUBMIT, callBack);
        return httpHandler;
    }

    @NonNull
     public static RequestParams getaddBrandRequestParams(String title) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("title", title);
        return params;
    }

//    //搜索建议
//    public static HttpHandler<String> searchExpand(String q, String size, RequestCallBack<String> callBack) {
//        RequestParams params = getsearchExpandRequestParams(q, size);
//        HttpHandler<String> httpHandler = HttpRequest.sign(params, URL.SEARCH_EXPANDED, callBack);
//        return httpHandler;
//    }

    @NonNull
    public static RequestParams getsearchExpandRequestParams(String q, String size) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("q", q);
        params.addQueryStringParameter("size", size);
        return params;
    }

    /**
     * 取消订阅情景主题
     *
     * @param id
     */
//    public static HttpHandler<String> cancelSubscribe(String id, RequestCallBack<String> callBack) {
//        RequestParams params = getcancelSubscribeRequestParams(id);
//        HttpHandler<String> httpHandler = HttpRequest.sign(params, URL.MY_REMOVE_INTEREST_SCENE_ID, callBack);
//        return httpHandler;
//    }

    @NonNull
    public static RequestParams getcancelSubscribeRequestParams(String id) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("id", id);
        return params;
    }

//    public static HttpHandler<String> subscribe(String id, RequestCallBack<String> callBack) {
//        RequestParams params = getsubscribeRequestParams(id);
//        HttpHandler<String> httpHandler = HttpRequest.sign(params, URL.MY_ADD_INTEREST_SCENE_ID, callBack);
//        return httpHandler;
//    }

    @NonNull
    public static RequestParams getsubscribeRequestParams(String id) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("id", id);
        return params;
    }

    //首页精选主题
    public static HttpHandler<String> subjectList(String page, String size, String stick, String fine, String type, String sort, RequestCallBack<String> callBack) {
        RequestParams params = getsubjectListRequestParams(page, size, stick, fine, type, sort);
        HttpHandler<String> httpHandler = HttpRequest.sign(params, URL.SCENE_SUBJECT_GETLIST, callBack);
        return httpHandler;
    }

    @NonNull
    public static RequestParams getsubjectListRequestParams(String page, String size, String stick, String fine, String type, String sort) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("page", page);
        params.addQueryStringParameter("size", size);
        params.addQueryStringParameter("stick", stick);
        params.addQueryStringParameter("fine", fine);
        params.addQueryStringParameter("type", type);
        params.addQueryStringParameter("sort", sort);
        params.addQueryStringParameter("use_cache", "1");
        return params;
    }

    //最新好货推荐
    public static HttpHandler<String> firstProducts(RequestCallBack<String> callBack) {
        RequestParams params = getfirstProductsRequestParams();
        HttpHandler<String> httpHandler = HttpRequest.sign(params, URL.PRODUCCT_INDEX_NEW, callBack);
        return httpHandler;
    }

    @NonNull
   public static RequestParams getfirstProductsRequestParams() {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("use_cache", "1");
        return params;
    }

    /**
     * @param page
     * @param pageType
     * @param sort
     * @param callBack
     */
    public static void getChoosenSubject(String page, String pageType, String fine, String sort, RequestCallBack<String> callBack) {
//        String url = URL.BASE_URL + "/scene_subject/getlist";
        RequestParams params = getChoosenSubjectRequestParams(page, pageType, fine, sort);
        HttpHandler<String> httpHandler = HttpRequest.sign(params, URL.CHOOSEN_SUBJECT_URL, callBack);
        NetworkManager.getInstance().add(MD5Utils.getMD5(URL.CHOOSEN_SUBJECT_URL),httpHandler);
    }

    @NonNull
    public static RequestParams getChoosenSubjectRequestParams(String page, String pageType, String fine, String sort) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("page", page);
        params.addQueryStringParameter("size", Constants.PAGE_SIZE);//
        params.addQueryStringParameter("fine", fine);
        params.addQueryStringParameter("type", pageType);
        params.addQueryStringParameter("sort", sort);
        params.addQueryStringParameter("use_cache", "1");
        return params;
    }

    //收藏情景
    public static HttpHandler<String> shoucang(String id, String type, RequestCallBack<String> callBack) {
        RequestParams params = getshoucangRequestParams(id, type);
        HttpHandler<String> httpHandler = HttpRequest.sign(params, URL.FAVORITE_AJAX_FAVORITE, callBack);
        return httpHandler;
    }

    @NonNull
    public static RequestParams getshoucangRequestParams(String id, String type) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("id", id);
        params.addQueryStringParameter("type", type);
        return params;
    }

    //取消收藏
    public static HttpHandler<String> cancelShoucang(String id, String type, RequestCallBack<String> callBack) {
        RequestParams params = getcancelShoucangRequestParams(id, type);
        HttpHandler<String> httpHandler = HttpRequest.sign(params, URL.FAVORITE_AJAX_CANCEL_FAVORITE, callBack);
        return httpHandler;
    }

    @NonNull
   public static RequestParams getcancelShoucangRequestParams(String id, String type) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("id", id);
        params.addQueryStringParameter("type", type);
        return params;
    }

    //首页用户列表
    public static HttpHandler<String> getUserList(int size, RequestCallBack<String> callBack) {
        RequestParams params = getUserListRequestParams(size);
        HttpHandler<String> httpHandler = HttpRequest.sign(params, URL.USER_FIND_USER, callBack);
        return httpHandler;
    }

    @NonNull
    public static RequestParams getUserListRequestParams(int size) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("page", "1");
        params.addQueryStringParameter("type", "1");
        params.addQueryStringParameter("size", size + "");
        params.addQueryStringParameter("edit_stick", "1");
        params.addQueryStringParameter("sort", "1");
        return params;
    }

    //临时产品库
    public static void getTempGoods(String id, RequestCallBack<String> callBack) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("id", id);
        HttpHandler<String> httpHandler = HttpRequest.sign(params, URL.USER_TEMP_VIEW, callBack);
    }

    /**
     * 更新分享数量
     *
     * @param id
     * @param callBack
     */
    public static void updateShareCount(String id, RequestCallBack<String> callBack) {
        RequestParams params = getupdateShareCountRequestParams(id);
        HttpHandler<String> httpHandler = HttpRequest.sign(params, URL.SCENE_SUBJECT_RECORD_SHARE_COUNT, callBack);
    }

    @NonNull
    public static RequestParams getupdateShareCountRequestParams(String id) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("id", id);
        return params;
    }

    //当前用户是否是管理员
    public static HttpHandler<String> isEditor(RequestCallBack<String> callBack) {
        RequestParams params = getisEditorRequestParams();
        HttpHandler<String> httpHandler = HttpRequest.sign(params, URL.USER_IS_EDITOR, callBack);
        return httpHandler;
    }

    @NonNull
    public static RequestParams getisEditorRequestParams() {
        return new RequestParams(ConstantCfg.CHARSET);
    }

    //管理员添加精选或取消精选
    public static HttpHandler<String> setFine(String id, String evt, RequestCallBack<String> callBack) {
        RequestParams params = getsetFineRequestParams(id, evt);
        HttpHandler<String> httpHandler = HttpRequest.sign(params, URL.USER_DO_FINE, callBack);
        return httpHandler;
    }

    @NonNull
    public static RequestParams getsetFineRequestParams(String id, String evt) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("id", id);
        params.addQueryStringParameter("evt", evt);
        return params;
    }

    //管理员添加推荐或取消推荐
    public static HttpHandler<String> setStick(String id, String evt, RequestCallBack<String> callBack) {
        RequestParams params = getsetStickRequestParams(id, evt);
        HttpHandler<String> httpHandler = HttpRequest.sign(params, URL.USER_DO_STICK, callBack);
        return httpHandler;
    }

    @NonNull
    public static RequestParams getsetStickRequestParams(String id, String evt) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("id", id);
        params.addQueryStringParameter("evt", evt);
        return params;
    }

    //管理员添加屏蔽或取消屏蔽
    public static HttpHandler<String> setCheck(String id, String evt, RequestCallBack<String> callBack) {
        RequestParams params = getsetCheckRequestParams(id, evt);
        HttpHandler<String> httpHandler = HttpRequest.sign(params, URL.USER_DO_CHECK, callBack);
        return httpHandler;
    }

    @NonNull
    public static RequestParams getsetCheckRequestParams(String id, String evt) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("id", id);
        params.addQueryStringParameter("evt", evt);
        return params;
    }

    /**
     * 请求地址
     * @param oid
     * @param pid
     * @param layer
     * @param callBack
     */
    public static void requestAddress(String oid, String pid,String layer,RequestCallBack<String> callBack) {
        RequestParams params = getrequestAddressRequestParams(oid, pid, layer);
        HttpHandler<String> httpHandler = HttpRequest.sign(params, URL.SHOPPING_FETCH_CHINA_CITY, callBack);
    }

    @NonNull
    public static RequestParams getrequestAddressRequestParams(String oid, String pid, String layer) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("oid",oid);
        params.addQueryStringParameter("pid",pid);
        params.addQueryStringParameter("layer",layer);
        return params;
    }

    /**
     * @param callBack
     */
    public static HttpHandler<String> getIndexChosenSubject(RequestCallBack<String> callBack) {
        RequestParams params = getIndexChosenSubjectRequestParams();
        return HttpRequest.sign(params, URL.SCENE_SUBJECT_INDEX_SUJECT_STICK, callBack);
    }

    @NonNull
    public static RequestParams getIndexChosenSubjectRequestParams() {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("use_cache", "1");
        return params;
    }

    /**
     * 退款单列表
     *
     * @param callBack
     */
    public static HttpHandler<String> getRefundList(String page, String size, RequestCallBack<String> callBack) {
        RequestParams params = getRefundListRequestParams(page, size);
        return HttpRequest.sign(params, URL.SHOPPING_REFUND_LIST, callBack);
    }

    @NonNull
    public static RequestParams getRefundListRequestParams(String page, String size) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("page", page);
        params.addQueryStringParameter("size", size);
        return params;
    }

    /**
     *  获取退款信息
     *  *
     * @param callBack
     */
    public static HttpHandler<String> getChargeBackInfo(String rId, String skuId, RequestCallBack<String> callBack) {
        RequestParams params = getChargeBackInfoRequestParams(rId, skuId);
        return HttpRequest.sign(params, URL.SHOPPING_CHECK_REFUND, callBack);
    }

    @NonNull
    public static RequestParams getChargeBackInfoRequestParams(String rId, String skuId) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("rid", rId);
        params.addQueryStringParameter("sku_id", skuId);
        return params;
    }

    /**
     * 申请退款
     *
     * @param callBack
     */
    public static HttpHandler<String> getApplyProductRefund(String rid, String sku_id, String refund_type,
                                                            String refund_reason, String refund_content, String refund_price,
                                                            RequestCallBack<String> callBack) {
        RequestParams params = getApplyProductRefundRequestParams(rid, sku_id, refund_type, refund_reason, refund_content, refund_price);
        return HttpRequest.sign(params, URL.SHOPPING_APPLY_PRODUCT_REFUND, callBack);
    }

    @NonNull
    public static RequestParams getApplyProductRefundRequestParams(String rid, String sku_id, String refund_type, String refund_reason, String refund_content, String refund_price) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("rid", rid);
        params.addQueryStringParameter("sku_id", sku_id);
        params.addQueryStringParameter("refund_type", refund_type);
        params.addQueryStringParameter("refund_reason", refund_reason);
        params.addQueryStringParameter("refund_content", refund_content);
        params.addQueryStringParameter("refund_price", refund_price);
        return params;
    }

    /**
     * 退款成功详情
     *
     * @param callBack
     */
    public static HttpHandler<String> getRefundSuccessInfo(String chargebackId,RequestCallBack<String> callBack) {
        RequestParams params = getRefundSuccessInfoRequestParams(chargebackId);
        return HttpRequest.sign(params, URL.SHOPPING_REFUND_VIEW, callBack);
    }

    @NonNull
    public static RequestParams getRefundSuccessInfoRequestParams(String chargebackId) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("id", chargebackId);
        return params;
    }

    /**
     * 获取版本信息
     *
     * @param callBack
     */
    public static HttpHandler<String> updateToLatestVersion(RequestCallBack<String> callBack) {
        RequestParams params = getupdateToLatestVersionRequestParams();
        return HttpRequest.sign(params, URL.FETCH_LATEST_VERSION, callBack);
    }

    @NonNull
    public static RequestParams getupdateToLatestVersionRequestParams() {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("from_to", "2");
        return params;
    }

    /**
     * 检查版本更新
     *
     * @param callBack
     */
    public static HttpHandler<String> checkVersionInfo(String versionName,RequestCallBack<String> callBack) {
        RequestParams params = getcheckVersionInfoRequestParams(versionName);
        return HttpRequest.sign(params, URL.CHECK_VERSION_INFO, callBack);
    }

    @NonNull
    public static RequestParams getcheckVersionInfoRequestParams(String versionName) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("from_to", "2");
        params.addQueryStringParameter("version", versionName);
        return params;
    }

    /**
     * 获取邮费接口
     *
     * @param callBack
     */
    public static HttpHandler<String> fetchFreight (String addbook_id, String rid, RequestCallBack<String> callBack) {
        RequestParams params = getFetchFreightRequestParams(addbook_id, rid);
        return HttpRequest.sign(params, URL.SHOPPING_FETCH_FREIGHT, callBack);
    }

    @NonNull
    public static RequestParams getFetchFreightRequestParams(String addbook_id, String rid) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("addbook_id", addbook_id);
        params.addQueryStringParameter("rid", rid);
        return params;
    }

    /**
     * 获取邮费接口
     *
     * @param callBack callBack
     */
    public static HttpHandler<String> shoppingTracking (String rid, String express_no, String express_caty, RequestCallBack<String> callBack) {
        RequestParams params = getShoppingTrackingRequestParams(rid, express_no, express_caty);
        return HttpRequest.sign(params, URL.SHOPPING_TRACKING, callBack);
    }

    public static RequestParams getShoppingTrackingRequestParams(String rid, String express_no, String express_caty) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("rid", rid);
        params.addQueryStringParameter("express_no", express_no);
        params.addQueryStringParameter("express_caty", express_caty);
        return params;
    }
}
