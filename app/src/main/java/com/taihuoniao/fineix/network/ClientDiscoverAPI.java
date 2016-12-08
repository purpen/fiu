package com.taihuoniao.fineix.network;

import com.baidu.mapapi.model.LatLng;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
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
    //统计用户想购买的数量
    public static void wantBuy(String id) {
        String url = NetworkConstance.WANT_BUY;
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("id", id);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {

            }

            @Override
            public void onFailure(HttpException error, String msg) {

            }
        });
    }

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
        String url = NetworkConstance.DELETE_PRODUCT;
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("id", id);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
    }

    //产品
    //列表
    public static HttpHandler<String> getProductList(String title, String sort, String category_id, String brand_id, String category_tag_ids, String page, String size, String ids, String ignore_ids,
                                                     String stick, String fine, String stage, RequestCallBack<String> callBack) {
        String url = NetworkConstance.URLSTRING_PRODUCTSLIST;
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
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
        return httpHandler;
    }

    //产品
    //添加产品
    public static void addProduct(String attrbute, String oid, String sku_id, String title, String market_price, String sale_price,
                                  String link, String cover_url, String banners_url, RequestCallBack<String> callBack) {
        String url = NetworkConstance.ADD_PRODUCT;
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
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
    }

    //产品
    //添加产品
    public static HttpHandler<String> addProduct(String title, String brand_id, RequestCallBack<String> callBack) {
        String url = NetworkConstance.ADD_PRODUCT;
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("title", title);
        params.addQueryStringParameter("brand_id", brand_id);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
        return httpHandler;
    }

    //产品
    //产品详情
    public static HttpHandler<String> goodsDetails(String id, RequestCallBack<String> callBack) {
        String url = NetworkConstance.GOOD_DETAILS;
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("id", id);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
        return httpHandler;
    }

    //产品
    //获取京东商品信息
    public static void getJDProductsData(String ids, RequestCallBack<String> callBack) {
        String url = NetworkConstance.URLSTRING_JD_PRODUCTSDATA;
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("ids", ids);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
    }

    //产品
    //获取淘宝商品详情
    public static void getTBProductsData(String ids, RequestCallBack<String> callBack) {
        String url = NetworkConstance.URLSTRING_TB_PRODUCTSDATA;
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("ids", ids);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
    }

    //情景
    //点赞，订阅，收藏，关注列表
    public static void commonList(String page, String size, String id, String user_id, String type, String event, RequestCallBack<String> callBack) {
        String url = NetworkConstance.COMMON_LISTS;
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("page", page);
        params.addQueryStringParameter("size", size);
        params.addQueryStringParameter("id", id);
        params.addQueryStringParameter("user_id", user_id);
        params.addQueryStringParameter("type", type);
        params.addQueryStringParameter("event", event);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
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
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("page", page);
        params.addQueryStringParameter("size", Constants.PAGE_SIZE);
        params.addQueryStringParameter("user_id", LoginInfo.getUserId() + "");
        params.addQueryStringParameter("type", type);
        params.addQueryStringParameter("event", event);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, NetworkConstance.FAVORITE_GET_NEW_LIST, callBack);
    }

    //情景
    //情景订阅
    public static void subsQingjing(String id, RequestCallBack<String> callBack) {
        String url = NetworkConstance.SUBS_QINGJING;
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("id", id);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
    }

    //情景
    //取消情景订阅
    public static void cancelSubsQingjing(String id, RequestCallBack<String> callBack) {
        String url = NetworkConstance.CANCEL_SUBS_QINGJING;
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("id", id);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
    }

    //情景
    //情景新增
    public static void createQingjing(String id, String title, String des, String tags, String address, String tmp, String lat, String lng, RequestCallBack<String> callBack) {
        String url = NetworkConstance.CREATE_QINGJING;
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("id", id);
        params.addQueryStringParameter("title", title);
        params.addQueryStringParameter("des", des);
        params.addQueryStringParameter("tags", tags);
        params.addQueryStringParameter("address", address);
        params.addQueryStringParameter("tmp", tmp);
        params.addQueryStringParameter("lat", lat);
        params.addQueryStringParameter("lng", lng);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
    }

    //情景
    //删除情景
    public static void deleteQingjing(String id, RequestCallBack<String> callBack) {
        String url = NetworkConstance.DELETE_QINGJING;
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("id", id);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
    }

    //情景
    //情景详情
    public static void qingjingDetails(String id, RequestCallBack<String> callBack) {
        String url = NetworkConstance.QINGJING_DETAILS;
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("id", id);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
    }


    //情景
    //列表数据
    public static void qingjingList(String page, String category_id, String sort, String fine, String dis, String lng, String lat, RequestCallBack<String> callBack) {
        String url = NetworkConstance.QINGJING_LISTS;
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("page", page);
        params.addQueryStringParameter("category_id", category_id);
        params.addQueryStringParameter("size", 10 + "");
        params.addQueryStringParameter("sort", sort);
        params.addQueryStringParameter("fine", fine);
        params.addQueryStringParameter("dis", 10000 + "");
        params.addQueryStringParameter("lng", lng);
        params.addQueryStringParameter("lat", lat);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
    }

    //场景
    //新增情景
    public static HttpHandler<String> createScene(String id, String title, String des, String scene_id, String tags,
                                                  String products, String address, String city, String tmp, String lat, String lng,
                                                  String subject_ids, RequestCallBack<String> callBack) {
        String url = NetworkConstance.CREATE_SCENE;
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
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
        return httpHandler;
    }

    //场景
    //删除场景
    public static HttpHandler<String> deleteScene(String id, RequestCallBack<String> callBack) {
        String url = NetworkConstance.DELETE_SCENE;
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("id", id);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
        return httpHandler;
    }

    //情境
    //情境点赞
    public static HttpHandler<String> loveQJ(String id, RequestCallBack<String> callBack) {
        String url = NetworkConstance.LOVE_SCENE;
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("id", id);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
        return httpHandler;
    }

    //情境
    //取消情境点赞
    public static HttpHandler<String> cancelLoveQJ(String id, RequestCallBack<String> callBack) {
        String url = NetworkConstance.CANCEL_LOVE_SCENE;
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("id", id);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
        return httpHandler;
    }

    //场景
    //场景详情
    public static HttpHandler<String> sceneDetails(String id, RequestCallBack<String> callBack) {
        String url = NetworkConstance.SCENE_DETAILS;
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("id", id);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
        return httpHandler;
    }

    //场景
    //列表数据
    public static HttpHandler<String> getSceneList(String page, String size, String scene_id, String category_ids, String sort, String fine, String dis, String lng, String lat, RequestCallBack<String> callBack) {
        String url = NetworkConstance.SCENE_LIST;
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
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
        return httpHandler;
    }

    public static void getSceneList(LatLng ll, String page, String size, String dis, RequestCallBack<String> callBack) {
        getSceneList(page, size, null, null, null, null, dis, String.valueOf(ll.longitude), String.valueOf(ll.latitude), callBack);
    }


    /**
     * 获取订阅情境的列表
     *
     * @param page
     * @param category_ids
     * @param callBack
     */
    public static void getQJList(String page, String category_ids, RequestCallBack<String> callBack) {
        String url = NetworkConstance.SCENE_LIST;
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("page", page);
        params.addQueryStringParameter("size", Constants.PAGE_SIZE);
        params.addQueryStringParameter("category_ids", category_ids);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
    }

    /**
     * 参与的情境
     *
     * @param subject_id
     * @param callBack
     */
    public static void participateActivity(String page, String subject_id, RequestCallBack<String> callBack) {
        String url = NetworkConstance.SCENE_LIST;
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("page", page);
        params.addQueryStringParameter("size", Constants.PAGE_SIZE);
        params.addQueryStringParameter("subject_id", subject_id);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
    }

    /**
     * 根据用户ID查找情境
     *
     * @param page
     * @param size
     * @param userId
     * @param callBack
     */
    public static void getSceneList(String page, String size, String userId,String show_all,RequestCallBack<String> callBack) {
        String url = NetworkConstance.SCENE_LIST;
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("page", page);
        params.addQueryStringParameter("size", size);
        params.addQueryStringParameter("user_id", userId);
        params.addQueryStringParameter("show_all", show_all);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
    }

    //个人中心
    //用户列表
    public static void userList(String page, String size, String sort, String has_scene, RequestCallBack<String> callBack) {
        String url = NetworkConstance.USER_LIST;
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("page", page);
        params.addQueryStringParameter("size", size);
        params.addQueryStringParameter("sort", sort);
        params.addQueryStringParameter("has_scene", has_scene);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
    }

    //标签
    //使用过的标签
    public static HttpHandler<String> usedLabelList(RequestCallBack<String> callBack) {
        String url = NetworkConstance.USED_LABEL_LIST;
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("type", 1 + "");
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
        return httpHandler;
    }

    //标签
    //标签列表
    public static void labelList(String parent_id, int page, String size, int sort, int is_hot, RequestCallBack<String> callBack) {
        String url = NetworkConstance.LABEL_LIST;
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("parent_id", parent_id);
        params.addQueryStringParameter("page", page + "");
        params.addQueryStringParameter("sort", sort + "");
        params.addQueryStringParameter("size", size);
        params.addQueryStringParameter("is_hot", is_hot + "");
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
    }

    //标签
    //场景页热门标签
    public static void cjHotLabel(boolean isCJ, RequestCallBack<String> callBack) {
        String url;
        if (isCJ) {
            url = NetworkConstance.CJ_HOT_LABEL;
        } else {
            url = NetworkConstance.PRODUCT_HOT_LABEL;
        }
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
    }

    //标签
    //热门标签
    public static void hotLabelList(String page, RequestCallBack<String> callBack) {
        String url = NetworkConstance.HOT_LABEL_LIST;
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("page", page);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
    }

    //收货地址
    //获取省市列表
    public static HttpHandler<String> getProvinceList(RequestCallBack<String> callBack) {
        String url = NetworkConstance.URLSTRING_PROVINCE_CITIES;
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
        return httpHandler;
    }

    //获取用户默认收货地址
    public static HttpHandler<String> getDefaultAddressNet(RequestCallBack<String> callBack) {
        String url = NetworkConstance.URLSTRING_DEFAULT_ADDRESS;
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
        return httpHandler;
    }

    //收货地址
    //获取用户收货地址列表
    public static void getAddressList(String page, RequestCallBack<String> callBack) {
        String url = NetworkConstance.URLSTRING_ADDRESS_LISTS;
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("page", page);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
//        NetworkManager.getInstance().add("getAddressList", httpHandler);
    }

    //收货地址
    //提交正在编辑的收货地址
    public static HttpHandler<String> commitAddressNet(String id, String name, String phone, String province_id, String city_id,String county_id,String town_id,String address, String zip, String is_default, RequestCallBack<String> callBack) {
        String url = NetworkConstance.URLSTRING_NEW_ADDRESS;
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
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
        return httpHandler;
    }

    //收货地址
    //删除某个收货地址
    public static HttpHandler<String> deleteAddressNet(String id, RequestCallBack<String> callBack) {
        String url = NetworkConstance.URLSTRING_DELETE_ADDRESS;
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("id", id);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
        return httpHandler;
//        NetworkManager.getInstance().add("deleteAddress", httpHandler);
    }

    //公共
    //举报
    public static HttpHandler<String> report(String target_id, String type, String evt, RequestCallBack<String> callBack) {
        String url = NetworkConstance.REPORT;
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("target_id", target_id);
        params.addQueryStringParameter("type", type);
        params.addQueryStringParameter("evt", evt);
        params.addQueryStringParameter("application", 3 + "");
        params.addQueryStringParameter("from_to", 4 + "");
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
        return httpHandler;
    }

    //公共
    //品牌列表
    public static void brandList(int page, int size, String mark, String self_run, String stick, String title, RequestCallBack<String> callBack) {
        String url = NetworkConstance.BRAND_LIST;
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("page", page + "");
        params.addQueryStringParameter("size", size + "");
        params.addQueryStringParameter("kind", 1 + "");
        params.addQueryStringParameter("mark", mark);
        params.addQueryStringParameter("self_run", self_run);
        params.addQueryStringParameter("stick", stick);
        params.addQueryStringParameter("title", title);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
    }

    //公共
    //品牌详情
    public static HttpHandler<String> brandDetail(String id, RequestCallBack<String> callBack) {
        String url = NetworkConstance.BRAND_DETAIL;
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("id", id);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
        return httpHandler;
    }

    //公共
    //分类列表
    public static HttpHandler<String> categoryList(String page, String domain, String show_all, RequestCallBack<String> callBack) {
        String url = NetworkConstance.CATEGORY_LIST;
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("page", page);
        params.addQueryStringParameter("size", 300 + "");
        params.addQueryStringParameter("show_all", show_all);
        params.addQueryStringParameter("domain", domain);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
        return httpHandler;
    }


    /**
     * 获得情景主题
     *
     * @param callBack
     */
    public static void categoryList(RequestCallBack<String> callBack) {
        String url = NetworkConstance.CATEGORY_LIST;
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("page", "1");
        params.addQueryStringParameter("size", "10");
        params.addQueryStringParameter("domain", "13");//情景主题
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
    }

    //公共
    //搜索列表
    public static HttpHandler<String> search(String q, String t, String cid, String page, String size, String evt, String sort, RequestCallBack<String> callBack) {
        String url = NetworkConstance.SEARCH;
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("q", q);
        params.addQueryStringParameter("t", t);
        params.addQueryStringParameter("tid", cid);
        params.addQueryStringParameter("page", page);
        params.addQueryStringParameter("evt", evt);
        params.addQueryStringParameter("sort", sort);
        params.addQueryStringParameter("size", size);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
        return httpHandler;
    }

    public static void searchUser(String q, String page, RequestCallBack<String> callBack) {
        String url = NetworkConstance.SEARCH;
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("q", q);
        params.addQueryStringParameter("t", "14"); //14.用户
        params.addQueryStringParameter("page", page);
        params.addQueryStringParameter("size", Constants.PAGE_SIZE);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
    }

    //公共
    //商品和场景关联列表
    public static HttpHandler<String> productAndScene(String page, String size, String sight_id, String product_id, String brand_id, RequestCallBack<String> callBack) {
        String url = NetworkConstance.PRODUCT_AND_SCENELIST;
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("page", page);
        params.addQueryStringParameter("size", size);
        params.addQueryStringParameter("sight_id", sight_id);
        params.addQueryStringParameter("product_id", product_id);
        params.addQueryStringParameter("brand_id", brand_id);
        return MD5Utils.sign(params, url, callBack);
    }

    //购物车
    //商品数量
    public static HttpHandler<String> cartNum(RequestCallBack<String> callBack) {
        String url = NetworkConstance.CART_NUMBER;
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
        return httpHandler;
    }

    //评论
    //提交评论
    public static HttpHandler<String> sendComment(String target_id, String content, String type, String target_user_id, String is_reply, String reply_id, String reply_user_id, RequestCallBack<String> callBack) {
        String url = NetworkConstance.SEND_COMMENT;
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("target_id", target_id);
        params.addQueryStringParameter("content", content);
        params.addQueryStringParameter("target_user_id", target_user_id);
        params.addQueryStringParameter("is_reply", is_reply);
        params.addQueryStringParameter("reply_id", reply_id);
        params.addQueryStringParameter("reply_user_id", reply_user_id);
        params.addQueryStringParameter("type", type);
        params.addQueryStringParameter("from_site", 4 + "");
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
        return httpHandler;
    }

    //评论
    //列表
    public static HttpHandler<String> commentsList(String page, String size, String target_id, String target_user_id, String type, RequestCallBack<String> callBack) {
        String url = NetworkConstance.COMMENTS_LIST;
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("page", page);
        params.addQueryStringParameter("size", size);
        params.addQueryStringParameter("sort", "1");
        params.addQueryStringParameter("target_id", target_id);
        params.addQueryStringParameter("target_user_id", target_user_id);
        params.addQueryStringParameter("type", type);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
        return httpHandler;
    }

    //评论
    //列表
    public static void mycommentsList(String page, String size, String target_id, String target_user_id, String type, RequestCallBack<String> callBack) {
        String url = NetworkConstance.MY_COMMENTS_LIST;
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("page", page);
        params.addQueryStringParameter("size", size);
        params.addQueryStringParameter("target_id", target_id);
        params.addQueryStringParameter("target_user_id", target_user_id);
        params.addQueryStringParameter("type", type);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
    }

    //评论
    //删除评论
    public static HttpHandler<String> deleteComment(String id, RequestCallBack<String> callBack) {
        String url = NetworkConstance.DELETE_COMMENT;
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("id", id);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
        return httpHandler;
    }

    //评论列表
    //商品详情评论列表
    public static HttpHandler<String> getGoodsDetailsCommentsList(String target_id, String page, RequestCallBack<String> callBack) {
        String url = NetworkConstance.COMMENTS_LIST;
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("page", page);
        params.addQueryStringParameter("target_id", target_id);
        params.addQueryStringParameter("type", "4");
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
        return httpHandler;
    }

    //分类
    //分类标签
    public static void categoryLabel(String tag_id, RequestCallBack<String> callBack) {
        String url = NetworkConstance.CATEGORY_LABEL;
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("tag_id", tag_id);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
    }

    //点击注册按钮
    public static void clickRegisterNet(RequestCallBack<String> callBack, String password, String phone, String code) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("mobile", phone);
        params.addQueryStringParameter("verify_code", code);
        params.addQueryStringParameter("password", password);
        params.addQueryStringParameter("from_to", "2");
        HttpHandler<String> httpHandler = MD5Utils.sign(params, NetworkConstance.AUTH_REGISTER, callBack, false);
    }

    //点击登录按钮
    public static void clickLoginNet(String phone, String password, RequestCallBack<String> callBack) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("mobile", phone);
        params.addQueryStringParameter("from_to", "2");     //登录渠道
        params.addQueryStringParameter("password", password);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, NetworkConstance.AUTH_LOGIN, callBack, false);
    }

    //第三方登录
    public static void thirdLoginNet(String oid, String access_token, String type, RequestCallBack<String> callBack) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("oid", oid);
        params.addQueryStringParameter("access_token", access_token);
        params.addQueryStringParameter("type", type);
        params.addQueryStringParameter("from_to", "2");
        HttpHandler<String> httpHandler = MD5Utils.sign(params, NetworkConstance.AUTH_THIRD_SIGN, callBack, false);
    }

    //注册及找回密码中的获取验证码
    public static void getVerifyCodeNet(RequestCallBack<String> callBack, String phone) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("mobile", phone);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, NetworkConstance.AUTH_VERIFY_CODE, callBack, false);
    }

    //第三方登录之快捷注册(绑定手机号)
    public static void bindPhoneNet(String oid, String union_id, String access_token, String account, String password, String type, RequestCallBack<String> callBack) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
////        params.addQueryStringParameter("uuid", uuid);
        params.addQueryStringParameter("account", account);
        params.addQueryStringParameter("password", password);
        params.addQueryStringParameter("oid", oid);
        params.addQueryStringParameter("union_id", union_id);
        params.addQueryStringParameter("access_token", access_token);
        params.addQueryStringParameter("third_source", type);
        params.addQueryStringParameter("from_to", "2"); //Android
        HttpHandler<String> httpHandler = MD5Utils.sign(params, NetworkConstance.AUTH_THIRD_REGISTER_WITH_PHONE, callBack, false);
    }

    //第三方登录之快捷注册(不绑定手机号)
    public static void skipBindNet(String oid, String union_id, String access_token, String nickname, String sex, String avatar_url, String type, RequestCallBack<String> callBack) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("nickname", nickname);
        params.addQueryStringParameter("sex", sex);
        params.addQueryStringParameter("oid", oid);
        params.addQueryStringParameter("union_id", union_id);
        params.addQueryStringParameter("avatar_url", avatar_url);
        params.addQueryStringParameter("access_token", access_token);
        params.addQueryStringParameter("third_source", type);
        params.addQueryStringParameter("from_to", "2");
        HttpHandler<String> httpHandler = MD5Utils.sign(params, NetworkConstance.AUTH_THIRD_REGISTER_WITHOUT_PHONE, callBack, false);
    }

    //stick
    public static void getQJData(LatLng ll, int radius, String page, String pageSize, String stick, RequestCallBack<String> callBack) {
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
//        HttpUtils httpUtils = new HttpUtils(NetworkConstance.CONN_TIMEOUT);
//        HttpHandler<String> handler = httpUtils.send(HttpRequest.HttpMethod.POST, NetworkConstance.QING_JING, params, callBack);
        MD5Utils.sign(params, NetworkConstance.QING_JING, callBack);
    }

    //订阅
    public static void getQJData(String page, String pageSize, String sort, String stick, RequestCallBack<String> callBack) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("page", page);
        params.addQueryStringParameter("size", pageSize);
        params.addQueryStringParameter("sort", sort);
        params.addQueryStringParameter("stick", stick);
        MD5Utils.sign(params, NetworkConstance.QING_JING, callBack);
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
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("page", page);
        params.addQueryStringParameter("size", pageSize);
        params.addQueryStringParameter("sort", "0");
        params.addQueryStringParameter("stick", "0");
        params.addQueryStringParameter("user_id", userId);
        MD5Utils.sign(params, NetworkConstance.QING_JING, callBack);
    }

    //找回忘记的密码
    public static void findPasswordNet(String phone, String newpassword, String code, RequestCallBack<String> callBack) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("mobile", phone);
        params.addQueryStringParameter("password", newpassword);
        params.addQueryStringParameter("verify_code", code);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, NetworkConstance.AUTH_FIND_PWD, callBack, false);
    }


    //账户处的用户个人信息
    public static void getMineInfo(String userId, RequestCallBack<String> callBack) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("user_id", userId);
        LogUtil.e("getMineInfo", userId);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, NetworkConstance.MINE_INFO, callBack, false);
    }

    //获取个人中心
    public static HttpHandler<String> getUserCenterData(RequestCallBack<String> callBack) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, NetworkConstance.USER_CENTER, callBack, false);
        return httpHandler;
    }

    /**
     * 获取Banner
     *
     * @param page_name
     * @param callBack
     */
    public static HttpHandler<String> getBanners(String page_name, RequestCallBack<String> callBack) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("page", String.valueOf(1));
        params.addQueryStringParameter("size", String.valueOf(5));
        params.addQueryStringParameter("name", page_name);
        return MD5Utils.sign(params, NetworkConstance.BANNERS_URL, callBack, false);
    }

    /**
     * 获取产品列表
     *
     * @param page
     * @param callBack
     */
    public static void getProductList(String page, RequestCallBack<String> callBack) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("page", String.valueOf(page));
        params.addQueryStringParameter("size", String.valueOf(10));
//        params.addBodyParameter("state",String.valueOf(1));
        MD5Utils.sign(params, NetworkConstance.PRODUCTS_URL, callBack, false);
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
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("page", page);
        params.addQueryStringParameter("size", size);
//        params.addBodyParameter("user_id", LoginInfo.getInstance().getId()+"");
        params.addQueryStringParameter("user_id", userId);//924808
//        LogUtil.e("userId",LoginInfo.getInstance().getId()+"");
        params.addQueryStringParameter("find_type", find_type);
        params.addQueryStringParameter("clean_remind", clean_remind);
        MD5Utils.sign(params, NetworkConstance.FOCUS_FAVORITE_URL, callBack, false);
    }

    /**
     * 关注操作
     *
     * @param follow_id
     * @param callBack
     */
    public static HttpHandler<String> focusOperate(String follow_id, RequestCallBack<String> callBack) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("follow_id", follow_id);
        return MD5Utils.sign(params, NetworkConstance.FOCUS_OPRATE_URL, callBack, false);
    }

    /**
     * 取消关注
     *
     * @param follow_id
     * @param callBack
     */
    public static HttpHandler<String> cancelFocusOperate(String follow_id, RequestCallBack<String> callBack) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("follow_id", follow_id);
        return MD5Utils.sign(params, NetworkConstance.CANCEL_FOCUS_URL, callBack, false);
    }

    /**
     * 意见反馈
     *
     * @param content
     * @param contact
     * @param callBack
     */
    public static void commitSuggestion(String content, String contact, RequestCallBack<String> callBack) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("content", content);
        params.addQueryStringParameter("contact", contact);
        params.addQueryStringParameter("from_to", "2"); //1.ios;2.android;3.ipad;4.win;5.web;6.wap
        params.addQueryStringParameter("kind", "3");   //1.网页版; 2.商城app; 3.Fiu
        MD5Utils.sign(params, NetworkConstance.SUGGESTION_URL, callBack, false);
    }

    /**
     * 更新用户信息
     *
     * @param key
     * @param value
     * @param callBack
     */
    public static void updateUserInfo(String key, String value, RequestCallBack<String> callBack) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        if (EditUserInfoActivity.isSubmitAddress) {
            params.addQueryStringParameter("province_id", key);
            params.addQueryStringParameter("district_id", value);
        } else {
            params.addQueryStringParameter(key, value);
        }
        MD5Utils.sign(params, NetworkConstance.UPDATE_USERINFO_URL, callBack, false);
    }

    /**
     * 更新用户信息
     *
     * @param nickname
     * @param sex
     * @param callBack
     */
    public static void updateNickNameSex(String nickname, String sex, RequestCallBack<String> callBack) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("nickname", nickname);
        params.addQueryStringParameter("sex", sex);
        MD5Utils.sign(params, NetworkConstance.UPDATE_USERINFO_URL, callBack, false);
    }

    /**
     * 更新用户信息
     *
     * @param age_group
     * @param assets
     * @param callBack
     */
    public static void updateAgeAssets(String age_group, String assets, RequestCallBack<String> callBack) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("age_group", age_group);
        params.addQueryStringParameter("assets", assets);
        MD5Utils.sign(params, NetworkConstance.UPDATE_USERINFO_URL, callBack, false);
    }

    /**
     * 提交订阅的主题
     *
     * @param interest_scene_cate
     * @param callBack
     */
    public static void subscribeTheme(String interest_scene_cate, RequestCallBack<String> callBack) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("interest_scene_cate", interest_scene_cate);
        MD5Utils.sign(params, NetworkConstance.UPDATE_USERINFO_URL, callBack, false);
    }

    /**
     * 更新签名和label
     *
     * @param label
     * @param summary
     * @param callBack
     */
    public static void updateSignatrueLabel(String label, String summary, RequestCallBack<String> callBack) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("label", label);
        params.addQueryStringParameter("summary", summary);
        MD5Utils.sign(params, NetworkConstance.UPDATE_USERINFO_URL, callBack, false);
    }

    /**
     * 获取所有城市
     *
     * @param callBack
     */
    public static void getAllCities(RequestCallBack<String> callBack) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        MD5Utils.sign(params, NetworkConstance.ALL_CITY_URL, callBack, false);
    }

    /**
     * 上传头像
     *
     * @param callBack
     */
    public static void uploadImg(String tmp, String type, RequestCallBack<String> callBack) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("tmp", tmp);
        params.addQueryStringParameter("type", type);
        MD5Utils.sign(params, NetworkConstance.UPLOAD_IMG_URL, callBack, false);
    }

    /**
     * 上传个人中心背景
     *
     * @param callBack
     */
    public static HttpHandler<String> uploadBgImg(String tmp, RequestCallBack<String> callBack) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("tmp", tmp);
        HttpHandler<String> handler = MD5Utils.sign(params, NetworkConstance.UPLOAD_BG_URL, callBack, false);
        NetworkManager.getInstance().add(NetworkConstance.UPLOAD_BG_URL, handler);
        return handler;
    }

    /**
     * 获取系统通知
     *
     * @param page
     * @param pageSize
     * @param callBack
     */
    public static void getSystemNotice(String page, String pageSize, RequestCallBack<String> callBack) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("page", page);
        params.addQueryStringParameter("size", pageSize);
        MD5Utils.sign(params, NetworkConstance.SYSTEM_NOTICE, callBack, false);
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
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("page", page);
        params.addQueryStringParameter("size", pageSize);
        params.addQueryStringParameter("type", type);
        MD5Utils.sign(params, NetworkConstance.MESSAGE_RECORD, callBack, false);
    }

    /**
     * 发送消息
     *
     * @param to_user_id
     * @param content
     * @param callBack
     */
    public static void sendMessage(String to_user_id, String content, RequestCallBack<String> callBack) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("to_user_id", to_user_id);
        params.addQueryStringParameter("content", content);
        MD5Utils.sign(params, NetworkConstance.SEND_MESSAGE, callBack, false);
    }

    /**
     * 私信详情
     *
     * @param to_user_id
     * @param callBack
     */
    public static void messageDetailList(String to_user_id, RequestCallBack<String> callBack) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("to_user_id", to_user_id);
        MD5Utils.sign(params, NetworkConstance.MESSAGE_DETAIL, callBack, false);
    }

    //验证红包是否可用
    public static void checkRedBagUsableNet(String rid, String code, RequestCallBack<String> callBack) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
//        params.addQueryStringParameter("uuid", uuid);
        params.addQueryStringParameter("rid", rid);
        params.addQueryStringParameter("code", code);
        MD5Utils.sign(params, NetworkConstance.SHOPPING_USE_BONUS, callBack, false);
    }


    //红包
    public static void myRedBagNet(String page, String size, String used, String time, RequestCallBack<String> callBack) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("page", page);
        params.addQueryStringParameter("size", size);
        params.addQueryStringParameter("used", used);
        params.addQueryStringParameter("is_expired", time);
        MD5Utils.sign(params, NetworkConstance.MY_BONUS, callBack, false);
    }

    /**
     * 修改密码
     *
     * @param password
     * @param new_password
     * @param callBack
     */
    public static void updatePassword(String password, String new_password, RequestCallBack<String> callBack) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("password", password);
        params.addQueryStringParameter("new_password", new_password);
        MD5Utils.sign(params, NetworkConstance.MY_MODIFY_PASSWORD, callBack, false);
    }

    //立即下单
    public static HttpHandler<String> nowConfirmOrder(String rrid, String addbook_id, String is_nowbuy, String summary, String transfer_time, String bonus_code, RequestCallBack<String> callBack) {
        String url = NetworkConstance.URLSTRING_NOW_CONFIRMORDER;
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("rrid", rrid);
        params.addQueryStringParameter("from_site", "8");
        params.addQueryStringParameter("addbook_id", addbook_id);
        params.addQueryStringParameter("is_nowbuy", is_nowbuy);
        params.addQueryStringParameter("summary", summary);
        params.addQueryStringParameter("transfer_time", transfer_time);
        params.addQueryStringParameter("bonus_code", bonus_code);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
//        NetworkManager.getInstance().add("nowConfirmOrder", httpHandler);
        return httpHandler;
    }

    //购物车
    public static HttpHandler<String> shopCartNet(RequestCallBack<String> callBack) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, NetworkConstance.SHOPPING_FETCH_CART, callBack);
        return httpHandler;
    }

    //购物车数量
    public static HttpHandler<String> shopCartNumberNet(RequestCallBack<String> callBack) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, NetworkConstance.SHOPPING_FETCH_CART_COUNT, callBack);
        return httpHandler;
    }

    //购物车结算下单
    public static void calculateShopCartNet(String array, RequestCallBack<String> callBack) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("array", array);
        params.addQueryStringParameter("referral_code", SPUtil.read("referral_code" ));
        HttpHandler<String> httpHandler = MD5Utils.sign(params, NetworkConstance.SHOPING_CHECKOUT, callBack);
    }

    //删除购物车
    public static void deletShopCartNet(String array, RequestCallBack<String> callBack) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("array", array);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, NetworkConstance.SHOPPING_REMOVE_CART, callBack);
    }

    //取消点赞
    public static HttpHandler<String> cancelLoveNet(String id, String type, RequestCallBack<String> callBack) {
        String url = NetworkConstance.URLSTRING_CANCELLOVE;
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("id", id);
        params.addQueryStringParameter("type", type);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
        return httpHandler;
    }

    //点赞
    public static HttpHandler<String> loveNet(String id, String type, RequestCallBack<String> callBack) {
        String url = NetworkConstance.URLSTRING_LOVE;
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("id", id);
        params.addQueryStringParameter("type", type);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
        return httpHandler;
    }

    //添加购物车
    public static HttpHandler<String> addToCartNet(String target_id, String type, String n, RequestCallBack<String> callBack) {
        String url = NetworkConstance.URLSTRING_ADD_TO_CART;
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("target_id", target_id);
        params.addQueryStringParameter("type", type);
        params.addQueryStringParameter("n", n);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
        return httpHandler;
    }

    //立即购买(验证数据并会生成临时订单)
    public static HttpHandler<String> buyNow(String target_id, String type, String n, RequestCallBack<String> callBack) {
        String url = NetworkConstance.URLSTRING_BUY_NOW;
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("target_id", target_id);
        params.addQueryStringParameter("type", type);
        params.addQueryStringParameter("n", n);
        params.addQueryStringParameter("referral_code", SPUtil.read("referral_code" ));
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
        return httpHandler;
    }

    //删除订单/my/delete_order
    public static void deleteOrderNet(String rid, RequestCallBack<String> callBack) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("rid", rid);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, NetworkConstance.MY_DELETE_ORDER, callBack);
    }

    //取消订单
    public static void cancelOrderNet(String rid, RequestCallBack<String> callBack) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("rid", rid);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, NetworkConstance.MY_CANCEL_ORDER, callBack);
    }

    //订单支付详情和订单详情都是这,发表评价界面的产品图片也从这获取
    public static HttpHandler<String> OrderPayNet(String rid, RequestCallBack<String> callBack) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("rid", rid);
        return MD5Utils.sign(params, NetworkConstance.SHOPPING_DETAILS, callBack);
    }

    //申请退款
    public static HttpHandler<String> applyForRefundNet(String rid, String option, String content, RequestCallBack<String> callBack) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("rid", rid);
        params.addQueryStringParameter("option", option);
        params.addQueryStringParameter("content", content);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, NetworkConstance.SHOPPING_APPLY_REFUND, callBack);
        return httpHandler;
    }

    //确认收货
    public static void confirmReceiveNet(String rid, RequestCallBack<String> callBack) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("rid", rid);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, NetworkConstance.SHOPPING_TAKE_DELIVERY, callBack);
    }

    //发表评价
    public static void publishEvaluateNet(String rid, String array, RequestCallBack<String> callBack) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("rid", rid);
        params.addQueryStringParameter("array", array);
        params.addQueryStringParameter("from_site", "4");
        HttpHandler<String> httpHandler = MD5Utils.sign(params, NetworkConstance.PRODUCT_AJAX_COMMENT, callBack);
    }

    //账户处的订单列表
    public static void orderListNet(String status, String page, String size, RequestCallBack<String> callBack) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("status", status);
        params.addQueryStringParameter("page", page);
        params.addQueryStringParameter("size", size);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, NetworkConstance.SHOPPING_ORDERS, callBack);
    }

    //账户处的用户个人信息
    public static void userInfoNet(RequestCallBack<String> callBack) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, NetworkConstance.AUTH_USER, callBack);
    }

    //购物车中单个商品的库存（即最大加减数）
    public static void shopcartInventoryNet(RequestCallBack<String> callBack) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, NetworkConstance.SHOPPING_FETCH_CART_PRODUCT_COUUNT, callBack);
    }

    //购物车中商品加减数量
    public static void shopcartAddSubtractNet(String array, RequestCallBack<String> callBack) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("array", array);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, NetworkConstance.SHOPPINGN_EDIT_CART, callBack);
    }

    //最fiu伙伴
    public static HttpHandler<String> fiuUserList(String page, String size, String sort, RequestCallBack<String> callBack) {
        String url = NetworkConstance.FIU_USER_LIST;
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("page", page);
        params.addQueryStringParameter("size", size);
        params.addQueryStringParameter("sort", sort);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
        return httpHandler;
    }

    //首页订阅的情景下的场景和关注的人所创建的场景列表
    public static void subsCJList(String page, String size, String sort, RequestCallBack<String> callBack) {
        String url = NetworkConstance.SUBS_CJLIST;
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("page", page);
        params.addQueryStringParameter("size", size);
        params.addQueryStringParameter("sort", sort);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
    }

    /**
     * 更新用户是否首次登录的标识
     *
     * @param type
     * @param callBack
     */
    public static void updateUserIdentify(String type, RequestCallBack<String> callBack) {
        String url = NetworkConstance.UPDATE_USER_IDENTIFY;
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("type", type);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
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
        String url = NetworkConstance.NOTICE_LIST;
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("page", page);
        params.addQueryStringParameter("size", size);
        params.addQueryStringParameter("type", type);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
    }


    /**
     * 发现好友列表
     *
     * @param callBack
     */
    public static void findFriends(String page, String size, String sight_count, String sort, RequestCallBack<String> callBack) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("page", page);
        params.addQueryStringParameter("size", size);
        params.addQueryStringParameter("type", "1"); //过滤已关注
        params.addQueryStringParameter("sight_count", sight_count); //场景数量
        params.addQueryStringParameter("sort", sort); //0是最新 1是随机
        HttpHandler<String> httpHandler = MD5Utils.sign(params, NetworkConstance.FIND_FRIENDS, callBack);
    }

    /**
     * 退出登录
     *
     * @param callBack
     */
    public static void logout(RequestCallBack<String> callBack) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("from_to", "2"); // 1.ios;2.android;3.win;4.ipad;
        HttpHandler<String> httpHandler = MD5Utils.sign(params, NetworkConstance.LOGOUT, callBack);
    }

    /**
     * 获取手机状态
     *
     * @param account
     * @param callBack
     */
    public static void getPhoneState(String account, RequestCallBack<String> callBack) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("account", account);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, NetworkConstance.GET_REGIST_STATE, callBack);
    }

    /**
     * 获取支付参数
     *
     * @param rid
     * @param payaway
     * @param callBack
     */
    public static void getPayParams(String rid, String payaway, RequestCallBack<String> callBack) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("rid", rid);
        params.addQueryStringParameter("payaway", payaway);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, NetworkConstance.PAY_URL, callBack, true);
    }

    /**
     * 判断服务器是否登录
     */
    public static void getLoginStatus(RequestCallBack<String> callBack) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, NetworkConstance.CHECK_LOGIN_URL, callBack);
    }


    public static void uploadIdentityInfo(String id, String info, String label, String contact, String id_card_a_tmp, String business_card_tmp, RequestCallBack<String> callBack) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("id", id);
        params.addQueryStringParameter("info", info);
        params.addQueryStringParameter("label", label);
        params.addQueryStringParameter("contact", contact);
        params.addQueryStringParameter("id_card_a_tmp", id_card_a_tmp);
        params.addQueryStringParameter("business_card_tmp", business_card_tmp);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, NetworkConstance.UPLOAD_IDENTIFY_URL, callBack);
        NetworkManager.getInstance().add(NetworkConstance.UPLOAD_IDENTIFY_URL, httpHandler);
    }

    public static void tixingFahuo(String rid, RequestCallBack<String> callBack) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("rid", rid);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, NetworkConstance.SHOPPING_ALERT_SEND_GOODS, callBack);
    }

    //分享场景语境次数
    public static void commitShareCJ(String id, RequestCallBack<String> callBack) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("id", id);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, NetworkConstance.SCENE_SIGHT_ADD_SHARE_CONTEXT_NUM, callBack);
    }

    /**
     * 获得认证信息
     *
     * @param callBack
     */
    public static void getAuthStatus(RequestCallBack<String> callBack) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, NetworkConstance.MY_FETCH_TALENT, callBack);
    }

    //送积分
    public static HttpHandler<String> getBonus(String type, String evt, String target_id, RequestCallBack<String> callBack) {
        String url = NetworkConstance.GET_BONUS;
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("type", type);
        params.addQueryStringParameter("evt", evt);
        params.addQueryStringParameter("target_id", target_id);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
        return httpHandler;
    }

    /**
     * 获取专题信息
     *
     * @param id
     * @param callBack
     */
    public static void getSubjectData(String id, RequestCallBack<String> callBack) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("id", id);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, NetworkConstance.SCENE_SUBJECT_VIEW, callBack);
    }


    //收藏产品和取消收藏
    public static void favorite(String id, String type, RequestCallBack<String> callBack) {
        String url = NetworkConstance.FAVORITE_PRODUCT;
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("id", id);
        params.addQueryStringParameter("type", type);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
    }

    public static void cancelFavorite(String id, String type, RequestCallBack<String> callBack) {
        String url = NetworkConstance.CANCEL_FAVORITE_PRODUCT;
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("id", id);
        params.addQueryStringParameter("type", type);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
    }


    public static void isInvited(RequestCallBack<String> callBack) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, NetworkConstance.GATEWAY_IS_INVITED, callBack);
    }


    public static void submitInviteCode(String code, RequestCallBack<String> callBack) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("code", code);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, NetworkConstance.GATEWAY_VALIDE_INVITE_CODE, callBack);
    }

    public static void updateInviteCodeStatus(String code, RequestCallBack<String> callBack) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("code", code);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, NetworkConstance.GATEWAY_DEL_INVITE_CODE, callBack);
    }

    /**
     * @param page
     * @param size
     * @param type
     * @param event
     * @param callBack
     */
    public static void getCollectOrdered(String page, String size, String type, String event, RequestCallBack<String> callBack) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("page", page);
        params.addQueryStringParameter("size", size);
        params.addQueryStringParameter("user_id", String.valueOf(LoginInfo.getUserId()));
        params.addQueryStringParameter("type", type);
        params.addQueryStringParameter("event", event);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, NetworkConstance.FAVORITE_GET_NEW_LIST, callBack);
    }

    //创建情景时，根据标题和描述搜索标签
    public static void searchLabel(String title, String content, RequestCallBack<String> callBack) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("title", title);
        params.addQueryStringParameter("size", 10 + "");
        params.addQueryStringParameter("content", content);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, NetworkConstance.GATEWAY_FETCH_CHINESE_WORD, callBack);
    }

    //语境列表
    public static HttpHandler<String> envirList(String page, String size, String sort, String category_id, String stick, RequestCallBack<String> callBack) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("page", page);
        params.addQueryStringParameter("size", size);
        params.addQueryStringParameter("sort", sort);
        params.addQueryStringParameter("category_id", category_id);
        params.addQueryStringParameter("stick", stick);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, NetworkConstance.SCENE_CONTEXT_GETLIST, callBack);
        return httpHandler;
    }

    //用户激活状态
    public static void activeStatus(RequestCallBack<String> callBack) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, NetworkConstance.GATEWAY_RECORD_FIU_USER_ACTIVE, callBack);
    }

    //验证验证码
    public static void submitCheckCode(String phone, String code, RequestCallBack<String> callBack) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("phone", phone);
        params.addQueryStringParameter("code", code);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, NetworkConstance.AUTH_CHECK_VERIFY_CODE, callBack);
    }

    //注册用户
    public static void registerUser(String mobile, String password, String verify_code, RequestCallBack<String> callBack) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("mobile", mobile);
        params.addQueryStringParameter("password", password);
        params.addQueryStringParameter("verify_code", verify_code);
        params.addQueryStringParameter("from_to", "2"); //1.ios;2.android;3.win;
        HttpHandler<String> httpHandler = MD5Utils.sign(params, NetworkConstance.AUTH_REGISTER, callBack);
    }

    //完善资料--->关注感兴趣的人
    public static void focusInterestUser(RequestCallBack<String> callBack) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("page", "1");
        params.addQueryStringParameter("size", "18");
        params.addQueryStringParameter("type", "1");
        params.addQueryStringParameter("edit_stick", "1");
        HttpHandler<String> httpHandler = MD5Utils.sign(params, NetworkConstance.USER_FIND_USER, callBack);
    }

    //开始关注感兴趣的人
    public static void focusUsers(String follow_ids, RequestCallBack<String> callBack) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("follow_ids", follow_ids);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, NetworkConstance.FOLLOW_BATCH_FOLLOW, callBack);
    }

    //获取推荐活动标签
    public static HttpHandler<String> activeTags(RequestCallBack<String> callBack) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, NetworkConstance.SCENE_SIGHT_STICK_ACTIVE_TAGS, callBack);
        return httpHandler;
    }

    //添加品牌
    public static HttpHandler<String> addBrand(String title, RequestCallBack<String> callBack) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("title", title);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, NetworkConstance.SCENE_BRANDS_SUBMIT, callBack);
        return httpHandler;
    }

    //搜索建议
    public static HttpHandler<String> searchExpand(String q, String size, RequestCallBack<String> callBack) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("q", q);
        params.addQueryStringParameter("size", size);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, NetworkConstance.SEARCH_EXPANDED, callBack);
        return httpHandler;
    }

    /**
     * 取消订阅情景主题
     *
     * @param id
     */
    public static HttpHandler<String> cancelSubscribe(String id, RequestCallBack<String> callBack) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("id", id);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, NetworkConstance.MY_REMOVE_INTEREST_SCENE_ID, callBack);
        return httpHandler;
    }

    public static HttpHandler<String> subscribe(String id, RequestCallBack<String> callBack) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("id", id);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, NetworkConstance.MY_ADD_INTEREST_SCENE_ID, callBack);
        return httpHandler;
    }

    //记录情景浏览次数
    public static void viewCount(String qjId) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("id", qjId);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, NetworkConstance.SCENE_SIGHT_RECORD_VIEW, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {

            }

            @Override
            public void onFailure(HttpException error, String msg) {

            }
        });
    }

    //首页精选主题
    public static HttpHandler<String> subjectList(String page, String size, String stick, String fine, String type, String sort, RequestCallBack<String> callBack) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("page", page);
        params.addQueryStringParameter("size", size);
        params.addQueryStringParameter("stick", stick);
        params.addQueryStringParameter("fine", fine);
        params.addQueryStringParameter("type", type);
        params.addQueryStringParameter("sort", sort);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, NetworkConstance.SCENE_SUBJECT_GETLIST, callBack);
        return httpHandler;
    }

    //最新好货推荐
    public static HttpHandler<String> firstProducts(RequestCallBack<String> callBack) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, NetworkConstance.PRODUCCT_INDEX_NEW, callBack);
        return httpHandler;
    }

    /**
     * @param page
     * @param pageType
     * @param sort
     * @param callBack
     */
    public static void getChoosenSubject(String page, String pageType, String fine, String sort, RequestCallBack<String> callBack) {
//        String url = NetworkConstance.BASE_URL + "/scene_subject/getlist";
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("page", page);
        params.addQueryStringParameter("size", Constants.PAGE_SIZE);//
        params.addQueryStringParameter("fine", fine);
        params.addQueryStringParameter("type", pageType);
        params.addQueryStringParameter("sort", sort);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, NetworkConstance.CHOOSEN_SUBJECT_URL, callBack);
        NetworkManager.getInstance().add(MD5Utils.getMD5(NetworkConstance.CHOOSEN_SUBJECT_URL),httpHandler);
    }

    //收藏情景
    public static HttpHandler<String> shoucang(String id, String type, RequestCallBack<String> callBack) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("id", id);
        params.addQueryStringParameter("type", type);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, NetworkConstance.FAVORITE_AJAX_FAVORITE, callBack);
        return httpHandler;
    }

    //取消收藏
    public static HttpHandler<String> cancelShoucang(String id, String type, RequestCallBack<String> callBack) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("id", id);
        params.addQueryStringParameter("type", type);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, NetworkConstance.FAVORITE_AJAX_CANCEL_FAVORITE, callBack);
        return httpHandler;
    }

    //首页用户列表
    public static HttpHandler<String> getUserList(int size, RequestCallBack<String> callBack) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("page", "1");
        params.addQueryStringParameter("type", "1");
        params.addQueryStringParameter("size", size + "");
        params.addQueryStringParameter("edit_stick", "1");
        params.addQueryStringParameter("sort", "1");
        HttpHandler<String> httpHandler = MD5Utils.sign(params, NetworkConstance.USER_FIND_USER, callBack);
        return httpHandler;
    }

    //临时产品库
    public static void getTempGoods(String id, RequestCallBack<String> callBack) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("id", id);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, NetworkConstance.USER_TEMP_VIEW, callBack);
    }

    /**
     * 更新分享数量
     *
     * @param id
     * @param callBack
     */
    public static void updateShareCount(String id, RequestCallBack<String> callBack) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("id", id);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, NetworkConstance.SCENE_SUBJECT_RECORD_SHARE_COUNT, callBack);
    }

    //当前用户是否是管理员
    public static HttpHandler<String> isEditor(RequestCallBack<String> callBack) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, NetworkConstance.USER_IS_EDITOR, callBack);
        return httpHandler;
    }

    //管理员添加精选或取消精选
    public static HttpHandler<String> setFine(String id, String evt, RequestCallBack<String> callBack) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("id", id);
        params.addQueryStringParameter("evt", evt);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, NetworkConstance.USER_DO_FINE, callBack);
        return httpHandler;
    }

    //管理员添加推荐或取消推荐
    public static HttpHandler<String> setStick(String id, String evt, RequestCallBack<String> callBack) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("id", id);
        params.addQueryStringParameter("evt", evt);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, NetworkConstance.USER_DO_STICK, callBack);
        return httpHandler;
    }

    //管理员添加屏蔽或取消屏蔽
    public static HttpHandler<String> setCheck(String id, String evt, RequestCallBack<String> callBack) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("id", id);
        params.addQueryStringParameter("evt", evt);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, NetworkConstance.USER_DO_CHECK, callBack);
        return httpHandler;
    }

    /**
     * 请求地址
     * @param oid
     * @param pid
     * @param layer
     * @param callBack
     */
    public static void requestAddress(String oid, String pid,String layer,RequestCallBack<String> callBack) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("oid",oid);
        params.addQueryStringParameter("pid",pid);
        params.addQueryStringParameter("layer",layer);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, NetworkConstance.SHOPPING_FETCH_CHINA_CITY, callBack);
    }

    /**
     * @param callBack
     */
    public static HttpHandler<String> getIndexChosenSubject(RequestCallBack<String> callBack) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        return MD5Utils.sign(params, NetworkConstance.SCENE_SUBJECT_INDEX_SUJECT_STICK, callBack);
    }

    /**
     * 退款单列表
     *
     * @param callBack
     */
    public static HttpHandler<String> getRefundList(String page, String size, RequestCallBack<String> callBack) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("page", page);
        params.addQueryStringParameter("size", size);
        return MD5Utils.sign(params, NetworkConstance.SHOPPING_REFUND_LIST, callBack);
    }

    /**
     *  获取退款信息
     *  *
     * @param callBack
     */
    public static HttpHandler<String> getChargeBackInfo(String rId, String skuId, RequestCallBack<String> callBack) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("rid", rId);
        params.addQueryStringParameter("sku_id", skuId);
        return MD5Utils.sign(params, NetworkConstance.SHOPPING_CHECK_REFUND, callBack);
    }

    /**
     * 申请退款
     *
     * @param callBack
     */
    public static HttpHandler<String> getApplyProductRefund(String rid, String sku_id, String refund_type,
                                                            String refund_reason, String refund_content, String refund_price,
                                                            RequestCallBack<String> callBack) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("rid", rid);
        params.addQueryStringParameter("sku_id", sku_id);
        params.addQueryStringParameter("refund_type", refund_type);
        params.addQueryStringParameter("refund_reason", refund_reason);
        params.addQueryStringParameter("refund_content", refund_content);
        params.addQueryStringParameter("refund_price", refund_price);
        return MD5Utils.sign(params, NetworkConstance.SHOPPING_APPLY_PRODUCT_REFUND, callBack);
    }

    /**
     * 退款成功详情
     *
     * @param callBack
     */
    public static HttpHandler<String> getRefundSuccessInfo(String chargebackId,RequestCallBack<String> callBack) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("id", chargebackId);
        return MD5Utils.sign(params, NetworkConstance.SHOPPING_REFUND_VIEW, callBack);
    }

    /**
     * 获取版本信息
     *
     * @param callBack
     */
    public static HttpHandler<String> updateToLatestVersion(RequestCallBack<String> callBack) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("from_to", "2");
        return MD5Utils.sign(params, NetworkConstance.FETCH_LATEST_VERSION, callBack);
    }

    /**
     * 检查版本更新
     *
     * @param callBack
     */
    public static HttpHandler<String> checkVersionInfo(String versionName,RequestCallBack<String> callBack) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("from_to", "2");
        params.addQueryStringParameter("version", versionName);
        return MD5Utils.sign(params, NetworkConstance.CHECK_VERSION_INFO, callBack);
    }

    /**
     * 获取邮费接口
     *
     * @param callBack
     */
    public static HttpHandler<String> fetchFreight (String addbook_id, String rid, RequestCallBack<String> callBack) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("addbook_id", addbook_id);
        params.addQueryStringParameter("rid", rid);
        return MD5Utils.sign(params, NetworkConstance.SHOPPING_FETCH_FREIGHT, callBack);
    }

    /**
     * 获取邮费接口
     *
     * @param callBack
     */
    public static HttpHandler<String> shoppingTracking (String rid, String express_no, String express_caty, RequestCallBack<String> callBack) {
        RequestParams params = new RequestParams(ConstantCfg.CHARSET);
        params.addQueryStringParameter("rid", rid);
        params.addQueryStringParameter("express_no", express_no);
        params.addQueryStringParameter("express_caty", express_caty);
        return MD5Utils.sign(params, NetworkConstance.SHOPPING_TRACKING, callBack);
    }
}
